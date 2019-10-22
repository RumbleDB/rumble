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

import java.math.BigDecimal;

import org.rumbledb.api.Item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

public class StringItem extends AtomicItem {


	private static final long serialVersionUID = 1L;
	private String _value;

    public StringItem() {
        super();
    }

    public StringItem(String value) {
        super();
        this._value = value;
    }

    public String getValue() {
        return _value;
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

    public int castToIntegerValue() {
        return Integer.parseInt(this.getValue());
    }

    private boolean isBooleanLiteral(String value) { return "true".equals(value) || "false".equals(value); }

    private boolean isNullLiteral(String value) { return "null".equals(value); }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case BooleanItem:
                return ItemFactory.getInstance().createBooleanItem(Boolean.parseBoolean(this.getStringValue()));
            case DoubleItem:
                return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(this.getStringValue()));
            case DecimalItem:
                return ItemFactory.getInstance().createDecimalItem(new BigDecimal(this.getStringValue()));
            case IntegerItem:
                return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(this.getStringValue()));
            case NullItem:
                return ItemFactory.getInstance().createNullItem();
            case DurationItem:
                return ItemFactory.getInstance().createDurationItem(DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.DurationItem));
            case YearMonthDurationItem:
                return ItemFactory.getInstance().createYearMonthDurationItem(DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.YearMonthDurationItem));
            case DayTimeDurationItem:
                return ItemFactory.getInstance().createDayTimeDurationItem(DurationItem.getDurationFromString(this.getStringValue(), AtomicTypes.DayTimeDurationItem));
            case StringItem:
                return this;
            default:
                throw new ClassCastException();
        }
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.StringItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        if (itemType == AtomicTypes.StringItem)
            return true;
        try {
            if (itemType == AtomicTypes.IntegerItem) {
                Integer.parseInt(this.getValue());
            } else if (itemType == AtomicTypes.DecimalItem) {
                if (this.getValue().contains("e") || this.getValue().contains("E")) return false;
                Float.parseFloat(this.getValue());
            } else if (itemType == AtomicTypes.DoubleItem) {
                Double.parseDouble(this.getValue());
            } else if (itemType == AtomicTypes.NullItem) {
                return isNullLiteral(this.getValue());
            } else if (itemType == AtomicTypes.DurationItem) {
                DurationItem.getDurationFromString(this._value, AtomicTypes.DurationItem);
            } else if (itemType == AtomicTypes.YearMonthDurationItem) {
                DurationItem.getDurationFromString(this.getValue(), AtomicTypes.YearMonthDurationItem);
            } else if (itemType == AtomicTypes.DayTimeDurationItem) {
                DurationItem.getDurationFromString(this.getValue(), AtomicTypes.DayTimeDurationItem);
            }
            else return isBooleanLiteral(this.getValue());
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public String serialize() {
        return this.getValue();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readString();
    }
    
    public boolean equals(Object otherItem)
    {
        if(!(otherItem instanceof Item))
        {
            return false;
        }
        Item o = (Item)otherItem;
        if(!o.isString())
        {
            return false;
        }
        return (getStringValue().equals(o.getStringValue()));
    }
    
    public int hashCode()
    {
        return getStringValue().hashCode();
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : this.getStringValue().compareTo(other.getStringValue());
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isString() && !other.isNull()) {
            throw new UnexpectedTypeException("Invalid args for string comparison " + this.serialize() +
                    ", " + other.serialize(), metadata);
        }
        return operator.apply(this, other);
    }
}