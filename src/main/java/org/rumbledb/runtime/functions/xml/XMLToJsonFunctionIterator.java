package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class XMLToJsonFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private static final String FUNCTIONS_NAMESPACE = "http://www.w3.org/2005/xpath-functions";
    private static final Pattern JSON_NUMBER_PATTERN =
        Pattern.compile("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?");
    private static final Pattern PERMISSIVE_NUMBER_PATTERN =
        Pattern.compile("[+-]?[0-9]+(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?");

    public XMLToJsonFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item input = materializeInput(context);
        if (input == null) {
            return null;
        }

        boolean indent = resolveIndentOption(context);
        JsonValue value = parseInputItem(input);
        return org.rumbledb.items.ItemFactory.getInstance().createStringItem(serialize(value, indent, 0));
    }

    private Item materializeInput(DynamicContext context) {
        try {
            return this.children.get(0).materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "fn:xml-to-json expects at most one input item [err:XPTY0004].",
                    getMetadata()
            );
        }
    }

    private boolean resolveIndentOption(DynamicContext context) {
        if (this.children.size() < 2) {
            return false;
        }

        Item options;
        try {
            options = this.children.get(1).materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "The options argument of fn:xml-to-json must be a single map item [err:XPTY0004].",
                    getMetadata()
            );
        }

        if (options == null) {
            throw new UnexpectedTypeException(
                    "The options argument of fn:xml-to-json must be a map item [err:XPTY0004].",
                    getMetadata()
            );
        }
        if (!options.isMap()) {
            throw new UnexpectedTypeException(
                    "The options argument of fn:xml-to-json must be a map item [err:XPTY0004].",
                    getMetadata()
            );
        }

        List<Item> indentOption = options.getSequenceByKey("indent");
        if (indentOption == null) {
            return false;
        }
        if (indentOption.size() != 1 || !indentOption.get(0).isBoolean()) {
            throw new UnexpectedTypeException(
                    "The indent option of fn:xml-to-json must be a single xs:boolean [err:XPTY0004].",
                    getMetadata()
            );
        }
        return indentOption.get(0).getBooleanValue();
    }

    private JsonValue parseInputItem(Item input) {
        if (input.isDocumentNode()) {
            Item root = extractDocumentElement(input);
            return parseJsonElement(root, true);
        }
        if (!input.isElementNode()) {
            throw new UnexpectedTypeException(
                    "fn:xml-to-json expects an element or document node [err:XPTY0004].",
                    getMetadata()
            );
        }
        return parseJsonElement(input, true);
    }

    private Item extractDocumentElement(Item document) {
        Item root = null;
        for (Item child : document.children()) {
            if (child.isCommentNode()) {
                continue;
            }
            if (child.isTextNode()) {
                if (!child.getStringValue().trim().isEmpty()) {
                    throw invalidRepresentation("Document node contains non-whitespace text.");
                }
                continue;
            }
            if (!child.isElementNode()) {
                throw invalidRepresentation("Document node contains a non-element child.");
            }
            if (root != null) {
                throw invalidRepresentation("Document node contains more than one top-level element.");
            }
            root = child;
        }
        if (root == null) {
            throw invalidRepresentation("Document node does not contain a JSON representation element.");
        }
        return root;
    }

    private JsonValue parseJsonElement(Item element, boolean allowKeyAttribute) {
        assertJsonElementName(element);
        validateAttributes(element, allowKeyAttribute);

        String localName = element.nodeName().getLocalName();
        return switch (localName) {
            case "map" -> parseMap(element);
            case "array" -> parseArray(element);
            case "string" -> parseStringValue(element);
            case "number" -> new JsonNumber(parseNumberValue(element));
            case "boolean" -> new JsonBoolean(parseBooleanValue(element));
            case "null" -> {
                ensureOnlyScalarChildren(element);
                yield JsonNull.INSTANCE;
            }
            default -> throw invalidRepresentation("Unsupported JSON representation element: " + localName + ".");
        };
    }

    private void assertJsonElementName(Item element) {
        Name name = element.nodeName();
        if (name == null || !FUNCTIONS_NAMESPACE.equals(name.getNamespace())) {
            throw invalidRepresentation("Element is not in the XPath functions namespace.");
        }
        String localName = name.getLocalName();
        if (
            !localName.equals("map")
                && !localName.equals("array")
                && !localName.equals("string")
                && !localName.equals("number")
                && !localName.equals("boolean")
                && !localName.equals("null")
        ) {
            throw invalidRepresentation("Element is not part of the XML representation of JSON.");
        }
    }

    private void validateAttributes(Item element, boolean allowKeyAttribute) {
        for (Item attribute : element.attributes()) {
            Name attributeName = attribute.nodeName();
            String namespace = attributeName.getNamespace();
            String localName = attributeName.getLocalName();

            if (FUNCTIONS_NAMESPACE.equals(namespace)) {
                throw invalidRepresentation("Attributes in the XPath functions namespace are not permitted.");
            }
            if (namespace != null && !namespace.isEmpty()) {
                continue;
            }
            if (localName.equals("key")) {
                if (!allowKeyAttribute) {
                    throw invalidRepresentation("A key attribute is not permitted on this element.");
                }
                continue;
            }
            if (localName.equals("escaped") || localName.equals("escaped-key")) {
                validateBooleanLikeAttribute(attribute.getStringValue(), localName);
                continue;
            }
            throw invalidRepresentation("Attribute " + localName + " is not permitted.");
        }
    }

    private JsonObject parseMap(Item element) {
        List<JsonObjectEntry> members = new ArrayList<>();
        LinkedHashMap<String, Boolean> seenKeys = new LinkedHashMap<>();
        for (Item child : element.children()) {
            if (child.isCommentNode()) {
                continue;
            }
            if (child.isTextNode()) {
                if (!child.getStringValue().trim().isEmpty()) {
                    throw invalidRepresentation("A map may not contain non-whitespace text nodes.");
                }
                continue;
            }
            if (!child.isElementNode()) {
                throw invalidRepresentation("A map contains an invalid child node.");
            }

            validateAttributes(child, true);
            JsonString key = extractRequiredKey(child);
            if (seenKeys.containsKey(key.semanticValue)) {
                throw invalidRepresentation("Duplicate keys are not permitted in the XML representation of JSON.");
            }
            seenKeys.put(key.semanticValue, Boolean.TRUE);
            members.add(new JsonObjectEntry(key.serializedValue, parseJsonElement(child, true)));
        }
        return new JsonObject(members);
    }

    private JsonArray parseArray(Item element) {
        List<JsonValue> members = new ArrayList<>();
        for (Item child : element.children()) {
            if (child.isCommentNode()) {
                continue;
            }
            if (child.isTextNode()) {
                if (!child.getStringValue().trim().isEmpty()) {
                    throw invalidRepresentation("An array may not contain non-whitespace text nodes.");
                }
                continue;
            }
            if (!child.isElementNode()) {
                throw invalidRepresentation("An array contains an invalid child node.");
            }
            members.add(parseJsonElement(child, false));
        }
        return new JsonArray(members);
    }

    private JsonString extractRequiredKey(Item element) {
        Item keyAttribute = getAttribute(element, "key");
        if (keyAttribute == null) {
            throw invalidRepresentation("Members of a map must carry a key attribute.");
        }
        String key = keyAttribute.getStringValue();
        if (isBooleanLikeAttributeTrue(getAttributeValue(element, "escaped-key"))) {
            EscapedJsonString escapedKey = normalizeEscapedJsonString(key);
            return new JsonString(escapedKey.semanticValue, escapedKey.serializedValue);
        }
        return new JsonString(key, escapeJsonString(key));
    }

    private JsonString parseStringValue(Item element) {
        String stringValue = collectScalarTextContent(element);
        if (isBooleanLikeAttributeTrue(getAttributeValue(element, "escaped"))) {
            EscapedJsonString escapedValue = normalizeEscapedJsonString(stringValue);
            return new JsonString(escapedValue.semanticValue, escapedValue.serializedValue);
        }
        return new JsonString(stringValue, escapeJsonString(stringValue));
    }

    private String parseNumberValue(Item element) {
        String stringValue = collectScalarTextContent(element).trim();
        if (stringValue.isEmpty()) {
            throw invalidRepresentation("A number element must not be empty.");
        }
        if (JSON_NUMBER_PATTERN.matcher(stringValue).matches()) {
            return stringValue;
        }
        if (!PERMISSIVE_NUMBER_PATTERN.matcher(stringValue).matches()) {
            throw invalidRepresentation("Invalid lexical representation for a JSON number.");
        }
        try {
            BigDecimal value = new BigDecimal(stringValue);
            String normalized = value.stripTrailingZeros().toString();
            if (normalized.contains("E+")) {
                normalized = normalized.replace("E+", "E");
            }
            if (normalized.equals("0") && stringValue.startsWith("-")) {
                return "-0";
            }
            return normalized;
        } catch (NumberFormatException e) {
            throw invalidRepresentation("Invalid lexical representation for a JSON number.");
        }
    }

    private boolean parseBooleanValue(Item element) {
        String stringValue = collectScalarTextContent(element).trim();
        if (stringValue.equals("true")) {
            return true;
        }
        if (stringValue.equals("false")) {
            return false;
        }
        throw invalidRepresentation("Invalid lexical representation for a JSON boolean.");
    }

    private void ensureOnlyScalarChildren(Item element) {
        for (Item child : element.children()) {
            if (child.isCommentNode()) {
                continue;
            }
            if (child.isTextNode()) {
                if (!child.getStringValue().trim().isEmpty()) {
                    throw invalidRepresentation("A null element may not contain text.");
                }
                continue;
            }
            throw invalidRepresentation("A scalar JSON representation element contains an invalid child.");
        }
    }

    private String collectScalarTextContent(Item element) {
        StringBuilder result = new StringBuilder();
        for (Item child : element.children()) {
            if (child.isCommentNode()) {
                continue;
            }
            if (child.isTextNode()) {
                result.append(child.getStringValue());
                continue;
            }
            throw invalidRepresentation("A scalar JSON representation element contains an invalid child.");
        }
        return result.toString();
    }

    private Item getAttribute(Item element, String localName) {
        for (Item attribute : element.attributes()) {
            Name attributeName = attribute.nodeName();
            String namespace = attributeName.getNamespace();
            if ((namespace == null || namespace.isEmpty()) && localName.equals(attributeName.getLocalName())) {
                return attribute;
            }
        }
        return null;
    }

    private String getAttributeValue(Item element, String localName) {
        Item attribute = getAttribute(element, localName);
        return attribute == null ? null : attribute.getStringValue();
    }

    private void validateBooleanLikeAttribute(String value, String attributeName) {
        if (value == null) {
            return;
        }
        String normalized = value.trim();
        if (
            normalized.equals("true")
                || normalized.equals("false")
                || normalized.equals("1")
                || normalized.equals("0")
        ) {
            return;
        }
        throw invalidRepresentation("Attribute " + attributeName + " must have a boolean value.");
    }

    private boolean isBooleanLikeAttributeTrue(String value) {
        if (value == null) {
            return false;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("true") || normalized.equals("1")) {
            return true;
        }
        if (normalized.equals("false") || normalized.equals("0")) {
            return false;
        }
        throw invalidRepresentation("Invalid boolean attribute value.");
    }

    private EscapedJsonString normalizeEscapedJsonString(String content) {
        StringBuilder normalized = new StringBuilder();
        StringBuilder semantic = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c != '\\') {
                appendEscapedCharacter(normalized, c);
                semantic.append(c);
                continue;
            }
            if (i + 1 >= content.length()) {
                throw invalidEscape("Dangling backslash in escaped JSON string.");
            }
            char next = content.charAt(++i);
            switch (next) {
                case '"':
                case '/':
                    normalized.append('\\').append(next);
                    semantic.append(next);
                    break;
                case '\\':
                    normalized.append("\\\\");
                    semantic.append('\\');
                    break;
                case 'b':
                    normalized.append("\\b");
                    semantic.append('\b');
                    break;
                case 'f':
                    normalized.append("\\f");
                    semantic.append('\f');
                    break;
                case 'n':
                    normalized.append("\\n");
                    semantic.append('\n');
                    break;
                case 'r':
                    normalized.append("\\r");
                    semantic.append('\r');
                    break;
                case 't':
                    normalized.append("\\t");
                    semantic.append('\t');
                    break;
                case 'u':
                    if (i + 4 >= content.length()) {
                        throw invalidEscape("Incomplete unicode escape in escaped JSON string.");
                    }
                    String hex = content.substring(i + 1, i + 5);
                    if (!hex.matches("[0-9A-Fa-f]{4}")) {
                        throw invalidEscape("Invalid unicode escape in escaped JSON string.");
                    }
                    normalized.append("\\u").append(hex);
                    semantic.append((char) Integer.parseInt(hex, 16));
                    i += 4;
                    break;
                default:
                    throw invalidEscape("Invalid escape sequence in escaped JSON string.");
            }
        }
        return new EscapedJsonString(semantic.toString(), normalized.toString());
    }

    private void appendEscapedCharacter(StringBuilder builder, char c) {
        switch (c) {
            case '"' -> builder.append("\\\"");
            case '\\' -> builder.append("\\\\");
            case '/' -> builder.append("\\/");
            case '\b' -> builder.append("\\b");
            case '\f' -> builder.append("\\f");
            case '\n' -> builder.append("\\n");
            case '\r' -> builder.append("\\r");
            case '\t' -> builder.append("\\t");
            default -> {
                if ((c >= 0x00 && c <= 0x1F) || (c >= 0x7F && c <= 0x9F)) {
                    builder.append(String.format("\\u%04X", (int) c));
                } else {
                    builder.append(c);
                }
            }
        }
    }

    private String serialize(JsonValue value, boolean indent, int level) {
        if (value instanceof JsonNull) {
            return "null";
        }
        if (value instanceof JsonBoolean jsonBoolean) {
            return jsonBoolean.value ? "true" : "false";
        }
        if (value instanceof JsonNumber jsonNumber) {
            return jsonNumber.lexicalValue;
        }
        if (value instanceof JsonString jsonString) {
            return "\"" + jsonString.serializedValue + "\"";
        }
        if (value instanceof JsonArray jsonArray) {
            return serializeArray(jsonArray, indent, level);
        }
        if (value instanceof JsonObject jsonObject) {
            return serializeObject(jsonObject, indent, level);
        }
        throw new IllegalStateException("Unsupported JSON value.");
    }

    private String serializeArray(JsonArray array, boolean indent, int level) {
        if (array.values.isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < array.values.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            if (indent) {
                result.append("\n").append("  ".repeat(level + 1));
            }
            result.append(serialize(array.values.get(i), indent, level + 1));
        }
        if (indent) {
            result.append("\n").append("  ".repeat(level));
        }
        result.append("]");
        return result.toString();
    }

    private String serializeObject(JsonObject object, boolean indent, int level) {
        if (object.values.isEmpty()) {
            return "{}";
        }
        StringBuilder result = new StringBuilder();
        result.append("{");
        boolean first = true;
        for (JsonObjectEntry entry : object.values) {
            if (!first) {
                result.append(",");
            }
            if (indent) {
                result.append("\n").append("  ".repeat(level + 1));
            }
            result.append("\"")
                .append(entry.serializedKey)
                .append("\":");
            if (indent) {
                result.append(" ");
            }
            result.append(serialize(entry.value, indent, level + 1));
            first = false;
        }
        if (indent) {
            result.append("\n").append("  ".repeat(level));
        }
        result.append("}");
        return result.toString();
    }

    private String escapeJsonString(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            appendEscapedCharacter(result, value.charAt(i));
        }
        return result.toString();
    }

    private RumbleException invalidRepresentation(String message) {
        return new RumbleException(message, ErrorCode.InvalidXMLRepresentationOfJSON, getMetadata());
    }

    private RumbleException invalidEscape(String message) {
        return new RumbleException(message, ErrorCode.InvalidEscapeSequenceJSON, getMetadata());
    }

    private interface JsonValue {
    }

    private static class JsonNull implements JsonValue {
        private static final JsonNull INSTANCE = new JsonNull();
    }

    private static class JsonBoolean implements JsonValue {
        private final boolean value;

        private JsonBoolean(boolean value) {
            this.value = value;
        }
    }

    private static class JsonNumber implements JsonValue {
        private final String lexicalValue;

        private JsonNumber(String lexicalValue) {
            this.lexicalValue = lexicalValue;
        }
    }

    private static class JsonString implements JsonValue {
        private final String semanticValue;
        private final String serializedValue;

        private JsonString(String semanticValue, String serializedValue) {
            this.semanticValue = semanticValue;
            this.serializedValue = serializedValue;
        }
    }

    private static class JsonArray implements JsonValue {
        private final List<JsonValue> values;

        private JsonArray(List<JsonValue> values) {
            this.values = values;
        }
    }

    private static class JsonObject implements JsonValue {
        private final List<JsonObjectEntry> values;

        private JsonObject(List<JsonObjectEntry> values) {
            this.values = values;
        }
    }

    private static class JsonObjectEntry {
        private final String serializedKey;
        private final JsonValue value;

        private JsonObjectEntry(String serializedKey, JsonValue value) {
            this.serializedKey = serializedKey;
            this.value = value;
        }
    }

    private static class EscapedJsonString {
        private final String semanticValue;
        private final String serializedValue;

        private EscapedJsonString(String semanticValue, String serializedValue) {
            this.semanticValue = semanticValue;
            this.serializedValue = serializedValue;
        }
    }
}
