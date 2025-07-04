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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.expressions.comparison;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

/**
 * An expression that represents a node comparison.
 * 
 * Node comparisons are used to compare two nodes, by their identity or by their document order.
 * 
 * @see <a href="https://www.w3.org/TR/xquery-31/#id-node-comparisons">XQuery 3.1, 3.7.3: Node Comparisons</a>
 */
public class NodeComparisonExpression extends Expression {

    public static enum NodeComparisonOperator {
        NC_PRECEDES("<<"),
        NC_FOLLOWS(">>"),
        NC_IS("is");

        private String symbol;

        NodeComparisonOperator(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return this.symbol;
        }

        public static NodeComparisonOperator fromSymbol(String symbol) {
            switch (symbol) {
                case "<<":
                    return NC_PRECEDES;
                case ">>":
                    return NC_FOLLOWS;
                case "is":
                    return NC_IS;
                default:
                    throw new OurBadException("Unrecognized node comparison symbol: " + symbol);
            }
        }
    }

    private Expression leftExpression;
    private Expression rightExpression;
    private NodeComparisonOperator operator;

    public NodeComparisonExpression(
            Expression leftExpression,
            Expression rightExpression,
            NodeComparisonOperator operator,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitNodeComparisonExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.leftExpression, this.rightExpression);
    }

    public NodeComparisonOperator getOperator() {
        return this.operator;
    }

    public Expression getLeftExpression() {
        return this.leftExpression;
    }

    public Expression getRightExpression() {
        return this.rightExpression;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("(\n");

        this.leftExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");

        indentIt(sb, indent);
        sb.append(this.operator.toString() + "\n");

        indentIt(sb, indent);
        sb.append("(\n");

        this.rightExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");
    }
}
