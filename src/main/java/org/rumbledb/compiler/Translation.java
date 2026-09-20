/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.compiler.context.AdditiveExprContext;
import org.rumbledb.compiler.context.AndExprContext;
import org.rumbledb.compiler.context.ComparisonExprContext;
import org.rumbledb.compiler.context.IfExprContext;
import org.rumbledb.compiler.context.IntersectExceptExprContext;
import org.rumbledb.compiler.context.MultiplicativeExprContext;
import org.rumbledb.compiler.context.OrExprContext;
import org.rumbledb.compiler.context.RangeExprContext;
import org.rumbledb.compiler.context.SimpleMapExprContext;
import org.rumbledb.compiler.context.SingleTypeCheckExprContext;
import org.rumbledb.compiler.context.StringConcatExprContext;
import org.rumbledb.compiler.context.SwitchExprContext;
import org.rumbledb.compiler.context.TypeCheckExprContext;
import org.rumbledb.compiler.context.UnaryExprContext;
import org.rumbledb.compiler.context.UnionExprContext;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

/**
 * Shared translation methods used by both JSONiq and XQuery translation visitors.
 */
public final class Translation {

    private Translation() {}

    public static <T extends ParserRuleContext> Expression orExpr(
            OrExprContext<T> ctx, TranslationContext translationContext, Function<T, Node> visitAndExpr) {
        Expression result = (Expression) visitAndExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (T child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitAndExpr.apply(child);
            result = new OrExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression andExpr(
            AndExprContext<T> ctx, TranslationContext translationContext, Function<T, Node> visitNextExpr) {
        Expression result = (Expression) visitNextExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (T child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitNextExpr.apply(child);
            result = new AndExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression comparisonExpr(
            ComparisonExprContext<T> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitStringConcatExpr) {
        Expression mainExpression = (Expression) visitStringConcatExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return mainExpression;
        }
        T child = ctx.rhs().get(0);
        Expression childExpression = (Expression) visitStringConcatExpr.apply(child);

        if (ctx.isNodeComp()) {
            NodeComparisonExpression.NodeComparisonOperator nodeOp =
                    NodeComparisonExpression.NodeComparisonOperator.fromSymbol(ctx.operatorSymbol());
            return new NodeComparisonExpression(
                    mainExpression, childExpression, nodeOp, translationContext.metadata(ctx.context()));
        }

        ComparisonExpression.ComparisonOperator kind =
                ComparisonExpression.ComparisonOperator.fromSymbol(ctx.operatorSymbol());
        if (kind.isValueComparison()
                || translationContext.configuration().optimization().optimizeGeneralComparisonToValueComparison()) {
            return new ComparisonExpression(
                    mainExpression, childExpression, kind, translationContext.metadata(ctx.context()));
        }

        Name variableNameLeft = Name.TEMP_VAR1;
        Name variableNameRight = Name.TEMP_VAR2;

        Clause firstClause = new ForClause(
                variableNameLeft, false, null, null, mainExpression, translationContext.metadata(ctx.context()));
        Clause secondClause = new ForClause(
                variableNameRight, false, null, null, childExpression, translationContext.metadata(ctx.context()));
        firstClause.chainWith(secondClause);
        Expression valueComparison = new ComparisonExpression(
                new VariableReferenceExpression(variableNameLeft, translationContext.metadata(ctx.context())),
                new VariableReferenceExpression(variableNameRight, translationContext.metadata(ctx.context())),
                kind.getCorrespondingValueComparison(),
                translationContext.metadata(ctx.context()));
        WhereClause whereClause = new WhereClause(valueComparison, translationContext.metadata(ctx.context()));
        secondClause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new StringLiteralExpression("", translationContext.metadata(ctx.context())),
                translationContext.metadata(ctx.context()));
        whereClause.chainWith(returnClause);
        Expression flworExpression = new FlworExpression(returnClause, translationContext.metadata(ctx.context()));
        return new FunctionCallExpression(
                Name.createVariableInDefaultFunctionNamespace("exists"),
                Collections.singletonList(flworExpression),
                translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext> Expression stringConcatExpr(
            StringConcatExprContext<T> ctx, TranslationContext translationContext, Function<T, Node> visitRangeExpr) {
        Expression result = (Expression) visitRangeExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (T child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitRangeExpr.apply(child);
            result = new StringConcatExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression rangeExpr(
            RangeExprContext<T> ctx, TranslationContext translationContext, Function<T, Node> visitAdditiveExpr) {
        Expression mainExpression = (Expression) visitAdditiveExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return mainExpression;
        }
        T child = ctx.rhs().get(0);
        Expression childExpression = (Expression) visitAdditiveExpr.apply(child);
        return new RangeExpression(mainExpression, childExpression, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext> Expression additiveExpr(
            AdditiveExprContext<T> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitMultiplicativeExpr) {
        Expression result = (Expression) visitMultiplicativeExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            T child = ctx.rhs().get(i);
            Expression rightExpression = (Expression) visitMultiplicativeExpr.apply(child);
            result = new AdditiveExpression(
                    result,
                    rightExpression,
                    ctx.op().get(i).getText().equals("-"),
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression multiplicativeExpr(
            MultiplicativeExprContext<T> ctx,
            TranslationContext translationContext,
            CommonTokenStream tokenStream,
            Function<T, Node> visitUnionExpr) {
        Expression result = (Expression) visitUnionExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            T child = ctx.rhs().get(i);
            Token operator = ctx.op().get(i);
            validateMultiplicativeOperator(ctx.mainExpr(), child, operator, translationContext, tokenStream);
            Expression rightExpression = (Expression) visitUnionExpr.apply(child);
            result = new MultiplicativeExpression(
                    result,
                    rightExpression,
                    MultiplicativeExpression.MultiplicativeOperator.fromSymbol(operator.getText()),
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static void validateMultiplicativeOperator(
            ParseTree leftExpression,
            ParseTree rightExpression,
            Token operator,
            TranslationContext translationContext,
            CommonTokenStream tokenStream) {
        String operatorText = operator.getText();
        if (operatorText.equals("*")
                && (leftExpression.getText().equals("/")
                        || leftExpression.getText().equals("//"))) {
            throw new ParsingException(
                    "Missing path step after leading slash.",
                    translationContext.metadata(translationContext.startToken(leftExpression), operator));
        }
        if (!operatorText.equals("div") && !operatorText.equals("idiv") && !operatorText.equals("mod")) {
            return;
        }
        if (DirectConstructorUtils.getHiddenTextAfter(
                                tokenStream,
                                translationContext.stopToken(leftExpression).getTokenIndex())
                        .isEmpty()
                && !hasKeywordOperatorBoundary(
                        translationContext.stopToken(leftExpression).getText(), false)) {
            throw new ParsingException(
                    "Keyword operator '" + operatorText + "' must be separated from the left operand.",
                    translationContext.metadata(translationContext.startToken(leftExpression), operator));
        }
        if (DirectConstructorUtils.getHiddenTextAfter(tokenStream, operator.getTokenIndex())
                        .isEmpty()
                && !hasKeywordOperatorBoundary(
                        translationContext.startToken(rightExpression).getText(), true)) {
            throw new ParsingException(
                    "Keyword operator '" + operatorText + "' must be separated from the right operand.",
                    translationContext.metadata(operator, translationContext.startToken(rightExpression)));
        }
    }

    public static boolean hasKeywordOperatorBoundary(String tokenText, boolean checkStart) {
        if (tokenText == null || tokenText.isEmpty()) {
            return false;
        }
        char boundaryCharacter = checkStart ? tokenText.charAt(0) : tokenText.charAt(tokenText.length() - 1);
        return !isPotentialKeywordOperandCharacter(boundaryCharacter);
    }

    public static boolean isPotentialKeywordOperandCharacter(char character) {
        return Character.isLetterOrDigit(character)
                || character == '_'
                || character == '-'
                || character == '.'
                || character == ':';
    }

    public static <T extends ParserRuleContext> Expression unionExpr(
            UnionExprContext<T> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitIntersectExceptExpr) {
        Expression result = (Expression) visitIntersectExceptExpr.apply(ctx.mainExpr());
        for (T child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitIntersectExceptExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.UNION,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression intersectExceptExpr(
            IntersectExceptExprContext<T> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitInstanceOfExpr) {
        Expression result = (Expression) visitInstanceOfExpr.apply(ctx.mainExpr());
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            T child = ctx.rhs().get(i);
            Expression rightExpression = (Expression) visitInstanceOfExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.fromSymbol(ctx.op().get(i).getText()),
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext, M extends ParserRuleContext> Expression simpleMapExpr(
            SimpleMapExprContext<T, M> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitPathExprForMain,
            Function<M, Node> visitPathExprForMap) {
        Expression result = (Expression) visitPathExprForMain.apply(ctx.mainExpr());
        if (ctx.mapExpr() == null || ctx.mapExpr().isEmpty()) {
            return result;
        }
        for (M child : ctx.mapExpr()) {
            Expression rightExpression = (Expression) visitPathExprForMap.apply(child);
            result = new SimpleMapExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression instanceOfExpr(
            TypeCheckExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitIsStaticallyExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitIsStaticallyExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new InstanceOfExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression isStaticallyExpr(
            TypeCheckExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitTreatExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitTreatExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new IsStaticallyExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression treatExpr(
            TypeCheckExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitCastableExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitCastableExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new TreatExpression(
                mainExpression,
                sequenceType,
                ErrorCode.DynamicTypeTreatErrorCode,
                translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression castableExpr(
            SingleTypeCheckExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitCastExpr,
            Function<S, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitCastExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastableExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression castExpr(
            SingleTypeCheckExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitArrowExpr,
            Function<S, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitArrowExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext> Expression unaryExpr(
            UnaryExprContext<T> ctx, TranslationContext translationContext, Function<T, Node> visitValueExpr) {
        Expression mainExpression = (Expression) visitValueExpr.apply(ctx.mainExpr());
        if (ctx.op() == null || ctx.op().isEmpty()) {
            return mainExpression;
        }
        boolean negated = false;
        for (Token t : ctx.op()) {
            if (t.getText().contentEquals("-")) {
                negated = !negated;
            }
        }
        return new UnaryExpression(mainExpression, negated, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression ifExpr(
            IfExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitExpr,
            Function<S, Node> visitExprSingle) {
        Expression condition = (Expression) visitExpr.apply(ctx.testCondition());
        Expression branch = (Expression) visitExprSingle.apply(ctx.branch());
        Expression elseBranch = (Expression) visitExprSingle.apply(ctx.elseBranch());
        return new ConditionalExpression(condition, branch, elseBranch, translationContext.metadata(ctx.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression switchExpr(
            SwitchExprContext<T, S> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitExpr,
            Function<S, Node> visitExprSingle) {
        Expression condition = (Expression) visitExpr.apply(ctx.cond());
        List<SwitchCase> cases = new ArrayList<>(ctx.cases().size());
        for (SwitchExprContext.Case<S> caseClause : ctx.cases()) {
            List<Expression> conditionExpressions =
                    new ArrayList<>(caseClause.cond().size());
            for (S expr : caseClause.cond()) {
                conditionExpressions.add((Expression) visitExprSingle.apply(expr));
            }
            SwitchCase c = new SwitchCase(conditionExpressions, (Expression) visitExprSingle.apply(caseClause.ret()));
            cases.add(c);
        }
        Expression defaultCase = (Expression) visitExprSingle.apply(ctx.def());
        return new SwitchExpression(condition, cases, defaultCase, translationContext.metadata(ctx.context()));
    }
}
