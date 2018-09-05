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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.StringRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectLookupIterator extends LocalRuntimeIterator {
    public ObjectLookupIterator(RuntimeIterator object, StringRuntimeIterator stringRuntimeIterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(object);
        this._children.add(stringRuntimeIterator);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._nextIndex = 0;

        RuntimeIterator iterator = this._children.get(0);
        this.items = getItemsFromIteratorWithCurrentContext(iterator);

        this._children.get(1).open(_currentDynamicContext);
        this._lookupKey = this._children.get(1).next();
        if(this._children.get(1).hasNext() || _lookupKey.isObject() || _lookupKey.isArray())
            throw new InvalidSelectorException("Type error; There is not exactly one supplied parameter for an array selector: "
                    + _lookupKey.serialize(), getMetadata());
        if(!_lookupKey.isString())
            throw new UnexpectedTypeException("Non numeric array lookup for " + _lookupKey.serialize(), getMetadata());
        this._children.get(1).close();

        setNextResult();
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;
        for (int i = _nextIndex; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof ObjectItem) {
                ObjectItem objItem = (ObjectItem)item;
                _nextResult = objItem.getItemByKey(((StringItem)_lookupKey).getStringValue());
                _nextIndex = i + 1;
                break;
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    private List<Item> items;
    private Item _lookupKey;
    private Item _nextResult;
    private int _nextIndex;     // index to start checking from if next item is requested
}
