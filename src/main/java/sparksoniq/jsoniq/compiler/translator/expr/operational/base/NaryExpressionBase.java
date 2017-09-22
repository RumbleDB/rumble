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
 package sparksoniq.jsoniq.compiler.translator.expr.operational.base;


import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;

import java.util.ArrayList;
import java.util.List;

public class NaryExpressionBase extends OperationalExpressionBase {

    public List<Expression> getRightExpressions() {
        return _rightExpressions;
    }

    public List<Operator> getOperators(){return _multipleOperators;}

    public Operator getSingleOperator(){return _singleOperator;}

    @Override
    public boolean isActive() {
        return _isActive;
    }

    public NaryExpressionBase(Expression mainExpression) {
        super(mainExpression, Operator.NONE);
        this._isActive = false;

    }

    public NaryExpressionBase(Expression mainExpression, List<Expression> rhs, Operator op) {
        super(mainExpression, op);
        this._rightExpressions = rhs;
        if(Operator.NONE != op)
            this._isActive = true;
    }

    public NaryExpressionBase(Expression mainExpression,
                              List<Expression> rhs, List<Operator> ops) {
        super(mainExpression, ops);
        this._rightExpressions = rhs;
        this._isActive =true;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(this._mainExpression);
        if(this._rightExpressions != null)
            _rightExpressions.forEach(e -> {
                if(e!=null)
                    result.add(e);
            });
        return getDescendantsFromChildren(result,depthSearch);
    }


    private List<Expression> _rightExpressions;



}
