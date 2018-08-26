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
package sparksoniq.jsoniq.runtime.iterator.control;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;


public class IfRuntimeIterator extends LocalRuntimeIterator {

    private List<Item> results = null;
    private int _currentIndex;

    public IfRuntimeIterator(RuntimeIterator condition, RuntimeIterator branch, RuntimeIterator elseBranch,
                             IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(condition);
        this._children.add(branch);
        this._children.add(elseBranch);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.results = null;
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;
        _currentIndex = 0;

        RuntimeIterator condition = this._children.get(0);
        RuntimeIterator branch = this._children.get(1);
        RuntimeIterator elseBranch = null;
        if (this._children.size() > 2)
            elseBranch = this._children.get(2);
        Item conditionResult = getSingleItemOfTypeFromIterator(condition, Item.class);
        results = new ArrayList<>();
        if (Item.getEffectiveBooleanValue(conditionResult)) {
            results = getItemsFromIteratorWithCurrentContext(branch);
        } else {
            results = getItemsFromIteratorWithCurrentContext(elseBranch);
        }

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in IfRunTimeIterator",
                getMetadata());
    }

    protected Item getResult() {
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }
}
