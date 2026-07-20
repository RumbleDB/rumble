/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.validation.ValidatorHandler;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** Emits the XML information represented by an XDM item tree as SAX events. */
final class XmlItemSaxEmitter {

    private static final String LAX_VALIDATION_WRAPPER = "rumble-lax-validation-wrapper";
    private final ValidatorHandler handler;
    private final Consumer<String> commentHandler;

    XmlItemSaxEmitter(ValidatorHandler handler, Consumer<String> commentHandler) {
        this.handler = handler;
        this.commentHandler = commentHandler;
    }

    void emit(Item root) throws SAXException {
        this.handler.startDocument();
        emitElement(root, true);
        this.handler.endDocument();
    }

    /** Emits the supplied root as the child assessed by an {@code xs:anyType} lax wildcard. */
    void emitAsLaxWildcard(Item root) throws SAXException {
        this.handler.startDocument();
        this.handler.startElement("", LAX_VALIDATION_WRAPPER, LAX_VALIDATION_WRAPPER, new AttributesImpl());
        emitElement(root, true);
        this.handler.endElement("", LAX_VALIDATION_WRAPPER, LAX_VALIDATION_WRAPPER);
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
        String namespace = XmlNameCodec.namespaceUri(name);
        String qualifiedName = XmlNameCodec.qualifiedName(name);
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
        } else if (child.isCommentNode()) {
            this.commentHandler.accept(child.getStringValue());
        }
    }

    private static AttributesImpl attributes(Item element) {
        AttributesImpl result = new AttributesImpl();
        for (Item attribute : element.attributes()) {
            Name name = attribute.nodeName();
            result.addAttribute(
                XmlNameCodec.namespaceUri(name),
                name.getLocalName(),
                XmlNameCodec.qualifiedName(name),
                "CDATA",
                attribute.getStringValue()
            );
        }
        return result;
    }

}
