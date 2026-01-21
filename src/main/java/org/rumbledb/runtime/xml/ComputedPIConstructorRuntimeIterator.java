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
import org.rumbledb.exceptions.InvalidProcessingInstructionTargetCastException;
import org.rumbledb.exceptions.InvalidProcessingInstructionTargetException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Runtime iterator for computed processing instruction constructors.
 *
 * @see org.rumbledb.expressions.xml.ComputedPIConstructorExpression
 */
public class ComputedPIConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");
    private final String staticTarget;
    private final AtomizationIterator nameIterator;
    private final AtomizationIterator contentIterator;

    public ComputedPIConstructorRuntimeIterator(
            String staticTarget,
            AtomizationIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            contentIterator != null ? Collections.singletonList(contentIterator) : Collections.emptyList(),
            staticContext
        );
        this.staticTarget = staticTarget;
        this.nameIterator = null;
        this.contentIterator = contentIterator;
    }

    public ComputedPIConstructorRuntimeIterator(
            AtomizationIterator nameIterator,
            AtomizationIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(nameIterator, contentIterator), staticContext);
        this.staticTarget = null;
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
        String target;
        if (this.staticTarget != null) {
            target = this.staticTarget;
        } else {
            List<Item> atomizedNameItems = this.nameIterator.materialize(dynamicContext);
            if (atomizedNameItems.size() != 1) {
                throw new UnexpectedStaticTypeException(
                        "Computed processing instruction constructor name must evaluate to a single atomic value of type xs:NCName, xs:string, or xs:untypedAtomic"
                );
            }
            Item atomizedNameItem = atomizedNameItems.get(0);
            if (!atomizedNameItem.isAtomic()) {
                throw new UnexpectedStaticTypeException(
                        "Computed processing instruction constructor name must evaluate to a single atomic value of type xs:NCName, xs:string, or xs:untypedAtomic"
                );
            }
            String nameString = atomizedNameItem.getStringValue();
            if (!isValidNCName(nameString)) {
                throw new InvalidProcessingInstructionTargetCastException(
                        "Processing instruction target cannot be cast to xs:NCName.",
                        getMetadata()
                );
            }
            target = nameString;
        }

        if (target != null && target.equalsIgnoreCase("xml")) {
            throw new InvalidProcessingInstructionTargetException(
                    "Processing instruction target must not be XML in any combination of upper and lower case.",
                    getMetadata()
            );
        }

        List<Item> materialized = this.contentIterator != null
            ? this.contentIterator.materialize(dynamicContext)
            : Collections.emptyList();

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

        String content = String.join(" ", stringValues);
        content = removeLeadingWhitespace(content);

        this.hasNext = false;
        return ItemFactory.getInstance()
            .createXmlProcessingInstructionNode(
                target,
                content
            );
    }

    private boolean isValidNCName(String value) {
        return value != null && NCNAME_PATTERN.matcher(value).matches();
    }

    private String removeLeadingWhitespace(String value) {
        int index = 0;
        while (index < value.length() && Character.isWhitespace(value.charAt(index))) {
            index++;
        }
        return value.substring(index);
    }
}

