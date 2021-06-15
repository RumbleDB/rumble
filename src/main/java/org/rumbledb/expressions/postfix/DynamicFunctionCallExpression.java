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

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType.Arity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicFunctionCallExpression extends Expression {

    private static final long serialVersionUID = 1L;
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
     * This behavior is different from all other postfix extensions, hence no override is required.
     */

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDynamicFunctionCallExpression(this, argument);
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(
            " | "
                + (this.staticSequenceType == null
                    ? "not set"
                    : this.staticSequenceType
                        + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)"))
        );
        buffer.append("\n");
        this.mainExpression.print(buffer, indent + 1);
        for (Expression arg : this.arguments) {
            if (arg == null) {
                for (int i = 0; i < indent + 1; ++i) {
                    buffer.append("  ");
                }
                buffer.append("?\n");
            } else {
                arg.print(buffer, indent + 1);
            }
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.mainExpression.serializeToJSONiq(sb, indent + 1);

        // TODO always ending with \n might be an issue here
        sb.append("(");
        if (this.arguments != null) {
            for (int i = 0; i < this.arguments.size(); i++) {
                this.arguments.get(i).serializeToJSONiq(sb, 0);
                if (i == this.arguments.size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(", ");
                }
            }
        }
        sb.append(")\n");
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        if (this.arguments.size() == 0) {
            this.highestExecutionMode = ExecutionMode.LOCAL;
            return;
        }
        if(this.getStaticSequenceType().getArity().equals(Arity.One))
        {
            this.highestExecutionMode = ExecutionMode.LOCAL;
            return;
        }
        this.highestExecutionMode = this.arguments.get(0).getHighestExecutionMode(visitorConfig);
        if (this.highestExecutionMode.equals(ExecutionMode.RDD)) {
            this.highestExecutionMode = ExecutionMode.LOCAL;
        }
    }
}
