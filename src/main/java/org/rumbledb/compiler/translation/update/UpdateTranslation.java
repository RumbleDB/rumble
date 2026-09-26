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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.update.AppendExprContext;
import org.rumbledb.compiler.context.update.DeleteExprContext;
import org.rumbledb.compiler.context.update.InsertExprContext;
import org.rumbledb.compiler.context.update.PairConstructorContext;
import org.rumbledb.compiler.context.update.RenameExprContext;
import org.rumbledb.compiler.context.update.ReplaceExprContext;
import org.rumbledb.compiler.context.update.TransformExprContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.postfix.LookupExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.xml.StepExpr;

public final class UpdateTranslation {

    private UpdateTranslation() {}

    public static <ExprSingleCtx extends ParserRuleContext> InsertExpression insertExpr(
            InsertExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression toInsertExpr;
        Expression posExpr = null;
        if (ctx.pairConstructor() != null && !ctx.pairConstructor().isEmpty()) {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (PairConstructorContext<ExprSingleCtx> currentPair : ctx.pairConstructor()) {
                Expression lhs = visitExprSingle.apply(currentPair.lhs());
                if (lhs instanceof StepExpr stepExpr) {
                    if ("jsoniq10".equals(translationContext.moduleContext().getQueryLanguage())) {
                        keys.add(new StringLiteralExpression(
                                stepExpr.getNodeTest().toString(), lhs.getMetadata()));
                    } else {
                        throw new ParsingException(
                                "Parser error: Unquoted keys are not supported in JSONiq versions >1.0. Either quote your keys or revert to JSONiq 1.0 using the --default-language jsoniq10 CLI option.",
                                lhs.getMetadata());
                    }
                } else {
                    keys.add(lhs);
                }
                values.add(visitExprSingle.apply(currentPair.rhs()));
            }
            toInsertExpr = new ObjectConstructorExpression(keys, values, translationContext.metadata(ctx.context()));
        } else if (ctx.toInsertExpr() != null) {
            toInsertExpr = visitExprSingle.apply(ctx.toInsertExpr());
            if (ctx.posExpr() != null) {
                posExpr = visitExprSingle.apply(ctx.posExpr());
            }
        } else {
            throw new OurBadException("Unrecognised expression to insert in Insert Expression");
        }
        Expression mainExpr = visitExprSingle.apply(ctx.mainExpr());

        return new InsertExpression(mainExpr, toInsertExpr, posExpr, translationContext.metadata(ctx.context()));
    }

    public static <PostfixExprCtx extends ParserRuleContext> DeleteExpression deleteExpr(
            DeleteExprContext<PostfixExprCtx> ctx,
            TranslationContext translationContext,
            Function<PostfixExprCtx, Expression> visitPostfixExpr) {
        UpdateTarget target =
                extractUpdateTarget(visitPostfixExpr.apply(ctx.updateLocator().mainExpr()));
        return new DeleteExpression(
                target.mainExpression(), target.locatorExpression(), translationContext.metadata(ctx.context()));
    }

    public static <PostfixExprCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            RenameExpression renameExpr(
                    RenameExprContext<PostfixExprCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<PostfixExprCtx, Expression> visitPostfixExpr,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        UpdateTarget target =
                extractUpdateTarget(visitPostfixExpr.apply(ctx.updateLocator().mainExpr()));
        Expression nameExpression = visitExprSingle.apply(ctx.nameExpr());
        return new RenameExpression(
                target.mainExpression(),
                target.locatorExpression(),
                nameExpression,
                translationContext.metadata(ctx.context()));
    }

    public static <PostfixExprCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            ReplaceExpression replaceExpr(
                    ReplaceExprContext<PostfixExprCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<PostfixExprCtx, Expression> visitPostfixExpr,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        UpdateTarget target =
                extractUpdateTarget(visitPostfixExpr.apply(ctx.updateLocator().mainExpr()));
        Expression newExpression = visitExprSingle.apply(ctx.replacerExpr());
        return new ReplaceExpression(
                target.mainExpression(),
                target.locatorExpression(),
                newExpression,
                translationContext.metadata(ctx.context()));
    }

    public static <VarBindingCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            TransformExpression transformExpr(
                    TransformExprContext<VarBindingCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        List<CopyDeclaration> copyDecls = ctx.copyDecl().stream()
                .map(copyDeclCtx -> {
                    Name var = parseVariableBinding.apply(copyDeclCtx.varRef());
                    Expression expr = visitExprSingle.apply(copyDeclCtx.srcExpr());
                    return new CopyDeclaration(var, expr);
                })
                .collect(Collectors.toList());
        Expression modifyExpression = visitExprSingle.apply(ctx.modExpr());
        Expression returnExpression = visitExprSingle.apply(ctx.retExpr());
        return new TransformExpression(
                copyDecls, modifyExpression, returnExpression, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> AppendExpression appendExpr(
            AppendExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression arrayExpression = visitExprSingle.apply(ctx.arrayExpr());
        Expression toAppendExpression = visitExprSingle.apply(ctx.toAppendExpr());
        return new AppendExpression(arrayExpression, toAppendExpression, translationContext.metadata(ctx.context()));
    }

    private record UpdateTarget(Expression mainExpression, Expression locatorExpression) {}

    private static UpdateTarget extractUpdateTarget(Expression locatorExpr) {
        if (locatorExpr instanceof LookupExpression lookup) {
            return new UpdateTarget(lookup.getMainExpression(), lookup.getLookupExpression());
        }
        throw new OurBadException("Unrecognized main expression found in update expression.");
    }
}
