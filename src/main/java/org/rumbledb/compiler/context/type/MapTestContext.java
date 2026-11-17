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

public record MapTestContext<SeqTypeCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>(
        boolean isAnyMap, EqNameCtx keyName, SeqTypeCtx valueSequenceType, ParserRuleContext context) {

    public static MapTestContext<JsoniqParser.SequenceTypeContext, JsoniqParser.EqNameContext> from(
            JsoniqParser.MapTestContext c) {
        boolean isAny = c.anyMapTest() != null || c.typedMapTest() == null;
        JsoniqParser.EqNameContext key = isAny ? null : c.typedMapTest().eqName();
        JsoniqParser.SequenceTypeContext valSeq =
                isAny ? null : c.typedMapTest().sequenceType();
        return new MapTestContext<>(isAny, key, valSeq, c);
    }

    public static MapTestContext<XQueryParser.SequenceTypeContext, XQueryParser.EqNameContext> from(
            XQueryParser.MapTestContext c) {
        boolean isAny = c.anyMapTest() != null || c.typedMapTest() == null;
        XQueryParser.EqNameContext key = isAny ? null : c.typedMapTest().eqName();
        XQueryParser.SequenceTypeContext valSeq =
                isAny ? null : c.typedMapTest().sequenceType();
        return new MapTestContext<>(isAny, key, valSeq, c);
    }
}
