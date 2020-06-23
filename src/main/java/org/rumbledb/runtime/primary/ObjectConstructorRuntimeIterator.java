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

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ObjectConstructorRuntimeIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private List<RuntimeIterator> keys;
    private List<RuntimeIterator> values;
    private boolean isMergedObject = false;

    public ObjectConstructorRuntimeIterator(
            List<RuntimeIterator> keys,
            List<RuntimeIterator> values,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(keys, executionMode, iteratorMetadata);
        this.children.addAll(values);
        this.keys = keys;
        this.values = values;
    }

    public ObjectConstructorRuntimeIterator(
            List<RuntimeIterator> childExpressions,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.addAll(childExpressions);
        this.isMergedObject = true;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            LinkedHashMap<String, Item> content = new LinkedHashMap<>();
            if (this.isMergedObject) {
                for (RuntimeIterator iterator : this.children) {
                    iterator.open(this.currentDynamicContextForLocalExecution);
                    while (iterator.hasNext()) {
                        ObjectItem item = (ObjectItem) iterator.next();
                        for (String key : item.getKeys()) {
                            if (content.containsKey(key)) {
                                throw new DuplicateObjectKeyException(
                                        "Duplicate key: " + key,
                                        getMetadata()
                                );
                            }
                            content.put(key, item.getItemByKey(key));
                        }
                    }
                    iterator.close();
                }
                this.hasNext = false;
                return ItemFactory.getInstance()
                    .createObjectItem(content);

            } else {
                Item key = null;
                Item value = null;
                for (int i = 0; i < this.keys.size(); ++i) {
                    RuntimeIterator keyIterator = this.keys.get(i);
                    RuntimeIterator valueIterator = this.values.get(i);

                    List<Item> currentResults = new ArrayList<>();
                    valueIterator.open(this.currentDynamicContextForLocalExecution);
                    while (valueIterator.hasNext()) {
                        currentResults.add(valueIterator.next());
                    }
                    valueIterator.close();
                    // SIMILAR TO ZORBA, if value is more than one item, wrap it in an array
                    if (currentResults.size() > 1) {
                        value = ItemFactory.getInstance().createArrayItem(currentResults);
                    } else if (currentResults.size() == 1) {
                        value = currentResults.get(0);
                    } else {
                        value = ItemFactory.getInstance().createNullItem();
                    }

                    keyIterator.open(this.currentDynamicContextForLocalExecution);
                    if (!keyIterator.hasNext()) {
                        throw new IteratorFlowException("A key cannot be the empty sequence", getMetadata());
                    }
                    key = keyIterator.next();
                    if (!key.isString()) {
                        throw new UnexpectedTypeException(
                                "Key provided for object creation must be of type String",
                                getMetadata()
                        );
                    }
                    if (content.containsKey(key.getStringValue())) {
                        throw new DuplicateObjectKeyException(
                                "Duplicate key: " + key.getStringValue(),
                                getMetadata()
                        );
                    }
                    content.put(key.getStringValue(), value);
                    if (keyIterator.hasNext()) {
                        throw new IteratorFlowException(
                                "A key cannot be a sequence of more than one item",
                                getMetadata()
                        );
                    }
                    keyIterator.close();
                }
                this.hasNext = false;
                return ItemFactory.getInstance()
                    .createObjectItem(content);
            }
        }
        throw new IteratorFlowException("Invalid next() call on object!", getMetadata());
    }
}
