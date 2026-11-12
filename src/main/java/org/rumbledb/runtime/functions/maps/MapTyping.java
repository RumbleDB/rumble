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
package org.rumbledb.runtime.functions.maps;

import java.util.Optional;

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/**
 * Static typing shared by the return type rules of the map functions, which keep the key and value types of the maps
 * they build where their signatures say map(*).
 */
final class MapTyping {

    private MapTyping() {}

    /** The type of the map passed as the first argument, when it is one map with known key and value types. */
    static Optional<ItemType> typedMapArgument(SequenceType type) {
        return type.getArity() == SequenceType.Arity.One
                        && type.getItemType().isMapItemType()
                        && !type.getItemType().equals(BuiltinTypesCatalogue.mapItem)
                ? Optional.of(type.getItemType())
                : Optional.empty();
    }

    /** The type of a key argument, which is atomized. A key that is always empty makes the call fail. */
    static Optional<ItemType> atomizedKeyType(SequenceType type) {
        return type.isEmptySequence()
                ? Optional.empty()
                : Optional.of(TypeAtomization.atomizedItemType(type.getItemType()));
    }

    static SequenceType mapType(ItemType keyType, SequenceType valueType) {
        return new SequenceType(ItemTypeFactory.mapOf(keyType, valueType));
    }
}
