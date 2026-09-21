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
import org.rumbledb.compiler.context.scripting.FlworStatementContext;
import org.rumbledb.compiler.context.scripting.SwitchStatementContext;
import org.rumbledb.compiler.context.scripting.TryCatchStatementContext;
import org.rumbledb.compiler.context.scripting.TypeSwitchStatementContext;
import org.rumbledb.compiler.context.scripting.VarDeclStatementContext;
import org.rumbledb.compiler.translation.ArithmeticTranslation;
import org.rumbledb.compiler.translation.ComparisonTranslation;
import org.rumbledb.compiler.translation.ControlTranslation;
import org.rumbledb.compiler.translation.DeclarationTranslation;
import org.rumbledb.compiler.translation.FlworTranslation;
import org.rumbledb.compiler.translation.ImportTranslation;
import org.rumbledb.compiler.translation.LogicTranslation;
import org.rumbledb.compiler.translation.ModuleTranslation;
import org.rumbledb.compiler.translation.PostfixTranslation;
import org.rumbledb.compiler.translation.PrimaryTranslation;
import org.rumbledb.compiler.translation.PrologBuilder;
import org.rumbledb.compiler.translation.QuantifiedTranslation;
import org.rumbledb.compiler.translation.SequenceTranslation;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.translation.TypeTranslation;
import org.rumbledb.compiler.translation.scripting.ControlStatementTranslation;
import org.rumbledb.compiler.translation.scripting.DeclarationStatementTranslation;
import org.rumbledb.compiler.translation.scripting.LoopStatementTranslation;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.block.BlockExpression;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.ValidateExpression;
import org.rumbledb.expressions.typing.ValidateExpression.ValidationMode;
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
    public Module visitModule(JsoniqParser.ModuleContext ctx) {
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
    public MainModule visitMainModule(JsoniqParser.MainModuleContext ctx) {
        return ModuleTranslation.mainModule(
                MainModuleContext.from(ctx), this.translationContext, this::visitProlog, this::visitProgram);
    }

    // region program
    @Override
    public Program visitProgram(JsoniqParser.ProgramContext ctx) {
        return ModuleTranslation.program(
                ProgramContext.from(ctx), this.translationContext, this::visitStatementsAndOptionalExpr);
    }

    // end region

    @Override
    public LibraryModule visitLibraryModule(JsoniqParser.LibraryModuleContext ctx) {
        return ModuleTranslation.libraryModule(
                LibraryModuleContext.from(ctx), this.translationContext, this::processURILiteral, this::visitProlog);
    }

    @Override
    public Prolog visitProlog(JsoniqParser.PrologContext ctx) {
        return new PrologVisitor().build(ctx);
    }

    private class PrologVisitor extends JsoniqParserBaseVisitor<Void> {
        private final PrologBuilder builder = new PrologBuilder(TranslationVisitor.this.translationContext);

        Prolog build(JsoniqParser.PrologContext ctx) {
            if (ctx == null) {
                return null;
            }
            for (JsoniqParser.PrologHeaderContext header : ctx.headers) {
                visit(header);
            }
            this.builder.finishHeader(createMetadataFromContext(ctx));
            for (JsoniqParser.AnnotatedDeclContext declaration : ctx.declarations) {
                visit(declaration);
            }
            return this.builder.build(createMetadataFromContext(ctx));
        }

        @Override
        public Void visitNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx) {
            this.builder.bindNamespace(
                    ctx.ncName().getText(), processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultNamespaceDecl(JsoniqParser.DefaultNamespaceDeclContext ctx) {
            boolean isFunction = ctx.type.getType() == JsoniqParser.KW_FUNCTION;
            this.builder.applyDefaultNamespace(
                    isFunction, processStringLiteral(ctx.stringLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitModuleImport(JsoniqParser.ModuleImportContext ctx) {
            this.builder.importModule(
                    ImportTranslation.moduleImport(
                            ModuleImportContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processURILiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitSchemaImport(JsoniqParser.SchemaImportContext ctx) {
            this.builder.importSchema(
                    ImportTranslation.schemaImport(
                            SchemaImportContext.from(ctx),
                            TranslationVisitor.this.translationContext,
                            TranslationVisitor.this::processURILiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitConstructionDecl(JsoniqParser.ConstructionDeclContext ctx) {
            boolean value = ctx.type.getType() == JsoniqParser.KW_PRESERVE;
            this.builder.applyConstruction(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBoundarySpaceDecl(JsoniqParser.BoundarySpaceDeclContext ctx) {
            boolean value = ctx.type.getType() == JsoniqParser.KW_PRESERVE;
            this.builder.applyBoundarySpace(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitEmptyOrderDecl(JsoniqParser.EmptyOrderDeclContext ctx) {
            boolean value = ctx.emptySequenceOrder.getText().equals("least");
            this.builder.applyEmptyOrder(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitCopyNamespacesDecl(JsoniqParser.CopyNamespacesDeclContext ctx) {
            boolean preserve = ctx.preserveMode().KW_PRESERVE() != null;
            boolean inherit = ctx.inheritMode().KW_INHERIT() != null;
            this.builder.applyCopyNamespaces(preserve, inherit, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBaseURIDecl(JsoniqParser.BaseURIDeclContext ctx) {
            this.builder.applyBaseUri(processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultCollationDecl(JsoniqParser.DefaultCollationDeclContext ctx) {
            this.builder.applyDefaultCollation(
                    processURILiteral(ctx.uriLiteral()),
                    createMetadataFromContext(ctx.uriLiteral()),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx) {
            Name name = ctx.eqName() == null ? null : parseEqName(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
            this.builder.applyDecimalFormat(
                    ctx.KW_DEFAULT() != null,
                    name,
                    ctx.DFPropertyName().stream().map(ParseTree::getText).toList(),
                    ctx.stringLiteral().stream()
                            .map(TranslationVisitor.this::processStringLiteral)
                            .toList(),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitOrderingModeDecl(JsoniqParser.OrderingModeDeclContext ctx) {
            this.builder.applyUnsupportedHeader(createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitVarDecl(JsoniqParser.VarDeclContext ctx) {
            this.builder.addVariable(DeclarationTranslation.varDecl(
                    VarDeclContext.from(ctx),
                    TranslationVisitor.this.translationContext,
                    TranslationVisitor.this::processAnnotations,
                    TranslationVisitor.this::parseVariableBinding,
                    TranslationVisitor.this::processSequenceType,
                    TranslationVisitor.this::visitExprSingle));
            return null;
        }

        @Override
        public Void visitContextItemDecl(JsoniqParser.ContextItemDeclContext ctx) {
            this.builder.addContextItem(DeclarationTranslation.contextItemDecl(
                    ContextItemDeclContext.from(ctx),
                    TranslationVisitor.this.translationContext,
                    TranslationVisitor.this::processSequenceType,
                    TranslationVisitor.this::visitExprSingle));
            return null;
        }

        @Override
        public Void visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
            this.builder.addFunction(DeclarationTranslation.functionDecl(
                    FunctionDeclContext.from(ctx),
                    TranslationVisitor.this.translationContext,
                    TranslationVisitor.this::processAnnotations,
                    TranslationVisitor.this::parseFunctionName,
                    TranslationVisitor.this::parseVariableBinding,
                    TranslationVisitor.this::processSequenceType,
                    TranslationVisitor.this::processSequenceType,
                    TranslationVisitor.this::visitStatementsAndOptionalExpr));
            return null;
        }

        @Override
        public Void visitOptionDecl(JsoniqParser.OptionDeclContext ctx) {
            this.builder.addOption(DeclarationTranslation.optionDecl(
                    OptionDeclContext.from(ctx),
                    TranslationVisitor.this.translationContext,
                    TranslationVisitor.this::parseEqName,
                    TranslationVisitor.this::processStringLiteral));
            return null;
        }

        @Override
        public Void visitTypeDecl(JsoniqParser.TypeDeclContext ctx) {
            this.builder.addType(processTypeDecl(ctx));
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
    public Expression visitExpr(JsoniqParser.ExprContext ctx) {
        return SequenceTranslation.expr(CommaExprContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public Expression visitExprSingle(JsoniqParser.ExprSingleContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Expression visitExprSimple(JsoniqParser.ExprSimpleContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }
    // endregion

    // region EnclosedExpression
    @Override
    public Expression visitEnclosedExpression(JsoniqParser.EnclosedExpressionContext ctx) {
        return SequenceTranslation.enclosedExpr(
                EnclosedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }
    // endregion

    // region Flowr
    @Override
    public FlworExpression visitFlworExpr(JsoniqParser.FlworExprContext ctx) {
        return FlworTranslation.flworExpr(
                FlworExprContext.from(ctx),
                this.translationContext,
                child -> this.visit(child) instanceof Clause clause ? clause : null,
                this::visitExprSingle);
    }

    @Override
    public ForClause visitForClause(JsoniqParser.ForClauseContext ctx) {
        return FlworTranslation.forClause(
                ForClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public ForClause visitForVar(JsoniqParser.ForVarContext ctx) {
        return FlworTranslation.forVar(
                ForVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public LetClause visitLetClause(JsoniqParser.LetClauseContext ctx) {
        return FlworTranslation.letClause(
                LetClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public LetClause visitLetVar(JsoniqParser.LetVarContext ctx) {
        return FlworTranslation.letVar(
                LetVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public WindowClause visitWindowClause(JsoniqParser.WindowClauseContext ctx) {
        return ctx.tumblingWindowClause() != null
                ? visitTumblingWindowClause(ctx.tumblingWindowClause())
                : visitSlidingWindowClause(ctx.slidingWindowClause());
    }

    @Override
    public WindowClause visitTumblingWindowClause(JsoniqParser.TumblingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public WindowClause visitSlidingWindowClause(JsoniqParser.SlidingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public GroupByClause visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        return FlworTranslation.groupByClause(
                GroupByClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public OrderByClause visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        return FlworTranslation.orderByClause(
                OrderByClauseContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public WhereClause visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        return FlworTranslation.whereClause(
                WhereClauseContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public CountClause visitCountClause(JsoniqParser.CountClauseContext ctx) {
        return FlworTranslation.countClause(
                CountClauseContext.from(ctx), this.translationContext, this::parseVariableBinding);
    }
    // endregion

    // region operational
    @Override
    public Expression visitOrExpr(JsoniqParser.OrExprContext ctx) {
        return LogicTranslation.orExpr(OrExprContext.from(ctx), this.translationContext, this::visitAndExpr);
    }

    @Override
    public Expression visitAndExpr(JsoniqParser.AndExprContext ctx) {
        return LogicTranslation.andExpr(AndExprContext.from(ctx), this.translationContext, this::visitNotExpr);
    }

    @Override
    public Expression visitNotExpr(JsoniqParser.NotExprContext ctx) {
        Expression mainExpression = this.visitComparisonExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        return new NotExpression(mainExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        return ComparisonTranslation.comparisonExpr(
                ComparisonExprContext.from(ctx), this.translationContext, this::visitStringConcatExpr);
    }

    @Override
    public Expression visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        return SequenceTranslation.stringConcatExpr(
                StringConcatExprContext.from(ctx), this.translationContext, this::visitRangeExpr);
    }

    @Override
    public Expression visitRangeExpr(JsoniqParser.RangeExprContext ctx) {
        return SequenceTranslation.rangeExpr(
                RangeExprContext.from(ctx), this.translationContext, this::visitAdditiveExpr);
    }

    @Override
    public Expression visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        return ArithmeticTranslation.additiveExpr(
                AdditiveExprContext.from(ctx), this.translationContext, this::visitMultiplicativeExpr);
    }

    @Override
    public Expression visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        return ArithmeticTranslation.multiplicativeExpr(
                MultiplicativeExprContext.from(ctx),
                this.translationContext,
                this.jsoniqTokenStream,
                this::visitUnionExpr);
    }

    @Override
    public Expression visitUnionExpr(JsoniqParser.UnionExprContext ctx) {
        return SequenceTranslation.unionExpr(
                UnionExprContext.from(ctx), this.translationContext, this::visitIntersectExceptExpr);
    }

    @Override
    public Expression visitIntersectExceptExpr(JsoniqParser.IntersectExceptExprContext ctx) {
        return SequenceTranslation.intersectExceptExpr(
                IntersectExceptExprContext.from(ctx), this.translationContext, this::visitInstanceOfExpr);
    }

    @Override
    public Expression visitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx) {
        return PostfixTranslation.simpleMapExpr(
                SimpleMapExprContext.from(ctx), this.translationContext, this::visitPathExpr, this::visitPathExpr);
    }

    @Override
    public Expression visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx) {
        return TypeTranslation.instanceOfExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitIsStaticallyExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx) {
        return TypeTranslation.isStaticallyExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitTreatExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitTreatExpr(JsoniqParser.TreatExprContext ctx) {
        return TypeTranslation.treatExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastableExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitCastableExpr(JsoniqParser.CastableExprContext ctx) {
        return TypeTranslation.castableExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastExpr,
                this::processSingleType);
    }

    @Override
    public Expression visitCastExpr(JsoniqParser.CastExprContext ctx) {
        return TypeTranslation.castExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitArrowExpr,
                this::processSingleType);
    }

    @Override
    public Expression visitArrowExpr(JsoniqParser.ArrowExprContext ctx) {
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
    public Expression visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        return ArithmeticTranslation.unaryExpr(
                UnaryExprContext.from(ctx), this.translationContext, this::visitValueExpr);
    }

    @Override
    public Expression visitValueExpr(JsoniqParser.ValueExprContext ctx) {
        return PrimaryTranslation.valueExpr(
                ValueExprContext.from(ctx), this.translationContext, this::visitSimpleMapExpr, this::visitValidateExpr);
    }

    @Override
    public Expression visitValidateExpr(JsoniqParser.ValidateExprContext ctx) {
        Expression mainExpression = this.visitExpr(ctx.expr());
        if (ctx.KW_TYPE() != null) {
            SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
            return new ValidateTypeExpression(mainExpression, true, sequenceType, createMetadataFromContext(ctx));
        }
        ValidationMode validationMode =
                ctx.validationMode() != null && ctx.validationMode().KW_LAX() != null
                        ? ValidationMode.LAX
                        : ValidationMode.STRICT;
        return new ValidateExpression(mainExpression, validationMode, null, createMetadataFromContext(ctx));
    }
    // endregion

    // region update

    @Override
    public InsertExpression visitInsertExpr(JsoniqParser.InsertExprContext ctx) {
        Expression toInsertExpr;
        Expression posExpr = null;
        if (ctx.pairConstructor() != null && !ctx.pairConstructor().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                Expression lhs = this.visitExprSingle(currentPair.lhs);
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
                    keys.add(lhs);
                }
                values.add(this.visitExprSingle(currentPair.rhs));
            }
            toInsertExpr = new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
        } else if (ctx.to_insert_expr != null) {
            toInsertExpr = this.visitExprSingle(ctx.to_insert_expr);
            if (ctx.pos_expr != null) {
                posExpr = this.visitExprSingle(ctx.pos_expr);
            }
        } else {
            throw new OurBadException("Unrecognised expression to insert in Insert Expression");
        }
        Expression mainExpr = this.visitExprSingle(ctx.main_expr);

        return new InsertExpression(mainExpr, toInsertExpr, posExpr, createMetadataFromContext(ctx));
    }

    @Override
    public DeleteExpression visitDeleteExpr(JsoniqParser.DeleteExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        return new DeleteExpression(mainExpression, locatorExpression, createMetadataFromContext(ctx));
    }

    @Override
    public RenameExpression visitRenameExpr(JsoniqParser.RenameExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression nameExpression = this.visitExprSingle(ctx.name_expr);
        return new RenameExpression(mainExpression, locatorExpression, nameExpression, createMetadataFromContext(ctx));
    }

    @Override
    public ReplaceExpression visitReplaceExpr(JsoniqParser.ReplaceExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression newExpression = this.visitExprSingle(ctx.replacer_expr);
        return new ReplaceExpression(mainExpression, locatorExpression, newExpression, createMetadataFromContext(ctx));
    }

    @Override
    public TransformExpression visitTransformExpr(JsoniqParser.TransformExprContext ctx) {
        List<CopyDeclaration> copyDecls = ctx.copyDecl().stream()
                .map(copyDeclCtx -> {
                    Name var = parseVariableBinding(copyDeclCtx.var_ref);
                    Expression expr = this.visitExprSingle(copyDeclCtx.src_expr);
                    return new CopyDeclaration(var, expr);
                })
                .collect(Collectors.toList());
        Expression modifyExpression = this.visitExprSingle(ctx.mod_expr);
        Expression returnExpression = this.visitExprSingle(ctx.ret_expr);
        return new TransformExpression(copyDecls, modifyExpression, returnExpression, createMetadataFromContext(ctx));
    }

    @Override
    public AppendExpression visitAppendExpr(JsoniqParser.AppendExprContext ctx) {
        Expression arrayExpression = this.visitExprSingle(ctx.array_expr);
        Expression toAppendExpression = this.visitExprSingle(ctx.to_append_expr);
        return new AppendExpression(arrayExpression, toAppendExpression, createMetadataFromContext(ctx));
    }

    @Override
    public CreateCollectionExpression visitCreateCollectionExpr(JsoniqParser.CreateCollectionExprContext ctx) {
        Expression collection = this.visitExprSimple(ctx.collection_name);
        Expression contentExpression;
        if (ctx.content != null) {
            contentExpression = this.visitExprSingle(ctx.content);
        } else {
            // use a CommaExpression as placeholder if the collection is created empty
            contentExpression = new CommaExpression(createMetadataFromContext(ctx));
        }
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        return new CreateCollectionExpression(collection, contentExpression, mode, createMetadataFromContext(ctx));
    }

    @Override
    public DeleteIndexFromCollectionExpression visitDeleteIndexExpr(JsoniqParser.DeleteIndexExprContext ctx) {
        Expression collection = this.visitExprSimple(ctx.collection_name);
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        boolean isFirst = (ctx.first != null);

        Expression numDelete = null;
        if (ctx.num != null) {
            numDelete = this.visitExprSingle(ctx.num);
        }

        return new DeleteIndexFromCollectionExpression(
                collection, numDelete, isFirst, mode, createMetadataFromContext(ctx));
    }

    @Override
    public DeleteSearchFromCollectionExpression visitDeleteSearchExpr(JsoniqParser.DeleteSearchExprContext ctx) {
        Expression contentExpression = this.visitExprSingle(ctx.content);
        return new DeleteSearchFromCollectionExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public EditCollectionExpression visitEditCollectionExpr(JsoniqParser.EditCollectionExprContext ctx) {
        Expression targetExpression = this.visitExprSingle(ctx.target);
        Expression contentExpression = this.visitExprSingle(ctx.content);
        return new EditCollectionExpression(targetExpression, contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public InsertIndexIntoCollectionExpression visitInsertIndexExpr(JsoniqParser.InsertIndexExprContext ctx) {
        Expression collection = this.visitExprSimple(ctx.collection_name);
        Expression contentExpression = this.visitExprSingle(ctx.content);
        Expression pos = ctx.pos != null ? this.visitExprSingle(ctx.pos) : null;
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        boolean isLast = (ctx.last != null);
        boolean isFirst = (ctx.first != null);

        return new InsertIndexIntoCollectionExpression(
                collection, contentExpression, pos, mode, isFirst, isLast, createMetadataFromContext(ctx));
    }

    @Override
    public InsertSearchIntoCollectionExpression visitInsertSearchExpr(JsoniqParser.InsertSearchExprContext ctx) {
        Expression targetExpression = this.visitExprSingle(ctx.target);
        Expression contentExpression = this.visitExprSingle(ctx.content);
        boolean isBefore = (ctx.before != null);
        return new InsertSearchIntoCollectionExpression(
                targetExpression, contentExpression, isBefore, createMetadataFromContext(ctx));
    }

    @Override
    public TruncateCollectionExpression visitTruncateCollectionExpr(JsoniqParser.TruncateCollectionExprContext ctx) {
        Expression collectionName = this.visitExprSimple(ctx.collection_name);
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        return new TruncateCollectionExpression(collectionName, mode, createMetadataFromContext(ctx));
    }

    public Expression getMainExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        Expression mainExpression = this.visitPostfixExpr(ctx.main_expr);
        if (mainExpression instanceof ObjectLookupExpression objectLookupExpression) {
            return objectLookupExpression.getMainExpression();
        } else if (mainExpression instanceof ArrayLookupExpression arrayLookupExpression) {
            return arrayLookupExpression.getMainExpression();
        } else {
            throw new OurBadException("Unrecognized main expression found in update expression.");
        }
    }

    public Expression getLocatorExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        Expression mainExpression = this.visitPostfixExpr(ctx.main_expr);
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
    public Expression visitPostfixExpr(JsoniqParser.PostfixExprContext ctx) {
        Expression mainExpression = this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext predicateContext) {
                Expression expr = this.visitPredicate(predicateContext);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop()));
            } else if (child instanceof JsoniqParser.LookupContext lookupContext) {
                Expression expr = this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop()));
            } else if (child instanceof JsoniqParser.ObjectLookupContext objectLookupContext) {
                Expression expr = this.visitObjectLookup(objectLookupContext);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), objectLookupContext.getStop()));
            } else if (child instanceof JsoniqParser.ArrayLookupContext arrayLookupContext) {
                Expression expr = this.visitArrayLookup(arrayLookupContext);
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
    public Expression visitPredicate(JsoniqParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Expression visitLookup(JsoniqParser.LookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    @Override
    public UnaryLookupExpression visitUnaryLookup(JsoniqParser.UnaryLookupContext ctx) {
        return new UnaryLookupExpression(this.visitKeySpecifier(ctx.keySpecifier()), createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitKeySpecifier(JsoniqParser.KeySpecifierContext ctx) {
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
    public Expression visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
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
    public Expression visitArrayLookup(JsoniqParser.ArrayLookupContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    // endregion

    // region primary
    @Override
    public Expression visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof TerminalNode) {
            return PrimaryTranslation.literalExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        return (Expression) visit(child);
    }

    @Override
    public Expression visitOrderedExpr(JsoniqParser.OrderedExprContext ctx) {
        throw new UnsupportedFeatureException("Ordered expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitUnorderedExpr(JsoniqParser.UnorderedExprContext ctx) {
        throw new UnsupportedFeatureException(
                "Unordered expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitStringConstructor(JsoniqParser.StringConstructorContext ctx) {
        throw new UnsupportedFeatureException("String constructor not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitLiteral(JsoniqParser.LiteralContext ctx) {
        return PrimaryTranslation.literal(
                LiteralExprContext.from(ctx), this.translationContext, this::processStringLiteral);
    }

    @Override
    public Expression visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
        // no merging constructor, just visit the k/v pairs
        if (ctx.merge_operator == null
                || ctx.merge_operator.size() == 0
                || ctx.merge_operator.get(0).getText().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                Expression lhs = this.visitExprSingle(currentPair.lhs);
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
                    keys.add(lhs);
                }
                values.add(this.visitExprSingle(currentPair.rhs));
            }
            if (this.translationContext.moduleContext().getQueryLanguage().equals("jsoniq10")) {
                return new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
            } else {
                return new MapConstructorExpression(keys, values, createMetadataFromContext(ctx));
            }
        }

        Expression childExpr;
        childExpr = this.visitExpr(ctx.expr());
        return new ObjectConstructorExpression(childExpr, createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitNodeConstructor(JsoniqParser.NodeConstructorContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public Expression visitDirectConstructor(JsoniqParser.DirectConstructorContext ctx) {
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

    private DirPIConstructorExpression visitDirPIConstructor(TerminalNode piToken, ExceptionMetadata metadata) {
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

    private DirElemConstructorExpression visitDirElemConstructorOpenClose(JsoniqParser.DirectConstructorContext ctx) {
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
                    this::visitDirElemContent);

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

    private DirElemConstructorExpression visitDirElemConstructorSingleTag(JsoniqParser.DirectConstructorContext ctx) {
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
    public Expression visitDirElemContent(JsoniqParser.DirElemContentContext ctx) {
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
    public Expression visitCommonContent(JsoniqParser.CommonContentContext ctx) {
        if (ctx.expr() != null) {
            return this.visitExpr(ctx.expr());
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
    public Expression visitComputedConstructor(JsoniqParser.ComputedConstructorContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public DocumentNodeConstructorExpression visitCompDocConstructor(JsoniqParser.CompDocConstructorContext ctx) {
        Expression contentExpression = this.visitEnclosedExpression(ctx.enclosedExpression());
        return new DocumentNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public TextNodeConstructorExpression visitCompTextConstructor(JsoniqParser.CompTextConstructorContext ctx) {
        Expression contentExpression = this.visitEnclosedExpression(ctx.enclosedExpression());

        return new TextNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public CommentNodeConstructorExpression visitCompCommentConstructor(
            JsoniqParser.CompCommentConstructorContext ctx) {
        Expression contentExpression = this.visitEnclosedExpression(ctx.enclosedExpression());

        return new CommentNodeConstructorExpression(contentExpression, createMetadataFromContext(ctx));
    }

    @Override
    public ComputedPIConstructorExpression visitCompPIConstructor(JsoniqParser.CompPIConstructorContext ctx) {
        Expression contentExpression = this.visitEnclosedExpression(ctx.enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedPIConstructorExpression(
                    ctx.ncName().getText(), contentExpression, createMetadataFromContext(ctx));
        }
        if (ctx.expr() != null) {
            Expression nameExpression = this.visitExpr(ctx.expr());
            return new ComputedPIConstructorExpression(
                    nameExpression, contentExpression, createMetadataFromContext(ctx));
        }
        throw new ParsingException(
                "Computed processing instruction constructor must have either a static NCName or a dynamic name expression",
                createMetadataFromContext(ctx));
    }

    @Override
    public ComputedAttributeConstructorExpression visitCompAttrConstructor(
            JsoniqParser.CompAttrConstructorContext ctx) {
        Expression valueExpression = this.visitEnclosedExpression(ctx.enclosedExpression());

        // Check if we have a static attribute name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.name != null) {
            // Static attribute name: attribute attributeName { value }
            Name attributeName = this.parseEqName(ctx.name, NameRole.NO_DEFAULT_NAMESPACE);
            return new ComputedAttributeConstructorExpression(
                    attributeName, valueExpression, createMetadataFromContext(ctx));
        } else if (ctx.name_expr != null) {
            // Dynamic attribute name: attribute { nameExpression } { value }
            Expression nameExpression = this.visitExpr(ctx.name_expr);
            return new ComputedAttributeConstructorExpression(
                    nameExpression, valueExpression, createMetadataFromContext(ctx));
        } else {
            throw new ParsingException(
                    "Computed attribute constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx));
        }
    }

    @Override
    public ComputedElementConstructorExpression visitCompElemConstructor(JsoniqParser.CompElemConstructorContext ctx) {
        Expression contentExpression = this.visitEnclosedContentExpr(ctx.enclosedContentExpr());

        // Check if we have a static element name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.eqName() != null) {
            // Static element name: element elementName { content }
            Name elementName = parseEqName(ctx.eqName(), NameRole.ELEMENT_CONSTRUCTOR);
            return new ComputedElementConstructorExpression(
                    elementName, contentExpression, createMetadataFromContext(ctx));
        } else if (ctx.expr() != null) {
            // Dynamic element name: element { nameExpression } { content }
            Expression nameExpression = this.visitExpr(ctx.expr());
            return new ComputedElementConstructorExpression(
                    nameExpression, contentExpression, createMetadataFromContext(ctx));
        } else {
            throw new ParsingException(
                    "Computed element constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx));
        }
    }

    @Override
    public ComputedNamespaceConstructorExpression visitCompNamespaceConstructor(
            JsoniqParser.CompNamespaceConstructorContext ctx) {
        Expression uriExpression =
                this.visitEnclosedExpression(ctx.enclosedURIExpr().enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedNamespaceConstructorExpression(
                    ctx.ncName().getText(), uriExpression, createMetadataFromContext(ctx));
        }
        if (ctx.enclosedPrefixExpr() != null) {
            Expression prefixExpression =
                    this.visitEnclosedExpression(ctx.enclosedPrefixExpr().enclosedExpression());
            return new ComputedNamespaceConstructorExpression(
                    prefixExpression, uriExpression, createMetadataFromContext(ctx));
        }
        throw new ParsingException(
                "Computed namespace constructor must have either a static prefix or a dynamic prefix expression",
                createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitEnclosedContentExpr(JsoniqParser.EnclosedContentExprContext ctx) {
        return this.visitEnclosedExpression(ctx.enclosedExpression());
    }

    @Override
    public ArrayConstructorExpression visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        return (ArrayConstructorExpression) visit(ctx.getChild(0));
    }

    @Override
    public ArrayConstructorExpression visitSquareArrayConstructor(JsoniqParser.SquareArrayConstructorContext ctx) {
        List<JsoniqParser.ExprSingleContext> memberCtxs = ctx.exprSingle();
        if (memberCtxs == null || memberCtxs.isEmpty()) {
            return new ArrayConstructorExpression(new ArrayList<>(), true, createMetadataFromContext(ctx));
        }
        List<Expression> memberExpressions = new ArrayList<>();
        if (this.translationContext.moduleContext().getQueryLanguage().equals("jsoniq10")) {
            // In JSONiq 1.0, the square array constructor behaves like the curly array constructor.
            // Thus, we concatenate all expressions into a single comma expression.
            for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                memberExpressions.add(this.visitExprSingle(memberCtx));
            }
            Expression commaExpression = new CommaExpression(memberExpressions, createMetadataFromContext(ctx));
            return new ArrayConstructorExpression(commaExpression, createMetadataFromContext(ctx));
        } else {
            log.debug("Not concatenating to comma.");
            // In JSONiq 4.0, the square array constructor behaves like in XQuery 4.0.
            for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                memberExpressions.add(this.visitExprSingle(memberCtx));
            }
            return new ArrayConstructorExpression(memberExpressions, true, createMetadataFromContext(ctx));
        }
    }

    @Override
    public ArrayConstructorExpression visitCurlyArrayConstructor(JsoniqParser.CurlyArrayConstructorContext ctx) {
        if (ctx.enclosedExpression() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(ctx));
        }
        Expression content = this.visitEnclosedExpression(ctx.enclosedExpression());
        return new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx) {
        return PrimaryTranslation.parenthesizedExpr(
                ParenthesizedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }

    @Override
    public VariableReferenceExpression visitVarRef(JsoniqParser.VarRefContext ctx) {
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
    public ContextItemExpression visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
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
    public FunctionCallExpression visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        return PrimaryTranslation.functionCall(
                FunctionCallContext.from(ctx), this.translationContext, this::parseFunctionName, this::visitArgument);
    }

    private List<Expression> getArgumentsFromArgumentListContext(JsoniqParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (JsoniqParser.ArgumentContext arg : ctx.args) {
                Expression currentArg = this.visitArgument(arg);
                arguments.add(currentArg);
            }
        }
        return arguments;
    }

    @Override
    public Expression visitArgument(JsoniqParser.ArgumentContext ctx) {
        if (ctx.exprSingle() != null) {
            return this.visitExprSingle(ctx.exprSingle());
        }
        return null;
    }

    @Override
    public Expression visitFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public NamedFunctionReferenceExpression visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx) {
        return PrimaryTranslation.namedFunctionRef(
                NamedFunctionRefContext.from(ctx), this.translationContext, this::parseFunctionName);
    }

    @Override
    public InlineFunctionExpression visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
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

        StatementsAndOptionalExpr funcBody = this.visitStatementsAndOptionalExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                annotations, null, fnParams, fnReturnType, funcBody, createMetadataFromContext(ctx));
    }
    // endregion

    // region control
    @Override
    public ConditionalExpression visitIfExpr(JsoniqParser.IfExprContext ctx) {
        return ControlTranslation.ifExpr(
                IfExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }

    @Override
    public SwitchExpression visitSwitchExpr(JsoniqParser.SwitchExprContext ctx) {
        return ControlTranslation.switchExpr(
                SwitchExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }
    // endregion

    // region quantified
    @Override
    public TypeSwitchExpression visitTypeswitchExpr(JsoniqParser.TypeswitchExprContext ctx) {
        return ControlTranslation.typeswitchExpr(
                TypeswitchExprContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public FunctionCallExpression visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx) {
        return QuantifiedTranslation.quantifiedExpr(
                QuantifiedExprContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public TryCatchExpression visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx) {
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
    public StatementsAndOptionalExpr visitStatements(JsoniqParser.StatementsContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statement()) {
            statements.add(this.visitStatement(stmt));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public StatementsAndExpr visitStatementsAndExpr(JsoniqParser.StatementsAndExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add(this.visitStatement(stmt));
        }
        Expression expression = this.visitExpr(ctx.expr());
        return new StatementsAndExpr(statements, expression, createMetadataFromContext(ctx));
    }

    @Override
    public StatementsAndOptionalExpr visitStatementsAndOptionalExpr(JsoniqParser.StatementsAndOptionalExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add(this.visitStatement(stmt));
        }
        if (ctx.expr() != null) {
            Expression expression = this.visitExpr(ctx.expr());
            return new StatementsAndOptionalExpr(statements, expression, createMetadataFromContext(ctx));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public Statement visitStatement(JsoniqParser.StatementContext ctx) {
        return (Statement) visit(ctx.getChild(0));
    }

    // mutation
    @Override
    public ApplyStatement visitApplyStatement(JsoniqParser.ApplyStatementContext ctx) {
        Expression exprSimple = this.visitExprSimple(ctx.exprSimple());
        return new ApplyStatement(exprSimple, createMetadataFromContext(ctx));
    }

    @Override
    public AssignStatement visitAssignStatement(JsoniqParser.AssignStatementContext ctx) {
        Name paramName = parseVariableReference(ctx.var_ref);
        Expression exprSingle = this.visitExprSingle(ctx.exprSingle());
        return new AssignStatement(exprSingle, paramName, createMetadataFromContext(ctx));
    }
    // end mutation

    // block
    @Override
    public BlockStatement visitBlockStatement(JsoniqParser.BlockStatementContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (JsoniqParser.StatementContext statement : ctx.statements().statement()) {
            statements.add(this.visitStatement(statement));
        }
        return new BlockStatement(statements, createMetadataFromContext(ctx));
    }

    @Override
    public BlockExpression visitBlockExpr(JsoniqParser.BlockExprContext ctx) {
        StatementsAndExpr statementsAndExpr = this.visitStatementsAndExpr(ctx.statementsAndExpr());
        return new BlockExpression(statementsAndExpr, createMetadataFromContext(ctx));
    }
    // end block

    // loops
    @Override
    public BreakStatement visitBreakStatement(JsoniqParser.BreakStatementContext ctx) {
        return new BreakStatement(createMetadataFromContext(ctx));
    }

    @Override
    public ContinueStatement visitContinueStatement(JsoniqParser.ContinueStatementContext ctx) {
        return new ContinueStatement(createMetadataFromContext(ctx));
    }

    @Override
    public ExitStatement visitExitStatement(JsoniqParser.ExitStatementContext ctx) {
        Expression exprSingle = this.visitExprSingle(ctx.exprSingle());
        return new ExitStatement(exprSingle, createMetadataFromContext(ctx));
    }

    @Override
    public FlowrStatement visitFlworStatement(JsoniqParser.FlworStatementContext ctx) {
        return LoopStatementTranslation.flworStatement(
                FlworStatementContext.from(ctx),
                this.translationContext,
                this::visitForClause,
                this::visitLetClause,
                child -> (Clause) this.visit(child),
                this::visitStatement);
    }

    @Override
    public WhileStatement visitWhileStatement(JsoniqParser.WhileStatementContext ctx) {
        Expression testCondition = this.visitExpr(ctx.test_expr);
        Statement statement = this.visitStatement(ctx.stmt);
        return new WhileStatement(testCondition, statement, createMetadataFromContext(ctx));
    }

    // end loops

    // control

    @Override
    public ConditionalStatement visitIfStatement(JsoniqParser.IfStatementContext ctx) {
        Expression condition = this.visitExpr(ctx.test_expr);
        Statement branch = this.visitStatement(ctx.branch);
        Statement elseBranch = this.visitStatement(ctx.else_branch);
        return new ConditionalStatement(condition, branch, elseBranch, createMetadataFromContext(ctx));
    }

    @Override
    public SwitchStatement visitSwitchStatement(JsoniqParser.SwitchStatementContext ctx) {
        return ControlStatementTranslation.switchStatement(
                SwitchStatementContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitExprSingle,
                this::visitStatement);
    }

    @Override
    public TryCatchStatement visitTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx) {
        return ControlStatementTranslation.tryCatchStatement(
                TryCatchStatementContext.from(ctx),
                this.translationContext,
                this::visitBlockStatement,
                this::parseEqName);
    }

    @Override
    public TypeSwitchStatement visitTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx) {
        return ControlStatementTranslation.typeSwitchStatement(
                TypeSwitchStatementContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitStatement,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    // end control

    // declaration

    @Override
    public Statement visitVarDeclStatement(JsoniqParser.VarDeclStatementContext ctx) {
        return DeclarationStatementTranslation.varDeclStatement(
                VarDeclStatementContext.from(ctx),
                this.translationContext,
                this::processAnnotations,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    // end declaration

    // start xml

    @Override
    public Expression visitPathExpr(JsoniqParser.PathExprContext ctx) {
        if (ctx.singleslash != null) {
            return visitSingleSlash(ctx, ctx.singleslash);
        } else if (ctx.doubleslash != null) {
            return visitDoubleSlash(ctx, ctx.doubleslash);
        } else if (ctx.relative != null) {
            return visitRelativeWithoutSlash(ctx.relative);
        }
        return visitSingleSlashNoStepExpr(ctx);
    }

    private Expression visitSingleSlashNoStepExpr(JsoniqParser.PathExprContext ctx) {
        // Case: No StepExpr, only dash
        return new PathRootExpression(createMetadataFromContext(ctx));
    }

    private Expression visitRelativeWithoutSlash(JsoniqParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1
                && relativeContext.stepExpr(0).postfixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostfixExpr(relativeContext.stepExpr(0).postfixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Expression visitDoubleSlash(
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

    private Expression visitSingleSlash(
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
            currentStepExpr = this.visitStepExpr(relativePathExprContext.stepExpr(i));
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
    public Expression visitStepExpr(JsoniqParser.StepExprContext ctx) {
        if (ctx.postfixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (JsoniqParser.PredicateContext predicateContext :
                    ctx.axisStep().predicateList().predicate()) {
                Expression predicate = this.visitPredicate(predicateContext);
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
                    literals.add(this.visitLiteral(literalContext));
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
            expressions.add(this.visitExpr(dirAttributeContentQuotContext.expr()));
        } else if (ctx instanceof JsoniqParser.DirAttributeContentAposContext dirAttributeContentAposContext
                && dirAttributeContentAposContext.expr() != null) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx));
            }
            expressions.add(this.visitExpr(dirAttributeContentAposContext.expr()));
        } else {
            // Preserve literal content after validating direct attribute restrictions.
            String childText = this.jsoniqTokenStream.getText(ctx.getSourceInterval());
            DirectConstructorUtils.validateLiteral(childText, ctx, this::createMetadataFromTree);
            String processedContent = DirectConstructorUtils.processLiteralContent(childText);
            expressions.add(new AttributeNodeContentExpression(processedContent, createMetadataFromTree(child)));
        }
        return expressions;
    }
}
