/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.value;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemUtil;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class DistinctValuesFunctionIterator extends LocalFunctionCallIterator {

    private RuntimeIterator _sequenceIterator;
    private Item _nextResult;
    private List<Item> _prevResults;

    public DistinctValuesFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "distinct-values function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _prevResults = new ArrayList<>();

        _sequenceIterator = this._children.get(0);
        _sequenceIterator.open(context);

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while(_sequenceIterator.hasNext()) {
            Item item = _sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException("Invalid args. distinct-values can't be performed on non-atomics", getMetadata().getExpressionMetadata());
            } else {
                if (!ItemUtil.listContainsItem(_prevResults, item)) {
                    _prevResults.add(item);
                    _nextResult = item;
                    break;
                }
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
