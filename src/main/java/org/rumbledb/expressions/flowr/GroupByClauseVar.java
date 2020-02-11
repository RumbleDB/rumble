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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

public class GroupByClauseVar extends FlworVarDecl {

    private final String uri;


    public GroupByClauseVar(
            VariableReferenceExpression varRef,
            FlworVarSequenceType sequence,
            Expression expr,
            String uri,
            ExceptionMetadata metadata
    ) {
        super(FLWOR_CLAUSES.GROUP_VAR, varRef, sequence, expr, metadata);
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    @Override
    public void initHighestExecutionAndVariableHighestStorageModes() {
        // Execution mode of groupByClause's expressions are not used while defining execution mode of
        // GroupByClauseRuntimeIterator
        this.highestExecutionMode = ExecutionMode.UNSET;

        this._variableHighestStorageMode = ExecutionMode.LOCAL;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitGroupByClauseVar(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(groupByVar " + this.variableReferenceNode.serializationString(false);
        if (this.asSequence != null)
            result += " as " + this.asSequence.serializationString(true);
        if (this.expression != null)
            result += " in " + this.expression.serializationString(true);
        result += ")";
        return result;
    }
}
