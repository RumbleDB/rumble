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

package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * An expression representing a document node constructor.
 * 
 * All document node constructors are computed constructors.
 * 
 * The result of a document node constructor is a new document node, with its own node identity.
 * 
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-documentConstructors">XQuery 3.1, 3.9.3.3: Document Node
 *      Constructors</a>
 */
public class DocumentNodeConstructorExpression extends Expression {
    /** The content expression */
    private final Expression contentExpression;

    /**
     * Constructor for document node constructor.
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
