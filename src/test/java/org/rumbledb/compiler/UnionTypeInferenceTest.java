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
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;

class UnionTypeInferenceTest {
    private static final RumbleConfiguration CONFIGURATION = RumbleConfiguration.builder()
            .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
            .build();

    private record Case(String query, List<ItemType> expectedTypes) {}

    static Stream<Arguments> mixedAtomicNodeQueries() {
        List<ItemType> mixed = List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.elementNode);
        return Stream.of(
                Arguments.of("for $i in ('3', <h3/>) return $i", mixed, SequenceCardinality.MANY),
                Arguments.of(
                        "let $i := if (xs:boolean('true')) then '3' else <h3/> return $i",
                        mixed,
                        SequenceCardinality.ONE),
                Arguments.of(
                        "typeswitch (('3', <h3/>)) case xs:string+ return '3' default return <h3/>",
                        mixed,
                        SequenceCardinality.ONE),
                Arguments.of(
                        "for $i in ('3', <h3/>, 1) return $i",
                        List.of(
                                BuiltinTypesCatalogue.stringItem,
                                BuiltinTypesCatalogue.elementNode,
                                BuiltinTypesCatalogue.integerItem),
                        SequenceCardinality.MANY),
                Arguments.of(
                        "for $i in ('3', <h3/> treat as node()) return $i",
                        List.of(BuiltinTypesCatalogue.stringItem, BuiltinTypesCatalogue.nodeItem),
                        SequenceCardinality.MANY));
    }

    // Anonymous unions have no SequenceType syntax, so their exact members are checked here.
    // Operator behavior and runtime errors belong to the annotation regressions.
    @ParameterizedTest
    @MethodSource("mixedAtomicNodeQueries")
    void mixedAtomicNodeUnionsRemainPrecise(String query, List<ItemType> members, SequenceCardinality cardinality) {
        var module = CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///mixed-union.xq"),
                new CompilationConfiguration(CONFIGURATION),
                ExternalBindings.empty());
        SequenceType inferred = module.getExpression().getStaticSequenceType();
        ItemType expected = ItemTypeFactory.createInferredUnionType(members);
        assertTrue(inferred.getItemType().isUnionType());
        assertEquals(
                java.util.Set.copyOf(members),
                java.util.Set.copyOf(inferred.getItemType().getTypes()));
        assertEquals(cardinality, inferred.getCardinality());
        for (ItemType member : members) {
            assertTrue(member.isSubtypeOf(expected));
        }
        if (module.getExpression() instanceof FlworExpression flwor) {
            assertEquals(
                    SequenceCardinality.ONE,
                    flwor.getReturnClause()
                            .getReturnExpr()
                            .getStaticSequenceType()
                            .getCardinality());
        }
    }

    static Stream<Arguments> unionQueries() {
        List<ItemType> floating = List.of(BuiltinTypesCatalogue.floatItem, BuiltinTypesCatalogue.doubleItem);
        List<ItemType> integers = List.of(BuiltinTypesCatalogue.integerItem);
        List<ItemType> strings = List.of(BuiltinTypesCatalogue.stringItem);
        List<Case> cases = new java.util.ArrayList<>();
        for (String operator : List.of("+", "-", "*", "div", "mod")) {
            cases.add(new Case("for $x in (xs:float(1), xs:double(2)) return $x " + operator + " 1", floating));
        }
        cases.addAll(List.of(
                new Case("for $x in (xs:float(1), xs:double(2)) for $y in (xs:decimal(3), 4) return $x + $y", floating),
                new Case("for $x in (xs:float(1), xs:double(2)) return $x idiv 1", integers),
                new Case(
                        "for $x in (xs:date(\"2000-01-01\"), xs:dateTime(\"2000-01-01T00:00:00\")) return $x + xs:dayTimeDuration(\"P1D\")",
                        List.of(BuiltinTypesCatalogue.dateItem, BuiltinTypesCatalogue.dateTimeItem)),
                new Case(
                        "for $x in (xs:float(1), xs:decimal(2)) return math:pow($x, 2)",
                        List.of(BuiltinTypesCatalogue.doubleItem)),
                new Case("for $x in (xs:anyURI(\"a\"), \"b\") return string-length($x)", integers),
                new Case(
                        "for $x in (xs:anyURI(\"a\"), \"b\") return $x eq \"a\"",
                        List.of(BuiltinTypesCatalogue.booleanItem)),
                new Case("for $x in (1, \"2\") return $x cast as xs:string", strings),
                new Case("for $x in (xs:boolean(\"true\"), 1) return $x cast as xs:string", strings),
                new Case("sum((xs:positiveInteger(1), xs:negativeInteger(-1)))", integers),
                new Case(
                        "sum((xs:untypedAtomic(\"1\"), 2))",
                        List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.doubleItem)),
                new Case("avg((xs:untypedAtomic(\"1\"), 2))", List.of(BuiltinTypesCatalogue.numericItem)),
                new Case(
                        "min((xs:untypedAtomic(\"1\"), 2))",
                        List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.doubleItem)),
                new Case(
                        "max((xs:untypedAtomic(\"1\"), 2))",
                        List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.doubleItem)),
                new Case(
                        "min((xs:int(1), xs:float(2)))",
                        List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.floatItem)),
                new Case(
                        "max((xs:anyURI(\"a\"), \"b\"))",
                        List.of(BuiltinTypesCatalogue.anyURIItem, BuiltinTypesCatalogue.stringItem)),
                new Case(
                        "sum((1, 2)[xs:boolean(\"false\")], \"empty\")",
                        List.of(BuiltinTypesCatalogue.integerItem, BuiltinTypesCatalogue.stringItem))));
        return cases.stream().flatMap(test -> Stream.of("jq", "xq").map(extension -> Arguments.of(test, extension)));
    }

    @ParameterizedTest
    @MethodSource("unionQueries")
    void inferredTypesCoverRuntimeResults(Case test, String extension) {
        URI uri = URI.create("file:///union-inference." + extension);
        SequenceType inferred = infer(test.query(), uri);
        ItemType expected = ItemTypeFactory.createInferredUnionType(test.expectedTypes());
        assertTrue(inferred.getItemType().isSubtypeOf(expected), () -> "Unexpected inferred type: " + inferred);
        List<Item> values =
                new Rumble(CONFIGURATION).runQuery(test.query(), uri).getAsList();
        assertFalse(values.isEmpty());
        for (Item value : values) {
            assertTrue(
                    value.getDynamicType().isSubtypeOf(inferred.getItemType()),
                    () -> value.getDynamicType() + " is excluded by " + inferred + " for " + test.query());
        }
    }

    @Test
    void sumEmptyInputUsesItsZeroArgument() {
        URI uri = URI.create("file:///sum-empty.xq");
        assertEquals(SequenceCardinality.ONE, infer("sum(())", uri).getCardinality());
        assertEquals(BuiltinTypesCatalogue.integerItem, infer("sum(())", uri).getItemType());
        assertEquals(
                BuiltinTypesCatalogue.stringItem,
                infer("sum((), \"zero\")", uri).getItemType());
        assertTrue(infer("sum((), ())", uri).isEmptySequence());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void callableFieldAlternativesCanBeInvoked(boolean singleton) {
        String query = "let $f := function($x as xs:integer) as xs:integer {$x} "
                + "let $o := {\"f\": if ("
                + singleton
                + ") then $f else ($f, $f)} return ($o.f)(1)";
        List<Item> values = new Rumble(CONFIGURATION).runQuery(query).getAsList();
        assertEquals(1, values.size());
        if (singleton) {
            assertEquals(1, values.get(0).getIntValue());
        } else {
            assertTrue(values.get(0).isFunction());
        }
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "for $x in (1, \"a\") return $x + 1",
                "for $x in (xs:float(1), \"a\") return math:pow($x, 2)",
                "for $x in (xs:date(\"2000-01-01\"), xs:dateTime(\"2000-01-01T00:00:00\")) return $x cast as xs:double"
            })
    void incompatibleUnionMembersAreStillRejected(String query) {
        for (String extension : List.of("jq", "xq")) {
            assertThrows(
                    UnexpectedStaticTypeException.class,
                    () -> infer(query, URI.create("file:///invalid-union." + extension)));
        }
    }

    @Test
    void nullableSumWithEmptyZeroRemainsOptional() {
        URI uri = URI.create("file:///sum-optional.xq");
        assertEquals(
                SequenceCardinality.ZERO_OR_ONE,
                infer("sum((1, 2)[xs:boolean(\"false\")], ())", uri).getCardinality());
    }

    @Test
    void nonAtomicCastOperandsRemainRuntimeChecksWhenStaticTypingIsDisabled() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureAnalysis(analysis -> analysis.enableStaticTyping(false))
                .build();
        assertDoesNotThrow(() -> CompilationPipeline.compileMainModule(
                "[1] cast as xs:string",
                URI.create("file:///array-cast.jq"),
                new CompilationConfiguration(configuration),
                ExternalBindings.empty()));
    }

    private static SequenceType infer(String query, URI uri) {
        return CompilationPipeline.compileMainModule(
                        query, uri, new CompilationConfiguration(CONFIGURATION), ExternalBindings.empty())
                .getExpression()
                .getStaticSequenceType();
    }
}
