package org.rumbledb.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.module.*;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.parser.XQueryParser;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XQueryTranslationVisitor extends org.rumbledb.parser.XQueryParserBaseVisitor<Node>{
    private StaticContext moduleContext;
    private RumbleRuntimeConfiguration configuration;
    private boolean isMainModule;

    public XQueryTranslationVisitor(
            StaticContext moduleContext,
            boolean isMainModule,
            RumbleRuntimeConfiguration configuration
    ) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindNamespace("local", Name.LOCAL_NS);
        this.configuration = configuration;
        this.isMainModule = isMainModule;
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(XQueryParser.ModuleContext ctx) {
//        TODO version is not a String
//        XQueryParser.VersionDeclContext ver = ctx.versionDecl(0);
//        if (!(ver == null) && !ver.version.isEmpty() && !ver.version.getText().trim().equals("1.0")) {
//            throw new JsoniqVersionException(createMetadataFromContext(ctx));
//        }
        if (this.isMainModule) {
            if (ctx.mainModule() != null) {
                return this.visitMainModule(ctx.mainModule(0)); // had to add 0
            }
            throw new ParsingException(
                    "Main module expected, but library module found.",
                    createMetadataFromContext(ctx)
            );
        }
        //        TODO
        else return null;
//        else {
//            if (ctx.libraryModule() != null) {
//                return this.visitLibraryModule(ctx.libraryModule());
//            }
//            throw new ParsingException(
//                    "Library module expected, but main module found.",
//                    createMetadataFromContext(ctx)
//            );
//        }
    }

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        return generateMetadata(ctx.getStart());
    }

    public ExceptionMetadata generateMetadata(Token token) {
        return new ExceptionMetadata(
                this.moduleContext.getStaticBaseURI().toString(),
                token.getLine(),
                token.getCharPositionInLine()
        );
    }

    @Override
    public Node visitMainModule(XQueryParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        Expression commaExpression = (Expression) this.visitExpr(ctx.queryBody().expr()); // Had to access query body
        MainModule module = new MainModule(prolog, commaExpression, createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

//TODO
//    @Override
//    public Node visitLibraryModule(JsoniqParser.LibraryModuleContext ctx) {
//        String prefix = ctx.NCName().getText();
//        String namespace = processURILiteral(ctx.uriLiteral());
//        if (namespace.equals("")) {
//            throw new EmptyModuleURIException("Module URI is empty.", createMetadataFromContext(ctx));
//        }
//        URI resolvedURI = FileSystemUtil.resolveURI(
//                this.moduleContext.getStaticBaseURI(),
//                namespace,
//                generateMetadata(ctx.getStop())
//        );
//        bindNamespace(
//                prefix,
//                resolvedURI.toString(),
//                generateMetadata(ctx.getStop())
//        );
//
//        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
//        LibraryModule module = new LibraryModule(prolog, resolvedURI.toString(), createMetadataFromContext(ctx));
//        module.setStaticContext(this.moduleContext);
//        return module;
//    }

    // TODO
//    @Override
//    public Node visitProlog(XQueryParser.PrologContext ctx) {
//        // bind namespaces
//        for (XQueryParser.NamespaceDeclContext namespace : ctx.namespaceDecl()) {
//            this.processNamespaceDecl(namespace);
//        }
//        List<XQueryParser.SetterContext> setters = ctx.setter();
//        boolean emptyOrderSet = false;
//        boolean defaultCollationSet = false;
//        for (XQueryParser.SetterContext setterContext : setters) {
//            if (setterContext.emptyOrderDecl() != null) {
//                if (emptyOrderSet) {
//                    throw new MoreThanOneEmptyOrderDeclarationException(
//                            "The empty order was already set.",
//                            createMetadataFromContext(setterContext.emptyOrderDecl())
//                    );
//                }
//                processEmptySequenceOrder(setterContext.emptyOrderDecl());
//                emptyOrderSet = true;
//                continue;
//            }
//            if (setterContext.defaultCollationDecl() != null) {
//                if (defaultCollationSet) {
//                    throw new DefaultCollationException(
//                            "The default collation was already set.",
//                            createMetadataFromContext(setterContext.defaultCollationDecl())
//                    );
//                }
//                processDefaultCollation(setterContext.defaultCollationDecl());
//                defaultCollationSet = true;
//                continue;
//            }
//            throw new UnsupportedFeatureException(
//                    "Setters are not supported yet, except for empty sequence ordering and default collations.",
//                    createMetadataFromContext(setterContext)
//            );
//        }
//        List<LibraryModule> libraryModules = new ArrayList<>();
//        Set<String> namespaces = new HashSet<>();
//        for (XQueryParser.ModuleImportContext namespace : ctx.moduleImport()) {
//            LibraryModule libraryModule = this.processModuleImport(namespace);
//            libraryModules.add(libraryModule);
//            if (namespaces.contains(libraryModule.getNamespace())) {
//                throw new DuplicateModuleTargetNamespaceException(
//                        "Duplicate module target namespace: " + libraryModule.getNamespace(),
//                        createMetadataFromContext(namespace)
//                );
//            }
//            namespaces.add(libraryModule.getNamespace());
//        }
//
//        // parse variables and function
//        List<VariableDeclaration> globalVariables = new ArrayList<>();
//        List<FunctionDeclaration> functionDeclarations = new ArrayList<>();
//        for (XQueryParser.AnnotatedDeclContext annotatedDeclaration : ctx.annotatedDecl()) {
//            if (annotatedDeclaration.varDecl() != null) {
//                VariableDeclaration variableDeclaration = (VariableDeclaration) this.visitVarDecl(
//                        annotatedDeclaration.varDecl()
//                );
//                if (!this.isMainModule) {
//                    String moduleNamespace = this.moduleContext.getStaticBaseURI().toString();
//                    String variableNamespace = variableDeclaration.getVariableName().getNamespace();
//                    if (variableNamespace == null || !variableNamespace.equals(moduleNamespace)) {
//                        throw new NamespaceDoesNotMatchModuleException(
//                                "Variable "
//                                        + variableDeclaration.getVariableName().getLocalName()
//                                        + ": namespace "
//                                        + variableNamespace
//                                        + " must match module namespace "
//                                        + moduleNamespace,
//                                generateMetadata(annotatedDeclaration.getStop())
//                        );
//                    }
//                }
//                globalVariables.add(variableDeclaration);
//            } else if (annotatedDeclaration.functionDecl() != null) {
//                InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) this.visitFunctionDecl(
//                        annotatedDeclaration.functionDecl()
//                );
//                if (!this.isMainModule) {
//                    String moduleNamespace = this.moduleContext.getStaticBaseURI().toString();
//                    String functionNamespace = inlineFunctionExpression.getName().getNamespace();
//                    if (functionNamespace == null || !functionNamespace.equals(moduleNamespace)) {
//                        throw new NamespaceDoesNotMatchModuleException(
//                                "Function "
//                                        + inlineFunctionExpression.getName().getLocalName()
//                                        + ": namespace "
//                                        + functionNamespace
//                                        + " must match module namespace "
//                                        + moduleNamespace,
//                                generateMetadata(annotatedDeclaration.getStop())
//                        );
//                    }
//                }
//                functionDeclarations.add(
//                        new FunctionDeclaration(inlineFunctionExpression, createMetadataFromContext(ctx))
//                );
//            }
//        }
//        for (XQueryParser.ModuleImportContext module : ctx.moduleImport()) {
//            this.visitModuleImport(module);
//        }
//
//        Prolog prolog = new Prolog(globalVariables, functionDeclarations, createMetadataFromContext(ctx));
//        for (LibraryModule libraryModule : libraryModules) {
//            prolog.addImportedModule(libraryModule);
//        }
//        return prolog;


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
//        if (content instanceof XQueryParser.FlowrExprContext) {
//            return this.visitFlowrExpr((XQueryParser.FlowrExprContext) content);
//        }
//        if (content instanceof XQueryParser.IfExprContext) {
//            return this.visitIfExpr((XQueryParser.IfExprContext) content);
//        }
//        if (content instanceof XQueryParser.QuantifiedExprContext) {
//            return this.visitQuantifiedExpr((XQueryParser.QuantifiedExprContext) content);
//        }
//        if (content instanceof XQueryParser.SwitchExprContext) {
//            return this.visitSwitchExpr((XQueryParser.SwitchExprContext) content);
//        }
//        if (content instanceof XQueryParser.TypeSwitchExprContext) {
//            return this.visitTypeSwitchExpr((XQueryParser.TypeSwitchExprContext) content);
//        }
//        if (content instanceof XQueryParser.TryCatchExprContext) {
//            return this.visitTryCatchExpr((XQueryParser.TryCatchExprContext) content);
//        }
        throw new OurBadException("Unrecognized ExprSingle.");
    }
    // endregion

    // region operational
//    @Override
//    public Node visitOrExpr(XQueryParser.OrExprContext ctx) {
//        Expression result = (Expression) this.visitAndExpr(ctx.andExpr(0)); // Had to access first
//        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
//            return result;
//        }
//        for (XQueryParser.AndExprContext child : ctx.rhs) {
//            Expression rightExpression = (Expression) this.visitAndExpr(child);
//            result = new OrExpression(result, rightExpression, createMetadataFromContext(ctx));
//        }
//        return result;
//    }
    // endregion
}
