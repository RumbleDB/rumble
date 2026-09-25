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

import org.rumbledb.compiler.context.SingleTypeCheckExprContext;
import org.rumbledb.compiler.context.TypeCheckExprContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.IsStaticallyExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

public final class TypeTranslation {

    private TypeTranslation() {}

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>
            Expression instanceOfExpr(
                    TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Node> visitIsStaticallyExpr,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitIsStaticallyExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new InstanceOfExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>
            Expression isStaticallyExpr(
                    TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Node> visitTreatExpr,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression mainExpression = (Expression) visitTreatExpr.apply(ctx.mainExpr());
        if (ctx.seq() == null || ctx.seq().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSequenceType.apply(ctx.seq());
        return new IsStaticallyExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext> Expression treatExpr(
            TypeCheckExprContext<MainExprCtx, SeqTypeCtx> ctx,
            TranslationContext translationContext,
            Function<MainExprCtx, Node> visitCastableExpr,
            Function<SeqTypeCtx, SequenceType> processSequenceType) {
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

    public static <MainExprCtx extends ParserRuleContext, SingleTypeCtx extends ParserRuleContext>
            Expression castableExpr(
                    SingleTypeCheckExprContext<MainExprCtx, SingleTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<MainExprCtx, Node> visitCastExpr,
                    Function<SingleTypeCtx, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitCastExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastableExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }

    public static <MainExprCtx extends ParserRuleContext, SingleTypeCtx extends ParserRuleContext> Expression castExpr(
            SingleTypeCheckExprContext<MainExprCtx, SingleTypeCtx> ctx,
            TranslationContext translationContext,
            Function<MainExprCtx, Node> visitArrowExpr,
            Function<SingleTypeCtx, SequenceType> processSingleType) {
        Expression mainExpression = (Expression) visitArrowExpr.apply(ctx.mainExpr());
        if (ctx.single() == null || ctx.single().isEmpty()) {
            return mainExpression;
        }
        SequenceType sequenceType = processSingleType.apply(ctx.single());
        return new CastExpression(mainExpression, sequenceType, translationContext.metadata(ctx.context()));
    }
}
