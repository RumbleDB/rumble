/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
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
import sparksoniq.jsoniq.compiler.translator.expr.flowr.*;
import sparksoniq.jsoniq.compiler.translator.expr.operational.*;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.*;
import sparksoniq.jsoniq.compiler.translator.expr.primary.*;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import sparksoniq.jsoniq.runtime.iterator.CommaExpressionIterator;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.control.IfRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.control.SwitchRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.jsoniq.runtime.iterator.operational.*;
import sparksoniq.jsoniq.runtime.iterator.postfix.ArrayLookupIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.ArrayUnboxingIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.ObjectLookupIterator;
import sparksoniq.jsoniq.runtime.iterator.postfix.PredicateIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.*;
import sparksoniq.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionIterator;
import sparksoniq.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionVarIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.iterator.flowr.*;
import sparksoniq.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.lang.reflect.Constructor;
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

    //region FLOWR
    @Override
    public RuntimeIterator visitFlowrExpression(FlworExpression expression, RuntimeIterator argument) {
        FlworClause startClause = expression.getStartClause();
        List<FlowrClauseSparkIterator> iterators = new ArrayList<>();
        iterators.addAll(this.visitFlowrClause(startClause, argument));
        for (FlworClause clause : expression.get_contentClauses())
            iterators.addAll(this.visitFlowrClause(clause, argument));
        ReturnClauseSparkIterator returnIterator =
                (ReturnClauseSparkIterator) (this.visitFlowrClause(expression.get_returnClause(), argument).get(0));
        return new FlworExpressionSparkRuntimeIterator(iterators.get(0), iterators.subList(1, iterators.size()),
                returnIterator, createIteratorMetadata(expression));
    }

    private List<FlowrClauseSparkIterator> visitFlowrClause(FlworClause clause,
                                                            RuntimeIterator argument) {
        List<FlowrClauseSparkIterator> result = new ArrayList<>();
        if (clause instanceof ForClause) {
            for (ForClauseVar var : ((ForClause) clause).getForVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(), argument);
                VariableReferenceIterator variableReferenceIterator =
                        (VariableReferenceIterator) this.visit(var.getVariableReference(), argument);
                result.add(new ForClauseSparkIterator(variableReferenceIterator, assignmentExpression,
                        createIteratorMetadata(clause)));
            }
        } else if (clause instanceof LetClause) {
            for (LetClauseVar var : ((LetClause) clause).getLetVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(), argument);
                VariableReferenceIterator variableReferenceIterator =
                        (VariableReferenceIterator) this.visit(var.getVariableReference(), argument);
                result.add(new LetClauseSparkIterator(variableReferenceIterator, assignmentExpression,
                        createIteratorMetadata(clause)));
            }
        } else if (clause instanceof GroupByClause) {
            List<GroupByClauseSparkIteratorExpression> expressions = new ArrayList<>();
            for (GroupByClauseVar groupExpr : ((GroupByClause) clause).getGroupVariables()) {
                expressions.add(new GroupByClauseSparkIteratorExpression(
                        groupExpr.getExpression() != null ? this.visit(groupExpr.getExpression(), argument) : null,
                        (VariableReferenceIterator) this.visit(groupExpr.getVariableReference(), argument),
                        createIteratorMetadata(groupExpr)));
            }
            result.add(new GroupByClauseSparkIterator(expressions, createIteratorMetadata(clause)));
        } else if (clause instanceof OrderByClause) {
            List<OrderByClauseSparkIteratorExpression> expressions = new ArrayList<>();
            for (OrderByClauseExpr orderExpr : ((OrderByClause) clause).getExpressions()) {
                expressions.add(new OrderByClauseSparkIteratorExpression(this.visit(orderExpr.getExpression(), argument),
                        orderExpr.isAscending(), orderExpr.getUri(), orderExpr.getEmptyOrder(), createIteratorMetadata(orderExpr)));
            }
            result.add(new OrderByClauseSparkIterator(expressions, ((OrderByClause) clause).isStable(),
                    createIteratorMetadata(clause)));
        } else if (clause instanceof ReturnClause) {
            result.add(new ReturnClauseSparkIterator(this.visit(((ReturnClause) clause).getReturnExpr(), argument),
                    createIteratorMetadata(clause)));
        } else if (clause instanceof WhereClause) {
            result.add(new WhereClauseSparkIterator(this.visit(((WhereClause) clause).getWhereExpression(), argument),
                    createIteratorMetadata(clause)));
        }
        return result;
    }

    @Override
    public RuntimeIterator visitOrderByClauseExpr(OrderByClauseExpr expression, RuntimeIterator argument) {
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitVariableReference(VariableReference expression, RuntimeIterator argument) {
        return new VariableReferenceIterator(expression.getVariableName(), expression.getType(),
                createIteratorMetadata(expression));
    }
    //endregion

    //region primary
    @Override
    public RuntimeIterator visitPostfixExpression(PostFixExpression expression, RuntimeIterator argument) {
        if (expression.isPrimary()) {
            return defaultAction(expression, argument);
        } else {
            RuntimeIterator previous = this.visit(expression.get_primaryExpressionNode(), argument);
            for (PostfixExtension extension : expression.getExtensions()) {
                try {
                    if (extension instanceof ArrayLookupExtension) {
                        RuntimeIterator iterator =
                                this.visit(((ArrayLookupExtension) extension).getExpression(), argument);
                        previous = new ArrayLookupIterator(previous, iterator, createIteratorMetadata(expression));
                    }
                    if (extension instanceof ObjectLookupExtension) {
                        RuntimeIterator iterator =
                                this.visit(((ObjectLookupExtension) extension).getExpression(), argument);
                        previous = new ObjectLookupIterator(previous, iterator, createIteratorMetadata(expression));
                    }
                    if (extension instanceof ArrayUnboxingExtension) {
                        previous = new ArrayUnboxingIterator(previous, createIteratorMetadata(expression));
                    }
                    if (extension instanceof PredicateExtension) {
                        RuntimeIterator filterExpression = //pass the predicate as argument for $$ expresions
                                this.visit(((PredicateExtension) extension).getExpression(), argument);
                        previous = new PredicateIterator(previous, filterExpression, createIteratorMetadata(expression));
                    }
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
        if (expression.getExpression() != null)
            result = this.visit(expression.getExpression(), argument);
        return new ArrayRuntimeIterator(result, createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitObjectConstructor(ObjectConstructor expression, RuntimeIterator argument) {
        if (expression.isMergedConstructor()) {
            List<RuntimeIterator> childExpressions = new ArrayList<>();
            for (Expression child : expression.getChildExpression().getExpressions())
                childExpressions.add((this.visit(child, argument)));
            return new ObjectConstructorRuntimeIterator(childExpressions, createIteratorMetadata(expression));
        } else {
            List<RuntimeIterator> keys = new ArrayList<>();
            List<RuntimeIterator> values = new ArrayList<>();
            for (Expression key : expression.getKeys())
                keys.add(this.visit(key, argument));
            for (Expression value : expression.getValues())
                values.add(this.visit(value, argument));
            return new ObjectConstructorRuntimeIterator(keys, values, createIteratorMetadata(expression));
        }
    }

    @Override
    public RuntimeIterator visitContextExpr(ContextExpression expression, RuntimeIterator argument) {
        return new ContextExpressionIterator(createIteratorMetadata(expression));
    }
    //endregion

    //region literals
    @Override
    public RuntimeIterator visitInteger(IntegerLiteral expression, RuntimeIterator argument) {
        return new IntegerRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitString(StringLiteral expression, RuntimeIterator argument) {
        return new StringRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitDouble(DoubleLiteral expression, RuntimeIterator argument) {
        return new DoubleRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitDecimal(DecimalLiteral expression, RuntimeIterator argument) {
        return new DecimalRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitNull(NullLiteral expression, RuntimeIterator argument) {
        return new NullRuntimeIterator(createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitBoolean(BooleanLiteral expression, RuntimeIterator argument) {
        return new BooleanRuntimeIterator(expression.getValue(), createIteratorMetadata(expression));
    }
    //endregion

    //region operational
    @Override
    public RuntimeIterator visitAdditiveExpr(AdditiveExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if (expression.getOperators().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new AdditiveExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                                expression.getOperators().subList(0, expression.getOperators().size() - 1), expression.getMetadata()), argument);
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }


            return new AdditiveOperationIterator(left, right,
                    expression.getOperators().get(expression.getOperators().size() - 1), createIteratorMetadata(expression));

        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitMultiplicativeExpr(MultiplicativeExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if (expression.getOperators().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new MultiplicativeExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                                expression.getOperators().subList(0, expression.getOperators().size() - 1), expression.getMetadata()), argument);
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }


            return new MultiplicativeOperationIterator(left, right,
                    expression.getOperators().get(expression.getOperators().size() - 1), createIteratorMetadata(expression));

        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitAndExpr(AndExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new AndExpression(expression.getMainExpression(),
                                expression.getRightExpressions().
                                        subList(0, expression.getRightExpressions().size() - 1),
                                expression.getMetadata()), argument);
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new AndOperationIterator(left, right, createIteratorMetadata(expression));

        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitOrExpr(OrExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new OrExpression(expression.getMainExpression(),
                                expression.getRightExpressions()
                                        .subList(0, expression.getRightExpressions().size() - 1),
                                expression.getMetadata()), argument);
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new OrOperationIterator(left, right, createIteratorMetadata(expression));

        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument) {
        if (expression.isActive())
            return new NotOperationIterator(this.visit(expression.getMainExpression(), argument),
                    createIteratorMetadata(expression));
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitUnaryExpr(UnaryExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            //compute +- final result
            int result = 1;
            for (OperationalExpressionBase.Operator op : expression.getOperators())
                if (op == OperationalExpressionBase.Operator.MINUS)
                    result *= -1;
            return new UnaryOperationIterator(this.visit(expression.getMainExpression(), argument),
                    result == -1 ? OperationalExpressionBase.Operator.MINUS : OperationalExpressionBase.Operator.PLUS,
                    createIteratorMetadata(expression));
        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new RangeOperationIterator(left, right, createIteratorMetadata(expression));
        } else
            return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new ComparisonOperationIterator(left, right, expression.getOperator(),
                    createIteratorMetadata(expression));
        } else
            return defaultAction(expression, argument);
    }

    //endregion
    @Override
    public RuntimeIterator visitCommaExpression(CommaExpression expression, RuntimeIterator argument) {
        List<RuntimeIterator> result = new ArrayList<>();
        for (Expression childExpr : expression.getExpressions())
            result.add(this.visit(childExpr, argument));
        if (result.size() == 1)
            return result.get(0);
        else
            return new CommaExpressionIterator(result, createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitParenthesizedExpression(ParenthesizedExpression expression, RuntimeIterator argument) {
        if (expression.getExpression() != null)
            return defaultAction(expression, argument);
        return new EmptySequenceIterator(createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitFunctionCall(FunctionCall expression, RuntimeIterator argument) {
        List<RuntimeIterator> arguments = new ArrayList<>();
        IteratorMetadata iteratorMetadata = createIteratorMetadata(expression);
        for (Expression arg : expression.getParameters())
            arguments.add(this.visit(arg, argument));

        try {
            Class<? extends RuntimeIterator> functionClass = Functions.getFunctionIteratorClass(expression, arguments);
            Constructor<? extends RuntimeIterator> ctor = functionClass.getConstructor(List.class, IteratorMetadata.class);
            return ctor.newInstance(arguments, iteratorMetadata);
        } catch (Exception e) {
            throw new UnknownFunctionCallException(expression.getFunctionName(), arguments.size(), createIteratorMetadata(expression));
        }
    }

    @Override
    public RuntimeIterator visitStringConcatExpr(StringConcatExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if (expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new StringConcatExpression(expression.getMainExpression(),
                                expression.getRightExpressions()
                                        .subList(0, expression.getRightExpressions().size() - 1),
                                expression.getMetadata()), argument);
            } else {
                left = this.visit(expression.getMainExpression(), argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new StringConcatIterator(left, right, createIteratorMetadata(expression));

        }
        return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument) {
        if (expression.isActive()) {
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new InstanceOfIterator(childExpression, expression.getsequenceType().getSequence(),
                    createIteratorMetadata(expression));
        } else
            return defaultAction(expression, argument);
    }

    @Override
    public RuntimeIterator visitIfExpression(IfExpression expression, RuntimeIterator argument) {
        return new IfRuntimeIterator(this.visit(expression.getCondition(), argument),
                this.visit(expression.getBranch(), argument),
                this.visit(expression.getElseBranch(), argument),
                createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitQuantifiedExpression(QuantifiedExpression expression, RuntimeIterator argument) {
        List<QuantifiedExpressionVarIterator> variables = new ArrayList<>();
        expression.getVariables().forEach(var -> variables.add((QuantifiedExpressionVarIterator) this.visit(var, argument)));
        RuntimeIterator evaluationExpression = this.visit(expression.getEvaluationExpression(), argument);
        return new QuantifiedExpressionIterator(expression.getOperator(),
                variables, evaluationExpression, createIteratorMetadata(expression));
    }

    @Override
    public RuntimeIterator visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, RuntimeIterator argument) {
        QuantifiedExpressionVarIterator iterator;
        iterator = new QuantifiedExpressionVarIterator(expression.getVariableReference().getVariableName(),
                expression.getSequenceType(), this.visit(expression.getExpression(), argument),
                createIteratorMetadata(expression));
        return iterator;
    }

    @Override
    public RuntimeIterator visitSwitchExpression(SwitchExpression expression, RuntimeIterator argument) {
        Map<RuntimeIterator, RuntimeIterator> cases = new LinkedHashMap<>();
        for (SwitchCaseExpression caseExpression : expression.getCases())
            cases.put(this.visit(caseExpression.getCondition(), argument),
                    this.visit(caseExpression.getReturnExpression(), argument));
        return new SwitchRuntimeIterator(this.visit(expression.getTestCondition(), argument),
                cases, this.visit(expression.getDefaultExpression(), argument),
                createIteratorMetadata(expression));
    }

    private IteratorMetadata createIteratorMetadata(ExpressionOrClause expression) {
        return new IteratorMetadata(expression.getMetadata());
    }
}
