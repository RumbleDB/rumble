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

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.LiteralExprContext;
import org.rumbledb.compiler.context.ParenthesizedExprContext;
import org.rumbledb.compiler.context.ValueExprContext;
import org.rumbledb.compiler.context.VarRefContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;

public final class PrimaryTranslation {

    private PrimaryTranslation() {}

    public static <M extends ParserRuleContext, V extends ParserRuleContext> Node valueExpr(
            ValueExprContext<M, V> ctx,
            TranslationContext translationContext,
            Function<M, Node> visitSimpleMapExpr,
            Function<V, Node> visitValidateExpr) {
        if (ctx.simpleMapExpr() != null) {
            return visitSimpleMapExpr.apply(ctx.simpleMapExpr());
        }
        if (ctx.validateExpr() != null) {
            return visitValidateExpr.apply(ctx.validateExpr());
        }
        // TODO: extension expression still unsupported
        throw new UnsupportedFeatureException(
                "Extension expression still unsupported", translationContext.metadata(ctx.context()));
    }

    public static <E extends ParserRuleContext> Expression parenthesizedExpr(
            ParenthesizedExprContext<E> ctx, TranslationContext translationContext, Function<E, Node> visitExpr) {
        if (ctx.expr() == null) {
            return new CommaExpression(translationContext.metadata(ctx.context()));
        }
        return (Expression) visitExpr.apply(ctx.expr());
    }

    public static <E extends ParserRuleContext> Expression varRef(
            VarRefContext<E> ctx, TranslationContext translationContext, BiFunction<E, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return new VariableReferenceExpression(name, translationContext.metadata(ctx.context()));
    }

    public static Expression contextItemExpr(ParserRuleContext ctx, TranslationContext translationContext) {
        return new ContextItemExpression(translationContext.metadata(ctx));
    }

    public static <S extends ParserRuleContext> Expression literal(
            LiteralExprContext<S> ctx,
            TranslationContext translationContext,
            Function<S, String> processStringLiteral) {
        if (ctx.stringLiteral() != null) {
            return new StringLiteralExpression(
                    processStringLiteral.apply(ctx.stringLiteral()), translationContext.metadata(ctx.context()));
        }
        if (ctx.numericLiteralText() != null) {
            return literalExpressionFromToken(ctx.numericLiteralText(), translationContext.metadata(ctx.context()));
        }
        throw new UnsupportedFeatureException(
                "Literal not yet implemented", translationContext.metadata(ctx.context()));
    }

    public static Expression literalExpressionFromToken(String token, ExceptionMetadata metadata) {
        switch (token) {
            case "null":
                return new NullLiteralExpression(metadata);
            case "true":
                return new BooleanLiteralExpression(true, metadata);
            case "false":
                return new BooleanLiteralExpression(false, metadata);
            default:
        }
        if (token.contains("E") || token.contains("e")) {
            return new DoubleLiteralExpression(Double.parseDouble(token), metadata);
        }
        if (token.contains(".")) {
            return new DecimalLiteralExpression(new BigDecimal(token), metadata);
        }
        return new IntegerLiteralExpression(token, metadata);
    }
}
