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

public class IntItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private int value;

    public IntItem() {
        super();
    }

    public IntItem(int value) {
        super();
        this.value = value;
    }

    @Override
    public int getIntValue() {
        return this.value;
    }

    @Override
    public BigInteger getIntegerValue() {
        return BigInteger.valueOf(this.value);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.value != 0;
    }

    public double castToDoubleValue() {
        return new Integer(this.value).doubleValue();
    }

    public BigDecimal castToDecimalValue() {
        return BigDecimal.valueOf(this.value);
    }

    public BigInteger castToIntegerValue() {
        return BigInteger.valueOf(this.value);
    }

    public int castToIntValue() {
        return this.value;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.integerItem)
            || type.equals(ItemType.decimalItem)
            || super.isTypeOf(type);
    }

    @Override
    public boolean canBePromotedTo(ItemType type) {
        return type.equals(ItemType.doubleItem) || super.canBePromotedTo(type);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(ItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(this.value != 0);
        }
        if (itemType.equals(ItemType.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue());
        }
        if (itemType.equals(ItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue());
        }
        if (itemType.equals(ItemType.integerItem)) {
            return this;
        }
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(String.valueOf(this.value));
        }
        throw new ClassCastException();
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return !itemType.equals(ItemType.atomicItem)
            &&
            !itemType.equals(ItemType.nullItem);
    }

    @Override
    public String serialize() {
        return String.valueOf(this.value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readInt();
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch (IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        return getIntValue();
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isInt()) {
            return Integer.compare(this.value, other.getIntValue());
        }
        if (other.isInteger()) {
            return this.castToIntegerValue().compareTo(other.getIntegerValue());
        }
        if (other.isDecimal()) {
            return this.castToDecimalValue().compareTo(other.getDecimalValue());
        }
        if (other.isDouble()) {
            return Double.compare(this.castToDoubleValue(), other.getDoubleValue());
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
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() + other.getDoubleValue());
        }
        if (
            other.isInt()
                && (this.value < Integer.MAX_VALUE / 2
                    && this.value > -Integer.MAX_VALUE / 2
                    && other.getIntValue() > -Integer.MAX_VALUE / 2
                    && other.getIntValue() < Integer.MAX_VALUE / 2)
        ) {
            return ItemFactory.getInstance().createIntItem(this.value + other.castToIntValue());
        }
        if (other.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToIntegerValue().add(other.getIntegerValue()));
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue().add(other.getDecimalValue()));
        }
        throw new OurBadException("Unexpected type encountered");
    }

    @Override
    public Item subtract(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() - other.getDoubleValue());
        }
        if (
            other.isInt()
                && (this.value < Integer.MAX_VALUE / 2
                    && this.value > -Integer.MAX_VALUE / 2
                    && other.getIntValue() > -Integer.MAX_VALUE / 2
                    && other.getIntValue() < Integer.MAX_VALUE / 2)
        ) {
            return ItemFactory.getInstance().createIntItem(this.value - other.castToIntValue());
        }
        if (other.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToIntegerValue().subtract(other.getIntegerValue()));
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().subtract(other.getDecimalValue()));
        }
        throw new OurBadException("Unexpected type encountered");
    }

    @Override
    public Item multiply(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() * other.getDoubleValue());
        }
        if (
            other.isInt()
                && (this.value < Short.MAX_VALUE
                    && this.value > -Short.MAX_VALUE
                    && other.getIntValue() < Short.MAX_VALUE
                    && other.getIntValue() > -Short.MAX_VALUE)
        ) {
            return ItemFactory.getInstance().createIntItem(this.value * other.castToIntValue());
        }
        if (other.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToIntegerValue().multiply(other.getIntegerValue()));
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().multiply(other.getDecimalValue()));
        }
        if (other.isYearMonthDuration()) {
            return ItemFactory.getInstance()
                .createYearMonthDurationItem(other.getDurationValue().multipliedBy(this.value));
        }
        if (other.isDayTimeDuration()) {
            return ItemFactory.getInstance()
                .createDayTimeDurationItem(other.getDurationValue().multipliedBy(this.value));
        }
        throw new OurBadException("Unexpected type encountered");
    }

    @Override
    public Item divide(Item other) {
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() / other.getDoubleValue());
        }
        BigDecimal bdResult = this.castToDecimalValue()
            .divide(other.castToDecimalValue(), 10, BigDecimal.ROUND_HALF_UP);
        if (bdResult.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance().createIntItem(bdResult.intValueExact());
        } else {
            return ItemFactory.getInstance().createDecimalItem(bdResult);
        }
    }

    @Override
    public Item modulo(Item other) {
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() % other.getDoubleValue());
        }
        if (other.isInt()) {
            return ItemFactory.getInstance().createIntItem(this.value % other.castToIntValue());
        }
        if (other.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToIntegerValue().mod(other.getIntegerValue()));
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().remainder(other.getDecimalValue()));
        }
        throw new OurBadException("Unexpected type encountered");
    }

    @Override
    public Item idivide(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance()
                .createDoubleItem((double) (long) (this.castToDoubleValue() / other.getDoubleValue()));
        }
        if (other.isInt()) {
            return ItemFactory.getInstance().createIntItem(this.value / other.getIntValue());
        }
        if (other.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToIntegerValue().divide(other.getIntegerValue()));
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createIntegerItem(this.castToDecimalValue().divide(other.getDecimalValue()).toBigInteger());
        }
        throw new OurBadException("Unexpected type encountered");
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.integerItem;
    }
}
