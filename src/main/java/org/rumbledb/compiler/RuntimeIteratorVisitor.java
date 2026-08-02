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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.expressions.control.CatchPattern;
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
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
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
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.TextNodeConstructorExpression;
import org.rumbledb.expressions.xml.TextNodeExpression;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.EmptySequenceIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.TupleRuntimePlan;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;
import org.rumbledb.runtime.arithmetics.MultiplicativeOperationIterator;
import org.rumbledb.runtime.arithmetics.UnaryOperationIterator;
import org.rumbledb.runtime.control.AtMostOneItemIfRuntimeIterator;
import org.rumbledb.runtime.control.IfRuntimeIterator;
import org.rumbledb.runtime.control.SwitchRuntimeIterator;
import org.rumbledb.runtime.control.TryCatchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIteratorCase;
import org.rumbledb.runtime.flwor.clauses.CountClauseIterator;
import org.rumbledb.runtime.flwor.clauses.ForClauseIterator;
import org.rumbledb.runtime.flwor.clauses.GroupByClauseIterator;
import org.rumbledb.runtime.flwor.clauses.LetClauseIterator;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseIterator;
import org.rumbledb.runtime.flwor.clauses.ReturnClauseIterator;
import org.rumbledb.runtime.flwor.clauses.WhereClauseIterator;
import org.rumbledb.runtime.flwor.clauses.WindowClauseIterator;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.flwor.expression.SimpleMapExpressionIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.functions.FunctionRuntimeIterator;
import org.rumbledb.runtime.functions.NamedFunctionRefRuntimeIterator;
import org.rumbledb.runtime.functions.StaticUserDefinedFunctionCallIterator;
import org.rumbledb.runtime.functions.sequences.general.DataFunctionIterator;
import org.rumbledb.runtime.logics.AndOperationIterator;
import org.rumbledb.runtime.logics.NotOperationIterator;
import org.rumbledb.runtime.logics.OrOperationIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.runtime.misc.NodeComparisonRuntimeIterator;
import org.rumbledb.runtime.misc.RangeOperationIterator;
import org.rumbledb.runtime.misc.NodeSetOperationIterator;
import org.rumbledb.runtime.misc.StringConcatIterator;
import org.rumbledb.runtime.navigation.ArrayLookupIterator;
import org.rumbledb.runtime.navigation.ArrayUnboxingIterator;
import org.rumbledb.runtime.navigation.ObjectLookupIterator;
import org.rumbledb.runtime.navigation.PredicateIterator;
import org.rumbledb.runtime.navigation.SequenceLookupIterator;
import org.rumbledb.runtime.primary.ArrayRuntimeIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;
import org.rumbledb.runtime.primary.ContextExpressionIterator;
import org.rumbledb.runtime.primary.DecimalRuntimeIterator;
import org.rumbledb.runtime.primary.DoubleRuntimeIterator;
import org.rumbledb.runtime.primary.IntegerRuntimeIterator;
import org.rumbledb.runtime.primary.MapConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.NullRuntimeIterator;
import org.rumbledb.runtime.primary.ObjectConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
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
import org.rumbledb.runtime.update.expression.CreateCollectionIterator;
import org.rumbledb.runtime.update.expression.DeleteExpressionIterator;
import org.rumbledb.runtime.update.expression.DeleteIndexFromCollectionIterator;
import org.rumbledb.runtime.update.expression.DeleteSearchFromCollectionIterator;
import org.rumbledb.runtime.update.expression.EditCollectionIterator;
import org.rumbledb.runtime.update.expression.InsertExpressionIterator;
import org.rumbledb.runtime.update.expression.InsertIndexIntoCollectionIterator;
import org.rumbledb.runtime.update.expression.InsertSearchIntoCollectionIterator;
import org.rumbledb.runtime.update.expression.RenameExpressionIterator;
import org.rumbledb.runtime.update.expression.ReplaceExpressionIterator;
import org.rumbledb.runtime.update.expression.TransformExpressionIterator;
import org.rumbledb.runtime.update.expression.TruncateCollectionIterator;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.runtime.xml.AttributeNodeContentRuntimeIterator;
import org.rumbledb.runtime.xml.AttributeNodeRuntimeIterator;
import org.rumbledb.runtime.xml.CommentNodeConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.ComputedAttributeConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.ComputedElementConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.ComputedNamespaceConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.ComputedPIConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.DirElemConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.DirPIConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.DocumentNodeConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.DirectCommentConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.PathRootRuntimeIterator;
import org.rumbledb.runtime.xml.PostfixLookupIterator;
import org.rumbledb.runtime.xml.SlashExprIterator;
import org.rumbledb.runtime.xml.StepExprIterator;
import org.rumbledb.runtime.xml.TextNodeConstructorRuntimeIterator;
import org.rumbledb.runtime.xml.TextNodeRuntimeIterator;
import org.rumbledb.runtime.xml.UnaryLookupIterator;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class RuntimeIteratorVisitor extends AbstractNodeVisitor<ItemRuntimePlan> {

    private final VisitorConfig visitorConfig;
    private final RumbleRuntimeConfiguration config;

    public RuntimeIteratorVisitor(RumbleRuntimeConfiguration config) {
        this.visitorConfig = VisitorConfig.runtimeIteratorVisitorConfig;
        this.config = config;
    }

    @Override
    public ItemRuntimePlan visit(Node node, ItemRuntimePlan argument) {
        return node.accept(this, argument);
    }

    @Override
    public ItemRuntimePlan visitDescendants(Node node, ItemRuntimePlan argument) {
        ItemRuntimePlan result = argument;
        for (Node child : node.getChildren()) {
            result = visit(child, argument);
        }
        return result;
    }

    @Override
    public ItemRuntimePlan visitProlog(Prolog expression, ItemRuntimePlan argument) {
        return argument;
    }

    @Override
    public ItemRuntimePlan visitCommaExpression(CommaExpression expression, ItemRuntimePlan argument) {
        List<ItemRuntimePlan> result = new ArrayList<>();
        for (Expression childExpr : expression.getExpressions()) {
            result.add(this.visit(childExpr, argument));
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            ItemRuntimePlan runtimeIterator = new CommaExpressionIterator(
                    result,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            return runtimeIterator;
        }
    }

    // region module

    @Override
    public ItemRuntimePlan visitMainModule(MainModule expression, ItemRuntimePlan argument) {
        return super.visitMainModule(expression, argument);
    }
    // endregion

    // region FLOWR
    @Override
    public ItemRuntimePlan visitFlowrExpression(FlworExpression expression, ItemRuntimePlan argument) {
        TupleRuntimePlan previous = this.visitFlowrClause(
            expression.getReturnClause().getPreviousClause(),
            argument
        );
        ReturnClause returnClause = expression.getReturnClause();
        ItemRuntimePlan runtimeIterator = new ReturnClauseIterator(
                previous,
                (this.visit(
                    returnClause.getReturnExpr(),
                    argument
                )),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                    .toBuilder()
                    .executionMode(returnClause.getHighestExecutionMode(this.visitorConfig))
                    .metadata(returnClause.getMetadata())
                    .build()
        );
        return runtimeIterator;
    }

    private TupleRuntimePlan visitFlowrClause(
            Clause clause,
            ItemRuntimePlan argument
    ) {
        TupleRuntimePlan previousIterator = null;
        if (clause.getPreviousClause() != null) {
            previousIterator = this.visitFlowrClause(clause.getPreviousClause(), argument);
        }
        if (clause instanceof ForClause forClause) {
            ItemRuntimePlan assignmentIterator = (this.visit(forClause.getExpression(), argument));
            return new ForClauseIterator(
                    previousIterator,
                    forClause.getVariableName(),
                    forClause.getPositionalVariableName(),
                    forClause.isAllowEmpty(),
                    assignmentIterator,
                    forClause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof LetClause letClause) {
            ItemRuntimePlan assignmentIterator = (this.visit(letClause.getExpression(), argument));
            return new LetClauseIterator(
                    previousIterator,
                    letClause.getVariableName(),
                    letClause.getStaticType(),
                    assignmentIterator,
                    letClause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof WindowClause windowClause) {
            ItemRuntimePlan sourceIterator = (this.visit(windowClause.getExpression(), argument));
            ItemRuntimePlan startIterator = (this.visit(windowClause.getStartCondition().expression(), argument));
            ItemRuntimePlan endIterator = windowClause.getEndCondition() == null
                ? null
                : (this.visit(windowClause.getEndCondition().expression(), argument));
            return new WindowClauseIterator(
                    previousIterator,
                    windowClause,
                    sourceIterator,
                    startIterator,
                    endIterator,
                    windowClause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof GroupByClause groupByClause) {
            List<GroupByClauseSparkIteratorExpression> groupingExpressions = new ArrayList<>();
            for (GroupByVariableDeclaration var : groupByClause.getGroupVariables()) {
                Expression groupByExpression = var.getExpression();
                ItemRuntimePlan groupByExpressionIterator = null;
                if (groupByExpression != null) {
                    groupByExpressionIterator = (this.visit(groupByExpression, argument));
                }

                Name variableName = var.getVariableName();

                groupingExpressions.add(
                    new GroupByClauseSparkIteratorExpression(
                            groupByExpressionIterator,
                            variableName,
                            var.getCollationURI(),
                            var.getActualSequenceType()
                    )
                );
            }
            return new GroupByClauseIterator(
                    previousIterator,
                    groupingExpressions,
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof OrderByClause orderByClause) {
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator = new ArrayList<>();
            for (OrderByClauseSortingKey orderExpr : orderByClause.getSortingKeys()) {
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
                            (this.visit(orderExpr.getExpression(), argument)),
                            orderExpr.isAscending(),
                            orderExpr.getUri(),
                            emptyOrder
                    )
                );
            }
            return new OrderByClauseIterator(
                    previousIterator,
                    expressionsWithIterator,
                    orderByClause.isStable(),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof WhereClause whereClause) {
            return new WhereClauseIterator(
                    previousIterator,
                    (this.visit(whereClause.getWhereExpression(), argument)),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else if (clause instanceof CountClause countClause) {
            return new CountClauseIterator(
                    previousIterator,
                    countClause.getCountVariableName(),
                    clause.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        throw new OurBadException("Clause unrecognized.");
    }

    @Override
    public ItemRuntimePlan visitVariableReference(
            VariableReferenceExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator = new VariableReferenceIterator(
                expression.getVariableName(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        return runtimeIterator;
    }
    // endregion

    // region updating

    @Override
    public ItemRuntimePlan visitDeleteExpression(DeleteExpression expression, ItemRuntimePlan argument) {

        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan lookupIterator = this.visit(expression.getLocatorExpression(), argument);

        ItemRuntimePlan runtimeIterator = new DeleteExpressionIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitRenameExpression(RenameExpression expression, ItemRuntimePlan argument) {

        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan lookupIterator = this.visit(expression.getLocatorExpression(), argument);
        ItemRuntimePlan nameIterator = this.visit(expression.getNameExpression(), argument);

        ItemRuntimePlan runtimeIterator = new RenameExpressionIterator(
                mainIterator,
                lookupIterator,
                nameIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitReplaceExpression(ReplaceExpression expression, ItemRuntimePlan argument) {

        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan lookupIterator = this.visit(expression.getLocatorExpression(), argument);
        ItemRuntimePlan replacerIterator = this.visit(expression.getReplacerExpression(), argument);

        ItemRuntimePlan runtimeIterator = new ReplaceExpressionIterator(
                mainIterator,
                lookupIterator,
                replacerIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitInsertExpression(InsertExpression expression, ItemRuntimePlan argument) {

        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan toInsertIterator = this.visit(expression.getToInsertExpression(), argument);
        ItemRuntimePlan positionIterator = expression.hasPositionExpression()
            ? this.visit(expression.getPositionExpression(), argument)
            : null;

        ItemRuntimePlan runtimeIterator = new InsertExpressionIterator(
                mainIterator,
                toInsertIterator,
                positionIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitAppendExpression(AppendExpression expression, ItemRuntimePlan argument) {

        ItemRuntimePlan arrayIterator = this.visit(expression.getArrayExpression(), argument);
        ItemRuntimePlan toAppendIterator = this.visit(expression.getToAppendExpression(), argument);

        ItemRuntimePlan runtimeIterator = new AppendExpressionIterator(
                arrayIterator,
                toAppendIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitTransformExpression(TransformExpression expression, ItemRuntimePlan argument) {

        // List<ItemRuntimePlan> copyDeclIterators = new ArrayList<>();
        Map<Name, ItemRuntimePlan> copyDeclMap = new HashMap<>();
        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            copyDeclMap.put(copyDecl.getVariableName(), this.visit(copyDecl.getSourceExpression(), argument));
        }
        ItemRuntimePlan modifyIterator = this.visit(expression.getModifyExpression(), argument);
        ItemRuntimePlan returnIterator = this.visit(expression.getReturnExpression(), argument);

        ItemRuntimePlan runtimeIterator = new TransformExpressionIterator(
                copyDeclMap,
                modifyIterator,
                returnIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                expression.getMutabilityLevel(),
                expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitCreateCollectionExpression(
            CreateCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = this.visit(expression.getContentExpression(), argument);
        ItemRuntimePlan targetIterator = this.visit(expression.getCollection(), argument);
        Mode mode = expression.getMode();

        ItemRuntimePlan runtimeIterator = new CreateCollectionIterator(
                targetIterator,
                contentIterator,
                mode,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDeleteIndexFromCollectionExpression(
            DeleteIndexFromCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan targetIterator = this.visit(expression.getCollection(), argument);
        Mode mode = expression.getMode();
        boolean isFirst = expression.isFirst();

        ItemRuntimePlan runtimeIterator = null;
        if (expression.getNumDelete() != null) {
            ItemRuntimePlan numDelete = this.visit(expression.getNumDelete(), argument);
            runtimeIterator = new DeleteIndexFromCollectionIterator(
                    targetIterator,
                    numDelete,
                    isFirst,
                    mode,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            runtimeIterator = new DeleteIndexFromCollectionIterator(
                    targetIterator,
                    isFirst,
                    mode,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDeleteSearchFromCollectionExpression(
            DeleteSearchFromCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = this.visit(expression.getContentExpression(), argument);
        ItemRuntimePlan runtimeIterator = new DeleteSearchFromCollectionIterator(
                contentIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitEditCollectionExpression(
            EditCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan targetIterator = this.visit(expression.getTargetExpression(), argument);
        ItemRuntimePlan contentIterator = this.visit(expression.getContentExpression(), argument);

        ItemRuntimePlan runtimeIterator = new EditCollectionIterator(
                targetIterator,
                contentIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitInsertIndexIntoCollectionExpression(
            InsertIndexIntoCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = this.visit(expression.getContentExpression(), argument);
        ItemRuntimePlan targetIterator = this.visit(expression.getCollection(), argument);
        Mode mode = expression.getMode();
        boolean isFirst = expression.isFirst();
        boolean isLast = expression.isLast();

        ItemRuntimePlan runtimeIterator = null;
        if (expression.getPosition() != null) {
            ItemRuntimePlan pos = this.visit(expression.getPosition(), argument);
            runtimeIterator = new InsertIndexIntoCollectionIterator(
                    targetIterator,
                    contentIterator,
                    pos,
                    mode,
                    isFirst,
                    isLast,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            runtimeIterator = new InsertIndexIntoCollectionIterator(
                    targetIterator,
                    contentIterator,
                    mode,
                    isFirst,
                    isLast,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitInsertSearchIntoCollectionExpression(
            InsertSearchIntoCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan targetIterator = this.visit(expression.getTargetExpression(), argument);
        ItemRuntimePlan contentIterator = this.visit(expression.getContentExpression(), argument);
        boolean isBefore = expression.isBefore();

        ItemRuntimePlan runtimeIterator = new InsertSearchIntoCollectionIterator(
                targetIterator,
                contentIterator,
                isBefore,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitTruncateCollectionExpression(
            TruncateCollectionExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan targetIterator = this.visit(expression.getCollectionName(), argument);
        Mode mode = expression.getMode();
        ItemRuntimePlan runtimeIterator = new TruncateCollectionIterator(
                targetIterator,
                mode,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    // endregion

    // region primary
    @Override
    public ItemRuntimePlan visitFilterExpression(FilterExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        Expression predicateExpression = expression.getPredicateExpression();

        // if we have a int in the predicate we can optimize to a SequenceLookupIterator
        if (predicateExpression instanceof IntegerLiteralExpression integerLiteral) {
            String lexicalValue = integerLiteral.getLexicalValue();
            if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                int n = ItemFactory.getInstance().createIntegerItem(lexicalValue).getIntValue();
                return getSequenceLookupIterator(expression, mainIterator, n);
            }
        }

        if (predicateExpression instanceof DecimalLiteralExpression decimalLiteral) {
            if (decimalLiteral.isIntValue()) {
                int n = decimalLiteral.getValue().intValue();
                return getSequenceLookupIterator(expression, mainIterator, n);
            }

            // if decimal has digits to the right of the decimal point, return empty sequence according to spec
            if (decimalLiteral.getValue().stripTrailingZeros().scale() > 0) {
                return new EmptySequenceIterator(
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                );
            }
        }

        if (
            predicateExpression instanceof ComparisonExpression comparisonExpression
                && comparisonExpression.getComparisonOperator()
                    .toString()
                    .equals("eq")
        ) {
            Node left = comparisonExpression.getChildren().get(0);
            Node right = comparisonExpression.getChildren().get(1);

            Node intLiteral = null;
            if (
                left instanceof FunctionCallExpression functionCall
                    && functionCall.getFunctionName().getLocalName().equals("position")
            ) {
                if (right instanceof IntegerLiteralExpression) {
                    intLiteral = right;
                }
            }
            if (
                right instanceof FunctionCallExpression functionCall
                    && functionCall.getFunctionName().getLocalName().equals("position")
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
        ItemRuntimePlan filterIterator = (this.visit(predicateExpression, argument));
        ItemRuntimePlan runtimeIterator = new PredicateIterator(
                (mainIterator),
                filterIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    private ItemRuntimePlan getSequenceLookupIterator(
            FilterExpression expression,
            ItemRuntimePlan mainIterator,
            int n
    ) {
        ItemRuntimePlan iterator = new SequenceLookupIterator(
                mainIterator,
                n,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return iterator;
    }

    @Override
    public ItemRuntimePlan visitArrayLookupExpression(ArrayLookupExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan mainIterator = (this.visit(expression.getMainExpression(), argument));
        ItemRuntimePlan lookupIterator = (this.visit(expression.getLookupExpression(), argument));
        ItemRuntimePlan runtimeIterator = new ArrayLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitObjectLookupExpression(
            ObjectLookupExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan mainIterator = (this.visit(expression.getMainExpression(), argument));
        ItemRuntimePlan lookupIterator = (this.visit(expression.getLookupExpression(), argument));
        ItemRuntimePlan runtimeIterator = new ObjectLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitPostfixLookupExpression(
            PostfixLookupExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        Expression lookup = expression.getLookupExpression(); // null if wildcard
        ItemRuntimePlan lookupIterator = (lookup == null)
            ? null
            : this.visit(expression.getLookupExpression(), argument);
        RuntimeStaticContext staticContextForRuntime =
            expression.getStaticContextForRuntime(this.config, this.visitorConfig);
        ItemRuntimePlan runtimeIterator = new PostfixLookupIterator(
                mainIterator,
                lookupIterator,
                staticContextForRuntime
        );
        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitUnaryLookupExpression(UnaryLookupExpression expression, ItemRuntimePlan argument) {
        Expression lookup = expression.getLookupExpression(); // null if wildcard
        ItemRuntimePlan lookupIterator = (lookup == null)
            ? null
            : this.visit(expression.getLookupExpression(), argument);
        RuntimeStaticContext staticContextForRuntime =
            expression.getStaticContextForRuntime(this.config, this.visitorConfig);
        ItemRuntimePlan runtimeIterator = new UnaryLookupIterator(
                lookupIterator,
                staticContextForRuntime
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDynamicFunctionCallExpression(
            DynamicFunctionCallExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        List<ItemRuntimePlan> arguments = new ArrayList<>();
        for (Expression arg : expression.getArguments()) {
            if (arg == null) { // check ArgumentPlaceholder
                arguments.add(null);
            } else {
                arguments.add(this.visit(arg, argument));
            }
        }
        ItemRuntimePlan runtimeIterator = new DynamicFunctionCallIterator(
                mainIterator,
                arguments,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitArrayUnboxingExpression(
            ArrayUnboxingExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan mainIterator = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new ArrayUnboxingIterator(
                mainIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitArrayConstructor(ArrayConstructorExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator;
        if (expression.isFixedSlotsArrayConstructor()) {
            List<ItemRuntimePlan> memberIterators = new ArrayList<>();
            if (expression.getMemberExpressions() != null) {
                for (Expression memberExpr : expression.getMemberExpressions()) {
                    memberIterators.add(this.visit(memberExpr, argument));
                }
            }
            runtimeIterator = new ArrayRuntimeIterator(
                    memberIterators,
                    true,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
            );
        } else {
            ItemRuntimePlan result = null;
            if (expression.getExpression() != null) {
                result = this.visit(expression.getExpression(), argument);
            }
            runtimeIterator = new ArrayRuntimeIterator(
                    result,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitObjectConstructor(
            ObjectConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator;
        if (expression.isMergedConstructor()) {
            runtimeIterator = new ObjectConstructorRuntimeIterator(
                    expression.getChildren()
                        .stream()
                        .map(arg -> this.visit(arg, argument))
                        .collect(Collectors.toList()),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
            );

            return runtimeIterator;
        } else {
            List<ItemRuntimePlan> keys = expression.getKeys()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            List<ItemRuntimePlan> values = expression.getValues()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            runtimeIterator = new ObjectConstructorRuntimeIterator(
                    keys,
                    values,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
            );

            return runtimeIterator;
        }
    }

    @Override
    public ItemRuntimePlan visitMapConstructor(MapConstructorExpression expression, ItemRuntimePlan argument) {
        List<ItemRuntimePlan> keys = expression.getKeys()
            .stream()
            .map(arg -> this.visit(arg, argument))
            .collect(Collectors.toList());
        List<ItemRuntimePlan> values = expression.getValues()
            .stream()
            .map(arg -> this.visit(arg, argument))
            .collect(Collectors.toList());
        List<SequenceType> keyTypes = expression.getKeys()
            .stream()
            .map(arg -> arg.getStaticSequenceType())
            .filter(
                arg -> !arg.getArity().equals(SequenceType.Arity.One)
                    || !arg.getItemType().isSubtypeOf(BuiltinTypesCatalogue.stringItem)
            )
            .collect(Collectors.toList());
        List<SequenceType> valueTypes = expression.getValues()
            .stream()
            .map(arg -> arg.getStaticSequenceType())
            .filter(
                arg -> !arg.getArity().equals(SequenceType.Arity.One)
            )
            .collect(Collectors.toList());
        if (keyTypes.isEmpty() && valueTypes.isEmpty()) {
            ItemRuntimePlan runtimeIterator = new ObjectConstructorRuntimeIterator(
                    keys,
                    values,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
            );

            return runtimeIterator;
        }
        ItemRuntimePlan runtimeIterator = new MapConstructorRuntimeIterator(
                keys,
                values,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                expression.isInSequentialBlock() || expression.getStaticContext().isQuerySideEffecting()
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDirElemConstructor(
            DirElemConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator = new DirElemConstructorRuntimeIterator(
                expression.getNodeName(),
                expression.getContent()
                    .stream()
                    .map(arg -> this.visit(arg, argument))
                    .collect(Collectors.toList()),
                expression.getAttributes()
                    .stream()
                    .map(arg -> (AttributeNodeRuntimeIterator) this.visit(arg, argument))
                    .collect(Collectors.toList()),
                expression.getNamespaceDeclarations(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDirPIConstructor(DirPIConstructorExpression expression, ItemRuntimePlan argument) {
        Expression contentExpression = expression.getContentExpression();
        DataFunctionIterator contentIterator = null;
        if (contentExpression != null) {
            ItemRuntimePlan contentExpressionIterator = this.visit(contentExpression, argument);
            contentIterator = new DataFunctionIterator(
                    Collections.singletonList(contentExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        ItemRuntimePlan runtimeIterator = new DirPIConstructorRuntimeIterator(
                expression.getTarget(),
                contentIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitComputedElementConstructor(
            ComputedElementConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        Expression contentExpression = expression.getContentExpression();
        ItemRuntimePlan contentIterator = contentExpression != null ? this.visit(contentExpression, argument) : null;

        ItemRuntimePlan runtimeIterator;
        if (expression.hasStaticName()) {
            // Static element name: element elementName { content }
            runtimeIterator = new ComputedElementConstructorRuntimeIterator(
                    expression.getElementName(),
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            // Dynamic element name: element { nameExpression } { content }
            ItemRuntimePlan nameExpressionIterator = this.visit(expression.getNameExpression(), argument);
            DataFunctionIterator nameIterator = new DataFunctionIterator(
                    Collections.singletonList(nameExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator = new ComputedElementConstructorRuntimeIterator(
                    nameIterator,
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDocumentNodeConstructor(
            DocumentNodeConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = expression.getContentExpression() != null
            ? this.visit(expression.getContentExpression(), argument)
            : null;
        ItemRuntimePlan runtimeIterator = new DocumentNodeConstructorRuntimeIterator(
                contentIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitComputedPIConstructor(
            ComputedPIConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        DataFunctionIterator contentIterator = null;
        if (expression.getContentExpression() != null) {
            ItemRuntimePlan contentExpressionIterator = this.visit(expression.getContentExpression(), argument);
            contentIterator = new DataFunctionIterator(
                    Collections.singletonList(contentExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        ItemRuntimePlan runtimeIterator;
        if (expression.hasStaticTarget()) {
            runtimeIterator = new ComputedPIConstructorRuntimeIterator(
                    expression.getTarget(),
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            ItemRuntimePlan nameExpressionIterator = this.visit(expression.getNameExpression(), argument);
            DataFunctionIterator nameIterator = new DataFunctionIterator(
                    Collections.singletonList(nameExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator = new ComputedPIConstructorRuntimeIterator(
                    nameIterator,
                    contentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitComputedNamespaceConstructor(
            ComputedNamespaceConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan uriExpressionIterator = this.visit(expression.getUriExpression(), argument);
        DataFunctionIterator uriIterator = new DataFunctionIterator(
                Collections.singletonList(uriExpressionIterator),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        ItemRuntimePlan runtimeIterator;
        if (expression.hasStaticPrefix()) {
            runtimeIterator = new ComputedNamespaceConstructorRuntimeIterator(
                    expression.getPrefix(),
                    uriIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            ItemRuntimePlan prefixExpressionIterator = this.visit(expression.getPrefixExpression(), argument);
            DataFunctionIterator prefixIterator = new DataFunctionIterator(
                    Collections.singletonList(prefixExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator = new ComputedNamespaceConstructorRuntimeIterator(
                    prefixIterator,
                    uriIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitComputedAttributeConstructor(
            ComputedAttributeConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator;
        // create atomized iterator for the content expression
        ItemRuntimePlan contentExpressionIterator = this.visit(expression.getValueExpression(), argument);
        DataFunctionIterator atomizedContentIterator = new DataFunctionIterator(
                Collections.singletonList(contentExpressionIterator),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        if (expression.hasStaticName()) {
            // Static attribute name: attribute attributeName { content }
            runtimeIterator = new ComputedAttributeConstructorRuntimeIterator(
                    expression.getAttributeName(),
                    atomizedContentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        } else {
            // Dynamic attribute name: attribute { nameExpression } { content }
            // create atomized iterator for the name expression
            ItemRuntimePlan nameExpressionIterator = this.visit(expression.getNameExpression(), argument);
            DataFunctionIterator atomizedNameIterator = new DataFunctionIterator(
                    Collections.singletonList(nameExpressionIterator),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
            runtimeIterator = new ComputedAttributeConstructorRuntimeIterator(
                    atomizedNameIterator,
                    atomizedContentIterator,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitCommentNodeConstructor(
            CommentNodeConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = visit(expression.getContentExpression(), argument);
        CommentNodeConstructorRuntimeIterator result = new CommentNodeConstructorRuntimeIterator(
                new DataFunctionIterator(
                        Collections.singletonList(contentIterator),
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                ),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return result;
    }

    @Override
    public ItemRuntimePlan visitDirectCommentConstructor(
            DirectCommentConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        DirectCommentConstructorRuntimeIterator result = new DirectCommentConstructorRuntimeIterator(
                expression.getContent(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return result;
    }

    @Override
    public ItemRuntimePlan visitTextNodeConstructor(
            TextNodeConstructorExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan contentIterator = visit(expression.getContentExpression(), argument);

        TextNodeConstructorRuntimeIterator result = new TextNodeConstructorRuntimeIterator(
                new DataFunctionIterator(
                        Collections.singletonList(contentIterator),
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                ),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return result;
    }

    @Override
    public ItemRuntimePlan visitTextNode(TextNodeExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new TextNodeRuntimeIterator(
                expression.getContent(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitAttributeNode(AttributeNodeExpression expression, ItemRuntimePlan argument) {
        List<DataFunctionIterator> atomizedValues = expression.getValue()
            .stream()
            .map(
                arg -> new DataFunctionIterator(
                        Collections.singletonList(this.visit(arg, argument)),
                        expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                )
            )
            .collect(Collectors.toList());

        ItemRuntimePlan runtimeIterator = new AttributeNodeRuntimeIterator(
                expression.getNodeName(),
                atomizedValues,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitAttributeNodeContent(
            AttributeNodeContentExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator = new AttributeNodeContentRuntimeIterator(
                expression.getContent(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitContextExpr(ContextItemExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new ContextExpressionIterator(
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitInlineFunctionExpr(InlineFunctionExpression expression, ItemRuntimePlan argument) {
        Map<Name, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>();
        for (Map.Entry<Name, SequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue());
        }
        SequenceType returnType = expression.getReturnType();
        ItemRuntimePlan bodyIterator = this.visit(expression.getBody(), argument);
        ItemRuntimePlan runtimeIterator = new FunctionRuntimeIterator(
                expression.getName(),
                paramNameToSequenceTypes,
                returnType,
                bodyIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitFunctionCall(FunctionCallExpression expression, ItemRuntimePlan argument) {
        List<ItemRuntimePlan> arguments = new ArrayList<>();
        for (Expression arg : expression.getArguments()) {
            if (arg == null) {
                arguments.add(null);
            } else {
                ItemRuntimePlan argumentIterator = this.visit(arg, argument);
                arguments.add(argumentIterator);
            }
        }
        Name fnName = expression.getFunctionName();
        int arity = arguments.size();
        FunctionIdentifier identifier = new FunctionIdentifier(fnName, arity);
        String queryLanguage = expression.getStaticContext().getQueryLanguage();

        ItemRuntimePlan runtimeIterator = null;
        if (BuiltinFunctionCatalogue.exists(identifier, queryLanguage)) {
            runtimeIterator = NamedFunctions.getBuiltInFunctionIterator(
                identifier,
                arguments,
                // Note: passing the static context of the function call expression makes
                // all builtin functions static-context-dependent.
                // This might be worth a more fine-grained adjustment later.
                expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                false
            );
        } else {
            runtimeIterator = new StaticUserDefinedFunctionCallIterator(
                    identifier,
                    arguments,
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig),
                    expression.isTailCallOptimization()
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitNamedFunctionRef(
            NamedFunctionReferenceExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan runtimeIterator = new NamedFunctionRefRuntimeIterator(
                expression.getIdentifier(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }
    // endregion

    // region literal
    @Override
    public ItemRuntimePlan visitInteger(IntegerLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new IntegerRuntimeIterator(
                expression.getLexicalValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitString(StringLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new StringRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDouble(DoubleLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new DoubleRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitDecimal(DecimalLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new DecimalRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitNull(NullLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new NullRuntimeIterator(
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitBoolean(BooleanLiteralExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new BooleanRuntimeIterator(
                expression.getValue(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }
    // endregion

    // region operational
    @Override
    public ItemRuntimePlan visitAdditiveExpr(AdditiveExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        ItemRuntimePlan left = (this.visit(
            leftExpression,
            argument
        ));
        ItemRuntimePlan right = (this.visit(
            rightExpression,
            argument
        ));
        if (!leftExpression.getStaticSequenceType().getItemType().isAtomicItemType()) {
            left = new DataFunctionIterator(
                    Collections.singletonList(left),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        if (!rightExpression.getStaticSequenceType().getItemType().isAtomicItemType()) {
            right = new DataFunctionIterator(
                    Collections.singletonList(right),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        ItemRuntimePlan runtimeIterator = new AdditiveOperationIterator(
                left,
                right,
                expression.isMinus(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitMultiplicativeExpr(MultiplicativeExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        ItemRuntimePlan left = (this.visit(
            leftExpression,
            argument
        ));
        ItemRuntimePlan right = (this.visit(
            rightExpression,
            argument
        ));
        if (!leftExpression.getStaticSequenceType().getItemType().isAtomicItemType()) {
            left = new DataFunctionIterator(
                    Collections.singletonList(left),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        if (!rightExpression.getStaticSequenceType().getItemType().isAtomicItemType()) {
            right = new DataFunctionIterator(
                    Collections.singletonList(right),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        ItemRuntimePlan runtimeIterator = new MultiplicativeOperationIterator(
                left,
                right,
                expression.getMultiplicativeOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitSimpleMapExpr(SimpleMapExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        ItemRuntimePlan left = (this.visit(
            leftExpression,
            argument
        ));
        ItemRuntimePlan right = (this.visit(
            rightExpression,
            argument
        ));

        ItemRuntimePlan runtimeIterator = new SimpleMapExpressionIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitAndExpr(AndExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        ItemRuntimePlan left = this.visit(
            leftExpression,
            argument
        );
        ItemRuntimePlan right = this.visit(
            rightExpression,
            argument
        );

        ItemRuntimePlan runtimeIterator = new AndOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitOrExpr(OrExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);
        ItemRuntimePlan left = this.visit(
            leftExpression,
            argument
        );
        ItemRuntimePlan right = this.visit(
            rightExpression,
            argument
        );

        ItemRuntimePlan runtimeIterator = new OrOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitNotExpr(NotExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan runtimeIterator = new NotOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitUnaryExpr(UnaryExpression expression, ItemRuntimePlan argument) {
        // compute +- final result
        ItemRuntimePlan runtimeIterator = new UnaryOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.isNegated(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitRangeExpr(RangeExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan left = this.visit(expression.getChildren().get(0), argument);
        ItemRuntimePlan right = this.visit(expression.getChildren().get(1), argument);
        ItemRuntimePlan runtimeIterator = new RangeOperationIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitNodeSetExpr(NodeSetExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan left = this.visit(expression.getLeftExpression(), argument);
        ItemRuntimePlan right = this.visit(expression.getRightExpression(), argument);
        ItemRuntimePlan runtimeIterator = new NodeSetOperationIterator(
                left,
                right,
                expression.getOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitComparisonExpr(ComparisonExpression expression, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) expression.getChildren().get(0);
        Expression rightExpression = (Expression) expression.getChildren().get(1);

        ItemRuntimePlan left = this.visit(leftExpression, argument);
        ItemRuntimePlan right = this.visit(rightExpression, argument);
        if (!(leftExpression.getStaticSequenceType().getItemType().isAtomicItemType())) {
            // Atomic comparison operators require atomized operands. If the operands are not atomic, we need to wrap
            // them in a DataFunctionIterator to atomize them.
            left = new DataFunctionIterator(
                    Collections.singletonList(left),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        if (!(rightExpression.getStaticSequenceType().getItemType().isAtomicItemType())) {
            right = new DataFunctionIterator(
                    Collections.singletonList(right),
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        ItemRuntimePlan runtimeIterator = new ComparisonIterator(
                left,
                right,
                expression.getComparisonOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitNodeComparisonExpr(NodeComparisonExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan left = this.visit(expression.getLeftExpression(), argument);
        ItemRuntimePlan right = this.visit(expression.getRightExpression(), argument);
        ItemRuntimePlan runtimeIterator = new NodeComparisonRuntimeIterator(
                left,
                right,
                expression.getOperator(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitStringConcatExpr(StringConcatExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan left = this.visit(expression.getChildren().get(0), argument);
        ItemRuntimePlan right = this.visit(expression.getChildren().get(1), argument);
        ItemRuntimePlan runtimeIterator = new StringConcatIterator(
                left,
                right,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitInstanceOfExpression(InstanceOfExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan childExpression = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new InstanceOfIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitValidateTypeExpression(
            ValidateTypeExpression expression,
            ItemRuntimePlan argument
    ) {
        ItemRuntimePlan childExpression = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new ValidateTypeIterator(
                childExpression,
                expression.getSequenceType().getItemType(),
                expression.isValidate(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        ItemRuntimePlan resultIterator = new TreatIterator(
                runtimeIterator,
                new SequenceType(BuiltinTypesCatalogue.item, expression.getSequenceType().getArity()),
                ErrorCode.InvalidInstance,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return resultIterator;
    }

    @Override
    public ItemRuntimePlan visitTreatExpression(TreatExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan childExpression = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new TreatIterator(
                childExpression,
                expression.getSequenceType(),
                expression.errorCodeThatShouldBeThrown(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitCastableExpression(CastableExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan childExpression = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new CastableIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitCastExpression(CastExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan childExpression = this.visit(expression.getMainExpression(), argument);
        ItemRuntimePlan runtimeIterator = new CastIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }
    // endregion

    // region control
    @Override
    public ItemRuntimePlan visitConditionalExpression(ConditionalExpression expression, ItemRuntimePlan argument) {
        ItemRuntimePlan conditionIterator = this.visit(expression.getCondition(), argument);
        ItemRuntimePlan thenIterator = this.visit(expression.getBranch(), argument);
        ItemRuntimePlan elseIterator = this.visit(expression.getElseBranch(), argument);
        ItemRuntimePlan runtimeIterator = null;
        if (
            thenIterator instanceof AbstractAtMostOneItemRuntimePlan
                &&
                elseIterator instanceof AbstractAtMostOneItemRuntimePlan
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
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitSwitchExpression(SwitchExpression expression, ItemRuntimePlan argument) {
        Map<ItemRuntimePlan, ItemRuntimePlan> cases = new LinkedHashMap<>();
        for (SwitchCase caseExpression : expression.getCases()) {
            ItemRuntimePlan caseExpr = this.visit(caseExpression.getReturnExpression(), argument);
            for (Expression conditionExpr : caseExpression.getConditionExpressions()) {
                ItemRuntimePlan condition = this.visit(conditionExpr, argument);
                cases.put(condition, caseExpr);
            }
        }
        ItemRuntimePlan runtimeIterator = new SwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                this.visit(expression.getDefaultExpression(), argument),
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }
    // endregion

    @Override
    public ItemRuntimePlan visitTypeSwitchExpression(TypeSwitchExpression expression, ItemRuntimePlan argument) {
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

        ItemRuntimePlan runtimeIterator = new TypeswitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                defaultCase,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitTryCatchExpression(TryCatchExpression expression, ItemRuntimePlan argument) {
        Map<CatchPattern, ItemRuntimePlan> cases = new LinkedHashMap<>();
        for (CatchPattern pattern : expression.getCatchPatterns()) {
            cases.put(
                pattern,
                this.visit(expression.getExpressionCatching(pattern), argument)
            );
        }
        return new TryCatchRuntimeIterator(
                this.visit(expression.getTryExpression(), argument),
                cases,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitWhileStatement(WhileStatement statement, ItemRuntimePlan argument) {
        ItemRuntimePlan testConditionIterator = this.visit(statement.getTestCondition(), argument);
        ItemRuntimePlan statementIterator = this.visit(statement.getStatement(), argument);
        return new WhileStatementIterator(
                testConditionIterator,
                statementIterator,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitVariableDeclStatement(VariableDeclStatement statement, ItemRuntimePlan argument) {
        Name varName = statement.getVariableName();
        List<ItemRuntimePlan> exprIterator = Collections.emptyList();
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
    public ItemRuntimePlan visitCommaVariableDeclStatement(
            CommaVariableDeclStatement statement,
            ItemRuntimePlan argument
    ) {
        List<ItemRuntimePlan> children = new ArrayList<>();
        for (VariableDeclStatement varDecl : statement.getVariables()) {
            children.add(this.visit(varDecl, argument));
        }
        return new CommaVariableDeclStatementIterator(
                children,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitAssignStatement(AssignStatement statement, ItemRuntimePlan argument) {
        return new AssignStatementIterator(
                this.visit(statement.getAssignExpression(), argument),
                statement.getName(),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitApplyStatement(ApplyStatement statement, ItemRuntimePlan argument) {
        return new ApplyStatementIterator(
                this.visit(statement.getApplyExpression(), argument),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitBreakStatement(BreakStatement statement, ItemRuntimePlan argument) {
        return new BreakStatementIterator(statement.getStaticContextForRuntime(this.config, this.visitorConfig));
    }

    @Override
    public ItemRuntimePlan visitContinueStatement(ContinueStatement statement, ItemRuntimePlan argument) {
        return new ContinueStatementIterator(statement.getStaticContextForRuntime(this.config, this.visitorConfig));
    }

    @Override
    public ItemRuntimePlan visitExitStatement(ExitStatement statement, ItemRuntimePlan argument) {
        return new ExitStatementIterator(
                this.visit(statement.getExitExpression(), argument),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
                    .toBuilder()
                    .isSequential(true)
                    .build()
        );
    }

    @Override
    public ItemRuntimePlan visitTryCatchStatement(TryCatchStatement statement, ItemRuntimePlan argument) {
        Map<CatchPattern, ItemRuntimePlan> cases = new LinkedHashMap<>();
        for (CatchPattern pattern : statement.getCatchPatterns()) {
            cases.put(
                pattern,
                this.visit(statement.getBlockStatementCatching(pattern), argument)
            );
        }
        return new TryCatchStatementIterator(
                this.visit(statement.getTryStatement(), argument),
                cases,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitBlockStatement(BlockStatement statement, ItemRuntimePlan argument) {
        List<ItemRuntimePlan> result = new ArrayList<>();
        for (Statement stmt : statement.getBlockStatements()) {
            ItemRuntimePlan childIterator = this.visit(stmt, argument);
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
    public ItemRuntimePlan visitSwitchStatement(SwitchStatement statement, ItemRuntimePlan argument) {
        Map<ItemRuntimePlan, ItemRuntimePlan> cases = new LinkedHashMap<>();
        for (SwitchCaseStatement caseExpression : statement.getCases()) {
            ItemRuntimePlan caseExpr = this.visit(caseExpression.getReturnStatement(), argument);
            for (Expression conditionExpr : caseExpression.getConditionExpressions()) {
                ItemRuntimePlan condition = this.visit(conditionExpr, argument);
                cases.put(condition, caseExpr);
            }
        }
        ItemRuntimePlan runtimeIterator = new SwitchStatementIterator(
                this.visit(statement.getTestCondition(), argument),
                cases,
                this.visit(statement.getDefaultStatement(), argument),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitTypeSwitchStatement(TypeSwitchStatement statement, ItemRuntimePlan argument) {
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

        ItemRuntimePlan runtimeIterator = new TypeSwitchStatementIterator(
                this.visit(statement.getTestCondition(), argument),
                cases,
                defaultCase,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitStatementsAndExpr(StatementsAndExpr statementsAndExpr, ItemRuntimePlan argument) {
        List<ItemRuntimePlan> result = new ArrayList<>();
        for (Statement statement : statementsAndExpr.getStatements()) {
            ItemRuntimePlan childIterator = this.visit(statement, argument);
            if (childIterator != null) {
                result.add(childIterator);
            }
        }
        ItemRuntimePlan exprIterator = this.visit(statementsAndExpr.getExpression(), argument);
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
    public ItemRuntimePlan visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            ItemRuntimePlan argument
    ) {
        List<ItemRuntimePlan> result = new ArrayList<>();
        ItemRuntimePlan exprIterator = null;
        for (Statement statement : statementsAndOptionalExpr.getStatements()) {
            ItemRuntimePlan childIterator = this.visit(statement, argument);
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
    public ItemRuntimePlan visitProgram(Program program, ItemRuntimePlan argument) {
        if (program.isSequential() || program.isUpdating()) {
            program.getStatementsAndOptionalExpr().getStaticContext().setIsQuerySideEffecting(true);
        }
        return new ProgramIterator(
                this.visit(program.getStatementsAndOptionalExpr(), argument),
                program.getStatementsAndOptionalExpr().getStaticContextForRuntime(this.config, this.visitorConfig)
        );
    }

    @Override
    public ItemRuntimePlan visitConditionalStatement(ConditionalStatement statement, ItemRuntimePlan argument) {
        ItemRuntimePlan conditionIterator = this.visit(statement.getCondition(), argument);
        ItemRuntimePlan thenIterator = this.visit(statement.getBranch(), argument);
        ItemRuntimePlan elseIterator = this.visit(statement.getElseBranch(), argument);
        List<ItemRuntimePlan> result = new ArrayList<>();
        result.add(conditionIterator);
        result.add(thenIterator);
        result.add(elseIterator);
        ItemRuntimePlan runtimeIterator = new ConditionalStatementIterator(
                result,
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );


        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitFlowrStatement(FlowrStatement statement, ItemRuntimePlan argument) {
        TupleRuntimePlan previous = this.visitFlowrClause(
            statement.getReturnStatementClause().getPreviousClause(),
            argument
        );
        ReturnStatementClause returnClause = statement.getReturnStatementClause();
        ItemRuntimePlan runtimeIterator = new ReturnStatementClauseIterator(
                previous,
                (this.visit(
                    returnClause.getReturnStatement(),
                    argument
                )),
                statement.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    @Override
    public ItemRuntimePlan visitSlashExpr(SlashExpr slashExpr, ItemRuntimePlan argument) {
        Expression leftExpression = (Expression) slashExpr.getChildren().get(0);
        Expression rightExpression = (Expression) slashExpr.getChildren().get(1);
        ItemRuntimePlan left = this.visit(
            leftExpression,
            argument
        );
        if (!isStaticallyGuaranteedNodeSequence(leftExpression)) {
            left = new TreatIterator(
                    left,
                    new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.ZeroOrMore),
                    ErrorCode.UnexpectedNode,
                    slashExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
            );
        }
        ItemRuntimePlan right = this.visit(
            rightExpression,
            argument
        );

        ItemRuntimePlan runtimeIterator = new SlashExprIterator(
                left,
                right,
                slashExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    private boolean isStaticallyGuaranteedNodeSequence(Expression expression) {
        SequenceType staticType = expression.getStaticSequenceType();
        return staticType != null
            && staticType.isSubtypeOf(new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.ZeroOrMore));
    }

    @Override
    public ItemRuntimePlan visitStepExpr(StepExpr stepExpr, ItemRuntimePlan argument) {
        AxisIterator axisIterator = this.visitAxisStep(stepExpr, stepExpr.getMetadata());
        NodeTest nodeTest = stepExpr.getNodeTest();
        return new StepExprIterator(
                axisIterator,
                nodeTest,
                stepExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
                    .toBuilder()
                    .staticType(SequenceType.createSequenceType("item"))
                    .build()
        );
    }

    @Override
    public ItemRuntimePlan visitPathRootExpr(
            PathRootExpression expression,
            ItemRuntimePlan argument
    ) {
        this.config.setOptimizeParentPointers(false);
        ItemRuntimePlan runtimeIterator = new PathRootRuntimeIterator(
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );

        return runtimeIterator;
    }

    private AxisIterator visitAxisStep(StepExpr stepExpr, ExceptionMetadata metadata) {
        return stepExpr.accept(
            new AxisIteratorVisitor(),
            stepExpr.getStaticContextForRuntime(this.config, this.visitorConfig)
                .toBuilder()
                .staticType(SequenceType.createSequenceType("string"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(metadata)
                .build()
        );
    }

}
