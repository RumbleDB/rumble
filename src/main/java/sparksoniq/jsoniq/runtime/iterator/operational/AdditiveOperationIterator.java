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
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.rumbledb.api.Item;


public class AdditiveOperationIterator extends BinaryOperationBaseIterator {

	private static final long serialVersionUID = 1L;
	
	Item _left;
    Item _right;

    public AdditiveOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                     OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left, right, operator, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            Type returnType = Item.getNumericResultType(_left, _right);
            if (returnType.equals(IntegerItem.class)) {
                try {
                    int l = _left.castToIntegerValue();
                    int r = _right.castToIntegerValue();
                    return this._operator == OperationalExpressionBase.Operator.PLUS ?
                            ItemFactory.getInstance().createIntegerItem(l + r) :
                            ItemFactory.getInstance().createIntegerItem(l - r);

                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else if (returnType.equals(DoubleItem.class)) {
                try {
                    double l = _left.castToDoubleValue();
                    double r = _right.castToDoubleValue();
                    return this._operator == OperationalExpressionBase.Operator.PLUS ?
                            ItemFactory.getInstance().createDoubleItem(l + r) :
                            ItemFactory.getInstance().createDoubleItem(l - r);

                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else if (returnType.equals(DecimalItem.class)) {
                try {
                    BigDecimal l = _left.castToDecimalValue();
                    BigDecimal r = _right.castToDecimalValue();
                    return this._operator == OperationalExpressionBase.Operator.PLUS ?
                            ItemFactory.getInstance().createDecimalItem(l.add(r)) :
                            ItemFactory.getInstance().createDecimalItem(l.subtract(r));

                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                }
            } else {
                throw new IteratorFlowException("Additive expression has non numeric args", getMetadata());
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
            if (_leftIterator.hasNext() || _rightIterator.hasNext() || !_left.isNumeric() || !_right.isNumeric())
                throw new UnexpectedTypeException("Additive expression has non numeric args " +
                        _left.serialize() + ", " + _right.serialize(), getMetadata());

            this._hasNext = true;
        }
        _leftIterator.close();
        _rightIterator.close();
    }

}
