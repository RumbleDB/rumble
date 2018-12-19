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

import java.util.LinkedList;
import java.util.Queue;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class ArrayUnboxingIterator extends LocalRuntimeIterator {

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults;   // queue that holds the results created by the current item in inspection

    public ArrayUnboxingIterator(RuntimeIterator arrayIterator, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(arrayIterator);
    }


    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResults.remove();  // save the result to be returned
            if (_nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException("Invalid next call in Array Unboxing", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _iterator = this._children.get(0);
        _iterator.open(context);
        _nextResults = new LinkedList<>();

        setNextResult();
    }

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            if (item instanceof ArrayItem) {
                ArrayItem arrItem = (ArrayItem) item;
                // if array is not empty, set the first item as the result
                if (0 < arrItem.getSize()) {
                    _nextResults.addAll(arrItem.getItems());
                    break;
                }
            }
        }

        if (_nextResults.isEmpty()) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
