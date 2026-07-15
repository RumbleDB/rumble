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

final class DirectAttributeValueUtils {

    private DirectAttributeValueUtils() {
    }

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

    private static Token getStopToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStop();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new IllegalArgumentException("Cannot get stop token from parse tree: " + tree.getClass().getName());
    }

    private static final class AttributeValueBuilder {
        private final BiFunction<ParseTree, ParseTree, ExceptionMetadata> metadataFactory;
        private final List<Expression> expressions = new ArrayList<>();
        private StringBuilder text;
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
                appendText(textExpression.getContent(), source);
                return;
            }
            flushText();
            this.expressions.add(expression);
        }

        List<Expression> finish() {
            flushText();
            return this.expressions;
        }

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
}
