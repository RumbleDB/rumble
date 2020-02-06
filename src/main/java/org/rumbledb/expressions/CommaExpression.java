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

package org.rumbledb.expressions;


import sparksoniq.jsoniq.ExecutionMode;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

public class CommaExpression extends Expression {

    private final List<Expression> _expressions;

    public CommaExpression(List<Expression> expressions, ExceptionMetadata metadata) {
        super(metadata);
        this._expressions = expressions;
    }

    public List<Expression> getExpressions() {
        return _expressions;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        if (this._expressions != null)
            _expressions.forEach(e -> {
                if (e != null)
                    result.add(e);
            });
        return getDescendantsFromChildren(result, depthSearch);
    }

    private boolean bypassCurrentExpressionForExecutionModeOperations() {
        return _expressions.size() == 1;
    }

    @Override
    public void initHighestExecutionMode() {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return;
        }
        super.initHighestExecutionMode();
    }

    @Override
    public ExecutionMode getHighestExecutionMode(boolean ignoreUnsetError) {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return this._expressions.get(0).getHighestExecutionMode(ignoreUnsetError);
        }
        return super.getHighestExecutionMode(ignoreUnsetError);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitCommaExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(expr ";
        for (Expression expr : _expressions) {

            result += "(exprSingle "
                + expr.serializationString(false)
                + (_expressions.size() >= 2
                    && _expressions.indexOf(expr) < _expressions.size() - 1 ? ") , " : ")");
        }

        result += ")";
        return result;
    }


}

