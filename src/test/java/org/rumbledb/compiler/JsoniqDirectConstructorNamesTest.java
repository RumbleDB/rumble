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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DirectElementConstructorTagMismatchException;
import org.rumbledb.exceptions.DuplicateDirectAttributeException;
import org.rumbledb.exceptions.ParsingException;

class JsoniqDirectConstructorNamesTest {
    @ParameterizedTest
    @ValueSource(
            strings = {
                "a.b",
                "a..b",
                "a.",
                "a.123",
                "a.1e2",
                "a.1e-2",
                "a.123b",
                "a.1.2",
                "a.-b",
                "element.name",
                "a.é",
                "é.名"
            })
    void acceptsDottedElementAndAttributeNames(String name) {
        for (String suffix : new String[] {"/>", "></" + name + ">"}) {
            Item element = run("<" + name + " " + name + "=\"value\"" + suffix);
            assertEquals(name, element.nodeName().getLocalName());
            assertEquals(name, element.attributes().get(0).nodeName().getLocalName());
            assertEquals("value", element.attributes().get(0).getStringValue());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"p.q", "p.1", "p."})
    void acceptsDottedPrefixesAndResolvesNamespaces(String prefix) {
        Item element = run("<"
                + prefix
                + ":a.b xmlns:"
                + prefix
                + "=\"urn:test\" "
                + prefix
                + ":c.d=\"value\"></"
                + prefix
                + ":a.b>");
        assertEquals("urn:test", element.nodeName().getNamespace());
        assertEquals("a.b", element.nodeName().getLocalName());
        assertEquals("urn:test", element.attributes().get(0).nodeName().getNamespace());
        assertEquals("c.d", element.attributes().get(0).nodeName().getLocalName());
    }

    @Test
    void defaultNamespaceAppliesToDottedElementsButNotUnprefixedAttributes() {
        Item element = run("<a.b xmlns=\"urn:default\" attr.name=\"value\"/>");
        assertEquals("urn:default", element.nodeName().getNamespace());
        assertEquals("a.b", element.nodeName().getLocalName());
        assertEquals(
                Name.createVariableInNoNamespace("attr.name"),
                element.attributes().get(0).nodeName());
    }

    @Test
    void nestedDottedConstructorsRestoreNamespaceBindingsForSiblings() {
        Item element = run("<p.q:outer.name xmlns:p.q=\"urn:outer\">"
                + "<p.q:inner.name xmlns:p.q=\"urn:inner\"/>"
                + "<p.q:sibling.name/></p.q:outer.name>");
        assertEquals("urn:outer", element.nodeName().getNamespace());
        assertEquals(2, element.children().size());
        assertEquals("inner.name", element.children().get(0).nodeName().getLocalName());
        assertEquals("urn:inner", element.children().get(0).nodeName().getNamespace());
        assertEquals("sibling.name", element.children().get(1).nodeName().getLocalName());
        assertEquals("urn:outer", element.children().get(1).nodeName().getNamespace());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "<a.b attr.name=\"1\" attr.name=\"2\"/>",
                "<a.b xmlns:p.q=\"urn:test\" xmlns:r.s=\"urn:test\" p.q:attr.name=\"1\" r.s:attr.name=\"2\"/>"
            })
    void rejectsDuplicateDottedAttributesByExpandedName(String query) {
        assertThrows(DuplicateDirectAttributeException.class, () -> run(query));
    }

    @Test
    void preservesSelectorsAndEnclosedExpressions() {
        assertEquals(42, run("let $obj := {\"field\": 42} return $obj.field").getIntValue());
        assertEquals(
                42, run("let $obj := {\"field\": 42} return $obj.\"field\"").getIntValue());
        assertEquals(
                "42",
                run("let $obj := {\"field\": 42} return <a.b attr.name=\"{$obj.field}\">{$obj.field}<c.d/></a.b>")
                        .attributes()
                        .get(0)
                        .getStringValue());
    }

    @Test
    void preservesJsonObjectsInsideAttributeExpressions() {
        assertEquals(
                "42",
                run("<a.b attr.name=\"{ {\"value\": 42}.value }\"/>")
                        .attributes()
                        .get(0)
                        .getStringValue());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "<a . b/>",
                "<a(:comment:).b/>",
                "<a.(:comment:)b/>",
                "<a. 123/>",
                "<a .123/>",
                "<a.b c . d=\"v\"/>",
                "<a.b></a . b>",
                "<.a/>",
                "<a:.b/>",
                "<a.b:c:d/>",
                "<a.1e+2/>",
                "declare variable $a.b := 1; $a.b",
                "declare function local:a.b() { 1 }; local:a.b()",
                "element a.b {}",
                "attribute a.b {\"v\"}"
            })
    void rejectsInvalidNamesAndDottedNamesOutsideDirectConstructors(String query) {
        assertThrows(ParsingException.class, () -> run(query));
    }

    @Test
    void rejectsMismatchedDottedTags() {
        assertThrows(DirectElementConstructorTagMismatchException.class, () -> run("<a.b></a.c>"));
    }

    private static Item run(String query) {
        return new Rumble(RumbleConfiguration.defaultConfiguration())
                .runQuery(query, URI.create("file:///constructor-names.jq"))
                .getAsList()
                .get(0);
    }
}
