/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectIntersectFunctionIterator extends ObjectFunctionIterator {
    public ObjectIntersectFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, ObjectFunctionOperators.INTERSECT, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator sequenceIterator = this._children.get(0);
            List<Item> items = getItemsFromIteratorWithCurrentContext(sequenceIterator);
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

            ObjectItem result = new ObjectItem(keyValuePairs);

            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " INTERSECT function",
                getMetadata());
    }
}
