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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.SchemaImportException;

public class XmlSchemaVersionSelectionTest {
    @Test
    public void schema10ImportsWorkInBothVersions(@TempDir Path directory) throws Exception {
        Files.writeString(
                directory.resolve("types.xsd"),
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="urn:test">
                      <xs:element name="value" type="xs:integer"/>
                    </xs:schema>
                    """);
        for (String version : new String[] {"1.0", "1.1"}) {
            var config = RumbleConfiguration.builder()
                    .configureSemantics(s -> s.queryLanguage("xquery31").xsdVersion(version))
                    .build();
            String query = "import schema namespace t = 'urn:test' at 'types.xsd'; "
                    + "data(validate strict { <t:value>42</t:value> })";
            Assertions.assertEquals(
                    "42",
                    new Rumble(config)
                            .runQuery(query, directory.resolve("query.xq").toUri())
                            .getAsList()
                            .get(0)
                            .getStringValue());
            var builtins = new org.rumbledb.context.StaticContext(directory.toUri(), config)
                    .getInScopeSchemaTypes()
                    .getXmlSchemaCatalog();
            Assertions.assertEquals(
                    version.equals("1.1")
                            ? org.apache.xerces.impl.Constants.W3C_XML_SCHEMA11_NS_URI
                            : org.apache.xerces.impl.Constants.W3C_XML_SCHEMA10_NS_URI,
                    ((org.apache.xerces.jaxp.validation.XSGrammarPoolContainer) builtins.getValidationSchema())
                            .getXMLSchemaVersion());
        }
    }

    @Test
    public void assertionsUseTheConfiguredVersion(@TempDir Path directory) throws Exception {
        Files.writeString(
                directory.resolve("types.xsd"),
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="urn:test"
                               elementFormDefault="qualified">
                      <xs:element name="value">
                        <xs:complexType>
                          <xs:attribute name="n" type="xs:integer" use="required"/>
                          <xs:assert test="@n gt 0"/>
                        </xs:complexType>
                      </xs:element>
                    </xs:schema>
                    """);
        String query =
                "import schema namespace t = 'urn:test' at 'types.xsd'; " + "validate strict { <t:value n='%s'/> }";
        for (String version : new String[] {"1.0", "1.1", "1.0", "1.1"}) {
            var config = RumbleConfiguration.builder()
                    .configureSemantics(s -> s.queryLanguage("xquery31").xsdVersion(version))
                    .build();
            var rumble = new Rumble(config);
            var base = directory.resolve("query.xq").toUri();
            if (version.equals("1.0")) {
                Assertions.assertThrows(SchemaImportException.class, () -> rumble.runQuery(query.formatted("1"), base)
                        .getAsList());
            } else {
                Assertions.assertEquals(
                        1,
                        rumble.runQuery(query.formatted("1"), base).getAsList().size());
                Assertions.assertThrows(
                        org.rumbledb.exceptions.InvalidInstanceException.class,
                        () -> rumble.runQuery(query.formatted("0"), base).getAsList());
            }
        }
    }
}
