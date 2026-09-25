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

public record FunctionDeclContext<
        AnnotationsCtx extends ParserRuleContext,
        FunctionNameCtx extends ParserRuleContext,
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        ReturnTypeCtx extends ParserRuleContext,
        FnBodyCtx extends ParserRuleContext>(
        ParserRuleContext context,
        AnnotationsCtx annotations,
        FunctionNameCtx functionName,
        List<FunctionParam<VarBindingCtx, SeqTypeCtx>> params,
        ReturnTypeCtx returnType,
        FnBodyCtx fnBody,
        boolean isExternal) {

    public record FunctionParam<VarBindingCtx extends ParserRuleContext, SeqTypeCtx extends ParserRuleContext>(
            ParserRuleContext context, VarBindingCtx name, SeqTypeCtx sequenceType) {}

    public static FunctionDeclContext<
                    JsoniqParser.AnnotationsContext,
                    JsoniqParser.FunctionNameContext,
                    JsoniqParser.VarBindingContext,
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.StatementsAndOptionalExprContext>
            from(JsoniqParser.FunctionDeclContext ctx) {
        List<FunctionParam<JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext>> params;
        if (ctx.paramList() != null && ctx.paramList().param() != null) {
            params = new ArrayList<>();
            for (JsoniqParser.ParamContext param : ctx.paramList().param()) {
                params.add(new FunctionParam<>(param, param.name, param.sequenceType()));
            }
        } else {
            params = Collections.emptyList();
        }
        return new FunctionDeclContext<>(
                ctx,
                ctx.annotations(),
                ctx.functionName(),
                params,
                ctx.return_type,
                ctx.fn_body,
                ctx.is_external != null);
    }

    public static FunctionDeclContext<
                    XQueryParser.AnnotationsContext,
                    XQueryParser.FunctionNameContext,
                    XQueryParser.VarBindingContext,
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.StatementsAndOptionalExprContext>
            from(XQueryParser.FunctionDeclContext ctx) {
        List<FunctionParam<XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext>> params;
        if (ctx.paramList() != null && ctx.paramList().param() != null) {
            params = new ArrayList<>();
            for (XQueryParser.ParamContext param : ctx.paramList().param()) {
                params.add(new FunctionParam<>(param, param.name, param.sequenceType()));
            }
        } else {
            params = Collections.emptyList();
        }
        return new FunctionDeclContext<>(
                ctx,
                ctx.annotations(),
                ctx.functionName(),
                params,
                ctx.return_type,
                ctx.fn_body,
                ctx.is_external != null);
    }
}
