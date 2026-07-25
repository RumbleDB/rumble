package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionsNonSerializableException;
import org.rumbledb.exceptions.RumbleException;

import java.io.Serial;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonSerializer implements Serializer, java.io.Serializable {

    @Serial
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
        if (
            item.isDocumentNode()
                || item.isElementNode()
                || item.isProcessingInstructionNode()
                || item.isTextNode()
                || item.isCommentNode()
        ) {
            appendJsonString(serializeNodeAsString(item), sb);
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
                appendJsonString(key, sb);
                sb.append(":");
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
        if (item.isCommentNode()) {
            appendJsonString(serializeNodeAsString(item), sb);
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
            appendJsonString(item.getStringValue(), sb);
        } else {
            sb.append(item.getStringValue());
        }
    }

    private void appendJsonString(String value, StringBuilder sb) {
        sb.append("\"");
        appendJsonStringContent(value, sb);
        sb.append("\"");
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
                    "JSON serialization does not allow duplicate map key string values.",
                    "SERE0022"
                );
            }
            sb.append(separator);
            if (firstTime) {
                separator = "," + separator;
                firstTime = false;
            }
            appendJsonString(keyString, sb);
            sb.append(":");
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

    private String serializeNodeAsString(Item item) {
        SerializationParameters nodeParams = SerializationParameters.defaults();
        nodeParams.setIndent(false);
        SerializationParameters.JsonNodeOutputMethod nodeOutputMethod = this.params.getJsonNodeOutputMethod();
        Serializer serializer;
        if (
            nodeOutputMethod == null
                || nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.UNSPECIFIED
                || nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.XML
        ) {
            nodeParams.setMethod("xml");
            nodeParams.setOmitXmlDeclaration(true);
            serializer = new XmlSerializer(nodeParams);
        } else if (nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.XHTML) {
            nodeParams.setMethod("xhtml");
            nodeParams.setOmitXmlDeclaration(true);
            serializer = new XhtmlSerializer(nodeParams);
        } else if (nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.HTML) {
            nodeParams.setMethod("html");
            serializer = new HtmlSerializer(nodeParams);
        } else if (nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.TEXT) {
            nodeParams.setMethod("text");
            serializer = new TextSerializer(nodeParams);
        } else if (nodeOutputMethod == SerializationParameters.JsonNodeOutputMethod.JSON) {
            nodeParams.setMethod("json");
            serializer = new JsonSerializer(nodeParams);
        } else {
            nodeParams.setMethod("xml");
            nodeParams.setOmitXmlDeclaration(true);
            serializer = new XmlSerializer(nodeParams);
        }
        return serializer.serialize(item);
    }

    private String applyNormalization(String value) {
        String normalizationForm = this.params.getNormalizationForm();
        if (normalizationForm == null || normalizationForm.equals("none")) {
            return value;
        }
        if (normalizationForm.equals("NFC")) {
            return Normalizer.normalize(value, Normalizer.Form.NFC);
        }
        return value;
    }

    private void appendJsonStringContent(String value, StringBuilder sb) {
        if (value == null || value.isEmpty()) {
            return;
        }
        StringBuilder pendingUnmapped = new StringBuilder();
        Map<String, String> characterMaps = this.params.getCharacterMaps();
        for (int index = 0; index < value.length();) {
            int codePoint = value.codePointAt(index);
            String current = new String(Character.toChars(codePoint));
            String replacement = characterMaps == null ? null : characterMaps.get(current);
            if (replacement != null) {
                appendEscapedNormalizedUnmappedRun(pendingUnmapped, sb);
                sb.append(replacement);
            } else {
                pendingUnmapped.append(current);
            }
            index += Character.charCount(codePoint);
        }
        appendEscapedNormalizedUnmappedRun(pendingUnmapped, sb);
    }

    private void appendEscapedNormalizedUnmappedRun(StringBuilder pendingUnmapped, StringBuilder sb) {
        if (pendingUnmapped.length() == 0) {
            return;
        }
        String normalized = applyNormalization(pendingUnmapped.toString());
        pendingUnmapped.setLength(0);
        SerializerUtils.appendJsonEscapedString(sb, normalized, this.params);
    }

    private RumbleException jsonSerializationError(String message, String errorCode) {
        return new RumbleException(
                message,
                new ErrorCode(new Name(Name.ERROR_NS, "err", errorCode)),
                ExceptionMetadata.EMPTY_METADATA
        );
    }
}
