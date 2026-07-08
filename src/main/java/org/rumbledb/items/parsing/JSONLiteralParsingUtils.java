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

    public static DecodedEscape decodeEscapeSequence(JSONCharSource source) {
        // The backslash itself was already consumed by the caller.
        if (source.isEnd()) {
            throw new IllegalArgumentException("Dangling backslash in escaped JSON string.");
        }

        char escapeKind = source.advance();
        return switch (escapeKind) {
            case '"' -> new DecodedEscape("\"", "\\\"");
            case '/' -> new DecodedEscape("/", "\\/");
            case '\\' -> new DecodedEscape("\\", "\\\\");
            case 'b' -> new DecodedEscape("\b", "\\b");
            case 'f' -> new DecodedEscape("\f", "\\f");
            case 'n' -> new DecodedEscape("\n", "\\n");
            case 'r' -> new DecodedEscape("\r", "\\r");
            case 't' -> new DecodedEscape("\t", "\\t");
            case 'u' -> decodeUnicodeEscape(source);
            default -> throw new IllegalArgumentException("Invalid escape sequence in escaped JSON string.");
        };
    }

    private static DecodedEscape decodeUnicodeEscape(JSONCharSource source) {
        int first = parseHexQuad(source);
        String firstEscape = "\\u" + hex4(first);

        if (Character.isHighSurrogate((char) first) && canReadLowSurrogateEscape(source)) {
            // Validate and decode the candidate second escape without consuming it yet:
            // invalid hex digits are still a hard error, but a valid quad that is not a
            // low surrogate must be left untouched for the caller to reprocess.
            int second = peekHexQuad(source, 2);
            if (Character.isLowSurrogate((char) second)) {
                for (int i = 0; i < 6; i++) {
                    source.advance();
                }
                int codePoint = Character.toCodePoint((char) first, (char) second);
                return new DecodedEscape(
                        new String(Character.toChars(codePoint)),
                        firstEscape + "\\u" + hex4(second)
                );
            }
        }

        return new DecodedEscape(String.valueOf((char) first), firstEscape);
    }

    private static int parseHexQuad(JSONCharSource source) {
        if (!source.hasCharsAhead(4)) {
            throw new IllegalArgumentException("Incomplete unicode escape in escaped JSON string.");
        }

        int value = 0;
        for (int i = 0; i < 4; i++) {
            char c = source.advance();
            int digit = Character.digit(c, 16);
            if (digit < 0) {
                throw new IllegalArgumentException("Invalid unicode escape in escaped JSON string.");
            }
            value = (value << 4) | digit;
        }
        return value;
    }

    /**
     * Like {@link #parseHexQuad(JSONCharSource)}, but reads 4 hex digits starting
     * {@code startOffset} characters ahead of the current position via {@code peek}
     * instead of {@code advance}, without consuming anything. The caller must have
     * already verified {@code hasCharsAhead(startOffset + 4)}.
     */
    private static int peekHexQuad(JSONCharSource source, int startOffset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            char c = source.peek(startOffset + i);
            int digit = Character.digit(c, 16);
            if (digit < 0) {
                throw new IllegalArgumentException("Invalid unicode escape in escaped JSON string.");
            }
            value = (value << 4) | digit;
        }
        return value;
    }

    private static boolean canReadLowSurrogateEscape(JSONCharSource source) {
        return source.hasCharsAhead(6) && source.peek(0) == '\\' && source.peek(1) == 'u';
    }

    private static String hex4(int value) {
        return String.format("%04X", value);
    }

    public static final class DecodedEscape {
        private final String decodedText;
        private final String rawEscape;

        private DecodedEscape(String decodedText, String rawEscape) {
            this.decodedText = decodedText;
            this.rawEscape = rawEscape;
        }

        public String getDecodedText() {
            return this.decodedText;
        }

        public String getRawEscape() {
            return this.rawEscape;
        }
    }
}
