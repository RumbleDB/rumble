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

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallExpression extends Expression {

    private final FunctionIdentifier identifier;
    private final List<Expression> arguments; // null for placeholder
    private final boolean isPartialApplication;

    public FunctionCallExpression(
            Name functionName,
            List<Expression> arguments,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.arguments = arguments;
        this.isPartialApplication = arguments.stream().anyMatch(arg -> arg == null);
        this.identifier = new FunctionIdentifier(
                functionName,
                this.arguments.size()
        );
    }

    // some may be null for partial application
    public List<Expression> getArguments() {
        return this.arguments;
    }

    public FunctionIdentifier getFunctionIdentifier() {
        return this.identifier;
    }

    public Name getFunctionName() {
        return this.identifier.getName();
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = this.arguments.stream().filter(arg -> arg != null).collect(Collectors.toList());
        return result;
    }

    public boolean isPartialApplication() {
        return this.isPartialApplication;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFunctionCall(this, argument);
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName() + " (" + this.identifier + ")");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append(
            " | "
                + (this.staticSequenceType == null
                    ? "not set"
                    : this.staticSequenceType
                        + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)"))
        );
        buffer.append("\n");
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
        sb.append(this.identifier.toString());

        // TODO check if i need to ignore () when I have arity??
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
}
