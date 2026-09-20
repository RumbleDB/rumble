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
package org.rumbledb.compiler.translation;

import java.util.Collections;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.QuantifiedExprContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.types.SequenceType;

public final class QuantifiedTranslation {

    private QuantifiedTranslation() {}

    public static <
                    ExprSingleCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext>
            Expression quantifiedExpr(
                    QuantifiedExprContext<ExprSingleCtx, VarBindingCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSingleCtx, Node> visitExprSingle,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Clause lastClause = null;
        Expression expression = (Expression) visitExprSingle.apply(ctx.exprSingle());
        boolean isUniversal = ctx.isUniversal();
        for (QuantifiedExprContext.Var<ExprSingleCtx, VarBindingCtx, SeqTypeCtx> currentVariable : ctx.vars()) {
            Expression varExpression;
            SequenceType sequenceType = null;
            Name variableName = parseVariableBinding.apply(currentVariable.varBinding());
            if (currentVariable.sequenceType() != null) {
                sequenceType = processSequenceType.apply(currentVariable.sequenceType());
            }

            varExpression = (Expression) visitExprSingle.apply(currentVariable.exprSingle());
            Clause newClause = new ForClause(
                    variableName,
                    false,
                    sequenceType,
                    null,
                    varExpression,
                    translationContext.metadata(currentVariable.context()));
            if (lastClause != null) {
                lastClause.chainWith(newClause);
            }
            lastClause = newClause;
        }
        if (lastClause == null) {
            throw new OurBadException("A quantified expression must bind at least one variable.");
        }
        WhereClause whereClause = null;
        if (!isUniversal) {
            whereClause = new WhereClause(expression, translationContext.metadata(ctx.exprSingle()));
        } else {
            whereClause = new WhereClause(
                    new NotExpression(expression, translationContext.metadata(ctx.exprSingle())),
                    translationContext.metadata(ctx.exprSingle()));
        }
        lastClause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new NullLiteralExpression(translationContext.metadata(ctx.context())),
                translationContext.metadata(ctx.context()));
        whereClause.chainWith(returnClause);
        Expression flworExpression = new FlworExpression(returnClause, translationContext.metadata(ctx.context()));
        if (!isUniversal) {
            return new FunctionCallExpression(
                    Name.createVariableInDefaultFunctionNamespace("exists"),
                    Collections.singletonList(flworExpression),
                    translationContext.metadata(ctx.context()));
        } else {
            return new FunctionCallExpression(
                    Name.createVariableInDefaultFunctionNamespace("empty"),
                    Collections.singletonList(flworExpression),
                    translationContext.metadata(ctx.context()));
        }
    }
}
