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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Prolog extends Node {

    private List<Node> declarations;

    public Prolog(
            List<VariableDeclaration> variableDeclarations,
            List<FunctionDeclaration> functionDeclarations,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.declarations = new ArrayList<Node>(variableDeclarations);
        this.declarations.addAll(functionDeclarations);
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.declarations.stream()
            .filter(x -> x instanceof FunctionDeclaration)
            .map(x -> (FunctionDeclaration) x)
            .collect(Collectors.toList());
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.declarations.stream()
            .filter(x -> x instanceof VariableDeclaration)
            .map(x -> (VariableDeclaration) x)
            .collect(Collectors.toList());
    }

    public void setDeclarations(List<Node> declarations) {
        this.declarations = declarations;
    }

    @Override
    public List<Node> getChildren() {
        return this.declarations.stream().filter(x -> x != null).collect(Collectors.toList());
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProlog(this, argument);
    }
}

