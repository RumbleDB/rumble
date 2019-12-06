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
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;


public class AdditiveOperationIterator extends BinaryOperationBaseIterator {

    private static final long serialVersionUID = 1L;

    Item _left;
    Item _right;

    public AdditiveOperationIterator(
            RuntimeIterator left,
            RuntimeIterator right,
            OperationalExpressionBase.Operator operator,
            IteratorMetadata iteratorMetadata
    ) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                switch (_operator) {
                    case PLUS:
                        return _left.add(_right);
                    case MINUS:
                        return _left.subtract(_right);
                    default:
                        throw new IteratorFlowException("Non recognized additive operator.", getMetadata());
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
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);

        if (!_leftIterator.hasNext() || !_rightIterator.hasNext()) {
            this._hasNext = false;
        } else {
            _left = _leftIterator.next();
            _right = _rightIterator.next();
            this.checkBinaryOperation(_left, _right, _operator);
            this._hasNext = true;
            if (_leftIterator.hasNext() || _rightIterator.hasNext())
                throw new UnexpectedTypeException(
                        "Sequence of more than one item can not be promoted to "
                            +
                            "parameter type atomic of function add()",
                        getMetadata()
                );
        }
        _leftIterator.close();
        _rightIterator.close();
    }

}
