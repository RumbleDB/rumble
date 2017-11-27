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
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class ArrayLookupIterator extends LocalRuntimeIterator {
    public ArrayLookupIterator(RuntimeIterator array, RuntimeIterator iterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(array);
        this._children.add(iterator);
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            this._children.get(0).open(_currentDynamicContext);
            this._children.get(1).open(_currentDynamicContext);
            this._array = (ArrayItem) this._children.get(0).next();
            Item lookup = this._children.get(1).next();
            if(this._children.get(1).hasNext() || lookup.isObject() || lookup.isArray())
                throw new InvalidSelectorException("Type error; There is not exactly one supplied parameter for an array selector: "
                        + lookup.serialize());
            if(!Item.isNumeric(lookup))
                throw new UnexpectedTypeException("Non numeric array lookup for " + lookup.serialize(), getMetadata());
            this._lookup = Item.getNumericValue(lookup, Integer.class);
            this._children.get(0).close();
            this._children.get(1).close();
            this._hasNext = false;
            //-1 for Jsoniq convetion, arrays start from 1
            return _array.getItemAt(_lookup - 1);
        }
        throw new IteratorFlowException("Invalid next call in Array Lookup");
    }

    private ArrayItem _array;
    private int _lookup;
}
