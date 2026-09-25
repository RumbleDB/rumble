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

import java.io.File;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSTypeDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.types.BuiltinTypesCatalogue;

/**
 * Checks the internal identities assigned to unnamed schema types. The XQuery regression tests show that
 * validation produces usable values, but could still pass if node annotations and atomic values received
 * different type names, or if distinct schema definitions accidentally shared one identity.
 */
class AnonymousSchemaTypeIdentityTest {
    /**
     * The same definition must receive the same name in both mapping paths, regardless of which runs first.
     * Separate definitions must remain distinct even when they impose identical restrictions.
     */
    @Test
    void sharesNamesAcrossNodeAndAtomicMappingWithoutMergingDistinctDefinitions() {
        XSModel model = new XMLSchemaLoader()
                .loadURI(new File("src/test/resources/test_files/xquery-parser/schema-cast/AnonymousSchemaTypes.xsd")
                        .toURI()
                        .toString());
        assertNotNull(model);
        // These two elements each declare their own anonymous restriction of xs:integer.
        XSTypeDefinition first =
                model.getElementDeclaration("count", "urn:anonymous-types").getTypeDefinition();
        XSTypeDefinition second =
                model.getElementDeclaration("otherCount", "urn:anonymous-types").getTypeDefinition();
        XmlSchemaTypeMapper mapper = new XmlSchemaTypeMapper();
        // Map the first definition as a node annotation first, and the second as an atomic type first.
        var annotation = mapper.mapTypeAnnotation(first);
        var atomic = mapper.mapGeneralizedAtomicType(first).orElseThrow();
        var otherAtomic = mapper.mapGeneralizedAtomicType(second).orElseThrow();
        assertEquals(annotation.name(), atomic.getName());
        assertEquals(otherAtomic.getName(), mapper.mapTypeAnnotation(second).name());
        assertNotEquals(atomic, otherAtomic);
        // Assigning an internal name must preserve the original inheritance relationship.
        assertTrue(atomic.isSubtypeOf(BuiltinTypesCatalogue.integerItem));
        assertTrue(annotation.isDerivedFrom(BuiltinTypesCatalogue.integerItem.getName()));
        // Looking up the same definition again must not generate a new identity.
        assertEquals(
                atomic.getName(),
                mapper.mapGeneralizedAtomicType(first).orElseThrow().getName());
    }

    /**
     * Checks that an anonymous base type is retained in a node's annotation hierarchy.
     * Skipping it could leave atomic subtyping correct while node type matching gives a different answer.
     */
    @Test
    void retainsAnonymousBaseTypesInAnnotationHierarchy() {
        // The inline pattern restriction and the additional length restriction produce two anonymous
        // simple types in Xerces, with one derived from the other.
        DOMInputImpl input = new DOMInputImpl();
        input.setStringData(
                """
                    <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:base" targetNamespace="urn:base">
                        <xs:complexType name="Base"><xs:simpleContent><xs:extension base="xs:string"/></xs:simpleContent></xs:complexType>
                        <xs:complexType name="Restricted"><xs:simpleContent><xs:restriction base="t:Base">
                            <xs:simpleType><xs:restriction base="xs:string"><xs:pattern value="[A-Z]+"/></xs:restriction></xs:simpleType>
                            <xs:maxLength value="3"/>
                        </xs:restriction></xs:simpleContent></xs:complexType>
                    </xs:schema>
                    """);
        XSModel model = new XMLSchemaLoader().load(input);
        assertNotNull(model);
        var complex = (XSComplexTypeDefinition) model.getTypeDefinition("Restricted", "urn:base");
        var simple = complex.getSimpleType();
        assertTrue(simple.getAnonymous());
        assertTrue(simple.getBaseType().getAnonymous());
        XmlSchemaTypeMapper mapper = new XmlSchemaTypeMapper();
        var base = mapper.mapGeneralizedAtomicType(simple.getBaseType()).orElseThrow();
        var derived = mapper.mapGeneralizedAtomicType(simple).orElseThrow();
        assertTrue(derived.isSubtypeOf(base));
        assertTrue(mapper.mapTypeAnnotation(simple).isDerivedFrom(base.getName()));
    }
}
