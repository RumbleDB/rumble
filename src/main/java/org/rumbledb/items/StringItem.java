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

public class StringItem implements Item {


    private static final long serialVersionUID = 1L;
    private String value;

    public StringItem() {
        super();
    }

    public StringItem(String value) {
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

    public String getValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.value;
    }

    public double castToDoubleValue() {
        String trimmedValue = this.value.trim();
        if (trimmedValue.equals("INF") || trimmedValue.equals("+INF")) {
            return Double.POSITIVE_INFINITY;
        }
        if (trimmedValue.equals("-INF")) {
            return Double.NEGATIVE_INFINITY;
        }
        if (trimmedValue.equals("NaN")) {
            return Double.NaN;
        }
        return Double.parseDouble(this.getValue());
    }

    public float castToFloatValue() {
        String trimmedValue = this.value.trim();
        if (trimmedValue.equals("INF") || trimmedValue.equals("+INF")) {
            return Float.POSITIVE_INFINITY;
        }
        if (trimmedValue.equals("-INF")) {
            return Float.NEGATIVE_INFINITY;
        }
        if (trimmedValue.equals("NaN")) {
            return Float.NaN;
        }
        if (trimmedValue.startsWith("-") && Float.parseFloat(this.getValue()) == -0f) {
            return -0f;
        }
        return Float.parseFloat(this.getValue());
    }

    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.value.trim());
    }

    public BigInteger castToIntegerValue() {
        return new BigInteger(this.value.trim());
    }

    public int castToIntValue() {
        return Integer.parseInt(this.value.trim());
    }

    @Override
    public boolean isString() {
        return true;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readString();
    }

    public int hashCode() {
        return getStringValue().hashCode();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.stringItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, '"' + this.value + '"', SequenceType.STRING);
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public String getSparkSQLValue() {
        return "\"" + this.value + "\"";
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        return "\"" + this.value + "\"";
    }

    @Override
    public String getSparkSQLType() {
        return "STRING";
    }
}
