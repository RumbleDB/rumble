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
        if (itemType.equals(ItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(Boolean.parseBoolean(this.getStringValue()));
        }
        if (itemType.equals(ItemType.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(this.getStringValue()));
        }
        if (itemType.equals(ItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(new BigDecimal(this.getStringValue()));
        }
        if (itemType.equals(ItemType.integerItem)) {
            return ItemFactory.getInstance().createIntegerItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.nullItem)) {
            return ItemFactory.getInstance().createNullItem();
        }
        if (itemType.equals(ItemType.durationItem)) {
            return ItemFactory.getInstance()
                .createDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), ItemType.durationItem)
                );
        }
        if (itemType.equals(ItemType.yearMonthDurationItem)) {
            return ItemFactory.getInstance()
                .createYearMonthDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), ItemType.yearMonthDurationItem)
                );
        }
        if (itemType.equals(ItemType.dayTimeDurationItem)) {
            return ItemFactory.getInstance()
                .createDayTimeDurationItem(
                    DurationItem.getDurationFromString(this.getStringValue(), ItemType.dayTimeDurationItem)
                );
        }
        if (itemType.equals(ItemType.dateTimeItem)) {
            return ItemFactory.getInstance().createDateTimeItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.dateItem)) {
            return ItemFactory.getInstance().createDateItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.timeItem)) {
            return ItemFactory.getInstance().createTimeItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.hexBinaryItem)) {
            return ItemFactory.getInstance().createHexBinaryItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.base64BinaryItem)) {
            return ItemFactory.getInstance().createBase64BinaryItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.anyURIItem)) {
            return ItemFactory.getInstance().createAnyURIItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.stringItem)) {
            return this;
        }
        throw new ClassCastException();
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.stringItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        if (itemType.equals(ItemType.stringItem)) {
            return true;
        }
        try {
            if (itemType.equals(ItemType.integerItem)) {
                Integer.parseInt(this.getValue());
            } else if (itemType.equals(ItemType.anyURIItem)) {
                AnyURIItem.parseAnyURIString(this.getValue());
            } else if (itemType.equals(ItemType.decimalItem)) {
                if (this.getValue().contains("e") || this.getValue().contains("E")) {
                    return false;
                }
                Float.parseFloat(this.getValue());
            } else if (itemType.equals(ItemType.doubleItem)) {
                Double.parseDouble(this.getValue());
            } else if (itemType.equals(ItemType.nullItem)) {
                return isNullLiteral(this.getValue());
            } else if (itemType.equals(ItemType.durationItem)) {
                DurationItem.getDurationFromString(this.value, ItemType.durationItem);
            } else if (itemType.equals(ItemType.yearMonthDurationItem)) {
                DurationItem.getDurationFromString(this.getValue(), ItemType.yearMonthDurationItem);
            } else if (itemType.equals(ItemType.dayTimeDurationItem)) {
                DurationItem.getDurationFromString(this.getValue(), ItemType.dayTimeDurationItem);
            } else if (itemType.equals(ItemType.dateTimeItem)) {
                DateTimeItem.parseDateTime(this.getValue(), ItemType.dateTimeItem);
            } else if (itemType.equals(ItemType.dateItem)) {
                DateTimeItem.parseDateTime(this.getValue(), ItemType.dateItem);
            } else if (itemType.equals(ItemType.timeItem)) {
                DateTimeItem.parseDateTime(this.getValue(), ItemType.timeItem);
            } else if (itemType.equals(ItemType.hexBinaryItem)) {
                HexBinaryItem.parseHexBinaryString(this.getValue());
            } else if (itemType.equals(ItemType.base64BinaryItem)) {
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
        return ItemType.stringItem;
    }
}
