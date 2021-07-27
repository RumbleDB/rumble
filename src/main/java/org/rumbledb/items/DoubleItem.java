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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DoubleItem implements Item {

    private static final long serialVersionUID = 1L;
    private Double value;

    public DoubleItem() {
        super();
    }

    public DoubleItem(double value) {
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
    public double getDoubleValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.serialize();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.value != 0;
    }

    @Override
    public double castToDoubleValue() {
        return this.value;
    }

    @Override
    public float castToFloatValue() {
        return this.value.floatValue();
    }

    public BigDecimal castToDecimalValue() {
        if (Double.isNaN(this.value) || Double.isInfinite(this.value)) {
            throw new IteratorFlowException("Cannot call castToDecimal on non numeric");
        }
        return BigDecimal.valueOf(getDoubleValue());
    }

    public int castToIntValue() {
        return Double.valueOf(this.value).intValue();
    }

    public BigInteger castToIntegerValue() {
        return BigDecimal.valueOf(this.value).toBigInteger();
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public String serialize() {
        if (Double.isNaN(this.value)) {
            return "NaN";
        }
        if (Double.isInfinite(this.value) && this.value > 0) {
            return "INF";
        }
        if (Double.isInfinite(this.value) && this.value < 0) {
            return "-INF";
        }
        if (Double.compare(this.value, -0d) == 0) {
            return "-0";
        }
        if (Double.compare(this.value, 0d) == 0) {
            return "0";
        }
        double abs = Math.abs(this.value);
        if (abs >= 0.000001 && abs <= 1000000) {
            return this.castToDecimalValue().stripTrailingZeros().toPlainString();
        }
        return Double.toString(this.value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeDouble(this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readDouble();
    }

    public int hashCode() {
        return (int) Math.round(getDoubleValue());
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.doubleItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, "" + this.value, BuiltinTypesCatalogue.doubleItem);
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
