package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

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
    private byte[] _value;
    private String _stringValue;

    public Base64BinaryItem() {
        super();
    }

    public Base64BinaryItem(String stringValue) {
        this._stringValue = stringValue;
        this._value = parseBase64BinaryString(stringValue);
    }

    public byte[] getValue() {
        return this._value;
    }

    @Override
    public byte[] getBinaryValue() {
        return this._value;
    }

    @Override
    public String getStringValue() {
        return _stringValue;
    }

    private static boolean checkInvalidBase64BinaryFormat(String base64BinaryString) {
        return base64BinaryPattern.matcher(base64BinaryString).matches();
    }

    static byte[] parseBase64BinaryString(String base64BinaryString) throws IllegalArgumentException {
        if (base64BinaryString == null || !checkInvalidBase64BinaryFormat(base64BinaryString))
            throw new IllegalArgumentException();
        return DatatypeConverter.parseBase64Binary(base64BinaryString);
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.Base64BinaryItem) || super.isTypeOf(type);
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
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.Base64BinaryItem)
            ||
            itemType.equals(AtomicTypes.HexBinaryItem)
            ||
            itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.getStringValue());
            case Base64BinaryItem:
                return this;
            case HexBinaryItem:
                return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(this._value));
            default:
                throw new ClassCastException();
        }
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
        if (other.isNull())
            return 1;
        if (other.isBase64Binary()) {
            return this.serializeValue().compareTo(Arrays.toString(other.getBinaryValue()));
        }
        throw new IteratorFlowException(
                "Cannot compare item of type "
                    + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    +
                    " with item of type "
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
        );
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isBase64Binary() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "\""
                        + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                        + "\": invalid type: can not compare for equality to type \""
                        + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
                        + "\"",
                    metadata
            );
        }
        if (other.isNull())
            return operator.apply(this, other);
        switch (operator) {
            case VC_EQ:
            case GC_EQ:
            case VC_NE:
            case GC_NE:
                return operator.apply(this, other);
        }
        throw new UnexpectedTypeException(
                "\""
                    + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
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
        this._value = input.readBytes(bytesLength);
        this._stringValue = StringUtils.chomp(Base64.encodeBase64String(this._value));
    }
}
