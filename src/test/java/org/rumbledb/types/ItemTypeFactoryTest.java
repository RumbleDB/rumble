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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;

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
    void inferredUnionsFlattenNullableJoins() {
        ItemType nullableInteger =
                BuiltinTypesCatalogue.integerItem.findLeastCommonSuperTypeWith(BuiltinTypesCatalogue.nullItem);
        ItemType union =
                ItemTypeFactory.createInferredUnionType(List.of(nullableInteger, BuiltinTypesCatalogue.stringItem));
        assertEquals(
                List.of(
                        BuiltinTypesCatalogue.integerItem,
                        BuiltinTypesCatalogue.nullItem,
                        BuiltinTypesCatalogue.stringItem),
                union.getTypes());
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

    @Test
    void integerRangeDetectionIsOptionalAndDoesNotChangeDynamicTypes() {
        var factory = ItemFactory.getInstance();
        List<String> values = List.of(
                "-2147483648",
                "2147483647",
                "-2147483649",
                "2147483648",
                "-9223372036854775808",
                "9223372036854775807",
                "-9223372036854775809",
                "9223372036854775808");
        List<ItemType> expected = List.of(
                BuiltinTypesCatalogue.intItem,
                BuiltinTypesCatalogue.intItem,
                BuiltinTypesCatalogue.longItem,
                BuiltinTypesCatalogue.longItem,
                BuiltinTypesCatalogue.longItem,
                BuiltinTypesCatalogue.longItem,
                BuiltinTypesCatalogue.integerItem,
                BuiltinTypesCatalogue.integerItem);
        for (int i = 0; i < values.size(); i++) {
            var item = factory.createIntegerItem(new BigInteger(values.get(i)));
            assertEquals(BuiltinTypesCatalogue.integerItem, ItemTypeFactory.createItemTypeFromItem(item));
            assertEquals(expected.get(i), ItemTypeFactory.createItemTypeFromItem(item, true));
            assertEquals(BuiltinTypesCatalogue.integerItem, item.getDynamicType());
            assertEquals(
                    BuiltinTypesCatalogue.integerItem,
                    TypeInferrenceUtils.inferItemTypeOfLocalItems(
                            List.of(item), ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.STRICT));
        }
        var decimal = factory.createDecimalItem(new BigDecimal("1.0"));
        assertEquals(BuiltinTypesCatalogue.decimalItem, ItemTypeFactory.createItemTypeFromItem(decimal, true));
    }
}
