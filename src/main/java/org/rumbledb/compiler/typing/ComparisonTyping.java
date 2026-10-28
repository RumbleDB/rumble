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
package org.rumbledb.compiler.typing;

import java.util.List;

import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Typing rules of value, general and node comparisons. */
public final class ComparisonTyping {

    private final StaticTypeErrors errors;

    public ComparisonTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferComparisonType(
            SequenceType leftInferredType,
            SequenceType rightInferredType,
            ComparisonExpression.ComparisonOperator operator,
            Node node) {
        SequenceType.Arity returnArity = SequenceType.Arity.One;

        // for value comparison arities * and + are not allowed, also if one return the empty sequence for sure throw
        // XPST0005 error
        if (operator.isValueComparison()) {
            if (leftInferredType.isEmptySequence() || rightInferredType.isEmptySequence()) {
                this.errors.reportInferredEmpty(node);
            }
            returnArity = AtomizedOperands.resolveArities(leftInferredType, rightInferredType);
            if (returnArity == null) {
                this.errors.report(
                        "'+' and '*' arities are not allowed for this comparison operator: " + operator,
                        node,
                        AtomizedOperands.mayBeSingleton(leftInferredType)
                                && AtomizedOperands.mayBeSingleton(rightInferredType));
                returnArity = SequenceType.Arity.OneOrZero;
            }
        }

        // if any of the element is the empty sequence, we set its sequence type to the other, if both are we do not
        // need additional checks
        boolean isLeftEmpty = leftInferredType.isEmptySequence();
        boolean isRightEmpty = rightInferredType.isEmptySequence();
        if (!isLeftEmpty || !isRightEmpty) {

            ItemType leftItemType = isLeftEmpty ? rightInferredType.getItemType() : leftInferredType.getItemType();
            ItemType rightItemType = isRightEmpty ? leftInferredType.getItemType() : rightInferredType.getItemType();

            if (!TypeAtomization.isAtomicOrNode(leftItemType) || !TypeAtomization.isAtomicOrNode(rightItemType)) {
                this.errors.report(
                        "It is not possible to compare with non-atomic types",
                        ErrorCode.NonAtomicElementErrorCode,
                        node,
                        TypeAtomization.mayBeAtomicOrNode(leftItemType)
                                && TypeAtomization.mayBeAtomicOrNode(rightItemType));
            }

            if (!areComparisonTypesCompatible(leftItemType, rightItemType, operator)) {
                this.errors.report(
                        "It is not possible to compare these types: "
                                + leftItemType
                                + " "
                                + operator
                                + " "
                                + rightItemType,
                        node,
                        TypeAtomization.mayAtomizeToAnyType(leftItemType)
                                || TypeAtomization.mayAtomizeToAnyType(rightItemType));
            }
        }

        return new SequenceType(BuiltinTypesCatalogue.booleanItem, returnArity);
    }

    // Static typing is pessimistic (XQuery 3.1, 2.2.3.1): every pair of member types must be comparable.
    private static boolean areComparisonTypesCompatible(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        return left.allMemberTypesMatch(leftMember -> right.allMemberTypesMatch(
                rightMember -> areAtomizedComparisonTypesCompatible(leftMember, rightMember, operator)));
    }

    private static boolean areAtomizedComparisonTypesCompatible(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        if (TypeAtomization.hasUnknownTypedValue(left) || TypeAtomization.hasUnknownTypedValue(right)) {
            return true; // Unknown typed values require runtime comparison checks.
        }
        left = TypeAtomization.atomizedItemType(left);
        right = TypeAtomization.atomizedItemType(right);
        boolean leftUntyped = left.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);
        boolean rightUntyped = right.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem);
        if (leftUntyped) {
            left = operator.isValueComparison() || rightUntyped
                    ? BuiltinTypesCatalogue.stringItem
                    : right.isNumeric() ? BuiltinTypesCatalogue.doubleItem : right.getCastingPrimitiveType();
        }
        if (rightUntyped) {
            right = operator.isValueComparison() || leftUntyped
                    ? BuiltinTypesCatalogue.stringItem
                    : left.isNumeric() ? BuiltinTypesCatalogue.doubleItem : left.getCastingPrimitiveType();
        }
        return areMemberTypesComparable(left, right, operator);
    }

    private static boolean areMemberTypesComparable(
            ItemType left, ItemType right, ComparisonExpression.ComparisonOperator operator) {
        // JSONiq null is comparable with every atomic value, including for ordering.
        if (left.equals(BuiltinTypesCatalogue.nullItem) || right.equals(BuiltinTypesCatalogue.nullItem)) {
            return true;
        }
        boolean compatible = left.equals(right)
                || (left.isNumeric() && right.isNumeric())
                || (left.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.durationItem))
                || (left.canBePromotedTo(BuiltinTypesCatalogue.stringItem)
                        && right.canBePromotedTo(BuiltinTypesCatalogue.stringItem));
        if (!compatible) {
            return false;
        }
        boolean ordered = operator != ComparisonExpression.ComparisonOperator.VC_EQ
                && operator != ComparisonExpression.ComparisonOperator.VC_NE
                && operator != ComparisonExpression.ComparisonOperator.GC_EQ
                && operator != ComparisonExpression.ComparisonOperator.GC_NE;
        // XQuery 3.1 supports binary ordering. Durations are ordered only within
        // the day-time subtype or within the year-month subtype.
        return !ordered
                || !(left.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                        || right.isSubtypeOf(BuiltinTypesCatalogue.durationItem))
                || (left.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem))
                || (left.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                        && right.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem));
    }

    public SequenceType inferNodeComparisonType(SequenceType leftType, SequenceType rightType, Node node) {

        SequenceType operandType = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceCardinality.ZERO_OR_ONE);
        for (SequenceType type : List.of(leftType, rightType)) {
            if (!type.isSubtypeOf(operandType)) {
                this.errors.reportMismatch(
                        "A node comparison operand must be a single node or empty, found " + type,
                        type,
                        operandType,
                        node);
            }
        }

        // XQuery 3.1 section 3.7.3: an empty operand produces an empty result. Otherwise,
        // a successful comparison produces one boolean, even when operand checks are deferred to runtime.
        SequenceCardinality cardinality;
        if (leftType.isEmptySequence() || rightType.isEmptySequence()) {
            // The strict Static Typing Feature rejects known-empty expressions other than () and data(()).
            this.errors.reportInferredEmpty(
                    "The node comparison is statically inferred to return an empty sequence", node);
            cardinality = SequenceCardinality.EMPTY;
        } else if (leftType.getCardinality().allowsZero()
                || rightType.getCardinality().allowsZero()) {
            cardinality = SequenceCardinality.ZERO_OR_ONE;
        } else {
            cardinality = SequenceCardinality.ONE;
        }
        return new SequenceType(BuiltinTypesCatalogue.booleanItem, cardinality);
    }

    /**
     * fn:min and fn:max compare every input item with the others, so every pair of member types must support
     * ordering, e.g. (xs:string | xs:integer) is rejected.
     */
    public static boolean hasMutuallyComparableItems(ItemType itemType) {
        if (!itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)
                || itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            return false;
        }
        return itemType.allMemberTypesMatch(left -> itemType.allMemberTypesMatch(
                right -> areMemberTypesComparable(left, right, ComparisonExpression.ComparisonOperator.VC_LT)));
    }
}
