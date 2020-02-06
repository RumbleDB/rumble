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

package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;

import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

public class DoubleItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private double _value;

    public DoubleItem() {
        super();
    }

    public DoubleItem(double value) {
        super();
        this._value = value;
    }

    public double getValue() {
        return _value;
    }

    @Override
    public double getDoubleValue() {
        return _value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getDoubleValue() != 0;
    }

    public double castToDoubleValue() {
        return getDoubleValue();
    }

    public BigDecimal castToDecimalValue() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue()))
            return super.castToDecimalValue();
        return BigDecimal.valueOf(getDoubleValue());
    }

    public int castToIntegerValue() {
        return new Double(getDoubleValue()).intValue();
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DoubleItem) || super.isTypeOf(type);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case BooleanItem:
                return ItemFactory.getInstance().createBooleanItem(this.getDoubleValue() != 0);
            case DoubleItem:
                return this;
            case DecimalItem:
                return ItemFactory.getInstance().createDecimalItem(this.castToDecimalValue());
            case IntegerItem:
                return ItemFactory.getInstance().createIntegerItem(this.castToIntegerValue());
            case StringItem:
                return ItemFactory.getInstance().createStringItem(String.valueOf(this.getDoubleValue()));
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        if (itemType == AtomicTypes.AtomicItem || itemType == AtomicTypes.NullItem)
            return false;
        else if (itemType == AtomicTypes.DecimalItem) {
            return !Double.isInfinite(this.getValue());
        } else if (itemType == AtomicTypes.IntegerItem) {
            return !(Integer.MAX_VALUE < this.getValue()) && !(Integer.MIN_VALUE > this.getValue());
        }
        return true;
    }

    @Override
    public String serialize() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue()))
            return String.valueOf(this.getDoubleValue());
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
        this._value = input.readDouble();
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
        return other.isNull() ? 1 : Double.compare(this.getDoubleValue(), other.castToDoubleValue());
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, ExceptionMetadata metadata) {
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
        return operator.apply(this, other);
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
        if (other.isYearMonthDuration() || other.isDayTimeDuration())
            return other.multiply(this);
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() * other.castToDoubleValue());
    }

    @Override
    public Item divide(Item other) {
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() / other.castToDoubleValue());
    }

    @Override
    public Item modulo(Item other) {
        return ItemFactory.getInstance().createDoubleItem(this.getDoubleValue() % other.castToDoubleValue());
    }

    @Override
    public Item idivide(Item other) {
        return ItemFactory.getInstance().createIntegerItem((int) (this.getDoubleValue() / other.castToDoubleValue()));
    }
}
