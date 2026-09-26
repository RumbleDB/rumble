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
package org.rumbledb.compiler.translation.update;

import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.update.CreateCollectionExprContext;
import org.rumbledb.compiler.context.update.DeleteIndexExprContext;
import org.rumbledb.compiler.context.update.DeleteSearchExprContext;
import org.rumbledb.compiler.context.update.EditCollectionExprContext;
import org.rumbledb.compiler.context.update.InsertIndexExprContext;
import org.rumbledb.compiler.context.update.InsertSearchExprContext;
import org.rumbledb.compiler.context.update.TruncateCollectionExprContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
import org.rumbledb.runtime.update.primitives.Mode;

public final class CollectionTranslation {

    private CollectionTranslation() {}

    public static <ExprSimpleCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            CreateCollectionExpression createCollectionExpr(
                    CreateCollectionExprContext<ExprSimpleCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSimpleCtx, Expression> visitExprSimple,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression collection = visitExprSimple.apply(ctx.collectionName());
        Expression contentExpression;
        if (ctx.content() != null) {
            contentExpression = visitExprSingle.apply(ctx.content());
        } else {
            contentExpression = new CommaExpression(translationContext.metadata(ctx.context()));
        }
        Mode mode = Mode.fromString(ctx.collectionMode());
        return new CreateCollectionExpression(
                collection, contentExpression, mode, translationContext.metadata(ctx.context()));
    }

    public static <ExprSimpleCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            DeleteIndexFromCollectionExpression deleteIndexExpr(
                    DeleteIndexExprContext<ExprSimpleCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSimpleCtx, Expression> visitExprSimple,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression collection = visitExprSimple.apply(ctx.collectionName());
        Mode mode = Mode.fromString(ctx.collectionMode());
        boolean isFirst = ctx.isFirst();

        Expression numDelete = null;
        if (ctx.num() != null) {
            numDelete = visitExprSingle.apply(ctx.num());
        }

        return new DeleteIndexFromCollectionExpression(
                collection, numDelete, isFirst, mode, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> DeleteSearchFromCollectionExpression deleteSearchExpr(
            DeleteSearchExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression contentExpression = visitExprSingle.apply(ctx.content());
        return new DeleteSearchFromCollectionExpression(contentExpression, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> EditCollectionExpression editCollectionExpr(
            EditCollectionExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression targetExpression = visitExprSingle.apply(ctx.target());
        Expression contentExpression = visitExprSingle.apply(ctx.content());
        return new EditCollectionExpression(
                targetExpression, contentExpression, translationContext.metadata(ctx.context()));
    }

    public static <ExprSimpleCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            InsertIndexIntoCollectionExpression insertIndexExpr(
                    InsertIndexExprContext<ExprSimpleCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSimpleCtx, Expression> visitExprSimple,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression collection = visitExprSimple.apply(ctx.collectionName());
        Expression contentExpression = visitExprSingle.apply(ctx.content());
        Expression pos = ctx.pos() != null ? visitExprSingle.apply(ctx.pos()) : null;
        Mode mode = Mode.fromString(ctx.collectionMode());

        return new InsertIndexIntoCollectionExpression(
                collection,
                contentExpression,
                pos,
                mode,
                ctx.isFirst(),
                ctx.isLast(),
                translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> InsertSearchIntoCollectionExpression insertSearchExpr(
            InsertSearchExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression targetExpression = visitExprSingle.apply(ctx.target());
        Expression contentExpression = visitExprSingle.apply(ctx.content());
        return new InsertSearchIntoCollectionExpression(
                targetExpression, contentExpression, ctx.isBefore(), translationContext.metadata(ctx.context()));
    }

    public static <ExprSimpleCtx extends ParserRuleContext> TruncateCollectionExpression truncateCollectionExpr(
            TruncateCollectionExprContext<ExprSimpleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSimpleCtx, Expression> visitExprSimple) {
        Expression collectionName = visitExprSimple.apply(ctx.collectionName());
        Mode mode = Mode.fromString(ctx.collectionMode());
        return new TruncateCollectionExpression(collectionName, mode, translationContext.metadata(ctx.context()));
    }
}
