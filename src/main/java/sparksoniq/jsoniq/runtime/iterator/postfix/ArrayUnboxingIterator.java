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

import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class ArrayUnboxingIterator extends LocalRuntimeIterator {

    private List<Item> results;
    private ArrayItem _array = null;
    private int _currentIndex = 0;
    
    public ArrayUnboxingIterator(RuntimeIterator arrayIterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(arrayIterator);
    }

    @Override
    public void reset(DynamicContext dc) {
        this._array = null;
        this._currentIndex = 0;
        super.reset(dc);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        } else {
            throw new IteratorFlowException("Illegal next() call in Array Unboxing!", getMetadata());
        }
    }

    protected Item getResult() {
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;
        this.results = new ArrayList<>();

        this._children.get(0).open(_currentDynamicContext);
        this._array = (ArrayItem) this._children.get(0).next();
        this._children.get(0).close();

        for (int i = 0; i < _array.getSize(); i++) {
            results.add(_array.getItemAt(i));
        }
        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
