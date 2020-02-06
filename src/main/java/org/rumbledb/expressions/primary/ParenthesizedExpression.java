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

import sparksoniq.jsoniq.ExecutionMode;


import sparksoniq.semantics.visitor.AbstractNodeVisitor;




import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;


public class ParenthesizedExpression extends PrimaryExpression {

    private Expression expression;

    public ParenthesizedExpression(Expression expression, ExceptionMetadata metadata) {
        super(metadata);
        this.expression = expression;
    }

    public ParenthesizedExpression(ExceptionMetadata metadata) {
        super(metadata);
        this.expression = null;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitParenthesizedExpression(this, argument);
    }

    private boolean bypassCurrentExpressionForExecutionModeOperations() {
        return expression != null;
    }

    @Override
    public void initHighestExecutionMode() {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return;
        }
        super.initHighestExecutionMode();
    }

    @Override
    public ExecutionMode getHighestExecutionMode(boolean ignoreUnsetError) {
        if (bypassCurrentExpressionForExecutionModeOperations()) {
            return this.expression.getHighestExecutionMode(ignoreUnsetError);
        }
        return super.getHighestExecutionMode(ignoreUnsetError);
    }

    @Override
    public List<Node> getDescendants(boolean depthSearch) {
        List<Node> result = new ArrayList<>();
        if (this.expression != null)
            result.add(this.expression);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(primaryExpr (parenthesizedExpr (";
        if (this.expression != null && ((CommaExpression) this.expression).getExpressions().size() > 0) {
            result += " " + this.expression.serializationString(true);
        }

        result += " )))";
        return result;
    }

}
