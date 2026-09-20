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

import org.rumbledb.compiler.context.ComparisonExprContext;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;

public final class ComparisonTranslation {

    private ComparisonTranslation() {}

    public static <T extends ParserRuleContext> Expression comparisonExpr(
            ComparisonExprContext<T> ctx,
            TranslationContext translationContext,
            Function<T, Node> visitStringConcatExpr) {
        Expression mainExpression = (Expression) visitStringConcatExpr.apply(ctx.mainExpr());
        if (ctx.rhs() == null || ctx.rhs().isEmpty()) {
            return mainExpression;
        }
        T child = ctx.rhs().get(0);
        Expression childExpression = (Expression) visitStringConcatExpr.apply(child);

        if (ctx.isNodeComp()) {
            NodeComparisonExpression.NodeComparisonOperator nodeOp =
                    NodeComparisonExpression.NodeComparisonOperator.fromSymbol(ctx.operatorSymbol());
            return new NodeComparisonExpression(
                    mainExpression, childExpression, nodeOp, translationContext.metadata(ctx.context()));
        }

        ComparisonExpression.ComparisonOperator kind =
                ComparisonExpression.ComparisonOperator.fromSymbol(ctx.operatorSymbol());
        if (kind.isValueComparison()
                || translationContext.configuration().optimization().optimizeGeneralComparisonToValueComparison()) {
            return new ComparisonExpression(
                    mainExpression, childExpression, kind, translationContext.metadata(ctx.context()));
        }

        Name variableNameLeft = Name.TEMP_VAR1;
        Name variableNameRight = Name.TEMP_VAR2;

        Clause firstClause = new ForClause(
                variableNameLeft, false, null, null, mainExpression, translationContext.metadata(ctx.context()));
        Clause secondClause = new ForClause(
                variableNameRight, false, null, null, childExpression, translationContext.metadata(ctx.context()));
        firstClause.chainWith(secondClause);
        Expression valueComparison = new ComparisonExpression(
                new VariableReferenceExpression(variableNameLeft, translationContext.metadata(ctx.context())),
                new VariableReferenceExpression(variableNameRight, translationContext.metadata(ctx.context())),
                kind.getCorrespondingValueComparison(),
                translationContext.metadata(ctx.context()));
        WhereClause whereClause = new WhereClause(valueComparison, translationContext.metadata(ctx.context()));
        secondClause.chainWith(whereClause);
        ReturnClause returnClause = new ReturnClause(
                new StringLiteralExpression("", translationContext.metadata(ctx.context())),
                translationContext.metadata(ctx.context()));
        whereClause.chainWith(returnClause);
        Expression flworExpression = new FlworExpression(returnClause, translationContext.metadata(ctx.context()));
        return new FunctionCallExpression(
                Name.createVariableInDefaultFunctionNamespace("exists"),
                Collections.singletonList(flworExpression),
                translationContext.metadata(ctx.context()));
    }
}
