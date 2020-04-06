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


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.base.Functions;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineFunctionExpression extends Expression {

    private final String name;
    private final Map<String, SequenceType> params;
    private final SequenceType returnType;
    private final Expression body;

    public InlineFunctionExpression(
            String name,
            Map<String, SequenceType> params,
            SequenceType returnType,
            Expression body,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, SequenceType> getParams() {
        return this.params;
    }

    public SequenceType getReturnType() {
        return this.returnType;
    }

    public Expression getBody() {
        return this.body;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    public void registerUserDefinedFunctionExecutionMode(
            VisitorConfig visitorConfig
    ) {
        FunctionIdentifier identifier = new FunctionIdentifier(this.name, this.params.size());
        // if named(static) function declaration
        if (!this.name.equals("")) {
            Functions.addUserDefinedFunctionExecutionMode(
                identifier,
                this.body.getHighestExecutionMode(visitorConfig),
                visitorConfig.suppressErrorsForFunctionSignatureCollision(),
                this.getMetadata()
            );
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFunctionDeclaration(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(functionDeclaration ";
        result += this.name;
        result += " (paramList (";
        for (Map.Entry<String, SequenceType> entry : this.params.entrySet()) {
            result += "param (";
            result += "NCName "
                + entry.getKey()
                + " sequenceType "
                + entry.getValue().toString()
                + ") , ";
        }
        result = result.substring(0, result.length() - 1); // remove last comma
        result += "))";

        result += " (sequenceType ( " + this.returnType.toString() + "))";

        result += " (expr (" + this.body.serializationString(false) + "))";

        result += ")";
        return result;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append("\n");
        this.body.print(buffer, indent + 1);
    }
}

