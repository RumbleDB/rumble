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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.CountClauseContext;
import org.rumbledb.compiler.context.GroupByClauseContext;
import org.rumbledb.compiler.context.GroupByVarContext;
import org.rumbledb.compiler.context.OrderByClauseContext;
import org.rumbledb.compiler.context.OrderByExprContext;
import org.rumbledb.compiler.context.WhereClauseContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.UnknownCollationException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.types.SequenceType;

public final class FlworTranslation {

    private FlworTranslation() {}

    public static <ExprSingleCtx extends ParserRuleContext> WhereClause whereClause(
            WhereClauseContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Node> visitExprSingle) {
        Expression expr = (Expression) visitExprSingle.apply(ctx.exprSingle());
        return new WhereClause(expr, translationContext.metadata(ctx.context()));
    }

    public static <VarBindingCtx extends ParserRuleContext> CountClause countClause(
            CountClauseContext<VarBindingCtx> ctx,
            TranslationContext translationContext,
            Function<VarBindingCtx, Name> parseVariableBinding) {
        return new CountClause(
                parseVariableBinding.apply(ctx.varBinding()), translationContext.metadata(ctx.context()));
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext,
                    UriLiteralCtx extends ParserRuleContext>
            GroupByClause groupByClause(
                    GroupByClauseContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx, UriLiteralCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Node> visitExprSingle,
                    Function<UriLiteralCtx, String> resolveCollationUri) {
        List<GroupByVariableDeclaration> vars = new ArrayList<>();
        for (GroupByVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx, UriLiteralCtx> varCtx : ctx.vars()) {
            String collationUri = null;
            if (varCtx.uriLiteral() != null) {
                String collation = resolveCollationUri.apply(varCtx.uriLiteral());
                if (!translationContext.moduleContext().isStaticallyKnownCollation(collation)) {
                    throw new UnknownCollationException(
                            "Unknown collation: " + collation, translationContext.metadata(varCtx.uriLiteral()));
                }
                collationUri = collation;
            }
            SequenceType seq = varCtx.seq() != null ? processSequenceType.apply(varCtx.seq()) : null;
            Expression expr =
                    varCtx.exprSingle() != null ? (Expression) visitExprSingle.apply(varCtx.exprSingle()) : null;
            Name var = parseVariableBinding.apply(varCtx.varRef());
            vars.add(new GroupByVariableDeclaration(var, seq, expr, collationUri));
        }
        return new GroupByClause(vars, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext, UriLiteralCtx extends ParserRuleContext>
            OrderByClause orderByClause(
                    OrderByClauseContext<ExprSingleCtx, UriLiteralCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSingleCtx, Node> visitExprSingle,
                    Function<UriLiteralCtx, String> resolveCollationUri) {
        List<OrderByClauseSortingKey> exprs = new ArrayList<>();
        for (OrderByExprContext<ExprSingleCtx, UriLiteralCtx> exprCtx : ctx.exprs()) {
            String uri = null;
            if (exprCtx.uriLiteral() != null) {
                String collation = resolveCollationUri.apply(exprCtx.uriLiteral());
                if (!translationContext.moduleContext().isStaticallyKnownCollation(collation)) {
                    throw new UnknownCollationException(
                            "Unknown collation: " + collation, translationContext.metadata(exprCtx.uriLiteral()));
                }
                uri = collation;
            }
            Expression expression = (Expression) visitExprSingle.apply(exprCtx.exprSingle());
            exprs.add(new OrderByClauseSortingKey(expression, exprCtx.ascending(), uri, exprCtx.emptyOrder()));
        }
        return new OrderByClause(exprs, ctx.stable(), translationContext.metadata(ctx.context()));
    }
}
