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

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.StringItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

public class AttributeNodeRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String qname;
    private List<RuntimeIterator> value;

    public AttributeNodeRuntimeIterator(
            String qname,
            List<RuntimeIterator> value,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.qname = qname;
        this.value = value;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        StringBuilder sb = new StringBuilder();
        // follow the spec as defined in https://www.w3.org/TR/xquery-31/#id-attributes

        // materialize the value of all the attribute components
        for (RuntimeIterator child : this.value) {
            List<Item> materialized = child.materialize(dynamicContext);
            if (materialized.size() == 1 && materialized.get(0) instanceof StringItem) {
                // component materialized to a string only. no need to atomize
                sb.append(((StringItem) materialized.get(0)).getStringValue());
            } else {
                // component materialized to a sequence. need to atomize and merge
                // 2. Each enclosed expression is converted to a string as follows:
                for (Item item : materialized) {
                        // 2.a apply atomization
                        List<Item> atomizedItem = item.atomizedValue();
                        // 2.b If the result of atomization is an empty sequence, the result is the zero-length string.
                        if (atomizedItem.isEmpty()) {
                            sb.append("");
                            // append the whitespace (2.c)
                            sb.append(" ");
                        } else {
                            // 2.b (cont.) Otherwise, each atomic value in the atomized sequence is cast into a string.
                            // 2.c The individual strings resulting from the previous step are merged into
                            // a single string by concatenating them with a single space character between each pair.
                            for (Item i : atomizedItem) {
                                sb.append(i.getStringValue());
                                sb.append(" ");
                            }
                            
                        }
                }
                // remove the last space
                sb.setLength(sb.length() - 1);
            }
        }

        // 3. Adjacent strings resulting from the above steps are concatenated with no intervening blanks.
        // The resulting string becomes the string-value property of the attribute node.
        // this is performed by using a StringBuilder

        // Create and return the attribute
        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlAttributeNode(
                this.qname,
                sb.toString()
            );
    }
}

