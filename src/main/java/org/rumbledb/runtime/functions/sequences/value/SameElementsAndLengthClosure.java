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

import org.apache.spark.api.java.function.FlatMapFunction2;
import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SameElementsAndLengthClosure implements FlatMapFunction2<Iterator<Item>, Iterator<Item>, Boolean> {


    private static final long serialVersionUID = 1L;

    public SameElementsAndLengthClosure() {
    }

    public Iterator<Boolean> call(Iterator<Item> iterator1, Iterator<Item> iterator2) throws Exception {
        List<Boolean> list = new ArrayList<>();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            if (!(iterator1.next().equals(iterator2.next()))) {
                list.add(true);
                return list.iterator();
            }
        }
        if (iterator1.hasNext() || iterator2.hasNext()) {
            list.add(true);
        }
        return list.iterator();
    }
};
