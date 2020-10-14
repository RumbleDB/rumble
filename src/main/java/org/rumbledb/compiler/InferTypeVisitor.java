package org.rumbledb.compiler;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.AdditiveExpression;
import org.rumbledb.expressions.primary.*;
import org.rumbledb.expressions.typing.CastExpression;
import org.rumbledb.expressions.typing.CastableExpression;
import org.rumbledb.expressions.typing.InstanceOfExpression;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This visitor infers a static SequenceType for each expression in the query
 */
public class InferTypeVisitor extends AbstractNodeVisitor<Void> {

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
    public Void visitCommaExpression(CommaExpression expression, Void argument) {
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
    public Void visitString(StringLiteralExpression expression, Void argument){
        System.out.println("visiting String literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.stringItem));
        return argument;
    }

    @Override
    public Void visitInteger(IntegerLiteralExpression expression, Void argument) {
        System.out.println("visiting Int literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.integerItem));
        return argument;
    }

    @Override
    public Void visitDouble(DoubleLiteralExpression expression, Void argument) {
        System.out.println("visiting Double literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.doubleItem));
        return argument;
    }

    @Override
    public Void visitDecimal(DecimalLiteralExpression expression, Void argument) {
        System.out.println("visiting Decimal literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.decimalItem));
        return argument;
    }

    @Override
    public Void visitNull(NullLiteralExpression expression, Void argument) {
        System.out.println("visiting Null literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.nullItem));
        return argument;
    }

    @Override
    public Void visitBoolean(BooleanLiteralExpression expression, Void argument) {
        System.out.println("visiting Boolean literal");
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public Void visitVariableReference(VariableReferenceExpression expression, Void argument) {
        SequenceType variableType = expression.getType();
        if(variableType == null){
            System.out.println("variable reference type was null so");
            variableType = SequenceType.MOST_GENERAL_SEQUENCE_TYPE;
        }
        System.out.println("visiting variable reference with type: " + variableType);
        expression.setInferredSequenceType(variableType);
        return argument;
    }

    @Override
    public Void visitArrayConstructor(ArrayConstructorExpression expression, Void argument) {
        System.out.println("visiting Array constructor literal");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.arrayItem));
        return argument;
    }

    @Override
    public Void visitObjectConstructor(ObjectConstructorExpression expression, Void argument) {
        System.out.println("visiting Object constructor literal");
        visitDescendants(expression, argument);
        // TODO: what about object merged constructor with childExpression
        for(Expression keyExpression : expression.getKeys()){
            SequenceType keySequenceType = keyExpression.getInferredSequenceType();
            if(keySequenceType == null){
                throw new UnexpectedStaticTypeException("One of the key in the object constructor has no inferred type");
            }
            ItemType keyType = keySequenceType.getItemType();
            if(!keyType.equals(ItemType.stringItem) && !keyType.equals(ItemType.atomicItem) && !keyType.equals(ItemType.item)){
                throw new UnexpectedStaticTypeException("The inferred static types for the keys of an Object must be String or one of its supertypes (i.e. atomicItem or item)");
            }
        }
        expression.setInferredSequenceType(new SequenceType(ItemType.objectItem));
        return argument;
    }

    // endregion

    // region typing

    @Override
    public Void visitCastableExpression(CastableExpression expression, Void argument) {
        System.out.println("visiting Castable expression");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public Void visitCastExpression(CastExpression expression, Void argument) {
        System.out.println("visiting Cast expression");
        visitDescendants(expression, argument);

        // check at static time for casting errors (note cast only allows for normal or ? arity)
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType castedSequenceType = expression.getSequenceType();

        // Arity basic check
        if(expressionSequenceType.isEmptySequence() && castedSequenceType.getArity() == SequenceType.Arity.One){
            throw new UnexpectedStaticTypeException("Empty sequence cannot be cast to type with quantifier '1'");
        }
        // ItemType static castability check
        if(!expressionSequenceType.getItemType().staticallyCastableAs(castedSequenceType.getItemType())){
            throw new UnexpectedStaticTypeException("It is not possible to cast a " +
                    expressionSequenceType.getItemType() + " as " + castedSequenceType.getItemType());
        }

        expression.setInferredSequenceType(castedSequenceType);
        return argument;
    }

    @Override
    public Void visitInstanceOfExpression(InstanceOfExpression expression, Void argument) {
        System.out.println("visiting InstanceOf expression");
        visitDescendants(expression, argument);
        expression.setInferredSequenceType(new SequenceType(ItemType.booleanItem));
        return argument;
    }

    @Override
    public Void visitTreatExpression(TreatExpression expression, Void argument) {
        System.out.println("visiting Treat expression");
        visitDescendants(expression, argument);

        // check at static time for treat errors
        SequenceType expressionSequenceType = expression.getMainExpression().getInferredSequenceType();
        SequenceType treatedSequenceType = expression.getSequenceType();

        // Empty sequence check (potentially any other arity could fullfill any other arity)
        if(expressionSequenceType.isEmptySequence() &&
                (treatedSequenceType.getArity() == SequenceType.Arity.One || treatedSequenceType.getArity() == SequenceType.Arity.OneOrMore)){
            throw new UnexpectedStaticTypeException("Empty sequence cannot be treated as type with quantifier '1' or '+'");
        }
        // ItemType static treatability check (if the types' spaces are mutually exclusive, one cannot be treated like the other for sure)
        if(!expressionSequenceType.getItemType().isSubtypeOf(treatedSequenceType.getItemType()) &&
           !treatedSequenceType.getItemType().isSubtypeOf(expressionSequenceType.getItemType())){
            throw new UnexpectedStaticTypeException("It is not possible to treat a " +
                    expressionSequenceType.getItemType() + " as " + treatedSequenceType.getItemType());
        }

        expression.setInferredSequenceType(treatedSequenceType);
        return argument;
    }

    // endregion

    // region arithmetic

    @Override
    public Void visitAdditiveExpr(AdditiveExpression expression, Void argument) {
        visitDescendants(expression, argument);

        // TODO: consider direct access with no casting
        List<Node> childrenExpressions = expression.getChildren();
        SequenceType leftInferredType = ((Expression) childrenExpressions.get(0)).getInferredSequenceType();
        SequenceType rightInferredType = ((Expression) childrenExpressions.get(1)).getInferredSequenceType();

        // if any of the child expression has null inferred type throw error
        if(leftInferredType == null || rightInferredType == null){
            throw new UnexpectedStaticTypeException("A child expression of a AdditiveExpression has no inferred type");
        }

        // if any of the children is the empty sequence just infer the empty sequence
        // TODO: check if returning () even when + is not supported with the other type is the intended behaviour
        if(leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()){
            expression.setInferredSequenceType(SequenceType.EMPTY_SEQUENCE);
            System.out.println("visiting Additive expression, set type: " + expression.getInferredSequenceType());
            return argument;
        }

        ItemType inferredType;
        SequenceType.Arity inferredArity;

        // if any of the children allows for the empty sequence the resulting arity is '?'
        if(leftInferredType.getArity() == SequenceType.Arity.OneOrZero ||
                leftInferredType.getArity() == SequenceType.Arity.ZeroOrMore ||
                rightInferredType.getArity() == SequenceType.Arity.OneOrZero ||
                rightInferredType.getArity() == SequenceType.Arity.ZeroOrMore
        ) inferredArity = SequenceType.Arity.OneOrZero;
        else inferredArity = SequenceType.Arity.One;

        inferredType = leftInferredType.getItemType().staticallyAddTo(rightInferredType.getItemType(), expression.isMinus());

        if(inferredType == null){
            if(inferredArity == SequenceType.Arity.OneOrZero){
                // we have incompatible types, but it is possible that at runtime one of the type resolve to be the empty sequence, that is the only possible output not causing an exception
                expression.setInferredSequenceType(SequenceType.EMPTY_SEQUENCE);
            } else {
                throw new UnexpectedStaticTypeException("The following types operation is not possible: " + leftInferredType + (expression.isMinus() ? " - " : " + ") + rightInferredType);
            }
        } else {
            expression.setInferredSequenceType(new SequenceType(inferredType, inferredArity));
        }

        System.out.println("visiting Additive expression, set type: " + expression.getInferredSequenceType());
        return argument;
    }


    // endregion
}
