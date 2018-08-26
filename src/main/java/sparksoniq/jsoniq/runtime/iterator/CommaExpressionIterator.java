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
package sparksoniq.jsoniq.runtime.iterator;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.exceptions.IteratorFlowException;

import java.util.ArrayList;
import java.util.List;

public class CommaExpressionIterator extends LocalRuntimeIterator {

    private int _currentIndex;
    private List<Item> results;

    public CommaExpressionIterator(List<RuntimeIterator> childIterators, IteratorMetadata iteratorMetadata) {
        super(childIterators, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        }
        throw new IteratorFlowException("Invalid next() call in Comma expression", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;
        this._currentIndex = 0;
        this.results = new ArrayList<>();

        // this._children.forEach(c -> c.reset(this._currentDynamicContext));
        for (RuntimeIterator child : this._children) {
            child.open(_currentDynamicContext);
            if (child.hasNext()) {
                results.add(child.next());
            }
            child.close();
        }
        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    protected Item getResult() {
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }
}
