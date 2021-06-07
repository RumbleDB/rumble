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

package org.rumbledb.expressions.flowr;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import java.util.Collections;
import java.util.List;

public class LetClause extends Clause {

    private static final long serialVersionUID = 1L;
    private final Name variableName;
    protected SequenceType sequenceType;
    protected Expression expression;

    // Holds whether the let variable will be stored in materialized(local) or native/spark(RDD or DF) format in a tuple
    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;

    public LetClause(
            Name variableName,
            SequenceType sequenceType,
            Expression expression,
            ExceptionMetadata metadataFromContext
    ) {
        super(FLWOR_CLAUSES.LET, metadataFromContext);
        if (variableName == null) {
            throw new SemanticException("Let clause must have at least one variable", metadataFromContext);
        }
        this.variableName = variableName;
        this.sequenceType = sequenceType;
        this.expression = expression;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType == null ? SequenceType.MOST_GENERAL_SEQUENCE_TYPE : this.sequenceType;
    }

    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode =
            (this.previousClause == null)
                ? ExecutionMode.LOCAL
                : this.previousClause.getHighestExecutionMode(visitorConfig);

        // if let clause is local, defined variables are stored according to the execution mode of the expression
        if (this.highestExecutionMode == ExecutionMode.LOCAL) {
            this.variableHighestStorageMode = this.expression.getHighestExecutionMode(visitorConfig);
        } else {
            this.variableHighestStorageMode = ExecutionMode.LOCAL;
        }
    }

    public ExecutionMode getVariableHighestStorageMode(VisitorConfig visitorConfig) {
        if (
            !visitorConfig.suppressErrorsForAccessingUnsetExecutionModes()
                && this.variableHighestStorageMode == ExecutionMode.UNSET
        ) {
            throw new OurBadException("An variable storage mode is accessed without being set.");
        }
        return this.variableHighestStorageMode;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.expression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitLetClause(this, argument);
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (" + (this.variableName) + ", " + this.getSequenceType().toString() + ") ");
        buffer.append(")");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
        if (this.previousClause != null) {
            this.previousClause.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("let $" + this.variableName.toString());
        if (this.sequenceType != null)
            sb.append(" as " + this.sequenceType.toString());
        sb.append(" := (");
        this.expression.serializeToJSONiq(sb, 0);
        sb.append(")\n");
    }
}
