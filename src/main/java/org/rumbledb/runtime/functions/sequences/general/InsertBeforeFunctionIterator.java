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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class InsertBeforeFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator positionIterator;
    private RuntimeIterator insertIterator;
    private Item nextResult;
    private int insertPosition; // position to start inserting
    private int currentPosition; // current position
    private boolean insertingNow; // check if currently iterating over insertIterator
    private boolean insertingCompleted;

    public InsertBeforeFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
        this.sequenceIterator = this.children.get(0);
        this.positionIterator = this.children.get(1);
        this.insertIterator = this.children.get(2);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        init(context);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();

        if (this.insertIterator.isRDDOrDataFrame()) {
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

    @Override
    protected void openLocal() {
        init(this.currentDynamicContextForLocalExecution);
        this.currentPosition = 1; // initialize index as the first item
        this.insertingNow = false;
        this.insertingCompleted = false;

        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        this.insertIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
        this.insertIterator.close();
    }

    @Override
    protected void resetLocal() {
        this.currentPosition = 1; // initialize index as the first item
        this.insertingNow = false;
        this.insertingCompleted = false;

        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        this.insertIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", getMetadata());
    }

    private void init(DynamicContext context) {
        Item positionItem = this.positionIterator.materializeFirstItemOrNull(context);
        this.insertPosition = positionItem.getIntValue();
    }

    public void setNextResult() {
        this.nextResult = null;

        // don't check for insertion triggers once insertion is completed
        if (!this.insertingCompleted) {
            if (!this.insertingNow) {
                if (this.insertPosition <= this.currentPosition) { // start inserting if condition is met
                    if (this.insertIterator.hasNext()) {
                        this.insertingNow = true;
                        this.nextResult = this.insertIterator.next();
                    } else {
                        this.insertingNow = false;
                        this.insertingCompleted = true;
                    }
                }
            } else { // if inserting
                if (this.insertIterator.hasNext()) { // return an item from insertIterator at each iteration
                    this.nextResult = this.insertIterator.next();
                } else {
                    this.insertingNow = false;
                    this.insertingCompleted = true;
                }
            }
        }

        // if not inserting, take the next element from input sequence
        if (!this.insertingNow) {
            if (this.sequenceIterator.hasNext()) {
                this.nextResult = this.sequenceIterator.next();
                this.currentPosition++;
            } else if (this.insertIterator.hasNext()) {
                this.nextResult = this.insertIterator.next();
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
            this.insertIterator.close();
        } else {
            this.hasNext = true;
        }
    }
}
