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

package org.rumbledb.compiler;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.bindings.DataFrameBinding;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
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
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
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
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
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
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
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
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
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
import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.parser.jsoniq.JsoniqParserBaseVisitor;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.jsoniq.JsoniqParser.DefaultCollationDeclContext;
import org.rumbledb.parser.jsoniq.JsoniqParser.EmptyOrderDeclContext;
import org.rumbledb.parser.jsoniq.JsoniqParser.SetterContext;
import org.rumbledb.parser.jsoniq.JsoniqParser.UriLiteralContext;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.ItemTypeReference;
import org.rumbledb.types.SequenceType;

import java.io.IOException;
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
public class TranslationVisitor extends JsoniqParserBaseVisitor<Node> {

    private StaticContext moduleContext;
    private RumbleConfiguration configuration;
    private ExternalBindings externalBindings;
    private boolean isMainModule;
    private String code;
    private ArrayDeque<Map<String, String>> dirElemNamespaceFrames;
    private final CommonTokenStream xQueryTokenStream;

    public TranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            RumbleConfiguration configuration,
            ExternalBindings externalBindings,
            String code,
            CommonTokenStream xQueryTokenStream
    ) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindDefaultNamespaces();
        this.configuration = configuration;
        this.externalBindings = externalBindings;
        this.isMainModule = isMainModule;
        this.code = code;
        this.dirElemNamespaceFrames = new ArrayDeque<>();
        this.xQueryTokenStream = xQueryTokenStream;

        String queryLanguage = configuration.semantics().queryLanguage();
        if (queryLanguage.equals("jsoniq10")) {
            this.moduleContext.setQueryLanguage("jsoniq10");
        } else if (queryLanguage.equals("jsoniq31")) {
            this.moduleContext.setQueryLanguage("jsoniq31");
        } else if (queryLanguage.equals("jsoniq40")) {
            this.moduleContext.setQueryLanguage("jsoniq40");
        }
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty()) {
            String version = processStringLiteral(ctx.vers).trim();
            if (version.equals("1.0")) {
                this.moduleContext.setQueryLanguage("jsoniq10");
            } else if (version.equals("3.1")) {
                this.moduleContext.setQueryLanguage("jsoniq31");
            } else if (version.equals("4.0")) {
                this.moduleContext.setQueryLanguage("jsoniq40");
            } else {
                throw new JsoniqVersionException(createMetadataFromContext(ctx));
            }
        }
        if (this.isMainModule) {
            if (ctx.mainModule() != null) {
                return this.visitMainModule(ctx.mainModule());
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
    public Node visitMainModule(JsoniqParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        // We override with a context item declaration if not present already.
        Program program = (Program) this.visitProgram(ctx.program());
        if (!prolog.hasContextItemDeclaration() && getExternalVariableType(Name.CONTEXT_ITEM) != null) {
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

        for (Name externalVariable : this.externalBindings.names()) {
            if (externalVariable.equals(Name.CONTEXT_ITEM) || hasDeclaration(prolog, externalVariable)) {
                continue;
            }

            SequenceType sequenceType = getExternalVariableType(externalVariable);
            if (sequenceType != null) {
                prolog.addDeclaration(
                    new VariableDeclaration(
                            externalVariable,
                            true,
                            sequenceType,
                            null,
                            null,
                            createMetadataFromContext(ctx)
                    )
                );
            }
        }

        MainModule module = new MainModule(prolog, program, createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

    private boolean hasDeclaration(Prolog prolog, Name variableName) {
        for (VariableDeclaration declaration : prolog.getVariableDeclarations()) {
            if (declaration.getVariableName().equals(variableName)) {
                return true;
            }
        }
        return false;
    }

    private SequenceType getExternalVariableType(Name variableName) {
        DataFrameBinding dataFrameBinding = this.externalBindings.get(variableName, DataFrameBinding.class)
            .orElse(null);
        if (dataFrameBinding != null) {
            Dataset<Row> dataFrame = dataFrameBinding.getDataFrame();
            ItemType itemType = ItemTypeFactory.createItemType(dataFrame.schema());
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }

        if (this.externalBindings.get(variableName).isPresent()) {
            return SequenceType.createSequenceType("item*");
        }
        return null;
    }

    // region program
    @Override
    public Node visitProgram(JsoniqParser.ProgramContext ctx) {
        StatementsAndOptionalExpr statementsAndOptionalExpr = (StatementsAndOptionalExpr) this
            .visitStatementsAndOptionalExpr(ctx.statementsAndOptionalExpr());
        return new Program(statementsAndOptionalExpr, createMetadataFromContext(ctx));
    }

    // end region

    @Override
    public Node visitLibraryModule(JsoniqParser.LibraryModuleContext ctx) {
        String prefix = ctx.ncName().getText();
        String namespace = processURILiteral(ctx.uriLiteral());
        if (namespace.equals("")) {
            throw new EmptyModuleURIException("Module URI is empty.", createMetadataFromContext(ctx));
        }
        URI resolvedURI = FileSystemUtil.resolveURI(
            this.moduleContext.getStaticBaseURI(),
            namespace,
            createMetadataFromContext(ctx)
        );
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
    public Node visitProlog(JsoniqParser.PrologContext ctx) {
        // bind namespaces
        for (JsoniqParser.NamespaceDeclContext namespace : ctx.namespaceDecl()) {
            this.processNamespaceDecl(namespace);
        }
        List<SetterContext> setters = ctx.setter();
        boolean emptyOrderSet = false;
        boolean defaultCollationSet = false;
        boolean baseURISet = false;
        for (SetterContext setterContext : setters) {
            if (setterContext.emptyOrderDecl() != null) {
                if (emptyOrderSet) {
                    throw new MoreThanOneEmptyOrderDeclarationException(
                            "The empty order was already set.",
                            createMetadataFromContext(setterContext.emptyOrderDecl())
                    );
                }
                processEmptySequenceOrder(setterContext.emptyOrderDecl());
                emptyOrderSet = true;
                continue;
            }
            JsoniqParser.DecimalFormatDeclContext decimalFormatDeclContext = setterContext.decimalFormatDecl();
            if (decimalFormatDeclContext != null) {
                processDecimalFormatDeclaration(
                    decimalFormatDeclContext,
                    createMetadataFromContext(decimalFormatDeclContext)
                );
                continue;
            }
            if (setterContext.defaultCollationDecl() != null) {
                if (defaultCollationSet) {
                    throw new DefaultCollationException(
                            "The default collation was already set.",
                            createMetadataFromContext(setterContext.defaultCollationDecl())
                    );
                }
                processDefaultCollation(setterContext.defaultCollationDecl());
                defaultCollationSet = true;
                continue;
            }
            if (setterContext.baseURIDecl() != null) {
                if (baseURISet) {
                    throw new MultipleBaseURIException(
                            "The base-uri was already set.",
                            createMetadataFromContext(setterContext.baseURIDecl())
                    );
                }
                String uriString = processURILiteral(setterContext.baseURIDecl().uriLiteral());
                URI uri = FileSystemUtil.resolveURI(
                    this.moduleContext.getStaticBaseURI(),
                    uriString,
                    ExceptionMetadata.EMPTY_METADATA
                );
                this.moduleContext.setStaticBaseUri(uri);
                baseURISet = true;
                continue;
            }
            throw new UnsupportedFeatureException(
                    "Setters are not supported yet, except for empty sequence ordering and default collations.",
                    createMetadataFromContext(setterContext)
            );
        }
        List<LibraryModule> libraryModules = new ArrayList<>();
        Set<String> namespaces = new HashSet<>();
        for (JsoniqParser.ModuleImportContext namespace : ctx.moduleImport()) {
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

        // parse variables and function
        List<VariableDeclaration> globalVariables = new ArrayList<>();
        List<FunctionDeclaration> functionDeclarations = new ArrayList<>();
        List<TypeDeclaration> typeDeclarations = new ArrayList<>();
        for (JsoniqParser.AnnotatedDeclContext annotatedDeclaration : ctx.annotatedDecl()) {
            if (annotatedDeclaration.varDecl() != null) {
                VariableDeclaration variableDeclaration = (VariableDeclaration) this.visitVarDecl(
                    annotatedDeclaration.varDecl()
                );
                if (!this.isMainModule) {
                    String moduleNamespace = this.moduleContext.getStaticBaseURI().toString();
                    String variableNamespace = variableDeclaration.getVariableName().getNamespace();
                    if (variableNamespace == null || !variableNamespace.equals(moduleNamespace)) {
                        throw new NamespaceDoesNotMatchModuleException(
                                "Variable "
                                    + variableDeclaration.getVariableName().getLocalName()
                                    + ": namespace "
                                    + variableNamespace
                                    + " must match module namespace "
                                    + moduleNamespace,
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
                    String moduleNamespace = this.moduleContext.getStaticBaseURI().toString();
                    String functionNamespace = inlineFunctionExpression.getName().getNamespace();
                    if (functionNamespace == null || !functionNamespace.equals(moduleNamespace)) {
                        throw new NamespaceDoesNotMatchModuleException(
                                "Function "
                                    + inlineFunctionExpression.getName().getLocalName()
                                    + ": namespace "
                                    + functionNamespace
                                    + " must match module namespace "
                                    + moduleNamespace,
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
            } else if (annotatedDeclaration.typeDecl() != null) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) this.visitTypeDecl(
                    annotatedDeclaration.typeDecl()
                );

                if (!this.isMainModule) {
                    String moduleNamespace = this.moduleContext.getStaticBaseURI().toString();
                    String typeNamespace = typeDeclaration.getDefinition().getName().getNamespace();
                    if (typeNamespace == null || !typeNamespace.equals(moduleNamespace)) {
                        throw new NamespaceDoesNotMatchModuleException(
                                "Type "
                                    + typeDeclaration.getDefinition().getName().getLocalName()
                                    + ": namespace "
                                    + typeNamespace
                                    + " must match module namespace "
                                    + moduleNamespace,
                                createMetadataFromContext(annotatedDeclaration.typeDecl())
                        );
                    }
                }
                typeDeclarations.add(typeDeclaration);
            }
        }
        for (JsoniqParser.ModuleImportContext module : ctx.moduleImport()) {
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
        return prolog;
    }

    private String processStringLiteral(JsoniqParser.StringLiteralContext ctx) {
        String rawValue = ctx.getText().substring(1, ctx.getText().length() - 1);
        return unescapeStringLiteral(rawValue);
    }

    private Item parseJSONItem(String value, ExceptionMetadata metadata) {
        return ItemParser.getItemFromJSONString(
            value,
            JSONParsingOptions.defaultInstance(true),
            this.configuration.semantics().xmlVersion(),
            true,
            metadata
        );
    }

    private Name nameForUnprefixedFunction(String localName) {
        String uri = this.moduleContext.getDefaultFunctionNamespaceUri();
        if (uri != null) {
            return new Name(uri, "", localName);
        }
        return Name.createVariableInDefaultFunctionNamespace(localName);
    }

    public Name parseFunctionName(JsoniqParser.FunctionNameContext ctx) {
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
            JsoniqParser.EqNameContext ctx,
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
     * bound; otherwise {@link Name#createVariableInDefaultAnnotationsNamespace}.</li>
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
            JsoniqParser.QnameContext ctx,
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
                    name = Name.createVariableInDefaultAnnotationsNamespace(localName);
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
    public Node visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        Name name = parseFunctionName(ctx.functionName());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false, false, false);
                paramType = SequenceType.createSequenceType("item*");
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

    @Override
    public Node visitTypeDecl(JsoniqParser.TypeDeclContext ctx) {
        String definitionString = ctx.type_definition.getText();
        Item definitionItem = null;
        if (definitionString.trim().startsWith("\"")) {
            throw new InvalidSchemaException(
                    "The schema definition must be an object.",
                    createMetadataFromContext(ctx)
            );
        }
        if (definitionString.trim().startsWith("[")) {
            throw new InvalidSchemaException(
                    "Schema definitions for top-level array types are not supported yet. Please let us know if you would like for us to prioritize this feature.",
                    createMetadataFromContext(ctx)
            );
        }
        try {
            definitionItem = parseJSONItem(definitionString, createMetadataFromContext(ctx));
        } catch (InvalidJSONException | ParsingException e) {
            ParsingException pe = new ParsingException(
                    "A type definition must be a JSON literal: no dynamic evaluation is allowed.",
                    createMetadataFromContext(ctx)
            );
            pe.initCause(e);
            throw pe;
        }
        Name name = parseName(ctx.qname(), false, true, false, false);
        String schemaLanguage = null;
        if (ctx.schema != null) {
            schemaLanguage = ctx.schema.getText();
        } else {
            schemaLanguage = "jsoundcompact";
        }
        ItemType type = null;
        switch (schemaLanguage) {
            case "jsoundcompact":
                type = ItemTypeFactory.createItemTypeFromJSoundCompactItem(name, definitionItem, this.moduleContext);
                break;
            case "jsoundverbose":
                type = ItemTypeFactory.createItemTypeFromJSoundVerboseItem(name, definitionItem, this.moduleContext);
                break;
            case "jsonschema":
                type = ItemTypeFactory.createItemTypeFromJSONSchemaItem(name, definitionItem, this.moduleContext);
                break;
            default:
                throw new OurBadException(
                        "Unrecognized schema syntax: " + schemaLanguage,
                        createMetadataFromContext(ctx)
                );
        }
        return new TypeDeclaration(
                type,
                createMetadataFromContext(ctx)
        );
    }
    // endregion

    // region expr
    @Override
    public Node visitExpr(JsoniqParser.ExprContext ctx) {
        List<Expression> expressions = new ArrayList<>();
        for (JsoniqParser.ExprSingleContext expr : ctx.exprSingle()) {
            expressions.add((Expression) this.visitExprSingle(expr));
        }
        if (expressions.size() == 1) {
            return expressions.get(0);
        }
        return new CommaExpression(expressions, createMetadataFromContext(ctx));
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
        throw new OurBadException("Unrecognized ExprSingle:" + content.getClass().getName());
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
        // empty expression
        if (ctx.expr() == null) {
            return null;
        }
        return this.visitExpr(ctx.expr());
    }
    // endregion

    // region Flowr
    @Override
    public Node visitFlworExpr(JsoniqParser.FlworExprContext ctx) {
        Clause clause;
        // check the start clause, for or let
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }

        Clause previousFLWORClause = clause.getLastClause();

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
    public Node visitForClause(JsoniqParser.ForClauseContext ctx) {
        ForClause clause = null;
        for (JsoniqParser.ForVarContext var : ctx.vars) {
            ForClause newClause = (ForClause) this.visitForVar(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitForVar(JsoniqParser.ForVarContext ctx) {
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
    public Node visitLetClause(JsoniqParser.LetClauseContext ctx) {
        LetClause clause = null;
        for (JsoniqParser.LetVarContext var : ctx.vars) {
            LetClause newClause = (LetClause) this.visitLetVar(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitLetVar(JsoniqParser.LetVarContext ctx) {
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
    public Node visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        List<GroupByVariableDeclaration> vars = new ArrayList<>();
        GroupByVariableDeclaration child;
        for (JsoniqParser.GroupByVarContext var : ctx.vars) {
            child = this.processGroupByVar(var);
            vars.add(child);
        }
        return new GroupByClause(vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        boolean stable = false;
        List<OrderByClauseSortingKey> exprs = new ArrayList<>();
        OrderByClauseSortingKey child;
        for (JsoniqParser.OrderByExprContext var : ctx.orderByExpr()) {
            child = this.processOrderByExpr(var);
            exprs.add(child);
        }
        if (ctx.stb != null && !ctx.stb.getText().isEmpty()) {
            stable = true;
        }
        return new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
    }

    public OrderByClauseSortingKey processOrderByExpr(JsoniqParser.OrderByExprContext ctx) {
        if (ctx.uriLiteral() != null) {
            String collation = processURILiteral(ctx.uriLiteral());
            if (!collation.equals(Name.DEFAULT_COLLATION_NS)) {
                throw new DefaultCollationException(
                        "Unknown collation: " + collation,
                        createMetadataFromContext(ctx.uriLiteral())
                );
            }
        }
        boolean ascending = true;
        if (ctx.desc != null && !ctx.desc.getText().isEmpty()) {
            ascending = false;
        }
        String uri = null;
        if (ctx.uriLiteral() != null) {
            uri = ctx.uriLiteral().getText();
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

    public GroupByVariableDeclaration processGroupByVar(JsoniqParser.GroupByVarContext ctx) {
        if (ctx.uriLiteral() != null) {
            String collation = processURILiteral(ctx.uriLiteral());
            if (!collation.equals(Name.DEFAULT_COLLATION_NS)) {
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
    public Node visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        Expression expr = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new WhereClause(expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCountClause(JsoniqParser.CountClauseContext ctx) {
        VariableReferenceExpression child = (VariableReferenceExpression) this.visitVarRef(ctx.varRef());
        return new CountClause(child.getVariableName(), createMetadataFromContext(ctx));
    }
    // endregion

    // region operational
    @Override
    public Node visitOrExpr(JsoniqParser.OrExprContext ctx) {
        Expression result = (Expression) this.visitAndExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (JsoniqParser.AndExprContext child : ctx.rhs) {
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
    public Node visitAndExpr(JsoniqParser.AndExprContext ctx) {
        Expression result = (Expression) this.visitNotExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (JsoniqParser.NotExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitNotExpr(child);
            result = new AndExpression(
                    result,
                    rightExpression,
                    createMetadataFromRange(ctx.main_expr.getStart(), child.getStop())
            );
        }
        return result;
    }

    @Override
    public Node visitNotExpr(JsoniqParser.NotExprContext ctx) {
        Expression mainExpression = (Expression) this.visitComparisonExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        return new NotExpression(
                mainExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        Expression mainExpression = (Expression) this.visitStringConcatExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.StringConcatExprContext child = ctx.rhs.get(0);
        Expression childExpression = (Expression) this.visitStringConcatExpr(child);

        ComparisonExpression.ComparisonOperator kind = ComparisonExpression.ComparisonOperator.fromSymbol(
            ctx.op.get(0).getText()
        );
        if (
            kind.isValueComparison() || this.configuration.optimization().optimizeGeneralComparisonToValueComparison()
        ) {
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
    public Node visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        Expression result = (Expression) this.visitRangeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (JsoniqParser.RangeExprContext child : ctx.rhs) {
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
    public Node visitRangeExpr(JsoniqParser.RangeExprContext ctx) {
        Expression mainExpression = (Expression) this.visitAdditiveExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.AdditiveExprContext child = ctx.rhs.get(0);
        Expression childExpression = (Expression) this.visitAdditiveExpr(child);
        return new RangeExpression(
                mainExpression,
                childExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        Expression result = (Expression) this.visitMultiplicativeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            JsoniqParser.MultiplicativeExprContext child = ctx.rhs.get(i);
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
    public Node visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        Expression result = (Expression) this.visitUnionExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            JsoniqParser.UnionExprContext child = ctx.rhs.get(i);
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
    public Node visitUnionExpr(JsoniqParser.UnionExprContext ctx) {
        Expression result = (Expression) this.visitIntersectExceptExpr(ctx.main_expr);
        for (JsoniqParser.IntersectExceptExprContext child : ctx.rhs) {
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
    public Node visitIntersectExceptExpr(JsoniqParser.IntersectExceptExprContext ctx) {
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
    public Node visitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx) {
        Expression result = (Expression) this.visitPathExpr(ctx.main_expr);
        if (ctx.map_expr == null || ctx.map_expr.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.map_expr.size(); ++i) {
            JsoniqParser.PathExprContext child = ctx.map_expr.get(i);
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
    public Node visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx) {
        Expression mainExpression = (Expression) this.visitIsStaticallyExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new InstanceOfExpression(
                mainExpression,
                sequenceType,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitIsStaticallyExpr(JsoniqParser.IsStaticallyExprContext ctx) {
        Expression mainExpression = (Expression) this.visitTreatExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new IsStaticallyExpression(
                mainExpression,
                sequenceType,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitTreatExpr(JsoniqParser.TreatExprContext ctx) {
        Expression mainExpression = (Expression) this.visitCastableExpr(ctx.main_expr);
        if (ctx.seq == null || ctx.seq.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SequenceTypeContext child = ctx.seq;
        SequenceType sequenceType = this.processSequenceType(child);
        return new TreatExpression(
                mainExpression,
                sequenceType,
                ErrorCode.DynamicTypeTreatErrorCode,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCastableExpr(JsoniqParser.CastableExprContext ctx) {
        Expression mainExpression = (Expression) this.visitCastExpr(ctx.main_expr);
        if (ctx.single == null || ctx.single.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SingleTypeContext child = ctx.single;
        SequenceType sequenceType = this.processSingleType(child);
        return new CastableExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitCastExpr(JsoniqParser.CastExprContext ctx) {
        Expression mainExpression = (Expression) this.visitArrowExpr(ctx.main_expr);
        if (ctx.single == null || ctx.single.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SingleTypeContext child = ctx.single;
        SequenceType sequenceType = this.processSingleType(child);
        return new CastExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitArrowExpr(JsoniqParser.ArrowExprContext ctx) {
        Expression mainExpression = (Expression) this.visitUnaryExpr(ctx.main_expr);
        Expression functionExpression = null;

        for (int i = 0; i < ctx.function.size(); ++i) {
            JsoniqParser.ArrowFunctionSpecifierContext functionCallContext = ctx.function.get(i);
            JsoniqParser.ArgumentListContext argumentListContext = ctx.arguments.get(i);
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
    public Node visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
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
    public Node visitValueExpr(JsoniqParser.ValueExprContext ctx) {
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
                    if (this.moduleContext.getQueryLanguage().equals("jsoniq10")) {
                        keys.add(
                            new StringLiteralExpression(
                                    stepExpr.getNodeTest().toString(),
                                    lhs.getMetadata()
                            )
                        );
                    } else {
                        throw new ParsingException(
                                "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --default-language jsoniq10 CLI option.",
                                lhs.getMetadata()
                        );
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
        return new RenameExpression(
                mainExpression,
                locatorExpression,
                nameExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitReplaceExpr(JsoniqParser.ReplaceExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression newExpression = (Expression) this.visitExprSingle(ctx.replacer_expr);
        return new ReplaceExpression(
                mainExpression,
                locatorExpression,
                newExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitTransformExpr(JsoniqParser.TransformExprContext ctx) {
        List<CopyDeclaration> copyDecls = ctx.copyDecl()
            .stream()
            .map(copyDeclCtx -> {
                Name var = ((VariableReferenceExpression) this.visitVarRef(copyDeclCtx.var_ref)).getVariableName();
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
        return new CreateCollectionExpression(
                collection,
                contentExpression,
                mode,
                createMetadataFromContext(ctx)
        );
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
                collection,
                numDelete,
                isFirst,
                mode,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitDeleteSearchExpr(JsoniqParser.DeleteSearchExprContext ctx) {
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        return new DeleteSearchFromCollectionExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitEditCollectionExpr(JsoniqParser.EditCollectionExprContext ctx) {
        Expression targetExpression = (Expression) this.visitExprSingle(ctx.target);
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        return new EditCollectionExpression(
                targetExpression,
                contentExpression,
                createMetadataFromContext(ctx)
        );
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
                collection,
                contentExpression,
                pos,
                mode,
                isFirst,
                isLast,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitInsertSearchExpr(JsoniqParser.InsertSearchExprContext ctx) {
        Expression targetExpression = (Expression) this.visitExprSingle(ctx.target);
        Expression contentExpression = (Expression) this.visitExprSingle(ctx.content);
        boolean isBefore = (ctx.before != null);
        return new InsertSearchIntoCollectionExpression(
                targetExpression,
                contentExpression,
                isBefore,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitTruncateCollectionExpr(JsoniqParser.TruncateCollectionExprContext ctx) {
        Expression collectionName = (Expression) this.visitExprSimple(ctx.collection_name);
        Mode mode = Mode.fromString(ctx.collectionMode.getText());
        return new TruncateCollectionExpression(
                collectionName,
                mode,
                createMetadataFromContext(ctx)
        );
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
                        createMetadataFromRange(ctx.main_expr.getStart(), predicateContext.getStop())
                );
            } else if (child instanceof JsoniqParser.LookupContext lookupContext) {
                Expression expr = (Expression) this.visitLookup(lookupContext);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), lookupContext.getStop())
                );
            } else if (child instanceof JsoniqParser.ObjectLookupContext objectLookupContext) {
                Expression expr = (Expression) this.visitObjectLookup(objectLookupContext);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), objectLookupContext.getStop())
                );
            } else if (child instanceof JsoniqParser.ArrayLookupContext arrayLookupContext) {
                Expression expr = (Expression) this.visitArrayLookup(arrayLookupContext);
                mainExpression = new ArrayLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayLookupContext.getStop())
                );
            } else if (child instanceof JsoniqParser.ArrayUnboxingContext arrayUnboxingContext) {
                this.visitArrayUnboxing(arrayUnboxingContext);
                mainExpression = new ArrayUnboxingExpression(
                        mainExpression,
                        createMetadataFromRange(ctx.main_expr.getStart(), arrayUnboxingContext.getStop())
                );
            } else if (child instanceof JsoniqParser.ArgumentListContext argumentListContext) {
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

    public Node visitKeySpecifier(JsoniqParser.KeySpecifierContext ctx) {
        if (ctx.lt != null) {
            String rawValue = ctx.lt.getText().substring(1, ctx.lt.getText().length() - 1);
            return new StringLiteralExpression(
                    unescapeStringLiteral(rawValue),
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

    @Override
    public Node visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        // TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        if (ctx.lt != null) {
            String rawValue = ctx.lt.getText().substring(1, ctx.lt.getText().length() - 1);
            return new StringLiteralExpression(
                    unescapeStringLiteral(rawValue),
                    createMetadataFromContext(ctx)
            );
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
                    (Expression) this.visitUnaryLookup(unaryLookupContext),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof JsoniqParser.NodeConstructorContext nodeConstructorContext) {
            return this.visitNodeConstructor(nodeConstructorContext);
        }
        if (child instanceof JsoniqParser.NumericLiteralContext) {
            return this.visitLiteral((JsoniqParser.LiteralContext) child);
        }
        if (child instanceof TerminalNode) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        throw new UnsupportedFeatureException(
                "Primary expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitLiteral(JsoniqParser.LiteralContext ctx) {
        ParseTree child = ctx.children.get(0);

        if (child instanceof JsoniqParser.StringLiteralContext) {
            String rawValue = child.getText().substring(1, child.getText().length() - 1);
            return new StringLiteralExpression(
                    unescapeStringLiteral(rawValue),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof JsoniqParser.NumericLiteralContext) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        if (child instanceof JsoniqParser.LiteralContext) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        if (child instanceof TerminalNode) {
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

    private String unescapeStringLiteral(String raw) {
        return StringEscapeUtils.unescapeJson(raw);
    }

    @Override
    public Node visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
        // no merging constructor, just visit the k/v pairs
        if (
            ctx.merge_operator == null
                || ctx.merge_operator.size() == 0
                ||
                ctx.merge_operator.get(0).getText().isEmpty()
        ) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                Node lhs = this.visitExprSingle(currentPair.lhs);
                if (lhs instanceof StepExpr stepExpr) {
                    if (this.moduleContext.getQueryLanguage().equals("jsoniq10")) {
                        keys.add(
                            new StringLiteralExpression(
                                    stepExpr.getNodeTest().toString(),
                                    lhs.getMetadata()
                            )
                        );
                    } else {
                        throw new ParsingException(
                                "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --default-language jsoniq10 CLI option.",
                                lhs.getMetadata()
                        );
                    }
                } else {
                    keys.add((Expression) lhs);
                }
                values.add((Expression) this.visitExprSingle(currentPair.rhs));
            }
            if (this.moduleContext.getQueryLanguage().equals("jsoniq10")) {
                return new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
            } else {
                return new MapConstructorExpression(keys, values, createMetadataFromContext(ctx));
            }

        }

        Expression childExpr;
        childExpr = (Expression) this.visitExpr(ctx.expr());
        return new ObjectConstructorExpression(
                childExpr,
                createMetadataFromContext(ctx)
        );
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
        throw new UnsupportedFeatureException(
                "Node constructor not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitDirectConstructor(JsoniqParser.DirectConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (ctx.COMMENT() != null) {
            String commentText = ctx.COMMENT().getText();
            String commentContent = commentText.substring(4, commentText.length() - 3);
            return new DirectCommentConstructorExpression(
                    commentContent,
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof JsoniqParser.DirElemConstructorOpenCloseContext dirElemConstructorOpenCloseContext) {
            return this.visitDirElemConstructorOpenClose(dirElemConstructorOpenCloseContext);
        } else if (
            child instanceof JsoniqParser.DirElemConstructorSingleTagContext dirElemConstructorSingleTagContext
        ) {
            return this.visitDirElemConstructorSingleTag(dirElemConstructorSingleTagContext);
        } else if (ctx.PI() != null) {
            return this.visitDirPIConstructor(ctx.PI(), createMetadataFromContext(ctx));
        } else if (ctx.COMMENT() != null) {
            throw new UnsupportedFeatureException(
                    "Direct comment constructor not yet implemented",
                    createMetadataFromContext(ctx)
            );
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

    /**
     * Whitespace in direct element content is lexed on the HIDDEN channel. Recover hidden tokens that appear
     * between two adjacent {@code dirElemContent} parse nodes (each node’s own text uses
     * {@link CommonTokenStream#getText(Interval)} in {@link #visitDirElemContent}).
     */
    private static void appendHiddenTokensAfter(
            CommonTokenStream tokenStream,
            int previousTokenIndex,
            StringBuilder destination
    ) {
        List<Token> hidden = tokenStream.getHiddenTokensToRight(previousTokenIndex);
        if (hidden == null) {
            return;
        }
        for (Token t : hidden) {
            destination.append(t.getText());
        }
    }

    @Override
    public Node visitDirElemConstructorOpenClose(JsoniqParser.DirElemConstructorOpenCloseContext ctx) {
        // check that the start and end tags are the same
        if (ctx.close_tag_name != null && !ctx.close_tag_name.getText().equals(ctx.open_tag_name.getText())) {
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

            // Document and Element Nodes impose the constraint that two consecutive Text Nodes can never occur as
            // adjacent siblings.
            // see https://www.w3.org/TR/xpath-datamodel-31/#TextNodeOverview
            // here, we merge adjacent text nodes into a single text node.
            List<Expression> content = new ArrayList<>();
            StringBuilder textAccumulator = null;
            ExceptionMetadata firstTextMetadata = null;
            Token previousToken = ctx.endOpen;

            for (JsoniqParser.DirElemContentContext child : ctx.dirElemContent()) {
                Expression childExpression = (Expression) this.visitDirElemContent(child);

                if (childExpression instanceof TextNodeExpression textNode) {

                    // If the parent of a text node is not empty, the Text Node must not contain the zero-length
                    // string as its content.
                    // see https://www.w3.org/TR/xpath-datamodel-31/#TextNodeOverview
                    // skip empty text nodes
                    if (textNode.getContent().isEmpty()) {
                        previousToken = child.getStop();
                        continue;
                    }

                    if (textAccumulator == null) {
                        textAccumulator = new StringBuilder();
                        firstTextMetadata = textNode.getMetadata();
                    }
                    appendHiddenTokensAfter(this.xQueryTokenStream, previousToken.getTokenIndex(), textAccumulator);
                    textAccumulator.append(textNode.getContent());
                } else {
                    // non-text node encountered
                    if (textAccumulator != null) {
                        appendHiddenTokensAfter(
                            this.xQueryTokenStream,
                            previousToken.getTokenIndex(),
                            textAccumulator
                        );
                        // finalize any accumulated text nodes
                        content.add(
                            new TextNodeExpression(
                                    textAccumulator.toString(),
                                    firstTextMetadata
                            )
                        );
                        textAccumulator = null;
                        firstTextMetadata = null;
                    }

                    // add the non-text node
                    content.add(childExpression);
                }
                previousToken = child.getStop();
            }

            // handle any remaining accumulated text at the end
            if (textAccumulator != null) {
                appendHiddenTokensAfter(this.xQueryTokenStream, previousToken.getTokenIndex(), textAccumulator);
                content.add(
                    new TextNodeExpression(
                            textAccumulator.toString(),
                            firstTextMetadata
                    )
                );
            }

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

    @Override
    public Node visitDirElemConstructorSingleTag(JsoniqParser.DirElemConstructorSingleTagContext ctx) {
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
    public Node visitDirElemContent(JsoniqParser.DirElemContentContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.DirectConstructorContext directConstructorContext) {
            return this.visitDirectConstructor(directConstructorContext);
        } else if (child instanceof JsoniqParser.CommonContentContext commonContentContext) {
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

    /**
     * Helper method to process text content that may contain entity/character references or escaped braces.
     * According to XQuery 3.1 spec, PredefinedEntityRef and CharRef must be expanded.
     * 
     * @param content The raw text content to process
     * @return The processed (unescaped) content
     */
    private String processTextContentWithEscaping(String content) {
        if (content.startsWith("&") && content.endsWith(";")) {
            // This is a PredefinedEntityRef or CharRef token - expand it
            return StringEscapeUtils.unescapeXml(content);
        }
        // Handle escaped braces: {{ or }}
        if (content.equals("{{")) {
            return "{";
        }
        if (content.equals("}}")) {
            return "}";
        }
        // Return content as-is if no escaping needed
        return content;
    }

    @Override
    public Node visitCommonContent(JsoniqParser.CommonContentContext ctx) {
        if (ctx.expr() != null) {
            return (Expression) this.visitExpr(ctx.expr());
        }
        // According to XQuery 3.1 spec, CommonContent can contain PredefinedEntityRef or CharRef
        // which must be expanded. Check if the content is an entity/character reference.
        String content = ctx.getText();
        String processedContent = processTextContentWithEscaping(content);
        return new TextNodeExpression(processedContent, createMetadataFromContext(ctx));
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
        return new DocumentNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCompTextConstructor(JsoniqParser.CompTextConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new TextNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitCompCommentConstructor(JsoniqParser.CompCommentConstructorContext ctx) {
        Expression contentExpression = (Expression) visit(ctx.enclosedExpression());

        return new CommentNodeConstructorExpression(
                contentExpression,
                createMetadataFromContext(ctx)
        );
    }

    public Node visitCompPIConstructor(JsoniqParser.CompPIConstructorContext ctx) {
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
    public Node visitCompAttrConstructor(JsoniqParser.CompAttrConstructorContext ctx) {
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
    public Node visitCompElemConstructor(JsoniqParser.CompElemConstructorContext ctx) {
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
    public Node visitCompNamespaceConstructor(JsoniqParser.CompNamespaceConstructorContext ctx) {
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
    public Node visitEnclosedContentExpr(JsoniqParser.EnclosedContentExprContext ctx) {
        return this.visitEnclosedExpression(ctx.enclosedExpression());
    }

    @Override
    public Node visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.SquareArrayConstructorContext sqCtx) {
            List<JsoniqParser.ExprSingleContext> memberCtxs = sqCtx.exprSingle();
            if (memberCtxs == null || memberCtxs.isEmpty()) {
                return new ArrayConstructorExpression(
                        new ArrayList<>(),
                        true,
                        createMetadataFromContext(sqCtx)
                );
            }
            List<Expression> memberExpressions = new ArrayList<>();
            if (this.moduleContext.getQueryLanguage().equals("jsoniq10")) {
                // In JSONiq 1.0, the square array constructor behaves like the curly array constructor.
                // Thus, we concatenate all expressions into a single comma expression.
                for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                    memberExpressions.add((Expression) this.visitExprSingle(memberCtx));
                }
                Expression commaExpression = new CommaExpression(
                        memberExpressions,
                        createMetadataFromContext(sqCtx)
                );
                return new ArrayConstructorExpression(
                        commaExpression,
                        createMetadataFromContext(sqCtx)
                );
            } else {
                System.err.println("Not concatenating to comma.");
                // In JSONiq 4.0, the square array constructor behaves like in XQuery 4.0.
                for (JsoniqParser.ExprSingleContext memberCtx : memberCtxs) {
                    memberExpressions.add((Expression) this.visitExprSingle(memberCtx));
                }
                return new ArrayConstructorExpression(
                        memberExpressions,
                        true,
                        createMetadataFromContext(sqCtx)
                );
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
        if (ctx.expr() == null) {
            return new CommaExpression(createMetadataFromContext(ctx));
        }
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Node visitVarRef(JsoniqParser.VarRefContext ctx) {
        Name name = parseEqName(ctx.eqName(), false, false, false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    public SequenceType processSequenceType(JsoniqParser.SequenceTypeContext ctx) {
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

    public SequenceType processSingleType(JsoniqParser.SingleTypeContext ctx) {
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
            // we have a function item type
            JsoniqParser.TypedFunctionTestContext typedFnCtx = fnCtx.typedFunctionTest();
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
            JsoniqParser.MapTestContext mapTestContext = itemTypeContext.mapTest();
            if (mapTestContext.anyMapTest() != null) {
                return BuiltinTypesCatalogue.mapItem;
            }
            JsoniqParser.TypedMapTestContext typedMapTestContext = mapTestContext.typedMapTest();
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

    private ItemType processKindTestAsItemType(JsoniqParser.KindTestContext kindTestContext) {
        if (kindTestContext.anyKindTest() != null) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (kindTestContext.documentTest() != null) {
            JsoniqParser.DocumentTestContext documentTestContext = kindTestContext.documentTest();
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
            JsoniqParser.AttributeTestContext attributeTestContext = kindTestContext.attributeTest();
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
            JsoniqParser.PiTestContext piTestContext = kindTestContext.piTest();
            if (piTestContext.ncName() != null) {
                return ItemTypeFactory.processingInstructionNodeItemType(piTestContext.ncName().getText());
            }
            if (piTestContext.stringLiteral() != null) {
                String rawValue = piTestContext.stringLiteral().getText();
                String targetName = rawValue.substring(1, rawValue.length() - 1);
                return ItemTypeFactory.processingInstructionNodeItemType(targetName);
            }
            return BuiltinTypesCatalogue.processingInstructionNode;
        }
        throw new UnsupportedFeatureException(
                "Unsupported kind test in item type: " + kindTestContext.getText(),
                createMetadataFromContext(kindTestContext)
        );
    }

    private ElementNodeItemType getElementTestAsItemType(JsoniqParser.ElementTestContext elementTestContext) {
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
    public Node visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        Name name = parseFunctionName(ctx.functionName());
        return processFunctionCall(
            name,
            getArgumentsFromArgumentListContext(ctx.argumentList()),
            createMetadataFromContext(ctx)
        );
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
                "Function item expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx) {
        Name name = parseFunctionName(ctx.functionName());
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
    public Node visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.createSequenceType("item*");
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
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
    public Node visitIfExpr(JsoniqParser.IfExprContext ctx) {
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
    public Node visitSwitchExpr(JsoniqParser.SwitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<SwitchCase> cases = new ArrayList<>();
        for (JsoniqParser.SwitchCaseClauseContext expr : ctx.cases) {
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
    public Node visitTypeswitchExpr(JsoniqParser.TypeswitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeswitchCase> cases = new ArrayList<>();
        for (JsoniqParser.CaseClauseContext expr : ctx.cses) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (expr.var_ref != null) {
                variableName = ((VariableReferenceExpression) this.visitVarRef(
                    expr.var_ref
                )).getVariableName();
            }
            if (expr.union != null && !expr.union.isEmpty()) {
                for (JsoniqParser.SequenceTypeContext sequenceType : expr.union) {
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
    public Node visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx) {
        Clause lastClause = null;
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        boolean isUniversal = false;
        if (ctx.ev == null) {
            isUniversal = false;
        } else {
            isUniversal = true;
        }
        for (JsoniqParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
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
    public Node visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx) {
        Expression tryExpression = ctx.try_expression == null
            ? new CommaExpression(createMetadataFromContext(ctx))
            : (Expression) this.visitExpr(ctx.try_expression);
        Map<CatchPattern, Expression> catchExpressions = new LinkedHashMap<>();
        for (JsoniqParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = catchCtx.catch_expression == null
                ? new CommaExpression(createMetadataFromContext(catchCtx))
                : (Expression) this.visitExpr(catchCtx.catch_expression);
            for (JsoniqParser.EqNameContext eqNameCtx : catchCtx.errors) {
                CatchPattern pattern = CatchPattern.exact(parseEqName(eqNameCtx, false, false, false, false));
                if (!catchExpressions.containsKey(pattern)) {
                    catchExpressions.put(pattern, catchExpression);
                }
            }
            for (JsoniqParser.WildcardContext wildcardCtx : catchCtx.jokers) {
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
    public Node visitVarDecl(JsoniqParser.VarDeclContext ctx) {
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
    public Node visitContextItemDecl(JsoniqParser.ContextItemDeclContext ctx) {
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
        Name paramName = parseEqName(ctx.varName().eqName(), false, false, false, false);
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
    public Node visitTryCatchStatement(JsoniqParser.TryCatchStatementContext ctx) {
        BlockStatement tryBlock = (BlockStatement) this.visitBlockStatement(ctx.try_block);
        Map<CatchPattern, BlockStatement> catchBlockStatements = new LinkedHashMap<>();
        for (JsoniqParser.CatchCaseStatementContext catchCtx : ctx.catches) {
            BlockStatement catchBlockStatement = (BlockStatement) this.visitBlockStatement(catchCtx.catch_block);
            for (JsoniqParser.EqNameContext eqNameCtx : catchCtx.errors) {
                CatchPattern pattern = CatchPattern.exact(parseEqName(eqNameCtx, false, false, false, false));
                if (!catchBlockStatements.containsKey(pattern)) {
                    catchBlockStatements.put(pattern, catchBlockStatement);
                }
            }
            for (JsoniqParser.WildcardContext wildcardCtx : catchCtx.jokers) {
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

    private CatchPattern parseWildcardPattern(JsoniqParser.WildcardContext wildcardContext) {
        if (wildcardContext instanceof JsoniqParser.AllNamesContext) {
            // Only * is provided
            return CatchPattern.catchAll();
        }
        if (wildcardContext instanceof JsoniqParser.AllWithLocalContext) {
            // Namespace is the wildcard
            String wildcardText = wildcardContext.getText();
            // First two characters *: are stripped to keep only the local name
            return CatchPattern.namespaceWildcard(wildcardText.substring(2), wildcardText);
        }
        if (wildcardContext instanceof JsoniqParser.AllWithNSContext) {
            // Local name is the wildcard
            String wildcardText = wildcardContext.getText();

            // Strip the last two characters :*
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
        if (wildcardContext instanceof JsoniqParser.BracedURILiteralContext) {
            // Declare namespace in place, and match any local name
            // For example, Q{http://example.com}:*
            String wildcardText = wildcardContext.getText();
            int closingBrace = wildcardText.indexOf('}');
            return CatchPattern.localNameWildcard(wildcardText.substring(2, closingBrace), wildcardText);
        }
        throw new OurBadException("Unsupported catch wildcard pattern: " + wildcardContext.getText());
    }

    @Override
    public Node visitTypeSwitchStatement(JsoniqParser.TypeSwitchStatementContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeSwitchStatementCase> cases = new ArrayList<>();
        for (JsoniqParser.CaseStatementContext stmt : ctx.cases) {
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
        return new FunctionCallExpression(
                Name.createVariableInDefaultBuiltinFunctionNamespace("root"),
                Collections.emptyList(),
                createMetadataFromContext(ctx)
        );
    }

    private Node visitRelativeWithoutSlash(JsoniqParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1 && relativeContext.stepExpr(0).postfixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostfixExpr(relativeContext.stepExpr(0).postfixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Node visitDoubleSlash(
            JsoniqParser.PathExprContext pathContext,
            JsoniqParser.RelativePathExprContext doubleSlashContext
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
            JsoniqParser.PathExprContext pathContext,
            JsoniqParser.RelativePathExprContext singleSlashContext
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
            JsoniqParser.RelativePathExprContext relativePathExprContext,
            Expression leftMost
    ) {
        return getSlashes(relativePathExprContext, leftMost, relativePathExprContext.getStart());
    }

    private Expression getSlashes(
            JsoniqParser.RelativePathExprContext relativePathExprContext,
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
    public Node visitStepExpr(JsoniqParser.StepExprContext ctx) {
        if (ctx.postfixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (JsoniqParser.PredicateContext predicateContext : ctx.axisStep().predicateList().predicate()) {
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
            JsoniqParser.NodeTestContext nodeTestContext,
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
        if (kindTest instanceof JsoniqParser.DocumentTestContext docContext) {
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
        } else if (kindTest instanceof JsoniqParser.AttributeTestContext attributeTestContext) {
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
                String rawValue = piContext.stringLiteral().getText();
                // Strip surrounding quotes from the string literal
                String targetName = rawValue.substring(1, rawValue.length() - 1);
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
        } else if (kindTest instanceof JsoniqParser.SchemaElementTestContext) {
            // XQuery 3.1 Section 2.5.5.4 - Schema Element Test (unsupported, requires schema import)
            throw new UnsupportedFeatureException(
                    "Schema element tests (schema-element(...)) are not supported",
                    createMetadataFromContext((ParserRuleContext) kindTest)
            );
        } else if (kindTest instanceof JsoniqParser.SchemaAttributeTestContext) {
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

    public void processNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx) {
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
        String rawValue = ctx.getText().substring(1, ctx.getText().length() - 1);
        return unescapeStringLiteral(rawValue);
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
        if (!uri.equals(Name.DEFAULT_COLLATION_NS)) {
            throw new DefaultCollationException(
                    "Unknown collation: " + uri,
                    createMetadataFromContext(ctx.uriLiteral())
            );
        }
    }

    public LibraryModule processModuleImport(JsoniqParser.ModuleImportContext ctx) {
        String namespace = processURILiteral(ctx.targetNamespace);
        URI resolvedURI = FileSystemUtil.resolveURI(
            this.moduleContext.getStaticBaseURI(),
            namespace,
            createMetadataFromContext(ctx)
        );
        LibraryModule libraryModule = null;
        try {
            libraryModule = VisitorHelpers.parseLibraryModuleFromLocation(
                resolvedURI,
                this.configuration,
                this.moduleContext,
                createMetadataFromContext(ctx)
            );
            if (!resolvedURI.toString().equals(libraryModule.getNamespace())) {
                throw new ModuleNotFoundException(
                        "A module with namespace "
                            + resolvedURI.toString()
                            + " was not found. The namespace of the module at this location was: "
                            + libraryModule.getNamespace(),
                        createMetadataFromContext(ctx)
                );
            }
        } catch (IOException e) {
            RumbleException exception = new ModuleNotFoundException(
                    "I/O error while attempting to import a module: " + namespace + " Cause: " + e.getMessage(),
                    createMetadataFromContext(ctx)
            );
            exception.initCause(e);
            throw exception;
        } catch (CannotRetrieveResourceException e) {
            RumbleException exception = new ModuleNotFoundException(
                    "Module not found: " + namespace + " Cause: " + e.getMessage(),
                    createMetadataFromContext(ctx)
            );
            exception.initCause(e);
            throw exception;
        }
        if (ctx.ncName() != null) {
            bindNamespace(
                ctx.ncName().getText(),
                resolvedURI.toString(),
                createMetadataFromContext(ctx)
            );
        }
        return libraryModule;
    }

    public ExceptionMetadata generateMetadata(Token start, Token end) {
        return ExceptionMetadata.fromTokens(this.moduleContext.getStaticBaseURI().toString(), start, end, this.code);
    }

    private List<Annotation> processAnnotations(JsoniqParser.AnnotationsContext annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (JsoniqParser.AnnotationContext annotationContext : annotations.annotation()) {
            // for backwards compatibility, the specification allows for updating without % sign
            if (annotationContext.updating != null) {
                Name name = Name.createVariableInDefaultAnnotationsNamespace("updating");
                parsedAnnotations.add(new Annotation(name, null));
                continue;
            }
            JsoniqParser.EqNameContext eqNameContext = annotationContext.eqName();
            Name name = parseEqName(eqNameContext, false, false, true, false);
            if (!annotationContext.literal().isEmpty()) {
                throw new UnsupportedFeatureException(
                        "Literals are currently not supported in annotations!",
                        createMetadataFromContext(annotationContext)
                );
            }
            parsedAnnotations.add(new Annotation(name, null));
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


    private DirAttributeProcessingResult getAttributesExpressionsList(JsoniqParser.DirAttributeListContext ctx) {
        DirAttributeProcessingResult result = new DirAttributeProcessingResult();

        // Process each attribute name-value pair
        List<JsoniqParser.QnameContext> attributeNames = ctx.attribute_qname;
        List<JsoniqParser.DirAttributeValueContext> attributeValues = ctx.attribute_value;

        for (int i = 0; i < attributeNames.size(); i++) {
            JsoniqParser.QnameContext qnameCtx = attributeNames.get(i);
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
            JsoniqParser.DirAttributeValueContext ctx,
            boolean allowEnclosedExpressions
    ) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.DirAttributeValueAposContext dirAttributeValueAposContext) {
            return this.getDirAttributeValueAposExpressions(
                dirAttributeValueAposContext,
                allowEnclosedExpressions
            );
        } else if (child instanceof JsoniqParser.DirAttributeValueQuotContext dirAttributeValueQuotContext) {
            return this.getDirAttributeValueQuotExpressions(
                dirAttributeValueQuotContext,
                allowEnclosedExpressions
            );
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
                        createMetadataFromContext(ctx)
                );
            }
            uriBuilder.append(attributeContent.getContent());
        }
        return uriBuilder.toString();
    }


    /**
     * Helper method to process quoted attribute values (both single and double quoted).
     * This method handles the common logic for merging adjacent text content and building expressions.
     * Returns a list of expressions where adjacent string literals are merged.
     */
    private List<Expression> processQuotedAttributeValue(
            ParserRuleContext ctx,
            String escapeSequence,
            String escapedChar,
            boolean allowEnclosedExpressions
    ) {

        // Similar to element content, we need to merge adjacent text content
        StringBuilder textAccumulator = null;
        ParseTree firstTextTree = null;
        ParseTree lastTextTree = null;
        List<Expression> contentExpressions = new ArrayList<>();

        // Process each child between the quotes (skip the first and last quote tokens)
        for (int i = 1; i < ctx.getChildCount() - 1; i++) {
            ParseTree child = ctx.getChild(i);
            List<Expression> childExpressions = new ArrayList<>();

            // Try to process as entity or character reference first
            // According to XQuery 3.1 spec, PredefinedEntityRef and CharRef are expanded
            String childText = child.getText();
            if (childText.startsWith("&") && childText.endsWith(";")) {
                // This is a PredefinedEntityRef or CharRef token - expand it
                String unescapedValue = StringEscapeUtils.unescapeXml(childText);
                childExpressions.add(
                    new AttributeNodeContentExpression(unescapedValue, createMetadataFromTree(child))
                );
            } else if (child.getText().equals(escapeSequence)) {
                // Escaped quote
                childExpressions.add(new AttributeNodeContentExpression(escapedChar, createMetadataFromTree(child)));
            } else {
                // Try the content visitor for nested content or text
                List<Expression> contentResult = processAttributeContent(
                    (ParserRuleContext) child,
                    allowEnclosedExpressions
                );
                if (contentResult != null && !contentResult.isEmpty()) {
                    childExpressions.addAll(contentResult);
                } else {
                    throw new UnsupportedOperationException("Unsupported attribute content: " + child.getText());
                }
            }

            // Process each expression returned from the child
            for (Expression childExpression : childExpressions) {
                if (childExpression instanceof AttributeNodeContentExpression attributeNodeContentExpression) {
                    // Text content - accumulate it
                    String content = (attributeNodeContentExpression).getContent();

                    if (textAccumulator == null) {
                        // Start accumulating text content
                        textAccumulator = new StringBuilder();
                        firstTextTree = child;
                    }

                    // Accumulate the text content
                    textAccumulator.append(content);
                    lastTextTree = child;
                } else {
                    // Non-text expression encountered (e.g., enclosed expression)
                    if (textAccumulator != null) {
                        // Finalize any accumulated text
                        contentExpressions.add(
                            new AttributeNodeContentExpression(
                                    textAccumulator.toString(),
                                    createMetadataFromTrees(firstTextTree, lastTextTree)
                            )
                        );
                        textAccumulator = null;
                        firstTextTree = null;
                        lastTextTree = null;
                    }

                    // Add the non-text expression
                    contentExpressions.add(childExpression);
                }
            }
        }

        // Handle any remaining accumulated text at the end
        if (textAccumulator != null) {
            contentExpressions.add(
                new AttributeNodeContentExpression(
                        textAccumulator.toString(),
                        createMetadataFromTrees(firstTextTree, lastTextTree)
                )
            );
        }

        return contentExpressions;
    }

    /**
     * Helper method to process attribute content (handles nested quotes, expressions, and escaped braces).
     */
    private List<Expression> processAttributeContent(ParserRuleContext ctx, boolean allowEnclosedExpressions) {
        ParseTree child = ctx.children.get(0);
        List<Expression> expressions = new ArrayList<>();

        if (ctx instanceof JsoniqParser.DirAttributeValueAposContext dirAttributeValueAposContext) {
            return this.getDirAttributeValueAposExpressions(
                dirAttributeValueAposContext,
                allowEnclosedExpressions
            );
        } else if (ctx instanceof JsoniqParser.DirAttributeValueQuotContext dirAttributeValueQuotContext) {
            return this.getDirAttributeValueQuotExpressions(
                dirAttributeValueQuotContext,
                allowEnclosedExpressions
            );
        } else if (
            ctx instanceof JsoniqParser.DirAttributeContentQuotContext dirAttributeContentQuotContext
                &&
                dirAttributeContentQuotContext.expr() != null
        ) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx)
                );
            }
            expressions.add((Expression) this.visitExpr(dirAttributeContentQuotContext.expr()));
        } else if (
            ctx instanceof JsoniqParser.DirAttributeContentAposContext dirAttributeContentAposContext
                &&
                dirAttributeContentAposContext.expr() != null
        ) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        createMetadataFromContext(ctx)
                );
            }
            expressions.add((Expression) this.visitExpr(dirAttributeContentAposContext.expr()));
        } else {
            // handle other cases
            String childText = child.getText();
            String processedContent = processTextContentWithEscaping(childText);
            expressions.add(new AttributeNodeContentExpression(processedContent, createMetadataFromTree(child)));
        }
        return expressions;
    }



    /**
     * Process dirAttributeValueApos and return a list of expressions.
     * This method deviates from the strict visitor pattern to return multiple expressions.
     */
    private List<Expression> getDirAttributeValueAposExpressions(
            JsoniqParser.DirAttributeValueAposContext ctx,
            boolean allowEnclosedExpressions
    ) {
        return processQuotedAttributeValue(ctx, "\"\"", "\"", allowEnclosedExpressions);
    }

    /**
     * Process dirAttributeValueQuot and return a list of expressions.
     * The list of expression is a mixed list of AttributeNodeContentExpression, and EnclosedExpressions
     * The returned list is already minimal i.e. no adjacent AttributeNodeContentExpression are present.
     * This method deviates from the strict visitor pattern to return multiple expressions.
     */
    private List<Expression> getDirAttributeValueQuotExpressions(
            JsoniqParser.DirAttributeValueQuotContext ctx,
            boolean allowEnclosedExpressions
    ) {
        return processQuotedAttributeValue(ctx, "''", "'", allowEnclosedExpressions);
    }


    @Override
    public Node visitDecimalFormatDecl(JsoniqParser.DecimalFormatDeclContext ctx) {
        return visitChildren(ctx);
    }

    private void processDecimalFormatDeclaration(
            JsoniqParser.DecimalFormatDeclContext ctx,
            ExceptionMetadata metadata
    ) {
        DecimalFormatDeclarationHelper.processDecimalFormatDeclaration(
            ctx,
            ctx.KW_DEFAULT() != null,
            ctx.eqName(),
            ctx.DFPropertyName(),
            ctx.stringLiteral(),
            this.moduleContext,
            true,
            metadata
        );
    }

}
