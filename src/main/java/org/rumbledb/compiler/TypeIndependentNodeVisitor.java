package org.rumbledb.compiler;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.UnaryExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.control.*;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.*;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.*;
import org.rumbledb.expressions.typing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TypeIndependentNodeVisitor extends AbstractNodeVisitor<Node> {
    @Override
    protected Node defaultAction(Node node, Node argument) {
        return node;
    }

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        MainModule result = new MainModule(
                (Prolog) visit(mainModule.getProlog(), argument),
                (Expression) visit(mainModule.getExpression(), mainModule.getProlog()),
                mainModule.getMetadata()
        );
        result.setStaticContext(mainModule.getStaticContext());
        return result;
    }

    @Override
    public Node visitCommaExpression(CommaExpression expression, Node argument) {
        List<Expression> children = new ArrayList<>();
        for (Expression child : expression.getExpressions()) {
            children.add((Expression) visit(child, argument));
        }
        CommaExpression result = new CommaExpression(children, expression.getMetadata());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    // region flwor
    @Override
    public Node visitFlowrExpression(FlworExpression expression, Node argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        Clause result = null;
        while (clause != null) {
            Clause temp = (Clause) this.visit(clause, argument);
            if (result != null) {
                result.chainWith(temp);
            }
            result = temp;
            clause = clause.getNextClause();
        }
        return new FlworExpression((ReturnClause) result, expression.getMetadata());
    }

    public Node visitVariableReference(VariableReferenceExpression expression, Node argument) {
        VariableReferenceExpression result = new VariableReferenceExpression(
                expression.getVariableName(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitForClause(ForClause clause, Node argument) {
        return new ForClause(
                clause.getVariableName(),
                clause.isAllowEmpty(),
                clause.getActualSequenceType(),
                clause.getPositionalVariableName(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
    }

    @Override
    public Node visitLetClause(LetClause clause, Node argument) {
        return new LetClause(
                clause.getVariableName(),
                clause.getActualSequenceType(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
    }

    @Override
    public Node visitGroupByClause(GroupByClause clause, Node argument) {
        List<GroupByVariableDeclaration> groupByVariableDeclarations = new ArrayList<>();
        for (GroupByVariableDeclaration variable : clause.getGroupVariables()) {
            groupByVariableDeclarations.add(
                new GroupByVariableDeclaration(
                        variable.getVariableName(),
                        variable.getActualSequenceType(),
                        (variable.getExpression() == null)
                            ? variable.getExpression()
                            : (Expression) visit(variable.getExpression(), argument)
                )
            );
        }
        return new GroupByClause(groupByVariableDeclarations, clause.getMetadata());
    }

    @Override
    public Node visitOrderByClause(OrderByClause clause, Node argument) {
        List<OrderByClauseSortingKey> groupByVariableDeclarations = new ArrayList<>();
        for (OrderByClauseSortingKey orderByClauseSortingKey : clause.getSortingKeys()) {
            groupByVariableDeclarations.add(
                new OrderByClauseSortingKey(
                        (Expression) visit(orderByClauseSortingKey.getExpression(), argument),
                        orderByClauseSortingKey.isAscending(),
                        orderByClauseSortingKey.getUri(),
                        orderByClauseSortingKey.getEmptyOrder()
                )
            );

        }
        return new OrderByClause(groupByVariableDeclarations, clause.isStable(), clause.getMetadata());
    }

    @Override
    public Node visitCountClause(CountClause expression, Node argument) {
        return new CountClause(
                (VariableReferenceExpression) visit(expression.getCountVariable(), argument),
                expression.getMetadata()
        );
    }

    @Override
    public Node visitWhereClause(WhereClause clause, Node argument) {
        return new WhereClause((Expression) visit(clause.getWhereExpression(), argument), clause.getMetadata());
    }

    @Override
    public Node visitReturnClause(ReturnClause clause, Node argument) {
        return new ReturnClause((Expression) visit(clause.getReturnExpr(), argument), clause.getMetadata());
    }
    // endregion

    // region postfix
    @Override
    public Node visitArrayUnboxingExpression(ArrayUnboxingExpression expression, Node argument) {
        ArrayUnboxingExpression result = new ArrayUnboxingExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitArrayLookupExpression(ArrayLookupExpression expression, Node argument) {
        ArrayLookupExpression result = new ArrayLookupExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getLookupExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitObjectLookupExpression(ObjectLookupExpression expression, Node argument) {
        ObjectLookupExpression result = new ObjectLookupExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getLookupExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitFilterExpression(FilterExpression expression, Node argument) {
        FilterExpression result = new FilterExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                (Expression) visit(expression.getPredicateExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitDynamicFunctionCallExpression(DynamicFunctionCallExpression expression, Node argument) {
        DynamicFunctionCallExpression result = new DynamicFunctionCallExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getArguments(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }
    // endregion

    // region primary
    public Node visitArrayConstructor(ArrayConstructorExpression expression, Node argument) {
        ArrayConstructorExpression result = new ArrayConstructorExpression(
                (expression.getExpression() == null)
                    ? expression.getExpression()
                    : (Expression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitObjectConstructor(ObjectConstructorExpression expression, Node argument) {
        if (expression.isMergedConstructor()) {
            return new ObjectConstructorExpression(
                    (Expression) visit(expression.getChildren().get(0), argument),
                    expression.getMetadata()
            );
        } else {
            List<Expression> keys = expression.getKeys()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            List<Expression> values = expression.getValues()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            return new ObjectConstructorExpression(keys, values, expression.getMetadata());
        }
    }

    @Override
    public Node visitContextExpr(ContextItemExpression expression, Node argument) {
        return new ContextItemExpression(expression.getMetadata());
    }

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        List<Expression> arguments = expression.getArguments()
            .stream()
            .map(expr -> expr != null ? (Expression) visit(expr, argument) : null)
            .collect(Collectors.toList());
        Expression result = new FunctionCallExpression(
                expression.getFunctionName(),
                arguments,
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitInlineFunctionExpr(InlineFunctionExpression expression, Node argument) {
        InlineFunctionExpression result = new InlineFunctionExpression(
                expression.getName(),
                expression.getParams(),
                expression.getReturnType(),
                (Expression) visit(expression.getBody(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitNamedFunctionRef(NamedFunctionReferenceExpression expression, Node argument) {
        return new NamedFunctionReferenceExpression(expression.getIdentifier(), expression.getMetadata());
    }
    // endregion

    // region literal
    public Node visitInteger(IntegerLiteralExpression expression, Node argument) {
        return new IntegerLiteralExpression(expression.getLexicalValue(), expression.getMetadata());
    }

    public Node visitString(StringLiteralExpression expression, Node argument) {
        return new StringLiteralExpression(expression.getValue(), expression.getMetadata());
    }

    public Node visitDouble(DoubleLiteralExpression expression, Node argument) {
        return new DoubleLiteralExpression(expression.getValue(), expression.getMetadata());
    }

    public Node visitDecimal(DecimalLiteralExpression expression, Node argument) {
        return new DecimalLiteralExpression(expression.getValue(), expression.getMetadata());
    }

    public Node visitNull(NullLiteralExpression expression, Node argument) {
        return new NullLiteralExpression(expression.getMetadata());
    }

    public Node visitBoolean(BooleanLiteralExpression expression, Node argument) {
        return new BooleanLiteralExpression(expression.getValue(), expression.getMetadata());
    }
    // endregion

    // region operational
    @Override
    public Node visitAdditiveExpr(AdditiveExpression expression, Node argument) {
        AdditiveExpression result = new AdditiveExpression(
                (Expression) visit(expression.getLeftExpression(), argument),
                (Expression) visit(expression.getRightExpression(), argument),
                expression.isMinus(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitMultiplicativeExpr(MultiplicativeExpression expression, Node argument) {
        MultiplicativeExpression result = new MultiplicativeExpression(
                (Expression) visit(expression.getLeftExpression(), argument),
                (Expression) visit(expression.getRightExpression(), argument),
                expression.getMultiplicativeOperator(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitSimpleMapExpr(SimpleMapExpression expression, Node argument) {
        SimpleMapExpression result = new SimpleMapExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitAndExpr(AndExpression expression, Node argument) {
        AndExpression result = new AndExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitOrExpr(OrExpression expression, Node argument) {
        OrExpression result = new OrExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitNotExpr(NotExpression expression, Node argument) {
        NotExpression result = new NotExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitUnaryExpr(UnaryExpression expression, Node argument) {
        UnaryExpression result = new UnaryExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.isNegated(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitRangeExpr(RangeExpression expression, Node argument) {
        RangeExpression result = new RangeExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitStringConcatExpr(StringConcatExpression expression, Node argument) {
        StringConcatExpression result = new StringConcatExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitComparisonExpr(ComparisonExpression expression, Node argument) {
        ComparisonExpression result = new ComparisonExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                expression.getComparisonOperator(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitInstanceOfExpression(InstanceOfExpression expression, Node argument) {
        InstanceOfExpression result = new InstanceOfExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitIsStaticallyExpr(IsStaticallyExpression expression, Node argument) {
        IsStaticallyExpression result = new IsStaticallyExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitTreatExpression(TreatExpression expression, Node argument) {
        TreatExpression result = new TreatExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getsequenceType(),
                expression.errorCodeThatShouldBeThrown(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitCastableExpression(CastableExpression expression, Node argument) {
        CastableExpression result = new CastableExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitCastExpression(CastExpression expression, Node argument) {
        CastExpression result = new CastExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }
    // endregion

    // region control
    @Override
    public Node visitConditionalExpression(ConditionalExpression expression, Node argument) {
        ConditionalExpression result = new ConditionalExpression(
                (Expression) visit(expression.getCondition(), argument),
                (Expression) visit(expression.getBranch(), argument),
                (Expression) visit(expression.getElseBranch(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitSwitchExpression(SwitchExpression expression, Node argument) {
        List<SwitchCase> resultCases = new ArrayList<>();
        for (SwitchCase switchCase : expression.getCases()) {
            List<Expression> children = new ArrayList<>();
            for (Expression child : switchCase.getConditionExpressions()) {
                children.add((Expression) visit(child, argument));
            }
            resultCases.add(new SwitchCase(children, (Expression) visit(switchCase.getReturnExpression(), argument)));
        }
        SwitchExpression result = new SwitchExpression(
                (Expression) visit(expression.getTestCondition(), argument),
                resultCases,
                (Expression) visit(expression.getDefaultExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitTypeSwitchExpression(TypeSwitchExpression expression, Node argument) {
        List<TypeswitchCase> resultCases = new ArrayList<>();
        for (TypeswitchCase switchCase : expression.getCases()) {
            if (switchCase.getUnion() == null) {
                resultCases.add(
                    new TypeswitchCase(
                            switchCase.getVariableName(),
                            (Expression) visit(switchCase.getReturnExpression(), argument)
                    )
                );
            } else {
                resultCases.add(
                    new TypeswitchCase(
                            switchCase.getVariableName(),
                            switchCase.getUnion(),
                            (Expression) visit(switchCase.getReturnExpression(), argument)
                    )
                );
            }
        }
        TypeswitchCase defaultCase = (expression.getDefaultCase().getUnion() == null)
            ? new TypeswitchCase(
                    expression.getDefaultCase().getVariableName(),
                    (Expression) visit(expression.getDefaultCase().getReturnExpression(), argument)
            )
            : new TypeswitchCase(
                    expression.getDefaultCase().getVariableName(),
                    expression.getDefaultCase().getUnion(),
                    (Expression) visit(expression.getDefaultCase().getReturnExpression(), argument)
            );

        TypeSwitchExpression result = new TypeSwitchExpression(
                (Expression) visit(expression.getTestCondition(), argument),
                resultCases,
                defaultCase,
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitTryCatchExpression(TryCatchExpression expression, Node argument) {
        Map<String, Expression> catchExpressions = new HashMap<>();
        for (String key : expression.getCatchExpressions().keySet()) {
            catchExpressions.put(key, (Expression) visit(expression.getCatchExpressions().get(key), argument));
        }
        TryCatchExpression result = new TryCatchExpression(
                (Expression) visit(expression.getTryExpression(), argument),
                catchExpressions,
                (expression.getExpressionCatchingAll() == null)
                    ? expression.getExpressionCatchingAll()
                    : (Expression) visit(expression.getExpressionCatchingAll(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }
    // endregion

    // region prolog
    @Override
    public Node visitVariableDeclaration(VariableDeclaration expression, Node argument) {
        return new VariableDeclaration(
                expression.getVariableName(),
                expression.external(),
                expression.getActualSequenceType(),
                (Expression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );
    }

    @Override
    public Node visitFunctionDeclaration(FunctionDeclaration expression, Node argument) {
        return new FunctionDeclaration(
                (InlineFunctionExpression) visit(expression.getExpression(), argument),
                expression.getMetadata()
        );
    }

    @Override
    public Node visitTypeDeclaration(TypeDeclaration expression, Node argument) {
        return new TypeDeclaration(expression.getDefinition(), expression.getMetadata());
    }

    @Override
    public Node visitValidateTypeExpression(ValidateTypeExpression expression, Node argument) {
        return new ValidateTypeExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
    }
}
