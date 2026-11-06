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
package org.rumbledb.compiler.context.update;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record InsertExprContext<ExprSingleCtx extends ParserRuleContext>(
        List<PairConstructorContext<ExprSingleCtx>> pairConstructor,
        ExprSingleCtx toInsertExpr,
        ExprSingleCtx posExpr,
        ExprSingleCtx mainExpr,
        ParserRuleContext context) {

    public static InsertExprContext<JsoniqParser.ExprSingleContext> from(JsoniqParser.InsertExprContext c) {
        List<PairConstructorContext<JsoniqParser.ExprSingleContext>> pairs = c.pairConstructor() != null
                ? c.pairConstructor().stream().map(PairConstructorContext::from).toList()
                : Collections.emptyList();
        return new InsertExprContext<>(pairs, c.to_insert_expr, c.pos_expr, c.main_expr, c);
    }

    public static InsertExprContext<XQueryParser.ExprSingleContext> from(XQueryParser.InsertExprContext c) {
        List<PairConstructorContext<XQueryParser.ExprSingleContext>> pairs = c.pairConstructor() != null
                ? c.pairConstructor().stream().map(PairConstructorContext::from).toList()
                : Collections.emptyList();
        return new InsertExprContext<>(pairs, c.to_insert_expr, c.pos_expr, c.main_expr, c);
    }
}
