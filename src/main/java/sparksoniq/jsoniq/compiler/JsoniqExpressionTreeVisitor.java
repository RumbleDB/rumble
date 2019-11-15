/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import sparksoniq.exceptions.DuplicateParamNameException;
import sparksoniq.exceptions.JsoniqVersionException;
import sparksoniq.exceptions.ModuleDeclarationException;
import sparksoniq.exceptions.UnsupportedFeatureException;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.jsoniq.compiler.translator.expr.CommaExpression;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.control.IfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.SwitchCaseExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.SwitchExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchCaseExpression;
import sparksoniq.jsoniq.compiler.translator.expr.control.TypeSwitchExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.CountClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworExpression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSingleType;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ForClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.GroupByClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.LetClauseVar;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.ReturnClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.WhereClause;
import sparksoniq.jsoniq.compiler.translator.expr.module.FunctionDeclarationExpression;
import sparksoniq.jsoniq.compiler.translator.expr.module.MainModuleExpression;
import sparksoniq.jsoniq.compiler.translator.expr.module.PrologExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.AdditiveExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.AndExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.CastExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.CastableExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.ComparisonExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.InstanceOfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.MultiplicativeExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.NotExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.OrExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.RangeExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.StringConcatExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.TreatExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.UnaryExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ArrayLookupExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ArrayUnboxingExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.ObjectLookupExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PostfixExtension;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PredicateExtension;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ArrayConstructor;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ContextExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.FunctionCall;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ObjectConstructor;
import sparksoniq.jsoniq.compiler.translator.expr.primary.ParenthesizedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.PrimaryExpression;
import sparksoniq.jsoniq.compiler.translator.expr.primary.StringLiteral;
import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


//used to build AST, will override methods
public class JsoniqExpressionTreeVisitor extends sparksoniq.jsoniq.compiler.parser.JsoniqBaseVisitor<Void> {

    private MainModuleExpression mainModuleExpression;

    private Expression currentExpression;
    private PrimaryExpression currentPrimaryExpression;
    private PostfixExtension currentPostFixExtension;
    private FlworClause currentFlworClause;

    public JsoniqExpressionTreeVisitor() {
    }
    //endregion expr

    public MainModuleExpression getMainModuleExpression() {
        return mainModuleExpression;
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
        MainModuleExpression node;
        this.visitProlog(ctx.prolog());
        PrologExpression prolog = (PrologExpression) this.currentExpression;
        this.visitExpr(ctx.expr());
        CommaExpression commaExpression = (CommaExpression) this.currentExpression;
        node = new MainModuleExpression(prolog, commaExpression, createMetadataFromContext(ctx));
        this.currentExpression = node;
        this.mainModuleExpression = node;
        return null;
    }

    @Override
    public Void visitProlog(JsoniqParser.PrologContext ctx) {
        List<FunctionDeclarationExpression> functionDeclarations = new ArrayList<>();
        PrologExpression node;
        for (JsoniqParser.FunctionDeclContext function: ctx.functionDecl()) {
            this.visitFunctionDecl(function);
            functionDeclarations.add((FunctionDeclarationExpression) this.currentExpression);
        }
        for (JsoniqParser.ModuleImportContext module: ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        node = new PrologExpression(functionDeclarations, createMetadataFromContext(ctx));
        this.currentExpression = node;
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
                ItemTypes.Item,
                SequenceType.Arity.ZeroOrMore,
                createMetadataFromContext(ctx)
        );
        CommaExpression fnBody;
        FunctionDeclarationExpression node;
        String paramName;
        FlworVarSequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = param.NCName().getText();
                paramType = new FlworVarSequenceType(
                        ItemTypes.Item,
                        SequenceType.Arity.ZeroOrMore,
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
        fnBody = (CommaExpression) this.currentExpression;

        node = new FunctionDeclarationExpression(fnName, fnParams, fnReturnType, fnBody, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    //region expr
    @Override
    public Void visitExpr(JsoniqParser.ExprContext ctx) {
        CommaExpression node;
        List<Expression> expressions = new ArrayList<>();
        Expression expression;
        for (JsoniqParser.ExprSingleContext expr : ctx.exprSingle()) {
            this.visitExprSingle(expr);
            expression = this.currentExpression;
            expressions.add(expression);

        }
        node = new CommaExpression(expressions, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitExprSingle(JsoniqParser.ExprSingleContext ctx) {

        Expression node;
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
        node = this.currentExpression;
        this.currentExpression = node;
        return null;
    }

    //region Flowr
    //TODO [EXPRVISITOR] count
    @Override
    public Void visitFlowrExpr(JsoniqParser.FlowrExprContext ctx) {
        FlworExpression node;
        FlworClause startClause, childClause;
        List<FlworClause> contentClauses = new ArrayList<>();
        ReturnClause returnClause;
        //check the start clause, for or let
        if (ctx.start_for == null) {
            this.visitLetClause(ctx.start_let);
            startClause = this.currentFlworClause;
        } else {
            this.visitForClause(ctx.start_for);
            startClause = this.currentFlworClause;
        }

        //exclude return + returnExpr
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext) {
                this.visitForClause((JsoniqParser.ForClauseContext) child);
                childClause = this.currentFlworClause;
            } else if (child instanceof JsoniqParser.LetClauseContext) {
                this.visitLetClause((JsoniqParser.LetClauseContext) child);
                childClause = this.currentFlworClause;
            } else if (child instanceof JsoniqParser.WhereClauseContext) {
                this.visitWhereClause((JsoniqParser.WhereClauseContext) child);
                childClause = this.currentFlworClause;
            } else if (child instanceof JsoniqParser.GroupByClauseContext) {
                this.visitGroupByClause((JsoniqParser.GroupByClauseContext) child);
                childClause = this.currentFlworClause;
            } else if (child instanceof JsoniqParser.OrderByClauseContext) {
                this.visitOrderByClause((JsoniqParser.OrderByClauseContext) child);
                childClause = this.currentFlworClause;
            } else if (child instanceof JsoniqParser.CountClauseContext) {
                this.visitCountClause((JsoniqParser.CountClauseContext) child);
                childClause = this.currentFlworClause;
            } else
                throw new UnsupportedFeatureException("FLOWR clause not implemented yet", createMetadataFromContext(ctx));

            contentClauses.add(childClause);
        }

        //visit return
        this.visitExprSingle(ctx.return_expr);
        Expression returnExpr = this.currentExpression;
        returnClause = new ReturnClause(returnExpr, new ExpressionMetadata(ctx.getStop().getLine(),
                ctx.getStop().getCharPositionInLine()));

        node = new FlworExpression(startClause, contentClauses, returnClause, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitForClause(JsoniqParser.ForClauseContext ctx) {
        ForClause node;
        List<ForClauseVar> vars = new ArrayList<>();
        ForClauseVar child;
        for (JsoniqParser.ForVarContext var : ctx.vars) {
            this.visitForVar(var);
            child = (ForClauseVar) this.currentFlworClause;
            vars.add(child);
        }

        node = new ForClause(vars, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitForVar(JsoniqParser.ForVarContext ctx) {
        VariableReference var, atVarRef = null;
        FlworVarSequenceType seq = null;
        Expression expr;
        boolean emptyFlag;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference) this.currentPrimaryExpression;
        if (ctx.seq != null) {
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType) this.currentExpression;
        }
        emptyFlag = (ctx.flag != null);
        if (ctx.at != null) {
            this.visitVarRef(ctx.at);
            atVarRef = (VariableReference) this.currentPrimaryExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        ForClauseVar node = new ForClauseVar(var, seq, emptyFlag, atVarRef, expr, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitLetClause(JsoniqParser.LetClauseContext ctx) {
        LetClause node;
        List<LetClauseVar> vars = new ArrayList<>();
        LetClauseVar child;
        for (JsoniqParser.LetVarContext var : ctx.vars) {
            this.visitLetVar(var);
            child = (LetClauseVar) this.currentFlworClause;
            vars.add(child);
        }

        node = new LetClause(vars, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitLetVar(JsoniqParser.LetVarContext ctx) {
        VariableReference var;
        FlworVarSequenceType seq = null;
        Expression expr;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference) this.currentPrimaryExpression;
        if (ctx.seq != null) {
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType) this.currentExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        LetClauseVar node = new LetClauseVar(var, seq, expr, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        GroupByClause node;
        List<GroupByClauseVar> vars = new ArrayList<>();
        GroupByClauseVar child;
        for (JsoniqParser.GroupByVarContext var : ctx.vars) {
            this.visitGroupByVar(var);
            child = (GroupByClauseVar) this.currentFlworClause;
            vars.add(child);
        }
        node = new GroupByClause(vars, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;

    }

    //endregion

    @Override
    public Void visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        OrderByClause node;
        boolean stable = false;
        List<OrderByClauseExpr> exprs = new ArrayList<>();
        OrderByClauseExpr child;
        for (JsoniqParser.OrderByExprContext var : ctx.orderByExpr()) {
            this.visitOrderByExpr(var);
            child = (OrderByClauseExpr) this.currentFlworClause;
            exprs.add(child);
        }
        if (ctx.stb != null && !ctx.stb.getText().isEmpty())
            stable = true;
        node = new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;

    }

    @Override
    public Void visitOrderByExpr(JsoniqParser.OrderByExprContext ctx) {
        OrderByClauseExpr node;
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
        node = new OrderByClauseExpr(expression, ascending, uri, empty_order, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitGroupByVar(JsoniqParser.GroupByVarContext ctx) {
        VariableReference var;
        FlworVarSequenceType seq = null;
        Expression expr = null;
        String uri = null;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference) this.currentPrimaryExpression;

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

        GroupByClauseVar node = new GroupByClauseVar(var, seq, expr, uri, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        WhereClause node;
        Expression expr;
        this.visitExprSingle(ctx.exprSingle());
        expr = this.currentExpression;
        node = new WhereClause(expr, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    @Override
    public Void visitCountClause(JsoniqParser.CountClauseContext ctx) {
        CountClause node;
        VariableReference child;
        this.visitVarRef(ctx.varRef());
        child = (VariableReference) this.currentPrimaryExpression;
        node = new CountClause(child, createMetadataFromContext(ctx));
        this.currentFlworClause = node;
        return null;
    }

    //region operational
    @Override
    public Void visitOrExpr(JsoniqParser.OrExprContext ctx) {
        AndExpression mainExpression, childExpression;
        OrExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitAndExpr(ctx.main_expr);
        mainExpression = (AndExpression) this.currentExpression;
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.AndExprContext child : ctx.rhs) {
                this.visitAndExpr(child);
                childExpression = (AndExpression) this.currentExpression;
                rhs.add(childExpression);
            }
            node = new OrExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        } else {
            node = new OrExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;

    }

    @Override
    public Void visitAndExpr(JsoniqParser.AndExprContext ctx) {
        NotExpression mainExpression, childExpression;
        AndExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitNotExpr(ctx.main_expr);
        mainExpression = (NotExpression) this.currentExpression;
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.NotExprContext child : ctx.rhs) {
                this.visitNotExpr(child);
                childExpression = (NotExpression) this.currentExpression;
                rhs.add(childExpression);
            }
            node = new AndExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        } else {
            node = new AndExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitNotExpr(JsoniqParser.NotExprContext ctx) {
        ComparisonExpression mainExpression;
        NotExpression node;
        this.visitComparisonExpr(ctx.main_expr);
        mainExpression = (ComparisonExpression) this.currentExpression;
        node = new NotExpression(mainExpression, !(ctx.op == null || ctx.op.isEmpty()),
                createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        StringConcatExpression mainExpression, childExpression;
        ComparisonExpression node;
        this.visitStringConcatExpr(ctx.main_expr);
        mainExpression = (StringConcatExpression) this.currentExpression;
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            JsoniqParser.StringConcatExprContext child = ctx.rhs.get(0);
            this.visitStringConcatExpr(child);
            childExpression = (StringConcatExpression) this.currentExpression;

            node = new ComparisonExpression(mainExpression, childExpression,
                    OperationalExpressionBase.getOperatorFromString(ctx.op.get(0).getText()),
                    createMetadataFromContext(ctx));
        } else {
            node = new ComparisonExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        RangeExpression mainExpression, childExpression;
        StringConcatExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitRangeExpr(ctx.main_expr);
        mainExpression = (RangeExpression) this.currentExpression;
        if (!(ctx.rhs == null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.RangeExprContext child : ctx.rhs) {
                this.visitRangeExpr(child);
                childExpression = (RangeExpression) this.currentExpression;
                rhs.add(childExpression);
            }
            node = new StringConcatExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        } else {
            node = new StringConcatExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }
    //endregion

    @Override
    public Void visitRangeExpr(JsoniqParser.RangeExprContext ctx) {

        AdditiveExpression mainExpression, childExpression;
        RangeExpression node;
        this.visitAdditiveExpr(ctx.main_expr);
        mainExpression = (AdditiveExpression) this.currentExpression;
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            JsoniqParser.AdditiveExprContext child = ctx.rhs.get(0);
            this.visitAdditiveExpr(child);
            childExpression = (AdditiveExpression) this.currentExpression;
            node = new RangeExpression(mainExpression, childExpression, createMetadataFromContext(ctx));
        } else {
            node = new RangeExpression(mainExpression, createMetadataFromContext(ctx));
        }

        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        MultiplicativeExpression mainExpression, childExpression;
        AdditiveExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitMultiplicativeExpr(ctx.main_expr);
        mainExpression = (MultiplicativeExpression) this.currentExpression;
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.MultiplicativeExprContext child : ctx.rhs) {
                this.visitMultiplicativeExpr(child);
                childExpression = (MultiplicativeExpression) this.currentExpression;
                rhs.add(childExpression);
            }
            node = new AdditiveExpression(mainExpression, rhs,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                    createMetadataFromContext(ctx));
        } else {
            node = new AdditiveExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        InstanceOfExpression mainExpression, childExpression;
        MultiplicativeExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitInstanceOfExpr(ctx.main_expr);
        mainExpression = (InstanceOfExpression) this.currentExpression;
        if (ctx.rhs != null && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.InstanceOfExprContext child : ctx.rhs) {
                this.visitInstanceOfExpr(child);
                childExpression = (InstanceOfExpression) this.currentExpression;
                rhs.add(childExpression);
            }
            node = new MultiplicativeExpression(mainExpression, rhs,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                    createMetadataFromContext(ctx));
        } else {
            node = new MultiplicativeExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx) {
        TreatExpression mainExpression;
        FlworVarSequenceType sequenceType;
        InstanceOfExpression node;
        this.visitTreatExpr(ctx.main_expr);
        mainExpression = (TreatExpression) this.currentExpression;
        if (ctx.seq != null && !ctx.seq.isEmpty()) {
            JsoniqParser.SequenceTypeContext child = ctx.seq;
            this.visitSequenceType(child);
            sequenceType = (FlworVarSequenceType) this.currentExpression;
            node = new InstanceOfExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
        } else {
            node = new InstanceOfExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitTreatExpr(JsoniqParser.TreatExprContext ctx) {
        CastableExpression mainExpression;
        FlworVarSequenceType sequenceType;
        TreatExpression node;
        this.visitCastableExpr(ctx.main_expr);
        mainExpression = (CastableExpression) this.currentExpression;
        if (ctx.seq != null && !ctx.seq.isEmpty()) {
            JsoniqParser.SequenceTypeContext child = ctx.seq;
            this.visitSequenceType(child);
            sequenceType = (FlworVarSequenceType) this.currentExpression;
            node = new TreatExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
        } else {
            node = new TreatExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitCastableExpr(JsoniqParser.CastableExprContext ctx) {
        CastExpression mainExpression;
        FlworVarSingleType singleType;
        CastableExpression node;
        this.visitCastExpr(ctx.main_expr);
        mainExpression = (CastExpression) this.currentExpression;
        if (ctx.single != null && !ctx.single.isEmpty()) {
            JsoniqParser.SingleTypeContext child = ctx.single;
            this.visitSingleType(child);
            singleType = (FlworVarSingleType) this.currentExpression;
            node = new CastableExpression(mainExpression, singleType, createMetadataFromContext(ctx));
        } else {
            node = new CastableExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitCastExpr(JsoniqParser.CastExprContext ctx) {
        UnaryExpression mainExpression;
        FlworVarSingleType singleType;
        CastExpression node;
        this.visitUnaryExpr(ctx.main_expr);
        mainExpression = (UnaryExpression) this.currentExpression;
        if (ctx.single != null && !ctx.single.isEmpty()) {
            JsoniqParser.SingleTypeContext child = ctx.single;
            this.visitSingleType(child);
            singleType = (FlworVarSingleType) this.currentExpression;
            node = new CastExpression(mainExpression, singleType, createMetadataFromContext(ctx));
        } else {
            node = new CastExpression(mainExpression, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        //TODO [EXPRVISITOR] jump from unary to postfix
        PostFixExpression mainExpression;
        UnaryExpression node;
        this.visitSimpleMapExpr(ctx.main_expr);
        mainExpression = (PostFixExpression) this.currentExpression;
        if (ctx.op == null || ctx.op.isEmpty())
            node = new UnaryExpression(mainExpression, createMetadataFromContext(ctx));
        else
            node = new UnaryExpression(mainExpression,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op), createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }
    //endregion

    //region postfix
    @Override
    public Void visitPostFixExpr(JsoniqParser.PostFixExprContext ctx) {
        PostfixExtension childExpression = null;
        PrimaryExpression mainExpression;
        PostFixExpression node;
        List<PostfixExtension> rhs = new ArrayList<>();
        this.visitPrimaryExpr(ctx.main_expr);
        mainExpression = this.currentPrimaryExpression;
        for (ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if (child instanceof JsoniqParser.PredicateContext) {
                this.visitPredicate((JsoniqParser.PredicateContext) child);
                childExpression = this.currentPostFixExtension;
            } else if (child instanceof JsoniqParser.ObjectLookupContext) {
                this.visitObjectLookup((JsoniqParser.ObjectLookupContext) child);
                childExpression = this.currentPostFixExtension;
            } else if (child instanceof JsoniqParser.ArrayLookupContext) {
                this.visitArrayLookup((JsoniqParser.ArrayLookupContext) child);
                childExpression = this.currentPostFixExtension;
            } else if (child instanceof JsoniqParser.ArrayUnboxingContext) {
                this.visitArrayUnboxing((JsoniqParser.ArrayUnboxingContext) child);
                childExpression = this.currentPostFixExtension;
            }
            rhs.add(childExpression);
        }
        node = new PostFixExpression(mainExpression, rhs, createMetadataFromContext(ctx));
        rhs.forEach(e -> e.setParent(node));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitPredicate(JsoniqParser.PredicateContext ctx) {
        PredicateExtension node;
        CommaExpression content;
        this.visitExpr(ctx.expr());
        content = (CommaExpression) this.currentExpression;
        node = new PredicateExtension(content, createMetadataFromContext(ctx));
        this.currentPostFixExtension = node;
        return null;
    }

    @Override
    public Void visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        //TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        ObjectLookupExtension node;
        Expression expr = null;
        if (ctx.lt != null) {
            expr = new StringLiteral(ValueTypeHandler.getStringValue(ctx.lt), createMetadataFromContext(ctx));
        } else if (ctx.nc != null) {
            expr = new StringLiteral(ctx.nc.getText(), createMetadataFromContext(ctx));
        } else if (ctx.kw != null) {
            expr = new StringLiteral(ctx.kw.getText(), createMetadataFromContext(ctx));
        } else if (ctx.pe != null) {
            this.visitParenthesizedExpr(ctx.pe);
            expr = this.currentPrimaryExpression;
        } else if (ctx.vr != null) {
            this.visitVarRef(ctx.vr);
            expr = this.currentPrimaryExpression;
        } else if (ctx.ci != null) {
            this.visitContextItemExpr(ctx.ci);
            expr = this.currentPrimaryExpression;
        } else if (ctx.tkw != null) {
            expr = new StringLiteral(ctx.tkw.getText(), createMetadataFromContext(ctx));
        }

        node = new ObjectLookupExtension(expr, createMetadataFromContext(ctx));
        this.currentPostFixExtension = node;
        return null;
    }

    @Override
    public Void visitArrayLookup(JsoniqParser.ArrayLookupContext ctx) {
        ArrayLookupExtension node;
        CommaExpression content;
        this.visitExpr(ctx.expr());
        content = (CommaExpression) this.currentExpression;
        node = new ArrayLookupExtension(content, createMetadataFromContext(ctx));
        this.currentPostFixExtension = node;
        return null;
    }

    @Override
    public Void visitArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx) {
        this.currentPostFixExtension = new ArrayUnboxingExtension(createMetadataFromContext(ctx));
        return null;
    }

    //region primary
    //TODO [EXPRVISITOR] orderedExpr unorderedExpr;
    @Override
    public Void visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx) {
        PrimaryExpression node = null;

        for (ParseTree child : ctx.children) {
            if (child instanceof JsoniqParser.VarRefContext) {
                this.visitVarRef((JsoniqParser.VarRefContext) child);
                node = this.currentPrimaryExpression;
            } else if (child instanceof JsoniqParser.ObjectConstructorContext) {
                this.visitObjectConstructor((JsoniqParser.ObjectConstructorContext) child);
                node = this.currentPrimaryExpression;
            } else if (child instanceof JsoniqParser.ArrayConstructorContext) {
                this.visitArrayConstructor((JsoniqParser.ArrayConstructorContext) child);
                node = this.currentPrimaryExpression;
            } else if (child instanceof JsoniqParser.ParenthesizedExprContext) {
                this.visitParenthesizedExpr((JsoniqParser.ParenthesizedExprContext) child);
                node = this.currentPrimaryExpression;
            } else if (child instanceof JsoniqParser.StringLiteralContext) {
                node = new StringLiteral(ValueTypeHandler.
                        getStringValue((JsoniqParser.StringLiteralContext) child), createMetadataFromContext(ctx));
            } else if (child instanceof TerminalNode) {
                node = ValueTypeHandler.getValueType(child.getText(), createMetadataFromContext(ctx));
            } else if (child instanceof JsoniqParser.ContextItemExprContext) {
                this.visitContextItemExpr((JsoniqParser.ContextItemExprContext) child);
                node = this.currentPrimaryExpression;
            } else if (child instanceof JsoniqParser.FunctionCallContext) {
                this.visitFunctionCall((JsoniqParser.FunctionCallContext) child);
                node = this.currentPrimaryExpression;
            } else
                throw new UnsupportedFeatureException("Primary expression not yet implemented",
                        createMetadataFromContext(ctx));
        }

        this.currentPrimaryExpression = node;
        return null;

    }

    @Override
    public Void visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
        ObjectConstructor node;
        //no merging constructor, just visit the k/v pairs
        if (ctx.merge_operator == null || ctx.merge_operator.size() == 0 ||
                ctx.merge_operator.get(0).getText().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            ObjectConstructor.PairConstructor pair;
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                this.visitPairConstructor(currentPair);
                pair = (ObjectConstructor.PairConstructor) this.currentPrimaryExpression;
                keys.add(pair.get_key());
                values.add(pair.get_value());
            }
            node = new ObjectConstructor(keys, values, createMetadataFromContext(ctx));
        } else {
            Expression childExpr;
            this.visitExpr(ctx.expr());
            childExpr = this.currentExpression;
            node = new ObjectConstructor((CommaExpression) childExpr, createMetadataFromContext(ctx));
        }

        this.currentPrimaryExpression = node;
        return null;
    }

    //endregion

    //TODO[EXPRVISITOR]? not supported in Pair constructor
    @Override
    public Void visitPairConstructor(JsoniqParser.PairConstructorContext ctx) {
        ObjectConstructor.PairConstructor node;
        Expression rhs, lhs;
        this.visitExprSingle(ctx.rhs);
        rhs = this.currentExpression;
        if (ctx.lhs != null) {
            this.visitExprSingle(ctx.lhs);
            lhs = this.currentExpression;
        } else {
            lhs = new StringLiteral(ctx.name.getText(), createMetadataFromContext(ctx));
        }
        node = new ObjectConstructor.PairConstructor(lhs, rhs, createMetadataFromContext(ctx));
        this.currentPrimaryExpression = node;
        return null;

    }

    @Override
    public Void visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        ArrayConstructor node;
        CommaExpression content;
        if (ctx.expr() == null)
            node = new ArrayConstructor(createMetadataFromContext(ctx));
        else {
            this.visitExpr(ctx.expr());
            content = (CommaExpression) this.currentExpression;
            node = new ArrayConstructor(content, createMetadataFromContext(ctx));
        }
        this.currentPrimaryExpression = node;
        return null;

    }

    @Override
    public Void visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx) {
        ParenthesizedExpression node;
        CommaExpression content;
        if (ctx.expr() == null)
            node = new ParenthesizedExpression(createMetadataFromContext(ctx));
        else {
            this.visitExpr(ctx.expr());
            content = (CommaExpression) this.currentExpression;
            node = new ParenthesizedExpression(content, createMetadataFromContext(ctx));
        }
        this.currentPrimaryExpression = node;
        return null;
    }
    //endregion

    @Override
    public Void visitVarRef(JsoniqParser.VarRefContext ctx) {
        VariableReference node;
        String name = ctx.name.getText();
        if (ctx.ns != null)
            name = name + ":" + ctx.ns.getText();
        node = new VariableReference(name, createMetadataFromContext(ctx));
        this.currentPrimaryExpression = node;
        return null;
    }

    @Override
    public Void visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        this.currentPrimaryExpression = new ContextExpression(createMetadataFromContext(ctx));
        return null;
    }

    @Override
    public Void visitSequenceType(JsoniqParser.SequenceTypeContext ctx) {
        FlworVarSequenceType node;
        if (ctx.item == null)
            node = new FlworVarSequenceType(createMetadataFromContext(ctx));
        else {
            ItemTypes item = FlworVarSequenceType.getItemType(ctx.item.getText());
            if (ctx.question.size() > 0)
                node = new FlworVarSequenceType(item, SequenceType.Arity.OneOrZero, createMetadataFromContext(ctx));
            else if (ctx.star.size() > 0)
                node = new FlworVarSequenceType(item, SequenceType.Arity.ZeroOrMore, createMetadataFromContext(ctx));
            else if (ctx.plus.size() > 0)
                node = new FlworVarSequenceType(item, SequenceType.Arity.OneOrMore, createMetadataFromContext(ctx));
            else
                node = new FlworVarSequenceType(item, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }


    @Override
    public Void visitSingleType(JsoniqParser.SingleTypeContext ctx) {
        FlworVarSingleType node;
        if (ctx.item == null)
            node = new FlworVarSingleType(createMetadataFromContext(ctx));
        else {
            AtomicTypes item = FlworVarSingleType.getAtomicType(ctx.item.getText());
            if (ctx.question.size() > 0)
                node = new FlworVarSingleType(item, true, createMetadataFromContext(ctx));
            else
                node = new FlworVarSingleType(item, createMetadataFromContext(ctx));
        }
        this.currentExpression = node;
        return null;
    }

    //region new features
    @Override
    public Void visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        FunctionCall node;
        String name;
        if (ctx.fn_name != null)
            name = ctx.fn_name.getText();
        else
            name = ctx.kw.getText();
        if (ctx.ns != null)
            name = name + ":" + ctx.ns.getText();
        List<Expression> parameters = new ArrayList<>();
        if (ctx.argumentList().args != null)
            for (JsoniqParser.ArgumentContext arg : ctx.argumentList().args) {
                this.visitArgument(arg);
                Expression currentArg = this.currentExpression;
                parameters.add(currentArg);
            }
        node = new FunctionCall(name, parameters, createMetadataFromContext(ctx));
        this.currentPrimaryExpression = node;
        return null;
    }

    @Override
    public Void visitArgument(JsoniqParser.ArgumentContext ctx) {
        this.visitExprSingle(ctx.exprSingle());
        return null;
    }

    @Override
    public Void visitIfExpr(JsoniqParser.IfExprContext ctx) {
        IfExpression node;
        Expression condition, branch, else_branch;
        this.visitExpr(ctx.test_condition);
        condition = this.currentExpression;
        this.visitExprSingle(ctx.branch);
        branch = this.currentExpression;
        this.visitExprSingle(ctx.else_branch);
        else_branch = this.currentExpression;
        node = new IfExpression(condition, branch, else_branch, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitSwitchExpr(JsoniqParser.SwitchExprContext ctx) {
        SwitchExpression node;
        Expression condition, defaultCase;
        this.visitExpr(ctx.cond);
        condition = this.currentExpression;
        List<SwitchCaseExpression> cases = new ArrayList<>();
        for (JsoniqParser.SwitchCaseClauseContext expr : ctx.cases) {
            this.visitSwitchCaseClause(expr);
            cases.add((SwitchCaseExpression) this.currentExpression);
        }
        this.visitExprSingle(ctx.def);
        defaultCase = this.currentExpression;
        node = new SwitchExpression(condition, cases, defaultCase, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitSwitchCaseClause(JsoniqParser.SwitchCaseClauseContext ctx) {
        SwitchCaseExpression node;
        Expression condition, returnExpression;
        //TODO multiple case expressions?
        this.visitExprSingle(ctx.cond.get(0));
        condition = this.currentExpression;
        this.visitExprSingle(ctx.ret);
        returnExpression = this.currentExpression;
        node = new SwitchCaseExpression(condition, returnExpression, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitTypeSwitchExpr(JsoniqParser.TypeSwitchExprContext ctx) {
        TypeSwitchExpression node;
        Expression condition, defaultCase;
        this.visitExpr(ctx.cond);
        condition = this.currentExpression;
        List<TypeSwitchCaseExpression> cases = new ArrayList<>();
        for (JsoniqParser.CaseClauseContext expr : ctx.cses) {
            this.visitCaseClause(expr);
            cases.add((TypeSwitchCaseExpression) this.currentExpression);
        }
        VariableReference varRefDefault = null;
        if (ctx.var_ref != null) {
            this.visitVarRef(ctx.var_ref);
            varRefDefault = (VariableReference) this.currentPrimaryExpression;
        }
        this.visitExprSingle(ctx.def);
        defaultCase = this.currentExpression;
        node = new TypeSwitchExpression(condition, cases, defaultCase, varRefDefault, createMetadataFromContext(ctx));
        this.currentExpression = node;
        return null;
    }

    @Override
    public Void visitCaseClause(JsoniqParser.CaseClauseContext ctx) {
        TypeSwitchCaseExpression node;
        Expression returnExpression;

        VariableReference var = null;
        List<FlworVarSequenceType> union = new ArrayList<>();
        if (ctx.var_ref != null) {
            this.visitVarRef(ctx.var_ref);
            var = (VariableReference) this.currentPrimaryExpression;
        }
        if (ctx.union != null && !ctx.union.isEmpty()) {
            for (JsoniqParser.SequenceTypeContext sequenceType : ctx.union) {
                this.visitSequenceType(sequenceType);
                union.add((FlworVarSequenceType) this.currentExpression);
            }
        }
        this.visitExprSingle(ctx.ret);
        returnExpression = this.currentExpression;
        node = new TypeSwitchCaseExpression(var, union, returnExpression, createMetadataFromContext(ctx));
        this.currentExpression = node;
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
            VariableReference varRef;
            Expression varExpression;
            FlworVarSequenceType sequenceType = null;
            this.visitVarRef(currentVariable.varRef());
            varRef = (VariableReference) this.currentPrimaryExpression;
            if (currentVariable.sequenceType() != null) {
                this.visitSequenceType(currentVariable.sequenceType());
                sequenceType = (FlworVarSequenceType) this.currentExpression;
            }

            this.visitExprSingle(currentVariable.exprSingle());
            varExpression = this.currentExpression;
            vars.add(new QuantifiedExpressionVar(varRef, varExpression,
                    (sequenceType == null ? null : sequenceType.getSequence()), createMetadataFromContext(ctx)));
        }
        this.currentExpression = new QuantifiedExpression(operator, expression, vars, createMetadataFromContext(ctx));
        return null;
    }

    private int getDepthLevel(JsoniqParser.ExprContext ctx) {
        int count = 0;
        ParseTree level = ctx;
        while (level != null) {
            level = level.getParent();
            count++;
        }

        return count;
    }

    private ExpressionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        int tokenLineNumber = ctx.getStart().getLine();
        int tokenColumnNumber = ctx.getStart().getCharPositionInLine();
        return new ExpressionMetadata(tokenLineNumber, tokenColumnNumber);
    }

}



