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

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record ItemTypeContext<
        SeqTypeCtx extends ParserRuleContext,
        ItemTypeCtx extends ParserRuleContext,
        EqNameCtx extends ParserRuleContext,
        AnnotationCtx extends ParserRuleContext,
        StringLiteralCtx extends ParserRuleContext>(
        ItemTypeCtx parenthesizedItemType,
        boolean isItem,
        boolean isNull,
        FunctionTestContext<SeqTypeCtx, AnnotationCtx> functionTest,
        MapTestContext<SeqTypeCtx, EqNameCtx> mapTest,
        ArrayTestContext<SeqTypeCtx> arrayTest,
        EqNameCtx eqName,
        KindTestContext<EqNameCtx, StringLiteralCtx> kindTest,
        ParserRuleContext context) {

    public static ItemTypeContext<
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.ItemTypeContext,
                    JsoniqParser.EqNameContext,
                    JsoniqParser.AnnotationContext,
                    JsoniqParser.StringLiteralContext>
            from(JsoniqParser.ItemTypeContext c) {
        return new ItemTypeContext<>(
                c.parenthesizedItemTest() != null ? c.parenthesizedItemTest().itemType() : null,
                c.KW_ITEM() != null,
                c.KW_NULL() != null,
                c.functionTest() != null ? FunctionTestContext.from(c.functionTest()) : null,
                c.mapTest() != null ? MapTestContext.from(c.mapTest()) : null,
                c.arrayTest() != null ? ArrayTestContext.from(c.arrayTest()) : null,
                c.eqName(),
                c.kindTest() != null ? KindTestContext.from(c.kindTest()) : null,
                c);
    }

    public static ItemTypeContext<
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.ItemTypeContext,
                    XQueryParser.EqNameContext,
                    XQueryParser.AnnotationContext,
                    XQueryParser.StringLiteralContext>
            from(XQueryParser.ItemTypeContext c) {
        return new ItemTypeContext<>(
                c.parenthesizedItemTest() != null ? c.parenthesizedItemTest().itemType() : null,
                c.KW_ITEM() != null,
                false,
                c.functionTest() != null ? FunctionTestContext.from(c.functionTest()) : null,
                c.mapTest() != null ? MapTestContext.from(c.mapTest()) : null,
                c.arrayTest() != null ? ArrayTestContext.from(c.arrayTest()) : null,
                c.eqName(),
                c.kindTest() != null ? KindTestContext.from(c.kindTest()) : null,
                c);
    }
}
