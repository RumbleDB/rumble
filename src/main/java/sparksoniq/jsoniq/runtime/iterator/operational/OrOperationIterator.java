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
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class OrOperationIterator extends BinaryOperationBaseIterator {

    public OrOperationIterator(RuntimeIterator left, RuntimeIterator right, IteratorMetadata iteratorMetadata) {
        super(left,right, OperationalExpressionBase.Operator.OR, iteratorMetadata);
    }

    @Override
    public AtomicItem next() {
        _leftIterator.open(_currentDynamicContext);
        _rightIterator.open(_currentDynamicContext);
        Item left = _leftIterator.next();
        Item right = _rightIterator.next();

        if((_leftIterator.hasNext() && (!(left instanceof ArrayItem || left instanceof ObjectItem))) ||
                (_rightIterator.hasNext() && (!(right instanceof ArrayItem && right instanceof ObjectItem))))
            // TODO: Define the correct exception effective boolean value is not defined
            throw new UnexpectedTypeException("Effective boolean value is not defined for sequence of multiple items if first item is not an array or an object  " +
                    left.serialize() + ", " + right.serialize(), getMetadata());

        _leftIterator.close();
        _rightIterator.close();
        this._hasNext = false;
        return new BooleanItem(Item.getEffectiveBooleanValue(left) || Item.getEffectiveBooleanValue(right)
                , ItemMetadata.fromIteratorMetadata(getMetadata()));
    }
}
