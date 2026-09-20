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

public record TypeswitchExprContext<
        T extends ParserRuleContext,
        S extends ParserRuleContext,
        V extends ParserRuleContext,
        Q extends ParserRuleContext>(
        T cond, List<Case<S, V, Q>> cases, V defaultVar, S def, ParserRuleContext context) {

    public record Case<S extends ParserRuleContext, V extends ParserRuleContext, Q extends ParserRuleContext>(
            V varRef, List<Q> union, S ret) {}

    public static TypeswitchExprContext<
                    JsoniqParser.ExprContext,
                    JsoniqParser.ExprSingleContext,
                    JsoniqParser.VarBindingContext,
                    JsoniqParser.SequenceTypeContext>
            from(JsoniqParser.TypeswitchExprContext c) {
        List<Case<JsoniqParser.ExprSingleContext, JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext>>
                cases = new ArrayList<>(c.cses != null ? c.cses.size() : 0);
        if (c.cses != null) {
            for (JsoniqParser.CaseClauseContext expr : c.cses) {
                cases.add(new Case<>(expr.var_ref, expr.union, expr.ret));
            }
        }
        return new TypeswitchExprContext<>(c.cond, cases, c.var_ref, c.def, c);
    }

    public static TypeswitchExprContext<
                    XQueryParser.ExprContext,
                    XQueryParser.ExprSingleContext,
                    XQueryParser.VarBindingContext,
                    XQueryParser.SequenceTypeContext>
            from(XQueryParser.TypeswitchExprContext c) {
        List<Case<XQueryParser.ExprSingleContext, XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext>>
                cases = new ArrayList<>(c.cses != null ? c.cses.size() : 0);
        if (c.cses != null) {
            for (XQueryParser.CaseClauseContext expr : c.cses) {
                cases.add(new Case<>(expr.var_ref, expr.union, expr.ret));
            }
        }
        return new TypeswitchExprContext<>(c.cond, cases, c.var_ref, c.def, c);
    }
}
