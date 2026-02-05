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
import org.rumbledb.exceptions.InvalidProcessingInstructionContentException;
import org.rumbledb.exceptions.InvalidProcessingInstructionTargetException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runtime iterator for direct processing instruction constructors.
 *
 * @see org.rumbledb.expressions.xml.DirPIConstructorExpression
 */
public class DirPIConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final String target;
    private final AtomizationIterator contentIterator;

    public DirPIConstructorRuntimeIterator(
            String target,
            AtomizationIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            contentIterator != null ? Collections.singletonList(contentIterator) : Collections.emptyList(),
            staticContext
        );
        this.target = target;
        this.contentIterator = contentIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // "The resulting NCName is then used as the target property of the newly constructed processing instruction
        // node. However, a dynamic error is raised if the NCName is equal to "XML" (in any combination of upper and
        // lower case) [err:XQDY0064]."
        if (this.target != null && this.target.equalsIgnoreCase("xml")) {
            throw new InvalidProcessingInstructionTargetException(
                    "Processing instruction target must not be XML in any combination of upper and lower case.",
                    getMetadata()
            );
        }

        // "Atomization is applied to the value of the content expression, converting it to a sequence of atomic values.
        // (If the content expression is absent, the result of this step is an empty sequence.)"
        List<Item> materialized = this.contentIterator != null
            ? this.contentIterator.materialize(dynamicContext)
            : Collections.emptyList();

        // "If the result of atomization is an empty sequence, it is replaced by a zero-length string. Otherwise, each
        // atomic value in the atomized sequence is cast into a string. If any of the resulting strings contains the
        // string "?>", a dynamic error [err:XQDY0026] is raised."
        List<String> stringValues = new ArrayList<>();
        if (materialized.isEmpty()) {
            stringValues.add("");
        } else {
            for (Item item : materialized) {
                String value = item.getStringValue();
                if (value.contains("?>")) {
                    throw new InvalidProcessingInstructionContentException(
                            "Processing instruction content must not contain the string '?>'.",
                            getMetadata()
                    );
                }
                stringValues.add(value);
            }
        }

        // "The individual strings resulting from the previous step are merged into a single string by concatenating
        // them with a single space character between each pair. Leading whitespace is removed from the resulting
        // string. The resulting string then becomes the content property of the constructed processing instruction
        // node."
        String content = String.join(" ", stringValues);
        content = removeLeadingWhitespace(content);

        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlProcessingInstructionNode(
                this.target,
                content
            );
    }

    private String removeLeadingWhitespace(String value) {
        int index = 0;
        while (index < value.length() && Character.isWhitespace(value.charAt(index))) {
            index++;
        }
        return value.substring(index);
    }
}

