package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.List;

/**
 * Shared helpers for {@link Serializer} implementations (map serialization, DM node names).
 */
public final class SerializerUtils {

    private SerializerUtils() {
    }

    public static void appendDmNodeNameLexical(StringBuffer sb, Item item) {
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
            StringBuffer sb,
            String indent,
            String optionalPrefixBeforeOpenBrace
    ) {
        if (optionalPrefixBeforeOpenBrace != null && !optionalPrefixBeforeOpenBrace.isEmpty()) {
            sb.append(optionalPrefixBeforeOpenBrace);
        }
        sb.append("{");
        String separator = " ";
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
            sb.append("\"").append(StringEscapeUtils.escapeJson(key.getStringValue())).append("\"").append(" : ");
            appendMapValue(serializer, params, mapItem, key, sb, indent);
        }
        if (params.getIndent()) {
            sb.append("\n").append(indent);
        } else {
            sb.append(" ");
        }
        sb.append("}");
    }

    private static void appendMapValue(
            Serializer serializer,
            SerializationParameters params,
            Item mapItem,
            Item key,
            StringBuffer sb,
            String indent
    ) {
        List<Item> sequence = mapItem.getSequenceByKey(key);
        if (sequence == null || sequence.isEmpty()) {
            sb.append("[]");
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
        String separator = " ";
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
        } else {
            sb.append(" ");
        }
        sb.append("]");
    }
}
