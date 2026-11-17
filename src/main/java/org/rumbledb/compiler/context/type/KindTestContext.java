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
package org.rumbledb.compiler.context.type;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.xml.AttributeTestContext;
import org.rumbledb.compiler.context.xml.DocumentTestContext;
import org.rumbledb.compiler.context.xml.ElementTestContext;
import org.rumbledb.compiler.context.xml.PiTestContext;
import org.rumbledb.compiler.context.xml.SchemaAttributeTestContext;
import org.rumbledb.compiler.context.xml.SchemaElementTestContext;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record KindTestContext<EqNameCtx extends ParserRuleContext, StringLiteralCtx extends ParserRuleContext>(
        SchemaElementTestContext<EqNameCtx> schemaElementTest,
        SchemaAttributeTestContext<EqNameCtx> schemaAttributeTest,
        boolean isAnyKindTest,
        DocumentTestContext<EqNameCtx> documentTest,
        ElementTestContext<EqNameCtx> elementTest,
        AttributeTestContext<EqNameCtx> attributeTest,
        boolean isCommentTest,
        boolean isTextTest,
        boolean isNamespaceNodeTest,
        PiTestContext<StringLiteralCtx> piTest,
        ParserRuleContext context) {

    public static KindTestContext<JsoniqParser.EqNameContext, JsoniqParser.StringLiteralContext> from(
            JsoniqParser.KindTestContext c) {
        return new KindTestContext<>(
                c.schemaElementTest() != null ? SchemaElementTestContext.from(c.schemaElementTest()) : null,
                c.schemaAttributeTest() != null ? SchemaAttributeTestContext.from(c.schemaAttributeTest()) : null,
                c.anyKindTest() != null,
                c.documentTest() != null ? DocumentTestContext.from(c.documentTest()) : null,
                c.elementTest() != null ? ElementTestContext.from(c.elementTest()) : null,
                c.attributeTest() != null ? AttributeTestContext.from(c.attributeTest()) : null,
                c.commentTest() != null,
                c.textTest() != null,
                c.namespaceNodeTest() != null,
                c.piTest() != null ? PiTestContext.from(c.piTest()) : null,
                c);
    }

    public static KindTestContext<XQueryParser.EqNameContext, XQueryParser.StringLiteralContext> from(
            XQueryParser.KindTestContext c) {
        return new KindTestContext<>(
                c.schemaElementTest() != null ? SchemaElementTestContext.from(c.schemaElementTest()) : null,
                c.schemaAttributeTest() != null ? SchemaAttributeTestContext.from(c.schemaAttributeTest()) : null,
                c.anyKindTest() != null,
                c.documentTest() != null ? DocumentTestContext.from(c.documentTest()) : null,
                c.elementTest() != null ? ElementTestContext.from(c.elementTest()) : null,
                c.attributeTest() != null ? AttributeTestContext.from(c.attributeTest()) : null,
                c.commentTest() != null,
                c.textTest() != null,
                c.namespaceNodeTest() != null,
                c.piTest() != null ? PiTestContext.from(c.piTest()) : null,
                c);
    }
}
