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
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpression extends Expression {
    private final Expression expression;
    private final Quantification quantifier;
    private final List<QuantifiedExpressionVar> variables;


    public QuantifiedExpression(
            Quantification quantifier,
            Expression expression,
            List<QuantifiedExpressionVar> vars,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.quantifier = quantifier;
        this.variables = vars;
        this.expression = expression;
    }

    public Node getEvaluationExpression() {
        return this.expression;
    }

    public Quantification getOperator() {
        return this.quantifier;
    }

    public List<QuantifiedExpressionVar> getVariables() {
        return this.variables;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.variables != null) {
            this.variables.forEach(v -> {
                if (v != null) {
                    result.add(v.getExpression());
                }
            });
        }
        result.add(this.expression);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitQuantifiedExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(quantifier.equals(Quantification.EVERY) ? "every " : "some ");
        String separator = "";
        for (QuantifiedExpressionVar v : this.variables) {
            sb.append(separator);
            sb.append(v.toString());
            separator = ", ";
        }
        return sb.toString();
    }

    public enum Quantification {
        EVERY,
        SOME
    }
}
