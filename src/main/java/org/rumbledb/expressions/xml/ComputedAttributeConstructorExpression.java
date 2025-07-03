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

package org.rumbledb.expressions.xml;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class ComputedAttributeConstructorExpression extends Expression {
    /** The static attribute name (if specified) */
    private final Name attributeName;
    /** The dynamic attribute name expression (if specified) */
    private final Expression nameExpression;
    /** The value expression */
    private final Expression valueExpression;

    /**
     * Constructor for static attribute name: attribute attributeName { value }
     * 
     * @param attributeName The static attribute name
     * @param valueExpression The value expression
     * @param metadata The exception metadata
     */
    public ComputedAttributeConstructorExpression(
            Name attributeName,
            Expression valueExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.attributeName = attributeName;
        this.nameExpression = null;
        this.valueExpression = valueExpression;
    }

    /**
     * Constructor for dynamic attribute name: attribute { nameExpression } { value }
     * 
     * @param nameExpression The dynamic attribute name expression
     * @param valueExpression The value expression
     * @param metadata The exception metadata
     */
    public ComputedAttributeConstructorExpression(
            Expression nameExpression,
            Expression valueExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.attributeName = null;
        this.nameExpression = nameExpression;
        this.valueExpression = valueExpression;
    }

    /**
     * Get the static attribute name
     * 
     * @return The static attribute name
     */
    public Name getAttributeName() {
        return this.attributeName;
    }

    /**
     * Get the dynamic attribute name expression
     * 
     * @return The dynamic attribute name expression
     */
    public Expression getNameExpression() {
        return this.nameExpression;
    }

    /**
     * Get the value expression
     * 
     * @return The value expression
     */
    public Expression getValueExpression() {
        return this.valueExpression;
    }

    /**
     * Check if the attribute has a static name
     * 
     * @return True if the attribute has a static name, false otherwise
     */
    public boolean hasStaticName() {
        return this.attributeName != null;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitComputedAttributeConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.nameExpression != null) {
            result.add(this.nameExpression);
        }
        if (this.valueExpression != null) {
            result.add(this.valueExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("attribute ");
        if (this.hasStaticName()) {
            sb.append(this.attributeName.toString());
        } else {
            sb.append("{ ");
            this.nameExpression.serializeToJSONiq(sb, 0);
            sb.append(" }");
        }
        sb.append(" { ");
        if (this.valueExpression != null) {
            this.valueExpression.serializeToJSONiq(sb, 0);
        }
        sb.append(" }\n");
    }
}
