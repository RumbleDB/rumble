package org.rumbledb.items;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;

public class UntypedAtomicItem implements Item {

    @Serial
    private static final long serialVersionUID = 1L;
    private String value;

    public UntypedAtomicItem() {
        super();
    }

    public UntypedAtomicItem(String value) {
        super();
        this.value = value;
    }

    @Override
    public Item copy(boolean mutable) {
        return new UntypedAtomicItem(this.value);
    }

    public String getValue() {
        return this.value;
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
                    ExceptionMetadata.EMPTY_METADATA
            );
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
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    @Override
    public BigDecimal castToDecimalValue() {
        try {
            return new BigDecimal(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:decimal.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    @Override
    public BigInteger castToIntegerValue() {
        try {
            return new BigInteger(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:integer.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    @Override
    public int castToIntValue() {
        try {
            return Integer.parseInt(this.value.trim());
        } catch (NumberFormatException e) {
            throw new CastException(
                    "Cannot cast xs:untypedAtomic value \"" + this.value + "\" to xs:int.",
                    ExceptionMetadata.EMPTY_METADATA
            );
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

    @Override
    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }



    public int hashCode() {
        return getStringValue().hashCode();
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

