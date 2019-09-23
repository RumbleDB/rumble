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
                Integer.parseInt(this._value);
            else if (type.getType() == AtomicTypes.DecimalItem) {
                if (this._value.contains("e") || this._value.contains("E")) return false;
                Float.parseFloat(this._value);
            } else if (type.getType() == AtomicTypes.DoubleItem)
                Double.parseDouble(this._value);
            else return isBooleanLiteral(this._value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String serialize() {
        return _value;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this._value);
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
}
