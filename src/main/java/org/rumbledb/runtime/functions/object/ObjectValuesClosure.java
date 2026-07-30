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
 * Authors: Stefan Irimescu, Can Berker Cikis, Ghislain Fourny
 *
 */

package org.rumbledb.runtime.functions.object;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectValuesClosure implements FlatMapFunction<Item, Item> {


    private static final long serialVersionUID = 1L;

    public ObjectValuesClosure() {
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!arg0.isObject()) {
            return results.iterator();
        }

        for (String key : arg0.getKeys()) {
            results.add(arg0.getItemByKey(key));
        }
        return results.iterator();
    }
};
