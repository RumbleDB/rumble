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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        // Determine the element name
        String elementName;
        if (this.staticElementName != null) {
            // Static element name
            elementName = this.staticElementName;
        } else {
            // Dynamic element name - evaluate the name expression
            // processing of the name expression according to
            // https://www.w3.org/TR/xquery-31/#id-computedElements

            // 1. atomization is applied to the value of the name expression
            List<Item> atomizedNameItems = this.nameIterator.materialize(dynamicContext);
            // 1. (cont.) If the result of atomization is not a single atomic value of type xs:QName, xs:string, or
            // xs:untypedAtomic, a type error is raised [err:XPTY0004].
            // TODO: better type checking of the name expression. As soon as we have a stable xml type system, we should
            // use it here.
            if (atomizedNameItems.size() != 1) {
                throw new UnexpectedStaticTypeException(
                        "Computed element constructor name must evaluate to a single atomic value"
                );
            }
            Item atomizedNameItem = atomizedNameItems.get(0);
            if (!(atomizedNameItem.isAtomic())) {
                throw new UnexpectedStaticTypeException(
                        "Computed element constructor name must evaluate to a single atomic value"
                );
            }

            elementName = atomizedNameItem.getStringValue();

        }

        // Process content
        List<Item> content = new ArrayList<>();
        if (this.contentIterator != null) {
            StringBuilder textAccumulator = null;

            this.contentIterator.open(dynamicContext);
            while (this.contentIterator.hasNext()) {
                Item item = this.contentIterator.next();

                // TODO: check spec how content is handled in case of computed element constructors
                // check if this item should be treated as text content
                // both proper text nodes, or any non-node items (e.g. generated by enclosed expressions) are
                // treated as text nodes.
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
                        continue;
                    }

                    if (textAccumulator == null) {
                        // start accumulating text content
                        textAccumulator = new StringBuilder();
                    }

                    // accumulate the text content
                    textAccumulator.append(textContent);
                } else {
                    // non-text node encountered
                    if (textAccumulator != null) {
                        // finalize any accumulated text content
                        content.add(
                            ItemFactory.getInstance()
                                .createXmlTextNode(
                                    textAccumulator.toString()
                                )
                        );
                        textAccumulator = null;
                    }

                    // add the non-text node
                    content.add(item);
                }
            }
            this.contentIterator.close();

            // handle any remaining accumulated text at the end
            if (textAccumulator != null) {
                content.add(
                    ItemFactory.getInstance()
                        .createXmlTextNode(
                            textAccumulator.toString()
                        )
                );
            }
        }

        // create and return the element item
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlElementNode(
                elementName,
                content,
                new ArrayList<>() // TODO: add support for attributes in computed element constructors
            );
    }
}
