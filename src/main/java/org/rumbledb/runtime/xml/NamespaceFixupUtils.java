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
 *
 */

package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.NamespaceItem;

import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class NamespaceFixupUtils {

    private NamespaceFixupUtils() {
    }

    static Item copyNodeForConstructor(Item item, RuntimeStaticContext staticContext) {
        Item copy = item.copy(true);
        if (!item.isElementNode()) {
            return copy;
        }
        configureCopiedElementNamespaces((ElementItem) item, (ElementItem) copy, staticContext, true);
        return copy;
    }

    static void applyNamespaceFixup(ElementItem element) {
        ensureElementNameBinding(element);

        List<Item> attributes = element.attributes();
        for (int i = 0; i < attributes.size(); i++) {
            Item attribute = attributes.get(i);
            if (attribute.isAttributeNode()) {
                attributes.set(i, fixupAttributeNamespace(attribute, element));
            }
        }

        List<Item> children = element.children();
        for (Item child : children) {
            if (child.isElementNode()) {
                applyNamespaceFixup((ElementItem) child);
            }
        }
    }

    private static void configureCopiedElementNamespaces(
            ElementItem original,
            ElementItem copy,
            RuntimeStaticContext staticContext,
            boolean rootOfCopiedTree
    ) {
        copy.setDeclaredNamespaces(selectPreservedNamespaces(original, staticContext.isCopyNamespacesPreserve()));
        if (rootOfCopiedTree) {
            copy.setInheritNamespacesFromParent(staticContext.isCopyNamespacesInherit());
        }

        List<Item> originalChildren = original.children();
        List<Item> copiedChildren = copy.children();
        for (int i = 0; i < originalChildren.size() && i < copiedChildren.size(); i++) {
            Item originalChild = originalChildren.get(i);
            Item copiedChild = copiedChildren.get(i);
            if (originalChild.isElementNode() && copiedChild.isElementNode()) {
                configureCopiedElementNamespaces(
                    (ElementItem) originalChild,
                    (ElementItem) copiedChild,
                    staticContext,
                    false
                );
            }
        }
    }

    private static Map<String, String> selectPreservedNamespaces(ElementItem element, boolean preserveAll) {
        Map<String, String> inScope = inScopeNamespaceMap(element);
        if (preserveAll) {
            return inScope;
        }
        Map<String, String> selected = new LinkedHashMap<>();
        for (String prefix : prefixesUsedByElementAndAttributes(element)) {
            if (inScope.containsKey(prefix)) {
                selected.put(prefix, inScope.get(prefix));
            }
        }
        return selected;
    }

    private static Set<String> prefixesUsedByElementAndAttributes(ElementItem element) {
        Set<String> result = new HashSet<>();
        addUsedPrefix(result, element.nodeName(), true);
        for (Item attribute : element.attributes()) {
            addUsedPrefix(result, attribute.nodeName(), false);
        }
        return result;
    }

    private static void addUsedPrefix(Set<String> prefixes, Name nodeName, boolean elementName) {
        if (nodeName == null) {
            return;
        }
        String namespace = normalize(nodeName.getNamespace());
        String prefix = normalize(nodeName.getPrefix());
        if (!prefix.isEmpty()) {
            prefixes.add(prefix);
            return;
        }
        if (elementName && !namespace.isEmpty()) {
            prefixes.add("");
        }
    }

    private static Map<String, String> inScopeNamespaceMap(Item element) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Item namespaceNode : element.namespaceNodes()) {
            String prefix = namespaceNode instanceof NamespaceItem namespace
                ? normalize(namespace.getPrefix())
                : normalize(namespaceNode.nodeName() == null ? "" : namespaceNode.nodeName().getLocalName());
            result.put(prefix, namespaceNode.getStringValue());
        }
        return result;
    }

    private static void ensureElementNameBinding(ElementItem element) {
        Name nodeName = element.nodeName();
        if (nodeName == null) {
            return;
        }
        String namespace = normalize(nodeName.getNamespace());
        String prefix = normalize(nodeName.getPrefix());
        String inScope = resolveInScopeNamespace(element, prefix);
        if (namespace.isEmpty()) {
            if (prefix.isEmpty() && inScope != null && !inScope.isEmpty()) {
                element.declareNamespaceBinding("", "");
            }
            return;
        }
        if (prefix.isEmpty()) {
            if (!namespace.equals(inScope)) {
                element.declareNamespaceBinding("", namespace);
            }
            return;
        }
        if (inScope == null) {
            element.declareNamespaceBinding(prefix, namespace);
            return;
        }
        if (namespace.equals(inScope)) {
            return;
        }
        String replacement = freshPrefix(element);
        element.setNodeName(new Name(namespace, replacement, nodeName.getLocalName()));
        element.declareNamespaceBinding(replacement, namespace);
    }

    private static Item fixupAttributeNamespace(Item attribute, ElementItem parent) {
        Name nodeName = attribute.nodeName();
        if (nodeName == null) {
            return attribute;
        }
        String namespace = normalize(nodeName.getNamespace());
        String prefix = normalize(nodeName.getPrefix());
        if (namespace.isEmpty()) {
            return attribute;
        }

        if (prefix.isEmpty()) {
            String replacement = freshPrefix(parent);
            parent.declareNamespaceBinding(replacement, namespace);
            return renameAttribute(attribute, new Name(namespace, replacement, nodeName.getLocalName()));
        }

        String bound = resolveInScopeNamespace(parent, prefix);
        if (bound == null) {
            parent.declareNamespaceBinding(prefix, namespace);
            return attribute;
        }
        if (namespace.equals(bound)) {
            return attribute;
        }

        String replacement = freshPrefix(parent);
        parent.declareNamespaceBinding(replacement, namespace);
        return renameAttribute(attribute, new Name(namespace, replacement, nodeName.getLocalName()));
    }

    private static Item renameAttribute(Item attribute, Name newName) {
        Item renamed = attribute.copy(true);
        ((AttributeItem) renamed).setNodeName(newName);
        renamed.setParent(attribute.parent());
        return renamed;
    }

    private static String resolveInScopeNamespace(Item element, String prefix) {
        String normalizedPrefix = normalize(prefix);
        for (Item namespaceNode : element.namespaceNodes()) {
            String namespacePrefix = namespaceNode instanceof NamespaceItem namespace
                ? normalize(namespace.getPrefix())
                : normalize(namespaceNode.nodeName() == null ? "" : namespaceNode.nodeName().getLocalName());
            if (normalizedPrefix.equals(namespacePrefix)) {
                return namespaceNode.getStringValue();
            }
        }
        return null;
    }

    private static String freshPrefix(Item context) {
        Set<String> usedPrefixes = new HashSet<>();
        for (Item namespaceNode : context.namespaceNodes()) {
            if (namespaceNode instanceof NamespaceItem namespace) {
                usedPrefixes.add(normalize(namespace.getPrefix()));
            } else if (namespaceNode.nodeName() != null) {
                usedPrefixes.add(normalize(namespaceNode.nodeName().getLocalName()));
            }
        }
        int counter = 0;
        String candidate;
        do {
            candidate = "ns" + counter++;
        } while (usedPrefixes.contains(candidate));
        return candidate;
    }

    private static String normalize(String value) {
        return value == null ? "" : value;
    }
}
