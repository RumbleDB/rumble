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

package sparksoniq.semantics.visitor;

import sparksoniq.exceptions.UndeclaredVariableException;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchCaseExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.CountClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarDecl;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionDeclaration;
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
        if (argument == null) {
            argument = new StaticContext();
        }
        if (expression instanceof Expression) {
            ((Expression) expression).setStaticContext(argument);
        }
        return expression.accept(this, argument);
    }

    // region primary
    @Override
    public StaticContext visitVariableReference(VariableReference expression, StaticContext argument) {
        String variableName = expression.getVariableName();
        if (!argument.isInScope(variableName)) {
            throw new UndeclaredVariableException(
                    "Uninitialized variable reference: " + variableName,
                    expression.getMetadata()
            );
        } else {
            expression.setType(argument.getVariableSequenceType(variableName));
            expression.setIsRDD(argument.getVariableIsRDD(variableName));
            expression.setIsDataFrame(argument.getVariableIsDataFrame(variableName));
            return defaultAction(expression, argument);
        }
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        expression.isRDD(); // initialize isRDD status // TODO: potentially improve initialization
        return super.visitFunctionDeclaration(expression, argument);
    }

    // endregion

    // region FLWOR
    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        StaticContext result = this.visit(expression.getStartClause(), argument);
        for (FlworClause clause : expression.get_contentClauses()) {
            result = this.visit(clause, result);
        }

        result = this.visit(expression.get_returnClause(), result);
        return result;
    }

    // region FLWOR vars
    @Override
    public StaticContext visitForClauseVar(ForClauseVar expression, StaticContext argument) {
        // TODO visit at...
        this.visit(expression.getExpression(), argument);

        StaticContext result = visitFlowrVarDeclaration(expression, argument);
        if (expression.getPositionalVariableReference() != null) {
            result.addVariable(
                expression.getPositionalVariableReference().getVariableName(),
                new SequenceType(new ItemType(ItemTypes.IntegerItem)),
                expression.getMetadata(),
                    false,
                    false
            );
        }
        return result;
    }

    @Override
    public StaticContext visitLetClauseVar(LetClauseVar expression, StaticContext argument) {
        this.visit(expression.getExpression(), argument);
        return visitFlowrVarDeclaration(expression, argument);
    }

    @Override
    public StaticContext visitGroupByClauseVar(GroupByClauseVar expression, StaticContext argument) {
        if (expression.getExpression() == null) {
            this.visit(expression.getVariableReference(), argument);
            return argument;
        }

        this.visit(expression.getExpression(), argument);
        return visitFlowrVarDeclaration(expression, argument);
    }

    @Override
    public StaticContext visitCountClause(CountClause expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        result.addVariable(
            expression.getCountVariable().getVariableName(),
            new SequenceType(new ItemType(ItemTypes.IntegerItem), SequenceType.Arity.One),
            expression.getMetadata(),
                false,
                false
        );
        return result;
    }

    private StaticContext visitFlowrVarDeclaration(FlworVarDecl expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        // TODO for now we only suppot as/default, no inference, flags
        SequenceType type = expression.getAsSequence() == null
            ? new SequenceType()
            : expression.getAsSequence().getSequence();
        result.addVariable(
                expression.getVariableReference().getVariableName(),
                type,
                expression.getMetadata(),
                expression.getVariableIsRDD(),
                expression.getVariableIsDataFrame()
        );
        return result;
    }
    // endregion
    // endregion

    // region quantifiers
    @Override
    public StaticContext visitQuantifiedExpression(QuantifiedExpression expression, StaticContext argument) {
        StaticContext context = argument;
        for (QuantifiedExpressionVar clause : expression.getVariables()) {
            context = this.visit(clause, context);
        }
        return context;
    }

    @Override
    public StaticContext visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        SequenceType type = expression.getSequenceType() == null ? new SequenceType() : expression.getSequenceType();
        result.addVariable(
                expression.getVariableReference().getVariableName(),
                type,
                expression.getMetadata(),
                false,
                false
        );
        this.visit(expression.getExpression(), argument);
        return result;
    }
    // endregion

    // region control
    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        this.visit(expression.getTestCondition(), argument);
        expression.getCases().forEach(typeSwitchCaseExpression -> this.visit(typeSwitchCaseExpression, argument));

        // add variable to context using the VariableReference and use that context to visit default return expression
        StaticContext defaultCaseStaticContext = argument;
        VariableReference defaultCaseVariableReference = expression.getVarRefDefault();
        if (defaultCaseVariableReference != null) {
            defaultCaseStaticContext = new StaticContext(argument);
            defaultCaseStaticContext.addVariable(
                defaultCaseVariableReference.getVariableName(),
                null,
                expression.getMetadata(),
                false,
                false
            );
        }
        this.visit(expression.getDefaultExpression(), defaultCaseStaticContext);

        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchCaseExpression(TypeSwitchCaseExpression expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        if (expression.getVariableReference() != null) {
            result.addVariable(
                    expression.getVariableReference().getVariableName(),
                    null,
                    expression.getMetadata(),
                    false,
                    false
            );
        }
        this.visit(expression.getReturnExpression(), result);
        return result;
    }
    // endregion

}
