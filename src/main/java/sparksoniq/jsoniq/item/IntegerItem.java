/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

import org.rumbledb.api.Item;

public class IntegerItem extends AtomicItem {


	private static final long serialVersionUID = 1L;
	private int _value;

    public IntegerItem() {
        super();
    }

    public IntegerItem(int value) {
        super();
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    @Override
    public int getIntegerValue() {
        return _value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getIntegerValue() != 0;
    }

    public double castToDoubleValue() {
    	return new Integer(getIntegerValue()).doubleValue();
    }

    public BigDecimal castToDecimalValue() {
        return BigDecimal.valueOf(getIntegerValue());
    }

    public int castToIntegerValue() {
    	return getIntegerValue();
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.IntegerItem) || type.getType().equals(ItemTypes.DecimalItem)
                || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType() != AtomicTypes.AtomicItem;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromInteger(this);
    }

    @Override
    public AtomicItem createFromBoolean(BooleanItem booleanItem) {
        return ItemFactory.getInstance().createIntegerItem(booleanItem.hashCode());
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(stringItem.getStringValue()));
    }

    @Override
    public AtomicItem createFromInteger(IntegerItem integerItem) {
        return integerItem;
    }

    @Override
    public AtomicItem createFromDecimal(DecimalItem decimalItem) {
        return ItemFactory.getInstance().createIntegerItem(decimalItem.castToIntegerValue());
    }

    @Override
    public AtomicItem createFromDouble(DoubleItem doubleItem) {
        return ItemFactory.getInstance().createIntegerItem(doubleItem.castToIntegerValue());
    }

    @Override
    public String serialize() {
        return String.valueOf(_value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readInt();
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch(IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        return getIntegerValue();
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : Integer.compare(this.getIntegerValue(), other.castToIntegerValue());
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isNumeric() && !other.isNull()) {
            throw new UnexpectedTypeException("Invalid args for numerics comparison " + this.serialize() +
                    ", " + other.serialize(), metadata);
        }
        return operator.apply(this, other);
    }


    @Override
    public Item add(Item other) {
        if (other.isDouble() || other.isDecimal()) return other.add(this);
        return ItemFactory.getInstance().createIntegerItem(this.getIntegerValue() + other.castToIntegerValue());
    }

    @Override
    public Item subtract(Item other, boolean inverted) {
        if (other.isDouble() || other.isDecimal()) return other.subtract(this, true);
        return ItemFactory.getInstance().createIntegerItem(this.getIntegerValue() - other.castToIntegerValue());
    }

    @Override
    public Item multiply(Item other) {
        if (other.isDouble() || other.isDecimal() || other.isYearMonthDuration() || other.isDayTimeDuration()) return other.multiply(this);
        return ItemFactory.getInstance().createIntegerItem(this.getIntegerValue() * other.castToIntegerValue());
    }

    @Override
    public Item divide(Item other, boolean inverted) {
        if (other.isDouble() || other.isDecimal()) return other.divide(this, true);
        BigDecimal bdResult = this.castToDecimalValue().divide(other.castToDecimalValue(), 10, BigDecimal.ROUND_HALF_UP);
        if (bdResult.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance().createIntegerItem(bdResult.intValueExact());
        } else {
            return ItemFactory.getInstance().createDecimalItem(bdResult);
        }
    }

    @Override
    public Item modulo(Item other, boolean inverted) {
        if (other.isDouble() || other.isDecimal()) return other.modulo(this, true);
        return ItemFactory.getInstance().createIntegerItem(this.getIntegerValue() % other.castToIntegerValue());
    }

    @Override
    public Item idivide(Item other, boolean inverted) {
        if (other.isDouble() || other.isDecimal()) return other.idivide(this, true);
        return ItemFactory.getInstance().createIntegerItem(this.getIntegerValue() / other.castToIntegerValue());
    }
}
