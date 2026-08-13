/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iq;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.RumbleException;

@Timeout(1000)
public class XQueryXmlSchemaCastTest {

    private static Rumble rumble;

    @BeforeAll
    public static void setUp() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        rumble = new Rumble(configuration);
    }

    @Test
    public void castsAListWhoseItemTypeIsANamespaceSensitiveUnion(@TempDir Path directory) throws Exception {
        String schemaImport = schemaImport(writeSchema(directory));
        assertTrue(schemaImport
                + "let $result := (\"a b xs:integer\" cast as t:ListOfUnions) "
                + "return count($result) eq 3 "
                + "and $result[1] instance of xs:NCName "
                + "and $result[1] instance of t:SensitiveUnion "
                + "and $result[3] instance of xs:QName");
    }

    @Test
    public void appliesListFacetsAndCastableUsesTheSameValidation(@TempDir Path directory) throws Exception {
        String schemaImport = schemaImport(writeSchema(directory));
        assertTrue(schemaImport + "\"A B\" castable as t:TwoCodes");
        assertTrue(schemaImport + "not(\"A B C\" castable as t:TwoCodes)");
        assertErrorCode(schemaImport + "\"A B C\" cast as t:TwoCodes", "FORG0001");
    }

    @Test
    public void listTypeNamesRemainInvalidInSequenceTypes(@TempDir Path directory) throws Exception {
        String schemaImport = schemaImport(writeSchema(directory));
        assertErrorCode(
                schemaImport + "let $value as t:TwoCodes := \"A B\" cast as t:TwoCodes return $value", "XPST0051");
    }

    private static Path writeSchema(Path directory) throws Exception {
        Path schema = directory.resolve("types.xsd");
        Files.writeString(
                schema,
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                               xmlns:t="urn:test"
                               targetNamespace="urn:test">
                      <xs:simpleType name="SensitiveUnion">
                        <xs:union memberTypes="xs:NCName xs:QName"/>
                      </xs:simpleType>
                      <xs:simpleType name="ListOfUnions">
                        <xs:list itemType="t:SensitiveUnion"/>
                      </xs:simpleType>
                      <xs:simpleType name="TwoCodes">
                        <xs:restriction base="t:ListOfUnions">
                          <xs:length value="2"/>
                        </xs:restriction>
                      </xs:simpleType>
                    </xs:schema>
                    """);
        return schema;
    }

    private static String schemaImport(Path schema) {
        return "import schema namespace t = \"urn:test\" at \"" + schema.toUri() + "\"; ";
    }

    private static void assertErrorCode(String query, String expectedCode) {
        RumbleException exception =
                Assertions.assertThrows(RumbleException.class, () -> rumble.runQueryToString(query));
        Assertions.assertEquals(expectedCode, exception.getErrorCode().toString());
    }

    private static void assertTrue(String query) {
        Assertions.assertTrue(rumble.runQuery(query).getAsList().get(0).getBooleanValue());
    }
}
