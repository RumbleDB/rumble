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

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * An expression representing a text node, as part of a direct element constructor content.
 * 
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-element-constructor">XQuery 3.1, 3.9.1: Direct Element
 *      Constructors</a>
 */
public class TextNodeExpression extends Expression {

    /** The content of the text node */
    private String content;

    /**
     * Constructor for a text node.
     * 
     * @param content The content of the text node
     * @param metadata The exception metadata
     */
    public TextNodeExpression(String content, ExceptionMetadata metadata) {
        super(metadata);
        this.content = content;
    }

    /**
     * Get the content of the text node
     * 
     * @return The content of the text node
     */
    public String getContent() {
        return this.content;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTextNode(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.content);
    }
}
