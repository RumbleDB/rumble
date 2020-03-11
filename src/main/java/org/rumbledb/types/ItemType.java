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

public enum ItemType {

    objectItem("object"),
    atomicItem("atomic"),
    stringItem("string"),
    integerItem("integer"),
    decimalItem("decimal"),
    doubleItem("double"),
    booleanItem("boolean"),
    arrayItem("array"),
    nullItem("null"),
    JSONItem("json-item"),
    durationItem("duration"),
    yearMonthDurationItem("yearMonthDuration"),
    dayTimeDurationItem("dayTimeDuration"),
    dateTimeItem("dateTime"),
    dateItem("date"),
    timeItem("time"),
    hexBinaryItem("hexBinary"),
    base64BinaryItem("base64Binary"),
    item("item"),
    functionItem("function");

    private final String name;

    ItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
