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
import org.rumbledb.exceptions.DivisionByZeroException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DoubleItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private double value;

    public DoubleItem() {
        super();
    }

    public DoubleItem(double value) {
        super();
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public double getDoubleValue() {
        return this.value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getDoubleValue() != 0;
    }

    public double castToDoubleValue() {
        return getDoubleValue();
    }

    public BigDecimal castToDecimalValue() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue())) {
            return super.castToDecimalValue();
        }
        return BigDecimal.valueOf(getDoubleValue());
    }

    public int castToIntValue() {
        return Double.valueOf(this.value).intValue();
    }

    public BigInteger castToIntegerValue() {
        return BigDecimal.valueOf(this.value).toBigInteger();
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.doubleItem) || super.isTypeOf(type);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(ItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(this.getDoubleValue() != 0);
        }
        if (itemType.equals(ItemType.doubleItem)) {
            return this;
        }
        if (itemType.equals(ItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue());
        }
        if (itemType.equals(ItemType.integerItem)) {
            return ItemFactory.getInstance().createIntegerItem(this.castToIntegerValue());
        }
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(String.valueOf(this.getDoubleValue()));
        }
        throw new ClassCastException();
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        if (itemType.equals(ItemType.atomicItem) || itemType.equals(ItemType.nullItem)) {
            return false;
        } else if (itemType.equals(ItemType.decimalItem)) {
            return !Double.isInfinite(this.getValue());
        } else if (itemType.equals(ItemType.integerItem)) {
            return !(Integer.MAX_VALUE < this.getValue()) && !(Integer.MIN_VALUE > this.getValue());
        }
        return true;
    }

    @Override
    public String serialize() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue())) {
            return String.valueOf(this.getDoubleValue());
        }
        boolean negativeZero = this.getDoubleValue() == 0 && String.valueOf(this.getDoubleValue()).charAt(0) == ('-');
        String doubleString = String.valueOf(this.castToDecimalValue().stripTrailingZeros().toPlainString());
        return negativeZero ? '-' + doubleString : doubleString;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeDouble(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readDouble();
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch (IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        return (int) Math.round(getDoubleValue());
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isNumeric()) {
            return Double.compare(this.getDoubleValue(), other.castToDoubleValue());
        }
        throw new OurBadException("Comparing an int to something that is not a number.");
    }

    @Override
    public Item compareItem(
            Item other,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        if (!other.isNumeric() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "Invalid args for numerics comparison "
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
    public Item add(Item other) {
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() + other.castToDoubleValue());
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() - other.castToDoubleValue());
    }

    @Override
    public Item multiply(Item other) {
        if (other.isYearMonthDuration() || other.isDayTimeDuration()) {
            return other.multiply(this);
        }
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() * other.castToDoubleValue());
    }

    @Override
    public Item divide(Item other) {
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() / other.castToDoubleValue());
    }

    @Override
    public Item modulo(Item other) {
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() % other.castToDoubleValue());
    }

    @Override
    public Item idivide(Item other) {
        return ItemFactory.getInstance().createIntItem((int) (this.getDoubleValue() / other.castToDoubleValue()));
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.doubleItem;
    }
}
