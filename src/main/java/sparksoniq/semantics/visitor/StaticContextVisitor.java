/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.semantics.visitor;

import sparksoniq.exceptions.UndeclaredVariableException;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.CountClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarDecl;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ContextExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import sparksoniq.semantics.StaticContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

public class StaticContextVisitor extends AbstractExpressionOrClauseVisitor<StaticContext> {

    @Override
    public StaticContext visit(ExpressionOrClause expression, StaticContext argument) {
        if (expression instanceof Expression)
            ((Expression) expression).setStaticContext(argument);
        return expression.accept(this, argument);
    }

    @Override
    public StaticContext visitVariableReference(VariableReference expression, StaticContext argument) {
        if (argument == null || !argument.isInScope(expression.getVariableName()))
            throw new UndeclaredVariableException("Uninitialized variable reference: " + expression.getVariableName(),
                    expression.getMetadata());
        else {
            expression.setType(argument.getVariableSequenceType(expression.getVariableName()));
            return defaultAction(expression, argument);
        }
    }

    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        StaticContext flowrContext;
        if (argument == null)
            flowrContext = new StaticContext();
        else
            flowrContext = argument;

        StaticContext result = this.visit(expression.getStartClause(), flowrContext);
        for (FlworClause clause : expression.get_contentClauses())
            result = this.visit(clause, result);

        result = this.visit(expression.get_returnClause(), result);
        return result;
    }

    //region FLOWR clauses
    @Override
    public StaticContext visitForClause(ForClause expression, StaticContext argument) {
        StaticContext result = argument;
        for (ForClauseVar var : expression.getForVariables())
            result = this.visit(var, result);
        return result;
    }

    @Override
    public StaticContext visitLetClause(LetClause expression, StaticContext argument) {
        StaticContext result = argument;
        for (LetClauseVar var : expression.getLetVariables())
            result = this.visit(var, result);
        return result;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause expression, StaticContext argument) {
        StaticContext result = argument;
        for (GroupByClauseVar var : expression.getGroupVariables())
            result = this.visit(var, result);
        return result;
    }
    //endregion

    //region FLOWR vars
    @Override
    public StaticContext visitForClauseVar(ForClauseVar expression, StaticContext argument) {
        StaticContext result = visitFlowrVarDeclaration(expression, argument);
        if (expression.getPositionalVariableReference() != null)
            result.addVariable(expression.getPositionalVariableReference().getVariableName(),
                    new SequenceType(new ItemType(ItemTypes.IntegerItem)), expression.getMetadata());
        //TODO visit at...
        this.visit(expression.getExpression(), argument);
        return result;
    }

    @Override
    public StaticContext visitLetClauseVar(LetClauseVar expression, StaticContext argument) {
        StaticContext result = visitFlowrVarDeclaration(expression, argument);
        this.visit(expression.getExpression(), argument);
        return result;
    }

    @Override
    public StaticContext visitGroupByClauseVar(GroupByClauseVar expression, StaticContext argument) {
        StaticContext result;
        if (expression.getExpression() == null) {
            this.visit(expression.getVariableReference(), argument);
            result = argument;
        } else {
            result = visitFlowrVarDeclaration(expression, argument);
            this.visit(expression.getExpression(), argument);
        }
        return result;
    }

    @Override
    public StaticContext visitCountClause(CountClause expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        result.addVariable(
                expression.getCountVariable().getVariableName(),
                new SequenceType(new ItemType(ItemTypes.IntegerItem), SequenceType.Arity.One),
                expression.getMetadata()
        );
        return result;
    }

    private StaticContext visitFlowrVarDeclaration(FlworVarDecl expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        //TODO for now we only suppot as/default, no inference, flags
        SequenceType type = expression.getAsSequence() == null ?
                new SequenceType() : expression.getAsSequence().getSequence();
        result.addVariable(expression.getVariableReference().getVariableName(), type, expression.getMetadata());
        return result;
    }
    //endregion

    @Override
    public StaticContext visitContextExpr(ContextExpression expression, StaticContext argument) {
        return defaultAction(expression, argument);
    }

    @Override
    public StaticContext visitQuantifiedExpression(QuantifiedExpression expression, StaticContext argument) {
        StaticContext context;
        if (argument == null)
            context = new StaticContext();
        else
            context = argument;
        for (QuantifiedExpressionVar clause : expression.getVariables())
            context = this.visit(clause, context);
        return context;
    }

    @Override
    public StaticContext visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        SequenceType type = expression.getSequenceType() == null ?
                new SequenceType() : expression.getSequenceType();
        result.addVariable(expression.getVariableReference().getVariableName(), type, expression.getMetadata());
        this.visit(expression.getExpression(), argument);
        return result;
    }
}
