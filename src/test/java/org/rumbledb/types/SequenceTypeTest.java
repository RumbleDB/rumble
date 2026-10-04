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
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.context.Name;

class SequenceTypeTest {
    @ParameterizedTest
    @EnumSource(SequenceCardinality.class)
    void atomicUnionEffectiveBooleanValueRespectsCardinality(SequenceCardinality cardinality) {
        ItemType union = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.integerItem));
        Set<SequenceCardinality> allowed =
                Set.of(SequenceCardinality.EMPTY, SequenceCardinality.ONE, SequenceCardinality.ZERO_OR_ONE);
        assertEquals(allowed.contains(cardinality), new SequenceType(union, cardinality).hasEffectiveBooleanValue());
    }

    @Test
    void namedAndNestedUnionsUseTheirMembers() {
        ItemType named = ItemTypeFactory.createXmlSchemaUnionType(
                new Name("urn:test", "test", "string-or-integer"),
                List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.integerItem));
        assertTrue(new SequenceType(named).hasEffectiveBooleanValue());
        ItemType nested = ItemTypeFactory.createInferredUnionType(List.of(named, BuiltinTypesCatalogue.booleanItem));
        assertTrue(new SequenceType(nested).hasEffectiveBooleanValue());
    }

    @Test
    void everyUnionMemberMustHaveAnEffectiveBooleanValue() {
        ItemType union = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.dateItem));
        assertFalse(new SequenceType(union).hasEffectiveBooleanValue());
        assertFalse(new SequenceType(union, SequenceCardinality.ZERO_OR_ONE).hasEffectiveBooleanValue());
    }

    @Test
    void unionCastabilityUsesMemberRules() {
        ItemType union = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.booleanItem, BuiltinTypesCatalogue.integerItem));
        assertTrue(union.isStaticallyCastableAs(BuiltinTypesCatalogue.stringItem));
        ItemType invalid = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.dateItem, BuiltinTypesCatalogue.dateTimeItem));
        assertFalse(invalid.isStaticallyCastableAs(BuiltinTypesCatalogue.doubleItem));
        ItemType structured = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.arrayItem, BuiltinTypesCatalogue.integerItem));
        assertFalse(structured.isStaticallyCastableAs(BuiltinTypesCatalogue.stringItem));
    }

    @Test
    void unionArgumentConversionChecksEachAlternative() {
        ItemType union = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.doubleItem, BuiltinTypesCatalogue.integerItem));
        assertTrue(new SequenceType(union)
                .isSubtypeOfOrCanBePromotedTo(new SequenceType(BuiltinTypesCatalogue.doubleItem)));
        ItemType invalid = ItemTypeFactory.createInferredUnionType(
                List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.integerItem));
        assertFalse(new SequenceType(invalid)
                .isSubtypeOfOrCanBePromotedTo(new SequenceType(BuiltinTypesCatalogue.doubleItem)));
    }

    @Test
    void functionSubtypingAndCoercionSupportUnions() {
        ItemType function = ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                List.of(new SequenceType(BuiltinTypesCatalogue.integerItem)),
                new SequenceType(BuiltinTypesCatalogue.integerItem)));
        ItemType target = ItemTypeFactory.createInferredUnionType(List.of(function, BuiltinTypesCatalogue.integerItem));
        assertTrue(function.isSubtypeOf(target));
        ItemType callable = ItemTypeFactory.createInferredUnionType(List.of(function, BuiltinTypesCatalogue.arrayItem));
        assertTrue(new SequenceType(callable).isSubtypeOfOrCanBePromotedTo(new SequenceType(function)));
        assertFalse(new SequenceType(target).isSubtypeOfOrCanBePromotedTo(new SequenceType(function)));
    }
}
