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
 * Expression representing a computed namespace constructor.
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-computed-namespaces">XQuery 3.1, 3.9.3.7: Computed Namespace
 *      Constructors</a>
 */
public class ComputedNamespaceConstructorExpression extends Expression {
    /** The static prefix (if specified) */
    private final String prefix;
    /** The dynamic prefix expression (if specified) */
    private final Expression prefixExpression;
    /** The URI expression */
    private final Expression uriExpression;

    /**
     * Constructor for static prefix: namespace prefix { uri }
     *
     * @param prefix The static namespace prefix
     * @param uriExpression The URI expression
     * @param metadata The exception metadata
     */
    public ComputedNamespaceConstructorExpression(
            String prefix,
            Expression uriExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.prefix = prefix;
        this.prefixExpression = null;
        this.uriExpression = uriExpression;
    }

    /**
     * Constructor for dynamic prefix: namespace { prefixExpression } { uri }
     *
     * @param prefixExpression The dynamic prefix expression
     * @param uriExpression The URI expression
     * @param metadata The exception metadata
     */
    public ComputedNamespaceConstructorExpression(
            Expression prefixExpression,
            Expression uriExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.prefix = null;
        this.prefixExpression = prefixExpression;
        this.uriExpression = uriExpression;
    }

    public boolean hasStaticPrefix() {
        return this.prefix != null;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Expression getPrefixExpression() {
        return this.prefixExpression;
    }

    public Expression getUriExpression() {
        return this.uriExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitComputedNamespaceConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("namespace ");
        if (this.hasStaticPrefix()) {
            sb.append(this.prefix);
        } else {
            sb.append("{ ");
            this.prefixExpression.serializeToJSONiq(sb, 0);
            sb.append(" }");
        }
        sb.append(" { ");
        this.uriExpression.serializeToJSONiq(sb, 0);
        sb.append(" }\n");
    }
}

