/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

package org.rumbledb.runtime.operational;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.InstanceOfClosure;
import org.rumbledb.runtime.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;


public class InstanceOfIterator extends UnaryOperationBaseIterator {

    private static final long serialVersionUID = 1L;
    private final SequenceType sequenceType;

    public InstanceOfIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(iterator, OperationalExpressionBase.Operator.INSTANCE_OF, executionMode, iteratorMetadata);
        this.sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            if (!this.child.isRDD()) {
                List<Item> items = new ArrayList<>();
                this.child.open(this.currentDynamicContextForLocalExecution);

                while (this.child.hasNext()) {
                    items.add(this.child.next());
                }
                this.child.close();
                this.hasNext = false;

                if (this.sequenceType.isEmptySequence()) {
                    return ItemFactory.getInstance().createBooleanItem(items.size() == 0);
                }

                if (isInvalidArity(items.size())) {
                    return ItemFactory.getInstance().createBooleanItem(false);
                }

                ItemType itemType = this.sequenceType.getItemType();
                for (Item item : items) {
                    if (!item.isTypeOf(itemType)) {
                        return ItemFactory.getInstance().createBooleanItem(false);
                    }
                }

                return ItemFactory.getInstance().createBooleanItem(true);
            } else {
                JavaRDD<Item> childRDD = this.child.getRDD(this.currentDynamicContextForLocalExecution);
                this.hasNext = false;

                if (isInvalidArity(childRDD.take(2).size())) {
                    return ItemFactory.getInstance().createBooleanItem(false);
                }

                JavaRDD<Item> result = childRDD.filter(new InstanceOfClosure(this.sequenceType.getItemType()));
                return ItemFactory.getInstance().createBooleanItem(result.isEmpty());
            }
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    private boolean isInvalidArity(long numOfItems) {
        return (numOfItems != 0 && this.sequenceType.isEmptySequence())
            ||
            (numOfItems == 0
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrMore))
            ||
            (numOfItems > 1
                && (this.sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    this.sequenceType.getArity() == SequenceType.Arity.OneOrZero));
    }
}
