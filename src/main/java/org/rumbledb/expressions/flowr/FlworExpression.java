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
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public class FlworExpression extends Expression {

    private Clause startClause;
    private List<Clause> contentClauses;
    private ReturnClause returnClause;

    public FlworExpression(
            Clause startClause,
            List<Clause> containingClauses,
            ReturnClause returnClause,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (
            startClause.getClauseType() != FLWOR_CLAUSES.FOR
                &&
                startClause.getClauseType() != FLWOR_CLAUSES.LET
        ) {
            throw new SemanticException("FLOWR clause must starts with a FOR or a LET\n", this.getMetadata());
        }

        setStartClause(startClause);
        setContentClauses(containingClauses);
        setReturnClause(returnClause);
    }

    public Clause getStartClause() {
        return this.startClause;
    }

    public List<Clause> getContentClauses() {
        return this.contentClauses;
    }

    private void setContentClauses(List<Clause> contentClauses) {
        this.contentClauses = new ArrayList<>();
        this.contentClauses.addAll(contentClauses);
    }

    public ReturnClause getReturnClause() {
        return this.returnClause;
    }

    private void setReturnClause(ReturnClause returnClause) {
        this.returnClause = returnClause;
    }

    @Override
    public void initHighestExecutionMode() {
        // overall flwor expression's execution mode is never used and remains unset
        this.highestExecutionMode = ExecutionMode.UNSET;
    }

    @Override
    public ExecutionMode getHighestExecutionMode(boolean ignoreUnsetError) {
        // overall flwor expression's execution mode is stored in the return clause
        return this.returnClause.getHighestExecutionMode(ignoreUnsetError);
    }

    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.startClause);
        if (this.contentClauses != null) {
            this.contentClauses.forEach(e -> {
                if (e != null) {
                    result.add(e);
                }
            });
        }
        result.add(this.returnClause);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFlowrExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(flowrExpr ";
        result += this.startClause.serializationString(true) + " ";
        for (Clause clause : this.contentClauses) {
            result += clause.serializationString(true) + " ";
        }
        result += this.returnClause.serializationString(true);
        result += "))";
        return result;
    }

    private void setStartClause(Clause startClause) {
        this.startClause = startClause;
    }


}


