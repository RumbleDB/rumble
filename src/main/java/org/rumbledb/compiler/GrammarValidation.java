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
package org.rumbledb.compiler;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

/** Syntax constraints checked after parsing so the grammars contain no target-specific predicates. */
final class GrammarValidation {
    private GrammarValidation() {}

    static void validate(ParseTree tree, Parser parser, CommonTokenStream tokens, URI uri, String query) {
        IntervalSet relativePathStarts = parser instanceof XQueryParser
                ? parser.getATN().nextTokens(parser.getATN().ruleToStartState[XQueryParser.RULE_relativePathExpr])
                : null;
        Deque<ParseTree> pending = new ArrayDeque<>();
        pending.push(tree);
        while (!pending.isEmpty()) {
            ParseTree current = pending.pop();
            if (current instanceof JsoniqParser.QnameContext || current instanceof XQueryParser.QnameContext) {
                validateQName((ParserRuleContext) current, uri, query);
            }
            if (current instanceof XQueryParser.PathExprContext path
                    && path.singleslash == null
                    && path.doubleslash == null
                    && path.relative == null) {
                Token next = nextToken(tokens, path.getStop());
                if (relativePathStarts.contains(next.getType())) {
                    throw new ParsingException(
                            "A leading lone slash cannot be followed by a token that starts a relative path.",
                            ExceptionMetadata.fromToken(uri.toString(), path.getStart(), query));
                }
            }
            for (int i = current.getChildCount() - 1; i >= 0; i--) {
                pending.push(current.getChild(i));
            }
        }
    }

    private static void validateQName(ParserRuleContext name, URI uri, String query) {
        String text = name.getText();
        if (name.getStart().getStartIndex() + text.codePointCount(0, text.length())
                != name.getStop().getStopIndex() + 1) {
            throw new ParsingException(
                    "Whitespace and comments are not allowed inside a QName.",
                    ExceptionMetadata.fromTokens(uri.toString(), name.getStart(), name.getStop(), query));
        }
    }

    private static Token nextToken(CommonTokenStream tokens, Token previous) {
        int index = previous.getTokenIndex() + 1;
        Token next = tokens.get(index);
        while (next.getChannel() != Token.DEFAULT_CHANNEL && next.getType() != Token.EOF) {
            next = tokens.get(++index);
        }
        return next;
    }
}
