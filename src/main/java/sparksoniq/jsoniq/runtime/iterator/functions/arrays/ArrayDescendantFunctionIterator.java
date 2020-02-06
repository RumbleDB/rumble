/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.jsoniq.runtime.iterator.functions.arrays;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayDescendantFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    private RuntimeIterator _iterator;
    private Queue<Item> _nextResults; // queue that holds the results created by the current item in inspection


    public ArrayDescendantFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(context);
        _nextResults = new LinkedList<>();

        setNextResult();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResults.remove(); // save the result to be returned
            if (_nextResults.isEmpty()) {
                // if there are no more results left in the queue, trigger calculation for the next result
                setNextResult();
            }
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " DESCENDANT-ARRAYS function",
                getMetadata()
        );
    }

    public void setNextResult() {
        while (_iterator.hasNext()) {
            Item item = _iterator.next();
            List<Item> singleItemList = new ArrayList<>();
            singleItemList.add(item);

            getDescendantArrays(singleItemList);
            if (!(_nextResults.isEmpty())) {
                break;
            }

        }

        if (_nextResults.isEmpty()) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }

    private void getDescendantArrays(List<Item> items) {
        for (Item item : items) {
            if (item.isArray()) {
                _nextResults.add(item);
                getDescendantArrays(item.getItems());
            } else if (item.isObject()) {
                getDescendantArrays(item.getValues());
            } else {
                // for atomic types: do nothing
            }
        }
    }
}
