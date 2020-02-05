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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;

public class MultiplicativeOperationIterator extends BinaryOperationBaseIterator {


    private static final long serialVersionUID = 1L;
    Item _left;
    Item _right;

    public MultiplicativeOperationIterator(
            RuntimeIterator left,
            RuntimeIterator right,
            OperationalExpressionBase.Operator operator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(left, right, operator, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _leftIterator.open(_currentDynamicContextForLocalExecution);
        _rightIterator.open(_currentDynamicContextForLocalExecution);

        // if left or right equals empty sequence, return empty sequence
        if (!_leftIterator.hasNext() || !_rightIterator.hasNext()) {
            this._hasNext = false;
        } else {
            _left = _leftIterator.next();
            _right = _rightIterator.next();
            this.checkBinaryOperation(_left, _right, _operator);
            this._hasNext = true;
            if (_leftIterator.hasNext() || _rightIterator.hasNext())
                throw new UnexpectedTypeException(
                        "Sequence of more than one item can not be promoted to parameter type atomic of function add()",
                        getMetadata()
                );
        }
        _leftIterator.close();
        _rightIterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                switch (_operator) {
                    case MUL:
                        return _left.multiply(_right);
                    case DIV:
                        return _left.divide(_right);
                    case IDIV:
                        return _left.idivide(_right);
                    case MOD:
                        return _left.modulo(_right);
                    default:
                        throw new IteratorFlowException("Non recognized multiplicative operator.", getMetadata());
                }
            } catch (RuntimeException e) {
                throw new UnexpectedTypeException(
                        " \""
                            + _operator.name().toLowerCase()
                            + "\": operation not possible with parameters of type \""
                            + ItemTypes.getItemTypeName(_left.getClass().getSimpleName())
                            + "\" and \""
                            + ItemTypes.getItemTypeName(_right.getClass().getSimpleName())
                            + "\"",
                        getMetadata()
                );
            }
        }
        throw new IteratorFlowException("Multiplicative expression has non numeric args", getMetadata());
    }

}
