/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import lombok.NonNull;
import java.io.Serial;
import java.util.List;

public class RemoveFunctionIterator extends HybridRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> sequenceIterator;
    private final RuntimePlan<Item> positionIterator;
    private int removePosition; // position to remove the item


    public RemoveFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.positionIterator = this.getChild(1);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(this.sequenceIterator, this.positionIterator, context, getMetadata());
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        init(context);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);

        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
        JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((item) -> item._2() != this.removePosition - 1);
        return filteredRDD.map((item) -> item._1);
    }



    private void init(DynamicContext context) {
        Item positionItem = this.positionIterator.materializeFirstOrNull(context);
        this.removePosition = positionItem.getIntValue();
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final RuntimePlan<Item> positionPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private int removePosition;
        private int currentPosition;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> sequencePlan,
                @NonNull RuntimePlan<Item> positionPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
            super(metadata);
            this.sequencePlan = sequencePlan;
            this.positionPlan = positionPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            Item positionItem = this.positionPlan.materializeFirstOrNull(this.context);
            this.removePosition = positionItem.getIntValue();
            this.currentPosition = 1;
            this.sequenceCursor = this.sequencePlan.getCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            if (this.currentPosition == this.removePosition && this.sequenceCursor.hasNext()) {
                this.sequenceCursor.next();
                this.currentPosition++;
            }
            return this.sequenceCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw exhausted();
            }
            this.currentPosition++;
            return this.sequenceCursor.next();
        }

        @Override
        protected void closeLocal() {
            if (this.sequenceCursor != null) {
                this.sequenceCursor.close();
                this.sequenceCursor = null;
            }
        }

        private RuntimeException exhausted() {
            return new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "remove function", this.metadata);
        }

    }
}
