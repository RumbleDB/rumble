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

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record GroupByClauseContext<
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        ExprSingleCtx extends ParserRuleContext,
        UriLiteralCtx extends ParserRuleContext>(
        List<GroupByVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx, UriLiteralCtx>> vars,
        ParserRuleContext context) {

    public static GroupByClauseContext<
                    JsoniqParser.VarBindingContext,
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.ExprSingleContext,
                    JsoniqParser.UriLiteralContext>
            from(JsoniqParser.GroupByClauseContext c) {
        List<
                        GroupByVarContext<
                                JsoniqParser.VarBindingContext,
                                JsoniqParser.SequenceTypeContext,
                                JsoniqParser.ExprSingleContext,
                                JsoniqParser.UriLiteralContext>>
                vars = c.vars.stream().map(GroupByVarContext::from).toList();
        return new GroupByClauseContext<>(vars, c);
    }

    public static GroupByClauseContext<
                    XQueryParser.VarBindingContext,
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.ExprSingleContext,
                    XQueryParser.UriLiteralContext>
            from(XQueryParser.GroupByClauseContext c) {
        List<
                        GroupByVarContext<
                                XQueryParser.VarBindingContext,
                                XQueryParser.SequenceTypeContext,
                                XQueryParser.ExprSingleContext,
                                XQueryParser.UriLiteralContext>>
                vars = c.vars.stream().map(GroupByVarContext::from).toList();
        return new GroupByClauseContext<>(vars, c);
    }
}
