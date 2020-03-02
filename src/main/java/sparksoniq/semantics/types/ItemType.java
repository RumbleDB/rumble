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

package sparksoniq.semantics.types;


import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;

public class ItemType implements Serializable {


    private static final long serialVersionUID = 1L;
    private ItemTypes type;

    public ItemType() {
    }

    public ItemType(ItemTypes type) {
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
        switch (type) {
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
            case NullItem:
                return "null";

            case JSONItem:
                return "json-item";
            case ArrayItem:
                return "array";
            case ObjectItem:
                return "object";
            default:
                return "item";
        }
    }
}
