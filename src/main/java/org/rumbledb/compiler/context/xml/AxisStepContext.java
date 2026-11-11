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

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record AxisStepContext<NodeTestCtx extends ParserRuleContext, PredicateCtx extends ParserRuleContext>(
        ParserRuleContext context,
        ForwardStepContext<NodeTestCtx> forwardStep,
        ReverseStepContext<NodeTestCtx> reverseStep,
        List<PredicateCtx> predicates) {

    public static AxisStepContext<JsoniqParser.NodeTestContext, JsoniqParser.PredicateContext> from(
            JsoniqParser.AxisStepContext c) {
        return new AxisStepContext<>(
                c,
                c.forwardStep() != null ? ForwardStepContext.from(c.forwardStep()) : null,
                c.reverseStep() != null ? ReverseStepContext.from(c.reverseStep()) : null,
                c.predicateList() != null && c.predicateList().predicate() != null
                        ? c.predicateList().predicate()
                        : Collections.emptyList());
    }

    public static AxisStepContext<XQueryParser.NodeTestContext, XQueryParser.PredicateContext> from(
            XQueryParser.AxisStepContext c) {
        return new AxisStepContext<>(
                c,
                c.forwardStep() != null ? ForwardStepContext.from(c.forwardStep()) : null,
                c.reverseStep() != null ? ReverseStepContext.from(c.reverseStep()) : null,
                c.predicateList() != null && c.predicateList().predicate() != null
                        ? c.predicateList().predicate()
                        : Collections.emptyList());
    }
}
