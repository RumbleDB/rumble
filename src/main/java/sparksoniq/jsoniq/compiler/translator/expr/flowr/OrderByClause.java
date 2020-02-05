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

package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import org.rumbledb.exceptions.ExceptionMetadata;;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.SemanticException;

public class OrderByClause extends FlworClause {


    private final List<OrderByClauseExpr> expressions;
    private final boolean isStable;

    public OrderByClause(List<OrderByClauseExpr> exprs, boolean stable, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.ORDER_BY, metadata);
        if (exprs == null || exprs.isEmpty())
            throw new SemanticException("Group clause must have at least one variable", metadata);
        this.expressions = exprs;
        this.isStable = stable;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        expressions.forEach(e -> {
            if (e != null)
                result.add(e);
        });
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitOrderByClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(orderByClause order by ";
        for (OrderByClauseExpr var : expressions)
            result += var.serializationString(true)
                + (expressions.indexOf(var) < expressions.size() - 1 ? " , " : "");
        result += ")";
        return result;
    }

    public boolean isStable() {
        return isStable;
    }

    public List<OrderByClauseExpr> getExpressions() {
        return expressions;
    }
}
