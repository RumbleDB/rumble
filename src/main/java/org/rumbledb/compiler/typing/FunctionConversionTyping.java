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

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Function-conversion compatibility for arguments and declared return values. */
public final class FunctionConversionTyping {
    private FunctionConversionTyping() {}

    /**
     * Whether some value of the actual type passes function conversion to the expected type, which atomizes nodes
     * and arrays and casts untyped values when an atomic type is expected.
     */
    public static boolean mayConvertTo(SequenceType actual, SequenceType expected) {
        SequenceType converted = actual;
        if (expected.getItemType().isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            converted = TypeAtomization.containsNodeOrArray(actual.getItemType())
                    ? TypeAtomization.inferType(actual)
                    : actual;
            SequenceType untyped = new SequenceType(BuiltinTypesCatalogue.untypedAtomicItem, expected.getCardinality());
            if (converted.mayMatch(untyped)) {
                return true;
            }
        }
        return converted.mayMatchOrBePromotedTo(expected);
    }

    /** Whether static typing accepts the conversion; unknown typed values are checked at runtime. */
    public static boolean isCompatible(SequenceType actual, SequenceType expected) {
        if (actual.isEmptySequence()) {
            return actual.isSubtypeOfOrCanBePromotedTo(expected);
        }
        ItemType itemType = actual.getItemType();
        if (itemType.isUnionType()) {
            return itemType.allMemberTypesMatch(
                    member -> isCompatible(new SequenceType(member, actual.getCardinality()), expected));
        }
        if (expected.getItemType().isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            if (TypeAtomization.hasUnknownAtomizedType(itemType)) {
                // Runtime function conversion checks the type and size of the typed value, just as for casts.
                return true;
            }
            if (TypeAtomization.hasUnknownAtomizedCount(itemType)) {
                // An array atomizes to any number of values: only their type is checked, their count at runtime.
                SequenceType atomized = TypeAtomization.inferType(actual);
                return atomized.isEmptySequence()
                        || isCompatible(new SequenceType(atomized.getItemType(), expected.getCardinality()), expected);
            }
            if (TypeAtomization.containsNodeOrArray(itemType)) {
                // Function conversion atomizes the node, so its known typed value must match.
                return isCompatible(TypeAtomization.inferType(actual), expected);
            }
            if (itemType.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return actual.getCardinality().isSubtypeOf(expected.getCardinality());
            }
        }
        return actual.isSubtypeOfOrCanBePromotedTo(expected);
    }
}
