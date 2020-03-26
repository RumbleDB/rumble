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

package org.rumbledb.expressions.module;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.InlineFunctionExpression;

import java.util.ArrayList;
import java.util.List;

public class Prolog extends Node {

    private final List<VariableDeclaration> variableDeclarations;
    private final List<InlineFunctionExpression> functionDeclarations;

    public Prolog(
            List<VariableDeclaration> variableDeclarations,
            List<InlineFunctionExpression> functionDeclarations,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.variableDeclarations = variableDeclarations;
        this.functionDeclarations = functionDeclarations;
    }

    public List<InlineFunctionExpression> getFunctionDeclaration() {
        return this.functionDeclarations;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.variableDeclarations;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.variableDeclarations);
        if (this.functionDeclarations != null) {
            this.functionDeclarations.forEach(e -> {
                if (e != null) {
                    result.add(e);
                }
            });
        }
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProlog(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder();
        result.append("(prolog ");
        result.append(" (varDecl ");
        String separator = "";
        for (VariableDeclaration var : this.variableDeclarations) {
            result.append(separator);
            result.append("(" + var.serializationString(false) + ") , ");
            separator = ", ";
        }
        result.append("), ");
        result.append(" (functionDecl ");
        separator = "";
        for (InlineFunctionExpression func : this.functionDeclarations) {
            result.append(separator);
            result.append("(" + func.serializationString(false) + ") , ");
            separator = ", ";
        }
        result.append("))");
        return result.toString();
    }
}

