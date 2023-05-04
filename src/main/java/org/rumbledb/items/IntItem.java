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
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntItem implements Item {


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

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
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
    public boolean isDouble() {
        return false;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readInt();
    }

    public int hashCode() {
        return getIntValue();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.intItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, "" + this.value, SequenceType.INT);
    }

    public boolean isNumeric() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
