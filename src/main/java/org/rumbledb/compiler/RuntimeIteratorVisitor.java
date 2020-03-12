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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ForClauseVar;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByClauseVar;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.LetClauseVar;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseExpr;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.operational.ComparisonExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.postfix.PredicateExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.control.IfRuntimeIterator;
import org.rumbledb.runtime.control.SwitchRuntimeIterator;
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
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.functions.FunctionRuntimeIterator;
import org.rumbledb.runtime.functions.StaticUserDefinedFunctionCallIterator;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.Functions;
import org.rumbledb.runtime.operational.AdditiveOperationIterator;
import org.rumbledb.runtime.operational.AndOperationIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.CastableIterator;
import org.rumbledb.runtime.operational.ComparisonOperationIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.runtime.operational.MultiplicativeOperationIterator;
import org.rumbledb.runtime.operational.NotOperationIterator;
import org.rumbledb.runtime.operational.OrOperationIterator;
import org.rumbledb.runtime.operational.RangeOperationIterator;
import org.rumbledb.runtime.operational.StringConcatIterator;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.runtime.operational.UnaryOperationIterator;
import org.rumbledb.runtime.postfix.ArrayLookupIterator;
import org.rumbledb.runtime.postfix.ArrayUnboxingIterator;
import org.rumbledb.runtime.postfix.ObjectLookupIterator;
import org.rumbledb.runtime.postfix.PredicateIterator;
import org.rumbledb.runtime.primary.ArrayRuntimeIterator;
import org.rumbledb.runtime.primary.BooleanRuntimeIterator;
import org.rumbledb.runtime.primary.ContextExpressionIterator;
import org.rumbledb.runtime.primary.DecimalRuntimeIterator;
import org.rumbledb.runtime.primary.DoubleRuntimeIterator;
import org.rumbledb.runtime.primary.IntegerRuntimeIterator;
import org.rumbledb.runtime.primary.NullRuntimeIterator;
import org.rumbledb.runtime.primary.ObjectConstructorRuntimeIterator;
import org.rumbledb.runtime.primary.StringRuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.runtime.quantifiers.QuantifiedExpressionIterator;
import org.rumbledb.runtime.quantifiers.QuantifiedExpressionVarIterator;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuntimeIteratorVisitor extends AbstractNodeVisitor<RuntimeIterator> {

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
    public RuntimeIterator visitCommaExpression(CommaExpression expression, RuntimeIterator argument) {
        List<RuntimeIterator> result = new ArrayList<>();
        for (Expression childExpr : expression.getExpressions()) {
            result.add(this.visit(childExpr, argument));
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return new CommaExpressionIterator(
                    result,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
    }

    // region module
    @Override
    public RuntimeIterator visitMainModule(MainModule expression, RuntimeIterator argument) {
        return super.visitMainModule(expression, argument);
    }

    @Override
    public RuntimeIterator visitProlog(Prolog expression, RuntimeIterator argument) {
        return super.visitProlog(expression, argument);
    }
    // endregion

    // region FLOWR
    @Override
    public RuntimeIterator visitFlowrExpression(FlworExpression expression, RuntimeIterator argument) {
        Clause startClause = expression.getStartClause();
        RuntimeTupleIterator previous = this.visitFlowrClause(startClause, argument, null);
        for (Clause clause : expression.getContentClauses()) {
            previous = this.visitFlowrClause(clause, argument, previous);
        }
        return new ReturnClauseSparkIterator(
                previous,
                this.visit(
                    (expression.getReturnClause()).getReturnExpr(),
                    argument
                ),
                expression.getReturnClause().getHighestExecutionMode(),
                expression.getReturnClause().getMetadata()
        );
    }

    private RuntimeTupleIterator visitFlowrClause(
            Clause clause,
            RuntimeIterator argument,
            RuntimeTupleIterator previousIterator
    ) {
        if (clause instanceof ForClause) {
            for (ForClauseVar var : ((ForClause) clause).getForVariables()) {
                RuntimeIterator assignmentIterator = this.visit(var.getExpression(), argument);
                if (var.getSequenceType() != SequenceType.mostGeneralSequenceType) {
                    ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                        var.getSequenceType(),
                        var.getExpression()
                    );
                    assignmentIterator = new TreatIterator(
                            assignmentIterator,
                            var.getSequenceType(),
                            false,
                            executionMode,
                            clause.getMetadata()
                    );
                }

                previousIterator = new ForClauseSparkIterator(
                        previousIterator,
                        var.getVariableReference().getVariableName(),
                        assignmentIterator,
                        var.getHighestExecutionMode(),
                        clause.getMetadata()
                );
            }
        } else if (clause instanceof LetClause) {
            for (LetClauseVar var : ((LetClause) clause).getLetVariables()) {
                RuntimeIterator assignmentIterator = this.visit(var.getExpression(), argument);
                if (var.getSequenceType() != SequenceType.mostGeneralSequenceType) {
                    ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                        var.getSequenceType(),
                        var.getExpression()
                    );
                    assignmentIterator = new TreatIterator(
                            assignmentIterator,
                            var.getSequenceType(),
                            false,
                            executionMode,
                            clause.getMetadata()
                    );
                }

                previousIterator = new LetClauseSparkIterator(
                        previousIterator,
                        var.getVariableReference().getVariableName(),
                        assignmentIterator,
                        var.getHighestExecutionMode(),
                        clause.getMetadata()
                );
            }
        } else if (clause instanceof GroupByClause) {
            List<GroupByClauseSparkIteratorExpression> groupingExpressions = new ArrayList<>();
            for (GroupByClauseVar var : ((GroupByClause) clause).getGroupVariables()) {
                Expression groupByExpression = var.getExpression();
                RuntimeIterator groupByExpressionIterator = null;
                if (groupByExpression != null) {
                    groupByExpressionIterator = this.visit(groupByExpression, argument);
                    if (var.getSequenceType() != SequenceType.mostGeneralSequenceType) {
                        ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                            var.getSequenceType(),
                            groupByExpression
                        );
                        groupByExpressionIterator = new TreatIterator(
                                groupByExpressionIterator,
                                var.getSequenceType(),
                                false,
                                executionMode,
                                clause.getMetadata()
                        );
                    }
                }

                VariableReferenceExpression variableReference = var.getVariableReference();
                VariableReferenceIterator variableReferenceIterator =
                    (VariableReferenceIterator) this.visit(variableReference, argument);

                groupingExpressions.add(
                    new GroupByClauseSparkIteratorExpression(
                            groupByExpressionIterator,
                            variableReferenceIterator,
                            var.getMetadata()
                    )
                );
            }
            previousIterator = new GroupByClauseSparkIterator(
                    previousIterator,
                    groupingExpressions,
                    clause.getHighestExecutionMode(),
                    clause.getMetadata()
            );
        } else if (clause instanceof OrderByClause) {
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator = new ArrayList<>();
            for (OrderByClauseExpr orderExpr : ((OrderByClause) clause).getExpressions()) {
                expressionsWithIterator.add(
                    new OrderByClauseAnnotatedChildIterator(
                            this.visit(orderExpr.getExpression(), argument),
                            orderExpr.isAscending(),
                            orderExpr.getUri(),
                            orderExpr.getEmptyOrder()
                    )
                );
            }
            previousIterator = new OrderByClauseSparkIterator(
                    previousIterator,
                    expressionsWithIterator,
                    ((OrderByClause) clause).isStable(),
                    clause.getHighestExecutionMode(),
                    clause.getMetadata()
            );
        } else if (clause instanceof WhereClause) {
            previousIterator = new WhereClauseSparkIterator(
                    previousIterator,
                    this.visit(((WhereClause) clause).getWhereExpression(), argument),
                    clause.getHighestExecutionMode(),
                    clause.getMetadata()
            );
        } else if (clause instanceof CountClause) {
            previousIterator = new CountClauseSparkIterator(
                    previousIterator,
                    this.visit(((CountClause) clause).getCountVariable(), argument),
                    clause.getHighestExecutionMode(),
                    clause.getMetadata()
            );
        }
        return previousIterator;
    }

    @Override
    public RuntimeIterator visitOrderByClauseExpr(OrderByClauseExpr expression, RuntimeIterator argument) {
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitVariableReference(VariableReferenceExpression expression, RuntimeIterator argument) {
        return new VariableReferenceIterator(
                expression.getVariableName(),
                expression.getType(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }
    // endregion

    // region primary
    @Override
    public RuntimeIterator visitPredicateExpression(PredicateExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator filterIterator = this.visit(expression.getPredicateExpression(), argument);
        return new PredicateIterator(
                mainIterator,
                filterIterator,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitArrayLookupExpression(ArrayLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        return new ArrayLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitObjectLookupExpression(ObjectLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        return new ObjectLookupIterator(
                mainIterator,
                lookupIterator,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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
        return new DynamicFunctionCallIterator(
                mainIterator,
                arguments,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitArrayUnboxingExpression(ArrayUnboxingExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        return new ArrayUnboxingIterator(
                mainIterator,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitArrayConstructor(ArrayConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator result = null;
        if (expression.getExpression() != null) {
            result = this.visit(expression.getExpression(), argument);
        }
        return new ArrayRuntimeIterator(
                result,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitObjectConstructor(ObjectConstructorExpression expression, RuntimeIterator argument) {
        RuntimeIterator iterator;
        if (expression.isMergedConstructor()) {
            iterator = new ObjectConstructorRuntimeIterator(
                    expression.getChildren()
                        .stream()
                        .map(arg -> this.visit(arg, argument))
                        .collect(Collectors.toList()),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
            return iterator;
        } else {
            List<RuntimeIterator> keys = expression.getKeys()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            List<RuntimeIterator> values = expression.getValues()
                .stream()
                .map(arg -> this.visit(arg, argument))
                .collect(Collectors.toList());
            iterator = new ObjectConstructorRuntimeIterator(
                    keys,
                    values,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
            return iterator;
        }
    }

    @Override
    public RuntimeIterator visitContextExpr(ContextItemExpression expression, RuntimeIterator argument) {
        return new ContextExpressionIterator(expression.getHighestExecutionMode(), expression.getMetadata());
    }

    @Override
    public RuntimeIterator visitFunctionDeclaration(InlineFunctionExpression expression, RuntimeIterator argument) {
        Map<String, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>();
        for (Map.Entry<String, SequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue());
        }
        SequenceType returnType = expression.getReturnType();
        RuntimeIterator bodyIterator = this.visit(expression.getBody(), argument);
        FunctionItem function = new FunctionItem(
                expression.getName(),
                paramNameToSequenceTypes,
                returnType,
                bodyIterator
        );
        if (expression.getName().equals("")) {
            // unnamed (inline function declaration)
            return new FunctionRuntimeIterator(
                    function,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            // named (static function declaration)
            Functions.addUserDefinedFunction(function, expression.getMetadata());
        }

        return defaultAction(expression, argument);
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
        String fnName = expression.getFunctionName();
        int arity = arguments.size();
        FunctionIdentifier identifier = new FunctionIdentifier(fnName, arity);

        if (Functions.checkBuiltInFunctionExists(identifier)) {
            return Functions.getBuiltInFunctionIterator(
                identifier,
                arguments,
                expression.getHighestExecutionMode(),
                iteratorMetadata
            );
        }
        return new StaticUserDefinedFunctionCallIterator(
                identifier,
                arguments,
                expression.getHighestExecutionMode(),
                iteratorMetadata
        );
    }

    @Override
    public RuntimeIterator visitNamedFunctionRef(
            NamedFunctionReferenceExpression expression,
            RuntimeIterator argument
    ) {
        FunctionIdentifier identifier = expression.getIdentifier();
        if (Functions.checkBuiltInFunctionExists(identifier)) {
            throw new UnsupportedFeatureException(
                    "Higher order functions using builtin functions are not supported.",
                    expression.getMetadata()
            );
        }
        if (Functions.checkUserDefinedFunctionExists(identifier)) {
            FunctionItem function = Functions.getUserDefinedFunction(identifier);
            return new FunctionRuntimeIterator(
                    function,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        throw new UnknownFunctionCallException(
                identifier.getName(),
                identifier.getArity(),
                expression.getMetadata()
        );
    }
    // endregion

    // region literal
    @Override
    public RuntimeIterator visitInteger(IntegerLiteralExpression expression, RuntimeIterator argument) {
        return new IntegerRuntimeIterator(
                expression.getValue(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitString(StringLiteralExpression expression, RuntimeIterator argument) {
        return new StringRuntimeIterator(
                expression.getValue(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitDouble(DoubleLiteralExpression expression, RuntimeIterator argument) {
        return new DoubleRuntimeIterator(
                expression.getValue(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitDecimal(DecimalLiteralExpression expression, RuntimeIterator argument) {
        return new DecimalRuntimeIterator(
                expression.getValue(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitNull(NullLiteralExpression expression, RuntimeIterator argument) {
        return new NullRuntimeIterator(expression.getHighestExecutionMode(), expression.getMetadata());
    }

    @Override
    public RuntimeIterator visitBoolean(BooleanLiteralExpression expression, RuntimeIterator argument) {
        return new BooleanRuntimeIterator(
                expression.getValue(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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

        return new AdditiveOperationIterator(
                left,
                right,
                expression.isMinus(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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

        return new MultiplicativeOperationIterator(
                left,
                right,
                expression.getMultiplicativeOperator(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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

        return new AndOperationIterator(
                left,
                right,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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

        return new OrOperationIterator(
                left,
                right,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument) {
        return new NotOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitUnaryExpr(UnaryExpression expression, RuntimeIterator argument) {
        // compute +- final result
        return new UnaryOperationIterator(
                this.visit(expression.getMainExpression(), argument),
                expression.isNegated(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getChildren().get(0), argument);
        RuntimeIterator right = this.visit(expression.getChildren().get(1), argument);
        return new RangeOperationIterator(
                left,
                right,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
        RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
        return new ComparisonOperationIterator(
                left,
                right,
                expression.getOperator(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitStringConcatExpr(StringConcatExpression expression, RuntimeIterator argument) {
        RuntimeIterator left = this.visit(expression.getChildren().get(0), argument);
        RuntimeIterator right = this.visit(expression.getChildren().get(1), argument);
        return new StringConcatIterator(
                left,
                right,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        return new InstanceOfIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitTreatExpression(TreatExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        return new TreatIterator(
                childExpression,
                expression.getsequenceType(),
                true,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitCastableExpression(CastableExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        return new CastableIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitCastExpression(CastExpression expression, RuntimeIterator argument) {
        RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
        return new CastIterator(
                childExpression,
                expression.getSequenceType(),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }
    // endregion

    // region quantifiers
    @Override
    public RuntimeIterator visitQuantifiedExpression(QuantifiedExpression expression, RuntimeIterator argument) {
        List<QuantifiedExpressionVarIterator> variables = new ArrayList<>();
        expression.getVariables()
            .forEach(var -> variables.add((QuantifiedExpressionVarIterator) this.visit(var, argument)));
        RuntimeIterator evaluationExpression = this.visit(expression.getEvaluationExpression(), argument);
        return new QuantifiedExpressionIterator(
                expression.getOperator(),
                variables,
                evaluationExpression,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, RuntimeIterator argument) {
        return new QuantifiedExpressionVarIterator(
                expression.getVariableReference().getVariableName(),
                expression.getSequenceType(),
                this.visit(expression.getExpression(), argument),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }
    // endregion

    // region control
    @Override
    public RuntimeIterator visitConditionalExpression(ConditionalExpression expression, RuntimeIterator argument) {
        return new IfRuntimeIterator(
                this.visit(expression.getCondition(), argument),
                this.visit(expression.getBranch(), argument),
                this.visit(expression.getElseBranch(), argument),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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
        return new SwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                this.visit(expression.getDefaultExpression(), argument),
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
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

        return new TypeswitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                defaultCase,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

}
