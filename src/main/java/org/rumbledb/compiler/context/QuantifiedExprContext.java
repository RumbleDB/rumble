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

public record QuantifiedExprContext<
        ExprSingleCtx extends ParserRuleContext,
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext>(
        boolean isUniversal,
        List<Var<ExprSingleCtx, VarBindingCtx, SeqTypeCtx>> vars,
        ExprSingleCtx exprSingle,
        ParserRuleContext context) {

    public record Var<
            ExprSingleCtx extends ParserRuleContext,
            VarBindingCtx extends ParserRuleContext,
            SeqTypeCtx extends ParserRuleContext>(
            VarBindingCtx varBinding, SeqTypeCtx sequenceType, ExprSingleCtx exprSingle, ParserRuleContext context) {}

    public static QuantifiedExprContext<
                    JsoniqParser.ExprSingleContext, JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext>
            from(JsoniqParser.QuantifiedExprContext c) {
        List<Var<JsoniqParser.ExprSingleContext, JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext>>
                vars = new ArrayList<>(c.vars != null ? c.vars.size() : 0);
        if (c.vars != null) {
            for (JsoniqParser.QuantifiedExprVarContext varCtx : c.vars) {
                vars.add(new Var<>(varCtx.varBinding(), varCtx.sequenceType(), varCtx.exprSingle(), varCtx));
            }
        }
        return new QuantifiedExprContext<>(c.ev != null, vars, c.exprSingle(), c);
    }

    public static QuantifiedExprContext<
                    XQueryParser.ExprSingleContext, XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext>
            from(XQueryParser.QuantifiedExprContext c) {
        List<Var<XQueryParser.ExprSingleContext, XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext>>
                vars = new ArrayList<>(c.vars != null ? c.vars.size() : 0);
        if (c.vars != null) {
            for (XQueryParser.QuantifiedExprVarContext varCtx : c.vars) {
                vars.add(new Var<>(varCtx.varBinding(), varCtx.sequenceType(), varCtx.exprSingle(), varCtx));
            }
        }
        return new QuantifiedExprContext<>(c.ev != null, vars, c.exprSingle(), c);
    }
}
