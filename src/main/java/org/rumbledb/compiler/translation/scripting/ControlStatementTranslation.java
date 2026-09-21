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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.NameTestContext;
import org.rumbledb.compiler.context.scripting.SwitchStatementContext;
import org.rumbledb.compiler.context.scripting.TryCatchStatementContext;
import org.rumbledb.compiler.context.scripting.TypeSwitchStatementContext;
import org.rumbledb.compiler.translation.ControlTranslation;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.types.SequenceType;

public final class ControlStatementTranslation {

    private ControlStatementTranslation() {}

    public static <
                    CondExprCtx extends ParserRuleContext,
                    CaseExprCtx extends ParserRuleContext,
                    StmtCtx extends ParserRuleContext>
            SwitchStatement switchStatement(
                    SwitchStatementContext<CondExprCtx, CaseExprCtx, StmtCtx> ctx,
                    TranslationContext translationContext,
                    Function<CondExprCtx, Expression> visitExpr,
                    Function<CaseExprCtx, Expression> visitExprSingle,
                    Function<StmtCtx, Statement> visitStatement) {
        Expression condition = visitExpr.apply(ctx.cond());
        List<SwitchCaseStatement> cases = new ArrayList<>(ctx.cases().size());
        for (SwitchStatementContext.Case<CaseExprCtx, StmtCtx> stmt : ctx.cases()) {
            List<Expression> conditionExpressions = new ArrayList<>(stmt.cond().size());
            for (CaseExprCtx exprSingle : stmt.cond()) {
                conditionExpressions.add(visitExprSingle.apply(exprSingle));
            }
            cases.add(new SwitchCaseStatement(conditionExpressions, visitStatement.apply(stmt.ret())));
        }
        Statement defaultCase = visitStatement.apply(ctx.def());
        return new SwitchStatement(condition, cases, defaultCase, translationContext.metadata(ctx.context()));
    }

    public static <BlockStmtCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>
            TryCatchStatement tryCatchStatement(
                    TryCatchStatementContext<BlockStmtCtx, EqNameCtx> ctx,
                    TranslationContext translationContext,
                    Function<BlockStmtCtx, BlockStatement> visitBlockStatement,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        BlockStatement tryBlock = visitBlockStatement.apply(ctx.tryBlock());
        Map<CatchPattern, BlockStatement> catchBlockStatements = new LinkedHashMap<>();
        for (TryCatchStatementContext.Catch<BlockStmtCtx, EqNameCtx> catchCtx : ctx.catches()) {
            BlockStatement catchBlockStatement = visitBlockStatement.apply(catchCtx.catchBlock());
            for (NameTestContext<EqNameCtx> catchTarget : catchCtx.nameTests()) {
                CatchPattern pattern = ControlTranslation.catchPattern(catchTarget, translationContext, parseEqName);
                if (!catchBlockStatements.containsKey(pattern)) {
                    catchBlockStatements.put(pattern, catchBlockStatement);
                }
            }
        }
        return new TryCatchStatement(tryBlock, catchBlockStatements, translationContext.metadata(ctx.context()));
    }

    public static <
                    CondExprCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    StmtCtx extends ParserRuleContext>
            TypeSwitchStatement typeSwitchStatement(
                    TypeSwitchStatementContext<CondExprCtx, VarBindingCtx, SeqTypeCtx, StmtCtx> ctx,
                    TranslationContext translationContext,
                    Function<CondExprCtx, Expression> visitExpr,
                    Function<StmtCtx, Statement> visitStatement,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression condition = visitExpr.apply(ctx.cond());
        List<TypeSwitchStatementCase> cases = new ArrayList<>(ctx.cases().size());
        for (TypeSwitchStatementContext.Case<VarBindingCtx, SeqTypeCtx, StmtCtx> stmt : ctx.cases()) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (stmt.varRef() != null) {
                variableName = parseVariableBinding.apply(stmt.varRef());
            }
            if (stmt.union() != null && !stmt.union().isEmpty()) {
                for (SeqTypeCtx sequenceTypeContext : stmt.union()) {
                    union.add(processSequenceType.apply(sequenceTypeContext));
                }
            }
            Statement returnStatement = visitStatement.apply(stmt.ret());
            cases.add(new TypeSwitchStatementCase(variableName, union, returnStatement));
        }
        Name defaultVariableName = null;
        if (ctx.defaultVar() != null) {
            defaultVariableName = parseVariableBinding.apply(ctx.defaultVar());
        }
        Statement defaultStatement = visitStatement.apply(ctx.def());
        return new TypeSwitchStatement(
                condition,
                cases,
                new TypeSwitchStatementCase(defaultVariableName, defaultStatement),
                translationContext.metadata(ctx.context()));
    }
}
