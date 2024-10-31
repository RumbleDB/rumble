package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.lang3.StringUtils;
import java.util.Base64;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Base64BinaryItem implements Item {

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
        this.value = parseBase64BinaryString(stringValue);
        this.stringValue = StringUtils.chomp(Base64.getEncoder().encodeToString(this.value));
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
    public boolean isBinary() {
        return true;
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
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isBase64Binary() {
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getValue());
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
        this.stringValue = StringUtils.chomp(Base64.getEncoder().encodeToString(this.value));
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.base64BinaryItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
