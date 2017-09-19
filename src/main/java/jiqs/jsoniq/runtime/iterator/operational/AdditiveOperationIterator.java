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
 package jiqs.jsoniq.runtime.iterator.operational;

import jiqs.jsoniq.exceptions.UnexpectedTypeException;
import jiqs.jsoniq.item.*;
import jiqs.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import jiqs.jsoniq.runtime.iterator.EmptySequenceIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;

import java.lang.reflect.Type;
import java.math.BigDecimal;


public class AdditiveOperationIterator extends BinaryOperationBaseIterator {

    public AdditiveOperationIterator(RuntimeIterator left, RuntimeIterator right, OperationalExpressionBase.Operator operator) {
        super(left,right,operator);
    }

    @Override
    public AtomicItem next() {

        if(!this._hasNext)
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE);
        if(_leftIterator instanceof EmptySequenceIterator || _rightIterator instanceof EmptySequenceIterator){
            this._hasNext = false;
            return null;
        }


        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);
        Item left = _leftIterator.next();
        Item right = _rightIterator.next();

        if(!Item.isNumeric(left) || !Item.isNumeric(right))
            throw new UnexpectedTypeException("Additive expression has non numeric args " +
                    left.serialize() + ", " + right.serialize());

        this._hasNext = false;
        _leftIterator.close();
        _rightIterator.close();

        Type returnType = Item.getNumericResultType(left, right);
        if(returnType.equals(IntegerItem.class)){
            int l = Item.<Integer>getNumericValue(left, Integer.class);
            int r = Item.<Integer>getNumericValue(right, Integer.class);
            return this._operator == OperationalExpressionBase.Operator.PLUS?
                    new IntegerItem(l + r) :
                    new IntegerItem(l - r);
        } else if(returnType.equals(DoubleItem.class)){
            double l = Item.<Double>getNumericValue(left, Double.class);
            double r = Item.<Double>getNumericValue(right, Double.class);
            return this._operator == OperationalExpressionBase.Operator.PLUS?
                    new DoubleItem(l + r) :
                    new DoubleItem(l - r);
        } else if(returnType.equals(DecimalItem.class)){
            BigDecimal l = Item.<BigDecimal>getNumericValue(left, BigDecimal.class);
            BigDecimal r = Item.<BigDecimal>getNumericValue(right, BigDecimal.class);
            return this._operator == OperationalExpressionBase.Operator.PLUS?
                    new DecimalItem(l.add(r)) :
                    new DecimalItem(l.subtract(r));
        }
        throw new IteratorFlowException("Additive expression has non numeric args");
    }
}
