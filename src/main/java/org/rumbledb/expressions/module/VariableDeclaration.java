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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.Collections;
import java.util.List;

public class VariableDeclaration extends Node {

    private final String variableName;
    private final boolean external;
    protected SequenceType sequenceType;
    protected Expression expression;

    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;

    public VariableDeclaration(
            String variableName,
            boolean external,
            SequenceType sequenceType,
            Expression expression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.variableName = variableName;
        this.external = external;
        this.sequenceType = sequenceType;
        this.expression = expression;
        if (!this.external && this.expression == null) {
            throw new OurBadException("If a variable is not external, an expression must be provided.");
        }
        if (!this.external) {
            throw new OurBadException(
                    "Non-external variables are not supported at the moment. Please contact us if you need this feature."
            );
        }
    }

    public String getVariableName() {
        return this.variableName;
    }

    public boolean external() {
        return this.external;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public List<Node> getChildren() {
        if (this.expression != null) {
            return Collections.singletonList(this.expression);
        }
        return Collections.emptyList();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitVariableDeclaration(this, argument);
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = ExecutionMode.LOCAL;
        this.variableHighestStorageMode = ExecutionMode.LOCAL;
    }

    public ExecutionMode getVariableHighestStorageMode(VisitorConfig visitorConfig) {
        if (
            !visitorConfig.suppressErrorsForAccessingUnsetExecutionModes()
                && this.variableHighestStorageMode == ExecutionMode.UNSET
        ) {
            throw new OurBadException("A variable storage mode is accessed without being set.");
        }
        return this.variableHighestStorageMode;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(
            " ("
                + (this.variableName)
                + ", "
                + (this.external ? "external, " : "")
                + this.sequenceType.toString()
                + ") "
        );
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }
}

