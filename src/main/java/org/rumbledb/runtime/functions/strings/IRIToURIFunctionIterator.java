package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class IRIToURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public IRIToURIFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item inputItem = this.children.get(0).materializeFirstItemOrNull(context);

        if (inputItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (!(inputItem.isString() || inputItem.isAnyURI() || inputItem.isUntypedAtomic())) {
            throw new UnexpectedTypeException(
                    "fn:iri-to-uri expects a string, xs:anyURI, or xs:untypedAtomic argument [err:XPTY0004].",
                    getMetadata()
            );
        }

        return ItemFactory.getInstance().createStringItem(encodeIri(inputItem.getStringValue()));
    }

    private static String encodeIri(String value) {
        StringBuilder result = new StringBuilder(value.length());
        value.codePoints().forEach(codePoint -> appendEncodedCodePoint(result, codePoint));
        return result.toString();
    }

    private static void appendEncodedCodePoint(StringBuilder result, int codePoint) {
        if (!mustEscape(codePoint)) {
            result.appendCodePoint(codePoint);
            return;
        }

        byte[] utf8Bytes = new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8);
        for (byte currentByte : utf8Bytes) {
            result.append('%');
            int unsigned = currentByte & 0xFF;
            char high = Character.toUpperCase(Character.forDigit((unsigned >>> 4) & 0xF, 16));
            char low = Character.toUpperCase(Character.forDigit(unsigned & 0xF, 16));
            result.append(high);
            result.append(low);
        }
    }

    private static boolean mustEscape(int codePoint) {
        if (codePoint < 0x20 || codePoint > 0x7E) {
            return true;
        }
        return switch (codePoint) {
            case ' ', '"', '<', '>', '\\', '^', '`', '{', '|', '}' -> true;
            default -> false;
        };
    }
}
