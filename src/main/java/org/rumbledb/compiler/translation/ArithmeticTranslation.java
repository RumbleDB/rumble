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
package org.rumbledb.compiler.translation;

import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.compiler.context.AdditiveExprContext;
import org.rumbledb.compiler.context.MultiplicativeExprContext;
import org.rumbledb.compiler.context.UnaryExprContext;
import org.rumbledb.compiler.utils.TokenStreamUtils;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;

public final class ArithmeticTranslation {

    private ArithmeticTranslation() {}

    public static <ChildExprCtx extends ParserRuleContext> Expression additiveExpr(
            AdditiveExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Expression> visitMultiplicativeExpr) {
        Expression result = visitMultiplicativeExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            ChildExprCtx child = ctx.rhs().get(i);
            Expression rightExpression = visitMultiplicativeExpr.apply(child);
            result = new AdditiveExpression(
                    result,
                    rightExpression,
                    ctx.op().get(i).getText().equals("-"),
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <ChildExprCtx extends ParserRuleContext> Expression multiplicativeExpr(
            MultiplicativeExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            CommonTokenStream tokenStream,
            Function<ChildExprCtx, Expression> visitUnionExpr) {
        Expression result = visitUnionExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            ChildExprCtx child = ctx.rhs().get(i);
            Token operator = ctx.op().get(i);
            validateMultiplicativeOperator(ctx.mainExpr(), child, operator, translationContext, tokenStream);
            Expression rightExpression = visitUnionExpr.apply(child);
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
        if (TokenStreamUtils.getHiddenTextAfter(
                                tokenStream,
                                translationContext.stopToken(leftExpression).getTokenIndex())
                        .isEmpty()
                && !hasKeywordOperatorBoundary(
                        translationContext.stopToken(leftExpression).getText(), false)) {
            throw new ParsingException(
                    "Keyword operator '" + operatorText + "' must be separated from the left operand.",
                    translationContext.metadata(translationContext.startToken(leftExpression), operator));
        }
        if (TokenStreamUtils.getHiddenTextAfter(tokenStream, operator.getTokenIndex())
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

    public static <ChildExprCtx extends ParserRuleContext> Expression unaryExpr(
            UnaryExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Expression> visitValueExpr) {
        Expression mainExpression = visitValueExpr.apply(ctx.mainExpr());
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
}
