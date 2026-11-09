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
package org.rumbledb.types;

import java.util.ArrayList;
import java.util.List;

/** Static counterparts of atomization, without changing the type of the original operand. */
public final class TypeAtomization {
    private TypeAtomization() {}

    /**
     * Whether every item of this type can be atomized: atomic values, nodes, and arrays, which atomize to the atomized
     * values of their members. Objects, maps and other function items cannot (FOTY0013).
     */
    public static boolean isAtomizable(ItemType type) {
        return type.allMemberTypesMatch(member -> member.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)
                || member.isSubtypeOf(BuiltinTypesCatalogue.nodeItem)
                || (isArray(member) && isAtomizable(arrayMemberType(member))));
    }

    /**
     * Whether any member is a node or an array, which atomization replaces with other values: a node's typed value, an
     * array's atomized members. The error type is both a node and atomic, and atomizes to itself.
     */
    public static boolean containsNodeOrArray(ItemType type) {
        return type.getMemberTypes().stream()
                .anyMatch(member -> (member.isSubtypeOf(BuiltinTypesCatalogue.nodeItem) || isArray(member))
                        && !member.isSubtypeOf(BuiltinTypesCatalogue.atomicItem));
    }

    /**
     * Whether atomizing this type yields values whose type is only known at runtime, such as a node's typed value or
     * the members of an array of any items. Checks on such values are left to runtime.
     */
    public static boolean hasUnknownAtomizedType(ItemType type) {
        return containsNodeOrArray(type) && atomizedItemType(type).equals(BuiltinTypesCatalogue.atomicItem);
    }

    /**
     * Whether atomizing any item of this type fails (FOTY0013): it is only objects, maps, other function items, or
     * arrays of them.
     */
    public static boolean isNeverAtomizable(ItemType type) {
        return type.allMemberTypesMatch(member -> isArray(member)
                ? !arrayMemberType(member).isEmptySequence()
                        && isNeverAtomizable(arrayMemberType(member).getItemType())
                : member.isObjectItemType() || member.isMapItemType() || member.isFunctionItemType());
    }

    /**
     * Whether the number of values that atomizing this type yields is only known at runtime: an array has any number of
     * members, and a node's unknown typed value any number of values. Cardinality checks on them are left to runtime.
     */
    public static boolean hasUnknownAtomizedCount(ItemType type) {
        return hasUnknownAtomizedType(type) || type.getMemberTypes().stream().anyMatch(TypeAtomization::isArray);
    }

    private static boolean isArray(ItemType type) {
        return type.isArrayItemType() || type.isXQueryArrayItemType();
    }

    private static boolean isAtomizable(SequenceType type) {
        return type.isEmptySequence() || isAtomizable(type.getItemType());
    }

    /** The type of one member of an array: a single item in a JSONiq array, a sequence in an XQuery array. */
    private static SequenceType arrayMemberType(ItemType array) {
        return array.isXQueryArrayItemType()
                ? array.getMemberSequenceType()
                : new SequenceType(array.getArrayContentFacet());
    }

    /** Returns the type of the values that atomizing one item of this type produces. */
    public static ItemType atomizedItemType(ItemType type) {
        return inferItemType(type).getItemType();
    }

    public static SequenceType inferType(SequenceType source) {
        if (source.isEmptySequence()) {
            return source;
        }
        SequenceType perItem = inferItemType(source.getItemType());
        // Each source item atomizes independently.
        return new SequenceType(perItem.getItemType(), perItem.getCardinality().repeated(source.getCardinality()));
    }

    private static SequenceType inferItemType(ItemType type) {
        if (type.isUnionType()) {
            return inferAlternativesType(type.getMemberTypes());
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            return new SequenceType(type);
        }
        if (isArray(type)) {
            // An array atomizes to the atomized values of its members, of which it has any number.
            SequenceType member = inferType(arrayMemberType(type));
            ItemType memberItemType =
                    member.isEmptySequence() ? BuiltinTypesCatalogue.atomicItem : member.getItemType();
            SequenceCardinality memberCardinality =
                    member.isEmptySequence() ? SequenceCardinality.EMPTY : member.getCardinality();
            return new SequenceType(memberItemType, memberCardinality.repeated(SequenceCardinality.ANY));
        }
        SequenceType typedValue = type.getTypedValueType();
        if (typedValue != null) {
            return typedValue;
        }
        // Other nodes may produce nothing, one value, a schema list, or an error.
        // Other item kinds also retain this conservative bound on successful atomization.
        return new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceCardinality.ANY);
    }

    /** Atomizes a value that may have any of these types. */
    static SequenceType inferAlternativesType(List<? extends ItemType> types) {
        List<ItemType> alternatives = new ArrayList<>();
        SequenceCardinality cardinality = null;
        for (ItemType member : types) {
            SequenceType atomized = inferItemType(member);
            alternatives.add(atomized.getItemType());
            cardinality =
                    cardinality == null ? atomized.getCardinality() : cardinality.union(atomized.getCardinality());
        }
        return new SequenceType(ItemTypeFactory.createInferredUnionType(alternatives), cardinality);
    }
}
