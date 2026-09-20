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

import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.compiler.context.AdditiveExprContext;
import org.rumbledb.compiler.context.AndExprContext;
import org.rumbledb.compiler.context.IntersectExceptExprContext;
import org.rumbledb.compiler.context.MultiplicativeExprContext;
import org.rumbledb.compiler.context.OrExprContext;
import org.rumbledb.compiler.context.RangeExprContext;
import org.rumbledb.compiler.context.SimpleMapExprContext;
import org.rumbledb.compiler.context.SingleTypeCheckExprContext;
import org.rumbledb.compiler.context.StringConcatExprContext;
import org.rumbledb.compiler.context.TypeCheckExprContext;
import org.rumbledb.compiler.context.UnaryExprContext;
import org.rumbledb.compiler.context.UnionExprContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

/**
 * Shared translation logic used by both JSONiq and XQuery translation visitors.
 */
public final class SharedTranslationLogic {

    private SharedTranslationLogic() {}

    public static <T extends ParserRuleContext> Expression translateOrExpr(
            OrExprContext<T> view, TranslationContext translationContext, Function<T, Node> visitAndExpr) {
        Expression result = (Expression) visitAndExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return result;
        }
        for (T child : view.rhs()) {
            Expression rightExpression = (Expression) visitAndExpr.apply(child);
            result = new OrExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression translateAndExpr(
            AndExprContext<T> view, TranslationContext translationContext, Function<T, Node> visitNextExpr) {
        Expression result = (Expression) visitNextExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return result;
        }
        for (T child : view.rhs()) {
            Expression rightExpression = (Expression) visitNextExpr.apply(child);
            result = new AndExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression translateStringConcatExpr(
            StringConcatExprContext<T> view, TranslationContext translationContext, Function<T, Node> visitRangeExpr) {
        Expression result = (Expression) visitRangeExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return result;
        }
        for (T child : view.rhs()) {
            Expression rightExpression = (Expression) visitRangeExpr.apply(child);
            result = new StringConcatExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression translateRangeExpr(
            RangeExprContext<T> view, TranslationContext translationContext, Function<T, Node> visitAdditiveExpr) {
        Expression mainExpression = (Expression) visitAdditiveExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return mainExpression;
        }
        T child = view.rhs().get(0);
        Expression childExpression = (Expression) visitAdditiveExpr.apply(child);
        return new RangeExpression(mainExpression, childExpression, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext> Expression translateAdditiveExpr(
            AdditiveExprContext<T> view,
            TranslationContext translationContext,
            Function<T, Node> visitMultiplicativeExpr) {
        Expression result = (Expression) visitMultiplicativeExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < view.rhs().size(); ++i) {
            T child = view.rhs().get(i);
            Expression rightExpression = (Expression) visitMultiplicativeExpr.apply(child);
            result = new AdditiveExpression(
                    result,
                    rightExpression,
                    view.op().get(i).getText().equals("-"),
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression translateMultiplicativeExpr(
            MultiplicativeExprContext<T> view,
            TranslationContext translationContext,
            CommonTokenStream tokenStream,
            Function<T, Node> visitUnionExpr) {
        Expression result = (Expression) visitUnionExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < view.rhs().size(); ++i) {
            T child = view.rhs().get(i);
            Token operator = view.op().get(i);
            validateMultiplicativeOperator(view.mainExpr(), child, operator, translationContext, tokenStream);
            Expression rightExpression = (Expression) visitUnionExpr.apply(child);
            result = new MultiplicativeExpression(
                    result,
                    rightExpression,
                    MultiplicativeExpression.MultiplicativeOperator.fromSymbol(operator.getText()),
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
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

    public static <T extends ParserRuleContext> Expression translateUnionExpr(
            UnionExprContext<T> view,
            TranslationContext translationContext,
            Function<T, Node> visitIntersectExceptExpr) {
        Expression result = (Expression) visitIntersectExceptExpr.apply(view.mainExpr());
        for (T child : view.rhs()) {
            Expression rightExpression = (Expression) visitIntersectExceptExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.UNION,
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext> Expression translateIntersectExceptExpr(
            IntersectExceptExprContext<T> view,
            TranslationContext translationContext,
            Function<T, Node> visitInstanceOfExpr) {
        Expression result = (Expression) visitInstanceOfExpr.apply(view.mainExpr());
        for (int i = 0; i < view.rhs().size(); ++i) {
            T child = view.rhs().get(i);
            Expression rightExpression = (Expression) visitInstanceOfExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.fromSymbol(
                            view.op().get(i).getText()),
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext, M extends ParserRuleContext> Expression translateSimpleMapExpr(
            SimpleMapExprContext<T, M> view,
            TranslationContext translationContext,
            Function<T, Node> visitPathExprForMain,
            Function<M, Node> visitPathExprForMap) {
        Expression result = (Expression) visitPathExprForMain.apply(view.mainExpr());
        if (view.mapExpr() == null || view.mapExpr().isEmpty()) {
            return result;
        }
        for (M child : view.mapExpr()) {
            Expression rightExpression = (Expression) visitPathExprForMap.apply(child);
            result = new SimpleMapExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(view.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression translateInstanceOfExpr(
            TypeCheckExprContext<T, S> view,
            TranslationContext translationContext,
            Function<T, Node> visitIsStaticallyExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitIsStaticallyExpr.apply(view.mainExpr());
        if (view.seq() == null || view.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(view.seq());
        return new InstanceOfExpression(mainExpression, sequenceType, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression translateIsStaticallyExpr(
            TypeCheckExprContext<T, S> view,
            TranslationContext translationContext,
            Function<T, Node> visitTreatExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitTreatExpr.apply(view.mainExpr());
        if (view.seq() == null || view.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(view.seq());
        return new IsStaticallyExpression(mainExpression, sequenceType, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression translateTreatExpr(
            TypeCheckExprContext<T, S> view,
            TranslationContext translationContext,
            Function<T, Node> visitCastableExpr,
            Function<S, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitCastableExpr.apply(view.mainExpr());
        if (view.seq() == null || view.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(view.seq());
        return new TreatExpression(
                mainExpression,
                sequenceType,
                ErrorCode.DynamicTypeTreatErrorCode,
                translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression translateCastableExpr(
            SingleTypeCheckExprContext<T, S> view,
            TranslationContext translationContext,
            Function<T, Node> visitCastExpr,
            Function<S, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitCastExpr.apply(view.mainExpr());
        if (view.single() == null || view.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(view.single());
        return new CastableExpression(mainExpression, sequenceType, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext, S extends ParserRuleContext> Expression translateCastExpr(
            SingleTypeCheckExprContext<T, S> view,
            TranslationContext translationContext,
            Function<T, Node> visitArrowExpr,
            Function<S, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitArrowExpr.apply(view.mainExpr());
        if (view.single() == null || view.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(view.single());
        return new CastExpression(mainExpression, sequenceType, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext> Expression translateUnaryExpr(
            UnaryExprContext<T> view, TranslationContext translationContext, Function<T, Node> visitValueExpr) {
        Expression mainExpression = (Expression) visitValueExpr.apply(view.mainExpr());
        if (view.op() == null || view.op().isEmpty()) {
            return mainExpression;
        }
        boolean negated = false;
        for (Token t : view.op()) {
            if (t.getText().contentEquals("-")) {
                negated = !negated;
            }
        }
        return new UnaryExpression(mainExpression, negated, translationContext.metadata(view.context()));
    }
}
