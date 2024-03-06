package org.rumbledb.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
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
import org.rumbledb.exceptions.XMLUnsupportedException;
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
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ArrayUnboxingExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
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
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.parser.XQueryParser;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
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

import static org.rumbledb.types.SequenceType.ITEM_STAR;

public class XQueryTranslationVisitor extends org.rumbledb.parser.XQueryParserBaseVisitor<Node> {
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
        this.moduleContext.bindNamespace("local", Name.LOCAL_NS);
        this.configuration = configuration;
        this.isMainModule = isMainModule;
        this.code = code;
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(XQueryParser.ModuleContext ctx) {
        XQueryParser.VersionDeclContext ver = ctx.versionDecl();
        if (!(ver == null) && !ver.version.isEmpty()) {
            String version = ver.version.getText().trim();
            if (!(version.equals("3.1") || version.equals("3.0") || version.equals("1.0"))) {
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

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        return generateMetadata(ctx.getStart());
    }

    public ExceptionMetadata generateMetadata(Token token) {
        return new ExceptionMetadata(
                this.moduleContext.getStaticBaseURI().toString(),
                token.getLine(),
                token.getCharPositionInLine(),
                this.code
        );
    }

    @Override
    public Node visitMainModule(XQueryParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        Expression commaExpression = (Expression) this.visitExpr(ctx.queryBody().expr()); // Had to access query body
        // TODO: Update XQuery grammar to support scripting.
        ExceptionMetadata metadata = createMetadataFromContext(ctx);
        Program program = new Program(new StatementsAndOptionalExpr(null, commaExpression, metadata), metadata);
        MainModule module = new MainModule(prolog, program, metadata);
        module.setStaticContext(this.moduleContext);
        return module;
    }

    // region Library

    @Override
    public Node visitLibraryModule(XQueryParser.LibraryModuleContext ctx) {
        String prefix = ctx.moduleDecl().ncName().getText();
        String namespace = processURILiteral(ctx.moduleDecl().uriLiteral());
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


    @Override
    public Node visitProlog(XQueryParser.PrologContext ctx) {
        // bind namespaces
        for (XQueryParser.NamespaceDeclContext namespace : ctx.namespaceDecl()) {
            this.processNamespaceDecl(namespace);
        }
        if (!ctx.defaultNamespaceDecl().isEmpty()) {
            throw new XMLUnsupportedException(
                    "defaultNamespaceDeclContext not supported YET",
                    createMetadataFromContext(ctx)
            );
        }
        if (!ctx.schemaImport().isEmpty()) {
            throw new XMLUnsupportedException("schemaImport not supported", createMetadataFromContext(ctx));
        }
        List<XQueryParser.SetterContext> setters = ctx.setter();
        boolean emptyOrderSet = false;
        boolean defaultCollationSet = false;
        for (XQueryParser.SetterContext setterContext : setters) {
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
            if (setterContext.copyNamespacesDecl() != null) {
                throw new XMLUnsupportedException("copyNamespacesDecl not supported", createMetadataFromContext(ctx));
            }
            if (setterContext.constructionDecl() != null) {
                throw new XMLUnsupportedException("constructionDecl not supported", createMetadataFromContext(ctx));
            }
            if (setterContext.boundarySpaceDecl() != null) {
                throw new XMLUnsupportedException("boundarySpaceDecl not supported", createMetadataFromContext(ctx));
            }
            if (setterContext.decimalFormatDecl() != null) {
                // TODO outside of the scope for thesis
                throw new XMLUnsupportedException(
                        "decimalFormatDecl not supported YET",
                        createMetadataFromContext(ctx)
                );
            }
            if (setterContext.baseURIDecl() != null) {
                // TODO outside of the scope for thesis
                throw new XMLUnsupportedException("baseURIDecl not supported YET", createMetadataFromContext(ctx));
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
            } else if (annotatedDeclaration.contextItemDecl() != null) {
                // TODO outside of the scope for thesis
                throw new XMLUnsupportedException("contextItemDecl not supported YET", createMetadataFromContext(ctx));
            } else if (annotatedDeclaration.optionDecl() != null) {
                throw new XMLUnsupportedException("optionDecl not supported", createMetadataFromContext(ctx));
            }
        }

        for (XQueryParser.ModuleImportContext module : ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        Prolog prolog = new Prolog(
                globalVariables,
                functionDeclarations,
                Collections.emptyList(),
                createMetadataFromContext(ctx)
        );
        for (LibraryModule libraryModule : libraryModules) {
            prolog.addImportedModule(libraryModule);
        }
        return prolog;
    }

    @Override
    public Node visitVarDecl(XQueryParser.VarDeclContext ctx) {
        SequenceType seq;
        boolean external = false;

        if (ctx.annotations() != null) {
            processAnnotations(ctx.annotations());
        }

        Name var = ((VariableReferenceExpression) this.visitVarName(ctx.varName())).getVariableName();
        if (ctx.typeDeclaration() != null) {
            seq = this.processSequenceType(ctx.typeDeclaration().sequenceType());
        } else {
            seq = SequenceType.ITEM_STAR;
        }

        XQueryParser.ExprSingleContext exprSingle = null;

        if (ctx.varDefaultValue() != null) {
            external = true;
            exprSingle = ctx.varDefaultValue().exprSingle();
        }

        Expression expr = null;
        if (ctx.varValue() != null) {
            exprSingle = ctx.varValue().exprSingle();
        }
        if (exprSingle != null) {
            expr = (Expression) this.visitExprSingle(exprSingle);
            if (!seq.equals(SequenceType.ITEM_STAR)) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }

        return new VariableDeclaration(var, external, seq, expr, createMetadataFromContext(ctx));
    }

    private void processAnnotations(XQueryParser.AnnotationsContext annotations) {
        if (annotations.annotation().size() > 1) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException(
                    "multipleAnnotations not supported YET",
                    createMetadataFromContext(annotations)
            );
        }

        XQueryParser.AnnotationContext annotationContext = annotations.annotation().get(0);
        XQueryParser.QNameContext newCtx = annotationContext.qName();

        String localName;
        if (newCtx.ncName() != null) {
            if (newCtx.ncName().local_name != null) {
                localName = newCtx.ncName().local_name.getText();
            } else {
                localName = newCtx.ncName().local_namekw.getText();
            }
            if (!localName.equals("public")) {
                // TODO outside of the scope for thesis
                throw new XMLUnsupportedException("private not supported YET", createMetadataFromContext(annotations));
            }
        } else {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException("prefix not supported YET", createMetadataFromContext(annotations));
        }
    }

    @Override
    public Node visitFunctionDecl(XQueryParser.FunctionDeclContext ctx) {
        Name name = parseName(ctx.eqName(), true, false);
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = ITEM_STAR;
        Name paramName;
        SequenceType paramType;

        if (ctx.annotations() != null) {
            processAnnotations(ctx.annotations());
        }

        if (ctx.functionParams() != null) {
            for (XQueryParser.FunctionParamContext param : ctx.functionParams().functionParam()) {
                paramName = parseName(param.qName(), false, false);
                paramType = ITEM_STAR;
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            name,
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.typeDeclaration() != null) {
                    paramType = this.processSequenceType(param.typeDeclaration().sequenceType());
                } else {
                    paramType = SequenceType.ITEM_STAR;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.functionReturn() != null) {
            fnReturnType = this.processSequenceType(ctx.functionReturn().sequenceType());
        } else {
            fnReturnType = SequenceType.ITEM_STAR;
        }

        Expression bodyExpression = null;
        if (ctx.functionBody() != null) {
            bodyExpression = (Expression) this.visitExpr(ctx.functionBody().enclosedExpression().expr());
        } else {
            bodyExpression = new CommaExpression(createMetadataFromContext(ctx));
        }

        return new InlineFunctionExpression(
                name,
                fnParams,
                fnReturnType,
                bodyExpression,
                createMetadataFromContext(ctx)
        );
    }

    public void processNamespaceDecl(XQueryParser.NamespaceDeclContext ctx) {
        bindNamespace(
            ctx.ncName().getText(),
            processURILiteral(ctx.uriLiteral()),
            generateMetadata(ctx.getStop())
        );
    }

    private void processEmptySequenceOrder(XQueryParser.EmptyOrderDeclContext ctx) {
        if (ctx.type.getText().equals("least")) {
            this.moduleContext.setEmptySequenceOrderLeast(true);
        }
        if (ctx.type.getText().equals("greatest")) {
            this.moduleContext.setEmptySequenceOrderLeast(false);
        }
    }

    private void processDefaultCollation(XQueryParser.DefaultCollationDeclContext ctx) {
        String uri = processURILiteral(ctx.uriLiteral());
        if (!uri.equals(Name.DEFAULT_COLLATION_NS)) {
            throw new DefaultCollationException(
                    "Unknown collation: " + uri,
                    createMetadataFromContext(ctx.uriLiteral())
            );
        }
    }

    public LibraryModule processModuleImport(XQueryParser.ModuleImportContext ctx) {
        String namespace = processURILiteral(ctx.nsURI);
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
        if (ctx.ncName() != null) {
            bindNamespace(
                ctx.ncName().getText(),
                resolvedURI.toString(),
                generateMetadata(ctx.getStop())
            );
        }
        return libraryModule;
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
        if (content instanceof XQueryParser.OrExprContext) {
            return this.visitOrExpr((XQueryParser.OrExprContext) content);
        }
        if (content instanceof XQueryParser.FlworExprContext) {
            return this.visitFlworExpr((XQueryParser.FlworExprContext) content);
        }
        if (content instanceof XQueryParser.IfExprContext) {
            return this.visitIfExpr((XQueryParser.IfExprContext) content);
        }
        if (content instanceof XQueryParser.QuantifiedExprContext) {
            return this.visitQuantifiedExpr((XQueryParser.QuantifiedExprContext) content);
        }
        if (content instanceof XQueryParser.SwitchExprContext) {
            return this.visitSwitchExpr((XQueryParser.SwitchExprContext) content);
        }
        if (content instanceof XQueryParser.TypeswitchExprContext) {
            return this.visitTypeswitchExpr((XQueryParser.TypeswitchExprContext) content);
        }
        if (content instanceof XQueryParser.TryCatchExprContext) {
            return this.visitTryCatchExpr((XQueryParser.TryCatchExprContext) content);
        }
        if (content instanceof XQueryParser.ExistUpdateExprContext) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException(
                    "ExistUpdateExprContext not supported YET",
                    createMetadataFromContext(ctx)
            );
        }
        throw new OurBadException("Unrecognized ExprSingle.");
    }
    // endregion

    // region Or
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
        String op = "";
        // is, <<, >>
        if (ctx.nodeComp() != null)
            throw new XMLUnsupportedException("nodeComp not supported", createMetadataFromContext(ctx));
        // eq, ne, ge, gt, le, lt
        if (ctx.valueComp() != null)
            op = ctx.valueComp().getText();
        // ==, !=, >=, >, <=, <
        if (ctx.generalComp() != null)
            op = ctx.generalComp().getText();
        return new ComparisonExpression(
                mainExpression,
                childExpression,
                ComparisonExpression.ComparisonOperator.fromSymbol(op),
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
                    createMetadataFromContext(ctx)
            );
        }
        return result;
    }

    @Override
    public Node visitUnionExpr(XQueryParser.UnionExprContext ctx) {
        Expression result = (Expression) this.visitIntersectExceptExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        throw new XMLUnsupportedException("UnionExprContext not supported", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitIntersectExceptExpr(XQueryParser.IntersectExceptExprContext ctx) {
        Expression result = (Expression) this.visitInstanceOfExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return result;
        }
        throw new XMLUnsupportedException("IntersectExceptExprContext not supported", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitInstanceOfExpr(XQueryParser.InstanceOfExprContext ctx) {
        Expression mainExpression = (Expression) this.visitTreatExpr(ctx.main_expr);
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

    private ItemType processItemType(XQueryParser.ItemTypeContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.KindTestContext) {
            throw new XMLUnsupportedException("KindTestContext not supported", createMetadataFromContext(ctx));
        } else if (child instanceof XQueryParser.FunctionTestContext) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException("FunctionTestContext not supported YET", createMetadataFromContext(ctx));
        } else if (child instanceof XQueryParser.MapTestContext) {
            XQueryParser.MapTestContext mapTestContext = (XQueryParser.MapTestContext) child;
            if (mapTestContext.typedMapTest() != null)
                throw new XMLUnsupportedException("typedMapTest not supported", createMetadataFromContext(ctx));
            else
                return BuiltinTypesCatalogue.objectItem;
        } else if (child instanceof XQueryParser.ArrayTestContext) {
            XQueryParser.ArrayTestContext arrayTestContext = (XQueryParser.ArrayTestContext) child;
            if (arrayTestContext.typedArrayTest() != null)
                throw new XMLUnsupportedException("typedArrayTest not supported", createMetadataFromContext(ctx));
            else
                return BuiltinTypesCatalogue.arrayItem;
        } else if (child instanceof XQueryParser.AtomicOrUnionTypeContext) {
            return BuiltinTypesCatalogue.getItemTypeByName(
                parseName(ctx.atomicOrUnionType().eqName().qName(), false, true)
            );
        } else if (child instanceof XQueryParser.ParenthesizedItemTestContext) {
            return processItemType(((XQueryParser.ParenthesizedItemTestContext) child).itemType());
        } else {
            // It has to be (KW_ITEM LPAREN RPAREN)
            return BuiltinTypesCatalogue.item;
        }
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

    public SequenceType processSingleType(XQueryParser.SingleTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.EMPTY_SEQUENCE;
        }

        ItemType itemType = BuiltinTypesCatalogue.getItemTypeByName(
            parseName(ctx.item.typeName().eqName().qName(), false, true)
        );
        if (ctx.question.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrZero
            );
        }
        return new SequenceType(itemType);
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

        for (int i = 0; i < ctx.function_call_expr.size(); ++i) {
            XQueryParser.ComplexArrowContext functionCallContext = ctx.function_call_expr.get(i);
            if (functionCallContext.arrowFunctionSpecifier().parenthesizedExpr() != null) {
                throw new XMLUnsupportedException("parenthesizedExpr not supported", createMetadataFromContext(ctx));
            }
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext(functionCallContext.argumentList()));
            Name name;
            if (functionCallContext.arrowFunctionSpecifier().eqName() != null)
                name = parseName(functionCallContext.arrowFunctionSpecifier().eqName(), true, false);
            else
                name = parseName(functionCallContext.arrowFunctionSpecifier().varRef().eqName(), true, false);
            mainExpression = processFunctionCall(name, functionCallContext, children);
        }
        return mainExpression;

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

    private Expression processFunctionCall(Name name, ParserRuleContext ctx, List<Expression> children) {
        if (
            BuiltinTypesCatalogue.typeExists(name)
                && children.size() == 1
        ) {
            return new CastExpression(
                    children.get(0),
                    new SequenceType(BuiltinTypesCatalogue.getItemTypeByName(name), SequenceType.Arity.OneOrZero),
                    createMetadataFromContext(ctx)
            );
        }
        if (
            BuiltinTypesCatalogue.typeExists(Name.createVariableInDefaultTypeNamespace(name.getLocalName()))
                && children.size() == 1
                && name.getNamespace() != null
                && name.getNamespace().equals(Name.JSONIQ_DEFAULT_FUNCTION_NS)
                && !name.getLocalName().equals("boolean")
        ) {
            return new CastExpression(
                    children.get(0),
                    new SequenceType(BuiltinTypesCatalogue.getItemTypeByName(name), SequenceType.Arity.OneOrZero),
                    createMetadataFromContext(ctx)
            );
        }
        return new FunctionCallExpression(
                name,
                children,
                createMetadataFromContext(ctx)
        );
    }

    private Name parseName(XQueryParser.EqNameContext ctx, boolean isFunction, boolean isType) {
        if (ctx.qName() == null)
            throw new XMLUnsupportedException("URIQualifiedName not supported", createMetadataFromContext(ctx));
        XQueryParser.QNameContext newCtx = ctx.qName();
        return parseName(newCtx, isFunction, isType);
    }

    private Name parseName(XQueryParser.QNameContext newCtx, boolean isFunction, boolean isType) {
        String localName = null;
        String prefix = null;
        Name name = null;
        if (newCtx.ncName() != null) {
            if (newCtx.ncName().local_name != null) {
                localName = newCtx.ncName().local_name.getText();
            } else {
                localName = newCtx.ncName().local_namekw.getText();
            }
        } else {
            // We know that it will have single : as parser would throw an error earlier
            String fullText = newCtx.FullQName().getText();
            prefix = fullText.split(":")[0];
            localName = fullText.split(":")[1];
        }
        if (prefix == null) {
            if (isFunction) {
                name = Name.createVariableInDefaultXQueryFunctionNamespace(localName);
            } else if (isType) {
                name = Name.createVariableInDefaultXQueryTypeNamespace(localName);
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
                generateMetadata(newCtx.getStop())
        );
    }

    @Override
    public Node visitUnaryExpr(XQueryParser.UnaryExprContext ctx) {
        if (ctx.main_expr.simpleMapExpr() == null)
            throw new XMLUnsupportedException(
                    "validateExpr and extensionExpr not supported",
                    createMetadataFromContext(ctx)
            );
        Expression mainExpression = (Expression) this.visitSimpleMapExpr(ctx.main_expr.simpleMapExpr());
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
    public Node visitPathExpr(XQueryParser.PathExprContext ctx) {
        if (ctx.singleslash != null || ctx.doubleslash != null)
            throw new XMLUnsupportedException("Path expressions are not supported", createMetadataFromContext(ctx));
        return this.visitRelativePathExpr(ctx.relative);
    }

    @Override
    public Node visitRelativePathExpr(XQueryParser.RelativePathExprContext ctx) {
        if (ctx.stepExpr().size() != 1) {
            throw new XMLUnsupportedException("Path expressions are not supported", createMetadataFromContext(ctx));
        }
        return this.visitStepExpr(ctx.stepExpr().get(0));
    }

    @Override
    public Node visitStepExpr(XQueryParser.StepExprContext ctx) {
        if (ctx.axisStep() != null) {
            throw new XMLUnsupportedException("Path expressions are not supported", createMetadataFromContext(ctx));
        }
        return this.visitPostfixExpr(ctx.postfixExpr());
    }

    @Override
    public Node visitPostfixExpr(XQueryParser.PostfixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof XQueryParser.PredicateContext) {
                Expression expr = (Expression) this.visitPredicate((XQueryParser.PredicateContext) child);
                mainExpression = new FilterExpression(
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
            } else if (child instanceof XQueryParser.LookupContext) {
                // Treat it as objectLookup in JSONiq

                XQueryParser.KeySpecifierContext key = ((XQueryParser.LookupContext) child).keySpecifier();
                if (key.ncName() != null) {
                    Expression expr = new StringLiteralExpression(
                            key.ncName().getText(),
                            createMetadataFromContext(ctx)
                    );
                    mainExpression = new ObjectLookupExpression(
                            mainExpression,
                            expr,
                            createMetadataFromContext(ctx)
                    );
                    mainExpression = new ArrayUnboxingExpression(mainExpression, createMetadataFromContext(ctx));
                } else if (key.IntegerLiteral() != null) {
                    Expression expr = new IntegerLiteralExpression(
                            key.IntegerLiteral().getText(),
                            createMetadataFromContext(ctx)
                    );
                    mainExpression = new ArrayLookupExpression(
                            mainExpression,
                            expr,
                            createMetadataFromContext(ctx)
                    );
                    mainExpression = new ArrayUnboxingExpression(mainExpression, createMetadataFromContext(ctx));
                } else {
                    // TODO not in scope of thesis
                    throw new XMLUnsupportedException(
                            "ParenthesizedExpr and STAR object lookup not supported yet",
                            createMetadataFromContext(ctx)
                    );
                }
            } else {
                throw new OurBadException("Unrecognized postfix expression found.");
            }
        }
        return mainExpression;
    }

    @Override
    public Node visitPrimaryExpr(XQueryParser.PrimaryExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        // In JSONiq we had stringLiteral separated from LITERAL that could then be NumericLiteral | BooleanLiteral
        if (child instanceof XQueryParser.LiteralContext) {
            if (((XQueryParser.LiteralContext) child).numericLiteral() != null)
                return getLiteralExpressionFromToken(child.getText(), createMetadataFromContext(ctx));
            else {
                XQueryParser.StringLiteralContext nestedChild = ((XQueryParser.LiteralContext) child).stringLiteral();
                return new StringLiteralExpression(
                        nestedChild.getText().substring(1, nestedChild.getText().length() - 1),
                        createMetadataFromContext(ctx)
                );
            }
        }
        if (child instanceof XQueryParser.VarRefContext) {
            return this.visitVarRef((XQueryParser.VarRefContext) child);
        }
        if (child instanceof XQueryParser.ParenthesizedExprContext) {
            return this.visitParenthesizedExpr((XQueryParser.ParenthesizedExprContext) child);
        }
        if (child instanceof XQueryParser.ContextItemExprContext) {
            return this.visitContextItemExpr((XQueryParser.ContextItemExprContext) child);
        }
        if (child instanceof XQueryParser.FunctionCallContext) {
            return this.visitFunctionCall((XQueryParser.FunctionCallContext) child);
        }
        if (child instanceof XQueryParser.OrderedExprContext) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException("OrderedExprContext not supported YET", createMetadataFromContext(ctx));
        }
        if (child instanceof XQueryParser.UnorderedExprContext) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException("UnorderedExprContext not supported YET", createMetadataFromContext(ctx));
        }
        if (child instanceof XQueryParser.NodeConstructorContext) {
            throw new XMLUnsupportedException("NodeConstructorContext not supported", createMetadataFromContext(ctx));
        }
        if (child instanceof XQueryParser.FunctionItemExprContext) {
            return this.visitFunctionItemExpr((XQueryParser.FunctionItemExprContext) child);
        }
        if (child instanceof XQueryParser.MapConstructorContext) {
            return this.visitMapConstructor((XQueryParser.MapConstructorContext) child);
        }
        if (child instanceof XQueryParser.ArrayConstructorContext) {
            return this.visitArrayConstructor((XQueryParser.ArrayConstructorContext) child);
        }
        if (child instanceof XQueryParser.StringConstructorContext) {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException(
                    "StringConstructorContext not supported YET",
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof XQueryParser.UnaryLookupContext) {
            return this.visitUnaryLookup((XQueryParser.UnaryLookupContext) child);
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
    public Node visitVarRef(XQueryParser.VarRefContext ctx) {
        Name name = parseName(ctx.eqName(), false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitVarName(XQueryParser.VarNameContext ctx) {
        Name name = parseName(ctx.eqName(), false, false);
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitMapConstructor(XQueryParser.MapConstructorContext ctx) {
        List<Expression> keys = new ArrayList<>();
        List<Expression> values = new ArrayList<>();
        for (XQueryParser.MapConstructorEntryContext currentPair : ctx.mapConstructorEntry()) {
            keys.add((Expression) this.visitExprSingle(currentPair.mapKey));
            // Wrapping value inside array
            values.add(
                new ArrayConstructorExpression(
                        (Expression) this.visitExprSingle(currentPair.mapValue),
                        createMetadataFromContext(ctx)
                )
            );
        }
        return new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitArrayConstructor(XQueryParser.ArrayConstructorContext ctx) {
        if (ctx.squareArrayConstructor() != null) {
            if (ctx.squareArrayConstructor().expr() == null) {
                return new ArrayConstructorExpression(createMetadataFromContext(ctx));
            }

            List<Expression> expressions = new ArrayList<>();
            for (XQueryParser.ExprSingleContext expr : ctx.squareArrayConstructor().expr().exprSingle()) {
                expressions.add((Expression) this.visitExprSingle(expr));
            }

            List<Expression> arrayConstructorExpressions = new ArrayList<>();
            for (Expression expression : expressions) {
                arrayConstructorExpressions.add(
                    new ArrayConstructorExpression(expression, createMetadataFromContext(ctx))
                );
            }

            Expression content = new CommaExpression(arrayConstructorExpressions, createMetadataFromContext(ctx));
            return new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
        } else {
            if (ctx.curlyArrayConstructor().enclosedExpression().expr() == null) {
                return new ArrayConstructorExpression(createMetadataFromContext(ctx));
            }
            Expression content = (Expression) this.visitExpr(ctx.curlyArrayConstructor().enclosedExpression().expr());
            Expression rightExpression = new ArrayConstructorExpression(
                    new ContextItemExpression(createMetadataFromContext(ctx)),
                    createMetadataFromContext(ctx)
            );

            Expression secondContent = new SimpleMapExpression(
                    content,
                    rightExpression,
                    createMetadataFromContext(ctx)
            );
            return new ArrayConstructorExpression(secondContent, createMetadataFromContext(ctx));
        }
    }

    @Override
    public Node visitUnaryLookup(XQueryParser.UnaryLookupContext ctx) {
        // TODO maybe wrap as method, redundant from LookupContext in prefix
        Expression mainExpression = null;
        XQueryParser.KeySpecifierContext key = ctx.keySpecifier();
        if (key.ncName() != null) {
            Expression expr = new StringLiteralExpression(key.ncName().getText(), createMetadataFromContext(ctx));
            mainExpression = new ObjectLookupExpression(
                    new ContextItemExpression(createMetadataFromContext(ctx)),
                    expr,
                    createMetadataFromContext(ctx)
            );
        } else if (key.IntegerLiteral() != null) {
            Expression expr = new IntegerLiteralExpression(
                    key.IntegerLiteral().getText(),
                    createMetadataFromContext(ctx)
            );
            mainExpression = new ArrayLookupExpression(
                    new ContextItemExpression(createMetadataFromContext(ctx)),
                    expr,
                    createMetadataFromContext(ctx)
            );
        } else {
            // TODO not in scope of thesis
            throw new XMLUnsupportedException(
                    "ParenthesizedExpr and STAR object lookup not supported yet",
                    createMetadataFromContext(ctx)
            );
        }
        return mainExpression;
    }

    @Override
    public Node visitParenthesizedExpr(XQueryParser.ParenthesizedExprContext ctx) {
        if (ctx.expr() == null) {
            return new CommaExpression(createMetadataFromContext(ctx));
        }
        return this.visitExpr(ctx.expr());
    }

    private static Expression getLiteralExpressionFromToken(String token, ExceptionMetadata metadataFromContext) {
        // TODO We'll just have to add the functions true(), false() and null()
        if (token.contains("E") || token.contains("e")) {
            return new DoubleLiteralExpression(Double.parseDouble(token), metadataFromContext);
        }
        if (token.contains(".")) {
            return new DecimalLiteralExpression(new BigDecimal(token), metadataFromContext);
        }
        return new IntegerLiteralExpression(token, metadataFromContext);
    }

    @Override
    public Node visitContextItemExpr(XQueryParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    @Override
    public Node visitFunctionCall(XQueryParser.FunctionCallContext ctx) {
        Name name = parseName(ctx.fn_name, true, false);
        return processFunctionCall(name, ctx, getArgumentsFromArgumentListContext(ctx.argumentList()));
    }

    @Override
    public Node visitFunctionItemExpr(XQueryParser.FunctionItemExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof XQueryParser.NamedFunctionRefContext) {
            return this.visitNamedFunctionRef((XQueryParser.NamedFunctionRefContext) child);
        }
        if (child instanceof XQueryParser.InlineFunctionRefContext) {
            return this.visitInlineFunctionRef((XQueryParser.InlineFunctionRefContext) child);
        }
        throw new UnsupportedFeatureException(
                "Function item expression not yet implemented",
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitNamedFunctionRef(XQueryParser.NamedFunctionRefContext ctx) {
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
    public Node visitInlineFunctionRef(XQueryParser.InlineFunctionRefContext ctx) {
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.ITEM_STAR;
        Name paramName;
        SequenceType paramType;
        if (ctx.functionParams() != null) {
            for (XQueryParser.FunctionParamContext param : ctx.functionParams().functionParam()) {
                // TODO here we have qname instead eqName
                paramName = parseName(param.name, false, false);
                paramType = SequenceType.ITEM_STAR;
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            Name.createVariableInDefaultFunctionNamespace("inline-function`"),
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.type.sequenceType() != null) {
                    paramType = this.processSequenceType(param.type.sequenceType());
                } else {
                    paramType = SequenceType.ITEM_STAR;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        }

        Expression expr = null;
        if (ctx.functionBody().enclosedExpression().expr() != null) {
            expr = (Expression) this.visitExpr(ctx.functionBody().enclosedExpression().expr());
        } else {
            expr = new CommaExpression(createMetadataFromContext(ctx));
        }

        return new InlineFunctionExpression(
                null,
                fnParams,
                fnReturnType,
                expr,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitPredicate(XQueryParser.PredicateContext ctx) {
        return this.visitExpr(ctx.expr());
    }
    // endregion

    // region Flowr
    @Override
    public Node visitFlworExpr(XQueryParser.FlworExprContext ctx) {
        Clause clause;
        // check the start clause, for or let
        XQueryParser.InitialClauseContext initialClause = ctx.initialClause();
        if (initialClause.letClause() != null) {
            clause = (Clause) this.visitLetClause(initialClause.letClause());
        } else if (initialClause.forClause() != null) {
            clause = (Clause) this.visitForClause(initialClause.forClause());
        } else {
            // TODO outside of the scope for thesis
            throw new XMLUnsupportedException("windowClause not supported YET", createMetadataFromContext(ctx));
        }

        Clause previousFLWORClause = clause.getLastClause();

        for (ParseTree child : ctx.intermediateClause()) {
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

        Expression returnExpr = (Expression) this.visitExprSingle(ctx.returnClause().exprSingle());
        ReturnClause returnClause = new ReturnClause(
                returnExpr,
                generateMetadata(ctx.getStop())
        );
        previousFLWORClause.chainWith(returnClause);

        return new FlworExpression(
                returnClause,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitForClause(XQueryParser.ForClauseContext ctx) {
        ForClause clause = null;
        for (XQueryParser.ForBindingContext var : ctx.vars) {
            ForClause newClause = (ForClause) this.visitForBinding(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitForBinding(XQueryParser.ForBindingContext ctx) {
        // var_ref in JSONiq is complex and has $ and var_name while XQuery has var_name straight away
        SequenceType seq = null;
        boolean emptyFlag;
        Name var = ((VariableReferenceExpression) this.visitVarName(ctx.name)).getVariableName();
        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq.sequenceType());
        } else {
            seq = SequenceType.ITEM_STAR;
        }
        emptyFlag = (ctx.flag != null);
        Name atVar = null;
        if (ctx.at != null) {
            atVar = ((VariableReferenceExpression) this.visitVarName(ctx.at.pvar)).getVariableName();
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
        SequenceType expressionType = new SequenceType(
                seq.getItemType(),
                SequenceType.Arity.ZeroOrMore
        );
        if (!expressionType.equals(SequenceType.ITEM_STAR)) {
            expr = new TreatExpression(expr, expressionType, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }


        return new ForClause(var, emptyFlag, seq, atVar, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitLetClause(XQueryParser.LetClauseContext ctx) {
        LetClause clause = null;
        for (XQueryParser.LetBindingContext var : ctx.vars) {
            LetClause newClause = (LetClause) this.visitLetBinding(var);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }

        return clause;
    }

    @Override
    public Node visitLetBinding(XQueryParser.LetBindingContext ctx) {
        SequenceType seq = null;
        Name var = ((VariableReferenceExpression) this.visitVarName(ctx.varName())).getVariableName();
        if (ctx.typeDeclaration() != null) {
            seq = this.processSequenceType(ctx.typeDeclaration().sequenceType());
        } else {
            seq = SequenceType.ITEM_STAR;
        }

        Expression expr = (Expression) this.visitExprSingle(ctx.exprSingle());
        if (!seq.equals(SequenceType.ITEM_STAR)) {
            expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }

        return new LetClause(var, seq, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitWhereClause(XQueryParser.WhereClauseContext ctx) {
        Expression expr = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new WhereClause(expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitGroupByClause(XQueryParser.GroupByClauseContext ctx) {
        List<GroupByVariableDeclaration> vars = new ArrayList<>();
        GroupByVariableDeclaration child;

        for (XQueryParser.GroupingSpecContext var : ctx.groupingSpecList().vars) {
            child = this.processGroupByVar(var);
            vars.add(child);
        }
        return new GroupByClause(vars, createMetadataFromContext(ctx));
    }

    public GroupByVariableDeclaration processGroupByVar(XQueryParser.GroupingSpecContext ctx) {
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
        Name var = ((VariableReferenceExpression) this.visitVarName(ctx.name)).getVariableName();

        if (ctx.typeDeclaration() != null) {
            seq = this.processSequenceType(ctx.typeDeclaration().sequenceType());
        } else {
            seq = SequenceType.ITEM_STAR;
        }

        if (ctx.exprSingle() != null) {
            expr = (Expression) this.visitExprSingle(ctx.exprSingle());
            if (!seq.equals(SequenceType.ITEM_STAR)) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }

        }


        return new GroupByVariableDeclaration(var, seq, expr);
    }

    private String processURILiteral(XQueryParser.UriLiteralContext ctx) {
        return ctx.getText().substring(1, ctx.getText().length() - 1);
    }

    @Override
    public Node visitOrderByClause(XQueryParser.OrderByClauseContext ctx) {
        boolean stable = false;
        List<OrderByClauseSortingKey> exprs = new ArrayList<>();
        OrderByClauseSortingKey child;
        for (XQueryParser.OrderSpecContext var : ctx.specs) {
            child = this.processOrderByExpr(var);
            exprs.add(child);
        }
        if (ctx.KW_STABLE() != null && !ctx.KW_STABLE().getText().isEmpty()) {
            stable = true;
        }
        return new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
    }

    public OrderByClauseSortingKey processOrderByExpr(XQueryParser.OrderSpecContext ctx) {
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

    @Override
    public Node visitCountClause(XQueryParser.CountClauseContext ctx) {
        VariableReferenceExpression child = (VariableReferenceExpression) this.visitVarName(ctx.varName());
        return new CountClause(child.getVariableName(), createMetadataFromContext(ctx));
    }

    // endregion

    // region If
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

    // endregion

    // region Quantified

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
        for (XQueryParser.QuantifiedVarContext currentVariable : ctx.vars) {
            Expression varExpression;
            SequenceType sequenceType = null;
            Name variableName = ((VariableReferenceExpression) this.visitVarName(
                currentVariable.varName()
            )).getVariableName();
            if (currentVariable.typeDeclaration() != null) {
                sequenceType = this.processSequenceType(currentVariable.typeDeclaration().sequenceType());
            } else {
                sequenceType = SequenceType.ITEM_STAR;
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

    // endregion

    // region Switch

    @Override
    public Node visitSwitchExpr(XQueryParser.SwitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<SwitchCase> cases = new ArrayList<>();
        for (XQueryParser.SwitchCaseClauseContext expr : ctx.cases) {
            List<Expression> conditionExpressions = new ArrayList<>();
            for (int i = 0; i < expr.cond.size(); ++i) {
                conditionExpressions.add((Expression) this.visitExprSingle(expr.cond.get(i).exprSingle()));
            }
            SwitchCase c = new SwitchCase(conditionExpressions, (Expression) this.visitExprSingle(expr.ret));
            cases.add(c);
        }
        Expression defaultCase = (Expression) this.visitExprSingle(ctx.def);
        return new SwitchExpression(condition, cases, defaultCase, createMetadataFromContext(ctx));
    }

    // endregion

    // region TypeSwitch

    @Override
    public Node visitTypeswitchExpr(XQueryParser.TypeswitchExprContext ctx) {
        Expression condition = (Expression) this.visitExpr(ctx.cond);
        List<TypeswitchCase> cases = new ArrayList<>();
        for (XQueryParser.CaseClauseContext expr : ctx.cses) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (expr.var_ref != null) {
                variableName = ((VariableReferenceExpression) this.visitVarName(
                    expr.var_ref
                )).getVariableName();
            }
            if (expr.union != null && !expr.union.isEmpty()) {
                for (XQueryParser.SequenceTypeContext sequenceType : expr.union.seq) {
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
            defaultVariableName = ((VariableReferenceExpression) this.visitVarName(
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
    // endregion

    // region TryCatch

    @Override
    public Node visitTryCatchExpr(XQueryParser.TryCatchExprContext ctx) {
        Expression tryExpression = (Expression) this.visitExpr(
            ctx.tryClause()
                .enclosedTryTargetExpression()
                .enclosedExpression()
                .expr()
        );
        Map<String, Expression> catchExpressions = new HashMap<>();
        Expression catchAllExpression = null;
        for (XQueryParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = (Expression) this.visitExpr(catchCtx.enclosedExpression().expr());
            boolean doesCatchAll = false;
            for (XQueryParser.NameTestContext qnameCtx : catchCtx.catchErrorList().errors) {
                if (qnameCtx.wildcard() != null) {
                    doesCatchAll = true;
                } else {
                    Name name = parseName(qnameCtx.eqName(), false, false);
                    if (!catchExpressions.containsKey(name.getLocalName())) {
                        catchExpressions.put(name.getLocalName(), catchExpression);
                    }
                }
            }
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
}
