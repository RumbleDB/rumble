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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rumbledb.runtime.HybridRuntimeIterator.dataFrameToRDDOfItems;

public class SequenceLookupIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private int position;

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

        // we can do an optimization using SparkSQL OFFSET if it is a DataFrame
        if (iterator.isDataFrame()) {
            JSoundDataFrame df = iterator.getDataFrame(dynamicContext);
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
            return SparkSessionManager.collectRDDwithLimit(rdd, this.getMetadata()).get(0);
        }

        List<Item> materializedItems = new ArrayList<>();
        this.iterator.materializeNFirstItems(
            dynamicContext,
            materializedItems,
            this.position
        );
        if (materializedItems.size() >= this.position) {
            return materializedItems.get(this.position - 1);
        } else {
            return null;
        }
    }

}
