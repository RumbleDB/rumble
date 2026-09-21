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
package org.rumbledb.compiler.translation;

import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.DuplicateModuleTargetNamespaceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneBoundarySpaceDeclarationException;
import org.rumbledb.exceptions.MoreThanOneCopyNamespacesDeclarationException;
import org.rumbledb.exceptions.MoreThanOneEmptyOrderDeclarationException;
import org.rumbledb.exceptions.MultipleBaseURIException;
import org.rumbledb.exceptions.NamespaceDoesNotMatchModuleException;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.OptionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.xml.schema.XmlSchemaCatalogLoader;

/**
 * Handles shared prolog translation state and validation for both Jsoniq and XQuery frontends.
 */
public final class PrologBuilder {

    public enum BooleanSettingKind {
        CONSTRUCTION,
        BOUNDARY_SPACE,
        EMPTY_ORDER
    }

    private final TranslationContext translationContext;
    private final String libraryNamespace;
    private final Set<BooleanSettingKind> booleanSettings = EnumSet.noneOf(BooleanSettingKind.class);
    private boolean copyNamespacesSet;
    private boolean baseUriSet;
    private boolean defaultCollationSet;
    private boolean defaultFunctionNamespaceSet;
    private final Set<String> moduleNamespaces = new HashSet<>();
    private final Set<String> schemaNamespaces = new HashSet<>();
    private final List<LibraryModule> modules = new ArrayList<>();
    private final List<SchemaImport> schemas = new ArrayList<>();
    private final List<VariableDeclaration> variables = new ArrayList<>();
    private final List<FunctionDeclaration> functions = new ArrayList<>();
    private final List<TypeDeclaration> types = new ArrayList<>();
    private final List<OptionDeclaration> options = new ArrayList<>();

    public PrologBuilder(TranslationContext translationContext, String libraryNamespace) {
        this.translationContext = translationContext;
        this.libraryNamespace = libraryNamespace;
    }

    // region State-Manipulating Header Receivers

    public void applyDefaultNamespace(boolean function, String uri, ExceptionMetadata metadata) {
        if (function) {
            if (this.defaultFunctionNamespaceSet) {
                throw new SemanticException("The default function namespace has already been declared.", metadata);
            }
            this.translationContext.moduleContext().setDefaultFunctionNamespaceUri(uri);
            this.defaultFunctionNamespaceSet = true;
        } else {
            this.translationContext.bindNamespace("", uri, metadata);
        }
    }

    public void applyBooleanSetting(BooleanSettingKind kind, boolean value, ExceptionMetadata metadata) {
        boolean duplicate = this.booleanSettings.contains(kind);
        switch (kind) {
            case CONSTRUCTION:
                if (duplicate) {
                    throw new SemanticException(
                            "The construction mode was already set.",
                            ErrorCode.MoreThanOneConstructionDeclarationErrorCode,
                            metadata);
                }
                this.translationContext.moduleContext().setConstructionPreserve(value);
                break;
            case BOUNDARY_SPACE:
                if (duplicate) {
                    throw new MoreThanOneBoundarySpaceDeclarationException(
                            "The boundary-space policy was already set.", metadata);
                }
                this.translationContext.moduleContext().setBoundarySpacePreserve(value);
                break;
            case EMPTY_ORDER:
                if (duplicate) {
                    throw new MoreThanOneEmptyOrderDeclarationException("The empty order was already set.", metadata);
                }
                this.translationContext.moduleContext().setEmptySequenceOrderLeast(value);
                break;
        }
        this.booleanSettings.add(kind);
    }

    public void applyCopyNamespaces(boolean preserve, boolean inherit, ExceptionMetadata metadata) {
        if (this.copyNamespacesSet) {
            throw new MoreThanOneCopyNamespacesDeclarationException(
                    "The copy-namespaces mode was already set.", metadata);
        }
        this.translationContext.moduleContext().setCopyNamespacesMode(preserve, inherit);
        this.copyNamespacesSet = true;
    }

    public void applyBaseUri(String literal, ExceptionMetadata metadata) {
        if (this.baseUriSet) {
            throw new MultipleBaseURIException("The base-uri was already set.", metadata);
        }
        URI uri =
                URILiteralUtils.resolve(this.translationContext.moduleContext().getStaticBaseURI(), literal, metadata);
        this.translationContext
                .moduleContext()
                .setStaticBaseUri(uri, URILiteralUtils.toStaticBaseUriString(uri, literal));
        this.baseUriSet = true;
    }

    public void applyDefaultCollation(String literal, ExceptionMetadata literalMetadata, ExceptionMetadata metadata) {
        if (this.defaultCollationSet) {
            throw new DefaultCollationException("The default collation was already set.", metadata);
        }
        String uri = URILiteralUtils.resolve(
                        this.translationContext.moduleContext().getStaticBaseURI(), literal, literalMetadata)
                .toString();
        if (!this.translationContext.moduleContext().isStaticallyKnownCollation(uri)) {
            throw new DefaultCollationException("Unknown collation: " + uri, literalMetadata);
        }
        this.translationContext.moduleContext().setDefaultCollation(uri);
        this.defaultCollationSet = true;
    }

    public void importModule(LibraryModule module, ExceptionMetadata metadata) {
        if (!this.moduleNamespaces.add(module.getNamespace())) {
            throw new DuplicateModuleTargetNamespaceException(
                    "Duplicate module target namespace: " + module.getNamespace(), metadata);
        }
        this.modules.add(module);
    }

    public void importSchema(SchemaImport schema, ExceptionMetadata metadata) {
        if (!this.schemaNamespaces.add(schema.getTargetNamespace())) {
            throw new SemanticException(
                    "The schema namespace " + schema.getTargetNamespace() + " is imported more than once.",
                    ErrorCode.DuplicateSchemaImportErrorCode,
                    metadata);
        }
        bindSchemaNamespace(schema);
        this.schemas.add(schema);
    }

    public void applyUnsupportedHeader(ExceptionMetadata metadata) {
        throw new UnsupportedFeatureException("Unsupported prolog declaration.", metadata);
    }

    private void bindSchemaNamespace(SchemaImport schema) {
        if (schema.getBindingKind() == SchemaImport.BindingKind.NONE) {
            return;
        }
        String namespace = schema.getTargetNamespace();
        if (schema.getBindingKind() == SchemaImport.BindingKind.DEFAULT_ELEMENT_NAMESPACE) {
            this.translationContext.bindNamespace("", namespace, schema.getMetadata());
            return;
        }
        if (namespace.isEmpty()) {
            throw new SemanticException(
                    "A schema import cannot bind a prefix to a zero-length target namespace.",
                    ErrorCode.SchemaImportWithoutTargetNamespaceErrorCode,
                    schema.getMetadata());
        }
        String prefix = schema.getPrefix();
        if (prefix.equals("xml") || prefix.equals("xmlns")) {
            throw new PredefinedPrefixInNamespaceDeclarationException(
                    "Schema import prefix " + prefix + " is reserved.", schema.getMetadata());
        }
        this.translationContext.bindNamespace(prefix, namespace, schema.getMetadata());
    }

    /** Complete header processing by loading schemas before any declaration is translated. */
    public void finishHeader(ExceptionMetadata metadata) {
        XmlSchemaCatalogLoader.load(
                        this.schemas,
                        this.translationContext.moduleContext().getStaticBaseURI(),
                        this.translationContext.compilationConfiguration())
                .ifPresent(catalog -> this.translationContext
                        .moduleContext()
                        .getInScopeSchemaTypes()
                        .importSchema(catalog, metadata));
    }

    public void addVariable(VariableDeclaration declaration, ExceptionMetadata metadata) {
        validateNamespace("Variable", declaration.getVariableName(), metadata);
        this.variables.add(declaration);
    }

    public void addContextItem(VariableDeclaration declaration) {
        this.variables.add(declaration);
    }

    public void addFunction(InlineFunctionExpression declaration, ExceptionMetadata metadata) {
        validateNamespace("Function", declaration.getName(), metadata);
        this.functions.add(new FunctionDeclaration(declaration, metadata));
    }

    public void addType(TypeDeclaration declaration, ExceptionMetadata metadata) {
        validateNamespace("Type", declaration.getDefinition().getName(), metadata);
        this.types.add(declaration);
    }

    public void addOption(OptionDeclaration declaration) {
        this.options.add(declaration);
    }

    public Prolog build(ExceptionMetadata metadata) {
        Prolog result = new Prolog(this.variables, this.functions, this.types, metadata);
        this.modules.forEach(result::addImportedModule);
        this.schemas.forEach(result::addSchemaImport);
        this.options.forEach(result::addDeclaration);
        return result;
    }

    private void validateNamespace(String kind, Name name, ExceptionMetadata metadata) {
        if (!this.translationContext.isMainModule()
                && (name.getNamespace() == null || !name.getNamespace().equals(this.libraryNamespace))) {
            throw new NamespaceDoesNotMatchModuleException(
                    kind
                            + " "
                            + name.getLocalName()
                            + ": namespace "
                            + name.getNamespace()
                            + " must match module namespace "
                            + this.libraryNamespace,
                    metadata);
        }
    }

    // endregion
}
