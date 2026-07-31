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
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectIntersectFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> iterator;

    public ObjectIntersectFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (!this.iterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return intersect(this.iterator.materialize(context));
        }

        // Enclose object values into arrays.
        JavaRDD<Item> childRDD = this.iterator.getRDD(context);
        Function<Item, Item> mapTransformation = new ObjectIntersectMapClosure(
                this.getRuntimeStaticContext().isQuerySideEffecting()
        );
        JavaRDD<Item> mapResult = childRDD.map(mapTransformation);

        // Reduce input objects.
        Function2<Item, Item, Item> transformation = new ObjectIntersectReduceClosure();
        Item result = mapResult.reduce(transformation);

        return result;

    }

    private Item intersect(List<Item> items) {
        LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
        boolean firstItem = true;
        for (Item item : items) {
            if (!item.isObject()) {
                continue;
            }
            if (firstItem) {
                for (String key : item.getStringKeys()) {
                    keyValuePairs.put(key, new ArrayList<>(List.of(item.getItemByKey(key))));
                }
                firstItem = false;
                continue;
            }
            Iterator<String> keyIterator = keyValuePairs.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                if (!item.getStringKeys().contains(key)) {
                    keyIterator.remove();
                } else {
                    keyValuePairs.get(key).add(item.getItemByKey(key));
                }
            }
        }
        return ItemFactory.getInstance().createObjectItemFromValueLists(keyValuePairs, true);
    }

}
