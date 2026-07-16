/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.ItemPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSValue;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XmlSchemaNodeProperties;
import org.xml.sax.Attributes;

/** Converts Xerces SAX and PSVI data into schema-augmented XDM items. */
final class PsviItemFactory {

    private final PSVIProvider psviProvider;
    private final XmlSchemaTypeMapper typeMapper;
    private final XmlSchemaTypedValueFactory typedValueFactory;

    PsviItemFactory(PSVIProvider psviProvider, XmlSchemaTypeMapper typeMapper) {
        this.psviProvider = psviProvider;
        this.typeMapper = typeMapper;
        this.typedValueFactory = new XmlSchemaTypedValueFactory(typeMapper);
    }

    List<Item> createAttributes(
            Attributes attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces
    ) {
        List<Item> result = new ArrayList<>(attributes.getLength());
        for (int index = 0; index < attributes.getLength(); index++) {
            AttributePSVI psvi = this.psviProvider.getAttributePSVI(index);
            String normalizedValue = psvi == null ? null : psvi.getSchemaNormalizedValue();
            Name name = XmlNameCodec.fromSax(
                attributes.getURI(index),
                attributes.getLocalName(index),
                attributes.getQName(index)
            );
            if (name.getNamespace() != null && name.getPrefix() == null) {
                name = XmlNameCodec.fromExpandedName(
                    name.getNamespace(),
                    attributePrefix(name.getNamespace(), declaredNamespaces, inScopeNamespaces),
                    name.getLocalName()
                );
            }
            Item attribute = ItemFactory.getInstance()
                .createXmlAttributeNode(
                    name,
                    normalizedValue == null ? attributes.getValue(index) : normalizedValue
                );
            if (psvi != null) {
                setSchemaProperties(attribute, psvi);
            }
            result.add(attribute);
        }
        return result;
    }

    Item createElement(
            Name name,
            List<Item> children,
            List<Item> attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces,
            ElementPSVI psvi
    ) {
        if (psvi != null) {
            XmlSchemaWhitespaceNormalizer.normalize(children, psvi);
            addDefaultAttributes(attributes, declaredNamespaces, inScopeNamespaces, psvi);
        }

        Item element = ItemFactory.getInstance().createXmlElementNode(name, children, attributes);
        for (Map.Entry<String, String> namespace : declaredNamespaces.entrySet()) {
            element.addOrReplaceNamespace(
                ItemFactory.getInstance().createXmlNamespaceNode(namespace.getKey(), namespace.getValue())
            );
        }
        if (psvi != null) {
            setSchemaProperties(element, psvi);
            element.setXmlSchemaProperties(
                element.getXmlSchemaProperties()
                    .withNilled(
                        psvi.getNil() ? XmlSchemaNodeProperties.Nilled.TRUE : XmlSchemaNodeProperties.Nilled.FALSE
                    )
            );
        }
        element.addParentToDescendants();
        return element;
    }

    private void addDefaultAttributes(
            List<Item> attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces,
            ElementPSVI elementPsvi
    ) {
        if (!(elementPsvi.getTypeDefinition() instanceof XSComplexTypeDefinition complexType)) {
            return;
        }
        XSObjectList attributeUses = complexType.getAttributeUses();
        for (int index = 0; index < attributeUses.getLength(); index++) {
            XSAttributeUse attributeUse = (XSAttributeUse) attributeUses.item(index);
            addDefaultAttribute(attributes, declaredNamespaces, inScopeNamespaces, attributeUse);
        }
    }

    private void addDefaultAttribute(
            List<Item> attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces,
            XSAttributeUse attributeUse
    ) {
        XSAttributeDeclaration declaration = attributeUse.getAttrDeclaration();
        String constraintValue = attributeUse.getConstraintValue();
        XSValue schemaValue = attributeUse.getValueConstraintValue();
        if (attributeUse.getConstraintType() == XSConstants.VC_NONE) {
            if (declaration.getConstraintType() == XSConstants.VC_NONE) {
                return;
            }
            constraintValue = declaration.getConstraintValue();
            schemaValue = declaration.getValueConstraintValue();
        }
        if (hasAttribute(attributes, declaration)) {
            return;
        }

        String namespace = declaration.getNamespace();
        String prefix = attributePrefix(namespace, declaredNamespaces, inScopeNamespaces);
        Item attribute = ItemFactory.getInstance()
            .createXmlAttributeNode(
                XmlNameCodec.fromExpandedName(namespace, prefix, declaration.getName()),
                constraintValue
            );
        attribute.setXmlSchemaProperties(
            attribute.getXmlSchemaProperties()
                .withSchemaType(
                    this.typeMapper.getTypeAnnotation(declaration.getTypeDefinition()),
                    this.typedValueFactory.create(schemaValue, declaration.getTypeDefinition())
                )
        );
        attributes.add(attribute);
    }

    private void setSchemaProperties(Item item, ItemPSVI psvi) {
        XmlSchemaNodeProperties properties = item.getXmlSchemaProperties();
        var typeAnnotation = this.typeMapper.getTypeAnnotation(psvi.getTypeDefinition());
        var typedValue = this.typedValueFactory.create(psvi, item.getStringValue());
        item.setXmlSchemaProperties(
            typedValue.map(value -> properties.withSchemaType(typeAnnotation, value))
                .orElseGet(() -> properties.withSchemaTypeWithoutTypedValue(typeAnnotation))
        );
    }

    private static boolean hasAttribute(List<Item> attributes, XSAttributeDeclaration declaration) {
        String declarationNamespace = XmlNameCodec.normalizeNamespace(declaration.getNamespace());
        for (Item attribute : attributes) {
            Name name = attribute.nodeName();
            if (
                name.getLocalName().equals(declaration.getName())
                    && XmlNameCodec.normalizeNamespace(name.getNamespace()).equals(declarationNamespace)
            ) {
                return true;
            }
        }
        return false;
    }

    private static String attributePrefix(
            String namespace,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces
    ) {
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

}
