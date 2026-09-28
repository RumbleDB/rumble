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

import lombok.extern.log4j.Log4j2;

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
import org.rumbledb.compiler.context.scripting.ApplyStatementContext;
import org.rumbledb.compiler.context.scripting.AssignStatementContext;
import org.rumbledb.compiler.context.scripting.BlockExprContext;
import org.rumbledb.compiler.context.scripting.BlockStatementContext;
import org.rumbledb.compiler.context.scripting.ExitStatementContext;
import org.rumbledb.compiler.context.scripting.FlworStatementContext;
import org.rumbledb.compiler.context.scripting.IfStatementContext;
import org.rumbledb.compiler.context.scripting.StatementsAndExprContext;
import org.rumbledb.compiler.context.scripting.StatementsAndOptionalExprContext;
import org.rumbledb.compiler.context.scripting.StatementsContext;
import org.rumbledb.compiler.context.scripting.SwitchStatementContext;
import org.rumbledb.compiler.context.scripting.TryCatchStatementContext;
import org.rumbledb.compiler.context.scripting.TypeSwitchStatementContext;
import org.rumbledb.compiler.context.scripting.VarDeclStatementContext;
import org.rumbledb.compiler.context.scripting.WhileStatementContext;
import org.rumbledb.compiler.context.xml.AttributeTestContext;
import org.rumbledb.compiler.context.xml.CommonContentContext;
import org.rumbledb.compiler.context.xml.CompAttrConstructorContext;
import org.rumbledb.compiler.context.xml.CompCommentConstructorContext;
import org.rumbledb.compiler.context.xml.CompDocConstructorContext;
import org.rumbledb.compiler.context.xml.CompElemConstructorContext;
import org.rumbledb.compiler.context.xml.CompNamespaceConstructorContext;
import org.rumbledb.compiler.context.xml.CompPIConstructorContext;
import org.rumbledb.compiler.context.xml.CompTextConstructorContext;
import org.rumbledb.compiler.context.xml.DirElemContentContext;
import org.rumbledb.compiler.context.xml.DirectConstructorContext;
import org.rumbledb.compiler.context.xml.DocumentTestContext;
import org.rumbledb.compiler.context.xml.ElementTestContext;
import org.rumbledb.compiler.context.xml.EnclosedContentExprContext;
import org.rumbledb.compiler.context.xml.NameTestContext;
import org.rumbledb.compiler.context.xml.PiTestContext;
import org.rumbledb.compiler.context.xml.SchemaAttributeTestContext;
import org.rumbledb.compiler.context.xml.SchemaElementTestContext;
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
import org.rumbledb.compiler.translation.scripting.BlockStatementTranslation;
import org.rumbledb.compiler.translation.scripting.ControlStatementTranslation;
import org.rumbledb.compiler.translation.scripting.DeclarationStatementTranslation;
import org.rumbledb.compiler.translation.scripting.LoopStatementTranslation;
import org.rumbledb.compiler.translation.scripting.MutationStatementTranslation;
import org.rumbledb.compiler.translation.xml.XmlComputedConstructorTranslation;
import org.rumbledb.compiler.translation.xml.XmlDirectConstructorTranslation;
import org.rumbledb.compiler.translation.xml.XmlNodeTestTranslation;
import org.rumbledb.compiler.utils.URILiteralUtils;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.*;
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
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Module;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
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
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.PathRootExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.TextNodeConstructorExpression;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.SchemaNodeTest;
import org.rumbledb.parser.xquery.XQueryParser;
import org.rumbledb.parser.xquery.XQueryParser.UriLiteralContext;
import org.rumbledb.parser.xquery.XQueryParserBaseVisitor;
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
public class XQueryTranslationVisitor extends XQueryParserBaseVisitor<Node> {

    private final CommonTokenStream xQueryTokenStream;
    private final TranslationContext translationContext;

    public XQueryTranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings,
            String code,
            CommonTokenStream xQueryTokenStream) {
        this.translationContext =
                new TranslationContext(moduleContext, compilationConfiguration, externalBindings, isMainModule, code);
        this.xQueryTokenStream = xQueryTokenStream;

        String queryLanguage =
                this.translationContext.configuration().semantics().queryLanguage();
        if (queryLanguage.equals("xquery10")) {
            this.translationContext.moduleContext().setQueryLanguage("xquery10");
        } else if (queryLanguage.equals("xquery30")) {
            this.translationContext.moduleContext().setQueryLanguage("xquery30");
        } else if (queryLanguage.equals("xquery31")) {
            this.translationContext.moduleContext().setQueryLanguage("xquery31");
        } else if (queryLanguage.equals("xquery40")) {
            this.translationContext.moduleContext().setQueryLanguage("xquery40");
        }
    }

    // endregion expr

    // region module
    @Override
    public Module visitModule(XQueryParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty()) {
            String version = processStringLiteral(ctx.vers).trim();
            if (version.equals("1.0")) {
                this.translationContext.moduleContext().setQueryLanguage("xquery10");
            } else if (version.equals("3.0")) {
                this.translationContext.moduleContext().setQueryLanguage("xquery31");
            } else if (version.equals("3.1")) {
                this.translationContext.moduleContext().setQueryLanguage("xquery31");
            } else if (version.equals("4.0")) {
                this.translationContext.moduleContext().setQueryLanguage("xquery40");
            } else {
                throw new JsoniqVersionException(createMetadataFromContext(ctx));
            }
        }
        if (this.translationContext.isMainModule()) {
            if (ctx.mainModule() != null) {
                return this.visitMainModule(ctx.mainModule().get(0));
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
    public MainModule visitMainModule(XQueryParser.MainModuleContext ctx) {
        return ModuleTranslation.mainModule(
                MainModuleContext.from(ctx), this.translationContext, this::visitProlog, this::visitProgram);
    }

    // region program
    @Override
    public Program visitProgram(XQueryParser.ProgramContext ctx) {
        return ModuleTranslation.program(
                ProgramContext.from(ctx), this.translationContext, this::visitStatementsAndOptionalExpr);
    }

    // end region

    @Override
    public LibraryModule visitLibraryModule(XQueryParser.LibraryModuleContext ctx) {
        return ModuleTranslation.libraryModule(
                LibraryModuleContext.from(ctx), this.translationContext, this::processURILiteral, this::visitProlog);
    }

    @Override
    public Prolog visitProlog(XQueryParser.PrologContext ctx) {
        return new PrologVisitor().build(ctx);
    }

    private class PrologVisitor extends XQueryParserBaseVisitor<Void> {
        private final PrologBuilder builder = new PrologBuilder(XQueryTranslationVisitor.this.translationContext);

        Prolog build(XQueryParser.PrologContext ctx) {
            if (ctx == null) {
                return null;
            }
            for (XQueryParser.PrologHeaderContext header : ctx.headers) {
                visit(header);
            }
            this.builder.finishHeader(createMetadataFromContext(ctx));
            for (XQueryParser.AnnotatedDeclContext declaration : ctx.declarations) {
                visit(declaration);
            }
            return this.builder.build(createMetadataFromContext(ctx));
        }

        @Override
        public Void visitNamespaceDecl(XQueryParser.NamespaceDeclContext ctx) {
            this.builder.bindNamespace(
                    ctx.ncName().getText(), processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultNamespaceDecl(XQueryParser.DefaultNamespaceDeclContext ctx) {
            boolean isFunction = ctx.type.getType() == XQueryParser.KW_FUNCTION;
            this.builder.applyDefaultNamespace(
                    isFunction, processStringLiteral(ctx.stringLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitModuleImport(XQueryParser.ModuleImportContext ctx) {
            this.builder.importModule(
                    ImportTranslation.moduleImport(
                            ModuleImportContext.from(ctx),
                            XQueryTranslationVisitor.this.translationContext,
                            XQueryTranslationVisitor.this::processURILiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitSchemaImport(XQueryParser.SchemaImportContext ctx) {
            this.builder.importSchema(
                    ImportTranslation.schemaImport(
                            SchemaImportContext.from(ctx),
                            XQueryTranslationVisitor.this.translationContext,
                            XQueryTranslationVisitor.this::processURILiteral),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitConstructionDecl(XQueryParser.ConstructionDeclContext ctx) {
            boolean value = ctx.type.getType() == XQueryParser.KW_PRESERVE;
            this.builder.applyConstruction(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBoundarySpaceDecl(XQueryParser.BoundarySpaceDeclContext ctx) {
            boolean value = ctx.type.getType() == XQueryParser.KW_PRESERVE;
            this.builder.applyBoundarySpace(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitEmptyOrderDecl(XQueryParser.EmptyOrderDeclContext ctx) {
            boolean value = ctx.emptySequenceOrder.getText().equals("least");
            this.builder.applyEmptyOrder(value, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitCopyNamespacesDecl(XQueryParser.CopyNamespacesDeclContext ctx) {
            boolean preserve = ctx.preserveMode().KW_PRESERVE() != null;
            boolean inherit = ctx.inheritMode().KW_INHERIT() != null;
            this.builder.applyCopyNamespaces(preserve, inherit, createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitBaseURIDecl(XQueryParser.BaseURIDeclContext ctx) {
            this.builder.applyBaseUri(processURILiteral(ctx.uriLiteral()), createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDefaultCollationDecl(XQueryParser.DefaultCollationDeclContext ctx) {
            this.builder.applyDefaultCollation(
                    processURILiteral(ctx.uriLiteral()),
                    createMetadataFromContext(ctx.uriLiteral()),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitDecimalFormatDecl(XQueryParser.DecimalFormatDeclContext ctx) {
            Name name = ctx.eqName() == null ? null : parseEqName(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
            this.builder.applyDecimalFormat(
                    ctx.KW_DEFAULT() != null,
                    name,
                    ctx.DFPropertyName().stream().map(ParseTree::getText).toList(),
                    ctx.stringLiteral().stream()
                            .map(XQueryTranslationVisitor.this::processStringLiteral)
                            .toList(),
                    createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitOrderingModeDecl(XQueryParser.OrderingModeDeclContext ctx) {
            this.builder.applyUnsupportedHeader(createMetadataFromContext(ctx));
            return null;
        }

        @Override
        public Void visitVarDecl(XQueryParser.VarDeclContext ctx) {
            this.builder.addVariable(DeclarationTranslation.varDecl(
                    VarDeclContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::processAnnotations,
                    XQueryTranslationVisitor.this::parseVariableBinding,
                    XQueryTranslationVisitor.this::processSequenceType,
                    XQueryTranslationVisitor.this::visitExprSingle));
            return null;
        }

        @Override
        public Void visitContextItemDecl(XQueryParser.ContextItemDeclContext ctx) {
            this.builder.addContextItem(DeclarationTranslation.contextItemDecl(
                    ContextItemDeclContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::processSequenceType,
                    XQueryTranslationVisitor.this::visitExprSingle));
            return null;
        }

        @Override
        public Void visitFunctionDecl(XQueryParser.FunctionDeclContext ctx) {
            this.builder.addFunction(DeclarationTranslation.functionDecl(
                    FunctionDeclContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::processAnnotations,
                    XQueryTranslationVisitor.this::parseFunctionName,
                    XQueryTranslationVisitor.this::parseVariableBinding,
                    XQueryTranslationVisitor.this::processSequenceType,
                    XQueryTranslationVisitor.this::processSequenceType,
                    XQueryTranslationVisitor.this::visitStatementsAndOptionalExpr));
            return null;
        }

        @Override
        public Void visitOptionDecl(XQueryParser.OptionDeclContext ctx) {
            this.builder.addOption(DeclarationTranslation.optionDecl(
                    OptionDeclContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName,
                    XQueryTranslationVisitor.this::processStringLiteral));
            return null;
        }
    }

    private String processStringLiteral(XQueryParser.StringLiteralContext ctx) {
        return parseStringLiteral(this.xQueryTokenStream.getText(ctx.getSourceInterval()));
    }

    public Name parseFunctionName(XQueryParser.FunctionNameContext ctx) {
        return this.translationContext.names().resolveFunctionName(ctx.getText(), createMetadataFromContext(ctx));
    }

    /**
     * Parse an EQName. Delegates to {@link #parseName} for the {@code qname} branch; URI-qualified names use
     * {@link URIQualifiedNameParser}.
     */
    public Name parseEqName(XQueryParser.EqNameContext ctx, NameRole role) {
        if (ctx.qname() != null) {
            return parseName(ctx.qname(), role);
        }
        return URIQualifiedNameParser.parse(ctx.URIQualifiedName().getText(), createMetadataFromContext(ctx));
    }

    /** Adapts the XQuery QName grammar to the shared role-aware resolver. */
    public Name parseName(XQueryParser.QnameContext ctx, NameRole role) {
        return this.translationContext
                .names()
                .resolveQName(
                        ctx.FullQName() == null ? null : ctx.FullQName().getText(),
                        ctx.ns == null ? null : ctx.ns.getText(),
                        ctx.local_name == null ? null : ctx.local_name.getText(),
                        role,
                        createMetadataFromContext(ctx));
    }

    // endregion

    // region expr
    @Override
    public Expression visitExpr(XQueryParser.ExprContext ctx) {
        return SequenceTranslation.expr(CommaExprContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public Expression visitExprSingle(XQueryParser.ExprSingleContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Expression visitExprSimple(XQueryParser.ExprSimpleContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }
    // endregion

    // region EnclosedExpression
    @Override
    public Expression visitEnclosedExpression(XQueryParser.EnclosedExpressionContext ctx) {
        return SequenceTranslation.enclosedExpr(
                EnclosedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }
    // endregion

    // region Flowr
    @Override
    public FlworExpression visitFlworExpr(XQueryParser.FlworExprContext ctx) {
        return FlworTranslation.flworExpr(
                FlworExprContext.from(ctx),
                this.translationContext,
                child -> this.visit(child) instanceof Clause clause ? clause : null,
                this::visitExprSingle);
    }

    @Override
    public ForClause visitForClause(XQueryParser.ForClauseContext ctx) {
        return FlworTranslation.forClause(
                ForClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public ForClause visitForVar(XQueryParser.ForVarContext ctx) {
        return FlworTranslation.forVar(
                ForVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public LetClause visitLetClause(XQueryParser.LetClauseContext ctx) {
        return FlworTranslation.letClause(
                LetClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public LetClause visitLetVar(XQueryParser.LetVarContext ctx) {
        return FlworTranslation.letVar(
                LetVarContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public WindowClause visitWindowClause(XQueryParser.WindowClauseContext ctx) {
        if (ctx.tumblingWindowClause() != null) {
            return this.visitTumblingWindowClause(ctx.tumblingWindowClause());
        }
        return this.visitSlidingWindowClause(ctx.slidingWindowClause());
    }

    @Override
    public WindowClause visitTumblingWindowClause(XQueryParser.TumblingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public WindowClause visitSlidingWindowClause(XQueryParser.SlidingWindowClauseContext ctx) {
        return FlworTranslation.windowClause(
                WindowClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle);
    }

    @Override
    public GroupByClause visitGroupByClause(XQueryParser.GroupByClauseContext ctx) {
        return FlworTranslation.groupByClause(
                GroupByClauseContext.from(ctx),
                this.translationContext,
                this::parseVariableBinding,
                this::processSequenceType,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public OrderByClause visitOrderByClause(XQueryParser.OrderByClauseContext ctx) {
        return FlworTranslation.orderByClause(
                OrderByClauseContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::resolveCollationUri);
    }

    @Override
    public WhereClause visitWhereClause(XQueryParser.WhereClauseContext ctx) {
        return FlworTranslation.whereClause(
                WhereClauseContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public CountClause visitCountClause(XQueryParser.CountClauseContext ctx) {
        return FlworTranslation.countClause(
                CountClauseContext.from(ctx), this.translationContext, this::parseVariableBinding);
    }
    // endregion

    // region operational
    @Override
    public Expression visitOrExpr(XQueryParser.OrExprContext ctx) {
        return LogicTranslation.orExpr(OrExprContext.from(ctx), this.translationContext, this::visitAndExpr);
    }

    @Override
    public Expression visitAndExpr(XQueryParser.AndExprContext ctx) {
        return LogicTranslation.andExpr(AndExprContext.from(ctx), this.translationContext, this::visitComparisonExpr);
    }

    @Override
    public Expression visitComparisonExpr(XQueryParser.ComparisonExprContext ctx) {
        return ComparisonTranslation.comparisonExpr(
                ComparisonExprContext.from(ctx), this.translationContext, this::visitStringConcatExpr);
    }

    @Override
    public Expression visitStringConcatExpr(XQueryParser.StringConcatExprContext ctx) {
        return SequenceTranslation.stringConcatExpr(
                StringConcatExprContext.from(ctx), this.translationContext, this::visitRangeExpr);
    }

    @Override
    public Expression visitRangeExpr(XQueryParser.RangeExprContext ctx) {
        return SequenceTranslation.rangeExpr(
                RangeExprContext.from(ctx), this.translationContext, this::visitAdditiveExpr);
    }

    @Override
    public Expression visitAdditiveExpr(XQueryParser.AdditiveExprContext ctx) {
        return ArithmeticTranslation.additiveExpr(
                AdditiveExprContext.from(ctx), this.translationContext, this::visitMultiplicativeExpr);
    }

    @Override
    public Expression visitMultiplicativeExpr(XQueryParser.MultiplicativeExprContext ctx) {
        return ArithmeticTranslation.multiplicativeExpr(
                MultiplicativeExprContext.from(ctx),
                this.translationContext,
                this.xQueryTokenStream,
                this::visitUnionExpr);
    }

    @Override
    public Expression visitUnionExpr(XQueryParser.UnionExprContext ctx) {
        return SequenceTranslation.unionExpr(
                UnionExprContext.from(ctx), this.translationContext, this::visitIntersectExceptExpr);
    }

    @Override
    public Expression visitIntersectExceptExpr(XQueryParser.IntersectExceptExprContext ctx) {
        return SequenceTranslation.intersectExceptExpr(
                IntersectExceptExprContext.from(ctx), this.translationContext, this::visitInstanceOfExpr);
    }

    @Override
    public Expression visitSimpleMapExpr(XQueryParser.SimpleMapExprContext ctx) {
        return PostfixTranslation.simpleMapExpr(
                SimpleMapExprContext.from(ctx), this.translationContext, this::visitPathExpr, this::visitPathExpr);
    }

    @Override
    public Expression visitInstanceOfExpr(XQueryParser.InstanceOfExprContext ctx) {
        return TypeTranslation.instanceOfExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitIsStaticallyExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitIsStaticallyExpr(XQueryParser.IsStaticallyExprContext ctx) {
        return TypeTranslation.isStaticallyExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitTreatExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitTreatExpr(XQueryParser.TreatExprContext ctx) {
        return TypeTranslation.treatExpr(
                TypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastableExpr,
                this::processSequenceType);
    }

    @Override
    public Expression visitCastableExpr(XQueryParser.CastableExprContext ctx) {
        return TypeTranslation.castableExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitCastExpr,
                this::processSingleType);
    }

    @Override
    public Expression visitCastExpr(XQueryParser.CastExprContext ctx) {
        return TypeTranslation.castExpr(
                SingleTypeCheckExprContext.from(ctx),
                this.translationContext,
                this::visitArrowExpr,
                this::processSingleType);
    }

    @Override
    public Expression visitArrowExpr(XQueryParser.ArrowExprContext ctx) {
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
    public Expression visitUnaryExpr(XQueryParser.UnaryExprContext ctx) {
        return ArithmeticTranslation.unaryExpr(
                UnaryExprContext.from(ctx), this.translationContext, this::visitValueExpr);
    }

    @Override
    public Expression visitValueExpr(XQueryParser.ValueExprContext ctx) {
        return PrimaryTranslation.valueExpr(
                ValueExprContext.from(ctx), this.translationContext, this::visitSimpleMapExpr, this::visitValidateExpr);
    }

    @Override
    public Expression visitValidateExpr(XQueryParser.ValidateExprContext ctx) {
        Expression mainExpression = this.visitExpr(ctx.expr());
        ValidationMode validationMode = ValidationMode.STRICT;
        Name typeName = null;
        if (ctx.validationMode() != null && ctx.validationMode().KW_LAX() != null) {
            validationMode = ValidationMode.LAX;
        } else if (ctx.KW_TYPE() != null) {
            validationMode = ValidationMode.TYPE;
            typeName = parseEqName(ctx.typeName().eqName(), NameRole.TYPE);
        }
        return new ValidateExpression(mainExpression, validationMode, typeName, createMetadataFromContext(ctx));
    }
    // endregion

    // region update

    // TODO: does this need to be implemented in XQuery?

    // @Override
    // public Node visitInsertExpr(XQueryParser.InsertExprContext ctx) {
    // Expression toInsertExpr;
    // Expression posExpr = null;
    // if (ctx.pairConstructor() != null && !ctx.pairConstructor().isEmpty()) {
    // List<Expression> keys = new ArrayList<>();
    // List<Expression> values = new ArrayList<>();
    // for (XQueryParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
    // Expression lhs = this.visitExprSingle(currentPair.lhs);
    // if (lhs instanceof StepExpr) {
    // throw new ParsingException(
    // "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to
    // JSONiq 1.0 using the --xquery-version CLI option.",
    // createMetadataFromContext(ctx)
    // );
    // } else {
    // keys.add(lhs);
    // }
    // values.add(this.visitExprSingle(currentPair.rhs));
    // }
    // toInsertExpr = new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
    // } else if (ctx.to_insert_expr != null) {
    // toInsertExpr = this.visitExprSingle(ctx.to_insert_expr);
    // if (ctx.pos_expr != null) {
    // posExpr = this.visitExprSingle(ctx.pos_expr);
    // }
    // } else {
    // throw new OurBadException("Unrecognised expression to insert in Insert Expression");
    // }
    // Expression mainExpr = this.visitExprSingle(ctx.main_expr);

    // return new InsertExpression(mainExpr, toInsertExpr, posExpr, createMetadataFromContext(ctx));
    // }

    // @Override
    // public Node visitDeleteExpr(XQueryParser.DeleteExprContext ctx) {
    // Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // return new DeleteExpression(mainExpression, locatorExpression, createMetadataFromContext(ctx));
    // }

    // @Override
    // public Node visitRenameExpr(XQueryParser.RenameExprContext ctx) {
    // Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // Expression nameExpression = this.visitExprSingle(ctx.name_expr);
    // return new RenameExpression(
    // mainExpression,
    // locatorExpression,
    // nameExpression,
    // createMetadataFromContext(ctx)
    // );
    // }

    // @Override
    // public Node visitReplaceExpr(XQueryParser.ReplaceExprContext ctx) {
    // Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
    // Expression newExpression = this.visitExprSingle(ctx.replacer_expr);
    // return new ReplaceExpression(
    // mainExpression,
    // locatorExpression,
    // newExpression,
    // createMetadataFromContext(ctx)
    // );
    // }

    // @Override
    // public Node visitTransformExpr(XQueryParser.TransformExprContext ctx) {
    // List<CopyDeclaration> copyDecls = ctx.copyDecl()
    // .stream()
    // .map(copyDeclCtx -> {
    // Name var = parseVariableBinding(copyDeclCtx.var_ref);
    // Expression expr = this.visitExprSingle(copyDeclCtx.src_expr);
    // return new CopyDeclaration(var, expr);
    // })
    // .collect(Collectors.toList());
    // Expression modifyExpression = this.visitExprSingle(ctx.mod_expr);
    // Expression returnExpression = this.visitExprSingle(ctx.ret_expr);
    // return new TransformExpression(copyDecls, modifyExpression, returnExpression,
    // createMetadataFromContext(ctx));
    // }

    // @Override
    // public Node visitAppendExpr(XQueryParser.AppendExprContext ctx) {
    // Expression arrayExpression = this.visitExprSingle(ctx.array_expr);
    // Expression toAppendExpression = this.visitExprSingle(ctx.to_append_expr);
    // return new AppendExpression(arrayExpression, toAppendExpression, createMetadataFromContext(ctx));
    // }

    // public Expression getMainExpressionFromUpdateLocatorContext(XQueryParser.UpdateLocatorContext ctx) {
    // Expression mainExpression = this.visitPrimaryExpr(ctx.main_expr);
    // for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 1)) {
    // if (child instanceof XQueryParser.LookupContext) {
    // Expression expr = this.visitLookup((XQueryParser.LookupContext) child);
    // mainExpression = new PostfixLookupExpression(
    // mainExpression,
    // expr,
    // createMetadataFromContext(ctx)
    // );
    // } else {
    // throw new OurBadException("Unrecognized locator expression found in update expression.");
    // }
    // }
    // return mainExpression;
    // }

    // public Expression getLocatorExpressionFromUpdateLocatorContext(XQueryParser.UpdateLocatorContext ctx) {
    // ParseTree locatorExprCtx = ctx.getChild(ctx.getChildCount() - 1);
    // if (locatorExprCtx instanceof XQueryParser.LookupContext) {
    // return this.visitLookup((XQueryParser.LookupContext) locatorExprCtx);
    // } else {
    // throw new OurBadException("Unrecognized locator found in update expression.");
    // }
    // }

    // endregion

    // region postfix
    @Override
    public Expression visitPostfixExpr(XQueryParser.PostfixExprContext ctx) {
        Expression mainExpression = this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof XQueryParser.PredicateContext predicateContext) {
                Expression expr = this.visitPredicate(predicateContext);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop()));
            } else if (child instanceof XQueryParser.LookupContext lookupContext) {
                Expression expr = this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop()));
            } else if (child instanceof XQueryParser.ArgumentListContext argumentListContext) {
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
    public Expression visitPredicate(XQueryParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Expression visitLookup(XQueryParser.LookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    @Override
    public UnaryLookupExpression visitUnaryLookup(XQueryParser.UnaryLookupContext ctx) {
        return new UnaryLookupExpression(this.visitKeySpecifier(ctx.keySpecifier()), createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitKeySpecifier(XQueryParser.KeySpecifierContext ctx) {
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

    // endregion

    // region primary
    @Override
    public Expression visitPrimaryExpr(XQueryParser.PrimaryExprContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public Expression visitOrderedExpr(XQueryParser.OrderedExprContext ctx) {
        throw new UnsupportedFeatureException("Ordered expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitUnorderedExpr(XQueryParser.UnorderedExprContext ctx) {
        throw new UnsupportedFeatureException(
                "Unordered expression not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitStringConstructor(XQueryParser.StringConstructorContext ctx) {
        throw new UnsupportedFeatureException("String constructor not yet implemented", createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitLiteral(XQueryParser.LiteralContext ctx) {
        return PrimaryTranslation.literal(
                LiteralExprContext.from(ctx), this.translationContext, this::processStringLiteral);
    }

    private String parseStringLiteral(String source) {
        return StringLiteralUtils.parseXQuery(source);
    }

    @Override
    public Expression visitObjectConstructor(XQueryParser.ObjectConstructorContext ctx) {
        List<Expression> keys = new ArrayList<>();
        List<Expression> values = new ArrayList<>();
        for (XQueryParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
            Expression lhs = this.visitExprSingle(currentPair.lhs);
            if (lhs instanceof StepExpr) {
                throw new ParsingException(
                        "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --xquery-version CLI option.",
                        createMetadataFromContext(ctx));
            } else {
                keys.add(lhs);
            }
            values.add(this.visitExprSingle(currentPair.rhs));
        }
        return new MapConstructorExpression(keys, values, createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitNodeConstructor(XQueryParser.NodeConstructorContext ctx) {
        if (ctx.directConstructor() != null) {
            return visitDirectConstructor(ctx.directConstructor());
        }
        return visitComputedConstructor(ctx.computedConstructor());
    }

    @Override
    public Expression visitDirectConstructor(XQueryParser.DirectConstructorContext ctx) {
        return XmlDirectConstructorTranslation.directConstructor(
                DirectConstructorContext.from(ctx),
                this.xQueryTokenStream,
                this.translationContext,
                this::parseName,
                this::visitDirElemContent,
                this::visitExpr);
    }

    @Override
    public Expression visitDirElemContent(XQueryParser.DirElemContentContext ctx) {
        return XmlDirectConstructorTranslation.dirElemContent(
                DirElemContentContext.from(ctx),
                this.xQueryTokenStream,
                this.translationContext,
                this::visitDirectConstructor,
                this::visitCommonContent);
    }

    @Override
    public Expression visitCommonContent(XQueryParser.CommonContentContext ctx) {
        return XmlDirectConstructorTranslation.commonContent(
                CommonContentContext.from(ctx), this.translationContext, this::visitExpr);
    }

    @Override
    public Expression visitComputedConstructor(XQueryParser.ComputedConstructorContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public DocumentNodeConstructorExpression visitCompDocConstructor(XQueryParser.CompDocConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compDocConstructor(
                CompDocConstructorContext.from(ctx), this.translationContext, this::visitEnclosedExpression);
    }

    @Override
    public TextNodeConstructorExpression visitCompTextConstructor(XQueryParser.CompTextConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compTextConstructor(
                CompTextConstructorContext.from(ctx), this.translationContext, this::visitEnclosedExpression);
    }

    @Override
    public CommentNodeConstructorExpression visitCompCommentConstructor(
            XQueryParser.CompCommentConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compCommentConstructor(
                CompCommentConstructorContext.from(ctx), this.translationContext, this::visitEnclosedExpression);
    }

    @Override
    public ComputedPIConstructorExpression visitCompPIConstructor(XQueryParser.CompPIConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compPIConstructor(
                CompPIConstructorContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitEnclosedExpression);
    }

    @Override
    public ComputedAttributeConstructorExpression visitCompAttrConstructor(
            XQueryParser.CompAttrConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compAttrConstructor(
                CompAttrConstructorContext.from(ctx),
                this.translationContext,
                this::parseEqName,
                this::visitExpr,
                this::visitEnclosedExpression);
    }

    @Override
    public ComputedElementConstructorExpression visitCompElemConstructor(XQueryParser.CompElemConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compElemConstructor(
                CompElemConstructorContext.from(ctx),
                this.translationContext,
                this::parseEqName,
                this::visitExpr,
                this::visitEnclosedContentExpr);
    }

    @Override
    public ComputedNamespaceConstructorExpression visitCompNamespaceConstructor(
            XQueryParser.CompNamespaceConstructorContext ctx) {
        return XmlComputedConstructorTranslation.compNamespaceConstructor(
                CompNamespaceConstructorContext.from(ctx), this.translationContext, this::visitEnclosedExpression);
    }

    @Override
    public Expression visitEnclosedContentExpr(XQueryParser.EnclosedContentExprContext ctx) {
        return XmlComputedConstructorTranslation.enclosedContentExpr(
                EnclosedContentExprContext.from(ctx), this::visitEnclosedExpression);
    }

    @Override
    public ArrayConstructorExpression visitArrayConstructor(XQueryParser.ArrayConstructorContext ctx) {
        return (ArrayConstructorExpression) visit(ctx.getChild(0));
    }

    @Override
    public ArrayConstructorExpression visitSquareArrayConstructor(XQueryParser.SquareArrayConstructorContext ctx) {
        List<XQueryParser.ExprSingleContext> memberCtxs = ctx.exprSingle();
        if (memberCtxs == null || memberCtxs.isEmpty()) {
            return new ArrayConstructorExpression(new ArrayList<>(), true, createMetadataFromContext(ctx));
        }
        List<Expression> memberExpressions = new ArrayList<>();
        for (XQueryParser.ExprSingleContext memberCtx : memberCtxs) {
            memberExpressions.add(this.visitExprSingle(memberCtx));
        }
        return new ArrayConstructorExpression(memberExpressions, true, createMetadataFromContext(ctx));
    }

    @Override
    public ArrayConstructorExpression visitCurlyArrayConstructor(XQueryParser.CurlyArrayConstructorContext ctx) {
        if (ctx.enclosedExpression() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(ctx));
        }
        Expression content = this.visitEnclosedExpression(ctx.enclosedExpression());
        return new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
    }

    @Override
    public Expression visitParenthesizedExpr(XQueryParser.ParenthesizedExprContext ctx) {
        return PrimaryTranslation.parenthesizedExpr(
                ParenthesizedExprContext.from(ctx), this.translationContext, this::visitExpr);
    }

    @Override
    public VariableReferenceExpression visitVarRef(XQueryParser.VarRefContext ctx) {
        return PrimaryTranslation.varRef(VarRefContext.from(ctx), this.translationContext, this::parseEqName);
    }

    private Name parseVariableReference(XQueryParser.VarRefContext ctx) {
        return parseVariableName(ctx.eqName());
    }

    private Name parseVariableBinding(XQueryParser.VarBindingContext ctx) {
        return parseVariableName(ctx.eqName());
    }

    private Name parseVariableName(XQueryParser.EqNameContext ctx) {
        return parseEqName(ctx, NameRole.NO_DEFAULT_NAMESPACE);
    }

    @Override
    public ContextItemExpression visitContextItemExpr(XQueryParser.ContextItemExprContext ctx) {
        return PrimaryTranslation.contextItemExpr(ctx, this.translationContext);
    }

    public SequenceType processSequenceType(XQueryParser.SequenceTypeContext ctx) {
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

    public SequenceType processSingleType(XQueryParser.SingleTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.createSequenceType("()");
        }

        ItemType itemType = processItemType(ctx.item);
        if (ctx.question.size() > 0) {
            return new SequenceType(itemType, SequenceType.Arity.OneOrZero);
        }
        return new SequenceType(itemType);
    }

    public ItemType processItemType(XQueryParser.ItemTypeContext itemTypeContext) {
        if (itemTypeContext.parenthesizedItemTest() != null) {
            return processItemType(itemTypeContext.parenthesizedItemTest().itemType());
        }
        if (itemTypeContext.KW_ITEM() != null) {
            return BuiltinTypesCatalogue.item;
        }
        if (itemTypeContext.functionTest() != null) {
            processAnnotations(itemTypeContext.functionTest().annotation());
            // we have a function item type
            XQueryParser.TypedFunctionTestContext typedFnCtx =
                    itemTypeContext.functionTest().typedFunctionTest();
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
            XQueryParser.MapTestContext mapTestContext = itemTypeContext.mapTest();
            if (mapTestContext.anyMapTest() != null) {
                return BuiltinTypesCatalogue.mapItem;
            }
            XQueryParser.TypedMapTestContext typedMapTestContext = mapTestContext.typedMapTest();
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
            XQueryParser.ArrayTestContext arrayTestContext = itemTypeContext.arrayTest();
            if (arrayTestContext.anyArrayTest() != null) {
                // XQuery 3.1 array(*) is the XDM array type (members are sequences), not js:array().
                return BuiltinTypesCatalogue.xqueryArrayItem;
            }
            XQueryParser.TypedArrayTestContext typedArrayTestContext = arrayTestContext.typedArrayTest();
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

    private ItemType processKindTestAsItemType(XQueryParser.KindTestContext kindTestContext) {
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
            XQueryParser.DocumentTestContext documentTestContext = kindTestContext.documentTest();
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
            XQueryParser.AttributeTestContext attributeTestContext = kindTestContext.attributeTest();
            Name attributeName = attributeTestContext.attributeNameOrWildcard() == null
                            || attributeTestContext.attributeNameOrWildcard().attributeName() == null
                    ? null
                    : parseEqName(
                            attributeTestContext
                                    .attributeNameOrWildcard()
                                    .attributeName()
                                    .eqName(),
                            NameRole.NO_DEFAULT_NAMESPACE);
            if (attributeTestContext.typeName() == null) {
                return attributeName == null
                        ? BuiltinTypesCatalogue.attributeNode
                        : ItemTypeFactory.attributeNodeItemType(attributeName);
            }
            Name typeName = parseEqName(attributeTestContext.typeName().eqName(), NameRole.TYPE);
            return ItemTypeFactory.attributeNodeItemType(
                    attributeName,
                    typeName,
                    this.translationContext
                            .moduleContext()
                            .getInScopeSchemaTypes()
                            .getXmlSchemaCatalog()
                            .getTypeHierarchy(typeName, createMetadataFromContext(attributeTestContext)));
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
            XQueryParser.PiTestContext piTestContext = kindTestContext.piTest();
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

    private ElementNodeItemType getSchemaElementTestAsItemType(XQueryParser.SchemaElementTestContext ctx) {
        Name name = parseEqName(ctx.elementDeclaration().elementName().eqName(), NameRole.ELEMENT_CONSTRUCTOR);
        return this.translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaElementTest(name, createMetadataFromContext(ctx));
    }

    private ItemType getSchemaAttributeTestAsItemType(XQueryParser.SchemaAttributeTestContext ctx) {
        Name name = parseEqName(ctx.attributeDeclaration().attributeName().eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return this.translationContext
                .moduleContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getSchemaAttributeTest(name, createMetadataFromContext(ctx));
    }

    private ElementNodeItemType getElementTestAsItemType(XQueryParser.ElementTestContext elementTestContext) {
        Name elementName = elementTestContext.elementNameOrWildcard() == null
                        || elementTestContext.elementNameOrWildcard().elementName() == null
                ? null
                : parseEqName(
                        elementTestContext.elementNameOrWildcard().elementName().eqName(),
                        NameRole.ELEMENT_CONSTRUCTOR);
        if (elementTestContext.typeName() == null) {
            return elementName == null
                    ? (ElementNodeItemType) BuiltinTypesCatalogue.elementNode
                    : (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(elementName);
        }
        Name typeName = parseEqName(elementTestContext.typeName().eqName(), NameRole.TYPE);
        return (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(
                elementName,
                typeName,
                this.translationContext
                        .moduleContext()
                        .getInScopeSchemaTypes()
                        .getXmlSchemaCatalog()
                        .getTypeHierarchy(typeName, createMetadataFromContext(elementTestContext)),
                elementTestContext.optional != null);
    }

    @Override
    public FunctionCallExpression visitFunctionCall(XQueryParser.FunctionCallContext ctx) {
        return PrimaryTranslation.functionCall(
                FunctionCallContext.from(ctx), this.translationContext, this::parseFunctionName, this::visitArgument);
    }

    private List<Expression> getArgumentsFromArgumentListContext(XQueryParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (XQueryParser.ArgumentContext arg : ctx.args) {
                Expression currentArg = this.visitArgument(arg);
                arguments.add(currentArg);
            }
        }
        return arguments;
    }

    @Override
    public Expression visitArgument(XQueryParser.ArgumentContext ctx) {
        if (ctx.exprSingle() != null) {
            return this.visitExprSingle(ctx.exprSingle());
        }
        return null;
    }

    @Override
    public Expression visitFunctionItemExpr(XQueryParser.FunctionItemExprContext ctx) {
        return (Expression) visit(ctx.getChild(0));
    }

    @Override
    public NamedFunctionReferenceExpression visitNamedFunctionRef(XQueryParser.NamedFunctionRefContext ctx) {
        return PrimaryTranslation.namedFunctionRef(
                NamedFunctionRefContext.from(ctx), this.translationContext, this::parseFunctionName);
    }

    @Override
    public InlineFunctionExpression visitInlineFunctionExpr(XQueryParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.createSequenceType("item*");
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
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
    public ConditionalExpression visitIfExpr(XQueryParser.IfExprContext ctx) {
        return ControlTranslation.ifExpr(
                IfExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }

    @Override
    public SwitchExpression visitSwitchExpr(XQueryParser.SwitchExprContext ctx) {
        return ControlTranslation.switchExpr(
                SwitchExprContext.from(ctx), this.translationContext, this::visitExpr, this::visitExprSingle);
    }
    // endregion

    // region quantified
    @Override
    public TypeSwitchExpression visitTypeswitchExpr(XQueryParser.TypeswitchExprContext ctx) {
        return ControlTranslation.typeswitchExpr(
                TypeswitchExprContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public FunctionCallExpression visitQuantifiedExpr(XQueryParser.QuantifiedExprContext ctx) {
        return QuantifiedTranslation.quantifiedExpr(
                QuantifiedExprContext.from(ctx),
                this.translationContext,
                this::visitExprSingle,
                this::parseVariableBinding,
                this::processSequenceType);
    }

    @Override
    public TryCatchExpression visitTryCatchExpr(XQueryParser.TryCatchExprContext ctx) {
        return ControlTranslation.tryCatchExpr(
                TryCatchExprContext.from(ctx), this.translationContext, this::visitExpr, this::parseEqName);
    }

    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext context) {
        return this.translationContext.metadata(context);
    }

    private ExceptionMetadata createMetadataFromRange(Token start, Token end) {
        return this.translationContext.metadata(start, end);
    }

    // region scripting
    @Override
    public StatementsAndOptionalExpr visitStatements(XQueryParser.StatementsContext ctx) {
        return BlockStatementTranslation.statements(
                StatementsContext.from(ctx), this.translationContext, this::visitStatement);
    }

    @Override
    public StatementsAndExpr visitStatementsAndExpr(XQueryParser.StatementsAndExprContext ctx) {
        return BlockStatementTranslation.statementsAndExpr(
                StatementsAndExprContext.from(ctx), this.translationContext, this::visitStatement, this::visitExpr);
    }

    @Override
    public StatementsAndOptionalExpr visitStatementsAndOptionalExpr(XQueryParser.StatementsAndOptionalExprContext ctx) {
        return BlockStatementTranslation.statementsAndOptionalExpr(
                StatementsAndOptionalExprContext.from(ctx),
                this.translationContext,
                this::visitStatement,
                this::visitExpr);
    }

    @Override
    public Statement visitStatement(XQueryParser.StatementContext ctx) {
        return (Statement) visit(ctx.getChild(0));
    }

    // mutation
    @Override
    public ApplyStatement visitApplyStatement(XQueryParser.ApplyStatementContext ctx) {
        return MutationStatementTranslation.applyStatement(
                ApplyStatementContext.from(ctx), this.translationContext, this::visitExprSimple);
    }

    @Override
    public AssignStatement visitAssignStatement(XQueryParser.AssignStatementContext ctx) {
        return MutationStatementTranslation.assignStatement(
                AssignStatementContext.from(ctx),
                this.translationContext,
                this::parseVariableReference,
                this::visitExprSingle);
    }
    // end mutation

    // block
    @Override
    public BlockStatement visitBlockStatement(XQueryParser.BlockStatementContext ctx) {
        return BlockStatementTranslation.blockStatement(
                BlockStatementContext.from(ctx), this.translationContext, this::visitStatement);
    }

    @Override
    public BlockExpression visitBlockExpr(XQueryParser.BlockExprContext ctx) {
        return BlockStatementTranslation.blockExpr(
                BlockExprContext.from(ctx), this.translationContext, this::visitStatementsAndExpr);
    }
    // end block

    // loops
    @Override
    public BreakStatement visitBreakStatement(XQueryParser.BreakStatementContext ctx) {
        return LoopStatementTranslation.breakStatement(ctx, this.translationContext);
    }

    @Override
    public ContinueStatement visitContinueStatement(XQueryParser.ContinueStatementContext ctx) {
        return LoopStatementTranslation.continueStatement(ctx, this.translationContext);
    }

    @Override
    public ExitStatement visitExitStatement(XQueryParser.ExitStatementContext ctx) {
        return LoopStatementTranslation.exitStatement(
                ExitStatementContext.from(ctx), this.translationContext, this::visitExprSingle);
    }

    @Override
    public FlowrStatement visitFlworStatement(XQueryParser.FlworStatementContext ctx) {
        return LoopStatementTranslation.flworStatement(
                FlworStatementContext.from(ctx),
                this.translationContext,
                this::visitForClause,
                this::visitLetClause,
                child -> (Clause) this.visit(child),
                this::visitStatement);
    }

    @Override
    public WhileStatement visitWhileStatement(XQueryParser.WhileStatementContext ctx) {
        return LoopStatementTranslation.whileStatement(
                WhileStatementContext.from(ctx), this.translationContext, this::visitExpr, this::visitStatement);
    }

    // end loops

    // control

    @Override
    public ConditionalStatement visitIfStatement(XQueryParser.IfStatementContext ctx) {
        return ControlStatementTranslation.ifStatement(
                IfStatementContext.from(ctx), this.translationContext, this::visitExpr, this::visitStatement);
    }

    @Override
    public SwitchStatement visitSwitchStatement(XQueryParser.SwitchStatementContext ctx) {
        return ControlStatementTranslation.switchStatement(
                SwitchStatementContext.from(ctx),
                this.translationContext,
                this::visitExpr,
                this::visitExprSingle,
                this::visitStatement);
    }

    @Override
    public TryCatchStatement visitTryCatchStatement(XQueryParser.TryCatchStatementContext ctx) {
        return ControlStatementTranslation.tryCatchStatement(
                TryCatchStatementContext.from(ctx),
                this.translationContext,
                this::visitBlockStatement,
                this::parseEqName);
    }

    @Override
    public TypeSwitchStatement visitTypeSwitchStatement(XQueryParser.TypeSwitchStatementContext ctx) {
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
    public Statement visitVarDeclStatement(XQueryParser.VarDeclStatementContext ctx) {
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
    public Expression visitPathExpr(XQueryParser.PathExprContext ctx) {
        if (ctx.singleslash != null) {
            return visitSingleSlash(ctx, ctx.singleslash);
        } else if (ctx.doubleslash != null) {
            return visitDoubleSlash(ctx, ctx.doubleslash);
        } else if (ctx.relative != null) {
            return visitRelativeWithoutSlash(ctx.relative);
        }
        return visitSingleSlashNoStepExpr(ctx);
    }

    private Expression visitSingleSlashNoStepExpr(XQueryParser.PathExprContext ctx) {
        // Case: No StepExpr, only dash
        return new PathRootExpression(createMetadataFromContext(ctx));
    }

    private Expression visitRelativeWithoutSlash(XQueryParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1
                && relativeContext.stepExpr(0).postfixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostfixExpr(relativeContext.stepExpr(0).postfixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Expression visitDoubleSlash(
            XQueryParser.PathExprContext pathContext, XQueryParser.RelativePathExprContext doubleSlashContext) {
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
            XQueryParser.PathExprContext pathContext, XQueryParser.RelativePathExprContext singleSlashContext) {
        Token leadingSlash = pathContext.getStart();
        PathRootExpression functionCallExpression =
                new PathRootExpression(createMetadataFromRange(leadingSlash, leadingSlash));
        return getSlashes(singleSlashContext, functionCallExpression, leadingSlash);
    }

    /**
     * This method takes a leftMost expression and a path and returns a nested tree of slash expressions which
     * correspond to the steps in the path applied to the leftMost expression
     */
    private Expression getSlashes(XQueryParser.RelativePathExprContext relativePathExprContext, Expression leftMost) {
        return getSlashes(relativePathExprContext, leftMost, relativePathExprContext.getStart());
    }

    private Expression getSlashes(
            XQueryParser.RelativePathExprContext relativePathExprContext, Expression leftMost, Token expressionStart) {
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
    public Expression visitStepExpr(XQueryParser.StepExprContext ctx) {
        if (ctx.postfixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (XQueryParser.PredicateContext predicateContext :
                    ctx.axisStep().predicateList().predicate()) {
                Expression predicate = this.visitPredicate(predicateContext);
                stepExpr = new FilterExpression(
                        stepExpr, predicate, createMetadataFromRange(ctx.getStart(), predicateContext.getStop()));
            }
            return stepExpr;
        }
        return this.visitPostfixExpr(ctx.postfixExpr());
    }

    private StepExpr getStep(XQueryParser.AxisStepContext ctx) {
        if (ctx.forwardStep() == null) {
            return getReverseStep(ctx.reverseStep());
        }
        return getForwardStep(ctx.forwardStep());
    }

    private StepExpr getForwardStep(XQueryParser.ForwardStepContext ctx) {
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

    private StepExpr getReverseStep(XQueryParser.ReverseStepContext ctx) {
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
            XQueryParser.NodeTestContext nodeTestContext, boolean unprefixedUsesDefaultElementNamespace) {
        return new NodeTestVisitor(unprefixedUsesDefaultElementNamespace).visit(nodeTestContext);
    }

    private class NodeTestVisitor extends XQueryParserBaseVisitor<NodeTest> {
        private final boolean unprefixedUsesDefaultElementNamespace;

        NodeTestVisitor(boolean unprefixedUsesDefaultElementNamespace) {
            this.unprefixedUsesDefaultElementNamespace = unprefixedUsesDefaultElementNamespace;
        }

        @Override
        public NodeTest visitNodeTest(XQueryParser.NodeTestContext ctx) {
            if (ctx.nameTest() != null) {
                return visitNameTest(ctx.nameTest());
            }
            if (ctx.kindTest() != null) {
                return visitKindTest(ctx.kindTest());
            }
            throw new ParsingException("Invalid node test", createMetadataFromContext(ctx));
        }

        @Override
        public NodeTest visitNameTest(XQueryParser.NameTestContext ctx) {
            return XmlNodeTestTranslation.nameTest(
                    NameTestContext.from(ctx),
                    this.unprefixedUsesDefaultElementNamespace,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitKindTest(XQueryParser.KindTestContext ctx) {
            NodeTest result = visit(ctx.getChild(0));
            if (result == null) {
                throw new UnsupportedFeatureException(
                        "Unsupported kind test: " + ctx.getText(), createMetadataFromContext(ctx));
            }
            return result;
        }

        @Override
        public NodeTest visitDocumentTest(XQueryParser.DocumentTestContext ctx) {
            return XmlNodeTestTranslation.documentTest(
                    DocumentTestContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitElementTest(XQueryParser.ElementTestContext ctx) {
            return XmlNodeTestTranslation.elementTest(
                    ElementTestContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitAttributeTest(XQueryParser.AttributeTestContext ctx) {
            return XmlNodeTestTranslation.attributeTest(
                    AttributeTestContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitSchemaElementTest(XQueryParser.SchemaElementTestContext ctx) {
            return XmlNodeTestTranslation.schemaElementTest(
                    SchemaElementTestContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitSchemaAttributeTest(XQueryParser.SchemaAttributeTestContext ctx) {
            return XmlNodeTestTranslation.schemaAttributeTest(
                    SchemaAttributeTestContext.from(ctx),
                    XQueryTranslationVisitor.this.translationContext,
                    XQueryTranslationVisitor.this::parseEqName);
        }

        @Override
        public NodeTest visitPiTest(XQueryParser.PiTestContext ctx) {
            return XmlNodeTestTranslation.piTest(
                    PiTestContext.from(ctx), XQueryTranslationVisitor.this::processStringLiteral);
        }

        @Override
        public NodeTest visitCommentTest(XQueryParser.CommentTestContext ctx) {
            return XmlNodeTestTranslation.commentTest();
        }

        @Override
        public NodeTest visitTextTest(XQueryParser.TextTestContext ctx) {
            return XmlNodeTestTranslation.textTest();
        }

        @Override
        public NodeTest visitNamespaceNodeTest(XQueryParser.NamespaceNodeTestContext ctx) {
            return XmlNodeTestTranslation.namespaceNodeTest();
        }

        @Override
        public NodeTest visitAnyKindTest(XQueryParser.AnyKindTestContext ctx) {
            return XmlNodeTestTranslation.anyKindTest();
        }
    }

    // end region

    private String processURILiteral(UriLiteralContext ctx) {
        // According to XQuery 3.1 spec, URI literals (which are string literals) must expand
        // predefined entity references and character references
        return processStringLiteral(ctx.stringLiteral());
    }

    private String resolveCollationUri(UriLiteralContext ctx) {
        String uriString = processURILiteral(ctx);
        URI uri = URILiteralUtils.resolve(
                this.translationContext.moduleContext().getStaticBaseURI(), uriString, createMetadataFromContext(ctx));
        return uri.toString();
    }

    private List<Annotation> processAnnotations(XQueryParser.AnnotationsContext annotations) {
        return processAnnotations(annotations.annotation());
    }

    private List<Annotation> processAnnotations(List<XQueryParser.AnnotationContext> annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (XQueryParser.AnnotationContext annotationContext : annotations) {
            XQueryParser.EqNameContext eqNameContext = annotationContext.eqName();
            Name name = parseEqName(eqNameContext, NameRole.ANNOTATION);
            Annotation.validateAnnotationName(name, createMetadataFromContext(annotationContext));
            List<Expression> literals = null;
            if (!annotationContext.literal().isEmpty()) {
                literals = new ArrayList<>();
                for (XQueryParser.LiteralContext literalContext : annotationContext.literal()) {
                    literals.add(this.visitLiteral(literalContext));
                }
            }
            parsedAnnotations.add(new Annotation(name, literals));
        }

        return parsedAnnotations;
    }
}
