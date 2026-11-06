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

public record ComparisonExprContext<ChildExprCtx extends ParserRuleContext>(
        ChildExprCtx mainExpr,
        List<ChildExprCtx> rhs,
        String operatorSymbol,
        boolean isNodeComp,
        ParserRuleContext context) {

    public static ComparisonExprContext<JsoniqParser.StringConcatExprContext> from(
            JsoniqParser.ComparisonExprContext c) {
        String operatorSymbol =
                (c.op == null || c.op.isEmpty()) ? null : c.op.get(0).getText();
        boolean isNodeComp = (c.op != null && !c.op.isEmpty() && c.op.get(0).nodeComp() != null);
        return new ComparisonExprContext<>(c.main_expr, c.rhs, operatorSymbol, isNodeComp, c);
    }

    public static ComparisonExprContext<XQueryParser.StringConcatExprContext> from(
            XQueryParser.ComparisonExprContext c) {
        String operatorSymbol =
                (c.op == null || c.op.isEmpty()) ? null : c.op.get(0).getText();
        boolean isNodeComp = (c.op != null && !c.op.isEmpty() && c.op.get(0).nodeComp() != null);
        return new ComparisonExprContext<>(c.main_expr, c.rhs, operatorSymbol, isNodeComp, c);
    }
}
