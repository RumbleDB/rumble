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

package org.rumbledb.expressions.primary;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionOrClause;


public class ArrayConstructor extends PrimaryExpression {

    private Expression expression;

    public ArrayConstructor(Expression expression, ExpressionMetadata metadata) {
        super(metadata);
        this.expression = expression;
    }

    public ArrayConstructor(ExpressionMetadata metadata) {
        super(metadata);
        this.expression = null;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        if (this.expression != null)
            result.add(this.expression);
        return getDescendantsFromChildren(result, depthSearch);
    }


    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitArrayConstructor(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(primaryExpr (arrayConstructor [";
        if (this.expression != null && ((CommaExpression) this.expression).getExpressions().size() > 0) {
            result += " " + this.expression.serializationString(true);
        }

        result += " ]))";
        return result;
    }

}
