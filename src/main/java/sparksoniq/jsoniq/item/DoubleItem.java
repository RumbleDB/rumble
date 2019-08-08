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
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

public class DoubleItem extends AtomicItem {

    /**
	 * 
	 */
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

    @Override
    public <T> T getNumericValue(Class<T> type) {
        Double result = this.getDoubleValue();
        if (type.equals(BigDecimal.class))
            return (T) BigDecimal.valueOf(result);
        if (type.equals(Integer.class))
            return (T) new Integer(result.intValue());
        return (T) result;
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        if (type.getType().equals(ItemTypes.DoubleItem) || super.isTypeOf(type))
            return true;
        return false;
    }

    @Override
    public String serialize() {
        return String.valueOf(_value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeDouble(this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readDouble();
    }
    
    public boolean equals(Object otherItem)
    {
        if(!(otherItem instanceof Item))
        {
            return false;
        }
        Item o = (Item)otherItem;
        if(o.isInteger())
        {
            return (getDoubleValue() == (double)o.getIntegerValue());
        }
        if(o.isDecimal())
        {
            return (getDoubleValue() == o.getDecimalValue().doubleValue());
        }
        if(o.isDouble())
        {
            return (getDoubleValue() == o.getDoubleValue());
        }
        return false;
    }
    
    public int hashCode()
    {
        return (int)Math.round(getDoubleValue());
    }
}
