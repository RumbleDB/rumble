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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class FloatItem implements Item {

    private static final long serialVersionUID = 1L;
    private float value;

    public FloatItem() {
    }

    public FloatItem(float value) {
        this.value = value;
    }

    @Override
    public float getFloatValue() {
        return this.value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.value != 0;
    }

    @Override
    public double castToDoubleValue() {
        return (double) this.value;
    }

    @Override
    public float castToFloatValue() {
        return this.value;
    }

    public BigDecimal castToDecimalValue() {
        if (Float.isNaN(this.value) || Float.isInfinite(this.value)) {
            throw new IteratorFlowException("Cannot call castToDecimal on non numeric");
        }
        return BigDecimal.valueOf(this.value);
    }

    public int castToIntValue() {
        return Float.valueOf(this.value).intValue();
    }

    public BigInteger castToIntegerValue() {
        return BigDecimal.valueOf(this.value).toBigInteger();
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(ItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(this.value != 0);
        }
        if (itemType.equals(ItemType.floatItem)) {
            return this;
        }
        if (itemType.equals(ItemType.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue());
        }
        if (itemType.equals(ItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue());
        }
        if (itemType.equals(ItemType.integerItem)) {
            return ItemFactory.getInstance().createIntegerItem(this.castToIntegerValue());
        }
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(serialize());
        }
        throw new ClassCastException();
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        if (itemType.equals(ItemType.atomicItem) || itemType.equals(ItemType.nullItem)) {
            return false;
        } else if (itemType.equals(ItemType.decimalItem)) {
            return !Float.isInfinite(this.value) && !Float.isNaN(this.value);
        } else if (itemType.equals(ItemType.integerItem)) {
            return !(Integer.MAX_VALUE < this.value) && !(Integer.MIN_VALUE > this.value);
        }
        return true;
    }

    @Override
    public String serialize() {
        if (Float.isNaN(this.value)) {
            return "NaN";
        }
        if (Float.isInfinite(this.value) && this.value > 0) {
            return "INF";
        }
        if (Float.isInfinite(this.value) && this.value < 0) {
            return "-INF";
        }
        if (Float.compare(this.value, 0f) == 0) {
            return "0";
        }
        if (Float.compare(this.value, -0f) == 0) {
            return "-0";
        }
        double abs = Math.abs(this.value);
        if (abs >= 0.000001 && abs <= 1000000) {
            return this.castToDecimalValue().stripTrailingZeros().toPlainString();
        }
        return Float.toString(this.value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeFloat(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readFloat();
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch (IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        return (int) Math.round(this.value);
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isNumeric()) {
            return Float.compare(this.value, other.castToFloatValue());
        }
        throw new OurBadException("Comparing a float to something that is not a number.");
    }

    @Override
    public Item add(Item other) {
        if (other.isDouble()) {
            return other.add(this);
        }
        return ItemFactory.getInstance().createFloatItem(this.value + other.castToFloatValue());
    }

    @Override
    public Item subtract(Item other) {
        if (other.isDouble()) {
            return other.subtract(this);
        }
        return ItemFactory.getInstance().createFloatItem(this.value - other.castToFloatValue());
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.floatItem;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public Item promoteTo(ItemType type) {
        return this.castAs(type);
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
    public boolean isDecimal() {
        return false;
    }

    @Override
    public boolean isInteger() {
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
    public boolean isString() {
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
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Item getItemAt(int position) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public List<String> getKeys() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public List<Item> getValues() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Item getItemByKey(String key) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public int getSize() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public boolean getBooleanValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public int getIntValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public BigInteger getIntegerValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public BigDecimal getDecimalValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public double getDoubleValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Period getDurationValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public DateTime getDateTimeValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public byte[] getBinaryValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public List<Name> getParameterNames() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public FunctionSignature getSignature() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
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
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public void append(Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public void putItemByKey(String key, Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public boolean canBePromotedTo(ItemType type) {
        return InstanceOfIterator.doesItemTypeMatchItem(type, this);
    }

    @Override
    public RuntimeIterator getBodyIterator() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public Map<Name, Dataset<Row>> getDFVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public DynamicContext getDynamicModuleContext() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }

    @Override
    public String getStringValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a float!");
    }
}
