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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

/**
 * An expression representing a direct comment constructor.
 */
public class DirectCommentConstructorExpression extends Expression {

    /** The literal content of the comment. */
    private final String content;

    /**
     * Constructor for a direct comment constructor.
     *
     * @param content The literal content of the comment
     * @param metadata The exception metadata
     */
    public DirectCommentConstructorExpression(String content, ExceptionMetadata metadata) {
        super(metadata);
        this.content = content;
    }

    /**
     * Get the literal content of the comment.
     *
     * @return The comment content
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
        return visitor.visitDirectCommentConstructor(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("<!--");
        sb.append(this.content);
        sb.append("-->");
    }
}

