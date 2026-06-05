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
 */

package org.rumbledb.expressions.miscellaneous;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class NodeSetExpression extends Expression {

    public enum NodeSetOperator {
        UNION("union"),
        INTERSECT("intersect"),
        EXCEPT("except");

        private final String symbol;

        NodeSetOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public static NodeSetOperator fromSymbol(String symbol) {
            switch (symbol) {
                case "union":
                case "|":
                    return UNION;
                case "intersect":
                    return INTERSECT;
                case "except":
                    return EXCEPT;
                default:
                    throw new OurBadException("Unrecognized node set operator: " + symbol);
            }
        }
    }

    private Expression leftExpression;
    private Expression rightExpression;
    private NodeSetOperator operator;

    public NodeSetExpression(
            Expression leftExpression,
            Expression rightExpression,
            NodeSetOperator operator,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitNodeSetExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.leftExpression, this.rightExpression);
    }

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public Expression getRightExpression() {
        return this.rightExpression;
    }

    public NodeSetOperator getOperator() {
        return this.operator;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("(\n");

        this.leftExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");

        indentIt(sb, indent);
        sb.append(this.operator.getSymbol()).append("\n");

        indentIt(sb, indent);
        sb.append("(\n");

        this.rightExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");
    }
}
