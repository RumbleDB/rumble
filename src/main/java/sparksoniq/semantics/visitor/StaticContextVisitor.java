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

import org.rumbledb.exceptions.UndeclaredVariableException;

import sparksoniq.jsoniq.ExecutionMode;
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
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionCall;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionDeclaration;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import sparksoniq.semantics.StaticContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

public class StaticContextVisitor extends AbstractExpressionOrClauseVisitor<StaticContext> {

    // indicate whether an error should be thrown if an duplicate user defined function declaration is detected
    private boolean ignoreDuplicateUserDefinedFunctionError;

    // indicate whether an error should be thrown if a function call is made for a non-existing function
    private boolean ignoreMissingFunctionError;

    private boolean ignoreUnsetExecutionModeAccessDuringFunctionDeclaration;

    public StaticContextVisitor() {
        this.setConfigForInitialPass();
    }

    // static context visitor's multipass algorithm enables function hoisting

    // initial pass collects function declaration information
    public void setConfigForInitialPass() {
        // initial pass ensures that built-in functions are not redeclared or a single function definition is duplicated
        ignoreDuplicateUserDefinedFunctionError = false;
        // functions calls that refer to undeclared functions will have their execution mode unset
        ignoreMissingFunctionError = true;
        // function declarations make undefined/unset function calls in their body will also have their execution mode
        // unset
        ignoreUnsetExecutionModeAccessDuringFunctionDeclaration = true;
    }

    // intermediate passes set execution mode for functions that refer to other functions
    public void setConfigForIntermediatePasses() {
        // user defined functions will be redeclared in consequent passes to update their execution modes
        ignoreDuplicateUserDefinedFunctionError = true;
        // since all function declaration information is available, functions cannot be missing anymore
        ignoreMissingFunctionError = false;
        ignoreUnsetExecutionModeAccessDuringFunctionDeclaration = true;
    }

    // final pass ensures that all function calls have their execution modes set
    public void setConfigForFinalPass() {
        ignoreDuplicateUserDefinedFunctionError = true;
        ignoreMissingFunctionError = false;
        ignoreUnsetExecutionModeAccessDuringFunctionDeclaration = false;
    }

    @Override
    protected StaticContext defaultAction(ExpressionOrClause expression, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(expression, argument);
        // initialize execution mode by visiting children and expressions first, then calling initialize methods
        expression.initHighestExecutionMode();
        return generatedContext;
    }

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
            expression.setHighestExecutionMode(argument.getVariableStorageMode(variableName));
            return argument;
        }
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        // define a static context for the function body, add params to the context and visit the body expression
        StaticContext functionDeclarationContext = new StaticContext(argument);
        expression.get_params()
            .forEach(
                (paramName, flworVarSequenceType) -> functionDeclarationContext.addVariable(
                    paramName,
                    flworVarSequenceType.getSequence(),
                    expression.getMetadata(),
                    ExecutionMode.LOCAL // static udf currently supports materialized(local) params, not RDDs or DFs
                )
            );
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.get_body(), functionDeclarationContext);
        expression.initHighestExecutionMode();
        expression.registerUserDefinedFunctionExecutionMode(
            ignoreDuplicateUserDefinedFunctionError,
            ignoreUnsetExecutionModeAccessDuringFunctionDeclaration
        );
        return functionDeclarationContext;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCall expression, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(expression, argument);
        expression.initFunctionCallHighestExecutionMode(ignoreMissingFunctionError);
        return generatedContext;
    }

    @Override
    public StaticContext visitPostfixExpression(PostFixExpression expression, StaticContext argument) {
        // visit and initialize firstly the primary expression, then the postfix extensions
        this.visit(expression.get_primaryExpressionNode(), argument);
        expression.initHighestExecutionMode();
        expression.getExtensions().forEach(extension -> extension.initHighestExecutionMode());
        return visitDescendants(expression, argument);
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
        expression.initHighestExecutionAndVariableHighestStorageModes();

        StaticContext result = visitFlowrVarDeclaration(expression, argument);
        if (expression.getPositionalVariableReference() != null) {
            result.addVariable(
                expression.getPositionalVariableReference().getVariableName(),
                new SequenceType(new ItemType(ItemTypes.IntegerItem)),
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
            new SequenceType(new ItemType(ItemTypes.IntegerItem), SequenceType.Arity.One),
            expression.getMetadata(),
            ExecutionMode.LOCAL
        );
        this.visit(expression.getCountVariable(), result);
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
        SequenceType type = expression.getSequenceType() == null ? new SequenceType() : expression.getSequenceType();
        result.addVariable(
            expression.getVariableReference().getVariableName(),
            type,
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
        expression.getCases().forEach(typeSwitchCaseExpression -> this.visit(typeSwitchCaseExpression, argument));

        VariableReference defaultCaseVariableReference = expression.getVarRefDefault();
        if (defaultCaseVariableReference == null) {
            this.visit(expression.getDefaultExpression(), argument);
        } else {
            // add variable to child context to visit default return expression
            StaticContext defaultCaseStaticContext = new StaticContext(argument);
            defaultCaseStaticContext.addVariable(
                defaultCaseVariableReference.getVariableName(),
                null,
                expression.getMetadata(),
                ExecutionMode.LOCAL
            );
            this.visit(expression.getDefaultExpression(), defaultCaseStaticContext);
        }
        expression.initHighestExecutionMode();
        // return the given context unchanged as defined variables go out of scope
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchCaseExpression(TypeSwitchCaseExpression expression, StaticContext argument) {
        StaticContext caseContext = new StaticContext(argument);
        VariableReference variableReference = expression.getVariableReference();
        if (variableReference != null) {
            caseContext.addVariable(
                variableReference.getVariableName(),
                null,
                expression.getMetadata(),
                ExecutionMode.LOCAL
            );
        }
        this.visit(expression.getReturnExpression(), caseContext);
        expression.initHighestExecutionMode();
        // return the given context unchanged as defined variables go out of scope
        return argument;
    }
    // endregion

}
