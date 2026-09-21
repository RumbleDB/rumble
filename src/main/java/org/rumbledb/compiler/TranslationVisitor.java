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
package org.rumbledb.compiler;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.api.Item;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.context.AdditiveExprContext;
import org.rumbledb.compiler.context.AndExprContext;
import org.rumbledb.compiler.context.ArrowExprContext;
import org.rumbledb.compiler.context.CommaExprContext;
import org.rumbledb.compiler.context.ComparisonExprContext;
import org.rumbledb.compiler.context.ContextItemDeclContext;
import org.rumbledb.compiler.context.CountClauseContext;
import org.rumbledb.compiler.context.EnclosedExprContext;
import org.rumbledb.compiler.context.FlworExprContext;
import org.rumbledb.compiler.context.ForClauseContext;
import org.rumbledb.compiler.context.ForVarContext;
import org.rumbledb.compiler.context.FunctionCallContext;
import org.rumbledb.compiler.context.FunctionDeclContext;
import org.rumbledb.compiler.context.GroupByClauseContext;
import org.rumbledb.compiler.context.IfExprContext;
import org.rumbledb.compiler.context.IntersectExceptExprContext;
import org.rumbledb.compiler.context.LetClauseContext;
import org.rumbledb.compiler.context.LetVarContext;
import org.rumbledb.compiler.context.LibraryModuleContext;
import org.rumbledb.compiler.context.LiteralExprContext;
import org.rumbledb.compiler.context.MainModuleContext;
import org.rumbledb.compiler.context.ModuleImportContext;
import org.rumbledb.compiler.context.MultiplicativeExprContext;
import org.rumbledb.compiler.context.NameTestContext;
import org.rumbledb.compiler.context.NamedFunctionRefContext;
import org.rumbledb.compiler.context.OptionDeclContext;
import org.rumbledb.compiler.context.OrExprContext;
import org.rumbledb.compiler.context.OrderByClauseContext;
import org.rumbledb.compiler.context.ParenthesizedExprContext;
import org.rumbledb.compiler.context.ProgramContext;
import org.rumbledb.compiler.context.QuantifiedExprContext;
import org.rumbledb.compiler.context.RangeExprContext;
import org.rumbledb.compiler.context.SchemaImportContext;
import org.rumbledb.compiler.context.SimpleMapExprContext;
import org.rumbledb.compiler.context.SingleTypeCheckExprContext;
import org.rumbledb.compiler.context.StringConcatExprContext;
import org.rumbledb.compiler.context.SwitchExprContext;
import org.rumbledb.compiler.context.TryCatchExprContext;
import org.rumbledb.compiler.context.TypeCheckExprContext;
import org.rumbledb.compiler.context.TypeswitchExprContext;
import org.rumbledb.compiler.context.UnaryExprContext;
import org.rumbledb.compiler.context.UnionExprContext;
import org.rumbledb.compiler.context.ValueExprContext;
import org.rumbledb.compiler.context.VarDeclContext;
import org.rumbledb.compiler.context.VarRefContext;
import org.rumbledb.compiler.context.WhereClauseContext;
import org.rumbledb.compiler.context.WindowClauseContext;
import org.rumbledb.compiler.translation.ArithmeticTranslation;
import org.rumbledb.compiler.translation.ComparisonTranslation;
import org.rumbledb.compiler.translation.ControlTranslation;
import org.rumbledb.compiler.translation.FlworTranslation;
import org.rumbledb.compiler.translation.LogicTranslation;
import org.rumbledb.compiler.translation.ModuleTranslation;
import org.rumbledb.compiler.translation.PostfixTranslation;
import org.rumbledb.compiler.translation.PrimaryTranslation;
import org.rumbledb.compiler.translation.PrologTranslation;
import org.rumbledb.compiler.translation.QuantifiedTranslation;
import org.rumbledb.compiler.translation.SequenceTranslation;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.translation.TypeTranslation;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.block.BlockExpression;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.NamespaceDeclaration;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.TextNodeConstructorExpression;
import org.rumbledb.expressions.xml.TextNodeExpression;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.CommentTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NamespaceNodeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.PITest;
import org.rumbledb.expressions.xml.node_test.SchemaNodeTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.jsoniq.JsoniqParser.UriLiteralContext;
import org.rumbledb.parser.jsoniq.JsoniqParserBaseVisitor;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.types.AttributeNodeItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.ItemTypeReference;
import org.rumbledb.types.SequenceType;

/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 *
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 */
@Log4j2
public class TranslationVisitor extends JsoniqParserBaseVisitor<Node> {

    private String libraryModuleNamespace;
    private final CommonTokenStream jsoniqTokenStream;
    private final TranslationContext translationContext;

    public TranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings,
            String code,
            CommonTokenStream jsoniqTokenStream) {
        this.translationContext =
                new TranslationContext(moduleContext, compilationConfiguration, externalBindings, isMainModule, code);
        this.jsoniqTokenStream = jsoniqTokenStream;

        String queryLanguage =
                this.translationContext.configuration().semantics().queryLanguage();
        if (queryLanguage.equals("jsoniq10")) {
            this.translationContext.moduleContext().setQueryLanguage("jsoniq10");
        } else if (queryLanguage.equals("jsoniq31")) {
            this.translationContext.moduleContext().setQueryLanguage("jsoniq31");
        } else if (queryLanguage.equals("jsoniq40")) {
            this.translationContext.moduleContext().setQueryLanguage("jsoniq40");
        }
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty()) {
            String version = processStringLiteral(ctx.vers).trim();
            if (version.equals("1.0")) {
                this.translationContext.moduleContext().setQueryLanguage("jsoniq10");
            } else if (version.equals("3.1")) {
                this.translationContext.moduleContext().setQueryLanguage("jsoniq31");
            } else if (version.equals("4.0")) {
                this.translationContext.moduleContext().setQueryLanguage("jsoniq40");
            } else {
                throw new JsoniqVersionException(createMetadataFromContext(ctx));
            }
        }
        if (this.translationContext.isMainModule()) {
            if (ctx.mainModule() != null) {
                return this.visitMainModule(ctx.mainModule());
            }
            throw new ParsingException(
                    "Main module expected, but library module found.", createMetadataFromContext(ctx));
        } else {
            if (ctx.libraryModule() != null) {
                return this.visitLibraryModule(ctx.libraryModule());
            }
            throw new ParsingException(
                    "Library module expected, but main module found.", createMetadataFromContext(ctx));
        }
    }

    @Override
    public Node visitMainModule(JsoniqParser.MainModuleContext ctx) {
        return ModuleTranslation.mainModule(
                MainModuleContext.from(ctx), this.translationContext, this::visitProlog, this::visitProgram);
    }

    // region program
    @Override
    public Node visitProgram(JsoniqParser.ProgramContext ctx) {
        return ModuleTranslation.program(
                ProgramContext.from(ctx), this.translationContext, this::visitStatementsAndOptionalExpr);
    }

    // end region

    @Override
    public Node visitLibraryModule(JsoniqParser.LibraryModuleContext ctx) {
        return ModuleTranslation.libraryModule(
                LibraryModuleContext.from(ctx),
                this.translationContext,
                this::processURILiteral,
                ns -> this.libraryModuleNamespace = ns,
                this::bindNamespace,
                this::visitProlog);
    }

    @Override
    public Prolog visitProlog(JsoniqParser.PrologContext ctx) {
        return new PrologBuilder().build(ctx);
    }

    private class PrologBuilder extends JsoniqParserBaseVisitor<Void> {
        private final PrologTranslation translation = new PrologTranslation(
                TranslationVisitor.this.translationContext, TranslationVisitor.this.libraryModuleNamespace);

        Prolog build(JsoniqParser.PrologContext ctx) {
            if (ctx == null) {
                return null;
            }
            visit(ctx);
            return this.translation.build(createMetadataFromContext(ctx));
        }

        @Override
        public Void visitProlog(JsoniqParser.PrologContext ctx) {
            return visitChildren(ctx);
        }

        @Override
        public Void visitSetter(JsoniqParser.SetterContext ctx) {
            return visitChildren(ctx);
        }

        @Override
        public Void visitAnnotatedDecl(JsoniqParser.AnnotatedDeclContext ctx) {
            this.translation.loadSchemaCatalog(createMetadataFromContext(ctx));
            return visitChildren(ctx);
        }

        @Override
        public Void visitNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx) {
            this.translation.bindNamespace(
                    ctx.ncName().getText(), processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultNamespaceDecl(JsoniqParser.DefaultNamespaceDeclContext ctx) {
            boolean isFunction = ctx.type.getType() == JsoniqParser.KW_FUNCTION;
            this.translation.applyDefaultNamespace(
                    isFunction, processStringLiteral(ctx.stringLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitModuleImport(JsoniqParser.ModuleImportContext ctx) {
            this.translation.importModule(
                    PrologTranslation.moduleImport(
                            ModuleImportContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processURILiteral,
                            TranslationVisitor.this::bindNamespace),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitSchemaImport(JsoniqParser.SchemaImportContext ctx) {
            this.translation.importSchema(
                    PrologTranslation.schemaImport(
                            SchemaImportContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processURILiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitConstructionDecl(JsoniqParser.ConstructionDeclContext ctx) {
            boolean value = ctx.type.getType() == JsoniqParser.KW_PRESERVE;
            this.translation.applyBooleanSetting(
                    PrologTranslation.BooleanSettingKind.CONSTRUCTION, value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBoundarySpaceDecl(JsoniqParser.BoundarySpaceDeclContext ctx) {
            boolean value = ctx.type.getType() == JsoniqParser.KW_PRESERVE;
            this.translation.applyBooleanSetting(
                    PrologTranslation.BooleanSettingKind.BOUNDARY_SPACE, value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitEmptyOrderDecl(JsoniqParser.EmptyOrderDeclContext ctx) {
            boolean value = ctx.emptySequenceOrder.getText().equals("least");
            this.translation.applyBooleanSetting(
                    PrologTranslation.BooleanSettingKind.EMPTY_ORDER, value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitCopyNamespacesDecl(JsoniqParser.CopyNamespacesDeclContext ctx) {
            boolean preserve = ctx.preserveMode().KW_PRESERVE() != null;
            boolean inherit = ctx.inheritMode().KW_INHERIT() != null;
            this.translation.applyCopyNamespaces(preserve, inherit, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBaseURIDecl(JsoniqParser.BaseURIDeclContext ctx) {
            this.translation.applyBaseUri(processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultCollationDecl(JsoniqParser.DefaultCollationDeclContext ctx) {
            this.translation.applyDefaultCollation(
                    processURILiteral(ctx.uriLiteral()),
                    createMetadataFromContext(ctx.uriLiteral()),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx) {
            this.translation.applyDecimalFormat(
                    () -> processDecimalFormatDeclaration(ctx, createMetadataFromContext(ctx)));
            return null;
        }

        @Override
        public Void visitOrderingModeDecl(JsoniqParser.OrderingModeDeclContext ctx) {
            this.translation.applyUnsupportedHeader(createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitVarDecl(JsoniqParser.VarDeclContext ctx) {
            this.translation.registerDeclaration(
                    PrologTranslation.varDecl(
                            VarDeclContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processAnnotations,
                            TranslationVisitor.this::parseVariableBinding,
                            TranslationVisitor.this::processSequenceType,
                            TranslationVisitor.this::visitExprSingle),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitContextItemDecl(JsoniqParser.ContextItemDeclContext ctx) {
            this.translation.registerDeclaration(
                    PrologTranslation.contextItemDecl(
                            ContextItemDeclContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processSequenceType,
                            TranslationVisitor.this::visitExprSingle),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
            this.translation.registerDeclaration(
                    PrologTranslation.functionDecl(
                            FunctionDeclContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processAnnotations,
                            TranslationVisitor.this::parseFunctionName,
                            TranslationVisitor.this::parseVariableBinding,
                            TranslationVisitor.this::processSequenceType,
                            TranslationVisitor.this::processSequenceType,
                            TranslationVisitor.this::visitStatementsAndOptionalExpr),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitOptionDecl(JsoniqParser.OptionDeclContext ctx) {
            this.translation.registerDeclaration(
                    PrologTranslation.optionDecl(
                            OptionDeclContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::parseEqName,
                            TranslationVisitor.this::processStringLiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitTypeDecl(JsoniqParser.TypeDeclContext ctx) {
            this.translation.registerDeclaration(processTypeDecl(ctx), createMetadataFromContext(ctx));
            return null;
        }
    }

    private String processStringLiteral(JsoniqParser.StringLiteralContext ctx) {
        // Note: tokenStrem.getText() preserves the original string literal including hidden tokens (e.g., whitespace,
        // comments)
        // We need original string literal with delimiters for accurate parsing of escape sequences and delimiters.
        String source = this.jsoniqTokenStream.getText(ctx.getSourceInterval());
        return StringLiteralUtils.parseJsoniq(source, createMetadataFromContext(ctx));
    }

    private Item parseJSONItem(String value, ExceptionMetadata metadata) {
        return ItemParser.getItemFromJSONString(
                value,
                JSONParsingOptions.defaultInstance(true),
                this.translationContext.configuration().semantics().xmlVersion(),
                true,
                metadata);
    }

    public Name parseFunctionName(JsoniqParser.FunctionNameContext ctx) {
        return this.translationContext.names().resolveFunctionName(ctx.getText(), createMetadataFromContext(ctx));
    }

    /**
     * Parse an EQName. Delegates to {@link #parseName} for the {@code qname} branch; URI-qualified names use
     * {@link URIQualifiedNameParser}.
     */
    public Name parseEqName(JsoniqParser.EqNameContext ctx, NameRole role) {
        if (ctx.qname() != null) {
            return parseName(ctx.qname(), role);
        }
        return URIQualifiedNameParser.parse(ctx.URIQualifiedName().getText(), createMetadataFromContext(ctx));
    }

    /** Adapts the JSONiq QName grammar to the shared role-aware resolver. */
    public Name parseName(JsoniqParser.QnameContext ctx, NameRole role) {
        return this.translationContext
                .names()
                .resolveQName(
                        ctx.FullQName() == null ? null : ctx.FullQName().getText(),
                        ctx.ns == null ? null : ctx.ns.getText(),
                        ctx.local_name == null ? null : ctx.local_name.getText(),
                        role,
                        createMetadataFromContext(ctx));
    }

    private TypeDeclaration processTypeDecl(JsoniqParser.TypeDeclContext ctx) {
        String definitionString = ctx.type_definition.getText();
        Item definitionItem = null;
        if (definitionString.trim().startsWith("\"")) {
            throw new InvalidSchemaException(
                    "The schema definition must be an object.", createMetadataFromContext(ctx));
        }
        if (definitionString.trim().startsWith("[")) {
            throw new InvalidSchemaException(
                    "Schema definitions for top-level array types are not supported yet. Please let us know if you would like for us to prioritize this feature.",
                    createMetadataFromContext(ctx));
        }
        try {
            definitionItem = parseJSONItem(definitionString, createMetadataFromContext(ctx));
        } catch (InvalidJSONException | ParsingException e) {
            ParsingException pe = new ParsingException(
                    "A type definition must be a JSON literal: no dynamic evaluation is allowed.",
                    createMetadataFromContext(ctx));
            pe.initCause(e);
            throw pe;
        }
        Name name = parseName(ctx.qname(), NameRole.TYPE);
        String schemaLanguage = null;
        if (ctx.schema != null) {
            schemaLanguage = ctx.schema.getText();
        } else {
            schemaLanguage = "jsoundcompact";
        }
        ItemType type = null;
        switch (schemaLanguage) {
            case "jsoundcompact":
                type = ItemTypeFactory.createItemTypeFromJSoundCompactItem(
                        name, definitionItem, this.translationContext.moduleContext());
                break;
            case "jsoundverbose":
                type = ItemTypeFactory.createItemTypeFromJSoundVerboseItem(
                        name, definitionItem, this.translationContext.moduleContext());
                break;
            case "jsonschema":
                type = ItemTypeFactory.createItemTypeFromJSONSchemaItem(
                        name, definitionItem, this.translationContext.moduleContext());
                break;
            default:
                throw new OurBadException(
                        "Unrecognized schema syntax: " + schemaLanguage, createMetadataFromContext(ctx));
        }
        return new TypeDeclaration(type, createMetadataFromContext(ctx));
    }
    // endregion

    // region expr
    @Override
    public Node visitExpr(JsoniqParser.ExprContext ctx) {
        return SequenceTranslation.expr(CommaExprContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public Node visitExprSingle(JsoniqParser.ExprSingleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof JsoniqParser.ExprSimpleContext exprSimpleContext) {
            return this.visitExprSimple(exprSimpleContext);
        }
        if (content instanceof JsoniqParser.FlworExprContext flworExprContext) {
            return this.visitFlworExpr(flworExprContext);
        }
        if (content instanceof JsoniqParser.IfExprContext ifExprContext) {
            return this.visitIfExpr(ifExprContext);
        }
        if (content instanceof JsoniqParser.SwitchExprContext switchExprContext) {
            return this.visitSwitchExpr(switchExprContext);
        }
        if (content instanceof JsoniqParser.TypeswitchExprContext typeswitchExprContext) {
            return this.visitTypeswitchExpr(typeswitchExprContext);
        }
        if (content instanceof JsoniqParser.TryCatchExprContext tryCatchExprContext) {
            return this.visitTryCatchExpr(tryCatchExprContext);
        }
        throw new OurBadException(
                "Unrecognized ExprSingle:" + content.getClass().getName());
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Node visitExprSimple(JsoniqParser.ExprSimpleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof JsoniqParser.OrExprContext orExprContext) {
            return this.visitOrExpr(orExprContext);
        }
        if (content instanceof JsoniqParser.QuantifiedExprContext quantifiedExprContext) {
            return this.visitQuantifiedExpr(quantifiedExprContext);
        }
        if (content instanceof JsoniqParser.DeleteExprContext deleteExprContext) {
            return this.visitDeleteExpr(deleteExprContext);
        }
        if (content instanceof JsoniqParser.InsertExprContext insertExprContext) {
            return this.visitInsertExpr(insertExprContext);
        }
        if (content instanceof JsoniqParser.ReplaceExprContext replaceExprContext) {
            return this.visitReplaceExpr(replaceExprContext);
        }
        if (content instanceof JsoniqParser.RenameExprContext renameExprContext) {
            return this.visitRenameExpr(renameExprContext);
        }
        if (content instanceof JsoniqParser.AppendExprContext appendExprContext) {
            return this.visitAppendExpr(appendExprContext);
        }
        if (content instanceof JsoniqParser.TransformExprContext transformExprContext) {
            return this.visitTransformExpr(transformExprContext);
        }
        if (content instanceof JsoniqParser.PathExprContext pathExprContext) {
            return this.visitPathExpr(pathExprContext);
        }

        if (content instanceof JsoniqParser.CreateCollectionExprContext createCollectionExprContext) {
            return this.visitCreateCollectionExpr(createCollectionExprContext);
        }
        if (content instanceof JsoniqParser.DeleteIndexExprContext deleteIndexExprContext) {
            return this.visitDeleteIndexExpr(deleteIndexExprContext);
        }
        if (content instanceof JsoniqParser.DeleteSearchExprContext deleteSearchExprContext) {
            return this.visitDeleteSearchExpr(deleteSearchExprContext);
        }
        if (content instanceof JsoniqParser.EditCollectionExprContext editCollectionExprContext) {
            return this.visitEditCollectionExpr(editCollectionExprContext);
        }
        if (content instanceof JsoniqParser.InsertIndexExprContext insertIndexExprContext) {
            return this.visitInsertIndexExpr(insertIndexExprContext);
        }
        if (content instanceof JsoniqParser.InsertSearchExprContext insertSearchExprContext) {
            return this.visitInsertSearchExpr(insertSearchExprContext);
        }
        if (content instanceof JsoniqParser.TruncateCollectionExprContext truncateCollectionExprContext) {
            return this.visitTruncateCollectionExpr(truncateCollectionExprContext);
        }
        throw new OurBadException("Translation Visitor: Unrecognized ExprSimple.");
    }

    // endregion

    // region EnclosedExpression
    @Override
    public Node visitEnclosedExpression(JsoniqParser.EnclosedExpressionContext ctx) {
        return SequenceTranslation.enclosedExpr(
                EnclosedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }
    // endregion

    // region Flowr
    @Override
    public Node visitFlworExpr(JsoniqParser.FlworExprContext ctx) {
        return FlworTranslation.flworExpr(
                FlworExprContext.from(ctx),
                this.translationContext,
                child -> this.visit(child) instanceof Clause clause ? clause : null,
                this::visitExprSingle);
    }

    @Override
    public Node visitForClause(JsoniqParser.ForClauseContext ctx) {
        return FlworTranslation.forClause(
                ForClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitForVar(JsoniqParser.ForVarContext ctx) {
        return FlworTranslation.forVar(
                ForVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitLetClause(JsoniqParser.LetClauseContext ctx) {
        return FlworTranslation.letClause(
                LetClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitLetVar(JsoniqParser.LetVarContext ctx) {
        return FlworTranslation.letVar(
                LetVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitWindowClause(JsoniqParser.WindowClauseContext ctx) {
        return ctx.tumblingWindowClause() != null
                ? visitTumblingWindowClause(ctx.tumblingWindowClause())
                : visitSlidingWindowClause(ctx.slidingWindowClause());
    }

    @Override
    public Node visitTumblingWindowClause(JsoniqParser.TumblingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitSlidingWindowClause(JsoniqParser.SlidingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public Node visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        return FlworTranslation.groupByClause(
                GroupByClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public Node visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        return FlworTranslation.orderByClause(
                OrderByClauseContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public Node visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        return FlworTranslation.whereClause(
                WhereClauseContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public Node visitCountClause(JsoniqParser.CountClauseContext ctx) {
        return FlworTranslation.countClause(
                CountClauseContext.from(ctx), this.translationContext, this::parseVariableBinding);
    }
    // endregion

    // region operational
    @Override
    public Node visitOrExpr(JsoniqParser.OrExprContext ctx) {
        return LogicTranslation.orExpr(OrExprContext.from(ctx), this.translationContext, this::visitAndExpr);
    }

    @Override
    public Node visitAndExpr(JsoniqParser.AndExprContext ctx) {
        return LogicTranslation.andExpr(AndExprContext.from(ctx), this.translationContext, this::visitNotExpr);
    }

    @Override
    public Node visitNotExpr(JsoniqParser.NotExprContext ctx) {
        Expression mainExpression = (Expression) this.visitComparisonExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        return new NotExpression(mainExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        return ComparisonTranslation.comparisonExpr(
                ComparisonExprContext.from(ctx), this.translationContext, this::visitStringConcatExpr);
    }

    @Override
    public Node visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        return SequenceTranslation.stringConcatExpr(
                StringConcatExprContext.from(ctx), this.translationContext, this::visitRangeExpr);
    }

    @Override
    public Node visitRangeExpr(JsoniqParser.RangeExprContext ctx) {
        return SequenceTranslation.rangeExpr(
                RangeExprContext.from(ctx), this.translationContext, this::visitAdditiveExpr);
    }

    @Override
    public Node visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        return ArithmeticTranslation.additiveExpr(
                AdditiveExprContext.from(ctx), this.translationContext, this::visitMultiplicativeExpr);
    }

    @Override
    public Node visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        return ArithmeticTranslation.multiplicativeExpr(
                MultiplicativeExprContext.from(ctx),
                this.translationContext,
                this.jsoniqTokenStream,
                this::visitUnionExpr);
    }

    @Override
    public Node visitUnionExpr(JsoniqParser.UnionExprContext ctx) {
        return SequenceTranslation.unionExpr(
                UnionExprContext.from(ctx), this.translationContext, this::visitIntersectExceptExpr);
    }

    @Override
    public Node visitIntersectExceptExpr(JsoniqParser.IntersectExceptExprContext ctx) {
        return SequenceTranslation.intersectExceptExpr(
                IntersectExceptExprContext.from(ctx), this.translationContext, this::visitInstanceOfExpr);
    }

    @Override
    public Node visitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx) {
        return PostfixTranslation.simpleMapExpr(
                SimpleMapExprContext.from(ctx), this.translationContext, this::visitPathExpr, this::visitPathExpr);
    }

    @Override
    public Node visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx) {
        return TypeTranslation.instanceOfExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitIsStaticallyExpr,
                this::processSequenceType);
    }

    @Override
    public Node visitIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx) {
        return TypeTranslation.isStaticallyExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitTreatExpr,
                this::processSequenceType);
    }

    @Override
    public Node visitTreatExpr(JsoniqParser.TreatExprContext ctx) {
        return TypeTranslation.treatExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastableExpr,
                this::processSequenceType);
    }

    @Override
    public Node visitCastableExpr(JsoniqParser.CastableExprContext ctx) {
        return TypeTranslation.castableExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastExpr,
                this::processSingleType);
    }

    @Override
    public Node visitCastExpr(JsoniqParser.CastExprContext ctx) {
        return TypeTranslation.castExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitArrowExpr,
                this::processSingleType);
    }

    @Override
    public Node visitArrowExpr(JsoniqParser.ArrowExprContext ctx) {
        return PostfixTranslation.arrowExpr(
                ArrowExprContext.from(ctx),
                this.translationContext,
                this::visitUnaryExpr,
                this::parseEqName,
                this::visitVarRef,
                this::visitParenthesizedExpr,
                this::getArgumentsFromArgumentListContext);
    }

    @Override
    public Node visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        return ArithmeticTranslation.unaryExpr(
                UnaryExprContext.from(ctx), this.translationContext, this::visitValueExpr);
    }

    @Override
    public Node visitValueExpr(JsoniqParser.ValueExprContext ctx) {
        return PrimaryTranslation.valueExpr(
                ValueExprContext.from(ctx), this.translationContext, this::visitSimpleMapExpr, this::visitValidateExpr);
    }

    @Override
    public Node visitValidateExpr(JsoniqParser.ValidateExprContext ctx) {
        Expression mainExpr = (Expression) this.visitExpr(ctx.expr());
        SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
        return new ValidateTypeExpression(mainExpr, true, sequenceType, createMetadataFromContext(ctx));
    }
    // endregion

    // region update

    @Override
    public Node visitInsertExpr(JsoniqParser.InsertExprContext ctx) {
        Expression toInsertExpr;
        Expression posExpr = null;
        if (ctx.pairConstructor() != null && !ctx.pairConstructor().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                Node lhs = this.visitExprSingle(currentPair.lhs);
                if (lhs instanceof StepExpr stepExpr) {
                    if (this.translationContext
                            .moduleContext()
                            .getQueryLanguage()
                            .equals("jsoniq10")) {
                        keys.add(new StringLiteralExpression(
                                stepExpr.getNodeTest().toString(), lhs.getMetadata()));
                    } else {
                        throw new ParsingException(
                                "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --default-language jsoniq10 CLI option.",
                                lhs.getMetadata());
                    }
                } else {
                    keys.add((Expression) lhs);
                }
                values.add((Expression) this.visitExprSingle(currentPair.rhs));
            }
            toInsertExpr = new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
        } else if (ctx.to_insert_expr != null) {
            toInsertExpr = (Expression) this.visitExprSingle(ctx.to_insert_expr);
            if (ctx.pos_expr != null) {
                posExpr = (Expression) this.visitExprSingle(ctx.pos_expr);
            }
        } else {
            throw new OurBadException("Unrecognised expression to insert in Insert Expression");
        }
        Expression mainExpr = (Expression) this.visitExprSingle(ctx.main_expr);

        return new InsertExpression(mainExpr, toInsertExpr, posExpr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitDeleteExpr(JsoniqParser.DeleteExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        return new DeleteExpression(mainExpression, locatorExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitRenameExpr(JsoniqParser.RenameExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression nameExpression = (Expression) this.visitExprSingle(ctx.name_expr);
        return new RenameExpression(mainExpression, locatorExpression, nameExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitReplaceExpr(JsoniqParser.ReplaceExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression newExpression = (Expression) this.visitExprSingle(ctx.replacer_expr);
        return new ReplaceExpression(mainExpression, locatorExpression, newExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTransformExpr(JsoniqParser.TransformExprContext ctx) {
        List<CopyDeclaration> copyDecls = ctx.copyDecl().stream()
                .map(copyDeclCtx -> {
                    Name var = parseVariableBinding(copyDeclCtx.var_ref);
                    Expression expr = (Expression) this.visitExprSingle(copyDeclCtx.src_expr);
                    return new CopyDeclaration(var, expr);
                })
                .collect(Collectors.toList());
        Expression modifyExpression = (Expression) this.visitExprSingle(ctx.mod_expr);
        Expression returnExpression = (Expression) this.visitExprSingle(ctx.ret_expr);
        return new TransformExpression(copyDecls, modifyExpression, returnExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitAppendExpr(JsoniqParser.AppendExprContext ctx) {
        Expression arrayExpression = (Expression) this.visitExprSingle(ctx.array_expr);
        Expression toAppendExpression = (Expression) this.visitExprSingle(ctx.to_append_expr);
        return new AppendExpression(arrayExpression, toAppendExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCreateCollectionExpr(JsoniqParser.CreateCollectionExprContext ctx) {
        Expression collection = (Expression) this.visitExprSimple(ctx.collection_name);
        Expression contentExpression;
        if (ctx.content != null) {
            contentExpression = (Expression) this.visitExprSingle(ctx.content);
        } else {
            // use a CommaExpression as placeholder if the collection is created empty
            contentExpression = new CommaExpression(createMetadataFromContext(ctx));
        }
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        return new CreateCollectionExpression(collection, contentExpression, mode, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitDeleteIndexExpr(JsoniqParser.DeleteIndexExprContext ctx) {
        Expression collection = (Expression) this.visitExprSimple(ctx.collection_name);
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        boolean isFirst = (ctx.first != null);

        Expression numDelete = null;
        if (ctx.num != null) {
            numDelete = (Expression) this.visitExprSingle(ctx.num);
        }

        return new DeleteIndexFromCollectionExpression(
                collection, numDelete, isFirst, mode, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitDeleteSearchExpr(JsoniqParser.DeleteSearchExprContext ctx) {
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        return new DeleteSearchFromCollectionExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitEditCollectionExpr(JsoniqParser.EditCollectionExprContext ctx) {
        Expression targetExpression = (Expression) this.visitExprSingle(ctx.target);
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        return new EditCollectionExpression(targetExpression, contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitInsertIndexExpr(JsoniqParser.InsertIndexExprContext ctx) {
        Expression collection = (Expression) this.visitExprSimple(ctx.collection_name);
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        Expression pos = ctx.pos != null ? (Expression) this.visitExprSingle(ctx.pos) : null;
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        boolean isLast = (ctx.last != null);
        boolean isFirst = (ctx.first != null);

        return new InsertIndexIntoCollectionExpression(
                collection, contentExpression, pos, mode, isFirst, isLast, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitInsertSearchExpr(JsoniqParser.InsertSearchExprContext ctx) {
        Expression targetExpression = (Expression) this.visitExprSingle(ctx.target);
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        boolean isBefore = (ctx.before != null);
        return new InsertSearchIntoCollectionExpression(
                targetExpression, contentExpression, isBefore, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTruncateCollectionExpr(JsoniqParser.TruncateCollectionExprContext ctx) {
        Expression collectionName = (Expression) this.visitExprSimple(ctx.collection_name);
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        return new TruncateCollectionExpression(collectionName, mode, createMetadataFromContext(ctx));
    }

    public Expression getMainExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        Expression mainExpression = (Expression) this.visitPostfixExpr(ctx.main_expr);
        if (mainExpression instanceof ObjectLookupExpression objectLookupExpression) {
            return objectLookupExpression.getMainExpression();
        } else if (mainExpression instanceof ArrayLookupExpression arrayLookupExpression) {
            return arrayLookupExpression.getMainExpression();
        } else {
            throw new OurBadException("Unrecognized main expression found in update expression.");
        }
    }

    public Expression getLocatorExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        Expression mainExpression = (Expression) this.visitPostfixExpr(ctx.main_expr);
        if (mainExpression instanceof ObjectLookupExpression objectLookupExpression) {
            return objectLookupExpression.getLookupExpression();
        } else if (mainExpression instanceof ArrayLookupExpression arrayLookupExpression) {
            return arrayLookupExpression.getLookupExpression();
        } else {
            throw new OurBadException("Unrecognized main expression found in update expression.");
        }
    }

    // endregion

    // region postfix
    @Override
    public Node visitPostfixExpr(JsoniqParser.PostfixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext predicateContext) {
                Expression expr = (Expression) this.visitPredicate(predicateContext);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop()));
            } else if (child instanceof JsoniqParser.LookupContext lookupContext) {
                Expression expr = (Expression) this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop()));
            } else if (child instanceof JsoniqParser.ObjectLookupContext objectLookupContext) {
                Expression expr = (Expression) this.visitObjectLookup(objectLookupContext);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), objectLookupContext.getStop()));
            } else if (child instanceof JsoniqParser.ArrayLookupContext arrayLookupContext) {
                Expression expr = (Expression) this.visitArrayLookup(arrayLookupContext);
                mainExpression = new ArrayLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayLookupContext.getStop()));
            } else if (child instanceof JsoniqParser.ArrayUnboxingContext arrayUnboxingContext) {
                this.visitArrayUnboxing(arrayUnboxingContext);
                mainExpression = new ArrayUnboxingExpression(
                        mainExpression,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayUnboxingContext.getStop()));
            } else if (child instanceof JsoniqParser.ArgumentListContext argumentListContext) {
                List<Expression> arguments = getArgumentsFromArgumentListContext(argumentListContext);
                mainExpression = new DynamicFunctionCallExpression(
                        mainExpression,
                        arguments,
                        createMetadataFromRange(ctx.main_expr.getStart(), argumentListContext.getStop()));
            } else {
                throw new OurBadException("Unrecognized postfix expression found.");
            }
        }
        return mainExpression;
    }

    @Override
    public Node visitPredicate(JsoniqParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Node visitLookup(JsoniqParser.LookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    @Override
    public Node visitUnaryLookup(JsoniqParser.UnaryLookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    @Override
    public Node visitKeySpecifier(JsoniqParser.KeySpecifierContext ctx) {
        if (ctx.lt != null) {
            return new StringLiteralExpression(processStringLiteral(ctx.lt), createMetadataFromContext(ctx));
        }
        if (ctx.in != null) {
            return new IntegerLiteralExpression(ctx.in.getText(), createMetadataFromContext(ctx));
        }
        if (ctx.nc != null) {
            return new StringLiteralExpression(ctx.nc.getText(), createMetadataFromContext(ctx));
        }
        if (ctx.pe != null) {
            return this.visitParenthesizedExpr(ctx.pe);
        }
        if (ctx.wc != null) {
            // wildcard isn't an expression, return null and let lookupiterator handle it
            return null;
        }
        if (ctx.vr != null) {
            return this.visitVarRef(ctx.vr);
        }

        throw new OurBadException("Unrecognized lookup.");
    }

    @Override
    public Node visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        // TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        if (ctx.lt != null) {
            return new StringLiteralExpression(processStringLiteral(ctx.lt), createMetadataFromContext(ctx));
        }
        if (ctx.nc != null) {
            return new StringLiteralExpression(ctx.nc.getText(), createMetadataFromContext(ctx));
        }
        if (ctx.kw != null) {
            return new StringLiteralExpression(ctx.kw.getText(), createMetadataFromContext(ctx));
        }
        if (ctx.pe != null) {
            return this.visitParenthesizedExpr(ctx.pe);
        }
        if (ctx.vr != null) {
            return this.visitVarRef(ctx.vr);
        }
        if (ctx.ci != null) {
            return this.visitContextItemExpr(ctx.ci);
        }

        throw new OurBadException("Unrecognized object lookup.");
    }

    @Override
    public Node visitArrayLookup(JsoniqParser.ArrayLookupContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    // endregion

    // region primary
    // TODO [EXPRVISITOR] orderedExpr unorderedExpr;
    @Override
    public Node visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.VarRefContext varRefContext) {
            return this.visitVarRef(varRefContext);
        }
        if (child instanceof JsoniqParser.ObjectConstructorContext objectConstructorContext) {
            return this.visitObjectConstructor(objectConstructorContext);
        }
        if (child instanceof JsoniqParser.ArrayConstructorContext arrayConstructorContext) {
            return this.visitArrayConstructor(arrayConstructorContext);
        }
        if (child instanceof JsoniqParser.ParenthesizedExprContext parenthesizedExprContext) {
            return this.visitParenthesizedExpr(parenthesizedExprContext);
        }
        if (child instanceof JsoniqParser.LiteralContext literalContext) {
            return this.visitLiteral(literalContext);
        }
        if (child instanceof JsoniqParser.ContextItemExprContext contextItemExprContext) {
            return this.visitContextItemExpr(contextItemExprContext);
        }
        if (child instanceof JsoniqParser.FunctionCallContext functionCallContext) {
            return this.visitFunctionCall(functionCallContext);
        }
        if (child instanceof JsoniqParser.FunctionItemExprContext functionItemExprContext) {
            return this.visitFunctionItemExpr(functionItemExprContext);
        }
        if (child instanceof JsoniqParser.BlockExprContext blockExprContext) {
            return this.visitBlockExpr(blockExprContext);
        }
        if (child instanceof JsoniqParser.UnaryLookupContext unaryLookupContext) {
            return new UnaryLookupExpression(
                    (Expression) this.visitUnaryLookup(unaryLookupContext), createMetadataFromContext(ctx));
        }
        if (child instanceof JsoniqParser.NodeConstructorContext nodeConstructorContext) {
            return this.visitNodeConstructor(nodeConstructorContext);
        }
        if (child instanceof JsoniqParser.NumericLiteralContext) {
            return this.visitLiteral((JsoniqParser.LiteralContext) child);
        }
        if (child instanceof TerminalNode) {
            return PrimaryTranslation.literalExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        throw new UnsupportedFeatureException("Primary expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitLiteral(JsoniqParser.LiteralContext ctx) {
        return PrimaryTranslation.literal(
                LiteralExprContext.from(ctx), this.translationContext, this::processStringLiteral);
    }

    @Override
    public Node visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
        // no merging constructor, just visit the k/v pairs
        if (ctx.merge_operator == null
                || ctx.merge_operator.size() == 0
                || ctx.merge_operator.get(0).getText().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                Node lhs = this.visitExprSingle(currentPair.lhs);
                if (lhs instanceof StepExpr stepExpr) {
                    if (this.translationContext
                            .moduleContext()
                            .getQueryLanguage()
                            .equals("jsoniq10")) {
                        keys.add(new StringLiteralExpression(
                                stepExpr.getNodeTest().toString(), lhs.getMetadata()));
                    } else {
                        throw new ParsingException(
                                "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --default-language jsoniq10 CLI option.",
                                lhs.getMetadata());
                    }
                } else {
                    keys.add((Expression) lhs);
                }
                values.add((Expression) this.visitExprSingle(currentPair.rhs));
            }
            if (this.translationContext.moduleContext().getQueryLanguage().equals("jsoniq10")) {
                return new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
            } else {
                return new MapConstructorExpression(keys, values, createMetadataFromContext(ctx));
            }
        }

        Expression childExpr;
        childExpr = (Expression) this.visitExpr(ctx.expr());
        return new ObjectConstructorExpression(childExpr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitNodeConstructor(JsoniqParser.NodeConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.DirectConstructorContext directConstructorContext) {
            return this.visitDirectConstructor(directConstructorContext);
        }
        if (child instanceof JsoniqParser.ComputedConstructorContext computedConstructorContext) {
            return this.visitComputedConstructor(computedConstructorContext);
        }
        throw new UnsupportedFeatureException("Node constructor not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitDirectConstructor(JsoniqParser.DirectConstructorContext ctx) {
        if (ctx.COMMENT() != null) {
            String commentText = ctx.COMMENT().getText();
            String commentContent = commentText.substring(4, commentText.length() - 3);
            return new DirectCommentConstructorExpression(commentContent, createMetadataFromContext(ctx));
        }
        if (ctx.open_close != null) {
            return this.visitDirElemConstructorOpenClose(ctx);
        } else if (ctx.single_tag != null) {
            return this.visitDirElemConstructorSingleTag(ctx);
        } else if (ctx.PI() != null) {
            return this.visitDirPIConstructor(ctx.PI(), createMetadataFromContext(ctx));
        }
        throw new UnsupportedFeatureException("Direct constructor not yet implemented", createMetadataFromContext(ctx));
    }

    private Node visitDirPIConstructor(TerminalNode piToken, ExceptionMetadata metadata) {
        String tokenText = piToken.getText();
        String inner = tokenText.substring(2, tokenText.length() - 2);
        int whitespaceIndex = indexOfWhitespace(inner);
        String target = whitespaceIndex == -1 ? inner : inner.substring(0, whitespaceIndex);
        Expression contentExpression = null;
        if (whitespaceIndex != -1) {
            int contentStart = whitespaceIndex;
            while (contentStart < inner.length() && Character.isWhitespace(inner.charAt(contentStart))) {
                contentStart++;
            }
            String content = inner.substring(contentStart);
            contentExpression = new StringLiteralExpression(content, metadata);
        }
        return new DirPIConstructorExpression(target, contentExpression, metadata);
    }

    private int indexOfWhitespace(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isWhitespace(value.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private Node visitDirElemConstructorOpenClose(JsoniqParser.DirectConstructorContext ctx) {
        JsoniqParser.DirElemConstructorOpenCloseContext openClose = ctx.open_close;
        // check that the start and end tags are the same
        if (openClose.close_tag_name != null
                && !openClose.close_tag_name.getText().equals(ctx.open_tag_name.getText())) {
            throw new DirectElementConstructorTagMismatchException(
                    "The name used in the end tag must exactly match the name used in the corresponding start tag.",
                    createMetadataFromContext(ctx));
        }

        this.translationContext.pushConstructorNamespaceFrame();
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes != null) {
                attributeResult = this.getAttributesExpressionsList(ctx.attributes);
            }

            List<Expression> content = DirectConstructorUtils.mergeElementContent(
                    this.jsoniqTokenStream,
                    openClose.endOpen,
                    openClose.dirElemContent(),
                    this.translationContext.moduleContext().isBoundarySpacePreserve(),
                    child -> (Expression) this.visitDirElemContent(child));

            return new DirElemConstructorExpression(
                    parseName(ctx.open_tag_name, NameRole.ELEMENT_CONSTRUCTOR),
                    content,
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    createMetadataFromContext(ctx));
        } finally {
            this.translationContext.popConstructorNamespaceFrame();
        }
    }

    private Node visitDirElemConstructorSingleTag(JsoniqParser.DirectConstructorContext ctx) {
        this.translationContext.pushConstructorNamespaceFrame();
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes != null) {
                attributeResult = this.getAttributesExpressionsList(ctx.attributes);
            }

            return new DirElemConstructorExpression(
                    parseName(ctx.open_tag_name, NameRole.ELEMENT_CONSTRUCTOR),
                    new ArrayList<>(),
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    createMetadataFromContext(ctx));
        } finally {
            this.translationContext.popConstructorNamespaceFrame();
        }
    }

    @Override
    public Node visitDirElemContent(JsoniqParser.DirElemContentContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.DirectConstructorContext directConstructorContext) {
            return this.visitDirectConstructor(directConstructorContext);
        } else if (child instanceof JsoniqParser.CommonContentContext commonContentContext) {
            return this.visitCommonContent(commonContentContext);
        } else {
            // Include lexer hidden-channel characters (e.g. spaces) in this fragment; ParseTree#getText() drops them.
            String text = this.jsoniqTokenStream.getText(ctx.getSourceInterval());
            if (ctx.CDATA() != null) {
                // filter out the <![CDATA[ and ]]>, and return the text
                return new TextNodeExpression(text.substring(9, text.length() - 3), createMetadataFromContext(ctx));
            }
            return new TextNodeExpression(text, createMetadataFromContext(ctx), isWhitespaceOnly(text));
        }
    }

    @Override
    public Node visitCommonContent(JsoniqParser.CommonContentContext ctx) {
        if (ctx.expr() != null) {
            return (Expression) this.visitExpr(ctx.expr());
        }
        String processedContent = DirectConstructorUtils.processLiteralContent(ctx.getText());
        return new TextNodeExpression(processedContent, createMetadataFromContext(ctx));
    }

    private boolean isWhitespaceOnly(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return !value.isEmpty();
    }

    @Override
    public Node visitComputedConstructor(JsoniqParser.ComputedConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.CompDocConstructorContext compDocConstructorContext) {
            return this.visitCompDocConstructor(compDocConstructorContext);
        } else if (child instanceof JsoniqParser.CompElemConstructorContext compElemConstructorContext) {
            return this.visitCompElemConstructor(compElemConstructorContext);
        } else if (child instanceof JsoniqParser.CompPIConstructorContext compPIConstructorContext) {
            return this.visitCompPIConstructor(compPIConstructorContext);
        } else if (child instanceof JsoniqParser.CompTextConstructorContext compTextConstructorContext) {
            return this.visitCompTextConstructor(compTextConstructorContext);
        } else if (child instanceof JsoniqParser.CompCommentConstructorContext compCommentConstructorContext) {
            return this.visitCompCommentConstructor(compCommentConstructorContext);
        } else if (child instanceof JsoniqParser.CompAttrConstructorContext compAttrConstructorContext) {
            return this.visitCompAttrConstructor(compAttrConstructorContext);
        } else if (child instanceof JsoniqParser.CompNamespaceConstructorContext compNamespaceConstructorContext) {
            return this.visitCompNamespaceConstructor(compNamespaceConstructorContext);
        }
        throw new UnsupportedFeatureException("Computed constructor", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompDocConstructor(JsoniqParser.CompDocConstructorContext ctx) {
        Expression contentExpression = (Expression) this.visitEnclosedExpression(ctx.enclosedExpression());
        return new DocumentNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompTextConstructor(JsoniqParser.CompTextConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new TextNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompCommentConstructor(JsoniqParser.CompCommentConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new CommentNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompPIConstructor(JsoniqParser.CompPIConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedPIConstructorExpression(
                    ctx.ncName().getText(), contentExpression, createMetadataFromContext(ctx));
        }
        if (ctx.expr() != null) {
            Expression nameExpression = (Expression) this.visitExpr(ctx.expr());
            return new ComputedPIConstructorExpression(
                    nameExpression, contentExpression, createMetadataFromContext(ctx));
        }
        throw new ParsingException(
                "Computed processing instruction constructor must have either a static NCName or a dynamic name expression",
                createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompAttrConstructor(JsoniqParser.CompAttrConstructorContext ctx) {
        Expression valueExpression = (Expression) visit(ctx.enclosedExpression());

        // Check if we have a static attribute name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.name != null) {
            // Static attribute name: attribute attributeName { value }
            Name attributeName = this.parseEqName(ctx.name, NameRole.NO_DEFAULT_NAMESPACE);
            return new ComputedAttributeConstructorExpression(
                    attributeName, valueExpression, createMetadataFromContext(ctx));
        } else if (ctx.name_expr != null) {
            // Dynamic attribute name: attribute { nameExpression } { value }
            Expression nameExpression = (Expression) this.visitExpr(ctx.name_expr);
            return new ComputedAttributeConstructorExpression(
                    nameExpression, valueExpression, createMetadataFromContext(ctx));
        } else {
            throw new ParsingException(
                    "Computed attribute constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx));
        }
    }

    @Override
    public Node visitCompElemConstructor(JsoniqParser.CompElemConstructorContext ctx) {
        Expression contentExpression = (Expression) this.visitEnclosedContentExpr(ctx.enclosedContentExpr());

        // Check if we have a static element name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.eqName() != null) {
            // Static element name: element elementName { content }
            Name elementName = parseEqName(ctx.eqName(), NameRole.ELEMENT_CONSTRUCTOR);
            return new ComputedElementConstructorExpression(
                    elementName, contentExpression, createMetadataFromContext(ctx));
        } else if (ctx.expr() != null) {
            // Dynamic element name: element { nameExpression } { content }
            Expression nameExpression = (Expression) this.visitExpr(ctx.expr());
            return new ComputedElementConstructorExpression(
                    nameExpression, contentExpression, createMetadataFromContext(ctx));
        } else {
            throw new ParsingException(
                    "Computed element constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx));
        }
    }

    @Override
    public Node visitCompNamespaceConstructor(JsoniqParser.CompNamespaceConstructorContext ctx) {
        Expression uriExpression =
                (Expression) this.visitEnclosedExpression(ctx.enclosedURIExpr().enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedNamespaceConstructorExpression(
                    ctx.ncName().getText(), uriExpression, createMetadataFromContext(ctx));
        }
        if (ctx.enclosedPrefixExpr() != null) {
            Expression prefixExpression = (Expression)
                    this.visitEnclosedExpression(ctx.enclosedPrefixExpr().enclosedExpression());
            return new ComputedNamespaceConstructorExpression(
                    prefixExpression, uriExpression, createMetadataFromContext(ctx));
        }
        throw new ParsingException(
                "Computed namespace constructor must have either a static prefix or a dynamic prefix expression",
                createMetadataFromContext(ctx));
    }

    @Override
    public Node visitEnclosedContentExpr(JsoniqParser.EnclosedContentExprContext ctx) {
        return this.visitEnclosedExpression(ctx.enclosedExpression());
    }

    @Override
    public Node visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.SquareArrayConstructorContext sqCtx) {
            List<JsoniqParser.ExprSingleContext> memberCtxs = sqCtx.exprSingle();
            if (memberCtxs == null || memberCtxs.isEmpty()) {
                return new ArrayConstructorExpression(new ArrayList<>(), true, createMetadataFromContext(sqCtx));
            }
            List<Expression> memberExpressions = new ArrayList<>();
            if (this.translationContext.moduleContext().getQueryLanguage().equals("jsoniq10")) {
                // In JSONiq 1.0, the square array constructor behaves like the curly array constructor.
                // Thus, we concatenate all expressions into a single comma expression.
                for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                    memberExpressions.add((Expression) this.visitExprSingle(memberCtx));
                }
                Expression commaExpression = new CommaExpression(memberExpressions, createMetadataFromContext(sqCtx));
                return new ArrayConstructorExpression(commaExpression, createMetadataFromContext(sqCtx));
            } else {
                log.debug("Not concatenating to comma.");
                // In JSONiq 4.0, the square array constructor behaves like in XQuery 4.0.
                for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                    memberExpressions.add((Expression) this.visitExprSingle(memberCtx));
                }
                return new ArrayConstructorExpression(memberExpressions, true, createMetadataFromContext(sqCtx));
            }
        }
        // else curlyArrayConstructor
        JsoniqParser.CurlyArrayConstructorContext childCtx = (JsoniqParser.CurlyArrayConstructorContext) child;
        if (childCtx.enclosedExpression() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(childCtx));
        }
        Expression content = (Expression) this.visitEnclosedExpression(childCtx.enclosedExpression());
        return new ArrayConstructorExpression(content, createMetadataFromContext(childCtx));
    }

    @Override
    public Node visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx) {
        return PrimaryTranslation.parenthesizedExpr(
                ParenthesizedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }

    @Override
    public Node visitVarRef(JsoniqParser.VarRefContext ctx) {
        return PrimaryTranslation.varRef(VarRefContext.from(ctx), this.translationContext, this::parseEqName);
    }

    private Name parseVariableReference(JsoniqParser.VarRefContext ctx) {
        return parseVariableName(ctx.eqName());
    }

    private Name parseVariableBinding(JsoniqParser.VarBindingContext ctx) {
        return parseVariableName(ctx.eqName());
    }

    private Name parseVariableName(JsoniqParser.EqNameContext ctx) {
        return parseEqName(ctx, NameRole.NO_DEFAULT_NAMESPACE);
    }

    @Override
    public Node visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        return PrimaryTranslation.contextItemExpr(ctx, this.translationContext);
    }

    public SequenceType processSequenceType(JsoniqParser.SequenceTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.createSequenceType("()");
        }
        ItemType itemType = processItemType(ctx.item);
        if (ctx.question.size() > 0) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrZero);
        }
        if (ctx.star.size() > 0) {
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }
        if (ctx.plus.size() > 0) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrMore);
        }
        return new SequenceType(itemType);
    }

    public SequenceType processSingleType(JsoniqParser.SingleTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.createSequenceType("()");
        }

        ItemType itemType = processItemType(ctx.item);
        if (ctx.question.size() > 0) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrZero);
        }
        return new SequenceType(itemType);
    }

    public ItemType processItemType(JsoniqParser.ItemTypeContext itemTypeContext) {
        if (itemTypeContext.parenthesizedItemTest() != null) {
            return processItemType(itemTypeContext.parenthesizedItemTest().itemType());
        }
        if (itemTypeContext.KW_ITEM() != null) {
            return BuiltinTypesCatalogue.item;
        }
        if (itemTypeContext.KW_NULL() != null) {
            return BuiltinTypesCatalogue.nullItem;
        }
        JsoniqParser.FunctionTestContext fnCtx = itemTypeContext.functionTest();
        if (fnCtx != null) {
            processAnnotations(fnCtx.annotation());
            // we have a function item type
            JsoniqParser.TypedFunctionTestContext typedFnCtx = fnCtx.typedFunctionTest();
            if (typedFnCtx != null) {
                SequenceType rt = processSequenceType(typedFnCtx.rt);
                List<SequenceType> st =
                        typedFnCtx.st.stream().map(this::processSequenceType).collect(Collectors.toList());
                FunctionSignature signature = new FunctionSignature(st, rt);
                // TODO: move item type creation to ItemFactory
                return ItemTypeFactory.createFunctionItemType(signature);

            } else {
                return BuiltinTypesCatalogue.anyFunctionItem;
            }
        }
        if (itemTypeContext.mapTest() != null) {
            JsoniqParser.MapTestContext mapTestContext = itemTypeContext.mapTest();
            if (mapTestContext.anyMapTest() != null) {
                return BuiltinTypesCatalogue.mapItem;
            }
            JsoniqParser.TypedMapTestContext typedMapTestContext = mapTestContext.typedMapTest();
            if (typedMapTestContext != null) {
                Name keyName = parseEqName(typedMapTestContext.eqName(), NameRole.TYPE);
                keyName = ItemTypeReference.renameAtomic(this.translationContext.moduleContext(), keyName);
                ItemType keyType;
                if (!BuiltinTypesCatalogue.typeExists(keyName)) {
                    keyType = new ItemTypeReference(keyName);
                } else {
                    keyType = BuiltinTypesCatalogue.getItemTypeByName(keyName);
                }
                SequenceType valueSequenceType = processSequenceType(typedMapTestContext.sequenceType());
                return ItemTypeFactory.mapOf(keyType, valueSequenceType);
            }
        }
        if (itemTypeContext.arrayTest() != null) {
            JsoniqParser.ArrayTestContext arrayTestContext = itemTypeContext.arrayTest();
            if (arrayTestContext.anyArrayTest() != null) {
                // XQuery 3.1 array(*) is the XDM array type (members are sequences), not js:array().
                return BuiltinTypesCatalogue.xqueryArrayItem;
            }
            JsoniqParser.TypedArrayTestContext typedArrayTestContext = arrayTestContext.typedArrayTest();
            if (typedArrayTestContext != null) {
                SequenceType contentSequenceType = processSequenceType(typedArrayTestContext.sequenceType());
                return ItemTypeFactory.xqueryArrayOf(contentSequenceType);
            }
        }
        if (itemTypeContext.eqName() != null) {
            Name name = parseEqName(itemTypeContext.eqName(), NameRole.TYPE);
            name = ItemTypeReference.renameAtomic(this.translationContext.moduleContext(), name);
            if (!BuiltinTypesCatalogue.typeExists(name)) {
                return new ItemTypeReference(name);
            }
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        if (itemTypeContext.kindTest() != null) {
            return processKindTestAsItemType(itemTypeContext.kindTest());
        }
        throw new UnsupportedFeatureException("Unsupported itemtype encountered", ExceptionMetadata.EMPTY_METADATA);
    }

    private ItemType processKindTestAsItemType(JsoniqParser.KindTestContext kindTestContext) {
        if (kindTestContext.schemaElementTest() != null) {
            return getSchemaElementTestAsItemType(kindTestContext.schemaElementTest());
        }
        if (kindTestContext.schemaAttributeTest() != null) {
            return getSchemaAttributeTestAsItemType(kindTestContext.schemaAttributeTest());
        }
        if (kindTestContext.anyKindTest() != null) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (kindTestContext.documentTest() != null) {
            JsoniqParser.DocumentTestContext documentTestContext = kindTestContext.documentTest();
            if (documentTestContext.schemaElementTest() != null) {
                return ItemTypeFactory.documentNodeItemType(
                        getSchemaElementTestAsItemType(documentTestContext.schemaElementTest()));
            }
            if (documentTestContext.elementTest() != null) {
                ElementNodeItemType elementTestType = getElementTestAsItemType(documentTestContext.elementTest());
                return ItemTypeFactory.documentNodeItemType(elementTestType);
            }
            return BuiltinTypesCatalogue.documentNode;
        }
        if (kindTestContext.elementTest() != null) {
            return getElementTestAsItemType(kindTestContext.elementTest());
        }
        if (kindTestContext.attributeTest() != null) {
            JsoniqParser.AttributeTestContext attributeTestContext = kindTestContext.attributeTest();
            if (attributeTestContext.typeName() != null) {
                throw new UnsupportedFeatureException(
                        "Typed attribute item tests are not supported yet",
                        createMetadataFromContext(attributeTestContext));
            }
            if (attributeTestContext.attributeNameOrWildcard() == null) {
                return BuiltinTypesCatalogue.attributeNode;
            }
            if (attributeTestContext.attributeNameOrWildcard().attributeName() == null) {
                return BuiltinTypesCatalogue.attributeNode;
            }
            Name attributeName = parseEqName(
                    attributeTestContext
                            .attributeNameOrWildcard()
                            .attributeName()
                            .eqName(),
                    NameRole.NO_DEFAULT_NAMESPACE);
            return ItemTypeFactory.attributeNodeItemType(attributeName);
        }
        if (kindTestContext.commentTest() != null) {
            return BuiltinTypesCatalogue.commentNode;
        }
        if (kindTestContext.textTest() != null) {
            return BuiltinTypesCatalogue.textNode;
        }
        if (kindTestContext.namespaceNodeTest() != null) {
            return BuiltinTypesCatalogue.namespaceNode;
        }
        if (kindTestContext.piTest() != null) {
            JsoniqParser.PiTestContext piTestContext = kindTestContext.piTest();
            if (piTestContext.ncName() != null) {
                return ItemTypeFactory.processingInstructionNodeItemType(
                        piTestContext.ncName().getText());
            }
            if (piTestContext.stringLiteral() != null) {
                String targetName = processStringLiteral(piTestContext.stringLiteral());
                return ItemTypeFactory.processingInstructionNodeItemType(targetName);
            }
            return BuiltinTypesCatalogue.processingInstructionNode;
        }
        throw new UnsupportedFeatureException(
                "Unsupported kind test in item type: " + kindTestContext.getText(),
                createMetadataFromContext(kindTestContext));
    }

    private ElementNodeItemType getSchemaElementTestAsItemType(JsoniqParser.SchemaElementTestContext ctx) {
        Name name = parseEqName(ctx.elementDeclaration().elementName().eqName(), NameRole.ELEMENT_CONSTRUCTOR);
        return this.translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaElementTest(name, createMetadataFromContext(ctx));
    }

    private ItemType getSchemaAttributeTestAsItemType(JsoniqParser.SchemaAttributeTestContext ctx) {
        Name name = parseEqName(ctx.attributeDeclaration().attributeName().eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return this.translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaAttributeTest(name, createMetadataFromContext(ctx));
    }

    private ElementNodeItemType getElementTestAsItemType(JsoniqParser.ElementTestContext elementTestContext) {
        if (elementTestContext.optional != null || elementTestContext.typeName() != null) {
            throw new UnsupportedFeatureException(
                    "Typed or nillable element item tests are not supported yet",
                    createMetadataFromContext(elementTestContext));
        }
        if (elementTestContext.elementNameOrWildcard() == null) {
            return (ElementNodeItemType) BuiltinTypesCatalogue.elementNode;
        }
        if (elementTestContext.elementNameOrWildcard().elementName() == null) {
            return (ElementNodeItemType) BuiltinTypesCatalogue.elementNode;
        }
        Name elementName = parseEqName(
                elementTestContext.elementNameOrWildcard().elementName().eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(elementName);
    }

    @Override
    public Node visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        return PrimaryTranslation.functionCall(
                FunctionCallContext.from(ctx), this.translationContext, this::parseFunctionName, arg ->
                        (Expression) this.visitArgument(arg));
    }

    private List<Expression> getArgumentsFromArgumentListContext(JsoniqParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (JsoniqParser.ArgumentContext arg : ctx.args) {
                Expression currentArg = (Expression) this.visitArgument(arg);
                arguments.add(currentArg);
            }
        }
        return arguments;
    }

    @Override
    public Node visitArgument(JsoniqParser.ArgumentContext ctx) {
        if (ctx.exprSingle() != null) {
            return this.visitExprSingle(ctx.exprSingle());
        }
        return null;
    }

    @Override
    public Node visitFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.NamedFunctionRefContext namedFunctionRefContext) {
            return this.visitNamedFunctionRef(namedFunctionRefContext);
        }
        if (child instanceof JsoniqParser.InlineFunctionExprContext inlineFunctionExprContext) {
            return this.visitInlineFunctionExpr(inlineFunctionExprContext);
        }
        throw new UnsupportedFeatureException(
                "Function item expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx) {
        return PrimaryTranslation.namedFunctionRef(
                NamedFunctionRefContext.from(ctx), this.translationContext, this::parseFunctionName);
    }

    @Override
    public Node visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.createSequenceType("item*");
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseVariableBinding(param.name);
                paramType = SequenceType.createSequenceType("item*");
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            Name.createVariableInDefaultFunctionNamespace("inline-function`"),
                            paramName,
                            createMetadataFromContext(param));
                }
                if (param.sequenceType() != null) {
                    paramType = this.processSequenceType(param.sequenceType());
                } else {
                    paramType = SequenceType.createSequenceType("item*");
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        }

        StatementsAndOptionalExpr funcBody =
                (StatementsAndOptionalExpr) this.visitStatementsAndOptionalExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                annotations, null, fnParams, fnReturnType, funcBody, createMetadataFromContext(ctx));
    }
    // endregion

    // region control
    @Override
    public Node visitIfExpr(JsoniqParser.IfExprContext ctx) {
        return ControlTranslation.ifExpr(
                IfExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }

    @Override
    public Node visitSwitchExpr(JsoniqParser.SwitchExprContext ctx) {
        return ControlTranslation.switchExpr(
                SwitchExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }
    // endregion

    // region quantified
    @Override
    public Node visitTypeswitchExpr(JsoniqParser.TypeswitchExprContext ctx) {
        return ControlTranslation.typeswitchExpr(
                TypeswitchExprContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public Node visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx) {
        return QuantifiedTranslation.quantifiedExpr(
                QuantifiedExprContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public Node visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx) {
        return ControlTranslation.tryCatchExpr(
                TryCatchExprContext.from(ctx), this.translationContext, this::visitExpr, this::parseEqName);
    }

    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext context) {
        return this.translationContext.metadata(context);
    }

    private ExceptionMetadata createMetadataFromTree(ParseTree tree) {
        return this.translationContext.metadata(tree);
    }

    private ExceptionMetadata createMetadataFromRange(Token start, Token end) {
        return this.translationContext.metadata(start, end);
    }

    private ExceptionMetadata createMetadataFromTrees(ParseTree startTree, ParseTree endTree) {
        return this.translationContext.metadata(startTree, endTree);
    }

    // region scripting
    @Override
    public Node visitStatements(JsoniqParser.StatementsContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatementsAndExpr(JsoniqParser.StatementsAndExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        Expression expression = (Expression) this.visitExpr(ctx.expr());
        return new StatementsAndExpr(statements, expression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatementsAndOptionalExpr(JsoniqParser.StatementsAndOptionalExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        if (ctx.expr() != null) {
            Expression expression = (Expression) this.visitExpr(ctx.expr());
            return new StatementsAndOptionalExpr(statements, expression, createMetadataFromContext(ctx));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatement(JsoniqParser.StatementContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof JsoniqParser.ApplyStatementContext applyStatementContext) {
            return this.visitApplyStatement(applyStatementContext);
        }
        if (content instanceof JsoniqParser.AssignStatementContext assignStatementContext) {
            return this.visitAssignStatement(assignStatementContext);
        }
        if (content instanceof JsoniqParser.BlockStatementContext blockStatementContext) {
            return this.visitBlockStatement(blockStatementContext);
        }
        if (content instanceof JsoniqParser.BreakStatementContext breakStatementContext) {
            return this.visitBreakStatement(breakStatementContext);
        }
        if (content instanceof JsoniqParser.ContinueStatementContext continueStatementContext) {
            return this.visitContinueStatement(continueStatementContext);
        }
        if (content instanceof JsoniqParser.ExitStatementContext exitStatementContext) {
            return this.visitExitStatement(exitStatementContext);
        }
        if (content instanceof JsoniqParser.FlworStatementContext flworStatementContext) {
            return this.visitFlworStatement(flworStatementContext);
        }
        if (content instanceof JsoniqParser.IfStatementContext ifStatementContext) {
            return this.visitIfStatement(ifStatementContext);
        }
        if (content instanceof JsoniqParser.SwitchStatementContext switchStatementContext) {
            return this.visitSwitchStatement(switchStatementContext);
        }
        if (content instanceof JsoniqParser.TryCatchStatementContext tryCatchStatementContext) {
            return this.visitTryCatchStatement(tryCatchStatementContext);
        }
        if (content instanceof JsoniqParser.TypeSwitchStatementContext typeSwitchStatementContext) {
            return this.visitTypeSwitchStatement(typeSwitchStatementContext);
        }
        if (content instanceof JsoniqParser.VarDeclStatementContext varDeclStatementContext) {
            return this.visitVarDeclStatement(varDeclStatementContext);
        }
        if (content instanceof JsoniqParser.WhileStatementContext whileStatementContext) {
            return this.visitWhileStatement(whileStatementContext);
        }
        throw new OurBadException("Unrecognized Statement.");
    }

    // mutation
    @Override
    public Node visitApplyStatement(JsoniqParser.ApplyStatementContext ctx) {
        Expression exprSimple = (Expression) this.visitExprSimple(ctx.exprSimple());
        return new ApplyStatement(exprSimple, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitAssignStatement(JsoniqParser.AssignStatementContext ctx) {
        Name paramName = parseVariableReference(ctx.var_ref);
        Expression exprSingle = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new AssignStatement(exprSingle, paramName, createMetadataFromContext(ctx));
    }
    // end mutation

    // block
    @Override
    public Node visitBlockStatement(JsoniqParser.BlockStatementContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext statement : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(statement));
        }
        return new BlockStatement(statements, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitBlockExpr(JsoniqParser.BlockExprContext ctx) {
        StatementsAndExpr statementsAndExpr = (StatementsAndExpr) this.visitStatementsAndExpr(ctx.statementsAndExpr());
        return new BlockExpression(statementsAndExpr, createMetadataFromContext(ctx));
    }
    // end block

    // loops
    @Override
    public Node visitBreakStatement(JsoniqParser.BreakStatementContext ctx) {
        return new BreakStatement(createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContinueStatement(JsoniqParser.ContinueStatementContext ctx) {
        return new ContinueStatement(createMetadataFromContext(ctx));
    }

    @Override
    public Node visitExitStatement(JsoniqParser.ExitStatementContext ctx) {
        Expression exprSingle = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new ExitStatement(exprSingle, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitFlworStatement(JsoniqParser.FlworStatementContext ctx) {
        Clause clause;
        // Check for start clause. Only for or let allowed.
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }
        Clause lastFlowrClause = clause.getLastClause();
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext forClauseContext) {
                clause = (Clause) this.visitForClause(forClauseContext);
            } else if (child instanceof JsoniqParser.LetClauseContext letClauseContext) {
                clause = (Clause) this.visitLetClause(letClauseContext);
            } else if (child instanceof JsoniqParser.WhereClauseContext whereClauseContext) {
                clause = (Clause) this.visitWhereClause(whereClauseContext);
            } else if (child instanceof JsoniqParser.GroupByClauseContext groupByClauseContext) {
                clause = (Clause) this.visitGroupByClause(groupByClauseContext);
            } else if (child instanceof JsoniqParser.OrderByClauseContext orderByClauseContext) {
                clause = (Clause) this.visitOrderByClause(orderByClauseContext);
            } else if (child instanceof JsoniqParser.CountClauseContext countClauseContext) {
                clause = (Clause) this.visitCountClause(countClauseContext);
            } else {
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet", createMetadataFromContext(ctx));
            }
            lastFlowrClause.chainWith(clause.getFirstClause());
            lastFlowrClause = clause.getLastClause();
        }
        Statement returnStatement = (Statement) this.visitStatement(ctx.returnStmt);
        ReturnStatementClause returnStatementClause =
                new ReturnStatementClause(returnStatement, returnStatement.getMetadata());
        lastFlowrClause.chainWith(returnStatementClause);
        returnStatementClause = returnStatementClause.detachInitialLetClausesForStatements();
        return new FlowrStatement(returnStatementClause, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitWhileStatement(JsoniqParser.WhileStatementContext ctx) {
        Expression testCondition = (Expression) this.visitExpr(ctx.test_expr);
        Statement statement = (Statement) this.visitStatement(ctx.stmt);
        return new WhileStatement(testCondition, statement, createMetadataFromContext(ctx));
    }

    // end loops

    // control

    @Override
    public Node visitIfStatement(JsoniqParser.IfStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.test_expr);
        // Verify and set branch.
        ParseTree branchContent = ctx.children.get(5);
        checkForUnsupportedStatement(branchContent);
        Statement branch = (Statement) this.visitStatement(ctx.branch);
        // Verify and set else branch.
        ParseTree elseBranchContent = ctx.children.get(7);
        checkForUnsupportedStatement(elseBranchContent);
        Statement elseBranch = (Statement) this.visitStatement(ctx.else_branch);
        return new ConditionalStatement(condition, branch, elseBranch, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitSwitchStatement(JsoniqParser.SwitchStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.condExpr);
        List<SwitchCaseStatement> cases = new ArrayList<>();
        for (JsoniqParser.SwitchCaseStatementContext stmt : ctx.cases) {
            List<Expression> conditionExpressions = new ArrayList<>();
            stmt.cond.forEach(exprSingle -> {
                conditionExpressions.add((Expression) this.visitExprSingle(exprSingle));
            });
            // TODO: Test this behaviour with return!
            // Verify return statement
            ParseTree caseTree = stmt.children.get(stmt.children.size() - 1);
            checkForUnsupportedStatement(caseTree);
            SwitchCaseStatement swCase =
                    new SwitchCaseStatement(conditionExpressions, (Statement) this.visitStatement(stmt.ret));
            cases.add(swCase);
        }
        // Verify default statement
        ParseTree defaultTree = ctx.children.get(ctx.children.size() - 1);
        checkForUnsupportedStatement(defaultTree);
        Statement defaultCase = (Statement) this.visitStatement(ctx.def);
        return new SwitchStatement(condition, cases, defaultCase, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx) {
        BlockStatement tryBlock = (BlockStatement) this.visitBlockStatement(ctx.try_block);
        Map<CatchPattern, BlockStatement> catchBlockStatements = new LinkedHashMap<>();
        for (JsoniqParser.CatchCaseStatementContext catchCtx : ctx.catches) {
            BlockStatement catchBlockStatement = (BlockStatement) this.visitBlockStatement(catchCtx.catch_block);
            for (var catchTarget : catchCtx.nameTest()) {
                CatchPattern pattern = ControlTranslation.catchPattern(
                        NameTestContext.from(catchTarget), this.translationContext, this::parseEqName);
                if (!catchBlockStatements.containsKey(pattern)) {
                    catchBlockStatements.put(pattern, catchBlockStatement);
                }
            }
        }
        return new TryCatchStatement(tryBlock, catchBlockStatements, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeSwitchStatementCase> cases = new ArrayList<>();
        for (JsoniqParser.CaseStatementContext stmt : ctx.cases) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (stmt.var_ref != null) {
                variableName = parseVariableBinding(stmt.var_ref);
            }
            if (stmt.union != null && !stmt.union.isEmpty()) {
                stmt.union.forEach(sequenceTypeContext -> {
                    union.add(this.processSequenceType(sequenceTypeContext));
                });
            }
            Statement returnStatement = (Statement) this.visitStatement(stmt.ret);
            cases.add(new TypeSwitchStatementCase(variableName, union, returnStatement));
        }
        Name defaultVariableName = null;
        if (ctx.var_ref != null) {
            defaultVariableName = parseVariableBinding(ctx.var_ref);
        }
        Statement defaultStatement = (Statement) this.visitStatement(ctx.def);
        return new TypeSwitchStatement(
                condition,
                cases,
                new TypeSwitchStatementCase(defaultVariableName, defaultStatement),
                createMetadataFromContext(ctx));
    }

    public void checkForUnsupportedStatement(ParseTree content) {
        if (content instanceof JsoniqParser.BreakStatementContext) {
            throw new OurBadException("Break statement is not supported in an if branch!");
        } else if (content instanceof JsoniqParser.ContinueStatementContext) {
            throw new OurBadException("Continue statement is not supported in an if branch!");
        } else if (content instanceof JsoniqParser.ExitStatementContext) {
            throw new OurBadException("Exit statement is not supported in an if branch!");
        }
    }

    // end control

    // declaration

    @Override
    public Node visitVarDeclStatement(JsoniqParser.VarDeclStatementContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        List<VariableDeclStatement> variables = new ArrayList<>();
        for (JsoniqParser.VarDeclForStatementContext varDecl : ctx.varDeclForStatement()) {
            SequenceType seq = null;
            Name var = parseVariableBinding(varDecl.var_ref);
            Expression exprSingle = null;

            if (varDecl.sequenceType() != null) {
                seq = this.processSequenceType(varDecl.sequenceType());
            }
            if (varDecl.exprSingle() != null) {
                exprSingle = (Expression) this.visitExprSingle(varDecl.exprSingle());
                if (seq != null) {
                    exprSingle = new TreatExpression(
                            exprSingle, seq, ErrorCode.UnexpectedTypeErrorCode, exprSingle.getMetadata());
                }
            }
            variables.add(
                    new VariableDeclStatement(annotations, var, seq, exprSingle, createMetadataFromContext(varDecl)));
        }
        if (variables.size() == 1) {
            return variables.get(0);
        }
        return new CommaVariableDeclStatement(variables, createMetadataFromContext(ctx));
    }

    // end declaration

    // start xml

    @Override
    public Node visitPathExpr(JsoniqParser.PathExprContext ctx) {
        if (ctx.singleslash != null) {
            return visitSingleSlash(ctx, ctx.singleslash);
        } else if (ctx.doubleslash != null) {
            return visitDoubleSlash(ctx, ctx.doubleslash);
        } else if (ctx.relative != null) {
            return visitRelativeWithoutSlash(ctx.relative);
        }
        return visitSingleSlashNoStepExpr(ctx);
    }

    private Node visitSingleSlashNoStepExpr(JsoniqParser.PathExprContext ctx) {
        // Case: No StepExpr, only dash
        return new PathRootExpression(createMetadataFromContext(ctx));
    }

    private Node visitRelativeWithoutSlash(JsoniqParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1
                && relativeContext.stepExpr(0).postfixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostfixExpr(relativeContext.stepExpr(0).postfixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Node visitDoubleSlash(
            JsoniqParser.PathExprContext pathContext, JsoniqParser.RelativePathExprContext doubleSlashContext) {
        Token leadingDoubleSlash = pathContext.getStart();
        PathRootExpression functionCallExpression =
                new PathRootExpression(createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash));
        StepExpr stepExpr = new ForwardStepExpr(
                ForwardAxis.DESCENDANT_OR_SELF,
                new AnyKindTest(),
                createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash));
        Expression starter = new SlashExpr(
                functionCallExpression, stepExpr, createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash));
        return getSlashes(doubleSlashContext, starter, leadingDoubleSlash);
    }

    private Node visitSingleSlash(
            JsoniqParser.PathExprContext pathContext, JsoniqParser.RelativePathExprContext singleSlashContext) {
        Token leadingSlash = pathContext.getStart();
        PathRootExpression functionCallExpression =
                new PathRootExpression(createMetadataFromRange(leadingSlash, leadingSlash));
        return getSlashes(singleSlashContext, functionCallExpression, leadingSlash);
    }

    /**
     * This method takes a leftMost expression and a path and returns a nested tree of slash expressions which
     * correspond to the steps in the path applied to the leftMost expression
     */
    private Expression getSlashes(JsoniqParser.RelativePathExprContext relativePathExprContext, Expression leftMost) {
        return getSlashes(relativePathExprContext, leftMost, relativePathExprContext.getStart());
    }

    private Expression getSlashes(
            JsoniqParser.RelativePathExprContext relativePathExprContext, Expression leftMost, Token expressionStart) {
        Expression currentTop = leftMost; // can be null
        Expression currentStepExpr;
        for (int i = 0; i < relativePathExprContext.stepExpr().size(); ++i) {
            currentStepExpr = (Expression) this.visitStepExpr(relativePathExprContext.stepExpr(i));
            if (i > 0 && relativePathExprContext.sep.get(i - 1).getText().equals("//")) {
                // Unroll '//' to forward axis
                StepExpr intermediaryStepExpr = new ForwardStepExpr(
                        ForwardAxis.DESCENDANT_OR_SELF,
                        new AnyKindTest(),
                        createMetadataFromRange(
                                relativePathExprContext.sep.get(i - 1), relativePathExprContext.sep.get(i - 1)));
                if (currentTop == null) {
                    currentTop = intermediaryStepExpr;
                } else {
                    currentTop = new SlashExpr(
                            currentTop,
                            intermediaryStepExpr,
                            createMetadataFromRange(expressionStart, relativePathExprContext.sep.get(i - 1)));
                }
            }
            if (currentTop == null) {
                currentTop = currentStepExpr;
            } else {
                currentTop = new SlashExpr(
                        currentTop,
                        currentStepExpr,
                        createMetadataFromRange(
                                expressionStart,
                                relativePathExprContext.stepExpr(i).getStop()));
            }
        }
        return currentTop;
    }

    @Override
    public Node visitStepExpr(JsoniqParser.StepExprContext ctx) {
        if (ctx.postfixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (JsoniqParser.PredicateContext predicateContext :
                    ctx.axisStep().predicateList().predicate()) {
                Expression predicate = (Expression) this.visitPredicate(predicateContext);
                stepExpr = new FilterExpression(
                        stepExpr, predicate, createMetadataFromRange(ctx.getStart(), predicateContext.getStop()));
            }
            return stepExpr;
        }
        return this.visitPostfixExpr(ctx.postfixExpr());
    }

    private StepExpr getStep(JsoniqParser.AxisStepContext ctx) {
        if (ctx.forwardStep() == null) {
            return getReverseStep(ctx.reverseStep());
        }
        return getForwardStep(ctx.forwardStep());
    }

    private StepExpr getForwardStep(JsoniqParser.ForwardStepContext ctx) {
        ForwardAxis forwardAxis;
        NodeTest nodeTest;
        if (ctx.nodeTest() == null) {
            // Abbreviated step: unprefixed names use default element namespace on child axis, not on @attr.
            boolean unprefixedUsesDefaultElementNs = ctx.abbrevForwardStep().AT() == null;
            nodeTest = getNodeTest(ctx.abbrevForwardStep().nodeTest(), unprefixedUsesDefaultElementNs);
            if (ctx.abbrevForwardStep().AT() != null) {
                // @ equivalent with 'attribute::'
                forwardAxis = ForwardAxis.ATTRIBUTE;
            } else if (nodeTest instanceof AttributeTest
                    || (nodeTest instanceof SchemaNodeTest schemaTest
                            && schemaTest.itemType() instanceof AttributeNodeItemType)) {
                forwardAxis = ForwardAxis.ATTRIBUTE;
            } else {
                forwardAxis = ForwardAxis.CHILD;
            }
            return new ForwardStepExpr(forwardAxis, nodeTest, createMetadataFromContext(ctx));
        }
        forwardAxis = ForwardAxis.fromString(ctx.forwardAxis().getText());
        boolean unprefixedUsesDefaultElementNs = forwardAxis != ForwardAxis.ATTRIBUTE;
        nodeTest = getNodeTest(ctx.nodeTest(), unprefixedUsesDefaultElementNs);
        return new ForwardStepExpr(forwardAxis, nodeTest, createMetadataFromContext(ctx));
    }

    private StepExpr getReverseStep(JsoniqParser.ReverseStepContext ctx) {
        if (ctx.nodeTest() == null) {
            // .. equivalent with 'parent::node()'
            ReverseAxis reverseAxis = ReverseAxis.PARENT;
            NodeTest nodeTest = new AnyKindTest();
            return new ReverseStepExpr(reverseAxis, nodeTest, createMetadataFromContext(ctx));
        }
        ReverseAxis reverseAxis = ReverseAxis.fromString(ctx.reverseAxis().getText());
        // Reverse axes only match element (and similar) nodes; unprefixed QNames use default element namespace.
        NodeTest nodeTest = getNodeTest(ctx.nodeTest(), true);
        return new ReverseStepExpr(reverseAxis, nodeTest, createMetadataFromContext(ctx));
    }

    /**
     * @param unprefixedUsesDefaultElementNamespace when true, unprefixed QNames in a {@link NameTest} use the
     *        in-scope default element namespace (child/descendant axes, etc.); when false (e.g. {@code attribute::}),
     *        unprefixed names have no namespace.
     */
    private NodeTest getNodeTest(
            JsoniqParser.NodeTestContext nodeTestContext, boolean unprefixedUsesDefaultElementNamespace) {
        if (nodeTestContext.nameTest() == null) {
            // kind test
            return getKindTest(nodeTestContext.kindTest().children.get(0));
        }
        if (nodeTestContext.nameTest().wildcard() == null) {
            NameRole role = unprefixedUsesDefaultElementNamespace
                    ? NameRole.ELEMENT_CONSTRUCTOR
                    : NameRole.NO_DEFAULT_NAMESPACE;
            Name name = parseEqName(nodeTestContext.nameTest().eqName(), role);
            return new NameTest(name);
        } else {
            String wildcard = nodeTestContext.nameTest().wildcard().getText();
            return new NameTest(wildcard);
        }
    }

    // XQuery 3.1 Section 2.5.5 - SequenceType Matching
    // KindTest ::= DocumentTest | ElementTest | AttributeTest | SchemaElementTest
    // | SchemaAttributeTest | PITest | CommentTest | TextTest
    // | NamespaceNodeTest | AnyKindTest
    private NodeTest getKindTest(ParseTree kindTest) {
        if (kindTest instanceof JsoniqParser.DocumentTestContext docContext) {
            // XQuery 3.1 Section 2.5.5.3 - Element Test (used within DocumentTest)
            // DocumentTest ::= "document-node" "(" (ElementTest | SchemaElementTest)? ")"
            // document-node() matches any document node.
            // document-node(element(...)) matches a document node containing an element matching the ElementTest.
            if (docContext.schemaElementTest() != null) {
                return new SchemaNodeTest(ItemTypeFactory.documentNodeItemType(
                        getSchemaElementTestAsItemType(docContext.schemaElementTest())));
            }
            if (docContext.elementTest() == null) {
                return new DocumentTest(null);
            }
            return new DocumentTest(getKindTest(docContext.elementTest()));
        } else if (kindTest instanceof JsoniqParser.ElementTestContext elementContext) {
            // XQuery 3.1 Section 2.5.5.3 - Element Test
            // ElementTest ::= "element" "(" (ElementNameOrWildcard ("," TypeName "?"?)?)? ")"
            // element() and element(*) match any single element node.
            // element(N) matches any element node whose name is N.
            // element(N, T) matches an element node whose name is N and whose type annotation is T.
            // element(*, T) matches any element node whose type annotation is T.
            // element(N, T?) also matches nillable elements (validation-related, unsupported).
            // Reject the nillable marker "?" (validation-related feature)
            if (elementContext.optional != null) {
                throw new UnsupportedFeatureException(
                        "Nillable element tests (element(name, type?)) are not supported (validation feature)",
                        createMetadataFromContext((ParserRuleContext) kindTest));
            }
            Name elementName;
            if (elementContext.elementNameOrWildcard() != null) {
                boolean hasWildcard = elementContext.elementNameOrWildcard().elementName() == null;
                if (!hasWildcard) {
                    elementName = parseEqName(
                            elementContext.elementNameOrWildcard().elementName().eqName(),
                            NameRole.ELEMENT_CONSTRUCTOR);
                    if (elementContext.typeName() == null) {
                        return new ElementTest(elementName, null);
                    }
                    Name typeName = parseEqName(elementContext.typeName().eqName(), NameRole.TYPE);
                    return new ElementTest(elementName, typeName);
                }
                // Wildcard case: element(*) or element(*, type)
                if (elementContext.typeName() != null) {
                    Name typeName = parseEqName(elementContext.typeName().eqName(), NameRole.TYPE);
                    return new ElementTest(typeName);
                }
                return new ElementTest(true);
            }
            return new ElementTest();
        } else if (kindTest instanceof JsoniqParser.AttributeTestContext attributeTestContext) {
            // XQuery 3.1 Section 2.5.5.5 - Attribute Test
            // AttributeTest ::= "attribute" "(" (AttribNameOrWildcard ("," TypeName)?)? ")"
            // attribute() and attribute(*) match any single attribute node.
            // attribute(N) matches any attribute node whose name is N.
            // attribute(N, T) matches an attribute node whose name is N and whose type annotation is T.
            // attribute(*, T) matches any attribute node whose type annotation is T.
            Name attributeName;
            if (attributeTestContext.attributeNameOrWildcard() != null) {
                boolean hasWildcard =
                        attributeTestContext.attributeNameOrWildcard().attributeName() == null;
                if (!hasWildcard) {
                    attributeName = parseEqName(
                            attributeTestContext
                                    .attributeNameOrWildcard()
                                    .attributeName()
                                    .eqName(),
                            NameRole.NO_DEFAULT_NAMESPACE);
                    if (attributeTestContext.typeName() != null) {
                        Name typeName =
                                parseEqName(attributeTestContext.typeName().eqName(), NameRole.TYPE);
                        return new AttributeTest(attributeName, typeName);
                    } else {
                        return new AttributeTest(attributeName, null);
                    }
                } else {
                    // Wildcard case: attribute(*) or attribute(*, type)
                    if (attributeTestContext.typeName() != null) {
                        Name typeName =
                                parseEqName(attributeTestContext.typeName().eqName(), NameRole.TYPE);
                        return new AttributeTest(typeName);
                    }
                    return new AttributeTest(true);
                }
            }
            return new AttributeTest();
        } else if (kindTest instanceof JsoniqParser.TextTestContext) {
            // XQuery 3.1 Section 2.5.5
            // TextTest ::= "text" "(" ")"
            // A TextTest matches any text node.
            return new TextTest();
        } else if (kindTest instanceof JsoniqParser.CommentTestContext) {
            // XQuery 3.1 Section 2.5.5
            // CommentTest ::= "comment" "(" ")"
            // A CommentTest matches any comment node.
            return new CommentTest();
        } else if (kindTest instanceof JsoniqParser.PiTestContext piContext) {
            // XQuery 3.1 Section 2.5.5
            // PITest ::= "processing-instruction" "(" (NCName | StringLiteral)? ")"
            // processing-instruction() matches any processing-instruction node.
            // processing-instruction(N) matches any processing-instruction node whose target
            // name equals fn:normalize-space(N).
            if (piContext.ncName() != null) {
                return new PITest(piContext.ncName().getText());
            }
            if (piContext.stringLiteral() != null) {
                String targetName = processStringLiteral(piContext.stringLiteral());
                return new PITest(targetName);
            }
            return new PITest();
        } else if (kindTest instanceof JsoniqParser.NamespaceNodeTestContext) {
            // XQuery 3.1 Section 2.5.5
            // NamespaceNodeTest ::= "namespace-node" "(" ")"
            // A NamespaceNodeTest matches any namespace node.
            return new NamespaceNodeTest();
        } else if (kindTest instanceof JsoniqParser.AnyKindTestContext) {
            // XQuery 3.1 Section 2.5.5
            // AnyKindTest ::= "node" "(" ")"
            // node() matches any node.
            return new AnyKindTest();
        } else if (kindTest instanceof JsoniqParser.SchemaElementTestContext ctx) {
            return new SchemaNodeTest(getSchemaElementTestAsItemType(ctx));
        } else if (kindTest instanceof JsoniqParser.SchemaAttributeTestContext ctx) {
            return new SchemaNodeTest(getSchemaAttributeTestAsItemType(ctx));
        } else {
            throw new UnsupportedFeatureException(
                    "Unsupported kind test: " + kindTest.getText(),
                    createMetadataFromContext((ParserRuleContext) kindTest));
        }
    }

    // end region

    public void bindNamespace(String prefix, String namespace, ExceptionMetadata metadata) {
        PrologTranslation.bindNamespace(this.translationContext, prefix, namespace, metadata);
    }

    private String processURILiteral(UriLiteralContext ctx) {
        // URI literals use the ordinary JSONiq string rules. XML entity
        // expansion is deliberately confined to direct attribute content.
        return processStringLiteral(ctx.stringLiteral());
    }

    private String resolveCollationUri(UriLiteralContext ctx) {
        String uriString = processURILiteral(ctx);
        URI uri = URILiteralUtils.resolve(
                this.translationContext.moduleContext().getStaticBaseURI(), uriString, createMetadataFromContext(ctx));
        return uri.toString();
    }

    private List<Annotation> processAnnotations(JsoniqParser.AnnotationsContext annotations) {
        return processAnnotations(annotations.annotation());
    }

    private List<Annotation> processAnnotations(List<JsoniqParser.AnnotationContext> annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (JsoniqParser.AnnotationContext annotationContext : annotations) {
            // for backwards compatibility, the specification allows for updating without % sign
            if (annotationContext.updating != null) {
                Name name = Name.createNameInDefaultXQueryAnnotationsNamespace("updating");
                parsedAnnotations.add(new Annotation(name, null));
                continue;
            }
            JsoniqParser.EqNameContext eqNameContext = annotationContext.eqName();
            Name name = parseEqName(eqNameContext, NameRole.ANNOTATION);
            Annotation.validateAnnotationName(name, createMetadataFromContext(annotationContext));
            List<Expression> literals = null;
            if (!annotationContext.literal().isEmpty()) {
                literals = new ArrayList<>();
                for (JsoniqParser.LiteralContext literalContext : annotationContext.literal()) {
                    literals.add((Expression) this.visitLiteral(literalContext));
                }
            }
            parsedAnnotations.add(new Annotation(name, literals));
        }

        return parsedAnnotations;
    }

    private static class DirAttributeProcessingResult {
        private final List<Expression> attributes;
        private final List<NamespaceDeclaration> namespaceDeclarations;

        private DirAttributeProcessingResult() {
            this.attributes = new ArrayList<>();
            this.namespaceDeclarations = new ArrayList<>();
        }
    }

    private DirAttributeProcessingResult getAttributesExpressionsList(JsoniqParser.DirAttributeListContext ctx) {
        DirAttributeProcessingResult result = new DirAttributeProcessingResult();

        List<JsoniqParser.QnameContext> attributeNames = ctx.attribute_qname;
        List<JsoniqParser.DirAttributeValueContext> attributeValues = ctx.attribute_value;

        // Namespace declarations are in scope for the entire element start tag,
        // including attributes that occur lexically before the declaration.
        for (int i = 0; i < attributeNames.size(); i++) {
            JsoniqParser.QnameContext qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if ("xmlns".equals(lexical) || lexical.startsWith("xmlns:")) {
                String declaredPrefix = "xmlns".equals(lexical) ? "" : lexical.substring("xmlns:".length());
                String uri = getNamespaceDeclarationUri(attributeValues.get(i));
                result.namespaceDeclarations.add(
                        new NamespaceDeclaration(declaredPrefix, uri, createMetadataFromContext(qnameCtx)));
                this.translationContext.bindConstructorNamespace(declaredPrefix, uri);
            }
        }

        // Translate non-namespace attributes after the complete namespace frame
        // has been established, while retaining their original source order.
        for (int i = 0; i < attributeNames.size(); i++) {
            JsoniqParser.QnameContext qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if ("xmlns".equals(lexical) || lexical.startsWith("xmlns:")) {
                continue;
            }
            Name attributeName = parseName(qnameCtx, NameRole.NO_DEFAULT_NAMESPACE);

            List<Expression> value = this.getAttributeValuesExpressionsList(attributeValues.get(i), true);
            AttributeNodeExpression attributeNode = new AttributeNodeExpression(
                    attributeName,
                    value,
                    createMetadataFromRange(
                            qnameCtx.getStart(), attributeValues.get(i).getStop()));
            result.attributes.add(attributeNode);
        }

        return result;
    }

    private List<Expression> getAttributeValuesExpressionsList(
            JsoniqParser.DirAttributeValueContext ctx, boolean allowEnclosedExpressions) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.DirAttributeValueAposContext
                || child instanceof JsoniqParser.DirAttributeValueQuotContext) {
            ParserRuleContext quotedValue = (ParserRuleContext) child;
            return processQuotedAttributeValue(quotedValue, allowEnclosedExpressions);
        }
        throw new UnsupportedOperationException("Unsupported attribute value: " + ctx.getText());
    }

    private String getNamespaceDeclarationUri(JsoniqParser.DirAttributeValueContext ctx) {
        List<Expression> uriExpressions = this.getAttributeValuesExpressionsList(ctx, false);
        StringBuilder uriBuilder = new StringBuilder();
        for (Expression expression : uriExpressions) {
            if (!(expression instanceof AttributeNodeContentExpression attributeContent)) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx));
            }
            uriBuilder.append(attributeContent.getContent());
        }
        return uriBuilder.toString();
    }

    private List<Expression> processQuotedAttributeValue(ParserRuleContext ctx, boolean allowEnclosedExpressions) {
        return DirectConstructorUtils.processQuotedValue(
                this.jsoniqTokenStream,
                ctx,
                allowEnclosedExpressions,
                this::createMetadataFromTree,
                this::createMetadataFromTrees,
                this::processAttributeContent);
    }

    /**
     * Helper method to process attribute content (handles nested quotes, expressions, and escaped braces).
     */
    private List<Expression> processAttributeContent(ParserRuleContext ctx, boolean allowEnclosedExpressions) {
        ParseTree child = ctx.children.get(0);
        List<Expression> expressions = new ArrayList<>();

        if (ctx instanceof JsoniqParser.DirAttributeContentQuotContext dirAttributeContentQuotContext
                && dirAttributeContentQuotContext.expr() != null) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx));
            }
            expressions.add((Expression) this.visitExpr(dirAttributeContentQuotContext.expr()));
        } else if (ctx instanceof JsoniqParser.DirAttributeContentAposContext dirAttributeContentAposContext
                && dirAttributeContentAposContext.expr() != null) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx));
            }
            expressions.add((Expression) this.visitExpr(dirAttributeContentAposContext.expr()));
        } else {
            // Preserve literal content after validating direct attribute restrictions.
            String childText = this.jsoniqTokenStream.getText(ctx.getSourceInterval());
            DirectConstructorUtils.validateLiteral(childText, ctx, this::createMetadataFromTree);
            String processedContent = DirectConstructorUtils.processLiteralContent(childText);
            expressions.add(new AttributeNodeContentExpression(processedContent, createMetadataFromTree(child)));
        }
        return expressions;
    }

    private void processDecimalFormatDeclaration(
            JsoniqParser.DecimalFormatDeclContext ctx, ExceptionMetadata metadata) {
        DecimalFormatDeclarationProcessor.process(
                ctx.KW_DEFAULT() != null,
                ctx.eqName(),
                ctx.DFPropertyName(),
                ctx.stringLiteral().stream()
                        .map(stringLiteral -> this.jsoniqTokenStream.getText(stringLiteral.getSourceInterval()))
                        .toList(),
                this.translationContext.moduleContext(),
                true,
                metadata);
    }
}
