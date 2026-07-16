/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/** Builds a new XDM element tree from Xerces's augmented SAX and PSVI callbacks. */
final class PsviNodeBuilder extends DefaultHandler {

    private final PSVIProvider psviProvider;
    private final PsviItemFactory itemFactory;
    private final Deque<ElementFrame> elements;
    private final Map<String, String> pendingNamespaces;
    private Item result;

    PsviNodeBuilder(PSVIProvider psviProvider, XmlSchemaTypeMapper typeMapper) {
        this.psviProvider = psviProvider;
        this.itemFactory = new PsviItemFactory(psviProvider, typeMapper);
        this.elements = new ArrayDeque<>();
        this.pendingNamespaces = new LinkedHashMap<>();
    }

    Item getResult() {
        return this.result;
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        this.pendingNamespaces.put(prefix, uri);
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) {
        Map<String, String> declaredNamespaces = new LinkedHashMap<>(this.pendingNamespaces);
        Map<String, String> inScopeNamespaces = this.elements.isEmpty()
            ? new LinkedHashMap<>()
            : new LinkedHashMap<>(this.elements.peek().inScopeNamespaces());
        inScopeNamespaces.putAll(declaredNamespaces);
        this.elements.push(
            new ElementFrame(
                    XmlNameCodec.fromSax(uri, localName, qualifiedName),
                    new ArrayList<>(),
                    this.itemFactory.createAttributes(attributes, declaredNamespaces, inScopeNamespaces),
                    declaredNamespaces,
                    inScopeNamespaces
            )
        );
        this.pendingNamespaces.clear();
    }

    @Override
    public void characters(char[] characters, int start, int length) {
        if (length == 0) {
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
        this.elements.peek().children().add(ItemFactory.getInstance().createXmlProcessingInstructionNode(target, data));
    }

    void comment(String content) {
        this.elements.peek().children().add(ItemFactory.getInstance().createXmlCommentNode(content));
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) {
        ElementFrame frame = this.elements.pop();
        ElementPSVI psvi = this.psviProvider.getElementPSVI();
        Item element = this.itemFactory.createElement(
            frame.name(),
            frame.children(),
            frame.attributes(),
            frame.declaredNamespaces(),
            frame.inScopeNamespaces(),
            psvi
        );

        if (this.elements.isEmpty()) {
            this.result = element;
        } else {
            this.elements.peek().children().add(element);
        }
    }

    private record ElementFrame(
            Name name,
            List<Item> children,
            List<Item> attributes,
            Map<String, String> declaredNamespaces,
            Map<String, String> inScopeNamespaces) {
    }
}
