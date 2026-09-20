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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.view.AdditiveExprView;
import org.rumbledb.compiler.view.RangeExprView;
import org.rumbledb.compiler.view.StringConcatExprView;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;

/**
 * Shared translation logic used by both JSONiq and XQuery translation visitors.
 */
public final class SharedTranslationLogic {

    private SharedTranslationLogic() {}

    public static <T extends ParserRuleContext> Expression translateStringConcatExpr(
            StringConcatExprView<T> view, TranslationContext translationContext, Function<T, Node> visitRangeExpr) {
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
            RangeExprView<T> view, TranslationContext translationContext, Function<T, Node> visitAdditiveExpr) {
        Expression mainExpression = (Expression) visitAdditiveExpr.apply(view.mainExpr());
        if (view.rhs() == null || view.rhs().isEmpty()) {
            return mainExpression;
        }
        T child = view.rhs().get(0);
        Expression childExpression = (Expression) visitAdditiveExpr.apply(child);
        return new RangeExpression(mainExpression, childExpression, translationContext.metadata(view.context()));
    }

    public static <T extends ParserRuleContext> Expression translateAdditiveExpr(
            AdditiveExprView<T> view,
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
}
