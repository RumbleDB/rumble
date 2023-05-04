package org.rumbledb.compiler;

import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class FunctionInliningVisitor extends CloneVisitor {

    public FunctionInliningVisitor() {
    }

    private FunctionDeclaration getFunctionDeclarationFromProlog(Prolog prolog, FunctionIdentifier functionIdentifier) {
        for (FunctionDeclaration declaration : prolog.getFunctionDeclarations()) {
            if (declaration.getFunctionIdentifier().equals(functionIdentifier)) {
                return declaration;
            }
        }
        for (LibraryModule module : prolog.getImportedModules()) {
            FunctionDeclaration result = getFunctionDeclarationFromProlog(module.getProlog(), functionIdentifier);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private boolean isVariableReferenced(Node expression, Name name) {
        if (expression instanceof VariableReferenceExpression) {
            return ((VariableReferenceExpression) expression).getVariableName().equals(name);
        }
        if (expression instanceof Clause) {
            if (
                ((Clause) expression).getPreviousClause() != null
                    && isVariableReferenced(((Clause) expression).getPreviousClause(), name)
            ) {
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
                && expression.getArguments().get(i) instanceof VariableReferenceExpression
                && paramNames.get(i)
                    .equals(((VariableReferenceExpression) expression.getArguments().get(i)).getVariableName());
        }
        return allArgumentsMatch;
    }

    private Expression createTypePromotion(
            Expression expression,
            SequenceType paramType
    ) {
        // integer > decimal > double
        if (paramType.getItemType() == BuiltinTypesCatalogue.doubleItem) {
            List<TypeswitchCase> cases = new ArrayList<>();
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("integer?")),
                        new CastExpression(
                                expression,
                                SequenceType.createSequenceType("double?"),
                                expression.getMetadata()
                        )
                )
            );
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("decimal?")),
                        new CastExpression(
                                expression,
                                SequenceType.createSequenceType("double?"),
                                expression.getMetadata()
                        )
                )
            );
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("double?")),
                        expression
                )
            );
            TypeSwitchExpression typeSwitchExpression = new TypeSwitchExpression(
                    expression,
                    cases,
                    new TypeswitchCase(
                            null,
                            new TreatExpression(
                                    expression,
                                    paramType,
                                    ErrorCode.UnexpectedTypeErrorCode,
                                    expression.getMetadata()
                            )
                    ),
                    expression.getMetadata()
            );
            typeSwitchExpression.setStaticSequenceType(paramType);
            return typeSwitchExpression;
        }
        if (paramType.getItemType() == BuiltinTypesCatalogue.decimalItem) {
            List<TypeswitchCase> cases = new ArrayList<>();
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("integer?")),
                        new CastExpression(
                                expression,
                                SequenceType.createSequenceType("decimal?"),
                                expression.getMetadata()
                        )
                )
            );
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("decimal?")),
                        expression
                )
            );
            TypeSwitchExpression typeSwitchExpression = new TypeSwitchExpression(
                    expression,
                    cases,
                    new TypeswitchCase(
                            null,
                            new TreatExpression(
                                    expression,
                                    paramType,
                                    ErrorCode.UnexpectedTypeErrorCode,
                                    expression.getMetadata()
                            )
                    ),
                    expression.getMetadata()
            );
            typeSwitchExpression.setStaticSequenceType(paramType);
            return typeSwitchExpression;
        }
        // anyURI > string
        if (paramType.getItemType() == BuiltinTypesCatalogue.stringItem) {
            List<TypeswitchCase> cases = new ArrayList<>();
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("anyURI?")),
                        new CastExpression(
                                expression,
                                SequenceType.createSequenceType("string?"),
                                expression.getMetadata()
                        )
                )
            );
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(SequenceType.createSequenceType("string?")),
                        expression
                )
            );
            TypeSwitchExpression typeSwitchExpression = new TypeSwitchExpression(
                    expression,
                    cases,
                    new TypeswitchCase(
                            null,
                            new TreatExpression(
                                    expression,
                                    paramType,
                                    ErrorCode.UnexpectedTypeErrorCode,
                                    expression.getMetadata()
                            )
                    ),
                    expression.getMetadata()
            );
            typeSwitchExpression.setStaticSequenceType(paramType);
            return typeSwitchExpression;
        }
        return expression;
    }

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        MainModule result = new MainModule(
                mainModule.getProlog(),
                (Expression) visit(mainModule.getExpression(), mainModule.getProlog()),
                mainModule.getMetadata()
        );
        result.setStaticContext(mainModule.getStaticContext());
        return result;
    }

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        FunctionDeclaration targetFunction = getFunctionDeclarationFromProlog(
            (Prolog) argument,
            expression.getFunctionIdentifier()
        );
        if (expression.isPartialApplication() || targetFunction == null || targetFunction.isRecursive()) {
            List<Expression> arguments = new ArrayList<>();
            for (Expression arg : expression.getArguments()) {
                arguments.add(arg == null ? null : (Expression) visit(arg, argument));
            }
            FunctionCallExpression result = new FunctionCallExpression(
                    expression.getFunctionName(),
                    arguments,
                    expression.getMetadata()
            );
            result.setStaticSequenceType(expression.getStaticSequenceType());
            return result;
        }
        InlineFunctionExpression inlineFunction = (InlineFunctionExpression) targetFunction.getExpression();
        Expression body = (Expression) visit(inlineFunction.getBody(), argument);
        List<Name> paramNames = new ArrayList<>(inlineFunction.getParams().keySet());
        if (expression.getArguments().size() == 0 || allArgumentsMatch(expression, paramNames)) {
            if (inlineFunction.getReturnType() != null) {
                return new TreatExpression(
                        body,
                        inlineFunction.getReturnType(),
                        ErrorCode.UnexpectedTypeErrorCode,
                        expression.getMetadata()
                );
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
            Expression argumentExpression = (Expression) visit(expression.getArguments().get(i), argument);
            // only use assignment clause when the variables have different names
            if (
                argumentExpression instanceof VariableReferenceExpression
                    && ((VariableReferenceExpression) argumentExpression).getVariableName().equals(paramName)
            ) {
                continue;
            }

            // if there is a name collision, use a temporary variable
            if (isVariableReferenced(expression.getArguments(), paramName, i)) {
                Name columnName = new Name(
                        "",
                        "",
                        String.format("param%s", UUID.randomUUID().toString().replaceAll("-", ""))
                );
                Clause expressionClause = new LetClause(
                        columnName,
                        null,
                        argumentExpression,
                        expression.getMetadata()
                );
                Expression assignmentExpression = createTypePromotion(
                    new VariableReferenceExpression(columnName, expression.getMetadata()),
                    paramType
                );
                Clause assignmentClause = new LetClause(
                        paramName,
                        null,
                        assignmentExpression,
                        expression.getMetadata()
                );
                if (assignmentClauses != null) {
                    assignmentClause.chainWith(assignmentClauses);
                }
                assignmentClauses = assignmentClause;
                if (expressionClauses != null) {
                    expressionClause.chainWith(expressionClauses);
                }
                expressionClauses = expressionClause;
            } else {
                Expression assignmentExpression = createTypePromotion(
                    argumentExpression,
                    paramType
                );
                Clause expressionClause = new LetClause(
                        paramName,
                        null,
                        assignmentExpression,
                        expression.getMetadata()
                );
                if (expressionClauses != null) {
                    expressionClause.chainWith(expressionClauses);
                }
                expressionClauses = expressionClause;
            }
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
                    expression.getMetadata()
            );
        }
        return result;
    }
}
