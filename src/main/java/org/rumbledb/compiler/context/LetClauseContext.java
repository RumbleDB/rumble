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

public record LetClauseContext<
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        ExprSingleCtx extends ParserRuleContext>(
        List<LetVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx>> vars, ParserRuleContext context) {

    public static LetClauseContext<
                    JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext, JsoniqParser.ExprSingleContext>
            from(JsoniqParser.LetClauseContext c) {
        List<
                        LetVarContext<
                                JsoniqParser.VarBindingContext,
                                JsoniqParser.SequenceTypeContext,
                                JsoniqParser.ExprSingleContext>>
                vars = c.vars.stream().map(LetVarContext::from).toList();
        return new LetClauseContext<>(vars, c);
    }

    public static LetClauseContext<
                    XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext, XQueryParser.ExprSingleContext>
            from(XQueryParser.LetClauseContext c) {
        List<
                        LetVarContext<
                                XQueryParser.VarBindingContext,
                                XQueryParser.SequenceTypeContext,
                                XQueryParser.ExprSingleContext>>
                vars = c.vars.stream().map(LetVarContext::from).toList();
        return new LetClauseContext<>(vars, c);
    }
}
