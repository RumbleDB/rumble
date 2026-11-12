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

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.RumbleConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.typing.TypeInferrenceUtils;

class DataFrameSchemaTypeTest {
    @Test
    void nullableNumericJoinMatchesTheNonNullJoinInEveryOrderAndGrouping() {
        List<ItemType> types = List.of(
                BuiltinTypesCatalogue.integerItem,
                BuiltinTypesCatalogue.decimalItem,
                BuiltinTypesCatalogue.floatItem,
                BuiltinTypesCatalogue.doubleItem);
        ItemType nil = BuiltinTypesCatalogue.nullItem;
        for (ItemType a : types) {
            for (ItemType b : types) {
                ItemType expected = a.findLeastCommonSuperTypeWith(b);
                List<ItemType> operands = List.of(a, b, nil);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (i == j) continue;
                        ItemType x = operands.get(i);
                        ItemType y = operands.get(j);
                        ItemType z = operands.get(3 - i - j);
                        assertNullable(expected, DataFrameSchemaType.merge(DataFrameSchemaType.merge(x, y), z));
                        assertNullable(expected, DataFrameSchemaType.merge(x, DataFrameSchemaType.merge(y, z)));
                    }
                }
            }
        }
    }

    private void assertNullable(ItemType expected, ItemType actual) {
        assertTrue(actual.canBeNull());
        assertEquals(expected, actual.getSingleNullableType());
        assertTrue(actual.isCompatibleWithDataFrames(new org.rumbledb.config.RumbleConfiguration()));
    }

    @Test
    void runtimeSchemaMergingDoesNotChangeStaticOrLaxJoins() {
        var sequence = new Rumble(new RumbleConfiguration()).runQuery("({\"n\":1},{\"n\":null},{\"n\":2.5e0})");
        ItemType before = sequence.getRuntimeStaticContext().getStaticType().getItemType();
        List<Item> items = sequence.getAsList();
        ItemType laxBefore = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items, ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.LAX);
        assertEquals(
                3, laxBefore.getObjectContentFacet("n").getType().getTypes().size());
        ItemType schema = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items, ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.DATAFRAME);
        assertNullable(
                BuiltinTypesCatalogue.numericItem,
                schema.getObjectContentFacet("n").getType());
        ItemType laxAfter = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items, ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.LAX);
        assertEquals(laxBefore.getIdentifierString(), laxAfter.getIdentifierString());
        assertSame(before, sequence.getRuntimeStaticContext().getStaticType().getItemType());
        assertEquals(3, before.getObjectContentFacet("n").getType().getTypes().size());
    }

    @Test
    void detectsNestedArrayAndObjectSchemasWithoutLosingOriginalNumericTypes() {
        List<Item> items = new Rumble(new RumbleConfiguration())
                .runQuery("({\"a\":[{\"n\":1},{\"n\":null}]},{\"a\":[{\"n\":2.5e0}]})")
                .getAsList();
        ItemType schema = TypeInferrenceUtils.inferItemTypeOfLocalItems(
                items, ExceptionMetadata.EMPTY_METADATA, TypeInferrenceUtils.TypeMergeMode.DATAFRAME);
        ItemType field = schema.getObjectContentFacet("a")
                .getType()
                .getArrayContentFacet()
                .getObjectContentFacet("n")
                .getType();
        assertNullable(BuiltinTypesCatalogue.numericItem, field);
        Item array = new Rumble(new RumbleConfiguration())
                .runQuery("[1,null,2.5e0]")
                .getAsList()
                .get(0);
        assertNullable(
                BuiltinTypesCatalogue.numericItem,
                DataFrameSchemaType.fromItem(array).getArrayContentFacet());
    }

    @Test
    void doesNotMergeNumericAndNonnumericAlternativesAsNumeric() {
        ItemType nullable =
                DataFrameSchemaType.merge(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.nullItem);
        ItemType mixed = DataFrameSchemaType.merge(nullable, BuiltinTypesCatalogue.stringItem);
        assertFalse(mixed.isNumeric());
        assertNull(mixed.getSingleNullableType());
        ItemType empty = DataFrameSchemaType.fromItem(ItemFactory.getInstance().createArrayItem(List.of(), true));
        ItemType numbers = ItemTypeFactory.createAnonymousArrayType(BuiltinTypesCatalogue.integerItem);
        assertEquals(
                BuiltinTypesCatalogue.integerItem,
                DataFrameSchemaType.merge(empty, numbers).getArrayContentFacet());
    }
}
