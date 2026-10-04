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
        if (type.isUnionType()) {
            return type.getTypes().stream().allMatch(TypeAtomization::isAtomicOrNode);
        }
        return type.isSubtypeOf(BuiltinTypesCatalogue.atomicItem) || type.isSubtypeOf(BuiltinTypesCatalogue.nodeItem);
    }

    public static boolean containsNode(ItemType type) {
        if (type.isUnionType()) {
            return type.getTypes().stream().anyMatch(TypeAtomization::containsNode);
        }
        return type.isSubtypeOf(BuiltinTypesCatalogue.nodeItem);
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
            List<ItemType> alternatives = new ArrayList<>();
            SequenceCardinality cardinality = null;
            for (ItemType member : type.getTypes()) {
                SequenceType atomized = inferItemType(member);
                alternatives.add(atomized.getItemType());
                cardinality =
                        cardinality == null ? atomized.getCardinality() : cardinality.union(atomized.getCardinality());
            }
            return new SequenceType(ItemTypeFactory.createInferredUnionType(alternatives), cardinality);
        }
        if (type.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            return new SequenceType(type);
        }
        // Node types do not currently carry inferred typed-value information.
        // Atomization may produce nothing, one value, a schema list, or an error.
        // Other item kinds also retain this conservative bound on successful atomization.
        return new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceCardinality.ANY);
    }
}
