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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.expressions.primary;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Document node constructors create document nodes according to the XQuery 3.1 specification.
 * All document node constructors are computed constructors. The result of a document node
 * constructor is a new document node, with its own node identity.
 * 
 * A document node constructor is useful when the result of a query is to be a document
 * in its own right.
 * 
 * Syntax: document { content }
 * 
 * The content expression of a document node constructor is processed in exactly the same way
 * as an enclosed expression in the content of a direct element constructor, as described in
 * Step 1e of 3.9.1.3 Content. The result of processing the content expression is a sequence
 * of nodes called the content sequence.
 */
public class DocumentNodeConstructorExpression extends Expression {
    /** The content expression */
    private final Expression contentExpression;

    /**
     * Constructor for document node constructor: document { content }
     * 
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public DocumentNodeConstructorExpression(
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.contentExpression = contentExpression;
    }

    /**
     * Get the content expression
     * 
     * @return The content expression
     */
    public Expression getContentExpression() {
        return this.contentExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDocumentNodeConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.contentExpression != null) {
            result.add(this.contentExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("document { ");
        if (this.contentExpression != null) {
            this.contentExpression.serializeToJSONiq(sb, 0);
        }
        sb.append(" }\n");
    }
}
