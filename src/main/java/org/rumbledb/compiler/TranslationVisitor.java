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
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
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
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.operational.AdditiveExpression;
import org.rumbledb.expressions.operational.AndExpression;
import org.rumbledb.expressions.operational.ComparisonExpression;
import org.rumbledb.expressions.operational.MultiplicativeExpression;
import org.rumbledb.expressions.operational.OrExpression;
import org.rumbledb.expressions.operational.RangeExpression;
import org.rumbledb.expressions.operational.StringConcatExpression;
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
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.expressions.quantifiers.QuantifiedExpressionVar;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import sparksoniq.jsoniq.compiler.ValueTypeHandler;

import static org.rumbledb.types.SequenceType.mostGeneralSequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Translation is the phase in which the Abstract Syntax Tree is transformed
 * into an Expression Tree, which is a JSONiq intermediate representation.
 *
 * @author Stefan Irimescu, Can Berker Cikis, Ghislain Fourny, Andrea Rinaldi
 */
public class TranslationVisitor extends org.rumbledb.parser.JsoniqBaseVisitor<Node> {

    public TranslationVisitor() {
    }
    // endregion expr

    // region module
    @Override
    public Node visitModule(JsoniqParser.ModuleContext ctx) {
        if (!(ctx.vers == null) && !ctx.vers.isEmpty() && !ctx.vers.getText().trim().equals("1.0")) {
            throw new JsoniqVersionException(createMetadataFromContext(ctx));
        }
        return this.visitMainModule(ctx.mainModule());
    }

    @Override
    public Node visitMainModule(JsoniqParser.MainModuleContext ctx) {
        Prolog prolog = (Prolog) this.visitProlog(ctx.prolog());
        Expression commaExpression = (Expression) this.visitExpr(ctx.expr());
        return new MainModule(prolog, commaExpression, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitProlog(JsoniqParser.PrologContext ctx) {
        List<InlineFunctionExpression> InlineFunctionExpressions = new ArrayList<>();
        for (JsoniqParser.FunctionDeclContext function : ctx.functionDecl()) {
            InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) this.visitFunctionDecl(
                function
            );
            InlineFunctionExpressions.add(inlineFunctionExpression);
        }
        for (JsoniqParser.ModuleImportContext module : ctx.moduleImport()) {
            this.visitModuleImport(module);
        }

        return new Prolog(InlineFunctionExpressions, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitModuleImport(JsoniqParser.ModuleImportContext ctx) {
        throw new ModuleDeclarationException("Modules are not supported in Sparksoniq", createMetadataFromContext(ctx));
    }

    @Override
    public Node visitFunctionDecl(JsoniqParser.FunctionDeclContext ctx) {
        String fnName = ctx.fn_name.getText();
        Map<String, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = mostGeneralSequenceType;
        String paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = param.NCName().getText();
                paramType = mostGeneralSequenceType;
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            fnName,
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.sequenceType() != null) {
                    paramType = this.processSequenceType(param.sequenceType());
                } else {
                    paramType = SequenceType.mostGeneralSequenceType;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        } else {
            fnReturnType = SequenceType.mostGeneralSequenceType;
        }

        Expression bodyExpression = (Expression) this.visitExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                fnName,
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
        throw new OurBadException("Unrecognized ExprSingle.");
    }
    // endregion

    // region Flowr
    // TODO [EXPRVISITOR] count
    @Override
    public Node visitFlowrExpr(JsoniqParser.FlowrExprContext ctx) {
        Clause startClause;
        Clause childClause;
        List<Clause> contentClauses = new ArrayList<>();
        ReturnClause returnClause;
        // check the start clause, for or let
        if (ctx.start_for == null) {
            startClause = (Clause) this.visitLetClause(ctx.start_let);
        } else {
            startClause = (Clause) this.visitForClause(ctx.start_for);
        }

        // exclude return + returnExpr
        Clause previousFLWORClause = startClause;
        for (ParseTree child : ctx.children.subList(1, ctx.children.size() - 2)) {
            if (child instanceof JsoniqParser.ForClauseContext) {
                childClause = (Clause) this.visitForClause((JsoniqParser.ForClauseContext) child);
            } else if (child instanceof JsoniqParser.LetClauseContext) {
                childClause = (Clause) this.visitLetClause((JsoniqParser.LetClauseContext) child);
            } else if (child instanceof JsoniqParser.WhereClauseContext) {
                childClause = (Clause) this.visitWhereClause((JsoniqParser.WhereClauseContext) child);
            } else if (child instanceof JsoniqParser.GroupByClauseContext) {
                childClause = (Clause) this.visitGroupByClause((JsoniqParser.GroupByClauseContext) child);
            } else if (child instanceof JsoniqParser.OrderByClauseContext) {
                childClause = (Clause) this.visitOrderByClause((JsoniqParser.OrderByClauseContext) child);
            } else if (child instanceof JsoniqParser.CountClauseContext) {
                childClause = (Clause) this.visitCountClause((JsoniqParser.CountClauseContext) child);
            } else {
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet",
                        createMetadataFromContext(ctx)
                );
            }
            childClause.setPreviousClause(previousFLWORClause);
            previousFLWORClause = childClause;
            contentClauses.add(childClause);
        }

        // visit return

        Expression returnExpr = (Expression) this.visitExprSingle(ctx.return_expr);
        returnClause = new ReturnClause(
                returnExpr,
                new ExceptionMetadata(
                        ctx.getStop().getLine(),
                        ctx.getStop().getCharPositionInLine()
                )
        );
        returnClause.setPreviousClause(previousFLWORClause);

        return new FlworExpression(
                startClause,
                contentClauses,
                returnClause,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitForClause(JsoniqParser.ForClauseContext ctx) {
        List<ForClauseVar> vars = new ArrayList<>();
        ForClauseVar child;
        for (JsoniqParser.ForVarContext var : ctx.vars) {
            child = (ForClauseVar) this.visitForVar(var);
            vars.add(child);
        }

        return new ForClause(vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitForVar(JsoniqParser.ForVarContext ctx) {
        SequenceType seq = null;
        boolean emptyFlag;
        VariableReferenceExpression var = (VariableReferenceExpression) this.visitVarRef(ctx.var_ref);
        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        } else {
            seq = SequenceType.mostGeneralSequenceType;
        }
        emptyFlag = (ctx.flag != null);
        VariableReferenceExpression atVarRef = null;
        if (ctx.at != null) {
            atVarRef = (VariableReferenceExpression) this.visitVarRef(ctx.at);
        }
        Expression expr = (Expression) this.visitExprSingle(ctx.ex);

        return new ForClauseVar(var, seq, emptyFlag, atVarRef, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitLetClause(JsoniqParser.LetClauseContext ctx) {
        List<LetClauseVar> vars = new ArrayList<>();
        LetClauseVar child;
        for (JsoniqParser.LetVarContext var : ctx.vars) {
            child = (LetClauseVar) this.visitLetVar(var);
            vars.add(child);
        }

        return new LetClause(vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitLetVar(JsoniqParser.LetVarContext ctx) {
        SequenceType seq = null;
        VariableReferenceExpression var = (VariableReferenceExpression) this.visitVarRef(ctx.var_ref);
        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        } else {
            seq = SequenceType.mostGeneralSequenceType;
        }

        Expression expr = (Expression) this.visitExprSingle(ctx.ex);

        return new LetClauseVar(var, seq, expr, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        List<GroupByClauseVar> vars = new ArrayList<>();
        GroupByClauseVar child;
        for (JsoniqParser.GroupByVarContext var : ctx.vars) {
            child = (GroupByClauseVar) this.visitGroupByVar(var);
            vars.add(child);
        }
        return new GroupByClause(vars, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        boolean stable = false;
        List<OrderByClauseExpr> exprs = new ArrayList<>();
        OrderByClauseExpr child;
        for (JsoniqParser.OrderByExprContext var : ctx.orderByExpr()) {
            child = (OrderByClauseExpr) this.visitOrderByExpr(var);
            exprs.add(child);
        }
        if (ctx.stb != null && !ctx.stb.getText().isEmpty()) {
            stable = true;
        }
        return new OrderByClause(exprs, stable, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitOrderByExpr(JsoniqParser.OrderByExprContext ctx) {
        boolean ascending = true;
        if (ctx.desc != null && !ctx.desc.getText().isEmpty()) {
            ascending = false;
        }
        String uri = null;
        if (ctx.uriLiteral() != null) {
            uri = ctx.uriLiteral().getText();
        }
        OrderByClauseExpr.EMPTY_ORDER empty_order = OrderByClauseExpr.EMPTY_ORDER.NONE;
        if (ctx.gr != null && !ctx.gr.getText().isEmpty()) {
            empty_order = OrderByClauseExpr.EMPTY_ORDER.LAST;
        }
        if (ctx.ls != null && !ctx.ls.getText().isEmpty()) {
            empty_order = OrderByClauseExpr.EMPTY_ORDER.FIRST;
        }
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        return new OrderByClauseExpr(
                expression,
                ascending,
                uri,
                empty_order,
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitGroupByVar(JsoniqParser.GroupByVarContext ctx) {
        SequenceType seq = null;
        Expression expr = null;
        String uri = null;
        VariableReferenceExpression var = (VariableReferenceExpression) this.visitVarRef(ctx.var_ref);

        if (ctx.seq != null) {
            seq = this.processSequenceType(ctx.seq);
        } else {
            seq = SequenceType.mostGeneralSequenceType;
        }

        if (ctx.ex != null) {

            expr = (Expression) this.visitExprSingle(ctx.ex);
        }

        if (ctx.uri != null) {
            uri = ctx.uri.getText();
        }

        return new GroupByClauseVar(var, seq, expr, uri, createMetadataFromContext(ctx));
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
        List<Expression> rhs = new ArrayList<>();
        Expression mainExpression = (Expression) this.visitAndExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        for (JsoniqParser.AndExprContext child : ctx.rhs) {
            rhs.add((Expression) this.visitAndExpr(child));
        }
        return new OrExpression(mainExpression, rhs, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitAndExpr(JsoniqParser.AndExprContext ctx) {
        List<Expression> rhs = new ArrayList<>();
        Expression mainExpression = (Expression) this.visitNotExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        for (JsoniqParser.NotExprContext child : ctx.rhs) {

            rhs.add((Expression) this.visitNotExpr(child));
        }
        return new AndExpression(mainExpression, rhs, createMetadataFromContext(ctx));
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
                OperationalExpressionBase.getOperatorFromString(ctx.op.get(0).getText()),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx) {
        List<Expression> rhs = new ArrayList<>();
        Expression mainExpression = (Expression) this.visitRangeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        for (JsoniqParser.RangeExprContext child : ctx.rhs) {
            rhs.add((Expression) this.visitRangeExpr(child));
        }
        return new StringConcatExpression(mainExpression, rhs, createMetadataFromContext(ctx));
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
        List<Expression> rhs = new ArrayList<>();
        Expression mainExpression = (Expression) this.visitMultiplicativeExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        for (JsoniqParser.MultiplicativeExprContext child : ctx.rhs) {
            rhs.add((Expression) this.visitMultiplicativeExpr(child));
        }
        return new AdditiveExpression(
                mainExpression,
                rhs,
                OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        List<Expression> rhs = new ArrayList<>();
        Expression mainExpression = (Expression) this.visitInstanceOfExpr(ctx.main_expr);
        if (ctx.rhs == null || ctx.rhs.isEmpty()) {
            return mainExpression;
        }
        for (JsoniqParser.InstanceOfExprContext child : ctx.rhs) {
            rhs.add((Expression) this.visitInstanceOfExpr(child));
        }
        return new MultiplicativeExpression(
                mainExpression,
                rhs,
                OperationalExpressionBase.getOperatorFromOpList(ctx.op),
                createMetadataFromContext(ctx)
        );
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
        return new TreatExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
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
        Expression mainExpression = (Expression) this.visitUnaryExpr(ctx.main_expr);
        if (ctx.single == null || ctx.single.isEmpty()) {
            return mainExpression;
        }
        JsoniqParser.SingleTypeContext child = ctx.single;
        SequenceType sequenceType = this.processSingleType(child);
        return new CastExpression(mainExpression, sequenceType, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        Expression mainExpression = (Expression) this.visitSimpleMapExpr(ctx.main_expr);
        if (ctx.op == null || ctx.op.isEmpty()) {
            return mainExpression;
        }
        return new UnaryExpression(
                mainExpression,
                OperationalExpressionBase.getOperatorFromOpList(ctx.op),
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
        String name = ctx.name.getText();
        if (ctx.ns != null) {
            name = name + ":" + ctx.ns.getText();
        }
        return new VariableReferenceExpression(name, createMetadataFromContext(ctx));
    }

    @Override
    public Node visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        return new ContextItemExpression(createMetadataFromContext(ctx));
    }

    public SequenceType processSequenceType(JsoniqParser.SequenceTypeContext ctx) {
        if (ctx.item == null) {
            return SequenceType.emptySequence;
        }
        ItemType itemType = new ItemType(ctx.item.getText());
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
            return SequenceType.emptySequence;
        }

        ItemType itemType = new ItemType(ctx.item.getText());
        if (ctx.question.size() > 0) {
            return new SequenceType(
                    itemType,
                    SequenceType.Arity.OneOrZero
            );
        }
        return new SequenceType(itemType);
    }

    @Override
    public Node visitFunctionCall(JsoniqParser.FunctionCallContext ctx) {
        String name;
        if (ctx.fn_name != null) {
            name = ctx.fn_name.getText();
        } else {
            name = ctx.kw.getText();
        }
        if (ctx.ns != null) {
            name = name + ":" + ctx.ns.getText();
        }
        return new FunctionCallExpression(
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
        Expression literal = ValueTypeHandler.getValueType(
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
        return new NamedFunctionReferenceExpression(
                new FunctionIdentifier(name, arity),
                createMetadataFromContext(ctx)
        );
    }

    @Override
    public Node visitInlineFunctionExpr(JsoniqParser.InlineFunctionExprContext ctx) {
        Map<String, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.mostGeneralSequenceType;
        String paramName;
        SequenceType paramType;
        if (ctx.paramList() != null) {
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                paramName = param.NCName().getText();
                paramType = SequenceType.mostGeneralSequenceType;
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            "inline-function`",
                            paramName,
                            createMetadataFromContext(param)
                    );
                }
                if (param.sequenceType() != null) {
                    paramType = this.processSequenceType(param.sequenceType());
                } else {
                    paramType = SequenceType.mostGeneralSequenceType;
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.return_type != null) {
            fnReturnType = this.processSequenceType(ctx.return_type);
        }

        Expression expr = (Expression) this.visitExpr(ctx.fn_body);

        return new InlineFunctionExpression(
                "",
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
            String variableName = null;
            if (expr.var_ref != null) {
                variableName = expr.var_ref.name.getText();
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
        String defaultVariableName = null;
        if (ctx.var_ref != null) {
            defaultVariableName = ctx.var_ref.name.getText();
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
        QuantifiedExpression.QuantifiedOperators operator;
        Expression expression = (Expression) this.visitExprSingle(ctx.exprSingle());
        if (ctx.ev == null) {
            operator = QuantifiedExpression.QuantifiedOperators.SOME;
        } else {
            operator = QuantifiedExpression.QuantifiedOperators.EVERY;
        }
        for (JsoniqParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
            VariableReferenceExpression varRef;
            Expression varExpression;
            SequenceType sequenceType = null;
            varRef = (VariableReferenceExpression) this.visitVarRef(currentVariable.varRef());
            if (currentVariable.sequenceType() != null) {
                sequenceType = this.processSequenceType(currentVariable.sequenceType());
            } else {
                sequenceType = SequenceType.mostGeneralSequenceType;
            }

            varExpression = (Expression) this.visitExprSingle(currentVariable.exprSingle());
            vars.add(
                new QuantifiedExpressionVar(
                        varRef,
                        varExpression,
                        sequenceType,
                        createMetadataFromContext(ctx)
                )
            );
        }
        return new QuantifiedExpression(operator, expression, vars, createMetadataFromContext(ctx));
    }
    // endregion

    private ExceptionMetadata createMetadataFromContext(ParserRuleContext ctx) {
        int tokenLineNumber = ctx.getStart().getLine();
        int tokenColumnNumber = ctx.getStart().getCharPositionInLine();
        return new ExceptionMetadata(tokenLineNumber, tokenColumnNumber);
    }

}


