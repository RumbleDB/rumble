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

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
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
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.AttributeNodeContentExpression;
import org.rumbledb.expressions.primary.AttributeNodeExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ComputedElementConstructorExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DirElemConstructorExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.TextNodeConstructorExpression;
import org.rumbledb.expressions.primary.TextNodeExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.*;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;
import org.rumbledb.runtime.arithmetics.MultiplicativeOperationIterator;
import org.rumbledb.runtime.arithmetics.UnaryOperationIterator;
import org.rumbledb.runtime.control.AtMostOneItemIfRuntimeIterator;
import org.rumbledb.runtime.control.IfRuntimeIterator;
import org.rumbledb.runtime.control.SwitchRuntimeIterator;
import org.rumbledb.runtime.control.TryCatchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIteratorCase;
import org.rumbledb.runtime.flwor.clauses.CountClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.ForClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.GroupByClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.LetClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.ReturnClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.WhereClauseSparkIterator;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.flwor.expression.SimpleMapExpressionIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.functions.FunctionRuntimeIterator;
import org.rumbledb.runtime.functions.NamedFunctionRefRuntimeIterator;
import org.rumbledb.runtime.functions.StaticUserDefinedFunctionCallIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;
import org.rumbledb.runtime.logics.AndOperationIterator;
import org.rumbledb.runtime.logics.NotOperationIterator;
import org.rumbledb.runtime.logics.OrOperationIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.misc.RangeOperationIterator;
import org.rumbledb.runtime.misc.StringConcatIterator;
import org.rumbledb.runtime.navigation.ArrayLookupIterator;
import org.rumbledb.runtime.navigation.ArrayUnboxingIterator;
import org.rumbledb.runtime.navigation.ObjectLookupIterator;
import org.rumbledb.runtime.navigation.PredicateIterator;
import org.rumbledb.runtime.navigation.SequenceLookupIterator;
import org.rumbledb.runtime.primary.ArrayRuntimeIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;
import org.rumbledb.runtime.primary.ComputedElementConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.ContextExpressionIterator;
import org.rumbledb.runtime.primary.DecimalRuntimeIterator;
import org.rumbledb.runtime.primary.DirElemConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.DoubleRuntimeIterator;
import org.rumbledb.runtime.primary.IntegerRuntimeIterator;
import org.rumbledb.runtime.primary.NullRuntimeIterator;
import org.rumbledb.runtime.primary.ObjectConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
import org.rumbledb.runtime.primary.TextNodeConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.runtime.scripting.ProgramIterator;
import org.rumbledb.runtime.scripting.block.StatementsOnlyIterator;
import org.rumbledb.runtime.scripting.block.StatementsWithExprIterator;
import org.rumbledb.runtime.scripting.control.ConditionalStatementIterator;
import org.rumbledb.runtime.scripting.control.SwitchStatementIterator;
import org.rumbledb.runtime.scripting.control.TryCatchStatementIterator;
import org.rumbledb.runtime.scripting.control.TypeSwitchStatementIterator;
import org.rumbledb.runtime.scripting.declaration.CommaVariableDeclStatementIterator;
import org.rumbledb.runtime.scripting.declaration.VariableDeclStatementIterator;
import org.rumbledb.runtime.scripting.flwor.ReturnStatementClauseIterator;
import org.rumbledb.runtime.scripting.loops.BreakStatementIterator;
import org.rumbledb.runtime.scripting.loops.ContinueStatementIterator;
import org.rumbledb.runtime.scripting.loops.ExitStatementIterator;
import org.rumbledb.runtime.scripting.loops.WhileStatementIterator;
import org.rumbledb.runtime.scripting.mutation.ApplyStatementIterator;
import org.rumbledb.runtime.scripting.mutation.AssignStatementIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.CastableIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.runtime.update.expression.AppendExpressionIterator;
import org.rumbledb.runtime.update.expression.DeleteExpressionIterator;
import org.rumbledb.runtime.update.expression.InsertExpressionIterator;
import org.rumbledb.runtime.update.expression.RenameExpressionIterator;
import org.rumbledb.runtime.update.expression.ReplaceExpressionIterator;
import org.rumbledb.runtime.update.expression.TransformExpressionIterator;
import org.rumbledb.runtime.xml.SlashExprIterator;
import org.rumbledb.runtime.xml.StepExprIterator;
import org.rumbledb.runtime.xml.TextNodeRuntimeIterator;
import org.rumbledb.runtime.xml.AttributeNodeContentRuntimeIterator;
import org.rumbledb.runtime.xml.AttributeNodeRuntimeIterator;
import org.rumbledb.runtime.xml.PostfixLookupIterator;
import org.rumbledb.runtime.xml.UnaryLookupIterator;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuntimeIteratorVisitor extends AbstractNodeVisitor<RuntimeIterator> {

    private VisitorConfig visitorConfig;
    private RumbleRuntimeConfiguration config;

    public RuntimeIteratorVisitor(RumbleRuntimeConfiguration config) {
        this.visitorConfig = VisitorConfig.runtimeIteratorVisitorConfig;
        this.config = config;
    }

    @Override
    public RuntimeIterator visit(Node node, RuntimeIterator argument) {
        return node.accept(this, argument);
    }

    @Override
    public RuntimeIterator visitDescendants(Node node, RuntimeIterator argument) {
        RuntimeIterator result = argument;
        for (Node child : node.getChildren()) {
            result = visit(child, argument);
        }
        return result;
    }

    @Override
    public RuntimeIterator visitProlog(Prolog expression, RuntimeIterator argument) {
        return argument;
    }

    @Override
    public RuntimeIterator visitCommaExpression(CommaExpression expression, RuntimeIterator argument) {
        List<RuntimeIterator> result = new ArrayList<>();
        for (Expression childExpr : expression.getExpressions()) {
            result.add(this.visit(childExpr, argument));
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            RuntimeIterator runtimeIterator = new CommaExpressionIterator(
                    result,
                    expression.isUpdating(),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator.setStaticContext(expression.getStaticContext());
            return runtimeIterator;
        }
    }

    // region module
    @Override
    public RuntimeIterator visitMainModule(MainModule expression, RuntimeIterator argument) {
        return super.visitMainModule(expression, argument);
    }
    // endregion

    // region FLOWR
    @Override
    public RuntimeIterator visitFlowrExpression(FlworExpression expression, RuntimeIterator argument) {
        RuntimeTupleIterator previous = this.visitFlowrClause(
            expression.getReturnClause().getPreviousClause(),
            argument
        );
        ReturnClause returnClause = expression.getReturnClause();
        RuntimeIterator runtimeIterator = new ReturnClauseSparkIterator(
                previous,
                this.visit(
                    returnClause.getReturnExpr(),
                    argument
                ),
                expression.isUpdating(),
                new RuntimeStaticContext(
                        this.config,
                        expression.getStaticSequenceType(),
                        returnClause.getHighestExecutionMode(this.visitorConfig),
                        returnClause.getMetadata()
                )
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    private RuntimeTupleIterator visitFlowrClause(
            Clause clause,
            RuntimeIterator argument
    ) {
        RuntimeTupleIterator previousIterator = null;
        if (clause.getPreviousClause() != null) {
            previousIterator = this.visitFlowrClause(clause.getPreviousClause(), argument);
        }
        if (clause instanceof ForClause) {
            ForClause forClause = (ForClause) clause;
            RuntimeIterator assignmentIterator = this.visit(forClause.getExpression(), argument);
            return new ForClauseSparkIterator(
                    previousIterator,
                    forClause.getVariableName(),
                    forClause.getPositionalVariableName(),
                    forClause.isAllowEmpty(),
                    assignmentIterator,
                    forClause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof LetClause) {
            LetClause letClause = (LetClause) clause;
            RuntimeIterator assignmentIterator = this.visit(letClause.getExpression(), argument);
            return new LetClauseSparkIterator(
                    previousIterator,
                    letClause.getVariableName(),
                    letClause.getActualSequenceType(),
                    assignmentIterator,
                    letClause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof GroupByClause) {
            List<GroupByClauseSparkIteratorExpression> groupingExpressions = new ArrayList<>();
            for (GroupByVariableDeclaration var : ((GroupByClause) clause).getGroupVariables()) {
                Expression groupByExpression = var.getExpression();
                RuntimeIterator groupByExpressionIterator = null;
                if (groupByExpression != null) {
                    groupByExpressionIterator = this.visit(groupByExpression, argument);
                }

                Name variableName = var.getVariableName();

                groupingExpressions.add(
                    new GroupByClauseSparkIteratorExpression(
                            groupByExpressionIterator,
                            variableName,
                            clause.getMetadata()
                    )
                );
            }
            return new GroupByClauseSparkIterator(
                    previousIterator,
                    groupingExpressions,
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof OrderByClause) {
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator = new ArrayList<>();
            for (OrderByClauseSortingKey orderExpr : ((OrderByClause) clause).getSortingKeys()) {
                OrderByClauseSortingKey.EMPTY_ORDER emptyOrder = orderExpr.getEmptyOrder();
                if (emptyOrder == OrderByClauseSortingKey.EMPTY_ORDER.NONE) {
                    if (clause.getStaticContext().isEmptySequenceOrderLeast()) {
                        emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.LEAST;
                    } else {
                        emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.GREATEST;
                    }
                }
                expressionsWithIterator.add(
                    new OrderByClauseAnnotatedChildIterator(
                            this.visit(orderExpr.getExpression(), argument),
                            orderExpr.isAscending(),
                            orderExpr.getUri(),
                            emptyOrder
                    )
                );
            }
            return new OrderByClauseSparkIterator(
                    previousIterator,
                    expressionsWithIterator,
                    ((OrderByClause) clause).isStable(),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof WhereClause) {
            return new WhereClauseSparkIterator(
                    previousIterator,
                    this.visit(((WhereClause) clause).getWhereExpression(), argument),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof CountClause) {
            return new CountClauseSparkIterator(
                    previousIterator,
                    ((CountClause) clause).getCountVariableName(),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        throw new OurBadException("Clause unrecognized.");
    }

    @Override
    public RuntimeIterator visitVariableReference(VariableReferenceExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new VariableReferenceIterator(
                expression.getVariableName(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }
    // endregion

    // region updating

    @Override
    public RuntimeIterator visitDeleteExpression(DeleteExpression expression, RuntimeIterator argument) {

        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLocatorExpression(), argument);

        RuntimeIterator runtimeIterator = new DeleteExpressionIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitRenameExpression(RenameExpression expression, RuntimeIterator argument) {

        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLocatorExpression(), argument);
        RuntimeIterator nameIterator = this.visit(expression.getNameExpression(), argument);

        RuntimeIterator runtimeIterator = new RenameExpressionIterator(
                mainIterator,
                lookupIterator,
                nameIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitReplaceExpression(ReplaceExpression expression, RuntimeIterator argument) {

        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLocatorExpression(), argument);
        RuntimeIterator replacerIterator = this.visit(expression.getReplacerExpression(), argument);

        RuntimeIterator runtimeIterator = new ReplaceExpressionIterator(
                mainIterator,
                lookupIterator,
                replacerIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitInsertExpression(InsertExpression expression, RuntimeIterator argument) {

        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator toInsertIterator = this.visit(expression.getToInsertExpression(), argument);
        RuntimeIterator positionIterator = expression.hasPositionExpression()
            ? this.visit(expression.getPositionExpression(), argument)
            : null;

        RuntimeIterator runtimeIterator = new InsertExpressionIterator(
                mainIterator,
                toInsertIterator,
                positionIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitAppendExpression(AppendExpression expression, RuntimeIterator argument) {

        RuntimeIterator arrayIterator = this.visit(expression.getArrayExpression(), argument);
        RuntimeIterator toAppendIterator = this.visit(expression.getToAppendExpression(), argument);

        RuntimeIterator runtimeIterator = new AppendExpressionIterator(
                arrayIterator,
                toAppendIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitTransformExpression(TransformExpression expression, RuntimeIterator argument) {

        List<RuntimeIterator> copyDeclIterators = new ArrayList<>();
        Map<Name, RuntimeIterator> copyDeclMap = new HashMap<>();
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            copyDeclMap.put(copyDecl.getVariableName(), this.visit(copyDecl.getSourceExpression(), argument));
        }
        RuntimeIterator modifyIterator = this.visit(expression.getModifyExpression(), argument);
        RuntimeIterator returnIterator = this.visit(expression.getReturnExpression(), argument);

        RuntimeIterator runtimeIterator = new TransformExpressionIterator(
                copyDeclMap,
                modifyIterator,
                returnIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                expression.getMutabilityLevel()
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());

        return runtimeIterator;
    }


    // endregion

    // region primary
    @Override
    public RuntimeIterator visitFilterExpression(FilterExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        Expression predicateExpression = expression.getPredicateExpression();

        // if we have a int in the predicate we can optimize to a SequenceLookupIterator
        if (predicateExpression instanceof IntegerLiteralExpression) {
            String lexicalValue = ((IntegerLiteralExpression) predicateExpression).getLexicalValue();
            if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                int n = ItemFactory.getInstance().createIntegerItem(lexicalValue).getIntValue();
                return getSequenceLookupIterator(expression, mainIterator, n);
            }
        }

        if (predicateExpression instanceof DecimalLiteralExpression) {
            if (((DecimalLiteralExpression) predicateExpression).isIntValue()) {
                int n = ((DecimalLiteralExpression) predicateExpression).getValue().intValue();
                return getSequenceLookupIterator(expression, mainIterator, n);
            }

            // if decimal has digits to the right of the decimal point, return empty sequence according to spec
            if (((DecimalLiteralExpression) predicateExpression).getValue().stripTrailingZeros().scale() > 0) {
                return new EmptySequenceIterator(
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                );
            }
        }

        if (
            predicateExpression instanceof ComparisonExpression
                && ((ComparisonExpression) predicateExpression).getComparisonOperator()
                    .toString()
                    .equals("eq")
        ) {
            Node left = predicateExpression.getChildren().get(0);
            Node right = predicateExpression.getChildren().get(1);

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
                    int n = ItemFactory.getInstance().createIntegerItem(lexicalValue).getIntValue();
                    return getSequenceLookupIterator(expression, mainIterator, n);
                }
            }
        }

        // fallback for alll other cases
        RuntimeIterator filterIterator = this.visit(predicateExpression, argument);
        RuntimeIterator runtimeIterator = new PredicateIterator(
                mainIterator,
                filterIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    private RuntimeIterator getSequenceLookupIterator(
            FilterExpression expression,
            RuntimeIterator mainIterator,
            int n
    ) {
        RuntimeIterator iterator = new SequenceLookupIterator(
                mainIterator,
                n,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        iterator.setStaticContext(expression.getStaticContext());
        return iterator;
    }

    @Override
    public RuntimeIterator visitArrayLookupExpression(ArrayLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        RuntimeIterator runtimeIterator = new ArrayLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitObjectLookupExpression(ObjectLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        RuntimeIterator runtimeIterator = new ObjectLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitPostfixLookupExpression(PostfixLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        Expression lookup = expression.getLookupExpression(); // null if wildcard
        RuntimeIterator lookupIterator = (lookup == null)
            ? null
            : this.visit(expression.getLookupExpression(), argument);
        RuntimeIterator runtimeIterator = new PostfixLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitUnaryLookupExpression(UnaryLookupExpression expression, RuntimeIterator argument) {
        Expression lookup = expression.getLookupExpression(); // null if wildcard
        RuntimeIterator lookupIterator = (lookup == null)
            ? null
            : this.visit(expression.getLookupExpression(), argument);
        RuntimeIterator runtimeIterator = new UnaryLookupIterator(
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitDynamicFunctionCallExpression(
            DynamicFunctionCallExpression expression,
            RuntimeIterator argument
    ) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        List<RuntimeIterator> arguments = new ArrayList<>();
        for (Expression arg : expression.getArguments()) {
            if (arg == null) { // check ArgumentPlaceholder
                arguments.add(null);
            } else {
                arguments.add(this.visit(arg, argument));
            }
        }
        RuntimeIterator runtimeIterator = new DynamicFunctionCallIterator(
                mainIterator,
                arguments,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitArrayUnboxingExpression(ArrayUnboxingExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new ArrayUnboxingIterator(
                mainIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitArrayConstructor(ArrayConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator result = null;
        if (expression.getExpression() != null) {
            result = this.visit(expression.getExpression(), argument);
        }
        RuntimeIterator runtimeIterator = new ArrayRuntimeIterator(
                result,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitObjectConstructor(ObjectConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator;
        if (expression.isMergedConstructor()) {
            runtimeIterator = new ObjectConstructorRuntimeIterator(
                    expression.getChildren()
                        .stream()
                        .map(arg -> this.visit(arg, argument))
                        .collect(Collectors.toList()),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator.setStaticContext(expression.getStaticContext());
            return runtimeIterator;
        } else {
            List<RuntimeIterator> keys = expression.getKeys()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            List<RuntimeIterator> values = expression.getValues()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            runtimeIterator = new ObjectConstructorRuntimeIterator(
                    keys,
                    values,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator.setStaticContext(expression.getStaticContext());
            return runtimeIterator;
        }
    }

    @Override
    public RuntimeIterator visitDirElemConstructor(DirElemConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new DirElemConstructorRuntimeIterator(
                expression.getTagName(),
                expression.getContent()
                    .stream()
                    .map(arg -> this.visit(arg, argument))
                    .collect(Collectors.toList()),
                expression.getAttributes()
                    .stream()
                    .map(arg -> this.visit(arg, argument))
                    .collect(Collectors.toList()),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitComputedElementConstructor(ComputedElementConstructorExpression expression, RuntimeIterator argument) {
        Expression contentExpression = expression.getContentExpression();
        RuntimeIterator contentIterator = contentExpression != null ?
            this.visit(contentExpression, argument) : null;

        RuntimeIterator runtimeIterator;
        if (expression.hasStaticName()) {
            // Static element name: element elementName { content }
            runtimeIterator = new ComputedElementConstructorRuntimeIterator(
                    expression.getElementName().toString(),
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            // Dynamic element name: element { nameExpression } { content }
            RuntimeIterator nameIterator = this.visit(expression.getNameExpression(), argument);
            runtimeIterator = new ComputedElementConstructorRuntimeIterator(
                    nameIterator,
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitTextNodeConstructor(TextNodeConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator contentIterator = visit(expression.getContentExpression(), argument);
        
        TextNodeConstructorRuntimeIterator result = new TextNodeConstructorRuntimeIterator(
            new AtomizationIterator(Collections.singletonList(contentIterator), expression.getStaticContextForRuntime(this.config, this.visitorConfig)),
            expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public RuntimeIterator visitTextNode(TextNodeExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new TextNodeRuntimeIterator(
                expression.getContent(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitAttributeNode(AttributeNodeExpression expression, RuntimeIterator argument) {
        List<AtomizationIterator> atomizedValues = expression.getValue()
                .stream()
                .map(arg -> new AtomizationIterator(
                        Collections.singletonList(this.visit(arg, argument)),
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                ))
                .collect(Collectors.toList());
        
        RuntimeIterator runtimeIterator = new AttributeNodeRuntimeIterator(
                expression.getQName(),
                atomizedValues,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitAttributeNodeContent(
            AttributeNodeContentExpression expression,
            RuntimeIterator argument
    ) {
        RuntimeIterator runtimeIterator = new AttributeNodeContentRuntimeIterator(
                expression.getContent(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitContextExpr(ContextItemExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new ContextExpressionIterator(
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitInlineFunctionExpr(InlineFunctionExpression expression, RuntimeIterator argument) {
        Map<Name, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>();
        for (Map.Entry<Name, SequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue());
        }
        SequenceType returnType = expression.getReturnType();
        RuntimeIterator bodyIterator = this.visit(expression.getBody(), argument);
        RuntimeIterator runtimeIterator = new FunctionRuntimeIterator(
                expression.getName(),
                paramNameToSequenceTypes,
                returnType,
                bodyIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                expression.isUpdating()
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitFunctionCall(FunctionCallExpression expression, RuntimeIterator argument) {
        List<RuntimeIterator> arguments = new ArrayList<>();
        ExceptionMetadata iteratorMetadata = expression.getMetadata();
        for (Expression arg : expression.getArguments()) {
            if (arg == null) {
                arguments.add(null);
            } else {
                RuntimeIterator argumentIterator = this.visit(arg, argument);
                arguments.add(argumentIterator);
            }
        }
        Name fnName = expression.getFunctionName();
        int arity = arguments.size();
        FunctionIdentifier identifier = new FunctionIdentifier(fnName, arity);

        RuntimeIterator runtimeIterator = null;
        if (BuiltinFunctionCatalogue.exists(identifier)) {
            runtimeIterator = NamedFunctions.getBuiltInFunctionIterator(
                identifier,
                arguments,
                // Note: passing the static context of the function call expression makes
                // all builtin functions static-context-dependent.
                // This might be worth a more fine-grained adjustment later.
                expression.getStaticContext(),
                this.config,
                expression.getHighestExecutionMode(this.visitorConfig),
                iteratorMetadata
            );
        } else {
            runtimeIterator = new StaticUserDefinedFunctionCallIterator(
                    identifier,
                    arguments,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isUpdating()
            );
        }
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitNamedFunctionRef(
            NamedFunctionReferenceExpression expression,
            RuntimeIterator argument
    ) {
        FunctionIdentifier identifier = expression.getIdentifier();
        if (BuiltinFunctionCatalogue.exists(identifier)) {
            throw new UnsupportedFeatureException(
                    "Higher order functions using builtin functions are not supported.",
                    expression.getMetadata()
            );
        }
        RuntimeIterator runtimeIterator = new NamedFunctionRefRuntimeIterator(
                identifier,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }
    // endregion

    // region literal
    @Override
    public RuntimeIterator visitInteger(IntegerLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new IntegerRuntimeIterator(
                expression.getLexicalValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitString(StringLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new StringRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitDouble(DoubleLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new DoubleRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitDecimal(DecimalLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new DecimalRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitNull(NullLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new NullRuntimeIterator(
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitBoolean(BooleanLiteralExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new BooleanRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }
    // endregion

    // region operational
    @Override
    public RuntimeIterator visitAdditiveExpr(AdditiveExpression expression, RuntimeIterator argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new AdditiveOperationIterator(
                left,
                right,
                expression.isMinus(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitMultiplicativeExpr(MultiplicativeExpression expression, RuntimeIterator argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new MultiplicativeOperationIterator(
                left,
                right,
                expression.getMultiplicativeOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitSimpleMapExpr(SimpleMapExpression expression, RuntimeIterator argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new SimpleMapExpressionIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitAndExpr(AndExpression expression, RuntimeIterator argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new AndOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitOrExpr(OrExpression expression, RuntimeIterator argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new OrOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument) {
        RuntimeIterator runtimeIterator = new NotOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitUnaryExpr(UnaryExpression expression, RuntimeIterator argument) {
        // compute +- final result
        RuntimeIterator runtimeIterator = new UnaryOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.isNegated(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getChildren().get(0), argument);
        RuntimeIterator right = this.visit(expression.getChildren().get(1), argument);
        RuntimeIterator runtimeIterator = new RangeOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getChildren().get(0), argument);
        RuntimeIterator right = this.visit(expression.getChildren().get(1), argument);
        if (left instanceof StepExprIterator) {
            // We potentially need to atomize
            left = new AtomizationIterator(
                    Collections.singletonList(left),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        RuntimeIterator runtimeIterator = new ComparisonIterator(
                left,
                right,
                expression.getComparisonOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitStringConcatExpr(StringConcatExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getChildren().get(0), argument);
        RuntimeIterator right = this.visit(expression.getChildren().get(1), argument);
        RuntimeIterator runtimeIterator = new StringConcatIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new InstanceOfIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitValidateTypeExpression(ValidateTypeExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new ValidateTypeIterator(
                childExpression,
                expression.getSequenceType().getItemType(),
                expression.isValidate(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        RuntimeIterator resultIterator = new TreatIterator(
                runtimeIterator,
                new SequenceType(BuiltinTypesCatalogue.item, expression.getSequenceType().getArity()),
                ErrorCode.InvalidInstance,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        resultIterator.setStaticContext(expression.getStaticContext());
        return resultIterator;
    }

    @Override
    public RuntimeIterator visitTreatExpression(TreatExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new TreatIterator(
                childExpression,
                expression.getSequenceType(),
                expression.isUpdating(),
                expression.errorCodeThatShouldBeThrown(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitCastableExpression(CastableExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new CastableIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitCastExpression(CastExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator runtimeIterator = new CastIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }
    // endregion

    // region control
    @Override
    public RuntimeIterator visitConditionalExpression(ConditionalExpression expression, RuntimeIterator argument) {
        RuntimeIterator conditionIterator = this.visit(expression.getCondition(), argument);
        RuntimeIterator thenIterator = this.visit(expression.getBranch(), argument);
        RuntimeIterator elseIterator = this.visit(expression.getElseBranch(), argument);
        RuntimeIterator runtimeIterator = null;
        if (
            thenIterator instanceof AtMostOneItemLocalRuntimeIterator
                &&
                elseIterator instanceof AtMostOneItemLocalRuntimeIterator
        ) {
            runtimeIterator = new AtMostOneItemIfRuntimeIterator(
                    conditionIterator,
                    thenIterator,
                    elseIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            runtimeIterator = new IfRuntimeIterator(
                    conditionIterator,
                    thenIterator,
                    elseIterator,
                    expression.isUpdating(),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitSwitchExpression(SwitchExpression expression, RuntimeIterator argument) {
        Map<RuntimeIterator, RuntimeIterator> cases = new LinkedHashMap<>();
        for (SwitchCase caseExpression : expression.getCases()) {
            RuntimeIterator caseExpr = this.visit(caseExpression.getReturnExpression(), argument);
            for (Expression conditionExpr : caseExpression.getConditionExpressions()) {
                RuntimeIterator condition = this.visit(conditionExpr, argument);
                cases.put(condition, caseExpr);
            }
        }
        RuntimeIterator runtimeIterator = new SwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                this.visit(expression.getDefaultExpression(), argument),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }
    // endregion

    @Override
    public RuntimeIterator visitTypeSwitchExpression(TypeSwitchExpression expression, RuntimeIterator argument) {
        List<TypeswitchRuntimeIteratorCase> cases = new ArrayList<>();
        for (TypeswitchCase caseExpression : expression.getCases()) {
            cases.add(
                new TypeswitchRuntimeIteratorCase(
                        caseExpression.getVariableName(),
                        caseExpression.getUnion(),
                        this.visit(caseExpression.getReturnExpression(), argument)
                )
            );
        }

        TypeswitchRuntimeIteratorCase defaultCase = new TypeswitchRuntimeIteratorCase(
                expression.getDefaultCase().getVariableName(),
                this.visit(expression.getDefaultCase().getReturnExpression(), argument)
        );

        RuntimeIterator runtimeIterator = new TypeswitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                defaultCase,
                expression.isUpdating(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitTryCatchExpression(TryCatchExpression expression, RuntimeIterator argument) {
        Map<String, RuntimeIterator> cases = new LinkedHashMap<>();
        for (String code : expression.getErrorsCaught()) {
            cases.put(
                code,
                this.visit(expression.getExpressionCatching(code), argument)
            );
        }
        if (expression.getExpressionCatchingAll() == null) {
            return new TryCatchRuntimeIterator(
                    this.visit(expression.getTryExpression(), argument),
                    cases,
                    null,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            return new TryCatchRuntimeIterator(
                    this.visit(expression.getTryExpression(), argument),
                    cases,
                    this.visit(expression.getExpressionCatchingAll(), argument),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
    }

    @Override
    public RuntimeIterator visitWhileStatement(WhileStatement statement, RuntimeIterator argument) {
        RuntimeIterator testConditionIterator = this.visit(statement.getTestCondition(), argument);
        RuntimeIterator statementIterator = this.visit(statement.getStatement(), argument);
        return new WhileStatementIterator(
                testConditionIterator,
                statementIterator,
                statement.isSequential(),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitVariableDeclStatement(VariableDeclStatement statement, RuntimeIterator argument) {
        Name varName = statement.getVariableName();
        List<RuntimeIterator> exprIterator = null;
        if (statement.getVariableExpression() != null) {
            exprIterator = Collections.singletonList(this.visit(statement.getVariableExpression(), argument));
        }
        return new VariableDeclStatementIterator(
                varName,
                exprIterator,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitCommaVariableDeclStatement(
            CommaVariableDeclStatement statement,
            RuntimeIterator argument
    ) {
        List<RuntimeIterator> children = new ArrayList<>();
        for (VariableDeclStatement varDecl : statement.getVariables()) {
            children.add(this.visit(varDecl, argument));
        }
        return new CommaVariableDeclStatementIterator(
                children,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitAssignStatement(AssignStatement statement, RuntimeIterator argument) {
        return new AssignStatementIterator(
                this.visit(statement.getAssignExpression(), argument),
                statement.getName(),
                statement.isSequential(),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitApplyStatement(ApplyStatement statement, RuntimeIterator argument) {
        return new ApplyStatementIterator(
                this.visit(statement.getApplyExpression(), argument),
                statement.isSequential(),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitBreakStatement(BreakStatement statement, RuntimeIterator argument) {
        return new BreakStatementIterator(statement.getStaticContextForRuntime(this.config, this.visitorConfig));
    }

    @Override
    public RuntimeIterator visitContinueStatement(ContinueStatement statement, RuntimeIterator argument) {
        return new ContinueStatementIterator(statement.getStaticContextForRuntime(this.config, this.visitorConfig));
    }

    @Override
    public RuntimeIterator visitExitStatement(ExitStatement statement, RuntimeIterator argument) {
        return new ExitStatementIterator(
                this.visit(statement.getExitExpression(), argument),
                true,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitTryCatchStatement(TryCatchStatement statement, RuntimeIterator argument) {
        Map<String, RuntimeIterator> cases = new LinkedHashMap<>();
        for (String code : statement.getErrorsCaught()) {
            cases.put(
                code,
                this.visit(statement.getBlockStatementCatching(code), argument)
            );
        }
        if (statement.getCatchAllStatement() == null) {
            return new TryCatchStatementIterator(
                    this.visit(statement.getTryStatement(), argument),
                    cases,
                    null,
                    statement.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            return new TryCatchStatementIterator(
                    this.visit(statement.getTryStatement(), argument),
                    cases,
                    this.visit(statement.getCatchAllStatement(), argument),
                    statement.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
    }

    @Override
    public RuntimeIterator visitBlockStatement(BlockStatement statement, RuntimeIterator argument) {
        List<RuntimeIterator> result = new ArrayList<>();
        for (Statement stmt : statement.getBlockStatements()) {
            RuntimeIterator childIterator = this.visit(stmt, argument);
            if (childIterator != null) {
                result.add(childIterator);
            }
        }
        return new StatementsOnlyIterator(
                result,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitSwitchStatement(SwitchStatement statement, RuntimeIterator argument) {
        Map<RuntimeIterator, RuntimeIterator> cases = new LinkedHashMap<>();
        for (SwitchCaseStatement caseExpression : statement.getCases()) {
            RuntimeIterator caseExpr = this.visit(caseExpression.getReturnStatement(), argument);
            for (Expression conditionExpr : caseExpression.getConditionExpressions()) {
                RuntimeIterator condition = this.visit(conditionExpr, argument);
                cases.put(condition, caseExpr);
            }
        }
        RuntimeIterator runtimeIterator = new SwitchStatementIterator(
                this.visit(statement.getTestCondition(), argument),
                cases,
                this.visit(statement.getDefaultStatement(), argument),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(statement.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitTypeSwitchStatement(TypeSwitchStatement statement, RuntimeIterator argument) {
        List<TypeswitchRuntimeIteratorCase> cases = new ArrayList<>();
        for (TypeSwitchStatementCase caseExpression : statement.getCases()) {
            cases.add(
                new TypeswitchRuntimeIteratorCase(
                        caseExpression.getVariableName(),
                        caseExpression.getUnion(),
                        this.visit(caseExpression.getReturnStatement(), argument)
                )
            );
        }

        TypeswitchRuntimeIteratorCase defaultCase = new TypeswitchRuntimeIteratorCase(
                statement.getDefaultCase().getVariableName(),
                this.visit(statement.getDefaultCase().getReturnStatement(), argument)
        );

        RuntimeIterator runtimeIterator = new TypeSwitchStatementIterator(
                this.visit(statement.getTestCondition(), argument),
                cases,
                defaultCase,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(statement.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, RuntimeIterator argument) {
        List<RuntimeIterator> result = new ArrayList<>();
        for (Statement statement : statementsAndExpr.getStatements()) {
            RuntimeIterator childIterator = this.visit(statement, argument);
            if (childIterator != null) {
                result.add(childIterator);
            }
        }
        RuntimeIterator exprIterator = this.visit(statementsAndExpr.getExpression(), argument);
        // if (result.isEmpty()) {
        // return exprIterator;
        // }
        return new StatementsWithExprIterator(
                result,
                exprIterator,
                statementsAndExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            RuntimeIterator argument
    ) {
        List<RuntimeIterator> result = new ArrayList<>();
        RuntimeIterator exprIterator = null;
        for (Statement statement : statementsAndOptionalExpr.getStatements()) {
            RuntimeIterator childIterator = this.visit(statement, argument);
            if (childIterator != null) {
                result.add(childIterator);
            }
        }
        if (statementsAndOptionalExpr.getExpression() != null) {
            exprIterator = this.visit(statementsAndOptionalExpr.getExpression(), argument);
        }
        // if (result.isEmpty()) {
        // return exprIterator;
        // }
        if (exprIterator != null) {
            return new StatementsWithExprIterator(
                    result,
                    exprIterator,
                    statementsAndOptionalExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
            );

        }
        return new StatementsOnlyIterator(
                result,
                statementsAndOptionalExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitProgram(Program program, RuntimeIterator argument) {
        return new ProgramIterator(
                this.visit(program.getStatementsAndOptionalExpr(), argument),
                program.getStatementsAndOptionalExpr().getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public RuntimeIterator visitConditionalStatement(ConditionalStatement statement, RuntimeIterator argument) {
        RuntimeIterator conditionIterator = this.visit(statement.getCondition(), argument);
        RuntimeIterator thenIterator = this.visit(statement.getBranch(), argument);
        RuntimeIterator elseIterator = this.visit(statement.getElseBranch(), argument);
        List<RuntimeIterator> result = new ArrayList<>();
        result.add(conditionIterator);
        result.add(thenIterator);
        result.add(elseIterator);
        RuntimeIterator runtimeIterator = new ConditionalStatementIterator(
                result,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        runtimeIterator.setStaticContext(statement.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitFlowrStatement(FlowrStatement statement, RuntimeIterator argument) {
        RuntimeTupleIterator previous = this.visitFlowrClause(
            statement.getReturnStatementClause().getPreviousClause(),
            argument
        );
        ReturnStatementClause returnClause = statement.getReturnStatementClause();
        RuntimeIterator runtimeIterator = new ReturnStatementClauseIterator(
                previous,
                this.visit(
                    returnClause.getReturnStatement(),
                    argument
                ),
                new RuntimeStaticContext(
                        this.config,
                        statement.getStaticSequenceType(),
                        returnClause.getHighestExecutionMode(this.visitorConfig),
                        returnClause.getMetadata()
                )
        );
        runtimeIterator.setStaticContext(statement.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitSlashExpr(SlashExpr slashExpr, RuntimeIterator argument) {
        Expression leftExpression = (Expression) slashExpr.getChildren().get(0);
        Expression rightExpression = (Expression) slashExpr.getChildren().get(1);
        RuntimeIterator left = this.visit(
            leftExpression,
            argument
        );
        RuntimeIterator right = this.visit(
            rightExpression,
            argument
        );

        RuntimeIterator runtimeIterator = new SlashExprIterator(
                left,
                right,
                slashExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(slashExpr.getStaticContext());
        return runtimeIterator;
    }

    @Override
    public RuntimeIterator visitStepExpr(StepExpr stepExpr, RuntimeIterator argument) {
        AxisIterator axisIterator = this.visitAxisStep(stepExpr, stepExpr.getMetadata());
        NodeTest nodeTest = stepExpr.getNodeTest();
        return new StepExprIterator(
                axisIterator,
                nodeTest,
                new RuntimeStaticContext(
                        this.config,
                        SequenceType.ITEM,
                        stepExpr.getHighestExecutionMode(this.visitorConfig),
                        stepExpr.getMetadata()
                )
        );
    }

    private AxisIterator visitAxisStep(StepExpr stepExpr, ExceptionMetadata metadata) {
        return stepExpr.accept(
            new AxisIteratorVisitor(),
            new RuntimeStaticContext(
                    this.config,
                    SequenceType.STRING,
                    ExecutionMode.LOCAL,
                    metadata
            )
        );
    }

}
