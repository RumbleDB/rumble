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
        List<QnameCtx> attributeQname,
        List<DirAttributeValueContext<ExprCtx>> attributeValue,
        ParserRuleContext context) {

    public static DirAttributeListContext<JsoniqParser.QnameContext, JsoniqParser.ExprContext> from(
            JsoniqParser.DirAttributeListContext c) {
        if (c == null) {
            return null;
        }
        List<DirAttributeValueContext<JsoniqParser.ExprContext>> values = new ArrayList<>();
        if (c.attribute_value != null) {
            for (JsoniqParser.DirAttributeValueContext val : c.attribute_value) {
                values.add(DirAttributeValueContext.from(val));
            }
        }
        return new DirAttributeListContext<>(
                c.attribute_qname != null ? c.attribute_qname : Collections.emptyList(), values, c);
    }

    public static DirAttributeListContext<XQueryParser.QnameContext, XQueryParser.ExprContext> from(
            XQueryParser.DirAttributeListContext c) {
        if (c == null) {
            return null;
        }
        List<DirAttributeValueContext<XQueryParser.ExprContext>> values = new ArrayList<>();
        if (c.attribute_value != null) {
            for (XQueryParser.DirAttributeValueContext val : c.attribute_value) {
                values.add(DirAttributeValueContext.from(val));
            }
        }
        return new DirAttributeListContext<>(
                c.attribute_qname != null ? c.attribute_qname : Collections.emptyList(), values, c);
    }
}
