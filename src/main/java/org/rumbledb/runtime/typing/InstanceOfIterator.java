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

package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.InstanceOfClosure;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InstanceOfIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;

    public InstanceOfIterator(
            RuntimeIterator child,
            SequenceType sequenceType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(child), executionMode, iteratorMetadata);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(dynamicContext, getMetadata());
        }
        if (!this.child.isRDDOrDataFrame()) {
            List<Item> items = new ArrayList<>();
            this.child.open(dynamicContext);

            while (this.child.hasNext()) {
                items.add(this.child.next());
            }
            this.child.close();

            if (this.sequenceType.isEmptySequence()) {
                return ItemFactory.getInstance().createBooleanItem(items.size() == 0);
            }

            if (isInvalidArity(items.size())) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }

            ItemType itemType = this.sequenceType.getItemType();
            for (Item item : items) {
                if (item != null && !item.getDynamicType().isResolved()) {
                    item.getDynamicType().resolve(dynamicContext, getMetadata());
                }
                if (!doesItemTypeMatchItem(itemType, item)) {
                    return ItemFactory.getInstance().createBooleanItem(false);
                }
            }

            return ItemFactory.getInstance().createBooleanItem(true);
        }
        if (this.child.isDataFrame()) {
            JSoundDataFrame childDF = this.child.getDataFrame(dynamicContext);
            if (isInvalidArity(childDF.take(2).size())) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }

            ItemType itemType = childDF.getItemType();
            return ItemFactory.getInstance().createBooleanItem(itemType.isSubtypeOf(this.sequenceType.getItemType()));
        }
        JavaRDD<Item> childRDD = this.child.getRDD(dynamicContext);

        if (isInvalidArity(childRDD.take(2).size())) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        JavaRDD<Item> result = childRDD.filter(new InstanceOfClosure(this.sequenceType.getItemType()));
        return ItemFactory.getInstance().createBooleanItem(result.isEmpty());
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

    /**
     * Item type tests. This supersedes the method isTypeOf() formerly located in the Item interface,
     * as part of the efforts to cleanly separate item storage from item manipulation (which is
     * the domain of responsibility of runtime iterators).
     * 
     * @param itemType the item type to match against the item.
     * @param itemToMatch the item to match against the type.
     * @return true if itemToMatch matches itemType.
     */
    public static boolean doesItemTypeMatchItem(ItemType itemType, Item itemToMatch) {
        return itemToMatch.getDynamicType().isSubtypeOf(itemType);
    }
}
