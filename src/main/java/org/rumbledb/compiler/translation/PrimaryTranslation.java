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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.ArrayConstructorContext;
import org.rumbledb.compiler.context.FunctionCallContext;
import org.rumbledb.compiler.context.InlineFunctionExprContext;
import org.rumbledb.compiler.context.LiteralExprContext;
import org.rumbledb.compiler.context.NamedFunctionRefContext;
import org.rumbledb.compiler.context.ParenthesizedExprContext;
import org.rumbledb.compiler.context.ValueExprContext;
import org.rumbledb.compiler.context.VarRefContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.compiler.utils.AnnotationValidator;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DuplicateParamNameException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NumericOverflowOrUnderflow;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.primary.ArrayConstructorExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.ContextItemExpression;
import org.rumbledb.expressions.primary.DecimalLiteralExpression;
import org.rumbledb.expressions.primary.DoubleLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.NullLiteralExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.types.SequenceType;

public final class PrimaryTranslation {

    private PrimaryTranslation() {}

    public static <SimpleMapExprCtx extends ParserRuleContext, ValidateExprCtx extends ParserRuleContext>
            Expression valueExpr(
                    ValueExprContext<SimpleMapExprCtx, ValidateExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<SimpleMapExprCtx, Expression> visitSimpleMapExpr,
                    Function<ValidateExprCtx, Expression> visitValidateExpr) {
        if (ctx.simpleMapExpr() != null) {
            return visitSimpleMapExpr.apply(ctx.simpleMapExpr());
        }
        if (ctx.validateExpr() != null) {
            return visitValidateExpr.apply(ctx.validateExpr());
        }
        // TODO: extension expression still unsupported
        throw new UnsupportedFeatureException(
                "Extension expression still unsupported", translationContext.metadata(ctx.context()));
    }

    public static <ExprCtx extends ParserRuleContext> Expression parenthesizedExpr(
            ParenthesizedExprContext<ExprCtx> ctx,
            TranslationContext translationContext,
            Function<ExprCtx, Expression> visitExpr) {
        if (ctx.expr() == null) {
            return new CommaExpression(translationContext.metadata(ctx.context()));
        }
        return visitExpr.apply(ctx.expr());
    }

    public static <EqNameCtx extends ParserRuleContext> VariableReferenceExpression varRef(
            VarRefContext<EqNameCtx> ctx,
            TranslationContext translationContext,
            BiFunction<EqNameCtx, NameRole, Name> parseEqName) {
        Name name = parseEqName.apply(ctx.eqName(), NameRole.NO_DEFAULT_NAMESPACE);
        return new VariableReferenceExpression(name, translationContext.metadata(ctx.context()));
    }

    public static ContextItemExpression contextItemExpr(ParserRuleContext ctx, TranslationContext translationContext) {
        return new ContextItemExpression(translationContext.metadata(ctx));
    }

    public static <StringLiteralCtx extends ParserRuleContext> Expression literal(
            LiteralExprContext<StringLiteralCtx> ctx,
            TranslationContext translationContext,
            Function<StringLiteralCtx, String> processStringLiteral) {
        if (ctx.stringLiteral() != null) {
            return new StringLiteralExpression(
                    processStringLiteral.apply(ctx.stringLiteral()), translationContext.metadata(ctx.context()));
        }
        if (ctx.numericLiteralText() != null) {
            return literalExpressionFromToken(ctx.numericLiteralText(), translationContext.metadata(ctx.context()));
        }
        throw new UnsupportedFeatureException(
                "Literal not yet implemented", translationContext.metadata(ctx.context()));
    }

    public static Expression literalExpressionFromToken(String token, ExceptionMetadata metadata) {
        switch (token) {
            case "null":
                return new NullLiteralExpression(metadata);
            case "true":
                return new BooleanLiteralExpression(true, metadata);
            case "false":
                return new BooleanLiteralExpression(false, metadata);
            default:
        }
        if (token.contains("E") || token.contains("e")) {
            return new DoubleLiteralExpression(Double.parseDouble(token), metadata);
        }
        if (token.contains(".")) {
            return new DecimalLiteralExpression(new BigDecimal(token), metadata);
        }
        return new IntegerLiteralExpression(token, metadata);
    }

    public static <FunctionNameCtx extends ParserRuleContext, ArgumentCtx extends ParserRuleContext>
            FunctionCallExpression functionCall(
                    FunctionCallContext<FunctionNameCtx, ArgumentCtx> ctx,
                    TranslationContext translationContext,
                    Function<FunctionNameCtx, Name> parseFunctionName,
                    Function<ArgumentCtx, Expression> visitArgument) {
        Name name = parseFunctionName.apply(ctx.functionName());
        List<Expression> arguments = new ArrayList<>();
        for (ArgumentCtx arg : ctx.arguments()) {
            arguments.add(visitArgument.apply(arg));
        }
        return new FunctionCallExpression(name, arguments, translationContext.metadata(ctx.context()));
    }

    public static <FunctionNameCtx extends ParserRuleContext> NamedFunctionReferenceExpression namedFunctionRef(
            NamedFunctionRefContext<FunctionNameCtx> ctx,
            TranslationContext translationContext,
            Function<FunctionNameCtx, Name> parseFunctionName) {
        Name name = parseFunctionName.apply(ctx.functionName());
        try {
            int arity = Integer.parseInt(ctx.arityLiteral());
            return new NamedFunctionReferenceExpression(
                    new FunctionIdentifier(name, arity), translationContext.metadata(ctx.context()));
        } catch (NumberFormatException e) {
            throw new NumericOverflowOrUnderflow(
                    "Named function reference arity is out of range for implementation limits: "
                            + name
                            + "#"
                            + ctx.arityLiteral(),
                    translationContext.metadata(ctx.context()));
        }
    }

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    FnBodyCtx extends ParserRuleContext>
            InlineFunctionExpression inlineFunctionExpr(
                    InlineFunctionExprContext<AnnotationsCtx, VarBindingCtx, SeqTypeCtx, FnBodyCtx> ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<FnBodyCtx, StatementsAndOptionalExpr> visitStatementsAndOptionalExpr) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        AnnotationValidator.validateInlineFunctionAnnotations(annotations);
        LinkedHashMap<Name, SequenceType> fnParams = new LinkedHashMap<>();
        SequenceType fnReturnType = SequenceType.createSequenceType("item*");
        if (ctx.params() != null) {
            for (InlineFunctionExprContext.InlineFunctionParam<VarBindingCtx, SeqTypeCtx> param : ctx.params()) {
                Name paramName = parseVariableBinding.apply(param.name());
                SequenceType paramType = SequenceType.createSequenceType("item*");
                if (fnParams.containsKey(paramName)) {
                    throw new DuplicateParamNameException(
                            Name.createVariableInDefaultFunctionNamespace("inline-function`"),
                            paramName,
                            translationContext.metadata(param.context()));
                }
                if (param.sequenceType() != null) {
                    paramType = processSequenceType.apply(param.sequenceType());
                } else {
                    paramType = SequenceType.createSequenceType("item*");
                }
                fnParams.put(paramName, paramType);
            }
        }

        if (ctx.returnType() != null) {
            fnReturnType = processSequenceType.apply(ctx.returnType());
        }

        StatementsAndOptionalExpr funcBody = visitStatementsAndOptionalExpr.apply(ctx.fnBody());

        return new InlineFunctionExpression(
                annotations, null, fnParams, fnReturnType, funcBody, translationContext.metadata(ctx.context()));
    }

    public static <ExprSingleCtx extends ParserRuleContext> ArrayConstructorExpression squareArrayConstructor(
            ArrayConstructorContext.Square<ExprSingleCtx> ctx,
            TranslationContext translationContext,
            Function<ExprSingleCtx, Expression> visitExprSingle) {
        List<ExprSingleCtx> memberCtxs = ctx.members();
        if (memberCtxs == null || memberCtxs.isEmpty()) {
            return new ArrayConstructorExpression(new ArrayList<>(), true, translationContext.metadata(ctx.context()));
        }
        List<Expression> memberExpressions = new ArrayList<>();
        if (translationContext.moduleContext().getQueryLanguage().equals("jsoniq10")) {
            for (ExprSingleCtx memberCtx : memberCtxs) {
                memberExpressions.add(visitExprSingle.apply(memberCtx));
            }
            Expression commaExpression =
                    new CommaExpression(memberExpressions, translationContext.metadata(ctx.context()));
            return new ArrayConstructorExpression(commaExpression, translationContext.metadata(ctx.context()));
        } else {
            for (ExprSingleCtx memberCtx : memberCtxs) {
                memberExpressions.add(visitExprSingle.apply(memberCtx));
            }
            return new ArrayConstructorExpression(memberExpressions, true, translationContext.metadata(ctx.context()));
        }
    }

    public static <EnclosedExprCtx extends ParserRuleContext> ArrayConstructorExpression curlyArrayConstructor(
            ArrayConstructorContext.Curly<EnclosedExprCtx> ctx,
            TranslationContext translationContext,
            Function<EnclosedExprCtx, Expression> visitEnclosedExpression) {
        if (ctx.enclosedExpression() == null) {
            return new ArrayConstructorExpression(translationContext.metadata(ctx.context()));
        }
        Expression content = visitEnclosedExpression.apply(ctx.enclosedExpression());
        return new ArrayConstructorExpression(content, translationContext.metadata(ctx.context()));
    }
}
