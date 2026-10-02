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

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

@Getter
public class UntypedAtomicItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private String value;

    public UntypedAtomicItem(String value) {
        this.value = value;
    }

    @Override
    public Item copy(boolean mutable) {
        return new UntypedAtomicItem(this.value);
    }

    @Override
    public String getStringValue() {
        return this.value;
    }

    @Override
    public Object getVariantValue() {
        return getStringValue();
    }

    @Override
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
        try {
            return Double.parseDouble(trimmedValue);
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:double.",
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
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
        try {
            float parsedValue = Float.parseFloat(trimmedValue);
            if (trimmedValue.startsWith("-") && parsedValue == -0f) {
                return -0f;
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:float.",
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public BigDecimal castToDecimalValue() {
        try {
            return new BigDecimal(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:decimal.",
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public BigInteger castToIntegerValue() {
        try {
            return new BigInteger(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:integer.",
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public int castToIntValue() {
        try {
            return Integer.parseInt(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:int.",
                    ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isUntypedAtomic() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.untypedAtomicItem;
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
