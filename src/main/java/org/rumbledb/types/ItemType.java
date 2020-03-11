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
    private final String name;
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
    public static final ItemType yearMonthDurationItem = new ItemType("yearmonthduration");
    public static final ItemType dayTimeDurationItem = new ItemType("daytimeduration");
    public static final ItemType dateTimeItem = new ItemType("datetime");
    public static final ItemType dateItem = new ItemType("date");
    public static final ItemType timeItem = new ItemType("time");
    public static final ItemType hexBinaryItem = new ItemType("hexbinary");
    public static final ItemType base64BinaryItem = new ItemType("base64binary");
    public static final ItemType item = new ItemType("item");
    public static final ItemType functionItem = new ItemType("function");

    public ItemType(String name) {
        this.name = name;
    }

    public static ItemType getItemTypeByName(String name) {
        name = name.toLowerCase();
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


    boolean isSubtypeOf(ItemType superType) {
        if (superType == item) {
            return true;
        }
        if (superType == JSONItem) {
            return this == objectItem
                || this == arrayItem
                || this == JSONItem
                || this == nullItem;
        }

        if (superType == atomicItem) {
            return this == stringItem
                || this == integerItem
                || this == decimalItem
                || this == doubleItem
                || this == booleanItem;
        }

        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
