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

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record DirElemOpenCloseContext<QnameCtx extends ParserRuleContext, DirElemContentCtx extends ParserRuleContext>(
        QnameCtx closeTagName, Token endOpen, List<DirElemContentCtx> dirElemContent, ParserRuleContext context) {

    public static DirElemOpenCloseContext<JsoniqParser.QnameContext, JsoniqParser.DirElemContentContext> from(
            JsoniqParser.DirElemConstructorOpenCloseContext c) {
        if (c == null) {
            return null;
        }
        return new DirElemOpenCloseContext<>(c.close_tag_name, c.endOpen, c.dirElemContent(), c);
    }

    public static DirElemOpenCloseContext<XQueryParser.QnameContext, XQueryParser.DirElemContentContext> from(
            XQueryParser.DirElemConstructorOpenCloseContext c) {
        if (c == null) {
            return null;
        }
        return new DirElemOpenCloseContext<>(c.close_tag_name, c.endOpen, c.dirElemContent(), c);
    }
}
