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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xs.XSTypeDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.rumbledb.api.Item;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.module.SchemaImport;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.resources.ResourceResolver;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Covers the internal conversion from Xerces validated values to Rumble items. Annotation tests exercise
 * {@code validate type}, but cannot directly provide Xerces {@link ValidatedInfo} instances or inspect the resulting
 * item representation, such as a selected union member or the items produced by a schema list.
 */
public class XercesTypedValueConverterTest {

    private static final XercesBuiltinAtomicTypeMapper TYPE_MAPPER = new XercesBuiltinAtomicTypeMapper();
    private static final XercesTypedValueConverter CONVERTER = new XercesTypedValueConverter(new XmlSchemaTypeMapper());

    @Test
    public void mapsBuiltInAtomicTypesByExpandedName() {
        List<String> typeNames = List.of(
                "string",
                "boolean",
                "decimal",
                "float",
                "double",
                "duration",
                "dateTime",
                "time",
                "date",
                "gYearMonth",
                "gYear",
                "gMonthDay",
                "gDay",
                "gMonth",
                "hexBinary",
                "base64Binary",
                "anyURI",
                "QName",
                "NOTATION",
                "normalizedString",
                "token",
                "language",
                "NMTOKEN",
                "Name",
                "NCName",
                "ID",
                "IDREF",
                "ENTITY",
                "integer",
                "nonPositiveInteger",
                "negativeInteger",
                "long",
                "int",
                "short",
                "byte",
                "nonNegativeInteger",
                "unsignedLong",
                "unsignedInt",
                "unsignedShort",
                "unsignedByte",
                "positiveInteger");

        for (String typeName : typeNames) {
            ItemType mappedType = TYPE_MAPPER.map(builtinType(typeName)).orElseThrow();
            Assertions.assertEquals(new Name(Name.XS_NS, "xs", typeName), mappedType.getName());
        }
    }

    @Test
    public void doesNotMapNonAtomicSchemaTypes() {
        Assertions.assertTrue(TYPE_MAPPER.map(SchemaGrammar.fAnyType).isEmpty());
        Assertions.assertTrue(TYPE_MAPPER.map(builtinType("NMTOKENS")).isEmpty());
    }

    @Test
    public void convertsStringsAndAppliesXercesWhitespaceNormalization() throws Exception {
        Item value = convert("normalizedString", "a\tb\nc", Map.of());

        Assertions.assertEquals("a b c", value.getStringValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.normalizedStringItem, value.getDynamicType());
    }

    @Test
    public void convertsBooleanAndNumericValuesWithTheirExactSchemaTypes() throws Exception {
        Item bool = convert("boolean", "1", Map.of());
        Item integer = convert("integer", "0042", Map.of());
        Item unsignedByte = convert("unsignedByte", "255", Map.of());
        Item decimal = convert("decimal", "003.140", Map.of());
        Item doubleValue = convert("double", "INF", Map.of());

        Assertions.assertTrue(bool.getBooleanValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.booleanItem, bool.getDynamicType());
        Assertions.assertEquals(BigInteger.valueOf(42), integer.getIntegerValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.integerItem, integer.getDynamicType());
        Assertions.assertEquals(BigInteger.valueOf(255), unsignedByte.getIntegerValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.unsignedByteItem, unsignedByte.getDynamicType());
        Assertions.assertEquals(new BigDecimal("3.140"), decimal.getDecimalValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.decimalItem, decimal.getDynamicType());
        Assertions.assertEquals(Double.POSITIVE_INFINITY, doubleValue.getDoubleValue());
    }

    @Test
    public void convertsTemporalDurationAndBinaryValues() throws Exception {
        Item dateTime = convert("dateTime", "2025-02-03T04:05:06+01:00", Map.of());
        Item duration = convert("duration", "P1Y2M3DT4H5M6S", Map.of());
        Item hexBinary = convert("hexBinary", "0A0B", Map.of());
        Item base64Binary = convert("base64Binary", "AQID", Map.of());

        Assertions.assertEquals(BuiltinTypesCatalogue.dateTimeItem, dateTime.getDynamicType());
        Assertions.assertEquals("2025-02-03T04:05:06+01:00", dateTime.getStringValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.durationItem, duration.getDynamicType());
        Assertions.assertEquals("P1Y2M3DT4H5M6S", duration.getStringValue());
        Assertions.assertArrayEquals(new byte[] {10, 11}, hexBinary.getBinaryValue());
        Assertions.assertArrayEquals(new byte[] {1, 2, 3}, base64Binary.getBinaryValue());
    }

    @Test
    public void convertsQNameFromTheExpandedXercesValue() throws Exception {
        Item value = convert("QName", "p:value", Map.of("p", "urn:test"));

        Assertions.assertEquals(new Name("urn:test", "p", "value"), value.getQNameValue());
        Assertions.assertEquals(BuiltinTypesCatalogue.QNameItem, value.getDynamicType());
    }

    @Test
    public void rejectsValuesWithoutASupportedRumbleRepresentation() throws Exception {
        ValidatedInfo notation = validate("NOTATION", "p:value", Map.of("p", "urn:test"));

        Assertions.assertThrows(OurBadException.class, () -> CONVERTER.convert(notation));
    }

    @Test
    public void convertsImportedAtomicRestrictionsWithTheCatalogType(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = loadCatalog(directory);
        XSSimpleType codeSchemaType = schemaType(catalog, "Code");
        ItemType codeItemType = catalog.getAtomicItemType(codeSchemaType).orElseThrow();

        List<Item> result = catalog.convertTypedValue(validate(codeSchemaType, "  ABC  ", Map.of()));

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("ABC", result.get(0).getStringValue());
        Assertions.assertSame(codeItemType, result.get(0).getDynamicType());
    }

    @Test
    public void convertsUnionValuesUsingTheSelectedAtomicMember(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = loadCatalog(directory);
        XSSimpleType unionType = schemaType(catalog, "IntegerOrCode");
        ItemType codeItemType =
                catalog.getAtomicItemType(schemaType(catalog, "Code")).orElseThrow();

        Item integer =
                catalog.convertTypedValue(validate(unionType, "42", Map.of())).get(0);
        Item code =
                catalog.convertTypedValue(validate(unionType, "ABC", Map.of())).get(0);

        Assertions.assertEquals(BigInteger.valueOf(42), integer.getIntegerValue());
        Assertions.assertSame(BuiltinTypesCatalogue.integerItem, integer.getDynamicType());
        Assertions.assertEquals("ABC", code.getStringValue());
        Assertions.assertSame(codeItemType, code.getDynamicType());
    }

    @Test
    public void convertsListsToAtomicSequencesIncludingUnionQNames(@TempDir Path directory) throws Exception {
        XmlSchemaCatalog catalog = loadCatalog(directory);

        List<Item> integers =
                catalog.convertTypedValue(validate(schemaType(catalog, "Integers"), " 1  02 3 ", Map.of()));
        List<Item> mixed = catalog.convertTypedValue(
                validate(schemaType(catalog, "IntegersOrQNames"), "1 p:value 2", Map.of("p", "urn:values")));

        Assertions.assertEquals(
                List.of(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3)),
                integers.stream().map(Item::getIntegerValue).toList());
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> integers.add(ItemFactory.getInstance().createIntegerItem("4")));
        Assertions.assertEquals(BigInteger.ONE, mixed.get(0).getIntegerValue());
        Assertions.assertEquals(
                new Name("urn:values", "p", "value"), mixed.get(1).getQNameValue());
        Assertions.assertSame(BuiltinTypesCatalogue.QNameItem, mixed.get(1).getDynamicType());
        Assertions.assertEquals(BigInteger.valueOf(2), mixed.get(2).getIntegerValue());
    }

    private static Item convert(String typeName, String lexicalValue, Map<String, String> namespaces) throws Exception {
        XSSimpleType schemaType = builtinType(typeName);
        List<Item> result = CONVERTER.convert(validate(schemaType, lexicalValue, namespaces));
        Assertions.assertEquals(1, result.size());
        return result.get(0);
    }

    private static ValidatedInfo validate(String typeName, String lexicalValue, Map<String, String> namespaces)
            throws InvalidDatatypeValueException {
        return validate(builtinType(typeName), lexicalValue, namespaces);
    }

    private static ValidatedInfo validate(XSSimpleType schemaType, String lexicalValue, Map<String, String> namespaces)
            throws InvalidDatatypeValueException {
        ValidatedInfo result = new ValidatedInfo();
        schemaType.validate(lexicalValue, new TestValidationContext(namespaces), result);
        return result;
    }

    private static XSSimpleType builtinType(String name) {
        XSTypeDefinition type = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(name);
        return Assertions.assertInstanceOf(XSSimpleType.class, type);
    }

    private static XSSimpleType schemaType(XmlSchemaCatalog catalog, String name) {
        XSTypeDefinition type =
                catalog.getTypeDefinition(new Name("urn:test", "t", name)).orElseThrow();
        return Assertions.assertInstanceOf(XSSimpleType.class, type);
    }

    private static XmlSchemaCatalog loadCatalog(Path directory) throws Exception {
        Files.writeString(
                directory.resolve("types.xsd"),
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                               xmlns:t="urn:test"
                               targetNamespace="urn:test">
                      <xs:simpleType name="Code">
                        <xs:restriction base="xs:token">
                          <xs:pattern value="[A-Z]+"/>
                        </xs:restriction>
                      </xs:simpleType>
                      <xs:simpleType name="IntegerOrCode">
                        <xs:union memberTypes="xs:integer t:Code"/>
                      </xs:simpleType>
                      <xs:simpleType name="Integers">
                        <xs:list itemType="xs:integer"/>
                      </xs:simpleType>
                      <xs:simpleType name="IntegerOrQName">
                        <xs:union memberTypes="xs:integer xs:QName"/>
                      </xs:simpleType>
                      <xs:simpleType name="IntegersOrQNames">
                        <xs:list itemType="t:IntegerOrQName"/>
                      </xs:simpleType>
                    </xs:schema>
                    """);

        RumbleConfiguration configuration = RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build();
        SchemaImport schemaImport = new SchemaImport(
                "urn:test",
                SchemaImport.BindingKind.PREFIX,
                "t",
                List.of("types.xsd"),
                ExceptionMetadata.EMPTY_METADATA);
        URI baseUri = directory.resolve("query.xq").toUri();
        return XmlSchemaCatalogLoader.load(
                        List.of(schemaImport),
                        baseUri,
                        new CompilationConfiguration(configuration, new ResourceResolver()))
                .orElseThrow();
    }

    private record TestValidationContext(Map<String, String> namespaces) implements ValidationContext {

        @Override
        public boolean needFacetChecking() {
            return true;
        }

        @Override
        public boolean needExtraChecking() {
            return true;
        }

        @Override
        public boolean needToNormalize() {
            return true;
        }

        @Override
        public boolean useNamespaces() {
            return true;
        }

        @Override
        public boolean isEntityDeclared(String name) {
            return false;
        }

        @Override
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        @Override
        public boolean isIdDeclared(String name) {
            return false;
        }

        @Override
        public void addId(String name) {}

        @Override
        public void addIdRef(String name) {}

        @Override
        public String getSymbol(String symbol) {
            return symbol;
        }

        @Override
        public String getURI(String prefix) {
            return this.namespaces.get(prefix);
        }

        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }
    }
}
