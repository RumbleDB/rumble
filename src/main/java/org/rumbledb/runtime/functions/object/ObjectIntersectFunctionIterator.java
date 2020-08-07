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
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectIntersectFunctionIterator extends HybridRuntimeIterator {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    public ObjectIntersectFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.iterator = arguments.get(0);
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            List<Item> items = this.iterator.materialize(this.currentDynamicContextForLocalExecution);
            LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
            boolean firstItem = true;
            for (Item item : items) {
                // ignore non-object items
                if (item.isObject()) {
                    if (firstItem) {
                        // add all key-value pairs of the first item
                        for (String key : item.getKeys()) {
                            Item value = item.getItemByKey(key);
                            ArrayList<Item> valueList = new ArrayList<>();
                            valueList.add(value);
                            keyValuePairs.put(key, valueList);
                        }
                        firstItem = false;
                    } else {
                        // iterate over existing keys in the map of results
                        Iterator<String> keyIterator = keyValuePairs.keySet().iterator();
                        while (keyIterator.hasNext()) {
                            String key = keyIterator.next();
                            // if the new item doesn't contain the same keys
                            if (!item.getKeys().contains(key)) {
                                // remove the key from the map
                                keyIterator.remove();
                            } else {
                                // add the matching key's value to the list
                                Item value = item.getItemByKey(key);
                                keyValuePairs.get(key).add(value);
                            }
                        }
                    }
                }
            }

            Item result = ItemFactory.getInstance().createObjectItem(keyValuePairs);

            this.hasNext = false;
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " INTERSECT function",
                getMetadata()
        );
    }

    @Override
    protected void openLocal() {
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected void resetLocal() {
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        // Enclose object values into arrays.
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        Function<Item, Item> mapTransformation = new ObjectIntersectMapClosure();
        JavaRDD<Item> mapResult = childRDD.map(mapTransformation);

        // Reduce input objects.
        Function2<Item, Item, Item> transformation = new ObjectIntersectReduceClosure();
        Item reduceResult = mapResult.reduce(transformation);

        // transform Item to JavaRDD<Item>
        JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
        List<Item> listResult = Arrays.asList(reduceResult);
        return sparkContext.parallelize(listResult);
    }
}
