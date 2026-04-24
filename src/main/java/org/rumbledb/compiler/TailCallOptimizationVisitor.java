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
        InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) fd.getExpression();
        Expression body = inlineFunctionExpression.getBody().getExpression();
        if (body instanceof ConditionalExpression) {
            if (((ConditionalExpression) body).getBranch() instanceof FunctionCallExpression) {
                if (
                    ((FunctionCallExpression) ((ConditionalExpression) body).getBranch()).getFunctionIdentifier()
                        .equals(fd.getFunctionIdentifier())
                ) {
                    if (
                        ((FunctionCallExpression) ((ConditionalExpression) body).getBranch()).getArguments()
                            .size() == fd.getChildren().size()
                    ) {
                        if (
                            !((FunctionCallExpression) ((ConditionalExpression) body).getBranch())
                                .isPartialApplication()
                        ) {
                            ((FunctionCallExpression) ((ConditionalExpression) body).getBranch())
                                .setTailCallOptimization(
                                    true
                                );
                        }
                    }
                }
            }
            if (((ConditionalExpression) body).getElseBranch() instanceof FunctionCallExpression) {
                if (
                    ((FunctionCallExpression) ((ConditionalExpression) body).getElseBranch()).getFunctionIdentifier()
                        .equals(fd.getFunctionIdentifier())
                ) {
                    if (
                        ((FunctionCallExpression) ((ConditionalExpression) body).getElseBranch()).getArguments()
                            .size() == fd.getChildren().size()
                    ) {
                        if (
                            !((FunctionCallExpression) ((ConditionalExpression) body).getElseBranch())
                                .isPartialApplication()
                        ) {
                            System.err.println("Set tail call optimization for function " + fd.getFunctionIdentifier());
                            ((FunctionCallExpression) ((ConditionalExpression) body).getElseBranch())
                                .setTailCallOptimization(
                                    true
                                );
                        }
                    }
                }
            }
        }
        return fd;
    }
}
