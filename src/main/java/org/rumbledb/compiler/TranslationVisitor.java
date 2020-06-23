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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.DuplicateModuleTargetNamespaceException;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.exceptions.EmptyModuleURIException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.JsoniqVersionException;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.exceptions.NamespacePrefixBoundTwiceException;
import org.rumbledb.exceptions.OurBadException;
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
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
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
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.postfix.PredicateExpression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.parser.JsoniqParser.FunctionCallContext;
import org.rumbledb.parser.JsoniqParser.UriLiteralContext;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.compiler.ValueTypeHandler;

import static org.rumbledb.types.SequenceType.MOST_GENERAL_SEQUENCE_TYPE;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 *
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 */
public class TranslationVisitor extends org.rumbledb.parser.JsoniqBaseVisitor<Node> {

    private StaticContext moduleContext;
    private RumbleRuntimeConfiguration configuration;

    public TranslationVisitor(URI staticBaseURI, RumbleRuntimeConfiguration configuration) {
        this.moduleContext = new StaticContext(staticBaseURI);
        this.moduleContext.bindNamespace("local", Name.LOCAL_NS);
        this.configuration = configuration;
    }

    // endregion expr

    // region module
    @Override
    public Node visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty() && !ctx.vers.getText().trim().equals("1.0")) {
            throw new JsoniqVersionException(createMetadataFromContext(ctx));
        }
        if (ctx.mainModule() != null) {
            return this.visitMainModule(ctx.mainModule());
        }
        if (ctx.libraryModule() != null) {
            return this.visitLibraryModule(ctx.libraryModule());
        }
        throw new OurBadException("No main or library module found.");
    }

    @Override
    public Node visitMainModule(JsoniqParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        Expression commaExpression = (Expression) this.visitExpr(ctx.expr());
        MainModule module = new MainModule(prolog, commaExpression, createMetadataFromContext(ctx));
        module.setStaticContext(this.moduleContext);
        return module;
    }

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
        for (JsoniqParser.AnnotatedDeclContext annotatedDeclaration : ctx.annotatedDecl()) {
            if (annotatedDeclaration.varDecl() != null) {
                VariableDeclaration variableDeclaration = (VariableDeclaration) this.visitVarDecl(
                    annotatedDeclaration.varDecl()
                );
                globalVariables.add(variableDeclaration);
            } else if (annotatedDeclaration.functionDecl() != null) {
                InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) this.visitFunctionDecl(
                    annotatedDeclaration.functionDecl()
                );
                functionDeclarations.add(
                    new FunctionDeclaration(inlineFunctionExpression, createMetadataFromContext(ctx))
                );
            }
        }
        for (JsoniqParser.ModuleImportContext module : ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        Prolog prolog = new Prolog(globalVariables, functionDeclarations, createMetadataFromContext(ctx));
        for (LibraryModule libraryModule : libraryModules) {
            prolog.addImportedModule(libraryModule);
        }
        return prolog;
    }

    public Name parseName(JsoniqParser.QnameContext ctx, boolean isFunction) {
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
                name = Name.createVariableInRumbleNamespace(localName);
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
        Name name = parseName(ctx.qname(), true);
        SequenceType fnReturnType = MOST_GENERAL_SEQUENCE_TYPE;
        Name paramName;
        SequenceType paramType;
        Map<Name, SequenceType> fnParams = null;
        if (ctx.paramList() != null) {
            fnParams = new LinkedHashMap<>(ctx.paramList().param().size(), 1);
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false);
                paramType = MOST_GENERAL_SEQUENCE_TYPE;
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
                    paramType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
                }
                fnParams.put(paramName, paramType);
            }
        } else {
            fnParams = new LinkedHashMap<>(1, 1);
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        } else {
            fnReturnType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }

        Expression bodyExpression = (Expression) this.visitExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                name,
                fnParams,
                fnReturnType,
                bodyExpression,
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
        if (content instanceof JsoniqParser.OrExprContext) {
            return this.visitOrExpr((JsoniqParser.OrExprContext) content);
        }
        if (content instanceof JsoniqParser.FlowrExprContext) {
            return this.visitFlowrExpr((JsoniqParser.FlowrExprContext) content);
        }
        if (content instanceof JsoniqParser.IfExprContext) {
            return this.visitIfExpr((JsoniqParser.IfExprContext) content);
        }
        if (content instanceof JsoniqParser.QuantifiedExprContext) {
            return this.visitQuantifiedExpr((JsoniqParser.QuantifiedExprContext) content);
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
        throw new OurBadException("Unrecognized ExprSingle.");
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
        } else {
            seq = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }
        emptyFlag = (ctx.flag != null);
        Name atVar = null;
        if (ctx.at != null) {
            atVar = ((VariableReferenceExpression) this.visitVarRef(ctx.at)).getVariableName();
        }
        Expression expr = (Expression) this.visitExprSingle(ctx.ex);
        // If the sequenceType is specified, we have to "extend" its arity to *
        // because TreatIterator is wrapping the whole assignment expression,
        // meaning there is not one TreatIterator for each variable we loop over.
        SequenceType expressionType = new SequenceType(
                seq.getItemType(),
                SequenceType.Arity.ZeroOrMore
        );
        if (!expressionType.equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
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
        } else {
            seq = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }

        Expression expr = (Expression) this.visitExprSingle(ctx.ex);
        if (!seq.equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
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
            empty_order = OrderByClauseSortingKey.EMPTY_ORDER.LAST;
        }
        if (ctx.ls != null && !ctx.ls.getText().isEmpty()) {
            empty_order = OrderByClauseSortingKey.EMPTY_ORDER.FIRST;
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
        SequenceType seq = null;
        Expression expr = null;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.var_ref)).getVariableName();

        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        } else {
            seq = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }

        if (ctx.ex != null) {
            expr = (Expression) this.visitExprSingle(ctx.ex);
            if (!seq.equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
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
        return new CountClause(child, createMetadataFromContext(ctx));
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
        return new ComparisonExpression(
                mainExpression,
                childExpression,
                ComparisonExpression.ComparisonOperator.fromSymbol(ctx.op.get(0).getText()),
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
        Expression mainExpression = (Expression) this.visitTreatExpr(ctx.main_expr);
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

        for (int i = 0; i < ctx.function_call_expr.size(); ++i) {
            FunctionCallContext functionCallContext = ctx.function_call_expr.get(i);
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext(functionCallContext.argumentList()));
            mainExpression = processFunctionCall(functionCallContext, children);
        }
        return mainExpression;

    }


    @Override
    public Node visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        Expression mainExpression = (Expression) this.visitSimpleMapExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        boolean negated = !ctx.op.isEmpty() && ctx.op.get(0).getText().contentEquals("-");
        return new UnaryExpression(
                mainExpression,
                negated,
                createMetadataFromContext(ctx)
        );
    }
    // endregion

    // region postfix
    @Override
    public Node visitPostFixExpr(JsoniqParser.PostFixExprContext ctx) {
        Expression mainExpression = (Expression) this.visitPrimaryExpr(ctx.main_expr);
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext) {
                Expression expr = (Expression) this.visitPredicate((JsoniqParser.PredicateContext) child);
                mainExpression = new PredicateExpression(
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
                    ValueTypeHandler.getStringValue(ctx.lt),
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
        if (ctx.tkw != null) {
            return new StringLiteralExpression(ctx.tkw.getText(), createMetadataFromContext(ctx));
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
                    ValueTypeHandler.getStringValue((JsoniqParser.StringLiteralContext) child),
                    createMetadataFromContext(ctx)
            );
        }
        if (child instanceof TerminalNode) {
            return ValueTypeHandler.getValueType(child.getText(), createMetadataFromContext(ctx));
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
        throw new UnsupportedFeatureException(
                "Primary expression not yet implemented",
                createMetadataFromContext(ctx)
        );
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
        Name name = parseName(ctx.qname(), false);
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
        ItemType itemType = ItemType.getItemTypeByName(ctx.item.getText());
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

        ItemType itemType = ItemType.getItemTypeByName(ctx.item.getText());
        if (ctx.question.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrZero
            );
        }
        return new SequenceType(itemType);
    }

    private Expression processFunctionCall(JsoniqParser.FunctionCallContext ctx, List<Expression> children) {
        Name name = parseName(ctx.fn_name, true);
        return new FunctionCallExpression(
                name,
                children,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        return processFunctionCall(ctx, getArgumentsFromArgumentListContext(ctx.argumentList()));
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
        Expression literal = ValueTypeHandler.getValueType(
            ctx.arity.getText(),
            createMetadataFromContext(ctx)
        );
        if (!(literal instanceof IntegerLiteralExpression)) {
            throw new RumbleException(
                    "Parser error: In a named function reference, arity must be an integer."
            );
        }

        Name name = parseName(ctx.fn_name, true);
        int arity = ((IntegerLiteralExpression) literal).getValue();
        return new NamedFunctionReferenceExpression(
                new FunctionIdentifier(name, arity),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        Map<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        Name paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = parseName(param.qname(), false);
                paramType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            Name.createVariableInRumbleNamespace("inline-function`"),
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.sequenceType() != null) {
                    paramType = this.processSequenceType(param.sequenceType());
                } else {
                    paramType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        }

        Expression expr = (Expression) this.visitExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                null,
                fnParams,
                fnReturnType,
                expr,
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
        List<QuantifiedExpressionVar> vars = new ArrayList<>();
        QuantifiedExpression.Quantification operator;
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        if (ctx.ev == null) {
            operator = QuantifiedExpression.Quantification.SOME;
        } else {
            operator = QuantifiedExpression.Quantification.EVERY;
        }
        for (JsoniqParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
            Expression varExpression;
            SequenceType sequenceType = null;
            Name variableName = ((VariableReferenceExpression) this.visitVarRef(
                currentVariable.varRef()
            )).getVariableName();
            if (currentVariable.sequenceType() != null) {
                sequenceType = this.processSequenceType(currentVariable.sequenceType());
            } else {
                sequenceType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
            }

            varExpression = (Expression) this.visitExprSingle(currentVariable.exprSingle());
            vars.add(
                new QuantifiedExpressionVar(
                        variableName,
                        varExpression,
                        sequenceType
                )
            );
        }
        return new QuantifiedExpression(operator, expression, vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitTryCatchExpr(JsoniqParser.TryCatchExprContext ctx) {
        Expression tryExpression = (Expression) this.visitExpr(ctx.try_expression);
        Map<String, Expression> catchExpressions = new HashMap<>();
        Expression catchAllExpression = null;
        for (JsoniqParser.CatchClauseContext catchCtx : ctx.catches) {
            Expression catchExpression = (Expression) this.visitExpr(catchCtx.catch_expression);
            for (JsoniqParser.QnameContext qnameCtx : catchCtx.errors) {
                Name name = parseName(qnameCtx, false);
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
        SequenceType seq = null;
        boolean external;
        Name var = ((VariableReferenceExpression) this.visitVarRef(ctx.varRef())).getVariableName();
        if (ctx.sequenceType() != null) {
            seq = this.processSequenceType(ctx.sequenceType());
        } else {
            seq = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }
        external = (ctx.external != null);
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = (Expression) this.visitExprSingle(ctx.exprSingle());
            if (!seq.equals(SequenceType.MOST_GENERAL_SEQUENCE_TYPE)) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }

        return new VariableDeclaration(var, external, seq, expr, createMetadataFromContext(ctx));
    }

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
                token.getCharPositionInLine()
        );
    }

}


