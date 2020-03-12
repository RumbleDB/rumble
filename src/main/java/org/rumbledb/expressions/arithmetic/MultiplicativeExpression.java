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
    public String serializationString(boolean prefix) {
        String result = "(additiveExpr ";
        result += this.getChildren().get(0).serializationString(true);
        result += this.multiplicativeOperator;
        result += this.getChildren().get(1).serializationString(true);
        result += ")";
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMultiplicativeExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(leftExpression, rightExpression);
    }

    public MultiplicativeOperator getMultiplicativeOperator() {
        return this.multiplicativeOperator;
    }

    public static MultiplicativeOperator getMultiplicativeOperatorFromSymbol(String symbol) {
        switch (symbol) {
            case "*":
                return MultiplicativeOperator.MUL;
            case "div":
                return MultiplicativeOperator.DIV;
            case "mod":
                return MultiplicativeOperator.MOD;
            case "idiv":
                return MultiplicativeOperator.IDIV;
        }
        throw new OurBadException("Unrecognized multiplicative symbol: " + symbol);
    }
}
