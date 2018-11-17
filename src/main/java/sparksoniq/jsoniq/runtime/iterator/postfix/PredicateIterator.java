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
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.primary.IntegerRuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.util.ArrayList;
import java.util.List;

public class PredicateIterator extends LocalRuntimeIterator {

    private RuntimeIterator _iterator;
    private RuntimeIterator _filter;
    private Item _nextResult;


    public PredicateIterator(RuntimeIterator sequence, RuntimeIterator filterExpression, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(sequence);
        this._children.add(filterExpression);
    }

    @Override
    public Item next() {
        if (_hasNext == true) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Predicate!", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        if (this._children.size() < 2) {
            throw new SparksoniqRuntimeException("Invalid Predicate! Must initialize filter before calling next");
        }

        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContext);
        _filter = this._children.get(1);

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> currentItems = new ArrayList<>();
            currentItems.add(item);
            _currentDynamicContext.addVariableValue("$$", currentItems);

            _filter.open(_currentDynamicContext);
            Item fil = null;
            if (_filter.hasNext()) {
                fil = _filter.next();
            }
            _filter.close();
            // if filter is an integer, it is used to return the element with the index equal to the given integer
            if (fil instanceof IntegerItem) {
                _iterator.close();
                List<Item> sequence = getItemsFromIteratorWithCurrentContext(_iterator);
                int index = ((IntegerItem) fil).getIntegerValue();
                // less than or equal to size -> b/c of -1
                if (index >= 1 && index <= sequence.size()) {
                    //-1 for Jsoniq convention, arrays start from 1
                    _nextResult = sequence.get(index - 1);
                }
                break;
            } else if (fil != null && fil.getEffectiveBooleanValue()) {
                _nextResult = item;
            }
            _filter.close();
        }
        _currentDynamicContext.removeVariable("$$");

        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
