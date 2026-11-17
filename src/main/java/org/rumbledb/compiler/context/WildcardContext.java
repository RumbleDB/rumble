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

import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record WildcardContext(
        WildcardType type, String text, String localName, String prefix, String uri, ParserRuleContext context) {

    public enum WildcardType {
        ALL,
        ALL_WITH_LOCAL,
        ALL_WITH_NS,
        BRACED_URI_LITERAL
    }

    public static WildcardContext from(JsoniqParser.WildcardContext c) {
        String text = c.getText();
        if (c instanceof JsoniqParser.AllNamesContext) {
            return new WildcardContext(WildcardType.ALL, text, null, null, null, c);
        }
        if (c instanceof JsoniqParser.AllWithLocalContext) {
            return new WildcardContext(WildcardType.ALL_WITH_LOCAL, text, text.substring(2), null, null, c);
        }
        if (c instanceof JsoniqParser.AllWithNSContext) {
            return new WildcardContext(
                    WildcardType.ALL_WITH_NS, text, null, text.substring(0, text.length() - 2), null, c);
        }
        if (c instanceof JsoniqParser.BracedURILiteralContext) {
            int closingBrace = text.indexOf('}');
            return new WildcardContext(
                    WildcardType.BRACED_URI_LITERAL, text, null, null, text.substring(2, closingBrace), c);
        }
        throw new OurBadException("Unsupported catch wildcard pattern: " + text);
    }

    public static WildcardContext from(XQueryParser.WildcardContext c) {
        String text = c.getText();
        if (c instanceof XQueryParser.AllNamesContext) {
            return new WildcardContext(WildcardType.ALL, text, null, null, null, c);
        }
        if (c instanceof XQueryParser.AllWithLocalContext) {
            return new WildcardContext(WildcardType.ALL_WITH_LOCAL, text, text.substring(2), null, null, c);
        }
        if (c instanceof XQueryParser.AllWithNSContext) {
            return new WildcardContext(
                    WildcardType.ALL_WITH_NS, text, null, text.substring(0, text.length() - 2), null, c);
        }
        if (c instanceof XQueryParser.BracedURILiteralContext) {
            int closingBrace = text.indexOf('}');
            return new WildcardContext(
                    WildcardType.BRACED_URI_LITERAL, text, null, null, text.substring(2, closingBrace), c);
        }
        throw new OurBadException("Unsupported catch wildcard pattern: " + text);
    }
}
