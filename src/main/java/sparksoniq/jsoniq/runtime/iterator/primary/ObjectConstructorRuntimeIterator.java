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

package sparksoniq.jsoniq.runtime.iterator.primary;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.ArrayList;
import java.util.List;

public class ObjectConstructorRuntimeIterator extends LocalRuntimeIterator {

    private List<RuntimeIterator> _keys;
    private List<RuntimeIterator> _values;
    private boolean _isMergedObject = false;

    public ObjectConstructorRuntimeIterator(List<RuntimeIterator> keys, List<RuntimeIterator> values,
                                            IteratorMetadata iteratorMetadata) {
        super(keys, iteratorMetadata);
        this._children.addAll(values);
        this._keys = keys;
        this._values = values;
    }

    public ObjectConstructorRuntimeIterator(List<RuntimeIterator> childExpressions,
                                            IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        childExpressions.forEach(c -> this._children.add(c));
        this._isMergedObject = true;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> values = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            if (_isMergedObject) {
                for (RuntimeIterator iterator : this._children) {
                    iterator.open(_currentDynamicContext);
                    while (iterator.hasNext()) {
                        ObjectItem item = (ObjectItem) iterator.next();
                        keys.addAll(item.getKeys());
                        values.addAll(item.getValues());
                    }
                    iterator.close();
                }
                this._hasNext = false;
                return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(getMetadata()));

            } else {

                for (RuntimeIterator valueIterator : this._values) {
                    List<Item> currentResults = new ArrayList<>();
                    valueIterator.open(this._currentDynamicContext);
                    while (valueIterator.hasNext()) {
                        currentResults.add(valueIterator.next());
                    }
                    valueIterator.close();
                    //SIMILAR TO ZORBA, if value is more than one item, wrap it in an array
                    if (currentResults.size() > 1) {
                        values.add(ItemFactory.getInstance().createArrayItem(currentResults));
                    } else if (currentResults.size() == 1) {
                        values.add(currentResults.get(0));
                    } else {
                        values.add(ItemFactory.getInstance().createNullItem());
                    }
                }

                for (RuntimeIterator keyIterator : this._keys) {
                    keyIterator.open(this._currentDynamicContext);
                    if (!keyIterator.hasNext()) {
                        throw new IteratorFlowException("A key cannot be the empty sequence", getMetadata());
                    }
                    Item key = keyIterator.next();
                    if (!key.isString()) {
                        throw new UnexpectedTypeException("Key provided for object creation must be of type String", getMetadata());
                    }
                    keys.add(key.getStringValue());
                    if (keyIterator.hasNext())
                        throw new IteratorFlowException("A key cannot be a sequence of more than one item", getMetadata());
                    keyIterator.close();
                }
                this._hasNext = false;
                return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(getMetadata()));
            }
        }
        throw new IteratorFlowException("Invalid next() call on object!", getMetadata());
    }
}
