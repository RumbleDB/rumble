package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.JSONLiteralParsingUtils;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializers;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class XMLToJsonFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
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
        Item value = parseInputItem(input);
        return ItemFactory.getInstance().createStringItem(serializeAsJson(value, indent));
    }

    private Item materializeInput(DynamicContext context) {
        try {
            return this.getChild(0).materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "fn:xml-to-json expects at most one input item [err:XPTY0004].",
                    getMetadata()
            );
        }
    }

    private boolean resolveIndentOption(DynamicContext context) {
        if (this.getChildren().size() < 2) {
            return false;
        }

        Item options;
        try {
            options = this.getChild(1).materializeAtMostOneItemOrNull(context);
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

    private Item parseInputItem(Item input) {
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

    private Item parseJsonElement(Item element, boolean allowKeyAttribute) {
        assertJsonElementName(element);
        validateAttributes(element, allowKeyAttribute);

        String localName = element.nodeName().getLocalName();
        return switch (localName) {
            case "map" -> parseMap(element);
            case "array" -> parseArray(element);
            case "string" -> parseStringValue(element);
            case "number" -> parseNumberValue(element);
            case "boolean" -> ItemFactory.getInstance().createBooleanItem(parseBooleanValue(element));
            case "null" -> {
                ensureOnlyScalarChildren(element);
                yield ItemFactory.getInstance().createNullItem();
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

    private Item parseMap(Item element) {
        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();
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

            String key = extractRequiredKey(child);
            if (keys.contains(key)) {
                throw invalidRepresentation("Duplicate keys are not permitted in the XML representation of JSON.");
            }
            keys.add(key);
            values.add(parseJsonElement(child, true));
        }
        return ItemFactory.getInstance().createObjectItem(keys, values, getMetadata(), false);
    }

    private Item parseArray(Item element) {
        List<Item> members = new ArrayList<>();
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
        return ItemFactory.getInstance().createArrayItem(members, false);
    }

    private String extractRequiredKey(Item element) {
        Item keyAttribute = getAttribute(element, "key");
        if (keyAttribute == null) {
            throw invalidRepresentation("Members of a map must carry a key attribute.");
        }
        String key = keyAttribute.getStringValue();
        if (isBooleanLikeAttributeTrue(getAttributeValue(element, "escaped-key"))) {
            return decodeEscapedJsonString(key);
        }
        return key;
    }

    private Item parseStringValue(Item element) {
        String stringValue = collectScalarTextContent(element);
        if (isBooleanLikeAttributeTrue(getAttributeValue(element, "escaped"))) {
            return ItemFactory.getInstance().createStringItem(decodeEscapedJsonString(stringValue));
        }
        return ItemFactory.getInstance().createStringItem(stringValue);
    }

    private Item parseNumberValue(Item element) {
        String stringValue = collectScalarTextContent(element).trim();
        if (stringValue.isEmpty()) {
            throw invalidRepresentation("A number element must not be empty.");
        }
        if (JSON_NUMBER_PATTERN.matcher(stringValue).matches()) {
            return JSONLiteralParsingUtils.getItemFromJSONNumber(
                stringValue,
                JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE
            );
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
                return ItemFactory.getInstance().createDoubleItem(-0d);
            }
            return JSONLiteralParsingUtils.getItemFromJSONNumber(normalized, JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE);
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

    private String decodeEscapedJsonString(String content) {
        StringBuilder decoded = new StringBuilder();
        int index = 0;
        while (index < content.length()) {
            char current = content.charAt(index);
            if (current != '\\') {
                decoded.append(current);
                index++;
                continue;
            }
            try {
                JSONLiteralParsingUtils.DecodedEscape decodedEscape =
                    JSONLiteralParsingUtils.decodeEscapeSequence(content, index);
                decoded.append(decodedEscape.getDecodedText());
                index = decodedEscape.getNextIndex();
            } catch (IllegalArgumentException e) {
                throw invalidEscape(e.getMessage());
            }
        }
        return decoded.toString();
    }

    private String serializeAsJson(Item value, boolean indent) {
        SerializationParameters params = SerializationParameters.defaults();
        params.setMethod("json");
        params.setEncoding("UTF-8");
        params.setIndent(indent);
        params.setItemSeparator("\n");
        String serialized = Serializers.from(params).serialize(value);
        return indent ? serialized : compactJson(serialized);
    }

    private String compactJson(String serialized) {
        StringBuilder result = new StringBuilder(serialized.length());
        boolean inString = false;
        boolean escaping = false;
        for (int i = 0; i < serialized.length(); i++) {
            char current = serialized.charAt(i);
            if (inString) {
                result.append(current);
                if (escaping) {
                    escaping = false;
                } else if (current == '\\') {
                    escaping = true;
                } else if (current == '"') {
                    inString = false;
                }
                continue;
            }
            if (Character.isWhitespace(current)) {
                continue;
            }
            result.append(current);
            if (current == '"') {
                inString = true;
            }
        }
        return result.toString();
    }

    private RumbleException invalidRepresentation(String message) {
        return new RumbleException(message, ErrorCode.InvalidXMLRepresentationOfJSON, getMetadata());
    }

    private RumbleException invalidEscape(String message) {
        return new RumbleException(message, ErrorCode.InvalidEscapeSequenceJSON, getMetadata());
    }
}
