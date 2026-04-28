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
import org.rumbledb.exceptions.InvalidCommentContentException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * Runtime iterator for computed comment node constructors.
 *
 * @see org.rumbledb.expressions.xml.CommentNodeConstructorExpression
 */
public class CommentNodeConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    /** The content iterator */
    private final AtomizationIterator contentIterator;

    public CommentNodeConstructorRuntimeIterator(
            AtomizationIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(contentIterator), staticContext);
        this.contentIterator = contentIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        List<Item> materialized = this.contentIterator.materialize(dynamicContext);
        if (materialized.isEmpty()) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(" ");
        for (Item item : materialized) {
            joiner.add(item.getStringValue());
        }
        String commentContent = joiner.toString();

        // Spec guard: "If the resulting string contains two adjacent hyphens or ends with a hyphen,
        // a dynamic error is raised [err:XQDY0072]." (XQuery 3.1 §3.9.3.5)
        if (commentContent.contains("--") || commentContent.endsWith("-")) {
            throw new InvalidCommentContentException(commentContent, getMetadata());
        }

        this.hasNext = false;
        return ItemFactory.getInstance().createXmlCommentNode(commentContent);
    }
}

