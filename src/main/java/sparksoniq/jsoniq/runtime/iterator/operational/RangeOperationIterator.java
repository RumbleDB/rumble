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

package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class RangeOperationIterator extends BinaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;
    private int _left;
    private int _right;
    private int _index;

    public RangeOperationIterator(RuntimeIterator left, RuntimeIterator right, IteratorMetadata iteratorMetadata) {
        super(left, right, OperationalExpressionBase.Operator.TO, iteratorMetadata);
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    @Override
    public Item next() {
        if (_hasNext == true) {
            if (_index == _right)
                this._hasNext = false;
            return ItemFactory.getInstance().createIntegerItem(_index++);
        }
        throw new IteratorFlowException("Invalid next call in Range Operation", getMetadata());
    }

    public void open(DynamicContext context) {
        super.open(context);

        _index = 0;
        _leftIterator.open(_currentDynamicContextForLocalExecution);
        _rightIterator.open(_currentDynamicContextForLocalExecution);
        if (_leftIterator.hasNext() && _rightIterator.hasNext()) {
            Item left = _leftIterator.next();
            Item right = _rightIterator.next();

            if (_leftIterator.hasNext() || _rightIterator.hasNext() || !(left.isInteger()) || !(right.isInteger()))
                throw new UnexpectedTypeException(
                        "Range expression has non numeric args "
                            +
                            left.serialize()
                            + ", "
                            + right.serialize(),
                        getMetadata()
                );
            try {
                _left = left.castToIntegerValue();
                _right = right.castToIntegerValue();
            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
            if (_right < _left) {
                this._hasNext = false;
            } else {
                _index = _left;
                this._hasNext = true;
            }
        } else {
            this._hasNext = false;
        }

        _leftIterator.close();
        _rightIterator.close();

    }
}
