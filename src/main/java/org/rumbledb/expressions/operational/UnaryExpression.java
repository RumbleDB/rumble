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




import java.util.Arrays;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;
import org.rumbledb.expressions.postfix.PostFixExpression;


public class UnaryExpression extends UnaryExpressionBase {

    public static final Operator[] operators = new Operator[] { Operator.PLUS, Operator.MINUS };
    private PostFixExpression _postfixExpression;

    public UnaryExpression(PostFixExpression _mainExpression, ExceptionMetadata metadata) {
        super(_mainExpression, metadata);
        this._postfixExpression = _mainExpression;
        this._isActive = false;
    }

    public UnaryExpression(PostFixExpression _mainExpression, List<Operator> ops, ExceptionMetadata metadata) {
        super(_mainExpression, ops, ops != null && !ops.isEmpty(), metadata);
        this.validateOperators(Arrays.asList(operators), ops);
        this._postfixExpression = _mainExpression;
    }

    public PostFixExpression get_postfixExpression() {
        return _postfixExpression;
    }

    @Override
    public boolean isActive() {
        return this._isActive;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitUnaryExpr(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(unaryExpr ";
        if (this._multipleOperators != null && this._multipleOperators.size() > 0) {
            for (Operator op : this._multipleOperators)
                result += getStringFromOperator(op) + " ";
        }
        result += _mainExpression.serializationString(true);
        result += ")";
        return result;
    }
}
