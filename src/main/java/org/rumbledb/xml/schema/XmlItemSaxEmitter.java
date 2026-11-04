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
package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.validation.ValidatorHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.xml.ElementItem;

/**
 * Emits a Rumble XML element or document as namespace-aware SAX events.
 *
 * Xerces validates SAX events, but RumbleDB stores XML as Item objects. This class bridges those models.
 */
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
                emitChild(child, Map.of());
            }
        } else {
            emitElement(root, Map.of());
        }
        this.handler.endDocument();
    }

    /**
     * Emits an element node and its children as SAX events.
     */
    private void emitElement(Item element, Map<String, String> inheritedNamespaces) throws SAXException {
        // Carry inherited bindings explicitly: parsed trees may omit parent pointers.
        Map<String, String> namespaces = ((ElementItem) element).isInheritNamespacesFromParent()
                ? new LinkedHashMap<>(inheritedNamespaces)
                : new LinkedHashMap<>();
        for (Item namespace : element.namespaceNodes()) {
            String prefix =
                    namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName();
            if ("xml".equals(prefix)) {
                continue;
            }
            namespaces.put(prefix, namespace.getStringValue());
        }
        for (Item namespace : element.declaredNamespaceNodes()) {
            if (namespace.getStringValue().isEmpty()) {
                namespaces.remove(
                        namespace.nodeName() == null ? "" : namespace.nodeName().getLocalName());
            }
        }
        List<String> prefixes = new ArrayList<>();
        // XDM children need not inherit every parent binding. Explicitly end those bindings
        // in the SAX namespace context so validation sees exactly the source node's scope.
        for (String prefix : inheritedNamespaces.keySet()) {
            if (!namespaces.containsKey(prefix)) {
                prefixes.add(prefix);
                this.handler.startPrefixMapping(prefix, "");
            }
        }
        for (Map.Entry<String, String> binding : namespaces.entrySet()) {
            if (!binding.getValue().equals(inheritedNamespaces.get(binding.getKey()))) {
                prefixes.add(binding.getKey());
                this.handler.startPrefixMapping(binding.getKey(), binding.getValue());
            }
        }

        Name name = element.nodeName();
        String namespace = XmlNameCodec.namespaceUri(name);
        String qualifiedName = XmlNameCodec.qualifiedName(name);
        this.handler.startElement(namespace, name.getLocalName(), qualifiedName, attributes(element));
        for (Item child : element.children()) {
            emitChild(child, namespaces);
        }
        this.handler.endElement(namespace, name.getLocalName(), qualifiedName);

        for (int index = prefixes.size() - 1; index >= 0; index--) {
            this.handler.endPrefixMapping(prefixes.get(index));
        }
    }

    /**
     * Emits a child node as SAX events.
     */
    private void emitChild(Item child, Map<String, String> inheritedNamespaces) throws SAXException {
        if (child.isElementNode()) {
            emitElement(child, inheritedNamespaces);
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
