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

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record RelativePathExprContext<StepExprCtx extends ParserRuleContext>(
        ParserRuleContext context, List<StepExprCtx> stepExpr, List<Token> sep) {

    public static RelativePathExprContext<JsoniqParser.StepExprContext> from(JsoniqParser.RelativePathExprContext c) {
        return new RelativePathExprContext<>(
                c,
                c.stepExpr() != null ? c.stepExpr() : Collections.emptyList(),
                c.sep != null ? c.sep : Collections.emptyList());
    }

    public static RelativePathExprContext<XQueryParser.StepExprContext> from(XQueryParser.RelativePathExprContext c) {
        return new RelativePathExprContext<>(
                c,
                c.stepExpr() != null ? c.stepExpr() : Collections.emptyList(),
                c.sep != null ? c.sep : Collections.emptyList());
    }
}
