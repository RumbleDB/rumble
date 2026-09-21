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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.ContextItemDeclContext;
import org.rumbledb.compiler.context.FunctionDeclContext;
import org.rumbledb.compiler.context.OptionDeclContext;
import org.rumbledb.compiler.context.VarDeclContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.utils.FunctionDeclarationValidator;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.module.OptionDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

/** Translates grammar-neutral declaration contexts into expression-tree declarations. */
public final class DeclarationTranslation {

    private DeclarationTranslation() {}

    public static <EqNameCtx extends ParserRuleContext, StringLiteralCtx extends ParserRuleContext>
            OptionDeclaration optionDecl(
                    OptionDeclContext<EqNameCtx, StringLiteralCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<StringLiteralCtx, String> processStringLiteral) {
        Name name = parseEqName.apply(ctx.name(), NameRole.NO_DEFAULT_NAMESPACE);
        String value = processStringLiteral.apply(ctx.value());
        return new OptionDeclaration(name, value, translationContext.metadata(ctx.context()));
    }

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            VariableDeclaration varDecl(
                    VarDeclContext<AnnotationsCtx, VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        SequenceType seq = null;
        Name var = parseVariableBinding.apply(ctx.varBinding());
        if (ctx.sequenceType() != null) {
            seq = processSequenceType.apply(ctx.sequenceType());
        }
        boolean external = ctx.isExternal();
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = visitExprSingle.apply(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }
        return new VariableDeclaration(
                var,
                external,
                seq,
                expr,
                annotations,
                translationContext.metadata(ctx.context()),
                translationContext.metadata(ctx.varBinding()));
    }

    public static <SeqTypeCtx extends ParserRuleContext, ExprSingleCtx extends ParserRuleContext>
            VariableDeclaration contextItemDecl(
                    ContextItemDeclContext<SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        SequenceType seq = null;
        if (ctx.sequenceType() != null) {
            seq = processSequenceType.apply(ctx.sequenceType());
        }
        boolean external = ctx.isExternal();
        Expression expr = null;
        if (ctx.exprSingle() != null) {
            expr = visitExprSingle.apply(ctx.exprSingle());
            if (seq != null) {
                expr = new TreatExpression(expr, seq, ErrorCode.UnexpectedTypeErrorCode, expr.getMetadata());
            }
        }
        return new VariableDeclaration(
                Name.CONTEXT_ITEM, external, seq, expr, null, translationContext.metadata(ctx.context()));
    }

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    FunctionNameCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ReturnTypeCtx extends ParserRuleContext,
                    FnBodyCtx extends ParserRuleContext>
            InlineFunctionExpression functionDecl(
                    FunctionDeclContext<
                                    AnnotationsCtx,
                                    FunctionNameCtx,
                                    VarBindingCtx,
                                    SeqTypeCtx,
                                    ReturnTypeCtx,
                                    FnBodyCtx>
                            ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<FunctionNameCtx, Name> parseFunctionName,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ReturnTypeCtx, SequenceType> processReturnType,
                    Function<FnBodyCtx, StatementsAndOptionalExpr> visitStatementsAndOptionalExpr) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        Name name = parseFunctionName.apply(ctx.functionName());
        FunctionDeclarationValidator.validateFunctionName(name, translationContext.metadata(ctx.functionName()));
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = null;
        for (FunctionDeclContext.FunctionParam<VarBindingCtx, SeqTypeCtx> param : ctx.params()) {
            Name paramName = parseVariableBinding.apply(param.name());
            if (fnParams.containsKey(paramName)) {
                throw new DuplicateParamNameException(name, paramName, translationContext.metadata(param.context()));
            }
            SequenceType paramType = param.sequenceType() == null
                    ? SequenceType.createSequenceType("item*")
                    : processSequenceType.apply(param.sequenceType());
            fnParams.put(paramName, paramType);
        }
        if (ctx.returnType() != null) {
            fnReturnType = processReturnType.apply(ctx.returnType());
        }
        StatementsAndOptionalExpr funcBody = visitStatementsAndOptionalExpr.apply(ctx.fnBody());
        return new InlineFunctionExpression(
                annotations,
                name,
                fnParams,
                fnReturnType,
                funcBody,
                ctx.isExternal(),
                translationContext.metadata(ctx.context()),
                translationContext.metadata(ctx.functionName()));
    }
}
