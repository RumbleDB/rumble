package org.rumbledb.runtime.functions.json;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.InvalidJSONException;
import org.rumbledb.exceptions.InvalidOptionException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonToXMLFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Name MAP_NAME = new Name(Name.FN_NS, "", "map");
    private static final Name ARRAY_NAME = new Name(Name.FN_NS, "", "array");
    private static final Name STRING_NAME = new Name(Name.FN_NS, "", "string");
    private static final Name NUMBER_NAME = new Name(Name.FN_NS, "", "number");
    private static final Name BOOLEAN_NAME = new Name(Name.FN_NS, "", "boolean");
    private static final Name NULL_NAME = new Name(Name.FN_NS, "", "null");
    private static final Name KEY_NAME = new Name(null, null, "key");

    private static final String DUPLICATES_REJECT = "reject";
    private static final String DUPLICATES_USE_FIRST = "use-first";
    private static final String DUPLICATES_RETAIN = "retain";

    public JsonToXMLFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(() -> materializeFirstItemOrNull(context), getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item jsonText = this.getChild(0).materializeFirstItemOrNull(context);
        if (jsonText == null) {
            return null;
        }

        boolean liberal = false;
        boolean escape = false;
        String duplicates = DUPLICATES_RETAIN;
        if (this.getChildren().size() > 1) {
            Item optionsItem = this.getChild(1).materializeFirstItemOrNull(context);
            if (optionsItem == null || !optionsItem.isMap()) {
                throw new UnexpectedTypeException(
                        "The options argument of fn:json-to-xml must be a map item [err:XPTY0004].",
                        getMetadata()
                );
            }
            liberal = readBooleanOption(optionsItem, "liberal", false);
            escape = readBooleanOption(optionsItem, "escape", false);
            duplicates = readDuplicatesOption(optionsItem);
            checkValidateOption(optionsItem);
            checkFallbackOption(optionsItem, escape);
        }
        if (escape) {
            throw new UnsupportedFeatureException(
                    "fn:json-to-xml: option 'escape' is not supported yet.",
                    getMetadata()
            );
        }

        JsonReader reader = new JsonReader(new StringReader(jsonText.getStringValue()));
        reader.setStrictness(liberal ? Strictness.LENIENT : Strictness.STRICT);
        Item root;
        try {
            root = parseValue(reader, null, duplicates);
            if (reader.peek() != JsonToken.END_DOCUMENT) {
                throw invalidJson("Unexpected content after the JSON value.");
            }
        } catch (MalformedJsonException | IllegalStateException | NumberFormatException e) {
            throw invalidJson("The argument is not a valid JSON text: " + e.getMessage());
        } catch (IOException e) {
            throw invalidJson("The argument is not a valid JSON text.");
        } finally {
            try {
                reader.close();
            } catch (IOException ignored) {
                // stream is backed by a StringReader, closing cannot meaningfully fail
            }
        }

        Item documentItem = ItemFactory.getInstance().createXmlDocumentNode(List.of(root));
        boolean removeParentPointers = context.getRumbleRuntimeConfiguration().optimizeParentPointers();
        if (!removeParentPointers) {
            documentItem.addParentToDescendants();
        }
        documentItem.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);
        return documentItem;
    }

    private Item parseValue(JsonReader reader, String key, String duplicates) throws IOException {
        List<Item> attributes = new ArrayList<>();
        if (key != null) {
            attributes.add(ItemFactory.getInstance().createXmlAttributeNode(KEY_NAME, sanitizeForXml(key)));
        }

        JsonToken token = reader.peek();
        switch (token) {
            case NULL:
                reader.nextNull();
                return declareNamespace(
                    ItemFactory.getInstance().createXmlElementNode(NULL_NAME, List.of(), attributes)
                );
            case BOOLEAN:
                return textElement(BOOLEAN_NAME, reader.nextBoolean() ? "true" : "false", attributes);
            case NUMBER:
                // nextString() on a NUMBER token returns the raw lexical text, preserving the exact
                // JSON representation as required by the spec.
                return textElement(NUMBER_NAME, reader.nextString(), attributes);
            case STRING:
                return textElement(STRING_NAME, sanitizeForXml(reader.nextString()), attributes);
            case BEGIN_ARRAY: {
                reader.beginArray();
                List<Item> children = new ArrayList<>();
                while (reader.hasNext()) {
                    children.add(parseValue(reader, null, duplicates));
                }
                reader.endArray();
                return declareNamespace(
                    ItemFactory.getInstance().createXmlElementNode(ARRAY_NAME, children, attributes)
                );
            }
            case BEGIN_OBJECT: {
                reader.beginObject();
                List<Item> children = new ArrayList<>();
                Set<String> seenKeys = new HashSet<>();
                while (reader.hasNext()) {
                    String childKey = reader.nextName();
                    boolean isDuplicate = !seenKeys.add(childKey);
                    if (isDuplicate && DUPLICATES_REJECT.equals(duplicates)) {
                        throw new DuplicateJSONKeyException(
                                "fn:json-to-xml: duplicate key '" + childKey + "'",
                                getMetadata()
                        );
                    }
                    if (isDuplicate && DUPLICATES_USE_FIRST.equals(duplicates)) {
                        reader.skipValue();
                        continue;
                    }
                    children.add(parseValue(reader, childKey, duplicates));
                }
                reader.endObject();
                return declareNamespace(
                    ItemFactory.getInstance().createXmlElementNode(MAP_NAME, children, attributes)
                );
            }
            default:
                throw invalidJson("Unexpected JSON token: " + token);
        }
    }

    private Item textElement(Name name, String text, List<Item> attributes) {
        List<Item> children = List.of(ItemFactory.getInstance().createXmlTextNode(text));
        return declareNamespace(ItemFactory.getInstance().createXmlElementNode(name, children, attributes));
    }

    private static Item declareNamespace(Item element) {
        element.addOrReplaceNamespace(ItemFactory.getInstance().createXmlNamespaceNode("", Name.FN_NS));
        return element;
    }

    private boolean readBooleanOption(Item optionsItem, String key, boolean defaultValue) {
        List<Item> sequence = optionsItem.getSequenceByKey(key);
        if (sequence == null) {
            return defaultValue;
        }
        if (sequence.size() != 1 || !sequence.get(0).isBoolean()) {
            throw new UnexpectedTypeException(
                    "The '" + key + "' option of fn:json-to-xml must be a single xs:boolean [err:XPTY0004].",
                    getMetadata()
            );
        }
        return sequence.get(0).getBooleanValue();
    }

    private String readDuplicatesOption(Item optionsItem) {
        List<Item> sequence = optionsItem.getSequenceByKey("duplicates");
        if (sequence == null) {
            return DUPLICATES_RETAIN;
        }
        if (sequence.size() != 1 || !sequence.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "The 'duplicates' option of fn:json-to-xml must be a single xs:string [err:XPTY0004].",
                    getMetadata()
            );
        }
        String value = sequence.get(0).getStringValue();
        if (
            !DUPLICATES_REJECT.equals(value)
                && !DUPLICATES_USE_FIRST.equals(value)
                && !DUPLICATES_RETAIN.equals(value)
        ) {
            throw new InvalidOptionException(
                    "fn:json-to-xml: invalid value for option 'duplicates': '" + value + "'",
                    getMetadata()
            );
        }
        return value;
    }

    private void checkValidateOption(Item optionsItem) {
        List<Item> sequence = optionsItem.getSequenceByKey("validate");
        if (sequence == null) {
            return;
        }
        if (sequence.size() != 1 || !sequence.get(0).isBoolean()) {
            throw new UnexpectedTypeException(
                    "The 'validate' option of fn:json-to-xml must be a single xs:boolean [err:XPTY0004].",
                    getMetadata()
            );
        }
        if (sequence.get(0).getBooleanValue()) {
            throw new UnsupportedFeatureException(
                    "fn:json-to-xml: option 'validate' is not supported yet.",
                    getMetadata()
            );
        }
    }

    private void checkFallbackOption(Item optionsItem, boolean escape) {
        List<Item> sequence = optionsItem.getSequenceByKey("fallback");
        if (sequence == null) {
            return;
        }
        if (sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option 'fallback': expected exactly one function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        List<Name> parameterNames = sequence.get(0).getParameterNames();
        if (parameterNames == null || parameterNames.size() != 1) {
            throw new UnexpectedTypeException(
                    "Invalid value for option 'fallback': expected a function of arity 1 [err:XPTY0004].",
                    getMetadata()
            );
        }
        if (escape) {
            throw new InvalidOptionException(
                    "fn:json-to-xml: option 'fallback' cannot be supplied when option 'escape' is true.",
                    getMetadata()
            );
        }
    }

    private InvalidJSONException invalidJson(String message) {
        return new InvalidJSONException("fn:json-to-xml: " + message, getMetadata());
    }

    private static String sanitizeForXml(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int i = 0;
        while (i < text.length()) {
            int codePoint = text.codePointAt(i);
            if (isValidXmlCodePoint(codePoint)) {
                result.appendCodePoint(codePoint);
            } else {
                result.append('\uFFFD');
            }
            i += Character.charCount(codePoint);
        }
        return result.toString();
    }

    private static boolean isValidXmlCodePoint(int c) {
        return c == 0x9
            || c == 0xA
            || c == 0xD
            || (c >= 0x20 && c <= 0xD7FF)
            || (c >= 0xE000 && c <= 0xFFFD)
            || (c >= 0x10000 && c <= 0x10FFFF);
    }
}
