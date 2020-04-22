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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.Collections;
import java.util.List;

public class ForClause extends Clause {

    private final String variableName;
    private final boolean allowEmpty;
    private final String positionalVariableName;
    protected SequenceType sequenceType;
    protected Expression expression;

    // Holds whether the for variable will be stored in materialized(local) or native/spark(RDD or DF) format in a tuple
    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;


    public ForClause(
            String variableName,
            boolean allowEmpty,
            SequenceType sequenceType,
            String positionalVariableName,
            Expression expression,
            ExceptionMetadata metadata
    ) {
        super(FLWOR_CLAUSES.FOR, metadata);
        if (variableName == null) {
            throw new SemanticException("For clause must have a variable", metadata);
        }
        this.variableName = variableName;
        this.allowEmpty = allowEmpty;
        this.positionalVariableName = positionalVariableName;
        // If the sequenceType is specified, we have to "extend" its arity to *
        // because TreatIterator is wrapping the whole assignment expression,
        // meaning there is not one TreatIterator for each variable we loop over.
        this.sequenceType = sequenceType;
        this.expression = expression;

    }

    public String getVariableName() {
        return this.variableName;
    }

    public boolean isAllowEmpty() {
        return this.allowEmpty;
    }

    public String getPositionalVariableName() {
        return this.positionalVariableName;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode =
            (this.expression.getHighestExecutionMode(visitorConfig).isRDD()
                || (this.previousClause != null
                    && this.previousClause.getHighestExecutionMode(visitorConfig).isDataFrame()))
                        ? ExecutionMode.DATAFRAME
                        : ExecutionMode.LOCAL;

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

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.expression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitForClause(this, argument);
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
                + this.sequenceType.toString()
                + ", "
                + (this.allowEmpty ? "allowing empty, " : "")
                + this.positionalVariableName
                + ") "
        );
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
        if (this.previousClause != null) {
            this.previousClause.print(buffer, indent + 1);
        }
    }
}
