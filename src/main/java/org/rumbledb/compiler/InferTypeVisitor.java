package org.rumbledb.compiler;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;
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
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.TryCatchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.logic.AndExpression;
import org.rumbledb.expressions.logic.NotExpression;
import org.rumbledb.expressions.logic.OrExpression;
import org.rumbledb.expressions.miscellaneous.RangeExpression;
import org.rumbledb.expressions.miscellaneous.StringConcatExpression;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.*;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.List;

/**
 * This visitor infers a static SequenceType for each expression in the query
 */
public class InferTypeVisitor extends AbstractNodeVisitor<StaticContext> {

    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;

    /**
     * Builds a new visitor.
     *
     * @param rumbleRuntimeConfiguration the configuration.
     */
    InferTypeVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
    }

    @Override
    public StaticContext visitCommaExpression(CommaExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        SequenceType inferredType = SequenceType.EMPTY_SEQUENCE;
        
        for(Expression childExpression : expression.getExpressions()){
            SequenceType childExpressionInferredType = childExpression.getInferredSequenceType();

            // if a child expression has no inferred type throw an error
            if(childExpressionInferredType == null){
                throw new UnexpectedStaticTypeException("A child expression of a CommaExpression has no inferred type");
            }

            // if the child expression is an EMPTY_SEQUENCE it does not affect the comma expression type
            if(!childExpressionInferredType.isEmptySequence()){
                if(inferredType.isEmptySequence()){
                    inferredType = childExpressionInferredType;
                } else{
                   ItemType resultingItemType = inferredType.getItemType().findCommonSuperType(childExpressionInferredType.getItemType());
                   SequenceType.Arity resultingArity =
                           ( (inferredType.getArity() == SequenceType.Arity.OneOrZero || inferredType.getArity() == SequenceType.Arity.ZeroOrMore) &&
                           (childExpressionInferredType.getArity() == SequenceType.Arity.OneOrZero || childExpressionInferredType.getArity() == SequenceType.Arity.ZeroOrMore)) ?
                                   SequenceType.Arity.ZeroOrMore : SequenceType.Arity.OneOrMore;
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
    public StaticContext visitString(StringLiteralExpression expression, StaticContext argument){
        System.out.println("visiting String literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.stringItem));
        return argument;
    }

    @Override
    public StaticContext visitInteger(IntegerLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Int literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.integerItem));
        return argument;
    }

    @Override
    public StaticContext visitDouble(DoubleLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Double literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.doubleItem));
        return argument;
    }

    @Override
    public StaticContext visitDecimal(DecimalLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Decimal literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.decimalItem));
        return argument;
    }

    @Override
    public StaticContext visitNull(NullLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Null literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.nullItem));
        return argument;
    }

    @Override
    public StaticContext visitBoolean(BooleanLiteralExpression expression, StaticContext argument) {
        System.out.println("visiting Boolean literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitVariableReference(VariableReferenceExpression expression, StaticContext argument) {
        SequenceType variableType = expression.getActualType();
        if(variableType == null){
            // if is null, no 'as [SequenceType]' part was present in the declaration, therefore we infer it
            System.out.println("variable reference type was null so we infer it");
            variableType = expression.getStaticContext().getVariableSequenceType(expression.getVariableName());
            // we also set variableReference type
            expression.setType(variableType);
        }
        System.out.println("visiting variable reference with type: " + variableType);
        expression.setInferredSequenceType(variableType);
        return argument;
    }

    @Override
    public StaticContext visitArrayConstructor(ArrayConstructorExpression expression, StaticContext argument) {
        System.out.println("visiting Array constructor literal");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.arrayItem));
        return argument;
    }

    @Override
    public StaticContext visitObjectConstructor(ObjectConstructorExpression expression, StaticContext argument) {
        System.out.println("visiting Object constructor literal");
        visitDescendants(expression, argument);
        if(expression.isMergedConstructor()){
            // if it is a merged constructor the child must be a subtype of object* inferred type
            SequenceType childSequenceType = ((Expression) expression.getChildren().get(0)).getInferredSequenceType();
            if(childSequenceType == null) {
                throw new UnexpectedStaticTypeException("The child expression has no inferred type");
            }
            if(!childSequenceType.isSubtypeOf(SequenceType.createSequenceType("object*"))){
                throw new UnexpectedStaticTypeException("The child expression must have object* sequence type, instead found: " + childSequenceType);
            }
        } else {
            for (Expression keyExpression : expression.getKeys()) {
                SequenceType keySequenceType = keyExpression.getInferredSequenceType();
                if (keySequenceType == null) {
                    throw new UnexpectedStaticTypeException("One of the key in the object constructor has no inferred type");
                }
                if (!keySequenceType.isSubtypeOf(SequenceType.createSequenceType("string")) && !keySequenceType.isSubtypeOf(SequenceType.createSequenceType("anyURI"))) {
                    throw new UnexpectedStaticTypeException("The inferred static sequence types for the keys of an Object must be a subtype of string or anyURI, instead found a: " + keySequenceType);
                }
            }
        }
        expression.setInferredSequenceType(new SequenceType(ItemType.objectItem));
        return argument;
    }

    @Override
    public StaticContext visitContextExpr(ContextItemExpression expression, StaticContext argument) {
        SequenceType contextType = expression.getStaticContext().getContextItemStaticType();
        if(contextType == null){
            contextType = new SequenceType(ItemType.item);
        }
        expression.setInferredSequenceType(contextType);
        System.out.println("Visited context expression, set type: " + contextType);
        return argument;
    }

    @Override
    public StaticContext visitInlineFunctionExpr(InlineFunctionExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.functionItem));
        System.out.println("Visited inline function expression");
        return argument;
    }

    @Override
    public StaticContext visitNamedFunctionRef(NamedFunctionReferenceExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.functionItem));
        System.out.println("Visited named function expression");
        return argument;
    }

    @Override
    public StaticContext visitFunctionCall(FunctionCallExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        BuiltinFunction function = null;
        try {
            function = BuiltinFunctionCatalogue.getBuiltinFunction(expression.getFunctionIdentifier());
        } catch (OurBadException exception){
            // TODO: where can i find all available functions in the static context
        }

        if(function == null){
            throw new UnexpectedStaticTypeException("Function " + expression.getFunctionIdentifier() + " is not available");
        }

        List<Expression> parameterExpressions = expression.getArguments();
        List<SequenceType> parameterTypes = function.getSignature().getParameterTypes();
        int paramsLength = parameterExpressions.size();

        for(int i = 0; i < paramsLength; ++i){
            if(parameterExpressions.get(i) != null){
                SequenceType actualType = parameterExpressions.get(i).getInferredSequenceType();
                SequenceType expectedType = parameterTypes.get(i);
                // check actual parameters is either a subtype of or can be promoted to expected type
                // TODO: should i consider automatic prmotion as valid or not
                if(!actualType.isSubtypeOfOrCanBePromotedTo(expectedType)){
                    throw new UnexpectedStaticTypeException("Argument " + i + " requires " + expectedType + " but " + actualType + " was found");
                }
            }
        }

        if(expression.isPartialApplication()){
            expression.setInferredSequenceType(new SequenceType(ItemType.functionItem));
        } else {
            SequenceType returnType = function.getSignature().getReturnType();
            if(returnType == null){
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
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitCastExpression(CastExpression expression, StaticContext argument) {
        System.out.println("visiting Cast expression");
        visitDescendants(expression, argument);

        // check at static time for casting errors (note cast only allows for normal or ? arity)
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType castedSequenceType = expression.getSequenceType();

        // Empty sequence check
        if(expressionSequenceType.isEmptySequence() && castedSequenceType.getArity() != SequenceType.Arity.OneOrZero){
            throw new UnexpectedStaticTypeException("Empty sequence cannot be cast to type with quantifier different from '?'");
        }

        // Arity check
        if(!castedSequenceType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)){
            throw new UnexpectedStaticTypeException("It is possible to cast only to types with arity '1' or '?'");
        }
        if(!expressionSequenceType.isAritySubtypeOf(castedSequenceType.getArity())){
            throw new UnexpectedStaticTypeException("It is never possible to cast a " +
                    expressionSequenceType + " as " + castedSequenceType);
        }

        // ItemType static castability check
        if(!expressionSequenceType.getItemType().staticallyCastableAs(castedSequenceType.getItemType())){
            throw new UnexpectedStaticTypeException("It is never possible to cast a " +
                    expressionSequenceType + " as " + castedSequenceType);
        }

        expression.setInferredSequenceType(castedSequenceType);
        return argument;
    }

    @Override
    public StaticContext visitInstanceOfExpression(InstanceOfExpression expression, StaticContext argument) {
        System.out.println("visiting InstanceOf expression");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public StaticContext visitTreatExpression(TreatExpression expression, StaticContext argument) {
        System.out.println("visiting Treat expression");
        visitDescendants(expression, argument);

        // check at static time for treat errors
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType treatedSequenceType = expression.getSequenceType();

        if(expressionSequenceType == null || treatedSequenceType == null){
            throw new UnexpectedStaticTypeException("The child expression of a Treat expression has no inferred type or it is being treated as null sequence type");
        }

        expression.setInferredSequenceType(treatedSequenceType);
        return argument;
    }

    // endregion

    // region arithmetic

    @Override
    public StaticContext visitAdditiveExpr(AdditiveExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();

        // if any of the child expression has null inferred type throw error
        if(leftInferredType == null || rightInferredType == null){
            throw new UnexpectedStaticTypeException("A child expression of a AdditiveExpression has no inferred type");
        }

        // if any of the children is the empty sequence throw error XPST0005
        if(leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }

        ItemType inferredType;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType.getArity(), rightInferredType.getArity());

        // arity check
        if(inferredArity == null){
            throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for additive expressions");
        }

        inferredType = leftInferredType.getItemType().staticallyAddTo(rightInferredType.getItemType(), expression.isMinus());

        if(inferredType == null){
            if(inferredArity == SequenceType.Arity.OneOrZero){
                // Only possible resulting type is empty sequence so throw error XPST0005
                throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
            } else {
                throw new UnexpectedStaticTypeException("The following types operation is not possible: " + leftInferredType + (expression.isMinus() ? " - " : " + ") + rightInferredType);
            }
        }

        expression.setInferredSequenceType(new SequenceType(inferredType, inferredArity));
        System.out.println("visiting Additive expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }

    // This function assume 2 numeric ItemType
    private ItemType resolveNumericType(ItemType left, ItemType right){
        if(left.equals(ItemType.doubleItem) || right.equals(ItemType.doubleItem)){
            return ItemType.doubleItem;
        } else if(left.equals(ItemType.decimalItem) || right.equals(ItemType.decimalItem)){
            return ItemType.decimalItem;
        } else {
            return ItemType.integerItem;
        }
    }

    // For arithmetic operations, given 2 arities, return the resulting arity or null in case of invalid arity
    private SequenceType.Arity resolveArities(SequenceType.Arity left, SequenceType.Arity right) {
        if(left == null ||
                left == SequenceType.Arity.ZeroOrMore ||
                left == SequenceType.Arity.OneOrMore ||
                right == null ||
                right == SequenceType.Arity.ZeroOrMore ||
                right == SequenceType.Arity.OneOrMore
        ) return null;
        return (left == SequenceType.Arity.OneOrZero || right == SequenceType.Arity.OneOrZero) ? SequenceType.Arity.OneOrZero : SequenceType.Arity.One;
    }

    @Override
    public StaticContext visitMultiplicativeExpr(MultiplicativeExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();

        // if any of the child expression has null inferred type throw error
        if(leftInferredType == null || rightInferredType == null){
            throw new UnexpectedStaticTypeException("A child expression of a MultiplicativeExpression has no inferred type");
        }

        // if any of the children is the empty sequence throw error XPST0005
        if(leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }

        ItemType inferredType = null;
        SequenceType.Arity inferredArity = resolveArities(leftInferredType.getArity(), rightInferredType.getArity());

        if(inferredArity == null){
            throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for multiplicative expressions");
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        // check resulting item for each operation
        if(leftItemType.isNumeric()){
            if(rightItemType.isNumeric()){
                if(expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.IDIV){
                    inferredType = ItemType.integerItem;
                } else if(expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.DIV) {
                    inferredType = resolveNumericType(ItemType.decimalItem, resolveNumericType(leftItemType, rightItemType));
                } else {
                    inferredType = resolveNumericType(leftItemType, rightItemType);
                }
            } else if(rightItemType.isSubtypeOf(ItemType.durationItem) &&
                    expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.MUL){
                inferredType = rightItemType;
            }
        } else if(leftItemType.isSubtypeOf(ItemType.durationItem)){
            if(rightItemType.isNumeric() && (
                    expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.MUL ||
                    expression.getMultiplicativeOperator() == MultiplicativeExpression.MultiplicativeOperator.DIV )){
                inferredType = rightItemType;
            } else if(rightItemType.equals(leftItemType) && !leftItemType.equals(ItemType.durationItem)){
                inferredType = ItemType.decimalItem;
            }
        }

        if(inferredType == null){
            if(inferredArity == SequenceType.Arity.OneOrZero){
                // Only possible resulting type is empty sequence so throw error XPST0005
                throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
            } else {
                throw new UnexpectedStaticTypeException("The following types expression is not valid: " + leftItemType + " " + expression.getMultiplicativeOperator() + " " + rightItemType);
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
        if(childInferredType == null){
            throw new UnexpectedStaticTypeException("The child expression of a UnaryExpression has no inferred type");
        }

        // if the child is the empty sequence just infer the empty sequence
        if(childInferredType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }

        if(childInferredType.getArity() == SequenceType.Arity.OneOrMore || childInferredType.getArity() == SequenceType.Arity.ZeroOrMore){
            throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for unary expressions");
        }

        // if inferred arity does not allow for empty sequence and static type is not an accepted one throw a static error
        ItemType childItemType = childInferredType.getItemType();
        if(!childItemType.isNumeric()){
            if(childInferredType.getArity() == SequenceType.Arity.OneOrZero){
                throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
            } else {
                throw new UnexpectedStaticTypeException("It is not possible to have an Unary expression with the following type: " + childInferredType);
            }
        }

        expression.setInferredSequenceType(new SequenceType(childItemType, childInferredType.getArity()));
        System.out.println("visiting Unary expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region logic

    private StaticContext visitAndOrExpr(Expression expression, StaticContext argument, String expressionName){
        visitDescendants(expression, argument);

        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();

        if(leftInferredType == null || rightInferredType == null){
            throw new UnexpectedStaticTypeException("A child expression of a " + expressionName + "Expression has no inferred type");
        }

        if(!leftInferredType.hasEffectiveBooleanValue()){
            throw new UnexpectedStaticTypeException("left expression of a " + expressionName + "Expression has " + leftInferredType + " inferred type, which has no effective boolean value");
        }

        if(!rightInferredType.hasEffectiveBooleanValue()){
            throw new UnexpectedStaticTypeException("right expression of a " + expressionName + "Expression has " + rightInferredType + " inferred type, which has no effective boolean value");
        }

        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
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
        if(childInferredType == null){
            throw new UnexpectedStaticTypeException("The child expression of NotExpression has no inferred type");
        }
        if(!childInferredType.hasEffectiveBooleanValue()){
            throw new UnexpectedStaticTypeException("The child expression of NotExpression has " + childInferredType + " inferred type, which has no effective boolean value");
        }

        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
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

        if(leftInferredType == null || rightInferredType == null){
            throw new UnexpectedStaticTypeException("A child expression of a ComparisonExpression has no inferred type");
        }

        ComparisonExpression.ComparisonOperator operator = expression.getComparisonOperator();

        // for value comparison arities * and + are not allowed, also if one return the empty sequence for sure throw XPST0005 error
        if(operator.isValueComparison()){
            if(leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()){
                throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
            }
            if(resolveArities(leftInferredType.getArity(), rightInferredType.getArity()) == null){
                throw new UnexpectedStaticTypeException("'+' and '*' arities are not allowed for this comparison operator: " + operator);
            }
        }

        ItemType leftItemType = leftInferredType.getItemType();
        ItemType rightItemType = rightInferredType.getItemType();

        // Type must be a strict subtype of atomic
        if(!leftItemType.isSubtypeOf(ItemType.atomicItem) || !rightItemType.isSubtypeOf(ItemType.atomicItem) || leftItemType.equals(ItemType.atomicItem) || rightItemType.equals(ItemType.atomicItem)){
            throw new UnexpectedStaticTypeException("It is not possible to compare with non-atomic types");
        }

        // Type must match exactly or be both numeric or both promotable to string or both durations
        if(!leftItemType.equals(rightItemType) &&
                !(leftItemType.isNumeric() && rightItemType.isNumeric()) &&
                !(leftItemType.isSubtypeOf(ItemType.durationItem) && rightItemType.isSubtypeOf(ItemType.durationItem)) &&
                !(leftItemType.canBePromotedToString() && rightItemType.canBePromotedToString())){
            throw new UnexpectedStaticTypeException("It is not possible to compare these types: " + leftItemType + " and " + rightItemType);
        }

        // Inequality is not defined for hexBinary and base64binary or for duration of different types
        if((operator != ComparisonExpression.ComparisonOperator.VC_EQ &&
                operator != ComparisonExpression.ComparisonOperator.VC_NE &&
                operator != ComparisonExpression.ComparisonOperator.GC_EQ &&
                operator != ComparisonExpression.ComparisonOperator.GC_NE) && (
                        leftItemType.equals(ItemType.hexBinaryItem) || leftItemType.equals(ItemType.base64BinaryItem) ||
                                leftItemType.equals(ItemType.durationItem) || rightItemType.equals(ItemType.durationItem) ||
                                ((leftItemType.equals(ItemType.dayTimeDurationItem) || leftItemType.equals(ItemType.yearMonthDurationItem)) && !rightItemType.equals(leftItemType))
                )){
            throw new UnexpectedStaticTypeException("It is not possible to compare these types: " + leftItemType + " " + operator + " " + rightItemType);
        }

        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
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

        if(ifType == null || thenType == null || elseType == null){
            throw new OurBadException("A child expression of a ConditionalExpression has no inferred type");
        }

        if(!ifType.hasEffectiveBooleanValue()){
            throw new UnexpectedStaticTypeException("The condition in the 'if' must have effective boolean value, found inferred type: " + ifType + " (which has not effective boolean value)");
        }

        if(thenType.isEmptySequence() && elseType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }

        expression.setInferredSequenceType(thenType.leastCommonSupertypeWith(elseType));
        System.out.println("visiting Conditional expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitTryCatchExpression(TryCatchExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);
        SequenceType inferredType = null;

        for(Node childNode : expression.getChildren()){
            SequenceType childType = ((Expression) childNode).getInferredSequenceType();
            if(childType == null){
                throw new OurBadException("A child expression of a TryCatchExpression has no inferred type");
            }

            if(inferredType == null){
                inferredType = childType;
            } else {
                inferredType = inferredType.leastCommonSupertypeWith(childType);
            }
        }

        if(inferredType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
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
        if(conditionType == null){
            throw new OurBadException("A child expression of a TypeSwitchExpression has no inferred type");
        }

        for(TypeswitchCase typeswitchCase : expression.getCases()){
            Name variableName = typeswitchCase.getVariableName();
            Expression returnExpression = typeswitchCase.getReturnExpression();
            // if we bind a variable we add the static type of it in the context of the return expression
            if(variableName != null){
                SequenceType variableType = null;
                for(SequenceType st : typeswitchCase.getUnion()){
                    variableType = variableType == null ? st : variableType.leastCommonSupertypeWith(st);
                }
                returnExpression.getStaticContext().replaceVariableSequenceType(variableName, variableType);
            }

            visit(returnExpression, argument);
            SequenceType caseType = returnExpression.getInferredSequenceType();
            if(caseType == null){
                throw new OurBadException("A child expression of a TypeSwitchExpression has no inferred type");
            }
            inferredType = inferredType == null ? caseType : inferredType.leastCommonSupertypeWith(caseType);
        }

        Name variableName = expression.getDefaultCase().getVariableName();
        Expression returnExpression = expression.getDefaultCase().getReturnExpression();
        // if we bind a variable in the default case, we infer testCondition type
        if(variableName != null){
            returnExpression.getStaticContext().replaceVariableSequenceType(variableName, conditionType);
        }
        visit(returnExpression, argument);
        SequenceType defaultType = returnExpression.getInferredSequenceType();
        if(defaultType == null){
            throw new OurBadException("A child expression of a TypeSwitchExpression has no inferred type");
        }
        inferredType = inferredType.leastCommonSupertypeWith(defaultType);

        if(inferredType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }
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

        if(leftType == null || rightType == null){
            throw new OurBadException("A child expression of a RangeExpression has no inferred type");
        }

        if(leftType.isEmptySequence() || rightType.isEmptySequence()){
            throw new UnexpectedStaticTypeException("Inferred type is empty sequence and this is not a CommaExpression", ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
        }

        SequenceType intOpt = new SequenceType(ItemType.integerItem, SequenceType.Arity.OneOrZero);
        if(!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)){
            throw new UnexpectedStaticTypeException("operands of the range expression must match type integer? instead found: " + leftType + " and " + rightType);
        }

        expression.setInferredSequenceType(new SequenceType(ItemType.integerItem, SequenceType.Arity.ZeroOrMore));
        System.out.println("visiting Range expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    @Override
    public StaticContext visitStringConcatExpr(StringConcatExpression expression, StaticContext argument) {
        visitDescendants(expression, argument);

        List<Node> children = expression.getChildren();
        SequenceType leftType = ((Expression) children.get(0)).getInferredSequenceType();
        SequenceType rightType = ((Expression) children.get(1)).getInferredSequenceType();

        if(leftType == null || rightType == null){
            throw new OurBadException("A child expression of a ConcatExpression has no inferred type");
        }

        SequenceType intOpt = new SequenceType(ItemType.atomicItem, SequenceType.Arity.OneOrZero);
        if(!leftType.isSubtypeOf(intOpt) || !rightType.isSubtypeOf(intOpt)){
            throw new UnexpectedStaticTypeException("operands of the concat expression must match type atomic? instead found: " + leftType + " and " + rightType);
        }

        expression.setInferredSequenceType(new SequenceType(ItemType.stringItem));
        System.out.println("visiting Concat expression, type set to: " + expression.getInferredSequenceType());
        return argument;
    }

    // endregion

    // region module

    @Override
    public StaticContext visitVariableDeclaration(VariableDeclaration expression, StaticContext argument) {
        // if expression has no type we infer it, and overwrite the type in the correspondent InScopeVariable
        visitDescendants(expression, argument);
        if(expression.getActualSequenceType() == null){
            SequenceType inferredType = expression.getExpression().getInferredSequenceType();
            if(inferredType == null){
                throw new OurBadException("The child expression of VariableDeclaration has no inferred type");
            }
            argument.replaceVariableSequenceType(expression.getVariableName(), inferredType);
        }

        return argument;
    }

    // endregion
}
