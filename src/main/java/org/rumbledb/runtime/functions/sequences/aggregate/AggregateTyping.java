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
package org.rumbledb.runtime.functions.sequences.aggregate;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.rumbledb.compiler.typing.ComparisonTyping;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Static typing shared by the return type rules of {@code sum()}, {@code avg()}, {@code min()} and {@code max()}. */
final class AggregateTyping {

    private AggregateTyping() {}

    /**
     * Checks that the input of fn:sum or fn:avg holds numbers, or durations of one kind, and returns its normalized
     * type.
     */
    static SequenceType validateStrictInput(
            FunctionCallExpression call, ReturnTypeContext context, String functionName) {
        SequenceType inputType = context.argumentType(0);
        if (inputType.isEmptySequence()) {
            return inputType;
        }

        ItemType inputItemType = normalize(inputType.getItemType());
        // The atomized values of nodes and arrays may only be known at runtime, so such inputs are checked per member
        // type.
        boolean validInput = TypeAtomization.containsNodeOrArray(inputType.getItemType())
                ? inputType
                        .getItemType()
                        .allMemberTypesMatch(member -> TypeAtomization.hasUnknownAtomizedType(member)
                                || isStrictAggregateItemType(normalize(member)))
                : isStrictAggregateItemType(inputItemType);
        if (!validInput) {
            context.errors()
                    .report(
                            functionName
                                    + " expects numbers, or durations of one kind, but its input is "
                                    + inputType
                                    + ".",
                            ErrorCode.InvalidArgumentType,
                            call,
                            mayAggregate(inputType, AggregateTyping::isStrictAggregateItemType));
        }

        return new SequenceType(
                inputItemType, TypeAtomization.inferType(inputType).getCardinality());
    }

    /** The return type of fn:min or fn:max, whose input must hold values that can be compared with each other. */
    static SequenceType inferExtremumType(FunctionCallExpression call, ReturnTypeContext context, String functionName) {
        SequenceType inputType = context.argumentType(0);
        if (inputType.isEmptySequence()) {
            return SequenceType.createSequenceType("anyAtomicType?");
        }

        // Untyped values are compared as xs:double. The atomized values of nodes and arrays are compared at runtime.
        ItemType inputItemType = normalize(inputType.getItemType());
        boolean comparedAtRuntime = TypeAtomization.containsNodeOrArray(inputType.getItemType())
                && TypeAtomization.isAtomizable(inputType.getItemType());
        if (!comparedAtRuntime && !ComparisonTyping.hasMutuallyComparableItems(inputItemType)) {
            context.errors()
                    .report(
                            functionName
                                    + " expects values that can be compared with each other, but its input is "
                                    + inputType
                                    + ".",
                            ErrorCode.InvalidArgumentType,
                            call,
                            mayAggregate(inputType, ComparisonTyping::hasMutuallyComparableItems));
        }

        return new SequenceType(
                inputItemType,
                TypeAtomization.inferType(inputType).getCardinality().atMostOne());
    }

    /**
     * Whether an aggregate may succeed: the input may be empty, or some of its member types may be accepted, once
     * normalized, or only be known at runtime.
     */
    private static boolean mayAggregate(SequenceType inputType, Predicate<ItemType> acceptsNormalized) {
        return inputType.getCardinality().allowsZero()
                || inputType.getItemType().getMemberTypes().stream()
                        .anyMatch(member -> TypeAtomization.mayAtomizeToAnyType(member)
                                || acceptsNormalized.test(normalize(member)));
    }

    private static boolean isStrictAggregateItemType(ItemType type) {
        return type.isSubtypeOf(BuiltinTypesCatalogue.numericItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem);
    }

    // Aggregates can convert untyped values and produce values outside a derived type's restrictions.
    private static ItemType normalize(ItemType type) {
        if (type.isUnionType()) {
            return ItemTypeFactory.createInferredUnionType(type.getMemberTypes().stream()
                    .map(AggregateTyping::normalize)
                    .collect(Collectors.toList()));
        }
        if (TypeAtomization.containsNodeOrArray(type)) {
            type = TypeAtomization.atomizedItemType(type);
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)) {
            return BuiltinTypesCatalogue.doubleItem;
        }
        if (type.isNumeric()
                || type.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            return type.getCastingPrimitiveType();
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return BuiltinTypesCatalogue.stringItem;
        }
        return type;
    }
}
