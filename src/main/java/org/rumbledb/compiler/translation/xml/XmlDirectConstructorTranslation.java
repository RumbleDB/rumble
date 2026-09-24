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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import org.rumbledb.compiler.DirectConstructorUtils;
import org.rumbledb.compiler.context.xml.CommonContentContext;
import org.rumbledb.compiler.context.xml.DirAttributeListContext;
import org.rumbledb.compiler.context.xml.DirElemContentContext;
import org.rumbledb.compiler.context.xml.DirElemOpenCloseContext;
import org.rumbledb.compiler.context.xml.DirectConstructorContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DirectElementConstructorTagMismatchException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NamespaceDeclarationAttributeEnclosedExpressionException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.NamespaceDeclaration;
import org.rumbledb.expressions.xml.TextNodeExpression;

public final class XmlDirectConstructorTranslation {

    private XmlDirectConstructorTranslation() {}

    private static class DirAttributeProcessingResult {
        public final List<Expression> attributes = new ArrayList<>();
        public final List<NamespaceDeclaration> namespaceDeclarations = new ArrayList<>();
    }

    public static <
                    QnameCtx extends ParserRuleContext,
                    DirAttrValueCtx extends ParserRuleContext,
                    DirElemContentCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            Expression directConstructor(
                    DirectConstructorContext<QnameCtx, DirAttrValueCtx, DirElemContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<DirElemContentCtx, Expression> visitDirElemContent,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        if (ctx.comment() != null) {
            String commentText = ctx.comment().getText();
            String commentContent = commentText.substring(4, commentText.length() - 3);
            return new DirectCommentConstructorExpression(commentContent, translationContext.metadata(ctx.context()));
        }
        if (ctx.openClose() != null) {
            return dirElemConstructorOpenClose(
                    ctx,
                    tokenStream,
                    translationContext,
                    parseName,
                    visitDirElemContent,
                    extractExprFromAttributeContent,
                    visitExpr);
        } else if (ctx.isSingleTag()) {
            return dirElemConstructorSingleTag(
                    ctx, tokenStream, translationContext, parseName, extractExprFromAttributeContent, visitExpr);
        } else if (ctx.pi() != null) {
            return dirPIConstructor(ctx.pi(), translationContext.metadata(ctx.context()));
        }
        throw new UnsupportedFeatureException(
                "Direct constructor not yet implemented", translationContext.metadata(ctx.context()));
    }

    public static DirPIConstructorExpression dirPIConstructor(TerminalNode piToken, ExceptionMetadata metadata) {
        String tokenText = piToken.getText();
        String inner = tokenText.substring(2, tokenText.length() - 2);
        int whitespaceIndex = indexOfWhitespace(inner);
        String target = whitespaceIndex == -1 ? inner : inner.substring(0, whitespaceIndex);
        Expression contentExpression = null;
        if (whitespaceIndex != -1) {
            int contentStart = whitespaceIndex;
            while (contentStart < inner.length() && Character.isWhitespace(inner.charAt(contentStart))) {
                contentStart++;
            }
            String content = inner.substring(contentStart);
            contentExpression = new StringLiteralExpression(content, metadata);
        }
        return new DirPIConstructorExpression(target, contentExpression, metadata);
    }

    public static int indexOfWhitespace(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isWhitespace(value.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static <
                    QnameCtx extends ParserRuleContext,
                    DirAttrValueCtx extends ParserRuleContext,
                    DirElemContentCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            DirElemConstructorExpression dirElemConstructorOpenClose(
                    DirectConstructorContext<QnameCtx, DirAttrValueCtx, DirElemContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<DirElemContentCtx, Expression> visitDirElemContent,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        DirElemOpenCloseContext<QnameCtx, DirElemContentCtx> openClose = ctx.openClose();
        if (openClose.closeTagName() != null
                && !openClose.closeTagName().getText().equals(ctx.openTagName().getText())) {
            throw new DirectElementConstructorTagMismatchException(
                    "The name used in the end tag must exactly match the name used in the corresponding start tag.",
                    translationContext.metadata(ctx.context()));
        }

        translationContext.pushConstructorNamespaceFrame();
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes() != null) {
                attributeResult = getAttributesExpressionsList(
                        ctx.attributes(),
                        tokenStream,
                        translationContext,
                        parseName,
                        extractExprFromAttributeContent,
                        visitExpr);
            }

            List<Expression> content = DirectConstructorUtils.mergeElementContent(
                    tokenStream,
                    openClose.endOpen(),
                    openClose.dirElemContent(),
                    translationContext.moduleContext().isBoundarySpacePreserve(),
                    visitDirElemContent);

            return new DirElemConstructorExpression(
                    parseName.apply(ctx.openTagName(), NameRole.ELEMENT_CONSTRUCTOR),
                    content,
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    translationContext.metadata(ctx.context()));
        } finally {
            translationContext.popConstructorNamespaceFrame();
        }
    }

    private static <
                    QnameCtx extends ParserRuleContext,
                    DirAttrValueCtx extends ParserRuleContext,
                    DirElemContentCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            DirElemConstructorExpression dirElemConstructorSingleTag(
                    DirectConstructorContext<QnameCtx, DirAttrValueCtx, DirElemContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        translationContext.pushConstructorNamespaceFrame();
        try {
            DirAttributeProcessingResult attributeResult = new DirAttributeProcessingResult();
            if (ctx.attributes() != null) {
                attributeResult = getAttributesExpressionsList(
                        ctx.attributes(),
                        tokenStream,
                        translationContext,
                        parseName,
                        extractExprFromAttributeContent,
                        visitExpr);
            }

            return new DirElemConstructorExpression(
                    parseName.apply(ctx.openTagName(), NameRole.ELEMENT_CONSTRUCTOR),
                    new ArrayList<>(),
                    attributeResult.attributes,
                    attributeResult.namespaceDeclarations,
                    translationContext.metadata(ctx.context()));
        } finally {
            translationContext.popConstructorNamespaceFrame();
        }
    }

    public static Expression dirElemContent(
            DirElemContentContext ctx,
            CommonTokenStream tokenStream,
            TranslationContext translationContext,
            Function<ParserRuleContext, Node> visit) {
        if (ctx.directConstructor() != null) {
            return (Expression) visit.apply(ctx.directConstructor());
        }
        if (ctx.commonContent() != null) {
            return (Expression) visit.apply(ctx.commonContent());
        }
        String text = tokenStream.getText(ctx.context().getSourceInterval());
        if (ctx.cdata() != null) {
            return new TextNodeExpression(
                    text.substring(9, text.length() - 3), translationContext.metadata(ctx.context()));
        }
        return new TextNodeExpression(
                text, translationContext.metadata(ctx.context()), DirectConstructorUtils.isWhitespaceOnly(text));
    }

    public static <ExprCtx extends ParserRuleContext> Expression commonContent(
            CommonContentContext<ExprCtx> ctx,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        if (ctx.expr() != null) {
            return visitExpr.apply(ctx.expr());
        }
        String processedContent =
                DirectConstructorUtils.processLiteralContent(ctx.context().getText());
        return new TextNodeExpression(processedContent, translationContext.metadata(ctx.context()));
    }

    private static <
                    QnameCtx extends ParserRuleContext,
                    DirAttrValueCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            DirAttributeProcessingResult getAttributesExpressionsList(
                    DirAttributeListContext<QnameCtx, DirAttrValueCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        DirAttributeProcessingResult result = new DirAttributeProcessingResult();

        List<QnameCtx> attributeNames = ctx.attributeQname();
        List<DirAttrValueCtx> attributeValues = ctx.attributeValue();

        // Namespace declarations are in scope for the entire element start tag,
        // including attributes that occur lexically before the declaration.
        for (int i = 0; i < attributeNames.size(); i++) {
            QnameCtx qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if ("xmlns".equals(lexical) || lexical.startsWith("xmlns:")) {
                String declaredPrefix = "xmlns".equals(lexical) ? "" : lexical.substring("xmlns:".length());
                String uri = getNamespaceDeclarationUri(
                        attributeValues.get(i),
                        tokenStream,
                        translationContext,
                        extractExprFromAttributeContent,
                        visitExpr);
                result.namespaceDeclarations.add(
                        new NamespaceDeclaration(declaredPrefix, uri, translationContext.metadata(qnameCtx)));
                translationContext.bindConstructorNamespace(declaredPrefix, uri);
            }
        }

        // Translate non-namespace attributes after the complete namespace frame
        // has been established, while retaining their original source order.
        for (int i = 0; i < attributeNames.size(); i++) {
            QnameCtx qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if ("xmlns".equals(lexical) || lexical.startsWith("xmlns:")) {
                continue;
            }
            Name attributeName = parseName.apply(qnameCtx, NameRole.NO_DEFAULT_NAMESPACE);

            List<Expression> value = getAttributeValuesExpressionsList(
                    attributeValues.get(i),
                    true,
                    tokenStream,
                    translationContext,
                    extractExprFromAttributeContent,
                    visitExpr);
            AttributeNodeExpression attributeNode = new AttributeNodeExpression(
                    attributeName,
                    value,
                    translationContext.metadata(
                            qnameCtx.getStart(), attributeValues.get(i).getStop()));
            result.attributes.add(attributeNode);
        }

        return result;
    }

    private static <DirAttrValueCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>
            List<Expression> getAttributeValuesExpressionsList(
                    DirAttrValueCtx ctx,
                    boolean allowEnclosedExpressions,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        if (ctx.getChildCount() > 0 && ctx.getChild(0) instanceof ParserRuleContext quotedValue) {
            return DirectConstructorUtils.processQuotedValue(
                    tokenStream,
                    quotedValue,
                    allowEnclosedExpressions,
                    translationContext::metadata,
                    translationContext::metadata,
                    (c, allow) -> processAttributeContent(
                            c, allow, tokenStream, translationContext, extractExprFromAttributeContent, visitExpr));
        }
        throw new UnsupportedOperationException("Unsupported attribute value: " + ctx.getText());
    }

    private static <DirAttrValueCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>
            String getNamespaceDeclarationUri(
                    DirAttrValueCtx ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
                    Function<ExprCtx, Expression> visitExpr) {
        List<Expression> uriExpressions = getAttributeValuesExpressionsList(
                ctx, false, tokenStream, translationContext, extractExprFromAttributeContent, visitExpr);
        StringBuilder uriBuilder = new StringBuilder();
        for (Expression expression : uriExpressions) {
            if (!(expression instanceof AttributeNodeContentExpression attributeContent)) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        translationContext.metadata(ctx));
            }
            uriBuilder.append(attributeContent.getContent());
        }
        return uriBuilder.toString();
    }

    private static <ExprCtx extends ParserRuleContext> List<Expression> processAttributeContent(
            ParserRuleContext ctx,
            boolean allowEnclosedExpressions,
            CommonTokenStream tokenStream,
            TranslationContext translationContext,
            Function<ParserRuleContext, ExprCtx> extractExprFromAttributeContent,
            Function<ExprCtx, Expression> visitExpr) {
        ExprCtx expr = extractExprFromAttributeContent.apply(ctx);
        if (expr != null) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        translationContext.metadata(ctx));
            }
            return List.of(visitExpr.apply(expr));
        }

        String childText = tokenStream.getText(ctx.getSourceInterval());
        DirectConstructorUtils.validateLiteral(childText, ctx, translationContext::metadata);
        String processedContent = DirectConstructorUtils.processLiteralContent(childText);
        ParseTree child = ctx.getChildCount() > 0 ? ctx.getChild(0) : ctx;
        return List.of(new AttributeNodeContentExpression(processedContent, translationContext.metadata(child)));
    }
}
