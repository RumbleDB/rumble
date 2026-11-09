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
package org.rumbledb.compiler.context;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record FlworExprContext<ExprSingleCtx extends ParserRuleContext>(
        ParseTree startClause,
        List<ParseTree> intermediateClauses,
        ExprSingleCtx returnExpr,
        ParserRuleContext context) {

    public static FlworExprContext<JsoniqParser.ExprSingleContext> from(JsoniqParser.FlworExprContext c) {
        ParseTree start = c.start_window != null ? c.start_window : (c.start_for == null ? c.start_let : c.start_for);
        return new FlworExprContext<>(start, intermediateClauses(c, start, c.return_expr), c.return_expr, c);
    }

    public static FlworExprContext<XQueryParser.ExprSingleContext> from(XQueryParser.FlworExprContext c) {
        ParseTree start = c.start_window != null ? c.start_window : (c.start_for == null ? c.start_let : c.start_for);
        return new FlworExprContext<>(start, intermediateClauses(c, start, c.return_expr), c.return_expr, c);
    }

    /**
     * Selects the clauses by kind rather than by position, because a parser that recovers from syntax errors can leave
     * out the return keyword and expression, or add error nodes.
     */
    private static List<ParseTree> intermediateClauses(ParserRuleContext flwor, ParseTree start, ParseTree returnExpr) {
        return flwor.children.stream()
                .filter(child -> child instanceof ParserRuleContext && child != start && child != returnExpr)
                .toList();
    }
}
