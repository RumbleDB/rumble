package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EscapeHTMLURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public EscapeHTMLURIFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item item = this.children.get(0).materializeFirstItemOrNull(context);
        if (item == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        String input = item.getStringValue();
        StringBuilder result = new StringBuilder(input.length());
        int i = 0;
        while (i < input.length()) {
            int codePoint = input.codePointAt(i);
            if (codePoint >= 32 && codePoint <= 126) {
                result.appendCodePoint(codePoint);
            } else {
                appendPercentEncoded(result, codePoint);
            }
            i += Character.charCount(codePoint);
        }
        return ItemFactory.getInstance().createStringItem(result.toString());
    }

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private static void appendPercentEncoded(StringBuilder result, int codePoint) {
        for (byte b : new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8)) {
            int unsigned = b & 0xFF;
            result.append('%');
            result.append(HEX_DIGITS[(unsigned >>> 4) & 0xF]);
            result.append(HEX_DIGITS[unsigned & 0xF]);
        }
    }
}
