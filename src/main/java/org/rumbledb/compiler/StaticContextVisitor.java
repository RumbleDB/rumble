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

package org.rumbledb.compiler;

import org.rumbledb.exceptions.UndeclaredVariableException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.FlworVarDecl;
import org.rumbledb.expressions.flowr.ForClauseVar;
import org.rumbledb.expressions.flowr.GroupByClauseVar;
import org.rumbledb.expressions.flowr.LetClauseVar;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.StaticContext;

/**
 * Static context visitor implements a multi-pass algorithm that enables function hoisting
 */
public class StaticContextVisitor extends AbstractNodeVisitor<StaticContext> {

    // indicate whether an error should be thrown if an duplicate user defined function declaration is detected
    private boolean ignoreDuplicateUserDefinedFunctionError;

    public StaticContextVisitor() {
        this.setConfigForInitialPass();
    }

    /**
     * The initial pass should collect all function declaration information to support hoisting.
     * User defined functions'(UDF) signatures should not collide with each other or built-in functions.
     * As Some functions may not be known yet, missing functions should not raise errors.
     * Since unknown functions have unset execution modes, errors should not be raised for accessing these.
     */
    public void setConfigForInitialPass() {
        this.ignoreDuplicateUserDefinedFunctionError = false;
        FunctionCallExpression.suppressMissingFunctionErrors = true;
        Node.suppressUnsetExecutionModeAccessedErrors = true;
    }

    /**
     * Intermediate passes should update the execution modes of expressions as more UDFs can be resolved.
     * As all UDFs should be known at this stage, missing functions should raise errors
     * As UDFs may still have unresolved execution modes, errors should not be raised for accessing these.
     */
    public void setConfigForIntermediatePasses() {
        // Updates on UDFs are currently performed by redeclaration, hence collision errors are disabled
        this.ignoreDuplicateUserDefinedFunctionError = true;
        FunctionCallExpression.suppressMissingFunctionErrors = false;
        Node.suppressUnsetExecutionModeAccessedErrors = true;
    }

    /**
     * All expression execution mode and UDF information should be available in the final pass
     */
    public void setConfigForFinalPass() {
        // Updates on UDFs are currently performed by redeclaration, hence collision errors are disabled
        this.ignoreDuplicateUserDefinedFunctionError = true;
        FunctionCallExpression.suppressMissingFunctionErrors = false;
        Node.suppressUnsetExecutionModeAccessedErrors = false;
    }

    @Override
    protected StaticContext defaultAction(Node node, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(node, argument);
        // initialize execution mode by visiting children and expressions first, then calling initialize methods
        node.initHighestExecutionMode();
        return generatedContext;
    }

    @Override
    public StaticContext visit(Node node, StaticContext argument) {
        if (argument == null) {
            argument = new StaticContext();
        }
        if (node instanceof Expression) {
            ((Expression) node).setStaticContext(argument);
        }
        return node.accept(this, argument);
    }

    // region primary
    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        String variableName = expression.getVariableName();
        if (!argument.isInScope(variableName)) {
            throw new UndeclaredVariableException(
                    "Uninitialized variable reference: " + variableName,
                    expression.getMetadata()
            );
        } else {
            expression.setType(argument.getVariableSequenceType(variableName));
            expression.setHighestExecutionMode(argument.getVariableStorageMode(variableName));
            return argument;
        }
    }

    @Override
    public StaticContext visitFunctionDeclaration(InlineFunctionExpression expression, StaticContext argument) {
        // define a static context for the function body, add params to the context and visit the body expression
        StaticContext functionDeclarationContext = new StaticContext(argument);
        expression.getParams()
            .forEach(
                (paramName, sequenceType) -> functionDeclarationContext.addVariable(
                    paramName,
                    sequenceType,
                    expression.getMetadata(),
                    ExecutionMode.LOCAL // static udf currently supports materialized(local) params, not RDDs or DFs
                )
            );
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.getBody(), functionDeclarationContext);
        expression.initHighestExecutionMode();
        expression.registerUserDefinedFunctionExecutionMode(
            this.ignoreDuplicateUserDefinedFunctionError
        );
        return functionDeclarationContext;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(expression, argument);
        expression.initFunctionCallHighestExecutionMode();
        return generatedContext;
    }
    // endregion

    // region FLWOR
    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        StaticContext result = this.visit(expression.getStartClause(), argument);
        for (Clause clause : expression.getContentClauses()) {
            result = this.visit(clause, result);
        }

        result = this.visit(expression.getReturnClause(), result);
        return result;
    }

    // region FLWOR vars
    @Override
    public StaticContext visitForClauseVar(ForClauseVar expression, StaticContext argument) {
        // TODO visit at...
        this.visit(expression.getExpression(), argument);
        expression.initHighestExecutionAndVariableHighestStorageModes();

        StaticContext result = visitFlowrVarDeclaration(expression, argument);
        if (expression.getPositionalVariableReference() != null) {
            result.addVariable(
                expression.getPositionalVariableReference().getVariableName(),
                new SequenceType(ItemType.integerItem),
                expression.getMetadata(),
                ExecutionMode.LOCAL
            );
        }
        return result;
    }

    @Override
    public StaticContext visitLetClauseVar(LetClauseVar expression, StaticContext argument) {
        this.visit(expression.getExpression(), argument);
        expression.initHighestExecutionAndVariableHighestStorageModes();
        return visitFlowrVarDeclaration(expression, argument);
    }

    @Override
    public StaticContext visitGroupByClauseVar(GroupByClauseVar expression, StaticContext argument) {
        StaticContext groupByClauseContext;
        if (expression.getExpression() != null) {
            // if a variable declaration takes place
            this.visit(expression.getExpression(), argument);
            // initialize execution and storage modes and then add the variable to the context
            expression.initHighestExecutionAndVariableHighestStorageModes();
            groupByClauseContext = visitFlowrVarDeclaration(expression, argument);
        } else {
            // if a variable is only referenced, use the context as is
            groupByClauseContext = argument;
        }
        // validate if the referenced variable exists in the current context
        this.visit(expression.getVariableReference(), groupByClauseContext);
        return groupByClauseContext;
    }

    @Override
    public StaticContext visitCountClause(CountClause expression, StaticContext argument) {
        expression.initHighestExecutionMode();
        StaticContext result = new StaticContext(argument);
        result.addVariable(
            expression.getCountVariable().getVariableName(),
            new SequenceType(ItemType.integerItem, SequenceType.Arity.One),
            expression.getMetadata(),
            ExecutionMode.LOCAL
        );
        this.visit(expression.getCountVariable(), result);
        return result;
    }

    private StaticContext visitFlowrVarDeclaration(FlworVarDecl expression, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        // TODO for now we only suppot as/default, no inference, flags
        result.addVariable(
            expression.getVariableReference().getVariableName(),
            expression.getSequenceType(),
            expression.getMetadata(),
            expression.getVariableHighestStorageMode()
        );
        return result;
    }
    // endregion
    // endregion

    // region quantifiers
    @Override
    public StaticContext visitQuantifiedExpression(QuantifiedExpression expression, StaticContext argument) {
        StaticContext contextWithQuantifiedExpressionVariables = argument;
        for (QuantifiedExpressionVar clause : expression.getVariables()) {
            contextWithQuantifiedExpressionVariables = this.visit(clause, contextWithQuantifiedExpressionVariables);
        }
        // validate expression with the defined variables
        this.visit(expression.getEvaluationExpression(), contextWithQuantifiedExpressionVariables);
        expression.initHighestExecutionMode();
        // return the given context unchanged as defined variables go out of scope
        return argument;
    }

    @Override
    public StaticContext visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, StaticContext argument) {
        // validate expression with starting context
        this.visit(expression.getExpression(), argument);
        expression.initHighestExecutionMode();

        // create a child context, add the variable and return it
        StaticContext result = new StaticContext(argument);
        result.addVariable(
            expression.getVariableReference().getVariableName(),
            expression.getSequenceType(),
            expression.getMetadata(),
            ExecutionMode.LOCAL
        );
        return result;
    }
    // endregion

    // region control
    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        this.visit(expression.getTestCondition(), argument);
        for (TypeswitchCase c : expression.getCases()) {
            StaticContext caseContext = new StaticContext(argument);
            String variableName = c.getVariableName();
            if (variableName != null) {
                caseContext.addVariable(
                    variableName,
                    null,
                    expression.getMetadata(),
                    ExecutionMode.LOCAL
                );
            }
            this.visit(c.getReturnExpression(), caseContext);
        }

        String defaultCaseVariableName = expression.getDefaultCase().getVariableName();
        if (defaultCaseVariableName == null) {
            this.visit(expression.getDefaultCase().getReturnExpression(), argument);
        } else {
            // add variable to child context to visit default return expression
            StaticContext defaultCaseStaticContext = new StaticContext(argument);
            defaultCaseStaticContext.addVariable(
                defaultCaseVariableName,
                null,
                expression.getMetadata(),
                ExecutionMode.LOCAL
            );
            this.visit(expression.getDefaultCase().getReturnExpression(), defaultCaseStaticContext);
        }
        expression.initHighestExecutionMode();
        // return the given context unchanged as defined variables go out of scope
        return argument;
    }
    // endregion

}
