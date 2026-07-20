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


import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Prolog extends Node {

    private List<Node> declarations;
    private List<LibraryModule> importedModules;
    private final List<SchemaImport> schemaImports;

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
        this.importedModules = new ArrayList<>();
        this.schemaImports = new ArrayList<>();
    }

    public void addImportedModule(LibraryModule importedModule) {
        this.importedModules.add(importedModule);
    }

    public List<LibraryModule> getImportedModules() {
        return this.importedModules;
    }

    public void addSchemaImport(SchemaImport schemaImport) {
        this.schemaImports.add(schemaImport);
    }

    public List<SchemaImport> getSchemaImports() {
        return this.schemaImports;
    }

    public List<Node> getDeclarations() {
        return this.declarations;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.declarations.stream()
            .<FunctionDeclaration>mapMulti((x, downstream) -> {
                if (x instanceof FunctionDeclaration functionDeclaration) {
                    downstream.accept(functionDeclaration);
                }
            })
            .collect(Collectors.toList());
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        return this.declarations.stream()
            .<VariableDeclaration>mapMulti((x, downstream) -> {
                if (x instanceof VariableDeclaration variableDeclaration) {
                    downstream.accept(variableDeclaration);
                }
            })
            .collect(Collectors.toList());
    }

    public List<TypeDeclaration> getTypeDeclarations() {
        return this.declarations.stream()
            .<TypeDeclaration>mapMulti((x, downstream) -> {
                if (x instanceof TypeDeclaration typeDeclaration) {
                    downstream.accept(typeDeclaration);
                }
            })
            .collect(Collectors.toList());
    }

    public boolean hasContextItemDeclaration() {
        for (Node d : this.declarations) {
            if (!(d instanceof VariableDeclaration vd)) {
                continue;
            }
            if (vd.getVariableName().equals(Name.CONTEXT_ITEM)) {
                return true;
            }
        }
        return false;
    }

    public void setDeclarations(List<Node> declarations) {
        this.declarations = declarations;
    }

    public void addDeclaration(Node declaration) {
        this.declarations.add(declaration);
    }

    public void clearDeclarations() {
        this.declarations.clear();
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.schemaImports);
        result.addAll(this.importedModules);
        result.addAll(this.declarations);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        for (SchemaImport schemaImport : this.schemaImports) {
            schemaImport.serializeToJSONiq(sb, indent);
        }
        for (Node declaration : this.declarations) {
            declaration.serializeToJSONiq(sb, indent);
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProlog(this, argument);
    }

    public static FunctionDeclaration getFunctionDeclarationFromProlog(
            Prolog prolog,
            FunctionIdentifier functionIdentifier
    ) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            if (declaration.getFunctionIdentifier().equals(functionIdentifier)) {
                return declaration;
            }
        }
        for (LibraryModule module : prolog.getImportedModules()) {
            FunctionDeclaration result = getFunctionDeclarationFromProlog(module.getProlog(), functionIdentifier);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
