package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

/**
 * Shared helpers for {@link Serializer} implementations (map serialization, DM node names).
 */
public final class SerializerUtils {

    private SerializerUtils() {
    }

    public static void appendDmNodeNameLexical(StringBuilder sb, Item item) {
        Name n = item.nodeName();
        if (n != null) {
            String p = n.getPrefix();
            if (p != null && !p.isEmpty()) {
                sb.append(p).append(':').append(n.getLocalName());
            } else {
                sb.append(n.getLocalName());
            }
        }
    }

    /**
     * Serializes a map item as a JSON object shape (with optional TYSON type prefix before "{").
     *
     * @param optionalPrefixBeforeOpenBrace e.g. TYSON {@code ("type") }; null or empty for JSON / hybrid
     */
    public static void serializeMapAsJsonSafeObject(
            Serializer serializer,
            SerializationParameters params,
            Item mapItem,
            StringBuilder sb,
            String indent,
            String optionalPrefixBeforeOpenBrace
    ) {
        if (optionalPrefixBeforeOpenBrace != null && !optionalPrefixBeforeOpenBrace.isEmpty()) {
            sb.append(optionalPrefixBeforeOpenBrace);
        }
        sb.append("{");
        String separator = "";
        if (params.getIndent()) {
            separator = "\n" + indent + "  ";
        }
        boolean firstTime = true;
        for (Item key : mapItem.getItemKeys()) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            sb.append("\"");
            appendJsonEscapedString(sb, key.getStringValue(), params);
            sb.append("\"").append(":");
            if (params.getIndent()) {
                sb.append(" ");
            }
            appendMapValue(serializer, params, mapItem, key, sb, indent);
        }
        if (params.getIndent()) {
            sb.append("\n").append(indent);
        }
        sb.append("}");
    }

    private static void appendMapValue(
            Serializer serializer,
            SerializationParameters params,
            Item mapItem,
            Item key,
            StringBuilder sb,
            String indent
    ) {
        List<Item> sequence = mapItem.getSequenceByKey(key);
        if (sequence == null || sequence.isEmpty()) {
            sb.append("null");
            return;
        }
        if (sequence.size() == 1) {
            if (params.getIndent()) {
                serializer.serialize(sequence.get(0), sb, indent + "  ", false);
            } else {
                serializer.serialize(sequence.get(0), sb, "", false);
            }
            return;
        }
        sb.append("[");
        String separator = "";
        if (params.getIndent()) {
            separator = "\n" + indent + "    ";
        }
        boolean firstTime = true;
        for (Item value : sequence) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            if (params.getIndent()) {
                serializer.serialize(value, sb, indent + "    ", false);
            } else {
                serializer.serialize(value, sb, "", false);
            }
        }
        if (params.getIndent()) {
            sb.append("\n").append(indent).append("  ");
        }
        sb.append("]");
    }

    public static void appendJsonEscapedString(
            StringBuilder sb,
            String value,
            SerializationParameters params
    ) {
        CharsetEncoder encoder = getCharsetEncoder(params);
        for (int i = 0; i < value.length();) {
            int codePoint = value.codePointAt(i);
            i += Character.charCount(codePoint);
            appendJsonEscapedCodePoint(sb, codePoint, encoder);
        }
    }

    private static CharsetEncoder getCharsetEncoder(SerializationParameters params) {
        String encoding = params.getEncoding() == null ? "UTF-8" : params.getEncoding();
        try {
            Charset charset = Charset.forName(encoding);
            return charset.newEncoder();
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new RumbleException(
                    "Unsupported serialization encoding: " + encoding,
                    new ErrorCode(new Name(Name.ERROR_NS, "err", "SESU0007")),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    private static void appendJsonEscapedCodePoint(
            StringBuilder sb,
            int codePoint,
            CharsetEncoder encoder
    ) {
        switch (codePoint) {
            case '"':
                sb.append("\\\"");
                return;
            case '\\':
                sb.append("\\\\");
                return;
            case '/':
                sb.append("\\/");
                return;
            case '\b':
                sb.append("\\b");
                return;
            case '\f':
                sb.append("\\f");
                return;
            case '\n':
                sb.append("\\n");
                return;
            case '\r':
                sb.append("\\r");
                return;
            case '\t':
                sb.append("\\t");
                return;
            default:
                break;
        }

        if ((codePoint >= 0x00 && codePoint <= 0x1F) || (codePoint >= 0x7F && codePoint <= 0x9F)) {
            appendJsonUnicodeEscape(sb, codePoint);
            return;
        }

        String asString = new String(Character.toChars(codePoint));
        if (!encoder.canEncode(CharBuffer.wrap(asString))) {
            if (codePoint <= 0xFFFF) {
                appendJsonUnicodeEscape(sb, codePoint);
            } else {
                for (char surrogate : asString.toCharArray()) {
                    appendJsonUnicodeEscape(sb, surrogate);
                }
            }
            return;
        }

        sb.append(asString);
    }

    private static void appendJsonUnicodeEscape(StringBuilder sb, int codeUnit) {
        sb.append("\\u");
        String hex = Integer.toHexString(codeUnit).toUpperCase();
        for (int i = hex.length(); i < 4; i++) {
            sb.append('0');
        }
        sb.append(hex);
    }
}
