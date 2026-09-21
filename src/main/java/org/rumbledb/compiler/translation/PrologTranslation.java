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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.ModuleImportLoader;
import org.rumbledb.compiler.context.ContextItemDeclContext;
import org.rumbledb.compiler.context.FunctionDeclContext;
import org.rumbledb.compiler.context.ModuleImportContext;
import org.rumbledb.compiler.context.OptionDeclContext;
import org.rumbledb.compiler.context.SchemaImportContext;
import org.rumbledb.compiler.context.VarDeclContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.utils.FunctionDeclarationValidator;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.DuplicateModuleTargetNamespaceException;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.exceptions.EmptyModuleURIException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneBoundarySpaceDeclarationException;
import org.rumbledb.exceptions.MoreThanOneCopyNamespacesDeclarationException;
import org.rumbledb.exceptions.MoreThanOneEmptyOrderDeclarationException;
import org.rumbledb.exceptions.MultipleBaseURIException;
import org.rumbledb.exceptions.NamespaceDoesNotMatchModuleException;
import org.rumbledb.exceptions.NamespacePrefixBoundTwiceException;
import org.rumbledb.exceptions.PredefinedPrefixInNamespaceDeclarationException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.OptionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;
import org.rumbledb.xml.schema.XmlSchemaCatalogLoader;

/**
 * Handles shared prolog translation state, syntax translation, and validation
 * for both Jsoniq and XQuery frontends.
 */
public final class PrologTranslation {

    @FunctionalInterface
    public interface NamespaceBinder {
        void bind(String prefix, String namespace, ExceptionMetadata metadata);
    }

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

    public PrologTranslation(TranslationContext translationContext, String libraryNamespace) {
        this.translationContext = translationContext;
        this.libraryNamespace = libraryNamespace;
    }

    // region Prolog Import Syntax Translators

    public static <UriLiteralCtx extends ParserRuleContext> SchemaImport schemaImport(
            SchemaImportContext<UriLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<UriLiteralCtx, String> processURILiteral) {
        String targetNamespace = URILiteralUtils.normalizeAsAnyURI(processURILiteral.apply(ctx.targetNamespace()));
        List<String> locationHints = ctx.locations().stream()
                .map(processURILiteral)
                .map(URILiteralUtils::normalizeAsAnyURI)
                .collect(Collectors.toList());
        return new SchemaImport(
                targetNamespace,
                ctx.bindingKind(),
                ctx.prefix(),
                locationHints,
                translationContext.metadata(ctx.context()));
    }

    public static <UriLiteralCtx extends ParserRuleContext> LibraryModule moduleImport(
            ModuleImportContext<UriLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<UriLiteralCtx, String> processURILiteral,
            NamespaceBinder bindNamespace) {
        ExceptionMetadata metadata = translationContext.metadata(ctx.context());
        String namespace = processURILiteral.apply(ctx.targetNamespace());
        if (namespace.isEmpty()) {
            throw new EmptyModuleURIException("Module URI is empty.", metadata);
        }
        if (ctx.prefix() != null) {
            String prefix = ctx.prefix();
            if (prefix.equals("xml") || prefix.equals("xmlns")) {
                throw new PredefinedPrefixInNamespaceDeclarationException(
                        "Module import prefix " + prefix + " is reserved.", metadata);
            }
        }
        namespace = URILiteralUtils.normalizeAsAnyURI(namespace);
        List<String> locationHints = ctx.locations().stream()
                .map(processURILiteral)
                .map(URILiteralUtils::normalizeAsAnyURI)
                .collect(Collectors.toList());
        LibraryModule libraryModule = ModuleImportLoader.load(
                namespace,
                locationHints,
                translationContext.moduleContext(),
                translationContext.compilationConfiguration(),
                metadata);
        if (ctx.prefix() != null) {
            bindNamespace.bind(ctx.prefix(), libraryModule.getNamespace(), metadata);
        }
        return libraryModule;
    }

    // endregion

    // region Prolog Declaration Syntax Translators

    public static <EqNameCtx extends ParserRuleContext, StringLiteralCtx extends ParserRuleContext>
            OptionDeclaration optionDecl(
                    OptionDeclContext<EqNameCtx, StringLiteralCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<StringLiteralCtx, String> processStringLiteral) {
        Name name = parseEqName.apply(ctx.name(), NameRole.NO_DEFAULT_NAMESPACE);
        String value = processStringLiteral.apply(ctx.value());
        return new OptionDeclaration(name, value, translationContext.metadata(ctx.context()));
    }

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            VariableDeclaration varDecl(
                    VarDeclContext<AnnotationsCtx, VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Node> visitExprSingle) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        SequenceType seq = null;
        Name var = parseVariableBinding.apply(ctx.varBinding());
        if (ctx.sequenceType() != null) {
            seq = processSequenceType.apply(ctx.sequenceType());
        }
        boolean external = ctx.isExternal();
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = (Expression) visitExprSingle.apply(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }
        return new VariableDeclaration(
                var,
                external,
                seq,
                expr,
                annotations,
                translationContext.metadata(ctx.context()),
                translationContext.metadata(ctx.varBinding()));
    }

    public static <SeqTypeCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            VariableDeclaration contextItemDecl(
                    ContextItemDeclContext<SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Node> visitExprSingle) {
        SequenceType seq = null;
        Name var = Name.CONTEXT_ITEM;
        if (ctx.sequenceType() != null) {
            seq = processSequenceType.apply(ctx.sequenceType());
        }
        boolean external = ctx.isExternal();
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = (Expression) visitExprSingle.apply(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }
        return new VariableDeclaration(var, external, seq, expr, null, translationContext.metadata(ctx.context()));
    }

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    FunctionNameCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ReturnTypeCtx extends ParserRuleContext,
                    FnBodyCtx extends ParserRuleContext>
            InlineFunctionExpression functionDecl(
                    FunctionDeclContext<
                                    AnnotationsCtx,
                                    FunctionNameCtx,
                                    VarBindingCtx,
                                    SeqTypeCtx,
                                    ReturnTypeCtx,
                                    FnBodyCtx>
                            ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<FunctionNameCtx, Name> parseFunctionName,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ReturnTypeCtx, SequenceType> processReturnType,
                    Function<FnBodyCtx, Node> visitStatementsAndOptionalExpr) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        Name name = parseFunctionName.apply(ctx.functionName());
        FunctionDeclarationValidator.validateFunctionName(name, translationContext.metadata(ctx.functionName()));
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        for (FunctionDeclContext.FunctionParam<VarBindingCtx, SeqTypeCtx> param : ctx.params()) {
            Name paramName = parseVariableBinding.apply(param.name());
            SequenceType paramType;
            if (fnParams.containsKey(paramName)) {
                throw new DuplicateParamNameException(name, paramName, translationContext.metadata(param.context()));
            }
            if (param.sequenceType() != null) {
                paramType = processSequenceType.apply(param.sequenceType());
            } else {
                paramType = SequenceType.createSequenceType("item*");
            }
            fnParams.put(paramName, paramType);
        }

        if (ctx.returnType() != null) {
            fnReturnType = processReturnType.apply(ctx.returnType());
        }

        StatementsAndOptionalExpr funcBody =
                (StatementsAndOptionalExpr) visitStatementsAndOptionalExpr.apply(ctx.fnBody());

        boolean isExternal = ctx.isExternal();

        return new InlineFunctionExpression(
                annotations,
                name,
                fnParams,
                fnReturnType,
                funcBody,
                isExternal,
                translationContext.metadata(ctx.context()),
                translationContext.metadata(ctx.functionName()));
    }

    // endregion

    // region State-Manipulating Header Receivers

    public void bindNamespace(String prefix, String uri, ExceptionMetadata metadata) {
        bindNamespace(this.translationContext, prefix, uri, metadata);
    }

    public static void bindNamespace(
            TranslationContext translationContext, String prefix, String namespace, ExceptionMetadata metadata) {
        boolean success = !prefix.isEmpty() && namespace.isEmpty()
                ? translationContext.moduleContext().unbindNamespace(prefix)
                : translationContext.moduleContext().bindNamespace(prefix, namespace);
        if (!success) {
            throw new NamespacePrefixBoundTwiceException("Prefix " + prefix + " is bound twice.", metadata);
        }
    }

    public void applyDefaultNamespace(boolean function, String uri, ExceptionMetadata metadata) {
        if (function) {
            if (this.defaultFunctionNamespaceSet) {
                throw new SemanticException("The default function namespace has already been declared.", metadata);
            }
            this.translationContext.moduleContext().setDefaultFunctionNamespaceUri(uri);
            this.defaultFunctionNamespaceSet = true;
        } else {
            bindNamespace("", uri, metadata);
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

    public void applyDecimalFormat(Runnable translate) {
        translate.run();
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
            bindNamespace("", namespace, schema.getMetadata());
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
        bindNamespace(prefix, namespace, schema.getMetadata());
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

    public void registerDeclaration(Node declaration, ExceptionMetadata metadata) {
        if (declaration == null) {
            return;
        }
        if (declaration instanceof VariableDeclaration varDecl) {
            if (!varDecl.getVariableName().equals(Name.CONTEXT_ITEM)) {
                validateNamespace("Variable", varDecl.getVariableName(), metadata);
            }
            this.variables.add(varDecl);
        } else if (declaration instanceof InlineFunctionExpression fn) {
            validateNamespace("Function", fn.getName(), metadata);
            this.functions.add(new FunctionDeclaration(fn, metadata));
        } else if (declaration instanceof TypeDeclaration type) {
            validateNamespace("Type", type.getDefinition().getName(), metadata);
            this.types.add(type);
        } else if (declaration instanceof OptionDeclaration option) {
            this.options.add(option);
        }
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
