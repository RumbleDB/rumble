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


import java.util.ArrayList;
import java.util.List;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

public abstract class BinaryExpressionBase extends OperationalExpressionBase {

    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public boolean isActive() {
        return _isActive;
    }

    public BinaryExpressionBase(Expression mainExpression, ExpressionMetadata metadata) {
        super(mainExpression, Operator.NONE, metadata);
        this._isActive = false;

    }

    public BinaryExpressionBase(Expression mainExpression, Expression rhs, Operator op,
                                ExpressionMetadata metadata) {
        super(mainExpression, op, metadata);
        this.rightExpression = rhs;
        if(Operator.NONE != op && rhs!=null)
            this._isActive = true;
    }
    public Operator getOperator(){
        return  this._singleOperator;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.add(this._mainExpression);
        if(this.rightExpression != null)
            result.add(this.rightExpression);
        return getDescendantsFromChildren(result,depthSearch);
    }

    private Expression rightExpression;
}
