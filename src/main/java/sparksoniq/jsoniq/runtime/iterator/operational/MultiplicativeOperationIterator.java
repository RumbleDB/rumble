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
package sparksoniq.jsoniq.runtime.iterator.operational;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class MultiplicativeOperationIterator extends BinaryOperationBaseIterator {

    Item _left;
    Item _right;

    public MultiplicativeOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                           OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);

        // if left or right equals empty sequence, return empty sequence
        if (!_leftIterator.hasNext() || !_rightIterator.hasNext()) {
            this._hasNext = false;
        } else {
            _left = _leftIterator.next();
            _right = _rightIterator.next();
            if (_leftIterator.hasNext() || _rightIterator.hasNext() || !Item.isNumeric(_left) || !Item.isNumeric(_right))
                throw new UnexpectedTypeException("Multiplicative expression has non numeric args " +
                        _left.serialize() + ", " + _right.serialize(), getMetadata());

            this._hasNext = true;
        }
        _leftIterator.close();
        _rightIterator.close();
    }

    @Override
    public AtomicItem next() {
        if (this._hasNext) {
            this._hasNext = false;

            Type returnType = Item.getNumericResultType(_left, _right);
            if (returnType.equals(IntegerItem.class)) {
                try {
                    int l = Item.<Integer>getNumericValue(_left, Integer.class);
                    int r = Item.<Integer>getNumericValue(_right, Integer.class);
                    switch (this._operator) {
                        case MUL:
                            return new IntegerItem(l * r);
                        case DIV:
                            BigDecimal decLeft = Item.<BigDecimal>getNumericValue(_left, BigDecimal.class);
                            BigDecimal decRight = Item.<BigDecimal>getNumericValue(_right, BigDecimal.class);
                            BigDecimal bdResult = decLeft.divide(decRight, 10, BigDecimal.ROUND_HALF_UP);
                            // if the result contains no decimal part, convert to integer
                            if (bdResult.stripTrailingZeros().scale() <= 0) {
                                try {
                                    // exception is thrown if information is lost during conversion to integer
                                    // this happens if the bigdecimal has a decimal part, or if it can't be fit to an integer
                                    return new IntegerItem(bdResult.intValueExact());
                                } catch (ArithmeticException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                return new DecimalItem(bdResult);
                            }
                        case MOD:
                            return new IntegerItem(l % r);
                        case IDIV:
                            return new IntegerItem(l / r);
                        default:
                            new IteratorFlowException("Non recognized multicative operator.", getMetadata());
                    }
                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else if (returnType.equals(DoubleItem.class)) {
                double l = Item.<Double>getNumericValue(_left, Double.class);
                double r = Item.<Double>getNumericValue(_right, Double.class);
                switch (this._operator) {
                    case MUL:
                        return new DoubleItem(l * r);
                    case DIV:
                        return new DoubleItem(l / r);
                    case MOD:
                        return new DoubleItem(l % r);
                    case IDIV:
                        return new IntegerItem((int) (l / r));
                    default:
                        new IteratorFlowException("Non recognized multicative operator.", getMetadata());
                }
            } else if (returnType.equals(DecimalItem.class)) {
                BigDecimal l = Item.<BigDecimal>getNumericValue(_left, BigDecimal.class);
                BigDecimal r = Item.<BigDecimal>getNumericValue(_right, BigDecimal.class);
                switch (this._operator) {
                    case MUL:
                        return new DecimalItem(l.multiply(r));
                    case DIV:
                        return new DecimalItem(l.divide(r, 10, BigDecimal.ROUND_HALF_UP));
                    case MOD:
                        return new DecimalItem(l.remainder(r));
                    case IDIV:
                        return new IntegerItem(l.divideToIntegralValue(r).intValueExact());
                    default:
                        new IteratorFlowException("Non recognized multicative operator.", getMetadata());
                }
            }
        }
        throw new IteratorFlowException("Multiplicative expression has non numeric args", getMetadata());
    }

}
