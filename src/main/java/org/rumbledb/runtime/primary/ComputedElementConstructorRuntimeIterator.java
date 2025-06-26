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
            // Processing of the name expression according to
            // https://www.w3.org/TR/xquery-31/#id-computedElements

            // 1. Atomization is applied to the value of the name expression. If the result of atomization is not a
            // single atomic value of type xs:QName, xs:string, or xs:untypedAtomic, a type error is raised
            // [err:XPTY0004].
            List<Item> atomizedNameItems = this.nameIterator.materialize(dynamicContext);
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
