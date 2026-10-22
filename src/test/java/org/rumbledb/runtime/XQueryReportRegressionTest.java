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
package org.rumbledb.runtime;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.rumbledb.api.Item;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.bindings.ItemSequenceBinding;
import org.rumbledb.compiler.CompilationPipeline;
import org.rumbledb.compiler.DynamicContextVisitor;
import org.rumbledb.compiler.RuntimeIteratorVisitor;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

class XQueryReportRegressionTest {
    private static final RumbleConfiguration CONFIG = RumbleConfiguration.builder()
            .configureSemantics(s -> s.queryLanguage("xquery31"))
            .build();

    private static List<Item> evaluate(String query, ExternalBindings bindings) {
        var module = CompilationPipeline.compileMainModule(
                query, URI.create("file:///report-regression.xq"), new CompilationConfiguration(CONFIG), bindings);
        var context = new DynamicContextVisitor(CONFIG, bindings).visit(module, null);
        return new RuntimeIteratorVisitor(CONFIG).generateRuntimePlan(module).materialize(context);
    }

    private static List<String> values(String query) {
        return evaluate(query, ExternalBindings.empty()).stream()
                .map(Item::getStringValue)
                .toList();
    }

    @Test
    void reverseAxisPredicatesUseProximityOrderAndReturnDocumentOrder() {
        assertEquals(
                List.of("b", "r", "a"),
                values("let $n := <r><a><b><c/></b></a></r>/a/b/c return "
                        + "(name($n/ancestor::*[1]), name($n/ancestor::*[last()]), name(($n/ancestor::*)[2]))"));
        assertEquals(List.of("a", "b"), values("<r><a><b><c/></b></a></r>/a/b/c/ancestor::*[position() le 2]/name()"));
        assertEquals(
                List.of("r", "a", "b"),
                values("let $n := <r><a><b><c/></b></a></r>/a/b/c return $n/ancestor::*/name()"));
        assertEquals(
                List.of("near", "far"),
                values("let $n := <r><far/><a><near/><b><leaf/></b></a></r>//leaf "
                        + "return (name($n/preceding::*[1]), name($n/preceding::*[last()]))"));
        assertEquals(
                List.of("c", "b"),
                values("let $n := <r><a><b><c/></b></a></r>//c return "
                        + "(name($n/ancestor-or-self::*[1]), name($n/ancestor-or-self::*[2]))"));
    }

    @Test
    void reverseAxisOrderingDoesNotCallAUserDefinedReverseFunction() {
        assertEquals(
                List.of("b"),
                values("declare default function namespace 'urn:custom'; "
                        + "declare function reverse($items) { () }; "
                        + "fn:name(<r><a><b><c/></b></a></r>/a/b/c/ancestor::*[1])"));
    }

    @Test
    void rangeAtomizesNodesAndPreservesEmptyAndLargeBounds() {
        assertEquals(List.of("1", "2", "3"), values("let $x := <e>3</e> return 1 to $x"));
        assertEquals(List.of("4"), values("count(<e>4294967295</e> to <e>4294967298</e>)"));
        assertEquals(List.of(), values("() to <e>3</e>"));
        assertEquals(List.of(), values("<e>3</e> to 1"));
        assertEquals(
                "XPTY0004",
                assertThrows(RumbleException.class, () -> values("1 to (2, 3)"))
                        .getErrorCode()
                        .toString());
    }

    @Test
    void initialFocusHasPositionAndSizeAndNestedFocusOverridesThem() {
        assertEquals(
                List.of("1", "1", "1", "1"),
                values(
                        "declare context item := <e/>; declare variable $x := position(); ($x, last(), position(), last())"));
        assertEquals(
                List.of("1", "3", "2", "3", "3", "3"),
                values("declare context item := <e/>; (1 to 3) ! (position(), last())"));
        for (String query :
                List.of("position()", "declare context item := position(); .", "declare context item := last(); .")) {
            assertEquals(
                    "XPDY0002",
                    assertThrows(RumbleException.class, () -> values(query))
                            .getErrorCode()
                            .toString());
        }
    }

    @Test
    void applySupportsMapsArraysAndChecksArity() {
        assertEquals(
                List.of("true"),
                values(
                        "fn:random-number-generator('a')?next => fn:apply([]) => fn:apply(['number']) instance of xs:double"));
        assertEquals(List.of("a", "b"), values("apply(map{'k': ('a', 'b')}, ['k'])"));
        assertEquals(List.of("a", "b"), values("apply([('a', 'b')], [1])"));
        assertEquals(List.of(), values("apply(map{}, ['missing'])"));
        assertEquals(
                "FOAP0001",
                assertThrows(RumbleException.class, () -> values("apply(map{}, [])"))
                        .getErrorCode()
                        .toString());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "not(map{1:'A','x':'B'} instance of function(xs:integer) as xs:string)",
                "not(map{1:'A','x':'B'} instance of function(xs:integer) as item()+)",
                "map{} instance of function(xs:integer) as empty-sequence()",
                "not(map{12:'z'} instance of function(xs:decimal) as xs:string)",
                "map{1:'a'} instance of function(xs:integer) as xs:string?",
                "[map{1:map{2:map{}}}] instance of array(map(xs:integer,map(xs:integer,map(xs:integer,map(*)))))",
                "let $f := function($a as array(map(xs:integer,map(xs:integer,map(xs:integer,map(*)))))) {array:size($a)} "
                        + "return $f([map{1:map{2:map{}}}]) eq 1",
                "not(map{1:map{2:'bad'}} instance of map(xs:integer,map(xs:integer,map(*))))"
            })
    void mapAndArrayTypesMatchTheirContents(String query) {
        assertEquals(List.of("true"), values(query));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "",
                "{1}",
                "1 ; 1",
                "declare namespace p = 'urn:p';",
                "module namespace p = 'urn:p';",
                "declare function local:f() external;",
                "declare function local:f(){1;2}; local:f()"
            })
    void strictXQueryRejectsMissingBodiesAndScripting(String query) {
        var strict = CONFIG.toBuilder()
                .configureSemantics(semantics -> semantics.allowScripting(false))
                .build();
        var error = assertThrows(
                RumbleException.class,
                () -> CompilationPipeline.compileMainModule(
                        query,
                        URI.create("file:///strict.xq"),
                        new CompilationConfiguration(strict),
                        ExternalBindings.empty()));
        assertEquals("XPST0003", error.getErrorCode().toString());
    }

    @Test
    void scriptingRemainsAvailableAndStrictModeAcceptsStandardExpressions() {
        assertEquals(List.of("1"), values("{1}"));
        var strict = CONFIG.toBuilder()
                .configureSemantics(semantics -> semantics.allowScripting(false))
                .build();
        var module = CompilationPipeline.compileMainModule(
                "declare variable $x := 2; $x + 1",
                URI.create("file:///strict.xq"),
                new CompilationConfiguration(strict),
                ExternalBindings.empty());
        var context = new DynamicContextVisitor(strict, ExternalBindings.empty()).visit(module, null);
        assertEquals(
                "3",
                new RuntimeIteratorVisitor(strict)
                        .generateRuntimePlan(module)
                        .materialize(context)
                        .get(0)
                        .getStringValue());
    }

    @Test
    void collectionsAreStableQueryScopedAndSupportAtomicValuesAndUris() {
        var items = List.of(
                ItemFactory.getInstance().createStringItem("one"),
                ItemFactory.getInstance().createStringItem("two"));
        var collection = new org.rumbledb.resources.ResourceCollection(items, List.of(URI.create("urn:doc:1")));
        var resolver = new org.rumbledb.resources.ResourceResolver(
                java.util.Map.of(), java.util.Map.of(URI.create("file:///named"), collection), collection);
        String query = "(collection(), collection(()), collection('named'), uri-collection())";
        var module = CompilationPipeline.compileMainModule(
                query,
                URI.create("file:///collections.xq"),
                new CompilationConfiguration(CONFIG, resolver),
                ExternalBindings.empty());
        var context = new DynamicContextVisitor(CONFIG, ExternalBindings.empty(), resolver).visit(module, null);
        assertEquals(
                List.of("one", "two", "one", "two", "one", "two", "urn:doc:1"),
                new RuntimeIteratorVisitor(CONFIG)
                        .generateRuntimePlan(module).materialize(context).stream()
                                .map(Item::getStringValue)
                                .toList());
        for (String invalid : List.of(
                "collection()", "uri-collection()", "collection('invalidURI%gg')", "uri-collection('missing')")) {
            assertEquals(
                    "FODC0002",
                    assertThrows(RumbleException.class, () -> values(invalid))
                            .getErrorCode()
                            .toString());
        }
    }

    @Test
    void suppliedExternalValuesOverrideDefaultsAndAreTypeChecked() {
        var bindings = ExternalBindings.empty();
        bindings.bind(
                Name.createVariableInNoNamespace("ext"),
                new ItemSequenceBinding(
                        List.of(ItemFactory.getInstance().createIntegerItem(java.math.BigInteger.valueOf(5)))));
        assertEquals(
                "5",
                evaluate("declare variable $ext as xs:integer external := 0; $ext", bindings)
                        .get(0)
                        .getStringValue());
        var invalid = ExternalBindings.empty();
        invalid.bind(
                Name.createVariableInNoNamespace("ext"),
                new ItemSequenceBinding(List.of(ItemFactory.getInstance().createStringItem("bad"))));
        assertEquals(
                "XPTY0004",
                assertThrows(
                                RumbleException.class,
                                () -> evaluate("declare variable $ext as xs:integer external := 0; $ext", invalid))
                        .getErrorCode()
                        .toString());
    }
}
