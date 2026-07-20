package org.rumbledb.serialization;

import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonSerializer implements Serializer, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final org.rumbledb.serialization.SerializationParameters params;

    public JsonSerializer(SerializationParameters params) {
        this.params = params;
    }

    @Override
    public String serialize(Item i) {
        StringBuilder sb = new StringBuilder();
        serialize(i, sb, "", true);
        return sb.toString();
    }

    @Override
    public void serialize(Item item, StringBuilder sb, String indent, boolean isTopLevel) {
        if (item.isFunction()) {
            throw new FunctionsNonSerializableException();
        }
        if (item.isAtomic()) {
            appendJSONAtomicItem(item, sb);
            return;
        }
        if (item.isArray()) {
            sb.append("[");
            if (item.isArrayOfItems()) {
                appendArrayMembers(item.getItemMembers(), sb, indent);
            } else {
                appendArraySequenceMembers(item.getSequenceMembers(), sb, indent);
            }
            if (this.params.getIndent()) {
                sb.append("\n").append(indent);
            }
            sb.append("]");
            return;
        }
        if (item.isObject()) {
            sb.append("{");
            String separator = "";
            if (this.params.getIndent()) {
                separator = "\n" + indent + "  ";
            }
            boolean firstTime = true;
            for (String key : item.getStringKeys()) {
                sb.append(separator);
                if (firstTime) {
                    separator = "," + separator;
                    firstTime = false;
                }
                Item value = item.getItemByKey(key);
                sb.append("\"").append(StringEscapeUtils.escapeJson(key)).append("\"").append(":");
                if (this.params.getIndent()) {
                    sb.append(" ");
                    serialize(value, sb, indent + "  ", false);
                } else {
                    serialize(value, sb, "", false);
                }
            }
            if (this.params.getIndent()) {
                sb.append("\n").append(indent);
            }
            sb.append("}");
            return;
        }
        if (item.isMap()) {
            serializeMapAsJsonObject(item, sb, indent);
            return;
        }
        if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                StringBuilder childBuffer = new StringBuilder();
                serialize(child, childBuffer, indent, isTopLevel);
                if (childBuffer.length() > 0 && childBuffer.charAt(childBuffer.length() - 1) == '\n') {
                    childBuffer.setLength(childBuffer.length() - 1);
                }
                sb.append(childBuffer);
            }
            return;
        }
        if (item.isElementNode()) {
            sb.append(indent);
            sb.append("<");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            for (Item attribute : item.attributes()) {
                serialize(attribute, sb, indent, isTopLevel);
            }
            for (Item namespace : item.declaredNamespaceNodes()) {
                serialize(namespace, sb, indent, isTopLevel);
            }
            sb.append(">");
            sb.append("\n");

            for (Item child : item.children()) {
                serialize(child, sb, indent + "  ", isTopLevel);
            }
            sb.append(indent);
            sb.append("</");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            sb.append(">");
            sb.append("\n");
            return;
        }
        if (item.isNamespaceNode()) {
            throw jsonSerializationError(
                "JSON serialization does not support attribute or namespace nodes.",
                "SENR0001"
            );
        }
        if (item.isAttributeNode()) {
            throw jsonSerializationError(
                "JSON serialization does not support attribute or namespace nodes.",
                "SENR0001"
            );
        }
        if (item.isProcessingInstructionNode()) {
            sb.append(indent);
            sb.append("<?");
            SerializerUtils.appendDmNodeNameLexical(sb, item);
            String content = item.getStringValue();
            if (content != null && !content.isEmpty()) {
                sb.append(" ");
                sb.append(content);
            }
            sb.append("?>");
            sb.append("\n");
            return;
        }
        if (item.isTextNode()) {
            sb.append(indent);
            sb.append(item.getStringValue());
            sb.append("\n");
            return;
        }
        if (item.isCommentNode()) {
            sb.append(indent);
            sb.append("<!--");
            sb.append(item.getStringValue());
            sb.append("-->");
            sb.append("\n");
        }
    }

    private void appendJSONAtomicItem(Item item, StringBuilder sb) {
        if (item.isNull()) {
            sb.append("null");
            return;
        }
        boolean isStringValue = item.isAtomic() && !item.isNumeric() && !item.isBoolean() && !item.isNull();
        if (item.isDouble()) {
            if (Double.isNaN(item.getDoubleValue()) || Double.isInfinite(item.getDoubleValue())) {
                throw jsonSerializationError(
                    "JSON serialization does not allow NaN or infinite xs:double values.",
                    "SERE0020"
                );
            }
        }
        if (item.isFloat()) {
            if (Float.isNaN(item.getFloatValue()) || Float.isInfinite(item.getFloatValue())) {
                throw jsonSerializationError(
                    "JSON serialization does not allow NaN or infinite xs:float values.",
                    "SERE0020"
                );
            }
        }
        if (isStringValue) {
            sb.append("\"");
            sb.append(StringEscapeUtils.escapeJson(item.getStringValue()));
            sb.append("\"");
        } else {
            sb.append(item.getStringValue());
        }
    }

    private void appendArrayMembers(List<Item> members, StringBuilder sb, String indent) {
        String separator = "";
        if (this.params.getIndent()) {
            separator = "\n" + indent + "  ";
        }
        boolean firstTime = true;
        for (Item member : members) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            if (this.params.getIndent()) {
                serialize(member, sb, indent + "  ", false);
            } else {
                serialize(member, sb, "", false);
            }
        }
    }

    private void appendArraySequenceMembers(List<List<Item>> memberSequences, StringBuilder sb, String indent) {
        String separator = "";
        if (this.params.getIndent()) {
            separator = "\n" + indent + "  ";
        }
        boolean firstTime = true;
        for (List<Item> memberSequence : memberSequences) {
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            if (memberSequence != null && memberSequence.size() > 1) {
                throw jsonSerializationError(
                    "JSON serialization does not allow sequences of length greater than one inside arrays.",
                    "SERE0023"
                );
            }
            appendJsonSequenceAsValue(memberSequence, sb, indent + "  ");
        }
    }

    private void appendJsonSequenceAsValue(List<Item> sequence, StringBuilder sb, String indent) {
        if (sequence == null || sequence.isEmpty()) {
            sb.append("null");
            return;
        }
        if (sequence.size() == 1) {
            if (this.params.getIndent()) {
                serialize(sequence.get(0), sb, indent, false);
            } else {
                serialize(sequence.get(0), sb, "", false);
            }
            return;
        }
        throw jsonSerializationError(
            "JSON serialization does not allow sequences of length greater than one as a value.",
            "SERE0023"
        );
    }

    private void serializeMapAsJsonObject(Item mapItem, StringBuilder sb, String indent) {
        sb.append("{");
        String separator = "";
        if (this.params.getIndent()) {
            separator = "\n" + indent + "  ";
        }
        boolean firstTime = true;
        Set<String> serializedKeys = this.params.getAllowDuplicateNames() ? null : new HashSet<>();
        for (Item key : mapItem.getItemKeys()) {
            String keyString = key.getStringValue();
            if (serializedKeys != null && !serializedKeys.add(keyString)) {
                throw jsonSerializationError(
                    "JSON serialization produced duplicate names after key stringification.",
                    "SERE0022"
                );
            }
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            sb.append("\"").append(StringEscapeUtils.escapeJson(keyString)).append("\"").append(":");
            if (this.params.getIndent()) {
                sb.append(" ");
            }
            appendJsonSequenceAsValue(mapItem.getSequenceByKey(key), sb, indent + "  ");
        }
        if (this.params.getIndent()) {
            sb.append("\n").append(indent);
        }
        sb.append("}");
    }

    private RumbleException jsonSerializationError(String message, String errorCode) {
        return new RumbleException(
                message,
                new ErrorCode(new Name(Name.ERROR_NS, "err", errorCode)),
                ExceptionMetadata.EMPTY_METADATA
        );
    }
}
