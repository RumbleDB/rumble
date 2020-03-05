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
    private ItemTypes type;
    public static final ItemType objectItem = new ItemType(ItemTypes.ObjectItem);
    public static final ItemType atomicItem = new ItemType(ItemTypes.AtomicItem);
    public static final ItemType stringItem = new ItemType(ItemTypes.StringItem);
    public static final ItemType integerItem = new ItemType(ItemTypes.IntegerItem);
    public static final ItemType decimalItem = new ItemType(ItemTypes.DecimalItem);
    public static final ItemType doubleItem = new ItemType(ItemTypes.DoubleItem);
    public static final ItemType booleanItem = new ItemType(ItemTypes.BooleanItem);
    public static final ItemType arrayItem = new ItemType(ItemTypes.ArrayItem);
    public static final ItemType nullItem = new ItemType(ItemTypes.NullItem);
    public static final ItemType JSONItem = new ItemType(ItemTypes.JSONItem);
    public static final ItemType durationItem = new ItemType(ItemTypes.DurationItem);
    public static final ItemType yearMonthDurationItem = new ItemType(ItemTypes.YearMonthDurationItem);
    public static final ItemType dayTimeDurationItem = new ItemType(ItemTypes.DayTimeDurationItem);
    public static final ItemType dateTimeItem = new ItemType(ItemTypes.DateTimeItem);
    public static final ItemType dateItem = new ItemType(ItemTypes.DateItem);
    public static final ItemType timeItem = new ItemType(ItemTypes.TimeItem);
    public static final ItemType anyURIItem = new ItemType(ItemTypes.AnyURIItem);
    public static final ItemType hexBinaryItem = new ItemType(ItemTypes.HexBinaryItem);
    public static final ItemType base64BinaryItem = new ItemType(ItemTypes.Base64BinaryItem);
    public static final ItemType item = new ItemType(ItemTypes.Item);
    public static final ItemType functionItem = new ItemType(ItemTypes.FunctionItem);

    public ItemType() {
    }

    private ItemType(ItemTypes type) {
        this.type = type;

    }

    public ItemType(String text) {
        text = text.toLowerCase();
        switch (text) {
            case "object":
                this.type = ItemTypes.ObjectItem;
                return;
            case "atomic":
                this.type = ItemTypes.AtomicItem;
                return;
            case "string":
                this.type = ItemTypes.StringItem;
                return;
            case "integer":
                this.type = ItemTypes.IntegerItem;
                return;
            case "decimal":
                this.type = ItemTypes.DecimalItem;
                return;
            case "double":
                this.type = ItemTypes.DoubleItem;
                return;
            case "boolean":
                this.type = ItemTypes.BooleanItem;
                return;
            case "null":
                this.type = ItemTypes.NullItem;
                return;
            case "array":
                this.type = ItemTypes.ArrayItem;
                return;
            case "json-item":
                this.type = ItemTypes.JSONItem;
                return;
            case "duration":
                this.type = ItemTypes.DurationItem;
                return;
            case "yearmonthduration":
                this.type = ItemTypes.YearMonthDurationItem;
                return;
            case "daytimeduration":
                this.type = ItemTypes.DayTimeDurationItem;
                return;
            case "datetime":
                this.type = ItemTypes.DateTimeItem;
                return;
            case "date":
                this.type = ItemTypes.DateItem;
                return;
            case "time":
                this.type = ItemTypes.TimeItem;
                return;
            case "anyuri":
                this.type = ItemTypes.AnyURIItem;
                return;
            case "hexbinary":
                this.type = ItemTypes.HexBinaryItem;
                return;
            case "base64binary":
                this.type = ItemTypes.Base64BinaryItem;
                return;
            case "item":
                this.type = ItemTypes.Item;
                return;
            default:
                throw new OurBadException("Type unrecognized: " + text);
        }
    }


    public ItemTypes getType() {
        return this.type;
    }

    public boolean isSubtypeOf(ItemType superType) {
        if (superType.getType() == ItemTypes.Item) {
            return true;
        }
        if (superType.getType() == ItemTypes.JSONItem) {
            if (
                this.type == ItemTypes.ObjectItem
                    || this.type == ItemTypes.ArrayItem
                    || this.type == ItemTypes.JSONItem
                    || this.type == ItemTypes.NullItem
            ) {
                return true;
            }
            return false;
        }

        if (superType.getType() == ItemTypes.AtomicItem) {
            if (
                this.type == ItemTypes.StringItem
                    || this.type == ItemTypes.IntegerItem
                    || this.type == ItemTypes.DecimalItem
                    || this.type == ItemTypes.DoubleItem
                    || this.type == ItemTypes.BooleanItem
                    || this.type == ItemTypes.AnyURIItem
            ) {
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemType)) {
            return false;
        }
        ItemType itemType = (ItemType) o;
        return this.getType().equals(itemType.getType());
    }


    @Override
    public String toString() {
        switch (this.type) {
            case Item:
                return "item";
            case IntegerItem:
                return "integer";
            case DecimalItem:
                return "decimal";
            case DoubleItem:
                return "double";
            case AtomicItem:
                return "atomic";
            case StringItem:
                return "string";
            case BooleanItem:
                return "boolean";
            case AnyURIItem:
                return "anyuri";
            case NullItem:
                return "null";
            case JSONItem:
                return "json-item";
            case ArrayItem:
                return "array";
            case ObjectItem:
                return "object";
            case Base64BinaryItem:
                return "base64Binary";
            case HexBinaryItem:
                return "hexBinary";
            case DateItem:
                return "date";
            case DateTimeItem:
                return "dateTime";
            case DayTimeDurationItem:
                return "dayTimeDuration";
            case DurationItem:
                return "duration";
            case FunctionItem:
                return "function(*)";
            case TimeItem:
                return "time";
            case YearMonthDurationItem:
                return "yearMonthDuration";
        }
        throw new OurBadException("Unrecognized type.");
    }
}
