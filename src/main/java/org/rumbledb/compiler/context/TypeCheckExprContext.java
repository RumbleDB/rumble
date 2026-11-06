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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record TypeCheckExprContext<MainExprCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>(
        MainExprCtx mainExpr, SeqTypeCtx seq, ParserRuleContext context) {

    public static TypeCheckExprContext<JsoniqParser.IsStaticallyExprContext, JsoniqParser.SequenceTypeContext> from(
            JsoniqParser.InstanceOfExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }

    public static TypeCheckExprContext<XQueryParser.IsStaticallyExprContext, XQueryParser.SequenceTypeContext> from(
            XQueryParser.InstanceOfExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }

    public static TypeCheckExprContext<JsoniqParser.TreatExprContext, JsoniqParser.SequenceTypeContext> from(
            JsoniqParser.IsStaticallyExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }

    public static TypeCheckExprContext<XQueryParser.TreatExprContext, XQueryParser.SequenceTypeContext> from(
            XQueryParser.IsStaticallyExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }

    public static TypeCheckExprContext<JsoniqParser.CastableExprContext, JsoniqParser.SequenceTypeContext> from(
            JsoniqParser.TreatExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }

    public static TypeCheckExprContext<XQueryParser.CastableExprContext, XQueryParser.SequenceTypeContext> from(
            XQueryParser.TreatExprContext c) {
        return new TypeCheckExprContext<>(c.main_expr, c.seq, c);
    }
}
