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

import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record WindowClauseContext<
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        ExprSingleCtx extends ParserRuleContext>(
        WindowClause.WindowType windowType,
        VarBindingCtx windowVariable,
        SeqTypeCtx seqType,
        ExprSingleCtx expression,
        WindowConditionContext<VarBindingCtx, ExprSingleCtx> startCondition,
        WindowConditionContext<VarBindingCtx, ExprSingleCtx> endCondition,
        ParserRuleContext context) {

    public static WindowClauseContext<
                    JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext, JsoniqParser.ExprSingleContext>
            from(JsoniqParser.TumblingWindowClauseContext c) {
        return new WindowClauseContext<>(
                WindowClause.WindowType.TUMBLING,
                c.name,
                c.type == null ? null : c.type.sequenceType(),
                c.exprSingle(),
                WindowConditionContext.fromStart(c.windowStartCondition()),
                WindowConditionContext.fromEnd(c.windowEndCondition()),
                c);
    }

    public static WindowClauseContext<
                    JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext, JsoniqParser.ExprSingleContext>
            from(JsoniqParser.SlidingWindowClauseContext c) {
        return new WindowClauseContext<>(
                WindowClause.WindowType.SLIDING,
                c.name,
                c.type == null ? null : c.type.sequenceType(),
                c.exprSingle(),
                WindowConditionContext.fromStart(c.windowStartCondition()),
                WindowConditionContext.fromEnd(c.windowEndCondition()),
                c);
    }

    public static WindowClauseContext<
                    XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext, XQueryParser.ExprSingleContext>
            from(XQueryParser.TumblingWindowClauseContext c) {
        return new WindowClauseContext<>(
                WindowClause.WindowType.TUMBLING,
                c.name,
                c.type == null ? null : c.type.sequenceType(),
                c.exprSingle(),
                WindowConditionContext.fromStart(c.windowStartCondition()),
                WindowConditionContext.fromEnd(c.windowEndCondition()),
                c);
    }

    public static WindowClauseContext<
                    XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext, XQueryParser.ExprSingleContext>
            from(XQueryParser.SlidingWindowClauseContext c) {
        return new WindowClauseContext<>(
                WindowClause.WindowType.SLIDING,
                c.name,
                c.type == null ? null : c.type.sequenceType(),
                c.exprSingle(),
                WindowConditionContext.fromStart(c.windowStartCondition()),
                WindowConditionContext.fromEnd(c.windowEndCondition()),
                c);
    }
}
