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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.FlatMappingLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectDescendantPairsFunctionIterator extends LocalFunctionCallIterator {


    @Serial
    private static final long serialVersionUID = 1L;

    public ObjectDescendantPairsFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new FlatMappingLocalCursor<>(
                this.getChild(0),
                context,
                item -> {
                    List<Item> results = new ArrayList<>();
                    getDescendantPairs(List.of(item), results);
                    return results.iterator();
                },
                getMetadata()
        );
    }

    private void getDescendantPairs(List<Item> items, java.util.Collection<Item> results) {
        for (Item item : items) {
            if (item.isArray()) {
                getDescendantPairs(item.getItemMembers(), results);
            } else if (item.isObject()) {
                List<String> keys = item.getStringKeys();
                for (String key : keys) {
                    Item value = item.getItemByKey(key);

                    List<String> keyList = Collections.singletonList(key);
                    List<Item> valueList = Collections.singletonList(value);

                    Item result = ItemFactory.getInstance()
                        .createObjectItem(keyList, valueList, getMetadata(), true);
                    results.add(result);
                    getDescendantPairs(valueList, results);
                }
            } else {
                // do nothing
            }
        }
    }
}
