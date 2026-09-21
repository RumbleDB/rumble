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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.IfExprContext;
import org.rumbledb.compiler.context.NameTestContext;
import org.rumbledb.compiler.context.SwitchExprContext;
import org.rumbledb.compiler.context.TryCatchExprContext;
import org.rumbledb.compiler.context.TypeswitchExprContext;
import org.rumbledb.compiler.context.WildcardContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.PrefixCannotBeExpandedException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchCase;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.types.SequenceType;

public final class ControlTranslation {

    private ControlTranslation() {}

    public static <TestExprCtx extends ParserRuleContext, BranchExprCtx extends ParserRuleContext>
            ConditionalExpression ifExpr(
                    IfExprContext<TestExprCtx, BranchExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<TestExprCtx, Expression> visitExpr,
                    Function<BranchExprCtx, Expression> visitExprSingle) {
        Expression condition = visitExpr.apply(ctx.testCondition());
        Expression branch = visitExprSingle.apply(ctx.branch());
        Expression elseBranch = visitExprSingle.apply(ctx.elseBranch());
        return new ConditionalExpression(condition, branch, elseBranch, translationContext.metadata(ctx.context()));
    }

    public static <CondExprCtx extends ParserRuleContext, CaseExprCtx extends ParserRuleContext>
            SwitchExpression switchExpr(
                    SwitchExprContext<CondExprCtx, CaseExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<CondExprCtx, Expression> visitExpr,
                    Function<CaseExprCtx, Expression> visitExprSingle) {
        Expression condition = visitExpr.apply(ctx.cond());
        List<SwitchCase> cases = new ArrayList<>(ctx.cases().size());
        for (SwitchExprContext.Case<CaseExprCtx> caseClause : ctx.cases()) {
            List<Expression> conditionExpressions =
                    new ArrayList<>(caseClause.cond().size());
            for (CaseExprCtx expr : caseClause.cond()) {
                conditionExpressions.add(visitExprSingle.apply(expr));
            }
            SwitchCase c = new SwitchCase(conditionExpressions, visitExprSingle.apply(caseClause.ret()));
            cases.add(c);
        }
        Expression defaultCase = visitExprSingle.apply(ctx.def());
        return new SwitchExpression(condition, cases, defaultCase, translationContext.metadata(ctx.context()));
    }

    public static <
                    CondExprCtx extends ParserRuleContext,
                    CaseExprCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext>
            TypeSwitchExpression typeswitchExpr(
                    TypeswitchExprContext<CondExprCtx, CaseExprCtx, VarBindingCtx, SeqTypeCtx> ctx,
                    TranslationContext translationContext,
                    Function<CondExprCtx, Expression> visitExpr,
                    Function<CaseExprCtx, Expression> visitExprSingle,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType) {
        Expression condition = visitExpr.apply(ctx.cond());
        List<TypeswitchCase> cases = new ArrayList<>(ctx.cases().size());
        for (TypeswitchExprContext.Case<CaseExprCtx, VarBindingCtx, SeqTypeCtx> expr : ctx.cases()) {
            List<SequenceType> union = new ArrayList<>();
            Name variableName = null;
            if (expr.varRef() != null) {
                variableName = parseVariableBinding.apply(expr.varRef());
            }
            if (expr.union() != null && !expr.union().isEmpty()) {
                for (SeqTypeCtx sequenceType : expr.union()) {
                    union.add(processSequenceType.apply(sequenceType));
                }
            }
            Expression expression = visitExprSingle.apply(expr.ret());
            cases.add(new TypeswitchCase(variableName, union, expression));
        }
        Name defaultVariableName = null;
        if (ctx.defaultVar() != null) {
            defaultVariableName = parseVariableBinding.apply(ctx.defaultVar());
        }
        Expression defaultCase = visitExprSingle.apply(ctx.def());
        return new TypeSwitchExpression(
                condition,
                cases,
                new TypeswitchCase(defaultVariableName, defaultCase),
                translationContext.metadata(ctx.context()));
    }

    public static CatchPattern wildcardPattern(WildcardContext ctx, TranslationContext translationContext) {
        return switch (ctx.type()) {
            case ALL -> CatchPattern.catchAll();
            case ALL_WITH_LOCAL -> CatchPattern.namespaceWildcard(ctx.localName(), ctx.text());
            case ALL_WITH_NS -> {
                String namespace = translationContext.resolveNamespace(ctx.prefix());
                if (namespace == null) {
                    throw new PrefixCannotBeExpandedException(
                            "Cannot expand prefix " + ctx.prefix(), translationContext.metadata(ctx.context()));
                }
                yield CatchPattern.localNameWildcard(namespace, ctx.text());
            }
            case BRACED_URI_LITERAL -> CatchPattern.localNameWildcard(ctx.uri(), ctx.text());
        };
    }

    public static <EqNameCtx extends ParserRuleContext> CatchPattern catchPattern(
            NameTestContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        if (ctx.wildcard() != null) {
            return wildcardPattern(ctx.wildcard(), translationContext);
        }
        return CatchPattern.exact(parseEqName.apply(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE));
    }

    public static <ExprCtx extends ParserRuleContext, EqNameCtx extends ParserRuleContext>
            TryCatchExpression tryCatchExpr(
                    TryCatchExprContext<ExprCtx, EqNameCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprCtx, Expression> visitExpr,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Expression tryExpression = ctx.tryExpr() == null
                ? new CommaExpression(translationContext.metadata(ctx.context()))
                : visitExpr.apply(ctx.tryExpr());
        Map<CatchPattern, Expression> catchExpressions = new LinkedHashMap<>();
        for (TryCatchExprContext.Catch<ExprCtx, EqNameCtx> catchCtx : ctx.catches()) {
            Expression catchExpression = catchCtx.catchExpr() == null
                    ? new CommaExpression(translationContext.metadata(catchCtx.context()))
                    : visitExpr.apply(catchCtx.catchExpr());
            for (NameTestContext<EqNameCtx> catchTarget : catchCtx.nameTests()) {
                CatchPattern pattern = catchPattern(catchTarget, translationContext, parseEqName);
                if (!catchExpressions.containsKey(pattern)) {
                    catchExpressions.put(pattern, catchExpression);
                }
            }
        }
        return new TryCatchExpression(tryExpression, catchExpressions, translationContext.metadata(ctx.context()));
    }
}
