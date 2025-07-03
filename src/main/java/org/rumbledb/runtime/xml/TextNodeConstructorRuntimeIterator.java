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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;
import java.util.Arrays;
import java.util.List;

public class TextNodeConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final AtomizationIterator contentIterator;

    /**
     * Constructor for text node constructor runtime iterator
     * 
     * @param contentIterator Iterator for the content expression
     * @param staticContext The static context
     */
    public TextNodeConstructorRuntimeIterator(
            AtomizationIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(contentIterator), staticContext);
        this.contentIterator = contentIterator;
    }

    private static List<RuntimeIterator> createChildList(RuntimeIterator contentIterator) {
        return Arrays.asList(contentIterator);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // Process content
        StringBuilder textContent = new StringBuilder();
        // processing of the content expression according to the spec
        // see https://www.w3.org/TR/xquery-31/#id-textConstructors

        // 1. Atomization is applied to the value of the content expression,
        // converting it to a sequence of atomic values.
        List<Item> materialized = this.contentIterator.materialize(dynamicContext);
        // 2. If the result of atomization is an empty sequence, no text node is constructed.
        if (materialized.isEmpty()) {
            return null;
        }
        // 2. (cont.) Otherwise, each atomic value in the atomized sequence is cast into a string.
        for (Item item : materialized) {
            // 3.The individual strings resulting from the previous step are merged into a single string
            // by concatenating them with a single space character between each pair.
            // The resulting string becomes the content property of the constructed text node.
            textContent.append(item.getStringValue());
            textContent.append(" ");
        }
        // remove the last space
        textContent.deleteCharAt(textContent.length() - 1);

        // Create and return the text node item
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlTextNode(
                textContent.toString()
            );
    }
}
