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

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record SwitchStatementContext<
        CondExprCtx extends ParserRuleContext,
        CaseExprCtx extends ParserRuleContext,
        StmtCtx extends ParserRuleContext>(
        CondExprCtx cond, List<Case<CaseExprCtx, StmtCtx>> cases, StmtCtx def, ParserRuleContext context) {

    public record Case<CaseExprCtx extends ParserRuleContext, StmtCtx extends ParserRuleContext>(
            List<CaseExprCtx> cond, StmtCtx ret) {}

    public static SwitchStatementContext<
                    JsoniqParser.ExprContext, JsoniqParser.ExprSingleContext, JsoniqParser.StatementContext>
            from(JsoniqParser.SwitchStatementContext c) {
        List<Case<JsoniqParser.ExprSingleContext, JsoniqParser.StatementContext>> cases =
                new ArrayList<>(c.cases != null ? c.cases.size() : 0);
        if (c.cases != null) {
            for (JsoniqParser.SwitchCaseStatementContext caseClause : c.cases) {
                cases.add(new Case<>(caseClause.cond, caseClause.ret));
            }
        }
        return new SwitchStatementContext<>(c.condExpr, cases, c.def, c);
    }

    public static SwitchStatementContext<
                    XQueryParser.ExprContext, XQueryParser.ExprSingleContext, XQueryParser.StatementContext>
            from(XQueryParser.SwitchStatementContext c) {
        List<Case<XQueryParser.ExprSingleContext, XQueryParser.StatementContext>> cases =
                new ArrayList<>(c.cases != null ? c.cases.size() : 0);
        if (c.cases != null) {
            for (XQueryParser.SwitchCaseStatementContext caseClause : c.cases) {
                cases.add(new Case<>(caseClause.cond, caseClause.ret));
            }
        }
        return new SwitchStatementContext<>(c.condExpr, cases, c.def, c);
    }
}
