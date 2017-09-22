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
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;


public class ComparisonOperationIterator extends BinaryOperationBaseIterator {

    public ComparisonOperationIterator(RuntimeIterator left, RuntimeIterator right,
                                       OperationalExpressionBase.Operator operator) {
        super(left,right,operator);
    }

    @Override
    public AtomicItem next() {
        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);
        Item left = _leftIterator.next();
        Item right = _rightIterator.next();

        _leftIterator.close();
        _rightIterator.close();
        this._hasNext = false;

        //TODO implement all comparisons (strings, objects)
        if(Item.isNumeric(left)) {
            if(!Item.isNumeric(left) || !Item.isNumeric(right))
                throw new UnexpectedTypeException("Invalid args for numeric comparison " + left.serialize() +
                        ", " + right.serialize());
            double l = Item.getNumericValue(left, Double.class);
            double r = Item.getNumericValue(right, Double.class);
            switch (this._operator){
                case GT:
                    return new BooleanItem(l > r);
                case GE:
                    return new BooleanItem(l >= r);
                case EQ:
                    return new BooleanItem(l == r);
                case LE:
                    return new BooleanItem(l <= r);
                case LT:
                    return new BooleanItem(l < r);
                case NE:
                    return new BooleanItem(l != r);
            }
        }
        if(left instanceof StringItem) {
            if(!(right instanceof StringItem))
                throw new IteratorFlowException("Invalid String comparison");
            String l = ((StringItem)left).getStringValue();
            String r = ((StringItem)right).getStringValue();
            switch (this._operator){
                case EQ:
                    return new BooleanItem(l.equals(r));
                case NE:
                    return new BooleanItem(!l.equals(r));
            }

        }

        throw new IteratorFlowException("Invalid comparison expression");
    }
}
