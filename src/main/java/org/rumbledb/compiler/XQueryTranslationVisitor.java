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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.compiler;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.OptionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.NamespaceDeclaration;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.MapConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
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
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.typing.ValidateTypeExpression;
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
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.parser.xquery.XQueryParserBaseVisitor;
import org.rumbledb.parser.xquery.XQueryParser;
import org.rumbledb.parser.xquery.XQueryParser.DefaultCollationDeclContext;
import org.rumbledb.parser.xquery.XQueryParser.EmptyOrderDeclContext;
import org.rumbledb.parser.xquery.XQueryParser.SetterContext;
import org.rumbledb.parser.xquery.XQueryParser.UriLiteralContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.ItemTypeReference;
import org.rumbledb.types.SequenceType;

import static org.rumbledb.types.SequenceType.createSequenceType;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 *
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 */
public class XQueryTranslationVisitor extends XQueryParserBaseVisitor<Node> {

    private StaticContext moduleContext;
    private RumbleRuntimeConfiguration configuration;
    private CompilationConfiguration compilationConfiguration;
    private boolean isMainModule;
    private String libraryModuleNamespace;
    private String code;
    private ArrayDeque<Map<String, String>> dirElemNamespaceFrames;
    private final CommonTokenStream xQueryTokenStream;

    public XQueryTranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            CompilationConfiguration compilationConfiguration,
            String code,
            CommonTokenStream xQueryTokenStream
    ) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindDefaultNamespaces();
        this.compilationConfiguration = compilationConfiguration;
        this.configuration = compilationConfiguration.runtimeConfiguration();
        this.isMainModule = isMainModule;
        this.code = code;
        this.dirElemNamespaceFrames = new ArrayDeque<>();
        this.xQueryTokenStream = xQueryTokenStream;

        if (this.configuration.getQueryLanguage().equals("xquery10")) {
            this.moduleContext.setQueryLanguage("xquery10");
        } else if (this.configuration.getQueryLanguage().equals("xquery30")) {
            this.moduleContext.setQueryLanguage("xquery30");
        } else if (this.configuration.getQueryLanguage().equals("xquery31")) {
            this.moduleContext.setQueryLanguage("xquery31");
        } else if (this.configuration.getQueryLanguage().equals("xquery40")) {
            this.moduleContext.setQueryLanguage("xquery40");
        }
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(XQueryParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty()) {
            String version = processStringLiteral(ctx.vers).trim();
            if (version.equals("1.0")) {
                this.moduleContext.setQueryLanguage("xquery10");
            } else if (version.equals("3.0")) {
                this.moduleContext.setQueryLanguage("xquery31");
            } else if (version.equals("3.1")) {
                this.moduleContext.setQueryLanguage("xquery31");
            } else if (version.equals("4.0")) {
                this.moduleContext.setQueryLanguage("xquery40");
            } else {
                throw new JsoniqVersionException(createMetadataFromContext(ctx));
            }
        }
        if (this.isMainModule) {
            if (ctx.mainModule() != null) {
                return this.visitMainModule(ctx.mainModule().get(0));
            }
            throw new ParsingException(
                    "Main module expected, but library module found.",
                    createMetadataFromContext(ctx)
            );
        } else {
            if (ctx.libraryModule() != null) {
                return this.visitLibraryModule(ctx.libraryModule());
            }
            throw new ParsingException(
                    "Library module expected, but main module found.",
                    createMetadataFromContext(ctx)
            );
        }
    }

    @Override
    public Node visitMainModule(XQueryParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        // We override with a context item declaration if not present already.
        Program program = (Program) this.visitProgram(ctx.program());
        if (!prolog.hasContextItemDeclaration()) {
            if (
                this.configuration.readFromStandardInput(Name.CONTEXT_ITEM)
                    || this.configuration.getUnparsedExternalVariableValue(Name.CONTEXT_ITEM) != null
            ) {
                System.err.println("[WARNING] Adding context item declaration.");
                prolog.addDeclaration(
                    new VariableDeclaration(
                            Name.CONTEXT_ITEM,
                            true,
                            SequenceType.createSequenceType("item"),
                            null,
                            null,
                            createMetadataFromContext(ctx)
                    )
                );
            }
        }
        for (Name externalVariable : this.configuration.getExternalVariablesReadFromDataFrames()) {
            boolean isAlreadyDeclared = false;
            for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
                if (declaration.getVariableName().equals(externalVariable)) {
                    isAlreadyDeclared = true;
                    continue;
                }
            }
            if (isAlreadyDeclared) {
                continue;
            }
            Dataset<Row> dataFrame = this.configuration.getExternalVariableValueReadFromDataFrame(externalVariable);
            ItemType itemType = ItemTypeFactory.createItemType(dataFrame.schema());
            prolog.addDeclaration(
                new VariableDeclaration(
                        externalVariable,
                        true,
                        new SequenceType(
                                itemType,
                                SequenceType.Arity.ZeroOrMore
                        ),
                        null,
                        null,
                        createMetadataFromContext(ctx)
                )
            );
        }
        for (Name externalVariable : this.configuration.getExternalVariablesReadFromListsOfItems()) {
            boolean isAlreadyDeclared = false;
            for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
                if (declaration.getVariableName().equals(externalVariable)) {
                    isAlreadyDeclared = true;
                    continue;
                }
            }
            if (isAlreadyDeclared) {
                continue;
            }
            prolog.addDeclaration(
                new VariableDeclaration(
                        externalVariable,
                        true,
                        SequenceType.createSequenceType("item*"),
                        null,
                        null,
                        createMetadataFromContext(ctx)
                )
            );
        }

        MainModule module = new MainModule(prolog, program, createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

    // region program
    @Override
    public Node visitProgram(XQueryParser.ProgramContext ctx) {
        StatementsAndOptionalExpr statementsAndOptionalExpr = (StatementsAndOptionalExpr) this
            .visitStatementsAndOptionalExpr(ctx.statementsAndOptionalExpr());
        return new Program(statementsAndOptionalExpr, createMetadataFromContext(ctx));
    }

    // end region

    @Override
    public Node visitLibraryModule(XQueryParser.LibraryModuleContext ctx) {
        String prefix = ctx.ncName().getText();
        String namespace = processURILiteral(ctx.uriLiteral());
        if (namespace.equals("")) {
            throw new EmptyModuleURIException("Module URI is empty.", createMetadataFromContext(ctx));
        }
        URI resolvedURI = URILiteralUtils.resolve(
            this.moduleContext.getStaticBaseURI(),
            namespace,
            createMetadataFromContext(ctx)
        );
        this.libraryModuleNamespace = resolvedURI.toString();
        bindNamespace(
            prefix,
            resolvedURI.toString(),
            createMetadataFromContext(ctx)
        );

        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        LibraryModule module = new LibraryModule(prolog, resolvedURI.toString(), createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

    @Override
    public Node visitProlog(XQueryParser.PrologContext ctx) {
        List<LibraryModule> libraryModules = new ArrayList<>();
        List<OptionDeclaration> optionDeclarations = new ArrayList<>();
        Set<String> namespaces = new HashSet<>();
        PrologPhase1Flags phase1 = new PrologPhase1Flags();
        for (int ci = 0; ci < ctx.getChildCount(); ci++) {
            ParseTree child = ctx.getChild(ci);
            if (child instanceof TerminalNode) {
                continue;
            }
            if (child instanceof XQueryParser.AnnotatedDeclContext) {
                break;
            }
            if (child instanceof XQueryParser.DefaultNamespaceDeclContext defaultNamespaceDeclContext) {
                processDefaultNamespaceDecl(defaultNamespaceDeclContext, phase1);
            } else if (child instanceof XQueryParser.NamespaceDeclContext namespaceDeclContext) {
                processNamespaceDecl(namespaceDeclContext);
            } else if (child instanceof SetterContext setterContext) {
                processPrologPhase1Setter(setterContext, phase1);
            } else if (child instanceof XQueryParser.SchemaImportContext) {
                // Not supported yet; previously skipped as well.
            } else if (child instanceof XQueryParser.ModuleImportContext namespace) {
                LibraryModule libraryModule = this.processModuleImport(namespace);
                libraryModules.add(libraryModule);
                if (namespaces.contains(libraryModule.getNamespace())) {
                    throw new DuplicateModuleTargetNamespaceException(
                            "Duplicate module target namespace: " + libraryModule.getNamespace(),
                            createMetadataFromContext(namespace)
                    );
                }
                namespaces.add(libraryModule.getNamespace());
            }
        }

        // parse variables and function
        List<VariableDeclaration> globalVariables = new ArrayList<>();
        List<FunctionDeclaration> functionDeclarations = new ArrayList<>();
        List<TypeDeclaration> typeDeclarations = new ArrayList<>();
        for (XQueryParser.AnnotatedDeclContext annotatedDeclaration : ctx.annotatedDecl()) {
            if (annotatedDeclaration.varDecl() != null) {
                VariableDeclaration variableDeclaration = (VariableDeclaration) this.visitVarDecl(
                    annotatedDeclaration.varDecl()
                );
                if (!this.isMainModule) {
                    String variableNamespace = variableDeclaration.getVariableName().getNamespace();
                    if (variableNamespace == null || !variableNamespace.equals(this.libraryModuleNamespace)) {
                        throw new NamespaceDoesNotMatchModuleException(
                                "Variable "
                                    + variableDeclaration.getVariableName().getLocalName()
                                    + ": namespace "
                                    + variableNamespace
                                    + " must match module namespace "
                                    + this.libraryModuleNamespace,
                                createMetadataFromContext(annotatedDeclaration.varDecl())
                        );
                    }
                }
                globalVariables.add(variableDeclaration);
            } else if (annotatedDeclaration.contextItemDecl() != null) {
                VariableDeclaration variableDeclaration = (VariableDeclaration) this.visitContextItemDecl(
                    annotatedDeclaration.contextItemDecl()
                );
                globalVariables.add(variableDeclaration);
            } else if (annotatedDeclaration.functionDecl() != null) {
                InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) this.visitFunctionDecl(
                    annotatedDeclaration.functionDecl()
                );
                if (!this.isMainModule) {
                    String functionNamespace = inlineFunctionExpression.getName().getNamespace();
                    if (functionNamespace == null || !functionNamespace.equals(this.libraryModuleNamespace)) {
                        throw new NamespaceDoesNotMatchModuleException(
                                "Function "
                                    + inlineFunctionExpression.getName().getLocalName()
                                    + ": namespace "
                                    + functionNamespace
                                    + " must match module namespace "
                                    + this.libraryModuleNamespace,
                                createMetadataFromContext(annotatedDeclaration.functionDecl())
                        );
                    }
                }
                functionDeclarations.add(
                    new FunctionDeclaration(
                            inlineFunctionExpression,
                            createMetadataFromContext(annotatedDeclaration.functionDecl())
                    )
                );
            } else if (annotatedDeclaration.optionDecl() != null) {
                optionDeclarations.add((OptionDeclaration) this.visitOptionDecl(annotatedDeclaration.optionDecl()));
            }
        }
        for (XQueryParser.ModuleImportContext module : ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        Prolog prolog = new Prolog(
                globalVariables,
                functionDeclarations,
                typeDeclarations,
                createMetadataFromContext(ctx)
        );
        for (LibraryModule libraryModule : libraryModules) {
            prolog.addImportedModule(libraryModule);
        }
        for (OptionDeclaration optionDeclaration : optionDeclarations) {
            prolog.addDeclaration(optionDeclaration);
        }
        return prolog;
    }

    @Override
    public Node visitOptionDecl(XQueryParser.OptionDeclContext ctx) {
        Name name = parseEqName(ctx.name, false, false, false, false);
        String value = processStringLiteral(ctx.value);
        return new OptionDeclaration(name, value, createMetadataFromContext(ctx));
    }

    private static final class PrologPhase1Flags {
        boolean emptyOrderSet;
        boolean defaultCollationSet;
        boolean baseURISet;
        boolean defaultFunctionNamespaceDeclared;
    }

    private void processPrologPhase1Setter(SetterContext setterContext, PrologPhase1Flags flags) {
        if (setterContext.emptyOrderDecl() != null) {
            if (flags.emptyOrderSet) {
                throw new MoreThanOneEmptyOrderDeclarationException(
                        "The empty order was already set.",
                        createMetadataFromContext(setterContext.emptyOrderDecl())
                );
            }
            processEmptySequenceOrder(setterContext.emptyOrderDecl());
            flags.emptyOrderSet = true;
            return;
        }
        if (setterContext.decimalFormatDecl() != null) {
            processDecimalFormatDeclaration(
                setterContext.decimalFormatDecl(),
                createMetadataFromContext(setterContext.decimalFormatDecl())
            );
            return;
        }
        if (setterContext.defaultCollationDecl() != null) {
            if (flags.defaultCollationSet) {
                throw new DefaultCollationException(
                        "The default collation was already set.",
                        createMetadataFromContext(setterContext.defaultCollationDecl())
                );
            }
            processDefaultCollation(setterContext.defaultCollationDecl());
            flags.defaultCollationSet = true;
            return;
        }
        if (setterContext.baseURIDecl() != null) {
            if (flags.baseURISet) {
                throw new MultipleBaseURIException(
                        "The base-uri was already set.",
                        createMetadataFromContext(setterContext.baseURIDecl())
                );
            }
            String uriString = processURILiteral(setterContext.baseURIDecl().uriLiteral());
            URI uri = URILiteralUtils.resolve(
                this.moduleContext.getStaticBaseURI(),
                uriString,
                createMetadataFromContext(setterContext.baseURIDecl())
            );
            this.moduleContext.setStaticBaseUri(uri);
            flags.baseURISet = true;
            return;
        }
        throw new UnsupportedFeatureException(
                "Setters are not supported yet, except for empty sequence ordering and default collations.",
                createMetadataFromContext(setterContext)
        );
    }

    private void processDefaultNamespaceDecl(
            XQueryParser.DefaultNamespaceDeclContext ctx,
            PrologPhase1Flags flags
    ) {
        String uri = processStringLiteral(ctx.stringLiteral());
        int declType = ctx.type.getType();
        if (declType == XQueryParser.KW_ELEMENT) {
            bindNamespace("", uri, createMetadataFromContext(ctx));
        } else if (declType == XQueryParser.KW_FUNCTION) {
            if (flags.defaultFunctionNamespaceDeclared) {
                throw new SemanticException(
                        "The default function namespace has already been declared.",
                        createMetadataFromContext(ctx)
                );
            }
            this.moduleContext.setDefaultFunctionNamespaceUri(uri);
            flags.defaultFunctionNamespaceDeclared = true;
        } else {
            throw new OurBadException("Unexpected default namespace declaration kind.");
        }
    }

    private String processStringLiteral(XQueryParser.StringLiteralContext ctx) {
        return parseStringLiteral(this.xQueryTokenStream.getText(ctx.getSourceInterval()));
    }

    private Name nameForUnprefixedFunction(String localName) {
        String uri = this.moduleContext.getDefaultFunctionNamespaceUri();
        if (uri != null) {
            return new Name(uri, "", localName);
        }
        return Name.createVariableInDefaultFunctionNamespace(localName);
    }

    public Name parseFunctionName(XQueryParser.FunctionNameContext ctx) {
        if (ctx.URIQualifiedName() != null) {
            return URIQualifiedNameParser.parse(
                ctx.URIQualifiedName().getText(),
                createMetadataFromContext(ctx)
            );
        } else if (ctx.FullQName() != null) {
            // Handle FullQName by parsing its text content
            String fullQNameText = ctx.FullQName().getText();
            int colonIndex = fullQNameText.indexOf(':');
            if (colonIndex == -1) {
                throw new ParsingException(
                        "Invalid FullQName format: " + fullQNameText,
                        createMetadataFromContext(ctx)
                );
            }
            String prefix = fullQNameText.substring(0, colonIndex);
            String localName = fullQNameText.substring(colonIndex + 1);
            String namespace = resolvePrefixForDirConstructor(prefix);
            if (namespace != null) {
                return new Name(namespace, prefix, localName);
            }
            throw new PrefixCannotBeExpandedException(
                    "Cannot expand prefix " + prefix,
                    createMetadataFromContext(ctx)
            );
        } else if (ctx.keywordOKForFunction() != null) {
            // if the rule matches a keyword, the prefix is not defined
            return nameForUnprefixedFunction(ctx.keywordOKForFunction().getText());
        } else {
            // Handle NCName case
            String localName = ctx.NCName().getText();
            return nameForUnprefixedFunction(localName);
        }
    }

    /**
     * Parse an EQName. Delegates to {@link #parseName} for the {@code qname} branch; URI-qualified names use
     * {@link URIQualifiedNameParser}.
     */
    public Name parseEqName(
            XQueryParser.EqNameContext ctx,
            boolean isFunction,
            boolean isType,
            boolean isAnnotation,
            boolean isElementConstructor
    ) {
        if (ctx.qname() != null) {
            return parseName(ctx.qname(), isFunction, isType, isAnnotation, isElementConstructor);
        }
        return URIQualifiedNameParser.parse(ctx.URIQualifiedName().getText(), createMetadataFromContext(ctx));
    }

    /**
     * Resolves a QName while parsing an XQuery construct.
     * <p>
     * <strong>Prefix resolution (any prefixed QName):</strong> the prefix is always resolved with
     * {@link #resolvePrefixForDirConstructor}, which consults {@link #dirElemNamespaceFrames} from innermost to
     * outermost, then falls back to {@link org.rumbledb.context.StaticContext#resolveNamespace(String)} on the module
     * context. So prefixed names can use namespace bindings established by {@code xmlns} / {@code xmlns:prefix} on an
     * enclosing direct element constructor, in source order, as well as prolog and imported bindings.
     * <p>
     * <strong>Unprefixed names:</strong> which default applies depends on the role flags (mutually exclusive in
     * typical use). These flags do not turn off prefix resolution for prefixed QNames; they only select behavior when
     * there is no prefix.
     * <ul>
     * <li>{@code isFunction}: unprefixed function name (module default function namespace; not read from
     * {@code dirElemNamespaceFrames}).</li>
     * <li>{@code isType}: unprefixed type name; uses the in-scope default element/type namespace from
     * {@link #resolvePrefixForDirConstructor(String)} with prefix {@code ""} (constructor {@code xmlns=""} and/or
     * prolog defaults) when bound; otherwise {@link Name#createVariableInDefaultTypeNamespace}.</li>
     * <li>{@code isAnnotation}: unprefixed annotation EQName; same default-namespace rule as types when a default is
     * bound; otherwise {@link Name#createNameInDefaultXQueryAnnotationsNamespace}.</li>
     * <li>{@code isElementConstructor}: unprefixed name in a direct element start tag or static computed element name;
     * uses default element namespace from {@link #resolvePrefixForDirConstructor(String)} with prefix {@code ""} if
     * bound, otherwise no namespace ({@link Name#createVariableInNoNamespace}).</li>
     * <li>Otherwise: no namespace ({@link Name#createVariableInNoNamespace}), e.g. variables.</li>
     * </ul>
     * The {@code isElementConstructor} parameter remains necessary so unprefixed <em>element tag</em> names are not
     * treated like unprefixed type names or plain NCNames: default-namespace and fallback rules differ (see branches
     * above).
     */
    public Name parseName(
            XQueryParser.QnameContext ctx,
            boolean isFunction,
            boolean isType,
            boolean isAnnotation,
            boolean isElementConstructor
    ) {
        String localName = null;
        String prefix = null;
        Name name = null;

        if (ctx.FullQName() != null) {
            // Handle FullQName by parsing its text content
            String fullQNameText = ctx.FullQName().getText();
            int colonIndex = fullQNameText.indexOf(':');
            if (colonIndex == -1) {
                throw new ParsingException(
                        "Invalid FullQName format: " + fullQNameText,
                        createMetadataFromContext(ctx)
                );
            }
            prefix = fullQNameText.substring(0, colonIndex);
            localName = fullQNameText.substring(colonIndex + 1);
        } else {
            // Handle the labeled ncName case
            localName = ctx.local_name.getText();
            if (ctx.ns != null) {
                prefix = ctx.ns.getText();
            }
        }

        if (prefix == null) {
            if (isFunction) {
                name = nameForUnprefixedFunction(localName);
            } else if (isType) {
                String defaultTypeNs = resolvePrefixForDirConstructor("");
                if (defaultTypeNs != null) {
                    name = new Name(defaultTypeNs, "", localName);
                } else {
                    name = Name.createVariableInDefaultTypeNamespace(localName);
                }
            } else if (isAnnotation) {
                String defaultAnnotationNs = resolvePrefixForDirConstructor("");
                if (defaultAnnotationNs != null) {
                    name = new Name(defaultAnnotationNs, "", localName);
                } else {
                    name = Name.createNameInDefaultXQueryAnnotationsNamespace(localName);
                }
            } else if (isElementConstructor) {
                String defaultElementNs = resolvePrefixForDirConstructor("");
                if (defaultElementNs != null) {
                    name = new Name(defaultElementNs, "", localName);
                } else {
                    name = Name.createVariableInNoNamespace(localName);
                }
            } else {
                name = Name.createVariableInNoNamespace(localName);
            }
        } else {
            String namespace = resolvePrefixForDirConstructor(prefix);
            if (namespace != null) {
                name = new Name(namespace, prefix, localName);
            }
        }
        if (name != null) {
            return name;
        }
        throw new PrefixCannotBeExpandedException(
                "Cannot expand prefix " + prefix,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitFunctionDecl(XQueryParser.FunctionDeclContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        Name name = parseFunctionName(ctx.functionName());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false, false, false);
                paramType = createSequenceType("item*");
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            name,
                            paramName,
                            createMetadataFromContext(param)
                    );
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

        StatementsAndOptionalExpr funcBody = (StatementsAndOptionalExpr) this
            .visitStatementsAndOptionalExpr(ctx.fn_body);

        boolean isExternal = ctx.is_external != null;

        return new InlineFunctionExpression(
                annotations,
                name,
                fnParams,
                fnReturnType,
                funcBody,
                isExternal,
                createMetadataFromContext(ctx)
        );
    }

    // endregion

    // region expr
    @Override
    public Node visitExpr(XQueryParser.ExprContext ctx) {
        List<Expression> expressions = new ArrayList<>();
        for (XQueryParser.ExprSingleContext expr : ctx.exprSingle()) {
            expressions.add((Expression) this.visitExprSingle(expr));
        }
        if (expressions.size() == 1) {
            return expressions.get(0);
        }
        return new CommaExpression(expressions, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitExprSingle(XQueryParser.ExprSingleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof XQueryParser.ExprSimpleContext exprSimpleContext) {
            return this.visitExprSimple(exprSimpleContext);
        }
        if (content instanceof XQueryParser.FlworExprContext flworExprContext) {
            return this.visitFlworExpr(flworExprContext);
        }
        if (content instanceof XQueryParser.IfExprContext ifExprContext) {
            return this.visitIfExpr(ifExprContext);
        }
        if (content instanceof XQueryParser.SwitchExprContext switchExprContext) {
            return this.visitSwitchExpr(switchExprContext);
        }
        if (content instanceof XQueryParser.TypeswitchExprContext typeswitchExprContext) {
            return this.visitTypeswitchExpr(typeswitchExprContext);
        }
        if (content instanceof XQueryParser.TryCatchExprContext tryCatchExprContext) {
            return this.visitTryCatchExpr(tryCatchExprContext);
        }
        throw new OurBadException("Unrecognized ExprSingle:" + content.getClass().getName());
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Node visitExprSimple(XQueryParser.ExprSimpleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof XQueryParser.OrExprContext orExprContext) {
            return this.visitOrExpr(orExprContext);
        }
        if (content instanceof XQueryParser.QuantifiedExprContext quantifiedExprContext) {
            return this.visitQuantifiedExpr(quantifiedExprContext);
        }
        // TODO: do these need to be implemented in the xquery translator?
        // if (content instanceof XQueryParser.DeleteExprContext) {
        // return this.visitDeleteExpr((XQueryParser.DeleteExprContext) content);
        // }
        // if (content instanceof XQueryParser.InsertExprContext) {
        // return this.visitInsertExpr((XQueryParser.InsertExprContext) content);
        // }
        // if (content instanceof XQueryParser.ReplaceExprContext) {
        // return this.visitReplaceExpr((XQueryParser.ReplaceExprContext) content);
        // }
        // if (content instanceof XQueryParser.RenameExprContext) {
        // return this.visitRenameExpr((XQueryParser.RenameExprContext) content);
        // }
        // if (content instanceof XQueryParser.AppendExprContext) {
        // return this.visitAppendExpr((XQueryParser.AppendExprContext) content);
        // }
        // if (content instanceof XQueryParser.TransformExprContext) {
        // return this.visitTransformExpr((XQueryParser.TransformExprContext) content);
        // }
        if (content instanceof XQueryParser.PathExprContext pathExprContext) {
            return this.visitPathExpr(pathExprContext);
        }
        throw new OurBadException("Unrecognized ExprSimple.");
    }

    // endregion

    // region EnclosedExpression
    @Override
    public Node visitEnclosedExpression(XQueryParser.EnclosedExpressionContext ctx) {
        // empty expression
        if (ctx.expr() == null) {
            return new CommaExpression(createMetadataFromContext(ctx));
        }
        return this.visitExpr(ctx.expr());
    }
    // endregion

    // region Flowr
    @Override
    public Node visitFlworExpr(XQueryParser.FlworExprContext ctx) {
        Clause clause;
        // check the start clause, for or let
        if (ctx.start_window != null) {
            clause = (Clause) this.visitWindowClause(ctx.start_window);
        } else if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }

        Clause previousFLWORClause = clause.getLastClause();

        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof XQueryParser.ForClauseContext forClauseContext) {
                clause = (Clause) this.visitForClause(forClauseContext);
            } else if (child instanceof XQueryParser.LetClauseContext letClauseContext) {
                clause = (Clause) this.visitLetClause(letClauseContext);
            } else if (child instanceof XQueryParser.WindowClauseContext windowClauseContext) {
                clause = (Clause) this.visitWindowClause(windowClauseContext);
            } else if (child instanceof XQueryParser.WhereClauseContext whereClauseContext) {
                clause = (Clause) this.visitWhereClause(whereClauseContext);
            } else if (child instanceof XQueryParser.GroupByClauseContext groupByClauseContext) {
                clause = (Clause) this.visitGroupByClause(groupByClauseContext);
            } else if (child instanceof XQueryParser.OrderByClauseContext orderByClauseContext) {
                clause = (Clause) this.visitOrderByClause(orderByClauseContext);
            } else if (child instanceof XQueryParser.CountClauseContext countClauseContext) {
                clause = (Clause) this.visitCountClause(countClauseContext);
            } else {
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet",
                        createMetadataFromContext(ctx)
                );
            }

            previousFLWORClause.chainWith(clause.getFirstClause());
            previousFLWORClause = clause.getLastClause();
        }

        Expression returnExpr = (Expression) this.visitExprSingle(ctx.return_expr);
        ReturnClause returnClause = new ReturnClause(
                returnExpr,
                returnExpr.getMetadata()
        );
        previousFLWORClause.chainWith(returnClause);

        returnClause = returnClause.detachInitialLetClauses();

        return new FlworExpression(
                returnClause,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitForClause(XQueryParser.ForClauseContext ctx) {
        ForClause clause = null;
        for (XQueryParser.ForVarContext var : ctx.vars) {
            ForClause newClause = (ForClause) this.visitForVar(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitForVar(XQueryParser.ForVarContext ctx) {
        SequenceType seq = null;
        boolean emptyFlag;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.var_ref)).getVariableName();
        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        }
        emptyFlag = (ctx.flag != null);
        Name atVar = null;
        if (ctx.at != null) {
            atVar = ((VariableReferenceExpression) this.visitVarRef(ctx.at)).getVariableName();
            if (atVar.equals(var)) {
                throw new PositionalVariableNameSameAsForVariableException(
                        "Positional variable " + var + " cannot have the same name as the main for variable.",
                        createMetadataFromContext(ctx.at)
                );
            }
        }
        Expression expr = (Expression) this.visitExprSingle(ctx.ex);
        // If the sequenceType is specified, we have to "extend" its arity to *
        // because TreatIterator is wrapping the whole assignment expression,
        // meaning there is not one TreatIterator for each variable we loop over.
        if (seq != null) {
            SequenceType expressionType = new SequenceType(
                    seq.getItemType(),
                    SequenceType.Arity.ZeroOrMore
            );
            expr = new TreatExpression(expr, expressionType, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }


        return new ForClause(var, emptyFlag, seq, atVar, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitLetClause(XQueryParser.LetClauseContext ctx) {
        LetClause clause = null;
        for (XQueryParser.LetVarContext var : ctx.vars) {
            LetClause newClause = (LetClause) this.visitLetVar(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitLetVar(XQueryParser.LetVarContext ctx) {
        SequenceType seq = null;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.var_ref)).getVariableName();
        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        }

        Expression expr = (Expression) this.visitExprSingle(ctx.ex);
        if (seq != null) {
            expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }

        return new LetClause(var, seq, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitWindowClause(XQueryParser.WindowClauseContext ctx) {
        if (ctx.tumblingWindowClause() != null) {
            return this.visitTumblingWindowClause(ctx.tumblingWindowClause());
        }
        return this.visitSlidingWindowClause(ctx.slidingWindowClause());
    }

    @Override
    public Node visitTumblingWindowClause(XQueryParser.TumblingWindowClauseContext ctx) {
        Name windowVariable = parseEqName(ctx.varName().eqName(), false, false, false, false);
        SequenceType sequenceType = ctx.type == null ? null : this.processSequenceType(ctx.type.sequenceType());
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        WindowClause.WindowCondition start = this.buildWindowStartCondition(ctx.windowStartCondition());
        WindowClause.WindowCondition end = ctx.windowEndCondition() == null
            ? null
            : this.buildWindowEndCondition(ctx.windowEndCondition());
        validateWindowVariables(windowVariable, start, end, createMetadataFromContext(ctx));
        return new WindowClause(
                WindowClause.WindowType.TUMBLING,
                windowVariable,
                sequenceType,
                expression,
                start,
                end,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitSlidingWindowClause(XQueryParser.SlidingWindowClauseContext ctx) {
        Name windowVariable = parseEqName(ctx.varName().eqName(), false, false, false, false);
        SequenceType sequenceType = ctx.type == null ? null : this.processSequenceType(ctx.type.sequenceType());
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        WindowClause.WindowCondition start = this.buildWindowStartCondition(ctx.windowStartCondition());
        WindowClause.WindowCondition end = this.buildWindowEndCondition(ctx.windowEndCondition());
        validateWindowVariables(windowVariable, start, end, createMetadataFromContext(ctx));
        return new WindowClause(
                WindowClause.WindowType.SLIDING,
                windowVariable,
                sequenceType,
                expression,
                start,
                end,
                createMetadataFromContext(ctx)
        );
    }

    private WindowClause.WindowCondition buildWindowStartCondition(XQueryParser.WindowStartConditionContext ctx) {
        return new WindowClause.WindowCondition(
                this.buildWindowVars(ctx.windowVars()),
                (Expression) this.visitExprSingle(ctx.exprSingle()),
                false
        );
    }

    private WindowClause.WindowCondition buildWindowEndCondition(XQueryParser.WindowEndConditionContext ctx) {
        return new WindowClause.WindowCondition(
                this.buildWindowVars(ctx.windowVars()),
                (Expression) this.visitExprSingle(ctx.exprSingle()),
                ctx.KW_ONLY() != null
        );
    }

    private WindowClause.WindowVars buildWindowVars(XQueryParser.WindowVarsContext ctx) {
        Name current = ctx.currentItem == null ? null : parseEqName(ctx.currentItem, false, false, false, false);
        Name position = ctx.positionalVar() == null
            ? null
            : parseEqName(ctx.positionalVar().pvar.eqName(), false, false, false, false);
        Name previous = ctx.previousItem == null ? null : parseEqName(ctx.previousItem, false, false, false, false);
        Name next = ctx.nextItem == null ? null : parseEqName(ctx.nextItem, false, false, false, false);
        return new WindowClause.WindowVars(current, position, previous, next);
    }

    private void validateWindowVariables(
            Name windowVariable,
            WindowClause.WindowCondition start,
            WindowClause.WindowCondition end,
            ExceptionMetadata metadata
    ) {
        List<Name> names = new ArrayList<>();
        names.add(windowVariable);
        names.addAll(start.variables().names());
        if (end != null) {
            names.addAll(end.variables().names());
        }
        if (names.size() != names.stream().distinct().count()) {
            throw new ParsingException(
                    "All variables in a window clause must have distinct names",
                    ErrorCode.DuplicatedVariableNameInWindowCode,
                    metadata
            );
        }
    }

    @Override
    public Node visitGroupByClause(XQueryParser.GroupByClauseContext ctx) {
        List<GroupByVariableDeclaration> vars = new ArrayList<>();
        GroupByVariableDeclaration child;
        for (XQueryParser.GroupByVarContext var : ctx.vars) {
            child = this.processGroupByVar(var);
            vars.add(child);
        }
        return new GroupByClause(vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitOrderByClause(XQueryParser.OrderByClauseContext ctx) {
        boolean stable = false;
        List<OrderByClauseSortingKey> exprs = new ArrayList<>();
        OrderByClauseSortingKey child;
        for (XQueryParser.OrderByExprContext var : ctx.orderByExpr()) {
            child = this.processOrderByExpr(var);
            exprs.add(child);
        }
        if (ctx.stb != null && !ctx.stb.getText().isEmpty()) {
            stable = true;
        }
        return new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
    }

    public OrderByClauseSortingKey processOrderByExpr(XQueryParser.OrderByExprContext ctx) {
        String uri = null;
        if (ctx.uriLiteral() != null) {
            String collation = processURILiteral(ctx.uriLiteral());
            if (!this.moduleContext.isStaticallyKnownCollation(collation)) {
                throw new DefaultCollationException(
                        "Unknown collation: " + collation,
                        createMetadataFromContext(ctx.uriLiteral())
                );
            }
            uri = collation;
        }
        boolean ascending = true;
        if (ctx.desc != null && !ctx.desc.getText().isEmpty()) {
            ascending = false;
        }
        OrderByClauseSortingKey.EMPTY_ORDER empty_order = OrderByClauseSortingKey.EMPTY_ORDER.NONE;
        if (ctx.gr != null && !ctx.gr.getText().isEmpty()) {
            empty_order = OrderByClauseSortingKey.EMPTY_ORDER.GREATEST;
        }
        if (ctx.ls != null && !ctx.ls.getText().isEmpty()) {
            empty_order = OrderByClauseSortingKey.EMPTY_ORDER.LEAST;
        }
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new OrderByClauseSortingKey(
                expression,
                ascending,
                uri,
                empty_order
        );
    }

    public GroupByVariableDeclaration processGroupByVar(XQueryParser.GroupByVarContext ctx) {
        if (ctx.uriLiteral() != null) {
            String collation = processURILiteral(ctx.uriLiteral());
            if (!this.moduleContext.isStaticallyKnownCollation(collation)) {
                throw new DefaultCollationException(
                        "Unknown collation: " + collation,
                        createMetadataFromContext(ctx.uriLiteral())
                );
            }
        }
        SequenceType seq = null;
        Expression expr = null;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.var_ref)).getVariableName();

        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        }

        if (ctx.ex != null) {
            expr = (Expression) this.visitExprSingle(ctx.ex);
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }

        }


        return new GroupByVariableDeclaration(var, seq, expr);
    }

    @Override
    public Node visitWhereClause(XQueryParser.WhereClauseContext ctx) {
        Expression expr = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new WhereClause(expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCountClause(XQueryParser.CountClauseContext ctx) {
        VariableReferenceExpression child = (VariableReferenceExpression) this.visitVarRef(ctx.varRef());
        return new CountClause(child.getVariableName(), createMetadataFromContext(ctx));
    }
    // endregion

    // region operational
    @Override
    public Node visitOrExpr(XQueryParser.OrExprContext ctx) {
        Expression result = (Expression) this.visitAndExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (XQueryParser.AndExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitAndExpr(child);
            result = new OrExpression(
                    result,
                    rightExpression,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitAndExpr(XQueryParser.AndExprContext ctx) {
        Expression result = (Expression) this.visitComparisonExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (XQueryParser.ComparisonExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitComparisonExpr(child);
            result = new AndExpression(
                    result,
                    rightExpression,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitComparisonExpr(XQueryParser.ComparisonExprContext ctx) {
        Expression mainExpression = (Expression) this.visitStringConcatExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.StringConcatExprContext child = ctx.rhs.get(0);
        Expression childExpression = (Expression) this.visitStringConcatExpr(child);

        String operatorSymbol = ctx.op.get(0).getText();

        // Check if node comparison operator
        if (ctx.op.get(0).nodeComp() != null) {
            NodeComparisonExpression.NodeComparisonOperator nodeOp = NodeComparisonExpression.NodeComparisonOperator
                .fromSymbol(operatorSymbol);
            return new NodeComparisonExpression(
                    mainExpression,
                    childExpression,
                    nodeOp,
                    createMetadataFromContext(ctx)
            );
        }

        // else, it's a generic or value comparison
        ComparisonExpression.ComparisonOperator kind = ComparisonExpression.ComparisonOperator.fromSymbol(
            operatorSymbol
        );
        if (kind.isValueComparison() || this.configuration.optimizeGeneralComparisonToValueComparison()) {
            return new ComparisonExpression(
                    mainExpression,
                    childExpression,
                    kind,
                    createMetadataFromContext(ctx)
            );
        }

        Name variableNameLeft = Name.TEMP_VAR1;
        Name variableNameRight = Name.TEMP_VAR2;

        Clause firstClause = new ForClause(
                variableNameLeft,
                false,
                null,
                null,
                mainExpression,
                createMetadataFromContext(ctx)
        );
        Clause secondClause = new ForClause(
                variableNameRight,
                false,
                null,
                null,
                childExpression,
                createMetadataFromContext(ctx)
        );
        firstClause.chainWith(secondClause);
        Expression valueComparison = new ComparisonExpression(
                new VariableReferenceExpression(variableNameLeft, createMetadataFromContext(ctx)),
                new VariableReferenceExpression(variableNameRight, createMetadataFromContext(ctx)),
                kind.getCorrespondingValueComparison(),
                createMetadataFromContext(ctx)
        );
        WhereClause whereClause = new WhereClause(valueComparison, createMetadataFromContext(ctx));
        secondClause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new StringLiteralExpression("", null),
                createMetadataFromContext(ctx)
        );
        whereClause.chainWith(returnClause);
        Expression flworExpression = new FlworExpression(returnClause, createMetadataFromContext(ctx));
        return new FunctionCallExpression(
                Name.createVariableInDefaultFunctionNamespace("exists"),
                Collections.singletonList(flworExpression),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitStringConcatExpr(XQueryParser.StringConcatExprContext ctx) {
        Expression result = (Expression) this.visitRangeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (XQueryParser.RangeExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitRangeExpr(child);
            result = new StringConcatExpression(
                    result,
                    rightExpression,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitRangeExpr(XQueryParser.RangeExprContext ctx) {
        Expression mainExpression = (Expression) this.visitAdditiveExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.AdditiveExprContext child = ctx.rhs.get(0);
        Expression childExpression = (Expression) this.visitAdditiveExpr(child);
        return new RangeExpression(
                mainExpression,
                childExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitAdditiveExpr(XQueryParser.AdditiveExprContext ctx) {
        Expression result = (Expression) this.visitMultiplicativeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            XQueryParser.MultiplicativeExprContext child = ctx.rhs.get(i);
            Expression rightExpression = (Expression) this.visitMultiplicativeExpr(child);
            result = new AdditiveExpression(
                    result,
                    rightExpression,
                    ctx.op.get(i).getText().equals("-"),
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitMultiplicativeExpr(XQueryParser.MultiplicativeExprContext ctx) {
        Expression result = (Expression) this.visitUnionExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            XQueryParser.UnionExprContext child = ctx.rhs.get(i);
            Expression rightExpression = (Expression) this.visitUnionExpr(child);
            result = new MultiplicativeExpression(
                    result,
                    rightExpression,
                    MultiplicativeExpression.MultiplicativeOperator.fromSymbol(ctx.op.get(i).getText()),
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitUnionExpr(XQueryParser.UnionExprContext ctx) {
        Expression result = (Expression) this.visitIntersectExceptExpr(ctx.main_expr);
        for (XQueryParser.IntersectExceptExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitIntersectExceptExpr(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.UNION,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitIntersectExceptExpr(XQueryParser.IntersectExceptExprContext ctx) {
        Expression result = (Expression) this.visitInstanceOfExpr(ctx.main_expr);
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            Expression rightExpression = (Expression) this.visitInstanceOfExpr(ctx.rhs.get(i));
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.fromSymbol(ctx.op.get(i).getText()),
                    createMetadataFromRange(ctx.main_expr.getStart(), ctx.rhs.get(i).getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitSimpleMapExpr(XQueryParser.SimpleMapExprContext ctx) {
        Expression result = (Expression) this.visitPathExpr(ctx.main_expr);
        if (ctx.map_expr == null || ctx.map_expr.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.map_expr.size(); ++i) {
            XQueryParser.PathExprContext child = ctx.map_expr.get(i);
            Expression rightExpression = (Expression) this.visitPathExpr(child);
            result = new SimpleMapExpression(
                    result,
                    rightExpression,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }


    @Override
    public Node visitInstanceOfExpr(XQueryParser.InstanceOfExprContext ctx) {
        Expression mainExpression = (Expression) this.visitIsStaticallyExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new InstanceOfExpression(
                mainExpression,
                sequenceType,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitIsStaticallyExpr(XQueryParser.IsStaticallyExprContext ctx) {
        Expression mainExpression = (Expression) this.visitTreatExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new IsStaticallyExpression(
                mainExpression,
                sequenceType,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitTreatExpr(XQueryParser.TreatExprContext ctx) {
        Expression mainExpression = (Expression) this.visitCastableExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new TreatExpression(
                mainExpression,
                sequenceType,
                ErrorCode.DynamicTypeTreatErrorCode,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCastableExpr(XQueryParser.CastableExprContext ctx) {
        Expression mainExpression = (Expression) this.visitCastExpr(ctx.main_expr);
        if (ctx.single == null || ctx.single.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.SingleTypeContext child = ctx.single;
        SequenceType sequenceType = this.processSingleType(child);
        return new CastableExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCastExpr(XQueryParser.CastExprContext ctx) {
        Expression mainExpression = (Expression) this.visitArrowExpr(ctx.main_expr);
        if (ctx.single == null || ctx.single.isEmpty()) {
            return mainExpression;
        }
        XQueryParser.SingleTypeContext child = ctx.single;
        SequenceType sequenceType = this.processSingleType(child);
        return new CastExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitArrowExpr(XQueryParser.ArrowExprContext ctx) {
        Expression mainExpression = (Expression) this.visitUnaryExpr(ctx.main_expr);
        Expression functionExpression = null;

        for (int i = 0; i < ctx.function.size(); ++i) {
            XQueryParser.ArrowFunctionSpecifierContext functionCallContext = ctx.function.get(i);
            XQueryParser.ArgumentListContext argumentListContext = ctx.arguments.get(i);
            ExceptionMetadata metadata = createMetadataFromRange(
                ctx.main_expr.getStart(),
                argumentListContext.getStop()
            );
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext(argumentListContext));
            if (functionCallContext.eqName() != null) {
                Name name = parseEqName(functionCallContext.eqName(), true, false, false, false);
                mainExpression = processFunctionCall(name, children, metadata);
                continue;
            } else if (functionCallContext.varRef() != null) {
                functionExpression = (Expression) this.visitVarRef(functionCallContext.varRef());
            } else {
                functionExpression = (Expression) this.visitParenthesizedExpr(functionCallContext.parenthesizedExpr());
            }
            mainExpression = new DynamicFunctionCallExpression(
                    functionExpression,
                    children,
                    metadata
            );
        }
        return mainExpression;

    }


    @Override
    public Node visitUnaryExpr(XQueryParser.UnaryExprContext ctx) {
        Expression mainExpression = (Expression) this.visitValueExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        boolean negated = false;
        for (Token t : ctx.op) {
            if (t.getText().contentEquals("-")) {
                negated = !negated;
            }
        }
        return new UnaryExpression(
                mainExpression,
                negated,
                createMetadataFromContext(ctx)
        );
    }


    @Override
    public Node visitValueExpr(XQueryParser.ValueExprContext ctx) {
        if (ctx.simpleMap_expr != null) {
            return this.visitSimpleMapExpr(ctx.simpleMap_expr);
        }
        if (ctx.validate_expr != null) {
            return this.visitValidateExpr(ctx.validate_expr);
        }
        // TODO: extension expression still unsupported
        throw new UnsupportedFeatureException("Extension expression still unsupported", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitValidateExpr(XQueryParser.ValidateExprContext ctx) {
        Expression mainExpr = (Expression) this.visitExpr(ctx.expr());
        SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
        return new ValidateTypeExpression(mainExpr, true, sequenceType, createMetadataFromContext(ctx));
        // TODO: this is not implemented in XQuery. Throw an unsupported feature exception.
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
    // Node lhs = this.visitExprSingle(currentPair.lhs);
    // if (lhs instanceof StepExpr) {
    // throw new ParsingException(
    // "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to
    // JSONiq 1.0 using the --xquery-version CLI option.",
    // createMetadataFromContext(ctx)
    // );
    // } else {
    // keys.add((Expression) lhs);
    // }
    // values.add((Expression) this.visitExprSingle(currentPair.rhs));
    // }
    // toInsertExpr = new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
    // } else if (ctx.to_insert_expr != null) {
    // toInsertExpr = (Expression) this.visitExprSingle(ctx.to_insert_expr);
    // if (ctx.pos_expr != null) {
    // posExpr = (Expression) this.visitExprSingle(ctx.pos_expr);
    // }
    // } else {
    // throw new OurBadException("Unrecognised expression to insert in Insert Expression");
    // }
    // Expression mainExpr = (Expression) this.visitExprSingle(ctx.main_expr);

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
    // Expression nameExpression = (Expression) this.visitExprSingle(ctx.name_expr);
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
    // Expression newExpression = (Expression) this.visitExprSingle(ctx.replacer_expr);
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
    // Name var = ((VariableReferenceExpression) this.visitVarRef(copyDeclCtx.var_ref)).getVariableName();
    // Expression expr = (Expression) this.visitExprSingle(copyDeclCtx.src_expr);
    // return new CopyDeclaration(var, expr);
    // })
    // .collect(Collectors.toList());
    // Expression modifyExpression = (Expression) this.visitExprSingle(ctx.mod_expr);
    // Expression returnExpression = (Expression) this.visitExprSingle(ctx.ret_expr);
    // return new TransformExpression(copyDecls, modifyExpression, returnExpression, createMetadataFromContext(ctx));
    // }

    // @Override
    // public Node visitAppendExpr(XQueryParser.AppendExprContext ctx) {
    // Expression arrayExpression = (Expression) this.visitExprSingle(ctx.array_expr);
    // Expression toAppendExpression = (Expression) this.visitExprSingle(ctx.to_append_expr);
    // return new AppendExpression(arrayExpression, toAppendExpression, createMetadataFromContext(ctx));
    // }

    // public Expression getMainExpressionFromUpdateLocatorContext(XQueryParser.UpdateLocatorContext ctx) {
    // Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
    // for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 1)) {
    // if (child instanceof XQueryParser.LookupContext) {
    // Expression expr = (Expression) this.visitLookup((XQueryParser.LookupContext) child);
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
    // return (Expression) this.visitLookup((XQueryParser.LookupContext) locatorExprCtx);
    // } else {
    // throw new OurBadException("Unrecognized locator found in update expression.");
    // }
    // }

    // endregion

    // region postfix
    @Override
    public Node visitPostfixExpr(XQueryParser.PostfixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof XQueryParser.PredicateContext predicateContext) {
                Expression expr = (Expression) this.visitPredicate(predicateContext);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop())
                );
            } else if (child instanceof XQueryParser.LookupContext lookupContext) {
                Expression expr = (Expression) this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop())
                );
            } else if (child instanceof XQueryParser.ArgumentListContext argumentListContext) {
                List<Expression> arguments = getArgumentsFromArgumentListContext(
                    argumentListContext
                );
                mainExpression = new DynamicFunctionCallExpression(
                        mainExpression,
                        arguments,
                        createMetadataFromRange(ctx.main_expr.getStart(), argumentListContext.getStop())
                );
            } else {
                throw new OurBadException("Unrecognized postfix expression found.");
            }
        }
        return mainExpression;
    }

    @Override
    public Node visitPredicate(XQueryParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Node visitLookup(XQueryParser.LookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    @Override
    public Node visitUnaryLookup(XQueryParser.UnaryLookupContext ctx) {
        return this.visitKeySpecifier(ctx.keySpecifier());
    }

    public Node visitKeySpecifier(XQueryParser.KeySpecifierContext ctx) {
        if (ctx.lt != null) {
            return new StringLiteralExpression(
                    processStringLiteral(ctx.lt),
                    createMetadataFromContext(ctx)
            );
        }
        if (ctx.in != null) {
            return new IntegerLiteralExpression(
                    ctx.in.getText(),
                    createMetadataFromContext(ctx)
            );
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
    // TODO [EXPRVISITOR] orderedExpr unorderedExpr;
    @Override
    public Node visitPrimaryExpr(XQueryParser.PrimaryExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.VarRefContext varRefContext) {
            return this.visitVarRef(varRefContext);
        }
        if (child instanceof XQueryParser.ObjectConstructorContext objectConstructorContext) {
            return this.visitObjectConstructor(objectConstructorContext);
        }
        if (child instanceof XQueryParser.ArrayConstructorContext arrayConstructorContext) {
            return this.visitArrayConstructor(arrayConstructorContext);
        }
        if (child instanceof XQueryParser.ParenthesizedExprContext parenthesizedExprContext) {
            return this.visitParenthesizedExpr(parenthesizedExprContext);
        }
        if (child instanceof XQueryParser.LiteralContext literalContext) {
            return this.visitLiteral(literalContext);
        }
        if (child instanceof XQueryParser.ContextItemExprContext contextItemExprContext) {
            return this.visitContextItemExpr(contextItemExprContext);
        }
        if (child instanceof XQueryParser.FunctionCallContext functionCallContext) {
            return this.visitFunctionCall(functionCallContext);
        }
        if (child instanceof XQueryParser.FunctionItemExprContext functionItemExprContext) {
            return this.visitFunctionItemExpr(functionItemExprContext);
        }
        if (child instanceof XQueryParser.BlockExprContext blockExprContext) {
            return this.visitBlockExpr(blockExprContext);
        }
        if (child instanceof XQueryParser.UnaryLookupContext unaryLookupContext) {
            return new UnaryLookupExpression(
                    (Expression) this.visitUnaryLookup(unaryLookupContext),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof XQueryParser.NodeConstructorContext nodeConstructorContext) {
            return this.visitNodeConstructor(nodeConstructorContext);
        }
        throw new UnsupportedFeatureException(
                "Primary expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitLiteral(XQueryParser.LiteralContext ctx) {
        ParseTree child = ctx.children.get(0);

        if (child instanceof XQueryParser.StringLiteralContext stringLiteralContext) {
            return new StringLiteralExpression(
                    processStringLiteral(stringLiteralContext),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof XQueryParser.NumericLiteralContext) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }

        throw new UnsupportedFeatureException(
                "Literal not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    private static Expression getLiteralExpressionFromToken(String token, ExceptionMetadata metadataFromContext) {
        switch (token) {
            case "null":
                return new NullLiteralExpression(metadataFromContext);
            case "true":
                return new BooleanLiteralExpression(true, metadataFromContext);
            case "false":
                return new BooleanLiteralExpression(false, metadataFromContext);
            default:
        }
        if (token.contains("E") || token.contains("e")) {
            return new DoubleLiteralExpression(Double.parseDouble(token), metadataFromContext);
        }
        if (token.contains(".")) {
            return new DecimalLiteralExpression(new BigDecimal(token), metadataFromContext);
        }
        return new IntegerLiteralExpression(token, metadataFromContext);
    }

    private String parseStringLiteral(String source) {
        return StringLiteralUtils.parseXQuery(source);
    }

    @Override
    public Node visitObjectConstructor(XQueryParser.ObjectConstructorContext ctx) {
        List<Expression> keys = new ArrayList<>();
        List<Expression> values = new ArrayList<>();
        for (XQueryParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
            Node lhs = this.visitExprSingle(currentPair.lhs);
            if (lhs instanceof StepExpr) {
                throw new ParsingException(
                        "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --xquery-version CLI option.",
                        createMetadataFromContext(ctx)
                );
            } else {
                keys.add((Expression) lhs);
            }
            values.add((Expression) this.visitExprSingle(currentPair.rhs));
        }
        return new MapConstructorExpression(keys, values, createMetadataFromContext(ctx));
    }


    @Override
    public Node visitNodeConstructor(XQueryParser.NodeConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.DirectConstructorContext directConstructorContext) {
            return this.visitDirectConstructor(directConstructorContext);
        }
        if (child instanceof XQueryParser.ComputedConstructorContext computedConstructorContext) {
            return this.visitComputedConstructor(computedConstructorContext);
        }
        throw new UnsupportedFeatureException(
                "Node constructor not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitDirectConstructor(XQueryParser.DirectConstructorContext ctx) {
        if (ctx.COMMENT() != null) {
            String commentText = ctx.COMMENT().getText();
            String commentContent = commentText.substring(4, commentText.length() - 3);
            return new DirectCommentConstructorExpression(
                    commentContent,
                    createMetadataFromContext(ctx)
            );
        }
        if (ctx.open_close != null) {
            return this.visitDirElemConstructorOpenClose(ctx);
        } else if (ctx.single_tag != null) {
            return this.visitDirElemConstructorSingleTag(ctx);
        } else if (ctx.PI() != null) {
            return this.visitDirPIConstructor(ctx.PI(), createMetadataFromContext(ctx));
        }
        throw new UnsupportedFeatureException(
                "Direct constructor not yet implemented",
                createMetadataFromContext(ctx)
        );
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

    private Node visitDirElemConstructorOpenClose(XQueryParser.DirectConstructorContext ctx) {
        XQueryParser.DirElemConstructorOpenCloseContext openClose = ctx.open_close;
        // check that the start and end tags are the same
        if (
            openClose.close_tag_name != null
                && !openClose.close_tag_name.getText().equals(ctx.open_tag_name.getText())
        ) {
            throw new DirectElementConstructorTagMismatchException(
                    "The name used in the end tag must exactly match the name used in the corresponding start tag.",
                    createMetadataFromContext(ctx)
            );
        }

        this.dirElemNamespaceFrames.push(new HashMap<>());
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes != null) {
                attributeResult = this.getAttributesExpressionsList(ctx.attributes);
            }

            List<Expression> content = DirectConstructorUtils.mergeElementContent(
                this.xQueryTokenStream,
                openClose.endOpen,
                openClose.dirElemContent(),
                child -> (Expression) this.visitDirElemContent(child)
            );

            return new DirElemConstructorExpression(
                    parseName(ctx.open_tag_name, false, false, false, true),
                    content,
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    createMetadataFromContext(ctx)
            );
        } finally {
            this.dirElemNamespaceFrames.pop();
        }

    }

    private Node visitDirElemConstructorSingleTag(XQueryParser.DirectConstructorContext ctx) {
        this.dirElemNamespaceFrames.push(new HashMap<>());
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes != null) {
                attributeResult = this.getAttributesExpressionsList(ctx.attributes);
            }

            return new DirElemConstructorExpression(
                    parseName(ctx.open_tag_name, false, false, false, true),
                    new ArrayList<>(),
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    createMetadataFromContext(ctx)
            );
        } finally {
            this.dirElemNamespaceFrames.pop();
        }
    }

    @Override
    public Node visitDirElemContent(XQueryParser.DirElemContentContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.DirectConstructorContext directConstructorContext) {
            return this.visitDirectConstructor(directConstructorContext);
        } else if (child instanceof XQueryParser.CommonContentContext commonContentContext) {
            return this.visitCommonContent(commonContentContext);
        } else {
            // Include lexer hidden-channel characters (e.g. spaces) in this fragment; ParseTree#getText() drops them.
            String text = this.xQueryTokenStream.getText(ctx.getSourceInterval());
            if (ctx.CDATA() != null) {
                // filter out the <![CDATA[ and ]]>, and return the text
                return new TextNodeExpression(text.substring(9, text.length() - 3), createMetadataFromContext(ctx));
            }
            return new TextNodeExpression(text, createMetadataFromContext(ctx));
        }
    }

    @Override
    public Node visitCommonContent(XQueryParser.CommonContentContext ctx) {
        if (ctx.expr() != null) {
            return (Expression) this.visitExpr(ctx.expr());
        }
        String processedContent = DirectConstructorUtils.processLiteralContent(ctx.getText());
        return new TextNodeExpression(processedContent, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitComputedConstructor(XQueryParser.ComputedConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.CompDocConstructorContext compDocConstructorContext) {
            return this.visitCompDocConstructor(compDocConstructorContext);
        } else if (child instanceof XQueryParser.CompElemConstructorContext compElemConstructorContext) {
            return this.visitCompElemConstructor(compElemConstructorContext);
        } else if (child instanceof XQueryParser.CompPIConstructorContext compPIConstructorContext) {
            return this.visitCompPIConstructor(compPIConstructorContext);
        } else if (child instanceof XQueryParser.CompTextConstructorContext compTextConstructorContext) {
            return this.visitCompTextConstructor(compTextConstructorContext);
        } else if (child instanceof XQueryParser.CompCommentConstructorContext compCommentConstructorContext) {
            return this.visitCompCommentConstructor(compCommentConstructorContext);
        } else if (child instanceof XQueryParser.CompAttrConstructorContext compAttrConstructorContext) {
            return this.visitCompAttrConstructor(compAttrConstructorContext);
        } else if (child instanceof XQueryParser.CompNamespaceConstructorContext compNamespaceConstructorContext) {
            return this.visitCompNamespaceConstructor(compNamespaceConstructorContext);
        }
        throw new UnsupportedFeatureException("Computed constructor", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCompDocConstructor(XQueryParser.CompDocConstructorContext ctx) {
        Expression contentExpression = (Expression) this.visitEnclosedExpression(ctx.enclosedExpression());
        return new DocumentNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCompTextConstructor(XQueryParser.CompTextConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new TextNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCompCommentConstructor(XQueryParser.CompCommentConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new CommentNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    public Node visitCompPIConstructor(XQueryParser.CompPIConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedPIConstructorExpression(
                    ctx.ncName().getText(),
                    contentExpression,
                    createMetadataFromContext(ctx)
            );
        }
        if (ctx.expr() != null) {
            Expression nameExpression = (Expression) this.visitExpr(ctx.expr());
            return new ComputedPIConstructorExpression(
                    nameExpression,
                    contentExpression,
                    createMetadataFromContext(ctx)
            );
        }
        throw new ParsingException(
                "Computed processing instruction constructor must have either a static NCName or a dynamic name expression",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCompAttrConstructor(XQueryParser.CompAttrConstructorContext ctx) {
        Expression valueExpression = (Expression) visit(ctx.enclosedExpression());

        // Check if we have a static attribute name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.name != null) {
            // Static attribute name: attribute attributeName { value }
            Name attributeName = this.parseEqName(ctx.name, false, false, false, false);
            return new ComputedAttributeConstructorExpression(
                    attributeName,
                    valueExpression,
                    createMetadataFromContext(ctx)
            );
        } else if (ctx.name_expr != null) {
            // Dynamic attribute name: attribute { nameExpression } { value }
            Expression nameExpression = (Expression) this.visitExpr(ctx.name_expr);
            return new ComputedAttributeConstructorExpression(
                    nameExpression,
                    valueExpression,
                    createMetadataFromContext(ctx)
            );
        } else {
            throw new ParsingException(
                    "Computed attribute constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx)
            );
        }
    }

    @Override
    public Node visitCompElemConstructor(XQueryParser.CompElemConstructorContext ctx) {
        Expression contentExpression = (Expression) this.visitEnclosedContentExpr(ctx.enclosedContentExpr());

        // Check if we have a static element name (eqName) or dynamic name expression (LBRACE expr RBRACE)
        if (ctx.eqName() != null) {
            // Static element name: element elementName { content }
            Name elementName = parseEqName(ctx.eqName(), false, false, false, true);
            return new ComputedElementConstructorExpression(
                    elementName,
                    contentExpression,
                    createMetadataFromContext(ctx)
            );
        } else if (ctx.expr() != null) {
            // Dynamic element name: element { nameExpression } { content }
            Expression nameExpression = (Expression) this.visitExpr(ctx.expr());
            return new ComputedElementConstructorExpression(
                    nameExpression,
                    contentExpression,
                    createMetadataFromContext(ctx)
            );
        } else {
            throw new ParsingException(
                    "Computed element constructor must have either a static name or dynamic name expression",
                    createMetadataFromContext(ctx)
            );
        }
    }

    @Override
    public Node visitCompNamespaceConstructor(XQueryParser.CompNamespaceConstructorContext ctx) {
        Expression uriExpression = (Expression) this.visitEnclosedExpression(
            ctx.enclosedURIExpr().enclosedExpression()
        );
        if (ctx.ncName() != null) {
            return new ComputedNamespaceConstructorExpression(
                    ctx.ncName().getText(),
                    uriExpression,
                    createMetadataFromContext(ctx)
            );
        }
        if (ctx.enclosedPrefixExpr() != null) {
            Expression prefixExpression = (Expression) this.visitEnclosedExpression(
                ctx.enclosedPrefixExpr().enclosedExpression()
            );
            return new ComputedNamespaceConstructorExpression(
                    prefixExpression,
                    uriExpression,
                    createMetadataFromContext(ctx)
            );
        }
        throw new ParsingException(
                "Computed namespace constructor must have either a static prefix or a dynamic prefix expression",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitEnclosedContentExpr(XQueryParser.EnclosedContentExprContext ctx) {
        return this.visitEnclosedExpression(ctx.enclosedExpression());
    }

    @Override
    public Node visitArrayConstructor(XQueryParser.ArrayConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.SquareArrayConstructorContext sqCtx) {
            List<XQueryParser.ExprSingleContext> memberCtxs = sqCtx.exprSingle();
            if (memberCtxs == null || memberCtxs.isEmpty()) {
                return new ArrayConstructorExpression(
                        new ArrayList<>(),
                        true,
                        createMetadataFromContext(sqCtx)
                );
            }
            List<Expression> memberExpressions = new ArrayList<>();
            for (XQueryParser.ExprSingleContext memberCtx : memberCtxs) {
                memberExpressions.add((Expression) this.visitExprSingle(memberCtx));
            }
            return new ArrayConstructorExpression(
                    memberExpressions,
                    true,
                    createMetadataFromContext(sqCtx)
            );
        }
        // else curlyArrayConstructor
        XQueryParser.CurlyArrayConstructorContext childCtx = (XQueryParser.CurlyArrayConstructorContext) child;
        if (childCtx.enclosedExpression() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(childCtx));
        }
        Expression content = (Expression) this.visitEnclosedExpression(childCtx.enclosedExpression());
        return new ArrayConstructorExpression(content, createMetadataFromContext(childCtx));
    }

    @Override
    public Node visitParenthesizedExpr(XQueryParser.ParenthesizedExprContext ctx) {
        if (ctx.expr() == null) {
            return new CommaExpression(createMetadataFromContext(ctx));
        }
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Node visitVarRef(XQueryParser.VarRefContext ctx) {
        Name name = parseEqName(ctx.eqName(), false, false, false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemExpr(XQueryParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    public SequenceType processSequenceType(XQueryParser.SequenceTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.createSequenceType("()");
        }
        ItemType itemType = processItemType(ctx.item);
        if (ctx.question.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrZero
            );
        }
        if (ctx.star.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.ZeroOrMore
            );
        }
        if (ctx.plus.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrMore
            );
        }
        return new SequenceType(itemType);
    }

    public SequenceType processSingleType(XQueryParser.SingleTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.createSequenceType("()");
        }

        ItemType itemType = processItemType(ctx.item);
        if (ctx.question.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrZero
            );
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
            XQueryParser.TypedFunctionTestContext typedFnCtx = itemTypeContext.functionTest().typedFunctionTest();
            if (typedFnCtx != null) {
                SequenceType rt = processSequenceType(typedFnCtx.rt);
                List<SequenceType> st = typedFnCtx.st.stream()
                    .map(this::processSequenceType)
                    .collect(Collectors.toList());
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
                Name keyName = parseEqName(typedMapTestContext.eqName(), false, true, false, false);
                keyName = ItemTypeReference.renameAtomic(this.moduleContext, keyName);
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
            Name name = parseEqName(itemTypeContext.eqName(), false, true, false, false);
            name = ItemTypeReference.renameAtomic(this.moduleContext, name);
            if (!BuiltinTypesCatalogue.typeExists(name)) {
                return new ItemTypeReference(name);
            }
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        if (itemTypeContext.kindTest() != null) {
            return processKindTestAsItemType(itemTypeContext.kindTest());
        }
        throw new UnsupportedFeatureException(
                "Unsupported itemtype encountered",
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    private ItemType processKindTestAsItemType(XQueryParser.KindTestContext kindTestContext) {
        if (kindTestContext.anyKindTest() != null) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (kindTestContext.documentTest() != null) {
            XQueryParser.DocumentTestContext documentTestContext = kindTestContext.documentTest();
            if (documentTestContext.schemaElementTest() != null) {
                throw new UnsupportedFeatureException(
                        "Schema element tests (schema-element(...)) are not supported",
                        createMetadataFromContext(documentTestContext)
                );
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
            if (attributeTestContext.typeName() != null) {
                throw new UnsupportedFeatureException(
                        "Typed attribute item tests are not supported yet",
                        createMetadataFromContext(attributeTestContext)
                );
            }
            if (attributeTestContext.attributeNameOrWildcard() == null) {
                return BuiltinTypesCatalogue.attributeNode;
            }
            if (attributeTestContext.attributeNameOrWildcard().attributeName() == null) {
                return BuiltinTypesCatalogue.attributeNode;
            }
            Name attributeName = parseEqName(
                attributeTestContext.attributeNameOrWildcard().attributeName().eqName(),
                false,
                false,
                false,
                false
            );
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
            XQueryParser.PiTestContext piTestContext = kindTestContext.piTest();
            if (piTestContext.ncName() != null) {
                return ItemTypeFactory.processingInstructionNodeItemType(piTestContext.ncName().getText());
            }
            if (piTestContext.stringLiteral() != null) {
                String targetName = processStringLiteral(piTestContext.stringLiteral());
                return ItemTypeFactory.processingInstructionNodeItemType(targetName);
            }
            return BuiltinTypesCatalogue.processingInstructionNode;
        }
        throw new UnsupportedFeatureException(
                "Unsupported kind test in item type: " + kindTestContext.getText(),
                createMetadataFromContext(kindTestContext)
        );
    }

    private ElementNodeItemType getElementTestAsItemType(XQueryParser.ElementTestContext elementTestContext) {
        if (elementTestContext.optional != null || elementTestContext.typeName() != null) {
            throw new UnsupportedFeatureException(
                    "Typed or nillable element item tests are not supported yet",
                    createMetadataFromContext(elementTestContext)
            );
        }
        if (elementTestContext.elementNameOrWildcard() == null) {
            return (ElementNodeItemType) BuiltinTypesCatalogue.elementNode;
        }
        if (elementTestContext.elementNameOrWildcard().elementName() == null) {
            return (ElementNodeItemType) BuiltinTypesCatalogue.elementNode;
        }
        Name elementName = parseEqName(
            elementTestContext.elementNameOrWildcard().elementName().eqName(),
            false,
            false,
            false,
            false
        );
        return (ElementNodeItemType) ItemTypeFactory.elementNodeItemType(elementName);
    }

    private Expression processFunctionCall(Name name, List<Expression> children, ExceptionMetadata metadata) {
        return new FunctionCallExpression(
                name,
                children,
                metadata
        );
    }

    @Override
    public Node visitFunctionCall(XQueryParser.FunctionCallContext ctx) {
        Name name = parseFunctionName(ctx.fn_name);
        return processFunctionCall(
            name,
            getArgumentsFromArgumentListContext(ctx.argumentList()),
            createMetadataFromContext(ctx)
        );
    }

    private List<Expression> getArgumentsFromArgumentListContext(XQueryParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (XQueryParser.ArgumentContext arg : ctx.args) {
                Expression currentArg = (Expression) this.visitArgument(arg);
                arguments.add(currentArg);
            }
        }
        return arguments;
    }

    @Override
    public Node visitArgument(XQueryParser.ArgumentContext ctx) {
        if (ctx.exprSingle() != null) {
            return this.visitExprSingle(ctx.exprSingle());
        }
        return null;
    }

    @Override
    public Node visitFunctionItemExpr(XQueryParser.FunctionItemExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.NamedFunctionRefContext namedFunctionRefContext) {
            return this.visitNamedFunctionRef(namedFunctionRefContext);
        }
        if (child instanceof XQueryParser.InlineFunctionExprContext inlineFunctionExprContext) {
            return this.visitInlineFunctionExpr(inlineFunctionExprContext);
        }
        throw new UnsupportedFeatureException(
                "Function item expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitNamedFunctionRef(XQueryParser.NamedFunctionRefContext ctx) {
        Name name = parseFunctionName(ctx.fn_name);
        String arityLiteral = ctx.arity.getText();
        try {
            int arity = Integer.parseInt(arityLiteral);
            return new NamedFunctionReferenceExpression(
                    new FunctionIdentifier(name, arity),
                    createMetadataFromContext(ctx)
            );
        } catch (NumberFormatException e) {
            throw new NumericOverflowOrUnderflow(
                    "Named function reference arity is out of range for implementation limits: "
                        + name
                        + "#"
                        + arityLiteral,
                    createMetadataFromContext(ctx)
            );
        }
    }

    @Override
    public Node visitInlineFunctionExpr(XQueryParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.createSequenceType("item*");
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false, false, false);
                paramType = SequenceType.createSequenceType("item*");
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            Name.createVariableInDefaultFunctionNamespace("inline-function`"),
                            paramName,
                            createMetadataFromContext(param)
                    );
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

        StatementsAndOptionalExpr funcBody = (StatementsAndOptionalExpr) this
            .visitStatementsAndOptionalExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                annotations,
                null,
                fnParams,
                fnReturnType,
                funcBody,
                createMetadataFromContext(ctx)
        );
    }
    // endregion

    // region control
    @Override
    public Node visitIfExpr(XQueryParser.IfExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.test_condition);
        Expression branch = (Expression) this.visitExprSingle(ctx.branch);
        Expression else_branch = (Expression) this.visitExprSingle(ctx.else_branch);
        return new ConditionalExpression(
                condition,
                branch,
                else_branch,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitSwitchExpr(XQueryParser.SwitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<SwitchCase> cases = new ArrayList<>();
        for (XQueryParser.SwitchCaseClauseContext expr : ctx.cases) {
            List<Expression> conditionExpressions = new ArrayList<>();
            for (int i = 0; i < expr.cond.size(); ++i) {
                conditionExpressions.add((Expression) this.visitExprSingle(expr.cond.get(i)));
            }
            SwitchCase c = new SwitchCase(conditionExpressions, (Expression) this.visitExprSingle(expr.ret));
            cases.add(c);
        }
        Expression defaultCase = (Expression) this.visitExprSingle(ctx.def);
        return new SwitchExpression(condition, cases, defaultCase, createMetadataFromContext(ctx));
    }
    // endregion

    // region quantified
    @Override
    public Node visitTypeswitchExpr(XQueryParser.TypeswitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeswitchCase> cases = new ArrayList<>();
        for (XQueryParser.CaseClauseContext expr : ctx.cses) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (expr.var_ref != null) {
                variableName = ((VariableReferenceExpression) this.visitVarRef(
                    expr.var_ref
                )).getVariableName();
            }
            if (expr.union != null && !expr.union.isEmpty()) {
                for (XQueryParser.SequenceTypeContext sequenceType : expr.union) {
                    union.add(this.processSequenceType(sequenceType));
                }
            }
            Expression expression = (Expression) this.visitExprSingle(expr.ret);
            cases.add(
                new TypeswitchCase(
                        variableName,
                        union,
                        expression
                )
            );
        }
        Name defaultVariableName = null;
        if (ctx.var_ref != null) {
            defaultVariableName = ((VariableReferenceExpression) this.visitVarRef(
                ctx.var_ref
            )).getVariableName();
        }
        Expression defaultCase = (Expression) this.visitExprSingle(ctx.def);
        return new TypeSwitchExpression(
                condition,
                cases,
                new TypeswitchCase(defaultVariableName, defaultCase),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitQuantifiedExpr(XQueryParser.QuantifiedExprContext ctx) {
        Clause lastClause = null;
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        boolean isUniversal = false;
        if (ctx.ev == null) {
            isUniversal = false;
        } else {
            isUniversal = true;
        }
        for (XQueryParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
            Expression varExpression;
            SequenceType sequenceType = null;
            Name variableName = ((VariableReferenceExpression) this.visitVarRef(
                currentVariable.varRef()
            )).getVariableName();
            if (currentVariable.sequenceType() != null) {
                sequenceType = this.processSequenceType(currentVariable.sequenceType());
            }

            varExpression = (Expression) this.visitExprSingle(currentVariable.exprSingle());
            Clause newClause = new ForClause(
                    variableName,
                    false,
                    sequenceType,
                    null,
                    varExpression,
                    createMetadataFromContext(currentVariable)
            );
            if (lastClause != null) {
                lastClause.chainWith(newClause);
            }
            lastClause = newClause;
        }
        if (lastClause == null) {
            throw new OurBadException("A quantified expression must bind at least one variable.");
        }
        WhereClause whereClause = null;
        if (!isUniversal) {
            whereClause = new WhereClause(expression, createMetadataFromContext(ctx.exprSingle()));
        } else {
            whereClause = new WhereClause(
                    new NotExpression(expression, createMetadataFromContext(ctx.exprSingle())),
                    createMetadataFromContext(ctx.exprSingle())
            );
        }
        lastClause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new NullLiteralExpression(createMetadataFromContext(ctx)),
                createMetadataFromContext(ctx)
        );
        whereClause.chainWith(returnClause);
        Expression flworExpression = new FlworExpression(returnClause, createMetadataFromContext(ctx));
        if (!isUniversal) {
            return new FunctionCallExpression(
                    Name.createVariableInDefaultFunctionNamespace("exists"),
                    Collections.singletonList(flworExpression),
                    createMetadataFromContext(ctx)
            );
        } else {
            return new FunctionCallExpression(
                    Name.createVariableInDefaultFunctionNamespace("empty"),
                    Collections.singletonList(flworExpression),
                    createMetadataFromContext(ctx)
            );
        }
    }

    @Override
    public Node visitTryCatchExpr(XQueryParser.TryCatchExprContext ctx) {
        Expression tryExpression = ctx.try_expression == null
            ? new CommaExpression(createMetadataFromContext(ctx))
            : (Expression) this.visitExpr(ctx.try_expression);
        Map<CatchPattern, Expression> catchExpressions = new LinkedHashMap<>();
        for (XQueryParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = catchCtx.catch_expression == null
                ? new CommaExpression(createMetadataFromContext(catchCtx))
                : (Expression) this.visitExpr(catchCtx.catch_expression);
            for (XQueryParser.EqNameContext eqNameCtx : catchCtx.errors) {
                CatchPattern pattern = CatchPattern.exact(parseEqName(eqNameCtx, false, false, false, false));
                if (!catchExpressions.containsKey(pattern)) {
                    catchExpressions.put(pattern, catchExpression);
                }
            }
            for (XQueryParser.WildcardContext wildcardCtx : catchCtx.jokers) {
                CatchPattern pattern = this.parseWildcardPattern(wildcardCtx);
                if (!catchExpressions.containsKey(pattern)) {
                    catchExpressions.put(pattern, catchExpression);
                }
            }
        }
        return new TryCatchExpression(
                tryExpression,
                catchExpressions,
                createMetadataFromContext(ctx)
        );
    }

    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        return generateMetadata(ctx.getStart(), ctx.getStop());
    }

    private ExceptionMetadata createMetadataFromTree(ParseTree tree) {
        return createMetadataFromRange(getStartToken(tree), getStopToken(tree));
    }

    private ExceptionMetadata createMetadataFromRange(Token start, Token end) {
        return generateMetadata(start, end);
    }

    private ExceptionMetadata createMetadataFromTrees(ParseTree startTree, ParseTree endTree) {
        return createMetadataFromRange(getStartToken(startTree), getStopToken(endTree));
    }

    private Token getStartToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStart();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new OurBadException("Cannot get start token from parse tree: " + tree.getClass().getName());
    }

    private Token getStopToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStop();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new OurBadException("Cannot get stop token from parse tree: " + tree.getClass().getName());
    }

    @Override
    public Node visitVarDecl(XQueryParser.VarDeclContext ctx) {
        // if there is no 'as sequenceType' is set to null to differentiate from the case of 'as item*'
        // but it is actually treated as if it was item*
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        SequenceType seq = null;
        boolean external;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.varRef())).getVariableName();
        if (ctx.sequenceType() != null) {
            seq = this.processSequenceType(ctx.sequenceType());
        }
        external = (ctx.external != null);
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = (Expression) this.visitExprSingle(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }
        return new VariableDeclaration(var, external, seq, expr, annotations, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemDecl(XQueryParser.ContextItemDeclContext ctx) {
        // if there is no 'as sequenceType' is set to null to differentiate from the case of 'as item*'
        // but it is actually treated as if it was item*
        SequenceType seq = null;
        boolean external;
        Name var = Name.CONTEXT_ITEM;
        if (ctx.sequenceType() != null) {
            seq = this.processSequenceType(ctx.sequenceType());
        }
        external = (ctx.external != null);
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = (Expression) this.visitExprSingle(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }

        return new VariableDeclaration(var, external, seq, expr, null, createMetadataFromContext(ctx));
    }

    // region scripting
    @Override
    public Node visitStatements(XQueryParser.StatementsContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (XQueryParser.StatementContext stmt : ctx.statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatementsAndExpr(XQueryParser.StatementsAndExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (XQueryParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        Expression expression = (Expression) this.visitExpr(ctx.expr());
        return new StatementsAndExpr(statements, expression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatementsAndOptionalExpr(XQueryParser.StatementsAndOptionalExprContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (XQueryParser.StatementContext stmt : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(stmt));
        }
        if (ctx.expr() != null) {
            Expression expression = (Expression) this.visitExpr(ctx.expr());
            return new StatementsAndOptionalExpr(statements, expression, createMetadataFromContext(ctx));
        }
        return new StatementsAndOptionalExpr(statements, null, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitStatement(XQueryParser.StatementContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof XQueryParser.ApplyStatementContext applyStatementContext) {
            return this.visitApplyStatement(applyStatementContext);
        }
        if (content instanceof XQueryParser.AssignStatementContext assignStatementContext) {
            return this.visitAssignStatement(assignStatementContext);
        }
        if (content instanceof XQueryParser.BlockStatementContext blockStatementContext) {
            return this.visitBlockStatement(blockStatementContext);
        }
        if (content instanceof XQueryParser.BreakStatementContext breakStatementContext) {
            return this.visitBreakStatement(breakStatementContext);
        }
        if (content instanceof XQueryParser.ContinueStatementContext continueStatementContext) {
            return this.visitContinueStatement(continueStatementContext);
        }
        if (content instanceof XQueryParser.ExitStatementContext exitStatementContext) {
            return this.visitExitStatement(exitStatementContext);
        }
        if (content instanceof XQueryParser.FlworStatementContext flworStatementContext) {
            return this.visitFlworStatement(flworStatementContext);
        }
        if (content instanceof XQueryParser.IfStatementContext ifStatementContext) {
            return this.visitIfStatement(ifStatementContext);
        }
        if (content instanceof XQueryParser.SwitchStatementContext switchStatementContext) {
            return this.visitSwitchStatement(switchStatementContext);
        }
        if (content instanceof XQueryParser.TryCatchStatementContext tryCatchStatementContext) {
            return this.visitTryCatchStatement(tryCatchStatementContext);
        }
        if (content instanceof XQueryParser.TypeSwitchStatementContext typeSwitchStatementContext) {
            return this.visitTypeSwitchStatement(typeSwitchStatementContext);
        }
        if (content instanceof XQueryParser.VarDeclStatementContext varDeclStatementContext) {
            return this.visitVarDeclStatement(varDeclStatementContext);
        }
        if (content instanceof XQueryParser.WhileStatementContext whileStatementContext) {
            return this.visitWhileStatement(whileStatementContext);
        }
        throw new OurBadException("Unrecognized Statement.");
    }

    // mutation
    @Override
    public Node visitApplyStatement(XQueryParser.ApplyStatementContext ctx) {
        Expression exprSimple = (Expression) this.visitExprSimple(ctx.exprSimple());
        return new ApplyStatement(exprSimple, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitAssignStatement(XQueryParser.AssignStatementContext ctx) {
        Name paramName = parseEqName(ctx.varName().eqName(), false, false, false, false);
        Expression exprSingle = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new AssignStatement(exprSingle, paramName, createMetadataFromContext(ctx));
    }
    // end mutation

    // block
    @Override
    public Node visitBlockStatement(XQueryParser.BlockStatementContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (XQueryParser.StatementContext statement : ctx.statements().statement()) {
            statements.add((Statement) this.visitStatement(statement));
        }
        return new BlockStatement(statements, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitBlockExpr(XQueryParser.BlockExprContext ctx) {
        StatementsAndExpr statementsAndExpr = (StatementsAndExpr) this.visitStatementsAndExpr(ctx.statementsAndExpr());
        return new BlockExpression(statementsAndExpr, createMetadataFromContext(ctx));
    }
    // end block

    // loops
    @Override
    public Node visitBreakStatement(XQueryParser.BreakStatementContext ctx) {
        return new BreakStatement(createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContinueStatement(XQueryParser.ContinueStatementContext ctx) {
        return new ContinueStatement(createMetadataFromContext(ctx));
    }

    @Override
    public Node visitExitStatement(XQueryParser.ExitStatementContext ctx) {
        Expression exprSingle = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new ExitStatement(exprSingle, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitFlworStatement(XQueryParser.FlworStatementContext ctx) {
        Clause clause;
        // Check for start clause. Only for or let allowed.
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }
        Clause lastFlowrClause = clause.getLastClause();
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof XQueryParser.ForClauseContext forClauseContext) {
                clause = (Clause) this.visitForClause(forClauseContext);
            } else if (child instanceof XQueryParser.LetClauseContext letClauseContext) {
                clause = (Clause) this.visitLetClause(letClauseContext);
            } else if (child instanceof XQueryParser.WhereClauseContext whereClauseContext) {
                clause = (Clause) this.visitWhereClause(whereClauseContext);
            } else if (child instanceof XQueryParser.GroupByClauseContext groupByClauseContext) {
                clause = (Clause) this.visitGroupByClause(groupByClauseContext);
            } else if (child instanceof XQueryParser.OrderByClauseContext orderByClauseContext) {
                clause = (Clause) this.visitOrderByClause(orderByClauseContext);
            } else if (child instanceof XQueryParser.CountClauseContext countClauseContext) {
                clause = (Clause) this.visitCountClause(countClauseContext);
            } else {
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet",
                        createMetadataFromContext(ctx)
                );
            }
            lastFlowrClause.chainWith(clause.getFirstClause());
            lastFlowrClause = clause.getLastClause();
        }
        Statement returnStatement = (Statement) this.visitStatement(ctx.returnStmt);
        ReturnStatementClause returnStatementClause = new ReturnStatementClause(
                returnStatement,
                returnStatement.getMetadata()
        );
        lastFlowrClause.chainWith(returnStatementClause);
        returnStatementClause = returnStatementClause.detachInitialLetClausesForStatements();
        return new FlowrStatement(returnStatementClause, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitWhileStatement(XQueryParser.WhileStatementContext ctx) {
        Expression testCondition = (Expression) this.visitExpr(ctx.test_expr);
        Statement statement = (Statement) this.visitStatement(ctx.stmt);
        return new WhileStatement(testCondition, statement, createMetadataFromContext(ctx));
    }


    // end loops

    // control

    @Override
    public Node visitIfStatement(XQueryParser.IfStatementContext ctx) {
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
    public Node visitSwitchStatement(XQueryParser.SwitchStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.condExpr);
        List<SwitchCaseStatement> cases = new ArrayList<>();
        for (XQueryParser.SwitchCaseStatementContext stmt : ctx.cases) {
            List<Expression> conditionExpressions = new ArrayList<>();
            stmt.cond.forEach(exprSingle -> {
                conditionExpressions.add((Expression) this.visitExprSingle(exprSingle));
            });
            // TODO: Test this behaviour with return!
            // Verify return statement
            ParseTree caseTree = stmt.children.get(stmt.children.size() - 1);
            checkForUnsupportedStatement(caseTree);
            SwitchCaseStatement swCase = new SwitchCaseStatement(
                    conditionExpressions,
                    (Statement) this.visitStatement(stmt.ret)
            );
            cases.add(swCase);
        }
        // Verify default statement
        ParseTree defaultTree = ctx.children.get(ctx.children.size() - 1);
        checkForUnsupportedStatement(defaultTree);
        Statement defaultCase = (Statement) this.visitStatement(ctx.def);
        return new SwitchStatement(condition, cases, defaultCase, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTryCatchStatement(XQueryParser.TryCatchStatementContext ctx) {
        BlockStatement tryBlock = (BlockStatement) this.visitBlockStatement(ctx.try_block);
        Map<CatchPattern, BlockStatement> catchBlockStatements = new LinkedHashMap<>();
        for (XQueryParser.CatchCaseStatementContext catchCtx : ctx.catches) {
            BlockStatement catchBlockStatement = (BlockStatement) this.visitBlockStatement(catchCtx.catch_block);
            for (XQueryParser.EqNameContext eqNameCtx : catchCtx.errors) {
                CatchPattern pattern = CatchPattern.exact(parseEqName(eqNameCtx, false, false, false, false));
                if (!catchBlockStatements.containsKey(pattern)) {
                    catchBlockStatements.put(pattern, catchBlockStatement);
                }
            }
            for (XQueryParser.WildcardContext wildcardCtx : catchCtx.jokers) {
                CatchPattern pattern = this.parseWildcardPattern(wildcardCtx);
                if (!catchBlockStatements.containsKey(pattern)) {
                    catchBlockStatements.put(pattern, catchBlockStatement);
                }
            }
        }
        return new TryCatchStatement(
                tryBlock,
                catchBlockStatements,
                createMetadataFromContext(ctx)
        );
    }

    private CatchPattern parseWildcardPattern(XQueryParser.WildcardContext wildcardContext) {
        if (wildcardContext instanceof XQueryParser.AllNamesContext) {
            return CatchPattern.catchAll();
        }
        if (wildcardContext instanceof XQueryParser.AllWithLocalContext) {
            String wildcardText = wildcardContext.getText();
            return CatchPattern.namespaceWildcard(wildcardText.substring(2), wildcardText);
        }
        if (wildcardContext instanceof XQueryParser.AllWithNSContext) {
            String wildcardText = wildcardContext.getText();
            String prefix = wildcardText.substring(0, wildcardText.length() - 2);
            String namespace = resolvePrefixForDirConstructor(prefix);
            if (namespace == null) {
                throw new PrefixCannotBeExpandedException(
                        "Cannot expand prefix " + prefix,
                        createMetadataFromContext(wildcardContext)
                );
            }
            return CatchPattern.localNameWildcard(namespace, wildcardText);
        }
        if (wildcardContext instanceof XQueryParser.BracedURILiteralContext) {
            String wildcardText = wildcardContext.getText();
            int closingBrace = wildcardText.indexOf('}');
            return CatchPattern.localNameWildcard(wildcardText.substring(2, closingBrace), wildcardText);
        }
        throw new OurBadException("Unsupported catch wildcard pattern: " + wildcardContext.getText());
    }

    @Override
    public Node visitTypeSwitchStatement(XQueryParser.TypeSwitchStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeSwitchStatementCase> cases = new ArrayList<>();
        for (XQueryParser.CaseStatementContext stmt : ctx.cases) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (stmt.var_ref != null) {
                variableName = ((VariableReferenceExpression) this.visitVarRef(stmt.var_ref)).getVariableName();
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
            defaultVariableName = ((VariableReferenceExpression) this.visitVarRef(ctx.var_ref)).getVariableName();
        }
        Statement defaultStatement = (Statement) this.visitStatement(ctx.def);
        return new TypeSwitchStatement(
                condition,
                cases,
                new TypeSwitchStatementCase(defaultVariableName, defaultStatement),
                createMetadataFromContext(ctx)
        );
    }

    public void checkForUnsupportedStatement(ParseTree content) {
        if (content instanceof XQueryParser.BreakStatementContext) {
            throw new OurBadException("Break statement is not supported in an if branch!");
        } else if (content instanceof XQueryParser.ContinueStatementContext) {
            throw new OurBadException("Continue statement is not supported in an if branch!");
        } else if (content instanceof XQueryParser.ExitStatementContext) {
            throw new OurBadException("Exit statement is not supported in an if branch!");
        }
    }

    // end control

    // declaration

    @Override
    public Node visitVarDeclStatement(XQueryParser.VarDeclStatementContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        List<VariableDeclStatement> variables = new ArrayList<>();
        for (XQueryParser.VarDeclForStatementContext varDecl : ctx.varDeclForStatement()) {
            SequenceType seq = null;
            Name var = ((VariableReferenceExpression) this.visitVarRef(varDecl.var_ref)).getVariableName();
            Expression exprSingle = null;

            if (varDecl.sequenceType() != null) {
                seq = this.processSequenceType(varDecl.sequenceType());
            }
            if (varDecl.exprSingle() != null) {
                exprSingle = (Expression) this.visitExprSingle(varDecl.exprSingle());
                if (seq != null) {
                    exprSingle = new TreatExpression(
                            exprSingle,
                            seq,
                            ErrorCode.UnexpectedTypeErrorCode,
                            exprSingle.getMetadata()
                    );
                }
            }
            variables.add(
                new VariableDeclStatement(
                        annotations,
                        var,
                        seq,
                        exprSingle,
                        createMetadataFromContext(varDecl)
                )
            );
        }
        if (variables.size() == 1) {
            return variables.get(0);
        }
        return new CommaVariableDeclStatement(variables, createMetadataFromContext(ctx));
    }

    // end declaration

    // start xml

    @Override
    public Node visitPathExpr(XQueryParser.PathExprContext ctx) {
        if (ctx.singleslash != null) {
            return visitSingleSlash(ctx, ctx.singleslash);
        } else if (ctx.doubleslash != null) {
            return visitDoubleSlash(ctx, ctx.doubleslash);
        } else if (ctx.relative != null) {
            return visitRelativeWithoutSlash(ctx.relative);
        }
        return visitSingleSlashNoStepExpr(ctx);
    }

    private Node visitSingleSlashNoStepExpr(XQueryParser.PathExprContext ctx) {
        // Case: No StepExpr, only dash
        return new FunctionCallExpression(
                Name.createVariableInDefaultBuiltinFunctionNamespace("root"),
                Collections.emptyList(),
                createMetadataFromContext(ctx)
        );
    }

    private Node visitRelativeWithoutSlash(XQueryParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1 && relativeContext.stepExpr(0).postfixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostfixExpr(relativeContext.stepExpr(0).postfixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Node visitDoubleSlash(
            XQueryParser.PathExprContext pathContext,
            XQueryParser.RelativePathExprContext doubleSlashContext
    ) {
        Token leadingDoubleSlash = pathContext.getStart();
        FunctionCallExpression functionCallExpression = new FunctionCallExpression(
                Name.createVariableInDefaultBuiltinFunctionNamespace("root"),
                Collections.emptyList(),
                createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash)
        );
        StepExpr stepExpr = new ForwardStepExpr(
                ForwardAxis.DESCENDANT_OR_SELF,
                new AnyKindTest(),
                createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash)
        );
        Expression starter = new SlashExpr(
                functionCallExpression,
                stepExpr,
                createMetadataFromRange(leadingDoubleSlash, leadingDoubleSlash)
        );
        return getSlashes(doubleSlashContext, starter, leadingDoubleSlash);
    }

    private Node visitSingleSlash(
            XQueryParser.PathExprContext pathContext,
            XQueryParser.RelativePathExprContext singleSlashContext
    ) {
        Token leadingSlash = pathContext.getStart();
        FunctionCallExpression functionCallExpression = new FunctionCallExpression(
                Name.createVariableInDefaultBuiltinFunctionNamespace("root"),
                Collections.emptyList(),
                createMetadataFromRange(leadingSlash, leadingSlash)
        );
        return getSlashes(singleSlashContext, functionCallExpression, leadingSlash);
    }

    /**
     * This method takes a leftMost expression and a path and returns a nested tree of slash expressions which
     * correspond to the steps in the path applied to the leftMost expression
     */
    private Expression getSlashes(
            XQueryParser.RelativePathExprContext relativePathExprContext,
            Expression leftMost
    ) {
        return getSlashes(relativePathExprContext, leftMost, relativePathExprContext.getStart());
    }

    private Expression getSlashes(
            XQueryParser.RelativePathExprContext relativePathExprContext,
            Expression leftMost,
            Token expressionStart
    ) {
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
                            relativePathExprContext.sep.get(i - 1),
                            relativePathExprContext.sep.get(i - 1)
                        )
                );
                if (currentTop == null) {
                    currentTop = intermediaryStepExpr;
                } else {
                    currentTop = new SlashExpr(
                            currentTop,
                            intermediaryStepExpr,
                            createMetadataFromRange(
                                expressionStart,
                                relativePathExprContext.sep.get(i - 1)
                            )
                    );

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
                            relativePathExprContext.stepExpr(i).getStop()
                        )
                );
            }
        }
        return currentTop;
    }

    @Override
    public Node visitStepExpr(XQueryParser.StepExprContext ctx) {
        if (ctx.postfixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (XQueryParser.PredicateContext predicateContext : ctx.axisStep().predicateList().predicate()) {
                Expression predicate = (Expression) this.visitPredicate(predicateContext);
                stepExpr = new FilterExpression(
                        stepExpr,
                        predicate,
                        createMetadataFromRange(ctx.getStart(), predicateContext.getStop())
                );
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
            } else if (nodeTest instanceof AttributeTest) {
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
            XQueryParser.NodeTestContext nodeTestContext,
            boolean unprefixedUsesDefaultElementNamespace
    ) {
        if (nodeTestContext.nameTest() == null) {
            // kind test
            return getKindTest(nodeTestContext.kindTest().children.get(0));
        }
        if (nodeTestContext.nameTest().wildcard() == null) {
            Name name = parseEqName(
                nodeTestContext.nameTest().eqName(),
                false,
                false,
                false,
                unprefixedUsesDefaultElementNamespace
            );
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
        if (kindTest instanceof XQueryParser.DocumentTestContext docContext) {
            // XQuery 3.1 Section 2.5.5.3 - Element Test (used within DocumentTest)
            // DocumentTest ::= "document-node" "(" (ElementTest | SchemaElementTest)? ")"
            // document-node() matches any document node.
            // document-node(element(...)) matches a document node containing an element matching the ElementTest.
            if (docContext.schemaElementTest() != null) {
                throw new UnsupportedFeatureException(
                        "Schema element tests within document-node() are not supported",
                        createMetadataFromContext((ParserRuleContext) kindTest)
                );
            }
            if (docContext.elementTest() == null) {
                return new DocumentTest(null);
            }
            return new DocumentTest(getKindTest(docContext.elementTest()));
        } else if (kindTest instanceof XQueryParser.ElementTestContext elementContext) {
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
                        createMetadataFromContext((ParserRuleContext) kindTest)
                );
            }
            Name elementName;
            if (elementContext.elementNameOrWildcard() != null) {
                boolean hasWildcard = elementContext.elementNameOrWildcard().elementName() == null;
                if (!hasWildcard) {
                    elementName = parseEqName(
                        elementContext.elementNameOrWildcard().elementName().eqName(),
                        false,
                        false,
                        false,
                        true
                    );
                    if (elementContext.typeName() == null) {
                        return new ElementTest(elementName, null);
                    }
                    Name typeName = parseEqName(elementContext.typeName().eqName(), false, true, false, false);
                    return new ElementTest(elementName, typeName);
                }
                // Wildcard case: element(*) or element(*, type)
                if (elementContext.typeName() != null) {
                    Name typeName = parseEqName(elementContext.typeName().eqName(), false, true, false, false);
                    return new ElementTest(typeName);
                }
                return new ElementTest(true);
            }
            return new ElementTest();
        } else if (kindTest instanceof XQueryParser.AttributeTestContext attributeTestContext) {
            // XQuery 3.1 Section 2.5.5.5 - Attribute Test
            // AttributeTest ::= "attribute" "(" (AttribNameOrWildcard ("," TypeName)?)? ")"
            // attribute() and attribute(*) match any single attribute node.
            // attribute(N) matches any attribute node whose name is N.
            // attribute(N, T) matches an attribute node whose name is N and whose type annotation is T.
            // attribute(*, T) matches any attribute node whose type annotation is T.
            Name attributeName;
            if (attributeTestContext.attributeNameOrWildcard() != null) {
                boolean hasWildcard = attributeTestContext.attributeNameOrWildcard().attributeName() == null;
                if (!hasWildcard) {
                    attributeName = parseEqName(
                        attributeTestContext.attributeNameOrWildcard().attributeName().eqName(),
                        false,
                        false,
                        false,
                        false
                    );
                    if (attributeTestContext.typeName() != null) {
                        Name typeName = parseEqName(
                            attributeTestContext.typeName().eqName(),
                            false,
                            true,
                            false,
                            false
                        );
                        return new AttributeTest(attributeName, typeName);
                    } else {
                        return new AttributeTest(attributeName, null);
                    }
                } else {
                    // Wildcard case: attribute(*) or attribute(*, type)
                    if (attributeTestContext.typeName() != null) {
                        Name typeName = parseEqName(
                            attributeTestContext.typeName().eqName(),
                            false,
                            true,
                            false,
                            false
                        );
                        return new AttributeTest(typeName);
                    }
                    return new AttributeTest(true);
                }
            }
            return new AttributeTest();
        } else if (kindTest instanceof XQueryParser.TextTestContext) {
            // XQuery 3.1 Section 2.5.5
            // TextTest ::= "text" "(" ")"
            // A TextTest matches any text node.
            return new TextTest();
        } else if (kindTest instanceof XQueryParser.CommentTestContext) {
            // XQuery 3.1 Section 2.5.5
            // CommentTest ::= "comment" "(" ")"
            // A CommentTest matches any comment node.
            return new CommentTest();
        } else if (kindTest instanceof XQueryParser.PiTestContext piContext) {
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
        } else if (kindTest instanceof XQueryParser.NamespaceNodeTestContext) {
            // XQuery 3.1 Section 2.5.5
            // NamespaceNodeTest ::= "namespace-node" "(" ")"
            // A NamespaceNodeTest matches any namespace node.
            return new NamespaceNodeTest();
        } else if (kindTest instanceof XQueryParser.AnyKindTestContext) {
            // XQuery 3.1 Section 2.5.5
            // AnyKindTest ::= "node" "(" ")"
            // node() matches any node.
            return new AnyKindTest();
        } else if (kindTest instanceof XQueryParser.SchemaElementTestContext) {
            // XQuery 3.1 Section 2.5.5.4 - Schema Element Test (unsupported, requires schema import)
            throw new UnsupportedFeatureException(
                    "Schema element tests (schema-element(...)) are not supported",
                    createMetadataFromContext((ParserRuleContext) kindTest)
            );
        } else if (kindTest instanceof XQueryParser.SchemaAttributeTestContext) {
            // XQuery 3.1 Section 2.5.5.6 - Schema Attribute Test (unsupported, requires schema import)
            throw new UnsupportedFeatureException(
                    "Schema attribute tests (schema-attribute(...)) are not supported",
                    createMetadataFromContext((ParserRuleContext) kindTest)
            );
        } else {
            throw new UnsupportedFeatureException(
                    "Unsupported kind test: " + kindTest.getText(),
                    createMetadataFromContext((ParserRuleContext) kindTest)
            );
        }
    }


    // end region

    public void processNamespaceDecl(XQueryParser.NamespaceDeclContext ctx) {
        bindNamespace(
            ctx.ncName().getText(),
            processURILiteral(ctx.uriLiteral()),
            createMetadataFromContext(ctx)
        );
    }

    public void bindNamespace(String prefix, String namespace, ExceptionMetadata metadata) {
        if (!prefix.isEmpty() && namespace.isEmpty()) {
            if (this.moduleContext.unbindNamespace(prefix)) {
                return;
            }
            throw new NamespacePrefixBoundTwiceException(
                    "Prefix " + prefix + " is bound twice.",
                    metadata
            );
        }
        boolean success = this.moduleContext.bindNamespace(
            prefix,
            namespace
        );
        if (!success) {
            throw new NamespacePrefixBoundTwiceException(
                    "Prefix " + prefix + " is bound twice.",
                    metadata
            );
        }
    }

    private String processURILiteral(UriLiteralContext ctx) {
        // According to XQuery 3.1 spec, URI literals (which are string literals) must expand
        // predefined entity references and character references
        return processStringLiteral(ctx.stringLiteral());
    }

    private void processEmptySequenceOrder(EmptyOrderDeclContext ctx) {
        if (ctx.emptySequenceOrder.getText().equals("least")) {
            this.moduleContext.setEmptySequenceOrderLeast(true);
        }
        if (ctx.emptySequenceOrder.getText().equals("greatest")) {
            this.moduleContext.setEmptySequenceOrderLeast(false);
        }
    }

    private void processDefaultCollation(DefaultCollationDeclContext ctx) {
        String uri = processURILiteral(ctx.uriLiteral());
        if (!this.moduleContext.isStaticallyKnownCollation(uri)) {
            throw new DefaultCollationException(
                    "Unknown collation: " + uri,
                    createMetadataFromContext(ctx.uriLiteral())
            );
        }
        this.moduleContext.setDefaultCollation(uri);
    }

    public LibraryModule processModuleImport(XQueryParser.ModuleImportContext ctx) {
        String namespace = processURILiteral(ctx.targetNamespace);
        List<String> locationHints = ctx.locations.stream()
            .map(this::processURILiteral)
            .collect(Collectors.toList());
        LibraryModule libraryModule = ModuleImportLoader.load(
            namespace,
            locationHints,
            this.moduleContext,
            this.compilationConfiguration,
            createMetadataFromContext(ctx)
        );
        if (ctx.ncName() != null) {
            bindNamespace(
                ctx.ncName().getText(),
                libraryModule.getNamespace(),
                createMetadataFromContext(ctx)
            );
        }
        return libraryModule;
    }

    public ExceptionMetadata generateMetadata(Token start, Token end) {
        return ExceptionMetadata.fromTokens(this.moduleContext.getStaticBaseURI().toString(), start, end, this.code);
    }

    private List<Annotation> processAnnotations(XQueryParser.AnnotationsContext annotations) {
        return processAnnotations(annotations.annotation());
    }

    private List<Annotation> processAnnotations(List<XQueryParser.AnnotationContext> annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (XQueryParser.AnnotationContext annotationContext : annotations) {
            XQueryParser.EqNameContext eqNameContext = annotationContext.eqName();
            Name name = parseEqName(eqNameContext, false, false, true, false);
            Annotation.validateAnnotationName(name, createMetadataFromContext(annotationContext));
            List<Expression> literals = null;
            if (!annotationContext.literal().isEmpty()) {
                literals = new ArrayList<>();
                for (XQueryParser.LiteralContext literalContext : annotationContext.literal()) {
                    literals.add((Expression) this.visitLiteral(literalContext));
                }
            }
            parsedAnnotations.add(new Annotation(name, literals));
        }

        return parsedAnnotations;
    }

    private String resolvePrefixForDirConstructor(String prefix) {
        for (Map<String, String> frame : this.dirElemNamespaceFrames) {
            if (frame.containsKey(prefix)) {
                return frame.get(prefix);
            }
        }
        return this.moduleContext.resolveNamespace(prefix);
    }

    private void bindDirConstructorNamespaceDeclaration(String prefix, String uri) {
        if (this.dirElemNamespaceFrames.isEmpty()) {
            return;
        }
        this.dirElemNamespaceFrames.peek().put(prefix, uri);
    }

    private static class DirAttributeProcessingResult {
        private final List<Expression> attributes;
        private final List<NamespaceDeclaration> namespaceDeclarations;

        private DirAttributeProcessingResult() {
            this.attributes = new ArrayList<>();
            this.namespaceDeclarations = new ArrayList<>();
        }
    }


    private DirAttributeProcessingResult getAttributesExpressionsList(XQueryParser.DirAttributeListContext ctx) {
        DirAttributeProcessingResult result = new DirAttributeProcessingResult();

        // Process each attribute name-value pair
        List<XQueryParser.QnameContext> attributeNames = ctx.attribute_qname;
        List<XQueryParser.DirAttributeValueContext> attributeValues = ctx.attribute_value;

        for (int i = 0; i < attributeNames.size(); i++) {
            XQueryParser.QnameContext qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if ("xmlns".equals(lexical) || lexical.startsWith("xmlns:")) {
                String declaredPrefix = "xmlns".equals(lexical) ? "" : lexical.substring("xmlns:".length());
                String uri = getNamespaceDeclarationUri(attributeValues.get(i));
                result.namespaceDeclarations.add(
                    new NamespaceDeclaration(declaredPrefix, uri, createMetadataFromContext(qnameCtx))
                );
                bindDirConstructorNamespaceDeclaration(declaredPrefix, uri);
                continue;
            }

            Name attributeName = parseName(qnameCtx, false, false, false, false);

            List<Expression> value = this.getAttributeValuesExpressionsList(attributeValues.get(i), true);
            AttributeNodeExpression attributeNode = new AttributeNodeExpression(
                    attributeName,
                    value,
                    createMetadataFromRange(qnameCtx.getStart(), attributeValues.get(i).getStop())
            );
            result.attributes.add(attributeNode);
        }

        return result;
    }

    private List<Expression> getAttributeValuesExpressionsList(
            XQueryParser.DirAttributeValueContext ctx,
            boolean allowEnclosedExpressions
    ) {
        ParseTree child = ctx.children.get(0);
        ParserRuleContext quotedValue;
        if (child instanceof XQueryParser.DirAttributeValueQuotContext doubleQuotedValue) {
            quotedValue = doubleQuotedValue;
        } else if (child instanceof XQueryParser.DirAttributeValueAposContext singleQuotedValue) {
            quotedValue = singleQuotedValue;
        } else {
            throw new UnsupportedOperationException("Unsupported attribute value: " + ctx.getText());
        }
        return processQuotedAttributeValue(
            quotedValue,
            allowEnclosedExpressions
        );
    }

    private String getNamespaceDeclarationUri(XQueryParser.DirAttributeValueContext ctx) {
        List<Expression> uriExpressions = this.getAttributeValuesExpressionsList(ctx, false);
        StringBuilder uriBuilder = new StringBuilder();
        for (Expression expression : uriExpressions) {
            if (!(expression instanceof AttributeNodeContentExpression attributeContent)) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx)
                );
            }
            uriBuilder.append(attributeContent.getContent());
        }
        return uriBuilder.toString();
    }


    private List<Expression> processQuotedAttributeValue(
            ParserRuleContext ctx,
            boolean allowEnclosedExpressions
    ) {
        return DirectConstructorUtils.processQuotedValue(
            this.xQueryTokenStream,
            ctx,
            allowEnclosedExpressions,
            this::createMetadataFromTree,
            this::createMetadataFromTrees,
            this::processAttributeContent
        );
    }

    private List<Expression> processAttributeContent(ParserRuleContext ctx, boolean allowEnclosedExpressions) {
        ParseTree child = ctx.children.get(0);

        if (
            ctx instanceof XQueryParser.DirAttributeContentQuotContext dirAttributeContentQuotContext
                && dirAttributeContentQuotContext.expr() != null
        ) {
            // Evaluate an enclosed expression in a double-quoted attribute.
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx)
                );
            }
            return List.of((Expression) this.visitExpr(dirAttributeContentQuotContext.expr()));
        } else if (
            ctx instanceof XQueryParser.DirAttributeContentAposContext dirAttributeContentAposContext
                && dirAttributeContentAposContext.expr() != null
        ) {
            // Evaluate an enclosed expression in an apostrophe-quoted attribute.
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx)
                );
            }
            return List.of((Expression) this.visitExpr(dirAttributeContentAposContext.expr()));
        }

        // Preserve literal content after validating direct attribute restrictions.
        String childText = this.xQueryTokenStream.getText(ctx.getSourceInterval());
        DirectConstructorUtils.validateLiteral(childText, ctx, this::createMetadataFromTree);
        String processedContent = DirectConstructorUtils.processLiteralContent(childText);
        return List.of(new AttributeNodeContentExpression(processedContent, createMetadataFromTree(child)));
    }

    @Override
    public Node visitDecimalFormatDecl(XQueryParser.DecimalFormatDeclContext ctx) {
        return visitChildren(ctx);
    }

    private void processDecimalFormatDeclaration(
            XQueryParser.DecimalFormatDeclContext ctx,
            ExceptionMetadata metadata
    ) {
        DecimalFormatDeclarationProcessor.process(
            ctx.KW_DEFAULT() != null,
            ctx.eqName(),
            ctx.DFPropertyName(),
            ctx.stringLiteral()
                .stream()
                .map(stringLiteral -> this.xQueryTokenStream.getText(stringLiteral.getSourceInterval()))
                .toList(),
            this.moduleContext,
            false,
            metadata
        );
    }
}
