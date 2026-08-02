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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.functions.sequences.general.InstanceOfClosure;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

public class InstanceOfIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan child;
    private final SequenceType sequenceType;

    public InstanceOfIterator(
            ItemRuntimePlan child,
            SequenceType sequenceType,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    @Override
    public Item evaluateAtMostOne(
            DynamicContext dynamicContext
    ) {
        return evaluate(this.child, this.sequenceType, getMetadata(), dynamicContext);
    }

    private static Item evaluate(
            ItemRuntimePlan child,
            SequenceType sequenceType,
            ExceptionMetadata metadata,
            DynamicContext dynamicContext
    ) {
        if (!sequenceType.isResolved()) {
            sequenceType.resolve(dynamicContext, metadata);
        }
        if (!child.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return evaluateLocal(child, sequenceType, metadata, dynamicContext);
        }
        if (child.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            HomogeneousItemDataFrame childDF = ItemRuntimeDataFrameFactory.INSTANCE
                .fromPlan(child, dynamicContext);
            if (isInvalidArity(childDF.take(2).size(), sequenceType)) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }

            ItemType itemType = childDF.getItemType();
            return ItemFactory.getInstance().createBooleanItem(itemType.isSubtypeOf(sequenceType.getItemType()));
        }
        JavaRDD<Item> childRDD = child.getRDD(dynamicContext);

        if (isInvalidArity(childRDD.take(2).size(), sequenceType)) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        JavaRDD<Item> result = childRDD.filter(new InstanceOfClosure(sequenceType.getItemType()));
        return ItemFactory.getInstance().createBooleanItem(result.isEmpty());
    }

    private static Item evaluateLocal(
            ItemRuntimePlan child,
            SequenceType sequenceType,
            ExceptionMetadata metadata,
            DynamicContext dynamicContext
    ) {
        List<Item> items = child.materialize(dynamicContext);

        if (sequenceType.isEmptySequence()) {
            return ItemFactory.getInstance().createBooleanItem(items.isEmpty());
        }
        if (isInvalidArity(items.size(), sequenceType)) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        ItemType itemType = sequenceType.getItemType();
        for (Item item : items) {
            if (item != null && !item.getDynamicType().isResolved()) {
                item.getDynamicType().resolve(dynamicContext, metadata);
            }
            if (!doesItemTypeMatchItem(itemType, item)) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }
        }
        return ItemFactory.getInstance().createBooleanItem(true);
    }

    private static boolean isInvalidArity(long numOfItems, SequenceType sequenceType) {
        return (numOfItems != 0 && sequenceType.isEmptySequence())
            ||
            (numOfItems == 0
                && (sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    sequenceType.getArity() == SequenceType.Arity.OneOrMore))
            ||
            (numOfItems > 1
                && (sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    sequenceType.getArity() == SequenceType.Arity.OneOrZero));
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
        if (itemToMatch.isMap()) {
            if (itemToMatch.getSize() == 0) {
                // empty map: matches
                // - all map types
                // - object types (js:object) WITHOUT a JSound schema attached
                if (
                    itemType.isSubtypeOf(BuiltinTypesCatalogue.mapItem)
                        && (!itemType.isObjectItemType() || itemType.equals(BuiltinTypesCatalogue.objectItem))
                ) {
                    return true;
                }
                return itemToMatch.getDynamicType().isSubtypeOf(itemType);
            }
            if (itemToMatch.getDynamicType().isSubtypeOf(itemType)) {
                // if the item already has a dynamic type that is a subtype of the required type, we can skip the more
                // expensive structural check
                return true;
            }
            List<Item> keys = itemToMatch.getItemKeys();
            ItemType keyType = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                keys,
                ExceptionMetadata.EMPTY_METADATA,
                TypeInferrenceUtils.TypeMergeMode.STRICT
            );
            SequenceType valueSequenceType = TypeInferrenceUtils.inferSequenceTypeOfLocalItemSequences(
                itemToMatch.getSequenceValues(),
                TypeInferrenceUtils.TypeMergeMode.STRICT
            );
            ItemType runtimeMapType = ItemTypeFactory.mapOf(keyType, valueSequenceType);

            // Structural map type vs. UDT: map(xs:string, xs:int) is not a subtype of a named object
            // schema type, but the validated item's dynamic type is (e.g. local:x).
            return runtimeMapType.isSubtypeOf(itemType);
        } else if (itemToMatch.isArray()) {
            List<List<Item>> members = itemToMatch.getSequenceMembers();
            if (members.isEmpty()) {
                // empty array: matches
                // - all array types
                // - js:array()
                if (
                    itemType.isSubtypeOf(BuiltinTypesCatalogue.xqueryArrayItem)
                        && (!itemType.isArrayItemType() || itemType.equals(BuiltinTypesCatalogue.arrayItem))
                )
                    return true;
                // default behavior for array types (js:array()) WITH restrictions
                return itemToMatch.getDynamicType().isSubtypeOf(itemType);
            }
            if (itemType.isXQueryArrayItemType()) {
                // If the expected type is an array, we can check the members against the expected member type.
                SequenceType expectedMemberType = itemType.getMemberSequenceType();
                SequenceType.Arity expectedArity = expectedMemberType.getArity();
                for (List<Item> memberSequence : members) {
                    if (expectedArity.equals(SequenceType.Arity.One) && memberSequence.size() != 1) {
                        return false;
                    }
                    if (expectedArity.equals(SequenceType.Arity.Zero) && !memberSequence.isEmpty()) {
                        return false;
                    }
                    if (expectedArity.equals(SequenceType.Arity.OneOrZero) && memberSequence.size() > 1) {
                        return false;
                    }
                    if (expectedArity.equals(SequenceType.Arity.OneOrMore) && memberSequence.isEmpty()) {
                        return false;
                    }
                    for (Item member : memberSequence) {
                        if (!doesItemTypeMatchItem(expectedMemberType.getItemType(), member)) {
                            return false;
                        }
                    }
                }
                return true;
            }
            SequenceType memberSequenceType = TypeInferrenceUtils.inferSequenceTypeOfLocalItemSequences(
                members,
                TypeInferrenceUtils.TypeMergeMode.STRICT
            );
            ItemType runtimeArrayType = ItemTypeFactory.xqueryArrayOf(memberSequenceType);
            // Structural array type vs. UDT: array(xs:string) is not a subtype of a named object
            // schema type, but the validated item's dynamic type is (e.g. local:x).
            return runtimeArrayType.isSubtypeOf(itemType)
                || itemToMatch.getDynamicType().isSubtypeOf(itemType);
        }
        return itemToMatch.getDynamicType().isSubtypeOf(itemType);
    }

}
