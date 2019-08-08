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
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class IndexOfFunctionIterator extends LocalFunctionCallIterator {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RuntimeIterator _sequenceIterator;
    private Item _search;
    private Item _nextResult;
    private int _currentIndex;

    public IndexOfFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "index-of function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _sequenceIterator = this._children.get(0);
        RuntimeIterator searchIterator = this._children.get(1);
        _currentIndex = 0;

        _sequenceIterator.open(context);
        searchIterator.open(context);

        if (!searchIterator.hasNext()) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with empty sequences", getMetadata());
        }
        _search = searchIterator.next();
        if (searchIterator.hasNext()) {
            throw new UnexpectedTypeException("Invalid args. index-of can't be performed with sequences with more than one items", getMetadata());
        }
        if (!_search.isAtomic()) {
            throw new NonAtomicKeyException("Invalid args. index-of can't be performed with a non-atomic parameter", getMetadata().getExpressionMetadata());
        }
        searchIterator.close();

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while (_sequenceIterator.hasNext()) {
            _currentIndex += 1;
            Item item = _sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException("Invalid args. index-of can't be performed with a non-atomic in the input sequence", getMetadata().getExpressionMetadata());
            } else {
                if (item.compareTo(_search) == 0) {
                    _nextResult = ItemFactory.getInstance().createIntegerItem(_currentIndex);
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
