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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.scripting.BlockExprContext;
import org.rumbledb.compiler.context.scripting.BlockStatementContext;
import org.rumbledb.compiler.context.scripting.StatementsAndExprContext;
import org.rumbledb.compiler.context.scripting.StatementsAndOptionalExprContext;
import org.rumbledb.compiler.context.scripting.StatementsContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.block.BlockExpression;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

public final class BlockStatementTranslation {

    private BlockStatementTranslation() {}

    public static <StmtCtx extends ParserRuleContext> StatementsAndOptionalExpr statements(
            StatementsContext<StmtCtx> ctx,
            TranslationContext translationContext,
            Function<StmtCtx, Statement> visitStatement) {
        List<Statement> statements = new ArrayList<>();
        if (ctx.statements() != null) {
            for (StmtCtx stmt : ctx.statements()) {
                statements.add(visitStatement.apply(stmt));
            }
        }
        return new StatementsAndOptionalExpr(statements, null, translationContext.metadata(ctx.context()));
    }

    public static <StmtCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>
            StatementsAndExpr statementsAndExpr(
                    StatementsAndExprContext<StmtCtx, ExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<StmtCtx, Statement> visitStatement,
                    Function<ExprCtx, Expression> visitExpr) {
        List<Statement> statements = new ArrayList<>();
        if (ctx.statements() != null) {
            for (StmtCtx stmt : ctx.statements()) {
                statements.add(visitStatement.apply(stmt));
            }
        }
        Expression expression = visitExpr.apply(ctx.expr());
        return new StatementsAndExpr(statements, expression, translationContext.metadata(ctx.context()));
    }

    public static <StmtCtx extends ParserRuleContext, ExprCtx extends ParserRuleContext>
            StatementsAndOptionalExpr statementsAndOptionalExpr(
                    StatementsAndOptionalExprContext<StmtCtx, ExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<StmtCtx, Statement> visitStatement,
                    Function<ExprCtx, Expression> visitExpr) {
        List<Statement> statements = new ArrayList<>();
        if (ctx.statements() != null) {
            for (StmtCtx stmt : ctx.statements()) {
                statements.add(visitStatement.apply(stmt));
            }
        }
        Expression expression = ctx.expr() != null ? visitExpr.apply(ctx.expr()) : null;
        return new StatementsAndOptionalExpr(statements, expression, translationContext.metadata(ctx.context()));
    }

    public static <StmtCtx extends ParserRuleContext> BlockStatement blockStatement(
            BlockStatementContext<StmtCtx> ctx,
            TranslationContext translationContext,
            Function<StmtCtx, Statement> visitStatement) {
        List<Statement> statements = new ArrayList<>();
        if (ctx.statements() != null) {
            for (StmtCtx statement : ctx.statements()) {
                statements.add(visitStatement.apply(statement));
            }
        }
        return new BlockStatement(statements, translationContext.metadata(ctx.context()));
    }

    public static <StatementsAndExprCtx extends ParserRuleContext> BlockExpression blockExpr(
            BlockExprContext<StatementsAndExprCtx> ctx,
            TranslationContext translationContext,
            Function<StatementsAndExprCtx, StatementsAndExpr> visitStatementsAndExpr) {
        StatementsAndExpr statementsAndExpr = visitStatementsAndExpr.apply(ctx.statementsAndExpr());
        return new BlockExpression(statementsAndExpr, translationContext.metadata(ctx.context()));
    }
}
