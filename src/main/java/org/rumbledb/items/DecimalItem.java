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

import lombok.NoArgsConstructor;
import org.rumbledb.api.Item;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

@NoArgsConstructor // For Kryo serialization
public class DecimalItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;
    private BigDecimal value;

    // Float/double casts retain their exact decimal value for op:same-key while serializing
    // with the concise lexical representation users expect from the source numeric value.
    private String displayValue;

    public DecimalItem(BigDecimal decimal) {
        this.value = decimal;
    }

    public DecimalItem(BigDecimal decimal, String displayValue) {
        this.value = decimal;
        this.displayValue = displayValue;
    }

    @Override
    public Item copy(boolean mutable) {
        return new DecimalItem(this.value, this.displayValue);
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
        if (this.displayValue != null) {
            return this.displayValue;
        }
        return String.valueOf(this.value.stripTrailingZeros().toPlainString());
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !(this.getDecimalValue().compareTo(BigDecimal.ZERO) == 0);
    }

    @Override
    public double castToDoubleValue() {
        return getDecimalValue().doubleValue();
    }

    @Override
    public float castToFloatValue() {
        return getDecimalValue().floatValue();
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return getDecimalValue();
    }

    @Override
    public int castToIntValue() {
        return getDecimalValue().intValue();
    }

    @Override
    public BigInteger castToIntegerValue() {
        return getDecimalValue().toBigInteger();
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.decimalItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, this.value.toString(), SequenceType.createSequenceType("decimal"));
    }

    @Override
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
