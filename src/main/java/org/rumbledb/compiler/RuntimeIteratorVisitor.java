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
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
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
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
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
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.RuntimeTupleIterator;
import org.rumbledb.runtime.arithmetics.AdditiveOperationIterator;
import org.rumbledb.runtime.arithmetics.MultiplicativeOperationIterator;
import org.rumbledb.runtime.arithmetics.UnaryOperationIterator;
import org.rumbledb.runtime.control.AtMostOneItemIfRuntimeIterator;
import org.rumbledb.runtime.control.IfRuntimeIterator;
import org.rumbledb.runtime.control.SwitchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIterator;
import org.rumbledb.runtime.control.TypeswitchRuntimeIteratorCase;
import org.rumbledb.runtime.control.TryCatchRuntimeIterator;
import org.rumbledb.runtime.flwor.clauses.CountClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.ForClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.GroupByClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.LetClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseSparkIterator;
import org.rumbledb.runtime.flwor.clauses.ReturnClauseSparkIterator;
import org.rumbledb.runtime.flwor.expression.SimpleMapExpressionIterator;
import org.rumbledb.runtime.flwor.clauses.WhereClauseSparkIterator;
import org.rumbledb.runtime.flwor.expression.GroupByClauseSparkIteratorExpression;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.runtime.functions.FunctionRuntimeIterator;
import org.rumbledb.runtime.functions.NamedFunctionRefRuntimeIterator;
import org.rumbledb.runtime.functions.StaticUserDefinedFunctionCallIterator;
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
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.CastableIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.runtime.typing.TreatIterator;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
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
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
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
                returnClause.getStaticContextForRuntime(this.config, this.visitorConfig)
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

    // region primary
    @Override
    public RuntimeIterator visitFilterExpression(FilterExpression expression, RuntimeIterator argument) {
        RuntimeIterator mainIterator = this.visit(expression.getMainExpression(), argument);
        if (expression.getPredicateExpression() instanceof IntegerLiteralExpression) {
            String lexicalValue = ((IntegerLiteralExpression) expression.getPredicateExpression()).getLexicalValue();
            if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                int n = ItemFactory.getInstance().createIntegerItem(lexicalValue).getIntValue();
                if (n <= this.config.getResultSizeCap()) {
                    RuntimeIterator runtimeIterator = new SequenceLookupIterator(
                            mainIterator,
                            n,
                            expression.getStaticContextForRuntime(this.config, this.visitorConfig)
                    );
                    runtimeIterator.setStaticContext(expression.getStaticContext());
                    return runtimeIterator;
                }
            }
        }
        RuntimeIterator filterIterator = this.visit(expression.getPredicateExpression(), argument);
        RuntimeIterator runtimeIterator = new PredicateIterator(
                mainIterator,
                filterIterator,
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
        );
        runtimeIterator.setStaticContext(expression.getStaticContext());
        return runtimeIterator;
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
                expression.getStaticContextForRuntime(this.config, this.visitorConfig)
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
                    expression.getStaticContextForRuntime(this.config, this.visitorConfig)
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
                expression.getsequenceType(),
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

}
