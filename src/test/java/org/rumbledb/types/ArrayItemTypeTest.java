package org.rumbledb.types;

import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.api.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArrayItemTypeTest {

    /**
     * Ensures that lax array merging reuses the object-type lax logic for element content,
     * so arrays of objects retain the union of their nested fields with proper required/unique semantics.
     */
    @Test
    public void laxMergeCombinesNestedObjectContent() {
        ObjectItemType leftObject = createObjectType(
            true,
            field("a", BuiltinTypesCatalogue.intItem, true, false),
            field("common", BuiltinTypesCatalogue.intItem, true, false)
        );
        ObjectItemType rightObject = createObjectType(
            true,
            field("b", BuiltinTypesCatalogue.stringItem, false, true),
            field("common", BuiltinTypesCatalogue.stringItem, false, true)
        );
        ArrayItemType left = createArrayType(leftObject, null, null);
        ArrayItemType right = createArrayType(rightObject, null, null);

        ItemType mergedType = left.findLeastCommonSuperTypeLax(right);
        Assert.assertTrue(mergedType instanceof ArrayItemType);
        ArrayItemType mergedArray = (ArrayItemType) mergedType;
        Assert.assertTrue(mergedArray.getArrayContentFacet() instanceof ObjectItemType);
        ObjectItemType mergedContent = (ObjectItemType) mergedArray.getArrayContentFacet();
        Map<String, FieldDescriptor> fields = mergedContent.getObjectContentFacet();
        Assert.assertEquals(3, fields.size());
        Assert.assertTrue(fields.containsKey("a"));
        Assert.assertTrue(fields.containsKey("b"));
        FieldDescriptor common = fields.get("common");
        Assert.assertNotNull(common);
        Assert.assertFalse("Common field should become optional if an operand is optional.", common.isRequired());
        Assert.assertTrue("Common field should be unique if any operand is unique.", common.isUnique());
    }

    /**
     * Verifies that lax merging propagates min/max length facets conservatively and drops them when incompatible.
     */
    @Test
    public void laxMergeRespectsLengthFacets() {
        ArrayItemType left = createArrayType(BuiltinTypesCatalogue.intItem, 1, 5);
        ArrayItemType right = createArrayType(BuiltinTypesCatalogue.intItem, 2, 4);
        ArrayItemType merged = (ArrayItemType) left.findLeastCommonSuperTypeLax(right);
        Assert.assertEquals(Integer.valueOf(2), merged.getMinLengthFacet());
        Assert.assertEquals(Integer.valueOf(4), merged.getMaxLengthFacet());

        ArrayItemType conflictingLeft = createArrayType(BuiltinTypesCatalogue.intItem, 5, 6);
        ArrayItemType conflictingRight = createArrayType(BuiltinTypesCatalogue.intItem, 1, 2);
        ArrayItemType conflictingMerged = (ArrayItemType) conflictingLeft.findLeastCommonSuperTypeLax(conflictingRight);
        Assert.assertNull(conflictingMerged.getMinLengthFacet());
        Assert.assertNull(conflictingMerged.getMaxLengthFacet());
    }

    /**
     * Ensures that lax merging falls back to the strict common supertype when the other operand is not an array.
     */
    @Test
    public void laxMergeFallsBackForNonArrayTypes() {
        ArrayItemType left = createArrayType(BuiltinTypesCatalogue.intItem, null, null);
        ItemType strict = left.findLeastCommonSuperTypeWith(BuiltinTypesCatalogue.objectItem);
        ItemType lax = left.findLeastCommonSuperTypeLax(BuiltinTypesCatalogue.objectItem);
        Assert.assertEquals(strict, lax);
    }

    private ArrayItemType createArrayType(ItemType content, Integer minLength, Integer maxLength) {
        return new ArrayItemType(
                null,
                BuiltinTypesCatalogue.arrayItem,
                content,
                minLength,
                maxLength,
                null
        );
    }

    private ObjectItemType createObjectType(boolean closed, FieldDescriptor... descriptors) {
        Map<String, FieldDescriptor> content = new LinkedHashMap<>();
        for (FieldDescriptor descriptor : descriptors) {
            content.put(descriptor.getName(), descriptor);
        }
        return new ObjectItemType(
                null,
                BuiltinTypesCatalogue.objectItem,
                closed,
                content,
                Collections.<String>emptyList(),
                Collections.<Item>emptyList()
        );
    }

    private FieldDescriptor field(String name, ItemType type, boolean required, boolean unique) {
        FieldDescriptor descriptor = new FieldDescriptor();
        descriptor.setName(name);
        descriptor.setType(type);
        descriptor.setRequired(required);
        descriptor.setUnique(unique);
        return descriptor;
    }
}

