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
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.JsoniqVersionException;
import org.rumbledb.exceptions.ModuleDeclarationException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.expressions.flowr.FlworVarSingleType;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ForClauseVar;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByClauseVar;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.LetClauseVar;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseExpr;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.operational.AdditiveExpression;
import org.rumbledb.expressions.operational.AndExpression;
import org.rumbledb.expressions.operational.CastExpression;
import org.rumbledb.expressions.operational.CastableExpression;
import org.rumbledb.expressions.operational.ComparisonExpression;
import org.rumbledb.expressions.operational.InstanceOfExpression;
import org.rumbledb.expressions.operational.MultiplicativeExpression;
import org.rumbledb.expressions.operational.NotExpression;
import org.rumbledb.expressions.operational.OrExpression;
import org.rumbledb.expressions.operational.RangeExpression;
import org.rumbledb.expressions.operational.StringConcatExpression;
import org.rumbledb.expressions.operational.TreatExpression;
import org.rumbledb.expressions.operational.UnaryExpression;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
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
import org.rumbledb.expressions.primary.PrimaryExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;

import sparksoniq.jsoniq.compiler.ValueTypeHandler;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static sparksoniq.semantics.types.SequenceType.mostGeneralSequenceType;


/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 * 
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 *
 */
public class TranslationVisitor extends org.rumbledb.parser.JsoniqBaseVisitor<Void> {

    private MainModule mainModule;
    private Expression currentExpression;
    private Clause currentClause;


    public TranslationVisitor() {
    }
    // endregion expr

    // region module
    public MainModule getMainModule() {
        return this.mainModule;
    }

    @Override
    public Void visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty() && !ctx.vers.getText().trim().equals("1.0"))
            throw new JsoniqVersionException(createMetadataFromContext(ctx));
        this.visitMainModule(ctx.mainModule());
        return null;
    }

    @Override
    public Void visitMainModule(JsoniqParser.MainModuleContext ctx) {
        this.visitProlog(ctx.prolog());
        Prolog prolog = (Prolog) this.currentExpression;
        this.visitExpr(ctx.expr());
        Expression commaExpression = this.currentExpression;
        this.mainModule = new MainModule(prolog, commaExpression, createMetadataFromContext(ctx));
        this.currentExpression = this.mainModule;
        return null;
    }

    @Override
    public Void visitProlog(JsoniqParser.PrologContext ctx) {
        List<InlineFunctionExpression> InlineFunctionExpressions = new ArrayList<>();
        for (JsoniqParser.FunctionDeclContext function : ctx.functionDecl()) {
            this.visitFunctionDecl(function);
            InlineFunctionExpressions.add((InlineFunctionExpression) this.currentExpression);
        }
        for (JsoniqParser.ModuleImportContext module : ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        this.currentExpression = new Prolog(InlineFunctionExpressions, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitModuleImport(JsoniqParser.ModuleImportContext ctx) {
        throw new ModuleDeclarationException("Modules are not supported in Sparksoniq", createMetadataFromContext(ctx));
    }

    @Override
    public Void visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
        String fnName = ctx.fn_name.getText();
        Map<String, FlworVarSequenceType> fnParams = new LinkedHashMap<>();
        FlworVarSequenceType fnReturnType = new FlworVarSequenceType(
                mostGeneralSequenceType,
                createMetadataFromContext(ctx)
        );
        String paramName;
        FlworVarSequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = param.NCName().getText();
                paramType = new FlworVarSequenceType(
                        mostGeneralSequenceType,
                        createMetadataFromContext(ctx)
                );
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            fnName,
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.sequenceType() != null) {
                    this.visitSequenceType(param.sequenceType());
                    paramType = (FlworVarSequenceType) this.currentExpression;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            this.visitSequenceType(ctx.return_type);
            fnReturnType = (FlworVarSequenceType) this.currentExpression;
        }

        this.visitExpr(ctx.fn_body);

        this.currentExpression = new InlineFunctionExpression(
                fnName,
                fnParams,
                fnReturnType,
                this.currentExpression,
                createMetadataFromContext(ctx)
        );
        return null;
    }
    // endregion

    // region expr
    @Override
    public Void visitExpr(JsoniqParser.ExprContext ctx) {
        List<Expression> expressions = new ArrayList<>();
        Expression expression;
        for (JsoniqParser.ExprSingleContext expr : ctx.exprSingle()) {
            this.visitExprSingle(expr);
            expression = this.currentExpression;
            expressions.add(expression);

        }
        this.currentExpression = new CommaExpression(expressions, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitExprSingle(JsoniqParser.ExprSingleContext ctx) {
        ParseTree content = ctx.children.get(0);
        if (content instanceof JsoniqParser.OrExprContext)
            this.visitOrExpr((JsoniqParser.OrExprContext) content);
        else if (content instanceof JsoniqParser.FlowrExprContext)
            this.visitFlowrExpr((JsoniqParser.FlowrExprContext) content);
        else if (content instanceof JsoniqParser.IfExprContext)
            this.visitIfExpr((JsoniqParser.IfExprContext) content);
        else if (content instanceof JsoniqParser.QuantifiedExprContext)
            this.visitQuantifiedExpr((JsoniqParser.QuantifiedExprContext) content);
        else if (content instanceof JsoniqParser.SwitchExprContext)
            this.visitSwitchExpr((JsoniqParser.SwitchExprContext) content);
        else if (content instanceof JsoniqParser.TypeSwitchExprContext)
            this.visitTypeSwitchExpr((JsoniqParser.TypeSwitchExprContext) content);
        return null;
    }
    // endregion

    // region Flowr
    // TODO [EXPRVISITOR] count
    @Override
    public Void visitFlowrExpr(JsoniqParser.FlowrExprContext ctx) {
        Clause startClause;
        Clause childClause;
        List<Clause> contentClauses = new ArrayList<>();
        ReturnClause returnClause;
        // check the start clause, for or let
        if (ctx.start_for == null) {
            this.visitLetClause(ctx.start_let);
            startClause = this.currentClause;
        } else {
            this.visitForClause(ctx.start_for);
            startClause = this.currentClause;
        }

        // exclude return + returnExpr
        Clause previousFLWORClause = startClause;
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext) {
                this.visitForClause((JsoniqParser.ForClauseContext) child);
                childClause = this.currentClause;
            } else if (child instanceof JsoniqParser.LetClauseContext) {
                this.visitLetClause((JsoniqParser.LetClauseContext) child);
                childClause = this.currentClause;
            } else if (child instanceof JsoniqParser.WhereClauseContext) {
                this.visitWhereClause((JsoniqParser.WhereClauseContext) child);
                childClause = this.currentClause;
            } else if (child instanceof JsoniqParser.GroupByClauseContext) {
                this.visitGroupByClause((JsoniqParser.GroupByClauseContext) child);
                childClause = this.currentClause;
            } else if (child instanceof JsoniqParser.OrderByClauseContext) {
                this.visitOrderByClause((JsoniqParser.OrderByClauseContext) child);
                childClause = this.currentClause;
            } else if (child instanceof JsoniqParser.CountClauseContext) {
                this.visitCountClause((JsoniqParser.CountClauseContext) child);
                childClause = this.currentClause;
            } else
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet",
                        createMetadataFromContext(ctx)
                );
            childClause.setPreviousClause(previousFLWORClause);
            previousFLWORClause = childClause;
            contentClauses.add(childClause);
        }

        // visit return
        this.visitExprSingle(ctx.return_expr);
        Expression returnExpr = this.currentExpression;
        returnClause = new ReturnClause(
                returnExpr,
                new ExceptionMetadata(
                        ctx.getStop().getLine(),
                        ctx.getStop().getCharPositionInLine()
                )
        );
        returnClause.setPreviousClause(previousFLWORClause);

        this.currentExpression = new FlworExpression(
                startClause,
                contentClauses,
                returnClause,
                createMetadataFromContext(ctx)
        );
        return null;
    }

    @Override
    public Void visitForClause(JsoniqParser.ForClauseContext ctx) {
        List<ForClauseVar> vars = new ArrayList<>();
        ForClauseVar child;
        for (JsoniqParser.ForVarContext var : ctx.vars) {
            this.visitForVar(var);
            child = (ForClauseVar) this.currentClause;
            vars.add(child);
        }

        this.currentClause = new ForClause(vars, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitForVar(JsoniqParser.ForVarContext ctx) {
        VariableReferenceExpression var, atVarRef = null;
        FlworVarSequenceType seq = null;
        Expression expr;
        boolean emptyFlag;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReferenceExpression) this.currentExpression;
        if (ctx.seq != null) {
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType) this.currentExpression;
        }
        emptyFlag = (ctx.flag != null);
        if (ctx.at != null) {
            this.visitVarRef(ctx.at);
            atVarRef = (VariableReferenceExpression) this.currentExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        this.currentClause = new ForClauseVar(var, seq, emptyFlag, atVarRef, expr, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitLetClause(JsoniqParser.LetClauseContext ctx) {
        List<LetClauseVar> vars = new ArrayList<>();
        LetClauseVar child;
        for (JsoniqParser.LetVarContext var : ctx.vars) {
            this.visitLetVar(var);
            child = (LetClauseVar) this.currentClause;
            vars.add(child);
        }

        this.currentClause = new LetClause(vars, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitLetVar(JsoniqParser.LetVarContext ctx) {
        VariableReferenceExpression var;
        FlworVarSequenceType seq = null;
        Expression expr;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReferenceExpression) this.currentExpression;
        if (ctx.seq != null) {
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType) this.currentExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        this.currentClause = new LetClauseVar(var, seq, expr, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        List<GroupByClauseVar> vars = new ArrayList<>();
        GroupByClauseVar child;
        for (JsoniqParser.GroupByVarContext var : ctx.vars) {
            this.visitGroupByVar(var);
            child = (GroupByClauseVar) this.currentClause;
            vars.add(child);
        }
        this.currentClause = new GroupByClause(vars, createMetadataFromContext(ctx));
        return null;

    }

    @Override
    public Void visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        boolean stable = false;
        List<OrderByClauseExpr> exprs = new ArrayList<>();
        OrderByClauseExpr child;
        for (JsoniqParser.OrderByExprContext var : ctx.orderByExpr()) {
            this.visitOrderByExpr(var);
            child = (OrderByClauseExpr) this.currentClause;
            exprs.add(child);
        }
        if (ctx.stb != null && !ctx.stb.getText().isEmpty())
            stable = true;
        this.currentClause = new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
        return null;

    }

    @Override
    public Void visitOrderByExpr(JsoniqParser.OrderByExprContext ctx) {
        boolean ascending = true;
        if (ctx.desc != null && !ctx.desc.getText().isEmpty())
            ascending = false;
        String uri = null;
        if (ctx.uriLiteral() != null)
            uri = ctx.uriLiteral().getText();
        OrderByClauseExpr.EMPTY_ORDER empty_order = OrderByClauseExpr.EMPTY_ORDER.NONE;
        if (ctx.gr != null && !ctx.gr.getText().isEmpty())
            empty_order = OrderByClauseExpr.EMPTY_ORDER.LAST;
        if (ctx.ls != null && !ctx.ls.getText().isEmpty())
            empty_order = OrderByClauseExpr.EMPTY_ORDER.FIRST;
        this.visitExprSingle(ctx.exprSingle());
        Expression expression = this.currentExpression;
        this.currentClause = new OrderByClauseExpr(
                expression,
                ascending,
                uri,
                empty_order,
                createMetadataFromContext(ctx)
        );
        return null;
    }

    @Override
    public Void visitGroupByVar(JsoniqParser.GroupByVarContext ctx) {
        VariableReferenceExpression var;
        FlworVarSequenceType seq = null;
        Expression expr = null;
        String uri = null;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReferenceExpression) this.currentExpression;

        if (ctx.seq != null) {
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType) this.currentExpression;
        }

        if (ctx.ex != null) {
            this.visitExprSingle(ctx.ex);
            expr = this.currentExpression;
        }

        if (ctx.uri != null)
            uri = ctx.uri.getText();

        this.currentClause = new GroupByClauseVar(var, seq, expr, uri, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        Expression expr;
        this.visitExprSingle(ctx.exprSingle());
        expr = this.currentExpression;
        this.currentClause = new WhereClause(expr, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitCountClause(JsoniqParser.CountClauseContext ctx) {
        VariableReferenceExpression child;
        this.visitVarRef(ctx.varRef());
        child = (VariableReferenceExpression) this.currentExpression;
        this.currentClause = new CountClause(child, createMetadataFromContext(ctx));
        return null;
    }
    // endregion

    // region operational
    @Override
    public Void visitOrExpr(JsoniqParser.OrExprContext ctx) {
        Expression mainExpression;
        List<Expression> rhs = new ArrayList<>();
        this.visitAndExpr(ctx.main_expr);
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            for (JsoniqParser.AndExprContext child : ctx.rhs) {
                this.visitAndExpr(child);
                rhs.add(this.currentExpression);
            }
            this.currentExpression = new OrExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitAndExpr(JsoniqParser.AndExprContext ctx) {
        Expression mainExpression;
        List<Expression> rhs = new ArrayList<>();
        this.visitNotExpr(ctx.main_expr);
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            for (JsoniqParser.NotExprContext child : ctx.rhs) {
                this.visitNotExpr(child);
                rhs.add(this.currentExpression);
            }
            this.currentExpression = new AndExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitNotExpr(JsoniqParser.NotExprContext ctx) {
        Expression mainExpression;
        this.visitComparisonExpr(ctx.main_expr);
        if (!(ctx.op == null || ctx.op.isEmpty())) {
            mainExpression = this.currentExpression;
            this.currentExpression = new NotExpression(
                    mainExpression,
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        Expression mainExpression, childExpression;
        this.visitStringConcatExpr(ctx.main_expr);
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.StringConcatExprContext child = ctx.rhs.get(0);
            this.visitStringConcatExpr(child);
            childExpression = this.currentExpression;

            this.currentExpression = new ComparisonExpression(
                    mainExpression,
                    childExpression,
                    OperationalExpressionBase.getOperatorFromString(ctx.op.get(0).getText()),
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        Expression mainExpression;
        List<Expression> rhs = new ArrayList<>();
        this.visitRangeExpr(ctx.main_expr);
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            for (JsoniqParser.RangeExprContext child : ctx.rhs) {
                this.visitRangeExpr(child);
                rhs.add(this.currentExpression);
            }
            this.currentExpression = new StringConcatExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitRangeExpr(JsoniqParser.RangeExprContext ctx) {

        Expression mainExpression, childExpression;
        this.visitAdditiveExpr(ctx.main_expr);
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.AdditiveExprContext child = ctx.rhs.get(0);
            this.visitAdditiveExpr(child);
            childExpression = this.currentExpression;
            this.currentExpression = new RangeExpression(
                    mainExpression,
                    childExpression,
                    createMetadataFromContext(ctx)
            );
        }

        return null;
    }

    @Override
    public Void visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        Expression mainExpression, childExpression;
        List<Expression> rhs = new ArrayList<>();
        this.visitMultiplicativeExpr(ctx.main_expr);
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            for (JsoniqParser.MultiplicativeExprContext child : ctx.rhs) {
                this.visitMultiplicativeExpr(child);
                childExpression = this.currentExpression;
                rhs.add(childExpression);
            }
            this.currentExpression = new AdditiveExpression(
                    mainExpression,
                    rhs,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        Expression mainExpression;
        List<Expression> rhs = new ArrayList<>();
        this.visitInstanceOfExpr(ctx.main_expr);
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            mainExpression = this.currentExpression;
            for (JsoniqParser.InstanceOfExprContext child : ctx.rhs) {
                this.visitInstanceOfExpr(child);
                rhs.add(this.currentExpression);
            }
            this.currentExpression = new MultiplicativeExpression(
                    mainExpression,
                    rhs,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx) {
        Expression mainExpression;
        FlworVarSequenceType sequenceType;
        this.visitTreatExpr(ctx.main_expr);
        if (ctx.seq != null && !ctx.seq.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.SequenceTypeContext child = ctx.seq;
            this.visitSequenceType(child);
            sequenceType = (FlworVarSequenceType) this.currentExpression;
            this.currentExpression = new InstanceOfExpression(
                    mainExpression,
                    sequenceType,
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitTreatExpr(JsoniqParser.TreatExprContext ctx) {
        Expression mainExpression;
        FlworVarSequenceType sequenceType;
        this.visitCastableExpr(ctx.main_expr);
        if (ctx.seq != null && !ctx.seq.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.SequenceTypeContext child = ctx.seq;
            this.visitSequenceType(child);
            sequenceType = (FlworVarSequenceType) this.currentExpression;
            this.currentExpression = new TreatExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitCastableExpr(JsoniqParser.CastableExprContext ctx) {
        Expression mainExpression;
        FlworVarSingleType singleType;
        this.visitCastExpr(ctx.main_expr);
        if (ctx.single != null && !ctx.single.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.SingleTypeContext child = ctx.single;
            this.visitSingleType(child);
            singleType = (FlworVarSingleType) this.currentExpression;
            this.currentExpression = new CastableExpression(mainExpression, singleType, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitCastExpr(JsoniqParser.CastExprContext ctx) {
        Expression mainExpression;
        FlworVarSingleType singleType;
        this.visitUnaryExpr(ctx.main_expr);
        if (ctx.single != null && !ctx.single.isEmpty()) {
            mainExpression = this.currentExpression;
            JsoniqParser.SingleTypeContext child = ctx.single;
            this.visitSingleType(child);
            singleType = (FlworVarSingleType) this.currentExpression;
            this.currentExpression = new CastExpression(mainExpression, singleType, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        Expression mainExpression;
        this.visitSimpleMapExpr(ctx.main_expr);
        if (!(ctx.op == null || ctx.op.isEmpty())) {
            mainExpression = this.currentExpression;
            this.currentExpression = new UnaryExpression(
                    mainExpression,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }
    // endregion

    // region postfix
    @Override
    public Void visitPostFixExpr(JsoniqParser.PostFixExprContext ctx) {
        this.visitPrimaryExpr(ctx.main_expr);
        Expression mainExpression = this.currentExpression;
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext) {
                this.visitPredicate((JsoniqParser.PredicateContext) child);
                mainExpression = new PredicateExpression(
                        mainExpression,
                        this.currentExpression,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ObjectLookupContext) {
                this.visitObjectLookup((JsoniqParser.ObjectLookupContext) child);
                mainExpression = new ObjectLookupExpression(
                        mainExpression,
                        this.currentExpression,
                        createMetadataFromContext(ctx)
                );
            } else if (child instanceof JsoniqParser.ArrayLookupContext) {
                this.visitArrayLookup((JsoniqParser.ArrayLookupContext) child);
                mainExpression = new ArrayLookupExpression(
                        mainExpression,
                        this.currentExpression,
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
        this.currentExpression = mainExpression;
        return null;
    }

    @Override
    public Void visitPredicate(JsoniqParser.PredicateContext ctx) {
        this.visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Void visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        // TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        if (ctx.lt != null) {
            this.currentExpression = new StringLiteralExpression(
                    ValueTypeHandler.getStringValue(ctx.lt),
                    createMetadataFromContext(ctx)
            );
        } else if (ctx.nc != null) {
            this.currentExpression = new StringLiteralExpression(ctx.nc.getText(), createMetadataFromContext(ctx));
        } else if (ctx.kw != null) {
            this.currentExpression = new StringLiteralExpression(ctx.kw.getText(), createMetadataFromContext(ctx));
        } else if (ctx.pe != null) {
            this.visitParenthesizedExpr(ctx.pe);
        } else if (ctx.vr != null) {
            this.visitVarRef(ctx.vr);
        } else if (ctx.ci != null) {
            this.visitContextItemExpr(ctx.ci);
        } else if (ctx.tkw != null) {
            this.currentExpression = new StringLiteralExpression(ctx.tkw.getText(), createMetadataFromContext(ctx));
        }

        return null;
    }

    @Override
    public Void visitArrayLookup(JsoniqParser.ArrayLookupContext ctx) {
        this.visitExpr(ctx.expr());
        return null;
    }

    @Override
    public Void visitArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx) {
        return null;
    }
    // endregion

    // region primary
    // TODO [EXPRVISITOR] orderedExpr unorderedExpr;
    @Override
    public Void visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.VarRefContext) {
            this.visitVarRef((JsoniqParser.VarRefContext) child);
        } else if (child instanceof JsoniqParser.ObjectConstructorContext) {
            this.visitObjectConstructor((JsoniqParser.ObjectConstructorContext) child);
        } else if (child instanceof JsoniqParser.ArrayConstructorContext) {
            this.visitArrayConstructor((JsoniqParser.ArrayConstructorContext) child);
        } else if (child instanceof JsoniqParser.ParenthesizedExprContext) {
            this.visitParenthesizedExpr((JsoniqParser.ParenthesizedExprContext) child);
        } else if (child instanceof JsoniqParser.StringLiteralContext) {
            this.currentExpression = new StringLiteralExpression(
                    ValueTypeHandler.getStringValue((JsoniqParser.StringLiteralContext) child),
                    createMetadataFromContext(ctx)
            );
        } else if (child instanceof TerminalNode) {
            this.currentExpression = ValueTypeHandler.getValueType(child.getText(), createMetadataFromContext(ctx));
        } else if (child instanceof JsoniqParser.ContextItemExprContext) {
            this.visitContextItemExpr((JsoniqParser.ContextItemExprContext) child);
        } else if (child instanceof JsoniqParser.FunctionCallContext) {
            this.visitFunctionCall((JsoniqParser.FunctionCallContext) child);
        } else if (child instanceof JsoniqParser.FunctionItemExprContext) {
            this.visitFunctionItemExpr((JsoniqParser.FunctionItemExprContext) child);
        } else
            throw new UnsupportedFeatureException(
                    "Primary expression not yet implemented",
                    createMetadataFromContext(ctx)
            );

        return null;

    }

    @Override
    public Void visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
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
                    this.visitExprSingle(currentPair.lhs);
                    keys.add(this.currentExpression);
                } else {
                    keys.add(new StringLiteralExpression(currentPair.name.getText(), createMetadataFromContext(ctx)));
                }
                this.visitExprSingle(currentPair.rhs);
                values.add(this.currentExpression);
            }
            this.currentExpression = new ObjectConstructorExpression(keys, values, createMetadataFromContext(ctx));
        } else {
            Expression childExpr;
            this.visitExpr(ctx.expr());
            childExpr = this.currentExpression;
            this.currentExpression = new ObjectConstructorExpression(
                    childExpr,
                    createMetadataFromContext(ctx)
            );
        }

        return null;
    }

    @Override
    public Void visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        Expression content;
        if (ctx.expr() == null)
            this.currentExpression = new ArrayConstructorExpression(createMetadataFromContext(ctx));
        else {
            this.visitExpr(ctx.expr());
            content = this.currentExpression;
            this.currentExpression = new ArrayConstructorExpression(content, createMetadataFromContext(ctx));
        }
        return null;

    }

    @Override
    public Void visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx) {
        if (ctx.expr() == null)
            this.currentExpression = new CommaExpression(createMetadataFromContext(ctx));
        else {
            this.visitExpr(ctx.expr());
            // implicit: this.currentExpression = this.currentExpression;
        }
        return null;
    }

    @Override
    public Void visitVarRef(JsoniqParser.VarRefContext ctx) {
        String name = ctx.name.getText();
        if (ctx.ns != null)
            name = name + ":" + ctx.ns.getText();
        this.currentExpression = new VariableReferenceExpression(name, createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        this.currentExpression = new ContextItemExpression(createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitSequenceType(JsoniqParser.SequenceTypeContext ctx) {
        if (ctx.item == null)
            this.currentExpression = new FlworVarSequenceType(createMetadataFromContext(ctx));
        else {
            ItemTypes item = FlworVarSequenceType.getItemType(ctx.item.getText());
            if (ctx.question.size() > 0)
                this.currentExpression = new FlworVarSequenceType(
                        item,
                        SequenceType.Arity.OneOrZero,
                        createMetadataFromContext(ctx)
                );
            else if (ctx.star.size() > 0)
                this.currentExpression = new FlworVarSequenceType(
                        item,
                        SequenceType.Arity.ZeroOrMore,
                        createMetadataFromContext(ctx)
                );
            else if (ctx.plus.size() > 0)
                this.currentExpression = new FlworVarSequenceType(
                        item,
                        SequenceType.Arity.OneOrMore,
                        createMetadataFromContext(ctx)
                );
            else
                this.currentExpression = new FlworVarSequenceType(item, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitSingleType(JsoniqParser.SingleTypeContext ctx) {
        if (ctx.item == null)
            this.currentExpression = new FlworVarSingleType(createMetadataFromContext(ctx));
        else {
            AtomicTypes item = FlworVarSingleType.getAtomicType(ctx.item.getText());
            if (ctx.question.size() > 0)
                this.currentExpression = new FlworVarSingleType(item, true, createMetadataFromContext(ctx));
            else
                this.currentExpression = new FlworVarSingleType(item, createMetadataFromContext(ctx));
        }
        return null;
    }

    @Override
    public Void visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        String name;
        if (ctx.fn_name != null)
            name = ctx.fn_name.getText();
        else
            name = ctx.kw.getText();
        if (ctx.ns != null)
            name = name + ":" + ctx.ns.getText();
        this.currentExpression = new FunctionCallExpression(
                name,
                getArgumentsFromArgumentListContext(ctx.argumentList()),
                createMetadataFromContext(ctx)
        );
        return null;
    }

    private List<Expression> getArgumentsFromArgumentListContext(JsoniqParser.ArgumentListContext ctx) {
        List<Expression> arguments = new ArrayList<>();
        if (ctx.args != null) {
            for (JsoniqParser.ArgumentContext arg : ctx.args) {
                this.visitArgument(arg);
                Expression currentArg = this.currentExpression;
                arguments.add(currentArg);
            }
        }
        return arguments;
    }

    @Override
    public Void visitArgument(JsoniqParser.ArgumentContext ctx) {
        if (ctx.exprSingle() != null) {
            this.visitExprSingle(ctx.exprSingle());
        } else {
            this.currentExpression = null;
        }
        return null;
    }

    @Override
    public Void visitFunctionItemExpr(JsoniqParser.FunctionItemExprContext ctx) {
        ParseTree child = ctx.children.get(0);
        if (child instanceof JsoniqParser.NamedFunctionRefContext) {
            this.visitNamedFunctionRef((JsoniqParser.NamedFunctionRefContext) child);
        } else if (child instanceof JsoniqParser.InlineFunctionExprContext) {
            this.visitInlineFunctionExpr((JsoniqParser.InlineFunctionExprContext) child);
        } else {
            throw new UnsupportedFeatureException(
                    "Function item expression not yet implemented",
                    createMetadataFromContext(ctx)
            );
        }
        return null;
    }

    @Override
    public Void visitNamedFunctionRef(JsoniqParser.NamedFunctionRefContext ctx) {
        PrimaryExpression literal = ValueTypeHandler.getValueType(
            ctx.arity.getText(),
            createMetadataFromContext(ctx)
        );
        if (!(literal instanceof IntegerLiteralExpression)) {
            throw new SparksoniqRuntimeException(
                    "Parser error: In a named function reference, arity must be an integer."
            );
        }

        String name = ctx.fn_name.getText();
        int arity = ((IntegerLiteralExpression) literal).getValue();
        this.currentExpression = new NamedFunctionReferenceExpression(
                new FunctionIdentifier(name, arity),
                createMetadataFromContext(ctx)
        );
        return null;
    }

    @Override
    public Void visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        Map<String, FlworVarSequenceType> fnParams = new LinkedHashMap<>();
        FlworVarSequenceType fnReturnType = new FlworVarSequenceType(
                mostGeneralSequenceType,
                createMetadataFromContext(ctx)
        );
        String paramName;
        FlworVarSequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = param.NCName().getText();
                paramType = new FlworVarSequenceType(
                        mostGeneralSequenceType,
                        createMetadataFromContext(ctx)
                );
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            "inline-function`",
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.sequenceType() != null) {
                    this.visitSequenceType(param.sequenceType());
                    paramType = (FlworVarSequenceType) this.currentExpression;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            this.visitSequenceType(ctx.return_type);
            fnReturnType = (FlworVarSequenceType) this.currentExpression;
        }

        this.visitExpr(ctx.fn_body);

        this.currentExpression = new InlineFunctionExpression(
                "",
                fnParams,
                fnReturnType,
                this.currentExpression,
                createMetadataFromContext(ctx)
        );
        return null;
    }
    // endregion

    // region control
    @Override
    public Void visitIfExpr(JsoniqParser.IfExprContext ctx) {
        Expression condition, branch, else_branch;
        this.visitExpr(ctx.test_condition);
        condition = this.currentExpression;
        this.visitExprSingle(ctx.branch);
        branch = this.currentExpression;
        this.visitExprSingle(ctx.else_branch);
        else_branch = this.currentExpression;
        this.currentExpression = new ConditionalExpression(
                condition,
                branch,
                else_branch,
                createMetadataFromContext(ctx)
        );
        return null;
    }

    @Override
    public Void visitSwitchExpr(JsoniqParser.SwitchExprContext ctx) {
        Expression condition, defaultCase;
        this.visitExpr(ctx.cond);
        condition = this.currentExpression;
        List<SwitchCase> cases = new ArrayList<>();
        for (JsoniqParser.SwitchCaseClauseContext expr : ctx.cases) {
            List<Expression> conditionExpressions = new ArrayList<>();
            for (int i = 0; i < expr.cond.size(); ++i) {
                this.visitExprSingle(expr.cond.get(i));
                conditionExpressions.add(this.currentExpression);
            }
            this.visitExprSingle(expr.ret);
            SwitchCase c = new SwitchCase(conditionExpressions, this.currentExpression);
            cases.add(c);
        }
        this.visitExprSingle(ctx.def);
        defaultCase = this.currentExpression;
        this.currentExpression = new SwitchExpression(condition, cases, defaultCase, createMetadataFromContext(ctx));
        return null;
    }
    // endregion

    // region quantified
    @Override
    public Void visitTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx) {
        Expression condition, defaultCase;
        this.visitExpr(ctx.cond);
        condition = this.currentExpression;
        List<TypeswitchCase> cases = new ArrayList<>();
        for (JsoniqParser.CaseClauseContext expr : ctx.cses) {
            List<FlworVarSequenceType> union = new ArrayList<>();
            String variableName = null;
            if (expr.var_ref != null) {
                variableName = expr.var_ref.name.getText();
            }
            if (expr.union != null && !expr.union.isEmpty()) {
                for (JsoniqParser.SequenceTypeContext sequenceType : expr.union) {
                    this.visitSequenceType(sequenceType);
                    union.add((FlworVarSequenceType) this.currentExpression);
                }
            }
            this.visitExprSingle(expr.ret);
            cases.add(
                new TypeswitchCase(
                        variableName,
                        union,
                        this.currentExpression
                )
            );
        }
        String defaultVariableName = null;
        if (ctx.var_ref != null) {
            defaultVariableName = ctx.var_ref.name.getText();
        }
        this.visitExprSingle(ctx.def);
        defaultCase = this.currentExpression;
        this.currentExpression = new TypeSwitchExpression(
                condition,
                cases,
                defaultCase,
                defaultVariableName,
                createMetadataFromContext(ctx)
        );
        return null;
    }

    @Override
    public Void visitCaseClause(JsoniqParser.CaseClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx) {
        List<QuantifiedExpressionVar> vars = new ArrayList<>();
        QuantifiedExpression.QuantifiedOperators operator;
        Expression expression;
        this.visitExprSingle(ctx.exprSingle());

        expression = this.currentExpression;
        if (ctx.ev == null)
            operator = QuantifiedExpression.QuantifiedOperators.SOME;
        else
            operator = QuantifiedExpression.QuantifiedOperators.EVERY;
        for (JsoniqParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
            VariableReferenceExpression varRef;
            Expression varExpression;
            FlworVarSequenceType sequenceType = null;
            this.visitVarRef(currentVariable.varRef());
            varRef = (VariableReferenceExpression) this.currentExpression;
            if (currentVariable.sequenceType() != null) {
                this.visitSequenceType(currentVariable.sequenceType());
                sequenceType = (FlworVarSequenceType) this.currentExpression;
            }

            this.visitExprSingle(currentVariable.exprSingle());
            varExpression = this.currentExpression;
            vars.add(
                new QuantifiedExpressionVar(
                        varRef,
                        varExpression,
                        (sequenceType == null ? null : sequenceType.getSequence()),
                        createMetadataFromContext(ctx)
                )
            );
        }
        this.currentExpression = new QuantifiedExpression(operator, expression, vars, createMetadataFromContext(ctx));
        return null;
    }
    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        int tokenLineNumber = ctx.getStart().getLine();
        int tokenColumnNumber = ctx.getStart().getCharPositionInLine();
        return new ExceptionMetadata(tokenLineNumber, tokenColumnNumber);
    }

}


