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

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.Name;

class ItemTypeFactoryTest {
    @Test
    void inferredUnionsFlattenAndRemoveSubsumedAlternatives() {
        ItemType numbers = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.decimalItem));
        assertSame(BuiltinTypesCatalogue.decimalItem, numbers);
        ItemType union = ItemTypeFactory.createInferredUnionType(List.of(BuiltinTypesCatalogue.booleanItem, numbers));
        ItemType normalized = ItemTypeFactory.createInferredUnionType(
                List.of(union, BuiltinTypesCatalogue.booleanItem, BuiltinTypesCatalogue.intItem));
        assertEquals(
                List.of(BuiltinTypesCatalogue.booleanItem, BuiltinTypesCatalogue.decimalItem), normalized.getTypes());
        assertSame(
                BuiltinTypesCatalogue.item,
                ItemTypeFactory.createInferredUnionType(List.of(union, BuiltinTypesCatalogue.item)));
    }

    @Test
    void namedNullableSchemaUnionRetainsItsIdentity() {
        ItemType named = new UnionItemType(
                Name.createVariableInDefaultTypeNamespace("namedNullable"),
                BuiltinTypesCatalogue.atomicItem,
                List.of(BuiltinTypesCatalogue.doubleItem, BuiltinTypesCatalogue.nullItem),
                true);
        assertSame(named, ItemTypeFactory.createInferredUnionType(List.of(named, BuiltinTypesCatalogue.nullItem)));
        assertSame(named, ItemTypeFactory.createObjectFieldType(new SequenceType(named, SequenceType.Arity.OneOrZero)));
    }
}
