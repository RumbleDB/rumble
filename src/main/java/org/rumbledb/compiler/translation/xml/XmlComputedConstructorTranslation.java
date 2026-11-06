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
package org.rumbledb.compiler.translation.xml;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.xml.CompAttrConstructorContext;
import org.rumbledb.compiler.context.xml.CompCommentConstructorContext;
import org.rumbledb.compiler.context.xml.CompDocConstructorContext;
import org.rumbledb.compiler.context.xml.CompElemConstructorContext;
import org.rumbledb.compiler.context.xml.CompNamespaceConstructorContext;
import org.rumbledb.compiler.context.xml.CompPIConstructorContext;
import org.rumbledb.compiler.context.xml.CompTextConstructorContext;
import org.rumbledb.compiler.context.xml.EnclosedContentExprContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.xml.CommentNodeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedAttributeConstructorExpression;
import org.rumbledb.expressions.xml.ComputedElementConstructorExpression;
import org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression;
import org.rumbledb.expressions.xml.ComputedPIConstructorExpression;
import org.rumbledb.expressions.xml.DocumentNodeConstructorExpression;
import org.rumbledb.expressions.xml.TextNodeConstructorExpression;

public final class XmlComputedConstructorTranslation {

    private XmlComputedConstructorTranslation() {}

    public static <EnclosedExprCtx extends ParserRuleContext> DocumentNodeConstructorExpression compDocConstructor(
            CompDocConstructorContext<EnclosedExprCtx> ctx,
            TranslationContext translationContext,
            Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression contentExpression = visitEnclosedExpression.apply(ctx.enclosedExpression());
        return new DocumentNodeConstructorExpression(contentExpression, translationContext.metadata(ctx.context()));
    }

    public static <EnclosedExprCtx extends ParserRuleContext> TextNodeConstructorExpression compTextConstructor(
            CompTextConstructorContext<EnclosedExprCtx> ctx,
            TranslationContext translationContext,
            Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression contentExpression = visitEnclosedExpression.apply(ctx.enclosedExpression());
        return new TextNodeConstructorExpression(contentExpression, translationContext.metadata(ctx.context()));
    }

    public static <EnclosedExprCtx extends ParserRuleContext> CommentNodeConstructorExpression compCommentConstructor(
            CompCommentConstructorContext<EnclosedExprCtx> ctx,
            TranslationContext translationContext,
            Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression contentExpression = visitEnclosedExpression.apply(ctx.enclosedExpression());
        return new CommentNodeConstructorExpression(contentExpression, translationContext.metadata(ctx.context()));
    }

    public static <ExprCtx extends ParserRuleContext, EnclosedExprCtx extends ParserRuleContext>
            ComputedPIConstructorExpression compPIConstructor(
                    CompPIConstructorContext<ExprCtx, EnclosedExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprCtx, Expression> visitExpr,
                    Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression contentExpression = visitEnclosedExpression.apply(ctx.enclosedExpression());
        if (ctx.ncName() != null) {
            return new ComputedPIConstructorExpression(
                    ctx.ncName(), contentExpression, translationContext.metadata(ctx.context()));
        }
        if (ctx.expr() != null) {
            Expression nameExpression = visitExpr.apply(ctx.expr());
            return new ComputedPIConstructorExpression(
                    nameExpression, contentExpression, translationContext.metadata(ctx.context()));
        }
        throw new ParsingException(
                "Computed processing instruction constructor must have either a static NCName or a dynamic name expression",
                translationContext.metadata(ctx.context()));
    }

    public static <
                    EqNameCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext,
                    EnclosedExprCtx extends ParserRuleContext>
            ComputedAttributeConstructorExpression compAttrConstructor(
                    CompAttrConstructorContext<EqNameCtx, ExprCtx, EnclosedExprCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<ExprCtx, Expression> visitExpr,
                    Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression valueExpression = visitEnclosedExpression.apply(ctx.enclosedExpression());
        if (ctx.name() != null) {
            Name attributeName = parseEqName.apply(ctx.name(), NameRole.NO_DEFAULT_NAMESPACE);
            return new ComputedAttributeConstructorExpression(
                    attributeName, valueExpression, translationContext.metadata(ctx.context()));
        } else if (ctx.nameExpr() != null) {
            Expression nameExpression = visitExpr.apply(ctx.nameExpr());
            return new ComputedAttributeConstructorExpression(
                    nameExpression, valueExpression, translationContext.metadata(ctx.context()));
        } else {
            throw new ParsingException(
                    "Computed attribute constructor must have either a static name or dynamic name expression",
                    translationContext.metadata(ctx.context()));
        }
    }

    public static <
                    EqNameCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext,
                    EnclosedContentExprCtx extends ParserRuleContext>
            ComputedElementConstructorExpression compElemConstructor(
                    CompElemConstructorContext<EqNameCtx, ExprCtx, EnclosedContentExprCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<ExprCtx, Expression> visitExpr,
                    Function<EnclosedContentExprCtx, Expression> visitEnclosedContentExpr) {
        Expression contentExpression = visitEnclosedContentExpr.apply(ctx.enclosedContentExpr());
        if (ctx.eqName() != null) {
            Name elementName = parseEqName.apply(ctx.eqName(), NameRole.ELEMENT_CONSTRUCTOR);
            return new ComputedElementConstructorExpression(
                    elementName, contentExpression, translationContext.metadata(ctx.context()));
        } else if (ctx.expr() != null) {
            Expression nameExpression = visitExpr.apply(ctx.expr());
            return new ComputedElementConstructorExpression(
                    nameExpression, contentExpression, translationContext.metadata(ctx.context()));
        } else {
            throw new ParsingException(
                    "Computed element constructor must have either a static name or dynamic name expression",
                    translationContext.metadata(ctx.context()));
        }
    }

    public static <EnclosedExprCtx extends ParserRuleContext>
            ComputedNamespaceConstructorExpression compNamespaceConstructor(
                    CompNamespaceConstructorContext<EnclosedExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        Expression uriExpression = visitEnclosedExpression.apply(ctx.uriExpr());
        if (ctx.ncName() != null) {
            return new ComputedNamespaceConstructorExpression(
                    ctx.ncName(), uriExpression, translationContext.metadata(ctx.context()));
        }
        if (ctx.prefixExpr() != null) {
            Expression prefixExpression = visitEnclosedExpression.apply(ctx.prefixExpr());
            return new ComputedNamespaceConstructorExpression(
                    prefixExpression, uriExpression, translationContext.metadata(ctx.context()));
        }
        throw new ParsingException(
                "Computed namespace constructor must have either a static prefix or a dynamic prefix expression",
                translationContext.metadata(ctx.context()));
    }

    public static <EnclosedExprCtx extends ParserRuleContext> Expression enclosedContentExpr(
            EnclosedContentExprContext<EnclosedExprCtx> ctx,
            Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        return visitEnclosedExpression.apply(ctx.enclosedExpression());
    }
}
