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
import org.rumbledb.expressions.xml.SlashExpr;
import org.rumbledb.types.SequenceType;

import static org.rumbledb.expressions.module.Prolog.getFunctionDeclarationFromProlog;

public class FunctionInliningVisitor extends CloneVisitor {

    private Prolog prolog;

    private boolean inPath;

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

    private boolean isVariableReferencedAfter(List<Expression> expressions, Name name, int index) {
        for (int i = index + 1; i < expressions.size(); i++) {
            if (isVariableReferenced(expressions.get(i), name)) {
                return true;
            }
        }
        return false;
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

    // Calls in a path expression are not inlined: an inlined body would read the context item of the step,
    // while a function body has no context item.
    @Override
    public Node visitSlashExpr(SlashExpr expression, Node argument) {
        boolean wasInPath = this.inPath;
        this.inPath = true;
        Node result = super.visitSlashExpr(expression, argument);
        this.inPath = wasInPath;
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
    // Calls in a path expression are not inlined either.
    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        FunctionDeclaration targetFunction =
                getFunctionDeclarationFromProlog(this.prolog, expression.getFunctionIdentifier());
        if (this.inPath
                || expression.isPartialApplication()
                || targetFunction == null
                // Imported function bodies must retain their module's static settings.
                || isImportedFunction(this.prolog, targetFunction)
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
        if (expression.getArguments().isEmpty()) {
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
        // The arguments are bound and converted in order, like the arguments of a call. A parameter that a later
        // argument reads would hide the variable it reads, so its argument is bound to a temporary variable, which is
        // assigned to the parameter once every argument is bound.
        List<Clause> clauses = new ArrayList<>();
        List<Clause> assignmentClauses = new ArrayList<>();
        for (int i = 0; i < expression.getArguments().size(); i++) {
            Name paramName = paramNames.get(i);
            Expression argumentExpression =
                    (Expression) visit(expression.getArguments().get(i), argument);
            // Every argument is converted, even a variable with the name of the parameter.
            Expression convertedArgument = convertArgument(
                    argumentExpression, inlineFunction.getParams().get(paramName), expression);
            if (isVariableReferencedAfter(expression.getArguments(), paramName, i)) {
                Name temporaryName = Name.createVariableInNoNamespace(
                        String.format("param%s", UUID.randomUUID().toString().replaceAll("-", "")));
                clauses.add(new LetClause(temporaryName, null, convertedArgument, expression.getMetadata()));
                assignmentClauses.add(new LetClause(
                        paramName,
                        null,
                        new VariableReferenceExpression(temporaryName, expression.getMetadata()),
                        expression.getMetadata()));
            } else {
                clauses.add(new LetClause(paramName, null, convertedArgument, expression.getMetadata()));
            }
        }
        clauses.addAll(assignmentClauses);
        clauses.add(returnClause);
        for (int i = 0; i + 1 < clauses.size(); i++) {
            clauses.get(i).chainWith(clauses.get(i + 1));
        }
        return new FlworExpression(returnClause, expression.getMetadata());
    }
}
