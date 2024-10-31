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

import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UndeclaredVariableException;
import org.rumbledb.exceptions.VariableAlreadyExistsException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Static context visitor implements a multi-pass algorithm that enables function hoisting
 */
public class StaticContextVisitor extends AbstractNodeVisitor<StaticContext> {

    private Map<String, StaticContext> importedModuleContexts;

    StaticContextVisitor() {
        this.importedModuleContexts = new HashMap<>();
    }

    @Override
    protected StaticContext defaultAction(Node node, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(node, argument);
        // initialize execution mode by visiting children and expressions first, then calling initialize methods
        return generatedContext;
    }

    @Override
    public StaticContext visit(Node node, StaticContext argument) {
        if (argument == null) {
            throw new OurBadException("No static context provided!");
        }
        if (node instanceof Expression) {
            ((Expression) node).setStaticContext(argument);
        }
        if (node instanceof Statement) {
            ((Statement) node).setStaticContext(argument);
        }
        if (node instanceof Clause) {
            ((Clause) node).setStaticContext(argument);
        }
        return node.accept(this, argument);
    }

    @Override
    public StaticContext visitMainModule(MainModule mainModule, StaticContext argument) {
        this.importedModuleContexts.clear();
        StaticContext generatedContext = visitDescendants(mainModule, argument);
        return generatedContext;
    }

    @Override
    public StaticContext visitLibraryModule(LibraryModule libraryModule, StaticContext argument) {
        if (!this.importedModuleContexts.containsKey(libraryModule.getNamespace())) {
            StaticContext moduleContext = libraryModule.getStaticContext();
            this.visit(libraryModule.getProlog(), moduleContext);
            this.importedModuleContexts.put(libraryModule.getNamespace(), moduleContext);
        }
        argument.importModuleContext(
            this.importedModuleContexts.get(libraryModule.getNamespace())
        );
        argument.getInScopeSchemaTypes()
            .importModuleTypes(
                this.importedModuleContexts.get(libraryModule.getNamespace()).getInScopeSchemaTypes()
            );
        return argument;
    }

    // region primary
    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        Name variableName = expression.getVariableName();
        if (!argument.isInScope(variableName)) {
            throw new UndeclaredVariableException(
                    "Uninitialized variable reference: " + variableName,
                    expression.getMetadata()
            );
        } else {
            // note: sequence type can be null
            expression.setActualType(argument.getVariableSequenceType(variableName));
            return argument;
        }
    }

    private void populateFunctionDeclarationStaticContext(
            StaticContext functionDeclarationContext,
            InlineFunctionExpression expression
    ) {
        for (Name name : expression.getParams().keySet()) {
            functionDeclarationContext.addVariable(
                name,
                expression.getParams().get(name),
                expression.getMetadata()
            );
        }
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration declaration, StaticContext argument) {
        InlineFunctionExpression expression = (InlineFunctionExpression) declaration.getExpression();
        if (expression.getActualReturnType() != null) {
            expression.getActualReturnType().resolve(argument, declaration.getMetadata());
        }
        for (Entry<Name, SequenceType> itemType : expression.getParams().entrySet()) {
            itemType.getValue().resolve(argument, declaration.getMetadata());
        }
        // define a static context for the function body, add params to the context and visit the body expression
        StaticContext functionDeclarationContext = new StaticContext(argument);
        expression.setStaticContext(functionDeclarationContext);
        populateFunctionDeclarationStaticContext(functionDeclarationContext, expression);
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.getBody(), functionDeclarationContext);
        argument.addFunctionSignature(
            expression.getFunctionIdentifier(),
            new FunctionSignature(
                    new ArrayList<>(expression.getParams().values()),
                    expression.getReturnType(),
                    expression.isUpdating()
            )
        );
        return argument;
    }

    @Override
    public StaticContext visitInlineFunctionExpr(InlineFunctionExpression expression, StaticContext argument) {
        // define a static context for the function body, add params to the context and visit the body expression
        StaticContext functionDeclarationContext = new StaticContext(argument);
        for (Entry<Name, SequenceType> entry : expression.getParams().entrySet()) {
            functionDeclarationContext.addVariable(
                entry.getKey(),
                entry.getValue(),
                expression.getMetadata()
            );
        }
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.getBody(), functionDeclarationContext);
        return functionDeclarationContext;
    }

    // endregion

    // region FLWOR
    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        this.visit(clause, argument);
        return argument;
    }

    // region FLWOR vars
    @Override
    public StaticContext visitForClause(ForClause clause, StaticContext argument) {
        // TODO visit at...
        this.visit(clause.getExpression(), argument);

        StaticContext result = new StaticContext(argument);// add a block level to function declaration body
        result.addVariable(
            clause.getVariableName(),
            clause.getActualSequenceType(),
            clause.getMetadata()
        );
        clause.getSequenceType().resolve(result, clause.getMetadata());

        if (clause.getPositionalVariableName() != null) {
            result.addVariable(
                clause.getPositionalVariableName(),
                new SequenceType(BuiltinTypesCatalogue.integerItem),
                clause.getMetadata()
            );
        }
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitLetClause(LetClause clause, StaticContext argument) {
        this.visit(clause.getExpression(), argument);

        StaticContext result = new StaticContext(argument);
        result.addVariable(
            clause.getVariableName(),
            clause.getActualSequenceType(),
            clause.getMetadata()
        );
        clause.getSequenceType().resolve(result, clause.getMetadata());
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitWhereClause(WhereClause clause, StaticContext argument) {
        this.visit(clause.getWhereExpression(), argument);

        StaticContext result = new StaticContext(argument);
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause clause, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            if (variable.getExpression() != null) {
                // if a variable declaration takes place
                this.visit(variable.getExpression(), argument);
                result.addVariable(
                    variable.getVariableName(),
                    variable.getActualSequenceType(),
                    clause.getMetadata()
                );
            } else if (!argument.isInScope(variable.getVariableName())) {
                throw new UndeclaredVariableException(
                        "Uninitialized variable reference: " + variable.getVariableName(),
                        clause.getMetadata()
                );
            }
        }
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause clause, StaticContext argument) {
        for (OrderByClauseSortingKey s : clause.getSortingKeys()) {
            this.visit(s.getExpression(), argument);
        }

        StaticContext result = new StaticContext(argument);
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitCountClause(CountClause clause, StaticContext argument) {
        StaticContext result = new StaticContext(argument);
        result.addVariable(
            clause.getCountVariableName(),
            SequenceType.INTEGER,
            clause.getMetadata()
        );
        this.visit(clause.getNextClause(), result);
        return argument;
    }

    @Override
    public StaticContext visitReturnClause(ReturnClause clause, StaticContext argument) {
        this.visit(clause.getReturnExpr(), argument);
        return argument;
    }

    // endregion

    // region control
    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        this.visit(expression.getTestCondition(), argument);
        for (TypeswitchCase c : expression.getCases()) {
            StaticContext caseContext = new StaticContext(argument);
            Name variableName = c.getVariableName();
            if (variableName != null) {
                caseContext.addVariable(
                    variableName,
                    null,
                    expression.getMetadata()
                );
            }
            this.visit(c.getReturnExpression(), caseContext);
            for (SequenceType sequenceType : c.getUnion()) {
                sequenceType.resolve(argument, expression.getMetadata());
            }
        }

        Name defaultCaseVariableName = expression.getDefaultCase().getVariableName();
        if (defaultCaseVariableName == null) {
            this.visit(expression.getDefaultCase().getReturnExpression(), argument);
        } else {
            // add variable to child context to visit default return expression
            StaticContext defaultCaseStaticContext = new StaticContext(argument);
            defaultCaseStaticContext.addVariable(
                defaultCaseVariableName,
                null,
                expression.getMetadata()
            );
            this.visit(expression.getDefaultCase().getReturnExpression(), defaultCaseStaticContext);
        }
        // return the given context unchanged as defined variables go out of scope
        return argument;
    }
    // endregion

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration variableDeclaration, StaticContext argument) {
        if (variableDeclaration.getExpression() != null) {
            this.visit(variableDeclaration.getExpression(), argument);
        }
        // first pass.
        argument.addVariable(
            variableDeclaration.getVariableName(),
            variableDeclaration.getActualSequenceType(),
            variableDeclaration.getMetadata(),
            variableDeclaration.isAssignable()
        );
        return argument;
    }

    @Override
    public StaticContext visitCommaVariableDeclStatement(CommaVariableDeclStatement statement, StaticContext argument) {
        StaticContext currentContext = new StaticContext(argument);
        for (VariableDeclStatement variableDeclStatement : statement.getVariables()) {
            currentContext = this.visit(variableDeclStatement, currentContext);
        }
        statement.setStaticContext(currentContext);
        return currentContext;
    }

    @Override
    public StaticContext visitTransformExpression(TransformExpression expression, StaticContext argument) {
        argument.setCurrentMutabilityLevel(argument.getCurrentMutabilityLevel() + 1);
        StaticContext result = argument;
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            result = this.visitCopyDecl(copyDecl, result, argument);
        }

        result = this.visit(expression.getModifyExpression(), result);
        result = this.visit(expression.getReturnExpression(), result);

        expression.setStaticContext(result);
        expression.setMutabilityLevel(result.getCurrentMutabilityLevel());

        argument.setCurrentMutabilityLevel(argument.getCurrentMutabilityLevel() - 1);
        return argument;
    }

    private StaticContext visitCopyDecl(
            CopyDeclaration copyDeclaration,
            StaticContext argument,
            StaticContext copyContext
    ) {
        this.visit(copyDeclaration.getSourceExpression(), copyContext);

        StaticContext result = new StaticContext(argument);
        result.addVariable(
            copyDeclaration.getVariableName(),
            copyDeclaration.getSourceSequenceType(),
            copyDeclaration.getSourceExpression().getMetadata()
        );
        copyDeclaration.getSourceSequenceType()
            .resolve(copyContext, copyDeclaration.getSourceExpression().getMetadata());

        return result;
    }

    @Override
    public StaticContext visitTypeDeclaration(TypeDeclaration declaration, StaticContext argument) {
        ItemType type = declaration.getDefinition();
        // only first pass
        argument.getInScopeSchemaTypes().addInScopeSchemaType(type, declaration.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitProlog(Prolog prolog, StaticContext argument) {
        StaticContext generatedContext = visitDescendants(prolog, argument);
        for (ItemType itemType : generatedContext.getInScopeSchemaTypes().getInScopeSchemaTypes()) {
            itemType.resolve(generatedContext, prolog.getMetadata());
        }
        return generatedContext;
    }

    @Override
    public StaticContext visitValidateTypeExpression(ValidateTypeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.getSequenceType().resolve(argument, expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitCastExpression(CastExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.getSequenceType().resolve(argument, expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitCastableExpression(CastableExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.getSequenceType().resolve(argument, expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.getSequenceType().resolve(argument, expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitInstanceOfExpression(InstanceOfExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.getSequenceType().resolve(argument, expression.getMetadata());
        return argument;
    }

    @Override
    public StaticContext visitVariableDeclStatement(
            VariableDeclStatement variableDeclStatement,
            StaticContext argument
    ) {
        if (variableDeclStatement.getVariableExpression() != null) {
            this.visit(variableDeclStatement.getVariableExpression(), argument);
        }
        if (argument.hasVariableInScopeOnly(variableDeclStatement.getVariableName())) {
            throw new VariableAlreadyExistsException(
                    variableDeclStatement.getVariableName(),
                    variableDeclStatement.getMetadata()
            );
        }
        StaticContext result = new StaticContext(argument);
        result.addVariable(
            variableDeclStatement.getVariableName(),
            variableDeclStatement.getActualSequenceType(),
            variableDeclStatement.getMetadata(),
            variableDeclStatement.isAssignable()
        );
        variableDeclStatement.setStaticContext(result);
        return result;
    }

    @Override
    public StaticContext visitProgram(Program program, StaticContext argument) {
        StaticContext currentContext = new StaticContext(argument);
        visitDescendants(program, currentContext);
        return currentContext;
    }

    @Override
    public StaticContext visitBlockStatement(
            BlockStatement statement,
            StaticContext argument
    ) {
        StaticContext currentContext = new StaticContext(argument);
        for (Statement child : statement.getBlockStatements()) {
            currentContext = this.visit(child, currentContext);
        }
        return currentContext;
    }

    @Override
    public StaticContext visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, StaticContext argument) {
        StaticContext currentContext = argument;
        for (Statement statement : statementsAndExpr.getStatements()) {
            currentContext = this.visit(statement, currentContext);
        }
        this.visit(statementsAndExpr.getExpression(), currentContext);
        statementsAndExpr.setStaticContext(argument);
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            StaticContext argument
    ) {
        StaticContext currentContext = argument;
        for (Statement statement : statementsAndOptionalExpr.getStatements()) {
            currentContext = this.visit(statement, currentContext);
        }
        if (statementsAndOptionalExpr.getExpression() != null) {
            this.visit(statementsAndOptionalExpr.getExpression(), currentContext);
        }
        statementsAndOptionalExpr.setStaticContext(argument);
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchStatement(TypeSwitchStatement statement, StaticContext argument) {
        this.visit(statement.getTestCondition(), argument);
        for (TypeSwitchStatementCase tssc : statement.getCases()) {
            StaticContext caseContext = new StaticContext(argument);
            Name variableName = tssc.getVariableName();
            if (variableName != null) {
                caseContext.addVariable(variableName, null, statement.getMetadata());
            }
            this.visit(tssc.getReturnStatement(), caseContext);
            for (SequenceType sequenceType : tssc.getUnion()) {
                sequenceType.resolve(argument, statement.getMetadata());
            }
        }
        Name defaultCaseVariableName = statement.getDefaultCase().getVariableName();
        if (defaultCaseVariableName == null) {
            this.visit(statement.getDefaultCase().getReturnStatement(), argument);
        } else {
            StaticContext defaultCaseStaticContext = new StaticContext(argument);
            defaultCaseStaticContext.addVariable(defaultCaseVariableName, null, statement.getMetadata());
            this.visit(statement.getDefaultCase().getReturnStatement(), defaultCaseStaticContext);
        }
        return argument;
    }

    @Override
    public StaticContext visitSwitchStatement(SwitchStatement statement, StaticContext argument) {
        this.visit(statement.getTestCondition(), argument);
        for (SwitchCaseStatement switchCaseStatement : statement.getCases()) {
            StaticContext caseContext = new StaticContext(argument);
            for (Expression conditionalExpr : switchCaseStatement.getConditionExpressions()) {
                this.visit(conditionalExpr, caseContext);
            }
            this.visit(switchCaseStatement.getReturnStatement(), caseContext);
        }
        StaticContext defaultCaseStaticContext = new StaticContext(argument);
        this.visit(statement.getDefaultStatement(), defaultCaseStaticContext);
        statement.setStaticContext(argument);
        return argument;
    }

    @Override
    public StaticContext visitConditionalStatement(ConditionalStatement statement, StaticContext argument) {
        StaticContext thenContext = new StaticContext(argument);
        StaticContext elseContext = new StaticContext(argument);
        this.visit(statement.getCondition(), argument);
        this.visit(statement.getBranch(), thenContext);
        this.visit(statement.getElseBranch(), elseContext);

        statement.setStaticContext(argument);
        return argument;
    }

    @Override
    public StaticContext visitAssignStatement(AssignStatement statement, StaticContext argument) {
        visit(statement.getAssignExpression(), argument);
        if (!argument.isInScope(statement.getName())) {
            throw new UndeclaredVariableException(
                    "Uninitialized variable reference: " + statement.getName(),
                    statement.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public StaticContext visitFlowrStatement(FlowrStatement statement, StaticContext argument) {
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        this.visit(clause, argument);
        return argument;
    }

    @Override
    public StaticContext visitReturnStatementClause(ReturnStatementClause clause, StaticContext argument) {
        this.visit(clause.getReturnStatement(), argument);
        return argument;
    }
}
