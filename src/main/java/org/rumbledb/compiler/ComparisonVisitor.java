package org.rumbledb.compiler;

import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
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
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.TypeDeclaration;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.*;
import org.rumbledb.expressions.typing.*;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

import java.util.*;
import java.util.stream.Collectors;

public class ComparisonVisitor extends AbstractNodeVisitor<Node> {
    @Override
    protected Node defaultAction(Node node, Node argument) {
        return node;
    }

    @Override
    public Node visitProlog(Prolog expression, Node argument) {
        List<Node> declarations = expression.getFunctionDeclarations()
            .stream()
            .map(expr -> visit(expr, argument))
            .collect(Collectors.toList());
        declarations.addAll(expression.getVariableDeclarations());
        declarations.addAll(expression.getTypeDeclarations());
        expression.setDeclarations(declarations);
        return expression;
    }

    @Override
    public Node visitCommaExpression(CommaExpression expression, Node argument) {
        List<Expression> children = new ArrayList<>();
        for (Expression child : expression.getExpressions()) {
            children.add((Expression) visit(child, argument));
        }
        CommaExpression result = new CommaExpression(children, expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
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
        Expression resultingExpression = new FlworExpression((ReturnClause) result, expression.getMetadata());
        resultingExpression.setStaticContext(expression.getStaticContext());
        resultingExpression.setStaticSequenceType(expression.getStaticSequenceType());
        return resultingExpression;
    }

    public Node visitVariableReference(VariableReferenceExpression expression, Node argument) {
        VariableReferenceExpression result = new VariableReferenceExpression(
                expression.getVariableName(),
                expression.getMetadata()
        );
        result.setActualType(expression.getActualType());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    @Override
    public Node visitForClause(ForClause clause, Node argument) {
        Clause result = new ForClause(
                clause.getVariableName(),
                clause.isAllowEmpty(),
                clause.getActualSequenceType(),
                clause.getPositionalVariableName(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitLetClause(LetClause clause, Node argument) {
        LetClause result = new LetClause(
                clause.getVariableName(),
                clause.getActualSequenceType(),
                (Expression) visit(clause.getExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticType(clause.getStaticType());
        result.setStaticContext(clause.getStaticContext());
        return result;
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
        Clause result = new GroupByClause(groupByVariableDeclarations, clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
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
        Clause result = new OrderByClause(groupByVariableDeclarations, clause.isStable(), clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitCountClause(CountClause expression, Node argument) {
        Clause result = new CountClause(
                (VariableReferenceExpression) visit(expression.getCountVariable(), argument),
                expression.getMetadata()
        );
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitWhereClause(WhereClause clause, Node argument) {
        Clause result = new WhereClause(
                (Expression) visit(clause.getWhereExpression(), argument),
                clause.getMetadata()
        );
        result.setStaticContext(clause.getStaticContext());
        return result;
    }

    @Override
    public Node visitReturnClause(ReturnClause clause, Node argument) {
        Clause result = new ReturnClause((Expression) visit(clause.getReturnExpr(), argument), clause.getMetadata());
        result.setStaticContext(clause.getStaticContext());
        return result;
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitObjectConstructor(ObjectConstructorExpression expression, Node argument) {
        if (expression.isMergedConstructor()) {
            Expression result = new ObjectConstructorExpression(
                    (Expression) visit(expression.getChildren().get(0), argument),
                    expression.getMetadata()
            );
            result.setStaticContext(expression.getStaticContext());
            return result;
        } else {
            List<Expression> keys = expression.getKeys()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            List<Expression> values = expression.getValues()
                .stream()
                .map(key -> (Expression) visit(key, argument))
                .collect(Collectors.toList());
            Expression result = new ObjectConstructorExpression(keys, values, expression.getMetadata());
            result.setStaticContext(expression.getStaticContext());
            result.setStaticSequenceType(expression.getStaticSequenceType());
            return result;
        }
    }

    @Override
    public Node visitContextExpr(ContextItemExpression expression, Node argument) {
        Expression result = new ContextItemExpression(expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    public Node visitNamedFunctionRef(NamedFunctionReferenceExpression expression, Node argument) {
        Expression result = new NamedFunctionReferenceExpression(expression.getIdentifier(), expression.getMetadata());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
        return result;
    }
    // endregion

    // region literal
    public Node visitInteger(IntegerLiteralExpression expression, Node argument) {
        Expression result = new IntegerLiteralExpression(expression.getLexicalValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitString(StringLiteralExpression expression, Node argument) {
        Expression result = new StringLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitDouble(DoubleLiteralExpression expression, Node argument) {
        Expression result = new DoubleLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitDecimal(DecimalLiteralExpression expression, Node argument) {
        Expression result = new DecimalLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitNull(NullLiteralExpression expression, Node argument) {
        Expression result = new NullLiteralExpression(expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }

    public Node visitBoolean(BooleanLiteralExpression expression, Node argument) {
        Expression result = new BooleanLiteralExpression(expression.getValue(), expression.getMetadata());
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitNotExpr(NotExpression expression, Node argument) {
        NotExpression result = new NotExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getMetadata()
        );
        result.setStaticSequenceType(expression.getStaticSequenceType());
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    @Override
    public Node visitComparisonExpr(ComparisonExpression expression, Node argument) {
        Expression leftChild = (Expression) visit(expression.getChildren().get(0), argument);
        Expression rightChild = (Expression) visit(expression.getChildren().get(1), argument);

        // if it's already value comparison, return it
        if (expression.getComparisonOperator().isValueComparison()) {
            ComparisonExpression result = new ComparisonExpression(
                    leftChild,
                    rightChild,
                    expression.getComparisonOperator(),
                    expression.getMetadata()
            );
            result.setStaticSequenceType(expression.getStaticSequenceType());
            result.setStaticContext(expression.getStaticContext());
            return result;
        }
        ComparisonExpression.ComparisonOperator comparisonOperator = ComparisonExpression.ComparisonOperator
            .getValueComparisonFromComparison(
                expression.getComparisonOperator()
            );
        // if left and right have arity one, use value comparison
        if (
            leftChild.getStaticSequenceType().getArity() == SequenceType.Arity.One
                && rightChild.getStaticSequenceType().getArity() == SequenceType.Arity.One
        ) {
            ComparisonExpression result = new ComparisonExpression(
                    leftChild,
                    rightChild,
                    comparisonOperator,
                    expression.getMetadata()
            );
            result.setStaticSequenceType(expression.getStaticSequenceType());
            result.setStaticContext(expression.getStaticContext());
            return result;
        }
        // if left or right are a sequence, use FLWOR
        if (
            SequenceType.Arity.OneOrMore.isSubtypeOf(leftChild.getStaticSequenceType().getArity())
                || SequenceType.Arity.OneOrMore.isSubtypeOf(rightChild.getStaticSequenceType().getArity())
        ) {
            Name variableNameLeft = Name.TEMP_VAR1;
            Name variableNameRight = Name.TEMP_VAR2;

            StaticContext leftContext = new StaticContext(expression.getStaticContext());
            leftContext.addVariable(variableNameLeft, leftChild.getStaticSequenceType(), expression.getMetadata());
            Clause firstClause = new ForClause(
                    variableNameLeft,
                    false,
                    leftChild.getStaticSequenceType(),
                    null,
                    leftChild,
                    expression.getMetadata()
            );
            firstClause.setStaticContext(leftContext);
            StaticContext rightContext = new StaticContext(leftContext);
            rightContext.addVariable(variableNameRight, rightChild.getStaticSequenceType(), expression.getMetadata());
            Clause secondClause = new ForClause(
                    variableNameRight,
                    false,
                    rightChild.getStaticSequenceType(),
                    null,
                    rightChild,
                    expression.getMetadata()
            );
            secondClause.setStaticContext(rightContext);
            firstClause.chainWith(secondClause);
            Expression leftReference = new VariableReferenceExpression(variableNameLeft, expression.getMetadata());
            leftReference.setStaticSequenceType(
                new SequenceType(leftChild.getStaticSequenceType().getItemType(), SequenceType.Arity.One)
            );
            leftReference.setStaticContext(rightContext);
            Expression rightReference = new VariableReferenceExpression(variableNameRight, expression.getMetadata());
            rightReference.setStaticSequenceType(
                new SequenceType(rightChild.getStaticSequenceType().getItemType(), SequenceType.Arity.One)
            );
            rightReference.setStaticContext(rightContext);
            Expression valueComparison = new ComparisonExpression(
                    leftReference,
                    rightReference,
                    comparisonOperator,
                    expression.getMetadata()
            );
            valueComparison.setStaticContext(rightContext);
            valueComparison.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One)
            );
            WhereClause whereClause = new WhereClause(valueComparison, expression.getMetadata());
            whereClause.setStaticContext(rightContext);
            secondClause.chainWith(whereClause);
            Expression stringLiteralExpression = new StringLiteralExpression("", null);
            stringLiteralExpression.setStaticContext(rightContext);
            stringLiteralExpression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.One)
            );
            ReturnClause returnClause = new ReturnClause(
                    stringLiteralExpression,
                    expression.getMetadata()
            );
            returnClause.setStaticContext(rightContext);
            whereClause.chainWith(returnClause);
            Expression flworExpression = new FlworExpression(returnClause, expression.getMetadata());
            flworExpression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.ZeroOrMore)
            );
            flworExpression.setStaticContext(expression.getStaticContext());
            FunctionCallExpression functionCallExpression = new FunctionCallExpression(
                    Name.createVariableInDefaultFunctionNamespace("exists"),
                    Collections.singletonList(flworExpression),
                    expression.getMetadata()
            );
            functionCallExpression.setStaticSequenceType(
                new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One)
            );
            functionCallExpression.setStaticContext(expression.getStaticContext());
            return functionCallExpression;
        }
        // otherwise, use ([left op right, false][[1]])
        ComparisonExpression comparisonExpression = new ComparisonExpression(
                (Expression) visit(expression.getChildren().get(0), argument),
                (Expression) visit(expression.getChildren().get(1), argument),
                comparisonOperator,
                expression.getMetadata()
        );
        comparisonExpression.setStaticSequenceType(expression.getStaticSequenceType());
        comparisonExpression.setStaticContext(expression.getStaticContext());
        BooleanLiteralExpression booleanExpression = new BooleanLiteralExpression(false, expression.getMetadata());
        booleanExpression.setStaticSequenceType(
            new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One)
        );
        booleanExpression.setStaticContext(expression.getStaticContext());

        List<Expression> commaExpressions = new ArrayList<>();
        commaExpressions.add(comparisonExpression);
        commaExpressions.add(booleanExpression);
        CommaExpression commaExpression = new CommaExpression(commaExpressions, expression.getMetadata());
        commaExpression.setStaticSequenceType(
            new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.OneOrMore)
        );
        commaExpression.setStaticContext(expression.getStaticContext());

        ArrayConstructorExpression arrayConstructorExpression = new ArrayConstructorExpression(
                commaExpression,
                expression.getMetadata()
        );
        arrayConstructorExpression.setStaticContext(expression.getStaticContext());
        arrayConstructorExpression.setStaticSequenceType(
            new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.One)
        );

        Expression integerLiteralExpression = new IntegerLiteralExpression("1", expression.getMetadata());
        integerLiteralExpression.setStaticSequenceType(SequenceType.INTEGER);
        integerLiteralExpression.setStaticContext(expression.getStaticContext());
        ArrayLookupExpression result = new ArrayLookupExpression(
                arrayConstructorExpression,
                integerLiteralExpression,
                expression.getMetadata()
        );
        result.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One));
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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
        result.setStaticContext(expression.getStaticContext());
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

    public Node visitTypeDeclaration(TypeDeclaration expression, Node argument) {
        return new TypeDeclaration(expression.getDefinition(), expression.getMetadata());
    }

    @Override
    public Node visitValidateTypeExpression(ValidateTypeExpression expression, Node argument) {
        Expression result = new ValidateTypeExpression(
                (Expression) visit(expression.getMainExpression(), argument),
                expression.getSequenceType(),
                expression.getMetadata()
        );
        result.setStaticContext(expression.getStaticContext());
        result.setStaticSequenceType(expression.getStaticSequenceType());
        return result;
    }
}
