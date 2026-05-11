package org.rumbledb.compiler;

import org.rumbledb.context.FunctionIdentifier;
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

        markTailCalls(body, fd.getFunctionIdentifier());

        return fd;
    }

    private void markTailCalls(Expression expression, FunctionIdentifier fd) {
        if (expression == null) {
            return;
        }

        if (expression instanceof FunctionCallExpression functionCall) {
            if (isTailRecursiveCall(functionCall, fd)) {
                // System.err.println("Set tail call optimization for function " + fd.getFunctionIdentifier());
                functionCall.setTailCallOptimization(true);
            }

            return;
        }

        if (expression instanceof ConditionalExpression conditional) {
            markTailCalls(conditional.getBranch(), fd);
            markTailCalls(conditional.getElseBranch(), fd);
        }
    }

    private boolean isTailRecursiveCall(FunctionCallExpression functionCall, FunctionIdentifier fd) {
        return functionCall.getFunctionIdentifier().equals(fd)
            && functionCall.getArguments().size() == fd.getArity()
            && !functionCall.isPartialApplication();
    }
}
