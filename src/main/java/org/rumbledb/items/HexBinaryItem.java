package org.rumbledb.items;

import java.io.Serial;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import lombok.Getter;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class HexBinaryItem extends AbstractAtomicItem {

    @Serial private static final long serialVersionUID = 1L;
    @Getter private byte[] value;
    private String stringValue;

    private static final String hexDigit = "[\\da-fA-F]";
    private static final String hexOctet = "(" + hexDigit + hexDigit + ")";
    private static final String hexBinary = hexOctet + "*";
    private static final Pattern hexBinaryPattern = Pattern.compile(hexBinary);

    HexBinaryItem(String stringValue) {
        this.stringValue = stringValue;
        this.value = parseHexBinaryString(stringValue);
    }

    @Override
    public Item copy(boolean mutable) {
        return new HexBinaryItem(this.stringValue);
    }

    @Override
    public byte[] getBinaryValue() {
        return this.value;
    }

    @Override
    public Object getVariantValue() {
        return getBinaryValue();
    }

    @Override
    public String getStringValue() {
        return this.stringValue.toUpperCase();
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
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.hexBinaryItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
