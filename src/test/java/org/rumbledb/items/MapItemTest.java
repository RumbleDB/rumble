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
package org.rumbledb.items;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.spark.RumbleKryoRegistrator;

class MapItemTest {
    private static final ItemFactory F = ItemFactory.getInstance();

    @Test
    void putKeepsEarlierVersionsAndTakesTheNewSameKey() {
        Item original = F.createMapItem(F.createIntItem(1), List.of(F.createStringItem("a")), false);
        Item updated = F.createMapItemAddingKey(
                original, F.createDecimalItem(BigDecimal.ONE), List.of(F.createStringItem("b")), false);
        Assertions.assertEquals("a", original.getItemByKey(F.createIntItem(1)).getStringValue());
        Assertions.assertEquals("b", updated.getItemByKey(F.createIntItem(1)).getStringValue());
        Assertions.assertTrue(updated.getItemKeys().get(0).isDecimal());
        Item removed = F.createMapItemRemovingKeys(updated, List.of(F.createIntItem(1)), false);
        Assertions.assertEquals(0, removed.getSize());
        Assertions.assertEquals(1, updated.getSize());
    }

    @Test
    void kryoRoundTripKeepsEntriesAndMutability() {
        Item map = F.createMapItem(
                List.of(F.createStringItem("a"), F.createIntItem(2)),
                List.of(List.of(F.createIntItem(1)), List.of(F.createIntItem(2), F.createIntItem(3))),
                ExceptionMetadata.EMPTY_METADATA,
                true);
        Kryo kryo = RumbleKryoRegistrator.newKryo();
        Output output = new Output(1024, -1);
        kryo.writeClassAndObject(output, map);
        Item copy = (Item) kryo.readClassAndObject(new Input(output.toBytes()));
        Assertions.assertEquals(map, copy);
        Assertions.assertFalse(copy.isObject());
        Assertions.assertEquals(0, copy.getMutabilityLevel());
        copy.removeItemByKey(F.createIntItem(2));
        Assertions.assertTrue(copy.isObject());
    }
}
