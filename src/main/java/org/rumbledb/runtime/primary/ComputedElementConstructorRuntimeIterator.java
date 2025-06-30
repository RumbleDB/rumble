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

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.exceptions.AttributeAfterNonAttributeException;
import org.rumbledb.exceptions.DuplicateAttributeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class ComputedElementConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String staticElementName;
    private AtomizationIterator nameIterator;
    private RuntimeIterator contentIterator;

    /**
     * Constructor for static element name: element elementName { content }
     * 
     * @param staticElementName The static element name
     * @param contentIterator The content iterator
     * @param staticContext The runtime static context
     */
    public ComputedElementConstructorRuntimeIterator(
            String staticElementName,
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(contentIterator), staticContext);
        this.staticElementName = staticElementName;
        this.nameIterator = null;
        this.contentIterator = contentIterator;
    }

    /**
     * Constructor for dynamic element name: element { nameExpression } { content }
     * 
     * @param nameIterator The dynamic element name iterator (wrapped in AtomizationIterator)
     * @param contentIterator The content iterator
     * @param staticContext The runtime static context
     */
    public ComputedElementConstructorRuntimeIterator(
            AtomizationIterator nameIterator,
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(nameIterator, contentIterator), staticContext);
        this.staticElementName = null;
        this.nameIterator = nameIterator;
        this.contentIterator = contentIterator;
    }

    private static List<RuntimeIterator> createChildList(RuntimeIterator... iterators) {
        List<RuntimeIterator> children = new ArrayList<>();
        for (RuntimeIterator iterator : iterators) {
            if (iterator != null) {
                children.add(iterator);
            }
        }
        return children;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
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

        // Determine the element name
        String elementName;
        if (this.staticElementName != null) {
            // Static element name
            elementName = this.staticElementName;
        } else {
            // Dynamic element name - evaluate the name expression
            // Processing of the name expression according to
            // https://www.w3.org/TR/xquery-31/#id-computedElements

            // 1. Atomization is applied to the value of the name expression. If the result of atomization is not a
            // single atomic value of type xs:QName, xs:string, or xs:untypedAtomic, a type error is raised
            // [err:XPTY0004].
            List<Item> atomizedNameItems = this.nameIterator.materialize(contextToUse);
            if (atomizedNameItems.size() != 1) {
                throw new UnexpectedStaticTypeException(
                        "Computed element constructor name must evaluate to a single atomic value of type xs:QName, xs:string, or xs:untypedAtomic"
                );
            }
            Item atomizedNameItem = atomizedNameItems.get(0);
            if (!(atomizedNameItem.isAtomic())) {
                throw new UnexpectedStaticTypeException(
                        "Computed element constructor name must evaluate to a single atomic value of type xs:QName, xs:string, or xs:untypedAtomic"
                );
            }
            // TODO: implement proper type checking when we have a stable xml type system

            // 2. If the atomized value of the name expression is of type xs:QName, that expanded QName is used as the
            // node-name property of the constructed element, retaining the prefix part of the QName.
            // 3. If the atomized value of the name expression is of type xs:string or xs:untypedAtomic, that value is
            // converted to an expanded QName. If the string value contains a namespace prefix, that prefix is resolved
            // to a namespace URI using the statically known namespaces. If the string value contains no namespace
            // prefix, it is treated as a local name in the default element/type namespace. The resulting expanded QName
            // is used as the node-name property of the constructed element, retaining the prefix part of the QName. If
            // conversion of the atomized name expression to an expanded QName is not successful, a dynamic error is
            // raised [err:XQDY0074].

            String nameString = atomizedNameItem.getStringValue();
            // TODO: implement proper QName processing with namespace resolution when we have a stable xml type system

            // A dynamic error is raised [err:XQDY0096] if the node-name of the constructed element node has any of the
            // following properties:
            // - Its namespace prefix is xmlns.
            // - Its namespace URI is http://www.w3.org/2000/xmlns/.
            // - Its namespace prefix is xml and its namespace URI is not http://www.w3.org/XML/1998/namespace.
            // - Its namespace prefix is other than xml and its namespace URI is http://www.w3.org/XML/1998/namespace.
            // TODO: implement proper validation of the element name

            elementName = nameString;
        }

        // Process content expression according to XQuery 3.1 specification
        // https://www.w3.org/TR/xquery-31/#id-computedElements
        ProcessedContent processedContent = processContentExpression(contextToUse);

        // Create and return the element item
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlElementNode(
                elementName,
                processedContent.children,
                processedContent.attributes
            );
    }

    /**
     * Processes the content expression according to the XQuery 3.1 specification.
     * 
     * Processing of the computed element constructor proceeds as follows:
     * 4. The properties of the newly constructed element node are determined as described in the specification.
     */
    private ProcessedContent processContentExpression(DynamicContext dynamicContext) {
        List<Item> rawContentSequence = new ArrayList<>();

        // Collect all content items
        if (this.contentIterator != null) {
            this.contentIterator.open(dynamicContext);
            while (this.contentIterator.hasNext()) {
                Item item = this.contentIterator.next();
                rawContentSequence.add(item);
            }
            this.contentIterator.close();
        }

        // 1. If the content sequence contains a document node, the document node is replaced in the content
        // sequence by its children.
        List<Item> expandedContentSequence = expandDocumentNodes(rawContentSequence);

        // 3. If the content sequence contains an attribute node or a namespace node following a node that is not an
        // attribute node or a namespace node, a type error is raised [err:XQTY0024].
        validateAttributeOrdering(expandedContentSequence);

        // Separate attributes from other content
        List<Item> attributes = new ArrayList<>();
        List<Item> nonAttributeContent = new ArrayList<>();

        for (Item item : expandedContentSequence) {
            if (item.isAttributeNode()) {
                attributes.add(item);
            } else if (item.isNode()) {
                // TODO: add namespace node check when we have namespace node support
                nonAttributeContent.add(item);
            } else {
                // Non-node items are converted to text nodes
                String textContent = item.getStringValue();
                if (!textContent.isEmpty()) {
                    nonAttributeContent.add(
                        ItemFactory.getInstance().createXmlTextNode(textContent)
                    );
                }
            }
        }

        // 2. Adjacent text nodes in the content sequence are merged into a single text node by concatenating their
        // contents, with no intervening blanks. After concatenation, any text node whose content is a zero-length
        // string
        // is deleted from the content sequence.
        List<Item> mergedChildren = mergeAdjacentTextNodes(nonAttributeContent);

        // Validate that no duplicate attribute names exist
        // If two or more attributes have the same node-name, a dynamic error is raised [err:XQDY0025].
        validateNoDuplicateAttributes(attributes);

        // TODO: Set parent property of each attribute and child node to the newly constructed element node
        // TODO: Handle xml:space attribute validation [err:XQDY0092]
        // TODO: Handle xml:base attribute for base-uri setting
        // TODO: Compute in-scope-namespaces as described in 3.9.4 In-scope Namespaces of a Constructed Element
        // TODO: Set other properties (nilled, string-value, typed-value, type-name, is-id, is-idrefs) when we have a
        // stable XML type system

        return new ProcessedContent(mergedChildren, attributes);
    }

    /**
     * 1: Replace document nodes with their children.
     */
    private List<Item> expandDocumentNodes(List<Item> contentSequence) {
        List<Item> expandedSequence = new ArrayList<>();

        for (Item item : contentSequence) {
            if (item.isDocumentNode()) {
                // Replace document node with its children
                expandedSequence.addAll(item.children());
            } else {
                expandedSequence.add(item);
            }
        }

        return expandedSequence;
    }

    /**
     * 3: Validate that attribute nodes and namespace nodes only appear before non-attribute/non-namespace nodes.
     */
    private void validateAttributeOrdering(List<Item> contentSequence) {
        boolean hasSeenNonAttributeNode = false;

        for (Item item : contentSequence) {
            if (item.isAttributeNode()) {
                // TODO: add namespace node check when we have namespace node support
                if (hasSeenNonAttributeNode) {
                    throw new AttributeAfterNonAttributeException(
                            "Attribute nodes must appear before all other nodes in element content"
                    );
                }
            } else if (item.isNode()) {
                hasSeenNonAttributeNode = true;
            } else {
                // Non-node items (which become text nodes) count as non-attribute nodes
                hasSeenNonAttributeNode = true;
            }
        }
    }

    /**
     * 2: Merge adjacent text nodes and remove empty text nodes.
     */
    private List<Item> mergeAdjacentTextNodes(List<Item> contentSequence) {
        List<Item> mergedSequence = new ArrayList<>();
        StringBuilder textAccumulator = null;

        for (Item item : contentSequence) {
            if (item.isTextNode()) {
                String textContent = item.getTextValue();

                if (textAccumulator == null) {
                    textAccumulator = new StringBuilder();
                }
                textAccumulator.append(textContent);
            } else {
                // Non-text node encountered
                if (textAccumulator != null) {
                    // Finalize any accumulated text content
                    String accumulatedText = textAccumulator.toString();
                    if (!accumulatedText.isEmpty()) {
                        mergedSequence.add(
                            ItemFactory.getInstance().createXmlTextNode(accumulatedText)
                        );
                    }
                    textAccumulator = null;
                }

                mergedSequence.add(item);
            }
        }

        // Handle any remaining accumulated text at the end
        if (textAccumulator != null) {
            String accumulatedText = textAccumulator.toString();
            if (!accumulatedText.isEmpty()) {
                mergedSequence.add(
                    ItemFactory.getInstance().createXmlTextNode(accumulatedText)
                );
            }
        }

        return mergedSequence;
    }

    /**
     * Validate that no two attributes have the same node-name.
     * If two or more attributes have the same node-name, a dynamic error is raised [err:XQDY0025].
     */
    private void validateNoDuplicateAttributes(List<Item> attributes) {
        Set<String> attributeNames = new HashSet<>();

        for (Item attribute : attributes) {
            if (attribute.isAttributeNode()) {
                String attributeName = attribute.nodeName();
                if (attributeNames.contains(attributeName)) {
                    throw new DuplicateAttributeException(attributeName);
                }
                attributeNames.add(attributeName);
            }
        }
    }

    /**
     * Helper class to hold processed content with separated attributes and children.
     */
    private static class ProcessedContent {
        final List<Item> children;
        final List<Item> attributes;

        ProcessedContent(List<Item> children, List<Item> attributes) {
            this.children = children;
            this.attributes = attributes;
        }
    }
}
