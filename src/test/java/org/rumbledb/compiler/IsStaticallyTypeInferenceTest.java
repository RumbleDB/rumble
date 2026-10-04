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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;

class IsStaticallyTypeInferenceTest {
    private static final RumbleConfiguration CONFIGURATION = RumbleConfiguration.builder()
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .build();

    @ParameterizedTest
    @ValueSource(strings = {"jq", "xq"})
    void occurrenceAssertionPreservesMultipleOnlyCardinality(String extension) {
        SequenceType type = infer("(1, 2) is statically xs:integer+", extension);
        assertEquals(SequenceCardinality.MANY, type.getCardinality());
    }

    @Test
    void occurrenceAssertionPreservesEmptyOrMultipleCardinality() {
        SequenceType type = infer(
                "declare variable $flag as xs:boolean external; "
                        + "(if ($flag) then (1, 2) else ()) is statically xs:integer*",
                "jq");
        assertEquals(SequenceCardinality.ZERO_OR_MANY, type.getCardinality());
    }

    @ParameterizedTest
    @ValueSource(strings = {"jq", "xq"})
    void inferredUnionDoesNotMatchItsCommonSupertype(String extension) {
        assertThrows(
                IsStaticallyUnexpectedTypeException.class,
                () -> infer("(\"s\", 12) is statically xs:anyAtomicType+", extension));
    }

    @ParameterizedTest
    @ValueSource(strings = {"comma.jq", "primary/literals.jq", "typing/cast9.jq"})
    void mixedAtomicFixturesKeepTheirExactUnionMembers(String fixture) throws IOException {
        // Query syntax cannot declare anonymous unions, so check these fixtures' item types here.
        Path path = Path.of("src/test/resources/test_files/static-typing", fixture);
        SequenceType type = infer(Files.readString(path), "jq");
        assertEquals(SequenceCardinality.MANY, type.getCardinality());
        assertTrue(type.getItemType().isUnionType());
        Set<ItemType> expectedMembers =
                switch (fixture) {
                    case "comma.jq" -> Set.of(
                            BuiltinTypesCatalogue.integerItem,
                            BuiltinTypesCatalogue.stringItem,
                            BuiltinTypesCatalogue.nullItem);
                    case "primary/literals.jq" -> Set.of(
                            BuiltinTypesCatalogue.decimalItem,
                            BuiltinTypesCatalogue.doubleItem,
                            BuiltinTypesCatalogue.booleanItem,
                            BuiltinTypesCatalogue.nullItem,
                            BuiltinTypesCatalogue.stringItem);
                    case "typing/cast9.jq" -> Set.of(
                            BuiltinTypesCatalogue.floatItem,
                            BuiltinTypesCatalogue.doubleItem,
                            BuiltinTypesCatalogue.decimalItem,
                            BuiltinTypesCatalogue.dateTimeItem,
                            BuiltinTypesCatalogue.dateItem,
                            BuiltinTypesCatalogue.durationItem,
                            BuiltinTypesCatalogue.hexBinaryItem,
                            BuiltinTypesCatalogue.base64BinaryItem,
                            BuiltinTypesCatalogue.booleanItem);
                    default -> throw new IllegalArgumentException(fixture);
                };
        assertEquals(expectedMembers, Set.copyOf(type.getItemType().getTypes()));
        assertEquals(expectedMembers.size(), type.getItemType().getTypes().size());
    }

    @Test
    void assertedMultipleValuesStillBecomeOnlyAnArrayInObjectFields() {
        SequenceType type = infer("{\"b\": ((1, 2) is statically xs:integer+)}.b", "jq");
        assertTrue(type.getItemType().isArrayItemType());
        assertEquals(BuiltinTypesCatalogue.integerItem, type.getItemType().getArrayContentFacet());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "1 is statically xs:integer+",
                "(1, 2) is statically xs:integer*",
                "1 is statically xs:decimal",
                "\"s\" is statically xs:anyAtomicType",
                "(false, 1) is statically xs:integer+"
            })
    void assertionStillRequiresAnExactDeclaredType(String query) {
        assertThrows(IsStaticallyUnexpectedTypeException.class, () -> infer(query, "jq"));
    }

    private static SequenceType infer(String query, String extension) {
        return CompilationPipeline.compileMainModule(
                        query,
                        URI.create("file:///static-assertion." + extension),
                        new CompilationConfiguration(CONFIGURATION),
                        ExternalBindings.empty())
                .getExpression()
                .getStaticSequenceType();
    }
}
