/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.types.BuiltinTypesCatalogue;

/**
 * Builds a new schema-annotated XDM tree from Xerces's SAX and PSVI (Post-Schema-Validation Information) callbacks.
 *
 * consumes the validated SAX stream and builds a new RumbleDB XML tree.
 */
final class PsviNodeBuilder extends DefaultHandler {

    private final PSVIProvider psviProvider;
    private final XmlSchemaCatalog catalog;
    private final boolean documentInput;
    private final Deque<ElementFrame> elements;
    private final Map<String, String> pendingNamespaces;
    private final List<Item> documentChildren;
    private Item result;

    PsviNodeBuilder(PSVIProvider psviProvider, XmlSchemaCatalog catalog, boolean documentInput) {
        this.psviProvider = psviProvider;
        this.catalog = catalog;
        this.documentInput = documentInput;
        this.elements = new ArrayDeque<>();
        this.pendingNamespaces = new LinkedHashMap<>();
        this.documentChildren = new ArrayList<>();
    }

    Item getResult() {
        if (this.result == null) {
            throw new OurBadException("Xerces validation did not produce an XML node.");
        }
        return this.result;
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        this.pendingNamespaces.put(prefix == null ? "" : prefix, uri == null ? "" : uri);
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        Map<String, String> declaredNamespaces = new LinkedHashMap<>(this.pendingNamespaces);
        Map<String, String> inScopeNamespaces = this.elements.isEmpty()
                ? new LinkedHashMap<>()
                : new LinkedHashMap<>(this.elements.peek().inScopeNamespaces());
        inScopeNamespaces.putAll(declaredNamespaces);
        this.elements.push(new ElementFrame(
                XmlNameCodec.fromSax(uri, localName, qualifiedName),
                new ArrayList<>(),
                createAttributes(attributes, declaredNamespaces, inScopeNamespaces),
                declaredNamespaces,
                inScopeNamespaces));
        this.pendingNamespaces.clear();
    }

    @Override
    public void characters(char[] characters, int start, int length) {
        if (length == 0 || this.elements.isEmpty()) {
            return;
        }
        List<Item> children = this.elements.peek().children();
        String value = new String(characters, start, length);
        if (!children.isEmpty() && children.get(children.size() - 1).isTextNode()) {
            Item previous = children.remove(children.size() - 1);
            value = previous.getStringValue() + value;
        }
        children.add(ItemFactory.getInstance().createXmlTextNode(value));
    }

    @Override
    public void processingInstruction(String target, String data) {
        addNode(ItemFactory.getInstance().createXmlProcessingInstructionNode(target, data));
    }

    void comment(String content) {
        addNode(ItemFactory.getInstance().createXmlCommentNode(content));
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) {
        ElementFrame frame = this.elements.pop();
        ElementPSVI psvi = this.psviProvider.getElementPSVI();
        if (psvi == null || psvi.getTypeDefinition() == null) {
            throw new OurBadException("Xerces did not provide PSVI type information for an element.");
        }
        XmlSchemaWhitespaceNormalizer.normalize(frame.children(), psvi);
        Item element =
                ItemFactory.getInstance().createXmlElementNode(frame.name(), frame.children(), frame.attributes());
        for (Map.Entry<String, String> namespace : frame.declaredNamespaces().entrySet()) {
            element.addOrReplaceNamespace(
                    ItemFactory.getInstance().createXmlNamespaceNode(namespace.getKey(), namespace.getValue()));
        }
        setElementSchemaType(element, psvi);
        addNode(element);
    }

    @Override
    public void endDocument() {
        if (!this.elements.isEmpty()) {
            throw new OurBadException("Xerces validation ended with unclosed elements.");
        }
        if (this.documentInput) {
            this.result = ItemFactory.getInstance().createXmlDocumentNode(this.documentChildren);
        } else if (this.documentChildren.size() == 1
                && this.documentChildren.get(0).isElementNode()) {
            this.result = this.documentChildren.get(0);
        } else {
            throw new OurBadException("Element validation produced an invalid result tree.");
        }
        this.result.addParentToDescendants();
        this.result.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);
    }

    private List<Item> createAttributes(
            Attributes attributes, Map<String, String> declaredNamespaces, Map<String, String> inScopeNamespaces) {
        List<Item> result = new ArrayList<>(attributes.getLength());
        for (int index = 0; index < attributes.getLength(); index++) {
            AttributePSVI psvi = this.psviProvider.getAttributePSVI(index);
            Name name = XmlNameCodec.fromSax(
                    attributes.getURI(index), attributes.getLocalName(index), attributes.getQName(index));
            if (name.getNamespace() != null && name.getPrefix() == null) {
                name = XmlNameCodec.fromExpandedName(
                        name.getNamespace(),
                        attributePrefix(name.getNamespace(), declaredNamespaces, inScopeNamespaces),
                        name.getLocalName());
            }
            XSValue schemaValue = psvi == null ? null : psvi.getSchemaValue();
            String normalizedValue = schemaValue == null ? null : schemaValue.getNormalizedValue();
            Item attribute = ItemFactory.getInstance()
                    .createXmlAttributeNode(
                            name, normalizedValue == null ? attributes.getValue(index) : normalizedValue);
            if (psvi != null && psvi.getTypeDefinition() != null && schemaValue != null) {
                List<Item> typedValue = this.catalog.convertTypedValue(schemaValue);
                attribute.setSchemaType(this.catalog.getTypeAnnotation(psvi.getTypeDefinition()), typedValue);
                setIdentityProperties(attribute, typedValue);
            } else if (Name.XML_NS.equals(name.getNamespace()) && "id".equals(name.getLocalName())) {
                attribute.setXmlSchemaIdentityProperties(true, false);
            }
            result.add(attribute);
        }
        return result;
    }

    private void setElementSchemaType(Item element, ElementPSVI psvi) {
        XSTypeDefinition schemaType = psvi.getTypeDefinition();
        var annotation = this.catalog.getTypeAnnotation(schemaType);
        List<Item> typedValue;
        if (psvi.getNil()) {
            typedValue = List.of();
        } else if (schemaType instanceof XSComplexTypeDefinition complexType) {
            switch (complexType.getContentType()) {
                case XSComplexTypeDefinition.CONTENTTYPE_EMPTY:
                    typedValue = List.of();
                    break;
                case XSComplexTypeDefinition.CONTENTTYPE_ELEMENT:
                    typedValue = null;
                    break;
                case XSComplexTypeDefinition.CONTENTTYPE_MIXED:
                    typedValue = List.of(ItemFactory.getInstance().createUntypedAtomicItem(element.getStringValue()));
                    break;
                case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE:
                    typedValue = schemaTypedValue(psvi);
                    break;
                default:
                    throw new OurBadException("Xerces returned an unknown complex-content type.");
            }
        } else {
            typedValue = schemaTypedValue(psvi);
        }

        if (typedValue == null) {
            element.setSchemaType(annotation);
        } else {
            element.setSchemaType(annotation, typedValue);
            setIdentityProperties(element, typedValue);
        }
        element.setXmlSchemaNilled(psvi.getNil());
    }

    private List<Item> schemaTypedValue(ElementPSVI psvi) {
        if (psvi.getSchemaValue() == null) {
            throw new OurBadException("Xerces did not provide a typed value for a simple-content element.");
        }
        return this.catalog.convertTypedValue(psvi.getSchemaValue());
    }

    private static void setIdentityProperties(Item node, List<Item> typedValue) {
        boolean id =
                typedValue.size() == 1 && typedValue.get(0).getDynamicType().isSubtypeOf(BuiltinTypesCatalogue.IDItem);
        boolean idRefs = typedValue.stream()
                .anyMatch(value -> value.getDynamicType().isSubtypeOf(BuiltinTypesCatalogue.IDREFItem));
        node.setXmlSchemaIdentityProperties(id, idRefs);
    }

    private void addNode(Item node) {
        if (this.elements.isEmpty()) {
            this.documentChildren.add(node);
        } else {
            this.elements.peek().children().add(node);
        }
    }

    private static String attributePrefix(
            String namespace, Map<String, String> declaredNamespaces, Map<String, String> inScopeNamespaces) {
        if (namespace == null || namespace.isEmpty()) {
            return null;
        }
        if (Name.XML_NS.equals(namespace)) {
            return "xml";
        }
        for (Map.Entry<String, String> binding : inScopeNamespaces.entrySet()) {
            if (!binding.getKey().isEmpty() && namespace.equals(binding.getValue())) {
                return binding.getKey();
            }
        }

        int suffix = 0;
        String prefix;
        do {
            prefix = "ns" + suffix++;
        } while (inScopeNamespaces.containsKey(prefix));
        declaredNamespaces.put(prefix, namespace);
        inScopeNamespaces.put(prefix, namespace);
        return prefix;
    }

    private record ElementFrame(
            Name name,
            List<Item> children,
            List<Item> attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces) {}
}
