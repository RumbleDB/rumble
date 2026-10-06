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

    public static boolean isAtomicOrNode(ItemType type) {
        return type.allMemberTypesMatch(member -> member.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)
                || member.isSubtypeOf(BuiltinTypesCatalogue.nodeItem));
    }

    public static boolean containsNode(ItemType type) {
        return type.getMemberTypes().stream().anyMatch(member -> member.isSubtypeOf(BuiltinTypesCatalogue.nodeItem));
    }

    /**
     * Whether atomizing this type yields values whose type is only known at runtime, such as a node's typed value.
     * Checks on such values are left to runtime.
     */
    public static boolean hasUnknownTypedValue(ItemType type) {
        return containsNode(type) && atomizedItemType(type).equals(BuiltinTypesCatalogue.atomicItem);
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
        if (type instanceof SchemaElementNodeItemType schemaElement
                && !schemaElement.getAlternatives().isEmpty()) {
            return inferAlternativesType(schemaElement.getAlternatives());
        }
        if (type instanceof ElementNodeItemType element && element.getTypedValueType() != null) {
            SequenceType typedValue = element.getTypedValueType();
            // A nilled element has an empty typed value.
            return element.isNillable()
                    ? new SequenceType(
                            typedValue.getItemType(),
                            typedValue.getCardinality().union(SequenceCardinality.EMPTY))
                    : typedValue;
        }
        if (type instanceof AttributeNodeItemType attribute && attribute.getTypedValueType() != null) {
            return attribute.getTypedValueType();
        }
        // Other nodes may produce nothing, one value, a schema list, or an error.
        // Other item kinds also retain this conservative bound on successful atomization.
        return new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceCardinality.ANY);
    }

    private static SequenceType inferAlternativesType(List<? extends ItemType> types) {
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
