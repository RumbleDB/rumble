package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UntypedAtomicItem implements Item {

    private static final long serialVersionUID = 1L;
    private String value;
    private double doubleValue;
    private byte activeType = 0;

    public UntypedAtomicItem() {
        super();
    }

    public UntypedAtomicItem(String value) {
        super();
        this.value = value;
    }


    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
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

    @Override
    public String getUntypedAtomicValue() {
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
    public boolean isUntypedAtomic() {
        return true;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getStringValue().isEmpty();
    }

    public String serialize() {
        return this.getValue();
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
        return BuiltinTypesCatalogue.untypedAtomicItem;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return new NativeClauseContext(context, '"' + this.value + '"', BuiltinTypesCatalogue.untypedAtomicItem);
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}

