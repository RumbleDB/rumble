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
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.Iterator;
import java.util.List;

public class DeepEqualFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;


    public DeepEqualFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator sequenceIterator1 = this.children.get(0);
        RuntimeIterator sequenceIterator2 = this.children.get(1);
        if (this.children.size() == 3) {
            String collation = this.children.get(2).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new DefaultCollationException("Wrong collation parameter", getMetadata());
            }
        }

        if (sequenceIterator1.isRDDOrDataFrame() && sequenceIterator2.isRDDOrDataFrame()) {
            JavaRDD<Item> rdd1 = sequenceIterator1.getRDD(context);
            JavaRDD<Item> rdd2 = sequenceIterator2.getRDD(context);
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
        List<Item> items1 = sequenceIterator1.materialize(context);
        List<Item> items2 = sequenceIterator2.materialize(context);

        boolean res = checkDeepEqual(items1, items2);
        return ItemFactory.getInstance().createBooleanItem(res);
    }

    public boolean checkDeepEqual(List<Item> items1, List<Item> items2) {
        if (items1.size() != items2.size()) {
            return false;
        } else {
            for (int i = 0; i < items1.size(); i++) {
                Item item1 = items1.get(i);
                Item item2 = items2.get(i);
                // Specific to deep-equal but does not apply to eq operator
                if (
                    ((item1.isFloat() && Float.isNaN(item1.getFloatValue()))
                        || (item1.isDouble() && Double.isNaN(item1.getDoubleValue())))
                        && ((item2.isFloat() && Float.isNaN(item2.getFloatValue()))
                            || (item2.isDouble() && Double.isNaN(item2.getDoubleValue())))
                ) {
                    return true;
                }
                if (!item1.equals(item2)) {
                    return false;
                }
            }
            return true;
        }
    }
}
