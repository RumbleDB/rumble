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

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record FunctionCallContext<FunctionNameCtx extends ParserRuleContext, ArgumentCtx extends ParserRuleContext>(
        FunctionNameCtx functionName, List<ArgumentCtx> arguments, ParserRuleContext context) {

    public static FunctionCallContext<JsoniqParser.FunctionNameContext, JsoniqParser.ArgumentContext> from(
            JsoniqParser.FunctionCallContext c) {
        List<JsoniqParser.ArgumentContext> args = c.argumentList() != null && c.argumentList().args != null
                ? c.argumentList().args
                : Collections.emptyList();
        return new FunctionCallContext<>(c.functionName(), args, c);
    }

    public static FunctionCallContext<XQueryParser.FunctionNameContext, XQueryParser.ArgumentContext> from(
            XQueryParser.FunctionCallContext c) {
        List<XQueryParser.ArgumentContext> args = c.argumentList() != null && c.argumentList().args != null
                ? c.argumentList().args
                : Collections.emptyList();
        return new FunctionCallContext<>(c.fn_name, args, c);
    }
}
