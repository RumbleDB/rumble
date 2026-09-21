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
import org.antlr.v4.runtime.tree.ParseTree;

import org.rumbledb.compiler.context.CountClauseContext;
import org.rumbledb.compiler.context.FlworExprContext;
import org.rumbledb.compiler.context.ForClauseContext;
import org.rumbledb.compiler.context.ForVarContext;
import org.rumbledb.compiler.context.GroupByClauseContext;
import org.rumbledb.compiler.context.GroupByVarContext;
import org.rumbledb.compiler.context.LetClauseContext;
import org.rumbledb.compiler.context.LetVarContext;
import org.rumbledb.compiler.context.OrderByClauseContext;
import org.rumbledb.compiler.context.OrderByExprContext;
import org.rumbledb.compiler.context.WhereClauseContext;
import org.rumbledb.compiler.context.WindowClauseContext;
import org.rumbledb.compiler.context.WindowConditionContext;
import org.rumbledb.compiler.context.WindowVarsContext;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.PositionalVariableNameSameAsForVariableException;
import org.rumbledb.exceptions.UnknownCollationException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.GroupByVariableDeclaration;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.flowr.WindowClause;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

public final class FlworTranslation {

    private FlworTranslation() {}

    public static <ExprSingleCtx extends ParserRuleContext> FlworExpression flworExpr(
            FlworExprContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ParseTree, Clause> visitClause,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Clause clause = visitClause.apply(ctx.startClause());
        if (clause == null) {
            throw new UnsupportedFeatureException(
                    "FLOWR clause not implemented yet", translationContext.metadata(ctx.context()));
        }
        Clause previousFLWORClause = clause.getLastClause();

        for (ParseTree child : ctx.intermediateClauses()) {
            Clause nextClause = visitClause.apply(child);
            if (nextClause == null) {
                throw new UnsupportedFeatureException(
                        "FLOWR clause not implemented yet", translationContext.metadata(ctx.context()));
            }
            previousFLWORClause.chainWith(nextClause.getFirstClause());
            previousFLWORClause = nextClause.getLastClause();
        }

        Expression returnExpr = visitExprSingle.apply(ctx.returnExpr());
        ReturnClause returnClause = new ReturnClause(returnExpr, returnExpr.getMetadata());
        previousFLWORClause.chainWith(returnClause);

        returnClause = returnClause.detachInitialLetClauses();

        return new FlworExpression(returnClause, translationContext.metadata(ctx.context()));
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            ForClause forClause(
                    ForClauseContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        ForClause clause = null;
        for (ForVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> varCtx : ctx.vars()) {
            ForClause newClause =
                    forVar(varCtx, translationContext, parseVariableBinding, processSequenceType, visitExprSingle);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }
        return clause;
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            ForClause forVar(
                    ForVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        SequenceType seq = null;
        Name var = parseVariableBinding.apply(ctx.varRef());
        if (ctx.seq() != null) {
            seq = processSequenceType.apply(ctx.seq());
        }
        boolean emptyFlag = ctx.allowingEmpty();
        Name atVar = null;
        if (ctx.at() != null) {
            atVar = parseVariableBinding.apply(ctx.at());
            if (atVar.equals(var)) {
                throw new PositionalVariableNameSameAsForVariableException(
                        "Positional variable " + var + " cannot have the same name as the main for variable.",
                        translationContext.metadata(ctx.at()));
            }
        }
        Expression expr = visitExprSingle.apply(ctx.exprSingle());
        // If the sequenceType is specified, we have to "extend" its arity to *
        // because TreatIterator is wrapping the whole assignment expression,
        // meaning there is not one TreatIterator for each variable we loop over.
        if (seq != null) {
            SequenceType expressionType = new SequenceType(seq.getItemType(), SequenceType.Arity.ZeroOrMore);
            expr = new TreatExpression(expr, expressionType, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }

        return new ForClause(var, emptyFlag, seq, atVar, expr, translationContext.metadata(ctx.context()));
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            LetClause letClause(
                    LetClauseContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        LetClause clause = null;
        for (LetVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> varCtx : ctx.vars()) {
            LetClause newClause =
                    letVar(varCtx, translationContext, parseVariableBinding, processSequenceType, visitExprSingle);
            if (clause != null) {
                clause.chainWith(newClause);
            }
            clause = newClause;
        }
        return clause;
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            LetClause letVar(
                    LetVarContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        SequenceType seq = null;
        Name var = parseVariableBinding.apply(ctx.varRef());
        if (ctx.seq() != null) {
            seq = processSequenceType.apply(ctx.seq());
        }

        Expression expr = visitExprSingle.apply(ctx.exprSingle());
        if (seq != null) {
            expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
        }

        return new LetClause(var, seq, expr, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> WhereClause whereClause(
            WhereClauseContext<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        Expression expr = visitExprSingle.apply(ctx.exprSingle());
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
                    Function<ExprSingleCtx, Expression> visitExprSingle,
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
            Expression expr = varCtx.exprSingle() != null ? visitExprSingle.apply(varCtx.exprSingle()) : null;
            Name var = parseVariableBinding.apply(varCtx.varRef());
            vars.add(new GroupByVariableDeclaration(var, seq, expr, collationUri));
        }
        return new GroupByClause(vars, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext, UriLiteralCtx extends ParserRuleContext>
            OrderByClause orderByClause(
                    OrderByClauseContext<ExprSingleCtx, UriLiteralCtx> ctx,
                    TranslationContext translationContext,
                    Function<ExprSingleCtx, Expression> visitExprSingle,
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
            Expression expression = visitExprSingle.apply(exprCtx.exprSingle());
            exprs.add(new OrderByClauseSortingKey(expression, exprCtx.ascending(), uri, exprCtx.emptyOrder()));
        }
        return new OrderByClause(exprs, ctx.stable(), translationContext.metadata(ctx.context()));
    }

    public static <
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            WindowClause windowClause(
                    WindowClauseContext<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        Name windowVariable = parseVariableBinding.apply(ctx.windowVariable());
        SequenceType sequenceType = ctx.seqType() == null ? null : processSequenceType.apply(ctx.seqType());
        Expression expression = visitExprSingle.apply(ctx.expression());
        WindowClause.WindowCondition start =
                buildWindowCondition(ctx.startCondition(), parseVariableBinding, visitExprSingle);
        WindowClause.WindowCondition end = ctx.endCondition() == null
                ? null
                : buildWindowCondition(ctx.endCondition(), parseVariableBinding, visitExprSingle);
        validateWindowVariables(windowVariable, start, end, translationContext.metadata(ctx.context()));
        return new WindowClause(
                ctx.windowType(),
                windowVariable,
                sequenceType,
                expression,
                start,
                end,
                translationContext.metadata(ctx.context()));
    }

    private static <VarBindingCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            WindowClause.WindowCondition buildWindowCondition(
                    WindowConditionContext<VarBindingCtx, ExprSingleCtx> ctx,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        return new WindowClause.WindowCondition(
                buildWindowVars(ctx.vars(), parseVariableBinding), visitExprSingle.apply(ctx.exprSingle()), ctx.only());
    }

    private static <VarBindingCtx extends ParserRuleContext> WindowClause.WindowVars buildWindowVars(
            WindowVarsContext<VarBindingCtx> ctx, Function<VarBindingCtx, Name> parseVariableBinding) {
        Name current = ctx.currentItem() == null ? null : parseVariableBinding.apply(ctx.currentItem());
        Name position = ctx.positionalVar() == null ? null : parseVariableBinding.apply(ctx.positionalVar());
        Name previous = ctx.previousItem() == null ? null : parseVariableBinding.apply(ctx.previousItem());
        Name next = ctx.nextItem() == null ? null : parseVariableBinding.apply(ctx.nextItem());
        return new WindowClause.WindowVars(current, position, previous, next);
    }

    private static void validateWindowVariables(
            Name windowVariable,
            WindowClause.WindowCondition start,
            WindowClause.WindowCondition end,
            ExceptionMetadata metadata) {
        List<Name> names = new ArrayList<>();
        names.add(windowVariable);
        names.addAll(start.variables().names());
        if (end != null) {
            names.addAll(end.variables().names());
        }
        if (names.size() != names.stream().distinct().count()) {
            throw new ParsingException(
                    "All variables in a window clause must have distinct names",
                    ErrorCode.DuplicatedVariableNameInWindowCode,
                    metadata);
        }
    }
}
