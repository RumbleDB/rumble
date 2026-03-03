package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UntypedAtomicItem implements Item {

    private static final long serialVersionUID = 1L;
    private String value;

    public UntypedAtomicItem() {
        super();
    }

    public UntypedAtomicItem(String value) {
        super();
        this.value = value;
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
        return Double.parseDouble(this.getValue());
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
        if (trimmedValue.startsWith("-") && Float.parseFloat(this.getValue()) == -0f) {
            return -0f;
        }
        return Float.parseFloat(this.getValue());
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return new BigDecimal(this.value.trim());
    }

    @Override
    public BigInteger castToIntegerValue() {
        return new BigInteger(this.value.trim());
    }

    @Override
    public int castToIntValue() {
        return Integer.parseInt(this.value.trim());
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isUntypedAtomic() {
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


