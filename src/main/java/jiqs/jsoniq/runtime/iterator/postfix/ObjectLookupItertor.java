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
 package jiqs.jsoniq.runtime.iterator.postfix;

import jiqs.jsoniq.exceptions.InvalidSelectorException;
import jiqs.jsoniq.exceptions.UnexpectedTypeException;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.item.ObjectItem;
import jiqs.jsoniq.item.StringItem;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.primary.StringRuntimeIterator;

public class ObjectLookupItertor extends LocalRuntimeIterator {
    public ObjectLookupItertor(RuntimeIterator object, StringRuntimeIterator stringRuntimeIterator) {
        super(null);
        this._children.add(object);
        this._children.add(stringRuntimeIterator);
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            this._children.get(0).open(_currentDynamicContext);
            this._children.get(1).open(_currentDynamicContext);
            _object = (ObjectItem) this._children.get(0).next();
            Item _lookupKey = this._children.get(1).next();
            if(this._children.get(1).hasNext() || _lookupKey.isObject() || _lookupKey.isArray())
                throw new InvalidSelectorException("Invalid selector item: " + _lookupKey.serialize());
            if(!_lookupKey.isString())
                throw new UnexpectedTypeException("Non numeric array lookup for " + _lookupKey.serialize());
            this._children.get(0).close();
            this._children.get(1).close();
            _hasNext = false;
            return _object.getItemByKey(((StringItem)_lookupKey).getStringValue());
        }
        throw new IteratorFlowException("Invalid next call in Object Lookup");
    }


    private ObjectItem _object = null;
}
