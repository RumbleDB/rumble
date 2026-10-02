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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record OrderByExprContext<ExprSingleCtx extends ParserRuleContext, UriLiteralCtx extends ParserRuleContext>(
        ExprSingleCtx exprSingle,
        UriLiteralCtx uriLiteral,
        boolean ascending,
        OrderByClauseSortingKey.EMPTY_ORDER emptyOrder,
        ParserRuleContext context) {

    public static OrderByExprContext<JsoniqParser.ExprSingleContext, JsoniqParser.UriLiteralContext> from(
            JsoniqParser.OrderByExprContext c) {
        boolean ascending = c.desc == null || c.desc.getText().isEmpty();
        OrderByClauseSortingKey.EMPTY_ORDER emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.NONE;
        if (c.gr != null && !c.gr.getText().isEmpty()) {
            emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.GREATEST;
        }
        if (c.ls != null && !c.ls.getText().isEmpty()) {
            emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.LEAST;
        }
        return new OrderByExprContext<>(c.exprSingle(), c.uriLiteral(), ascending, emptyOrder, c);
    }

    public static OrderByExprContext<XQueryParser.ExprSingleContext, XQueryParser.UriLiteralContext> from(
            XQueryParser.OrderByExprContext c) {
        boolean ascending = c.desc == null || c.desc.getText().isEmpty();
        OrderByClauseSortingKey.EMPTY_ORDER emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.NONE;
        if (c.gr != null && !c.gr.getText().isEmpty()) {
            emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.GREATEST;
        }
        if (c.ls != null && !c.ls.getText().isEmpty()) {
            emptyOrder = OrderByClauseSortingKey.EMPTY_ORDER.LEAST;
        }
        return new OrderByExprContext<>(c.exprSingle(), c.uriLiteral(), ascending, emptyOrder, c);
    }
}
