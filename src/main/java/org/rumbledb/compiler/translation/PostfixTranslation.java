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

    public static <MainExprCtx extends ParserRuleContext, MapExprCtx extends ParserRuleContext>
            Expression simpleMapExpr(
                    SimpleMapExprContext<MainExprCtx, MapExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Node> visitPathExprForMain,
                    Function<MapExprCtx, Node> visitPathExprForMap) {
        Expression result = (Expression) visitPathExprForMain.apply(ctx.mainExpr());
        if (ctx.mapExpr() == null || ctx.mapExpr().isEmpty()) {
            return result;
        }
        for (MapExprCtx child : ctx.mapExpr()) {
            Expression rightExpression = (Expression) visitPathExprForMap.apply(child);
            result = new SimpleMapExpression(
                    result,
                    rightExpression,
                    translationContext.metadata(ctx.mainExpr().getStart(), child.getStop()));
        }
        return result;
    }

    public static <
                    MainExprCtx extends ParserRuleContext,
                    EqNameCtx extends ParserRuleContext,
                    VarRefCtx extends ParserRuleContext,
                    ParenthesizedExprCtx extends ParserRuleContext,
                    ArgumentListCtx extends ParserRuleContext>
            Expression arrowExpr(
                    ArrowExprContext<MainExprCtx, EqNameCtx, VarRefCtx, ParenthesizedExprCtx, ArgumentListCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Node> visitUnaryExpr,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<VarRefCtx, Node> visitVarRef,
                    Function<ParenthesizedExprCtx, Node> visitParenthesizedExpr,
                    Function<ArgumentListCtx, List<Expression>> getArgumentsFromArgumentListContext) {
        Expression mainExpression = (Expression) visitUnaryExpr.apply(ctx.mainExpr());
        Expression functionExpression = null;

        for (ArrowExprContext.ArrowCall<EqNameCtx, VarRefCtx, ParenthesizedExprCtx, ArgumentListCtx> call :
                ctx.calls()) {
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
