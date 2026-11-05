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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.parser.xquery.XQueryParser;
import org.rumbledb.parser.xquery.XQueryParserBaseVisitor;

/** Enforces standard XQuery boundaries while leaving scripting available to existing applications. */
final class XQuerySyntaxValidator extends XQueryParserBaseVisitor<Void> {
    private final URI uri;
    private final String query;

    XQuerySyntaxValidator(URI uri, String query) {
        this.uri = uri;
        this.query = query;
    }

    private ParsingException invalid(ParserRuleContext context) {
        var token = context.getStart();
        return new ParsingException(
                "Expected an XQuery expression; scripting extensions are disabled.",
                ExceptionMetadata.fromPoint(
                        this.uri.toString(), token.getLine(), token.getCharPositionInLine(), this.query));
    }

    @Override
    public Void visitModule(XQueryParser.ModuleContext context) {
        if (context.mainModule().size() > 1) {
            throw invalid(context);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitProgram(XQueryParser.ProgramContext context) {
        var body = context.statementsAndOptionalExpr();
        if (body.expr() == null || !body.statements().statement().isEmpty()) {
            throw invalid(context);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitStatementsAndOptionalExpr(XQueryParser.StatementsAndOptionalExprContext context) {
        if (!context.statements().statement().isEmpty()) {
            throw invalid(context);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitBlockExpr(XQueryParser.BlockExprContext context) {
        throw invalid(context);
    }
}
