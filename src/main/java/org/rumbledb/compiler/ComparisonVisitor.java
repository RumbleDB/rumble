package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.postfix.FilterExpression;
import org.rumbledb.expressions.primary.BooleanLiteralExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.ItemType;

public class ComparisonVisitor extends CloneVisitor {
    @Override
    public Node visitComparisonExpr(ComparisonExpression expression, Node argument) {
        Expression leftChild = (Expression) visit(expression.getChildren().get(0), argument);
        Expression rightChild = (Expression) visit(expression.getChildren().get(1), argument);

        // Normalize xs:untypedAtomic for general comparisons at the expression level by inserting
        // explicit cast expressions based on the other operand’s static type. This ensures that
        // ComparisonIterator can remain simple and only implement the value-comparison
        // “untyped as string” rule.
        if (!expression.getComparisonOperator().isValueComparison()) {
            Expression[] normalized = normalizeUntypedForGeneralComparison(
                leftChild,
                rightChild,
                expression.getStaticContext(),
                expression.getMetadata()
            );
            leftChild = normalized[0];
            rightChild = normalized[1];
        }

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
            // Preserve whether this comparison originated as a general comparison (=, !=, <, ...)
            // or a value comparison (eq, ne, lt, ...). This is used at runtime to distinguish
            // value vs general comparison semantics (e.g., for xs:untypedAtomic handling)
            // while still mapping to the same underlying value comparison operator.
            result.setOriginalComparisonOperator(expression.getComparisonOperator());
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
            ((ComparisonExpression) valueComparison)
                .setOriginalComparisonOperator(expression.getComparisonOperator());
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
        comparisonExpression.setOriginalComparisonOperator(expression.getComparisonOperator());
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

        Expression integerLiteralExpression = new IntegerLiteralExpression("1", expression.getMetadata());
        integerLiteralExpression.setStaticSequenceType(SequenceType.INTEGER);
        integerLiteralExpression.setStaticContext(expression.getStaticContext());
        FilterExpression result = new FilterExpression(
                commaExpression,
                integerLiteralExpression,
                expression.getMetadata()
        );
        result.setStaticSequenceType(new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One));
        result.setStaticContext(expression.getStaticContext());
        return result;
    }

    /**
     * Implements the XQuery/XPath 3.1 §3.7.2 rules for general comparisons involving xs:untypedAtomic
     * at the expression level using explicit cast expressions:
     * - Both operands untypedAtomic → both cast to xs:string.
     * - One operand untypedAtomic, other atomic T:
     *   - If T is numeric → cast untyped to xs:double.
     *   - If T is xs:yearMonthDuration or xs:dayTimeDuration → cast untyped to that duration type.
     *   - Otherwise → cast untyped to the primitive base type of T.
     *
     * The method is purely static-type driven; if static types are insufficiently precise or if
     * operands are not atomic, the original expressions are returned unchanged.
     */
    private Expression[] normalizeUntypedForGeneralComparison(
            Expression left,
            Expression right,
            StaticContext context,
            org.rumbledb.exceptions.ExceptionMetadata metadata
    ) {
        SequenceType leftType = left.getStaticSequenceType();
        SequenceType rightType = right.getStaticSequenceType();
        if (leftType == null || rightType == null) {
            return new Expression[] { left, right };
        }
        ItemType leftItemType = leftType.getItemType();
        ItemType rightItemType = rightType.getItemType();
        if (leftItemType == null || rightItemType == null) {
            return new Expression[] { left, right };
        }

        boolean leftIsUntyped = leftItemType.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);
        boolean rightIsUntyped = rightItemType.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);

        if (!leftIsUntyped && !rightIsUntyped) {
            return new Expression[] { left, right };
        }

        // Both operands statically untyped → cast both to xs:string
        if (leftIsUntyped && rightIsUntyped) {
            SequenceType stringType = new SequenceType(
                    BuiltinTypesCatalogue.stringItem,
                    SequenceType.Arity.OneOrZero
            );
            Expression castLeft = buildCastExpression(left, stringType, context, metadata);
            Expression castRight = buildCastExpression(right, stringType, context, metadata);
            return new Expression[] { castLeft, castRight };
        }

        // Exactly one operand is untypedAtomic: cast it based on the other operand's primitive type
        boolean untypedOnLeft = leftIsUntyped;
        Expression untypedExpr = untypedOnLeft ? left : right;
        SequenceType untypedSeqType = untypedOnLeft ? leftType : rightType;
        ItemType otherItemType = untypedOnLeft ? rightItemType : leftItemType;

        if (!otherItemType.isAtomicItemType()) {
            // If the other side is not atomic, do not attempt any rewrite here.
            return new Expression[] { left, right };
        }

        ItemType primitiveOther =
            otherItemType.isPrimitive()
                ? otherItemType
                : otherItemType.getPrimitiveType();

        ItemType castTargetItemType;
        if (primitiveOther.isNumeric()) {
            castTargetItemType = BuiltinTypesCatalogue.doubleItem;
        } else if (primitiveOther.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            castTargetItemType = BuiltinTypesCatalogue.yearMonthDurationItem;
        } else if (primitiveOther.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            castTargetItemType = BuiltinTypesCatalogue.dayTimeDurationItem;
        } else {
            castTargetItemType = primitiveOther;
        }

        SequenceType.Arity untypedArity = untypedSeqType.getArity();
        SequenceType castSequenceType = new SequenceType(
                castTargetItemType,
                untypedArity == SequenceType.Arity.One ? SequenceType.Arity.One : SequenceType.Arity.OneOrZero
        );
        Expression castUntyped = buildCastExpression(untypedExpr, castSequenceType, context, metadata);

        if (untypedOnLeft) {
            return new Expression[] { castUntyped, right };
        } else {
            return new Expression[] { left, castUntyped };
        }
    }

    private Expression buildCastExpression(
            Expression operand,
            SequenceType targetType,
            StaticContext context,
            org.rumbledb.exceptions.ExceptionMetadata metadata
    ) {
        CastExpression castExpression = new CastExpression(operand, targetType, metadata);
        castExpression.setStaticContext(context);
        castExpression.setStaticSequenceType(targetType);
        return castExpression;
    }
}
