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
public class XQueryValidateXmlSchemaTest {

    private static Rumble rumble;

    @BeforeAll
    public static void setUp() {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        rumble = new Rumble(configuration);
    }

    @Test
    public void validatesAgainstImportedAtomicAndComplexTypes(@TempDir Path directory) throws Exception {
        Path schema = writeSchema(directory);
        String schemaImport = schemaImport(schema);

        assertTrue(schemaImport + "data(validate type t:Count { <value>42</value> }) instance of t:Count");
        assertTrue(schemaImport
                + "let $validated := validate type t:Record { "
                + "<t:record xmlns:t=\"urn:test\"><t:amount>42</t:amount></t:record> } "
                + "return data($validated/t:amount) instance of t:Count");
    }

    @Test
    public void executesStrictAndLaxValidation(@TempDir Path directory) throws Exception {
        Path schema = writeSchema(directory);
        String schemaImport = schemaImport(schema);

        assertTrue(schemaImport
                + "let $validated := validate strict { "
                + "<t:root xmlns:t=\"urn:test\"><t:amount>42</t:amount></t:root> } "
                + "return data($validated/t:amount) instance of t:Count");
        assertTrue(schemaImport
                + "let $validated := validate lax { "
                + "<u:unknown xmlns:u=\"urn:unknown\" xmlns:t=\"urn:test\">"
                + "<t:countValue>42</t:countValue></u:unknown> } "
                + "return data($validated/t:countValue) instance of t:Count");
    }

    @Test
    public void strictAndLaxWorkWithoutUserSchemaImports() {
        assertErrorCode("validate strict { <unknown/> }", "XQDY0084");
        assertTrue("data(validate lax { <unknown>value</unknown> }) instance of xs:untypedAtomic");
    }

    @Test
    public void reportsInvalidImportedSchemaInstances(@TempDir Path directory) throws Exception {
        Path schema = writeSchema(directory);
        assertErrorCode(
                schemaImport(schema) + "validate strict { <t:countValue xmlns:t=\"urn:test\">wrong</t:countValue> }",
                "XQDY0027");
    }

    private static Path writeSchema(Path directory) throws Exception {
        Path schema = directory.resolve("types.xsd");
        Files.writeString(
                schema,
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                               xmlns:t="urn:test"
                               targetNamespace="urn:test"
                               elementFormDefault="qualified">
                      <xs:simpleType name="Count">
                        <xs:restriction base="xs:integer"/>
                      </xs:simpleType>
                      <xs:complexType name="Record">
                        <xs:sequence>
                          <xs:element name="amount" type="t:Count"/>
                        </xs:sequence>
                      </xs:complexType>
                      <xs:element name="root" type="t:Record"/>
                      <xs:element name="countValue" type="t:Count"/>
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
