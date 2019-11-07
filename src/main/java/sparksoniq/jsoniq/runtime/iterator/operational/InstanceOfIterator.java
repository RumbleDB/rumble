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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.InstanceOfClosure;
import sparksoniq.jsoniq.runtime.iterator.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;


public class InstanceOfIterator extends UnaryOperationBaseIterator {

    private static final long serialVersionUID = 1L;
    private final SequenceType _sequenceType;

    public InstanceOfIterator(RuntimeIterator iterator, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(iterator, OperationalExpressionBase.Operator.INSTANCE_OF, iteratorMetadata);
        this._sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            if (!_child.isRDD()) {
                List<Item> items = new ArrayList<>();
                _child.open(_currentDynamicContext);
                while (_child.hasNext())
                    items.add(_child.next());
                _child.close();
                this._hasNext = false;
                if (isInvalidArity(items.size())) return ItemFactory.getInstance().createBooleanItem(false);
                ItemType itemType = _sequenceType.getItemType();
                for (Item item : items) {
                    if (!item.isTypeOf(itemType)) {
                        return ItemFactory.getInstance().createBooleanItem(false);
                    }
                }
                return ItemFactory.getInstance().createBooleanItem(true);
            } else {
                JavaRDD<Item> childRDD = _child.getRDD(_currentDynamicContext);
                this._hasNext = false;
                if (isInvalidArity(childRDD.count())) return ItemFactory.getInstance().createBooleanItem(false);
                JavaRDD<Boolean> result = childRDD.map(new InstanceOfClosure(_sequenceType.getItemType()));

                return ItemFactory.getInstance().createBooleanItem(result.distinct().count() == 1 && result.first());
            }
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private boolean isInvalidArity(long numOfItems) {
        return (numOfItems != 0 && _sequenceType.isEmptySequence()) ||
                (numOfItems == 0 && (_sequenceType.getArity() == SequenceType.Arity.One ||
                        _sequenceType.getArity() == SequenceType.Arity.OneOrMore)) ||
                (numOfItems > 1 && (_sequenceType.getArity() == SequenceType.Arity.One ||
                        _sequenceType.getArity() == SequenceType.Arity.OneOrZero));
    }
}
