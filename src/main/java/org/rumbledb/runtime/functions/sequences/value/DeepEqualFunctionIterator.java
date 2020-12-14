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

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import scala.Tuple2;

import java.util.Iterator;
import java.util.List;

public class DeepEqualFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;


    public DeepEqualFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;

            RuntimeIterator sequenceIterator1 = this.children.get(0);
            RuntimeIterator sequenceIterator2 = this.children.get(1);

            if (sequenceIterator1.isRDDOrDataFrame() && sequenceIterator2.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd1 = sequenceIterator1.getRDD(this.currentDynamicContextForLocalExecution);
                JavaRDD<Item> rdd2 = sequenceIterator2.getRDD(this.currentDynamicContextForLocalExecution);
                if (rdd1.partitions().size() == rdd2.partitions().size()) {
                    FlatMapFunction2<Iterator<Item>, Iterator<Item>, Boolean> filter =
                        new SameElementsAndLengthClosure();
                    JavaRDD<Boolean> differences = rdd1.zipPartitions(rdd2, filter);
                    return ItemFactory.getInstance().createBooleanItem(differences.isEmpty());
                } else {
                    JavaPairRDD<Long, Item> rdd1Zipped = rdd1.zipWithIndex().mapToPair(Tuple2::swap);
                    JavaPairRDD<Long, Item> rdd2Zipped = rdd2.zipWithIndex().mapToPair(Tuple2::swap);
                    JavaPairRDD<Long, Tuple2<Optional<Item>, Optional<Item>>> rddJoined = rdd1Zipped.fullOuterJoin(
                        rdd2Zipped
                    );
                    JavaPairRDD<Long, Tuple2<Optional<Item>, Optional<Item>>> rddFiltered = rddJoined.filter(
                        tuple -> !tuple._2()._1().equals(tuple._2()._2())
                    );
                    return ItemFactory.getInstance().createBooleanItem(rddFiltered.isEmpty());
                }
            }
            List<Item> items1 = sequenceIterator1.materialize(this.currentDynamicContextForLocalExecution);
            List<Item> items2 = sequenceIterator2.materialize(this.currentDynamicContextForLocalExecution);

            boolean res = checkDeepEqual(items1, items2);
            return ItemFactory.getInstance().createBooleanItem(res);
        } else {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "deep-equal function", getMetadata());
        }
    }


    public boolean checkDeepEqual(List<Item> items1, List<Item> items2) {
        if (items1.size() != items2.size()) {
            return false;
        } else {
            for (int i = 0; i < items1.size(); i++) {
                Item item1 = items1.get(i);
                Item item2 = items2.get(i);

                if (!item1.equals(item2)) {
                    return false;
                }
            }
            return true;
        }
    }
}
