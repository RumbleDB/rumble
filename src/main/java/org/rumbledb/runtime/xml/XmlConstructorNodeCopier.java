/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.xml;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.ConstructionMode;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XmlSchemaNodeProperties;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

/** Copies nodes inserted by element and document constructors and applies construction mode. */
final class XmlConstructorNodeCopier {

    private XmlConstructorNodeCopier() {
    }

    static Item copy(Item node, ConstructionMode mode, ExceptionMetadata metadata) {
        if (
            mode == ConstructionMode.PRESERVE
                && node.isAttributeNode()
                && hasNamespaceSensitiveTypedValue(node)
        ) {
            throw new RumbleException(
                    "A namespace-sensitive attribute cannot be copied without its parent element.",
                    ErrorCode.NamespaceSensitiveConstructionErrorCode,
                    metadata
            );
        }
        Item copy = node.copy(false);
        if (mode == ConstructionMode.STRIP) {
            stripSchemaProperties(copy);
        }
        restoreParentLinks(copy);
        return copy;
    }

    static void initializeConstructedElement(Item element, ConstructionMode mode) {
        XmlSchemaTypeAnnotation type = mode == ConstructionMode.PRESERVE
            ? XmlSchemaTypeAnnotation.anyType()
            : XmlSchemaTypeAnnotation.untypedElement();
        element.setXmlSchemaProperties(
            XmlSchemaNodeProperties.none()
                .withSchemaType(type, untypedValue(element))
                .withNilled(XmlSchemaNodeProperties.Nilled.FALSE)
        );
    }

    static void initializeConstructedAttribute(Item attribute) {
        if (isXmlId(attribute)) {
            return;
        }
        attribute.setXmlSchemaProperties(
            XmlSchemaNodeProperties.none()
                .withSchemaType(XmlSchemaTypeAnnotation.untypedAttribute(), untypedValue(attribute))
                .withIdentityProperties(false, false)
        );
    }

    private static void stripSchemaProperties(Item node) {
        if (node.isElementNode()) {
            node.setXmlSchemaProperties(
                XmlSchemaNodeProperties.none()
                    .withSchemaType(XmlSchemaTypeAnnotation.untypedElement(), untypedValue(node))
                    .withNilled(XmlSchemaNodeProperties.Nilled.FALSE)
            );
            node.attributes().forEach(XmlConstructorNodeCopier::stripSchemaProperties);
            node.children().forEach(XmlConstructorNodeCopier::stripSchemaProperties);
        } else if (node.isAttributeNode()) {
            node.setXmlSchemaProperties(
                XmlSchemaNodeProperties.none()
                    .withSchemaType(XmlSchemaTypeAnnotation.untypedAttribute(), untypedValue(node))
                    .withIdentityProperties(isXmlId(node), false)
            );
        } else if (node.isDocumentNode()) {
            node.children().forEach(XmlConstructorNodeCopier::stripSchemaProperties);
        }
    }

    private static void restoreParentLinks(Item node) {
        if (node.isElementNode()) {
            node.attributes().forEach(XmlConstructorNodeCopier::restoreParentLinks);
            node.children().forEach(XmlConstructorNodeCopier::restoreParentLinks);
            node.addParentToDescendants();
        } else if (node.isDocumentNode()) {
            node.children().forEach(XmlConstructorNodeCopier::restoreParentLinks);
            node.addParentToDescendants();
        }
    }

    private static boolean hasNamespaceSensitiveTypedValue(Item node) {
        XmlSchemaNodeProperties properties = node.getXmlSchemaProperties();
        return properties.hasTypedValue()
            && properties.typedValue().stream().anyMatch(value -> value.isQName() || value.isNotation());
    }

    private static boolean isXmlId(Item attribute) {
        Name name = attribute.nodeName();
        return Name.XML_NS.equals(name.getNamespace()) && "id".equals(name.getLocalName());
    }

    private static List<Item> untypedValue(Item node) {
        return List.of(ItemFactory.getInstance().createUntypedAtomicItem(node.getStringValue()));
    }
}
