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

package org.rumbledb.expressions.operational;


import sparksoniq.semantics.visitor.AbstractNodeVisitor;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.util.Arrays;
import java.util.List;

import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.operational.base.NaryExpressionBase;

public class AdditiveExpression extends NaryExpressionBase {

    public static final Operator[] operators = new Operator[] { Operator.PLUS, Operator.MINUS };

    public AdditiveExpression(Expression _mainExpression, ExceptionMetadata metadata) {
        super(_mainExpression, metadata);

    }

    public AdditiveExpression(
            Expression _mainExpression,
            List<Expression> rhs,
            List<Operator> ops,
            ExceptionMetadata metadata
    ) {
        super(_mainExpression, rhs, ops, metadata);
        validateOperators(Arrays.asList(operators), ops);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAdditiveExpr(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(additiveExpr ";
        result += _mainExpression.serializationString(true);
        if (this.getRightExpressions() != null && this.getRightExpressions().size() > 0)
            for (Expression expr : this.getRightExpressions())
                result += " "
                    +
                    getStringFromOperator(this._multipleOperators.get(this.getRightExpressions().indexOf(expr)))
                    + " "
                    + expr.serializationString(true);
        result += ")";
        return result;
    }

}
