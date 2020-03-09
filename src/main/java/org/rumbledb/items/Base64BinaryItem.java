package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.types.ItemType;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Base64BinaryItem extends AtomicItem {

    private static final String b04char = "([AQgw])";
    private static final String b04 = "(" + b04char + "(\\s)?)";
    private static final String b16char = "([AEIMQUYcgkosw048])";
    private static final String b16 = "(" + b16char + "(\\s)?)";
    private static final String b64char = "([A-Za-z0-9+/])";
    private static final String b64 = "(" + b64char + "(\\s)?)";
    private static final String padded8 = "(" + b64 + b04 + "=(\\s)?=)";
    private static final String padded16 = "(" + b64 + b64 + b16 + "=)";
    private static final String b64finalQuad = "(" + b64 + b64 + b64 + b64char + ")";
    private static final String b64final = "(" + b64finalQuad + "|" + padded16 + "|" + padded8 + ")";
    private static final String b64quad = "(" + b64 + b64 + b64 + b64 + ")";
    private static final String base64Binary = "((" + b64quad + ")*" + "(" + b64final + "))?";
    private static final Pattern base64BinaryPattern = Pattern.compile(base64Binary);

    private static final long serialVersionUID = 1L;
    private byte[] value;
    private String stringValue;

    public Base64BinaryItem() {
        super();
    }

    public Base64BinaryItem(String stringValue) {
        this.stringValue = stringValue;
        this.value = parseBase64BinaryString(stringValue);
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

    private static boolean checkInvalidBase64BinaryFormat(String base64BinaryString) {
        return base64BinaryPattern.matcher(base64BinaryString).matches();
    }

    static byte[] parseBase64BinaryString(String base64BinaryString) throws IllegalArgumentException {
        if (base64BinaryString == null || !checkInvalidBase64BinaryFormat(base64BinaryString)) {
            throw new IllegalArgumentException();
        }
        return DatatypeConverter.parseBase64Binary(base64BinaryString);
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.base64BinaryItem) || super.isTypeOf(type);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isBase64Binary() {
        return true;
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(ItemType.base64BinaryItem)
            ||
            itemType.equals(ItemType.hexBinaryItem)
            ||
            itemType.equals(ItemType.stringItem);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(this.getStringValue());
        }
        if (itemType.equals(ItemType.base64BinaryItem)) {
            return this;
        }
        if (itemType.equals(ItemType.hexBinaryItem)) {
            return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(this.value));
        }
        throw new ClassCastException();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        if (otherItem.isBase64Binary()) {
            return Arrays.equals(this.getValue(), otherItem.getBinaryValue());
        }
        return false;
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        if (other.isBase64Binary()) {
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
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, ExceptionMetadata metadata) {
        if (!other.isBase64Binary() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "\""
                        + this.getDynamicType().toString()
                        + "\": invalid type: can not compare for equality to type \""
                        + other.getDynamicType().toString()
                        + "\"",
                    metadata
            );
        }
        if (other.isNull()) {
            return operator.apply(this, other);
        }
        switch (operator) {
            case VC_EQ:
            case GC_EQ:
            case VC_NE:
            case GC_NE:
                return operator.apply(this, other);
        }
        throw new UnexpectedTypeException(
                "\""
                    + this.getDynamicType().toString()
                    + "\": invalid type: can not compare for equality to type \""
                    + other.getDynamicType().toString()
                    + "\"",
                metadata
        );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getValue());
    }

    @Override
    public String serialize() {
        return this.getStringValue();
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
        this.stringValue = StringUtils.chomp(Base64.encodeBase64String(this.value));
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.base64BinaryItem;
    }
}
