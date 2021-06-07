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

package org.rumbledb.expressions.arithmetic;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class MultiplicativeExpression extends Expression {
    private static final long serialVersionUID = 1L;

    public static enum MultiplicativeOperator {
        MUL("*"),
        DIV("div"),
        MOD("mod"),
        IDIV("idiv");

        private String name;

        MultiplicativeOperator(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static MultiplicativeOperator fromSymbol(String symbol) {
            if (symbol.equals(MUL.toString())) {
                return MUL;
            }
            if (symbol.equals(DIV.toString())) {
                return DIV;
            }
            if (symbol.equals(MOD.toString())) {
                return MOD;
            }
            if (symbol.equals(IDIV.toString())) {
                return IDIV;
            }
            throw new OurBadException("Unrecognized multiplicative symbol: " + symbol);
        }
    };

    private Expression leftExpression;
    private Expression rightExpression;
    private MultiplicativeOperator multiplicativeOperator;

    public MultiplicativeExpression(
            Expression leftExpression,
            Expression rightExpression,
            MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.multiplicativeOperator = multiplicativeOperator;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMultiplicativeExpr(this, argument);
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

    public MultiplicativeOperator getMultiplicativeOperator() {
        return this.multiplicativeOperator;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (" + (this.multiplicativeOperator) + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + (this.inferredSequenceType == null ? "not set" : this.inferredSequenceType));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("(\n");

        this.leftExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");

        indentIt(sb, indent);
        sb.append(this.multiplicativeOperator.toString() + "\n");

        indentIt(sb, indent);
        sb.append("(\n");

        this.rightExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append(")\n");
    }
}
