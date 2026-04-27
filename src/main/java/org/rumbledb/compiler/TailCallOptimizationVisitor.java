package org.rumbledb.compiler;

import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;

public class TailCallOptimizationVisitor extends CloneVisitor {

    @Override
    public Node visitFunctionDeclaration(FunctionDeclaration expression, Node argument) {
        FunctionDeclaration fd = new FunctionDeclaration(
                (InlineFunctionExpression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );

        InlineFunctionExpression inlineFunctionExpression =
            (InlineFunctionExpression) fd.getExpression();

        Expression body = inlineFunctionExpression.getBody().getExpression();

        markTailCalls(body, fd);

        return fd;
    }

    private void markTailCalls(Expression expression, FunctionDeclaration fd) {
        if (expression == null) {
            return;
        }

        if (expression instanceof FunctionCallExpression) {
            FunctionCallExpression functionCall = (FunctionCallExpression) expression;
            if (isTailRecursiveCall(functionCall, fd)) {
                // System.err.println("Set tail call optimization for function " + fd.getFunctionIdentifier());
                functionCall.setTailCallOptimization(true);
            }

            return;
        }

        if (expression instanceof ConditionalExpression) {
            ConditionalExpression conditional = (ConditionalExpression) expression;
            markTailCalls(conditional.getBranch(), fd);
            markTailCalls(conditional.getElseBranch(), fd);
        }
    }

    private boolean isTailRecursiveCall(FunctionCallExpression functionCall, FunctionDeclaration fd) {
        return functionCall.getFunctionIdentifier().equals(fd.getFunctionIdentifier())
            && functionCall.getArguments().size() == fd.getFunctionIdentifier().getArity()
            && !functionCall.isPartialApplication();
    }
}
