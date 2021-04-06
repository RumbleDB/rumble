package org.rumbledb.compiler;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.*;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
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
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.postfix.*;
import org.rumbledb.expressions.primary.*;
import org.rumbledb.expressions.typing.*;
import org.rumbledb.types.*;
import org.rumbledb.types.AtomicItemType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This visitor infers a static SequenceType for each expression in the query
 */
public class InferTypeVisitor extends AbstractNodeVisitor<StaticContext> {

    @SuppressWarnings("unused")
    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    /**
     * Builds a new visitor.
     *
     * @param rumbleRuntimeConfiguration the configuration.
     */
    InferTypeVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    /**
     * Perform basic checks on a list of SequenceType, available checks are for null (OurBad exception) and inferred the
     * empty sequence (XPST0005)
     *
     * @param types list of sequence types to check
     * @param nodeName name of the node to use in the errors
     * @param nullCheck flag indicating to perform null check
     * @param inferredEmptyCheck flag indicating to perform empty sequence check
     */
    private void basicChecks(List<SequenceType> types, String nodeName, boolean nullCheck, boolean inferredEmptyCheck) {
        if (nullCheck) {
            for (SequenceType type : types) {
                if (type == null) {
                    throw new OurBadException("A child expression of a " + nodeName + " has no inferred type");
                }
            }
        }
        if (inferredEmptyCheck) {
            for (SequenceType type : types) {
                if (type.isEmptySequence()) {
                    throw new UnexpectedStaticTypeException(
                            "Inferred type for "
                                + nodeName
                                + " is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                            ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                    );
                }
            }
        }
    }

    /**
     * Perform basic checks on a SequenceType, available checks are for null (OurBad exception) and inferred the empty
     * sequence (XPST0005)
     *
     * @param type sequence types to check
     * @param nodeName name of the node to use in the errors
     * @param nullCheck flag indicating to perform null check
     * @param inferredEmptyCheck flag indicating to perform empty sequence check
     */
    private void basicChecks(SequenceType type, String nodeName, boolean nullCheck, boolean inferredEmptyCheck) {
        if (nullCheck) {
            if (type == null) {
                throw new OurBadException("A child expression of a " + nodeName + " has no inferred type");
            }
        }
        if (inferredEmptyCheck) {
            if (type.isEmptySequence()) {
                throw new UnexpectedStaticTypeException(
                        "Inferred type for "
                            + nodeName
                            + " is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            }
        }
    }

    @Override
    public StaticContext visitCommaExpression(CommaExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferredType = SequenceType.EMPTY_SEQUENCE;

        for (Expression childExpression : expression.getExpressions()) {
            SequenceType childExpressionInferredType = childExpression.getInferredSequenceType();

            // if a child expression has no inferred type throw an error
            if (childExpressionInferredType == null) {
                throw new UnexpectedStaticTypeException("A child expression of a CommaExpression has no inferred type");
            }

            // if the child expression is an EMPTY_SEQUENCE it does not affect the comma expression type
            if (!childExpressionInferredType.isEmptySequence()) {
                if (inferredType.isEmptySequence()) {
                    inferredType = childExpressionInferredType;
                } else {
                    ItemType resultingItemType = inferredType.getItemType()
                        .findCommonSuperType(childExpressionInferredType.getItemType());
                    SequenceType.Arity resultingArity =
                        ((inferredType.getArity() == SequenceType.Arity.OneOrZero
                            || inferredType.getArity() == SequenceType.Arity.ZeroOrMore)
                            &&
                            (childExpressionInferredType.getArity() == SequenceType.Arity.OneOrZero
                                || childExpressionInferredType.getArity() == SequenceType.Arity.ZeroOrMore))
                                    ? SequenceType.Arity.ZeroOrMore
                                    : SequenceType.Arity.OneOrMore;
                    inferredType = new SequenceType(resultingItemType, resultingArity);
                }
            }
        }

        System.out.println("visited comma expression with inferred type: " + inferredType);
        expression.setInferredSequenceType(inferredType);
        return argument;
    }

    // region primary

    @Override
    public StaticContext visitString(StringLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting String literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.stringItem));
        return argument;
    }

    @Override
    public StaticContext visitInteger(IntegerLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Int literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.integerItem));
        return argument;
    }

    @Override
    public StaticContext visitDouble(DoubleLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Double literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.doubleItem));
        return argument;
    }

    @Override
    public StaticContext visitDecimal(DecimalLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Decimal literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.decimalItem));
        return argument;
    }

    @Override
    public StaticContext visitNull(NullLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Null literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.nullItem));
        return argument;
    }

    @Override
    public StaticContext visitBoolean(BooleanLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Boolean literal");
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        SequenceType variableType = expression.getActualType();
        if (variableType == null) {
            // if is null, no 'as [SequenceType]' part was present in the declaration, therefore we infer it
            System.out.println("variable reference type was null so we infer it");
            variableType = expression.getStaticContext().getVariableSequenceType(expression.getVariableName());
            // we also set variableReference type
            expression.setType(variableType);
        }
        basicChecks(variableType, expression.getClass().getSimpleName(), false, true);
        System.out.println("visiting variable reference with type: " + variableType);
        expression.setInferredSequenceType(variableType);
        return argument;
    }

    @Override
    public StaticContext visitArrayConstructor(ArrayConstructorExpression expression, StaticContext argument) {
        System.out.println("visiting Array constructor literal");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.arrayItem));
        return argument;
    }

    @Override
    public StaticContext visitObjectConstructor(ObjectConstructorExpression expression, StaticContext argument) {
        System.out.println("visiting Object constructor literal");
        visitDescendants(expression, argument);
        if (expression.isMergedConstructor()) {
            // if it is a merged constructor the child must be a subtype of object* inferred type
            SequenceType childSequenceType = ((Expression) expression.getChildren().get(0)).getInferredSequenceType();
            if (childSequenceType == null) {
                throw new UnexpectedStaticTypeException("The child expression has no inferred type");
            }
            if (!childSequenceType.isSubtypeOf(SequenceType.createSequenceType("object*"))) {
                throw new UnexpectedStaticTypeException(
                        "The child expression must have object* sequence type, instead found: " + childSequenceType
                );
            }
        } else {
            for (Expression keyExpression : expression.getKeys()) {
                SequenceType keySequenceType = keyExpression.getInferredSequenceType();
                if (keySequenceType == null) {
                    throw new UnexpectedStaticTypeException(
                            "One of the key in the object constructor has no inferred type"
                    );
                }
                if (
                    !keySequenceType.isSubtypeOf(SequenceType.createSequenceType("string"))
                        && !keySequenceType.isSubtypeOf(SequenceType.createSequenceType("anyURI"))
                ) {
                    throw new UnexpectedStaticTypeException(
                            "The inferred static sequence types for the keys of an Object must be a subtype of string or anyURI, instead found a: "
                                + keySequenceType
                    );
                }
            }
        }
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.objectItem));
        return argument;
    }

    @Override
    public StaticContext visitContextExpr(ContextItemExpression expression, StaticContext argument) {
        SequenceType contextType = expression.getStaticContext().getContextItemStaticType();
        if (contextType == null) {
            contextType = new SequenceType(AtomicItemType.item);
        }
        expression.setInferredSequenceType(contextType);
        System.out.println("Visited context expression, set type: " + contextType);
        return argument;
    }

    @Override
    public StaticContext visitInlineFunctionExpr(InlineFunctionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType returnType = expression.getActualReturnType();
        if (returnType == null) {
            returnType = expression.getBody().getInferredSequenceType();
        }
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.functionItem));
        System.out.println("Visited inline function expression");
        return argument;
    }

    private FunctionSignature getSignature(FunctionIdentifier identifier, StaticContext staticContext) {
        BuiltinFunction function = null;
        FunctionSignature signature = null;
        try {
            function = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        } catch (OurBadException exception) {
            signature = staticContext.getFunctionSignature(identifier);
        }

        if (function != null) {
            signature = function.getSignature();
        }

        return signature;
    }

    @Override
    public StaticContext visitNamedFunctionRef(NamedFunctionReferenceExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        expression.setInferredSequenceType(new SequenceType(AtomicItemType.functionItem));
        System.out.println("Visited named function expression");
        return argument;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        FunctionSignature signature = getSignature(expression.getFunctionIdentifier(), expression.getStaticContext());

        List<Expression> parameterExpressions = expression.getArguments();
        List<SequenceType> parameterTypes = signature.getParameterTypes();
        List<SequenceType> partialParams = new ArrayList<>();
        int paramsLength = parameterExpressions.size();

        for (int i = 0; i < paramsLength; ++i) {
            if (parameterExpressions.get(i) != null) {
                SequenceType actualType = parameterExpressions.get(i).getInferredSequenceType();
                SequenceType expectedType = parameterTypes.get(i);
                // check actual parameters is either a subtype of or can be promoted to expected type
                if (!actualType.isSubtypeOfOrCanBePromotedTo(expectedType)) {
                    throw new UnexpectedStaticTypeException(
                            "Argument " + i + " requires " + expectedType + " but " + actualType + " was found"
                    );
                }
            } else {
                partialParams.add(parameterTypes.get(i));
            }
        }

        if (expression.isPartialApplication()) {
            expression.setInferredSequenceType(new SequenceType(AtomicItemType.functionItem));
        } else {
            SequenceType returnType = signature.getReturnType();
            if (returnType == null) {
                returnType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
            }
            expression.setInferredSequenceType(returnType);
        }

        System.out.println("Visited static function call, set type to " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region typing

    @Override
    public StaticContext visitCastableExpression(CastableExpression expression, StaticContext argument) {
        System.out.println("visiting Castable expression");
        visitDescendants(expression, argument);
        ItemType itemType = expression.getSequenceType().getItemType();
        if (itemType.equals(AtomicItemType.atomicItem)) {
            throw new UnexpectedStaticTypeException(
                    "atomic item type is not allowed in castable expression",
                    ErrorCode.CastableErrorCode
            );
        }
        SequenceType expressionType = expression.getMainExpression().getInferredSequenceType();
        basicChecks(expressionType, expression.getClass().getSimpleName(), true, false);
        if (
            !expressionType.isEmptySequence()
                && !expressionType.getItemType().isSubtypeOf(AtomicItemType.atomicItem)
        ) {
            throw new UnexpectedStaticTypeException(
                    "non-atomic item types are not allowed in castable expression, found "
                        + expressionType.getItemType(),
                    expressionType.getItemType().isSubtypeOf(AtomicItemType.JSONItem)
                        ? ErrorCode.NonAtomicElementErrorCode
                        : ErrorCode.AtomizationError
            );
        }
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitCastExpression(CastExpression expression, StaticContext argument) {
        System.out.println("visiting Cast expression");
        visitDescendants(expression, argument);

        // check at static time for casting errors (note cast only allows for normal or ? arity)
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType castedSequenceType = expression.getSequenceType();

        if (castedSequenceType.getItemType().equals(AtomicItemType.atomicItem)) {
            throw new UnexpectedStaticTypeException(
                    "atomic item type is not allowed in cast expression",
                    ErrorCode.CastableErrorCode
            );
        }

        // Empty sequence case
        if (expressionSequenceType.isEmptySequence()) {
            if (castedSequenceType.getArity() != SequenceType.Arity.OneOrZero) {
                throw new UnexpectedStaticTypeException(
                        "Empty sequence cannot be cast to type with quantifier different from '?'"
                );
            } else {
                // no additional check is needed
                expression.setInferredSequenceType(castedSequenceType);
                return argument;
            }
        }

        if (!expressionSequenceType.isAritySubtypeOf(castedSequenceType.getArity())) {
            throw new UnexpectedStaticTypeException(
                    "with static type feature it is not possible to cast a "
                        +
                        expressionSequenceType
                        + " as "
                        + castedSequenceType
            );
        }

        // ItemType static castability check
        if (!expressionSequenceType.getItemType().isSubtypeOf(AtomicItemType.atomicItem)) {
            throw new UnexpectedStaticTypeException(
                    "It is never possible to cast a non-atomic sequence type: "
                        +
                        expressionSequenceType,
                    expressionSequenceType.getItemType().isSubtypeOf(AtomicItemType.JSONItem)
                        ? ErrorCode.NonAtomicElementErrorCode
                        : ErrorCode.AtomizationError
            );
        }
        if (!expressionSequenceType.getItemType().isStaticallyCastableAs(castedSequenceType.getItemType())) {
            throw new UnexpectedStaticTypeException(
                    "It is never possible to cast a "
                        +
                        expressionSequenceType
                        + " as "
                        + castedSequenceType
            );
        }

        expression.setInferredSequenceType(castedSequenceType);
        return argument;
    }

    @Override
    public StaticContext visitIsStaticallyExpr(IsStaticallyExpression expression, StaticContext argument) {
        System.out.println("visiting StaticallyIs expression");
        visitDescendants(expression, argument);

        SequenceType inferred = expression.getMainExpression().getInferredSequenceType();
        SequenceType expected = expression.getSequenceType();
        if (!inferred.equals(expected)) {
            throw new IsStaticallyUnexpectedTypeException(
                    "expected static type is " + expected + " instead " + inferred + " was inferred"
            );
        }

        expression.setInferredSequenceType(expected);
        return argument;
    }

    @Override
    public StaticContext visitInstanceOfExpression(InstanceOfExpression expression, StaticContext argument) {
        System.out.println("visiting InstanceOf expression");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        System.out.println("visiting Treat expression");
        visitDescendants(expression, argument);

        // check at static time for treat errors
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType treatedSequenceType = expression.getSequenceType();

        if (expressionSequenceType == null || treatedSequenceType == null) {
            throw new UnexpectedStaticTypeException(
                    "The child expression of a Treat expression has no inferred type or it is being treated as null sequence type"
            );
        }

        expression.setInferredSequenceType(treatedSequenceType);
        return argument;
    }

    // endregion

    // region arithmetic

    @Override
    public StaticContext visitAdditiveExpr(AdditiveExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftInferredType = expression.getLeftExpression().getInferredSequenceType();
        SequenceType rightInferredType = expression.getRightExpression().getInferredSequenceType();

        basicChecks(
            Arrays.asList(leftInferredType, rightInferredType),
            expression.getClass().getSimpleName(),
            true,
            true
        );

        ItemType inferredType = null;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType.getArity(), rightInferredType.getArity());

        // arity check
        if (inferredArity == null) {
            throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for additive expressions");
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        // check item type combination
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                inferredType = resolveNumericType(leftItemType, rightItemType);
            }
        } else if (
            leftItemType.equals(AtomicItemType.dateItem)
                || leftItemType.equals(AtomicItemType.dateTimeItem)
        ) {
            if (
                rightItemType.equals(AtomicItemType.dayTimeDurationItem)
                    || rightItemType.equals(AtomicItemType.yearMonthDurationItem)
            ) {
                inferredType = leftItemType;
            } else if (expression.isMinus() && rightItemType.equals(leftItemType)) {
                inferredType = AtomicItemType.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(AtomicItemType.timeItem)) {
            if (rightItemType.equals(AtomicItemType.dayTimeDurationItem)) {
                inferredType = leftItemType;
            } else if (expression.isMinus() && rightItemType.equals(leftItemType)) {
                inferredType = AtomicItemType.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(AtomicItemType.dayTimeDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                inferredType = leftItemType;
            } else if (
                !expression.isMinus()
                    && (rightItemType.equals(AtomicItemType.dateTimeItem)
                        || rightItemType.equals(AtomicItemType.dateItem)
                        || rightItemType.equals(AtomicItemType.timeItem))
            ) {
                inferredType = rightItemType;
            }
        } else if (leftItemType.equals(AtomicItemType.yearMonthDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                inferredType = leftItemType;
            } else if (
                !expression.isMinus()
                    && (rightItemType.equals(AtomicItemType.dateTimeItem)
                        || rightItemType.equals(AtomicItemType.dateItem))
            ) {
                inferredType = rightItemType;
            }
        }

        if (inferredType == null) {
            if (inferredArity == SequenceType.Arity.OneOrZero) {
                // Only possible resulting type is empty sequence so throw error XPST0005
                throw new UnexpectedStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            } else {
                throw new UnexpectedStaticTypeException(
                        "The following types operation is not possible: "
                            + leftInferredType
                            + (expression.isMinus() ? " - " : " + ")
                            + rightInferredType
                );
            }
        }

        expression.setInferredSequenceType(new SequenceType(inferredType, inferredArity));
        System.out.println("visiting Additive expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }

    // This function assume 2 numeric ItemType
    private ItemType resolveNumericType(ItemType left, ItemType right) {
        if (left.equals(AtomicItemType.doubleItem) || right.equals(AtomicItemType.doubleItem)) {
            return AtomicItemType.doubleItem;
        } else if (left.equals(AtomicItemType.decimalItem) || right.equals(AtomicItemType.decimalItem)) {
            return AtomicItemType.decimalItem;
        } else {
            return AtomicItemType.integerItem;
        }
    }

    // For arithmetic operations, given 2 arities, return the resulting arity or null in case of invalid arity
    private SequenceType.Arity resolveArities(SequenceType.Arity left, SequenceType.Arity right) {
        if (
            left == null
                ||
                left == SequenceType.Arity.ZeroOrMore
                ||
                left == SequenceType.Arity.OneOrMore
                ||
                right == null
                ||
                right == SequenceType.Arity.ZeroOrMore
                ||
                right == SequenceType.Arity.OneOrMore
        )
            return null;
        return (left == SequenceType.Arity.OneOrZero || right == SequenceType.Arity.OneOrZero)
            ? SequenceType.Arity.OneOrZero
            : SequenceType.Arity.One;
    }

    @Override
    public StaticContext visitMultiplicativeExpr(MultiplicativeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType leftInferredType = expression.getLeftExpression().getInferredSequenceType();
        SequenceType rightInferredType = expression.getRightExpression().getInferredSequenceType();

        basicChecks(
            Arrays.asList(leftInferredType, rightInferredType),
            expression.getClass().getSimpleName(),
            true,
            true
        );

        ItemType inferredType = null;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType.getArity(), rightInferredType.getArity());

        if (inferredArity == null) {
            throw new UnexpectedStaticTypeException(
                    "'+' and '*' arities are not allowed for multiplicative expressions"
            );
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        // check resulting item for each operation
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                if (expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.IDIV) {
                    inferredType = AtomicItemType.integerItem;
                } else if (
                    expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.DIV
                ) {
                    inferredType = resolveNumericType(
                        AtomicItemType.decimalItem,
                        resolveNumericType(leftItemType, rightItemType)
                    );
                } else {
                    inferredType = resolveNumericType(leftItemType, rightItemType);
                }
            } else if (
                rightItemType.isSubtypeOf(AtomicItemType.durationItem)
                    && !rightItemType.equals(AtomicItemType.durationItem)
                    &&
                    expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.MUL
            ) {
                inferredType = rightItemType;
            }
        } else if (
            leftItemType.isSubtypeOf(AtomicItemType.durationItem)
                && !leftItemType.equals(AtomicItemType.durationItem)
        ) {
            if (
                rightItemType.isNumeric()
                    && (expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.MUL
                        ||
                        expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.DIV)
            ) {
                inferredType = leftItemType;
            } else if (rightItemType.equals(leftItemType)) {
                inferredType = AtomicItemType.decimalItem;
            }
        }

        if (inferredType == null) {
            if (inferredArity == SequenceType.Arity.OneOrZero) {
                // Only possible resulting type is empty sequence so throw error XPST0005
                throw new UnexpectedStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            } else {
                throw new UnexpectedStaticTypeException(
                        "The following types expression is not valid: "
                            + leftItemType
                            + " "
                            + expression.getMultiplicativeOperator()
                            + " "
                            + rightItemType
                );
            }
        }

        expression.setInferredSequenceType(new SequenceType(inferredType, inferredArity));
        System.out.println("visiting Multiplicative expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitUnaryExpr(UnaryExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType childInferredType = expression.getMainExpression().getInferredSequenceType();

        // if the child expression has null inferred type throw error
        if (childInferredType == null) {
            throw new UnexpectedStaticTypeException("The child expression of a UnaryExpression has no inferred type");
        }

        // if the child is the empty sequence just infer the empty sequence
        if (childInferredType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        if (
            childInferredType.getArity() == SequenceType.Arity.OneOrMore
                || childInferredType.getArity() == SequenceType.Arity.ZeroOrMore
        ) {
            throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for unary expressions");
        }

        // if inferred arity does not allow for empty sequence and static type is not an accepted one throw a static
        // error
        ItemType childItemType = childInferredType.getItemType();
        if (!childItemType.isNumeric()) {
            if (childInferredType.getArity() == SequenceType.Arity.OneOrZero) {
                throw new UnexpectedStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            } else {
                throw new UnexpectedStaticTypeException(
                        "It is not possible to have an Unary expression with the following type: " + childInferredType
                );
            }
        }

        expression.setInferredSequenceType(new SequenceType(childItemType, childInferredType.getArity()));
        System.out.println("visiting Unary expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region logic

    private StaticContext visitAndOrExpr(Expression expression, StaticContext argument, String expressionName) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();

        if (leftInferredType == null || rightInferredType == null) {
            throw new UnexpectedStaticTypeException(
                    "A child expression of a " + expressionName + "Expression has no inferred type"
            );
        }

        if (!leftInferredType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "left expression of a "
                        + expressionName
                        + "Expression has "
                        + leftInferredType
                        + " inferred type, which has no effective boolean value"
            );
        }

        if (!rightInferredType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "right expression of a "
                        + expressionName
                        + "Expression has "
                        + rightInferredType
                        + " inferred type, which has no effective boolean value"
            );
        }

        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitAndExpr(AndExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "And");
    }

    @Override
    public StaticContext visitOrExpr(OrExpression expression, StaticContext argument) {
        return visitAndOrExpr(expression, argument, "Or");
    }

    @Override
    public StaticContext visitNotExpr(NotExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType childInferredType = expression.getMainExpression().getInferredSequenceType();
        if (childInferredType == null) {
            throw new UnexpectedStaticTypeException("The child expression of NotExpression has no inferred type");
        }
        if (!childInferredType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "The child expression of NotExpression has "
                        + childInferredType
                        + " inferred type, which has no effective boolean value"
            );
        }

        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem));
        return argument;
    }

    // endregion

    // region comparison

    @Override
    public StaticContext visitComparisonExpr(ComparisonExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();
        SequenceType.Arity returnArity = SequenceType.Arity.One;

        if (leftInferredType == null || rightInferredType == null) {
            throw new UnexpectedStaticTypeException(
                    "A child expression of a ComparisonExpression has no inferred type"
            );
        }

        ComparisonExpression.ComparisonOperator operator = expression.getComparisonOperator();

        // for value comparison arities * and + are not allowed, also if one return the empty sequence for sure throw
        // XPST0005 error
        if (operator.isValueComparison()) {
            if (leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()) {
                throw new UnexpectedStaticTypeException(
                        "Inferred type is empty sequence and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            }
            returnArity = resolveArities(leftInferredType.getArity(), rightInferredType.getArity());
            if (returnArity == null) {
                throw new UnexpectedStaticTypeException(
                        "'+' and '*' arities are not allowed for this comparison operator: " + operator
                );
            }
        }

        // if any of the element is the empty sequence, we set its sequence type to the other, if both are we do not
        // need additional checks
        boolean isLeftEmpty = leftInferredType.isEmptySequence();
        boolean isRightEmpty = rightInferredType.isEmptySequence();
        if (!isLeftEmpty || !isRightEmpty) {

            ItemType leftItemType = isLeftEmpty ? rightInferredType.getItemType() : leftInferredType.getItemType();
            ItemType rightItemType = isRightEmpty ? leftInferredType.getItemType() : rightInferredType.getItemType();

            // Type must be a strict subtype of atomic
            if (
                leftItemType.isSubtypeOf(AtomicItemType.JSONItem)
                    || rightItemType.isSubtypeOf(AtomicItemType.JSONItem)
            ) {
                throw new UnexpectedStaticTypeException(
                        "It is not possible to compare with non-atomic types",
                        ErrorCode.NonAtomicElementErrorCode
                );
            }
            if (
                !leftItemType.isSubtypeOf(AtomicItemType.atomicItem)
                    || !rightItemType.isSubtypeOf(AtomicItemType.atomicItem)
                    || leftItemType.equals(AtomicItemType.atomicItem)
                    || rightItemType.equals(AtomicItemType.atomicItem)
            ) {
                throw new UnexpectedStaticTypeException("It is not possible to compare with non-atomic types");
            }

            // Type must match exactly or be both numeric or both promotable to string or both durations or one must be
            // null
            if (
                !leftItemType.equals(rightItemType)
                    &&
                    !(leftItemType.isNumeric() && rightItemType.isNumeric())
                    &&
                    !(leftItemType.isSubtypeOf(AtomicItemType.durationItem)
                        && rightItemType.isSubtypeOf(AtomicItemType.durationItem))
                    &&
                    !(leftItemType.canBePromotedTo(AtomicItemType.stringItem)
                        && rightItemType.canBePromotedTo(AtomicItemType.stringItem))
                    &&
                    !(leftItemType.equals(AtomicItemType.nullItem)
                        || rightItemType.equals(AtomicItemType.nullItem))
            ) {
                throw new UnexpectedStaticTypeException(
                        "It is not possible to compare these types: " + leftItemType + " and " + rightItemType
                );
            }

            // Inequality is not defined for hexBinary and base64binary or for duration of different types
            if (
                (operator != ComparisonExpression.ComparisonOperator.VC_EQ
                    &&
                    operator != ComparisonExpression.ComparisonOperator.VC_NE
                    &&
                    operator != ComparisonExpression.ComparisonOperator.GC_EQ
                    &&
                    operator != ComparisonExpression.ComparisonOperator.GC_NE)
                    && (leftItemType.equals(AtomicItemType.hexBinaryItem)
                        || leftItemType.equals(AtomicItemType.base64BinaryItem)
                        ||
                        leftItemType.equals(AtomicItemType.durationItem)
                        || rightItemType.equals(AtomicItemType.durationItem)
                        ||
                        ((leftItemType.equals(AtomicItemType.dayTimeDurationItem)
                            || leftItemType.equals(AtomicItemType.yearMonthDurationItem))
                            && !rightItemType.equals(leftItemType)))
            ) {
                throw new UnexpectedStaticTypeException(
                        "It is not possible to compare these types: "
                            + leftItemType
                            + " "
                            + operator
                            + " "
                            + rightItemType
                );
            }
        }

        expression.setInferredSequenceType(new SequenceType(AtomicItemType.booleanItem, returnArity));
        return argument;
    }

    // endregion

    // region control

    @Override
    public StaticContext visitConditionalExpression(ConditionalExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType ifType = expression.getCondition().getInferredSequenceType();
        SequenceType thenType = expression.getBranch().getInferredSequenceType();
        SequenceType elseType = expression.getElseBranch().getInferredSequenceType();

        if (ifType == null || thenType == null || elseType == null) {
            throw new OurBadException("A child expression of a ConditionalExpression has no inferred type");
        }

        if (!ifType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "The condition in the 'if' must have effective boolean value, found inferred type: "
                        + ifType
                        + " (which has not effective boolean value)"
            );
        }

        // if the if branch is false at static time (i.e. subtype of null?) we only use else branch
        SequenceType resultingType = ifType.isSubtypeOf(SequenceType.createSequenceType("null?"))
            ? elseType
            : thenType.leastCommonSupertypeWith(elseType);

        if (resultingType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        expression.setInferredSequenceType(resultingType);
        System.out.println("visiting Conditional expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // throw errors if [type] does not conform to switch test and cases requirements
    public void checkSwitchType(SequenceType type) {
        if (type == null) {
            throw new OurBadException("A child expression of a SwitchExpression has no inferred type");
        }
        if (type.isEmptySequence()) {
            return; // no further check is required
        }
        if (type.getArity() == SequenceType.Arity.OneOrMore || type.getArity() == SequenceType.Arity.ZeroOrMore) {
            throw new UnexpectedStaticTypeException(
                    "+ and * arities are not allowed for the expressions of switch test condition and cases"
            );
        }
        ItemType itemType = type.getItemType();
        if (itemType.isFunctionItem()) {
            throw new UnexpectedStaticTypeException(
                    "function item not allowed for the expressions of switch test condition and cases",
                    ErrorCode.UnexpectedFunctionItem
            );
        }
        if (itemType.isSubtypeOf(AtomicItemType.JSONItem)) {
            throw new UnexpectedStaticTypeException(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                        + itemType,
                    ErrorCode.NonAtomicElementErrorCode
            );
        }
        if (!itemType.isSubtypeOf(AtomicItemType.atomicItem)) {
            throw new UnexpectedStaticTypeException(
                    "switch test condition and cases expressions' item type must match atomic, instead inferred: "
                        + itemType
            );
        }
    }

    @Override
    public StaticContext visitSwitchExpression(SwitchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType testType = expression.getTestCondition().getInferredSequenceType();
        checkSwitchType(testType);

        SequenceType returnType = expression.getDefaultExpression().getInferredSequenceType();
        if (returnType == null) {
            throw new OurBadException("A child expression of a SwitchExpression has no inferred type");
        }

        for (SwitchCase switchCase : expression.getCases()) {
            boolean addToReturnType = false;
            for (Expression caseExpression : switchCase.getConditionExpressions()) {
                // test the case expression
                checkSwitchType(caseExpression.getInferredSequenceType());
                // if has overlap with the test condition will add the return type to the possible ones
                if (caseExpression.getInferredSequenceType().hasOverlapWith(testType)) {
                    addToReturnType = true;
                }
            }
            SequenceType caseReturnType = switchCase.getReturnExpression().getInferredSequenceType();
            if (caseReturnType == null) {
                throw new OurBadException("A child expression of a SwitchExpression has no inferred type");
            }
            if (addToReturnType) {
                returnType = returnType.leastCommonSupertypeWith(caseReturnType);
            }
        }

        expression.setInferredSequenceType(returnType);
        System.out.println("visiting Switch expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitTryCatchExpression(TryCatchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType inferredType = null;

        for (Node childNode : expression.getChildren()) {
            SequenceType childType = ((Expression) childNode).getInferredSequenceType();
            if (childType == null) {
                throw new OurBadException("A child expression of a TryCatchExpression has no inferred type");
            }

            if (inferredType == null) {
                inferredType = childType;
            } else {
                inferredType = inferredType.leastCommonSupertypeWith(childType);
            }
        }

        if (inferredType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }
        expression.setInferredSequenceType(inferredType);
        System.out.println("visiting TryCatch expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitTypeSwitchExpression(TypeSwitchExpression expression, StaticContext argument) {
        visit(expression.getTestCondition(), argument);
        SequenceType inferredType = null;

        SequenceType conditionType = expression.getTestCondition().getInferredSequenceType();
        basicChecks(conditionType, expression.getClass().getSimpleName(), true, false);

        for (TypeswitchCase typeswitchCase : expression.getCases()) {
            Name variableName = typeswitchCase.getVariableName();
            Expression returnExpression = typeswitchCase.getReturnExpression();
            // if we bind a variable we add the static type of it in the context of the return expression
            if (variableName != null) {
                SequenceType variableType = null;
                for (SequenceType st : typeswitchCase.getUnion()) {
                    variableType = variableType == null ? st : variableType.leastCommonSupertypeWith(st);
                }
                returnExpression.getStaticContext().replaceVariableSequenceType(variableName, variableType);
            }

            visit(returnExpression, argument);
            SequenceType caseType = returnExpression.getInferredSequenceType();
            basicChecks(caseType, expression.getClass().getSimpleName(), true, false);
            inferredType = inferredType == null ? caseType : inferredType.leastCommonSupertypeWith(caseType);
        }

        Name variableName = expression.getDefaultCase().getVariableName();
        Expression returnExpression = expression.getDefaultCase().getReturnExpression();
        // if we bind a variable in the default case, we infer testCondition type
        if (variableName != null) {
            returnExpression.getStaticContext().replaceVariableSequenceType(variableName, conditionType);
        }
        visit(returnExpression, argument);
        SequenceType defaultType = returnExpression.getInferredSequenceType();
        basicChecks(defaultType, expression.getClass().getSimpleName(), true, false);
        inferredType = inferredType.leastCommonSupertypeWith(defaultType);

        basicChecks(inferredType, expression.getClass().getSimpleName(), false, true);
        expression.setInferredSequenceType(inferredType);
        System.out.println("visiting TypeSwitch expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region miscellaneous

    @Override
    public StaticContext visitRangeExpr(RangeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = ((Expression) children.get(0)).getInferredSequenceType();
        SequenceType rightType = ((Expression) children.get(1)).getInferredSequenceType();

        if (leftType == null || rightType == null) {
            throw new OurBadException("A child expression of a RangeExpression has no inferred type");
        }

        if (leftType.isEmptySequence() || rightType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        SequenceType intOpt = new SequenceType(AtomicItemType.integerItem, SequenceType.Arity.OneOrZero);
        if (!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)) {
            throw new UnexpectedStaticTypeException(
                    "operands of the range expression must match type integer? instead found: "
                        + leftType
                        + " and "
                        + rightType
            );
        }

        expression.setInferredSequenceType(
            new SequenceType(AtomicItemType.integerItem, SequenceType.Arity.ZeroOrMore)
        );
        System.out.println("visiting Range expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitStringConcatExpr(StringConcatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = ((Expression) children.get(0)).getInferredSequenceType();
        SequenceType rightType = ((Expression) children.get(1)).getInferredSequenceType();

        if (leftType == null || rightType == null) {
            throw new OurBadException("A child expression of a ConcatExpression has no inferred type");
        }

        SequenceType intOpt = new SequenceType(AtomicItemType.atomicItem, SequenceType.Arity.OneOrZero);
        if (!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)) {
            throw new UnexpectedStaticTypeException(
                    "operands of the concat expression must match type atomic? instead found: "
                        + leftType
                        + " and "
                        + rightType
            );
        }

        expression.setInferredSequenceType(new SequenceType(AtomicItemType.stringItem));
        System.out.println("visiting Concat expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region postfix

    @Override
    public StaticContext visitArrayLookupExpression(ArrayLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getInferredSequenceType();
        SequenceType lookupType = expression.getLookupExpression().getInferredSequenceType();

        if (mainType == null || lookupType == null) {
            throw new OurBadException("A child expression of a ArrayLookupExpression has no inferred type");
        }

        if (!lookupType.isSubtypeOf(SequenceType.createSequenceType("integer"))) {
            throw new UnexpectedStaticTypeException(
                    "the lookup expression type must match integer, instead " + lookupType + " was inferred"
            );
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        SequenceType.Arity inferredArity = mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
            ? SequenceType.Arity.OneOrZero
            : SequenceType.Arity.ZeroOrMore;
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.item, inferredArity));
        System.out.println("visiting ArrayLookup expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitObjectLookupExpression(ObjectLookupExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getInferredSequenceType();
        SequenceType lookupType = expression.getLookupExpression().getInferredSequenceType();

        if (mainType == null || lookupType == null) {
            throw new OurBadException("A child expression of a ObjectLookupExpression has no inferred type");
        }

        // must be castable to string
        if (!lookupType.isSubtypeOf(SequenceType.createSequenceType("atomic"))) {
            throw new UnexpectedStaticTypeException(
                    "the lookup expression type must be castable to string (i.e. must match atomic), instead "
                        + lookupType
                        + " was inferred"
            );
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("object*")) || mainType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        SequenceType.Arity inferredArity = mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
            ? SequenceType.Arity.OneOrZero
            : SequenceType.Arity.ZeroOrMore;
        expression.setInferredSequenceType(new SequenceType(AtomicItemType.item, inferredArity));
        System.out.println("visiting ObjectLookup expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitArrayUnboxingExpression(ArrayUnboxingExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getInferredSequenceType();

        if (mainType == null) {
            throw new OurBadException("A child expression of a ArrayUnboxingExpression has no inferred type");
        }

        if (!mainType.hasOverlapWith(SequenceType.createSequenceType("array*")) || mainType.isEmptySequence()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }

        expression.setInferredSequenceType(SequenceType.createSequenceType("item*"));
        System.out.println(
            "visiting ArrayUnboxingExpression expression, type set to: " + expression.getInferredSequenceType()
        );
        return argument;
    }

    @Override
    public StaticContext visitFilterExpression(FilterExpression expression, StaticContext argument) {
        visit(expression.getMainExpression(), argument);
        SequenceType mainType = expression.getMainExpression().getInferredSequenceType();
        basicChecks(mainType, expression.getClass().getSimpleName(), true, true);

        Expression predicateExpression = expression.getPredicateExpression();
        // set context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(new SequenceType(mainType.getItemType()));
        visit(predicateExpression, argument);
        SequenceType predicateType = predicateExpression.getInferredSequenceType();
        // unset context item static type
        predicateExpression.getStaticContext().setContextItemStaticType(null);

        basicChecks(predicateType, expression.getClass().getSimpleName(), true, true);
        // always false so the return type is for sure ()
        if (predicateType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type for FilterExpression is empty sequence (with active static typing feature, only allowed for CommaExpression)",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }
        if (!predicateType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "Inferred type " + predicateType + " in FilterExpression has no effective boolean value"
            );
        }

        // if we are filter one or less items or we use an integer to select a specific position we return at most one
        // element, otherwise *
        SequenceType.Arity inferredArity = (mainType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)
            || predicateType.getItemType().equals(AtomicItemType.integerItem))
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.ZeroOrMore;
        expression.setInferredSequenceType(new SequenceType(mainType.getItemType(), inferredArity));
        System.out.println("visiting Filter expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // return [true] if [types] are subtype of or can be promoted to expected types, [false] otherwise
    public boolean checkArguments(List<SequenceType> expectedTypes, List<SequenceType> types) {
        int length = expectedTypes.size();
        if (length != types.size()) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            if (!types.get(i).isSubtypeOfOrCanBePromotedTo(expectedTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StaticContext visitDynamicFunctionCallExpression(
            DynamicFunctionCallExpression expression,
            StaticContext argument
    ) {
        // since we do not specify function's signature in the itemType we can only check that it is a function
        visitDescendants(expression, argument);

        SequenceType mainType = expression.getMainExpression().getInferredSequenceType();
        basicChecks(mainType, expression.getClass().getSimpleName(), true, false);

        FunctionSignature signature = null;
        boolean isAnyFunction = false;
        if (!mainType.isEmptySequence()) {
            ItemType type = mainType.getItemType();
            if (type.isFunctionItem()) {
                if (type.equals(AtomicItemType.functionItem)) {
                    isAnyFunction = true;
                } else {
                    signature = type.getSignature();
                }
            }
        }

        List<SequenceType> argsType = expression.getArguments()
            .stream()
            .map(expr -> expr.getInferredSequenceType())
            .collect(Collectors.toList());
        if (isAnyFunction || (signature != null && checkArguments(signature.getParameterTypes(), argsType))) {
            // TODO: need to add support for partial application
            expression.setInferredSequenceType(SequenceType.MOST_GENERAL_SEQUENCE_TYPE);
            System.out.println(
                "visiting DynamicFunctionCall expression, type set to: " + expression.getInferredSequenceType()
            );
            return argument;
        }

        throw new UnexpectedStaticTypeException(
                "the type of a dynamic function call main expression must be function, instead inferred " + mainType
        );
    }

    @Override
    public StaticContext visitSimpleMapExpr(SimpleMapExpression expression, StaticContext argument) {
        List<Node> nodes = expression.getChildren();
        Expression leftExpression = (Expression) nodes.get(0);
        Expression rightExpression = (Expression) nodes.get(1);

        visit(leftExpression, argument);
        SequenceType leftType = leftExpression.getInferredSequenceType();
        basicChecks(leftType, expression.getClass().getSimpleName(), true, true);

        // set context item static type
        rightExpression.getStaticContext().setContextItemStaticType(new SequenceType(leftType.getItemType()));
        visit(rightExpression, argument);
        rightExpression.getStaticContext().setContextItemStaticType(null);

        SequenceType rightType = rightExpression.getInferredSequenceType();
        basicChecks(rightType, expression.getClass().getSimpleName(), true, true);

        SequenceType.Arity resultingArity = leftType.getArity().multiplyWith(rightType.getArity());
        expression.setInferredSequenceType(new SequenceType(rightType.getItemType(), resultingArity));
        System.out.println("visiting SimpleMap expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region FLOWR

    @Override
    public StaticContext visitFlowrExpression(FlworExpression expression, StaticContext argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        SequenceType.Arity forArities = SequenceType.Arity.One; // One is arity multiplication's neutral element
        SequenceType forType;

        while (clause != null) {
            this.visit(clause, argument);
            // if there are for clauses we need to consider their arities for the returning expression
            if (clause.getClauseType() == FLWOR_CLAUSES.FOR) {
                forType = ((ForClause) clause).getExpression().getInferredSequenceType();
                // if forType is the empty sequence that means that allowing empty is set otherwise we would have thrown
                // an error
                // therefore this for loop will generate one tuple binding the empty sequence, so as for the arities
                // count as arity.One
                if (!forType.isEmptySequence()) {
                    forArities = forType.getArity().multiplyWith(forArities);
                }
            } else if (clause.getClauseType() == FLWOR_CLAUSES.WHERE) {
                // where clause could reject all tuples so arity change from + => * and 1 => ?
                if (forArities == SequenceType.Arity.One) {
                    forArities = SequenceType.Arity.OneOrZero;
                } else if (forArities == SequenceType.Arity.OneOrMore) {
                    forArities = SequenceType.Arity.ZeroOrMore;
                }
            }
            clause = clause.getParentClause();
        }

        SequenceType returnType = expression.getReturnClause().getReturnExpr().getInferredSequenceType();
        basicChecks(returnType, expression.getClass().getSimpleName(), true, true);
        returnType = new SequenceType(returnType.getItemType(), returnType.getArity().multiplyWith(forArities));
        expression.setInferredSequenceType(returnType);
        System.out.println("visiting Flowr expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitForClause(ForClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);

        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = (declaredType == null
            ? expression.getExpression()
            : ((TreatExpression) expression.getExpression()).getMainExpression()).getInferredSequenceType();
        basicChecks(inferredType, expression.getClass().getSimpleName(), true, false);
        if (inferredType.isEmptySequence()) {
            if (!expression.isAllowEmpty()) {
                // for sure we will not have any tuple to process and return the empty sequence
                throw new UnexpectedStaticTypeException(
                        "In for clause Inferred type is empty sequence, empty is not allowed, so the result returned is for sure () and this is not a CommaExpression",
                        ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
                );
            }
        } else {
            // we take the single arity version of the inferred type or optional arity if we allow empty and the
            // sequence allows () (i.e. arity ? or *)
            if (
                expression.isAllowEmpty()
                    && (inferredType.getArity() == SequenceType.Arity.OneOrZero
                        || inferredType.getArity() == SequenceType.Arity.ZeroOrMore)
            ) {
                inferredType = new SequenceType(inferredType.getItemType(), SequenceType.Arity.OneOrZero);
            } else {
                inferredType = new SequenceType(inferredType.getItemType());
            }
        }

        checkVariableType(
            declaredType,
            inferredType,
            expression.getParentClause().getStaticContext(),
            expression.getClass().getSimpleName(),
            expression.getVariableName()
        );

        System.out.println("visiting For clause, inferred var " + expression.getVariableName() + " : " + inferredType);
        return argument;
    }

    @Override
    public StaticContext visitLetClause(LetClause expression, StaticContext argument) {
        visit(expression.getExpression(), argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = (declaredType == null
            ? expression.getExpression()
            : ((TreatExpression) expression.getExpression()).getMainExpression()).getInferredSequenceType();
        checkVariableType(
            declaredType,
            inferredType,
            expression.getParentClause().getStaticContext(),
            expression.getClass().getSimpleName(),
            expression.getVariableName()
        );

        System.out.println("visiting Let clause, var " + expression.getVariableName() + " : " + inferredType);
        return argument;
    }

    @Override
    public StaticContext visitWhereClause(WhereClause expression, StaticContext argument) {
        visit(expression.getWhereExpression(), argument);
        SequenceType whereType = expression.getWhereExpression().getInferredSequenceType();
        basicChecks(whereType, expression.getClass().getSimpleName(), true, false);
        if (!whereType.hasEffectiveBooleanValue()) {
            throw new UnexpectedStaticTypeException(
                    "where clause inferred type (" + whereType + ") has no effective boolean value"
            );
        }
        if (whereType.isEmptySequence() || whereType.isSubtypeOf(SequenceType.createSequenceType("null?"))) {
            throw new UnexpectedStaticTypeException(
                    "where clause always return false, so return expression inferred type is empty sequence and this is not a CommaExpression",
                    ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression
            );
        }
        return argument;
    }

    @Override
    public StaticContext visitGroupByClause(GroupByClause expression, StaticContext argument) {
        Clause nextClause = expression.getParentClause(); // != null because group by cannot be last clause of FLOWR
                                                          // expression
        Set<Name> groupingVars = new HashSet<>();
        for (GroupByVariableDeclaration groupByVar : expression.getGroupVariables()) {
            // if we are grouping by an existing var (i.e. expr is null), then the appropriate type is already inferred
            Expression groupByVarExpr = groupByVar.getExpression();
            SequenceType expectedType;
            if (groupByVarExpr != null) {
                visit(groupByVarExpr, argument);
                SequenceType declaredType = groupByVar.getActualSequenceType();
                SequenceType inferredType;
                if (declaredType == null) {
                    inferredType = groupByVarExpr.getInferredSequenceType();
                    expectedType = inferredType;
                } else {
                    inferredType = ((TreatExpression) groupByVarExpr).getMainExpression().getInferredSequenceType();
                    expectedType = declaredType;
                }
                checkVariableType(
                    declaredType,
                    inferredType,
                    nextClause.getStaticContext(),
                    expression.getClass().getSimpleName(),
                    groupByVar.getVariableName()
                );
            } else {
                expectedType = expression.getStaticContext().getVariableSequenceType(groupByVar.getVariableName());
            }
            // check that expectedType is a subtype of atomic?
            if (expectedType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
                throw new UnexpectedStaticTypeException(
                        "group by variable "
                            + groupByVar.getVariableName()
                            + " must match atomic? instead found "
                            + expectedType,
                        ErrorCode.NonAtomicElementErrorCode
                );
            }
            if (!expectedType.isSubtypeOf(SequenceType.createSequenceType("atomic?"))) {
                throw new UnexpectedStaticTypeException(
                        "group by variable "
                            + groupByVar.getVariableName()
                            + " must match atomic? instead found "
                            + expectedType
                );
            }
            groupingVars.add(groupByVar.getVariableName());
        }

        // finally if there was a for clause we need to change the arity of the variables binded so far in the flowr
        // expression, from ? to * and from 1 to +
        // excluding the grouping variables
        StaticContext firstClauseStaticContext = expression.getFirstClause().getStaticContext();
        nextClause.getStaticContext().incrementArities(firstClauseStaticContext, groupingVars);
        return argument;
    }

    @Override
    public StaticContext visitOrderByClause(OrderByClause expression, StaticContext argument) {
        visitDescendants(expression, argument);
        for (OrderByClauseSortingKey orderClause : expression.getSortingKeys()) {
            SequenceType orderType = orderClause.getExpression().getInferredSequenceType();
            basicChecks(orderType, expression.getClass().getSimpleName(), true, false);
            if (orderType.isSubtypeOf(SequenceType.createSequenceType("json-item*"))) {
                throw new UnexpectedStaticTypeException(
                        "order by sorting expression's type must match atomic? and be comparable using 'gt' operator (so duration, hexBinary, base64Binary and atomic item type are not allowed), instead inferred: "
                            + orderType,
                        ErrorCode.NonAtomicElementErrorCode
                );
            }
            if (
                !orderType.isSubtypeOf(SequenceType.createSequenceType("atomic?"))
                    ||
                    orderType.getItemType().equals(AtomicItemType.atomicItem)
                    ||
                    orderType.getItemType().equals(AtomicItemType.durationItem)
                    ||
                    orderType.getItemType().equals(AtomicItemType.hexBinaryItem)
                    ||
                    orderType.getItemType().equals(AtomicItemType.base64BinaryItem)
            ) {
                throw new UnexpectedStaticTypeException(
                        "order by sorting expression's type must match atomic? and be comparable using 'gt' operator (so duration, hexBinary, base64Binary and atomic item type are not allowed), instead inferred: "
                            + orderType
                );
            }
        }

        return argument;
    }

    // endregion

    // region module

    // if [declaredType] is not null, check if the inferred type matches or can be promoted to the declared type
    // (otherwise throw type error)
    // if [declaredType] is null, replace the type of [variableName] in the [context] with the inferred type
    public void checkVariableType(
            SequenceType declaredType,
            SequenceType inferredType,
            StaticContext context,
            String nodeName,
            Name variableName
    ) {
        basicChecks(inferredType, nodeName, true, false);

        if (declaredType == null) {
            // if declared type is null, we overwrite the type in the correspondent InScopeVariable with the inferred
            // type
            context.replaceVariableSequenceType(variableName, inferredType);
        } else {
            if (!inferredType.isSubtypeOf(declaredType)) {
                throw new UnexpectedStaticTypeException(
                        "In a "
                            + nodeName
                            + ", the variable $"
                            + variableName
                            + " inferred type "
                            + inferredType
                            + " does not match or can be promoted to the declared type "
                            + declaredType
                );
            }
        }
    }

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType declaredType = expression.getActualSequenceType();
        SequenceType inferredType = (declaredType == null
            ? expression.getExpression()
            : ((TreatExpression) expression.getExpression()).getMainExpression()).getInferredSequenceType();
        checkVariableType(
            declaredType,
            inferredType,
            argument,
            expression.getClass().getSimpleName(),
            expression.getVariableName()
        );

        return argument;
    }

    @Override
    public StaticContext visitFunctionDeclaration(FunctionDeclaration expression, StaticContext argument) {
        visitDescendants(expression, argument);

        InlineFunctionExpression inlineExpression = (InlineFunctionExpression) expression.getExpression();
        SequenceType inferredType = inlineExpression.getBody().getInferredSequenceType();
        SequenceType expectedType = inlineExpression.getActualReturnType();

        if (expectedType == null) {
            expectedType = inferredType;
        } else if (!inferredType.isSubtypeOfOrCanBePromotedTo(expectedType)) {
            throw new UnexpectedStaticTypeException(
                    "The declared function return inferred type "
                        + inferredType
                        + " does not match or can be promoted to the expected return type "
                        + expectedType
            );
        }

        // add function signature to the statically known one
        argument.addFunctionSignature(
            inlineExpression.getFunctionIdentifier(),
            new FunctionSignature(new ArrayList<SequenceType>(inlineExpression.getParams().values()), expectedType)
        );

        return argument;
    }

    // endregion
}
