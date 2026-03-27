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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DefaultCollationException;
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
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
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
                    tuple -> {
                        Optional<Item> left = tuple._2()._1();
                        Optional<Item> right = tuple._2()._2();
                        if (!left.isPresent() && !right.isPresent()) {
                            return false;
                        }
                        if (left.isPresent() ^ right.isPresent()) {
                            return true;
                        }
                        return !DeepEqualComparison.deepEqualItems(left.get(), right.get());
                    }
                );
                return ItemFactory.getInstance().createBooleanItem(rddFiltered.isEmpty());
            }
        }
        List<Item> items1 = sequenceIterator1.materialize(context);
        List<Item> items2 = sequenceIterator2.materialize(context);

        boolean res = DeepEqualComparison.deepEqualSequences(items1, items2);
        return ItemFactory.getInstance().createBooleanItem(res);
    }

    /**
     * Checks if two lists of items are deep-equal according to specification.
     *
     * @param items1 The first list of items
     * @param items2 The second list of items
     * @return true if the lists are deep-equal, false otherwise
     */
    public boolean checkDeepEqual(List<Item> items1, List<Item> items2) {
        return DeepEqualComparison.deepEqualSequences(items1, items2);
    }
}
