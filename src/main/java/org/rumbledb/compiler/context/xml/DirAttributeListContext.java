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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record DirAttributeListContext<QnameCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>(
        List<DirAttributeContext<QnameCtx, ExprCtx>> attributes, ParserRuleContext context) {

    public record DirAttributeContext<QnameCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>(
            QnameCtx name, DirAttributeValueContext<ExprCtx> value) {}

    public static DirAttributeListContext<JsoniqParser.QnameContext, JsoniqParser.ExprContext> from(
            JsoniqParser.DirAttributeListContext c) {
        if (c == null) {
            return null;
        }
        if (c.attribute_qname == null || c.attribute_value == null) {
            return new DirAttributeListContext<>(Collections.emptyList(), c);
        }
        int count = Math.min(c.attribute_qname.size(), c.attribute_value.size());
        List<DirAttributeContext<JsoniqParser.QnameContext, JsoniqParser.ExprContext>> attributes =
                new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            attributes.add(new DirAttributeContext<>(
                    c.attribute_qname.get(i), DirAttributeValueContext.from(c.attribute_value.get(i))));
        }
        return new DirAttributeListContext<>(attributes, c);
    }

    public static DirAttributeListContext<XQueryParser.QnameContext, XQueryParser.ExprContext> from(
            XQueryParser.DirAttributeListContext c) {
        if (c == null) {
            return null;
        }
        if (c.attribute_qname == null || c.attribute_value == null) {
            return new DirAttributeListContext<>(Collections.emptyList(), c);
        }
        int count = Math.min(c.attribute_qname.size(), c.attribute_value.size());
        List<DirAttributeContext<XQueryParser.QnameContext, XQueryParser.ExprContext>> attributes =
                new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            attributes.add(new DirAttributeContext<>(
                    c.attribute_qname.get(i), DirAttributeValueContext.from(c.attribute_value.get(i))));
        }
        return new DirAttributeListContext<>(attributes, c);
    }
}
