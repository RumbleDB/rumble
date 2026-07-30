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
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;
import scala.Tuple2;

import lombok.NonNull;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InsertBeforeFunctionIterator extends HybridRuntimeIterator {


    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> sequenceIterator;
    private final RuntimePlan<Item> positionIterator;
    private final RuntimePlan<Item> insertIterator;
    private int insertPosition; // position to start inserting

    public InsertBeforeFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.getChild(0);
        this.positionIterator = this.getChild(1);
        this.insertIterator = this.getChild(2);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EvaluationCursor(
                this.sequenceIterator,
                this.positionIterator,
                this.insertIterator,
                context,
                getMetadata()
        );
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        init(context);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();

        if (
            this.insertIterator.getRuntimeStaticContext()
                .getExecutionMode()
                .isRDDOrDataFrame()
        ) {
            JavaRDD<Item> insertsRDD = this.insertIterator.getRDD(context);
            JavaRDD<Item> beforeRDD = zippedRDD
                .filter((item) -> item._2() < this.insertPosition - 1)
                .map((item) -> item._1);
            JavaRDD<Item> afterRDD = zippedRDD
                .filter((item) -> item._2() >= this.insertPosition - 1)
                .map((item) -> item._1);
            return beforeRDD.union(insertsRDD).union(afterRDD);
        }

        List<Item> inserts = this.insertIterator.materialize(context);
        int numPartitions = zippedRDD.partitions().size();
        int indexOfInsertion = this.insertPosition;

        return zippedRDD.mapPartitionsWithIndex((partitionIndex, iterator) -> {
            List<Item> list = new ArrayList<>();
            int lastIndex = -1;
            if (partitionIndex == 0 && indexOfInsertion - 1 < 0) {
                list.addAll(inserts);
            }
            Tuple2<Item, Long> element;
            while (iterator.hasNext()) {
                element = iterator.next();
                if (element._2() == indexOfInsertion - 1) {
                    list.addAll(inserts);
                }
                list.add(element._1());
                lastIndex = element._2().intValue();
            }
            if (partitionIndex == numPartitions - 1 && indexOfInsertion - 1 > lastIndex) {
                list.addAll(inserts);
            }
            return list.iterator();
        }, false);
    }



    private void init(DynamicContext context) {
        Item positionItem = this.positionIterator.materializeFirstOrNull(context);
        this.insertPosition = positionItem.getIntValue();
    }

    private static final class EvaluationCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> sequencePlan;
        private final RuntimePlan<Item> positionPlan;
        private final RuntimePlan<Item> insertPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> sequenceCursor;
        private Cursor<Item> insertCursor;
        private int insertPosition;
        private int currentPosition;
        private boolean insertionCompleted;

        private EvaluationCursor(
                @NonNull RuntimePlan<Item> sequencePlan,
                @NonNull RuntimePlan<Item> positionPlan,
                @NonNull RuntimePlan<Item> insertPlan,
                @NonNull DynamicContext context,
                @NonNull ExceptionMetadata metadata
        ) {
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
            this.insertPosition = positionItem.getIntValue();
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
            return new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", this.metadata);
        }

    }
}
