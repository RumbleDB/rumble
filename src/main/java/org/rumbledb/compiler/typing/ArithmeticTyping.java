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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Typing rules of the additive, multiplicative and unary arithmetic operators. */
public final class ArithmeticTyping {

    private final StaticTypeErrors errors;

    public ArithmeticTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferAdditiveType(SequenceType leftType, SequenceType rightType, boolean minus, Node node) {
        return inferArithmeticType(
                leftType,
                rightType,
                minus ? "-" : "+",
                (left, right) -> inferAdditiveItemType(left, right, minus),
                node);
    }

    public SequenceType inferMultiplicativeType(
            SequenceType leftType,
            SequenceType rightType,
            MultiplicativeExpression.MultiplicativeOperator operator,
            Node node) {
        return inferArithmeticType(
                leftType,
                rightType,
                operator.toString(),
                (left, right) -> inferMultiplicativeItemType(left, right, operator),
                node);
    }

    private SequenceType inferArithmeticType(
            SequenceType leftType,
            SequenceType rightType,
            String operator,
            BiFunction<ItemType, ItemType, ItemType> operation,
            Node node) {
        this.errors.rejectInferredEmpty(leftType, node);
        this.errors.rejectInferredEmpty(rightType, node);

        SequenceType.Arity inferredArity = AtomizedOperands.resolveArities(leftType, rightType);
        if (inferredArity == null) {
            this.errors.report(
                    "'+' and '*' arities are not allowed for the operands of " + operator, node.getMetadata());
            inferredArity = SequenceType.Arity.OneOrZero;
        }

        ItemType inferredType = inferBinaryOperationType(leftType.getItemType(), rightType.getItemType(), operation);
        if (inferredType == null) {
            if (inferredArity == SequenceType.Arity.OneOrZero) {
                // Only possible resulting type is empty sequence so throw error XPST0005
                this.errors.reportInferredEmpty(node);
            } else {
                this.errors.report(
                        "The following types operation is not possible: " + leftType + " " + operator + " " + rightType,
                        node.getMetadata());
            }
            inferredType = BuiltinTypesCatalogue.atomicItem;
        }
        return new SequenceType(inferredType, inferredArity);
    }

    // Evaluate each possible operand pair. A null result means an unsupported pair.
    private ItemType inferBinaryOperationType(
            ItemType left, ItemType right, BiFunction<ItemType, ItemType, ItemType> operation) {
        List<ItemType> results = new ArrayList<>();
        for (ItemType leftMember : left.getMemberTypes()) {
            for (ItemType rightMember : right.getMemberTypes()) {
                ItemType result = inferMemberOperationType(leftMember, rightMember, operation);
                if (result == null) {
                    return null;
                }
                results.add(result);
            }
        }
        return ItemTypeFactory.createInferredUnionType(results);
    }

    private ItemType inferMemberOperationType(
            ItemType left, ItemType right, BiFunction<ItemType, ItemType, ItemType> operation) {
        if (!TypeAtomization.isAtomicOrNode(left) || !TypeAtomization.isAtomicOrNode(right)) {
            return null;
        }
        if (!TypeAtomization.hasUnknownTypedValue(left)
                && !TypeAtomization.hasUnknownTypedValue(right)
                && (left.equals(BuiltinTypesCatalogue.atomicItem) || right.equals(BuiltinTypesCatalogue.atomicItem))) {
            return null; // Retain strict checks for declared, unrefined atomic operands.
        }
        return operation.apply(normalizeArithmeticItemType(left), normalizeArithmeticItemType(right));
    }

    private ItemType normalizeArithmeticItemType(ItemType type) {
        ItemType atomized = TypeAtomization.atomizedItemType(type);
        return atomized.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)
                ? BuiltinTypesCatalogue.doubleItem
                : atomized;
    }

    private ItemType inferAdditiveItemType(ItemType leftItemType, ItemType rightItemType, boolean minus) {
        if (leftItemType.equals(BuiltinTypesCatalogue.atomicItem)
                || rightItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            // An unknown node typed value leaves successful arithmetic results atomic.
            return BuiltinTypesCatalogue.atomicItem;
        }
        leftItemType = leftItemType.getCastingPrimitiveType();
        rightItemType = rightItemType.getCastingPrimitiveType();
        ItemType result = null;
        // check item type combination
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                result = resolveNumericType(leftItemType, rightItemType);
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.dateItem)
                || leftItemType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            if (rightItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                    || rightItemType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                result = leftItemType;
            } else if (minus && rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.timeItem)) {
            if (rightItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                result = leftItemType;
            } else if (minus && rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.dayTimeDurationItem;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                result = leftItemType;
            } else if (!minus
                    && (rightItemType.equals(BuiltinTypesCatalogue.dateTimeItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.dateItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.timeItem))) {
                result = rightItemType;
            }
        } else if (leftItemType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            if (rightItemType.equals(leftItemType)) {
                result = leftItemType;
            } else if (!minus
                    && (rightItemType.equals(BuiltinTypesCatalogue.dateTimeItem)
                            || rightItemType.equals(BuiltinTypesCatalogue.dateItem))) {
                result = rightItemType;
            }
        }

        return result;
    }

    private ItemType inferMultiplicativeItemType(
            ItemType leftItemType, ItemType rightItemType, MultiplicativeExpression.MultiplicativeOperator operator) {
        if (leftItemType.equals(BuiltinTypesCatalogue.atomicItem)
                || rightItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            return operator == MultiplicativeExpression.MultiplicativeOperator.IDIV
                    ? BuiltinTypesCatalogue.integerItem
                    : BuiltinTypesCatalogue.atomicItem;
        }
        leftItemType = leftItemType.getCastingPrimitiveType();
        rightItemType = rightItemType.getCastingPrimitiveType();
        ItemType result = null;
        // check resulting item for each operation
        if (leftItemType.isNumeric()) {
            if (rightItemType.isNumeric()) {
                if (operator == MultiplicativeExpression.MultiplicativeOperator.IDIV) {
                    result = BuiltinTypesCatalogue.integerItem;
                } else if (operator == MultiplicativeExpression.MultiplicativeOperator.DIV) {
                    result = resolveNumericType(
                            BuiltinTypesCatalogue.decimalItem, resolveNumericType(leftItemType, rightItemType));
                } else {
                    result = resolveNumericType(leftItemType, rightItemType);
                }
            } else if (rightItemType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                    && !rightItemType.equals(BuiltinTypesCatalogue.durationItem)
                    && operator == MultiplicativeExpression.MultiplicativeOperator.MUL) {
                result = rightItemType;
            }
        } else if (leftItemType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)
                && !leftItemType.equals(BuiltinTypesCatalogue.durationItem)) {
            if (rightItemType.isNumeric()
                    && (operator == MultiplicativeExpression.MultiplicativeOperator.MUL
                            || operator == MultiplicativeExpression.MultiplicativeOperator.DIV)) {
                result = leftItemType;
            } else if (rightItemType.equals(leftItemType)) {
                result = BuiltinTypesCatalogue.decimalItem;
            }
        }

        return result;
    }

    // Both operands are concrete numeric types.
    private ItemType resolveNumericType(ItemType left, ItemType right) {
        List<ItemType> leftMembers = left.getMemberTypes();
        List<ItemType> rightMembers = right.getMemberTypes();
        if (leftMembers.size() > 1 || rightMembers.size() > 1) {
            // Each member pair is promoted separately, e.g. (xs:integer | xs:double) + xs:integer
            // is (xs:integer | xs:double).
            List<ItemType> results = new ArrayList<>();
            for (ItemType leftMember : leftMembers) {
                for (ItemType rightMember : rightMembers) {
                    results.add(resolveNumericType(leftMember, rightMember));
                }
            }
            return ItemTypeFactory.createInferredUnionType(results);
        }
        if (left.equals(BuiltinTypesCatalogue.doubleItem) || right.equals(BuiltinTypesCatalogue.doubleItem)) {
            return BuiltinTypesCatalogue.doubleItem;
        } else if (left.equals(BuiltinTypesCatalogue.floatItem) || right.equals(BuiltinTypesCatalogue.floatItem)) {
            return BuiltinTypesCatalogue.floatItem;
        } else if (left.equals(BuiltinTypesCatalogue.decimalItem) || right.equals(BuiltinTypesCatalogue.decimalItem)) {
            return BuiltinTypesCatalogue.decimalItem;
        } else {
            return BuiltinTypesCatalogue.integerItem;
        }
    }

    public SequenceType inferUnaryType(SequenceType childInferredType, Node expression) {

        // if the child is the empty sequence just infer the empty sequence
        this.errors.rejectInferredEmpty(childInferredType, expression);

        SequenceType.Arity resultArity = AtomizedOperands.atomizedSingletonArity(childInferredType);
        if (resultArity == null) {
            this.errors.report("'+' and '*' arities are not allowed for unary expressions", expression.getMetadata());
            resultArity = SequenceType.Arity.OneOrZero;
        }

        // if inferred arity does not allow for empty sequence and static type is not an accepted one throw a static
        // error
        ItemType childItemType = inferUnaryItemType(childInferredType.getItemType());
        if (childItemType == null) {
            if (childInferredType.getArity() == SequenceType.Arity.OneOrZero) {
                this.errors.reportInferredEmpty(expression);
            } else {
                this.errors.report(
                        "It is not possible to have an Unary expression with the following type: " + childInferredType,
                        expression.getMetadata());
            }
            childItemType = BuiltinTypesCatalogue.numericItem;
        }

        return new SequenceType(childItemType, resultArity);
    }

    private ItemType inferUnaryItemType(ItemType type) {
        List<ItemType> results = new ArrayList<>();
        for (ItemType member : type.getMemberTypes()) {
            if (!TypeAtomization.isAtomicOrNode(member)) {
                return null;
            }
            if (TypeAtomization.hasUnknownTypedValue(member)) {
                results.add(BuiltinTypesCatalogue.numericItem);
                continue;
            }
            ItemType normalized = normalizeArithmeticItemType(member);
            if (!normalized.isNumeric()) {
                return null;
            }
            results.add(normalized);
        }
        return ItemTypeFactory.createInferredUnionType(results);
    }
}
