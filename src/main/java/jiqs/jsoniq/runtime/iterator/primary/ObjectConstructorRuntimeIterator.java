/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package jiqs.jsoniq.runtime.iterator.primary;

import jiqs.jsoniq.item.ArrayItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.item.ObjectItem;
import jiqs.jsoniq.item.StringItem;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.exceptions.IteratorFlowException;

import java.util.ArrayList;
import java.util.List;

public class ObjectConstructorRuntimeIterator extends LocalRuntimeIterator {

    public ObjectConstructorRuntimeIterator(List<RuntimeIterator> keys, List<RuntimeIterator> values) {
        super(keys);
        this._children.addAll(values);
        this._keys = keys;
        this._values = values;
    }

    public ObjectConstructorRuntimeIterator(List<ObjectConstructorRuntimeIterator> childExpressions) {
        super(null);
        childExpressions.forEach(c -> this._children.add(c));
        this._isMergedObject = true;
    }

    @Override
    public ObjectItem next() {
        if(this._hasNext){
            List<Item> values = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            if(_isMergedObject) {
                for(RuntimeIterator iterator : this._children){
                    iterator.open(_currentDynamicContext);
                    ObjectItem item = (ObjectItem) iterator.next();
                    iterator.close();
                    keys.addAll(item.getKeys());
                    values.addAll(item.getValues());
                }
                this._hasNext = false;
                return new ObjectItem(keys, values);

            } else {

                for(RuntimeIterator valueIterator : this._values){
                    List<Item> currentResults = new ArrayList<>();
                    valueIterator.open(this._currentDynamicContext);
                    while (valueIterator.hasNext())
                        currentResults.add(valueIterator.next());
//                    if(valueIterator.hasNext())
//                        throw new IteratorFlowException("Object value must return one item!");
                    valueIterator.close();
                    //SIMILAR TO ZORBA, if value is more than one item, wrap it in an array
                    if(currentResults.size() > 1)
                        values.add(new ArrayItem(currentResults));
                    else
                        values.add(currentResults.get(0));
                }

                for(RuntimeIterator keyIterator : this._keys){
                    keyIterator.open(this._currentDynamicContext);
                    try{
                        keys.add(((StringItem) keyIterator.next()).getStringValue());} catch (Exception ex) {
                        throw new IteratorFlowException("Object must have string keys!");
                    }
                    if(keyIterator.hasNext())
                        throw new IteratorFlowException("Object value must return one item!");
                    keyIterator.close();
                }
                this._hasNext = false;
                return new ObjectItem(keys, values);
            }
        }
        throw new IteratorFlowException("Invalid next() call on object!");
    }

    private List<RuntimeIterator>  _keys;
    private List<RuntimeIterator>  _values;
    private boolean _isMergedObject = false;
}
