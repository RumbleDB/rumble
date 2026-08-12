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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.xml.validation.ValidatorHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

/** Emits a Rumble XML element or document as namespace-aware SAX events. */
final class XmlItemSaxEmitter {

    private final ValidatorHandler handler;
    private final Consumer<String> commentHandler;

    XmlItemSaxEmitter(ValidatorHandler handler, Consumer<String> commentHandler) {
        this.handler = handler;
        this.commentHandler = commentHandler;
    }

    void emit(Item root) throws SAXException {
        this.handler.startDocument();
        if (root.isDocumentNode()) {
            for (Item child : root.children()) {
                emitChild(child, true);
            }
        } else {
            emitElement(root, true);
        }
        this.handler.endDocument();
    }

    private void emitElement(Item element, boolean root) throws SAXException {
        List<Item> namespaces = root ? element.namespaceNodes() : element.declaredNamespaceNodes();
        List<String> prefixes = new ArrayList<>(namespaces.size());
        for (Item namespace : namespaces) {
            String prefix =
                    namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName();
            if ("xml".equals(prefix)) {
                continue;
            }
            prefixes.add(prefix);
            this.handler.startPrefixMapping(prefix, namespace.getStringValue());
        }

        Name name = element.nodeName();
        String namespace = XmlNameCodec.namespaceUri(name);
        String qualifiedName = XmlNameCodec.qualifiedName(name);
        this.handler.startElement(namespace, name.getLocalName(), qualifiedName, attributes(element));
        for (Item child : element.children()) {
            emitChild(child, false);
        }
        this.handler.endElement(namespace, name.getLocalName(), qualifiedName);

        for (int index = prefixes.size() - 1; index >= 0; index--) {
            this.handler.endPrefixMapping(prefixes.get(index));
        }
    }

    private void emitChild(Item child, boolean documentChild) throws SAXException {
        if (child.isElementNode()) {
            emitElement(child, documentChild);
        } else if (child.isTextNode()) {
            char[] text = child.getStringValue().toCharArray();
            this.handler.characters(text, 0, text.length);
        } else if (child.isProcessingInstructionNode()) {
            this.handler.processingInstruction(child.nodeName().getLocalName(), child.getStringValue());
        } else if (child.isCommentNode()) {
            this.commentHandler.accept(child.getStringValue());
        } else {
            throw new SAXException("Unsupported XML child node kind " + child.nodeKind() + ".");
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
                    attribute.getStringValue());
        }
        return result;
    }
}
