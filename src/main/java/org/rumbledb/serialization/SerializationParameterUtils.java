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
package org.rumbledb.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.InvalidSerializationParameterValueException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.XmlParsingUtils;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

public final class SerializationParameterUtils {

    public static final String SERIALIZATION_NAMESPACE = "http://www.w3.org/2010/xslt-xquery-serialization";
    private static final Set<String> STANDARD_PARAMETERS = Set.of(
            "method",
            "encoding",
            "version",
            "omit-xml-declaration",
            "standalone",
            "doctype-system",
            "doctype-public",
            "media-type",
            "normalization-form",
            "undeclare-prefixes",
            "include-content-type",
            "escape-uri-attributes",
            "html-version",
            "byte-order-mark",
            "indent",
            "item-separator",
            "allow-duplicate-names",
            "json-node-output-method",
            "use-character-maps",
            "cdata-section-elements",
            "suppress-indentation");

    private SerializationParameterUtils() {}

    // -------------------------------------------------------------------------
    // Public API — fn:serialize / element-document paths
    // -------------------------------------------------------------------------

    public static SerializationParameters defaultsForSerializeFunction(String queryLanguage) {
        SerializationParameters params = new SerializationParameters();
        if (queryLanguage == null || !queryLanguage.startsWith("xquery")) {
            params.setMethod("xml-json-hybrid");
        }
        return params;
    }

    public static void applyParameterItems(
            SerializationParameters params, List<Item> optionsItems, ExceptionMetadata metadata) {
        if (optionsItems == null || optionsItems.isEmpty()) {
            return;
        }
        if (optionsItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:serialize must be a serialization-parameters element or map.", metadata);
        }
        Item options = optionsItems.get(0);
        if (!(options.isMap()
                || options.isObject()
                || (options.isElementNode() && isSerializationParametersElement(options)))) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:serialize must be a serialization-parameters element or map.", metadata);
        }
        applyParameterItem(params, options, null, metadata);
    }

    private static void applyParameterItems(
            SerializationParameters params,
            List<Item> optionsItems,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata) {
        if (optionsItems == null || optionsItems.isEmpty()) {
            return;
        }
        if (optionsItems.size() == 1) {
            applyParameterItem(params, optionsItems.get(0), explicitParameterNames, metadata);
            return;
        }
        for (Item item : optionsItems) {
            if (!item.isElementNode()) {
                throw new InvalidArgumentTypeException(
                        "The second argument of fn:serialize must be a map or serialization parameter elements.",
                        metadata);
            }
        }
        applyParameterElements(params, optionsItems, explicitParameterNames, metadata);
    }

    public static void applyParameterDocument(
            SerializationParameters params,
            StaticContext staticContext,
            String location,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata) {
        URI uri = FileSystemUtil.resolveURI(staticContext.getStaticBaseURI(), location, metadata);
        try (InputStream xmlFileStream = FileSystemUtil.getDataInputStream(uri, metadata)) {
            Document xmlDocument = XmlParsingUtils.parseResource(
                    new InputSource(xmlFileStream),
                    uri.toString(),
                    "serialization parameter document \"" + uri + "\"",
                    metadata);
            Item item = ItemParser.getItemFromXML(
                    xmlDocument,
                    uri.toString(),
                    staticContext.getRumbleConfiguration().optimization().optimizeParentPointers());
            applyParameterItem(params, item, explicitParameterNames, metadata);
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to read the serialization parameter document.", metadata);
            ex.initCause(e);
            throw ex;
        }
    }

    // -------------------------------------------------------------------------
    // Public API — CLI / Spark DataFrameWriter path (lenient parsing)
    // -------------------------------------------------------------------------

    /**
     * Builds a fresh {@link SerializationParameters} from a map of string options.
     * Uses lenient boolean parsing (accepts "yes"/"no"/"true"/"false"/"1"/"0").
     * Intended for CLI / Spark DataFrameWriter options.
     */
    public static SerializationParameters buildFromConfig(Map<String, String> options) {
        return buildFromConfig(options, null);
    }

    /**
     * Builds a fresh {@link SerializationParameters} from a map of string options, using the
     * given query language to determine the default serialization method.
     * Uses lenient boolean parsing. Intended for CLI / Spark DataFrameWriter options.
     */
    public static SerializationParameters buildFromConfig(Map<String, String> options, String queryLanguage) {
        SerializationParameters params = SerializationParameters.defaults(queryLanguage);
        if (options != null) {
            options.forEach((k, v) -> applyConfigOption(params, k, v));
        }
        return params;
    }

    /**
     * Applies a single string-valued option using lenient parsing.
     * Intended for CLI / Spark DataFrameWriter options.
     */
    public static void applyConfigOption(SerializationParameters params, String name, String value) {
        if (value == null) {
            return;
        }
        switch (name) {
            case "method" -> params.setMethod(value.trim());
            case "encoding" -> params.setEncoding(value);
            case "version" -> params.setVersion(value);
            case "omit-xml-declaration" -> params.setOmitXmlDeclaration(parseLenientBoolean(name, value));
            case "standalone" -> params.setStandalone(parseStandaloneConfig(name, value));
            case "doctype-system" -> params.setDoctypeSystem(value);
            case "doctype-public" -> params.setDoctypePublic(value);
            case "media-type" -> params.setMediaType(value);
            case "normalization-form" -> params.setNormalizationForm(value);
            case "undeclare-prefixes" -> params.setUndeclarePrefixes(parseLenientBoolean(name, value));
            case "include-content-type" -> params.setIncludeContentType(parseLenientBoolean(name, value));
            case "escape-uri-attributes" -> params.setEscapeUriAttributes(parseLenientBoolean(name, value));
            case "html-version" -> params.setHtmlVersion(value);
            case "byte-order-mark" -> params.setByteOrderMark(parseLenientBoolean(name, value));
            case "indent" -> params.setIndent(parseLenientBoolean(name, value));
            case "indent-spaces" -> params.setIndentSpaces(parseIndentSpaces(name, value));
            case "item-separator" -> params.setItemSeparator(value);
            case "allow-duplicate-names" -> params.setAllowDuplicateNames(parseLenientBoolean(name, value));
            case "json-node-output-method" -> params.setJsonNodeOutputMethod(
                    parseJsonNodeOutputMethodConfig(name, value));
            case "use-character-maps" -> params.setCharacterMaps(parseCharacterMapsConfig(name, value));
            case "cdata-section-elements" -> params.setCdataSectionElements(parseExpandedQNameSet(value));
            case "suppress-indentation" -> params.setSuppressIndentation(parseExpandedQNameSet(value));
            default -> params.getSparkOptions().put(name, value);
        }
    }

    // -------------------------------------------------------------------------
    // Private — item / element dispatch
    // -------------------------------------------------------------------------

    private static void applyParameterItem(
            SerializationParameters params,
            Item options,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata) {
        if (options.isDocumentNode()) {
            List<Item> elementChildren = new ArrayList<>();
            for (Item child : options.children()) {
                if (child.isElementNode()) {
                    elementChildren.add(child);
                }
            }
            applyParameterItems(params, elementChildren, explicitParameterNames, metadata);
            return;
        }
        if (options.isElementNode()) {
            if (isSerializationParametersElement(options)) {
                if (options.attributes().iterator().hasNext()) {
                    throw invalidParameterDocument("Unexpected serialization-parameters attribute.", metadata);
                }
                List<Item> childElements = new ArrayList<>();
                for (Item child : options.children()) {
                    if (child.isElementNode()) {
                        childElements.add(child);
                    }
                }
                applyParameterElements(params, childElements, explicitParameterNames, metadata);
                return;
            }
            applyParameterElements(params, List.of(options), explicitParameterNames, metadata);
            return;
        }
        if (options.isMap() || options.isObject()) {
            applyParameterMap(params, options, explicitParameterNames, metadata);
            return;
        }
        throw new InvalidArgumentTypeException(
                "The second argument of fn:serialize must be a map or serialization parameter elements.", metadata);
    }

    private static void applyParameterMap(
            SerializationParameters params,
            Item options,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata) {
        for (Item key : options.getItemKeys()) {
            String parameterName = parameterNameFromKey(key, metadata);
            if (parameterName == null) {
                continue;
            }
            if (explicitParameterNames != null && explicitParameterNames.contains(parameterName)) {
                continue;
            }
            List<Item> valueSequence = options.getSequenceByKey(key);
            if ("use-character-maps".equals(parameterName)) {
                params.setCharacterMaps(characterMapsFromMapValue(valueSequence, metadata));
                continue;
            }
            boolean standardParameter = STANDARD_PARAMETERS.contains(parameterName);
            if (standardParameter && (valueSequence == null || valueSequence.isEmpty())) {
                continue;
            }
            String value = standardParameter
                    ? itemValuesToString(parameterName, valueSequence, metadata)
                    : sequenceToParameterValue(parameterName, valueSequence, null, metadata);
            applySpecString(params, parameterName, value, metadata);
        }
    }

    // -------------------------------------------------------------------------
    // Private — item-to-string conversions (map-value path, strict/spec rules)
    // -------------------------------------------------------------------------

    private static String itemValuesToString(String parameterName, List<Item> values, ExceptionMetadata metadata) {
        // Multi-value QName list parameters:
        if ("cdata-section-elements".equals(parameterName) || "suppress-indentation".equals(parameterName)) {
            return joinQNameItems(parameterName, values, metadata);
        }
        // All other parameters expect exactly one item:
        if (values.size() != 1) {
            throw new UnexpectedTypeException(parameterName + " must contain one item.", metadata);
        }
        Item value = values.get(0);
        return switch (parameterName) {
            case "omit-xml-declaration",
                    "standalone",
                    "undeclare-prefixes",
                    "include-content-type",
                    "escape-uri-attributes",
                    "byte-order-mark",
                    "indent",
                    "allow-duplicate-names" -> itemToYesNo(parameterName, value, metadata);
            case "html-version" -> itemToHtmlVersion(parameterName, value, metadata);
            case "method", "json-node-output-method" -> itemToMethodString(parameterName, value, metadata);
            default -> itemToString(parameterName, value, metadata);
        };
    }

    private static String joinQNameItems(String parameterName, List<Item> values, ExceptionMetadata metadata) {
        List<Item> qnames = new ArrayList<>();
        for (Item value : values) {
            if (value.isArray()) {
                for (List<Item> member : value.getSequenceMembers()) {
                    qnames.addAll(member);
                }
            } else {
                qnames.add(value);
            }
        }
        List<String> names = new ArrayList<>();
        for (Item qname : qnames) {
            if (!qname.isQName()) {
                throw new UnexpectedTypeException(parameterName + " must contain QNames.", metadata);
            }
            names.add(expandedQName(qname.getQNameValue()));
        }
        return String.join(" ", names);
    }

    /**
     * Converts an XQuery item to "yes" or "no" for boolean serialization parameters.
     * Accepts xs:boolean or xs:untypedAtomic (cast to boolean).
     */
    private static String itemToYesNo(String parameterName, Item value, ExceptionMetadata metadata) {
        if (value.isUntypedAtomic()) {
            value = CastIterator.castItemToType(value, BuiltinTypesCatalogue.booleanItem, metadata);
        }
        if (!value.isBoolean()) {
            throw new UnexpectedTypeException(parameterName + " must be a boolean.", metadata);
        }
        return value.getBooleanValue() ? "yes" : "no";
    }

    private static String itemToHtmlVersion(String parameterName, Item value, ExceptionMetadata metadata) {
        if (value.isUntypedAtomic()) {
            value = CastIterator.castItemToType(value, BuiltinTypesCatalogue.decimalItem, metadata);
        }
        if (!(value.isDecimal() || value.isInteger())) {
            throw new UnexpectedTypeException("html-version must be a decimal.", metadata);
        }
        return value.getStringValue();
    }

    private static String itemToMethodString(String parameterName, Item value, ExceptionMetadata metadata) {
        if (value.isQName()) {
            Name name = value.getQNameValue();
            // QName with no namespace is treated as a no-namespace method name
            if (name.getNamespace() == null || name.getNamespace().isEmpty()) {
                return name.getLocalName();
            }
            return expandedQName(name);
        }
        return itemToString(parameterName, value, metadata);
    }

    private static String itemToString(String parameterName, Item value, ExceptionMetadata metadata) {
        if (value.isString() || value.isUntypedAtomic() || value.isAnyURI()) {
            return value.getStringValue();
        }
        throw new UnexpectedTypeException(parameterName + " must be a string.", metadata);
    }

    // -------------------------------------------------------------------------
    // Private — apply string value to SerializationParameters (spec-strict)
    // -------------------------------------------------------------------------

    /**
     * Applies a single string-valued serialization parameter using strict spec rules.
     * Called from element-document and fn:serialize paths.
     * Only accepts "yes"/"no" for boolean parameters (not "true"/"false").
     */
    private static void applySpecString(
            SerializationParameters params, String name, String value, ExceptionMetadata metadata) {
        switch (name) {
            case "method" -> params.setMethod(value == null ? null : value.trim());
            case "encoding" -> params.setEncoding(value == null ? null : value.trim());
            case "version" -> params.setVersion(value == null ? null : value.trim());
            case "omit-xml-declaration" -> params.setOmitXmlDeclaration(parseYesNo(name, value, metadata));
            case "standalone" -> params.setStandalone(parseStandalone(name, value, metadata));
            case "doctype-system" -> params.setDoctypeSystem(value);
            case "doctype-public" -> params.setDoctypePublic(value);
            case "media-type" -> params.setMediaType(value == null ? null : value.trim());
            case "normalization-form" -> params.setNormalizationForm(validateNonEmpty(name, value, metadata));
            case "undeclare-prefixes" -> params.setUndeclarePrefixes(parseYesNo(name, value, metadata));
            case "include-content-type" -> params.setIncludeContentType(parseYesNo(name, value, metadata));
            case "escape-uri-attributes" -> params.setEscapeUriAttributes(parseYesNo(name, value, metadata));
            case "html-version" -> params.setHtmlVersion(value == null ? null : value.trim());
            case "byte-order-mark" -> params.setByteOrderMark(parseYesNo(name, value, metadata));
            case "indent" -> params.setIndent(parseYesNo(name, value, metadata));
            case "item-separator" -> params.setItemSeparator(value);
            case "allow-duplicate-names" -> params.setAllowDuplicateNames(parseYesNo(name, value, metadata));
            case "json-node-output-method" -> params.setJsonNodeOutputMethod(
                    parseJsonNodeOutputMethod(name, value, metadata));
            case "cdata-section-elements" -> params.setCdataSectionElements(parseExpandedQNameSet(value));
            case "suppress-indentation" -> params.setSuppressIndentation(parseExpandedQNameSet(value));
                // use-character-maps: handled separately via applyCharacterMapsParameter / characterMapsFromMapValue
            default -> params.getExtensionParameters().put(name, value);
        }
    }

    /** Parses "yes"/"no" strictly. Throws SEPM0016 for anything else. */
    private static boolean parseYesNo(String name, String value, ExceptionMetadata metadata) {
        String trimmed = value == null ? null : value.trim();
        if ("yes".equals(trimmed)) {
            return true;
        }
        if ("no".equals(trimmed)) {
            return false;
        }
        throw new InvalidSerializationParameterValueException(name, value, "'yes' or 'no'", metadata);
    }

    /** Parses standalone: "yes", "no", or "omit". */
    private static SerializationParameters.Standalone parseStandalone(
            String name, String value, ExceptionMetadata metadata) {
        String trimmed = value == null ? null : value.trim();
        if ("yes".equals(trimmed)) {
            return SerializationParameters.Standalone.YES;
        }
        if ("no".equals(trimmed)) {
            return SerializationParameters.Standalone.NO;
        }
        if ("omit".equals(trimmed)) {
            return SerializationParameters.Standalone.OMIT;
        }
        throw new InvalidSerializationParameterValueException(name, value, "'yes', 'no', or 'omit'", metadata);
    }

    /** Validates non-empty string. */
    private static String validateNonEmpty(String name, String value, ExceptionMetadata metadata) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidSerializationParameterValueException(
                    name, value == null ? "null" : "''", "a non-empty string", metadata);
        }
        return value.trim();
    }

    private static SerializationParameters.JsonNodeOutputMethod parseJsonNodeOutputMethod(
            String name, String value, ExceptionMetadata metadata) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.startsWith("Q{")) {
            // EQName form: Q{namespace}localname
            int close = trimmed.indexOf('}');
            if (close >= 0) {
                String localPart = trimmed.substring(close + 1);
                try {
                    return SerializationParameters.JsonNodeOutputMethod.valueOf(localPart.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // fall through
                }
            }
        }
        try {
            return SerializationParameters.JsonNodeOutputMethod.valueOf(trimmed.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSerializationParameterValueException(
                    name, value, "'xml', 'xhtml', 'html', 'text', or 'json'", metadata);
        }
    }

    private static Set<String> parseExpandedQNameSet(String value) {
        Set<String> result = new HashSet<>();
        if (value != null && !value.trim().isEmpty()) {
            for (String token : value.trim().split("[,\\s]+")) {
                if (!token.isEmpty()) {
                    result.add(token);
                }
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Private — lenient parsers for CLI / config path
    // -------------------------------------------------------------------------

    /**
     * Lenient boolean: accepts yes/no/true/false/1/0 (case-insensitive).
     */
    private static boolean parseLenientBoolean(String name, String value) {
        String lower = value.toLowerCase().trim();
        return switch (lower) {
            case "yes", "true", "1" -> true;
            case "no", "false", "0" -> false;
            default -> throw new InvalidSerializationParameterValueException(
                    name, value, "'yes', 'no', 'true', or 'false'");
        };
    }

    /**
     * Lenient standalone: yes/no/true/false/1/0/omit.
     */
    private static SerializationParameters.Standalone parseStandaloneConfig(String name, String value) {
        String lower = value.toLowerCase().trim();
        return switch (lower) {
            case "yes", "true", "1" -> SerializationParameters.Standalone.YES;
            case "no", "false", "0" -> SerializationParameters.Standalone.NO;
            case "omit" -> SerializationParameters.Standalone.OMIT;
            default -> throw new InvalidSerializationParameterValueException(name, value, "'yes', 'no', or 'omit'");
        };
    }

    private static SerializationParameters.JsonNodeOutputMethod parseJsonNodeOutputMethodConfig(
            String name, String value) {
        String normalized = value.trim();
        if (normalized.startsWith("Q{")) {
            int close = normalized.indexOf('}');
            if (close >= 0) {
                try {
                    return SerializationParameters.JsonNodeOutputMethod.valueOf(
                            normalized.substring(close + 1).toUpperCase());
                } catch (IllegalArgumentException e) {
                    // fall through
                }
            }
        }
        try {
            return SerializationParameters.JsonNodeOutputMethod.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSerializationParameterValueException(
                    name, value, "'xml', 'xhtml', 'html', 'text', or 'json'");
        }
    }

    private static int parseIndentSpaces(String name, String value) {
        try {
            int spaces = Integer.parseInt(value.trim());
            if (spaces < 0) {
                throw new InvalidSerializationParameterValueException(name, value, "a non-negative integer");
            }
            return spaces;
        } catch (NumberFormatException e) {
            throw new InvalidSerializationParameterValueException(name, value, "a non-negative integer");
        }
    }

    private static Map<String, String> parseCharacterMapsConfig(String name, String value) {
        Map<String, String> result = new HashMap<>();
        if (value != null && !value.trim().isEmpty()) {
            for (String pair : value.split(",")) {
                String trimmed = pair.trim();
                int eq = trimmed.indexOf('=');
                if (eq <= 0 || eq == trimmed.length() - 1) {
                    throw new InvalidSerializationParameterValueException(name, trimmed, "key=value pairs");
                }
                result.put(
                        trimmed.substring(0, eq).trim(),
                        trimmed.substring(eq + 1).trim());
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Private — parameter-name extraction
    // -------------------------------------------------------------------------

    private static String parameterNameFromKey(Item key, ExceptionMetadata metadata) {
        if (key.isQName()) {
            Name qName = key.getQNameValue();
            String namespace = qName.getNamespace();
            // Per F&O 3.1 §22.2: The key of the entry is an xs:string value in the cases of parameter names
            // defined in these specifications, or an xs:QName (with non-absent namespace) in the case of
            // implementation-defined serialization parameters.
            // A QName with no namespace is not a valid parameter key and is ignored.
            if (namespace == null || namespace.isEmpty()) {
                return null;
            }
            if (SERIALIZATION_NAMESPACE.equals(namespace)) {
                return qName.getLocalName();
            }
            return null; // ignored: implementation-defined namespace
        }
        if (key.isString() || key.isUntypedAtomic() || key.isAnyURI()) {
            return key.getStringValue();
        }
        throw new InvalidArgumentTypeException("Serialization parameter map keys must be strings or QNames.", metadata);
    }

    // -------------------------------------------------------------------------
    // Private — element path
    // -------------------------------------------------------------------------

    private static void applyParameterElements(
            SerializationParameters params,
            List<Item> elements,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata) {
        Set<Name> seen = new HashSet<>();
        for (Item element : elements) {
            if (!element.isElementNode()) {
                continue;
            }
            Name name = element.nodeName();
            if (name == null) {
                continue;
            }
            String namespace = name.getNamespace();
            if (!seen.add(name)) {
                throw new RumbleException(
                        "Duplicate serialization parameter: " + name.getLocalName(),
                        ErrorCode.DuplicateSerializationParameter,
                        metadata);
            }
            if (!SERIALIZATION_NAMESPACE.equals(namespace)) {
                if (namespace != null && !namespace.isEmpty()) {
                    continue;
                }
                throw invalidParameterDocument("Invalid serialization parameter: " + name.getLocalName(), metadata);
            }
            if (!STANDARD_PARAMETERS.contains(name.getLocalName())) {
                throw invalidParameterDocument("Invalid serialization parameter: " + name.getLocalName(), metadata);
            }
            if (explicitParameterNames != null && explicitParameterNames.contains(name.getLocalName())) {
                continue;
            }
            if ("use-character-maps".equals(name.getLocalName())) {
                if (element.attributes().iterator().hasNext()) {
                    throw invalidParameterDocument("Unexpected use-character-maps attribute.", metadata);
                }
                applyCharacterMapsParameter(params, element, metadata);
                continue;
            }
            for (Item attribute : element.attributes()) {
                Name attributeName = attribute.nodeName();
                if (attributeName == null
                        || !"value".equals(attributeName.getLocalName())
                        || (attributeName.getNamespace() != null
                                && !attributeName.getNamespace().isEmpty())) {
                    throw invalidParameterDocument("Unexpected serialization parameter attribute.", metadata);
                }
            }
            for (Item child : element.children()) {
                if (child.isElementNode()) {
                    throw invalidParameterDocument("Unexpected serialization parameter child element.", metadata);
                }
            }
            String value = attributeValue(element, "value");
            if (value == null) {
                throw invalidParameterDocument("Missing serialization parameter value.", metadata);
            }
            if ("cdata-section-elements".equals(name.getLocalName())
                    || "suppress-indentation".equals(name.getLocalName())) {
                value = expandLexicalQNames(value, element, false);
            }
            try {
                applySpecString(params, name.getLocalName(), value, metadata);
            } catch (InvalidSerializationParameterValueException e) {
                throw invalidParameterDocument(e.getMessage(), metadata);
            }
        }
    }

    private static void applyCharacterMapsParameter(
            SerializationParameters params, Item useCharacterMapsElement, ExceptionMetadata metadata) {
        Map<String, String> characterMaps = new HashMap<>();
        for (Item child : useCharacterMapsElement.children()) {
            if (!child.isElementNode()) {
                continue;
            }
            Name childName = child.nodeName();
            if (childName == null || !"character-map".equals(childName.getLocalName())) {
                throw invalidParameterDocument("Invalid character-map child.", metadata);
            }
            if (!SERIALIZATION_NAMESPACE.equals(childName.getNamespace())) {
                throw invalidParameterDocument("Invalid character-map namespace.", metadata);
            }
            for (Item attribute : child.attributes()) {
                Name attributeName = attribute.nodeName();
                if (attributeName == null
                        || (attributeName.getNamespace() != null
                                && !attributeName.getNamespace().isEmpty())
                        || !("character".equals(attributeName.getLocalName())
                                || "map-string".equals(attributeName.getLocalName()))) {
                    throw invalidParameterDocument("Unexpected character-map attribute.", metadata);
                }
            }
            for (Item grandchild : child.children()) {
                if (grandchild.isElementNode()) {
                    throw invalidParameterDocument("Unexpected character-map child element.", metadata);
                }
            }
            String character = attributeValue(child, "character");
            String mapString = attributeValue(child, "map-string");
            if (character == null || mapString == null) {
                throw invalidParameterDocument("character-map requires character and map-string attributes.", metadata);
            }
            if (!isSingleCharacter(character)) {
                throw invalidParameterDocument("Character-map keys must be single characters.", metadata);
            }
            if (characterMaps.containsKey(character)) {
                throw new RumbleException(
                        "Duplicate character-map entry: " + character, ErrorCode.DuplicateCharacterMap, metadata);
            }
            characterMaps.put(character, mapString);
        }
        params.setCharacterMaps(characterMaps);
    }

    private static Map<String, String> characterMapsFromMapValue(List<Item> valueSequence, ExceptionMetadata metadata) {
        if (valueSequence.size() != 1
                || !(valueSequence.get(0).isMap() || valueSequence.get(0).isObject())) {
            throw new UnexpectedTypeException("use-character-maps must be a map.", metadata);
        }
        Item map = valueSequence.get(0);
        Map<String, String> mappings = new HashMap<>();
        for (Item character : map.getItemKeys()) {
            if (!character.isString()) {
                throw new UnexpectedTypeException("Character map keys must be strings.", metadata);
            }
            String keyValue = character.getStringValue();
            if (!isSingleCharacter(keyValue)) {
                throw new InvalidSerializationParameterValueException(
                        "use-character-maps", keyValue, "a single character", metadata);
            }
            List<Item> mapped = map.getSequenceByKey(character);
            if (mapped == null || mapped.size() != 1 || !mapped.get(0).isString()) {
                throw new UnexpectedTypeException("Character map values must be strings.", metadata);
            }
            mappings.put(keyValue, mapped.get(0).getStringValue());
        }
        return mappings;
    }

    // -------------------------------------------------------------------------
    // Private — non-standard parameter value extraction
    // -------------------------------------------------------------------------

    private static String sequenceToParameterValue(
            String parameterName, List<Item> valueSequence, Item namespaceContext, ExceptionMetadata metadata) {
        if (valueSequence == null || valueSequence.isEmpty()) {
            return null;
        }
        List<String> values = new ArrayList<>();
        for (Item item : valueSequence) {
            values.addAll(itemToParameterTokens(parameterName, item, namespaceContext, metadata));
        }
        return String.join(" ", values);
    }

    private static List<String> itemToParameterTokens(
            String parameterName, Item item, Item namespaceContext, ExceptionMetadata metadata) {
        if (item == null) {
            return List.of();
        }
        if (item.isArray()) {
            List<String> values = new ArrayList<>();
            for (List<Item> memberSequence : item.getSequenceMembers()) {
                String value = sequenceToParameterValue(parameterName, memberSequence, namespaceContext, metadata);
                if (value != null) {
                    values.add(value);
                }
            }
            return values;
        }
        if (item.isQName()) {
            return List.of(expandedQName(item.getQNameValue()));
        }
        if ("cdata-section-elements".equals(parameterName) || "suppress-indentation".equals(parameterName)) {
            return List.of(expandLexicalQNames(item.getStringValue(), namespaceContext, false));
        }
        return List.of(item.getStringValue());
    }

    // -------------------------------------------------------------------------
    // Private — QName / namespace utilities
    // -------------------------------------------------------------------------

    private static String expandedQName(Name name) {
        String namespace = name.getNamespace();
        if (namespace == null || namespace.isEmpty()) {
            return name.getLocalName();
        }
        return "Q{" + namespace + "}" + name.getLocalName();
    }

    private static boolean isSerializationParametersElement(Item item) {
        Name name = item.nodeName();
        return name != null
                && "serialization-parameters".equals(name.getLocalName())
                && SERIALIZATION_NAMESPACE.equals(name.getNamespace());
    }

    private static String attributeValue(Item element, String localName) {
        for (Item attribute : element.attributes()) {
            Name name = attribute.nodeName();
            if (name != null
                    && localName.equals(name.getLocalName())
                    && (name.getNamespace() == null || name.getNamespace().isEmpty())) {
                return attribute.getStringValue();
            }
        }
        return null;
    }

    private static String expandLexicalQNames(
            String value, Item contextNode, boolean useDefaultNamespaceForUnprefixed) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (String token : value.trim().split("[,\\s]+")) {
            if (token.isEmpty()) {
                continue;
            }
            sb.append(separator).append(expandLexicalQName(token, contextNode, useDefaultNamespaceForUnprefixed));
            separator = " ";
        }
        return sb.toString();
    }

    private static String expandLexicalQName(String token, Item contextNode, boolean useDefaultNamespaceForUnprefixed) {
        if (token.startsWith("Q{")) {
            return token;
        }
        int colon = token.indexOf(':');
        if (colon < 0) {
            if (!useDefaultNamespaceForUnprefixed) {
                return token;
            }
            String namespace = resolveNamespace("", contextNode);
            if (namespace == null || namespace.isEmpty()) {
                return token;
            }
            return "Q{" + namespace + "}" + token;
        }
        String prefix = token.substring(0, colon);
        String localName = token.substring(colon + 1);
        String namespace = resolveNamespace(prefix, contextNode);
        if (namespace == null) {
            return token;
        }
        return "Q{" + namespace + "}" + localName;
    }

    private static String resolveNamespace(String prefix, Item contextNode) {
        if (contextNode == null) {
            return null;
        }
        Map<String, String> namespaces = new HashMap<>();
        Item current = contextNode;
        while (current != null && current.isNode()) {
            for (Item namespaceNode : current.declaredNamespaceNodes()) {
                Name name = namespaceNode.nodeName();
                String currentPrefix = name == null ? "" : name.getLocalName();
                namespaces.putIfAbsent(currentPrefix, namespaceNode.getStringValue());
            }
            current = current.parent();
        }
        return namespaces.get(prefix);
    }

    private static boolean isSingleCharacter(String value) {
        return value.codePointCount(0, value.length()) == 1;
    }

    private static RumbleException invalidParameterDocument(String message, ExceptionMetadata metadata) {
        return new RumbleException(message, ErrorCode.InvalidSerializationParameterDocument, metadata);
    }
}
