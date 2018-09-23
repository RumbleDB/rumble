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

import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Type;
import java.math.BigDecimal;


public class AdditiveOperationIterator extends BinaryOperationBaseIterator {

    Item _left;
    Item _right;

    public AdditiveOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                     OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(left,right,operator, iteratorMetadata);
    }

    @Override
    public AtomicItem next() {
        if(this._hasNext){
            this._hasNext = false;

            Type returnType = Item.getNumericResultType(_left, _right);
            if(returnType.equals(IntegerItem.class)){
                int l = Item.<Integer>getNumericValue(_left, Integer.class);
                int r = Item.<Integer>getNumericValue(_right, Integer.class);
                return this._operator == OperationalExpressionBase.Operator.PLUS?
                        new IntegerItem(l + r,
                                ItemMetadata.fromIteratorMetadata(getMetadata())) :
                        new IntegerItem(l - r,
                                ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if(returnType.equals(DoubleItem.class)){
                double l = Item.<Double>getNumericValue(_left, Double.class);
                double r = Item.<Double>getNumericValue(_right, Double.class);
                return this._operator == OperationalExpressionBase.Operator.PLUS?
                        new DoubleItem(l + r,
                                ItemMetadata.fromIteratorMetadata(getMetadata())) :
                        new DoubleItem(l - r,
                                ItemMetadata.fromIteratorMetadata(getMetadata()));
            } else if(returnType.equals(DecimalItem.class)){
                BigDecimal l = Item.<BigDecimal>getNumericValue(_left, BigDecimal.class);
                BigDecimal r = Item.<BigDecimal>getNumericValue(_right, BigDecimal.class);
                return this._operator == OperationalExpressionBase.Operator.PLUS?
                        new DecimalItem(l.add(r),
                                ItemMetadata.fromIteratorMetadata(getMetadata())) :
                        new DecimalItem(l.subtract(r),
                                ItemMetadata.fromIteratorMetadata(getMetadata()));
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

        if(!_leftIterator.hasNext() || !_rightIterator.hasNext()){
            this._hasNext = false;
        }
        else {
            _left = _leftIterator.next();
            _right = _rightIterator.next();
            if(_leftIterator.hasNext() || _rightIterator.hasNext() || !Item.isNumeric(_left) || !Item.isNumeric(_right))
                throw new UnexpectedTypeException("Additive expression has non numeric args " +
                        _left.serialize() + ", " + _right.serialize(), getMetadata());

            this._hasNext = true;
        }
        _leftIterator.close();
        _rightIterator.close();
    }

}
