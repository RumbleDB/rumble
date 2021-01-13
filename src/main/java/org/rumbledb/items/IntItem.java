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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IntItem extends AtomicItem {


    private static final long serialVersionUID = 1L;
    private int value;

    public IntItem() {
        super();
    }

    public IntItem(int value) {
        super();
        this.value = value;
    }

    @Override
    public int getIntValue() {
        return this.value;
    }

    @Override
    public BigInteger getIntegerValue() {
        return BigInteger.valueOf(this.value);
    }

    @Override
    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf(this.value);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.value != 0;
    }

    public double castToDoubleValue() {
        return new Integer(this.value).doubleValue();
    }

    public float castToFloatValue() {
        return new Integer(this.value).floatValue();
    }

    public BigDecimal castToDecimalValue() {
        return BigDecimal.valueOf(this.value);
    }

    public BigInteger castToIntegerValue() {
        return BigInteger.valueOf(this.value);
    }

    public int castToIntValue() {
        return this.value;
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
    public boolean isInt() {
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
        return type.equals(AtomicItemType.floatItem) || type.equals(AtomicItemType.doubleItem) || super.canBePromotedTo(type);
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
        output.writeInt(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readInt();
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch (IteratorFlowException e) {
            return false;
        }
    }

    public int hashCode() {
        return getIntValue();
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isInt()) {
            return Integer.compare(this.value, other.getIntValue());
        }
        if (other.isInteger()) {
            return this.castToIntegerValue().compareTo(other.getIntegerValue());
        }
        if (other.isDecimal()) {
            return this.castToDecimalValue().compareTo(other.getDecimalValue());
        }
        if (other.isDouble()) {
            return Double.compare(this.castToDoubleValue(), other.getDoubleValue());
        }
        throw new OurBadException("Comparing an int to something that is not a number.");
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.integerItem;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }
}
