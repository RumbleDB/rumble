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
package org.rumbledb.compiler.context.scripting;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.NameTestContext;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record TryCatchStatementContext<BlockStmtCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>(
        BlockStmtCtx tryBlock, List<Catch<BlockStmtCtx, EqNameCtx>> catches, ParserRuleContext context) {

    public record Catch<BlockStmtCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>(
            BlockStmtCtx catchBlock, List<NameTestContext<EqNameCtx>> nameTests, ParserRuleContext context) {}

    public static TryCatchStatementContext<JsoniqParser.BlockStatementContext, JsoniqParser.EqNameContext> from(
            JsoniqParser.TryCatchStatementContext c) {
        List<Catch<JsoniqParser.BlockStatementContext, JsoniqParser.EqNameContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (JsoniqParser.CatchCaseStatementContext catchCtx : c.catches) {
                List<NameTestContext<JsoniqParser.EqNameContext>> nameTests = new ArrayList<>(
                        catchCtx.nameTest() != null ? catchCtx.nameTest().size() : 0);
                if (catchCtx.nameTest() != null) {
                    for (JsoniqParser.NameTestContext nt : catchCtx.nameTest()) {
                        nameTests.add(NameTestContext.from(nt));
                    }
                }
                catches.add(new Catch<>(catchCtx.catch_block, nameTests, catchCtx));
            }
        }
        return new TryCatchStatementContext<>(c.try_block, catches, c);
    }

    public static TryCatchStatementContext<XQueryParser.BlockStatementContext, XQueryParser.EqNameContext> from(
            XQueryParser.TryCatchStatementContext c) {
        List<Catch<XQueryParser.BlockStatementContext, XQueryParser.EqNameContext>> catches =
                new ArrayList<>(c.catches != null ? c.catches.size() : 0);
        if (c.catches != null) {
            for (XQueryParser.CatchCaseStatementContext catchCtx : c.catches) {
                List<NameTestContext<XQueryParser.EqNameContext>> nameTests = new ArrayList<>(
                        catchCtx.nameTest() != null ? catchCtx.nameTest().size() : 0);
                if (catchCtx.nameTest() != null) {
                    for (XQueryParser.NameTestContext nt : catchCtx.nameTest()) {
                        nameTests.add(NameTestContext.from(nt));
                    }
                }
                catches.add(new Catch<>(catchCtx.catch_block, nameTests, catchCtx));
            }
        }
        return new TryCatchStatementContext<>(c.try_block, catches, c);
    }
}
