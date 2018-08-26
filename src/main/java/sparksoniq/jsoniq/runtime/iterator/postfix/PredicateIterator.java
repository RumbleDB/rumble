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

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class PredicateIterator extends LocalRuntimeIterator {

    private List<Item> unfilteredSequence = null;
    private List<Item> results = null;
    private int _currentIndex = 0;

    public PredicateIterator(RuntimeIterator sequence, RuntimeIterator filterExpression, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(sequence);
        this._children.add(filterExpression);
    }

    @Override
    public void reset(DynamicContext dc) {
        super.reset(dc);
        this.results = null;
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException("Invalid next() call in Predicate!", getMetadata());
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

        if (this._children.size() < 2) {
            throw new SparksoniqRuntimeException("Invalid Predicate! Must initialize filter before calling next");
        }

        unfilteredSequence = new ArrayList<>();
        RuntimeIterator sequence = this._children.get(0);
        //get unfiltered sequence
        sequence.open(_currentDynamicContext);
        while (sequence.hasNext())
            unfilteredSequence.add(sequence.next());
        sequence.close();
        // get filtered list
        results = new ArrayList<>();
        RuntimeIterator filter = this._children.get(1);
        for (Item item : unfilteredSequence) {
            //set the current item for the $$ children
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            _currentDynamicContext.addVariableValue("$$", currentItems);
            filter.open(_currentDynamicContext);
            if (Item.getEffectiveBooleanValue(filter.next()))
                results.add(item);
            filter.close();
            filter.reset(_currentDynamicContext);
        }
        _currentDynamicContext.removeVariable("$$");

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }
}
