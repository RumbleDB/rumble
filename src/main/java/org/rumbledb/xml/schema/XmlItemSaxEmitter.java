/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.ValidatorHandler;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** Emits the XML information represented by an XDM item tree as SAX events. */
final class XmlItemSaxEmitter {

    private final ValidatorHandler handler;

    XmlItemSaxEmitter(ValidatorHandler handler) {
        this.handler = handler;
    }

    void emit(Item root) throws SAXException {
        this.handler.startDocument();
        emitElement(root, true);
        this.handler.endDocument();
    }

    private void emitElement(Item element, boolean root) throws SAXException {
        List<Item> namespaces = root ? element.namespaceNodes() : element.declaredNamespaceNodes();
        List<String> prefixes = new ArrayList<>(namespaces.size());
        for (Item namespace : namespaces) {
            String prefix = namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName();
            prefixes.add(prefix);
            this.handler.startPrefixMapping(prefix, namespace.getStringValue());
        }

        Name name = element.nodeName();
        String namespace = namespaceUri(name);
        String qualifiedName = qualifiedName(name);
        this.handler.startElement(namespace, name.getLocalName(), qualifiedName, attributes(element));
        for (Item child : element.children()) {
            emitChild(child);
        }
        this.handler.endElement(namespace, name.getLocalName(), qualifiedName);

        for (int i = prefixes.size() - 1; i >= 0; i--) {
            this.handler.endPrefixMapping(prefixes.get(i));
        }
    }

    private void emitChild(Item child) throws SAXException {
        if (child.isElementNode()) {
            emitElement(child, false);
        } else if (child.isTextNode()) {
            char[] text = child.getStringValue().toCharArray();
            this.handler.characters(text, 0, text.length);
        } else if (child.isProcessingInstructionNode()) {
            this.handler.processingInstruction(child.nodeName().getLocalName(), child.getStringValue());
        }
    }

    private static AttributesImpl attributes(Item element) {
        AttributesImpl result = new AttributesImpl();
        for (Item attribute : element.attributes()) {
            Name name = attribute.nodeName();
            result.addAttribute(
                namespaceUri(name),
                name.getLocalName(),
                qualifiedName(name),
                "CDATA",
                attribute.getStringValue()
            );
        }
        return result;
    }

    private static String namespaceUri(Name name) {
        return name.getNamespace() == null ? "" : name.getNamespace();
    }

    private static String qualifiedName(Name name) {
        String prefix = name.getPrefix();
        return prefix == null || prefix.isEmpty() ? name.getLocalName() : prefix + ":" + name.getLocalName();
    }
}
