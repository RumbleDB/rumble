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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.IntersectExceptExprContext;
import org.rumbledb.compiler.context.RangeExprContext;
import org.rumbledb.compiler.context.StringConcatExprContext;
import org.rumbledb.compiler.context.UnionExprContext;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;

public final class SequenceTranslation {

    private SequenceTranslation() {}

    public static <ChildExprCtx extends ParserRuleContext> Expression stringConcatExpr(
            StringConcatExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Node> visitRangeExpr) {
        Expression result = (Expression) visitRangeExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return result;
        }
        for (ChildExprCtx child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitRangeExpr.apply(child);
            result = new StringConcatExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <ChildExprCtx extends ParserRuleContext> Expression rangeExpr(
            RangeExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Node> visitAdditiveExpr) {
        Expression mainExpression = (Expression) visitAdditiveExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return mainExpression;
        }
        ChildExprCtx child = ctx.rhs().get(0);
        Expression childExpression = (Expression) visitAdditiveExpr.apply(child);
        return new RangeExpression(mainExpression, childExpression, translationContext.metadata(ctx.context()));
    }

    public static <ChildExprCtx extends ParserRuleContext> Expression unionExpr(
            UnionExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Node> visitIntersectExceptExpr) {
        Expression result = (Expression) visitIntersectExceptExpr.apply(ctx.mainExpr());
        for (ChildExprCtx child : ctx.rhs()) {
            Expression rightExpression = (Expression) visitIntersectExceptExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.UNION,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <ChildExprCtx extends ParserRuleContext> Expression intersectExceptExpr(
            IntersectExceptExprContext<ChildExprCtx> ctx,
            TranslationContext translationContext,
            Function<ChildExprCtx, Node> visitInstanceOfExpr) {
        Expression result = (Expression) visitInstanceOfExpr.apply(ctx.mainExpr());
        for (int i = 0; i < ctx.rhs().size(); ++i) {
            ChildExprCtx child = ctx.rhs().get(i);
            Expression rightExpression = (Expression) visitInstanceOfExpr.apply(child);
            result = new NodeSetExpression(
                    result,
                    rightExpression,
                    NodeSetExpression.NodeSetOperator.fromSymbol(ctx.op().get(i).getText()),
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }
}
