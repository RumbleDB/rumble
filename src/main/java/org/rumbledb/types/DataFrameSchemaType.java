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
import java.util.Arrays;
import java.util.List;

import org.rumbledb.api.Item;

/** Runtime schema detection only: nullable numeric joins must not change static type joins. */
public final class DataFrameSchemaType {
    private DataFrameSchemaType() {}

    public static ItemType fromItem(Item item) {
        if (item.isObject()) {
            List<ItemType> fields = new ArrayList<>();
            for (String key : item.getStringKeys()) {
                fields.add(fromItem(item.getItemByKey(key)));
            }
            return ItemTypeFactory.createAnonymousObjectType(item.getStringKeys(), fields);
        }
        if (item.isArrayOfItems()) {
            if (item.getSize() == 0) {
                return ItemTypeFactory.createEmptyArrayType();
            }
            ItemType content = fromItem(item.getItemAt(0));
            for (int i = 1; i < item.getSize(); i++) {
                content = merge(content, fromItem(item.getItemAt(i)));
            }
            return ItemTypeFactory.createAnonymousArrayType(content);
        }
        return ItemTypeFactory.createItemTypeFromItem(item);
    }

    public static ItemType merge(ItemType left, ItemType right) {
        List<ItemType> members = new ArrayList<>();
        if (collectNumericAlternatives(left, members) && collectNumericAlternatives(right, members)) {
            ItemType numeric = null;
            boolean nullable = false;
            for (ItemType member : members) {
                if (member.equals(BuiltinTypesCatalogue.nullItem)) {
                    nullable = true;
                } else if (numeric == null) {
                    numeric = member;
                } else {
                    numeric = numeric.findLeastCommonSuperTypeWith(member);
                }
            }
            if (numeric == null) {
                return BuiltinTypesCatalogue.nullItem;
            }
            return nullable
                    ? new UnionItemType(
                            null,
                            BuiltinTypesCatalogue.atomicItem,
                            Arrays.asList(numeric, BuiltinTypesCatalogue.nullItem),
                            false)
                    : numeric;
        }
        ItemType joined = left.findLeastCommonSuperTypeLax(right);
        if (left instanceof ObjectItemType && right instanceof ObjectItemType && joined instanceof ObjectItemType) {
            List<FieldDescriptor> fields = new ArrayList<>();
            for (FieldDescriptor field : joined.getObjectContentFacet()) {
                FieldDescriptor copy = FieldDescriptor.copy(field);
                FieldDescriptor a = left.getObjectContentFacet(field.getName());
                FieldDescriptor b = right.getObjectContentFacet(field.getName());
                if (a != null && b != null) {
                    copy.setType(merge(a.getType(), b.getType()));
                }
                fields.add(copy);
            }
            return new ObjectItemType(
                    null,
                    BuiltinTypesCatalogue.objectItem,
                    joined.getClosedFacet(),
                    joined.getObjectKeysFacet(),
                    fields,
                    joined.getConstraintsFacet(),
                    joined.getEnumerationFacet());
        }
        if (left instanceof ArrayItemType && right instanceof ArrayItemType && joined instanceof ArrayItemType) {
            ItemType content;
            if (Integer.valueOf(0).equals(left.getMaxLengthFacet())) {
                content = right.getArrayContentFacet();
            } else if (Integer.valueOf(0).equals(right.getMaxLengthFacet())) {
                content = left.getArrayContentFacet();
            } else {
                content = merge(left.getArrayContentFacet(), right.getArrayContentFacet());
            }
            return new ArrayItemType(
                    null,
                    BuiltinTypesCatalogue.arrayItem,
                    content,
                    joined.getMinLengthFacet(),
                    joined.getMaxLengthFacet(),
                    joined.getEnumerationFacet());
        }
        return joined;
    }

    private static boolean collectNumericAlternatives(ItemType type, List<ItemType> members) {
        if (type.isUnionType()) {
            for (ItemType member : type.getTypes()) {
                if (!collectNumericAlternatives(member, members)) {
                    return false;
                }
            }
            return true;
        }
        if (type.isNumeric() || type.equals(BuiltinTypesCatalogue.nullItem)) {
            members.add(type);
            return true;
        }
        return false;
    }
}
