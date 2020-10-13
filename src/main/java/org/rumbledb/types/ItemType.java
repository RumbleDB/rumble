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

    private static final long serialVersionUID = 1L;
    private String name;

    public static final ItemType objectItem = new ItemType("object");
    public static final ItemType atomicItem = new ItemType("atomic");
    public static final ItemType stringItem = new ItemType("string");
    public static final ItemType integerItem = new ItemType("integer");
    public static final ItemType decimalItem = new ItemType("decimal");
    public static final ItemType doubleItem = new ItemType("double");
    public static final ItemType booleanItem = new ItemType("boolean");
    public static final ItemType arrayItem = new ItemType("array");
    public static final ItemType nullItem = new ItemType("null");
    public static final ItemType JSONItem = new ItemType("json-item");
    public static final ItemType durationItem = new ItemType("duration");
    public static final ItemType yearMonthDurationItem = new ItemType("yearMonthDuration");
    public static final ItemType dayTimeDurationItem = new ItemType("dayTimeDuration");
    public static final ItemType dateTimeItem = new ItemType("dateTime");
    public static final ItemType dateItem = new ItemType("date");
    public static final ItemType timeItem = new ItemType("time");
    public static final ItemType hexBinaryItem = new ItemType("hexBinary");
    public static final ItemType anyURIItem = new ItemType("anyURI");
    public static final ItemType base64BinaryItem = new ItemType("base64Binary");
    public static final ItemType item = new ItemType("item");
    public static final ItemType functionItem = new ItemType("function");

    public ItemType() {
    }

    private ItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ItemType getItemTypeByName(String name) {
        if (name.equals(objectItem.name)) {
            return objectItem;
        }
        if (name.equals(atomicItem.name)) {
            return atomicItem;
        }
        if (name.equals(stringItem.name)) {
            return stringItem;
        }
        if (name.equals(integerItem.name)) {
            return integerItem;
        }
        if (name.equals(decimalItem.name)) {
            return decimalItem;
        }
        if (name.equals(doubleItem.name)) {
            return doubleItem;
        }
        if (name.equals(booleanItem.name)) {
            return booleanItem;
        }
        if (name.equals(nullItem.name)) {
            return nullItem;
        }
        if (name.equals(arrayItem.name)) {
            return arrayItem;
        }
        if (name.equals(JSONItem.name)) {
            return JSONItem;
        }
        if (name.equals(durationItem.name)) {
            return durationItem;
        }
        if (name.equals(yearMonthDurationItem.name)) {
            return yearMonthDurationItem;
        }
        if (name.equals(dayTimeDurationItem.name)) {
            return dayTimeDurationItem;
        }
        if (name.equals(dateTimeItem.name)) {
            return dateTimeItem;
        }
        if (name.equals(dateItem.name)) {
            return dateItem;
        }
        if (name.equals(timeItem.name)) {
            return timeItem;
        }
        if (name.equals(anyURIItem.name)) {
            return anyURIItem;
        }
        if (name.equals(hexBinaryItem.name)) {
            return hexBinaryItem;
        }
        if (name.equals(base64BinaryItem.name)) {
            return base64BinaryItem;
        }
        if (name.equals(item.name)) {
            return item;
        }
        throw new OurBadException("Type unrecognized: " + name);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.name.equals(((ItemType) other).getName());
    }


    // Returns true if [this] is a subtype of [superType], any type is considered a subtype of itself
    public boolean isSubtypeOf(ItemType superType) {
        // TODO: what about Dates/Durations, base64, hex and anyURI
        if (superType.equals(item)) {
            return true;
        } else if (superType.equals(JSONItem)) {
            return this.equals(objectItem)
                || this.equals(arrayItem)
                || this.equals(JSONItem);
        } else if (superType.equals(atomicItem)) {
            return this.equals(stringItem)
                || this.equals(integerItem)
                || this.equals(decimalItem)
                || this.equals(doubleItem)
                || this.equals(booleanItem)
                || this.equals(nullItem)
                || this.equals(anyURIItem)
                || this.equals(atomicItem);
        }
        return this.equals(superType);
    }
    
    public ItemType findCommonSuperType(ItemType other){
        // TODO: check relation between Int and Double (numeric in general)
        // TODO: first check is necessary due to inconsistency in ItemType subtype check
        if(other.isSubtypeOf(this)){
            return this;
        } else if(this.isSubtypeOf(other)){
            return other;
        } else if(this.isSubtypeOf(ItemType.atomicItem) && other.isSubtypeOf(ItemType.atomicItem)){
            return ItemType.atomicItem;
        } else if(this.isSubtypeOf(ItemType.JSONItem) && other.isSubtypeOf(ItemType.JSONItem)){
            return ItemType.JSONItem;
        } else {
            return ItemType.item;
        }
    }

    public boolean staticallyCastableAs(ItemType other){
        // TODO: is null atomic or JSONitem?
        // TODO: are jsonitems not castable to or form (excluding null) what about item
        // TODO: no need for null special check if above applies
        // TODO: can we cast to item (depends on inner functioning of cast as)
        // JSON items cannot be cast from and to
        if(this.isSubtypeOf(JSONItem) || other.isSubtypeOf(JSONItem))
            return false;
        // anything can be casted to itself
        if(this.equals(other))
            return true;
        // anything can be casted from and to a string (or from one of its supertype)
        if(this.equals(stringItem) || this.equals(item) || this.equals(atomicItem) || other.equals(stringItem))
            return true;
        // boolean and numeric can be cast between themselves
        if(this.equals(booleanItem) || this.equals(integerItem) || this.equals(doubleItem) || this.equals(decimalItem)){
            if(other.equals(integerItem) ||
                    other.equals(doubleItem) ||
                    other.equals(decimalItem) ||
                    other.equals(stringItem) ||
                    other.equals(booleanItem)
            ) return true;
            else return false;
        }
        // base64 and hex can be cast between themselves
        if(this.equals(base64BinaryItem) || this.equals(hexBinaryItem)){
            if(other.equals(base64BinaryItem) ||
                    other.equals(hexBinaryItem) ||
                    other.equals(stringItem)
            ) return true;
            else return false;
        }
        // durations can be cast between themselves
        if(this.equals(durationItem) || this.equals(yearMonthDurationItem) || this.equals(dayTimeDurationItem)){
            if(other.equals(durationItem) ||
                    other.equals(yearMonthDurationItem) ||
                    other.equals(dayTimeDurationItem)
            ) return true;
            else return false;
        }
        // DateTime can be cast also to Date or Time
        if(this.equals(dateTimeItem)){
            if(other.equals(dateItem) || other.equals(timeItem)) return true;
            else return false;
        }
        // Date can be cast also to DateTime
        if(this.equals(dateItem)){
            if(other.equals(dateTimeItem)) return true;
            else return false;
        }
        // Otherwise this cannot be casted to other
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
