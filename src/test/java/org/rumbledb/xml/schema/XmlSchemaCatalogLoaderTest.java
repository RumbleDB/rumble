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

package org.rumbledb.xml.schema;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.SchemaImportException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.resources.ResourceResolver;

public class XmlSchemaCatalogLoaderTest {

    private static final String NAMESPACE = "urn:test";

    @Test
    public void loadsImportedTypesAndRelativeIncludes(@TempDir Path directory) throws Exception {
        Files.writeString(
                directory.resolve("included.xsd"),
                schema(null, "<xs:simpleType name=\"Code\"><xs:restriction base=\"xs:string\"/></xs:simpleType>"));
        Path rootSchema = directory.resolve("root.xsd");
        Files.writeString(rootSchema, schema(NAMESPACE, "<xs:include schemaLocation=\"included.xsd\"/>"));

        MainModule module = compile(
                "import schema namespace t = \"urn:test\" at \"root.xsd\"; 1",
                directory.resolve("query.xq").toUri(),
                new ResourceResolver());

        XmlSchemaCatalog catalog = module.getStaticContext().getXmlSchemaCatalog();
        Assertions.assertNotNull(catalog);
        XSTypeDefinition type =
                catalog.getTypeDefinition(new Name(NAMESPACE, "t", "Code")).orElseThrow();
        Assertions.assertEquals(XSSimpleTypeDefinition.VARIETY_ATOMIC, ((XSSimpleTypeDefinition) type).getVariety());
    }

    @Test
    public void usesResourceMappingsWhenTheImportHasNoLocationHint(@TempDir Path directory) throws Exception {
        URI logicalSchemaUri = URI.create(NAMESPACE);
        Path schema = directory.resolve("mapped.xsd");
        Files.writeString(
                schema,
                schema(NAMESPACE, "<xs:simpleType name=\"Code\"><xs:restriction base=\"xs:string\"/></xs:simpleType>"));

        MainModule module = compile(
                "import schema namespace t = \"urn:test\"; 1",
                directory.resolve("query.xq").toUri(),
                new ResourceResolver(Map.of(logicalSchemaUri, schema.toUri())));

        Assertions.assertTrue(module.getStaticContext()
                .getXmlSchemaCatalog()
                .getTypeDefinition(new Name(NAMESPACE, "t", "Code"))
                .isPresent());
    }

    @Test
    public void rejectsAMismatchedTargetNamespace(@TempDir Path directory) throws Exception {
        Files.writeString(directory.resolve("wrong.xsd"), schema("urn:other", ""));

        SchemaImportException exception = Assertions.assertThrows(
                SchemaImportException.class,
                () -> compile(
                        "import schema namespace t = \"urn:test\" at \"wrong.xsd\"; 1",
                        directory.resolve("query.xq").toUri(),
                        new ResourceResolver()));

        Assertions.assertEquals("XQST0059", exception.getErrorCode().toString());
    }

    @Test
    public void rejectsAnInvalidSchema(@TempDir Path directory) throws Exception {
        Files.writeString(
                directory.resolve("invalid.xsd"),
                schema(
                        NAMESPACE,
                        "<xs:simpleType name=\"Broken\"><xs:restriction base=\"xs:missing\"/></xs:simpleType>"));

        SchemaImportException exception = Assertions.assertThrows(
                SchemaImportException.class,
                () -> compile(
                        "import schema namespace t = \"urn:test\" at \"invalid.xsd\"; 1",
                        directory.resolve("query.xq").toUri(),
                        new ResourceResolver()));

        Assertions.assertEquals("XQST0059", exception.getErrorCode().toString());
    }

    @Test
    public void reportsAMissingSchemaAsXqst0059(@TempDir Path directory) {
        SchemaImportException exception = Assertions.assertThrows(
                SchemaImportException.class,
                () -> compile(
                        "import schema namespace t = \"urn:test\" at \"missing.xsd\"; 1",
                        directory.resolve("query.xq").toUri(),
                        new ResourceResolver()));

        Assertions.assertEquals("XQST0059", exception.getErrorCode().toString());
    }

    private static MainModule compile(String query, URI queryUri, ResourceResolver resolver) {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        return VisitorHelpers.parseMainModule(
                query, queryUri, new CompilationConfiguration(configuration, resolver), ExternalBindings.empty());
    }

    private static String schema(String targetNamespace, String declarations) {
        String namespaceAttributes = targetNamespace == null
                ? ""
                : " targetNamespace=\"" + targetNamespace + "\" xmlns:t=\"" + targetNamespace + "\"";
        return "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\""
                + namespaceAttributes
                + ">"
                + declarations
                + "</xs:schema>";
    }
}
