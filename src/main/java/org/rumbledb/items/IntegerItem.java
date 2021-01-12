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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.AtomicItemType;
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
    public BigDecimal getDecimalValue() {
        return new BigDecimal(this.value);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.value.equals(BigInteger.ZERO);
    }

    @Override
    public double castToDoubleValue() {
        return this.value.doubleValue();
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
    public int castToIntValue() {
        return this.value.intValue();
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(AtomicItemType.integerItem)
            || type.equals(AtomicItemType.decimalItem)
            || super.isTypeOf(type);
    }

    @Override
    public boolean canBePromotedTo(ItemType type) {
        return type.equals(AtomicItemType.doubleItem) || super.canBePromotedTo(type);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(AtomicItemType.booleanItem)) {
            return ItemFactory.getInstance().createBooleanItem(!this.value.equals(BigInteger.ZERO));
        }
        if (itemType.equals(AtomicItemType.doubleItem)) {
            return ItemFactory.getInstance().createDoubleItem(this.castToDoubleValue());
        }
        if (itemType.equals(AtomicItemType.decimalItem)) {
            return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue());
        }
        if (itemType.equals(AtomicItemType.integerItem)) {
            return this;
        }
        if (itemType.equals(AtomicItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(serialize());
        }
        throw new ClassCastException();
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return !itemType.equals(AtomicItemType.atomicItem)
            &&
            !itemType.equals(AtomicItemType.nullItem);
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
        return this.value.hashCode();
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isInteger()) {
            return this.value.compareTo(other.getIntegerValue());
        }
        if (other.isDecimal()) {
            return this.castToDecimalValue().compareTo(other.getDecimalValue());
        }
        if (other.isDouble()) {
            return Double.compare(this.castToDoubleValue(), other.getDoubleValue());
        }
        throw new OurBadException("Comparing an integer to something that is not a number.");
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
            .createIntegerItem(this.value.add(other.castToIntegerValue()));
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
            .createIntegerItem(this.value.subtract(other.castToIntegerValue()));
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.integerItem;
    }

    @Override
    public String getSparkSqlQuery() {
        return this.value.toString();
    }

    @Override
    public boolean isNumeric() {
        return true;
    }
}
