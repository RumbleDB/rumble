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

import org.rumbledb.api.Item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

public class BooleanItem extends AtomicItem {


	private static final long serialVersionUID = 1L;
	private boolean _value;

    public BooleanItem() {
        super();
    }

    public BooleanItem(boolean value) {
        super();
        this._value = value;
    }

    public boolean getValue() {
        return _value;
    }

    @Override
    public boolean getBooleanValue() {
        return _value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getBooleanValue();
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.BooleanItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType() != AtomicTypes.AtomicItem;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromBoolean(this);
    }

    @Override
    public AtomicItem createFromBoolean(BooleanItem booleanItem) {
        return booleanItem;
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createBooleanItem(Boolean.parseBoolean(stringItem.getStringValue()));
    }

    @Override
    public AtomicItem createFromInteger(IntegerItem integerItem) {
        return ItemFactory.getInstance().createBooleanItem(integerItem.getIntegerValue() != 0);
    }

    @Override
    public AtomicItem createFromDecimal(DecimalItem decimalItem) {
        return ItemFactory.getInstance().createBooleanItem(!decimalItem.getDecimalValue().equals(BigDecimal.ZERO));
    }

    @Override
    public AtomicItem createFromDouble(DoubleItem doubleItem) {
        return ItemFactory.getInstance().createBooleanItem(doubleItem.getDoubleValue() != 0);
    }

    @Override
    public String serialize() {
        return String.valueOf(_value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readBoolean();
    }
    
    public boolean equals(Object otherItem)
    {
        if(!(otherItem instanceof Item))
        {
            return false;
        }
        Item o = (Item)otherItem;
        if(!o.isBoolean())
        {
            return false;
        }
        return (getBooleanValue() == o.getBooleanValue());
    }
    
    public int hashCode()
    {
        return getBooleanValue()?1:0;
    }
}
