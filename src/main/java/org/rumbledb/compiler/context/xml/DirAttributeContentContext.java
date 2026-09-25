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

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record DirAttributeContentContext<ExprCtx extends ParserRuleContext>(ExprCtx expr, ParserRuleContext context) {

    public boolean isEnclosed() {
        return this.expr != null;
    }

    public static DirAttributeContentContext<JsoniqParser.ExprContext> from(
            JsoniqParser.DirAttributeContentQuotContext c) {
        if (c == null) {
            return null;
        }
        return new DirAttributeContentContext<>(c.expr(), c);
    }

    public static DirAttributeContentContext<JsoniqParser.ExprContext> from(
            JsoniqParser.DirAttributeContentAposContext c) {
        if (c == null) {
            return null;
        }
        return new DirAttributeContentContext<>(c.expr(), c);
    }

    public static DirAttributeContentContext<XQueryParser.ExprContext> from(
            XQueryParser.DirAttributeContentQuotContext c) {
        if (c == null) {
            return null;
        }
        return new DirAttributeContentContext<>(c.expr(), c);
    }

    public static DirAttributeContentContext<XQueryParser.ExprContext> from(
            XQueryParser.DirAttributeContentAposContext c) {
        if (c == null) {
            return null;
        }
        return new DirAttributeContentContext<>(c.expr(), c);
    }
}
