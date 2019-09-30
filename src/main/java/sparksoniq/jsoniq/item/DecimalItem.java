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


public class DecimalItem extends AtomicItem {


	private static final long serialVersionUID = 1L;
	private BigDecimal _value;

    public DecimalItem() {
        super();
    }

    public DecimalItem(BigDecimal decimal) {
        super();
        this._value = decimal;
    }

    public BigDecimal getValue() {
        return _value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return _value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getDecimalValue().equals(BigDecimal.ZERO);
    }

    public double castToDoubleValue() {
    	return getDecimalValue().doubleValue();
    }

    public BigDecimal castToDecimalValue() {
        return getDecimalValue();
    }

    public int castToIntegerValue() {
    	return getDecimalValue().intValue();
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DecimalItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType() != AtomicTypes.AtomicItem;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromDecimal(this);
    }

    @Override
    public AtomicItem createFromBoolean(BooleanItem booleanItem) {
        return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(booleanItem.hashCode()));
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createDecimalItem(new BigDecimal(stringItem.getStringValue()));
    }

    @Override
    public AtomicItem createFromInteger(IntegerItem integerItem) {
        return ItemFactory.getInstance().createDecimalItem(integerItem.castToDecimalValue());
    }

    @Override
    public AtomicItem createFromDecimal(DecimalItem decimalItem) {
        return decimalItem;
    }

    @Override
    public AtomicItem createFromDouble(DoubleItem doubleItem) {
        return ItemFactory.getInstance().createDecimalItem(doubleItem.castToDecimalValue());
    }

    @Override
    public String serialize() {
        return String.valueOf(_value.stripTrailingZeros().toPlainString());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, BigDecimal.class);
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch(IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        if (getDecimalValue().stripTrailingZeros().scale() == 0) {
            return getDecimalValue().intValue();
        }
        return getDecimalValue().hashCode();
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : this.getDecimalValue().compareTo(other.castToDecimalValue());
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
        return ItemFactory.getInstance().createDecimalItem(this.getDecimalValue().add(other.castToDecimalValue()));
    }

    @Override
    public Item subtract(Item other, boolean negated) {
        BigDecimal abs_result = this.getDecimalValue().subtract(other.castToDecimalValue());
        return ItemFactory.getInstance().createDecimalItem(negated ? abs_result.negate() : abs_result);
    }

    @Override
    public Item multiply(Item other) {
        if (other.isYearMonthDuration() || other.isDayTimeDuration()) return other.multiply(this);
        return ItemFactory.getInstance().createDecimalItem(this.getDecimalValue().multiply(other.castToDecimalValue()));
    }

    @Override
    public Item divide(Item other, boolean inverted) {
        BigDecimal result = inverted ? other.castToDecimalValue().divide(this.getDecimalValue(), 10, BigDecimal.ROUND_HALF_UP) :
                this.getDecimalValue().divide(other.castToDecimalValue(), 10, BigDecimal.ROUND_HALF_UP);
        return ItemFactory.getInstance().createDecimalItem(result);
    }

    @Override
    public Item modulo(Item other, boolean inverted) {
        BigDecimal result = inverted ? other.castToDecimalValue().remainder(this.getDecimalValue()) :
                this.getDecimalValue().remainder(other.castToDecimalValue());
        return ItemFactory.getInstance().createDecimalItem(result);
    }

    @Override
    public Item idivide(Item other, boolean inverted) {
        int result = inverted ? other.castToDecimalValue().divideToIntegralValue(this.getDecimalValue()).intValueExact() :
                this.getDecimalValue().divideToIntegralValue(other.castToDecimalValue()).intValueExact();
        return ItemFactory.getInstance().createIntegerItem(result);
    }
}
