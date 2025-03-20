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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.DuplicateModuleTargetNamespaceException;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.exceptions.EmptyModuleURIException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.MoreThanOneEmptyOrderDeclarationException;
import org.rumbledb.exceptions.NamespaceDoesNotMatchModuleException;
import org.rumbledb.exceptions.NamespacePrefixBoundTwiceException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.PositionalVariableNameSameAsForVariableException;
import org.rumbledb.exceptions.PrefixCannotBeExpandedException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
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
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
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
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
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
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.UnaryLookupExpression;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.parser.xquery.XQueryBaseVisitor;
import org.rumbledb.parser.xquery.XQueryParser;
import org.rumbledb.parser.xquery.XQueryParser.DefaultCollationDeclContext;
import org.rumbledb.parser.xquery.XQueryParser.EmptyOrderDeclContext;
import org.rumbledb.parser.xquery.XQueryParser.SetterContext;
import org.rumbledb.parser.xquery.XQueryParser.UriLiteralContext;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.ItemTypeReference;
import org.rumbledb.types.SequenceType;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.rumbledb.types.SequenceType.ITEM_STAR;


/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 *
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 */
public class XQueryTranslationVisitor extends XQueryBaseVisitor<Node> {

    private StaticContext moduleContext;
    private RumbleRuntimeConfiguration configuration;
    private boolean isMainModule;
    private String code;

    public XQueryTranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            RumbleRuntimeConfiguration configuration,
            String code
    ) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindDefaultNamespaces();
        this.configuration = configuration;
        this.isMainModule = isMainModule;
        this.code = code;
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(XQueryParser.ModuleContext ctx) {
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
                            SequenceType.ITEM,
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
        String prefix = ctx.NCName().getText();
        String namespace = processURILiteral(ctx.uriLiteral());
        if (namespace.equals("")) {
            throw new EmptyModuleURIException("Module URI is empty.", createMetadataFromContext(ctx));
        }
        URI resolvedURI = FileSystemUtil.resolveURI(
            this.moduleContext.getStaticBaseURI(),
            namespace,
            generateMetadata(ctx.getStop())
        );
        bindNamespace(
            prefix,
            resolvedURI.toString(),
            generateMetadata(ctx.getStop())
        );

        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        LibraryModule module = new LibraryModule(prolog, resolvedURI.toString(), createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

    @Override
    public Node visitProlog(XQueryParser.PrologContext ctx) {
        // bind namespaces
        for (XQueryParser.NamespaceDeclContext namespace : ctx.namespaceDecl()) {
            this.processNamespaceDecl(namespace);
        }
        List<SetterContext> setters = ctx.setter();
        boolean emptyOrderSet = false;
        boolean defaultCollationSet = false;
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
            throw new UnsupportedFeatureException(
                    "Setters are not supported yet, except for empty sequence ordering and default collations.",
                    createMetadataFromContext(setterContext)
            );
        }
        List<LibraryModule> libraryModules = new ArrayList<>();
        Set<String> namespaces = new HashSet<>();
        for (XQueryParser.ModuleImportContext namespace : ctx.moduleImport()) {
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
        for (XQueryParser.AnnotatedDeclContext annotatedDeclaration : ctx.annotatedDecl()) {
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
                                generateMetadata(annotatedDeclaration.getStop())
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
                                generateMetadata(annotatedDeclaration.getStop())
                        );
                    }
                }
                functionDeclarations.add(
                    new FunctionDeclaration(inlineFunctionExpression, createMetadataFromContext(ctx))
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
                                generateMetadata(annotatedDeclaration.getStop())
                        );
                    }
                }
                typeDeclarations.add(typeDeclaration);
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
        return prolog;
    }

    public Name parseName(XQueryParser.QnameContext ctx, boolean isFunction, boolean isType, boolean isAnnotation) {
        String localName = null;
        String prefix = null;
        Name name = null;
        if (ctx.local_name != null) {
            localName = ctx.local_name.getText();
        } else {
            localName = ctx.local_namekw.getText();
        }
        if (ctx.ns != null) {
            prefix = ctx.ns.getText();
        } else if (ctx.nskw != null) {
            prefix = ctx.nskw.getText();
        }
        if (prefix == null) {
            if (isFunction) {
                name = Name.createVariableInDefaultFunctionNamespace(localName);
            } else if (isType) {
                name = Name.createVariableInDefaultTypeNamespace(localName);
            } else if (isAnnotation) {
                name = Name.createVariableInDefaultAnnotationsNamespace(localName);
            } else {
                name = Name.createVariableInNoNamespace(localName);
            }
        } else {
            name = Name.createVariableResolvingPrefix(prefix, localName, this.moduleContext);
        }
        if (name != null) {
            return name;
        }
        throw new PrefixCannotBeExpandedException(
                "Cannot expand prefix " + prefix,
                generateMetadata(ctx.getStop())
        );
    }

    @Override
    public Node visitFunctionDecl(XQueryParser.FunctionDeclContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        Name name = parseName(ctx.qname(), true, false, false);
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false, false);
                paramType = ITEM_STAR;
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
                    paramType = SequenceType.ITEM_STAR;
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
    public Node visitTypeDecl(XQueryParser.TypeDeclContext ctx) {
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
            definitionItem = ItemParser.getItemFromString(definitionString, createMetadataFromContext(ctx));
        } catch (ParsingException e) {
            ParsingException pe = new ParsingException(
                    "A type definition must be a JSON literal: no dynamic evaluation is allowed.",
                    createMetadataFromContext(ctx)
            );
            pe.initCause(e);
            throw pe;
        }
        Name name = parseName(ctx.qname(), false, true, false);
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
        if (content instanceof XQueryParser.ExprSimpleContext) {
            return this.visitExprSimple((XQueryParser.ExprSimpleContext) content);
        }
        if (content instanceof XQueryParser.FlowrExprContext) {
            return this.visitFlowrExpr((XQueryParser.FlowrExprContext) content);
        }
        if (content instanceof XQueryParser.IfExprContext) {
            return this.visitIfExpr((XQueryParser.IfExprContext) content);
        }
        if (content instanceof XQueryParser.SwitchExprContext) {
            return this.visitSwitchExpr((XQueryParser.SwitchExprContext) content);
        }
        if (content instanceof XQueryParser.TypeSwitchExprContext) {
            return this.visitTypeSwitchExpr((XQueryParser.TypeSwitchExprContext) content);
        }
        if (content instanceof XQueryParser.TryCatchExprContext) {
            return this.visitTryCatchExpr((XQueryParser.TryCatchExprContext) content);
        }
        throw new OurBadException("Unrecognized ExprSingle:" + content.getClass().getName());
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Node visitExprSimple(XQueryParser.ExprSimpleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof XQueryParser.OrExprContext) {
            return this.visitOrExpr((XQueryParser.OrExprContext) content);
        }
        if (content instanceof XQueryParser.QuantifiedExprContext) {
            return this.visitQuantifiedExpr((XQueryParser.QuantifiedExprContext) content);
        }
        if (content instanceof XQueryParser.DeleteExprContext) {
            return this.visitDeleteExpr((XQueryParser.DeleteExprContext) content);
        }
        if (content instanceof XQueryParser.InsertExprContext) {
            return this.visitInsertExpr((XQueryParser.InsertExprContext) content);
        }
        if (content instanceof XQueryParser.ReplaceExprContext) {
            return this.visitReplaceExpr((XQueryParser.ReplaceExprContext) content);
        }
        if (content instanceof XQueryParser.RenameExprContext) {
            return this.visitRenameExpr((XQueryParser.RenameExprContext) content);
        }
        if (content instanceof XQueryParser.AppendExprContext) {
            return this.visitAppendExpr((XQueryParser.AppendExprContext) content);
        }
        if (content instanceof XQueryParser.TransformExprContext) {
            return this.visitTransformExpr((XQueryParser.TransformExprContext) content);
        }
        if (content instanceof XQueryParser.PathExprContext) {
            return this.visitPathExpr((XQueryParser.PathExprContext) content);
        }
        throw new OurBadException("Unrecognized ExprSimple.");
    }

    // endregion

    // region Flowr
    @Override
    public Node visitFlowrExpr(XQueryParser.FlowrExprContext ctx) {
        Clause clause;
        // check the start clause, for or let
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }

        Clause previousFLWORClause = clause.getLastClause();

        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof XQueryParser.ForClauseContext) {
                clause = (Clause) this.visitForClause((XQueryParser.ForClauseContext) child);
            } else if (child instanceof XQueryParser.LetClauseContext) {
                clause = (Clause) this.visitLetClause((XQueryParser.LetClauseContext) child);
            } else if (child instanceof XQueryParser.WhereClauseContext) {
                clause = (Clause) this.visitWhereClause((XQueryParser.WhereClauseContext) child);
            } else if (child instanceof XQueryParser.GroupByClauseContext) {
                clause = (Clause) this.visitGroupByClause((XQueryParser.GroupByClauseContext) child);
            } else if (child instanceof XQueryParser.OrderByClauseContext) {
                clause = (Clause) this.visitOrderByClause((XQueryParser.OrderByClauseContext) child);
            } else if (child instanceof XQueryParser.CountClauseContext) {
                clause = (Clause) this.visitCountClause((XQueryParser.CountClauseContext) child);
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
                generateMetadata(ctx.getStop())
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

    public GroupByVariableDeclaration processGroupByVar(XQueryParser.GroupByVarContext ctx) {
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
            result = new OrExpression(result, rightExpression, createMetadataFromContext(ctx));
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
            result = new AndExpression(result, rightExpression, createMetadataFromContext(ctx));
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

        ComparisonExpression.ComparisonOperator kind = ComparisonExpression.ComparisonOperator.fromSymbol(
            ctx.op.get(0).getText()
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
                generateMetadata(ctx.start)
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
            result = new StringConcatExpression(result, rightExpression, createMetadataFromContext(ctx));
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
                    createMetadataFromContext(ctx)
            );
        }
        return result;
    }

    @Override
    public Node visitMultiplicativeExpr(XQueryParser.MultiplicativeExprContext ctx) {
        Expression result = (Expression) this.visitInstanceOfExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            XQueryParser.InstanceOfExprContext child = ctx.rhs.get(i);
            Expression rightExpression = (Expression) this.visitInstanceOfExpr(child);
            result = new MultiplicativeExpression(
                    result,
                    rightExpression,
                    MultiplicativeExpression.MultiplicativeOperator.fromSymbol(ctx.op.get(i).getText()),
                    createMetadataFromContext(ctx)
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
                    createMetadataFromContext(ctx)
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
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext(ctx.arguments.get(i)));
            if (functionCallContext.qname() != null) {
                Name name = parseName(functionCallContext.qname(), true, false, false);
                mainExpression = processFunctionCall(name, children, createMetadataFromContext(functionCallContext));
                continue;
            } else if (functionCallContext.varRef() != null) {
                functionExpression = (Expression) this.visitVarRef(functionCallContext.varRef());
            } else {
                functionExpression = (Expression) this.visitParenthesizedExpr(functionCallContext.parenthesizedExpr());
            }
            mainExpression = new DynamicFunctionCallExpression(
                    functionExpression,
                    children,
                    createMetadataFromContext(functionCallContext)
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
        return this.visitAnnotateExpr(ctx.annotate_expr);
    }

    @Override
    public Node visitAnnotateExpr(XQueryParser.AnnotateExprContext ctx) {
        Expression mainExpr = (Expression) this.visitExpr(ctx.expr());
        SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
        return new ValidateTypeExpression(mainExpr, false, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitValidateExpr(XQueryParser.ValidateExprContext ctx) {
        Expression mainExpr = (Expression) this.visitExpr(ctx.expr());
        SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
        return new ValidateTypeExpression(mainExpr, true, sequenceType, createMetadataFromContext(ctx));
    }
    // endregion

    // region update

    @Override
    public Node visitInsertExpr(XQueryParser.InsertExprContext ctx) {
        Expression toInsertExpr;
        Expression posExpr = null;
        if (ctx.pairConstructor() != null && !ctx.pairConstructor().isEmpty()) {
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
    public Node visitDeleteExpr(XQueryParser.DeleteExprContext ctx) {
        Expression mainExpression = getMainExpressionFromUpdateLocatorContext(ctx.updateLocator());
        Expression locatorExpression = getLocatorExpressionFromUpdateLocatorContext(ctx.updateLocator());
        return new DeleteExpression(mainExpression, locatorExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitRenameExpr(XQueryParser.RenameExprContext ctx) {
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
    public Node visitReplaceExpr(XQueryParser.ReplaceExprContext ctx) {
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
    public Node visitTransformExpr(XQueryParser.TransformExprContext ctx) {
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
    public Node visitAppendExpr(XQueryParser.AppendExprContext ctx) {
        Expression arrayExpression = (Expression) this.visitExprSingle(ctx.array_expr);
        Expression toAppendExpression = (Expression) this.visitExprSingle(ctx.to_append_expr);
        return new AppendExpression(arrayExpression, toAppendExpression, createMetadataFromContext(ctx));
    }

    public Expression getMainExpressionFromUpdateLocatorContext(XQueryParser.UpdateLocatorContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 1)) {
            if (child instanceof XQueryParser.LookupContext) {
                Expression expr = (Expression) this.visitLookup((XQueryParser.LookupContext) child);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else {
                throw new OurBadException("Unrecognized locator expression found in update expression.");
            }
        }
        return mainExpression;
    }

    public Expression getLocatorExpressionFromUpdateLocatorContext(XQueryParser.UpdateLocatorContext ctx) {
        ParseTree locatorExprCtx = ctx.getChild(ctx.getChildCount() - 1);
        if (locatorExprCtx instanceof XQueryParser.LookupContext) {
            return (Expression) this.visitLookup((XQueryParser.LookupContext) locatorExprCtx);
        } else {
            throw new OurBadException("Unrecognized locator found in update expression.");
        }
    }

    // endregion

    // region postfix
    @Override
    public Node visitPostFixExpr(XQueryParser.PostFixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof XQueryParser.PredicateContext) {
                Expression expr = (Expression) this.visitPredicate((XQueryParser.PredicateContext) child);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof XQueryParser.LookupContext) {
                Expression expr = (Expression) this.visitLookup((XQueryParser.LookupContext) child);
                mainExpression = new PostfixLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof XQueryParser.ArgumentListContext) {
                List<Expression> arguments = getArgumentsFromArgumentListContext(
                    (XQueryParser.ArgumentListContext) child
                );
                mainExpression = new DynamicFunctionCallExpression(
                        mainExpression,
                        arguments,
                        createMetadataFromContext(ctx)
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
                    ctx.lt.getText().substring(1, ctx.lt.getText().length() - 1),
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
        if (child instanceof XQueryParser.VarRefContext) {
            return this.visitVarRef((XQueryParser.VarRefContext) child);
        }
        if (child instanceof XQueryParser.ObjectConstructorContext) {
            return this.visitObjectConstructor((XQueryParser.ObjectConstructorContext) child);
        }
        if (child instanceof XQueryParser.ArrayConstructorContext) {
            return this.visitArrayConstructor((XQueryParser.ArrayConstructorContext) child);
        }
        if (child instanceof XQueryParser.ParenthesizedExprContext) {
            return this.visitParenthesizedExpr((XQueryParser.ParenthesizedExprContext) child);
        }
        if (child instanceof XQueryParser.StringLiteralContext) {
            return new StringLiteralExpression(
                    child.getText().substring(1, child.getText().length() - 1),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof TerminalNode) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        if (child instanceof XQueryParser.ContextItemExprContext) {
            return this.visitContextItemExpr((XQueryParser.ContextItemExprContext) child);
        }
        if (child instanceof XQueryParser.FunctionCallContext) {
            return this.visitFunctionCall((XQueryParser.FunctionCallContext) child);
        }
        if (child instanceof XQueryParser.FunctionItemExprContext) {
            return this.visitFunctionItemExpr((XQueryParser.FunctionItemExprContext) child);
        }
        if (child instanceof XQueryParser.BlockExprContext) {
            return this.visitBlockExpr((XQueryParser.BlockExprContext) child);
        }
        if (child instanceof XQueryParser.UnaryLookupContext) {
            return new UnaryLookupExpression(
                    (Expression) this.visitUnaryLookup((XQueryParser.UnaryLookupContext) child),
                    createMetadataFromContext(ctx)
            );
        }
        throw new UnsupportedFeatureException(
                "Primary expression not yet implemented",
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

    @Override
    public Node visitObjectConstructor(XQueryParser.ObjectConstructorContext ctx) {
        // no merging constructor, just visit the k/v pairs
        if (
            ctx.merge_operator == null
                || ctx.merge_operator.size() == 0
                ||
                ctx.merge_operator.get(0).getText().isEmpty()
        ) {
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
            return new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
        }

        Expression childExpr;
        childExpr = (Expression) this.visitExpr(ctx.expr());
        return new ObjectConstructorExpression(
                childExpr,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitArrayConstructor(XQueryParser.ArrayConstructorContext ctx) {
        if (ctx.expr() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(ctx));
        }
        Expression content = (Expression) this.visitExpr(ctx.expr());
        return new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
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
        Name name = parseName(ctx.qname(), false, false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemExpr(XQueryParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    public SequenceType processSequenceType(XQueryParser.SequenceTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.EMPTY_SEQUENCE;
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
            return SequenceType.EMPTY_SEQUENCE;
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
        if (itemTypeContext.functionTest() != null) {
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
        if (itemTypeContext.qname() != null) {
            Name name = parseName(itemTypeContext.qname(), false, true, false);
            name = ItemTypeReference.renameAtomic(this.configuration, name);
            if (!BuiltinTypesCatalogue.typeExists(name)) {
                return new ItemTypeReference(name);
            }
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        if (itemTypeContext.kindTest() != null) {
            // TODO need to implement Node types
            throw new UnsupportedFeatureException(
                    "Kindtest not yet supported",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        throw new UnsupportedFeatureException(
                "Unsupported itemtype encountered",
                ExceptionMetadata.EMPTY_METADATA
        );

    }

    private Expression processFunctionCall(Name name, List<Expression> children, ExceptionMetadata metadata) {

        Name typeName = name;
        if (name.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)) {
            typeName = Name.createVariableInDefaultTypeNamespace(name.getLocalName());
        }
        if (
            BuiltinTypesCatalogue.typeExists(typeName)
                && children.size() == 1
                && !name.equals(Name.createVariableInDefaultFunctionNamespace("boolean"))
        ) {
            return new CastExpression(
                    children.get(0),
                    new SequenceType(BuiltinTypesCatalogue.getItemTypeByName(typeName), SequenceType.Arity.OneOrZero),
                    metadata
            );
        }
        return new FunctionCallExpression(
                name,
                children,
                metadata
        );
    }

    @Override
    public Node visitFunctionCall(XQueryParser.FunctionCallContext ctx) {
        Name name = parseName(ctx.fn_name, true, false, false);
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
        if (child instanceof XQueryParser.NamedFunctionRefContext) {
            return this.visitNamedFunctionRef((XQueryParser.NamedFunctionRefContext) child);
        }
        if (child instanceof XQueryParser.InlineFunctionExprContext) {
            return this.visitInlineFunctionExpr((XQueryParser.InlineFunctionExprContext) child);
        }
        throw new UnsupportedFeatureException(
                "Function item expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitNamedFunctionRef(XQueryParser.NamedFunctionRefContext ctx) {
        Name name = parseName(ctx.fn_name, true, false, false);
        int arity = 0;
        try {
            arity = Integer.parseInt(ctx.arity.getText());
        } catch (NumberFormatException e) {
            throw new ParsingException(
                    "Parser error: In a named function reference, arity must be an integer.",
                    createMetadataFromContext(ctx)
            );
        }
        return new NamedFunctionReferenceExpression(
                new FunctionIdentifier(name, arity),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitInlineFunctionExpr(XQueryParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.ITEM_STAR;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false, false);
                paramType = SequenceType.ITEM_STAR;
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
                    paramType = SequenceType.ITEM_STAR;
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
    public Node visitTypeSwitchExpr(XQueryParser.TypeSwitchExprContext ctx) {
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
        Clause clause = null;
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
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
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
        clause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new NullLiteralExpression(generateMetadata(ctx.start)),
                generateMetadata(ctx.start)
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
        Expression tryExpression = (Expression) this.visitExpr(ctx.try_expression);
        Map<String, Expression> catchExpressions = new HashMap<>();
        Expression catchAllExpression = null;
        for (XQueryParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = (Expression) this.visitExpr(catchCtx.catch_expression);
            for (XQueryParser.QnameContext qnameCtx : catchCtx.errors) {
                Name name = parseName(qnameCtx, false, false, false);
                if (!catchExpressions.containsKey(name.getLocalName())) {
                    catchExpressions.put(name.getLocalName(), catchExpression);
                }
            }
            boolean doesCatchAll = !catchCtx.jokers.isEmpty();
            if (doesCatchAll && catchAllExpression == null) {
                catchAllExpression = catchExpression;
            }
        }
        return new TryCatchExpression(
                tryExpression,
                catchExpressions,
                catchAllExpression,
                createMetadataFromContext(ctx)
        );
    }

    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        return generateMetadata(ctx.getStart());
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
        if (content instanceof XQueryParser.ApplyStatementContext) {
            return this.visitApplyStatement((XQueryParser.ApplyStatementContext) content);
        }
        if (content instanceof XQueryParser.AssignStatementContext) {
            return this.visitAssignStatement((XQueryParser.AssignStatementContext) content);
        }
        if (content instanceof XQueryParser.BlockStatementContext) {
            return this.visitBlockStatement((XQueryParser.BlockStatementContext) content);
        }
        if (content instanceof XQueryParser.BreakStatementContext) {
            return this.visitBreakStatement((XQueryParser.BreakStatementContext) content);
        }
        if (content instanceof XQueryParser.ContinueStatementContext) {
            return this.visitContinueStatement((XQueryParser.ContinueStatementContext) content);
        }
        if (content instanceof XQueryParser.ExitStatementContext) {
            return this.visitExitStatement((XQueryParser.ExitStatementContext) content);
        }
        if (content instanceof XQueryParser.FlowrStatementContext) {
            return this.visitFlowrStatement((XQueryParser.FlowrStatementContext) content);
        }
        if (content instanceof XQueryParser.IfStatementContext) {
            return this.visitIfStatement((XQueryParser.IfStatementContext) content);
        }
        if (content instanceof XQueryParser.SwitchStatementContext) {
            return this.visitSwitchStatement((XQueryParser.SwitchStatementContext) content);
        }
        if (content instanceof XQueryParser.TryCatchStatementContext) {
            return this.visitTryCatchStatement((XQueryParser.TryCatchStatementContext) content);
        }
        if (content instanceof XQueryParser.TypeSwitchStatementContext) {
            return this.visitTypeSwitchStatement((XQueryParser.TypeSwitchStatementContext) content);
        }
        if (content instanceof XQueryParser.VarDeclStatementContext) {
            return this.visitVarDeclStatement((XQueryParser.VarDeclStatementContext) content);
        }
        if (content instanceof XQueryParser.WhileStatementContext) {
            return this.visitWhileStatement((XQueryParser.WhileStatementContext) content);
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
        Name paramName = parseName(ctx.qname(), false, false, false);
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
    public Node visitFlowrStatement(XQueryParser.FlowrStatementContext ctx) {
        Clause clause;
        // Check for start clause. Only for or let allowed.
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }
        Clause lastFlowrClause = clause.getLastClause();
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof XQueryParser.ForClauseContext) {
                clause = (Clause) this.visitForClause((XQueryParser.ForClauseContext) child);
            } else if (child instanceof XQueryParser.LetClauseContext) {
                clause = (Clause) this.visitLetClause((XQueryParser.LetClauseContext) child);
            } else if (child instanceof XQueryParser.WhereClauseContext) {
                clause = (Clause) this.visitWhereClause((XQueryParser.WhereClauseContext) child);
            } else if (child instanceof XQueryParser.GroupByClauseContext) {
                clause = (Clause) this.visitGroupByClause((XQueryParser.GroupByClauseContext) child);
            } else if (child instanceof XQueryParser.OrderByClauseContext) {
                clause = (Clause) this.visitOrderByClause((XQueryParser.OrderByClauseContext) child);
            } else if (child instanceof XQueryParser.CountClauseContext) {
                clause = (Clause) this.visitCountClause((XQueryParser.CountClauseContext) child);
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
                generateMetadata(ctx.getStop())
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
        Map<String, BlockStatement> catchBlockStatements = new HashMap<>();
        BlockStatement catchAllBlockStatement = null;
        for (XQueryParser.CatchCaseStatementContext catchCtx : ctx.catches) {
            BlockStatement catchBlockStatement = (BlockStatement) this.visitBlockStatement(catchCtx.catch_block);
            for (XQueryParser.QnameContext qnameCtx : catchCtx.errors) {
                Name name = parseName(qnameCtx, false, false, false);
                if (!catchBlockStatements.containsKey(name.getLocalName())) {
                    catchBlockStatements.put(name.getLocalName(), catchBlockStatement);
                }
            }
            boolean doesCatchAll = !catchCtx.jokers.isEmpty();
            if (doesCatchAll && catchAllBlockStatement == null) {
                catchAllBlockStatement = catchBlockStatement;
            }
        }
        return new TryCatchStatement(
                tryBlock,
                catchBlockStatements,
                catchAllBlockStatement,
                createMetadataFromContext(ctx)
        );
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
                        createMetadataFromContext(ctx)
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
            return visitSingleSlash(ctx.singleslash);
        } else if (ctx.doubleslash != null) {
            return visitDoubleSlash(ctx.doubleslash);
        } else if (ctx.relative != null) {
            return visitRelativeWithoutSlash(ctx.relative);
        }
        return visitSingleSlashNoStepExpr(ctx);
    }

    private Node visitSingleSlashNoStepExpr(XQueryParser.PathExprContext ctx) {
        // Case: No StepExpr, only dash
        return new FunctionCallExpression(
                Name.createVariableInDefaultXQueryTypeNamespace("root"),
                Collections.emptyList(),
                createMetadataFromContext(ctx)
        );
    }

    private Node visitRelativeWithoutSlash(XQueryParser.RelativePathExprContext relativeContext) {
        if (relativeContext.stepExpr().size() == 1 && relativeContext.stepExpr(0).postFixExpr() != null) {
            // We only have a postfix expression, not a path expression
            return this.visitPostFixExpr(relativeContext.stepExpr(0).postFixExpr());
        }
        return getSlashes(relativeContext, null);
    }

    private Node visitDoubleSlash(XQueryParser.RelativePathExprContext doubleSlashContext) {
        FunctionCallExpression functionCallExpression = new FunctionCallExpression(
                Name.createVariableInDefaultXQueryTypeNamespace("root"),
                Collections.emptyList(),
                createMetadataFromContext(doubleSlashContext)
        );
        StepExpr stepExpr = new ForwardStepExpr(
                ForwardAxis.DESCENDANT_OR_SELF,
                new AnyKindTest(),
                createMetadataFromContext(doubleSlashContext)
        );
        Expression starter = new SlashExpr(
                functionCallExpression,
                stepExpr,
                createMetadataFromContext(doubleSlashContext)
        );
        return getSlashes(doubleSlashContext, starter);
    }

    private Node visitSingleSlash(XQueryParser.RelativePathExprContext singleSlashContext) {
        FunctionCallExpression functionCallExpression = new FunctionCallExpression(
                Name.createVariableInDefaultXQueryTypeNamespace("root"),
                Collections.emptyList(),
                createMetadataFromContext(singleSlashContext)
        );
        return getSlashes(singleSlashContext, functionCallExpression);
    }

    /**
     * This method takes a leftMost expression and a path and returns a nested tree of slash expressions which
     * correspond to the steps in the path applied to the leftMost expression
     */
    private Expression getSlashes(
            XQueryParser.RelativePathExprContext relativePathExprContext,
            Expression leftMost
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
                        createMetadataFromContext(relativePathExprContext)
                );
                if (currentTop == null) {
                    currentTop = intermediaryStepExpr;
                } else {
                    currentTop = new SlashExpr(
                            currentTop,
                            intermediaryStepExpr,
                            createMetadataFromContext(relativePathExprContext)
                    );

                }
            }
            if (currentTop == null) {
                currentTop = currentStepExpr;
            } else {
                currentTop = new SlashExpr(
                        currentTop,
                        currentStepExpr,
                        createMetadataFromContext(relativePathExprContext)
                );
            }
        }
        return currentTop;
    }

    @Override
    public Node visitStepExpr(XQueryParser.StepExprContext ctx) {
        if (ctx.postFixExpr() == null) {
            Expression stepExpr = getStep(ctx.axisStep());
            for (XQueryParser.PredicateContext predicateContext : ctx.axisStep().predicateList().predicate()) {
                Expression predicate = (Expression) this.visitPredicate(predicateContext);
                stepExpr = new FilterExpression(
                        stepExpr,
                        predicate,
                        createMetadataFromContext(ctx)
                );
            }
            return stepExpr;
        }
        return this.visitPostFixExpr(ctx.postFixExpr());
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
            nodeTest = getNodeTest(ctx.abbrevForwardStep().nodeTest());
            if (ctx.abbrevForwardStep().Kat_symbol() != null) {
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
        nodeTest = getNodeTest(ctx.nodeTest());
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
        NodeTest nodeTest = getNodeTest(ctx.nodeTest());
        return new ReverseStepExpr(reverseAxis, nodeTest, createMetadataFromContext(ctx));
    }

    private NodeTest getNodeTest(XQueryParser.NodeTestContext nodeTestContext) {
        if (nodeTestContext.nameTest() == null) {
            // kind test
            return getKindTest(nodeTestContext.kindTest().children.get(0));
        }
        if (nodeTestContext.nameTest().wildcard() == null) {
            Name name = parseName(nodeTestContext.nameTest().qname(), false, false, false);
            return new NameTest(name);
        } else {
            String wildcard = nodeTestContext.nameTest().wildcard().getText();
            return new NameTest(wildcard);
        }
    }

    private NodeTest getKindTest(ParseTree kindTest) {
        if (kindTest instanceof XQueryParser.DocumentTestContext) {
            XQueryParser.DocumentTestContext docContext = (XQueryParser.DocumentTestContext) kindTest;
            if (docContext.schemaElementTest() != null) {
                throw new UnsupportedFeatureException(
                        "Kind tests of type document, element, attribute, text and any are supported at the moment",
                        createMetadataFromContext((ParserRuleContext) kindTest)
                );
            }
            if (docContext.elementTest() == null) {
                return new DocumentTest(null);
            }
            return new DocumentTest(getKindTest(docContext.elementTest()));
        } else if (kindTest instanceof XQueryParser.ElementTestContext) {
            XQueryParser.ElementTestContext elementContext = (XQueryParser.ElementTestContext) kindTest;
            Name elementName;
            if (elementContext.elementNameOrWildcard() != null) {
                boolean hasWildcard = elementContext.elementNameOrWildcard().elementName() == null;
                if (!hasWildcard) {
                    elementName = parseName(
                        elementContext.elementNameOrWildcard().elementName().qname(),
                        false,
                        false,
                        false
                    );
                    if (elementContext.typeName() == null) {
                        return new ElementTest(elementName, null);
                    }
                    Name typeName = parseName(elementContext.typeName().qname(), false, false, false);
                    return new ElementTest(elementName, typeName);
                }
                return new ElementTest(true);
            }
            return new ElementTest();
        } else if (kindTest instanceof XQueryParser.AttributeTestContext) {
            XQueryParser.AttributeTestContext attributeTestContext =
                (XQueryParser.AttributeTestContext) kindTest;
            Name elementName;
            if (attributeTestContext.attributeNameOrWildcard() != null) {
                boolean hasWildcard = attributeTestContext.attributeNameOrWildcard().attributeName() == null;
                if (!hasWildcard) {
                    elementName = parseName(
                        attributeTestContext.attributeNameOrWildcard().attributeName().qname(),
                        false,
                        false,
                        false
                    );
                    if (attributeTestContext.typeName() != null) {
                        Name typeName = parseName(attributeTestContext.typeName().qname(), false, false, false);
                        return new AttributeTest(elementName, typeName);
                    } else {
                        return new AttributeTest(elementName, null);
                    }
                } else {
                    return new AttributeTest(true);
                }
            }
            return new AttributeTest();
        } else if (kindTest instanceof XQueryParser.TextTestContext) {
            return new TextTest();
        } else if (kindTest instanceof XQueryParser.AnyKindTestContext) {
            return new AnyKindTest();
        } else {
            throw new UnsupportedFeatureException(
                    "Kind tests of type document, element, attribute, text and any are supported at the moment",
                    createMetadataFromContext((ParserRuleContext) kindTest)
            );
        }
    }


    // end region

    public void processNamespaceDecl(XQueryParser.NamespaceDeclContext ctx) {
        bindNamespace(
            ctx.NCName().getText(),
            processURILiteral(ctx.uriLiteral()),
            generateMetadata(ctx.getStop())
        );
    }

    public void bindNamespace(String prefix, String namespace, ExceptionMetadata metadata) {
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
        return ctx.getText().substring(1, ctx.getText().length() - 1);
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

    public LibraryModule processModuleImport(XQueryParser.ModuleImportContext ctx) {
        String namespace = processURILiteral(ctx.targetNamespace);
        URI resolvedURI = FileSystemUtil.resolveURI(
            this.moduleContext.getStaticBaseURI(),
            namespace,
            generateMetadata(ctx.getStop())
        );
        LibraryModule libraryModule = null;
        try {
            libraryModule = VisitorHelpers.parseLibraryModuleFromLocation(
                resolvedURI,
                this.configuration,
                this.moduleContext,
                generateMetadata(ctx.getStop())
            );
            if (!resolvedURI.toString().equals(libraryModule.getNamespace())) {
                throw new ModuleNotFoundException(
                        "A module with namespace "
                            + resolvedURI.toString()
                            + " was not found. The namespace of the module at this location was: "
                            + libraryModule.getNamespace(),
                        generateMetadata(ctx.getStop())
                );
            }
        } catch (IOException e) {
            RumbleException exception = new ModuleNotFoundException(
                    "I/O error while attempting to import a module: " + namespace + " Cause: " + e.getMessage(),
                    generateMetadata(ctx.getStop())
            );
            exception.initCause(e);
            throw exception;
        } catch (CannotRetrieveResourceException e) {
            RumbleException exception = new ModuleNotFoundException(
                    "Module not found: " + namespace + " Cause: " + e.getMessage(),
                    generateMetadata(ctx.getStop())
            );
            exception.initCause(e);
            throw exception;
        }
        if (ctx.NCName() != null) {
            bindNamespace(
                ctx.NCName().getText(),
                resolvedURI.toString(),
                generateMetadata(ctx.getStop())
            );
        }
        return libraryModule;
    }

    public ExceptionMetadata generateMetadata(Token token) {
        return new ExceptionMetadata(
                this.moduleContext.getStaticBaseURI().toString(),
                token.getLine(),
                token.getCharPositionInLine(),
                this.code
        );
    }

    private List<Annotation> processAnnotations(XQueryParser.AnnotationsContext annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (XQueryParser.AnnotationContext annotationContext : annotations.annotation()) {
            // for backwards compatibility, the specification allows for updating without % sign
            if (annotationContext.updating != null) {
                Name name = Name.createVariableInDefaultAnnotationsNamespace("updating");
                parsedAnnotations.add(new Annotation(name, null));
                continue;
            }
            XQueryParser.QnameContext qnameContext = annotationContext.qname();
            Name name = parseName(qnameContext, false, false, true);
            if (!annotationContext.Literal().isEmpty()) {
                throw new OurBadException("Literals are currently not supported in annotations!");
            }
            parsedAnnotations.add(new Annotation(name, null));
        }

        return parsedAnnotations;
    }

}
