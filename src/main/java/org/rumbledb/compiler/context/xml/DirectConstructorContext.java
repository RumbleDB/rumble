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
import org.antlr.v4.runtime.tree.TerminalNode;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record DirectConstructorContext<
        QnameCtx extends ParserRuleContext,
        ExprCtx extends ParserRuleContext,
        DirElemContentCtx extends ParserRuleContext>(
        TerminalNode comment,
        TerminalNode pi,
        QnameCtx openTagName,
        DirAttributeListContext<QnameCtx, ExprCtx> attributes,
        boolean isSingleTag,
        DirElemOpenCloseContext<QnameCtx, DirElemContentCtx> openClose,
        ParserRuleContext context) {

    public static DirectConstructorContext<
                    JsoniqParser.QnameContext, JsoniqParser.ExprContext, JsoniqParser.DirElemContentContext>
            from(JsoniqParser.DirectConstructorContext c) {
        if (c == null) {
            return null;
        }
        return new DirectConstructorContext<>(
                c.COMMENT(),
                c.PI(),
                c.open_tag_name,
                DirAttributeListContext.from(c.attributes),
                c.single_tag != null,
                DirElemOpenCloseContext.from(c.open_close),
                c);
    }

    public static DirectConstructorContext<
                    XQueryParser.QnameContext, XQueryParser.ExprContext, XQueryParser.DirElemContentContext>
            from(XQueryParser.DirectConstructorContext c) {
        if (c == null) {
            return null;
        }
        return new DirectConstructorContext<>(
                c.COMMENT(),
                c.PI(),
                c.open_tag_name,
                DirAttributeListContext.from(c.attributes),
                c.single_tag != null,
                DirElemOpenCloseContext.from(c.open_close),
                c);
    }
}
