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

package org.rumbledb.runtime.navigation;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

import static org.rumbledb.runtime.HybridRuntimeIterator.dataFrameToRDDOfItems;

public class SequenceLookupIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private int position;
    private final int optimizationThreshold = 10_000_000; // do optimization only if position is above this threshold

    public SequenceLookupIterator(
            RuntimeIterator sequence,
            int position,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(sequence), staticContext);
        this.iterator = sequence;
        this.position = position;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        if (this.position <= 0) {
            return null;
        }
        // we can do an optimization using SparkSQL OFFSET if it is a DataFrame
        if (this.iterator.isDataFrame() && this.position > this.optimizationThreshold) {
            JSoundDataFrame df = this.iterator.getDataFrame(dynamicContext);
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            df = df.evaluateSQL(
                String.format(
                    "SELECT * FROM %s LIMIT 1 OFFSET %s",
                    input,
                    Integer.toString(this.position - 1)
                ),
                df.getItemType()
            );
            JavaRDD<Item> rdd = dataFrameToRDDOfItems(
                df,
                this.getMetadata()
            );

            List<Item> results = rdd.take(1);
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);

        }

        if (this.iterator.isRDD() && this.position > this.optimizationThreshold) {
            JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);

            if (childRDD.isEmpty()) {
                return null;
            }
            JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
            JavaPairRDD<Item, Long> filteredRDD;
            filteredRDD = zippedRDD.filter(
                (input) -> input._2() == this.position - 1
            );
            List<Tuple2<Item, Long>> results = filteredRDD.take(1);
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0)._1();
        }

        if (this.position <= 0) {
            return null;
        }
        this.iterator.open(dynamicContext);
        int currentPosition = 0;
        Item result = null;
        while (this.iterator.hasNext() && currentPosition < this.position) {
            result = this.iterator.next();
            ++currentPosition;
        }
        this.iterator.close();
        if (currentPosition == this.position) {
            return result;
        }
        return null;
    }

}
