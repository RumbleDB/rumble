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
package sparksoniq.jsoniq.runtime.iterator.operational;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class MultiplicativeOperationIterator extends BinaryOperationBaseIterator {

    private AtomicItem result;

    public MultiplicativeOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                           OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        if (_leftIterator instanceof EmptySequenceIterator || _rightIterator instanceof EmptySequenceIterator) {
            this._hasNext = false;
        } else {
            _leftIterator.open(_currentDynamicContext);
            _rightIterator.open(_currentDynamicContext);
            Item left = _leftIterator.next();
            Item right = _rightIterator.next();
            if (!Item.isNumeric(left) || !Item.isNumeric(right))
                throw new UnexpectedTypeException("Multiplicative expression has non numeric args " +
                        left.serialize() + ", " + right.serialize(), getMetadata());
            _leftIterator.close();
            _rightIterator.close();

            Type returnType = Item.getNumericResultType(left, right);
            if (returnType.equals(IntegerItem.class)) {
                int l = Item.<Integer>getNumericValue(left, Integer.class);
                int r = Item.<Integer>getNumericValue(right, Integer.class);
                switch (this._operator) {
                    case MUL:
                        this.result = new IntegerItem(l * r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case DIV:
                        this.result = new IntegerItem(l / r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case MOD:
                        this.result = new IntegerItem(l % r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case IDIV:
                        this.result = new IntegerItem(l / r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                }

            } else if (returnType.equals(DoubleItem.class)) {
                double l = Item.<Double>getNumericValue(left, Double.class);
                double r = Item.<Double>getNumericValue(right, Double.class);
                switch (this._operator) {
                    case MUL:
                        this.result = new DoubleItem(l * r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case DIV:
                        this.result = new DoubleItem(l / r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case MOD:
                        this.result = new DoubleItem(l % r, ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case IDIV:
                        this.result = new DoubleItem((int) (l / r), ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                }

            } else if (returnType.equals(DecimalItem.class)) {
                BigDecimal l = Item.<BigDecimal>getNumericValue(left, BigDecimal.class);
                BigDecimal r = Item.<BigDecimal>getNumericValue(right, BigDecimal.class);
                switch (this._operator) {
                    case MUL:
                        this.result = new DecimalItem(l.multiply(r), ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case DIV:
                        this.result = new DecimalItem(l.divide(r, 10, BigDecimal.ROUND_HALF_UP),
                                ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case MOD:
                        this.result = new DecimalItem(l.remainder(r), ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                    case IDIV:
                        this.result = new DecimalItem(l.divideToIntegralValue(r), ItemMetadata.fromIteratorMetadata(getMetadata()));
                        break;
                }

            } else {
                throw new IteratorFlowException("Additive expression has non numeric args", getMetadata());
            }
            this._hasNext = true;
        }
    }

    @Override
    public AtomicItem next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }
}
