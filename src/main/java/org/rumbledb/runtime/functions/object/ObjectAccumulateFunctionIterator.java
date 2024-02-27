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

package org.rumbledb.runtime.functions.object;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectAccumulateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ObjectAccumulateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator iterator = this.children.get(0);

        if (!iterator.isDataFrame()) {
            if (this.hasNext) {
                List<Item> items = iterator.materialize(context);
                LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
                for (Item item : items) {
                    // ignore non-object items
                    if (item.isObject()) {
                        for (String key : item.getKeys()) {
                            Item value = item.getItemByKey(key);
                            if (!keyValuePairs.containsKey(key)) {
                                List<Item> valueList = new ArrayList<>();
                                valueList.add(value);
                                keyValuePairs.put(key, valueList);
                            }
                            // store values for key collisions in a list
                            else {
                                keyValuePairs.get(key).add(value);
                            }
                        }
                    }
                }

                Item result = ItemFactory.getInstance().createObjectItem(keyValuePairs);

                this.hasNext = false;
                return result;
            }
        }

        JavaRDD<Item> childRDD = iterator.getRDD(context);
        Function<Item, Item> mapTransformation = new ObjectIntersectMapClosure();
        JavaRDD<Item> mapResult = childRDD.map(mapTransformation);

        Function2<Item, Item, Item> reductionTransformation = new ObjectIntersectReduceClosure();
        Item result = mapResult.reduce(reductionTransformation);

        return result;


    }

}
