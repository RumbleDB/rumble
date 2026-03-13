package org.rumbledb.types;

import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectItemTypeTest {
    /**
     * Tests that the lax merge operation includes all fields from both object type operands.
     * Verifies that:
     * - Fields from both left and right operands are present in the merged result
     * - Field properties (required, unique) are preserved from their respective sources
     * - The closed facet is set to false (open) if at least one operand is open
     */
    @Test
    public void laxMergeIncludesAllFieldsFromBothOperands() {
        ObjectItemType left = createObjectType(
            true,
            field("first", BuiltinTypesCatalogue.intItem, true, false, null)
        );
        ObjectItemType right = createObjectType(
            false,
            field("second", BuiltinTypesCatalogue.stringItem, false, true, null)
        );

        ItemType mergedType = left.findLeastCommonSuperTypeLax(right);
        Assert.assertTrue(mergedType instanceof ObjectItemType);
        ObjectItemType mergedObject = (ObjectItemType) mergedType;
        Map<String, FieldDescriptor> content = mergedObject.getObjectContentFacet();
        Assert.assertEquals(2, content.size());
        Assert.assertTrue(content.get("first").isRequired());
        Assert.assertFalse(content.get("second").isRequired());
        Assert.assertTrue(content.get("second").isUnique());
        Assert.assertFalse("Merged object should be open if any operand is open.", mergedObject.getClosedFacet());
    }

    /**
     * Tests that overlapping fields between two object types are merged using lax supertype logic recursively.
     * Verifies that:
     * - When both operands define a field with the same name but different nested object types, the nested types are
     * merged recursively using findLeastCommonSuperTypeLax
     * - The merged nested object contains all fields from both nested types
     * - The required flag is set to true only if both operands require the field (AND semantics)
     * - The unique flag is set to true if either operand marks the field as unique (OR semantics)
     */
    @Test
    public void overlappingFieldsUseLaxSuperTypesRecursively() {
        ObjectItemType nestedLeft = createObjectType(
            true,
            field("a", BuiltinTypesCatalogue.intItem, true, false, null)
        );
        ObjectItemType nestedRight = createObjectType(
            true,
            field("b", BuiltinTypesCatalogue.stringItem, true, false, null)
        );
        ObjectItemType left = createObjectType(
            true,
            field("nested", nestedLeft, true, false, null)
        );
        ObjectItemType right = createObjectType(
            true,
            field("nested", nestedRight, false, true, null)
        );

        FieldDescriptor mergedDescriptor = ((ObjectItemType) left.findLeastCommonSuperTypeLax(right))
            .getObjectContentFacet()
            .get("nested");
        Assert.assertFalse(
            "Nested field should be optional if any operand is optional.",
            mergedDescriptor.isRequired()
        );
        Assert.assertTrue("Nested field should be unique if any operand is unique.", mergedDescriptor.isUnique());
        ItemType mergedNestedType = mergedDescriptor.getType();
        Assert.assertTrue(mergedNestedType instanceof ObjectItemType);
        Map<String, FieldDescriptor> innerContent = ((ObjectItemType) mergedNestedType).getObjectContentFacet();
        Assert.assertEquals(2, innerContent.size());
        Assert.assertTrue(innerContent.containsKey("a"));
        Assert.assertTrue(innerContent.containsKey("b"));
    }

    /**
     * Tests that default values are preserved in merged field descriptors only when they are compatible.
     * Verifies that:
     * - When both operands define the same default value for a field, it is preserved in the merged result
     * - When operands define conflicting default values, the default is discarded (set to null)
     */
    @Test
    public void defaultValuesArePreservedOnlyWhenCompatible() {
        Item defaultTrue = ItemFactory.getInstance().createBooleanItem(true);
        FieldDescriptor leftDescriptor = field(
            "flag",
            BuiltinTypesCatalogue.booleanItem,
            true,
            false,
            defaultTrue
        );
        FieldDescriptor rightDescriptor = field(
            "flag",
            BuiltinTypesCatalogue.booleanItem,
            true,
            false,
            ItemFactory.getInstance().createBooleanItem(true)
        );
        ObjectItemType left = createObjectType(true, leftDescriptor);
        ObjectItemType right = createObjectType(true, rightDescriptor);

        FieldDescriptor merged = ((ObjectItemType) left.findLeastCommonSuperTypeLax(right)).getObjectContentFacet()
            .get("flag");
        Assert.assertEquals(defaultTrue, merged.getDefaultValue());

        FieldDescriptor conflictingRight = field(
            "flag",
            BuiltinTypesCatalogue.booleanItem,
            true,
            false,
            ItemFactory.getInstance().createBooleanItem(false)
        );
        merged = ((ObjectItemType) left.findLeastCommonSuperTypeLax(createObjectType(true, conflictingRight)))
            .getObjectContentFacet()
            .get("flag");
        Assert.assertNull("Conflicting defaults should be discarded.", merged.getDefaultValue());
    }

    /**
     * Helper method to create an object type with the given closed option and field descriptors.
     * 
     * @param closed whether the object type is closed
     * @param descriptors the field descriptors
     * @return the created object type
     */
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

    /**
     * Helper method to create a field descriptor with the given properties.
     * 
     * @param name the name of the field
     * @param type the type of the field
     * @param required whether the field is required
     * @param unique whether the field is unique
     * @param defaultValue the default value of the field
     * @return the created field descriptor
     */
    private FieldDescriptor field(
            String name,
            ItemType type,
            boolean required,
            boolean unique,
            Item defaultValue
    ) {
        FieldDescriptor descriptor = new FieldDescriptor();
        descriptor.setName(name);
        descriptor.setType(type);
        descriptor.setRequired(required);
        descriptor.setUnique(unique);
        if (defaultValue != null) {
            descriptor.setDefaultValue(defaultValue);
        }
        return descriptor;
    }
}

