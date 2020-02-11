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


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineFunctionExpression extends PrimaryExpression {

    private final String name;
    private final Map<String, FlworVarSequenceType> params;
    private final FlworVarSequenceType returnType;
    private final Expression body;

    public InlineFunctionExpression(
            String name,
            Map<String, FlworVarSequenceType> params,
            FlworVarSequenceType returnType,
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

    public Map<String, FlworVarSequenceType> getParams() {
        return this.params;
    }

    public FlworVarSequenceType getReturnType() {
        return this.returnType;
    }

    public Expression getBody() {
        return this.body;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        return result;
    }

    public void registerUserDefinedFunctionExecutionMode(
            boolean ignoreDuplicateUserDefinedFunctionError,
            boolean ignoreUnsetExecutionModeAccessDuringFunctionDeclaration
    ) {
        FunctionIdentifier identifier = new FunctionIdentifier(this.name, this.params.size());
        // if named(static) function declaration
        if (!this.name.equals("")) {
            Functions.addUserDefinedFunctionExecutionMode(
                identifier,
                this.body.getHighestExecutionMode(ignoreUnsetExecutionModeAccessDuringFunctionDeclaration),
                ignoreDuplicateUserDefinedFunctionError,
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
        for (Map.Entry<String, FlworVarSequenceType> entry : this.params.entrySet()) {
            result += "param (";
            result += "NCName "
                + entry.getKey()
                + " sequenceType "
                + entry.getValue().serializationString(false)
                + ") , ";
        }
        result = result.substring(0, result.length() - 1); // remove last comma
        result += "))";

        result += " (sequenceType ( " + this.returnType.serializationString(false) + "))";

        result += " (expr (" + this.body.serializationString(false) + "))";

        result += ")";
        return result;
    }
}

