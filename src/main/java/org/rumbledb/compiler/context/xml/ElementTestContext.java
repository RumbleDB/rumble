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

public record ElementTestContext<EqNameCtx extends ParserRuleContext>(
        boolean hasOptional,
        boolean hasElementNameOrWildcard,
        boolean isWildcard,
        EqNameCtx elementNameEqName,
        EqNameCtx typeNameEqName,
        ParserRuleContext context) {

    public static ElementTestContext<JsoniqParser.EqNameContext> from(JsoniqParser.ElementTestContext c) {
        boolean hasElementNameOrWildcard = c.elementNameOrWildcard() != null;
        boolean hasWildcard =
                hasElementNameOrWildcard && c.elementNameOrWildcard().STAR() != null;
        JsoniqParser.EqNameContext elementName =
                (hasElementNameOrWildcard && c.elementNameOrWildcard().elementName() != null)
                        ? c.elementNameOrWildcard().elementName().eqName()
                        : null;
        JsoniqParser.EqNameContext typeName =
                c.typeName() != null ? c.typeName().eqName() : null;
        return new ElementTestContext<>(
                c.optional != null, hasElementNameOrWildcard, hasWildcard, elementName, typeName, c);
    }

    public static ElementTestContext<XQueryParser.EqNameContext> from(XQueryParser.ElementTestContext c) {
        boolean hasElementNameOrWildcard = c.elementNameOrWildcard() != null;
        boolean hasWildcard =
                hasElementNameOrWildcard && c.elementNameOrWildcard().STAR() != null;
        XQueryParser.EqNameContext elementName =
                (hasElementNameOrWildcard && c.elementNameOrWildcard().elementName() != null)
                        ? c.elementNameOrWildcard().elementName().eqName()
                        : null;
        XQueryParser.EqNameContext typeName =
                c.typeName() != null ? c.typeName().eqName() : null;
        return new ElementTestContext<>(
                c.optional != null, hasElementNameOrWildcard, hasWildcard, elementName, typeName, c);
    }
}
