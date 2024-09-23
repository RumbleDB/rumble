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
 * Authors: Ioana Stefan
 *
 */

package org.rumbledb.runtime.functions.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.spark.api.java.function.Function2;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

public class ObjectIntersectReduceClosure implements Function2<Item, Item, Item> {


    private static final long serialVersionUID = 1L;

    public ObjectIntersectReduceClosure() {
    }

    @Override
    public Item call(Item v1, Item v2) throws Exception {
        if (!v1.isObject())
            return v2;
        else if (!v2.isObject())
            return v1;

        LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();

        // add all key-value pairs in v1
        for (String key : v1.getKeys()) {
            Item value = v1.getItemByKey(key);
            ArrayList<Item> valueList = new ArrayList<>();
            valueList.add(value);
            keyValuePairs.put(key, valueList);
        }

        // iterate over existing keys in the map of results
        Iterator<String> keyIterator = keyValuePairs.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            // if the new item doesn't contain the same keys
            if (!v2.getKeys().contains(key)) {
                // remove the key from the map
                keyIterator.remove();
            } else {
                // add the matching key's value to the list
                Item value = v2.getItemByKey(key);
                Item prevValue = keyValuePairs.get(key).get(0);
                for (Item elem : value.getItems())
                    prevValue.putItem(elem);
            }
        }

        return ItemFactory.getInstance().createObjectItem(keyValuePairs, true);
    }
};
