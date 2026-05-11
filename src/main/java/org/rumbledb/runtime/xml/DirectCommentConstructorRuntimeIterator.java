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

import java.util.Collections;

/**
 * Runtime iterator for direct comment node constructors.
 *
 * @see org.rumbledb.expressions.xml.DirectCommentConstructorExpression
 */
public class DirectCommentConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final String content;

    public DirectCommentConstructorRuntimeIterator(String content, RuntimeStaticContext staticContext) {
        super(Collections.emptyList(), staticContext);
        this.content = content;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // The DirCommentContents of a comment must not contain two consecutive hyphens
        // or end with a hyphen. These rules are syntactically enforced by the grammar.
        this.hasNext = false;
        return ItemFactory.getInstance().createXmlCommentNode(this.content);
    }
}

