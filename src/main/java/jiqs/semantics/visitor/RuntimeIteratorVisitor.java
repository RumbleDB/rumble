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
 package jiqs.semantics.visitor;

import jiqs.jsoniq.compiler.translator.expr.control.IfExpression;
import jiqs.jsoniq.compiler.translator.expr.flowr.*;
import jiqs.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import jiqs.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import jiqs.jsoniq.compiler.translator.expr.CommaExpression;
import jiqs.jsoniq.compiler.translator.expr.Expression;
import jiqs.jsoniq.compiler.translator.expr.ExpressionOrClause;
import jiqs.jsoniq.compiler.translator.expr.operational.*;
import jiqs.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import jiqs.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import jiqs.jsoniq.compiler.translator.expr.postfix.extensions.*;
import jiqs.jsoniq.compiler.translator.expr.primary.*;
import jiqs.jsoniq.exceptions.UnknownFunctionCallException;
import jiqs.jsoniq.exceptions.UnsupportedFeatureException;
import jiqs.jsoniq.runtime.iterator.CommaExpressionIterator;
import jiqs.jsoniq.runtime.iterator.EmptySequenceIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.jsoniq.runtime.iterator.control.IfRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.functions.ArrayFunctionIterator;
import jiqs.jsoniq.runtime.iterator.functions.CountFunctionIterator;
import jiqs.jsoniq.runtime.iterator.functions.ArithmeticFunctionIterator;
import jiqs.jsoniq.runtime.iterator.functions.ObjectFunctionIterator;
import jiqs.jsoniq.runtime.iterator.functions.base.Functions;
import jiqs.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionIterator;
import jiqs.jsoniq.runtime.iterator.quantifiers.QuantifiedExpressionVarIterator;
import jiqs.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;
import jiqs.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;
import jiqs.spark.iterator.flowr.*;
import jiqs.spark.iterator.flowr.base.FlowrClauseSparkIterator;
import jiqs.spark.iterator.function.ParallelizeFunctionIterator;
import jiqs.spark.iterator.function.ParseJsonFunctionIterator;
import jiqs.jsoniq.runtime.iterator.operational.*;
import jiqs.jsoniq.runtime.iterator.postfix.ArrayLookupItertor;
import jiqs.jsoniq.runtime.iterator.postfix.ArrayUnboxingItertor;
import jiqs.jsoniq.runtime.iterator.postfix.ObjectLookupItertor;
import jiqs.jsoniq.runtime.iterator.postfix.PredicateIterator;
import jiqs.jsoniq.runtime.iterator.primary.*;

import java.util.ArrayList;
import java.util.List;

import static jiqs.jsoniq.runtime.iterator.functions.ArithmeticFunctionIterator.*;

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
        for(FlworClause clause : expression.get_contentClauses())
            iterators.addAll(this.visitFlowrClause(clause, argument));
        ReturnClauseSparkIterator returnIterator =
                (ReturnClauseSparkIterator) (this.visitFlowrClause(expression.get_returnClause(), argument).get(0));
        return new FlworExpressionSparkRuntimeIterator(iterators.get(0), iterators.subList(1, iterators.size()),
                returnIterator);
    }

    private List<FlowrClauseSparkIterator> visitFlowrClause(FlworClause clause,
                                                                            RuntimeIterator argument) {
        List<FlowrClauseSparkIterator> result = new ArrayList<>();
        if(clause instanceof ForClause) {
            for(ForClauseVar var : ((ForClause)clause).getForVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(),argument);
                VariableReferenceIterator variableReferenceIterator =
                        (VariableReferenceIterator) this.visit(var.getVariableReference(), argument);
                result.add(new ForClauseSparkIterator(variableReferenceIterator, assignmentExpression));
            }
        } else if (clause instanceof LetClause) {
            for(LetClauseVar var : ((LetClause)clause).getLetVariables()) {
                RuntimeIterator assignmentExpression = this.visit(var.getExpression(),argument);
                VariableReferenceIterator variableReferenceIterator =
                        (VariableReferenceIterator) this.visit(var.getVariableReference(), argument);
                result.add(new LetClauseSparkIterator(variableReferenceIterator, assignmentExpression));
            }
        }else if (clause instanceof GroupByClause){
            List<GroupByClauseSparkIteratorExpression> expressions = new ArrayList<>();
            for(GroupByClauseVar orderExpr : ((GroupByClause) clause).getGroupVariables()) {
                expressions.add( new GroupByClauseSparkIteratorExpression(
                        orderExpr.getExpression() != null? this.visit(orderExpr.getExpression(), argument): null,
                        (VariableReferenceIterator) this.visit(orderExpr.getVariableReference(), argument)));
            }
            result.add(new GroupByClauseSparkIterator(expressions));
        }else if (clause instanceof OrderByClause){
            List<OrderByClauseSparkIteratorExpression> expressions = new ArrayList<>();
            for(OrderByClauseExpr orderExpr : ((OrderByClause)clause).getExpressions()) {
                expressions.add( new OrderByClauseSparkIteratorExpression(this.visit(orderExpr.getExpression(), argument),
                        orderExpr.isAscending(), orderExpr.getUri(), orderExpr.getEmptyOrder()));
            }
            result.add(new OrderByClauseSparkIterator(expressions, ((OrderByClause)clause).isStable()));
        } else if (clause instanceof ReturnClause){
            result.add(new ReturnClauseSparkIterator(this.visit(((ReturnClause)clause).getReturnExpr(), argument)));
        }
        else if(clause instanceof WhereClause) {
            result.add(new WhereClauseSparkIterator(this.visit(((WhereClause)clause).getWhereExpression(), argument)));
        }
        return result;
    }

    @Override public RuntimeIterator visitOrderByClauseExpr(OrderByClauseExpr expression, RuntimeIterator argument){
        return defaultAction(expression, argument);
    }

    @Override public RuntimeIterator visitVariableReference(VariableReference expression, RuntimeIterator argument){
        return new VariableReferenceIterator(expression.getVariableName(), expression.getType());
    }
    //endregion

    //region primary
    @Override
    public RuntimeIterator visitPostfixExpression(PostFixExpression expression, RuntimeIterator argument){
        if(expression.isPrimary()){
            return defaultAction(expression, argument);
        } else {
            RuntimeIterator previous = this.visit(expression.get_primaryExpressionNode(), argument);
            for(PostfixExtension extension : expression.getExtensions()){
                try {
                    if (extension instanceof ArrayLookupExtension) {
                        RuntimeIterator iterator =
                                this.visit(((ArrayLookupExtension) extension).getExpression(), argument);
                        previous = new ArrayLookupItertor(previous, iterator);
                    }
                    if (extension instanceof ObjectLookupExtension) {
                        StringRuntimeIterator iterator = (StringRuntimeIterator)
                                this.visit(((ObjectLookupExtension) extension).getField(), argument);
                        previous = new ObjectLookupItertor(previous, iterator);
                    }
                    if (extension instanceof ArrayUnboxingExtension) {
                        previous = new ArrayUnboxingItertor(previous);
                    }
                    if (extension instanceof PredicateExtension) {
                        RuntimeIterator filterExpression = //pass the predicate as argument for $$ expresions
                                this.visit(((PredicateExtension) extension).getExpression(), argument);
                        previous = new PredicateIterator(previous, filterExpression);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    throw new UnsupportedFeatureException("Invalid Postfix extension");
                }
            }
            return previous;
        }
    }
    @Override
    public RuntimeIterator visitArrayConstructor(ArrayConstructor expression, RuntimeIterator argument){
        RuntimeIterator result = null;
        if(expression.getExpression() != null)
            result = this.visit(expression.getExpression(), argument);
        return new ArrayRuntimeIterator(result);
    }
    @Override
    public RuntimeIterator visitObjectConstructor(ObjectConstructor expression, RuntimeIterator argument){
        if(expression.isMergedConstructor()) {
            List<ObjectConstructorRuntimeIterator> childExpressions = new ArrayList<>();
            for(Expression child : expression.getChildExpression().getExpressions())
                childExpressions.add(((ObjectConstructorRuntimeIterator) this.visit(child, argument)));
            return new ObjectConstructorRuntimeIterator(childExpressions);
        } else {
            List<RuntimeIterator> keys = new ArrayList<>();
            List<RuntimeIterator> values = new ArrayList<>();
            for(Expression key: expression.getKeys())
                keys.add(this.visit(key,argument));
            for(Expression value: expression.getValues())
                values.add(this.visit(value,argument));
            return new ObjectConstructorRuntimeIterator(keys,values);
        }
    }

    @Override
    public RuntimeIterator visitContextExpr(ContextExpression expression, RuntimeIterator argument) {
        return new ContextExpressionIterator();
    }
    //endregion

    //region literals
    @Override
    public RuntimeIterator visitInteger(IntegerLiteral expression, RuntimeIterator argument){
        return new IntegerRuntimeIterator(expression.getValue());
    }
    @Override
    public RuntimeIterator visitString(StringLiteral expression, RuntimeIterator argument){
        return new StringRuntimeIterator(expression.getValue());
    }
    @Override
    public RuntimeIterator visitDouble(DoubleLiteral expression, RuntimeIterator argument){
        return new DoubleRuntimeIterator(expression.getValue());
    }
    @Override
    public RuntimeIterator visitDecimal(DecimalLiteral expression, RuntimeIterator argument){
        return new DecimalRuntimeIterator(expression.getValue());
    }
    @Override
    public RuntimeIterator visitNull(NullLiteral expression, RuntimeIterator argument){
        return new NullRuntimeIterator();
    }
    @Override
    public RuntimeIterator visitBoolean(BooleanLiteral expression, RuntimeIterator argument){
        return new BooleanRuntimeIterator(expression.getValue());
    }
    //endregion

    //region operational
    @Override
    public RuntimeIterator visitAdditiveExpr(AdditiveExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if(expression.getOperators().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new AdditiveExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                                expression.getOperators().subList(0, expression.getOperators().size() - 1)), argument);
            }else{
                left = this.visit(expression.getMainExpression(),argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }


            return new AdditiveOperationIterator(left, right,
                    expression.getOperators().get(expression.getOperators().size() - 1));

        }
            return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitMultiplicativeExpr(MultiplicativeExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if(expression.getOperators().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new MultiplicativeExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1),
                                expression.getOperators().subList(0, expression.getOperators().size() - 1)), argument);
            }else{
                left = this.visit(expression.getMainExpression(),argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }


            return new MultiplicativeOperationIterator(left, right,
                    expression.getOperators().get(expression.getOperators().size() - 1));

        }
        return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitAndExpr(AndExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if(expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new AndExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1)), argument);
            }else{
                left = this.visit(expression.getMainExpression(),argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new AndOperationIterator(left, right);

        }
        return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitOrExpr(OrExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if(expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new OrExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1)), argument);
            }else{
                left = this.visit(expression.getMainExpression(),argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new OrOperationIterator(left, right);

        }
        return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitNotExpr(NotExpression expression, RuntimeIterator argument){
        if(expression.isActive())
            return new NotOperationIterator(this.visit(expression.getMainExpression(), argument));
        return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitUnaryExpr(UnaryExpression expression, RuntimeIterator argument){
        if(expression.isActive())
        {
            //compute +- final result
            int result = 1;
            for(OperationalExpressionBase.Operator op : expression.getOperators())
                if(op == OperationalExpressionBase.Operator.MINUS)
                    result *= -1;
            return new UnaryOperationIterator(this.visit(expression.getMainExpression(), argument),
                    result == -1? OperationalExpressionBase.Operator.MINUS: OperationalExpressionBase.Operator.PLUS);
        }
        return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitRangeExpr(RangeExpression expression, RuntimeIterator argument){
        if(expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new RangeOperationIterator(left, right);
        }
        else
            return defaultAction(expression, argument);
    }
    @Override
    public RuntimeIterator visitComparisonExpr(ComparisonExpression expression, RuntimeIterator argument){
        if(expression.isActive()) {
            RuntimeIterator left = this.visit(expression.getMainExpression(), argument);
            RuntimeIterator right = this.visit(expression.getRightExpression(), argument);
            return new ComparisonOperationIterator(left, right, expression.getOperator());
        }
        else
            return defaultAction(expression, argument);
    }
    //endregion
    @Override
    public RuntimeIterator visitCommaExpression(CommaExpression expression, RuntimeIterator argument){
        List<RuntimeIterator> result = new ArrayList<>();
        for(Expression childExpr : expression.getExpressions())
            result.add(this.visit(childExpr, argument));
        if(result.size() == 1)
            return result.get(0);
        else
            return new CommaExpressionIterator(result);
    }

    @Override
    public RuntimeIterator visitParenthesizedExpression(ParenthesizedExpression expression, RuntimeIterator argument){
        if(expression.getExpression() != null)
            return defaultAction(expression, argument);
        return new EmptySequenceIterator();
    }

    @Override
    public RuntimeIterator visitFunctionCall(FunctionCall expression, RuntimeIterator argument){
        List<RuntimeIterator> arguments = new ArrayList<>();
        for(Expression arg: expression.getParameters())
            arguments.add(this.visit(arg, argument));


        switch (expression.getFunctionName()){
            case Functions.JSON_FILE:
                return new ParseJsonFunctionIterator(arguments);
            case Functions.PARALLELIZE:
                return new ParallelizeFunctionIterator(arguments);
            case Functions.COUNT:
                return new CountFunctionIterator(arguments);
            case Functions.MAX:
                return new ArithmeticFunctionIterator(arguments,
                        ArithmeticFunctionOperator.MAX);
            case Functions.MIN:
                return new ArithmeticFunctionIterator(arguments,
                        ArithmeticFunctionOperator.MIN);
            case Functions.AVG:
                return new ArithmeticFunctionIterator(arguments,
                        ArithmeticFunctionOperator.AVG);
            case Functions.SUM:
                return new ArithmeticFunctionIterator(arguments,
                        ArithmeticFunctionOperator.SUM);
            case Functions.KEYS:
                return new ObjectFunctionIterator(arguments,
                        ObjectFunctionIterator.ObjectFunctionOperators.KEYS);
            case Functions.VALUES:
                return new ObjectFunctionIterator(arguments,
                        ObjectFunctionIterator.ObjectFunctionOperators.VALUES);
            case Functions.SIZE:
                return new ArrayFunctionIterator(arguments,
                        ArrayFunctionIterator.ArrayFunctionOperators.SIZE);

        }

        throw new UnknownFunctionCallException();
    }

    @Override
    public RuntimeIterator visitStringConcatExpr(StringConcatExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator left, right;
            //convert nary to tree of iterators
            if(expression.getRightExpressions().size() > 1) {
                right = this.visit(expression.getRightExpressions().get(expression.getRightExpressions().size() - 1),
                        argument);
                left = this.visit(
                        new StringConcatExpression(expression.getMainExpression(),
                                expression.getRightExpressions().subList(0, expression.getRightExpressions().size() - 1)), argument);
            }else{
                left = this.visit(expression.getMainExpression(),argument);
                right = this.visit(expression.getRightExpressions().get(0), argument);
            }

            return new StringConcatIterator(left, right);

        }
        return defaultAction(expression, argument);
    }

    @Override public RuntimeIterator visitInstanceOfExpression(InstanceOfExpression expression, RuntimeIterator argument){
        if(expression.isActive()){
            RuntimeIterator childExpression = this.visit(expression.getMainExpression(), argument);
            return new InstanceOfIterator(childExpression, expression.getsequenceType().getSequence());
        }
        else
            return defaultAction(expression, argument);
    }

    @Override public RuntimeIterator visitIfExpression(IfExpression expression, RuntimeIterator argument){
        return new IfRuntimeIterator(this.visit(expression.getCondition(), argument),
                                         this.visit(expression.getBranch(), argument),
                                         this.visit(expression.getElseBranch(), argument));
    }

    @Override public RuntimeIterator visitQuantifiedExpression(QuantifiedExpression expression, RuntimeIterator argument){
        List<QuantifiedExpressionVarIterator> variables = new ArrayList<>();
        expression.getVariables().forEach(var -> variables.add((QuantifiedExpressionVarIterator) this.visit(var, argument)));
        RuntimeIterator evaluationExpression = this.visit(expression.getEvaluationExpression(), argument);
        return new QuantifiedExpressionIterator(expression.getOperator(),
                variables, evaluationExpression);
    }

    @Override public RuntimeIterator visitQuantifiedExpressionVar(QuantifiedExpressionVar expression, RuntimeIterator argument){
        QuantifiedExpressionVarIterator iterator;
        iterator = new QuantifiedExpressionVarIterator(expression.getVariableReference(),
                expression.getSequenceType(), this.visit(expression.getExpression(), argument));
        return iterator;
    }
}
