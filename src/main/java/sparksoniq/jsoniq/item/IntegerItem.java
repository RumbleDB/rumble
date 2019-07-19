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

public class IntegerItem extends AtomicItem {

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
    public <T> T getNumericValue(Class<T> type) {
        Integer result = this.getIntegerValue();
        if (type.equals(BigDecimal.class))
            return (T) BigDecimal.valueOf(result);
        if (type.equals(Double.class))
            return (T) new Double(result.doubleValue());
        return (T) result;
    }

    @Override
    public boolean isInteger() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        if (type.getType().equals(ItemTypes.IntegerItem) || type.getType().equals(ItemTypes.DecimalItem)
                || super.isTypeOf(type))
            return true;
        return false;
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
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (o.isInteger()) {
            return getIntegerValue() == o.getIntegerValue();
        }
        if (o.isDecimal()) {
            if (o.getDecimalValue().stripTrailingZeros().scale() > 0) {
                return false;
            }
            return o.getDecimalValue().intValueExact() == getIntegerValue();
        }
        if (o.isDouble()) {
            return (o.getDoubleValue() == (double) getIntegerValue());
        }
        return false;
    }

    public int hashCode() {
        return getIntegerValue();
    }
}
