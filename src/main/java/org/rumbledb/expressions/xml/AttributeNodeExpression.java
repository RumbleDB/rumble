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
 * Expression representing an attribute node in a direct element constructor.
 * 
 * @see https://www.w3.org/TR/xquery-31/#id-attributes
 */
public class AttributeNodeExpression extends Expression {
    /**
     * The qname of the attribute node.
     */
    private String qname;
    /**
     * The value of the attribute node.
     * 
     * The value is a list of expressions. This is because an attribute node can be
     * constructed from multiple expressions and literals, which are materialized at runtime.
     */
    private List<Expression> value;

    public AttributeNodeExpression(String qname, List<Expression> value, ExceptionMetadata metadata) {
        super(metadata);
        this.qname = qname;
        this.value = value;
    }

    public String getQName() {
        return this.qname;
    }

    public List<Expression> getValue() {
        return this.value;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.value);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAttributeNode(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.qname);
        sb.append("=");
        for (Expression child : this.value) {
            child.serializeToJSONiq(sb, indent);
        }
    }
}
