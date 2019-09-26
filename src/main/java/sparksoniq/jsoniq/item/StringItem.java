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
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
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
        return _value;
    }

    public double castToDoubleValue() {
    	return Double.parseDouble(_value);
    }

    public BigDecimal castToDecimalValue() {
        return new BigDecimal(_value);
    }

    public int castToIntegerValue() {
        return Integer.parseInt(_value);
    }

    private boolean isBooleanLiteral(String value) { return "true".equals(value) || "false".equals(value); }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.StringItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        if (type.getType() == AtomicTypes.StringItem)
            return true;
        try {
            if (type.getType() == AtomicTypes.IntegerItem)
                Integer.parseInt(this.getValue());
            else if (type.getType() == AtomicTypes.DecimalItem) {
                if (this.getValue().contains("e") || this.getValue().contains("E")) return false;
                Float.parseFloat(this.getValue());
            } else if (type.getType() == AtomicTypes.DoubleItem) {
                Double.parseDouble(this.getValue());
            } else if (type.getType() == AtomicTypes.DurationItem) {
                DurationItem.getDurationFromString(this.getValue(), AtomicTypes.DurationItem);
            } else if (type.getType() == AtomicTypes.YearMonthDurationItem) {
                DurationItem.getDurationFromString(this.getValue(), AtomicTypes.YearMonthDurationItem);
            } else if (type.getType() == AtomicTypes.DayTimeDurationItem) {
                DurationItem.getDurationFromString(this.getValue(), AtomicTypes.DayTimeDurationItem);
            } else if (type.getType() == AtomicTypes.DateTimeItem) {
                DateTimeItem.getDateTimeFromString(this.getValue(), AtomicTypes.DateTimeItem);
            } else if (type.getType() == AtomicTypes.DateItem) {
                DateTimeItem.getDateTimeFromString(this.getValue(), AtomicTypes.DateItem);
            } else if (type.getType() == AtomicTypes.TimeItem) {
                DateTimeItem.getDateTimeFromString(this.getValue(), AtomicTypes.TimeItem);
            }
            else return isBooleanLiteral(this.getValue());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromString(this);
    }

    @Override
    public AtomicItem createFromBoolean(BooleanItem booleanItem) {
        return ItemFactory.getInstance().createStringItem(String.valueOf(booleanItem.getBooleanValue()));
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return stringItem;
    }

    @Override
    public AtomicItem createFromInteger(IntegerItem integerItem) {
        return ItemFactory.getInstance().createStringItem(String.valueOf(integerItem.getIntegerValue()));
    }

    @Override
    public AtomicItem createFromDecimal(DecimalItem decimalItem) {
        return ItemFactory.getInstance().createStringItem(String.valueOf(decimalItem.getDecimalValue()));
    }

    @Override
    public AtomicItem createFromDouble(DoubleItem doubleItem) {
        return ItemFactory.getInstance().createStringItem(String.valueOf(doubleItem.getDoubleValue()));
    }

    @Override
    public AtomicItem createFromDuration(DurationItem durationItem) {
        return ItemFactory.getInstance().createStringItem(durationItem.serialize());
    }

    @Override
    public AtomicItem createFromDayTimeDuration(DayTimeDurationItem dayTimeDurationItem) {
        return ItemFactory.getInstance().createStringItem(dayTimeDurationItem.serialize());
    }

    @Override
    public AtomicItem createFromYearMonthDuration(YearMonthDurationItem yearMonthDurationItem) {
        return ItemFactory.getInstance().createStringItem(yearMonthDurationItem.serialize());
    }

    @Override
    public AtomicItem createFromDateTime(DateTimeItem dateTimeItem) {
        return ItemFactory.getInstance().createStringItem(dateTimeItem.serialize());
    }

    @Override
    public AtomicItem createFromDate(DateItem dateItem) {
        return ItemFactory.getInstance().createStringItem(dateItem.serialize());
    }

    @Override
    public AtomicItem createFromTime(TimeItem timeItem) {
        return ItemFactory.getInstance().createStringItem(timeItem.serialize());
    }

    @Override
    public String serialize() {
        return getValue();
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
        if (!other.isString()) {
            throw new UnexpectedTypeException("Invalid args for string comparison " + this.serialize() +
                    ", " + other.serialize(), metadata);
        }
        return operator.apply(this, other);
    }
}
