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
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.compiler.context.scripting.FlworStatementContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.statement.Statement;

public final class LoopStatementTranslation {

    private LoopStatementTranslation() {}

    public static <
                    ForClauseCtx extends ParserRuleContext,
                    LetClauseCtx extends ParserRuleContext,
                    StmtCtx extends ParserRuleContext>
            FlowrStatement flworStatement(
                    FlworStatementContext<ForClauseCtx, LetClauseCtx, StmtCtx> ctx,
                    TranslationContext translationContext,
                    Function<ForClauseCtx, Clause> visitForClause,
                    Function<LetClauseCtx, Clause> visitLetClause,
                    Function<ParseTree, Clause> visitClause,
                    Function<StmtCtx, Statement> visitStatement) {
        Clause clause;
        // Check for start clause. Only for or let allowed.
        if (ctx.startFor() == null) {
            clause = visitLetClause.apply(ctx.startLet());
        } else {
            clause = visitForClause.apply(ctx.startFor());
        }
        Clause lastFlowrClause = clause.getLastClause();
        for (ParseTree child : ctx.intermediateClauses()) {
            clause = visitClause.apply(child);
            lastFlowrClause.chainWith(clause.getFirstClause());
            lastFlowrClause = clause.getLastClause();
        }
        Statement returnStatement = visitStatement.apply(ctx.returnStmt());
        ReturnStatementClause returnStatementClause =
                new ReturnStatementClause(returnStatement, returnStatement.getMetadata());
        lastFlowrClause.chainWith(returnStatementClause);
        returnStatementClause = returnStatementClause.detachInitialLetClausesForStatements();
        return new FlowrStatement(returnStatementClause, translationContext.metadata(ctx.context()));
    }
}
