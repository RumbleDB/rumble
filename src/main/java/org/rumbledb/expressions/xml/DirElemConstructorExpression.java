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

public class DirElemConstructorExpression extends Expression {
    private final String tagName;
    private final List<Expression> content;
    private final List<Expression> attributes;

    public DirElemConstructorExpression(
            String tagName,
            List<Expression> content,
            List<Expression> attributes,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.tagName = tagName;
        this.content = content;
        this.attributes = attributes;
    }

    public String getTagName() {
        return this.tagName;
    }

    public List<Expression> getContent() {
        return this.content;
    }

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
        sb.append("<" + this.tagName);
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
        sb.append("</" + this.tagName + ">\n");
    }

}
