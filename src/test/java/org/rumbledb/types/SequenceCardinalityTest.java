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

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.CompilationPipeline;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;

class SequenceCardinalityTest {
    @Test
    void operationsAgreeWithConcreteSequenceLengths() {
        for (SequenceCardinality left : SequenceCardinality.values()) {
            for (SequenceCardinality right : SequenceCardinality.values()) {
                Set<Integer> sums = new HashSet<>();
                Set<Integer> repeated = new HashSet<>();
                Set<Integer> choices = new HashSet<>(sizes(left));
                choices.addAll(sizes(right));
                for (int a : sizes(left)) {
                    for (int b : sizes(right)) {
                        sums.add(Math.min(2, a + b));
                    }
                }
                for (int iterations : sizes(right)) {
                    repeated.addAll(totals(sizes(left), iterations));
                }
                assertEquals(sums, sizes(left.concatenate(right)), left + " concatenated with " + right);
                assertEquals(repeated, sizes(left.repeated(right)), left + " repeated " + right + " times");
                assertEquals(choices, sizes(left.union(right)));
                assertEquals(sizes(right).containsAll(sizes(left)), left.isSubtypeOf(right));
                assertEquals(sizes(left).stream().anyMatch(sizes(right)::contains), left.overlaps(right));
            }
            assertTrue(left.isSubtypeOf(SequenceCardinality.fromArity(left.toArity())));
            Set<Integer> allowingEmptySizes = new HashSet<>();
            for (int size : sizes(left)) {
                allowingEmptySizes.add(Math.max(1, size));
            }
            assertEquals(allowingEmptySizes, sizes(left.replaceZeroWithOne()));
        }
    }

    @Test
    void sequenceTypesKeepMultipleOnlyThroughJoiningAndGrouping() {
        SequenceType multiple = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceCardinality.MANY);
        SequenceType empty = SequenceType.createSequenceType("()");
        SequenceType singleton = new SequenceType(BuiltinTypesCatalogue.integerItem);
        assertEquals(SequenceType.Arity.OneOrMore, multiple.getArity());
        // Equality follows the declared notation, so the refinement is invisible to it.
        assertEquals(SequenceType.createSequenceType("integer+"), multiple);
        assertEquals(SequenceType.createSequenceType("integer+").hashCode(), multiple.hashCode());
        assertTrue(multiple.isSubtypeOf(SequenceType.createSequenceType("integer+")));
        assertFalse(SequenceType.createSequenceType("integer+").isSubtypeOf(multiple));
        assertFalse(multiple.hasOverlapWith(singleton));
        assertEquals(
                SequenceCardinality.ZERO_OR_MANY,
                multiple.leastCommonSupertypeWith(empty).getCardinality());
        assertEquals(SequenceCardinality.MANY, multiple.incrementArity().getCardinality());
        assertEquals(
                SequenceCardinality.MANY, singleton.concatenateWith(singleton).getCardinality());
    }

    @Test
    void independentIterationsCanProduceExactlyOneItem() {
        // Only the first of the two iterations returns an item, so the case below matches and returns a string.
        String query = "switch (for $x in (1, 2) return if ($x eq 1) then $x else ()) "
                + "case 1 return \"one\" default return 2";
        URI uri = URI.create("file:///iterations.jq");
        RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
        ItemType inferred = CompilationPipeline.compileMainModule(
                        query, uri, new CompilationConfiguration(configuration), ExternalBindings.empty())
                .getExpression()
                .getStaticSequenceType()
                .getItemType();
        Item value = new Rumble(configuration).runQuery(query, uri).getAsList().get(0);
        assertTrue(
                value.getDynamicType().isSubtypeOf(inferred), () -> value.serialize() + " is excluded by " + inferred);
    }

    /** Totals of independently sized sequences, capped at 2 like sizes(); two iterations reach every total. */
    private static Set<Integer> totals(Set<Integer> sizes, int iterations) {
        Set<Integer> totals = Set.of(0);
        for (int i = 0; i < iterations; i++) {
            Set<Integer> next = new HashSet<>();
            for (int total : totals) {
                for (int size : sizes) {
                    next.add(Math.min(2, total + size));
                }
            }
            totals = next;
        }
        return totals;
    }

    private static Set<Integer> sizes(SequenceCardinality cardinality) {
        Set<Integer> sizes = new HashSet<>();
        if (cardinality.allowsZero()) sizes.add(0);
        if (cardinality.allowsOne()) sizes.add(1);
        if (cardinality.allowsMany()) sizes.add(2);
        return sizes;
    }
}
