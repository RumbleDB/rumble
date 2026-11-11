/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.items;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.io.NumberOutput;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class FloatItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private float value;

    public FloatItem(float value) {
        this.value = value;
    }

    @Override
    public Item copy(boolean mutable) {
        return new FloatItem(this.value);
    }

    @Override
    public float getFloatValue() {
        return this.value;
    }

    @Override
    public Object getVariantValue() {
        return getFloatValue();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.value != 0;
    }

    @Override
    public String getStringValue() {
        if (Float.isNaN(this.value)) {
            return "NaN";
        }
        if (Float.isInfinite(this.value) && this.value > 0) {
            return "INF";
        }
        if (Float.isInfinite(this.value) && this.value < 0) {
            return "-INF";
        }
        if (Float.compare(this.value, -0f) == 0) {
            return "-0";
        }
        if (Float.compare(this.value, 0f) == 0) {
            return "0";
        }
        // Use the shortest decimal that round-trips to this float. JDK 17's Float.toString
        // can emit excess digits, and multiplying a float to adjust its exponent changes its value.
        BigDecimal decimal = new BigDecimal(NumberOutput.toString(this.value, true)).stripTrailingZeros();
        float abs = Math.abs(this.value);
        if (abs >= 0.000001f && abs < 1000000f) {
            return decimal.toPlainString();
        }
        int exponent = decimal.precision() - decimal.scale() - 1;
        String mantissa = decimal.scaleByPowerOfTen(-exponent).toPlainString();
        if (mantissa.indexOf('.') < 0) {
            mantissa += ".0";
        }
        return mantissa + "E" + exponent;
    }

    @Override
    public double castToDoubleValue() {
        return (double) this.value;
    }

    @Override
    public float castToFloatValue() {
        return this.value;
    }

    @Override
    public BigDecimal castToDecimalValue() {
        if (Float.isNaN(this.value) || Float.isInfinite(this.value)) {
            throw new IteratorFlowException("Cannot call castToDecimal on non numeric");
        }
        return BigDecimal.valueOf(this.value);
    }

    @Override
    public int castToIntValue() {
        return Float.valueOf(this.value).intValue();
    }

    @Override
    public BigInteger castToIntegerValue() {
        return BigDecimal.valueOf(this.value).toBigInteger();
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.floatItem;
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
    public boolean isNaN() {
        return Float.isNaN(this.value);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        if (Float.isInfinite(this.value)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (Float.isNaN(this.value)) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                context, "CAST (" + this.value + "D AS FLOAT)", SequenceType.createSequenceType("float"));
    }

    @Override
    public String getSparkSQLValue() {
        if (Float.isInfinite(this.value) && this.value > 0) {
            return "Infinity";
        }
        if (Float.isInfinite(this.value) && this.value < 0) {
            return "-Infinity";
        }
        return this.getStringValue();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        if (Float.isInfinite(this.value) && this.value > 0) {
            return "Infinity";
        }
        if (Float.isInfinite(this.value) && this.value < 0) {
            return "-Infinity";
        }
        return this.getStringValue();
    }

    @Override
    public String getSparkSQLType() {
        // TODO: Make enum?
        return "FLOAT";
    }
}
