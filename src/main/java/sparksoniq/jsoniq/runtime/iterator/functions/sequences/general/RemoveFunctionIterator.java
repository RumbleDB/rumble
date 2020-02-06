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

public class RemoveFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator _sequenceIterator;
    private Item _nextResult;
    private int _removePosition; // position to remove the item
    private int _currentPosition; // current position


    public RemoveFunctionIterator(
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
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "remove function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _currentPosition = 1;

        RuntimeIterator positionIterator = this._children.get(1);
        positionIterator.open(context);
        if (!positionIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Invalid args. remove can't be performed with empty sequence as the position",
                    getMetadata()
            );
        }
        Item positionItem = positionIterator.next();
        if (positionItem.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an array parameter as the position",
                    getMetadata()
            );
        } else if (positionItem.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. remove can't be performed with an object parameter as the position",
                    getMetadata()
            );
        } else if (!(positionItem instanceof IntegerItem)) {
            throw new UnexpectedTypeException(
                    "Invalid args. Position parameter should be an integer",
                    getMetadata()
            );
        }
        _removePosition = ((IntegerItem) positionItem).getIntegerValue();
        positionIterator.close();

        _sequenceIterator = this._children.get(0);
        _sequenceIterator.open(context);
        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        if (_sequenceIterator.hasNext()) {
            if (_currentPosition == _removePosition) {
                _sequenceIterator.next(); // skip item to be removed
                _currentPosition++;
                if (_sequenceIterator.hasNext()) {
                    _nextResult = _sequenceIterator.next();
                    _currentPosition++;
                }
            } else {
                _nextResult = _sequenceIterator.next();
                _currentPosition++;
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
