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
package org.rumbledb.compiler;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

class ObjectConstructorTypeInferenceTest {
    private static final RumbleConfiguration CONFIGURATION = RumbleConfiguration.builder()
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .build();

    @Test
    void preservesFieldsInOriginalFlworExample() {
        String query = "for $i in 1 to 10 return {\"i\": $i, \"power_of_two\": math:pow($i, 2)}";
        SequenceType result = infer(query, "jq");
        ItemType objectType = result.getItemType();
        assertEquals(List.of("i", "power_of_two"), objectType.getObjectKeysFacet());
        assertEquals(BuiltinTypesCatalogue.integerItem, field(objectType, "i"));
        ItemType powerType = field(objectType, "power_of_two");
        assertTrue(powerType.isUnionType());
        assertEquals(List.of(BuiltinTypesCatalogue.doubleItem, BuiltinTypesCatalogue.nullItem), powerType.getTypes());
        assertTrue(objectType.getClosedFacet());
        assertEquals(SequenceType.Arity.ZeroOrMore, result.getArity());
        List<Item> results = new Rumble(CONFIGURATION).runQuery(query).getAsList();
        assertEquals(10, results.size());
        for (Item object : results) {
            assertTrue(object.getItemByKey("i").getDynamicType().isSubtypeOf(field(objectType, "i")));
            assertTrue(object.getItemByKey("power_of_two").getDynamicType().isSubtypeOf(powerType));
        }
    }

    @Test
    void storesEmptySequenceAsRequiredNullField() {
        ItemType objectType = infer("{\"empty\": (), \"known\": 1}", "jq").getItemType();
        assertEquals(BuiltinTypesCatalogue.nullItem, field(objectType, "empty"));
        assertEquals(BuiltinTypesCatalogue.integerItem, field(objectType, "known"));
        assertEquals(SequenceType.Arity.One, infer("{\"empty\": ()}.empty", "jq").getArity());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void sequenceValuesCoverNullSingletonAndWrappedArray(int length) {
        String query = "let $values := (for $i in 1 to 2 where $i le " + length
                + " return $i) return {\"value\": $values, \"known\": 1}";
        ItemType objectType = infer(query, "jq").getItemType();
        ItemType valueType = field(objectType, "value");
        assertEquals(BuiltinTypesCatalogue.integerItem, field(objectType, "known"));
        assertTrue(valueType.isUnionType());
        assertEquals(3, valueType.getTypes().size());
        assertEquals(BuiltinTypesCatalogue.integerItem, valueType.getTypes().get(0));
        ItemType arrayType = valueType.getTypes().get(1);
        assertTrue(arrayType.isArrayItemType());
        assertEquals(BuiltinTypesCatalogue.integerItem, arrayType.getArrayContentFacet());
        assertEquals(BuiltinTypesCatalogue.nullItem, valueType.getTypes().get(2));
        Item value = new Rumble(CONFIGURATION).runQuery(query).getAsList().get(0).getItemByKey("value");
        // Dynamic arrays carry a generic type; check their members against the inferred content.
        if (length > 1) {
            assertTrue(value.isArray());
            assertEquals(length, value.getSize());
            for (Item member : value.getItemMembers()) {
                assertTrue(member.getDynamicType().isSubtypeOf(arrayType.getArrayContentFacet()));
            }
        } else {
            assertTrue(value.getDynamicType().isSubtypeOf(valueType));
            assertEquals(length == 0, value.isNull());
        }
    }

    @Test
    void nonemptySequenceIncludesScalarAndArrayButNoIntroducedNull() {
        ItemType objectType = infer(
                "declare variable $values as xs:integer+ := (1, 2); {\"value\": $values}", "jq").getItemType();
        ItemType valueType = field(objectType, "value");
        assertTrue(valueType.isUnionType());
        assertEquals(2, valueType.getTypes().size());
        assertFalse(valueType.canBeNull());
    }

    @Test
    void preservesNestedObjectFields() {
        ItemType objectType = infer("{\"nested\": {\"value\": math:pow(2, 3)}, \"known\": 1}", "jq")
                .getItemType();
        ItemType nestedType = field(objectType, "nested");
        assertTrue(nestedType.isObjectItemType());
        assertTrue(field(nestedType, "value").isUnionType());
        assertEquals(BuiltinTypesCatalogue.integerItem, infer(
                "{\"nested\": {\"value\": math:pow(2, 3)}, \"known\": 1}.known", "jq").getItemType());
    }

    @Test
    void optionalObjectIncludesOriginalShapeAndNull() {
        ItemType objectType = infer(
                "declare variable $value as object? := (); {\"value\": $value, \"known\": 1}", "jq")
                .getItemType();
        ItemType valueType = field(objectType, "value");
        assertTrue(valueType.isUnionType());
        assertTrue(valueType.getTypes().get(0).isObjectItemType());
        assertEquals(BuiltinTypesCatalogue.nullItem, valueType.getTypes().get(1));
    }

    @Test
    void keepsGenericTypeForDynamicKeysAndMergedConstructors() {
        assertEquals(BuiltinTypesCatalogue.objectItem,
                infer("{string(1): math:pow(2, 3)}", "jq").getItemType());
        assertEquals(BuiltinTypesCatalogue.objectItem,
                infer("{| {\"value\": 1} |}", "jq").getItemType());
        assertTrue(infer("({})", "jq").getItemType().getObjectKeysFacet().isEmpty());
    }

    @Test
    void nullableUnionAlreadyContainingNullDoesNotDuplicateIt() {
        ItemType type = field(infer("{\"value\": if (true) then null else ()}", "jq").getItemType(), "value");
        assertEquals(BuiltinTypesCatalogue.nullItem, type);
    }

    @Test
    void scalarArrayUnionsRemainSoundWhenMergedWithAtomicTypes() {
        ItemType valueType = ItemTypeFactory.createObjectFieldType(
                new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrMore));
        assertFalse(valueType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem));
        assertFalse(valueType.isSubtypeOf(BuiltinTypesCatalogue.integerItem));
        assertTrue(valueType.isSubtypeOf(BuiltinTypesCatalogue.item));
        ItemType merged = valueType.findLeastCommonSuperTypeWith(BuiltinTypesCatalogue.stringItem);
        assertTrue(valueType.getTypes().stream().allMatch(type -> type.isSubtypeOf(merged)));
        assertTrue(BuiltinTypesCatalogue.stringItem.isSubtypeOf(merged));
    }

    @Test
    void inferredNullableUnionCanBeComparedWithEquivalentUnion() {
        SequenceType inputType = new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.OneOrZero);
        ItemType left = ItemTypeFactory.createObjectFieldType(inputType);
        ItemType right = ItemTypeFactory.createObjectFieldType(inputType);
        assertTrue(left.isSubtypeOf(right));
        assertTrue(right.isSubtypeOf(left));
        assertTrue(left.isSubtypeOf(BuiltinTypesCatalogue.atomicItem));
    }

    private static ItemType field(ItemType objectType, String key) {
        assertTrue(objectType.isObjectItemType());
        FieldDescriptor field = objectType.getObjectContentFacet(key);
        assertNotNull(field);
        assertTrue(field.isRequired(), "Literal-key fields are present even when their value is null");
        return field.getType();
    }

    private static SequenceType infer(String query, String extension) {
        return CompilationPipeline.compileMainModule(query, URI.create("file:///object-inference." + extension),
                new CompilationConfiguration(CONFIGURATION), ExternalBindings.empty())
                .getExpression().getStaticSequenceType();
    }
}
