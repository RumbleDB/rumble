package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;
import java.util.Arrays;
import java.util.regex.Pattern;

public class HexBinaryItem implements Item {

    private static final long serialVersionUID = 1L;
    private byte[] value;
    private String stringValue;

    private final static String hexDigit = "[\\da-fA-F]";
    private final static String hexOctet = "(" + hexDigit + hexDigit + ")";
    private final static String hexBinary = hexOctet + "*";
    private final static Pattern hexBinaryPattern = Pattern.compile(hexBinary);

    public HexBinaryItem() {
        super();
    }

    HexBinaryItem(String stringValue) {
        this.stringValue = stringValue;
        this.value = parseHexBinaryString(stringValue);
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

    public byte[] getValue() {
        return this.value;
    }

    @Override
    public byte[] getBinaryValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.stringValue;
    }

    private static boolean checkInvalidHexBinaryFormat(String hexBinaryString) {
        return hexBinaryPattern.matcher(hexBinaryString).matches();
    }

    static byte[] parseHexBinaryString(String hexBinaryString) throws IllegalArgumentException {
        if (hexBinaryString == null || !checkInvalidHexBinaryFormat(hexBinaryString)) {
            throw new IllegalArgumentException();
        }
        try {
            return (byte[]) new Hex().decode(hexBinaryString);
        } catch (DecoderException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean isHexBinary() {
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getValue());
    }

    @Override
    public String serialize() {
        return this.getStringValue().toUpperCase();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(this.getValue().length);
        output.writeBytes(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        int bytesLength = input.readInt();
        this.value = input.readBytes(bytesLength);
        this.stringValue = Hex.encodeHexString(this.value);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.hexBinaryItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
