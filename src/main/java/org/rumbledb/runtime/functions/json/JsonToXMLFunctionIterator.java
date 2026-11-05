/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.json;

import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.SequenceType;
import org.rumbledb.xml.schema.XmlSchemaCatalogLoader;
import org.rumbledb.xml.schema.XmlSchemaValidator;

public class JsonToXMLFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
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

    public JsonToXMLFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item jsonText = this.getChild(0).materializeFirstOrNull(context);
        if (jsonText == null) {
            return null;
        }

        boolean liberal = false;
        boolean escape = false;
        boolean validate = false;
        String duplicates = DUPLICATES_RETAIN;
        Item fallback = null;
        if (this.getChildren().size() > 1) {
            Item optionsItem = this.getChild(1).materializeFirstOrNull(context);
            if (optionsItem == null || !optionsItem.isMap()) {
                throw new UnexpectedTypeException(
                        "The options argument of fn:json-to-xml must be a map item [err:XPTY0004].", getMetadata());
            }
            liberal = readBooleanOption(optionsItem, "liberal", false);
            escape = readBooleanOption(optionsItem, "escape", false);
            validate = readBooleanOption(optionsItem, "validate", false);
            duplicates = readDuplicatesOption(optionsItem, validate);
            checkFallbackOption(optionsItem, escape);
            List<Item> fallbackValue = optionsItem.getSequenceByKey("fallback");
            fallback = fallbackValue == null ? null : fallbackValue.get(0);
        }
        if (validate && DUPLICATES_RETAIN.equals(duplicates)) {
            throw new InvalidOptionException(
                    "fn:json-to-xml: duplicates='retain' is incompatible with validate=true().", getMetadata());
        }
        JsonReader reader = new JsonReader(new StringReader(jsonText.getStringValue()));
        reader.setStrictness(liberal ? Strictness.LENIENT : Strictness.STRICT);
        Item root;
        try {
            root = parseValue(reader, null, new ParseOptions(escape, fallback, duplicates, context));
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

        DocumentItem documentItem = ItemFactory.getInstance().createXmlDocumentNode(List.of(root));
        documentItem.setConstructionBaseUri(this.staticContext.getStaticURI());
        boolean removeParentPointers =
                context.getRumbleConfiguration().optimization().optimizeParentPointers();
        if (!removeParentPointers) {
            documentItem.addParentToDescendants();
        }
        documentItem.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);
        if (validate) {
            return new XmlSchemaValidator(XmlSchemaCatalogLoader.loadXPathFunctionsCatalog())
                    .validateStrict(documentItem, getMetadata());
        }
        return documentItem;
    }

    private record ParseOptions(boolean escape, Item fallback, String duplicates, DynamicContext context) {}

    private Item parseValue(JsonReader reader, String key, ParseOptions options) throws IOException {
        List<Item> attributes = new ArrayList<>();
        if (key != null) {
            String convertedKey = convertString(key, options);
            attributes.add(ItemFactory.getInstance().createXmlAttributeNode(KEY_NAME, convertedKey));
            addEscapedAttribute(attributes, "escaped-key", convertedKey, options);
        }

        JsonToken token = reader.peek();
        switch (token) {
            case NULL:
                reader.nextNull();
                return declareNamespace(
                        ItemFactory.getInstance().createXmlElementNode(NULL_NAME, List.of(), attributes));
            case BOOLEAN:
                return textElement(BOOLEAN_NAME, reader.nextBoolean() ? "true" : "false", attributes);
            case NUMBER:
                // nextString() on a NUMBER token returns the raw lexical text, preserving the exact
                // JSON representation as required by the spec.
                return textElement(NUMBER_NAME, reader.nextString(), attributes);
            case STRING: {
                String value = convertString(reader.nextString(), options);
                addEscapedAttribute(attributes, "escaped", value, options);
                return textElement(STRING_NAME, value, attributes);
            }
            case BEGIN_ARRAY: {
                reader.beginArray();
                List<Item> children = new ArrayList<>();
                while (reader.hasNext()) {
                    children.add(parseValue(reader, null, options));
                }
                reader.endArray();
                return declareNamespace(
                        ItemFactory.getInstance().createXmlElementNode(ARRAY_NAME, children, attributes));
            }
            case BEGIN_OBJECT: {
                reader.beginObject();
                List<Item> children = new ArrayList<>();
                Set<String> seenKeys = new HashSet<>();
                while (reader.hasNext()) {
                    String childKey = reader.nextName();
                    String comparisonKey = options.escape() ? convertString(childKey, options) : childKey;
                    boolean isDuplicate = !seenKeys.add(comparisonKey);
                    if (isDuplicate && DUPLICATES_REJECT.equals(options.duplicates())) {
                        throw new DuplicateJSONKeyException(
                                "fn:json-to-xml: duplicate key '" + childKey + "'", getMetadata());
                    }
                    if (isDuplicate && DUPLICATES_USE_FIRST.equals(options.duplicates())) {
                        reader.skipValue();
                        continue;
                    }
                    children.add(parseValue(reader, childKey, options));
                }
                reader.endObject();
                return declareNamespace(ItemFactory.getInstance().createXmlElementNode(MAP_NAME, children, attributes));
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
                    getMetadata());
        }
        return sequence.get(0).getBooleanValue();
    }

    private String readDuplicatesOption(Item optionsItem, boolean validate) {
        List<Item> sequence = optionsItem.getSequenceByKey("duplicates");
        if (sequence == null) {
            return validate ? DUPLICATES_REJECT : DUPLICATES_RETAIN;
        }
        if (sequence.size() != 1 || !sequence.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "The 'duplicates' option of fn:json-to-xml must be a single xs:string [err:XPTY0004].",
                    getMetadata());
        }
        String value = sequence.get(0).getStringValue();
        if (!DUPLICATES_REJECT.equals(value)
                && !DUPLICATES_USE_FIRST.equals(value)
                && !DUPLICATES_RETAIN.equals(value)) {
            throw new InvalidOptionException(
                    "fn:json-to-xml: invalid value for option 'duplicates': '" + value + "'", getMetadata());
        }
        return value;
    }

    private void checkFallbackOption(Item optionsItem, boolean escape) {
        List<Item> sequence = optionsItem.getSequenceByKey("fallback");
        if (sequence == null) {
            return;
        }
        if (sequence.size() != 1 || sequence.get(0) == null || !sequence.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Invalid value for option 'fallback': expected exactly one function item [err:XPTY0004].",
                    getMetadata());
        }
        List<Name> parameterNames = sequence.get(0).getParameterNames();
        if (parameterNames == null || parameterNames.size() != 1) {
            throw new UnexpectedTypeException(
                    "Invalid value for option 'fallback': expected a function of arity 1 [err:XPTY0004].",
                    getMetadata());
        }
        if (escape) {
            throw new InvalidOptionException(
                    "fn:json-to-xml: option 'fallback' cannot be supplied when option 'escape' is true.",
                    getMetadata());
        }
    }

    private InvalidJSONException invalidJson(String message) {
        return new InvalidJSONException("fn:json-to-xml: " + message, getMetadata());
    }

    private static void addEscapedAttribute(List<Item> attributes, String name, String value, ParseOptions options) {
        if (options.escape() && value.indexOf('\\') >= 0) {
            attributes.add(ItemFactory.getInstance().createXmlAttributeNode(new Name(null, null, name), "true"));
        }
    }

    private String convertString(String text, ParseOptions options) {
        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); ) {
            int codePoint = text.codePointAt(i);
            if (options.escape()
                    && (codePoint <= 0x1F
                            || (codePoint >= 0x7F && codePoint <= 0x9F)
                            || codePoint == '\\'
                            || !isValidXmlCodePoint(codePoint))) {
                result.append(escapeCodePoint(codePoint));
            } else if (isValidXmlCodePoint(codePoint)) {
                result.appendCodePoint(codePoint);
            } else if (options.fallback() == null) {
                result.append('\uFFFD');
            } else {
                result.append(invokeFallback(options, escapeCodePoint(codePoint)));
            }
            i += Character.charCount(codePoint);
        }
        return result.toString();
    }

    private String invokeFallback(ParseOptions options, String escaped) {
        RuntimeStaticContext localContext = this.staticContext.toBuilder()
                .executionMode(ExecutionMode.LOCAL)
                .staticType(SequenceType.createSequenceType("item*"))
                .build();
        ItemRuntimePlan callback = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(options.fallback(), localContext),
                List.of(new ConstantRuntimeIterator(ItemFactory.getInstance().createStringItem(escaped), localContext)),
                localContext);
        Item replacement;
        try {
            replacement = callback.materializeAtMostOne(options.context());
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException("The fallback function must return one string.", getMetadata());
        }
        if (replacement == null || !replacement.isString()) {
            throw new UnexpectedTypeException("The fallback function must return one string.", getMetadata());
        }
        return replacement.getStringValue();
    }

    private static String escapeCodePoint(int codePoint) {
        return switch (codePoint) {
            case '\\' -> "\\\\";
            case '\b' -> "\\b";
            case '\f' -> "\\f";
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            default -> String.format("\\u%04X", codePoint);
        };
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
