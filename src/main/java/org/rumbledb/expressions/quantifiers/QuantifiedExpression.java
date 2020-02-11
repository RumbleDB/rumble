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

package org.rumbledb.expressions.quantifiers;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpression extends Expression {
    private final Expression expression;
    private final QuantifiedOperators _operator;
    private final List<QuantifiedExpressionVar> _variables;


    public QuantifiedExpression(
            QuantifiedOperators operator,
            Expression expression,
            List<QuantifiedExpressionVar> vars,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this._operator = operator;
        this._variables = vars;
        this.expression = expression;
    }

    public Node getEvaluationExpression() {
        return this.expression;
    }

    public QuantifiedOperators getOperator() {
        return this._operator;
    }

    public List<QuantifiedExpressionVar> getVariables() {
        return this._variables;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this._variables != null)
            this._variables.forEach(e -> {
                if (e != null)
                    result.add(e);
            });
<<<<<<< HEAD
        result.add(this.expression);
        return getDescendantsFromChildren(result, depthSearch);
=======
        result.add(_expression);
        return result;
>>>>>>> c94fc8ddae10d0d8652a240536e13bdcdb7fce0d
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitQuantifiedExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        return "";
    }

    public enum QuantifiedOperators {
        EVERY,
        SOME
    }
}
