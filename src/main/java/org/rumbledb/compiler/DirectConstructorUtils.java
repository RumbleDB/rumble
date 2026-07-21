package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.xml.AttributeNodeContentExpression;
import org.rumbledb.expressions.xml.DirectCommentConstructorExpression;
import org.rumbledb.expressions.xml.TextNodeExpression;

/** Shared processing for JSONiq and XQuery direct element and attribute constructors. */
final class DirectConstructorUtils {

    private static final class AttributeValueBuilder {
        private final BiFunction<ParseTree, ParseTree, ExceptionMetadata> metadataFactory;
        private final List<Expression> expressions = new ArrayList<>();
        private StringBuilder text;

        // Used to track the first and last parse-tree nodes of a literal run so that we can assign a source range to
        // the merged content
        private ParseTree firstTextTree;
        private ParseTree lastTextTree;

        /** Creates a builder that can assign a source range to each merged literal fragment. */
        AttributeValueBuilder(BiFunction<ParseTree, ParseTree, ExceptionMetadata> metadataFactory) {
            this.metadataFactory = metadataFactory;
        }

        /** Adds literal text to the current run, ignoring empty fragments. */
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

        /** Adds an expression, merging literal attribute content and preserving non-literal boundaries. */
        void append(Expression expression, ParseTree source) {
            if (expression instanceof AttributeNodeContentExpression textExpression) {
                appendText(textExpression.getContent(), source);
                return;
            }
            flushText();
            this.expressions.add(expression);
        }

        /** Flushes the final literal run and returns the normalized expression sequence. */
        List<Expression> finish() {
            flushText();
            return this.expressions;
        }

        /** Materializes the current literal run as one attribute content expression. */
        private void flushText() {
            if (this.text == null) {
                return;
            }
            this.expressions.add(
                new AttributeNodeContentExpression(
                        this.text.toString(),
                        this.metadataFactory.apply(this.firstTextTree, this.lastTextTree)
                )
            );
            this.text = null;
            this.firstTextTree = null;
            this.lastTextTree = null;
        }
    }

    private static final class TextRunState {
        private final StringBuilder text;
        private final ExceptionMetadata firstTextMetadata;
        private final boolean boundaryWhitespaceOnly;

        private TextRunState(
                StringBuilder text,
                ExceptionMetadata firstTextMetadata,
                boolean boundaryWhitespaceOnly
        ) {
            this.text = text;
            this.firstTextMetadata = firstTextMetadata;
            this.boundaryWhitespaceOnly = boundaryWhitespaceOnly;
        }
    }

    /**
     * Converts a quoted direct attribute value into its ordered content expressions.
     *
     * <p>
     * This method restores whitespace and comments from the hidden token channel, expands XML references and
     * doubled delimiters, delegates enclosed expressions and literal fragments to the language-specific visitor,
     * and merges adjacent literal fragments while preserving their combined source range.
     *
     * @param tokenStream token stream used to recover hidden attribute content
     * @param ctx complete quoted attribute-value context
     * @param allowEnclosedExpressions whether enclosed expressions are valid in this attribute
     * @param metadataFactory creates metadata for one parse-tree node
     * @param rangeMetadataFactory creates metadata spanning two parse-tree nodes
     * @param contentProcessor processes grammar-specific attribute content
     * @return the normalized sequence of attribute content expressions
     */
    static List<Expression> processQuotedValue(
            CommonTokenStream tokenStream,
            ParserRuleContext ctx,
            boolean allowEnclosedExpressions,
            Function<ParseTree, ExceptionMetadata> metadataFactory,
            BiFunction<ParseTree, ParseTree, ExceptionMetadata> rangeMetadataFactory,
            BiFunction<ParserRuleContext, Boolean, List<Expression>> contentProcessor
    ) {
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
                            StringEscapeUtils.unescapeXml(childText),
                            metadataFactory.apply(child)
                    ),
                    child
                );
            } else if (childText.equals(escapeSequence)) {
                result.append(
                    new AttributeNodeContentExpression(delimiter, metadataFactory.apply(child)),
                    child
                );
            } else {
                for (
                    Expression expression : contentProcessor.apply(
                        (ParserRuleContext) child,
                        allowEnclosedExpressions
                    )
                ) {
                    result.append(expression, child);
                }
            }
            previousToken = getStopToken(child);
        }

        appendHiddenText(tokenStream, result, previousToken, ctx, metadataFactory);
        return result.finish();
    }

    /**
     * Concatenates hidden-channel tokens immediately following a token.
     *
     * @return the hidden text, or an empty string when no hidden tokens follow
     */
    static String getHiddenTextAfter(CommonTokenStream tokenStream, int previousTokenIndex) {
        List<Token> hidden = tokenStream.getHiddenTokensToRight(previousTokenIndex);
        if (hidden == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Token token : hidden) {
            result.append(token.getText());
        }
        return result.toString();
    }

    /**
     * Processes direct element children and normalizes adjacent text nodes into a single node.
     *
     * <p>
     * Hidden-channel content between parsed children is restored, empty text nodes are omitted, and non-text
     * expressions delimit text runs. The first text node in each run supplies the resulting metadata.
     *
     * @param tokenStream token stream used to recover hidden element content
     * @param firstContentToken token immediately before the first child
     * @param children direct element content contexts in source order
     * @param contentProcessor converts a grammar-specific child context to an expression
     * @return element content with adjacent text nodes merged
     */
    static <T extends ParserRuleContext> List<Expression> mergeElementContent(
            CommonTokenStream tokenStream,
            Token firstContentToken,
            List<T> children,
            boolean preserveBoundarySpace,
            Function<T, Expression> contentProcessor
    ) {
        List<Expression> content = new ArrayList<>();
        StringBuilder text = null;
        ExceptionMetadata firstTextMetadata = null;
        boolean boundaryWhitespaceOnly = true;
        Token previousToken = firstContentToken;

        for (T child : children) {
            Expression expression = contentProcessor.apply(child);
            if (expression instanceof TextNodeExpression textNode) {
                if (textNode.getContent().isEmpty()) {
                    previousToken = child.getStop();
                    continue;
                }
                if (text == null) {
                    text = new StringBuilder();
                    firstTextMetadata = textNode.getMetadata();
                    boundaryWhitespaceOnly = true;
                }
                String hiddenText = getHiddenTextAfter(tokenStream, previousToken.getTokenIndex());
                if (textNode.getContent().contains("<!--") && textNode.getContent().contains("-->")) {
                    if (isDirectCommentLiteral(hiddenText)) {
                        flushTextNode(content, text, firstTextMetadata, boundaryWhitespaceOnly, preserveBoundarySpace);
                        text = new StringBuilder();
                        firstTextMetadata = textNode.getMetadata();
                        boundaryWhitespaceOnly = true;
                        content.add(
                            new DirectCommentConstructorExpression(
                                    hiddenText.substring(4, hiddenText.length() - 3),
                                    ExceptionMetadata.EMPTY_METADATA
                            )
                        );
                    } else {
                        boundaryWhitespaceOnly = appendBoundarySegment(
                            text,
                            hiddenText,
                            true,
                            boundaryWhitespaceOnly
                        );
                    }
                    TextRunState state = appendTextWithEmbeddedComments(
                        content,
                        text,
                        textNode.getContent(),
                        firstTextMetadata,
                        textNode.getMetadata(),
                        boundaryWhitespaceOnly,
                        preserveBoundarySpace
                    );
                    text = state.text;
                    firstTextMetadata = state.firstTextMetadata;
                    boundaryWhitespaceOnly = state.boundaryWhitespaceOnly;
                    previousToken = child.getStop();
                    continue;
                }
                if (isDirectCommentLiteral(hiddenText)) {
                    flushTextNode(content, text, firstTextMetadata, boundaryWhitespaceOnly, preserveBoundarySpace);
                    text = new StringBuilder();
                    firstTextMetadata = textNode.getMetadata();
                    boundaryWhitespaceOnly = true;
                    content.add(
                        new DirectCommentConstructorExpression(
                                hiddenText.substring(4, hiddenText.length() - 3),
                                ExceptionMetadata.EMPTY_METADATA
                        )
                    );
                } else {
                    boundaryWhitespaceOnly = appendBoundarySegment(
                        text,
                        hiddenText,
                        true,
                        boundaryWhitespaceOnly
                    );
                }
                text.append(textNode.getContent());
                boundaryWhitespaceOnly = boundaryWhitespaceOnly
                    && textNode.isBoundaryWhitespace()
                    && isWhitespaceOnly(textNode.getContent());
            } else {
                if (text != null) {
                    boundaryWhitespaceOnly = appendBoundarySegment(
                        text,
                        getHiddenTextAfter(tokenStream, previousToken.getTokenIndex()),
                        true,
                        boundaryWhitespaceOnly
                    );
                    flushTextNode(content, text, firstTextMetadata, boundaryWhitespaceOnly, preserveBoundarySpace);
                    text = null;
                    firstTextMetadata = null;
                }
                content.add(expression);
            }
            previousToken = child.getStop();
        }

        if (text != null) {
            boundaryWhitespaceOnly = appendBoundarySegment(
                text,
                getHiddenTextAfter(tokenStream, previousToken.getTokenIndex()),
                true,
                boundaryWhitespaceOnly
            );
            flushTextNode(content, text, firstTextMetadata, boundaryWhitespaceOnly, preserveBoundarySpace);
        }
        return content;
    }

    private static void flushTextNode(
            List<Expression> content,
            StringBuilder text,
            ExceptionMetadata metadata,
            boolean boundaryWhitespaceOnly,
            boolean preserveBoundarySpace
    ) {
        if (text.length() == 0) {
            return;
        }
        if (!preserveBoundarySpace && boundaryWhitespaceOnly && isWhitespaceOnly(text.toString())) {
            return;
        }
        content.add(new TextNodeExpression(text.toString(), metadata));
    }

    private static boolean isDirectCommentLiteral(String text) {
        return text != null && text.startsWith("<!--") && text.endsWith("-->");
    }

    private static TextRunState appendTextWithEmbeddedComments(
            List<Expression> content,
            StringBuilder textBuilder,
            String sourceText,
            ExceptionMetadata firstTextMetadata,
            ExceptionMetadata fragmentMetadata,
            boolean boundaryWhitespaceOnly,
            boolean preserveBoundarySpace
    ) {
        int position = 0;
        while (position < sourceText.length()) {
            int commentStart = sourceText.indexOf("<!--", position);
            if (commentStart < 0) {
                String trailingText = sourceText.substring(position);
                textBuilder.append(trailingText);
                return new TextRunState(
                        textBuilder,
                        firstTextMetadata,
                        boundaryWhitespaceOnly && isWhitespaceOnly(trailingText)
                );
            }
            String prefix = sourceText.substring(position, commentStart);
            textBuilder.append(prefix);
            boundaryWhitespaceOnly = boundaryWhitespaceOnly && isWhitespaceOnly(prefix);
            flushTextNode(content, textBuilder, firstTextMetadata, boundaryWhitespaceOnly, preserveBoundarySpace);
            textBuilder = new StringBuilder();
            firstTextMetadata = fragmentMetadata;
            boundaryWhitespaceOnly = true;

            int commentEnd = sourceText.indexOf("-->", commentStart);
            if (commentEnd < 0) {
                String trailingText = sourceText.substring(commentStart);
                textBuilder.append(trailingText);
                return new TextRunState(
                        textBuilder,
                        firstTextMetadata,
                        boundaryWhitespaceOnly && isWhitespaceOnly(trailingText)
                );
            }
            content.add(
                new DirectCommentConstructorExpression(
                        sourceText.substring(commentStart + 4, commentEnd),
                        ExceptionMetadata.EMPTY_METADATA
                )
            );
            position = commentEnd + 3;
        }
        return new TextRunState(textBuilder, firstTextMetadata, boundaryWhitespaceOnly);
    }

    private static boolean appendBoundarySegment(
            StringBuilder text,
            String segment,
            boolean boundaryCandidate,
            boolean boundaryWhitespaceOnly
    ) {
        if (segment.isEmpty()) {
            return boundaryWhitespaceOnly;
        }
        text.append(segment);
        return boundaryWhitespaceOnly && boundaryCandidate && isWhitespaceOnly(segment);
    }

    private static boolean isWhitespaceOnly(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return !value.isEmpty();
    }

    /**
     * Expands an XML entity or character reference and collapses an escaped brace pair.
     *
     * @return the decoded literal, or the original content when it is not an escape
     */
    static String processLiteralContent(String content) {
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
     * Rejects a literal less-than sign in direct attribute content.
     *
     * @param source literal source text to validate
     * @param tree parse-tree node used to locate a validation error
     * @param metadataFactory creates error metadata for the supplied node
     */
    static void validateLiteral(
            String source,
            ParseTree tree,
            Function<ParseTree, ExceptionMetadata> metadataFactory
    ) {
        if (source.indexOf('<') >= 0) {
            throw new ParsingException(
                    "A direct attribute value must not contain a literal '<' character.",
                    metadataFactory.apply(tree)
            );
        }
    }

    /** Restores, validates, and appends hidden content following the previous visible token. */
    private static void appendHiddenText(
            CommonTokenStream tokenStream,
            AttributeValueBuilder result,
            Token previousToken,
            ParseTree tree,
            Function<ParseTree, ExceptionMetadata> metadataFactory
    ) {
        String hiddenText = getHiddenTextAfter(tokenStream, previousToken.getTokenIndex());
        validateLiteral(hiddenText, tree, metadataFactory);
        result.appendText(hiddenText, tree);
    }

    /** Returns the final token represented by either a rule context or a terminal node. */
    private static Token getStopToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStop();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new IllegalArgumentException("Cannot get stop token from parse tree: " + tree.getClass().getName());
    }
}
