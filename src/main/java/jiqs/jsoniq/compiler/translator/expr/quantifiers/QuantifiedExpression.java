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
 package jiqs.jsoniq.compiler.translator.expr.quantifiers;

import jiqs.jsoniq.compiler.translator.expr.Expression;
import jiqs.jsoniq.compiler.translator.expr.ExpressionOrClause;
import jiqs.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpression extends Expression {
    public enum QuantifiedOperators{
        EVERY,
        SOME
    }

    public ExpressionOrClause getEvaluationExpression() {
        return _expression;
    }

    public QuantifiedOperators getOperator() {
        return _operator;
    }


    public List<QuantifiedExpressionVar> getVariables() {
        return _variables;
    }

    public QuantifiedExpression(QuantifiedOperators operator, Expression expression, List<QuantifiedExpressionVar> vars){
        super();
        this._operator = operator;
        this._variables  = vars;
        this._expression = expression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        if(_variables!=null)
            _variables.forEach(e -> {
                if (e != null)
                    result.add(e);
            });
        result.add(_expression);
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitQuantifiedExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        return "";
    }

    private final Expression _expression;
    private final QuantifiedOperators _operator;
    private final List<QuantifiedExpressionVar> _variables;
}
