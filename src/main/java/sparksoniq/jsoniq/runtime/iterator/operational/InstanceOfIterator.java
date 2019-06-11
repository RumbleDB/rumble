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
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class InstanceOfIterator extends UnaryOperationIterator {

    private final SequenceType _sequenceType;

    public InstanceOfIterator(RuntimeIterator child, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.INSTANCE_OF, iteratorMetadata);
        this._sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> items = new ArrayList<>();
            _child.open(_currentDynamicContext);
            while (_child.hasNext())
                items.add(_child.next());
            _child.close();
            this._hasNext = false;
            //Empty sequence, more items
            if (items.isEmpty() && (_sequenceType.getArity() == SequenceType.Arity.One ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrMore)) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }
            if (items.size() == 1 && _sequenceType.getArity() == SequenceType.Arity.ZeroOrMore) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }
            for (Item item : items) {
                if (!item.isTypeOf(_sequenceType.getItemType())) {
                    return ItemFactory.getInstance().createBooleanItem(false);
                }
            }
            return ItemFactory.getInstance().createBooleanItem(true);
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }
}
