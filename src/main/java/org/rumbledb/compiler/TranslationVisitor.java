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
import org.rumbledb.exceptions.JsoniqVersionException;
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
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
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
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.parser.JsoniqParser.DefaultCollationDeclContext;
import org.rumbledb.parser.JsoniqParser.EmptyOrderDeclContext;
import org.rumbledb.parser.JsoniqParser.SetterContext;
import org.rumbledb.parser.JsoniqParser.UriLiteralContext;
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
public class TranslationVisitor extends org.rumbledb.parser.JsoniqBaseVisitor<Node> {

    private StaticContext moduleContext;
    private RumbleRuntimeConfiguration configuration;
    private boolean isMainModule;
    private String code;

    public TranslationVisitor(
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
    public Node visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty() && !ctx.vers.getText().trim().equals("1.0")) {
            throw new JsoniqVersionException(createMetadataFromContext(ctx));
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
    public Node visitProgram(JsoniqParser.ProgramContext ctx) {
        StatementsAndOptionalExpr statementsAndOptionalExpr = (StatementsAndOptionalExpr) this
            .visitStatementsAndOptionalExpr(ctx.statementsAndOptionalExpr());
        return new Program(statementsAndOptionalExpr, createMetadataFromContext(ctx));
    }

    // end region

    @Override
    public Node visitLibraryModule(JsoniqParser.LibraryModuleContext ctx) {
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
    public Node visitProlog(JsoniqParser.PrologContext ctx) {
        // bind namespaces
        for (JsoniqParser.NamespaceDeclContext namespace : ctx.namespaceDecl()) {
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

    public Name parseName(JsoniqParser.QnameContext ctx, boolean isFunction, boolean isType) {
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
    public Node visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        Name name = parseName(ctx.qname(), true, false);
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false);
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

        return new InlineFunctionExpression(
                annotations,
                name,
                fnParams,
                fnReturnType,
                funcBody,
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
            definitionItem = ItemParser.getItemFromString(definitionString, createMetadataFromContext(ctx));
        } catch (ParsingException e) {
            ParsingException pe = new ParsingException(
                    "A type definition must be a JSON literal: no dynamic evaluation is allowed.",
                    createMetadataFromContext(ctx)
            );
            pe.initCause(e);
            throw pe;
        }
        Name name = parseName(ctx.qname(), false, true);
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
        if (content instanceof JsoniqParser.ExprSimpleContext) {
            return this.visitExprSimple((JsoniqParser.ExprSimpleContext) content);
        }
        if (content instanceof JsoniqParser.FlowrExprContext) {
            return this.visitFlowrExpr((JsoniqParser.FlowrExprContext) content);
        }
        if (content instanceof JsoniqParser.IfExprContext) {
            return this.visitIfExpr((JsoniqParser.IfExprContext) content);
        }
        if (content instanceof JsoniqParser.SwitchExprContext) {
            return this.visitSwitchExpr((JsoniqParser.SwitchExprContext) content);
        }
        if (content instanceof JsoniqParser.TypeSwitchExprContext) {
            return this.visitTypeSwitchExpr((JsoniqParser.TypeSwitchExprContext) content);
        }
        if (content instanceof JsoniqParser.TryCatchExprContext) {
            return this.visitTryCatchExpr((JsoniqParser.TryCatchExprContext) content);
        }
        throw new OurBadException("Unrecognized ExprSingle:" + content.getClass().getName());
    }
    // endregion

    // begin region ExprSimple
    @Override
    public Node visitExprSimple(JsoniqParser.ExprSimpleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof JsoniqParser.OrExprContext) {
            return this.visitOrExpr((JsoniqParser.OrExprContext) content);
        }
        if (content instanceof JsoniqParser.QuantifiedExprContext) {
            return this.visitQuantifiedExpr((JsoniqParser.QuantifiedExprContext) content);
        }
        if (content instanceof JsoniqParser.DeleteExprContext) {
            return this.visitDeleteExpr((JsoniqParser.DeleteExprContext) content);
        }
        if (content instanceof JsoniqParser.InsertExprContext) {
            return this.visitInsertExpr((JsoniqParser.InsertExprContext) content);
        }
        if (content instanceof JsoniqParser.ReplaceExprContext) {
            return this.visitReplaceExpr((JsoniqParser.ReplaceExprContext) content);
        }
        if (content instanceof JsoniqParser.RenameExprContext) {
            return this.visitRenameExpr((JsoniqParser.RenameExprContext) content);
        }
        if (content instanceof JsoniqParser.AppendExprContext) {
            return this.visitAppendExpr((JsoniqParser.AppendExprContext) content);
        }
        if (content instanceof JsoniqParser.TransformExprContext) {
            return this.visitTransformExpr((JsoniqParser.TransformExprContext) content);
        }
        throw new OurBadException("Unrecognized ExprSimple.");
    }

    // endregion

    // region Flowr
    @Override
    public Node visitFlowrExpr(JsoniqParser.FlowrExprContext ctx) {
        Clause clause;
        // check the start clause, for or let
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }

        Clause previousFLWORClause = clause.getLastClause();

        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext) {
                clause = (Clause) this.visitForClause((JsoniqParser.ForClauseContext) child);
            } else if (child instanceof JsoniqParser.LetClauseContext) {
                clause = (Clause) this.visitLetClause((JsoniqParser.LetClauseContext) child);
            } else if (child instanceof JsoniqParser.WhereClauseContext) {
                clause = (Clause) this.visitWhereClause((JsoniqParser.WhereClauseContext) child);
            } else if (child instanceof JsoniqParser.GroupByClauseContext) {
                clause = (Clause) this.visitGroupByClause((JsoniqParser.GroupByClauseContext) child);
            } else if (child instanceof JsoniqParser.OrderByClauseContext) {
                clause = (Clause) this.visitOrderByClause((JsoniqParser.OrderByClauseContext) child);
            } else if (child instanceof JsoniqParser.CountClauseContext) {
                clause = (Clause) this.visitCountClause((JsoniqParser.CountClauseContext) child);
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
            result = new OrExpression(result, rightExpression, createMetadataFromContext(ctx));
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
            result = new AndExpression(result, rightExpression, createMetadataFromContext(ctx));
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
    public Node visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        Expression result = (Expression) this.visitRangeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (JsoniqParser.RangeExprContext child : ctx.rhs) {
            Expression rightExpression = (Expression) this.visitRangeExpr(child);
            result = new StringConcatExpression(result, rightExpression, createMetadataFromContext(ctx));
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
                    createMetadataFromContext(ctx)
            );
        }
        return result;
    }

    @Override
    public Node visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        Expression result = (Expression) this.visitInstanceOfExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs.size(); ++i) {
            JsoniqParser.InstanceOfExprContext child = ctx.rhs.get(i);
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
    public Node visitSimpleMapExpr(JsoniqParser.SimpleMapExprContext ctx) {
        Expression result = (Expression) this.visitPostFixExpr(ctx.main_expr);
        if (ctx.map_expr == null || ctx.map_expr.isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.map_expr.size(); ++i) {
            JsoniqParser.PostFixExprContext child = ctx.map_expr.get(i);
            Expression rightExpression = (Expression) this.visitPostFixExpr(child);
            result = new SimpleMapExpression(
                    result,
                    rightExpression,
                    createMetadataFromContext(ctx)
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
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext(ctx.arguments.get(i)));
            if (functionCallContext.qname() != null) {
                Name name = parseName(functionCallContext.qname(), true, false);
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
        return this.visitAnnotateExpr(ctx.annotate_expr);
    }

    @Override
    public Node visitAnnotateExpr(JsoniqParser.AnnotateExprContext ctx) {
        Expression mainExpr = (Expression) this.visitExpr(ctx.expr());
        SequenceType sequenceType = this.processSequenceType(ctx.sequenceType());
        return new ValidateTypeExpression(mainExpr, false, sequenceType, createMetadataFromContext(ctx));
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
                if (currentPair.lhs != null) {
                    keys.add((Expression) this.visitExprSingle(currentPair.lhs));
                } else {
                    keys.add(new StringLiteralExpression(currentPair.name.getText(), createMetadataFromContext(ctx)));
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

    public Expression getMainExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 1)) {
            if (child instanceof JsoniqParser.ObjectLookupContext) {
                Expression expr = (Expression) this.visitObjectLookup((JsoniqParser.ObjectLookupContext) child);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ArrayLookupContext) {
                Expression expr = (Expression) this.visitArrayLookup((JsoniqParser.ArrayLookupContext) child);
                mainExpression = new ArrayLookupExpression(
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

    public Expression getLocatorExpressionFromUpdateLocatorContext(JsoniqParser.UpdateLocatorContext ctx) {
        ParseTree locatorExprCtx = ctx.getChild(ctx.getChildCount() - 1);
        if (locatorExprCtx instanceof JsoniqParser.ObjectLookupContext) {
            return (Expression) this.visitObjectLookup((JsoniqParser.ObjectLookupContext) locatorExprCtx);
        }
        if (locatorExprCtx instanceof JsoniqParser.ArrayLookupContext) {
            return (Expression) this.visitArrayLookup((JsoniqParser.ArrayLookupContext) locatorExprCtx);
        } else {
            throw new OurBadException("Unrecognized locator found in update expression.");
        }
    }

    // endregion

    // region postfix
    @Override
    public Node visitPostFixExpr(JsoniqParser.PostFixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext) {
                Expression expr = (Expression) this.visitPredicate((JsoniqParser.PredicateContext) child);
                mainExpression = new FilterExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ObjectLookupContext) {
                Expression expr = (Expression) this.visitObjectLookup((JsoniqParser.ObjectLookupContext) child);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ArrayLookupContext) {
                Expression expr = (Expression) this.visitArrayLookup((JsoniqParser.ArrayLookupContext) child);
                mainExpression = new ArrayLookupExpression(
                        mainExpression,
                        expr,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ArrayUnboxingContext) {
                this.visitArrayUnboxing((JsoniqParser.ArrayUnboxingContext) child);
                mainExpression = new ArrayUnboxingExpression(mainExpression, createMetadataFromContext(ctx));
            } else if (child instanceof JsoniqParser.ArgumentListContext) {
                List<Expression> arguments = getArgumentsFromArgumentListContext(
                    (JsoniqParser.ArgumentListContext) child
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
    public Node visitPredicate(JsoniqParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }

    @Override
    public Node visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        // TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        if (ctx.lt != null) {
            return new StringLiteralExpression(
                    ctx.lt.getText().substring(1, ctx.lt.getText().length() - 1),
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
        if (child instanceof JsoniqParser.VarRefContext) {
            return this.visitVarRef((JsoniqParser.VarRefContext) child);
        }
        if (child instanceof JsoniqParser.ObjectConstructorContext) {
            return this.visitObjectConstructor((JsoniqParser.ObjectConstructorContext) child);
        }
        if (child instanceof JsoniqParser.ArrayConstructorContext) {
            return this.visitArrayConstructor((JsoniqParser.ArrayConstructorContext) child);
        }
        if (child instanceof JsoniqParser.ParenthesizedExprContext) {
            return this.visitParenthesizedExpr((JsoniqParser.ParenthesizedExprContext) child);
        }
        if (child instanceof JsoniqParser.StringLiteralContext) {
            return new StringLiteralExpression(
                    child.getText().substring(1, child.getText().length() - 1),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof TerminalNode) {
            return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
        }
        if (child instanceof JsoniqParser.ContextItemExprContext) {
            return this.visitContextItemExpr((JsoniqParser.ContextItemExprContext) child);
        }
        if (child instanceof JsoniqParser.FunctionCallContext) {
            return this.visitFunctionCall((JsoniqParser.FunctionCallContext) child);
        }
        if (child instanceof JsoniqParser.FunctionItemExprContext) {
            return this.visitFunctionItemExpr((JsoniqParser.FunctionItemExprContext) child);
        }
        if (child instanceof JsoniqParser.BlockExprContext) {
            return this.visitBlockExpr((JsoniqParser.BlockExprContext) child);
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
                if (currentPair.lhs != null) {
                    keys.add((Expression) this.visitExprSingle(currentPair.lhs));
                } else {
                    keys.add(new StringLiteralExpression(currentPair.name.getText(), createMetadataFromContext(ctx)));
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
    public Node visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        if (ctx.expr() == null) {
            return new ArrayConstructorExpression(createMetadataFromContext(ctx));
        }
        Expression content = (Expression) this.visitExpr(ctx.expr());
        return new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
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
        Name name = parseName(ctx.qname(), false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    public SequenceType processSequenceType(JsoniqParser.SequenceTypeContext ctx) {
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

    public SequenceType processSingleType(JsoniqParser.SingleTypeContext ctx) {
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

    public ItemType processItemType(JsoniqParser.ItemTypeContext itemTypeContext) {
        if (itemTypeContext.NullLiteral() != null) {
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
        Name name = parseName(itemTypeContext.qname(), false, true);
        if (!BuiltinTypesCatalogue.typeExists(name)) {
            return new ItemTypeReference(name);
        }
        return BuiltinTypesCatalogue.getItemTypeByName(name);
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
    public Node visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        Name name = parseName(ctx.fn_name, true, false);
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
        if (child instanceof JsoniqParser.NamedFunctionRefContext) {
            return this.visitNamedFunctionRef((JsoniqParser.NamedFunctionRefContext) child);
        }
        if (child instanceof JsoniqParser.InlineFunctionExprContext) {
            return this.visitInlineFunctionExpr((JsoniqParser.InlineFunctionExprContext) child);
        }
        throw new UnsupportedFeatureException(
                "Function item expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx) {
        Name name = parseName(ctx.fn_name, true, false);
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
    public Node visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        List<Annotation> annotations = processAnnotations(ctx.annotations());
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.ITEM_STAR;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false, false);
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
    public Node visitTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx) {
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
        Clause clause = null;
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
    public Node visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx) {
        Expression tryExpression = (Expression) this.visitExpr(ctx.try_expression);
        Map<String, Expression> catchExpressions = new HashMap<>();
        Expression catchAllExpression = null;
        for (JsoniqParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = (Expression) this.visitExpr(catchCtx.catch_expression);
            for (JsoniqParser.QnameContext qnameCtx : catchCtx.errors) {
                Name name = parseName(qnameCtx, false, false);
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
        if (content instanceof JsoniqParser.ApplyStatementContext) {
            return this.visitApplyStatement((JsoniqParser.ApplyStatementContext) content);
        }
        if (content instanceof JsoniqParser.AssignStatementContext) {
            return this.visitAssignStatement((JsoniqParser.AssignStatementContext) content);
        }
        if (content instanceof JsoniqParser.BlockStatementContext) {
            return this.visitBlockStatement((JsoniqParser.BlockStatementContext) content);
        }
        if (content instanceof JsoniqParser.BreakStatementContext) {
            return this.visitBreakStatement((JsoniqParser.BreakStatementContext) content);
        }
        if (content instanceof JsoniqParser.ContinueStatementContext) {
            return this.visitContinueStatement((JsoniqParser.ContinueStatementContext) content);
        }
        if (content instanceof JsoniqParser.ExitStatementContext) {
            return this.visitExitStatement((JsoniqParser.ExitStatementContext) content);
        }
        if (content instanceof JsoniqParser.FlowrStatementContext) {
            return this.visitFlowrStatement((JsoniqParser.FlowrStatementContext) content);
        }
        if (content instanceof JsoniqParser.IfStatementContext) {
            return this.visitIfStatement((JsoniqParser.IfStatementContext) content);
        }
        if (content instanceof JsoniqParser.SwitchStatementContext) {
            return this.visitSwitchStatement((JsoniqParser.SwitchStatementContext) content);
        }
        if (content instanceof JsoniqParser.TryCatchStatementContext) {
            return this.visitTryCatchStatement((JsoniqParser.TryCatchStatementContext) content);
        }
        if (content instanceof JsoniqParser.TypeSwitchStatementContext) {
            return this.visitTypeSwitchStatement((JsoniqParser.TypeSwitchStatementContext) content);
        }
        if (content instanceof JsoniqParser.VarDeclStatementContext) {
            return this.visitVarDeclStatement((JsoniqParser.VarDeclStatementContext) content);
        }
        if (content instanceof JsoniqParser.WhileStatementContext) {
            return this.visitWhileStatement((JsoniqParser.WhileStatementContext) content);
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
        Name paramName = parseName(ctx.qname(), false, false);
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
    public Node visitFlowrStatement(JsoniqParser.FlowrStatementContext ctx) {
        Clause clause;
        // Check for start clause. Only for or let allowed.
        if (ctx.start_for == null) {
            clause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            clause = (Clause) this.visitForClause(ctx.start_for);
        }
        Clause lastFlowrClause = clause.getLastClause();
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext) {
                clause = (Clause) this.visitForClause((JsoniqParser.ForClauseContext) child);
            } else if (child instanceof JsoniqParser.LetClauseContext) {
                clause = (Clause) this.visitLetClause((JsoniqParser.LetClauseContext) child);
            } else if (child instanceof JsoniqParser.WhereClauseContext) {
                clause = (Clause) this.visitWhereClause((JsoniqParser.WhereClauseContext) child);
            } else if (child instanceof JsoniqParser.GroupByClauseContext) {
                clause = (Clause) this.visitGroupByClause((JsoniqParser.GroupByClauseContext) child);
            } else if (child instanceof JsoniqParser.OrderByClauseContext) {
                clause = (Clause) this.visitOrderByClause((JsoniqParser.OrderByClauseContext) child);
            } else if (child instanceof JsoniqParser.CountClauseContext) {
                clause = (Clause) this.visitCountClause((JsoniqParser.CountClauseContext) child);
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
        Map<String, BlockStatement> catchBlockStatements = new HashMap<>();
        BlockStatement catchAllBlockStatement = null;
        for (JsoniqParser.CatchCaseStatementContext catchCtx : ctx.catches) {
            BlockStatement catchBlockStatement = (BlockStatement) this.visitBlockStatement(catchCtx.catch_block);
            for (JsoniqParser.QnameContext qnameCtx : catchCtx.errors) {
                Name name = parseName(qnameCtx, false, false);
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

    // end region

    public void processNamespaceDecl(JsoniqParser.NamespaceDeclContext ctx) {
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

    public LibraryModule processModuleImport(JsoniqParser.ModuleImportContext ctx) {
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

    private List<Annotation> processAnnotations(JsoniqParser.AnnotationsContext annotations) {
        List<Annotation> parsedAnnotations = new ArrayList<>();
        annotations.annotation().forEach(annotationContext -> {
            JsoniqParser.QnameContext qnameContext = annotationContext.qname();
            Name name = parseName(qnameContext, false, false);
            if (!annotationContext.Literal().isEmpty()) {
                throw new OurBadException("Literals are currently not supported in annotations!");
            }
            parsedAnnotations.add(new Annotation(name, null));
            // List<Expression> literals = new ArrayList<>();
            // ExceptionMetadata metadata = createMetadataFromContext(annotationContext);
            // annotationContext.Literal().forEach(literal -> {
            // literals.add(getLiteralExpressionFromToken(literal.getText(), metadata));
            // });
            // parsedAnnotations.add(new Annotation(name, literals));
        });

        return parsedAnnotations;
    }

}


