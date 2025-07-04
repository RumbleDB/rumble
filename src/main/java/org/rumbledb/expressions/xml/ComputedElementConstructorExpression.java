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

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression representing a computed element constructor.
 * 
 * @see https://www.w3.org/TR/xquery-31/#id-computedElements
 */
public class ComputedElementConstructorExpression extends Expression {
    /** The static element name (if specified) */
    private final Name elementName;
    /** The dynamic element name expression (if specified) */
    private final Expression nameExpression;
    /** The content expression */
    private final Expression contentExpression;

    /**
     * Constructor for static element name: element elementName { content }
     * 
     * @param elementName The static element name
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public ComputedElementConstructorExpression(
            Name elementName,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.elementName = elementName;
        this.nameExpression = null;
        this.contentExpression = contentExpression;
    }

    /**
     * Constructor for dynamic element name: element { nameExpression } { content }
     * 
     * @param nameExpression The dynamic element name expression
     * @param contentExpression The content expression
     * @param metadata The exception metadata
     */
    public ComputedElementConstructorExpression(
            Expression nameExpression,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.elementName = null;
        this.nameExpression = nameExpression;
        this.contentExpression = contentExpression;
    }

    /**
     * Get the static element name
     * 
     * @return The static element name
     */
    public Name getElementName() {
        return this.elementName;
    }

    /**
     * Get the dynamic element name expression
     * 
     * @return The dynamic element name expression
     */
    public Expression getNameExpression() {
        return this.nameExpression;
    }

    /**
     * Get the content expression
     * 
     * @return The content expression
     */
    public Expression getContentExpression() {
        return this.contentExpression;
    }

    /**
     * Check if the element has a static name
     * 
     * @return True if the element has a static name, false otherwise
     */
    public boolean hasStaticName() {
        return this.elementName != null;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitComputedElementConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.nameExpression != null) {
            result.add(this.nameExpression);
        }
        if (this.contentExpression != null) {
            result.add(this.contentExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("element ");
        if (this.hasStaticName()) {
            sb.append(this.elementName.toString());
        } else {
            sb.append("{ ");
            this.nameExpression.serializeToJSONiq(sb, 0);
            sb.append(" }");
        }
        sb.append(" { ");
        if (this.contentExpression != null) {
            this.contentExpression.serializeToJSONiq(sb, 0);
        }
        sb.append(" }\n");
    }
}
