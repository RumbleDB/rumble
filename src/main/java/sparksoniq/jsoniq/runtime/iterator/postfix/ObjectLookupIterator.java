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
package sparksoniq.jsoniq.runtime.iterator.postfix;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.*;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.StringRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObjectLookupIterator extends LocalRuntimeIterator {

    private List<Item> results;
    private int _currentIndex;
    private List<Item> items;
    private Item _lookupKey;

    public ObjectLookupIterator(RuntimeIterator object, RuntimeIterator lookupIterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(object);
        this._children.add(lookupIterator);
    }

    @Override
    public void open(DynamicContext context) {
        if(this.isOpen())
            throw new IteratorFlowException("Variable reference iterator already open", getMetadata());
        this._currentDynamicContext = context;
        this._currentIndex = 0;

        this.items = getItemsFromIteratorWithCurrentContext(this._children.get(0));

        this._children.get(1).open(_currentDynamicContext);
        this._lookupKey = this._children.get(1).next();
        if (this._children.get(1).hasNext())
            throw new InvalidSelectorException("Type error; There is not exactly one supplied parameter for an object selector: "
                    + _lookupKey.serialize(), getMetadata());
        if (_lookupKey.isNull() || _lookupKey.isObject() || _lookupKey.isArray()) {
            throw new InvalidSelectorException("Type error; Object selector can't be converted to a string: "
                    + _lookupKey.serialize(), getMetadata());
        } else {
            // convert to string
            if (_lookupKey.isBoolean()) {
                Boolean value = ((BooleanItem)_lookupKey).getValue();
                _lookupKey = new StringItem(value.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if (_lookupKey.isDecimal()) {
                BigDecimal value = ((DecimalItem)_lookupKey).getValue();
                _lookupKey = new StringItem(value.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if (_lookupKey.isDouble()) {
                Double value = ((DoubleItem)_lookupKey).getValue();
                _lookupKey = new StringItem(value.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if (_lookupKey.isInteger()) {
                Integer value = ((IntegerItem)_lookupKey).getValue();
                _lookupKey = new StringItem(value.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if (_lookupKey.isString()) {
                // do nothing
            }
        }

        if (!_lookupKey.isString())
            throw new UnexpectedTypeException("Non numeric object lookup for " + _lookupKey.serialize(), getMetadata());
        this._children.get(1).close();

        for (Item i : items) {
            if (i instanceof ObjectItem) {
                ObjectItem objItem = (ObjectItem) i;
                Item result = objItem.getItemByKey(((StringItem) _lookupKey).getStringValue());
                if (result != null)
                    results.add(result);
            }
        }

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            if(results == null){
                RuntimeIterator iterator = this._children.get(0);
                List<Item> items = getItemsFromIteratorWithCurrentContext(iterator);
                results = new ArrayList<>();
                _currentIndex = 0;

                this._children.get(1).open(_currentDynamicContext);
                Item _lookupKey = this._children.get(1).next();
                if(this._children.get(1).hasNext() || _lookupKey.isObject() || _lookupKey.isArray())
                    throw new InvalidSelectorException("Type error; There is not exactly one supplied parameter for an array selector: "
                            + _lookupKey.serialize(), getMetadata());
                if(!_lookupKey.isString())
                    throw new UnexpectedTypeException("Non numeric array lookup for " + _lookupKey.serialize(), getMetadata());
                this._children.get(1).close();

                for (Item i:items) {
                    if (i instanceof ObjectItem) {
                        ObjectItem objItem = (ObjectItem)i;
                        results.add(objItem.getItemByKey(((StringItem)_lookupKey).getStringValue()));
                    }
                }
            }
            return getResult();
        }
        throw new IteratorFlowException("Invalid next call in Object Lookup", getMetadata());
    }

    protected Item getResult() {
        // if no results return empty sequence
        if (results == null || results.size() == 0) {
            _hasNext = false;
            return null;
        }
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }
}
