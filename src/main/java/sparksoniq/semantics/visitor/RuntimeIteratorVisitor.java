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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.IfExpression;
import org.rumbledb.expressions.control.SwitchCaseExpression;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchCaseExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ForClauseVar;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByClauseVar;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.LetClauseVar;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseExpr;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.operational.AdditiveExpression;
import org.rumbledb.expressions.operational.AndExpression;
import org.rumbledb.expressions.operational.CastExpression;
import org.rumbledb.expressions.operational.CastableExpression;
import org.rumbledb.expressions.operational.ComparisonExpression;
import org.rumbledb.expressions.operational.InstanceOfExpression;
import org.rumbledb.expressions.operational.MultiplicativeExpression;
import org.rumbledb.expressions.operational.NotExpression;
import org.rumbledb.expressions.operational.OrExpression;
import org.rumbledb.expressions.operational.RangeExpression;
import org.rumbledb.expressions.operational.StringConcatExpression;
import org.rumbledb.expressions.operational.TreatExpression;
import org.rumbledb.expressions.operational.UnaryExpression;
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
import org.rumbledb.expressions.primary.ParenthesizedExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.CommaExpressionIterator;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.control.IfRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.control.SwitchRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.control.TypeSwitchCase;
import sparksoniq.jsoniq.runtime.iterator.control.TypeSwitchRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.DynamicFunctionCallIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.FunctionRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.StaticUserDefinedFunctionCallIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.jsoniq.runtime.iterator.operational.AdditiveOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.AndOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.CastIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.CastableIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.InstanceOfIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.MultiplicativeOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.NotOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.OrOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.RangeOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.StringConcatIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.TreatIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.UnaryOperationIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.ArrayLookupIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.ArrayUnboxingIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.ObjectLookupIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.PredicateIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ArrayRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.BooleanRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ContextExpressionIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.DecimalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.DoubleRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.IntegerRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.NullRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.ObjectConstructorRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.StringRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;
import sparksoniq.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionIterator;
import sparksoniq.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionVarIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.spark.iterator.flowr.CountClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.ForClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.GroupByClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.LetClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.OrderByClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.ReturnClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.WhereClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseAnnotatedChildIterator;

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
        FlworClause startClause = expression.getStartClause();
        RuntimeTupleIterator previous = this.visitFlowrClause(startClause, argument, null);
        for (FlworClause clause : expression.getContentClauses()) {
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
            FlworClause clause,
            RuntimeIterator argument,
            RuntimeTupleIterator previousIterator
    ) {
        if (clause instanceof ForClause) {
            for (ForClauseVar var : ((ForClause) clause).getForVariables()) {
                RuntimeIterator assignmentIterator = this.visit(var.getExpression(), argument);
                if (var.getAsSequence() != null && var.getAsSequence().getSequence() != null) {
                    ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                        var.getAsSequence().getSequence(),
                        var.getExpression()
                    );
                    assignmentIterator = new TreatIterator(
                            assignmentIterator,
                            var.getAsSequence().getSequence(),
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
                if (var.getAsSequence() != null && var.getAsSequence().getSequence() != null) {
                    ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                        var.getAsSequence().getSequence(),
                        var.getExpression()
                    );
                    assignmentIterator = new TreatIterator(
                            assignmentIterator,
                            var.getAsSequence().getSequence(),
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
                    if (var.getAsSequence() != null && var.getAsSequence().getSequence() != null) {
                        ExecutionMode executionMode = TreatExpression.calculateIsRDDFromSequenceTypeAndExpression(
                            var.getAsSequence().getSequence(),
                            groupByExpression
                        );
                        groupByExpressionIterator = new TreatIterator(
                                groupByExpressionIterator,
                                var.getAsSequence().getSequence(),
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
    public RuntimeIterator visitParenthesizedExpression(ParenthesizedExpression expression, RuntimeIterator argument) {
        if (expression.getExpression() != null) {
            return defaultAction(expression, argument);
        }
        return new EmptySequenceIterator(expression.getHighestExecutionMode(), expression.getMetadata());
    }

    @Override
    public RuntimeIterator visitPredicateExpression(PredicateExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        ExecutionMode executionMode = mainIterator.getHighestExecutionMode();
        RuntimeIterator filterIterator = this.visit(expression.getPredicateExpression(), argument);
        return new PredicateIterator(
                mainIterator,
                filterIterator,
                executionMode,
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitArrayLookupExpression(ArrayLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        ExecutionMode executionMode = mainIterator.getHighestExecutionMode();
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        return new ArrayLookupIterator(
                mainIterator,
                lookupIterator,
                executionMode,
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitObjectLookupExpression(ObjectLookupExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        ExecutionMode executionMode = mainIterator.getHighestExecutionMode();
        RuntimeIterator lookupIterator = this.visit(expression.getLookupExpression(), argument);
        return new ObjectLookupIterator(
                mainIterator,
                lookupIterator,
                executionMode,
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
        ExecutionMode executionMode = mainIterator.getHighestExecutionMode();
        return new DynamicFunctionCallIterator(
                mainIterator,
                arguments,
                executionMode,
                expression.getMetadata()
        );
    }

    @Override
    public RuntimeIterator visitArrayUnboxingExpression(ArrayUnboxingExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        ExecutionMode executionMode = mainIterator.getHighestExecutionMode();
        return new ArrayUnboxingIterator(
                mainIterator,
                executionMode,
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
        for (Map.Entry<String, FlworVarSequenceType> paramEntry : expression.getParams().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue().getSequence());
        }
        SequenceType returnType = expression.getReturnType().getSequence();
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
        if (expression.isActive()) {
            RuntimeIterator left, right;
            // convert nary to tree of iterators
            if (expression.getOperators().size() > 1) {
                right = this.visit(
                    expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                    argument
                );
                AdditiveExpression remainingExpressions = new AdditiveExpression(
                        expression.getMainExpression(),
                        expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                        expression.getOperators().subList(0, expression.getOperators().size() - 1),
                        expression.getMetadata()
                );
                remainingExpressions.initHighestExecutionMode();
                left = this.visit(
                    remainingExpressions,
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new AdditiveOperationIterator(
                    left,
                    right,
                    expression.getOperators().get(expression.getOperators().size() - 1),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitMultiplicativeExpr(MultiplicativeExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            // convert nary to tree of iterators
            if (expression.getOperators().size() > 1) {
                right = this.visit(
                    expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                    argument
                );
                MultiplicativeExpression remainingExpressions = new MultiplicativeExpression(
                        expression.getMainExpression(),
                        expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                        expression.getOperators().subList(0, expression.getOperators().size() - 1),
                        expression.getMetadata()
                );
                remainingExpressions.initHighestExecutionMode();
                left = this.visit(
                    remainingExpressions,
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new MultiplicativeOperationIterator(
                    left,
                    right,
                    expression.getOperators().get(expression.getOperators().size() - 1),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitAndExpr(AndExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            // convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(
                    expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                    argument
                );
                AndExpression remainingExpressiongs = new AndExpression(
                        expression.getMainExpression(),
                        expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                        expression.getMetadata()
                );
                remainingExpressiongs.initHighestExecutionMode();
                left = this.visit(
                    remainingExpressiongs,
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new AndOperationIterator(
                    left,
                    right,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitOrExpr(OrExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            // convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(
                    expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                    argument
                );
                OrExpression remainingExpressions = new OrExpression(
                        expression.getMainExpression(),
                        expression.getRightExpressions()
                            .subList(0, expression.getRightExpressions().size() - 1),
                        expression.getMetadata()
                );
                remainingExpressions.initHighestExecutionMode();
                left = this.visit(
                    remainingExpressions,
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new OrOperationIterator(
                    left,
                    right,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            return new NotOperationIterator(
                    this.visit(expression.getMainExpression(), argument),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitUnaryExpr(UnaryExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            // compute +- final result
            int result = 1;
            for (OperationalExpressionBase.Operator op : expression.getOperators()) {
                if (op == OperationalExpressionBase.Operator.MINUS) {
                    result *= -1;
                }
            }
            return new UnaryOperationIterator(
                    this.visit(expression.getMainExpression(), argument),
                    result == -1 ? OperationalExpressionBase.Operator.MINUS : OperationalExpressionBase.Operator.PLUS,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new RangeOperationIterator(
                    left,
                    right,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new ComparisonOperationIterator(
                    left,
                    right,
                    expression.getOperator(),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitStringConcatExpr(StringConcatExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            // convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(
                    expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                    argument
                );
                StringConcatExpression remainingExpressions = new StringConcatExpression(
                        expression.getMainExpression(),
                        expression.getRightExpressions()
                            .subList(0, expression.getRightExpressions().size() - 1),
                        expression.getMetadata()
                );
                remainingExpressions.initHighestExecutionMode();
                left = this.visit(
                    remainingExpressions,
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new StringConcatIterator(
                    left,
                    right,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new InstanceOfIterator(
                    childExpression,
                    expression.getsequenceType().getSequence(),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitTreatExpression(TreatExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new TreatIterator(
                    childExpression,
                    expression.getsequenceType().getSequence(),
                    true,
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitCastableExpression(CastableExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new CastableIterator(
                    childExpression,
                    expression.getAtomicType().getSingleType(),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitCastExpression(CastExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new CastIterator(
                    childExpression,
                    expression.getFlworVarSingleType().getSingleType(),
                    expression.getHighestExecutionMode(),
                    expression.getMetadata()
            );
        } else {
            return defaultAction(expression, argument);
        }
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
    public RuntimeIterator visitIfExpression(IfExpression expression, RuntimeIterator argument) {
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
        for (SwitchCaseExpression caseExpression : expression.getCases()) {
            cases.put(
                this.visit(caseExpression.getCondition(), argument),
                this.visit(caseExpression.getReturnExpression(), argument)
            );
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
        List<TypeSwitchCase> cases = new ArrayList<>();
        for (TypeSwitchCaseExpression caseExpression : expression.getCases()) {
            String caseVariableName = null;
            if (caseExpression.getVariableReference() != null) {
                caseVariableName = caseExpression.getVariableReference().getVariableName();
            }
            cases.add(
                new TypeSwitchCase(
                        caseVariableName,
                        caseExpression.getUnion(),
                        this.visit(caseExpression.getReturnExpression(), argument)
                )
            );
        }

        TypeSwitchCase defaultCase;
        String defaultCaseVariableName = null;
        if (expression.getDefaultVariableReferenceExpression() != null) {
            defaultCaseVariableName = expression.getDefaultVariableReferenceExpression().getVariableName();
        }
        defaultCase = new TypeSwitchCase(
                defaultCaseVariableName,
                this.visit(expression.getDefaultExpression(), argument)
        );

        return new TypeSwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                defaultCase,
                expression.getHighestExecutionMode(),
                expression.getMetadata()
        );
    }

}
