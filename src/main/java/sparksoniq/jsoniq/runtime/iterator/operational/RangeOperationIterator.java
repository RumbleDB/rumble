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
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.BinaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

public class RangeOperationIterator extends BinaryOperationBaseIterator {

    public RangeOperationIterator(RuntimeIterator left, RuntimeIterator right, IteratorMetadata iteratorMetadata) {
        super(left, right, OperationalExpressionBase.Operator.TO, iteratorMetadata);
    }

    public boolean hasNext() {
        return this._hasNext;
    }

    @Override
    public AtomicItem next() {
        if (!isInitialized) {
            _index = 0;
            _leftIterator.open(_currentDynamicContext);
            _rightIterator.open(_currentDynamicContext);
            Item left = _leftIterator.next();
            Item right = _rightIterator.next();
            _leftIterator.close();
            _rightIterator.close();

            if (!(left instanceof IntegerItem) || !(right instanceof IntegerItem))
                throw new UnexpectedTypeException("Range expression has non numerics args " +
                        left.serialize() + ", " + right.serialize(), getMetadata());
            _left = Item.getNumericValue(left, Integer.class);
            _right = Item.getNumericValue(right, Integer.class);
            if (_right < _left) {
                this._hasNext = false;
                return null;
            }


            isInitialized = true;
            _index = _left;
            if (_index == _right)
                this._hasNext = false;
            return new IntegerItem(_index++, ItemMetadata.fromIteratorMetadata(getMetadata()));

        } else {
            if (_index > _right)
                throw new IteratorFlowException("Range Operation invalid next() call", getMetadata());
            if (_index == _right)
                this._hasNext = false;
            return new IntegerItem(_index++, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.isInitialized = false;
    }

    private boolean isInitialized = false;
    private int _left;
    private int _right;
    private int _index;
}
