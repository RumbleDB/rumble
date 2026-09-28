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

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record FunctionTestContext<SeqTypeCtx extends ParserRuleContext, AnnotationCtx extends ParserRuleContext>(
        boolean isAnyFunction,
        SeqTypeCtx returnType,
        List<SeqTypeCtx> parameterTypes,
        List<AnnotationCtx> annotations,
        ParserRuleContext context) {

    public static FunctionTestContext<JsoniqParser.SequenceTypeContext, JsoniqParser.AnnotationContext> from(
            JsoniqParser.FunctionTestContext c) {
        boolean isAny = c.typedFunctionTest() == null;
        JsoniqParser.SequenceTypeContext returnType = isAny ? null : c.typedFunctionTest().rt;
        List<JsoniqParser.SequenceTypeContext> paramTypes = isAny ? Collections.emptyList() : c.typedFunctionTest().st;
        return new FunctionTestContext<>(isAny, returnType, paramTypes, c.annotation(), c);
    }

    public static FunctionTestContext<XQueryParser.SequenceTypeContext, XQueryParser.AnnotationContext> from(
            XQueryParser.FunctionTestContext c) {
        boolean isAny = c.typedFunctionTest() == null;
        XQueryParser.SequenceTypeContext returnType = isAny ? null : c.typedFunctionTest().rt;
        List<XQueryParser.SequenceTypeContext> paramTypes = isAny ? Collections.emptyList() : c.typedFunctionTest().st;
        return new FunctionTestContext<>(isAny, returnType, paramTypes, c.annotation(), c);
    }
}
