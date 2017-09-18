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
 package jiqs.semantics.visitor;

import jiqs.jsoniq.compiler.translator.expr.CommaExpression;
import jiqs.jsoniq.compiler.translator.expr.ExpressionOrClause;
import jiqs.jsoniq.compiler.translator.expr.control.IfExpression;
import jiqs.jsoniq.compiler.translator.expr.operational.InstanceOfExpression;
import jiqs.jsoniq.compiler.translator.expr.flowr.*;
import jiqs.jsoniq.compiler.translator.expr.operational.*;
import jiqs.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import jiqs.jsoniq.compiler.translator.expr.primary.*;
import jiqs.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import jiqs.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;

public abstract class AbstractExpressionOrClauseVisitor<T> {

    public T visit(ExpressionOrClause expression, T argument) {
        return expression.accept(this, argument);
    }

    public T visitDescendants(ExpressionOrClause expression, T argument) {
        T result = argument;
        for (ExpressionOrClause child : expression.getDescendants()) {
            result = visit(child, argument);
        }
        return result;
    }

    protected T defaultAction(ExpressionOrClause expression, T argument) {
        return visitDescendants(expression, argument);
    }

    public T visitFlowrExpression(FlworExpression expression, T argument) {
        return defaultAction(expression, argument);
    }

    public T visitVariableReference(VariableReference expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitForClause(ForClause expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitLetClause(LetClause expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitGroupByClause(GroupByClause expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitOrderByClause(OrderByClause expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitForClauseVar(ForClauseVar expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitLetClauseVar(LetClauseVar expression, T argument){
        return defaultAction(expression, argument);
    }

    public T visitGroupByClauseVar(GroupByClauseVar expression, T argument){ return defaultAction(expression, argument);}

    public T visitOrderByClauseExpr(OrderByClauseExpr expression, T argument){ return defaultAction(expression, argument);}

    public T visitWhereClause(WhereClause expression, T argument){ return defaultAction(expression, argument);}

    public T visitReturnClause(ReturnClause expression, T argument){ return defaultAction(expression, argument);}

    //region primary
    public T visitPostfixExpression(PostFixExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitArrayConstructor(ArrayConstructor expression, T argument){ return defaultAction(expression, argument);}

    public T visitObjectConstructor(ObjectConstructor expression, T argument){ return defaultAction(expression, argument);}

    public abstract T visitContextExpr(ContextExpression expression, T argument);
    //endregion

    //region literal
    public T visitInteger(IntegerLiteral expression, T argument){ return defaultAction(expression, argument);}

    public T visitString(StringLiteral expression, T argument){ return defaultAction(expression, argument);}

    public T visitDouble(DoubleLiteral expression, T argument){ return defaultAction(expression, argument);}

    public T visitDecimal(DecimalLiteral expression, T argument){ return defaultAction(expression, argument);}

    public T visitNull(NullLiteral expression, T argument){ return defaultAction(expression, argument);}

    public T visitBoolean(BooleanLiteral expression, T argument){ return defaultAction(expression, argument);}
    //endregion

    //region operational
    public T visitAdditiveExpr(AdditiveExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitMultiplicativeExpr(MultiplicativeExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitAndExpr(AndExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitOrExpr(OrExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitNotExpr(NotExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitUnaryExpr(UnaryExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitRangeExpr(RangeExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitStringConcatExpr(StringConcatExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitComparisonExpr(ComparisonExpression expression, T argument){ return defaultAction(expression, argument);}
    //endregion
    public T visitCommaExpression(CommaExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitParenthesizedExpression(ParenthesizedExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitFunctionCall(FunctionCall expression, T argument){ return defaultAction(expression, argument);}

    public T visitInstanceOfExpression(InstanceOfExpression expression, T argument){ return defaultAction(expression, argument);}


    public T visitIfExpression(IfExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitQuantifiedExpression(QuantifiedExpression expression, T argument){ return defaultAction(expression, argument);}

    public T visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, T argument){ return defaultAction(expression, argument);}

    }
