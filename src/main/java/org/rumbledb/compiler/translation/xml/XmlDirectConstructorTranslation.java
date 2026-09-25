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
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.text.StringEscapeUtils;

import org.rumbledb.compiler.context.xml.CommonContentContext;
import org.rumbledb.compiler.context.xml.DirAttributeContentContext;
import org.rumbledb.compiler.context.xml.DirAttributeListContext;
import org.rumbledb.compiler.context.xml.DirAttributeValueContext;
import org.rumbledb.compiler.context.xml.DirElemContentContext;
import org.rumbledb.compiler.context.xml.DirElemOpenCloseContext;
import org.rumbledb.compiler.context.xml.DirectConstructorContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.utils.TokenStreamUtils;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DirectElementConstructorTagMismatchException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NamespaceDeclarationAttributeEnclosedExpressionException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.AttributeNodeExpression;
import org.rumbledb.expressions.xml.DirElemConstructorExpression;
import org.rumbledb.expressions.xml.DirPIConstructorExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.NamespaceDeclaration;
import org.rumbledb.expressions.xml.TextNodeExpression;

/**
 * Translation logic for XML direct constructors (elements, comments, processing instructions, attributes, and content).
 * Operates on parser-agnostic context adapters so that direct constructor AST construction is shared between JSONiq and
 * XQuery.
 */
public final class XmlDirectConstructorTranslation {

    private XmlDirectConstructorTranslation() {}

    /**
     * Holds the translated attribute node expressions and namespace declarations for an element constructor.
     */
    private record DirAttributeProcessingResult(
            List<Expression> attributes, List<NamespaceDeclaration> namespaceDeclarations) {}

    private static final class AttributeValueBuilder {
        private final BiFunction<ParseTree, ParseTree, ExceptionMetadata> metadataFactory;
        private final List<Expression> expressions = new ArrayList<>();
        private StringBuilder text;

        // Used to track the first and last parse-tree nodes of a literal run so that we can assign a source range to
        // the merged content
        private ParseTree firstTextTree;
        private ParseTree lastTextTree;

        AttributeValueBuilder(BiFunction<ParseTree, ParseTree, ExceptionMetadata> metadataFactory) {
            this.metadataFactory = metadataFactory;
        }

        void appendText(String value, ParseTree source) {
            if (value.isEmpty()) {
                return;
            }
            if (this.text == null) {
                this.text = new StringBuilder();
                this.firstTextTree = source;
            }
            this.text.append(value);
            this.lastTextTree = source;
        }

        void append(Expression expression, ParseTree source) {
            if (expression instanceof AttributeNodeContentExpression textExpression) {
                this.appendText(textExpression.getContent(), source);
                return;
            }
            this.flushText();
            this.expressions.add(expression);
        }

        List<Expression> finish() {
            this.flushText();
            return this.expressions;
        }

        private void flushText() {
            if (this.text == null) {
                return;
            }
            this.expressions.add(new AttributeNodeContentExpression(
                    this.text.toString(), this.metadataFactory.apply(this.firstTextTree, this.lastTextTree)));
            this.text = null;
            this.firstTextTree = null;
            this.lastTextTree = null;
        }
    }

    private static final class ElementContentBuilder {
        private final boolean preserveBoundarySpace;
        private final List<Expression> expressions = new ArrayList<>();
        private StringBuilder text;
        private ExceptionMetadata firstTextMetadata;
        private boolean boundaryWhitespaceOnly;

        ElementContentBuilder(boolean preserveBoundarySpace) {
            this.preserveBoundarySpace = preserveBoundarySpace;
        }

        void appendHiddenText(String value, ExceptionMetadata metadata) {
            if (value.isEmpty()) {
                return;
            }
            this.ensureText(metadata);
            this.text.append(value);
            this.boundaryWhitespaceOnly = this.boundaryWhitespaceOnly && isWhitespaceOnly(value);
        }

        void append(Expression expression) {
            if (expression instanceof TextNodeExpression textExpression) {
                String value = textExpression.getContent();
                if (value.isEmpty()) {
                    return;
                }
                this.ensureText(textExpression.getMetadata());
                this.text.append(value);
                this.boundaryWhitespaceOnly =
                        this.boundaryWhitespaceOnly && textExpression.isBoundaryWhitespace() && isWhitespaceOnly(value);
                return;
            }
            this.flushText();
            this.expressions.add(expression);
        }

        List<Expression> finish() {
            this.flushText();
            return this.expressions;
        }

        private void ensureText(ExceptionMetadata metadata) {
            if (this.text != null) {
                return;
            }
            this.text = new StringBuilder();
            this.firstTextMetadata = metadata;
            this.boundaryWhitespaceOnly = true;
        }

        private void flushText() {
            if (this.text == null) {
                return;
            }
            if (!this.text.isEmpty() && (this.preserveBoundarySpace || !this.boundaryWhitespaceOnly)) {
                this.expressions.add(new TextNodeExpression(this.text.toString(), this.firstTextMetadata));
            }
            this.text = null;
            this.firstTextMetadata = null;
            this.boundaryWhitespaceOnly = true;
        }
    }

    /**
     * Translates a direct constructor (element, comment, or processing instruction) into an {@link Expression}.
     *
     * @param <QnameCtx> the parse-tree context type for QNames
     * @param <DirElemContentCtx> the parse-tree context type for direct element content
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the direct constructor context adapter
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param parseName function resolving a QName to a {@link Name}
     * @param visitDirElemContent visitor function for direct element content
     * @param visitExpr visitor function for general expressions
     * @return the translated direct constructor expression
     */
    public static <
                    QnameCtx extends ParserRuleContext,
                    DirElemContentCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            Expression directConstructor(
                    DirectConstructorContext<QnameCtx, ExprCtx, DirElemContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<DirElemContentCtx, Expression> visitDirElemContent,
                    Function<ExprCtx, Expression> visitExpr) {
        if (ctx.comment() != null) {
            String commentText = ctx.comment().getText();
            String commentContent = commentText.substring(4, commentText.length() - 3);
            return new DirectCommentConstructorExpression(commentContent, translationContext.metadata(ctx.context()));
        }
        if (ctx.openClose() != null || ctx.isSingleTag()) {
            return dirElemConstructor(ctx, tokenStream, translationContext, parseName, visitDirElemContent, visitExpr);
        } else if (ctx.pi() != null) {
            return dirPIConstructor(ctx.pi(), translationContext.metadata(ctx.context()));
        }
        throw new UnsupportedFeatureException(
                "Direct constructor not yet implemented", translationContext.metadata(ctx.context()));
    }

    /**
     * Translates a direct processing instruction token into a {@link DirPIConstructorExpression}.
     *
     * @param piToken the terminal node containing the processing instruction
     * @param metadata the source metadata
     * @return the translated processing instruction expression
     */
    private static DirPIConstructorExpression dirPIConstructor(TerminalNode piToken, ExceptionMetadata metadata) {
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

    /**
     * Finds the index of the first whitespace character in a string.
     *
     * @param value the string to search
     * @return the 0-based index of the first whitespace character, or -1 if none is found
     */
    private static int indexOfWhitespace(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isWhitespace(value.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Translates a direct element constructor (open/close tag or single empty tag) into a
     * {@link DirElemConstructorExpression}.
     * Verifies matching end tag names and manages the constructor namespace frame during translation.
     */
    private static <
                    QnameCtx extends ParserRuleContext,
                    DirElemContentCtx extends ParserRuleContext,
                    ExprCtx extends ParserRuleContext>
            DirElemConstructorExpression dirElemConstructor(
                    DirectConstructorContext<QnameCtx, ExprCtx, DirElemContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<DirElemContentCtx, Expression> visitDirElemContent,
                    Function<ExprCtx, Expression> visitExpr) {
        DirElemOpenCloseContext<QnameCtx, DirElemContentCtx> openClose = ctx.openClose();
        if (openClose != null
                && openClose.closeTagName() != null
                && !openClose.closeTagName().getText().equals(ctx.openTagName().getText())) {
            throw new DirectElementConstructorTagMismatchException(
                    "The name used in the end tag must exactly match the name used in the corresponding start tag.",
                    translationContext.metadata(ctx.context()));
        }

        translationContext.pushConstructorNamespaceFrame();
        try {
            DirAttributeProcessingResult attributeResult =
                    new DirAttributeProcessingResult(Collections.emptyList(), Collections.emptyList());
            if (ctx.attributes() != null) {
                attributeResult = getAttributesExpressionsList(
                        ctx.attributes(), tokenStream, translationContext, parseName, visitExpr);
            }

            List<Expression> content = openClose != null
                    ? mergeElementContent(
                            tokenStream,
                            openClose.endOpen(),
                            openClose.dirElemContent(),
                            translationContext.moduleContext().isBoundarySpacePreserve(),
                            visitDirElemContent)
                    : Collections.emptyList();

            return new DirElemConstructorExpression(
                    parseName.apply(ctx.openTagName(), NameRole.ELEMENT_CONSTRUCTOR),
                    content,
                    attributeResult.attributes(),
                    attributeResult.namespaceDeclarations(),
                    translationContext.metadata(ctx.context()));
        } finally {
            translationContext.popConstructorNamespaceFrame();
        }
    }

    /**
     * Translates direct element content (nested direct constructor, common content, CDATA, or literal text)
     * into an {@link Expression}.
     *
     * @param <DirectConstructorCtx> the parse-tree context type for direct constructors
     * @param <CommonContentCtx> the parse-tree context type for common content
     * @param ctx the element content context adapter
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param visitDirectConstructor visitor function for nested direct constructors
     * @param visitCommonContent visitor function for common content
     * @return the translated element content expression
     */
    public static <DirectConstructorCtx extends ParserRuleContext, CommonContentCtx extends ParserRuleContext>
            Expression dirElemContent(
                    DirElemContentContext<DirectConstructorCtx, CommonContentCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    Function<DirectConstructorCtx, Expression> visitDirectConstructor,
                    Function<CommonContentCtx, Expression> visitCommonContent) {
        if (ctx.directConstructor() != null) {
            return visitDirectConstructor.apply(ctx.directConstructor());
        }
        if (ctx.commonContent() != null) {
            return visitCommonContent.apply(ctx.commonContent());
        }
        String text = tokenStream.getText(ctx.context().getSourceInterval());
        if (ctx.cdata() != null) {
            return new TextNodeExpression(
                    text.substring(9, text.length() - 3), translationContext.metadata(ctx.context()));
        }
        return new TextNodeExpression(text, translationContext.metadata(ctx.context()), isWhitespaceOnly(text));
    }

    /**
     * Translates direct constructor common content (enclosed expression or literal content) into an {@link Expression}.
     *
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the common content context adapter
     * @param translationContext the current translation context
     * @param visitExpr visitor function for expressions
     * @return the translated common content expression
     */
    public static <ExprCtx extends ParserRuleContext> Expression commonContent(
            CommonContentContext<ExprCtx> ctx,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        if (ctx.expr() != null) {
            return visitExpr.apply(ctx.expr());
        }
        String processedContent = processLiteralContent(ctx.context().getText());
        return new TextNodeExpression(processedContent, translationContext.metadata(ctx.context()));
    }

    /**
     * Processes element direct attributes in two passes:
     * <ol>
     * <li>Namespace declaration attributes ({@code xmlns} or {@code xmlns:*}) are extracted, validated to ensure they
     * do not contain enclosed expressions, and registered into the constructor namespace frame.</li>
     * <li>Non-namespace attributes are translated within the established namespace scope, preserving their original
     * source ordering.</li>
     * </ol>
     *
     * @param <QnameCtx> the parse-tree context type for QNames
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the attribute list context adapter
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param parseName function resolving a QName to a {@link Name}
     * @param visitExpr visitor function for expressions
     * @return a {@link DirAttributeProcessingResult} containing the translated attributes and namespace declarations
     */
    private static <QnameCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>
            DirAttributeProcessingResult getAttributesExpressionsList(
                    DirAttributeListContext<QnameCtx, ExprCtx> ctx,
                    CommonTokenStream tokenStream,
                    TranslationContext translationContext,
                    BiFunction<QnameCtx, NameRole, Name> parseName,
                    Function<ExprCtx, Expression> visitExpr) {
        List<Expression> attributes = new ArrayList<>();
        List<NamespaceDeclaration> namespaceDeclarations = new ArrayList<>();

        List<QnameCtx> attributeNames = ctx.attributeQname();
        List<DirAttributeValueContext<ExprCtx>> attributeValues = ctx.attributeValue();

        // Namespace declarations are in scope for the entire element start tag,
        // including attributes that occur lexically before the declaration.
        for (int i = 0; i < attributeNames.size(); i++) {
            QnameCtx qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if (isNamespaceDeclaration(lexical)) {
                String declaredPrefix = "xmlns".equals(lexical) ? "" : lexical.substring("xmlns:".length());
                String uri =
                        getNamespaceDeclarationUri(attributeValues.get(i), tokenStream, translationContext, visitExpr);
                namespaceDeclarations.add(
                        new NamespaceDeclaration(declaredPrefix, uri, translationContext.metadata(qnameCtx)));
                translationContext.bindConstructorNamespace(declaredPrefix, uri);
            }
        }

        // Translate non-namespace attributes after the complete namespace frame
        // has been established, while retaining their original source order.
        for (int i = 0; i < attributeNames.size(); i++) {
            QnameCtx qnameCtx = attributeNames.get(i);
            String lexical = qnameCtx.getText();
            if (isNamespaceDeclaration(lexical)) {
                continue;
            }
            Name attributeName = parseName.apply(qnameCtx, NameRole.NO_DEFAULT_NAMESPACE);

            List<Expression> value = getAttributeValuesExpressionsList(
                    attributeValues.get(i), true, tokenStream, translationContext, visitExpr);
            AttributeNodeExpression attributeNode = new AttributeNodeExpression(
                    attributeName,
                    value,
                    translationContext.metadata(
                            qnameCtx.getStart(),
                            attributeValues.get(i).context().getStop()));
            attributes.add(attributeNode);
        }

        return new DirAttributeProcessingResult(attributes, namespaceDeclarations);
    }

    /**
     * Checks if a lexical attribute name represents a namespace declaration ({@code xmlns} or {@code xmlns:prefix}).
     *
     * @param lexical the attribute name string
     * @return {@code true} if the name is a namespace declaration, {@code false} otherwise
     */
    private static boolean isNamespaceDeclaration(String lexical) {
        return "xmlns".equals(lexical) || lexical.startsWith("xmlns:");
    }

    /**
     * Translates a direct attribute value (quoted or apostrophe string) into a list of literal content and enclosed
     * expressions.
     *
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the attribute value context adapter
     * @param allowEnclosedExpressions whether enclosed expressions {@code {expr}} are permitted in this attribute
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param visitExpr visitor function for expressions
     * @return the list of translated expressions representing the attribute value components
     */
    private static <ExprCtx extends ParserRuleContext> List<Expression> getAttributeValuesExpressionsList(
            DirAttributeValueContext<ExprCtx> ctx,
            boolean allowEnclosedExpressions,
            CommonTokenStream tokenStream,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        if (ctx.quotedValue() != null) {
            return processQuotedValue(
                    tokenStream,
                    ctx.quotedValue(),
                    translationContext::metadata,
                    translationContext::metadata,
                    (DirAttributeContentContext<ExprCtx> c) -> processAttributeContent(
                            c, allowEnclosedExpressions, tokenStream, translationContext, visitExpr),
                    ctx.contentAdapter());
        }
        throw new UnsupportedOperationException(
                "Unsupported attribute value: " + ctx.context().getText());
    }

    /**
     * Extracts and validates the static URI literal from a namespace declaration attribute value.
     *
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the attribute value context adapter
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param visitExpr visitor function for expressions
     * @return the resolved namespace URI string
     * @throws NamespaceDeclarationAttributeEnclosedExpressionException if the attribute contains an enclosed expression
     */
    private static <ExprCtx extends ParserRuleContext> String getNamespaceDeclarationUri(
            DirAttributeValueContext<ExprCtx> ctx,
            CommonTokenStream tokenStream,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        List<Expression> uriExpressions =
                getAttributeValuesExpressionsList(ctx, false, tokenStream, translationContext, visitExpr);
        StringBuilder uriBuilder = new StringBuilder();
        for (Expression expression : uriExpressions) {
            if (!(expression instanceof AttributeNodeContentExpression attributeContent)) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        translationContext.metadata(ctx.context()));
            }
            uriBuilder.append(attributeContent.getContent());
        }
        return uriBuilder.toString();
    }

    /**
     * Translates a single attribute content chunk (enclosed expression or literal text).
     *
     * @param <ExprCtx> the parse-tree context type for expressions
     * @param ctx the attribute content context adapter
     * @param allowEnclosedExpressions whether enclosed expressions are permitted
     * @param tokenStream the common token stream
     * @param translationContext the current translation context
     * @param visitExpr visitor function for expressions
     * @return the list containing the translated expression
     */
    private static <ExprCtx extends ParserRuleContext> List<Expression> processAttributeContent(
            DirAttributeContentContext<ExprCtx> ctx,
            boolean allowEnclosedExpressions,
            CommonTokenStream tokenStream,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        if (ctx.isEnclosed()) {
            if (!allowEnclosedExpressions) {
                throw new NamespaceDeclarationAttributeEnclosedExpressionException(
                        "Namespace declaration attributes cannot contain enclosed expressions.",
                        translationContext.metadata(ctx.context()));
            }
            return List.of(visitExpr.apply(ctx.expr()));
        }

        String childText = tokenStream.getText(ctx.context().getSourceInterval());
        validateLiteral(childText, ctx.context(), translationContext::metadata);
        String processedContent = processLiteralContent(childText);
        ParseTree child = ctx.context().getChildCount() > 0 ? ctx.context().getChild(0) : ctx.context();
        return List.of(new AttributeNodeContentExpression(processedContent, translationContext.metadata(child)));
    }

    /**
     * Iterates the children of a quoted attribute value (between opening and closing delimiters), preserving
     * hidden-channel tokens, unescaping predefined XML entities, resolving escaped quote characters, and
     * delegating attribute content chunks to the provided processor.
     */
    private static <ExprCtx extends ParserRuleContext> List<Expression> processQuotedValue(
            CommonTokenStream tokenStream,
            ParserRuleContext ctx,
            Function<ParseTree, ExceptionMetadata> metadataFactory,
            BiFunction<ParseTree, ParseTree, ExceptionMetadata> rangeMetadataFactory,
            Function<DirAttributeContentContext<ExprCtx>, List<Expression>> contentProcessor,
            Function<ParserRuleContext, DirAttributeContentContext<ExprCtx>> contentAdapter) {
        AttributeValueBuilder result = new AttributeValueBuilder(rangeMetadataFactory);
        Token previousToken = ctx.getStart();
        String delimiter = previousToken.getText();
        String escapeSequence = delimiter + delimiter;

        // Skip the opening and closing delimiter tokens.
        for (int i = 1; i < ctx.getChildCount() - 1; i++) {
            ParseTree child = ctx.getChild(i);
            appendHiddenText(tokenStream, result, previousToken, child, metadataFactory);

            String childText = child.getText();
            if (childText.startsWith("&") && childText.endsWith(";")) {
                result.append(
                        new AttributeNodeContentExpression(
                                StringEscapeUtils.unescapeXml(childText), metadataFactory.apply(child)),
                        child);
            } else if (childText.equals(escapeSequence)) {
                result.append(new AttributeNodeContentExpression(delimiter, metadataFactory.apply(child)), child);
            } else if (child instanceof ParserRuleContext ruleContext) {
                DirAttributeContentContext<ExprCtx> contentCtx = contentAdapter.apply(ruleContext);
                if (contentCtx != null) {
                    for (Expression expression : contentProcessor.apply(contentCtx)) {
                        result.append(expression, child);
                    }
                }
            }
            previousToken = getStopToken(child);
        }

        appendHiddenText(tokenStream, result, previousToken, ctx, metadataFactory);
        return result.finish();
    }

    /**
     * Merges element content expressions and intervening hidden-channel whitespace/text, respecting the boundary
     * space preservation setting.
     */
    private static <T extends ParserRuleContext> List<Expression> mergeElementContent(
            CommonTokenStream tokenStream,
            Token firstContentToken,
            List<T> children,
            boolean preserveBoundarySpace,
            Function<T, Expression> contentProcessor) {
        ElementContentBuilder result = new ElementContentBuilder(preserveBoundarySpace);
        Token previousToken = firstContentToken;

        for (T child : children) {
            Expression expression = contentProcessor.apply(child);
            result.appendHiddenText(
                    TokenStreamUtils.getHiddenTextAfter(tokenStream, previousToken.getTokenIndex()),
                    expression.getMetadata());
            result.append(expression);
            previousToken = child.getStop();
        }

        result.appendHiddenText(
                TokenStreamUtils.getHiddenTextAfter(tokenStream, previousToken.getTokenIndex()),
                ExceptionMetadata.EMPTY_METADATA);
        return result.finish();
    }

    /**
     * Checks if a string consists exclusively of whitespace characters and is non-empty.
     *
     * @param value the string to check
     * @return {@code true} if non-empty and all whitespace, {@code false} otherwise
     */
    private static boolean isWhitespaceOnly(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return !value.isEmpty();
    }

    /**
     * Processes literal text within a direct constructor, unescaping XML entities and resolving double braces
     * ({@code {{}} and {@code }}}).
     *
     * @param content the raw literal content
     * @return the processed literal text
     */
    private static String processLiteralContent(String content) {
        if (content.startsWith("&") && content.endsWith(";")) {
            return StringEscapeUtils.unescapeXml(content);
        }
        if (content.equals("{{")) {
            return "{";
        }
        if (content.equals("}}")) {
            return "}";
        }
        return content;
    }

    /**
     * Validates that literal attribute content does not contain forbidden characters (such as {@code '<'}).
     *
     * @param source the literal text to validate
     * @param tree the source parse-tree node
     * @param metadataFactory metadata factory function
     * @throws ParsingException if the literal contains an invalid character
     */
    private static void validateLiteral(
            String source, ParseTree tree, Function<ParseTree, ExceptionMetadata> metadataFactory) {
        if (source.indexOf('<') >= 0) {
            throw new ParsingException(
                    "A direct attribute value must not contain a literal '<' character.", metadataFactory.apply(tree));
        }
    }

    /**
     * Retrieves hidden-channel tokens following {@code previousToken} and appends them to the attribute builder.
     */
    private static void appendHiddenText(
            CommonTokenStream tokenStream,
            AttributeValueBuilder result,
            Token previousToken,
            ParseTree tree,
            Function<ParseTree, ExceptionMetadata> metadataFactory) {
        String hiddenText = TokenStreamUtils.getHiddenTextAfter(tokenStream, previousToken.getTokenIndex());
        validateLiteral(hiddenText, tree, metadataFactory);
        result.appendText(hiddenText, tree);
    }

    /**
     * Resolves the stop token for a parse-tree node.
     *
     * @param tree the parse tree node (either {@link ParserRuleContext} or {@link TerminalNode})
     * @return the stop token
     */
    private static Token getStopToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStop();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new IllegalArgumentException(
                "Cannot get stop token from parse tree: " + tree.getClass().getName());
    }
}
