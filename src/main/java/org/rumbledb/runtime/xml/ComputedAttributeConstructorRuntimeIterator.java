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
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComputedAttributeConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String staticAttributeName;
    private AtomizationIterator nameIterator;
    private AtomizationIterator contentExpression;

    /**
     * Constructor for static attribute name: attribute attributeName { value }
     * 
     * @param staticAttributeName The static attribute name
     * @param contentExpression The value iterator
     * @param staticContext The runtime static context
     */
    public ComputedAttributeConstructorRuntimeIterator(
            String staticAttributeName,
            AtomizationIterator contentExpression,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(contentExpression), staticContext);
        this.staticAttributeName = staticAttributeName;
        this.nameIterator = null;
        this.contentExpression = contentExpression;
    }

    /**
     * Constructor for dynamic attribute name: attribute { nameExpression } { value }
     * 
     * @param nameIterator The dynamic attribute name iterator (wrapped in AtomizationIterator)
     * @param contentExpression The value iterator
     * @param staticContext The runtime static context
     */
    public ComputedAttributeConstructorRuntimeIterator(
            AtomizationIterator nameIterator,
            AtomizationIterator contentExpression,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(nameIterator, contentExpression), staticContext);
        this.staticAttributeName = null;
        this.nameIterator = nameIterator;
        this.contentExpression = contentExpression;
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
        // Determine the attribute name
        String attributeName;
        if (this.staticAttributeName != null) {
            // Static attribute name
            attributeName = this.staticAttributeName;
        } else {
            // Dynamic attribute name - evaluate the name expression
            // processing of the name expression according to
            // https://www.w3.org/TR/xquery-31/#id-computedAttributes

            // 1. Atomization is applied to the result of the name expression. If the result of
            // atomization is not a single atomic value of type xs:QName, xs:string, or
            // xs:untypedAtomic, a type error is raised [err:XPTY0004].
            List<Item> atomizedNameItems = this.nameIterator.materialize(dynamicContext);
            if (atomizedNameItems.size() != 1) {
                throw new UnexpectedStaticTypeException(
                        "Computed attribute constructor name must evaluate to a single atomic value"
                );
            }
            Item atomizedNameItem = atomizedNameItems.get(0);
            if (!(atomizedNameItem.isAtomic())) {
                throw new UnexpectedStaticTypeException(
                        "Computed attribute constructor name must evaluate to a single atomic value"
                );
            }

            // 2. If the atomized value of the name expression is of type xs:QName:
            // a. If the expanded QName returned by the atomized name expression has a namespace URI
            // but has no prefix, it is given an implementation-dependent prefix.
            // b. The resulting expanded QName (including its prefix) is used as the node-name
            // property of the constructed attribute node.
            //
            // 3. If the atomized value of the name expression is of type xs:string or xs:untypedAtomic,
            // that value is converted to an expanded QName. If the string value contains a namespace
            // prefix, that prefix is resolved to a namespace URI using the statically known namespaces.
            // If the string value contains no namespace prefix, it is treated as a local name in no
            // namespace. The resulting expanded QName (including its prefix) is used as the node-name
            // property of the constructed attribute. If conversion of the atomized name expression to
            // an expanded QName is not successful, a dynamic error is raised [err:XQDY0074].

            // For now, we implement simplified processing by converting to string value
            // TODO: Implement full QName processing with namespace resolution when we have a stable xml type system
            attributeName = atomizedNameItem.getStringValue();
        }

        // Process content expression according to XQuery 3.1 spec
        // https://www.w3.org/TR/xquery-31/#id-computedAttributes
        StringBuilder contentExpressionBuilder = new StringBuilder();

        if (this.contentExpression != null) {
            // 1: Atomization is applied to the result of the content expression,
            // converting it to a sequence of atomic values. (If the content expression
            // is absent, the result of this step is an empty sequence.)
            // Note: contentExpression is already an AtomizationIterator
            List<Item> atomizedContentItems = this.contentExpression.materialize(dynamicContext);

            // 2: If the result of atomization is an empty sequence, the value of
            // the attribute is the zero-length string. Otherwise, each atomic value in
            // the atomized sequence is cast into a string.
            if (!atomizedContentItems.isEmpty()) {
                List<String> stringValues = new ArrayList<>();
                for (Item item : atomizedContentItems) {
                    stringValues.add(item.getStringValue());
                }

                // 3: The individual strings resulting from the previous step are
                // merged into a single string by concatenating them with a single space
                // character between each pair.
                contentExpressionBuilder.append(String.join(" ", stringValues));
            }
            // If empty sequence, contentExpressionBuilder remains empty (zero-length string)
        }

        String attributeValue = contentExpressionBuilder.toString();

        // 5: If the attribute name is xml:id, then xml:id processing is performed
        // Note: we currently do not support xml:id processing

        // 6: If the attribute name is xml:id, the is-id property of the resulting attribute node is set to true;
        // otherwise the is-id property is set to false. The is-idrefs property of the attribute node is unconditionally
        // set to false.
        // Note: we currently do not support is-id and is-idrefs properties

        // 7: If the attribute name is xml:space and the attribute value is other
        // than preserve or default, a dynamic error MAY be raised [err:XQDY0092].
        // Note: this is a MAY, so we are not mandated to raise an error here

        // Create and return the attribute item
        // 4: The parent property of the attribute node is set to empty.
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlAttributeNode(
                attributeName,
                attributeValue
            );
    }
}
