/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.sequences.general;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

public class InsertBeforeFunctionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan sequenceIterator;
    private final ItemRuntimePlan positionIterator;
    private final ItemRuntimePlan insertIterator;

    public InsertBeforeFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.positionIterator = this.getChild(1);
        this.insertIterator = this.getChild(2);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(
                this.sequenceIterator, this.positionIterator, this.insertIterator, context, getMetadata());
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        long position = this.positionIterator.materializeFirstOrNull(context).getLongValue();
        long insertIndex = position <= 1 ? 0 : position - 1;
        JavaPairRDD<Item, Long> zippedRDD =
                this.sequenceIterator.getRDD(context).zipWithIndex();
        JavaRDD<Item> beforeRDD =
                zippedRDD.filter(item -> item._2() < insertIndex).map(item -> item._1());
        JavaRDD<Item> afterRDD =
                zippedRDD.filter(item -> item._2() >= insertIndex).map(item -> item._1());
        return beforeRDD.union(this.insertIterator.getRDD(context)).union(afterRDD);
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan sequencePlan;
        private final ItemRuntimePlan positionPlan;
        private final ItemRuntimePlan insertPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private Cursor<Item> insertCursor;
        private long insertPosition;
        private long currentPosition;
        private boolean insertionCompleted;

        private EvaluationCursor(
                @NonNull ItemRuntimePlan sequencePlan,
                @NonNull ItemRuntimePlan positionPlan,
                @NonNull ItemRuntimePlan insertPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.positionPlan = positionPlan;
            this.insertPlan = insertPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            Item positionItem = this.positionPlan.materializeFirstOrNull(this.context);
            this.insertPosition = positionItem.getLongValue();
            this.currentPosition = 1;
            this.insertionCompleted = false;

            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
            this.insertCursor = this.insertPlan.getCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            if (!this.insertionCompleted && this.insertPosition <= this.currentPosition) {
                if (this.insertCursor.hasNext()) {
                    return true;
                }
                this.insertionCompleted = true;
            }

            if (this.sequenceCursor.hasNext()) {
                return true;
            }

            if (!this.insertionCompleted && this.insertCursor.hasNext()) {
                return true;
            }
            this.insertionCompleted = true;
            return false;
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw exhausted();
            }
            if (!this.insertionCompleted && this.insertPosition <= this.currentPosition) {
                return this.insertCursor.next();
            }
            if (this.sequenceCursor.hasNext()) {
                this.currentPosition++;
                return this.sequenceCursor.next();
            }
            return this.insertCursor.next();
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
            if (this.insertCursor != null) {
                this.insertCursor.close();
                this.insertCursor = null;
            }
        }

        private RuntimeException exhausted() {
            return new IteratorFlowException(
                    IteratorFlowException.FLOW_EXCEPTION_MESSAGE + "insert-before function", this.metadata);
        }
    }
}
