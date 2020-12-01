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

package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;

public class StringItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private String value;

    public StringItem() {
        super();
    }

    public StringItem(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.getValue();
    }

    public double castToDoubleValue() {
        return Double.parseDouble(this.getValue());
    }

    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.getValue());
    }

    public BigInteger castToIntegerValue() {
        return new BigInteger(this.getValue());
    }

    public int castToIntValue() {
        return Integer.parseInt(this.getValue());
    }

    private boolean isBooleanLiteral(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    private boolean isNullLiteral(String value) {
        return "null".equals(value);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(AtomicItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(Boolean.parseBoolean(this.getStringValue()));
        }
        if (itemType.equals(AtomicItemType.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(this.getStringValue()));
        }
        if (itemType.equals(AtomicItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(new BigDecimal(this.getStringValue()));
        }
        if (itemType.equals(AtomicItemType.integerItem)) {
            return ItemFactory.getInstance().createIntegerItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.nullItem)) {
            return ItemFactory.getInstance().createNullItem();
        }
        if (itemType.equals(AtomicItemType.durationItem)) {
            return ItemFactory.getInstance()
                .createDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), AtomicItemType.durationItem)
                );
        }
        if (itemType.equals(AtomicItemType.yearMonthDurationItem)) {
            return ItemFactory.getInstance()
                .createYearMonthDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), AtomicItemType.yearMonthDurationItem)
                );
        }
        if (itemType.equals(AtomicItemType.dayTimeDurationItem)) {
            return ItemFactory.getInstance()
                .createDayTimeDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), AtomicItemType.dayTimeDurationItem)
                );
        }
        if (itemType.equals(AtomicItemType.dateTimeItem)) {
            return ItemFactory.getInstance().createDateTimeItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.dateItem)) {
            return ItemFactory.getInstance().createDateItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.timeItem)) {
            return ItemFactory.getInstance().createTimeItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.hexBinaryItem)) {
            return ItemFactory.getInstance().createHexBinaryItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.base64BinaryItem)) {
            return ItemFactory.getInstance().createBase64BinaryItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.anyURIItem)) {
            return ItemFactory.getInstance().createAnyURIItem(this.getStringValue());
        }
        if (itemType.equals(AtomicItemType.stringItem)) {
            return this;
        }
        throw new ClassCastException();
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(AtomicItemType.stringItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        if (itemType.equals(AtomicItemType.stringItem)) {
            return true;
        }
        try {
            if (itemType.equals(AtomicItemType.integerItem)) {
                Integer.parseInt(this.getValue());
            } else if (itemType.equals(AtomicItemType.anyURIItem)) {
                AnyURIItem.parseAnyURIString(this.getValue());
            } else if (itemType.equals(AtomicItemType.decimalItem)) {
                if (this.getValue().contains("e") || this.getValue().contains("E")) {
                    return false;
                }
                Float.parseFloat(this.getValue());
            } else if (itemType.equals(AtomicItemType.doubleItem)) {
                Double.parseDouble(this.getValue());
            } else if (itemType.equals(AtomicItemType.nullItem)) {
                return isNullLiteral(this.getValue());
            } else if (itemType.equals(AtomicItemType.durationItem)) {
                DurationItem.getDurationFromString(this.value, AtomicItemType.durationItem);
            } else if (itemType.equals(AtomicItemType.yearMonthDurationItem)) {
                DurationItem.getDurationFromString(this.getValue(), AtomicItemType.yearMonthDurationItem);
            } else if (itemType.equals(AtomicItemType.dayTimeDurationItem)) {
                DurationItem.getDurationFromString(this.getValue(), AtomicItemType.dayTimeDurationItem);
            } else if (itemType.equals(AtomicItemType.dateTimeItem)) {
                DateTimeItem.parseDateTime(this.getValue(), AtomicItemType.dateTimeItem);
            } else if (itemType.equals(AtomicItemType.dateItem)) {
                DateTimeItem.parseDateTime(this.getValue(), AtomicItemType.dateItem);
            } else if (itemType.equals(AtomicItemType.timeItem)) {
                DateTimeItem.parseDateTime(this.getValue(), AtomicItemType.timeItem);
            } else if (itemType.equals(AtomicItemType.hexBinaryItem)) {
                HexBinaryItem.parseHexBinaryString(this.getValue());
            } else if (itemType.equals(AtomicItemType.base64BinaryItem)) {
                Base64BinaryItem.parseBase64BinaryString(this.getValue());
            } else {
                return isBooleanLiteral(this.getValue());
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public String serialize() {
        return this.getValue();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readString();
    }

    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isString()) {
            return false;
        }
        return (getStringValue().equals(o.getStringValue()));
    }

    public int hashCode() {
        return getStringValue().hashCode();
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : this.getStringValue().compareTo(other.getStringValue());
    }

    @Override
    public Item compareItem(
            Item other,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        if (!other.isString() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "Invalid args for string comparison "
                        + this.serialize()
                        +
                        ", "
                        + other.serialize(),
                    metadata
            );
        }
        return super.compareItem(other, comparisonOperator, metadata);
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.stringItem;
    }
}
