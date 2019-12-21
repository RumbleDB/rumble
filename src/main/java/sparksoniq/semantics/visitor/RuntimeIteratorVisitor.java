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

import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.exceptions.UnsupportedFeatureException;
import sparksoniq.jsoniq.compiler.translator.expr.CommaExpression;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.control.IfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.SwitchCaseExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.SwitchExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchCaseExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.CountClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.WhereClause;
import sparksoniq.jsoniq.compiler.translator.expr.module.MainModule;
import sparksoniq.jsoniq.compiler.translator.expr.module.Prolog;
import sparksoniq.jsoniq.compiler.translator.expr.operational.AdditiveExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.AndExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.CastExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.CastableExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.ComparisonExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.InstanceOfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.MultiplicativeExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.NotExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.OrExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.RangeExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.StringConcatExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.TreatExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.UnaryExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ArrayLookupExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ArrayUnboxingExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.DynamicFunctionCallExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ObjectLookupExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PostfixExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PredicateExtension;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ArgumentPlaceholder;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ArrayConstructor;
import sparksoniq.jsoniq.compiler.translator.expr.primary.BooleanLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ContextExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.DecimalLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.DoubleLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionCall;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionDeclaration;
import sparksoniq.jsoniq.compiler.translator.expr.primary.IntegerLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.NamedFunctionRef;
import sparksoniq.jsoniq.compiler.translator.expr.primary.NullLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ObjectConstructor;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ParenthesizedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.StringLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
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
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
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

public class RuntimeIteratorVisitor extends AbstractExpressionOrClauseVisitor<RuntimeIterator> {

    @Override
    public RuntimeIterator visit(ExpressionOrClause expression, RuntimeIterator argument) {
        return expression.accept(this, argument);
    }

    @Override
    public RuntimeIterator visitDescendants(ExpressionOrClause expression, RuntimeIterator argument) {
        RuntimeIterator result = argument;
        for (ExpressionOrClause child : expression.getDescendants()) {
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
            CommaExpressionIterator iterator = new CommaExpressionIterator(result, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
        RuntimeTupleIterator previous = null;
        previous = this.visitFlowrClause(startClause, argument, null);
        for (FlworClause clause : expression.get_contentClauses()) {
            previous = this.visitFlowrClause(clause, argument, previous);
        }
        RuntimeIterator returnIterator = new ReturnClauseSparkIterator(
                previous,
                this.visit(
                    (expression.get_returnClause()).getReturnExpr(),
                    argument
                ),
                createIteratorMetadata(expression.get_returnClause())
        );
        returnIterator.setIsRDD(expression.get_returnClause().isRDD());
        returnIterator.setIsDataFrame(expression.get_returnClause().isDataFrame());
        return returnIterator;
    }

    private RuntimeTupleIterator visitFlowrClause(
            FlworClause clause,
            RuntimeIterator argument,
            RuntimeTupleIterator previousIterator
    ) {
        if (clause instanceof ForClause) {
            for (ForClauseVar var : ((ForClause) clause).getForVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(), argument);
                if (var.getAsSequence() != null && var.getAsSequence().getSequence() != null) {
                    assignmentExpression = new TreatIterator(
                            assignmentExpression,
                            var.getAsSequence().getSequence(),
                            false,
                            createIteratorMetadata(clause)
                    );
                    TreatExpression.setIsRDDIsDataFrameOfTreatIteratorGeneratedWithoutTreatExpression(
                            assignmentExpression,
                            var.getAsSequence().getSequence(),
                            var.getExpression()
                    );
                }

                previousIterator = new ForClauseSparkIterator(
                        previousIterator,
                        var.getVariableReference().getVariableName(),
                        assignmentExpression,
                        createIteratorMetadata(clause)
                );
                previousIterator.setIsDataFrame(var.isDataFrame());
            }
        } else if (clause instanceof LetClause) {
            for (LetClauseVar var : ((LetClause) clause).getLetVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(), argument);
                if (var.getAsSequence() != null && var.getAsSequence().getSequence() != null) {
                    assignmentExpression = new TreatIterator(
                            assignmentExpression,
                            var.getAsSequence().getSequence(),
                            false,
                            createIteratorMetadata(clause)
                    );
                    TreatExpression.setIsRDDIsDataFrameOfTreatIteratorGeneratedWithoutTreatExpression(
                            assignmentExpression,
                            var.getAsSequence().getSequence(),
                            var.getExpression()
                    );
                }

                previousIterator = new LetClauseSparkIterator(
                        previousIterator,
                        var.getVariableReference().getVariableName(),
                        assignmentExpression,
                        createIteratorMetadata(clause)
                );
                previousIterator.setIsDataFrame(var.isDataFrame());
            }
        } else if (clause instanceof GroupByClause) {
            List<GroupByClauseSparkIteratorExpression> expressions = new ArrayList<>();
            for (GroupByClauseVar groupExpr : ((GroupByClause) clause).getGroupVariables()) {
                RuntimeIterator groupByExpression = null;
                if (groupExpr.getExpression() != null) {
                    groupByExpression = this.visit(groupExpr.getExpression(), argument);
                    if (groupExpr.getAsSequence() != null && groupExpr.getAsSequence().getSequence() != null) {
                        groupByExpression = new TreatIterator(
                                groupByExpression,
                                groupExpr.getAsSequence().getSequence(),
                                false,
                                createIteratorMetadata(clause)
                        );
                        TreatExpression.setIsRDDIsDataFrameOfTreatIteratorGeneratedWithoutTreatExpression(
                                groupByExpression,
                                groupExpr.getAsSequence().getSequence(),
                                groupExpr.getExpression()
                        );
                    }
                }

                VariableReference variableReference = groupExpr.getVariableReference();
                VariableReferenceIterator variableReferenceIterator =
                        (VariableReferenceIterator) this.visit(variableReference, argument);
                variableReferenceIterator.setIsRDD(variableReference.isRDD());
                variableReferenceIterator.setIsDataFrame(variableReference.isDataFrame());

                expressions.add(
                    new GroupByClauseSparkIteratorExpression(
                            groupByExpression,
                            variableReferenceIterator,
                            createIteratorMetadata(groupExpr)
                    )
                );
            }
            previousIterator = new GroupByClauseSparkIterator(
                    previousIterator,
                    expressions,
                    createIteratorMetadata(clause)
            );
            previousIterator.setIsDataFrame(clause.isDataFrame());
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
                    createIteratorMetadata(clause)
            );
            previousIterator.setIsDataFrame(clause.isDataFrame());
        } else if (clause instanceof WhereClause) {
            previousIterator = new WhereClauseSparkIterator(
                    previousIterator,
                    this.visit(((WhereClause) clause).getWhereExpression(), argument),
                    createIteratorMetadata(clause)
            );
            previousIterator.setIsDataFrame(clause.isDataFrame());
        } else if (clause instanceof CountClause) {
            previousIterator = new CountClauseSparkIterator(
                    previousIterator,
                    this.visit(((CountClause) clause).getCountVariable(), argument),
                    createIteratorMetadata(clause)
            );
            previousIterator.setIsDataFrame(clause.isDataFrame());
        }
        return previousIterator;
    }

    @Override
    public RuntimeIterator visitOrderByClauseExpr(OrderByClauseExpr expression, RuntimeIterator argument) {
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitVariableReference(VariableReference expression, RuntimeIterator argument) {
        RuntimeIterator variableReferenceIterator = new VariableReferenceIterator(
                expression.getVariableName(),
                expression.getType(),
                createIteratorMetadata(expression)
        );
        variableReferenceIterator.setIsRDD(expression.isRDD());
        variableReferenceIterator.setIsDataFrame(expression.isDataFrame());
        return variableReferenceIterator;
    }
    // endregion

    // region primary
    @Override
    public RuntimeIterator visitParenthesizedExpression(ParenthesizedExpression expression, RuntimeIterator argument) {
        if (expression.getExpression() != null) {
            return defaultAction(expression, argument);
        }
        RuntimeIterator iterator = new EmptySequenceIterator(createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitPostfixExpression(PostFixExpression expression, RuntimeIterator argument) {
        if (expression.isPrimary()) {
            return defaultAction(expression, argument);
        } else {
            RuntimeIterator previous = this.visit(expression.get_primaryExpressionNode(), argument);
            previous.setIsRDD(expression.isRDD());
            previous.setIsDataFrame(expression.isDataFrame());
            for (PostfixExtension extension : expression.getExtensions()) {
                try {
                    if (extension instanceof ArrayLookupExtension) {
                        RuntimeIterator iterator =
                            this.visit(((ArrayLookupExtension) extension).getExpression(), argument);
                        previous = new ArrayLookupIterator(previous, iterator, createIteratorMetadata(expression));
                    } else if (extension instanceof ObjectLookupExtension) {
                        RuntimeIterator iterator =
                            this.visit(((ObjectLookupExtension) extension).getExpression(), argument);
                        previous = new ObjectLookupIterator(previous, iterator, createIteratorMetadata(expression));
                    } else if (extension instanceof ArrayUnboxingExtension) {
                        previous = new ArrayUnboxingIterator(previous, createIteratorMetadata(expression));
                    } else if (extension instanceof PredicateExtension) {
                        RuntimeIterator filterExpression = // pass the predicate as argument for $$ expresions
                            this.visit(((PredicateExtension) extension).getExpression(), argument);
                        previous = new PredicateIterator(
                                previous,
                                filterExpression,
                                createIteratorMetadata(expression)
                        );
                    } else if (extension instanceof DynamicFunctionCallExtension) {
                        List<RuntimeIterator> arguments = new ArrayList<>();
                        for (Expression arg : ((DynamicFunctionCallExtension) extension).getArguments()) {
                            if (arg == null) { // check ArgumentPlaceholder
                                arguments.add(null);
                            } else {
                                arguments.add(this.visit(arg, argument));
                            }
                        }
                        IteratorMetadata metadata = createIteratorMetadata(expression);
                        previous = new DynamicFunctionCallIterator(previous, arguments, metadata);
                    }
                    previous.setIsRDD(extension.isRDD());
                    previous.setIsDataFrame(extension.isDataFrame());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new UnsupportedFeatureException("Invalid Postfix extension", expression.getMetadata());
                }
            }
            return previous;
        }
    }

    @Override
    public RuntimeIterator visitArrayConstructor(ArrayConstructor expression, RuntimeIterator argument) {
        RuntimeIterator result = null;
        if (expression.getExpression() != null) {
            result = this.visit(expression.getExpression(), argument);
        }
        RuntimeIterator iterator = new ArrayRuntimeIterator(result, createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitObjectConstructor(ObjectConstructor expression, RuntimeIterator argument) {
        RuntimeIterator iterator;
        if (expression.isMergedConstructor()) {
            List<RuntimeIterator> childExpressions = new ArrayList<>();
            for (Expression child : expression.getChildExpression().getExpressions()) {
                childExpressions.add((this.visit(child, argument)));
            }
            iterator = new ObjectConstructorRuntimeIterator(childExpressions, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        } else {
            List<RuntimeIterator> keys = new ArrayList<>();
            List<RuntimeIterator> values = new ArrayList<>();
            for (Expression key : expression.getKeys()) {
                keys.add(this.visit(key, argument));
            }
            for (Expression value : expression.getValues()) {
                values.add(this.visit(value, argument));
            }
            iterator = new ObjectConstructorRuntimeIterator(keys, values, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        }
    }

    @Override
    public RuntimeIterator visitContextExpr(ContextExpression expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new ContextExpressionIterator(createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitFunctionDeclaration(FunctionDeclaration expression, RuntimeIterator argument) {
        Map<String, SequenceType> paramNameToSequenceTypes = new LinkedHashMap<>();
        for (Map.Entry<String, FlworVarSequenceType> paramEntry : expression.get_params().entrySet()) {
            paramNameToSequenceTypes.put(paramEntry.getKey(), paramEntry.getValue().getSequence());
        }
        SequenceType returnType = expression.get_returnType().getSequence();
        RuntimeIterator bodyIterator = this.visit(expression.get_body(), argument);
        FunctionItem function = new FunctionItem(
                expression.get_name(),
                paramNameToSequenceTypes,
                returnType,
                bodyIterator
        );
        if (expression.get_name().equals("")) {
            // unnamed (inline function declaration)
            RuntimeIterator iterator = new FunctionRuntimeIterator(function, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        } else {
            // named (static function declaration)
            Functions.addUserDefinedFunction(function, expression.getMetadata());
        }

        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitFunctionCall(FunctionCall expression, RuntimeIterator argument) {
        List<RuntimeIterator> arguments = new ArrayList<>();
        IteratorMetadata iteratorMetadata = createIteratorMetadata(expression);
        boolean isPartialApplication = false;
        for (Expression arg : expression.getArguments()) {
            if (arg instanceof ArgumentPlaceholder) {
                arguments.add(null);
                isPartialApplication = true;
            } else {
                RuntimeIterator argumentIterator = this.visit(arg, argument);
                argumentIterator.setIsRDD(arg.isRDD());
                argumentIterator.setIsDataFrame(arg.isDataFrame());
                arguments.add(argumentIterator);
            }
        }
        String fnName = expression.getFunctionName();
        int arity = arguments.size();
        FunctionIdentifier identifier = new FunctionIdentifier(fnName, arity);

        if (Functions.checkBuiltInFunctionExists(identifier)) {
            if (isPartialApplication) {
                throw new UnsupportedFeatureException(
                        "Partial application on built-in functions are not supported.",
                        expression.getMetadata()
                );
            }
            return Functions.getBuiltInFunctionIterator(
                identifier,
                arguments,
                expression.isRDD(),
                expression.isDataFrame(),
                iteratorMetadata
            );
        }
        RuntimeIterator iterator = new StaticUserDefinedFunctionCallIterator(identifier, arguments, iteratorMetadata);
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitNamedFunctionRef(NamedFunctionRef expression, RuntimeIterator argument) {
        FunctionIdentifier identifier = expression.getIdentifier();
        if (Functions.checkBuiltInFunctionExists(identifier)) {
            throw new UnsupportedFeatureException(
                    "Higher order functions using builtin functions are not supported.",
                    expression.getMetadata()
            );
        }
        if (Functions.checkUserDefinedFunctionExists(identifier)) {
            FunctionItem function = Functions.getUserDefinedFunction(identifier);
            RuntimeIterator iterator = new FunctionRuntimeIterator(function, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
    public RuntimeIterator visitInteger(IntegerLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new IntegerRuntimeIterator(
                expression.getValue(),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitString(StringLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new StringRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitDouble(DoubleLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new DoubleRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitDecimal(DecimalLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new DecimalRuntimeIterator(
                expression.getValue(),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitNull(NullLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new NullRuntimeIterator(createIteratorMetadata(expression));
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitBoolean(BooleanLiteral expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new BooleanRuntimeIterator(
                expression.getValue(),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
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
                left = this.visit(
                    new AdditiveExpression(
                            expression.getMainExpression(),
                            expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                            expression.getOperators().subList(0, expression.getOperators().size() - 1),
                            expression.getMetadata()
                    ),
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            RuntimeIterator iterator = new AdditiveOperationIterator(
                    left,
                    right,
                    expression.getOperators().get(expression.getOperators().size() - 1),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
                left = this.visit(
                    new MultiplicativeExpression(
                            expression.getMainExpression(),
                            expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                            expression.getOperators().subList(0, expression.getOperators().size() - 1),
                            expression.getMetadata()
                    ),
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            RuntimeIterator iterator = new MultiplicativeOperationIterator(
                    left,
                    right,
                    expression.getOperators().get(expression.getOperators().size() - 1),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
                left = this.visit(
                    new AndExpression(
                            expression.getMainExpression(),
                            expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                            expression.getMetadata()
                    ),
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            RuntimeIterator iterator = new AndOperationIterator(left, right, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
                left = this.visit(
                    new OrExpression(
                            expression.getMainExpression(),
                            expression.getRightExpressions()
                                .subList(0, expression.getRightExpressions().size() - 1),
                            expression.getMetadata()
                    ),
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            RuntimeIterator iterator = new OrOperationIterator(left, right, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator iterator = new NotOperationIterator(
                    this.visit(expression.getMainExpression(), argument),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
            RuntimeIterator iterator = new UnaryOperationIterator(
                    this.visit(expression.getMainExpression(), argument),
                    result == -1 ? OperationalExpressionBase.Operator.MINUS : OperationalExpressionBase.Operator.PLUS,
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            RuntimeIterator iterator = new RangeOperationIterator(left, right, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            RuntimeIterator iterator = new ComparisonOperationIterator(
                    left,
                    right,
                    expression.getOperator(),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
                left = this.visit(
                    new StringConcatExpression(
                            expression.getMainExpression(),
                            expression.getRightExpressions()
                                .subList(0, expression.getRightExpressions().size() - 1),
                            expression.getMetadata()
                    ),
                    argument
                );
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            RuntimeIterator iterator = new StringConcatIterator(left, right, createIteratorMetadata(expression));
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator iterator = new InstanceOfIterator(
                    childExpression,
                    expression.getsequenceType().getSequence(),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitTreatExpression(TreatExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator treatIterator = new TreatIterator(
                    childExpression,
                    expression.getsequenceType().getSequence(),
                    true,
                    createIteratorMetadata(expression)
            );
            treatIterator.setIsRDD(expression.isRDD());
            treatIterator.setIsDataFrame(expression.isDataFrame());
            return treatIterator;
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitCastableExpression(CastableExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator iterator = new CastableIterator(
                    childExpression,
                    expression.get_atomicType().getSingleType(),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
        } else {
            return defaultAction(expression, argument);
        }
    }

    @Override
    public RuntimeIterator visitCastExpression(CastExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator iterator = new CastIterator(
                    childExpression,
                    expression.getFlworVarSingleType().getSingleType(),
                    createIteratorMetadata(expression)
            );
            iterator.setIsRDD(expression.isRDD());
            iterator.setIsDataFrame(expression.isDataFrame());
            return iterator;
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
        RuntimeIterator iterator = new QuantifiedExpressionIterator(
                expression.getOperator(),
                variables,
                evaluationExpression,
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    @Override
    public RuntimeIterator visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, RuntimeIterator argument) {
        QuantifiedExpressionVarIterator iterator;
        iterator = new QuantifiedExpressionVarIterator(
                expression.getVariableReference().getVariableName(),
                expression.getSequenceType(),
                this.visit(expression.getExpression(), argument),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }
    // endregion

    // region control
    @Override
    public RuntimeIterator visitIfExpression(IfExpression expression, RuntimeIterator argument) {
        RuntimeIterator iterator = new IfRuntimeIterator(
                this.visit(expression.getCondition(), argument),
                this.visit(expression.getBranch(), argument),
                this.visit(expression.getElseBranch(), argument),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
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
        RuntimeIterator iterator = new SwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                this.visit(expression.getDefaultExpression(), argument),
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
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
        if (expression.getVarRefDefault() != null) {
            defaultCaseVariableName = expression.getVarRefDefault().getVariableName();
        }
        defaultCase = new TypeSwitchCase(
                defaultCaseVariableName,
                this.visit(expression.getDefaultExpression(), argument)
        );

        RuntimeIterator iterator = new TypeSwitchRuntimeIterator(
                this.visit(expression.getTestCondition(), argument),
                cases,
                defaultCase,
                createIteratorMetadata(expression)
        );
        iterator.setIsRDD(expression.isRDD());
        iterator.setIsDataFrame(expression.isDataFrame());
        return iterator;
    }

    private IteratorMetadata createIteratorMetadata(ExpressionOrClause expression) {
        return new IteratorMetadata(expression.getMetadata());
    }
}
