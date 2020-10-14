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
    private int index; // for private matrix operation

    public static final ItemType item = new ItemType("item", 0);
    public static final ItemType atomicItem = new ItemType("atomic", 1);
    public static final ItemType stringItem = new ItemType("string", 2);
    public static final ItemType integerItem = new ItemType("integer", 3);
    public static final ItemType decimalItem = new ItemType("decimal", 4);
    public static final ItemType doubleItem = new ItemType("double", 5);
    public static final ItemType booleanItem = new ItemType("boolean", 6);
    public static final ItemType nullItem = new ItemType("null", 7);
    public static final ItemType durationItem = new ItemType("duration", 8);
    public static final ItemType yearMonthDurationItem = new ItemType("yearMonthDuration", 9);
    public static final ItemType dayTimeDurationItem = new ItemType("dayTimeDuration", 10);
    public static final ItemType dateTimeItem = new ItemType("dateTime", 11);
    public static final ItemType dateItem = new ItemType("date", 12);
    public static final ItemType timeItem = new ItemType("time", 13);
    public static final ItemType hexBinaryItem = new ItemType("hexBinary", 14);
    public static final ItemType anyURIItem = new ItemType("anyURI", 15);
    public static final ItemType base64BinaryItem = new ItemType("base64Binary", 16);
    public static final ItemType JSONItem = new ItemType("json-item", 17);
    public static final ItemType objectItem = new ItemType("object", 18);
    public static final ItemType arrayItem = new ItemType("array", 19);
    public static final ItemType functionItem = new ItemType("function", 20);

    public ItemType() {
    }

    private ItemType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // resulting type of [row] + [col]
    private static ItemType addTable[][] = {
    //                item          atomic      string  integer     decimal     double      bool    null    duration                y-m-dur                 d-time-dur          date-time       date        time        hex         any-uri     base64      json-item   object      array       function
    /* item     */  { atomicItem,   atomicItem, null,   atomicItem, atomicItem, atomicItem, null,   null,   atomicItem,             atomicItem,             atomicItem,         atomicItem,     atomicItem, timeItem,   null,       null,       null,       null,       null,       null,       null },
    /* atomic   */  { atomicItem,   atomicItem, null,   atomicItem, atomicItem, atomicItem, null,   null,   atomicItem,             atomicItem,             atomicItem,         atomicItem,     atomicItem, timeItem,   null,       null,       null,       null,       null,       null,       null },
    /* string   */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* integer  */  { atomicItem,   atomicItem, null,   integerItem,decimalItem,doubleItem, null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* decimal  */  { atomicItem,   atomicItem, null,   decimalItem,decimalItem,doubleItem, null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* double   */  { atomicItem,   atomicItem, null,   doubleItem, doubleItem, doubleItem, null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* bool     */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* null     */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* duration */  { atomicItem,   atomicItem, null,   null,       null,       null,       null,   null,   durationItem,           yearMonthDurationItem,  dayTimeDurationItem,dateTimeItem,   dateItem,   timeItem,   null,       null,       null,       null,       null,       null,       null },
    /* y-m-dur  */  { atomicItem,   atomicItem, null,   null,       null,       null,       null,   null,   yearMonthDurationItem,  yearMonthDurationItem,  null,               dateTimeItem,   dateItem,   null,       null,       null,       null,       null,       null,       null,       null },
    /* d-t-dur  */  { atomicItem,   atomicItem, null,   null,       null,       null,       null,   null,   dayTimeDurationItem,    null,                   dayTimeDurationItem,dateTimeItem,   dateItem,   timeItem,   null,       null,       null,       null,       null,       null,       null },
    /* date-time*/  { atomicItem,   atomicItem, null,   null,       null,       null,       null,   null,   dateTimeItem,           dateTimeItem,           dateTimeItem,       null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* date     */  { atomicItem,   atomicItem, null,   null,       null,       null,       null,   null,   dateItem,               dateItem,               dateItem,           null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* time     */  { timeItem,     timeItem,   null,   null,       null,       null,       null,   null,   timeItem,               null,                   timeItem,           null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* hex      */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* any-uri  */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* base64   */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* json-item*/  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* object   */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* array    */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    /* function */  { null,         null,       null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,           null,       null,       null,       null,       null,       null,       null,       null,       null },
    };

    // resulting type of [row] - [col]
    private static ItemType subTable[][] = {
            //                item                  atomic                  string  integer     decimal     double      bool    null    duration                y-m-dur                 d-time-dur          date-time           date                time                hex         any-uri     base64      json-item   object      array       function
            /* item     */  { atomicItem,           atomicItem,             null,   atomicItem, atomicItem, atomicItem, null,   null,   atomicItem,             atomicItem,             atomicItem,         atomicItem,         atomicItem,         atomicItem,         null,       null,       null,       null,       null,       null,       null },
            /* atomic   */  { atomicItem,           atomicItem,             null,   atomicItem, atomicItem, atomicItem, null,   null,   atomicItem,             atomicItem,             atomicItem,         atomicItem,         atomicItem,         atomicItem,         null,       null,       null,       null,       null,       null,       null },
            /* string   */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* integer  */  { atomicItem,           atomicItem,             null,   integerItem,decimalItem,doubleItem, null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* decimal  */  { atomicItem,           atomicItem,             null,   decimalItem,decimalItem,doubleItem, null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* double   */  { atomicItem,           atomicItem,             null,   doubleItem, doubleItem, doubleItem, null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* bool     */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* null     */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* duration */  { durationItem,         durationItem,           null,   null,       null,       null,       null,   null,   durationItem,           yearMonthDurationItem,  dayTimeDurationItem,null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* y-m-dur  */  { yearMonthDurationItem,yearMonthDurationItem,  null,   null,       null,       null,       null,   null,   yearMonthDurationItem,  yearMonthDurationItem,  null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* d-t-dur  */  { dayTimeDurationItem,  dayTimeDurationItem,    null,   null,       null,       null,       null,   null,   dayTimeDurationItem,    null,                   dayTimeDurationItem,null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* date-time*/  { atomicItem,           atomicItem,             null,   null,       null,       null,       null,   null,   dateTimeItem,           dateTimeItem,           dateTimeItem,       dayTimeDurationItem,null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* date     */  { atomicItem,           atomicItem,             null,   null,       null,       null,       null,   null,   dateItem,               dateItem,               dateItem,           null,               dayTimeDurationItem,null,               null,       null,       null,       null,       null,       null,       null },
            /* time     */  { atomicItem,           atomicItem,             null,   null,       null,       null,       null,   null,   timeItem,               null,                   timeItem,           null,               null,               dayTimeDurationItem,null,       null,       null,       null,       null,       null,       null },
            /* hex      */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* any-uri  */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* base64   */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* json-item*/  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* object   */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* array    */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
            /* function */  { null,                 null,                   null,   null,       null,       null,       null,   null,   null,                   null,                   null,               null,               null,               null,               null,       null,       null,       null,       null,       null,       null },
    };

    public String getName() {
        return this.name;
    }

    public int getIndex() { return this.index; }

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

    // return the resulting statically inferred ItemType from adding [this] to [other], return null in case of incompatible inferred static types
    public ItemType staticallyAddTo(ItemType other, boolean isMinus) {
        if(isMinus){
            return subTable[this.getIndex()][other.getIndex()];
        } else {
            return addTable[this.getIndex()][other.getIndex()];
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
