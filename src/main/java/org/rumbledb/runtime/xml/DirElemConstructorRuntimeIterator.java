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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.AttributeOrNamespaceAfterNonAttributeException;
import org.rumbledb.exceptions.DuplicateAttributeException;
import org.rumbledb.expressions.xml.NamespaceDeclaration;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * Runtime iterator for direct element constructors.
 *
 * @see org.rumbledb.expressions.xml.DirElemConstructorExpression
 */
public class DirElemConstructorRuntimeIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Name elementName;
    private final List<ItemRuntimePlan> content;
    private final List<AttributeNodeRuntimeIterator> attributes;
    private final List<NamespaceDeclaration> namespaceDeclarations;

    public DirElemConstructorRuntimeIterator(
            Name elementName,
            List<ItemRuntimePlan> content,
            List<AttributeNodeRuntimeIterator> attributes,
            List<NamespaceDeclaration> namespaceDeclarations,
            RuntimeStaticContext staticContext) {
        super(
                Stream.concat(attributes.stream().<ItemRuntimePlan>map(iterator -> iterator), content.stream())
                        .toList(),
                staticContext);
        this.content = content;
        this.attributes = attributes;
        this.namespaceDeclarations = namespaceDeclarations;
        this.elementName = elementName;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        BiFunction<ItemRuntimePlan, DynamicContext, List<Item>> materialize =
                (iterator, childContext) -> iterator.materialize(childContext);
        // Check if this is the top-level runtime iterator for XML tree building
        DynamicContext contextToUse;
        if (dynamicContext.getTopLevelRuntimeIterator() == null) {
            // This is the top-level runtime iterator - create a new context and set this iterator as top-level
            contextToUse = new DynamicContext(dynamicContext);
            contextToUse.setTopLevelRuntimeIterator(this);
        } else {
            // A top-level iterator is already set - use the provided context
            contextToUse = dynamicContext;
        }
        List<Item> content = new ArrayList<>();
        List<Item> attributes = new ArrayList<>();
        List<Item> namespaces = new ArrayList<>();
        // process all child content
        if (this.content != null) {
            StringBuilder textAccumulator = null;
            boolean hasSeenNonAttributeNode = false;
            for (ItemRuntimePlan iterator : this.content) {
                boolean previousItemWasAtomic = false;
                for (Item childItem : materialize.apply(iterator, contextToUse)) {
                    List<Item> expandedItems = new ArrayList<>();
                    XmlConstructorContentUtils.appendExpandedItem(childItem, expandedItems);
                    for (Item item : expandedItems) {
                        if (item.isAttributeNode() || item.isNamespaceNode()) {
                            if (hasSeenNonAttributeNode) {
                                throw new AttributeOrNamespaceAfterNonAttributeException(
                                        "Attribute or namespace nodes must appear before all other nodes in element content");
                            }
                            if (item.isAttributeNode()) {
                                attributes.add(item.copy(true));
                            } else {
                                namespaces.add(item.copy(true));
                            }
                            continue;
                        }
                        // check if this item should be treated as text content
                        // both proper text nodes, or any non-node items (e.g. generated by enclosed expressions) are
                        // treated as text nodes in the context of a direct element constructor.
                        if (item.isTextNode() || !item.isNode()) {
                            String textContent;
                            if (item.isTextNode()) {
                                textContent = item.getTextValue();
                            } else {
                                // non-node item - convert to string
                                textContent = item.getStringValue();
                            }
                            // skip empty text content according to XML spec
                            if (textContent.isEmpty()) {
                                previousItemWasAtomic = item.isAtomic();
                                continue;
                            }
                            if (textAccumulator == null) {
                                // start accumulating text content
                                textAccumulator = new StringBuilder();
                            }
                            if (item.isAtomic() && previousItemWasAtomic) {
                                textAccumulator.append(' ');
                            }
                            // accumulate the text content
                            textAccumulator.append(textContent);
                            hasSeenNonAttributeNode = true;
                            previousItemWasAtomic = item.isAtomic();
                        } else {
                            hasSeenNonAttributeNode = true;
                            // non-text node encountered
                            if (textAccumulator != null) {
                                // finalize any accumulated text content
                                content.add(ItemFactory.getInstance().createXmlTextNode(textAccumulator.toString()));
                                textAccumulator = null;
                            }
                            // add the non-text node
                            content.add(NamespaceFixupUtils.copyNodeForConstructor(item, this.staticContext));
                            previousItemWasAtomic = false;
                        }
                    }
                }
            }
            // handle any remaining accumulated text at the end
            if (textAccumulator != null) {
                hasSeenNonAttributeNode = true;
                content.add(ItemFactory.getInstance().createXmlTextNode(textAccumulator.toString()));
            }
        }
        // process namespace declaration attributes (they create namespace nodes, not attribute nodes)
        if (this.namespaceDeclarations != null) {
            for (NamespaceDeclaration declaration : this.namespaceDeclarations) {
                String prefix = declaration.getPrefix();
                String uri = declaration.getUri();
                NamespaceBindingUtils.validateNamespaceDeclaration(prefix, uri);
                namespaces.add(ItemFactory.getInstance().createXmlNamespaceNode(prefix, uri));
            }
        }
        // process regular attributes
        if (this.attributes != null) {
            for (ItemRuntimePlan iterator : this.attributes) {
                for (Item item : materialize.apply(iterator, contextToUse)) {
                    // attributes should be attribute nodes
                    if (item.isAttributeNode()) {
                        attributes.add(item.copy(true));
                    }
                }
            }
        }
        validateNoDuplicateAttributes(attributes);
        // create and return the element item
        ElementItem elementItem =
                (ElementItem) ItemFactory.getInstance().createXmlElementNode(this.elementName, content, attributes);
        // Only add namespaces explicitly declared on this element
        for (Item namespace : namespaces) {
            elementItem.addOrReplaceNamespace(namespace);
        }
        // set the parent of the child nodes to the element node
        elementItem.addParentToDescendants();
        NamespaceFixupUtils.applyNamespaceFixup(elementItem);
        // Set XML document position if this is the top-level runtime iterator
        if (dynamicContext.getTopLevelRuntimeIterator() == null) {
            // This is the top-level runtime iterator - set XML document positions recursively
            String documentPath = XMLDocumentPosition.generateConstructedTreePath();
            elementItem.setXmlDocumentPosition(documentPath, 0);
        }
        return elementItem;
    }

    private void validateNoDuplicateAttributes(List<Item> attributes) {
        Set<Name> attributeNames = new HashSet<>();

        for (Item attribute : attributes) {
            if (!attribute.isAttributeNode()) {
                continue;
            }
            Name expanded = attribute.nodeName();
            if (expanded == null) {
                continue;
            }
            if (attributeNames.contains(expanded)) {
                throw new DuplicateAttributeException(expanded.toString(), getMetadata());
            }
            attributeNames.add(expanded);
        }
    }
}
