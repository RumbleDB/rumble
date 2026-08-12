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
import java.util.List;
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
import org.rumbledb.context.InScopeSchemaTypes;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.SchemaImportException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

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
    public void registersGeneralizedAtomicTypesAndKeepsListsAsSchemaMetadata(@TempDir Path directory) throws Exception {
        Files.writeString(
                directory.resolve("types.xsd"),
                schema(
                        NAMESPACE,
                        """
                        <xs:simpleType name="Code">
                          <xs:restriction base="xs:string"/>
                        </xs:simpleType>
                        <xs:simpleType name="RestrictedCode">
                          <xs:restriction base="t:Code">
                            <xs:pattern value="[A-Z]+"/>
                          </xs:restriction>
                        </xs:simpleType>
                        <xs:simpleType name="Codes">
                          <xs:list itemType="t:Code"/>
                        </xs:simpleType>
                        <xs:simpleType name="CodeOrInteger">
                          <xs:union memberTypes="t:Code xs:integer"/>
                        </xs:simpleType>
                        <xs:simpleType name="RestrictedCodeOrInteger">
                          <xs:restriction base="t:CodeOrInteger">
                            <xs:enumeration value="A"/>
                          </xs:restriction>
                        </xs:simpleType>
                        <xs:complexType name="Record"/>
                        <xs:element name="AnonymousValue">
                          <xs:simpleType>
                            <xs:restriction base="xs:string"/>
                          </xs:simpleType>
                        </xs:element>
                        """));

        MainModule module = compile(
                "import schema namespace t = \"urn:test\" at \"types.xsd\"; 1 instance of t:CodeOrInteger",
                directory.resolve("query.xq").toUri(),
                new ResourceResolver());

        InScopeSchemaTypes inScopeTypes = module.getStaticContext().getInScopeSchemaTypes();
        Name codeName = new Name(NAMESPACE, "t", "Code");
        Name restrictedCodeName = new Name(NAMESPACE, "t", "RestrictedCode");
        Name codesName = new Name(NAMESPACE, "t", "Codes");
        Name unionName = new Name(NAMESPACE, "t", "CodeOrInteger");
        Name recordName = new Name(NAMESPACE, "t", "Record");
        ItemType code = inScopeTypes.getInScopeSchemaType(codeName);
        ItemType restrictedCode = inScopeTypes.getInScopeSchemaType(restrictedCodeName);
        ItemType union = inScopeTypes.getInScopeSchemaType(unionName);
        XmlSchemaCatalog catalog = module.getStaticContext().getXmlSchemaCatalog();
        XSTypeDefinition restrictedCodeDefinition =
                catalog.getTypeDefinition(restrictedCodeName).orElseThrow();
        XSTypeDefinition codesDefinition = catalog.getTypeDefinition(codesName).orElseThrow();
        XSTypeDefinition unionDefinition = catalog.getTypeDefinition(unionName).orElseThrow();
        XSTypeDefinition recordDefinition =
                catalog.getTypeDefinition(recordName).orElseThrow();
        XSTypeDefinition anonymousDefinition = catalog.getSchemaModel()
                .getElementDeclaration("AnonymousValue", NAMESPACE)
                .getTypeDefinition();

        Assertions.assertEquals(BuiltinTypesCatalogue.stringItem, code.getBaseType());
        Assertions.assertEquals(code, restrictedCode.getBaseType());
        Assertions.assertEquals(BuiltinTypesCatalogue.stringItem, restrictedCode.getPrimitiveType());
        Assertions.assertSame(
                restrictedCode,
                catalog.getAtomicItemType(restrictedCodeDefinition).orElseThrow());
        Assertions.assertTrue(restrictedCode.isSubtypeOf(code));
        Assertions.assertTrue(restrictedCode.isSubtypeOf(BuiltinTypesCatalogue.stringItem));
        Assertions.assertTrue(union.isUnionType());
        Assertions.assertEquals(List.of(code, BuiltinTypesCatalogue.integerItem), union.getTypes());
        Assertions.assertTrue(code.isSubtypeOf(union));
        Assertions.assertTrue(union.isSubtypeOf(BuiltinTypesCatalogue.atomicItem));
        Assertions.assertSame(
                union, catalog.getGeneralizedAtomicItemType(unionDefinition).orElseThrow());
        Assertions.assertSame(code, catalog.getListItemType(codesDefinition).orElseThrow());
        XmlSchemaTypeAnnotation listAnnotation = catalog.getTypeAnnotation(codesDefinition);
        Assertions.assertEquals(codesName, listAnnotation.name());
        Assertions.assertTrue(listAnnotation.isDerivedFrom(new Name(Name.XS_NS, "xs", "anySimpleType")));
        Assertions.assertTrue(
                catalog.getTypeAnnotation(recordDefinition).isDerivedFrom(new Name(Name.XS_NS, "xs", "anyType")));
        XmlSchemaTypeAnnotation anonymousAnnotation = catalog.getTypeAnnotation(anonymousDefinition);
        Assertions.assertSame(anonymousAnnotation, catalog.getTypeAnnotation(anonymousDefinition));
        Assertions.assertEquals(
                "http://rumbledb.org/anonymous-schema-types",
                anonymousAnnotation.name().getNamespace());
        Assertions.assertTrue(anonymousAnnotation.isDerivedFrom(BuiltinTypesCatalogue.stringItem.getName()));
        Assertions.assertFalse(inScopeTypes.checkInScopeSchemaTypeExists(codesName));
        Assertions.assertFalse(
                inScopeTypes.checkInScopeSchemaTypeExists(new Name(NAMESPACE, "t", "RestrictedCodeOrInteger")));
        Assertions.assertFalse(inScopeTypes.checkInScopeSchemaTypeExists(recordName));
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
