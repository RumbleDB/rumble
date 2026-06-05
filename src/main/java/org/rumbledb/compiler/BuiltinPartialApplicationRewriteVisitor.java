package org.rumbledb.compiler;

import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.types.SequenceType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Rewrites direct partial application of builtins, e.g. {@code fn:max(0, ?)},
 * into
 * an equivalent inline function, e.g. {@code function($x) { fn:max(0, $x) }}.
 * <p>
 * This keeps builtin partial application on the same code path as ordinary
 * inline functions.
 */
public class BuiltinPartialApplicationRewriteVisitor extends CloneVisitor {

    private InlineFunctionExpression rewriteBuiltinPartialApplication(
            Name functionName,
            BuiltinFunction builtin,
            List<Expression> arguments,
            Expression sourceExpression
    ) {
        List<SequenceType> parameterTypes = builtin.getSignature().getParameterTypes();
        Map<Name, SequenceType> params = new LinkedHashMap<>();

        /// We will create a new function call to builtin function, but this time
        /// replace ? with real parameters
        List<Expression> fullArguments = new ArrayList<>(arguments.size());

        for (int i = 0; i < arguments.size(); i++) {
            Expression currentArgument = arguments.get(i);
            if (currentArgument != null) {
                /// It was not a ?
                fullArguments.add(currentArgument);
                continue;
            }

            Name parameterName = Name.createVariableInNoNamespace(String.format("param%s", i));
            params.put(parameterName, parameterTypes.get(i));
            VariableReferenceExpression variableReference = new VariableReferenceExpression(
                    parameterName,
                    sourceExpression.getMetadata()
            );
            variableReference.setActualType(parameterTypes.get(i));
            fullArguments.add(variableReference);
        }

        FunctionCallExpression bodyCall = new FunctionCallExpression(
                functionName,
                fullArguments,
                sourceExpression.getMetadata()
        );
        StatementsAndOptionalExpr body = new StatementsAndOptionalExpr(
                Collections.emptyList(),
                bodyCall,
                sourceExpression.getMetadata()
        );
        return new InlineFunctionExpression(
                Collections.emptyList(),
                null,
                params,
                builtin.getSignature().getReturnType(),
                body,
                sourceExpression.getMetadata()
        );
    }

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        BuiltinFunction builtin = BuiltinFunctionCatalogue.getBuiltinFunction(expression.getFunctionIdentifier());

        if (!expression.isPartialApplication() || builtin == null) {
            /// In case of non-partial application or non-builtin function, we still need to
            /// keep descending
            /// Because a partial builtin function might be in the nested level
            /// See qt3 test hof-041
            return super.visitFunctionCall(expression, argument);
        }

        List<Expression> arguments = expression.getArguments()
            .stream()
            .map(expr -> expr != null ? (Expression) visit(expr, argument) : null)
            .collect(Collectors.toList());
        return rewriteBuiltinPartialApplication(expression.getFunctionName(), builtin, arguments, expression);
    }

    @Override
    public Node visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Node argument) {
        List<Expression> arguments = expression.getArguments()
            .stream()
            .map(expr -> expr != null ? (Expression) visit(expr, argument) : null)
            .collect(Collectors.toList());
        Expression rewrittenMainExpression = (Expression) visit(expression.getMainExpression(), argument);

        if (!(rewrittenMainExpression instanceof NamedFunctionReferenceExpression namedFunctionReference)) {
            return super.visitDynamicFunctionCallExpression(expression, argument);
        }

        BuiltinFunction builtin = BuiltinFunctionCatalogue.getBuiltinFunction(namedFunctionReference.getIdentifier());
        boolean isPartialApplication = arguments.stream().anyMatch(arg -> arg == null);
        if (!isPartialApplication || builtin == null) {
            return super.visitDynamicFunctionCallExpression(expression, argument);
        }

        return rewriteBuiltinPartialApplication(
            namedFunctionReference.getIdentifier().getName(),
            builtin,
            arguments,
            expression
        );
    }
}
