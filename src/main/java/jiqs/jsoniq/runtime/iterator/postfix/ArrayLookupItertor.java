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

import jiqs.jsoniq.item.ArrayItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.CommaExpressionIterator;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

public class ArrayLookupItertor extends LocalRuntimeIterator {
    public ArrayLookupItertor(RuntimeIterator array, RuntimeIterator iterator) {
        super(null);
        this._children.add(array);
        this._children.add(iterator);
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            this._children.get(0).open(_currentDynamicContext);
            this._children.get(1).open(_currentDynamicContext);
            this._array = (ArrayItem) this._children.get(0).next();
            this._lookup = Item.getNumericValue(this._children.get(1).next(), Integer.class);
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
