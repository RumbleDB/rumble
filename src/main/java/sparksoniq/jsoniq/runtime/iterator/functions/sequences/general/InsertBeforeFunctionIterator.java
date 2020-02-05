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

package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class InsertBeforeFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _sequenceIterator;
    private RuntimeIterator _insertIterator;
    private Item _nextResult;
    private int _insertPosition; // position to start inserting
    private int _currentPosition; // current position
    private boolean _insertingNow; // check if currently iterating over insertIterator
    private boolean _insertingCompleted;

    public InsertBeforeFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            Item result = _nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "insert-before function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _currentPosition = 1; // initialize index as the first item
        _insertingNow = false;
        _insertingCompleted = false;

        RuntimeIterator positionIterator = this._children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. insert-before can't be performed with empty sequence as the position",
                    getMetadata()
            );
        }
        Item positionItem = positionIterator.next();
        if (positionItem.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an array parameter as the position",
                    getMetadata().getExceptionMetadata()
            );
        } else if (positionItem.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. insert-before can't be performed with an object parameter as the position",
                    getMetadata().getExceptionMetadata()
            );
        } else if (!(positionItem.isInteger())) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata()
            );
        }
        _insertPosition = ((IntegerItem) positionItem).getIntegerValue();
        positionIterator.close();

        _sequenceIterator = this._children.get(0);
        _insertIterator = this._children.get(2);

        _sequenceIterator.open(context);
        _insertIterator.open(context);

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        // don't check for insertion triggers once insertion is completed
        if (_insertingCompleted == false) {
            if (!_insertingNow) {
                if (_insertPosition <= _currentPosition) { // start inserting if condition is met
                    if (_insertIterator.hasNext()) {
                        _insertingNow = true;
                        _nextResult = _insertIterator.next();
                    } else {
                        _insertingNow = false;
                        _insertingCompleted = true;
                    }
                }
            } else { // if inserting
                if (_insertIterator.hasNext()) { // return an item from _insertIterator at each iteration
                    _nextResult = _insertIterator.next();
                } else {
                    _insertingNow = false;
                    _insertingCompleted = true;
                }
            }
        }

        // if not inserting, take the next element from input sequence
        if (_insertingNow == false) {
            if (_sequenceIterator.hasNext()) {
                _nextResult = _sequenceIterator.next();
                _currentPosition++;
            } else if (_insertIterator.hasNext()) {
                _nextResult = _insertIterator.next();
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _sequenceIterator.close();
            _insertIterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
