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
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;


public class ForClauseVar extends FlworVarDecl {

    private final boolean allowEmpty;
    private final VariableReferenceExpression positionalVariableReferenceExpression;

    public ForClauseVar(
            VariableReferenceExpression variableReferenceExpression,
            SequenceType sequenceType,
            boolean emptyFlag,
            VariableReferenceExpression positionalVariableReferenceExpression,
            Expression expression,
            ExceptionMetadata metadataFromContext
    ) {
        super(FLWOR_CLAUSES.FOR_VAR, variableReferenceExpression, sequenceType, expression, metadataFromContext);
        this.allowEmpty = emptyFlag;
        this.positionalVariableReferenceExpression = positionalVariableReferenceExpression;

        // If the sequenceType is specified, we have to "extend" its arity to *
        // because TreatIterator is wrapping the whole assignment expression,
        // meaning there is not one TreatIterator for each variable we loop over.
        this.sequenceType = new SequenceType(
                sequenceType.getItemType(),
                SequenceType.Arity.ZeroOrMore
        );
    }

    public boolean allowsEmpty() {
        return this.allowEmpty;
    }

    public VariableReferenceExpression getPositionalVariableReference() {
        return this.positionalVariableReferenceExpression;
    }

    @Override
    public void initHighestExecutionAndVariableHighestStorageModes(VisitorConfig visitorConfig) {
        this.highestExecutionMode =
            (this.expression.getHighestExecutionMode(visitorConfig).isRDD()
                || (this.previousClause != null
                    && this.previousClause.getHighestExecutionMode(visitorConfig).isDataFrame()))
                        ? ExecutionMode.DATAFRAME
                        : ExecutionMode.LOCAL;

        this.variableHighestStorageMode = ExecutionMode.LOCAL;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();

        if (this.positionalVariableReferenceExpression != null) {
            result.add(this.positionalVariableReferenceExpression);
        }
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitForClauseVar(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(forVar " + this.variableReferenceExpression.serializationString(false) + " ";
        if (this.sequenceType != null) {
            result += "as " + this.sequenceType.toString() + " ";
        }
        if (this.allowEmpty) {
            result += "allowing empty ";
        }
        if (this.positionalVariableReferenceExpression != null) {
            result += "at " + this.positionalVariableReferenceExpression.serializationString(false) + " ";
        }
        result += "in " + this.expression.serializationString(true);
        result += "))";
        return result;
    }
}
