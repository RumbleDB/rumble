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

public record TryCatchExprContext<ExprCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>(
        ExprCtx tryExpr, List<Catch<ExprCtx, EqNameCtx>> catches, ParserRuleContext context) {

    public record Catch<ExprCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>(
            ExprCtx catchExpr, List<NameTestContext<EqNameCtx>> nameTests, ParserRuleContext context) {}

    public static TryCatchExprContext<JsoniqParser.ExprContext, JsoniqParser.EqNameContext> from(
            JsoniqParser.TryCatchExprContext c) {
        List<Catch<JsoniqParser.ExprContext, JsoniqParser.EqNameContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (JsoniqParser.CatchClauseContext catchCtx : c.catches) {
                List<NameTestContext<JsoniqParser.EqNameContext>> nameTests = new ArrayList<>(
                        catchCtx.nameTest() != null ? catchCtx.nameTest().size() : 0);
                if (catchCtx.nameTest() != null) {
                    for (JsoniqParser.NameTestContext nt : catchCtx.nameTest()) {
                        nameTests.add(NameTestContext.from(nt));
                    }
                }
                catches.add(new Catch<>(catchCtx.catch_expression, nameTests, catchCtx));
            }
        }
        return new TryCatchExprContext<>(c.try_expression, catches, c);
    }

    public static TryCatchExprContext<XQueryParser.ExprContext, XQueryParser.EqNameContext> from(
            XQueryParser.TryCatchExprContext c) {
        List<Catch<XQueryParser.ExprContext, XQueryParser.EqNameContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (XQueryParser.CatchClauseContext catchCtx : c.catches) {
                List<NameTestContext<XQueryParser.EqNameContext>> nameTests = new ArrayList<>(
                        catchCtx.nameTest() != null ? catchCtx.nameTest().size() : 0);
                if (catchCtx.nameTest() != null) {
                    for (XQueryParser.NameTestContext nt : catchCtx.nameTest()) {
                        nameTests.add(NameTestContext.from(nt));
                    }
                }
                catches.add(new Catch<>(catchCtx.catch_expression, nameTests, catchCtx));
            }
        }
        return new TryCatchExprContext<>(c.try_expression, catches, c);
    }
}
