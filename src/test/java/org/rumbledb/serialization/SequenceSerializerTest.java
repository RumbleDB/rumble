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
package org.rumbledb.serialization;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

class SequenceSerializerTest {
    private static final ItemFactory ITEMS = ItemFactory.getInstance();

    @Test
    void textFlattensNestedArraysAndSpacesAdjacentAtomicValuesWhenSeparatorIsAbsent() {
        SerializationParameters parameters = new SerializationParameters();
        parameters.setMethod("text");
        Item nested = ITEMS.createArrayItem(
                List.of(ITEMS.createIntItem(2), ITEMS.createArrayItem(List.of(ITEMS.createIntItem(3)), false)), false);
        assertEquals(
                "1 2 3 4",
                SequenceSerializer.serialize(
                        List.of(ITEMS.createIntItem(1), nested, ITEMS.createIntItem(4)),
                        parameters,
                        ExceptionMetadata.EMPTY_METADATA));
        parameters.setItemSeparator("|");
        assertEquals(
                "1|2|3|4",
                SequenceSerializer.serialize(
                        List.of(ITEMS.createIntItem(1), nested, ITEMS.createIntItem(4)),
                        parameters,
                        ExceptionMetadata.EMPTY_METADATA));
    }

    @Test
    void jsonSerializesAnArrayAsOneValueButRejectsMultipleTopLevelItems() {
        SerializationParameters parameters = new SerializationParameters();
        parameters.setMethod("json");
        assertEquals("null", SequenceSerializer.serialize(List.of(), parameters, ExceptionMetadata.EMPTY_METADATA));
        Item array = ITEMS.createArrayItem(List.of(ITEMS.createIntItem(1), ITEMS.createIntItem(2)), false);
        assertEquals(
                "[1,2]",
                SequenceSerializer.serialize(List.of(array), parameters, ExceptionMetadata.EMPTY_METADATA)
                        .replace(" ", ""));
        RumbleException error = assertThrows(
                RumbleException.class,
                () -> SequenceSerializer.serialize(
                        List.of(ITEMS.createIntItem(1), ITEMS.createIntItem(2)),
                        parameters,
                        ExceptionMetadata.EMPTY_METADATA));
        assertTrue(error.getMessage().contains("SERE0023"), error.getMessage());
    }

    @Test
    void adaptiveUsesNewlinesWhenSeparatorIsAbsentAndPreservesEmptySequences() {
        SerializationParameters parameters = new SerializationParameters();
        parameters.setMethod("adaptive");
        assertEquals(
                "1\n2",
                SequenceSerializer.serialize(
                        List.of(ITEMS.createIntItem(1), ITEMS.createIntItem(2)),
                        parameters,
                        ExceptionMetadata.EMPTY_METADATA));
        assertEquals("", SequenceSerializer.serialize(List.of(), parameters, ExceptionMetadata.EMPTY_METADATA));
    }
}
