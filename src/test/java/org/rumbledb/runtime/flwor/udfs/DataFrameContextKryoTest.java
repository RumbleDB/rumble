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

package org.rumbledb.runtime.flwor.udfs;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.api.Item;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.StringItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.types.BuiltinTypesCatalogue;

public class DataFrameContextKryoTest {

    @Test
    public void kryoRoundTripsItemsWithoutNoArgumentConstructors() {
        Assertions.assertThrows(NoSuchMethodException.class, StringItem.class::getDeclaredConstructor);
        Assertions.assertThrows(NoSuchMethodException.class, AnnotatedItem.class::getDeclaredConstructor);
        Assertions.assertThrows(NoSuchMethodException.class, FunctionIdentifier.class::getDeclaredConstructor);

        ItemFactory factory = ItemFactory.getInstance();

        FunctionIdentifier identifier = new FunctionIdentifier(new Name("urn:test", "t", "function"), 2);
        Assertions.assertEquals(identifier, roundTripObject(identifier));

        Item stringCopy = roundTrip(factory.createStringItem("value"));
        Assertions.assertEquals("value", stringCopy.getStringValue());

        Item arrayCopy = roundTrip(factory.createArrayItem(
                new ArrayList<>(List.of(factory.createStringItem("first"), factory.createIntItem(2))), false));
        Assertions.assertEquals(2, arrayCopy.getSize());
        Assertions.assertEquals("first", arrayCopy.getItemAt(0).getStringValue());
        Assertions.assertEquals(2, arrayCopy.getItemAt(1).getIntValue());

        Item objectCopy = roundTrip(factory.createObjectItem(
                new ArrayList<>(List.of("key")),
                new ArrayList<>(List.of(factory.createStringItem("object value"))),
                ExceptionMetadata.EMPTY_METADATA,
                false));
        Assertions.assertEquals("object value", objectCopy.getItemByKey("key").getStringValue());

        Item annotatedCopy = roundTrip(
                factory.createAnnotatedItem(factory.createStringItem("en"), BuiltinTypesCatalogue.languageItem));
        Assertions.assertEquals("en", annotatedCopy.getStringValue());
        Assertions.assertEquals(
                BuiltinTypesCatalogue.languageItem.getName(),
                annotatedCopy.getDynamicType().getName());

        TextItem text = new TextItem("node text");
        text.setXmlDocumentPosition("document.xml", 1);
        Item textCopy = roundTrip(text);
        Assertions.assertEquals("node text", textCopy.getStringValue());
        Assertions.assertEquals(text.getXmlDocumentPosition(), textCopy.getXmlDocumentPosition());
    }

    private Item roundTrip(Item item) {
        return (Item) roundTripObject(item);
    }

    private Object roundTripObject(Object value) {
        DataFrameContext context = new DataFrameContext();
        context.getOutput().clear();
        context.getKryo().writeClassAndObject(context.getOutput(), value);
        byte[] bytes = context.getOutput().toBytes();
        context.getInput().setBuffer(bytes);
        return context.getKryo().readClassAndObject(context.getInput());
    }
}
