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

public record ArrowExprContext<
        T extends ParserRuleContext,
        E extends ParserRuleContext,
        V extends ParserRuleContext,
        P extends ParserRuleContext,
        A extends ParserRuleContext>(
        T mainExpr, List<ArrowCall<E, V, P, A>> calls, ParserRuleContext context) {

    public record ArrowCall<
            E extends ParserRuleContext,
            V extends ParserRuleContext,
            P extends ParserRuleContext,
            A extends ParserRuleContext>(
            E eqName, V varRef, P parenthesizedExpr, A argumentList) {}

    public static ArrowExprContext<
                    JsoniqParser.UnaryExprContext,
                    JsoniqParser.EqNameContext,
                    JsoniqParser.VarRefContext,
                    JsoniqParser.ParenthesizedExprContext,
                    JsoniqParser.ArgumentListContext>
            from(JsoniqParser.ArrowExprContext c) {
        List<
                        ArrowCall<
                                JsoniqParser.EqNameContext,
                                JsoniqParser.VarRefContext,
                                JsoniqParser.ParenthesizedExprContext,
                                JsoniqParser.ArgumentListContext>>
                calls = new ArrayList<>(c.function.size());
        for (int i = 0; i < c.function.size(); ++i) {
            JsoniqParser.ArrowFunctionSpecifierContext fn = c.function.get(i);
            JsoniqParser.ArgumentListContext args = c.arguments.get(i);
            calls.add(new ArrowCall<>(fn.eqName(), fn.varRef(), fn.parenthesizedExpr(), args));
        }
        return new ArrowExprContext<>(c.main_expr, calls, c);
    }

    public static ArrowExprContext<
                    XQueryParser.UnaryExprContext,
                    XQueryParser.EqNameContext,
                    XQueryParser.VarRefContext,
                    XQueryParser.ParenthesizedExprContext,
                    XQueryParser.ArgumentListContext>
            from(XQueryParser.ArrowExprContext c) {
        List<
                        ArrowCall<
                                XQueryParser.EqNameContext,
                                XQueryParser.VarRefContext,
                                XQueryParser.ParenthesizedExprContext,
                                XQueryParser.ArgumentListContext>>
                calls = new ArrayList<>(c.function.size());
        for (int i = 0; i < c.function.size(); ++i) {
            XQueryParser.ArrowFunctionSpecifierContext fn = c.function.get(i);
            XQueryParser.ArgumentListContext args = c.arguments.get(i);
            calls.add(new ArrowCall<>(fn.eqName(), fn.varRef(), fn.parenthesizedExpr(), args));
        }
        return new ArrowExprContext<>(c.main_expr, calls, c);
    }
}
