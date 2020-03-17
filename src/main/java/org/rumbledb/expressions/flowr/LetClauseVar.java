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
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;


public class LetClauseVar extends FlworVarDecl {

    public LetClauseVar(
            VariableReferenceExpression variableReferenceExpression,
            SequenceType sequenceType,
            Expression expression,
            ExceptionMetadata metadataFromContext
    ) {
        super(FLWOR_CLAUSES.LET_VAR, variableReferenceExpression, sequenceType, expression, metadataFromContext);
    }

    @Override
    public void initHighestExecutionAndVariableHighestStorageModes(VisitorConfig visitorConfig) {
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

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitLetClauseVar(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(letVar " + this.variableReferenceExpression.serializationString(false) + " ";
        if (this.sequenceType != null) {
            result += "as " + this.sequenceType.toString() + " ";
        }
        result += ":= " + this.expression.serializationString(true);
        result += "))";
        return result;
    }

}

