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


import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;

public class ItemType implements Serializable {

    public static ItemType getItemTypeByName(String name) {
        if  (name.equals(AtomicItemType.objectItem.getName())) {
            return AtomicItemType.objectItem;
        }
        if  (name.equals(AtomicItemType.atomicItem.getName())) {
            return AtomicItemType.atomicItem;
        }
        if  (name.equals(AtomicItemType.stringItem.getName())) {
            return AtomicItemType.stringItem;
        }
        if  (name.equals(AtomicItemType.integerItem.getName())) {
            return AtomicItemType.integerItem;
        }
        if  (name.equals(AtomicItemType.decimalItem.getName())) {
            return AtomicItemType.decimalItem;
        }
        if  (name.equals(AtomicItemType.doubleItem.getName())) {
            return AtomicItemType.doubleItem;
        }
        if  (name.equals(AtomicItemType.booleanItem.getName())) {
            return AtomicItemType.booleanItem;
        }
        if  (name.equals(AtomicItemType.nullItem.getName())) {
            return AtomicItemType.nullItem;
        }
        if  (name.equals(AtomicItemType.arrayItem.getName())) {
            return AtomicItemType.arrayItem;
        }
        if  (name.equals(AtomicItemType.JSONItem.getName())) {
            return AtomicItemType.JSONItem;
        }
        if  (name.equals(AtomicItemType.durationItem.getName())) {
            return AtomicItemType.durationItem;
        }
        if  (name.equals(AtomicItemType.yearMonthDurationItem.getName())) {
            return AtomicItemType.yearMonthDurationItem;
        }
        if  (name.equals(AtomicItemType.dayTimeDurationItem.getName())) {
            return AtomicItemType.dayTimeDurationItem;
        }
        if  (name.equals(AtomicItemType.dateTimeItem.getName())) {
            return AtomicItemType.dateTimeItem;
        }
        if  (name.equals(AtomicItemType.dateItem.getName())) {
            return AtomicItemType.dateItem;
        }
        if  (name.equals(AtomicItemType.timeItem.getName())) {
            return AtomicItemType.timeItem;
        }
        if  (name.equals(AtomicItemType.anyURIItem.getName())) {
            return AtomicItemType.anyURIItem;
        }
        if  (name.equals(AtomicItemType.hexBinaryItem.getName())) {
            return AtomicItemType.hexBinaryItem;
        }
        if  (name.equals(AtomicItemType.base64BinaryItem.getName())) {
            return AtomicItemType.base64BinaryItem;
        }
        if (name.equals(item.name)) {
            return item;
        }
        throw new OurBadException("Type unrecognized: " + name);
    }

    protected static final long serialVersionUID = 1L;
    protected String name;

    public static final ItemType item = new ItemType("item");
    public static final ItemType functionItem = new ItemType("function");

    public ItemType() {
    }

    protected ItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.name.equals(other.toString());
    }


    // Returns true if [this] is a subtype of [superType], any type is considered a subtype of itself
    public boolean isSubtypeOf(ItemType superType) {
        return this.equals(superType);
    }

    public ItemType findCommonSuperType(ItemType other) {
        // item is the most generic type
        return this;
    }

    /**
     * Check at static time if [this] could be casted to [other] item type, requires [this] to be an atomic type
     *
     * @param other a strict subtype of atomic item type to which we are trying to cast
     * @return true if it is possible at static time to cast [this] to [other], false otherwise
     */
    public boolean staticallyCastableAs(ItemType other) {
        // this is not atomic and therefore cannot be casted
        // TODO: consider throwing error here
        return false;
    }

    // return [true] if this is a numeric type (i.e. [integerItem], [decimalItem] or [doubleItem]), false otherwise
    public boolean isNumeric() {
        return false;
    }

    // returns [true] if this can be promoted to string
    public boolean canBePromotedToString() {
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
