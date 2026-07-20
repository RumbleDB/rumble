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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runtime iterator for document node constructors.
 * 
 * Document node constructors create document nodes according to the XQuery 3.1 specification.
 * All document node constructors are computed constructors. The result of a document node
 * constructor is a new document node, with its own node identity.
 * 
 * @see org.rumbledb.expressions.xml.DocumentNodeConstructorExpression
 */
public class DocumentNodeConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator contentIterator;

    /**
     * Constructor for document node constructor runtime iterator
     * 
     * @param contentIterator Iterator for the content expression
     * @param staticContext The static context
     */
    public DocumentNodeConstructorRuntimeIterator(
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            contentIterator != null ? Collections.singletonList(contentIterator) : Collections.emptyList(),
            staticContext
        );
        this.contentIterator = contentIterator;
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

        // Process content expression according to specification,
        // The content expression of a document node constructor is processed in exactly the same way
        // as an enclosed expression in the content of a direct element constructor, as described in
        // Step 1e of 3.9.1.3 Content. The result of processing the content expression is a sequence
        // of nodes called the content sequence.
        List<Item> processedContent = processContentExpression(contextToUse);

        // Create and return the document node item
        this.hasNext = false;
        Item documentItem = ItemFactory.getInstance()
            .createXmlDocumentNode(
                processedContent
            );

        // Set the parent of the child nodes to the document node
        documentItem.addParentToDescendants();

        // Set XML document position if this is the top-level runtime iterator
        if (dynamicContext.getTopLevelRuntimeIterator() == null) {
            // This is the top-level runtime iterator - set XML document positions recursively
            String documentPath = XMLDocumentPosition.generateConstructedTreePath();
            documentItem.setXmlDocumentPosition(documentPath, 0);
        }

        return documentItem;
    }

    /**
     * Processes the content expression of the document node constructor.
     * 
     * Processing of the document node constructor proceeds as follows:
     * 1. If the content sequence contains a document node, the document node is replaced in the content
     * sequence by its children.
     * 2. Adjacent text nodes in the content sequence are merged into a single text node by concatenating
     * their contents, with no intervening blanks. After concatenation, any text node whose content
     * is a zero-length string is deleted from the content sequence.
     * 3. If the content sequence contains an attribute node, a type error is raised [err:XPTY0004].
     * 4. If the content sequence contains a namespace node, a type error is raised [err:XPTY0004].
     */
    private List<Item> processContentExpression(DynamicContext dynamicContext) {
        List<Item> contentSequence = new ArrayList<>();
        StringBuilder textAccumulator = null;
        boolean previousItemWasAtomic = false;

        // Collect all content items
        if (this.contentIterator != null) {
            this.contentIterator.open(dynamicContext);
            while (this.contentIterator.hasNext()) {
                List<Item> expandedItems = new ArrayList<>();
                XmlConstructorContentUtils.appendExpandedItem(this.contentIterator.next(), expandedItems);
                for (Item item : expandedItems) {
                    if (item.isAttributeNode() || item.isNamespaceNode()) {
                        if (textAccumulator != null) {
                            flushTextAccumulator(contentSequence, textAccumulator);
                            textAccumulator = null;
                        }
                        contentSequence.add(item);
                        previousItemWasAtomic = false;
                        continue;
                    }

                    if (item.isTextNode() || !item.isNode()) {
                        String textContent = item.isTextNode() ? item.getTextValue() : item.getStringValue();
                        if (textContent.isEmpty()) {
                            previousItemWasAtomic = item.isAtomic();
                            continue;
                        }
                        if (textAccumulator == null) {
                            textAccumulator = new StringBuilder();
                        }
                        if (item.isAtomic() && previousItemWasAtomic) {
                            textAccumulator.append(' ');
                        }
                        textAccumulator.append(textContent);
                        previousItemWasAtomic = item.isAtomic();
                        continue;
                    }

                    if (textAccumulator != null) {
                        flushTextAccumulator(contentSequence, textAccumulator);
                        textAccumulator = null;
                    }
                    contentSequence.add(item);
                    previousItemWasAtomic = false;
                }
            }
            this.contentIterator.close();
        }
        if (textAccumulator != null) {
            flushTextAccumulator(contentSequence, textAccumulator);
        }

        // 3. If the content sequence contains an attribute node, a type error is raised [err:XPTY0004].
        // 4. If the content sequence contains a namespace node, a type error is raised [err:XPTY0004].
        validateNoAttributesOrNamespaces(contentSequence);

        // 2. Adjacent text nodes in the content sequence are merged into a single text node by concatenating
        // their contents, with no intervening blanks. After concatenation, any text node whose content
        // is a zero-length string is deleted from the content sequence.
        return mergeAdjacentTextNodes(contentSequence);
    }

    private void flushTextAccumulator(List<Item> contentSequence, StringBuilder textAccumulator) {
        String accumulatedText = textAccumulator.toString();
        if (!accumulatedText.isEmpty()) {
            contentSequence.add(ItemFactory.getInstance().createXmlTextNode(accumulatedText));
        }
    }

    /**
     * Validates that the content sequence contains no attribute or namespace nodes.
     * 3. If the content sequence contains an attribute node, a type error is raised [err:XPTY0004].
     * 4. If the content sequence contains a namespace node, a type error is raised [err:XPTY0004].
     */
    private void validateNoAttributesOrNamespaces(List<Item> contentSequence) {
        for (Item item : contentSequence) {
            if (item.isAttributeNode()) {
                // 3. If the content sequence contains an attribute node, a type error is raised [err:XPTY0004].
                throw new UnexpectedStaticTypeException(
                        "Document node constructor content cannot contain attribute nodes [err:XPTY0004]"
                );
            }
            // 4. If the content sequence contains a namespace node, a type error is raised [err:XPTY0004].
            if (item.isNamespaceNode()) {
                throw new UnexpectedStaticTypeException(
                        "Document node constructor content cannot contain namespace nodes [err:XPTY0004]"
                );
            }
        }
    }

    /**
     * Merges adjacent text nodes in the content sequence.
     * 2. Adjacent text nodes in the content sequence are merged into a single text node by concatenating
     * their contents, with no intervening blanks. After concatenation, any text node whose content
     * is a zero-length string is deleted from the content sequence.
     */
    private List<Item> mergeAdjacentTextNodes(List<Item> contentSequence) {
        List<Item> mergedSequence = new ArrayList<>();
        StringBuilder textAccumulator = null;

        for (Item item : contentSequence) {
            if (item.isTextNode()) {
                String textContent = item.getStringValue();

                if (textAccumulator == null) {
                    // Start accumulating text nodes
                    textAccumulator = new StringBuilder();
                }

                // Accumulate the text content
                textAccumulator.append(textContent);
            } else {
                // Non-text node encountered
                if (textAccumulator != null) {
                    // Finalize any accumulated text nodes
                    String accumulatedText = textAccumulator.toString();
                    // After concatenation, any text node whose content is a zero-length string is deleted
                    // from the content sequence.
                    if (!accumulatedText.isEmpty()) {
                        mergedSequence.add(ItemFactory.getInstance().createXmlTextNode(accumulatedText));
                    }
                    textAccumulator = null;
                }

                // Add the non-text node
                mergedSequence.add(item);
            }
        }

        // Handle any remaining accumulated text at the end
        if (textAccumulator != null) {
            String accumulatedText = textAccumulator.toString();
            // After concatenation, any text node whose content is a zero-length string is deleted
            // from the content sequence.
            if (!accumulatedText.isEmpty()) {
                mergedSequence.add(ItemFactory.getInstance().createXmlTextNode(accumulatedText));
            }
        }

        return mergedSequence;
    }
}
