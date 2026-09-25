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
package org.rumbledb.compiler.context.xml;

import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record DirAttributeValueContext<ExprCtx extends ParserRuleContext>(
        ParserRuleContext quotedValue,
        Function<ParserRuleContext, DirAttributeContentContext<ExprCtx>> contentAdapter,
        ParserRuleContext context) {

    public static DirAttributeValueContext<JsoniqParser.ExprContext> from(JsoniqParser.DirAttributeValueContext c) {
        if (c == null) {
            return null;
        }
        if (c.dirAttributeValueQuot() != null) {
            return new DirAttributeValueContext<>(
                    c.dirAttributeValueQuot(),
                    child -> child instanceof JsoniqParser.DirAttributeContentQuotContext quot
                            ? DirAttributeContentContext.from(quot)
                            : null,
                    c);
        }
        if (c.dirAttributeValueApos() != null) {
            return new DirAttributeValueContext<>(
                    c.dirAttributeValueApos(),
                    child -> child instanceof JsoniqParser.DirAttributeContentAposContext apos
                            ? DirAttributeContentContext.from(apos)
                            : null,
                    c);
        }
        return null;
    }

    public static DirAttributeValueContext<XQueryParser.ExprContext> from(XQueryParser.DirAttributeValueContext c) {
        if (c == null) {
            return null;
        }
        if (c.dirAttributeValueQuot() != null) {
            return new DirAttributeValueContext<>(
                    c.dirAttributeValueQuot(),
                    child -> child instanceof XQueryParser.DirAttributeContentQuotContext quot
                            ? DirAttributeContentContext.from(quot)
                            : null,
                    c);
        }
        if (c.dirAttributeValueApos() != null) {
            return new DirAttributeValueContext<>(
                    c.dirAttributeValueApos(),
                    child -> child instanceof XQueryParser.DirAttributeContentAposContext apos
                            ? DirAttributeContentContext.from(apos)
                            : null,
                    c);
        }
        return null;
    }
}
