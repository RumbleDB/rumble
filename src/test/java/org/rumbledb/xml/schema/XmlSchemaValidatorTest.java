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
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.api.Item;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.exceptions.TypedValueUnavailableException;
import org.rumbledb.exceptions.ValidateException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.types.BuiltinTypesCatalogue;

public class XmlSchemaValidatorTest {

    private static final String NAMESPACE = "urn:test";
    private static final ItemFactory FACTORY = ItemFactory.getInstance();

    @Test
    public void validatesAndBuildsASeparateSchemaAnnotatedDocument(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item count = element("count", text(" 42 "));
        Item numbers = element("numbers", text("1  2"));
        Item choice = element("choice", text("7"));
        Item root = element("root", List.of(count, numbers, choice));
        root.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("t", NAMESPACE));
        root.addParentToDescendants();
        Item input = FACTORY.createXmlDocumentNode(List.of(
                FACTORY.createXmlProcessingInstructionNode("before", "value"),
                root,
                FACTORY.createXmlCommentNode("after")));
        input.addParentToDescendants();

        Item result = new XmlSchemaValidator(catalog).validateStrict(input, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(result.isDocumentNode());
        Assertions.assertNotSame(input, result);
        Assertions.assertTrue(result.children().get(0).isProcessingInstructionNode());
        Assertions.assertTrue(result.children().get(2).isCommentNode());
        Item validatedRoot = result.children().get(1);
        Assertions.assertSame(result, validatedRoot.parent());
        Assertions.assertThrows(TypedValueUnavailableException.class, validatedRoot::typedValue);
        Assertions.assertEquals(
                new Name(NAMESPACE, null, "RootType"),
                validatedRoot.getSchemaTypeAnnotation().name());
        Assertions.assertFalse(validatedRoot.nilled().get(0).getBooleanValue());

        Item validatedCount = validatedRoot.children().get(0);
        Assertions.assertEquals("42", validatedCount.getStringValue());
        Assertions.assertEquals(
                new Name(NAMESPACE, null, "Count"),
                validatedCount.getSchemaTypeAnnotation().name());
        Assertions.assertEquals("42", validatedCount.typedValue().get(0).getStringValue());

        Item validatedNumbers = validatedRoot.children().get(1);
        Assertions.assertEquals("1 2", validatedNumbers.getStringValue());
        Assertions.assertEquals(2, validatedNumbers.typedValue().size());
        Assertions.assertTrue(validatedNumbers.typedValue().stream()
                .allMatch(value -> value.getDynamicType().equals(BuiltinTypesCatalogue.integerItem)));

        Item validatedChoice = validatedRoot.children().get(2);
        Assertions.assertEquals(
                new Name(NAMESPACE, null, "IntegerOrWord"),
                validatedChoice.getSchemaTypeAnnotation().name());
        Assertions.assertEquals(
                BuiltinTypesCatalogue.integerItem,
                validatedChoice.typedValue().get(0).getDynamicType());

        Item defaultedAttribute = validatedRoot.attributes().stream()
                .filter(attribute -> "state".equals(attribute.nodeName().getLocalName()))
                .findFirst()
                .orElseThrow();
        Assertions.assertEquals("ready", defaultedAttribute.getStringValue());
        Assertions.assertEquals(
                BuiltinTypesCatalogue.tokenItem,
                defaultedAttribute.typedValue().get(0).getDynamicType());
    }

    @Test
    public void supportsNilledElementsAndIdentityProperties(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item nilled = element(
                "nilValue",
                List.of(),
                List.of(FACTORY.createXmlAttributeNode(new Name(Name.XSI_NS, "xsi", "nil"), "true")));
        nilled.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("t", NAMESPACE));
        nilled.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("xsi", Name.XSI_NS));

        Item validated = new XmlSchemaValidator(catalog).validateStrict(nilled, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertTrue(validated.nilled().get(0).getBooleanValue());
        Assertions.assertTrue(validated.typedValue().isEmpty());

        Item identifier = element("identifier", text("value"));
        identifier.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("t", NAMESPACE));
        Item validatedIdentifier =
                new XmlSchemaValidator(catalog).validateStrict(identifier, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertTrue(validatedIdentifier.isId());
        Assertions.assertFalse(validatedIdentifier.isIdrefs());
    }

    @Test
    public void validatesAnUndeclaredElementAgainstAnImportedType(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item input = FACTORY.createXmlElementNode(new Name(null, null, "value"), text("18"), List.of());

        Item result = new XmlSchemaValidator(catalog)
                .validateType(input, new Name(NAMESPACE, "t", "Count"), ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals(
                new Name(NAMESPACE, null, "Count"),
                result.getSchemaTypeAnnotation().name());
        Assertions.assertEquals("18", result.typedValue().get(0).getStringValue());
    }

    @Test
    public void laxValidationUsesAnyTypeForAnUndeclaredRoot(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item input = FACTORY.createXmlElementNode(new Name("urn:unknown", "u", "unknown"), text("value"), List.of());
        input.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("u", "urn:unknown"));

        Item result = new XmlSchemaValidator(catalog).validateLax(input, ExceptionMetadata.EMPTY_METADATA);

        Assertions.assertEquals(
                new Name(Name.XS_NS, "xs", "anyType"),
                result.getSchemaTypeAnnotation().name());
        Assertions.assertEquals("value", result.typedValue().get(0).getStringValue());
    }

    @Test
    public void reportsInvalidSchemaInstances(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item input = element("countValue", text("not-an-integer"));
        input.addOrReplaceNamespace(FACTORY.createXmlNamespaceNode("t", NAMESPACE));

        Assertions.assertThrows(InvalidInstanceException.class, () -> new XmlSchemaValidator(catalog)
                .validateStrict(input, ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    public void strictValidationRequiresAGlobalElementDeclaration(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = compileCatalog(directory);
        Item input = FACTORY.createXmlElementNode(new Name("urn:unknown", "u", "unknown"), List.of(), List.of());

        ValidateException exception =
                Assertions.assertThrows(ValidateException.class, () -> new XmlSchemaValidator(catalog)
                        .validateStrict(input, ExceptionMetadata.EMPTY_METADATA));

        Assertions.assertEquals("XQDY0084", exception.getErrorCode().toString());
    }

    private static XmlSchemaCatalog compileCatalog(Path directory) throws Exception {
        Files.writeString(directory.resolve("types.xsd"), schema());
        MainModule module = compile(
                "import schema namespace t = \"urn:test\" at \"types.xsd\"; 1",
                directory.resolve("query.xq").toUri());
        return module.getStaticContext().getXmlSchemaCatalog();
    }

    private static MainModule compile(String query, URI queryUri) {
        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        return VisitorHelpers.parseMainModule(
                query,
                queryUri,
                new CompilationConfiguration(configuration, new ResourceResolver()),
                ExternalBindings.empty());
    }

    private static Item element(String localName, List<Item> children) {
        return element(localName, children, List.of());
    }

    private static Item element(String localName, List<Item> children, List<Item> attributes) {
        return FACTORY.createXmlElementNode(new Name(NAMESPACE, "t", localName), children, attributes);
    }

    private static List<Item> text(String value) {
        List<Item> result = new ArrayList<>();
        result.add(FACTORY.createXmlTextNode(value));
        return result;
    }

    private static String schema() {
        return """
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                           xmlns:t="urn:test"
                           targetNamespace="urn:test"
                           elementFormDefault="qualified">
                  <xs:simpleType name="Count">
                    <xs:restriction base="xs:integer"/>
                  </xs:simpleType>
                  <xs:simpleType name="Numbers">
                    <xs:list itemType="xs:integer"/>
                  </xs:simpleType>
                  <xs:simpleType name="IntegerOrWord">
                    <xs:union memberTypes="xs:integer xs:string"/>
                  </xs:simpleType>
                  <xs:complexType name="RootType">
                    <xs:sequence>
                      <xs:element name="count" type="t:Count"/>
                      <xs:element name="numbers" type="t:Numbers"/>
                      <xs:element name="choice" type="t:IntegerOrWord"/>
                    </xs:sequence>
                    <xs:attribute name="state" type="xs:token" default=" ready "/>
                  </xs:complexType>
                  <xs:element name="root" type="t:RootType"/>
                  <xs:element name="countValue" type="t:Count"/>
                  <xs:element name="nilValue" type="xs:integer" nillable="true"/>
                  <xs:element name="identifier" type="xs:ID"/>
                </xs:schema>
                """;
    }
}
