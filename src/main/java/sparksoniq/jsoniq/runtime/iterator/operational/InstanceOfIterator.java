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
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class InstanceOfIterator extends UnaryOperationIterator {

    private final SequenceType _sequenceType;
    protected AtomicItem result;

    public InstanceOfIterator(RuntimeIterator child, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.INSTANCE_OF, iteratorMetadata);
        this._sequenceType = sequenceType;
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        List<Item> items = new ArrayList<>();
        _child.open(_currentDynamicContext);
        while (_child.hasNext())
            items.add(_child.next());
        _child.close();
        //Empty sequence, more items
        if (items.isEmpty() && _sequenceType.getArity() != SequenceType.Arity.OneOrZero
                && _sequenceType.getArity() != SequenceType.Arity.ZeroOrMore)
            this.result = new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
        if (items.size() == 1 && _sequenceType.getArity() != SequenceType.Arity.OneOrZero
                && _sequenceType.getArity() != SequenceType.Arity.OneOrMore &&
                _sequenceType.getArity() != SequenceType.Arity.One)
            this.result = new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
        for (Item item : items) {
            if (!item.isTypeOf(_sequenceType.getItemType())) {
                this.result = new BooleanItem(false, ItemMetadata.fromIteratorMetadata(getMetadata()));
                break;
            }
        }
        if (this.result == null) {
            this.result = new BooleanItem(true, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        this._hasNext = true;
    }

    @Override
    public AtomicItem next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }
}
