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
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class FlworExpression extends Expression {

    private FlworClause _startClause;
    private List<FlworClause> _contentClauses;
    private ReturnClause _returnClause;

    public FlworExpression(
            FlworClause startClause,
            List<FlworClause> containingClauses,
            ReturnClause returnClause,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (
            startClause.getClauseType() != FLWOR_CLAUSES.FOR
                &&
                startClause.getClauseType() != FLWOR_CLAUSES.LET
        )
            throw new SemanticException("FLOWR clause must starts with a FOR or a LET\n", this.getMetadata());

        set_startClause(startClause);
        set_contentClauses(containingClauses);
        set_returnClause(returnClause);
    }

    public FlworClause getStartClause() {
        return this._startClause;
    }

    public List<FlworClause> get_contentClauses() {
        return this._contentClauses;
    }

    private void set_contentClauses(List<FlworClause> contentClauses) {
        this._contentClauses = new ArrayList<>();
        this._contentClauses.addAll(contentClauses);
    }

    public ReturnClause get_returnClause() {
        return this._returnClause;
    }

    private void set_returnClause(ReturnClause returnClause) {
        this._returnClause = returnClause;
    }

    @Override
    public void initHighestExecutionMode() {
        // overall flwor expression's execution mode is never used and remains unset
        this.highestExecutionMode = ExecutionMode.UNSET;
    }

    @Override
    public ExecutionMode getHighestExecutionMode(boolean ignoreUnsetError) {
        // overall flwor expression's execution mode is stored in the return clause
        return this._returnClause.getHighestExecutionMode(ignoreUnsetError);
    }

    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this._startClause);
        if (this._contentClauses != null)
            this._contentClauses.forEach(e -> {
                if (e != null)
                    result.add(e);
            });
<<<<<<< HEAD
        result.add(this._returnClause);
        return getDescendantsFromChildren(result, depthSearch);
=======
        result.add(_returnClause);
        return result;
>>>>>>> c94fc8ddae10d0d8652a240536e13bdcdb7fce0d
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFlowrExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(flowrExpr ";
        result += this._startClause.serializationString(true) + " ";
        for (FlworClause clause : this._contentClauses)
            result += clause.serializationString(true) + " ";
        result += this._returnClause.serializationString(true);
        result += "))";
        return result;
    }

    private void set_startClause(FlworClause startClause) {
        this._startClause = startClause;
    }


}


