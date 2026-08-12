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

package org.rumbledb.items.xml;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TypedValueUnavailableException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.xml.BuiltinTypeValidator;
import org.rumbledb.types.BuiltinTypesCatalogue;

public class NodeTypedValueTest {

    private static final ItemFactory FACTORY = ItemFactory.getInstance();
    private static final Name ELEMENT_NAME = new Name("", "", "value");

    @Test
    public void untypedElementDerivesUntypedAtomicValueFromItsStringValue() {
        Item element = createElement("42");

        List<Item> typedValue = element.typedValue();

        Assertions.assertEquals(1, typedValue.size());
        Assertions.assertTrue(typedValue.get(0).isUntypedAtomic());
        Assertions.assertEquals("42", typedValue.get(0).getStringValue());
    }

    @Test
    public void annotatedElementReturnsStoredTypedValueWithoutRecastingItsLexicalValue() {
        Item element = createElement("0042");
        Item integer = FACTORY.createIntegerItem("42");
        element.setSchemaType(BuiltinTypesCatalogue.integerItem, List.of(integer));

        List<Item> typedValue = element.typedValue();

        Assertions.assertEquals(1, typedValue.size());
        Assertions.assertSame(integer, typedValue.get(0));
        Assertions.assertEquals(BuiltinTypesCatalogue.integerItem, element.getSchemaType());
    }

    @Test
    public void nodeCopyPreservesItsSchemaAnnotationAndTypedValue() {
        Item element = createElement("0042");
        Item integer = FACTORY.createIntegerItem("42");
        element.setSchemaType(BuiltinTypesCatalogue.integerItem, List.of(integer));

        Item copy = element.copy(false);

        Assertions.assertEquals(BuiltinTypesCatalogue.integerItem, copy.getSchemaType());
        Assertions.assertSame(integer, copy.typedValue().get(0));
    }

    @Test
    public void documentTypedValueRemainsUntypedWhenItsElementIsTyped() {
        Item element = createElement("42");
        Item document = FACTORY.createXmlDocumentNode(List.of(element));
        Item validatedDocument = BuiltinTypeValidator.validate(
                document, BuiltinTypesCatalogue.integerItem, ExceptionMetadata.EMPTY_METADATA);

        List<Item> typedValue = validatedDocument.typedValue();

        Assertions.assertEquals(1, typedValue.size());
        Assertions.assertTrue(typedValue.get(0).isUntypedAtomic());
        Assertions.assertEquals("42", typedValue.get(0).getStringValue());
        Assertions.assertTrue(
                validatedDocument.children().get(0).typedValue().get(0).isInteger());
    }

    @Test
    public void unavailableTypedValueRaisesFoty0012() {
        Item element = createElement("content");
        element.setSchemaType(BuiltinTypesCatalogue.stringItem);

        TypedValueUnavailableException exception =
                Assertions.assertThrows(TypedValueUnavailableException.class, element::atomizedValue);

        Assertions.assertEquals("FOTY0012", exception.getErrorCode().toString());
    }

    @Test
    public void clearingSchemaTypeRestoresUntypedBehavior() {
        Item element = createElement("42");
        element.setSchemaType(BuiltinTypesCatalogue.integerItem, List.of(FACTORY.createIntegerItem("42")));

        element.clearSchemaType();

        Assertions.assertNull(element.getSchemaType());
        Assertions.assertTrue(element.typeName().isEmpty());
        Assertions.assertTrue(element.typedValue().get(0).isUntypedAtomic());
    }

    @Test
    public void availableTypedValueSupportsImmutableSequencesAndEmptyValues() {
        Item first = FACTORY.createStringItem("first");
        Item second = FACTORY.createStringItem("second");
        Item element = createElement("first second");
        element.setSchemaType(BuiltinTypesCatalogue.stringItem, List.of(first, second));

        Assertions.assertEquals(List.of(first, second), element.typedValue());
        Assertions.assertThrows(
                UnsupportedOperationException.class, () -> element.typedValue().add(FACTORY.createStringItem("third")));

        element.setSchemaType(BuiltinTypesCatalogue.stringItem, Collections.emptyList());
        Assertions.assertTrue(element.typedValue().isEmpty());
    }

    @Test
    public void typedValueRejectsNonAtomicItems() {
        Assertions.assertThrows(OurBadException.class, () -> NodeTypedValue.available(List.of(createElement("x"))));
        Assertions.assertThrows(
                OurBadException.class, () -> NodeTypedValue.available(List.of(FACTORY.createNullItem())));
    }

    @Test
    public void rejectedTypedValueDoesNotPartiallyChangeTheNode() {
        Item element = createElement("42");
        Item integer = FACTORY.createIntegerItem("42");
        element.setSchemaType(BuiltinTypesCatalogue.integerItem, List.of(integer));

        Assertions.assertThrows(
                OurBadException.class,
                () -> element.setSchemaType(BuiltinTypesCatalogue.stringItem, List.of(createElement("invalid"))));

        Assertions.assertEquals(BuiltinTypesCatalogue.integerItem, element.getSchemaType());
        Assertions.assertSame(integer, element.typedValue().get(0));
    }

    @Test
    public void attributeRequiresAndStoresAnAvailableTypedValue() {
        Item attribute = FACTORY.createXmlAttributeNode(new Name("", "", "count"), "42");
        Item integer = FACTORY.createIntegerItem("42");

        Assertions.assertThrows(
                OurBadException.class, () -> attribute.setSchemaType(BuiltinTypesCatalogue.integerItem));
        attribute.setSchemaType(BuiltinTypesCatalogue.integerItem, List.of(integer));

        Assertions.assertSame(integer, attribute.typedValue().get(0));
        Assertions.assertSame(integer, attribute.copy(false).typedValue().get(0));
    }

    private static Item createElement(String value) {
        return FACTORY.createXmlElementNode(
                ELEMENT_NAME, List.of(FACTORY.createXmlTextNode(value)), Collections.emptyList());
    }
}
