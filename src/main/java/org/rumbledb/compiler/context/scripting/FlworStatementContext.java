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

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record FlworStatementContext<
        ForClauseCtx extends ParserRuleContext,
        LetClauseCtx extends ParserRuleContext,
        StmtCtx extends ParserRuleContext>(
        ForClauseCtx startFor,
        LetClauseCtx startLet,
        List<ParseTree> intermediateClauses,
        StmtCtx returnStmt,
        ParserRuleContext context) {

    public static FlworStatementContext<
                    JsoniqParser.ForClauseContext, JsoniqParser.LetClauseContext, JsoniqParser.StatementContext>
            from(JsoniqParser.FlworStatementContext c) {
        List<ParseTree> intermediate = c.children != null && c.children.size() > 3
                ? c.children.subList(1, c.children.size() - 2)
                : Collections.emptyList();
        return new FlworStatementContext<>(c.start_for, c.start_let, intermediate, c.returnStmt, c);
    }

    public static FlworStatementContext<
                    XQueryParser.ForClauseContext, XQueryParser.LetClauseContext, XQueryParser.StatementContext>
            from(XQueryParser.FlworStatementContext c) {
        List<ParseTree> intermediate = c.children != null && c.children.size() > 3
                ? c.children.subList(1, c.children.size() - 2)
                : Collections.emptyList();
        return new FlworStatementContext<>(c.start_for, c.start_let, intermediate, c.returnStmt, c);
    }
}
