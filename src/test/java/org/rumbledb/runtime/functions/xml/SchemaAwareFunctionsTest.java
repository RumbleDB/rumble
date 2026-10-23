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
package org.rumbledb.runtime.functions.xml;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class SchemaAwareFunctionsTest {
    private List<Item> evaluate(String query) {
        return new Rumble(RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build()).runQuery(query, Path.of("src/test/resources/test_files/xquery-parser/schema-id/test.xq").toUri())
                .getAsList();
    }

    private void assertAllTrue(String query, int count) {
        List<Item> results = evaluate(query);
        assertEquals(count, results.size());
        for (int index = 0; index < results.size(); index++) {
            assertTrue(results.get(index).getBooleanValue(), "Assertion " + (index + 1));
        }
    }

    @Test
    void comparesTypedValuesInsteadOfLexicalText() {
        assertAllTrue("""
            not(deep-equal(validate type xs:integer {<e>2012</e>}, validate type xs:gYear {<e>2012</e>})),
            deep-equal(validate type xs:integer {<e>01</e>}, validate type xs:decimal {<e>1.0</e>}),
            not(deep-equal(validate type xs:integer {<e>1</e>}, <e>1</e>)),
            deep-equal(<e><!-- ignored -->hello</e>, <e>hello</e>)
            """, 4);
    }

    @Test
    void comparesSchemaContentCategoriesAndLists() {
        assertAllTrue("""
            declare boundary-space preserve;
            import schema namespace t = "urn:deep-equal" at "DeepEqual.xsd";
            let $element := validate type t:elementOnly {<e/>}
            let $mixed := validate type t:mixed {<e/>}
            let $empty := validate type t:empty {<e/>}
            let $simple := validate type t:simple {<e>1</e>}
            return (
                not(deep-equal($element, $mixed)), not(deep-equal($mixed, $empty)),
                not(deep-equal($element, $empty)), not(deep-equal($simple, <e>1</e>)),
                not(deep-equal(validate type t:list {<e/>}, validate type t:union {<e/>})),
                deep-equal(validate type t:list {<e>01 2</e>}, validate type t:list {<e>1 02</e>}),
                deep-equal($element, validate type t:elementOnly {<e><!-- ignored --></e>}),
                deep-equal($empty, validate type t:empty {<e/>}),
                deep-equal(validate type t:elementOnly {<e> <child/> </e>},
                           validate type t:elementOnly {<e><child/></e>}),
                not(deep-equal(validate type t:mixed {<e> <child/> </e>},
                              validate type t:mixed {<e><child/></e>}))
            )
            """, 10);
    }

    @Test
    void returnsTypedAnalyzeStringNodesWithoutSchemaImport() {
        assertAllTrue("""
            let $r := analyze-string("banana", "(b)(anana)")
            return (
                ($r//@nr)[1] instance of attribute(nr, xs:positiveInteger),
                not($r instance of element(*, xs:untyped)),
                data(($r//@nr)[1]) instance of xs:positiveInteger,
                string($r) eq "banana", string(data($r)) eq "banana",
                $r/fn:match/parent::* is $r,
                empty(analyze-string((), "x")/*)
            )
            """, 7);
    }

    @Test
    void exposesStandardAnalyzeStringSchemaDeclarations() {
        assertAllTrue("""
            import schema "http://www.w3.org/2005/xpath-functions";
            let $r := analyze-string("ab cd", "((a)(b))")
            return (
                $r instance of schema-element(fn:analyze-string-result),
                $r/fn:match[1] instance of schema-element(fn:match),
                $r/fn:match/fn:group instance of schema-element(fn:group),
                $r/fn:non-match instance of schema-element(fn:non-match),
                data($r/fn:non-match) instance of xs:string,
                every $n in $r//@nr satisfies data($n) instance of xs:positiveInteger,
                string($r) eq "ab cd"
            )
            """, 7);
    }
}
