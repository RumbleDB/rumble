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

import java.util.Arrays;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

/**
 * Runtime iterator for attribute nodes in a direct element constructor.
 * 
 * @see AttributeNodeExpression
 */
public class AttributeNodeRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String qname;
    private List<AtomizationIterator> atomizedValues;

    public AttributeNodeRuntimeIterator(
            String qname,
            List<AtomizationIterator> atomizedValues,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(atomizedValues), staticContext);
        this.qname = qname;
        this.atomizedValues = atomizedValues;
    }

    private static List<RuntimeIterator> createChildList(List<AtomizationIterator> atomizedValues) {
        return Arrays.asList(atomizedValues.toArray(new RuntimeIterator[0]));
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        StringBuilder sb = new StringBuilder();
        // follow the spec as defined in https://www.w3.org/TR/xquery-31/#id-attributes
        // 2. Each enclosed expression is converted to a string as follows:
        for (AtomizationIterator atomizedValueIterator : this.atomizedValues) {

            // 2.a Atomization is applied to each enclosed expression, converting it to a sequence of atomic values.
            List<Item> atomizedItems = atomizedValueIterator.materialize(dynamicContext);


            // 2.b If the result of atomization is an empty sequence, the result is the zero-length string.
            if (atomizedItems.isEmpty()) {
                // Empty atomization result contributes nothing to the final string
                sb.append("");
            } else {
                // 2.b (cont.) Otherwise, each atomic value in the atomized sequence is cast into a string.
                // 2.c The individual strings resulting from the previous step are merged into
                // a single string by concatenating them with a single space character between each pair.
                for (int i = 0; i < atomizedItems.size(); i++) {
                    Item atomicItem = atomizedItems.get(i);
                    sb.append(atomicItem.getStringValue());
                    if (i < atomizedItems.size() - 1) {
                        sb.append(" ");
                    }
                }
            }
        }

        // 3. Adjacent strings resulting from the above steps are concatenated with no intervening blanks.
        // The resulting string becomes the string-value property of the attribute node.
        // this is performed by using the same StringBuilder for all the attribute components

        // Create and return the attribute
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlAttributeNode(
                this.qname,
                sb.toString()
            );
    }
}

