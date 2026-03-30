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
 * Expression representing a direct element constructor.
 * 
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-element-constructor">XQuery 3.1, 3.9.1: Direct Element
 *      Constructors</a>
 */
public class DirElemConstructorExpression extends Expression {
    /** Resolved expanded name of the element (compile-time). */
    private final Name elementName;
    /** The content of the element */
    private final List<Expression> content;
    /** The attributes of the element */
    private final List<Expression> attributes;

    /**
     * Constructor for a direct element constructor.
     * 
     * @param elementName Resolved expanded name of the element
     * @param content The content of the element
     * @param attributes The attributes of the element
     * @param metadata The exception metadata
     */
    public DirElemConstructorExpression(
            Name elementName,
            List<Expression> content,
            List<Expression> attributes,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.elementName = elementName;
        this.content = content;
        this.attributes = attributes;
    }

    /**
     * Resolved expanded name of the element.
     */
    public Name getNodeName() {
        return this.elementName;
    }

    /**
     * Get the content of the element
     * 
     * @return The content of the element
     */
    public List<Expression> getContent() {
        return this.content;
    }

    /**
     * Get the attributes of the element
     * 
     * @return The attributes of the element
     */
    public List<Expression> getAttributes() {
        return this.attributes;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDirElemConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.content != null) {
            result.addAll(this.content);
        }
        // in the XML data model, attributes are considered children
        if (this.attributes != null) {
            result.addAll(this.attributes);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("<" + this.elementName);
        if (this.attributes != null && !this.attributes.isEmpty()) {
            for (Expression attr : this.attributes) {
                attr.serializeToJSONiq(sb, indent);
                sb.append(" ");
            }
        }
        sb.append(">");
        if (this.content != null && !this.content.isEmpty()) {
            sb.append("\n");
            for (Expression expr : this.content) {
                expr.serializeToJSONiq(sb, indent + 1);
            }
            indentIt(sb, indent);
        }
        sb.append("</" + this.elementName + ">\n");
    }

}
