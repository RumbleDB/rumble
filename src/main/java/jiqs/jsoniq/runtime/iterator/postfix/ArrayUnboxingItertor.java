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
import jiqs.semantics.DynamicContext;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

public class ArrayUnboxingItertor extends LocalRuntimeIterator {
    public ArrayUnboxingItertor(RuntimeIterator arrayIterator) {
        super(null);
        this._children.add(arrayIterator);
    }

    @Override
    public Item next() {
        if(_array == null) {
            this._children.get(0).open(_currentDynamicContext);
            this._array = (ArrayItem) this._children.get(0).next();
            this._children.get(0).close();
        }

        if(currentIndex == _array.getSize() - 1)
            this._hasNext = false;
        if(currentIndex < _array.getSize()) {
            return _array.getItemAt(currentIndex++);
        }

        else
            throw new IteratorFlowException("Illegal next() call in Array Unboxing!");
    }

    @Override
    public void reset(DynamicContext dc) {
        this._array = null;
        this.currentIndex = 0;
        super.reset(dc);
    }

    private ArrayItem _array = null;
    private int currentIndex = 0;
}
