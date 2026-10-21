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
                (InlineFunctionExpression) visit(expression.getExpression(), argument), expression.getMetadata());

        InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) fd.getExpression();

        Expression body = inlineFunctionExpression.getBody().getExpression();

        inlineFunctionExpression.hasTailCalls(markTailCalls(body, fd.getFunctionIdentifier()));

        return fd;
    }

    // Returns whether a call was marked.
    private boolean markTailCalls(Expression expression, FunctionIdentifier fd) {
        if (expression == null) {
            return false;
        }

        if (expression instanceof FunctionCallExpression functionCall) {
            if (isTailRecursiveCall(functionCall, fd)) {
                // System.err.println("Set tail call optimization for function " + fd.getFunctionIdentifier());
                functionCall.setTailCallOptimization(true);
                return true;
            }

            return false;
        }

        if (expression instanceof ConditionalExpression conditional) {
            boolean branchHasTailCalls = markTailCalls(conditional.getBranch(), fd);
            boolean elseBranchHasTailCalls = markTailCalls(conditional.getElseBranch(), fd);
            return branchHasTailCalls || elseBranchHasTailCalls;
        }

        return false;
    }

    private boolean isTailRecursiveCall(FunctionCallExpression functionCall, FunctionIdentifier fd) {
        return functionCall.getFunctionIdentifier().equals(fd)
                && functionCall.getArguments().size() == fd.getArity()
                && !functionCall.isPartialApplication();
    }
}
