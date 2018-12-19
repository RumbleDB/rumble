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


import java.util.List;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

public abstract class UnaryExpressionBase extends OperationalExpressionBase {

    @Override
    public boolean isActive() {
        return _isActive;
    }

    public List<Operator> getOperators(){return _multipleOperators;}

    public Operator getSingleOperator(){return _singleOperator;}

    protected UnaryExpressionBase(Expression _mainExpression, ExpressionMetadata metadata) {
        super(_mainExpression, Operator.NONE, metadata);
    }

    protected UnaryExpressionBase(Expression _mainExpression, List<Operator> ops, boolean isActive,
                                  ExpressionMetadata metadata) {
        super(_mainExpression, ops, metadata);
        this._isActive = isActive;

    }

    protected UnaryExpressionBase(Expression _mainExpression, Operator singleOperator, boolean isActive,
                                  ExpressionMetadata metadata) {
        super(_mainExpression, singleOperator, metadata);
        this._isActive = isActive;
    }
}
