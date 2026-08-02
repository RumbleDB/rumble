package org.rumbledb.serialization;

import org.rumbledb.api.Item;
import org.rumbledb.config.SerializationParameterBuilder;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.InvalidSerializationParameterValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SerializationParameterUtils {

    public static final String SERIALIZATION_NAMESPACE = "http://www.w3.org/2010/xslt-xquery-serialization";

    private SerializationParameterUtils() {
    }

    public static SerializationParameters defaultsForSerializeFunction(String queryLanguage) {
        SerializationParameters params = SerializationParameters.defaults(queryLanguage);
        params.setItemSeparator(" ");
        params.setOmitXmlDeclaration(true);
        return params;
    }

    public static void applyParameterItems(
            SerializationParameters params,
            List<Item> optionsItems,
            ExceptionMetadata metadata
    ) {
        applyParameterItems(params, optionsItems, null, metadata);
    }

    private static void applyParameterItems(
            SerializationParameters params,
            List<Item> optionsItems,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata
    ) {
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
                        metadata
                );
            }
        }
        applyParameterElements(params, optionsItems, explicitParameterNames, metadata);
    }

    public static void applyParameterDocument(
            SerializationParameters params,
            StaticContext staticContext,
            String location,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata
    ) {
        try {
            URI uri = FileSystemUtil.resolveURI(staticContext.getStaticBaseURI(), location, metadata);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            try (
                InputStream xmlFileStream = FileSystemUtil.getDataInputStream(
                    uri,
                    staticContext.getRumbleConfiguration(),
                    metadata
                )
            ) {
                Document xmlDocument = documentBuilder.parse(xmlFileStream);
                Item item = ItemParser.getItemFromXML(
                    xmlDocument,
                    uri.toString(),
                    staticContext.getRumbleConfiguration().optimizeParentPointers()
                );
                applyParameterItem(params, item, explicitParameterNames, metadata);
            }
        } catch (ParserConfigurationException e) {
            throw new OurBadException("Document builder creation failed with: " + e, metadata);
        } catch (CannotRetrieveResourceException e) {
            throw e;
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to read the serialization parameter document.",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        } catch (SAXException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException(
                    "Unable to parse the serialization parameter document as well-formed XML.",
                    metadata
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private static void applyParameterItem(
            SerializationParameters params,
            Item options,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata
    ) {
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
                "The second argument of fn:serialize must be a map or serialization parameter elements.",
                metadata
        );
    }

    private static void applyParameterMap(
            SerializationParameters params,
            Item options,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata
    ) {
        for (Item key : options.getItemKeys()) {
            String parameterName = parameterNameFromKey(key, metadata);
            if (parameterName == null) {
                continue;
            }
            if (explicitParameterNames != null && explicitParameterNames.contains(parameterName)) {
                continue;
            }
            List<Item> valueSequence = options.getSequenceByKey(key);
            applyNormalizedParameter(
                params,
                parameterName,
                sequenceToParameterValue(parameterName, valueSequence, null, metadata),
                metadata
            );
        }
    }

    private static void applyParameterElements(
            SerializationParameters params,
            List<Item> elements,
            Set<String> explicitParameterNames,
            ExceptionMetadata metadata
    ) {
        for (Item element : elements) {
            if (!element.isElementNode()) {
                continue;
            }
            Name name = element.nodeName();
            if (name == null) {
                continue;
            }
            String namespace = name.getNamespace();
            if (namespace != null && !namespace.isEmpty() && !SERIALIZATION_NAMESPACE.equals(namespace)) {
                continue;
            }
            if (explicitParameterNames != null && explicitParameterNames.contains(name.getLocalName())) {
                continue;
            }
            if ("use-character-maps".equals(name.getLocalName())) {
                applyCharacterMapsParameter(params, element, metadata);
                continue;
            }
            String value = attributeValue(element, "value");
            if (value == null) {
                value = element.getStringValue();
            }
            if (
                "cdata-section-elements".equals(name.getLocalName())
                    || "suppress-indentation".equals(name.getLocalName())
            ) {
                value = expandLexicalQNames(value, element, false);
            }
            applyNormalizedParameter(params, name.getLocalName(), value, metadata);
        }
    }

    private static void applyCharacterMapsParameter(
            SerializationParameters params,
            Item useCharacterMapsElement,
            ExceptionMetadata metadata
    ) {
        Map<String, String> characterMaps = new HashMap<>();
        for (Item child : useCharacterMapsElement.children()) {
            if (!child.isElementNode()) {
                continue;
            }
            Name childName = child.nodeName();
            if (
                childName == null
                    || !"character-map".equals(childName.getLocalName())
            ) {
                continue;
            }
            String childNamespace = childName.getNamespace();
            if (
                childNamespace != null
                    && !childNamespace.isEmpty()
                    && !SERIALIZATION_NAMESPACE.equals(childNamespace)
            ) {
                continue;
            }
            String character = attributeValue(child, "character");
            String mapString = attributeValue(child, "map-string");
            if (character == null || mapString == null) {
                throw new InvalidSerializationParameterValueException(
                        "use-character-maps",
                        child.getStringValue(),
                        "character-map elements with character and map-string attributes",
                        metadata
                );
            }
            characterMaps.put(character, mapString);
        }
        params.setCharacterMaps(characterMaps);
    }

    private static void applyNormalizedParameter(
            SerializationParameters params,
            String parameterName,
            String value,
            ExceptionMetadata metadata
    ) {
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
            Name qName = key.getQNameValue();
            String namespace = qName.getNamespace();
            if (namespace == null || namespace.isEmpty() || SERIALIZATION_NAMESPACE.equals(namespace)) {
                return qName.getLocalName();
            }
            return null;
        }
        if (key.isString() || key.isUntypedAtomic() || key.isAnyURI()) {
            return key.getStringValue();
        }
        throw new InvalidArgumentTypeException(
                "Serialization parameter map keys must be strings or QNames.",
                metadata
        );
    }

    private static String sequenceToParameterValue(
            String parameterName,
            List<Item> valueSequence,
            Item namespaceContext,
            ExceptionMetadata metadata
    ) {
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
            String parameterName,
            Item item,
            Item namespaceContext,
            ExceptionMetadata metadata
    ) {
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
            if (
                name != null
                    && localName.equals(name.getLocalName())
                    && (name.getNamespace() == null || name.getNamespace().isEmpty())
            ) {
                return attribute.getStringValue();
            }
        }
        return null;
    }

    private static String expandLexicalQNames(
            String value,
            Item contextNode,
            boolean useDefaultNamespaceForUnprefixed
    ) {
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

    private static String expandLexicalQName(
            String token,
            Item contextNode,
            boolean useDefaultNamespaceForUnprefixed
    ) {
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
