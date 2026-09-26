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

public record AttributeTestContext<EqNameCtx extends ParserRuleContext>(
        boolean hasAttributeNameOrWildcard,
        boolean isWildcard,
        EqNameCtx attributeNameEqName,
        EqNameCtx typeNameEqName,
        ParserRuleContext context) {

    public static AttributeTestContext<JsoniqParser.EqNameContext> from(JsoniqParser.AttributeTestContext c) {
        boolean hasAttributeNameOrWildcard = c.attributeNameOrWildcard() != null;

        boolean hasWildcard =
                hasAttributeNameOrWildcard && c.attributeNameOrWildcard().STAR() != null;
        JsoniqParser.EqNameContext attributeName =
                hasAttributeNameOrWildcard && c.attributeNameOrWildcard().attributeName() != null
                        ? c.attributeNameOrWildcard().attributeName().eqName()
                        : null;
        JsoniqParser.EqNameContext typeName =
                c.typeName() != null ? c.typeName().eqName() : null;
        return new AttributeTestContext<>(hasAttributeNameOrWildcard, hasWildcard, attributeName, typeName, c);
    }

    public static AttributeTestContext<XQueryParser.EqNameContext> from(XQueryParser.AttributeTestContext c) {
        boolean hasAttributeNameOrWildcard = c.attributeNameOrWildcard() != null;
        boolean hasWildcard =
                hasAttributeNameOrWildcard && c.attributeNameOrWildcard().STAR() != null;
        XQueryParser.EqNameContext attributeName =
                hasAttributeNameOrWildcard && c.attributeNameOrWildcard().attributeName() != null
                        ? c.attributeNameOrWildcard().attributeName().eqName()
                        : null;
        XQueryParser.EqNameContext typeName =
                c.typeName() != null ? c.typeName().eqName() : null;
        return new AttributeTestContext<>(hasAttributeNameOrWildcard, hasWildcard, attributeName, typeName, c);
    }
}
