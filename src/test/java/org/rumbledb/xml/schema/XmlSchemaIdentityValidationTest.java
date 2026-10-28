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
package org.rumbledb.xml.schema;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.InvalidInstanceException;

import static org.junit.jupiter.api.Assertions.*;

class XmlSchemaIdentityValidationTest {
    private String evaluate(String query) {
        return new Rumble(RumbleConfiguration.builder().build()).runQuery(query).getAsList().get(0).getStringValue();
    }

    private static final String DOCUMENT = """
        document { <root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xmlns:xs="http://www.w3.org/2001/XMLSchema">
          <target xml:id="target"/><ref xsi:type="xs:IDREF">target</ref>
        </root> }
        """;

    @Test
    void laxDocumentResolvesXmlIdWithoutSchemaImport() {
        assertEquals("target", evaluate("string((validate lax { " + DOCUMENT + " })/root/ref)"));
    }

    @Test
    void elementOperandDoesNotEnforceDocumentReferenceConstraints() {
        assertEquals("missing", evaluate("""
            string(validate lax { <ref xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                      xmlns:xs="http://www.w3.org/2001/XMLSchema"
                                      xsi:type="xs:IDREF">missing</ref> })
            """));
    }

    @Test
    void documentStillRejectsMissingReference() {
        assertThrows(InvalidInstanceException.class,
            () -> evaluate("string(validate lax { " + DOCUMENT.replace(
                "<ref xsi:type=\"xs:IDREF\">target", "<ref xsi:type=\"xs:IDREF\">missing") + " })"));
    }

    @Test
    void documentStillRejectsDuplicateIds() {
        assertThrows(InvalidInstanceException.class,
            () -> evaluate("string(validate lax { " + DOCUMENT.replace(
                "<target xml:id=\"target\"/>", "<target xml:id=\"target\"/><other xml:id=\"target\"/>") + " })"));
    }

    @Test
    void preservesExplicitXmlNamespaceSchemas(@TempDir Path directory) throws Exception {
        for (boolean declaresId : new boolean[] {false, true}) {
            Path schema = directory.resolve("xml-" + declaresId + ".xsd");
            Files.writeString(schema,
                "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' "
                + "targetNamespace='http://www.w3.org/XML/1998/namespace'>"
                + "<xs:attribute name='lang' type='xs:language'/>"
                + (declaresId ? "<xs:attribute name='id' type='xs:ID'/>" : "")
                + "</xs:schema>");
            Path wrapper = directory.resolve("wrapper.xsd");
            Files.writeString(wrapper,
                "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' targetNamespace='urn:wrapper'>"
                + "<xs:import namespace='http://www.w3.org/XML/1998/namespace' schemaLocation='"
                + schema.toUri() + "'/></xs:schema>");
            assertEquals("target", evaluate("import schema namespace t = "
                + "'urn:wrapper' at '" + wrapper.toUri() + "'; "
                + "string((validate lax { " + DOCUMENT + " })/root/ref)"));
        }
    }

    @Test
    void importedSchemaValidatesOnlyIdrefMembersOfUnionLists(@TempDir Path directory) throws Exception {
        Path schema = directory.resolve("ids.xsd");
        Files.writeString(schema, """
            <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:ids"
                       targetNamespace="urn:ids" elementFormDefault="qualified">
              <xs:simpleType name="referenceOrInteger"><xs:union memberTypes="xs:IDREF xs:integer"/></xs:simpleType>
              <xs:simpleType name="references"><xs:list itemType="t:referenceOrInteger"/></xs:simpleType>
              <xs:element name="root"><xs:complexType><xs:sequence>
                <xs:element name="target" type="xs:anyType"/>
                <xs:element name="refs" type="t:references"/>
              </xs:sequence></xs:complexType></xs:element>
            </xs:schema>
            """);
        String query = "import schema namespace t = 'urn:ids' at '" + schema.toUri() + "'; "
            + "string((validate strict { document { <t:root><t:target xml:id='target'/>"
            + "<t:refs>target 234 976</t:refs></t:root> } })/t:root/t:refs)";
        assertEquals("target 234 976", evaluate(query));
    }
}
