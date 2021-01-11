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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.ComparisonOperationIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class StringItem implements Item {


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
        return this.value;
    }

    public double castToDoubleValue() {
        if (this.value.equals("INF") || this.value.equals("+INF")) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.value.equals("-INF")) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.value.equals("NaN")) {
            return Double.NaN;
        }
        return Double.parseDouble(this.getValue());
    }

    public float castToFloatValue() {
        if (this.value.equals("INF") || this.value.equals("+INF")) {
            return Float.POSITIVE_INFINITY;
        }
        if (this.value.equals("-INF")) {
            return Float.NEGATIVE_INFINITY;
        }
        if (this.value.equals("NaN")) {
            return Float.NaN;
        }
        return Float.parseFloat(this.getValue());
    }

    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.value);
    }

    public BigInteger castToIntegerValue() {
        return new BigInteger(this.value);
    }

    public int castToIntValue() {
        return Integer.parseInt(this.value);
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

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        if (itemType.equals(ItemType.stringItem)) {
            return true;
        }
        try {
            if (itemType.equals(ItemType.integerItem)) {
                @SuppressWarnings("unused")
                BigInteger i = new BigInteger(this.value);
                return true;
            } else if (itemType.equals(ItemType.anyURIItem)) {
                AnyURIItem.parseAnyURIString(this.getValue());
            } else if (itemType.equals(ItemType.decimalItem)) {
                if (this.getValue().contains("e") || this.getValue().contains("E")) {
                    return false;
                }
                Float.parseFloat(this.getValue());
            } else if (itemType.equals(ItemType.doubleItem)) {
                if (
                    this.value.equals("INF")
                        || this.value.equals("+INF")
                        || this.value.equals("-INF")
                        || this.value.equals("NaN")
                ) {
                    return true;
                }
                Double.parseDouble(this.getValue());
            } else if (itemType.equals(ItemType.floatItem)) {
                if (
                    this.value.equals("INF")
                        || this.value.equals("+INF")
                        || this.value.equals("-INF")
                        || this.value.equals("NaN")
                ) {
                    return true;
                }
                Float.parseFloat(this.getValue());
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

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            int c = ComparisonOperationIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public int hashCode() {
        return getStringValue().hashCode();
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.stringItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isDecimal() {
        return false;
    }

    @Override
    public boolean isInteger() {
        return false;
    }

    @Override
    public boolean isFloat() {
        return false;
    }

    @Override
    public boolean isInt() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isDuration() {
        return false;
    }

    @Override
    public boolean isYearMonthDuration() {
        return false;
    }

    @Override
    public boolean isDayTimeDuration() {
        return false;
    }

    @Override
    public boolean isDateTime() {
        return false;
    }

    @Override
    public boolean isDate() {
        return false;
    }

    @Override
    public boolean isTime() {
        return false;
    }

    @Override
    public boolean isAnyURI() {
        return false;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isHexBinary() {
        return false;
    }

    @Override
    public boolean isBase64Binary() {
        return false;
    }

    @Override
    public List<Item> getItems() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Item getItemAt(int position) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public List<String> getKeys() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public List<Item> getValues() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Item getItemByKey(String key) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public int getSize() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public boolean getBooleanValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public double getDoubleValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public float getFloatValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public int getIntValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public BigInteger getIntegerValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public BigDecimal getDecimalValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Period getDurationValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public DateTime getDateTimeValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public byte[] getBinaryValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public List<Name> getParameterNames() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public FunctionSignature getSignature() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public boolean hasTimeZone() {
        return false;
    }

    @Override
    public boolean hasDateTime() {
        return false;
    }

    @Override
    public void putItem(Item item) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public void append(Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public void putItemByKey(String key, Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public boolean canBePromotedTo(ItemType type) {
        return InstanceOfIterator.doesItemTypeMatchItem(type, this);
    }

    @Override
    public RuntimeIterator getBodyIterator() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public Map<Name, Dataset<Row>> getDFVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }

    @Override
    public DynamicContext getDynamicModuleContext() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a string!");
    }
}
