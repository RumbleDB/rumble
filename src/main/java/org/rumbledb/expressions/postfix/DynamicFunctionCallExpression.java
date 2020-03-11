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

package org.rumbledb.expressions.postfix;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicFunctionCallExpression extends Expression {

    private Expression mainExpression;
    private List<Expression> arguments;

    public DynamicFunctionCallExpression(
            Expression mainExpression,
            List<Expression> arguments,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a postfix expression.");
        }
        this.mainExpression = mainExpression;
        this.arguments = arguments;
        if (this.arguments == null) {
            this.arguments = new ArrayList<>();
        }
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.mainExpression);
        result.addAll(this.arguments.stream().filter(arg -> arg != null).collect(Collectors.toList()));
        return result;
    }

    /**
     * DynamicFunctionCall is always locally evaluated as execution mode cannot be determined at static analysis phase.
     * This behavior is different from all other postfix extensions, hence this override is required.
     */
    @Override
    public void initHighestExecutionMode() {
        this.highestExecutionMode = ExecutionMode.LOCAL;
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder();
        result.append("(dynamicFunctionCall ");
        result.append(this.mainExpression.serializationString(false));
        for (Expression arg : this.arguments) {
            result.append("(argument (exprSingle ");
            result.append(arg.serializationString(false));
            result.append((this.arguments.indexOf(arg) < this.arguments.size() - 1 ? ")) , " : ")) "));
        }
        result.append("))");
        return result.toString();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDynamicFunctionCallExpression(this, argument);
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }
}
