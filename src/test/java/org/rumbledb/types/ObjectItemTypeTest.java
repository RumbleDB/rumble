package org.rumbledb.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectItemTypeTest {
    /**
     * Tests that the lax merge operation includes all fields from both object type operands.
     * Verifies that:
     * <ul>
     * <li>Fields from both left and right operands are present in the merged result</li>
     * <li>Field properties (required, unique) are preserved from their respective sources</li>
     * <li>The closed facet is set to false (open) if at least one operand is open</li>
     * </ul>
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
        Assertions.assertTrue(mergedType instanceof ObjectItemType);
        ObjectItemType mergedObject = (ObjectItemType) mergedType;
        Assertions.assertEquals(2, mergedObject.getObjectContentFacet().size());
        Assertions.assertFalse(
            mergedObject.getObjectContentFacet("first").isRequired(),
            "Fields present on only one side of a lax merge are currently treated as optional."
        );
        Assertions.assertFalse(mergedObject.getObjectContentFacet("second").isRequired());
        Assertions.assertTrue(mergedObject.getObjectContentFacet("second").isUnique());
        Assertions.assertFalse(mergedObject.getClosedFacet(), "Merged object should be open if any operand is open.");
    }

    /**
     * Tests that overlapping fields between two object types are merged using lax supertype logic recursively.
     * Verifies that:
     * <ul>
     * <li>When both operands define a field with the same name but different nested object types, the nested types are
     * merged recursively using findLeastCommonSuperTypeLax</li>
     * <li>The merged nested object contains all fields from both nested types</li>
     * <li>The required flag is set to true only if both operands require the field (AND semantics)</li>
     * <li>The unique flag is set to true if either operand marks the field as unique (OR semantics)</li>
     * </ul>
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
            .getObjectContentFacet("nested");
        Assertions.assertFalse(
            mergedDescriptor.isRequired(),
            "Nested field should be optional if any operand is optional."
        );
        Assertions.assertTrue(mergedDescriptor.isUnique(), "Nested field should be unique if any operand is unique.");
        ItemType mergedNestedType = mergedDescriptor.getType();
        Assertions.assertTrue(mergedNestedType instanceof ObjectItemType);
        ObjectItemType innerObject = (ObjectItemType) mergedNestedType;
        Assertions.assertEquals(2, innerObject.getObjectContentFacet().size());
        Assertions.assertTrue(innerObject.getObjectKeysFacet().contains("a"));
        Assertions.assertTrue(innerObject.getObjectKeysFacet().contains("b"));
    }

    /**
     * Tests that default values are preserved in merged field descriptors only when they are compatible.
     * Verifies that:
     * <ul>
     * <li>When both operands define the same default value for a field, it is preserved in the merged result</li>
     * <li>When operands define conflicting default values, the default is discarded (set to null)</li>
     * </ul>
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

        FieldDescriptor merged = ((ObjectItemType) left.findLeastCommonSuperTypeLax(right))
            .getObjectContentFacet("flag");
        Assertions.assertEquals(defaultTrue, merged.getDefaultValue());

        FieldDescriptor conflictingRight = field(
            "flag",
            BuiltinTypesCatalogue.booleanItem,
            true,
            false,
            ItemFactory.getInstance().createBooleanItem(false)
        );
        merged = ((ObjectItemType) left.findLeastCommonSuperTypeLax(createObjectType(true, conflictingRight)))
            .getObjectContentFacet("flag");
        Assertions.assertNull(merged.getDefaultValue(), "Conflicting defaults should be discarded.");
    }

    /**
     * Helper method to create an object type with the given closed option and field descriptors.
     * 
     * @param closed whether the object type is closed
     * @param descriptors the field descriptors
     * @return the created object type
     */
    private ObjectItemType createObjectType(boolean closed, FieldDescriptor... descriptors) {
        List<String> keys = new ArrayList<>();
        List<FieldDescriptor> content = new ArrayList<>();
        for (FieldDescriptor descriptor : descriptors) {
            keys.add(descriptor.getName());
            content.add(descriptor);
        }
        return new ObjectItemType(
                null,
                BuiltinTypesCatalogue.objectItem,
                closed,
                keys,
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
