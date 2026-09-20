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

import org.rumbledb.compiler.context.AndExprContext;
import org.rumbledb.compiler.context.OrExprContext;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.OrExpression;

public final class LogicTranslation {

    private LogicTranslation() {}

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
}
