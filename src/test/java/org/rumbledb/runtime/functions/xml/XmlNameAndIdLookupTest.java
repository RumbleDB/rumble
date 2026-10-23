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

class XmlNameAndIdLookupTest {
    private List<Item> evaluate(String query) {
        return new Rumble(RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build()).runQuery(query, Path.of("src/test/resources/test_files/xquery-parser/schema-id/test.xq").toUri())
                .getAsList();
    }

    @Test
    void returnsLexicalNodeNames() {
        assertEquals(List.of("recorded", "p:recorded", "plain", "", "", "target"),
                evaluate("""
                    name(parse-xml('<recorded xmlns="urn:records"/>')/*),
                    name(<p:recorded xmlns:p="urn:records"/>), name(<plain/>),
                    name(()), name(text {"hello"}), name(processing-instruction target {"value"})
                    """).stream().map(Item::getStringValue).toList());
    }

    @Test
    void findsSchemaIdsAndReturnsOwnersInDocumentOrder() {
        List<Item> results = evaluate("""
            import schema namespace t = "urn:identity" at "Identity.xsd";
            let $doc := validate strict { document {
                <t:root><t:entry key="alpha" refs="beta clé"/><t:entry key="beta"/>
                    <t:key>clé</t:key><t:ref>alpha</t:ref><t:refs>alpha beta</t:refs></t:root>
            } }
            return (
                element-with-id("alpha", $doc) is $doc/t:root/t:entry[1],
                element-with-id("clé", $doc) is $doc/t:root,
                deep-equal(element-with-id(("beta clé", "alpha alpha"), $doc),
                    ($doc/t:root, $doc/t:root/t:entry)),
                element-with-id("beta", $doc/t:root/t:entry[1]/@key) is $doc/t:root/t:entry[2],
                empty(element-with-id(("missing", "1invalid", "p:name"), $doc)),
                empty(element-with-id((), $doc)),
                $doc/element-with-id("alpha") is $doc/t:root/t:entry[1]
            )
            """);
        assertEquals(7, results.size());
        results.forEach(item -> assertTrue(item.getBooleanValue()));
    }

    @Test
    void retainsXmlIdAndUnicodeLookup() {
        List<Item> results = evaluate("""
            let $doc := document { <root><a xml:id="clé"/><b xml:id="second"/></root> }
            return (
                element-with-id("clé", $doc) is $doc/root/a,
                deep-equal(element-with-id(("second clé", "clé"), $doc), ($doc/root/a, $doc/root/b))
            )
            """);
        assertEquals(2, results.size());
        results.forEach(item -> assertTrue(item.getBooleanValue()));
    }
}
