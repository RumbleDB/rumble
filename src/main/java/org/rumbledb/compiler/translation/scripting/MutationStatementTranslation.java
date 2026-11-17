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
package org.rumbledb.compiler.translation.scripting;

import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.scripting.ApplyStatementContext;
import org.rumbledb.compiler.context.scripting.AssignStatementContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;

public final class MutationStatementTranslation {

    private MutationStatementTranslation() {}

    public static <ExprSimpleCtx extends ParserRuleContext> ApplyStatement applyStatement(
            ApplyStatementContext<ExprSimpleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSimpleCtx, Expression> visitExprSimple) {
        Expression exprSimple = visitExprSimple.apply(ctx.exprSimple());
        return new ApplyStatement(exprSimple, translationContext.metadata(ctx.context()));
    }

    public static <VarRefCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            AssignStatement assignStatement(
                    AssignStatementContext<VarRefCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarRefCtx, Name> parseVariableReference,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Name paramName = parseVariableReference.apply(ctx.varRef());
        Expression exprSingle = visitExprSingle.apply(ctx.exprSingle());
        return new AssignStatement(exprSingle, paramName, translationContext.metadata(ctx.context()));
    }
}
