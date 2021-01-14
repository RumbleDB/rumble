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


public class DecimalItem extends ItemImpl {


    private static final long serialVersionUID = 1L;
    private BigDecimal value;

    public DecimalItem() {
        super();
    }

    public DecimalItem(BigDecimal decimal) {
        super();
        this.value = decimal;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return this.value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getDecimalValue().equals(BigDecimal.ZERO);
    }

    public double castToDoubleValue() {
        return getDecimalValue().doubleValue();
    }

    public float castToFloatValue() {
        return getDecimalValue().floatValue();
    }

    public BigDecimal castToDecimalValue() {
        return getDecimalValue();
    }

    public int castToIntValue() {
        return getDecimalValue().intValue();
    }

    public BigInteger castToIntegerValue() {
        return getDecimalValue().toBigInteger();
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    @Override
    public String serialize() {
        return String.valueOf(this.value.stripTrailingZeros().toPlainString());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = kryo.readObject(input, BigDecimal.class);
    }

    public boolean equals(Object otherItem) {
        try {
            return (otherItem instanceof Item) && this.compareTo((Item) otherItem) == 0;
        } catch (IteratorFlowException e) {
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
        if (other.isNull()) {
            return 1;
        }
        if (other.isInteger()) {
            return this.value.compareTo(other.castToDecimalValue());
        }
        if (other.isDecimal()) {
            return this.value.compareTo(other.getDecimalValue());
        }
        if (other.isDouble()) {
            return Double.compare(this.castToDoubleValue(), other.getDoubleValue());
        }
        throw new OurBadException("Comparing an int to something that is not a number.");
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.decimalItem;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
