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

package org.rumbledb.types;


import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class ItemType implements Serializable {

    protected static final long serialVersionUID = 1L;
    protected Name name;

    public static final ItemType item = new ItemType(Name.createVariableInDefaultTypeNamespace("item"));

    public ItemType() {
    }

    protected ItemType(Name name) {
        this.name = name;
    }

    public Name getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.name.equals(((ItemType) other).getName());
    }

    // Returns true if [this] is a function item
    public boolean isFunctionItem() {
        return false;
    }

    // Returns the signature of a function item
    public FunctionSignature getSignature() {
        throw new OurBadException("called getSignature on a non-function item");
    }

    // Returns true if [this] is a subtype of [superType], any type is considered a subtype of itself
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.equals(item)) {
            return true;
        } else if (superType.equals(AtomicItemType.JSONItem)) {
            return this.equals(AtomicItemType.objectItem)
                || this.equals(AtomicItemType.arrayItem)
                || this.equals(AtomicItemType.JSONItem);
        } else if (superType.equals(AtomicItemType.atomicItem)) {
            return this.equals(AtomicItemType.stringItem)
                || this.equals(AtomicItemType.integerItem)
                || this.equals(AtomicItemType.decimalItem)
                || this.equals(AtomicItemType.doubleItem)
                || this.equals(AtomicItemType.booleanItem)
                || this.equals(AtomicItemType.nullItem)
                || this.equals(AtomicItemType.anyURIItem)
                || this.equals(AtomicItemType.hexBinaryItem)
                || this.equals(AtomicItemType.base64BinaryItem)
                || this.equals(AtomicItemType.dateTimeItem)
                || this.equals(AtomicItemType.dateItem)
                || this.equals(AtomicItemType.timeItem)
                || this.equals(AtomicItemType.durationItem)
                || this.equals(AtomicItemType.yearMonthDurationItem)
                || this.equals(AtomicItemType.dayTimeDurationItem)
                || this.equals(AtomicItemType.atomicItem);
        } else if (superType.equals(AtomicItemType.durationItem)) {
            return this.equals(AtomicItemType.yearMonthDurationItem)
                || this.equals(AtomicItemType.dayTimeDurationItem)
                || this.equals(AtomicItemType.durationItem);
        } else if (superType.equals(AtomicItemType.decimalItem)) {
            return this.equals(AtomicItemType.integerItem) || this.equals(AtomicItemType.decimalItem);
        }
        return this.equals(superType);
    }

    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        // item is the most generic type
        return this;
    }

    /**
     * Check at static time if [this] could be casted to [other] item type, requires [this] to be an atomic type
     *
     * @param other a strict subtype of atomic item type to which we are trying to cast
     * @return true if it is possible at static time to cast [this] to [other], false otherwise
     */
    public boolean isStaticallyCastableAs(ItemType other) {
        // this is not atomic and therefore cannot be casted
        // TODO: consider throwing error here
        return false;
    }

    // return [true] if this is a numeric type (i.e. [integerItem], [decimalItem] or [doubleItem]), false otherwise
    public boolean isNumeric() {
        return false;
    }

    // returns [true] if this can be promoted to itemType
    public boolean canBePromotedTo(ItemType itemType) {
        return false;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    public boolean isObjectItemType() {
        return this.equals(AtomicItemType.objectItem);
    }

    public boolean getClosedFacet() {
        return false;
    }

    public Map<String, FieldDescriptor> getObjectContentFacet() {
        return Collections.emptyMap();
    }

    public boolean isFunctionItemType() {
        return false;
    }

    public int getTypeTreeDepth() {
        throw new UnsupportedOperationException("getTypeTreeDepth not implemented.");
    }

    public ItemType getBaseType() {
        throw new UnsupportedOperationException("getBaseType not implemented.");
    }

    public String getIdentifierString() {
        throw new UnsupportedOperationException("getIdentifierString not implemented.");
    }
}
