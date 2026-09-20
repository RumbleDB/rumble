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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record TryCatchExprContext<T extends ParserRuleContext, C extends ParserRuleContext>(
        T tryExpr, List<Catch<T, C>> catches, ParserRuleContext context) {

    public record Catch<T extends ParserRuleContext, C extends ParserRuleContext>(
            T catchExpr, List<C> nameTests, ParserRuleContext context) {}

    public static TryCatchExprContext<JsoniqParser.ExprContext, JsoniqParser.NameTestContext> from(
            JsoniqParser.TryCatchExprContext c) {
        List<Catch<JsoniqParser.ExprContext, JsoniqParser.NameTestContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (JsoniqParser.CatchClauseContext catchCtx : c.catches) {
                catches.add(new Catch<>(catchCtx.catch_expression, catchCtx.nameTest(), catchCtx));
            }
        }
        return new TryCatchExprContext<>(c.try_expression, catches, c);
    }

    public static TryCatchExprContext<XQueryParser.ExprContext, XQueryParser.NameTestContext> from(
            XQueryParser.TryCatchExprContext c) {
        List<Catch<XQueryParser.ExprContext, XQueryParser.NameTestContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (XQueryParser.CatchClauseContext catchCtx : c.catches) {
                catches.add(new Catch<>(catchCtx.catch_expression, catchCtx.nameTest(), catchCtx));
            }
        }
        return new TryCatchExprContext<>(c.try_expression, catches, c);
    }
}
