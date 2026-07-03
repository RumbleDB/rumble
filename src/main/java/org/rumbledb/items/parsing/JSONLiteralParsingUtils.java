package org.rumbledb.items.parsing;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;

import java.math.BigDecimal;

public final class JSONLiteralParsingUtils {

    private JSONLiteralParsingUtils() {
    }

    public static Item getItemFromJSONNumber(String number, String numberFormat) {
        if (JSONParsingOptions.NUMBER_FORMAT_DOUBLE.equals(numberFormat)) {
            return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
        }

        if (JSONParsingOptions.NUMBER_FORMAT_DECIMAL.equals(numberFormat)) {
            return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
        }

        if (JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE.equals(numberFormat)) {
            if (number.contains("E") || number.contains("e")) {
                return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
            }
            if (number.contains(".")) {
                return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
            }
            return ItemFactory.getInstance().createIntegerItem(number);
        }

        throw new OurBadException("Unexpected number-format: " + numberFormat);
    }

    public static DecodedEscape decodeEscapeSequence(String input, int escapeStart) {
        if (
            input == null
                || escapeStart < 0
                || escapeStart >= input.length()
                || input.charAt(escapeStart) != '\\'
        ) {
            throw new IllegalArgumentException("Escape sequence must start with a backslash.");
        }
        if (escapeStart + 1 >= input.length()) {
            throw new IllegalArgumentException("Dangling backslash in escaped JSON string.");
        }

        char escapeKind = input.charAt(escapeStart + 1);
        return switch (escapeKind) {
            case '"' -> new DecodedEscape("\"", "\\\"", escapeStart + 2);
            case '/' -> new DecodedEscape("/", "\\/", escapeStart + 2);
            case '\\' -> new DecodedEscape("\\", "\\\\", escapeStart + 2);
            case 'b' -> new DecodedEscape("\b", "\\b", escapeStart + 2);
            case 'f' -> new DecodedEscape("\f", "\\f", escapeStart + 2);
            case 'n' -> new DecodedEscape("\n", "\\n", escapeStart + 2);
            case 'r' -> new DecodedEscape("\r", "\\r", escapeStart + 2);
            case 't' -> new DecodedEscape("\t", "\\t", escapeStart + 2);
            case 'u' -> decodeUnicodeEscape(input, escapeStart);
            default -> throw new IllegalArgumentException("Invalid escape sequence in escaped JSON string.");
        };
    }

    private static DecodedEscape decodeUnicodeEscape(String input, int escapeStart) {
        int first = parseHexQuad(input, escapeStart + 2);
        String firstEscape = "\\u" + hex4(first);
        int nextIndex = escapeStart + 6;

        if (Character.isHighSurrogate((char) first) && canReadLowSurrogateEscape(input, nextIndex)) {
            int second = parseHexQuad(input, nextIndex + 2);
            if (Character.isLowSurrogate((char) second)) {
                int codePoint = Character.toCodePoint((char) first, (char) second);
                return new DecodedEscape(
                        new String(Character.toChars(codePoint)),
                        firstEscape + "\\u" + hex4(second),
                        nextIndex + 6
                );
            }
        }

        return new DecodedEscape(String.valueOf((char) first), firstEscape, nextIndex);
    }

    private static int parseHexQuad(String input, int start) {
        if (start + 4 > input.length()) {
            throw new IllegalArgumentException("Incomplete unicode escape in escaped JSON string.");
        }

        int value = 0;
        for (int i = 0; i < 4; i++) {
            char c = input.charAt(start + i);
            int digit = Character.digit(c, 16);
            if (digit < 0) {
                throw new IllegalArgumentException("Invalid unicode escape in escaped JSON string.");
            }
            value = (value << 4) | digit;
        }
        return value;
    }

    private static boolean canReadLowSurrogateEscape(String input, int index) {
        return index + 5 < input.length() && input.charAt(index) == '\\' && input.charAt(index + 1) == 'u';
    }

    private static String hex4(int value) {
        return String.format("%04X", value);
    }

    public static final class DecodedEscape {
        private final String decodedText;
        private final String rawEscape;
        private final int nextIndex;

        private DecodedEscape(String decodedText, String rawEscape, int nextIndex) {
            this.decodedText = decodedText;
            this.rawEscape = rawEscape;
            this.nextIndex = nextIndex;
        }

        public String getDecodedText() {
            return this.decodedText;
        }

        public String getRawEscape() {
            return this.rawEscape;
        }

        public int getNextIndex() {
            return this.nextIndex;
        }
    }
}
