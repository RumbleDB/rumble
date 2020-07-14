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

public class IntegerItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private BigInteger value;

    public IntegerItem() {
        super();
    }

    public IntegerItem(BigInteger value) {
        super();
        this.value = value;
    }

    @Override
    public BigInteger getIntegerValue() {
        return this.value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getIntValue() != 0;
    }

    @Override
    public double castToDoubleValue() {
        return new Integer(getIntValue()).doubleValue();
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.value);
    }

    @Override
    public BigInteger castToIntegerValue() {
        return this.value;
    }

    @Override
    public boolean isInteger() {
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
            return ItemFactory.getInstance().createBooleanItem(this.getIntValue() != 0);
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
            return ItemFactory.getInstance().createStringItem(String.valueOf(this.getIntValue()));
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
        kryo.writeObject(output, this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = kryo.readObject(input, BigInteger.class);
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
        if (other instanceof IntegerItem || other instanceof IntItem) {
            return this.value.compareTo(other.castToIntegerValue());
        }
        throw new OurBadException("Comparing a big integer to something that is not an integer.");
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
        if (other.isDecimal()) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue().add(other.getDecimalValue()));
        }
        return ItemFactory.getInstance()
            .createIntegerItem(this.getIntegerValue().add(other.castToIntegerValue()));
    }

    @Override
    public Item subtract(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() - other.getDoubleValue());
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().subtract(other.getDecimalValue()));
        }
        return ItemFactory.getInstance()
            .createIntegerItem(this.getIntegerValue().subtract(other.castToIntegerValue()));
    }

    @Override
    public Item multiply(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() * other.getDoubleValue());
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().multiply(other.getDecimalValue()));
        }
        if (other.isYearMonthDuration()) {
            return ItemFactory.getInstance()
                .createYearMonthDurationItem(other.getDurationValue().multipliedBy(this.getIntValue()));
        }
        if (other.isDayTimeDuration()) {
            return ItemFactory.getInstance()
                .createDayTimeDurationItem(other.getDurationValue().multipliedBy(this.getIntValue()));
        }
        return ItemFactory.getInstance()
            .createIntegerItem(this.getIntegerValue().multiply(other.castToIntegerValue()));
    }

    @Override
    public Item divide(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() / other.getDoubleValue());
        }
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        BigDecimal bdResult = this.castToDecimalValue()
            .divide(other.castToDecimalValue(), 10, BigDecimal.ROUND_HALF_UP);
        if (bdResult.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance().createIntegerItem(bdResult.toBigIntegerExact());
        } else {
            return ItemFactory.getInstance().createDecimalItem(bdResult);
        }
    }

    @Override
    public Item modulo(Item other) {
        if (other.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue() % other.getDoubleValue());
        }
        if (other.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.castToDecimalValue().remainder(other.getDecimalValue()));
        }
        if (other.equals(ItemFactory.getInstance().createIntItem(0))) {
            throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
        }
        return ItemFactory.getInstance()
            .createIntegerItem(this.getIntegerValue().mod(other.castToIntegerValue()));
    }

    @Override
    public Item idivide(Item other) {
        return ItemFactory.getInstance()
            .createIntegerItem(this.getIntegerValue().divide(other.castToIntegerValue()));
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.integerItem;
    }
}
