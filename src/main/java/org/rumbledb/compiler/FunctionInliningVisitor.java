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
import org.rumbledb.errorcodes.ErrorCode;
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
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

import static org.rumbledb.expressions.module.Prolog.getFunctionDeclarationFromProlog;

public class FunctionInliningVisitor extends CloneVisitor {

    private String queryLanguage;
    private Prolog prolog;
    private boolean constructionPreserve;

    private boolean hasDifferentConstructionMode(Prolog prolog, FunctionDeclaration target) {
        for (var module : prolog.getImportedModules()) {
            if (module.getProlog().getFunctionDeclarations().contains(target)) {
                return module.getStaticContext().isConstructionPreserve() != this.constructionPreserve;
            }
            if (hasDifferentConstructionMode(module.getProlog(), target)) {
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
        if (mainModule.getStaticContext() != null) {
            this.queryLanguage = mainModule.getStaticContext().getQueryLanguage();
            this.constructionPreserve = mainModule.getStaticContext().isConstructionPreserve();
        }
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

    // Inlining is disabled for functions that:
    // 1. Are sequential.
    // 2. Contain an exit statement.
    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        FunctionDeclaration targetFunction =
                getFunctionDeclarationFromProlog(this.prolog, expression.getFunctionIdentifier());
        if (expression.isPartialApplication()
                || targetFunction == null
                // Rebuilding an inlined body in the caller's context would change its constructor semantics.
                || hasDifferentConstructionMode(this.prolog, targetFunction)
                || targetFunction.isRecursive()
                || targetFunction.getExpression().isSequential()
                || ((InlineFunctionExpression) targetFunction.getExpression()).hasExitStatement()) {
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
                TreatExpression result = new TreatExpression(
                        body,
                        inlineFunction.getReturnType(),
                        ErrorCode.UnexpectedTypeErrorCode,
                        expression.getMetadata());
                result.setSequential(inlineFunction.isSequential());
                return result;
            }
            return body;
        }
        body.setStaticSequenceType(inlineFunction.getReturnType());
        ReturnClause returnClause = new ReturnClause(body, expression.getMetadata());
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
                TreatExpression result = new TreatExpression(
                        body,
                        inlineFunction.getReturnType(),
                        ErrorCode.UnexpectedTypeErrorCode,
                        expression.getMetadata());
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
        FlworExpression result = new FlworExpression(returnClause, expression.getMetadata());
        if (inlineFunction.getReturnType() != null) {
            return new TreatExpression(
                    result,
                    inlineFunction.getReturnType(),
                    ErrorCode.UnexpectedTypeErrorCode,
                    expression.getMetadata());
        }
        return result;
    }
}
