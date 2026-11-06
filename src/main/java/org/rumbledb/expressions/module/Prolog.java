/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.expressions.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

public class Prolog extends Node {

    @Getter
    @Setter
    private List<PrologDeclaration> declarations;

    // The declarations are those of every module this one reaches, each knowing its own module's context.
    @Getter
    @Setter
    private StaticContext moduleContext;

    @Getter
    private final List<SchemaImport> schemaImports;

    public Prolog(
            List<VariableDeclaration> variableDeclarations,
            List<FunctionDeclaration> functionDeclarations,
            List<TypeDeclaration> typeDeclarations,
            ExceptionMetadata metadata) {
        super(metadata);
        this.declarations = new ArrayList<PrologDeclaration>(variableDeclarations);
        this.declarations.addAll(functionDeclarations);
        this.declarations.addAll(typeDeclarations);
        this.schemaImports = new ArrayList<>();
    }

    /** A prolog without declarations, to be filled in or to stand in for an imported module. */
    public Prolog(ExceptionMetadata metadata) {
        this(List.of(), List.of(), List.of(), metadata);
    }

    /** The contexts of the modules these declarations belong to, each once. */
    public List<StaticContext> getModuleContexts() {
        return this.declarations.stream()
                .map(PrologDeclaration::getModuleContext)
                .distinct()
                .toList();
    }

    public boolean isImported(PrologDeclaration declaration) {
        return declaration.getModuleContext() != this.moduleContext;
    }

    public void addSchemaImport(SchemaImport schemaImport) {
        this.schemaImports.add(schemaImport);
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

    /** Whether this module itself declares the context item. */
    public boolean hasContextItemDeclaration() {
        return getVariableDeclarations().stream()
                .anyMatch(declaration -> !isImported(declaration)
                        && declaration.getVariableName().equals(Name.CONTEXT_ITEM));
    }

    public void addDeclaration(PrologDeclaration declaration) {
        this.declarations.add(declaration);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.schemaImports);
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
            Prolog prolog, FunctionIdentifier functionIdentifier) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            if (declaration.getFunctionIdentifier().equals(functionIdentifier)) {
                return declaration;
            }
        }
        return null;
    }
}
