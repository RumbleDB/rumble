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
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.runtime.iterator.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.math.BigDecimal;

public class UnaryOperationIterator extends UnaryOperationBaseIterator {

    public UnaryOperationIterator(RuntimeIterator child, OperationalExpressionBase.Operator operator,
                                  IteratorMetadata iteratorMetadata) {
        super(child, operator, iteratorMetadata);
    }

    @Override
    public Item next() {

        if(!this._hasNext)
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE);
        if(_child instanceof EmptySequenceIterator){
            this._hasNext = false;
            return null;
        }

        _child.open(_currentDynamicContext);
        Item child = _child.next();
        _child.close();
        this._hasNext = false;
        if(this._operator== OperationalExpressionBase.Operator.MINUS)
        {
            if(Item.isNumeric(child)){
                if(child instanceof IntegerItem)
                    return new IntegerItem(-1 * ((IntegerItem)child).getIntegerValue());
                if(child instanceof DoubleItem)
                    return new DoubleItem(-1 * ((DoubleItem)child).getDoubleValue());
                if(child instanceof DecimalItem)
                    return new DecimalItem(((DecimalItem)child).getDecimalValue().multiply(new BigDecimal(-1)));
            }
            throw new UnexpectedTypeException("Unary expression has non numeric args " +
                    child.serialize());
        } else return child;

    }
}
