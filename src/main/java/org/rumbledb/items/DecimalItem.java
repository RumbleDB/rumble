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

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


public class DecimalItem implements Item {


    @Serial
    private static final long serialVersionUID = 1L;
    private BigDecimal value;

    public DecimalItem() {
        super();
    }

    public DecimalItem(BigDecimal decimal) {
        super();
        this.value = decimal;
    }

    @Override
    public Item copy(boolean mutable) {
        return new DecimalItem(this.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Item otherItem) {
            long c = ComparisonIterator.compareItems(
                this,
                otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return this.value;
    }

    @Override
    public Object getVariantValue() {
        return getDecimalValue();
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value.stripTrailingZeros().toPlainString());
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !(this.getDecimalValue().compareTo(BigDecimal.ZERO) == 0);
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
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = kryo.readObject(input, BigDecimal.class);
    }

    public int hashCode() {
        if (getDecimalValue().stripTrailingZeros().scale() == 0) {
            return getDecimalValue().intValue();
        }
        return getDecimalValue().hashCode();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.decimalItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, this.value.toString(), SequenceType.createSequenceType("decimal"));
    }

    public boolean isNumeric() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public String getSparkSQLValue() {
        return this.value.stripTrailingZeros().toPlainString();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        return this.value.stripTrailingZeros().toPlainString();
    }

    @Override
    public String getSparkSQLType() {
        // TODO: Make enum?
        return "DECIMAL";
    }
}
