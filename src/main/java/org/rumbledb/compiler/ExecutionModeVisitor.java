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

import org.apache.log4j.LogManager;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunction.BuiltinFunctionExecutionMode;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.InScopeVariable;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
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
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.misc.RangeOperationIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Static context visitor implements a multi-pass algorithm that enables function hoisting
 */
public class ExecutionModeVisitor extends AbstractNodeVisitor<StaticContext> {

    private VisitorConfig visitorConfig;
    private RumbleRuntimeConfiguration configuration;
    private List<Statement> exitStatementChildren;

    ExecutionModeVisitor(RumbleRuntimeConfiguration configuration) {
        this.visitorConfig = VisitorConfig.staticContextVisitorInitialPassConfig;
        this.configuration = configuration;
        this.exitStatementChildren = new ArrayList<>();
    }

    void setVisitorConfig(VisitorConfig visitorConfig) {
        this.visitorConfig = visitorConfig;
        this.exitStatementChildren = new ArrayList<>();
    }

    public ExecutionMode DATAFRAMEifConfigurationAllows() {
        if (this.configuration.dataFrameExecution()) {
            return ExecutionMode.DATAFRAME;
        } else {
            return ExecutionMode.RDD;
        }
    }

    @Override
    protected StaticContext defaultAction(Node node, StaticContext argument) {
        visitDescendants(node, argument);
        node.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    @Override
    public StaticContext visitMainModule(MainModule mainModule, StaticContext argument) {
        visitDescendants(mainModule, mainModule.getStaticContext());
        mainModule.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    @Override
    public StaticContext visitLibraryModule(LibraryModule libraryModule, StaticContext argument) {
        if (libraryModule.getProlog() != null) {
            this.visit(libraryModule.getProlog(), libraryModule.getStaticContext());
        }
        libraryModule.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    // region primary
    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        if (expression.alwaysReturnsAtMostOneItem()) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        Name variableName = expression.getVariableName();
        ExecutionMode mode = expression.getStaticContext().getVariableStorageMode(variableName);
        if (this.visitorConfig.setUnsetToLocal() && mode.equals(ExecutionMode.UNSET)) {
            if (
                expression.getStaticSequenceType().getArity().equals(Arity.OneOrMore)
                    ||
                    expression.getStaticSequenceType().getArity().equals(Arity.ZeroOrMore)
            ) {
                if (expression.getStaticSequenceType().getItemType().isObjectItemType()) {
                    System.err.println(
                        "[WARNING] Forcing execution mode of variable "
                            + expression.getVariableName()
                            + " to DataFrame based on static object* type."
                    );
                    expression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                    return argument;
                }
            }
            System.err.println(
                "[WARNING] Forcing execution mode of variable " + expression.getVariableName() + " to local."
            );
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        expression.setHighestExecutionMode(mode);
        return argument;
    }

    private void populateFunctionDeclarationStaticContext(
            StaticContext functionDeclarationContext,
            List<ExecutionMode> modes,
            InlineFunctionExpression expression
    ) {
        int i = 0;
        for (Name name : expression.getParams().keySet()) {
            ExecutionMode mode = modes.get(i);
            SequenceType type = expression.getParams().get(name);
            if (type.isEmptySequence()) {
                mode = ExecutionMode.LOCAL;
            } else if (type.getArity().equals(Arity.OneOrZero) || type.getArity().equals(Arity.One)) {
                mode = ExecutionMode.LOCAL;
            }
            functionDeclarationContext.setVariableStorageMode(
                name,
                mode
            );
            ++i;
        }
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration declaration, StaticContext argument) {
        InlineFunctionExpression expression = (InlineFunctionExpression) declaration.getExpression();
        // define a static context for the function body, add params to the context and visit the body expression
        List<ExecutionMode> modes = expression.getStaticContext()
            .getUserDefinedFunctionsExecutionModes()
            .getParameterExecutionMode(
                expression.getFunctionIdentifier(),
                expression.getMetadata()
            );
        populateFunctionDeclarationStaticContext(expression.getStaticContext(), modes, expression);
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.getBody(), expression.getStaticContext());
        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        declaration.setHighestExecutionMode(expression.getBody().getHighestExecutionMode(this.visitorConfig));
        expression.registerUserDefinedFunctionExecutionMode(
            this.visitorConfig
        );
        // Reset exit statements list as we exit function scope
        this.exitStatementChildren.clear();
        return argument;
    }

    @Override
    public StaticContext visitInlineFunctionExpr(InlineFunctionExpression expression, StaticContext argument) {
        // define a static context for the function body, add params to the context and visit the body expression
        expression.getParams()
            .forEach(
                (paramName, sequenceType) -> expression.getBody()
                    .getStaticContext()
                    .setVariableStorageMode(
                        paramName,
                        ExecutionMode.LOCAL
                    )
            );
        // visit the body first to make its execution mode available while adding the function to the catalog
        this.visit(expression.getBody(), expression.getBody().getStaticContext());
        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        expression.registerUserDefinedFunctionExecutionMode(
            this.visitorConfig
        );
        // Reset exit statements list as we exit function scope
        this.exitStatementChildren.clear();
        return argument;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, expression.getStaticContext());
        FunctionIdentifier identifier = expression.getFunctionIdentifier();
        if (!BuiltinFunctionCatalogue.exists(identifier)) {
            List<ExecutionMode> modes = new ArrayList<>();
            for (Expression parameter : expression.getArguments()) {
                if (parameter == null) {
                    // This is for a partial application, for ? arguments.
                    // We do not modify the current mode for this parameter.
                    modes.add(ExecutionMode.UNSET);
                } else {
                    modes.add(parameter.getHighestExecutionMode(this.visitorConfig));
                }
            }
            expression.getStaticContext()
                .getUserDefinedFunctionsExecutionModes()
                .setParameterExecutionMode(
                    identifier,
                    modes,
                    expression.getMetadata()
                );
        }
        if (BuiltinFunctionCatalogue.exists(expression.getFunctionIdentifier())) {
            BuiltinFunction builtinFunction = BuiltinFunctionCatalogue.getBuiltinFunction(
                expression.getFunctionIdentifier()
            );
            expression.setHighestExecutionMode(
                getBuiltInFunctionExecutionMode(
                    builtinFunction,
                    expression.getArguments().size() > 0
                        ? expression.getArguments().get(0).getHighestExecutionMode(this.visitorConfig)
                        : null
                )
            );
        } else {
            if (
                expression.getStaticContext()
                    .getUserDefinedFunctionsExecutionModes()
                    .exists(expression.getFunctionIdentifier())
            ) {
                if (expression.isPartialApplication()) {
                    expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                } else {
                    expression.setHighestExecutionMode(
                        expression.getStaticContext()
                            .getUserDefinedFunctionsExecutionModes()
                            .getExecutionMode(expression.getFunctionIdentifier(), expression.getMetadata())
                    );
                }
            } else {

                if (!this.visitorConfig.suppressErrorsForCallingMissingFunctions()) {
                    throw new UnknownFunctionCallException(
                            expression.getFunctionIdentifier().getName(),
                            expression.getFunctionIdentifier().getArity(),
                            expression.getMetadata()
                    );
                }
            }
        }
        return argument;
    }

    private ExecutionMode getBuiltInFunctionExecutionMode(
            BuiltinFunction builtinFunction,
            ExecutionMode firstMode
    ) {
        BuiltinFunctionExecutionMode functionExecutionMode = builtinFunction.getBuiltinFunctionExecutionMode();
        if (functionExecutionMode == BuiltinFunctionExecutionMode.LOCAL) {
            return ExecutionMode.LOCAL;
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.RDD) {
            return ExecutionMode.RDD;
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.DATAFRAME) {
            return DATAFRAMEifConfigurationAllows();
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT) {
            ExecutionMode firstArgumentExecutionMode = firstMode;
            if (firstArgumentExecutionMode.isDataFrame()) {
                return DATAFRAMEifConfigurationAllows();
            }
            if (firstArgumentExecutionMode.isRDDOrDataFrame()) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
        }
        if (
            functionExecutionMode == BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
        ) {
            ExecutionMode firstArgumentExecutionMode = firstMode;
            if (
                firstArgumentExecutionMode.isRDDOrDataFrame()
                    && !firstArgumentExecutionMode.isDataFrame()
            ) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
        }
        throw new OurBadException(
                "Unhandled functionExecutionMode detected while extracting execution mode for built-in function."
        );
    }

    // endregion

    @Override
    public StaticContext visitReturnClause(ReturnClause expression, StaticContext argument) {
        visit(expression.getReturnExpr(), expression.getReturnExpr().getStaticContext());
        if (expression.getPreviousClause().getHighestExecutionMode(this.visitorConfig).isDataFrame()) {
            if (
                expression.getReturnExpr()
                    .getStaticSequenceType()
                    .getItemType()
                    .isCompatibleWithDataFrames(this.configuration)
            ) {
                expression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                return argument;
            } else {
                expression.setHighestExecutionMode(ExecutionMode.RDD);
                return argument;
            }
        }
        if (expression.getReturnExpr().getHighestExecutionMode(this.visitorConfig).isRDD()) {
            expression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        if (expression.getReturnExpr().getHighestExecutionMode(this.visitorConfig).isDataFrame()) {
            if (
                expression.getReturnExpr()
                    .getStaticSequenceType()
                    .getItemType()
                    .isCompatibleWithDataFrames(this.configuration)
            ) {
                expression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                return argument;
            } else {
                expression.setHighestExecutionMode(ExecutionMode.RDD);
                return argument;
            }
        }
        if (expression.getReturnExpr().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            expression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        if (expression.getPreviousClause().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            expression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    // region FLWOR
    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        while (clause != null) {
            if (clause.getNextClause() != null) {
                this.visit(clause, clause.getNextClause().getStaticContext());
            } else {
                this.visit(clause, null);
            }
            clause = clause.getNextClause();
        }
        if (expression.alwaysReturnsAtMostOneItem()) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        } else {
            expression.setHighestExecutionMode(
                expression.getReturnClause().getHighestExecutionMode(this.visitorConfig)
            );
        }
        return argument;
    }

    // region FLWOR vars
    @Override
    public StaticContext visitForClause(ForClause clause, StaticContext argument) {
        this.visit(clause.getExpression(), clause.getExpression().getStaticContext());
        clause.setHighestExecutionMode(
            (clause.getExpression().getHighestExecutionMode(this.visitorConfig).isRDDOrDataFrame()
                || (clause.getPreviousClause() != null
                    && clause.getPreviousClause().getHighestExecutionMode(this.visitorConfig).isDataFrame()))
                        ? ExecutionMode.DATAFRAME
                        : ExecutionMode.LOCAL
        );
        clause.setVariableHighestStorageMode(ExecutionMode.LOCAL);

        argument.setVariableStorageMode(
            clause.getVariableName(),
            clause.getVariableHighestStorageMode(this.visitorConfig)
        );

        if (clause.getPositionalVariableName() != null) {
            argument.setVariableStorageMode(
                clause.getPositionalVariableName(),
                ExecutionMode.LOCAL
            );
        }
        return argument;
    }

    @Override
    public StaticContext visitLetClause(LetClause clause, StaticContext argument) {
        this.visit(clause.getExpression(), clause.getExpression().getStaticContext());

        if (clause.getPreviousClause() == null) {
            clause.setHighestExecutionMode(ExecutionMode.LOCAL);
        } else {
            clause.setHighestExecutionMode(
                clause.getPreviousClause().getHighestExecutionMode(this.visitorConfig)
            );
        }

        // if let clause is local, defined variables are stored according to the execution mode of the expression
        if (clause.getHighestExecutionMode(this.visitorConfig) == ExecutionMode.LOCAL) {
            clause.setVariableHighestExecutionMode(clause.getExpression().getHighestExecutionMode(this.visitorConfig));
        } else {
            clause.setVariableHighestExecutionMode(ExecutionMode.LOCAL);
        }

        argument.setVariableStorageMode(
            clause.getVariableName(),
            clause.getVariableHighestStorageMode(this.visitorConfig)
        );

        return argument;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause clause, StaticContext argument) {
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            if (variable.getExpression() != null) {
                // if a variable declaration takes place
                this.visit(variable.getExpression(), null);
                argument.setVariableStorageMode(
                    variable.getVariableName(),
                    ExecutionMode.LOCAL
                );
            }
        }
        clause.setHighestExecutionMode(clause.getPreviousClause().getHighestExecutionMode(this.visitorConfig));
        StaticContext clauseContext = clause.getStaticContext();
        for (Entry<Name, InScopeVariable> entry : argument.getInScopeVariables().entrySet()) {
            boolean isKeyVariable = false;
            for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
                if (variable.getExpression() != null) {
                    if (entry.getKey().equals(variable.getVariableName())) {
                        isKeyVariable = true;
                    }
                }
            }
            if (isKeyVariable) {
                continue;
            }
            argument.setVariableStorageMode(
                entry.getKey(),
                clauseContext.getVariableStorageMode(entry.getKey())
            );
        }
        return argument;
    }

    @Override
    public StaticContext visitCountClause(CountClause expression, StaticContext argument) {
        expression.setHighestExecutionMode(expression.getPreviousClause().getHighestExecutionMode(this.visitorConfig));
        argument.setVariableStorageMode(
            expression.getCountVariableName(),
            ExecutionMode.LOCAL
        );
        return argument;
    }

    // endregion

    // region control
    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        this.visit(expression.getTestCondition(), null);
        for (TypeswitchCase c : expression.getCases()) {
            Name variableName = c.getVariableName();
            if (variableName != null) {
                c.getReturnExpression()
                    .getStaticContext()
                    .setVariableStorageMode(
                        variableName,
                        ExecutionMode.LOCAL
                    );
            }
            this.visit(c.getReturnExpression(), c.getReturnExpression().getStaticContext());
        }

        Name defaultCaseVariableName = expression.getDefaultCase().getVariableName();
        if (defaultCaseVariableName == null) {
            this.visit(expression.getDefaultCase().getReturnExpression(), null);
        } else {
            // add variable to child context to visit default return expression
            expression.getDefaultCase()
                .getReturnExpression()
                .getStaticContext()
                .setVariableStorageMode(
                    defaultCaseVariableName,
                    ExecutionMode.LOCAL
                );
            this.visit(expression.getDefaultCase().getReturnExpression(), null);
        }

        ExecutionMode defaultMode = expression.getDefaultCase()
            .getReturnExpression()
            .getHighestExecutionMode(this.visitorConfig);

        if (defaultMode.isUnset()) {
            expression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }

        for (TypeswitchCase c : expression.getCases()) {
            ExecutionMode mode = c.getReturnExpression().getHighestExecutionMode(this.visitorConfig);
            if (mode.isUnset()) {
                expression.setHighestExecutionMode(ExecutionMode.UNSET);
                return argument;
            }
            if (defaultMode.isRDD() && mode.isLocal()) {
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            }
            if (defaultMode.isDataFrame() && !mode.isDataFrame()) {
                expression.setHighestExecutionMode(mode);
            }
        }

        expression.setHighestExecutionMode(defaultMode);
        return argument;
    }
    // endregion

    @Override
    public StaticContext visitSwitchExpression(SwitchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        ExecutionMode defaultMode = expression.getDefaultExpression().getHighestExecutionMode(this.visitorConfig);

        if (defaultMode.isUnset()) {
            expression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }

        for (SwitchCase c : expression.getCases()) {
            ExecutionMode mode = c.getReturnExpression().getHighestExecutionMode(this.visitorConfig);
            if (mode.isUnset()) {
                expression.setHighestExecutionMode(ExecutionMode.UNSET);
                return argument;
            }
            if (defaultMode.isRDD() && mode.isLocal()) {
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            }
            if (defaultMode.isDataFrame() && !mode.isDataFrame()) {
                expression.setHighestExecutionMode(mode);
            }
        }

        expression.setHighestExecutionMode(defaultMode);
        return argument;
    }

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration variableDeclaration, StaticContext argument) {
        if (variableDeclaration.getExpression() != null) {
            this.visit(variableDeclaration.getExpression(), null);
        }
        variableDeclaration.setHighestExecutionMode(ExecutionMode.LOCAL);
        variableDeclaration.setVariableHighestStorageMode(ExecutionMode.LOCAL);
        // first pass.
        argument.setVariableStorageMode(
            variableDeclaration.getVariableName(),
            variableDeclaration.getVariableHighestStorageMode(this.visitorConfig)
        );
        return argument;
    }

    @Override
    public StaticContext visitTransformExpression(TransformExpression expression, StaticContext argument) {
        StaticContext innerContext = expression.getStaticContext();
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            this.visit(copyDecl.getSourceExpression(), copyDecl.getSourceExpression().getStaticContext());
            // first pass.
            innerContext.setVariableStorageMode(
                copyDecl.getVariableName(),
                ExecutionMode.LOCAL
            );
        }
        expression.setHighestExecutionMode(ExecutionMode.LOCAL);

        this.visit(expression.getModifyExpression(), innerContext);
        this.visit(expression.getReturnExpression(), innerContext);

        return argument;
    }

    @Override
    public StaticContext visitProlog(Prolog prolog, StaticContext argument) {
        visitDescendants(prolog, argument);
        prolog.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }


    @Override
    public StaticContext visitProgram(Program program, StaticContext argument) {
        visitDescendants(program, argument);
        ExecutionMode mergedExecutionMode = program.getStatementsAndOptionalExpr().getHighestExecutionMode();
        for (Statement statement : this.exitStatementChildren) {
            ExecutionMode statementExecMode = statement.getHighestExecutionMode(this.visitorConfig);
            mergedExecutionMode = getHighestExecutionMode(mergedExecutionMode, statementExecMode);
        }
        program.setHighestExecutionMode(mergedExecutionMode);
        return argument;
    }

    @Override
    public StaticContext visitValidateTypeExpression(ValidateTypeExpression expression, StaticContext argument) {
        visitDescendants(expression, null);
        SequenceType targetType = expression.getStaticSequenceType();
        switch (targetType.getArity()) {
            case Zero:
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            case One:
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            case OneOrZero:
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            case OneOrMore:
                if (
                    targetType.getItemType().isObjectItemType()
                        && targetType.getItemType().isCompatibleWithDataFrames(this.configuration)
                ) {
                    LogManager.getLogger("ExecutionModeVisitor")
                        .info(
                            "Validation against "
                                + expression.getSequenceType().getItemType().getName()
                                + " compatible with data frames."
                        );
                    expression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                } else {
                    if (
                        expression.getMainExpression()
                            .getHighestExecutionMode(this.visitorConfig)
                            .equals(ExecutionMode.LOCAL)
                    ) {
                        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                    } else {
                        expression.setHighestExecutionMode(ExecutionMode.RDD);
                    }
                }
                return argument;
            case ZeroOrMore:
                if (
                    targetType.getItemType().isObjectItemType()
                        && targetType.getItemType().isCompatibleWithDataFrames(this.configuration)
                ) {
                    LogManager.getLogger("ExecutionModeVisitor")
                        .info(
                            "Validation against "
                                + expression.getSequenceType().getItemType().getName()
                                + " compatible with data frames."
                        );
                    expression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                } else {
                    if (
                        expression.getMainExpression()
                            .getHighestExecutionMode(this.visitorConfig)
                            .equals(ExecutionMode.LOCAL)
                    ) {
                        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                    } else {
                        expression.setHighestExecutionMode(ExecutionMode.RDD);
                    }
                }
                return argument;
            default:
                return argument;
        }
    }

    @Override
    public StaticContext visitRangeExpr(RangeExpression rangeExpression, StaticContext argument) {
        visitDescendants(rangeExpression, argument);
        Expression left = rangeExpression.getLeftExpression();
        Expression right = rangeExpression.getRightExpression();
        if (
            left instanceof IntegerLiteralExpression
                &&
                right instanceof IntegerLiteralExpression
        ) {
            String leftLiteral = ((IntegerLiteralExpression) left).getLexicalValue();
            String rightLiteral = ((IntegerLiteralExpression) right).getLexicalValue();
            BigInteger leftValue = ItemFactory.getInstance().createIntegerItem(leftLiteral).getIntegerValue();
            BigInteger rightValue = ItemFactory.getInstance().createIntegerItem(rightLiteral).getIntegerValue();
            if (
                rightValue.subtract(leftValue).compareTo(BigInteger.valueOf(RangeOperationIterator.PARTITION_SIZE)) >= 0
            ) {
                rangeExpression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
                return argument;
            }
        }
        rangeExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    @Override
    public StaticContext visitSimpleMapExpr(SimpleMapExpression simpleMapExpression, StaticContext argument) {
        visitDescendants(simpleMapExpression, argument);
        Expression left = simpleMapExpression.getLeftExpression();
        ExecutionMode leftMode = left.getHighestExecutionMode(this.visitorConfig);
        if (leftMode.equals(ExecutionMode.LOCAL)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        SequenceType staticSequenceType = simpleMapExpression.getStaticSequenceType();
        if (staticSequenceType.getArity().equals(Arity.One)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (staticSequenceType.getArity().equals(Arity.OneOrZero)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (staticSequenceType.getArity().equals(Arity.Zero)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (leftMode.equals(ExecutionMode.RDD)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        ItemType itemType = staticSequenceType.getItemType();
        if (!itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        if (itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        if (itemType.equals(BuiltinTypesCatalogue.numericItem)) {
            simpleMapExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        simpleMapExpression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
        return argument;
    }

    @Override
    public StaticContext visitConditionalExpression(
            ConditionalExpression conditionalExpression,
            StaticContext argument
    ) {
        visitDescendants(conditionalExpression, argument);
        if (conditionalExpression.getBranch().getHighestExecutionMode(this.visitorConfig).isLocal()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (conditionalExpression.getElseBranch().getHighestExecutionMode(this.visitorConfig).isLocal()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (conditionalExpression.getBranch().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        if (conditionalExpression.getElseBranch().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        if (conditionalExpression.getBranch().getHighestExecutionMode(this.visitorConfig).isRDD()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        if (conditionalExpression.getElseBranch().getHighestExecutionMode(this.visitorConfig).isRDD()) {
            conditionalExpression.setHighestExecutionMode(ExecutionMode.RDD);
            return argument;
        }
        conditionalExpression.setHighestExecutionMode(DATAFRAMEifConfigurationAllows());
        return argument;
    }

    @Override
    public StaticContext visitCommaExpression(CommaExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.getExpressions().isEmpty()) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }

        for (Expression e : expression.getExpressions()) {
            if (!e.getHighestExecutionMode(this.visitorConfig).isRDDOrDataFrame()) {
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            }
        }

        expression.setHighestExecutionMode(ExecutionMode.RDD);
        return argument;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause clause, StaticContext argument) {
        for (OrderByClauseSortingKey s : clause.getSortingKeys()) {
            visit(s.getExpression(), argument);
        }
        clause.setHighestExecutionMode(
            clause.getPreviousClause().getHighestExecutionMode(this.visitorConfig)
        );
        return argument;
    }

    @Override
    public StaticContext visitWhereClause(WhereClause clause, StaticContext argument) {
        visit(clause.getWhereExpression(), argument);
        clause.setHighestExecutionMode(
            clause.getPreviousClause().getHighestExecutionMode(this.visitorConfig)
        );
        return argument;
    }

    @Override
    public StaticContext visitArrayUnboxingExpression(ArrayUnboxingExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setHighestExecutionMode(expression.getMainExpression().getHighestExecutionMode(this.visitorConfig));
        return argument;
    }

    @Override
    public StaticContext visitArrayLookupExpression(ArrayLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setHighestExecutionMode(expression.getMainExpression().getHighestExecutionMode(this.visitorConfig));
        return argument;
    }

    @Override
    public StaticContext visitObjectLookupExpression(ObjectLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setHighestExecutionMode(expression.getMainExpression().getHighestExecutionMode(this.visitorConfig));
        return argument;
    }

    @Override
    public StaticContext visitXQueryLookupExpression(XQueryLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setHighestExecutionMode(expression.getMainExpression().getHighestExecutionMode(this.visitorConfig));
        return argument;
    }

    @Override
    public StaticContext visitFilterExpression(FilterExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        if (expression.getPredicateExpression() instanceof IntegerLiteralExpression) {
            String lexicalValue = ((IntegerLiteralExpression) expression.getPredicateExpression()).getLexicalValue();
            if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                return argument;
            }
        }


        // START eq optimization
        if (
            expression.getPredicateExpression() instanceof ComparisonExpression
                && ((ComparisonExpression) expression.getPredicateExpression()).getComparisonOperator()
                    .toString()
                    .equals("eq")
        ) {
            Node left = expression.getPredicateExpression().getChildren().get(0);
            Node right = expression.getPredicateExpression().getChildren().get(1);

            Node intLiteral = null;
            if (
                left instanceof FunctionCallExpression
                    && ((FunctionCallExpression) left).getFunctionName().getLocalName().equals("position")
            ) {
                if (right instanceof IntegerLiteralExpression) {
                    intLiteral = right;
                }
            }
            if (
                right instanceof FunctionCallExpression
                    && ((FunctionCallExpression) right).getFunctionName().getLocalName().equals("position")
            ) {
                if (left instanceof IntegerLiteralExpression) {
                    intLiteral = left;
                }
            }
            if (intLiteral != null) {
                String lexicalValue = ((IntegerLiteralExpression) intLiteral).getLexicalValue();
                if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                    expression.setHighestExecutionMode(ExecutionMode.LOCAL);
                    return argument;
                }
            }
        }
        // END eq optimization
        expression.setHighestExecutionMode(expression.getMainExpression().getHighestExecutionMode(this.visitorConfig));
        if (!expression.getStaticContext().getRumbleConfiguration().getNativeSQLPredicates()) {
            if (expression.getHighestExecutionMode().equals(ExecutionMode.DATAFRAME)) {
                expression.setHighestExecutionMode(ExecutionMode.RDD);
            }
        }
        return argument;
    }

    @Override
    public StaticContext visitDynamicFunctionCallExpression(
            DynamicFunctionCallExpression expression,
            StaticContext argument
    ) {
        visitDescendants(expression, argument);
        if (expression.getArguments().size() == 0) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (expression.getStaticSequenceType().getArity().equals(Arity.One)) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        expression.setHighestExecutionMode(
            expression.getArguments().get(0).getHighestExecutionMode(this.visitorConfig)
        );
        if (expression.getHighestExecutionMode().equals(ExecutionMode.RDD)) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        }
        if (!this.configuration.getDataFrameExecutionModeDetection()) {
            expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        }
        return argument;
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType sequenceType = expression.getSequenceType();
        if (
            !sequenceType.isEmptySequence()
                && sequenceType.getArity() != SequenceType.Arity.One
                && sequenceType.getArity() != SequenceType.Arity.OneOrZero
        ) {
            expression.setHighestExecutionMode(
                expression.getMainExpression().getHighestExecutionMode(this.visitorConfig)
            );
            return argument;
        }
        expression.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    @Override
    public StaticContext visitReturnStatementClause(ReturnStatementClause statement, StaticContext argument) {
        visit(statement.getReturnStatement(), statement.getReturnStatement().getStaticContext());
        if (statement.getReturnStatement().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            statement.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        if (statement.getPreviousClause().getHighestExecutionMode(this.visitorConfig).isUnset()) {
            statement.setHighestExecutionMode(ExecutionMode.UNSET);
            return argument;
        }
        statement.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    // region FLWOR
    @Override
    public StaticContext visitFlowrStatement(FlowrStatement statement, StaticContext argument) {
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        while (clause != null) {
            if (clause.getNextClause() != null) {
                this.visit(clause, clause.getNextClause().getStaticContext());
            } else {
                this.visit(clause, null);
            }
            clause = clause.getNextClause();
        }
        statement.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            StaticContext argument
    ) {
        visitDescendants(statementsAndOptionalExpr, statementsAndOptionalExpr.getStaticContext());
        if (statementsAndOptionalExpr.getStatements().isEmpty() && statementsAndOptionalExpr.getExpression() == null) {
            statementsAndOptionalExpr.setHighestExecutionMode(ExecutionMode.LOCAL);
            return argument;
        }
        if (statementsAndOptionalExpr.getStatements().isEmpty()) {
            statementsAndOptionalExpr.setHighestExecutionMode(
                statementsAndOptionalExpr.getExpression().getHighestExecutionMode()
            );
            return argument;
        }
        ExecutionMode statementsExecMode = getHighestExecutionModeFromStatements(
            statementsAndOptionalExpr.getStatements()
        );
        if (statementsAndOptionalExpr.getExpression() != null) {
            ExecutionMode exprExecutionMode = statementsAndOptionalExpr.getExpression()
                .getHighestExecutionMode(this.visitorConfig);
            ExecutionMode mergedExecutionMode = getHighestExecutionMode(statementsExecMode, exprExecutionMode);
            statementsAndOptionalExpr.setHighestExecutionMode(mergedExecutionMode);
        } else {
            statementsAndOptionalExpr.setHighestExecutionMode(statementsExecMode);
        }
        return argument;
    }

    @Override
    public StaticContext visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, StaticContext argument) {
        visitDescendants(statementsAndExpr, statementsAndExpr.getStaticContext());
        if (statementsAndExpr.getStatements().isEmpty()) {
            statementsAndExpr.setHighestExecutionMode(
                statementsAndExpr.getExpression().getHighestExecutionMode(this.visitorConfig)
            );
            return argument;
        }
        ExecutionMode statementsExecMode = getHighestExecutionModeFromStatements(statementsAndExpr.getStatements());
        ExecutionMode exprExecutionMode = statementsAndExpr.getExpression().getHighestExecutionMode(this.visitorConfig);
        ExecutionMode mergedExecutionMode = getHighestExecutionMode(statementsExecMode, exprExecutionMode);

        statementsAndExpr.setHighestExecutionMode(mergedExecutionMode);
        return argument;
    }

    @Override
    public StaticContext visitSlashExpr(SlashExpr slashExpr, StaticContext argument) {
        visitDescendants(slashExpr, argument);
        slashExpr.setHighestExecutionMode(slashExpr.getLeftExpression().getHighestExecutionMode());
        return argument;
    }

    @Override
    public StaticContext visitExitStatement(ExitStatement exitStatement, StaticContext argument) {
        visit(exitStatement.getExitExpression(), argument);
        exitStatement.setHighestExecutionMode(exitStatement.getExitExpression().getHighestExecutionMode());
        this.exitStatementChildren.add(exitStatement);
        return argument;
    }

    private ExecutionMode getHighestExecutionMode(ExecutionMode firstExecMode, ExecutionMode secondExecMode) {
        if (firstExecMode == ExecutionMode.UNSET || secondExecMode == ExecutionMode.UNSET) {
            return ExecutionMode.UNSET;
        }
        if (firstExecMode.isRDD() && secondExecMode.isLocal()) {
            return ExecutionMode.LOCAL;
        }
        if (firstExecMode.isDataFrame() && !secondExecMode.isDataFrame()) {
            return secondExecMode;
        }
        return firstExecMode;
    }

    private ExecutionMode getHighestExecutionModeFromStatements(List<Statement> statements) {
        ExecutionMode result = ExecutionMode.UNSET;
        for (Statement statement : statements) {
            ExecutionMode statementExecMode = statement.getHighestExecutionMode(this.visitorConfig);
            if (statementExecMode.isUnset()) {
                return ExecutionMode.UNSET;
            }
            if (result.isUnset()) {
                result = statementExecMode;
            } else if (result.isRDD() && statementExecMode.isLocal()) {
                return ExecutionMode.LOCAL;
            } else if (result.isDataFrame() && !statementExecMode.isDataFrame()) {
                result = statementExecMode;
            }
        }
        return result;
    }
}
