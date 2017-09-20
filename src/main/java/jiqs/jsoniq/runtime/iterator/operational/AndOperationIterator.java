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

import jiqs.exceptions.IteratorFlowException;
import jiqs.jsoniq.item.AtomicItem;
import jiqs.jsoniq.item.BooleanItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;

public class AndOperationIterator extends BinaryOperationBaseIterator {

    public AndOperationIterator(RuntimeIterator left, RuntimeIterator right) {
        super(left,right, OperationalExpressionBase.Operator.AND);
    }

    @Override
    public AtomicItem next() {
        if(this._hasNext) {
            _leftIterator.open(_currentDynamicContext);
            _rightIterator.open(_currentDynamicContext);
            Item left = _leftIterator.next();
            Item right = _rightIterator.next();
            _leftIterator.close();
            _rightIterator.close();
            this._hasNext = false;
            return new BooleanItem(Item.getEffectiveBooleanValue(left) && Item.getEffectiveBooleanValue(right));
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE);

    }
}
