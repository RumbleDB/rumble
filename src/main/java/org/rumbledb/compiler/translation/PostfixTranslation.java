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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.ArrowExprContext;
import org.rumbledb.compiler.context.SimpleMapExprContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.SimpleMapExpression;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;

public final class PostfixTranslation {

    private PostfixTranslation() {}

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

    public static <
                    T extends ParserRuleContext,
                    E extends ParserRuleContext,
                    V extends ParserRuleContext,
                    P extends ParserRuleContext,
                    A extends ParserRuleContext>
            Expression arrowExpr(
                    ArrowExprContext<T, E, V, P, A> ctx,
                    TranslationContext translationContext,
                    Function<T, Node> visitUnaryExpr,
                    BiFunction<E, NameRole, Name> parseEqName,
                    Function<V, Node> visitVarRef,
                    Function<P, Node> visitParenthesizedExpr,
                    Function<A, List<Expression>> getArgumentsFromArgumentListContext) {
        Expression mainExpression = (Expression) visitUnaryExpr.apply(ctx.mainExpr());
        Expression functionExpression = null;

        for (ArrowExprContext.ArrowCall<E, V, P, A> call : ctx.calls()) {
            ExceptionMetadata metadata = translationContext.metadata(
                    ctx.mainExpr().getStart(), call.argumentList().getStop());
            List<Expression> children = new ArrayList<Expression>();
            children.add(mainExpression);
            children.addAll(getArgumentsFromArgumentListContext.apply(call.argumentList()));
            if (call.eqName() != null) {
                Name name = parseEqName.apply(call.eqName(), NameRole.FUNCTION);
                mainExpression = new FunctionCallExpression(name, children, metadata);
                continue;
            } else if (call.varRef() != null) {
                functionExpression = (Expression) visitVarRef.apply(call.varRef());
            } else {
                functionExpression = (Expression) visitParenthesizedExpr.apply(call.parenthesizedExpr());
            }
            mainExpression = new DynamicFunctionCallExpression(functionExpression, children, metadata);
        }
        return mainExpression;
    }
}
