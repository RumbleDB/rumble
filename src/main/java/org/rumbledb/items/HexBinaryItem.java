package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;
import java.util.Arrays;
import java.util.regex.Pattern;

public class HexBinaryItem extends ItemImpl {

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
    public boolean isHexBinary() {
        return true;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        if (otherItem.isHexBinary()) {
            return Arrays.equals(this.getBinaryValue(), otherItem.getBinaryValue());
        }
        return false;
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isHexBinary()) {
            return this.serializeValue().compareTo(Arrays.toString(other.getBinaryValue()));
        }
        throw new IteratorFlowException(
                "Cannot compare item of type "
                    + this.getDynamicType().toString()
                    +
                    " with item of type "
                    + other.getDynamicType().toString()
        );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getValue());
    }

    @Override
    public String serialize() {
        return this.getStringValue().toUpperCase();
    }

    private String serializeValue() {
        return Arrays.toString(this.getValue());
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
        return AtomicItemType.hexBinaryItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
