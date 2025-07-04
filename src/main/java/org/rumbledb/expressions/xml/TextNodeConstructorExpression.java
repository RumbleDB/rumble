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
 * An expression representing a text node constructor.
 * 
 * All text node constructors are computed constructors. The result of a text node constructor is a new text node, with its own node identity.
 * 
 * Syntax: text { content }
 * 
 * @see https://www.w3.org/TR/xquery-31/#id-textConstructors
 */
public class TextNodeConstructorExpression extends Expression {
    /** The content expression */
    private final Expression contentExpression;

    /**
     * Constructor for text node constructor.
     * 
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public TextNodeConstructorExpression(
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
        return visitor.visitTextNodeConstructor(this, argument);
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
        sb.append("text { ");
        if (this.contentExpression != null) {
            this.contentExpression.serializeToJSONiq(sb, 0);
        }
        sb.append(" }\n");
    }
}
