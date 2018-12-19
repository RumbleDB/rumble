/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class OrderByClauseExpr extends FlworClause {
    public Expression getExpression() {
        return _expression;
    }

    public boolean isAscending() {
        return _ascending;
    }

    public EMPTY_ORDER getEmptyOrder() {
        return _emptyOrder;
    }

    public String getUri() {
        return _uri;
    }

    public OrderByClauseExpr(Expression expression, boolean ascending,
                             String uri, EMPTY_ORDER empty_order, ExpressionMetadata metadata) {
        super(FLWOR_CLAUSES.ORDER_BY_EXPR, metadata);
        this._expression = expression;
        this._ascending = ascending;
        this._uri = uri;
        this._emptyOrder = empty_order;
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitOrderByClauseExpr(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(orderByExpr " + _expression.serializationString(false);
//        if(this.asSequence !=null)
//            result += " as " + asSequence.serializationString(true);
//        if(this.expression!=null)
//            result += " in " + this.expression.serializationString(true);
//        result += ")";
        return result;
    }

    public enum EMPTY_ORDER {
        NONE,
        FIRST,
        LAST
    }

    private final Expression _expression;
    private final boolean _ascending;
    private final EMPTY_ORDER _emptyOrder;
    private final String _uri;
}
