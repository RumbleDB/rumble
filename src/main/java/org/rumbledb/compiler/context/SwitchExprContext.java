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

public record SwitchExprContext<CondExprCtx extends ParserRuleContext, CaseExprCtx extends ParserRuleContext>(
        CondExprCtx cond, List<Case<CaseExprCtx>> cases, CaseExprCtx def, ParserRuleContext context) {

    public record Case<CaseExprCtx extends ParserRuleContext>(List<CaseExprCtx> cond, CaseExprCtx ret) {}

    public static SwitchExprContext<JsoniqParser.ExprContext, JsoniqParser.ExprSingleContext> from(
            JsoniqParser.SwitchExprContext c) {
        List<Case<JsoniqParser.ExprSingleContext>> cases = new ArrayList<>(c.cases.size());
        for (JsoniqParser.SwitchCaseClauseContext caseClause : c.cases) {
            cases.add(new Case<>(caseClause.cond, caseClause.ret));
        }
        return new SwitchExprContext<>(c.cond, cases, c.def, c);
    }

    public static SwitchExprContext<XQueryParser.ExprContext, XQueryParser.ExprSingleContext> from(
            XQueryParser.SwitchExprContext c) {
        List<Case<XQueryParser.ExprSingleContext>> cases = new ArrayList<>(c.cases.size());
        for (XQueryParser.SwitchCaseClauseContext caseClause : c.cases) {
            cases.add(new Case<>(caseClause.cond, caseClause.ret));
        }
        return new SwitchExprContext<>(c.cond, cases, c.def, c);
    }
}
