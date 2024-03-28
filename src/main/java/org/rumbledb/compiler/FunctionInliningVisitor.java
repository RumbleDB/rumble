package org.rumbledb.compiler;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
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
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.rumbledb.expressions.module.Prolog.getFunctionDeclarationFromProlog;


public class FunctionInliningVisitor extends CloneVisitor {

    public FunctionInliningVisitor() {
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

    private void addCastCase(
            List<TypeswitchCase> cases,
            Expression expression,
            SequenceType testType,
            SequenceType targetType
    ) {
        if (testType.equals(targetType)) {
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(testType),
                        expression
                )
            );
        } else if (testType.getArity().isSubtypeOf(Arity.OneOrZero)) {
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(testType),
                        new CastExpression(
                                expression,
                                targetType,
                                expression.getMetadata()
                        )
                )
            );
        } else {
            Name variableName = Name.createVariableInNoNamespace(
                String.format("param%s", UUID.randomUUID().toString().replaceAll("-", ""))
            );
            Clause forClause = new ForClause(
                    variableName,
                    false,
                    new SequenceType(testType.getItemType(), Arity.One),
                    null,
                    expression,
                    expression.getMetadata()
            );
            Expression castExpression = new CastExpression(
                    new VariableReferenceExpression(variableName, expression.getMetadata()),
                    new SequenceType(targetType.getItemType(), Arity.One),
                    expression.getMetadata()
            );
            ReturnClause returnClause = new ReturnClause(
                    castExpression,
                    expression.getMetadata()
            );
            forClause.chainWith(returnClause);
            cases.add(
                new TypeswitchCase(
                        null,
                        Collections.singletonList(testType),
                        new FlworExpression(returnClause, expression.getMetadata())
                )
            );
        }
    }

    private Expression createTypePromotion(
            Expression expression,
            SequenceType paramType
    ) {
        // integer > decimal > double
        if (paramType.getItemType() == BuiltinTypesCatalogue.doubleItem) {
            List<TypeswitchCase> cases = new ArrayList<>();
            switch (paramType.getArity()) {
                case One:
                    addCastCase(cases, expression, SequenceType.INTEGER, SequenceType.DOUBLE);
                    addCastCase(cases, expression, SequenceType.DECIMAL, SequenceType.DOUBLE);
                    addCastCase(cases, expression, SequenceType.DOUBLE, SequenceType.DOUBLE);
                    break;
                case OneOrZero:
                    addCastCase(cases, expression, SequenceType.INTEGER_QM, SequenceType.DOUBLE_QM);
                    addCastCase(cases, expression, SequenceType.DECIMAL_QM, SequenceType.DOUBLE_QM);
                    addCastCase(cases, expression, SequenceType.DOUBLE_QM, SequenceType.DOUBLE_QM);
                    break;
                case OneOrMore:
                    addCastCase(cases, expression, SequenceType.INTEGER_PLUS, SequenceType.DOUBLE_PLUS);
                    addCastCase(cases, expression, SequenceType.DECIMAL_PLUS, SequenceType.DOUBLE_PLUS);
                    addCastCase(cases, expression, SequenceType.DOUBLE_PLUS, SequenceType.DOUBLE_PLUS);
                    break;
                case ZeroOrMore:
                    addCastCase(cases, expression, SequenceType.INTEGER_STAR, SequenceType.DOUBLE_STAR);
                    addCastCase(cases, expression, SequenceType.DECIMAL_STAR, SequenceType.DOUBLE_STAR);
                    addCastCase(cases, expression, SequenceType.DOUBLE_STAR, SequenceType.DOUBLE_STAR);
                    break;
                case Zero:
            }
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
            switch (paramType.getArity()) {
                case One:
                    addCastCase(cases, expression, SequenceType.INTEGER, SequenceType.DECIMAL);
                    addCastCase(cases, expression, SequenceType.DECIMAL, SequenceType.DECIMAL);
                    break;
                case OneOrZero:
                    addCastCase(cases, expression, SequenceType.INTEGER_QM, SequenceType.DECIMAL_QM);
                    addCastCase(cases, expression, SequenceType.DECIMAL_QM, SequenceType.DECIMAL_QM);
                    break;
                case OneOrMore:
                    addCastCase(cases, expression, SequenceType.INTEGER_PLUS, SequenceType.DECIMAL_PLUS);
                    addCastCase(cases, expression, SequenceType.DECIMAL_PLUS, SequenceType.DECIMAL_PLUS);
                    break;
                case ZeroOrMore:
                    addCastCase(cases, expression, SequenceType.INTEGER_STAR, SequenceType.DECIMAL_STAR);
                    addCastCase(cases, expression, SequenceType.DECIMAL_STAR, SequenceType.DECIMAL_STAR);
                    break;
                case Zero:
            }
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
            switch (paramType.getArity()) {
                case One:
                    addCastCase(cases, expression, SequenceType.ANYURI, SequenceType.STRING);
                    addCastCase(cases, expression, SequenceType.STRING, SequenceType.STRING);
                    break;
                case OneOrZero:
                    addCastCase(cases, expression, SequenceType.ANYURI_QM, SequenceType.STRING_QM);
                    addCastCase(cases, expression, SequenceType.STRING_QM, SequenceType.STRING_QM);
                    break;
                case OneOrMore:
                    addCastCase(cases, expression, SequenceType.ANYURI_PLUS, SequenceType.STRING_PLUS);
                    addCastCase(cases, expression, SequenceType.STRING_PLUS, SequenceType.STRING_PLUS);
                    break;
                case ZeroOrMore:
                    addCastCase(cases, expression, SequenceType.ANYURI_STAR, SequenceType.STRING_STAR);
                    addCastCase(cases, expression, SequenceType.STRING_STAR, SequenceType.STRING_STAR);
                    break;
                case Zero:
            }
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
                (Program) visit(mainModule.getProgram(), mainModule.getProlog()),
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
        StatementsAndOptionalExpr body = (StatementsAndOptionalExpr) visit(inlineFunction.getBody(), argument);
        List<Name> paramNames = new ArrayList<>(inlineFunction.getParams().keySet());
        if (expression.getArguments().isEmpty() || allArgumentsMatch(expression, paramNames)) {
            if (inlineFunction.getReturnType() != null) {
                TreatExpression result = new TreatExpression(
                        body,
                        inlineFunction.getReturnType(),
                        ErrorCode.UnexpectedTypeErrorCode,
                        expression.getMetadata()
                );
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
                Name columnName = Name.createVariableInNoNamespace(
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
