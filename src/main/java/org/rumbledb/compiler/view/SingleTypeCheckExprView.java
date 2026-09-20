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
package org.rumbledb.compiler.view;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record SingleTypeCheckExprView<T extends ParserRuleContext, S extends ParserRuleContext>(
        T mainExpr, S single, ParserRuleContext context) {

    public static SingleTypeCheckExprView<JsoniqParser.CastExprContext, JsoniqParser.SingleTypeContext> from(
            JsoniqParser.CastableExprContext c) {
        return new SingleTypeCheckExprView<>(c.main_expr, c.single, c);
    }

    public static SingleTypeCheckExprView<XQueryParser.CastExprContext, XQueryParser.SingleTypeContext> from(
            XQueryParser.CastableExprContext c) {
        return new SingleTypeCheckExprView<>(c.main_expr, c.single, c);
    }

    public static SingleTypeCheckExprView<JsoniqParser.ArrowExprContext, JsoniqParser.SingleTypeContext> from(
            JsoniqParser.CastExprContext c) {
        return new SingleTypeCheckExprView<>(c.main_expr, c.single, c);
    }

    public static SingleTypeCheckExprView<XQueryParser.ArrowExprContext, XQueryParser.SingleTypeContext> from(
            XQueryParser.CastExprContext c) {
        return new SingleTypeCheckExprView<>(c.main_expr, c.single, c);
    }
}
