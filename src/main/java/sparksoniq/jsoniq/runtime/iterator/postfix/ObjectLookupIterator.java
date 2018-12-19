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

import java.math.BigDecimal;

import sparksoniq.exceptions.InvalidSelectorException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ContextExpressionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class ObjectLookupIterator extends LocalRuntimeIterator {

    private RuntimeIterator _iterator;
    private Item _lookupKey;
    private boolean _contextLookup;
    private Item _nextResult;

    public ObjectLookupIterator(RuntimeIterator object, RuntimeIterator lookupIterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(object);
        this._children.add(lookupIterator);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._currentDynamicContext = context;

        _iterator = this._children.get(0);
        RuntimeIterator lookupIterator = this._children.get(1);

        if (lookupIterator instanceof ContextExpressionIterator) {
            _contextLookup = true;
        } else {
            _contextLookup = false;
        }

        if (!_contextLookup) {
            lookupIterator.open(_currentDynamicContext);
            if (lookupIterator.hasNext()) {
                this._lookupKey = lookupIterator.next();
            } else {
                throw new InvalidSelectorException("Invalid Lookup Key; Object lookup can't be performed with zero keys: "
                        , getMetadata());
            }
            if (lookupIterator.hasNext())
                throw new InvalidSelectorException("\"Invalid Lookup Key; Object lookup can't be performed with multiple keys: "
                        + _lookupKey.serialize(), getMetadata());
            if (_lookupKey.isNull() || _lookupKey.isObject() || _lookupKey.isArray()) {
                throw new UnexpectedTypeException("Type error; Object selector can't be converted to a string: "
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
                throw new UnexpectedTypeException("Non string object lookup for " + _lookupKey.serialize(), getMetadata());
            lookupIterator.close();
        }

        _iterator.open(_currentDynamicContext);
        setNextResult();
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item instanceof ObjectItem) {
                ObjectItem objItem = (ObjectItem) item;
                if (!_contextLookup) {
                    Item result = objItem.getItemByKey(((StringItem) _lookupKey).getStringValue());
                    if (result != null) {
                        _nextResult = result;
                        break;
                    }
                } else {
                    Item contextItem = _currentDynamicContext.getVariableValue("$$").get(0);
                    _nextResult = objItem.getItemByKey(((StringItem)contextItem).getStringValue());
                }
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
