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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntegerItem implements Item {


    private static final long serialVersionUID = 1L;
    private BigInteger value;

    public IntegerItem() {
        super();
    }

    public IntegerItem(BigInteger value) {
        super();
        this.value = value;
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public BigInteger getIntegerValue() {
        return this.value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return new BigDecimal(this.value);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.value.equals(BigInteger.ZERO);
    }

    @Override
    public double castToDoubleValue() {
        return this.value.doubleValue();
    }

    @Override
    public float castToFloatValue() {
        return this.value.floatValue();
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.value);
    }

    @Override
    public BigInteger castToIntegerValue() {
        return this.value;
    }

    @Override
    public int castToIntValue() {
        return this.value.intValue();
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
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = kryo.readObject(input, BigInteger.class);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.integerItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, this.value.toString(), SequenceType.INTEGER);
    }

    public boolean isNumeric() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
