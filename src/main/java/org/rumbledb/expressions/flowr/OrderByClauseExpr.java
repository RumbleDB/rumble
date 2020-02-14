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
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public class OrderByClauseExpr extends FlworClause {
    private final Expression expression;
    private final boolean ascending;
    private final EMPTY_ORDER emptyOrder;
    private final String uri;

    public OrderByClauseExpr(
            Expression expression,
            boolean ascending,
            String uri,
            EMPTY_ORDER empty_order,
            ExceptionMetadata metadata
    ) {
        super(FLWOR_CLAUSES.ORDER_BY_EXPR, metadata);
        this.expression = expression;
        this.ascending = ascending;
        this.uri = uri;
        this.emptyOrder = empty_order;
    }

    @Override
    public void initHighestExecutionMode() {
        // OrderByClauseExpr's execution mode is not used. Leave it unset
        this.highestExecutionMode = ExecutionMode.UNSET;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public EMPTY_ORDER getEmptyOrder() {
        return this.emptyOrder;
    }

    public String getUri() {
        return this.uri;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitOrderByClauseExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.expression);
        return result;
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(orderByExpr " + this.expression.serializationString(false);
        // if(this.asSequence !=null)
        // result += " as " + asSequence.serializationString(true);
        // if(this.expression!=null)
        // result += " in " + this.expression.serializationString(true);
        // result += ")";
        return result;
    }

    public enum EMPTY_ORDER {
        NONE,
        FIRST,
        LAST
    }
}
