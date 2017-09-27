/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler;

import sparksoniq.exceptions.ModuleDeclarationException;
import sparksoniq.jsoniq.compiler.translator.expr.control.IfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.InstanceOfExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpressionVar;
import sparksoniq.exceptions.JsoniqVersionException;
import sparksoniq.exceptions.UnsupportedFeatureException;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.compiler.translator.expr.CommaExpression;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.*;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.*;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.*;
import sparksoniq.jsoniq.compiler.translator.expr.primary.*;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.semantics.types.SequenceType;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;


//used to build AST, will override methods
public class JsoniqExpressionTreeVisitor extends sparksoniq.jsoniq.compiler.parser.JsoniqBaseVisitor<Void> {

    public Expression getQueryExpression() {
        return queryExpression;
    }

    private CommaExpression queryExpression;
    private Expression currentExpression;
    private PrimaryExpression currentPrimaryExpression;
    private PostfixExtension currentPostFixExtension;
    private FlworClause currentFlworClause;

    private int getDepthLevel(JsoniqParser.ExprContext ctx)
    {
        int count = 0;
        ParseTree level = ctx;
        while(level != null)
        {
            level = level.getParent();
            count++;
        }

        return count;
    }

    public JsoniqExpressionTreeVisitor() {}

    @Override public Void visitModule(JsoniqParser.ModuleContext ctx) {
        if(!(ctx.vers == null) && !ctx.vers.isEmpty() && !ctx.vers.getText().trim().equals("1.0"))
            throw new JsoniqVersionException();
        this.visitMainModule(ctx.mainModule());
        return null;
    }

    @Override public Void visitModuleImport(JsoniqParser.ModuleImportContext ctx) {
        throw new ModuleDeclarationException("Modules are not supported in Sparksoniq");
    }

    //region expr
    @Override public Void visitExpr(JsoniqParser.ExprContext ctx) {
        CommaExpression node;
        List<Expression> expressions = new ArrayList<>();
        Expression expression;
        for (JsoniqParser.ExprSingleContext expr : ctx.exprSingle())
        {
            this.visitExprSingle(expr);
            expression = this.currentExpression;
            expressions.add(expression);

        }
        node = new CommaExpression(expressions);
        this.currentExpression = node;
        if(getDepthLevel(ctx) == 3)
            queryExpression = node;
        return null;
    }

    @Override public Void visitExprSingle(JsoniqParser.ExprSingleContext ctx) {

        Expression node;
        ParseTree content = ctx.children.get(0);
        if(content instanceof JsoniqParser.OrExprContext)
            this.visitOrExpr((JsoniqParser.OrExprContext)content);
        else if(content instanceof JsoniqParser.FlowrExprContext)
            this.visitFlowrExpr((JsoniqParser.FlowrExprContext)content);
        else if(content instanceof JsoniqParser.IfExprContext)
            this.visitIfExpr((JsoniqParser.IfExprContext)content);
        else if(content instanceof JsoniqParser.QuantifiedExprContext)
            this.visitQuantifiedExpr((JsoniqParser.QuantifiedExprContext)content);
        node = this.currentExpression;
        this.currentExpression = node;
        return null;
    }
    //endregion expr

    //region Flowr
    //TODO [EXPRVISITOR] count
    @Override public Void visitFlowrExpr(JsoniqParser.FlowrExprContext ctx) {
        FlworExpression node;
        FlworClause startClause, childClause;
        List<FlworClause> contentClauses = new ArrayList<>();
        ReturnClause returnClause;
        //check the start clause, for or let
        if(ctx.start_for == null) {
            this.visitLetClause(ctx.start_let);
            startClause = this.currentFlworClause;
        } else {
            this.visitForClause(ctx.start_for);
            startClause =  this.currentFlworClause;
        }

        //exclude return + returnExpr
        for(ParseTree child : ctx.children.subList(1,ctx.children.size() -2))
        {
            if(child instanceof JsoniqParser.ForClauseContext) {
                this.visitForClause((JsoniqParser.ForClauseContext)child);
                childClause = this.currentFlworClause;
            }
            else  if(child instanceof JsoniqParser.LetClauseContext) {
                this.visitLetClause((JsoniqParser.LetClauseContext)child);
                childClause = this.currentFlworClause;
            }
            else  if(child instanceof JsoniqParser.WhereClauseContext) {
                this.visitWhereClause((JsoniqParser.WhereClauseContext)child);
                childClause = this.currentFlworClause;
            }
            else  if(child instanceof JsoniqParser.GroupByClauseContext) {
                this.visitGroupByClause((JsoniqParser.GroupByClauseContext)child);
                childClause = this.currentFlworClause;
            }
            else  if(child instanceof JsoniqParser.OrderByClauseContext) {
                this.visitOrderByClause((JsoniqParser.OrderByClauseContext)child);
                childClause = this.currentFlworClause;
            }
            else
                throw new UnsupportedFeatureException("FLOWR clause not implemented yet");

            contentClauses.add(childClause);
        }

        //visit return
        this.visitExprSingle(ctx.return_Expr);
        Expression returnExpr = this.currentExpression;
        returnClause = new ReturnClause(returnExpr);

        node = new FlworExpression(startClause, contentClauses, returnClause);
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitForClause(JsoniqParser.ForClauseContext ctx) {
        ForClause node;
        List<ForClauseVar> vars = new ArrayList<>();
        ForClauseVar child;
        for(JsoniqParser.ForVarContext var: ctx.vars){
            this.visitForVar(var);
            child = (ForClauseVar)this.currentFlworClause;
            vars.add(child);
        }

        node = new ForClause(vars);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitForVar(JsoniqParser.ForVarContext ctx) {
        VariableReference var, atVarRef = null;
        FlworVarSequenceType seq = null;
        Expression expr;
        boolean emptyFlag;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference)this.currentPrimaryExpression;
        if(ctx.seq !=null){
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType)this.currentExpression;
        }
        emptyFlag = (ctx.flag != null);
        if(ctx.at !=null){
            this.visitVarRef(ctx.at);
            atVarRef = (VariableReference)this.currentPrimaryExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        ForClauseVar node = new ForClauseVar(var,seq,emptyFlag,atVarRef,expr);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitLetClause(JsoniqParser.LetClauseContext ctx) {
        LetClause node;
        List<LetClauseVar> vars = new ArrayList<>();
        LetClauseVar child;
        for(JsoniqParser.LetVarContext var: ctx.vars){
            this.visitLetVar(var);
            child = (LetClauseVar)this.currentFlworClause;
            vars.add(child);
        }

        node = new LetClause(vars);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitLetVar(JsoniqParser.LetVarContext ctx) {
        VariableReference var;
        FlworVarSequenceType seq = null;
        Expression expr;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference)this.currentPrimaryExpression;
        if(ctx.seq !=null){
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType)this.currentExpression;
        }
        this.visitExprSingle(ctx.ex);
        expr = this.currentExpression;

        LetClauseVar node = new LetClauseVar(var,seq,expr);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitGroupByClause(JsoniqParser.GroupByClauseContext ctx) {
        GroupByClause node;
        List<GroupByClauseVar> vars = new ArrayList<>();
        GroupByClauseVar child;
        for(JsoniqParser.GroupByVarContext var: ctx.vars){
            this.visitGroupByVar(var);
            child = (GroupByClauseVar)this.currentFlworClause;
            vars.add(child);
        }
        node = new GroupByClause(vars);
        this.currentFlworClause = node;
        return null;

    }

     @Override public Void visitOrderByClause(JsoniqParser.OrderByClauseContext ctx) {
        OrderByClause node;
        boolean stable = false;
        List<OrderByClauseExpr> exprs = new ArrayList<>();
         OrderByClauseExpr child;
        for(JsoniqParser.OrderByExprContext var: ctx.orderByExpr()){
            this.visitOrderByExpr(var);
            child = (OrderByClauseExpr)this.currentFlworClause;
            exprs.add(child);
        }
        if(ctx.stb!=null && !ctx.stb.getText().isEmpty())
            stable = true;
        node = new OrderByClause(exprs, stable);
        this.currentFlworClause = node;
        return null;

    }

    @Override public Void visitOrderByExpr(JsoniqParser.OrderByExprContext ctx){
        OrderByClauseExpr node;
        boolean ascending = true;
        if(ctx.desc != null && !ctx.desc.getText().isEmpty())
            ascending = false;
        String uri = null;
        if(ctx.uriLiteral() != null)
            uri = ctx.uriLiteral().getText();
        OrderByClauseExpr.EMPTY_ORDER empty_order = OrderByClauseExpr.EMPTY_ORDER.NONE;
        if(ctx.gr != null && !ctx.gr.getText().isEmpty())
            empty_order = OrderByClauseExpr.EMPTY_ORDER.LAST;
        if(ctx.ls != null && !ctx.ls.getText().isEmpty())
            empty_order = OrderByClauseExpr.EMPTY_ORDER.FIRST;
        this.visitExprSingle(ctx.exprSingle());
        Expression expression = this.currentExpression;
        node = new OrderByClauseExpr(expression,ascending, uri, empty_order);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitGroupByVar(JsoniqParser.GroupByVarContext ctx) {
        VariableReference var;
        FlworVarSequenceType seq = null;
        Expression expr = null;
        String uri = null;
        this.visitVarRef(ctx.var_ref);
        var = (VariableReference)this.currentPrimaryExpression;

        if(ctx.seq !=null){
            this.visitSequenceType(ctx.seq);
            seq = (FlworVarSequenceType)this.currentExpression;
        }

        if(ctx.ex !=null){
            this.visitExprSingle(ctx.ex);
            expr = this.currentExpression;
        }

        if(ctx.uri!=null)
            uri = ctx.uri.getText();

        GroupByClauseVar node = new GroupByClauseVar(var,seq,expr, uri);
        this.currentFlworClause = node;
        return null;
    }

    @Override public Void visitWhereClause(JsoniqParser.WhereClauseContext ctx) {
        WhereClause node;
        Expression expr;
        this.visitExprSingle(ctx.exprSingle());
        expr = this.currentExpression;
        node = new WhereClause(expr);
        this.currentFlworClause = node;
        return null; }
    //endregion

    //region operational
    @Override public Void visitOrExpr(JsoniqParser.OrExprContext ctx) {
        AndExpression mainExpression, childExpression;
        OrExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitAndExpr(ctx.mainExpr);
        mainExpression = (AndExpression)this.currentExpression;
        if(!(ctx.rhs ==null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.AndExprContext child : ctx.rhs) {
                this.visitAndExpr(child);
                childExpression = (AndExpression)this.currentExpression;
                rhs.add(childExpression);
            }
             node = new OrExpression(mainExpression, rhs);
        }else{
             node = new OrExpression(mainExpression);
        }
       this.currentExpression = node;
        return null;

    }

    @Override public Void visitAndExpr(JsoniqParser.AndExprContext ctx) {
        NotExpression mainExpression, childExpression;
        AndExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitNotExpr(ctx.mainExpr);
        mainExpression = (NotExpression)this.currentExpression;
        if(!(ctx.rhs ==null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.NotExprContext child : ctx.rhs) {
                this.visitNotExpr(child);
                childExpression = (NotExpression)this.currentExpression;
                rhs.add(childExpression);
            }
            node = new AndExpression(mainExpression, rhs);
        }else{
            node = new AndExpression(mainExpression);
        }
       this.currentExpression = node;
        return null;
    }

    @Override public Void visitNotExpr(JsoniqParser.NotExprContext ctx) {
        ComparisonExpression mainExpression;
        NotExpression node;
        this.visitComparisonExpr(ctx.mainExpr);
        mainExpression = (ComparisonExpression)this.currentExpression;
        node = new NotExpression(mainExpression, !(ctx.op == null || ctx.op.isEmpty()));
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitComparisonExpr(JsoniqParser.ComparisonExprContext ctx) {
        StringConcatExpression mainExpression, childExpression;
        ComparisonExpression node;
        this.visitStringConcatExpr(ctx.mainExpr);
        mainExpression = (StringConcatExpression)this.currentExpression;
        if(ctx.rhs !=null && !ctx.rhs.isEmpty()) {
            JsoniqParser.StringConcatExprContext child = ctx.rhs.get(0);
                this.visitStringConcatExpr(child);
                childExpression = (StringConcatExpression)this.currentExpression;

            node = new ComparisonExpression(mainExpression, childExpression,
                    OperationalExpressionBase.getOperatorFromString(ctx.op.get(0).getText()));
        }else{
            node = new ComparisonExpression(mainExpression);
        }
       this.currentExpression = node;
        return null;
    }

    @Override public Void visitStringConcatExpr(JsoniqParser.StringConcatExprContext ctx){
        RangeExpression mainExpression, childExpression;
        StringConcatExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitRangeExpr(ctx.mainExpr);
        mainExpression = (RangeExpression) this.currentExpression;
        if(!(ctx.rhs ==null) && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.RangeExprContext child : ctx.rhs) {
                this.visitRangeExpr(child);
                childExpression = (RangeExpression)this.currentExpression;
                rhs.add(childExpression);
            }
            node = new StringConcatExpression(mainExpression, rhs);
        }else{
            node = new StringConcatExpression(mainExpression);
        }
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitRangeExpr(JsoniqParser.RangeExprContext ctx) {

        AdditiveExpression mainExpression, childExpression;
        RangeExpression node;
        this.visitAdditiveExpr(ctx.mainExpr);
        mainExpression = (AdditiveExpression)this.currentExpression;
        if(ctx.rhs !=null && !ctx.rhs.isEmpty()) {
            JsoniqParser.AdditiveExprContext child = ctx.rhs.get(0);
            this.visitAdditiveExpr(child);
            childExpression = (AdditiveExpression)this.currentExpression;
            node = new RangeExpression(mainExpression, childExpression);
        }else{
            node = new RangeExpression(mainExpression);
        }

        this.currentExpression = node;
        return null;
    }

    @Override public Void visitAdditiveExpr(JsoniqParser.AdditiveExprContext ctx) {
        MultiplicativeExpression mainExpression, childExpression;
        AdditiveExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitMultiplicativeExpr(ctx.mainExpr);
        mainExpression = (MultiplicativeExpression)this.currentExpression;
        if(ctx.rhs !=null && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.MultiplicativeExprContext child : ctx.rhs) {
                this.visitMultiplicativeExpr(child);
                childExpression = (MultiplicativeExpression)this.currentExpression;
                rhs.add(childExpression);
            }
            node = new AdditiveExpression(mainExpression, rhs,
                    OperationalExpressionBase.getOperatorFromOpList( ctx.op));
        }else{
            node = new AdditiveExpression(mainExpression);
        }
       this.currentExpression = node;
        return null;
    }

    @Override public Void visitMultiplicativeExpr(JsoniqParser.MultiplicativeExprContext ctx) {
        InstanceOfExpression mainExpression, childExpression;
        MultiplicativeExpression node;
        List<Expression> rhs = new ArrayList<>();
        this.visitInstanceOfExpr(ctx.mainExpr);
        mainExpression = (InstanceOfExpression)this.currentExpression;
        if(ctx.rhs !=null && !ctx.rhs.isEmpty()) {
            for (JsoniqParser.InstanceOfExprContext child : ctx.rhs) {
                this.visitInstanceOfExpr(child);
                childExpression = (InstanceOfExpression)this.currentExpression;
                rhs.add(childExpression);
            }
            node = new MultiplicativeExpression(mainExpression, rhs,
                    OperationalExpressionBase.getOperatorFromOpList( ctx.op));
        }else{
            node = new MultiplicativeExpression(mainExpression);
        }
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitInstanceOfExpr(JsoniqParser.InstanceOfExprContext ctx){
        UnaryExpression mainExpression;
        FlworVarSequenceType sequenceType;
        InstanceOfExpression node;
        this.visitTreatExpr(ctx.mainExpr);
        mainExpression = (UnaryExpression)this.currentExpression;
        if(ctx.seq !=null && !ctx.seq.isEmpty()) {
            JsoniqParser.SequenceTypeContext child = ctx.seq;
            this.visitSequenceType(child);
            sequenceType = (FlworVarSequenceType) this.currentExpression;
            node = new InstanceOfExpression(mainExpression, sequenceType);
        }else{
            node = new InstanceOfExpression(mainExpression);
        }
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitUnaryExpr(JsoniqParser.UnaryExprContext ctx) {
        //TODO [EXPRVISITOR] jump from unary to postfix
        PostFixExpression mainExpression;
        UnaryExpression node;
        this.visitSimpleMapExpr(ctx.mainExpr);
        mainExpression = (PostFixExpression)this.currentExpression;
        if(ctx.op == null || ctx.op.isEmpty())
            node = new UnaryExpression(mainExpression);
        else
            node = new UnaryExpression(mainExpression,
                    OperationalExpressionBase.getOperatorFromOpList(ctx.op));
       this.currentExpression = node;
        return null;
    }
    //endregion

    //region postfix
    @Override public Void visitPostFixExpr(JsoniqParser.PostFixExprContext ctx) {
        PostfixExtension childExpression = null;
        PrimaryExpression mainExpression;
        PostFixExpression node;
        List<PostfixExtension> rhs = new ArrayList<>();
        this.visitPrimaryExpr(ctx.mainExpr);
        mainExpression = this.currentPrimaryExpression;
        for(ParseTree child : ctx.children.subList(1, ctx.children.size())) {
            if(child instanceof JsoniqParser.PredicateContext){
                this.visitPredicate((JsoniqParser.PredicateContext)child);
                childExpression = this.currentPostFixExtension;
            }
            else if(child instanceof JsoniqParser.ObjectLookupContext) {
                this.visitObjectLookup((JsoniqParser.ObjectLookupContext)child);
                childExpression = this.currentPostFixExtension;
            }
            else if(child instanceof JsoniqParser.ArrayLookupContext) {
                this.visitArrayLookup((JsoniqParser.ArrayLookupContext)child);
                childExpression = this.currentPostFixExtension;
            }

            else if(child instanceof JsoniqParser.ArrayUnboxingContext) {
                this.visitArrayUnboxing((JsoniqParser.ArrayUnboxingContext) child);
                childExpression = this.currentPostFixExtension;
            }
            rhs.add(childExpression);
        }
        node = new PostFixExpression(mainExpression, rhs);
        rhs.forEach(e -> e.setParent(node));
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitPredicate(JsoniqParser.PredicateContext ctx) {
        PredicateExtension node;
        CommaExpression content;
        this.visitExpr(ctx.expr());
        content = (CommaExpression)this.currentExpression;
        node = new PredicateExtension(content);
        this.currentPostFixExtension = node;
        return null;
    }

    @Override public Void visitObjectLookup(JsoniqParser.ObjectLookupContext ctx) {
        //TODO [EXPRVISITOR] support for ParenthesizedExpr | varRef | contextItemexpr in object lookup
        ObjectLookupExtension node;
        StringLiteral literal = null;
        if(ctx.lt != null)
            literal = new StringLiteral(ValueTypeHandler.
                getStringValue(ctx.lt));
        else if(ctx.nc != null)
            literal = new StringLiteral(ctx.nc.getText());
        else if(ctx.kw != null)
            literal = new StringLiteral(ctx.kw.getText());
        node = new ObjectLookupExtension(literal);
        this.currentPostFixExtension = node;
        return null;
    }

    @Override public Void visitArrayLookup(JsoniqParser.ArrayLookupContext ctx) {
        ArrayLookupExtension node;
        CommaExpression content;
        this.visitExpr(ctx.expr());
        content = (CommaExpression)this.currentExpression;
        node = new ArrayLookupExtension(content);
        this.currentPostFixExtension = node;
        return null;
    }

    @Override public Void visitArrayUnboxing(JsoniqParser.ArrayUnboxingContext ctx) {
        this.currentPostFixExtension = new ArrayUnboxingExtension();
        return null;
    }
    //endregion

    //region primary
    //TODO [EXPRVISITOR] orderedExpr unorderedExpr;
    @Override public Void visitPrimaryExpr(JsoniqParser.PrimaryExprContext ctx) {
        PrimaryExpression node = null;

        for(ParseTree child : ctx.children) {
            if(child instanceof JsoniqParser.VarRefContext){
                this.visitVarRef((JsoniqParser.VarRefContext)child);
                node = this.currentPrimaryExpression;
            }
            else if(child instanceof JsoniqParser.ObjectConstructorContext) {
                this.visitObjectConstructor((JsoniqParser.ObjectConstructorContext)child);
                node = this.currentPrimaryExpression;
            }
            else if(child instanceof JsoniqParser.ArrayConstructorContext) {
                this.visitArrayConstructor((JsoniqParser.ArrayConstructorContext)child);
                node = this.currentPrimaryExpression;
            }
            else if(child instanceof JsoniqParser.ParenthesizedExprContext) {
                this.visitParenthesizedExpr((JsoniqParser.ParenthesizedExprContext)child);
                node = this.currentPrimaryExpression;
            }
            else if(child instanceof JsoniqParser.StringLiteralContext) {
                node = new StringLiteral(ValueTypeHandler.getStringValue((JsoniqParser.StringLiteralContext) child));
            }
            else if(child instanceof TerminalNode) {
                node = ValueTypeHandler.getValueType(child.getText());
            }
            else if(child instanceof JsoniqParser.ContextItemExprContext) {
                this.visitContextItemExpr((JsoniqParser.ContextItemExprContext)child);
                node = this.currentPrimaryExpression;
            }
            else if(child instanceof JsoniqParser.FunctionCallContext) {
                this.visitFunctionCall((JsoniqParser.FunctionCallContext)child);
                node = this.currentPrimaryExpression;
            }
            else
                throw new UnsupportedFeatureException("Primary expression not yet implemented");
        }

        this.currentPrimaryExpression = node;
        return null;

    }

    @Override public Void visitObjectConstructor(JsoniqParser.ObjectConstructorContext ctx) {
        ObjectConstructor node;

        //no merging constructor, just visit the k/v pairs
        if(ctx.mergeOperator == null || ctx.mergeOperator.size() == 0 ||
                ctx.mergeOperator.get(0).getText().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            ObjectConstructor.PairConstructor pair;
            for (JsoniqParser.PairConstructorContext currentPair : ctx.pairConstructor()) {
                this.visitPairConstructor(currentPair);
                pair = (ObjectConstructor.PairConstructor)this.currentPrimaryExpression;
                keys.add(pair.get_key());
                values.add(pair.get_value());
            }
            node = new ObjectConstructor(keys, values);
        } else {
            Expression childExpr;
            this.visitExpr(ctx.expr());
            childExpr = this.currentExpression;
            node = new ObjectConstructor((CommaExpression) childExpr);
        }

        this.currentPrimaryExpression = node;
        return null;
    }

    //TODO[EXPRVISITOR]? not supported in Pair constructor
    @Override public Void visitPairConstructor(JsoniqParser.PairConstructorContext ctx) {
        ObjectConstructor.PairConstructor node;
        Expression rhs,lhs;
        this.visitExprSingle(ctx.rhs);
        rhs = this.currentExpression;
        this.visitExprSingle(ctx.lhs);
        lhs = this.currentExpression;
        node = new ObjectConstructor.PairConstructor(lhs, rhs);
        this.currentPrimaryExpression = node;
        return null;

    }

    @Override public Void visitArrayConstructor(JsoniqParser.ArrayConstructorContext ctx) {
        ArrayConstructor node;
        CommaExpression content;
        if(ctx.expr() == null)
            node = new ArrayConstructor();
        else {
            this.visitExpr(ctx.expr());
            content = (CommaExpression)this.currentExpression;
            node = new ArrayConstructor(content);
        }
        this.currentPrimaryExpression = node;
        return null;

    }

    @Override public Void visitParenthesizedExpr(JsoniqParser.ParenthesizedExprContext ctx){
        ParenthesizedExpression node;
        CommaExpression content;
        if(ctx.expr() == null)
            node = new ParenthesizedExpression();
        else {
            this.visitExpr(ctx.expr());
            content = (CommaExpression)this.currentExpression;
            node = new ParenthesizedExpression(content);
        }
        this.currentPrimaryExpression = node;
        return null;
    }

    @Override public Void visitVarRef(JsoniqParser.VarRefContext ctx){
        VariableReference node;
        String name = ctx.name.getText();
        if(ctx.ns !=null)
            name = name + ":" + ctx.ns.getText();
        node = new VariableReference(name);
        this.currentPrimaryExpression = node;
        return null;
    }

    @Override public Void visitContextItemExpr(JsoniqParser.ContextItemExprContext ctx) {
        this.currentPrimaryExpression = new ContextExpression();
        return null;
    }

    //endregion

    @Override public Void visitSequenceType(JsoniqParser.SequenceTypeContext ctx){
        FlworVarSequenceType node;
        if(ctx.item == null)
            node = new FlworVarSequenceType();
        else {
            ItemTypes item = FlworVarSequenceType.getItemType(ctx.item.getText());
            if(ctx.question.size() >= 0 )
                node = new FlworVarSequenceType(item, SequenceType.Arity.OneOrZero);
            else if(ctx.star.size() >= 0 )
                node = new FlworVarSequenceType(item, SequenceType.Arity.ZeroOrMore);
            else if(ctx.plus.size() >= 0 )
                node = new FlworVarSequenceType(item, SequenceType.Arity.OneOrMore);
            else
                node = new FlworVarSequenceType(item);
        }
        this.currentExpression = node;
        return null;
    }

    //region new features
    @Override public Void visitFunctionCall(JsoniqParser.FunctionCallContext ctx){
        FunctionCall node;
        String name;
        if(ctx.fcnName!=null)
            name = ctx.fcnName.getText();
        else
            name = ctx.kw.getText();
        if(ctx.ns !=null)
            name = name + ":" + ctx.ns.getText();
        List<Expression> parameters = new ArrayList<>();
        if(ctx.argumentList().args !=null)
            for(JsoniqParser.ArgumentContext arg :ctx.argumentList().args){
                this.visitArgument(arg);
                Expression currentArg = this.currentExpression;
                parameters.add(currentArg);
            }
        node = new FunctionCall(name, parameters);
        this.currentPrimaryExpression = node;
        return null;
    }

    @Override public Void visitArgument(JsoniqParser.ArgumentContext ctx){
        this.visitExprSingle(ctx.exprSingle());
        return null;
    }
    //endregion

    @Override public Void visitIfExpr(JsoniqParser.IfExprContext ctx){
        IfExpression node;
        Expression condition, branch, elseBranch;
        this.visitExpr(ctx.condition);
        condition = this.currentExpression;
        this.visitExprSingle(ctx.branch);
        branch = this.currentExpression;
        this.visitExprSingle(ctx.elseBranch);
        elseBranch = this.currentExpression;
        node = new IfExpression(condition,branch,elseBranch);
        this.currentExpression = node;
        return null;
    }

    @Override public Void visitQuantifiedExpr(JsoniqParser.QuantifiedExprContext ctx){
        List<QuantifiedExpressionVar> vars = new ArrayList<>();
        QuantifiedExpression.QuantifiedOperators operator;
        Expression expression;
        this.visitExprSingle(ctx.exprSingle());

        expression = this.currentExpression;
        if(ctx.ev == null)
            operator = QuantifiedExpression.QuantifiedOperators.SOME;
        else
            operator = QuantifiedExpression.QuantifiedOperators.EVERY;
        for(JsoniqParser.QuantifiedExprVarContext currentVariable : ctx.vars) {
            VariableReference varRef;
            Expression varExpression;
            FlworVarSequenceType sequenceType = null;
            this.visitVarRef(currentVariable.varRef());
            varRef = (VariableReference) this.currentPrimaryExpression;
            if(currentVariable.sequenceType() != null){
                this.visitSequenceType(currentVariable.sequenceType());
                sequenceType = (FlworVarSequenceType) this.currentExpression;
            }

            this.visitExprSingle(currentVariable.exprSingle());
            varExpression = this.currentExpression;
            vars.add(new QuantifiedExpressionVar(varRef, varExpression,
                    sequenceType == null? null :sequenceType.getSequence()));
        }
        this.currentExpression = new QuantifiedExpression(operator, expression, vars);
        return null;
    }






}



