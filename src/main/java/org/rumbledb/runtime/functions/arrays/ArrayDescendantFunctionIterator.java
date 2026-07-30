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

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.FlatMappingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ArrayDescendantFunctionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> iterator;


    public ArrayDescendantFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = arguments.get(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new FlatMappingLocalCursor<>(
                this.iterator,
                context,
                item -> {
                    List<Item> results = new ArrayList<>();
                    getDescendantArrays(List.of(item), results);
                    return results.iterator();
                },
                getMetadata()
        );
    }

    private static void getDescendantArrays(List<Item> items, java.util.Collection<Item> results) {
        for (Item item : items) {
            if (item.isArray()) {
                results.add(item);
                if (item.isArrayOfItems()) {
                    getDescendantArrays(item.getItemMembers(), results);
                } else {
                    for (java.util.List<Item> member : item.getSequenceMembers()) {
                        getDescendantArrays(member, results);
                    }
                }
            } else if (item.isObject()) {
                getDescendantArrays(item.getItemValues(), results);
            } else {
                // for atomic types: do nothing
            }
        }
    }



    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.iterator.getRDD(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new ArrayDescendantClosure();
        return childRDD.flatMap(transformation);
    }
}
