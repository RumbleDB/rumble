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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record InlineFunctionExprContext<
        AnnotationsCtx extends ParserRuleContext,
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        FnBodyCtx extends ParserRuleContext>(
        AnnotationsCtx annotations,
        List<InlineFunctionParam<VarBindingCtx, SeqTypeCtx>> params,
        SeqTypeCtx returnType,
        FnBodyCtx fnBody,
        ParserRuleContext context) {

    public record InlineFunctionParam<VarBindingCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>(
            VarBindingCtx name, SeqTypeCtx sequenceType, ParserRuleContext context) {}

    public static InlineFunctionExprContext<
                    JsoniqParser.AnnotationsContext,
                    JsoniqParser.VarBindingContext,
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.StatementsAndOptionalExprContext>
            from(JsoniqParser.InlineFunctionExprContext ctx) {
        List<InlineFunctionParam<JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext>> params;
        if (ctx.paramList() != null && ctx.paramList().param() != null) {
            params = new ArrayList<>();
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                params.add(new InlineFunctionParam<>(param.name, param.sequenceType(), param));
            }
        } else {
            params = Collections.emptyList();
        }
        return new InlineFunctionExprContext<>(ctx.annotations(), params, ctx.return_type, ctx.fn_body, ctx);
    }

    public static InlineFunctionExprContext<
                    XQueryParser.AnnotationsContext,
                    XQueryParser.VarBindingContext,
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.StatementsAndOptionalExprContext>
            from(XQueryParser.InlineFunctionExprContext ctx) {
        List<InlineFunctionParam<XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext>> params;
        if (ctx.paramList() != null && ctx.paramList().param() != null) {
            params = new ArrayList<>();
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                params.add(new InlineFunctionParam<>(param.name, param.sequenceType(), param));
            }
        } else {
            params = Collections.emptyList();
        }
        return new InlineFunctionExprContext<>(ctx.annotations(), params, ctx.return_type, ctx.fn_body, ctx);
    }
}
