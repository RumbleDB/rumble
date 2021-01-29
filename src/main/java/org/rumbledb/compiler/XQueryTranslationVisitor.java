package org.rumbledb.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.*;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.parser.XQueryParser;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

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
        // TODO figure out the nodeComp and put it in else if. We do not have from symbol for it!
        // is, <<, >>
        if (ctx.nodeComp() != null)
            return null;
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

    // TODO Insert unionExpr and intersectExceptExpr

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
        ItemType itemType = AtomicItemType.getItemTypeByName(ctx.item.getText());
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

        ItemType itemType = AtomicItemType.getItemTypeByName(ctx.item.getText());
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

//    @Override
//    public Node visitArrowExpr(XQueryParser.ArrowExprContext ctx) {
//        Expression mainExpression = (Expression) this.visitUnaryExpr(ctx.main_expr);
//
//        for (int i = 0; i < ctx.function_call_expr.size(); ++i) {
//            XQueryParser.FunctionCallContext functionCallContext = ctx.function_call_expr.get(i);
//            List<Expression> children = new ArrayList<Expression>();
//            children.add(mainExpression);
//            children.addAll(getArgumentsFromArgumentListContext(functionCallContext.argumentList()));
//            mainExpression = processFunctionCall(functionCallContext, children);
//        }
//        return mainExpression;
//
//    }

    // endregion
}
