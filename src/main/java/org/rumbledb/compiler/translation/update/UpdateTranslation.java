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
import org.rumbledb.expressions.postfix.ArrayLookupExpression;
import org.rumbledb.expressions.postfix.ObjectLookupExpression;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.xml.PostfixLookupExpression;
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
        Expression locatorTree = visitPostfixExpr.apply(ctx.updateLocator().mainExpr());
        return new DeleteExpression(
                extractMainExpression(locatorTree),
                extractLocatorExpression(locatorTree),
                translationContext.metadata(ctx.context()));
    }

    public static <PostfixExprCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            RenameExpression renameExpr(
                    RenameExprContext<PostfixExprCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<PostfixExprCtx, Expression> visitPostfixExpr,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression locatorTree = visitPostfixExpr.apply(ctx.updateLocator().mainExpr());
        Expression nameExpression = visitExprSingle.apply(ctx.nameExpr());
        return new RenameExpression(
                extractMainExpression(locatorTree),
                extractLocatorExpression(locatorTree),
                nameExpression,
                translationContext.metadata(ctx.context()));
    }

    public static <PostfixExprCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            ReplaceExpression replaceExpr(
                    ReplaceExprContext<PostfixExprCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<PostfixExprCtx, Expression> visitPostfixExpr,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression locatorTree = visitPostfixExpr.apply(ctx.updateLocator().mainExpr());
        Expression newExpression = visitExprSingle.apply(ctx.replacerExpr());
        return new ReplaceExpression(
                extractMainExpression(locatorTree),
                extractLocatorExpression(locatorTree),
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

    private static Expression extractMainExpression(Expression locatorExpr) {
        if (locatorExpr instanceof ObjectLookupExpression objectLookupExpression) {
            return objectLookupExpression.getMainExpression();
        } else if (locatorExpr instanceof ArrayLookupExpression arrayLookupExpression) {
            return arrayLookupExpression.getMainExpression();
        } else if (locatorExpr instanceof PostfixLookupExpression postfixLookupExpression) {
            return postfixLookupExpression.getMainExpression();
        } else {
            throw new OurBadException("Unrecognized main expression found in update expression.");
        }
    }

    private static Expression extractLocatorExpression(Expression locatorExpr) {
        if (locatorExpr instanceof ObjectLookupExpression objectLookupExpression) {
            return objectLookupExpression.getLookupExpression();
        } else if (locatorExpr instanceof ArrayLookupExpression arrayLookupExpression) {
            return arrayLookupExpression.getLookupExpression();
        } else if (locatorExpr instanceof PostfixLookupExpression postfixLookupExpression) {
            return postfixLookupExpression.getLookupExpression();
        } else {
            throw new OurBadException("Unrecognized main expression found in update expression.");
        }
    }
}
