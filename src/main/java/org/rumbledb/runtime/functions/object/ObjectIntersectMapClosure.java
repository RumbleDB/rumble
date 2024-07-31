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

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectIntersectMapClosure implements Function<Item, Item> {


    private static final long serialVersionUID = 1L;

    public ObjectIntersectMapClosure() {
    }

    public Item call(Item arg0) throws Exception {
        if (!arg0.isObject())
            return arg0;

        LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
        for (String key : arg0.getKeys()) {
            Item value = arg0.getItemByKey(key);
            Item arrayValue = ItemFactory.getInstance()
                .createArrayItem(new ArrayList<Item>(Collections.singletonList(value)));
            keyValuePairs.put(key, new ArrayList<Item>(Collections.singletonList(arrayValue)));
        }

        return ItemFactory.getInstance().createObjectItem(keyValuePairs, true);
    }
};
