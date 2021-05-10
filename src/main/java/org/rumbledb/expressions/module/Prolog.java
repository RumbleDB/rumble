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
    private List<LibraryModule> importedModules;

    public Prolog(
            List<VariableDeclaration> variableDeclarations,
            List<FunctionDeclaration> functionDeclarations,
            List<TypeDeclaration> typeDeclarations,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.declarations = new ArrayList<Node>(variableDeclarations);
        this.declarations.addAll(functionDeclarations);
        this.declarations.addAll(typeDeclarations);
        System.err.println(this.declarations.size() + "declarations.");
        this.importedModules = new ArrayList<>();
    }

    public void addImportedModule(LibraryModule importedModule) {
        this.importedModules.add(importedModule);
    }

    public List<LibraryModule> getImportedModules() {
        return this.importedModules;
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

    public List<TypeDeclaration> getTypeDeclarations() {
        return this.declarations.stream()
            .filter(x -> x instanceof TypeDeclaration)
            .map(x -> (TypeDeclaration) x)
            .collect(Collectors.toList());
    }

    public void setDeclarations(List<Node> declarations) {
        System.err.println("Setting.");
        this.declarations = declarations;
    }

    public void clearDeclarations() {
        System.err.println("Clearing.");
        this.declarations.clear();
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.importedModules);
        result.addAll(this.declarations);
        System.err.println(result.size() + "children." + this.hashCode());
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        for (int i = 0; i < this.declarations.size(); i++) {
            this.declarations.get(i).serializeToJSONiq(sb, indent);
            this.importedModules.get(i).serializeToJSONiq(sb, indent);
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProlog(this, argument);
    }
}

