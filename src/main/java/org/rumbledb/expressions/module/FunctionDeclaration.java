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

package org.rumbledb.expressions.module;


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.InlineFunctionExpression;

import java.util.Collections;
import java.util.List;

public class FunctionDeclaration extends Node {

    private final InlineFunctionExpression functionExpression;

    public FunctionDeclaration(
            InlineFunctionExpression functionExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.functionExpression = functionExpression;
    }

    public Expression getExpression() {
        return this.functionExpression;
    }

    public FunctionIdentifier getFunctionIdentifier() {
        return this.functionExpression.getFunctionIdentifier();
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.functionExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFunctionDeclaration(this, argument);
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = this.functionExpression.getBody().getHighestExecutionMode(visitorConfig);
    }

    /**
     * Prints the node tree to a string buffer.
     *
     * @param buffer a string buffer to write to
     * @param indent the current level of indentation
     */
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append("FunctionDeclaration " + this.getFunctionIdentifier());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.functionExpression.serializeToJSONiq(sb, 0);
    }
}

