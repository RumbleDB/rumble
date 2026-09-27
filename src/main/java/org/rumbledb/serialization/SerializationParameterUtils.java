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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.rumbledb.api.Item;
import org.rumbledb.config.SerializationParameterBuilder;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.InvalidSerializationParameterValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.parsing.ItemParser;
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
    private static final Set<String> BOOLEAN_PARAMETERS = Set.of(
            "omit-xml-declaration",
            "undeclare-prefixes",
            "include-content-type",
            "escape-uri-attributes",
            "byte-order-mark",
            "indent",
            "standalone",
            "allow-duplicate-names");

    private SerializationParameterUtils() {}

    public static SerializationParameters defaultsForSerializeFunction(String queryLanguage) {
        SerializationParameters params = SerializationParameters.defaults(queryLanguage);
        params.setItemSeparator(" ");
        params.setOmitXmlDeclaration(true);
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
        try {
            URI uri = FileSystemUtil.resolveURI(staticContext.getStaticBaseURI(), location, metadata);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            try (InputStream xmlFileStream = FileSystemUtil.getDataInputStream(uri, metadata)) {
                Document xmlDocument = documentBuilder.parse(xmlFileStream);
                Item item = ItemParser.getItemFromXML(
                        xmlDocument,
                        uri.toString(),
                        staticContext.getRumbleConfiguration().optimization().optimizeParentPointers());
                applyParameterItem(params, item, explicitParameterNames, metadata);
            }
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e, metadata);
        } catch (CannotRetrieveResourceException e) {
            throw e;
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to read the serialization parameter document.", metadata);
            ex.initCause(e);
            throw ex;
        } catch (SAXException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to parse the serialization parameter document as well-formed XML.", metadata);
            ex.initCause(e);
            throw ex;
        }
    }

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
            boolean standardParameter = STANDARD_PARAMETERS.contains(parameterName);
            if (standardParameter && (valueSequence == null || valueSequence.isEmpty())) {
                continue;
            }
            if ("use-character-maps".equals(parameterName)) {
                params.setCharacterMaps(characterMapsFromMapValue(valueSequence, metadata));
                continue;
            }
            String value = standardParameter
                    ? mapParameterValue(parameterName, valueSequence, metadata)
                    : sequenceToParameterValue(parameterName, valueSequence, null, metadata);
            applyNormalizedParameter(params, parameterName, value, metadata);
        }
    }

    private static String mapParameterValue(String parameterName, List<Item> values, ExceptionMetadata metadata) {
        if ("cdata-section-elements".equals(parameterName) || "suppress-indentation".equals(parameterName)) {
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

        if (values.size() != 1) {
            throw new UnexpectedTypeException(parameterName + " must contain one item.", metadata);
        }
        Item value = values.get(0);
        if (BOOLEAN_PARAMETERS.contains(parameterName)) {
            if (value.isUntypedAtomic()) {
                value = CastIterator.castItemToType(value, BuiltinTypesCatalogue.booleanItem, metadata);
            }
            if (!value.isBoolean()) {
                throw new UnexpectedTypeException(parameterName + " must be a boolean.", metadata);
            }
            return value.getBooleanValue() ? "yes" : "no";
        }
        if ("html-version".equals(parameterName)) {
            if (value.isUntypedAtomic()) {
                value = CastIterator.castItemToType(value, BuiltinTypesCatalogue.decimalItem, metadata);
            }
            if (!(value.isDecimal() || value.isInteger())) {
                throw new UnexpectedTypeException("html-version must be a decimal.", metadata);
            }
            return value.getStringValue();
        }
        if (("method".equals(parameterName) || "json-node-output-method".equals(parameterName)) && value.isQName()) {
            Name name = value.getQNameValue();
            if (name.getNamespace() == null || name.getNamespace().isEmpty()) {
                throw new UnexpectedTypeException(parameterName + " QName must have a namespace.", metadata);
            }
            return expandedQName(name);
        }
        if (value.isString() || value.isUntypedAtomic() || value.isAnyURI()) {
            return value.getStringValue();
        }
        throw new UnexpectedTypeException(parameterName + " must be a string.", metadata);
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
                applyNormalizedParameter(params, name.getLocalName(), value, metadata);
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

    private static boolean isSingleCharacter(String value) {
        return value.codePointCount(0, value.length()) == 1;
    }

    private static RumbleException invalidParameterDocument(String message, ExceptionMetadata metadata) {
        return new RumbleException(message, ErrorCode.InvalidSerializationParameterDocument, metadata);
    }

    private static void applyNormalizedParameter(
            SerializationParameters params, String parameterName, String value, ExceptionMetadata metadata) {
        if (value == null && "standalone".equals(parameterName)) {
            return;
        }
        if (value == null) {
            throw new InvalidSerializationParameterValueException(parameterName, "()", "a valid value", metadata);
        }
        SerializationParameterBuilder.update(params, parameterName, value);
    }

    private static String parameterNameFromKey(Item key, ExceptionMetadata metadata) {
        if (key.isQName()) {
            return null;
        }
        if (key.isString() || key.isUntypedAtomic() || key.isAnyURI()) {
            return key.getStringValue();
        }
        throw new InvalidArgumentTypeException("Serialization parameter map keys must be strings or QNames.", metadata);
    }

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
}
