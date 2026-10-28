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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.FunctionArgumentConversionExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CreateCollectionExpression;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.DeleteIndexFromCollectionExpression;
import org.rumbledb.expressions.update.DeleteSearchFromCollectionExpression;
import org.rumbledb.expressions.update.EditCollectionExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.InsertIndexIntoCollectionExpression;
import org.rumbledb.expressions.update.InsertSearchIntoCollectionExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.expressions.update.TruncateCollectionExpression;
import org.rumbledb.types.SequenceType;

import static org.rumbledb.expressions.module.Prolog.getFunctionDeclarationFromProlog;

public class FunctionInliningVisitor extends CloneVisitor {

    private Prolog prolog;

    private boolean isImportedFunction(Prolog prolog, FunctionDeclaration target) {
        for (var module : prolog.getImportedModules()) {
            if (module.getProlog().getFunctionDeclarations().contains(target)) {
                // Rebuilding an imported body in the caller's context loses module-local
                // namespaces, collations, decimal formats and other static settings.
                return true;
            }
            if (isImportedFunction(module.getProlog(), target)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVariableReferenced(Node expression, Name name) {
        if (expression instanceof VariableReferenceExpression variableReference) {
            return variableReference.getVariableName().equals(name);
        }
        if (expression instanceof Clause clause) {
            if (clause.getPreviousClause() != null && isVariableReferenced(clause.getPreviousClause(), name)) {
                return true;
            }
        }
        for (Node child : expression.getChildren()) {
            if (isVariableReferenced(child, name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether an expression may be updating: an update, or a call to a function declared updating. A copy-modify
     * expression only updates its copies, and a function item is not updating itself.
     */
    private boolean mayBeUpdating(Node node) {
        if (node instanceof InsertExpression
                || node instanceof DeleteExpression
                || node instanceof ReplaceExpression
                || node instanceof RenameExpression
                || node instanceof AppendExpression
                || node instanceof CreateCollectionExpression
                || node instanceof DeleteIndexFromCollectionExpression
                || node instanceof DeleteSearchFromCollectionExpression
                || node instanceof EditCollectionExpression
                || node instanceof InsertIndexIntoCollectionExpression
                || node instanceof InsertSearchIntoCollectionExpression
                || node instanceof TruncateCollectionExpression) {
            return true;
        }
        if (node instanceof TransformExpression transform) {
            return mayBeUpdating(transform.getReturnExpression());
        }
        if (node instanceof InlineFunctionExpression) {
            return false;
        }
        if (node instanceof FunctionCallExpression call) {
            FunctionDeclaration target = getFunctionDeclarationFromProlog(this.prolog, call.getFunctionIdentifier());
            if (target != null && ((InlineFunctionExpression) target.getExpression()).isUpdating()) {
                return true;
            }
        }
        if (node instanceof Clause clause && clause.getPreviousClause() != null) {
            if (mayBeUpdating(clause.getPreviousClause())) {
                return true;
            }
        }
        for (Node child : node.getChildren()) {
            if (child != null && mayBeUpdating(child)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVariableReferenced(List<Expression> expressions, Name name, int index) {
        for (int i = 0; i < index; i++) {
            if (isVariableReferenced(expressions.get(i), name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean allArgumentsMatch(FunctionCallExpression expression, List<Name> paramNames) {
        boolean allArgumentsMatch = true;
        for (int i = 0; i < expression.getArguments().size(); i++) {
            allArgumentsMatch = allArgumentsMatch
                    && expression.getArguments().get(i) instanceof VariableReferenceExpression variableReference
                    && paramNames.get(i).equals(variableReference.getVariableName());
        }
        return allArgumentsMatch;
    }

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        this.prolog = mainModule.getProlog();
        MainModule result = new MainModule(
                mainModule.getProlog(),
                (Program) visit(mainModule.getProgram(), mainModule.getProlog()),
                mainModule.getMetadata());
        result.setStaticContext(mainModule.getStaticContext());
        return result;
    }

    // An inlined call converts its arguments exactly like the function call it replaces.
    private static Expression convertArgument(
            Expression argument, SequenceType parameterType, FunctionCallExpression call) {
        return new FunctionArgumentConversionExpression(
                argument, parameterType, call.getFunctionIdentifier().getName(), argument.getMetadata());
    }

    // Return value errors are reported at the function body, as when the function is called without inlining.
    private static FunctionArgumentConversionExpression convertReturnValue(
            Expression value, SequenceType returnType, FunctionCallExpression call, InlineFunctionExpression function) {
        return new FunctionArgumentConversionExpression(
                value,
                returnType,
                call.getFunctionIdentifier().getName(),
                true,
                function.getBody().getMetadata());
    }

    // Inlining is disabled for functions that:
    // 1. Are sequential.
    // 2. Contain an exit statement.
    // It is also disabled for calls with an argument that may be updating, which is an error (XUST0001). Updating
    // expressions are classified after inlining, which binds an unused argument in a let clause that is then removed,
    // so the call is kept for this error to be reported.
    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        FunctionDeclaration targetFunction =
                getFunctionDeclarationFromProlog(this.prolog, expression.getFunctionIdentifier());
        if (expression.isPartialApplication()
                || targetFunction == null
                // Imported function bodies must retain their module's static settings.
                || isImportedFunction(this.prolog, targetFunction)
                || targetFunction.isRecursive()
                || targetFunction.getExpression().isSequential()
                || ((InlineFunctionExpression) targetFunction.getExpression()).hasExitStatement()
                || expression.getArguments().stream().anyMatch(arg -> arg != null && mayBeUpdating(arg))) {
            List<Expression> arguments = new ArrayList<>();
            for (Expression arg : expression.getArguments()) {
                arguments.add(arg == null ? null : (Expression) visit(arg, argument));
            }
            FunctionCallExpression result =
                    new FunctionCallExpression(expression.getFunctionName(), arguments, expression.getMetadata());
            result.setStaticSequenceType(expression.getStaticSequenceType());
            return result;
        }
        InlineFunctionExpression inlineFunction = (InlineFunctionExpression) targetFunction.getExpression();
        StatementsAndOptionalExpr body = (StatementsAndOptionalExpr) visit(inlineFunction.getBody(), argument);
        List<Name> paramNames = new ArrayList<>(inlineFunction.getParams().keySet());
        if (expression.getArguments().isEmpty() || allArgumentsMatch(expression, paramNames)) {
            if (inlineFunction.getReturnType() != null) {
                FunctionArgumentConversionExpression result =
                        convertReturnValue(body, inlineFunction.getReturnType(), expression, inlineFunction);
                result.setSequential(inlineFunction.isSequential());
                return result;
            }
            return body;
        }
        body.setStaticSequenceType(inlineFunction.getReturnType());
        // The return value is converted inside the FLWOR expression, which binds the arguments and is evaluated once,
        // so that conversion errors are reported at the body, not at the call.
        Expression returnValue = inlineFunction.getReturnType() == null
                ? body
                : convertReturnValue(body, inlineFunction.getReturnType(), expression, inlineFunction);
        ReturnClause returnClause = new ReturnClause(returnValue, expression.getMetadata());
        Clause expressionClauses = null;
        Clause assignmentClauses = null;
        for (int i = 0; i < expression.getArguments().size(); i++) {
            Name paramName = paramNames.get(i);
            SequenceType paramType = inlineFunction.getParams().get(paramName);
            Expression argumentExpression =
                    (Expression) visit(expression.getArguments().get(i), argument);
            // only use assignment clause when the variables have different names
            if (argumentExpression instanceof VariableReferenceExpression variableReference
                    && variableReference.getVariableName().equals(paramName)) {
                continue;
            }

            // if there is a name collision, use a temporary variable
            if (isVariableReferenced(expression.getArguments(), paramName, i)) {
                Name columnName = Name.createVariableInNoNamespace(
                        String.format("param%s", UUID.randomUUID().toString().replaceAll("-", "")));
                Clause expressionClause = new LetClause(columnName, null, argumentExpression, expression.getMetadata());
                Expression assignmentExpression = convertArgument(
                        new VariableReferenceExpression(columnName, expression.getMetadata()), paramType, expression);
                Clause assignmentClause =
                        new LetClause(paramName, null, assignmentExpression, expression.getMetadata());
                if (assignmentClauses != null) {
                    assignmentClause.chainWith(assignmentClauses);
                }
                assignmentClauses = assignmentClause;
                if (expressionClauses != null) {
                    expressionClause.chainWith(expressionClauses);
                }
                expressionClauses = expressionClause;
            } else {
                Expression assignmentExpression = convertArgument(argumentExpression, paramType, expression);
                Clause expressionClause =
                        new LetClause(paramName, null, assignmentExpression, expression.getMetadata());
                if (expressionClauses != null) {
                    expressionClause.chainWith(expressionClauses);
                }
                expressionClauses = expressionClause;
            }
        }
        if (expressionClauses == null) {
            if (inlineFunction.getReturnType() != null) {
                FunctionArgumentConversionExpression result =
                        convertReturnValue(body, inlineFunction.getReturnType(), expression, inlineFunction);
                result.setSequential(inlineFunction.isSequential());
                return result;
            }
            return body;
        }
        if (assignmentClauses != null) {
            assignmentClauses.getLastClause().chainWith(returnClause);
            expressionClauses.getLastClause().chainWith(assignmentClauses);
        } else {
            expressionClauses.getLastClause().chainWith(returnClause);
        }
        return new FlworExpression(returnClause, expression.getMetadata());
    }
}
