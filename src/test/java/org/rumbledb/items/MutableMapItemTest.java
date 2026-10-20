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

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.spark.RumbleKryoRegistrator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

class MutableMapItemTest {
    private static final ItemFactory F = ItemFactory.getInstance();

    private static List<Item> value(int i) {
        return List.of(F.createIntItem(i));
    }

    private static Item mutable() {
        return F.createMapItem(
                List.of(F.createStringItem("a"), F.createIntItem(2)),
                List.of(value(1), value(2)),
                ExceptionMetadata.EMPTY_METADATA,
                true);
    }

    @Test
    void mutableUpdatesAndFunctionalUpdatesPreserveEarlierSnapshots() {
        Item original = mutable();
        Assertions.assertInstanceOf(MutableMapItem.class, original);
        Item snapshot = F.createMapItemAddingKey(original, F.createStringItem("extra"), value(3));
        Item removed = F.createMapItemRemovingKeys(original, List.of(F.createIntItem(2)));
        original.removeItemByKey("a");
        original.putItemByKey("later", F.createIntItem(4));
        Assertions.assertEquals(2, original.getSize());
        Assertions.assertFalse(original.hasKey("a"));
        Assertions.assertEquals(1, snapshot.getItemByKey("a").getIntValue());
        Assertions.assertFalse(snapshot.hasKey("later"));
        Assertions.assertEquals(1, removed.getSize());
        Assertions.assertTrue(removed.hasKey("a"));
        Assertions.assertEquals(-1, snapshot.getMutabilityLevel());
        Assertions.assertEquals(0, original.getMutabilityLevel());
    }

    @Test
    void shapeAndDuplicateRulesUseThePersistentRoot() {
        Item map = mutable();
        Assertions.assertFalse(map.isObject());
        Assertions.assertThrows(
                DuplicateObjectKeyException.class, () -> map.putItemByKey(F.createDoubleItem(2), F.createIntItem(3)));
        Assertions.assertEquals(2, map.getSize());
        map.removeItemByKey(F.createDoubleItem(2));
        Assertions.assertTrue(map.isObject());
        map.putSequenceByKey("empty", List.of());
        Assertions.assertFalse(map.isObject());
        map.removeItemByKey("empty");
        Assertions.assertTrue(map.isObject());
        Assertions.assertEquals(1, map.getItemByKey("a").getIntValue());
        Item copy = map.copy(false);
        map.removeItemByKey("a");
        Assertions.assertTrue(copy.hasKey("a"));
        Assertions.assertEquals(0, map.getSize());
    }

    @Test
    void javaAndKryoRoundTripsPreserveMutableIdentityAndUpdateMetadata() throws Exception {
        Item map = mutable();
        map.setMutabilityLevel(2);
        map.setTopLevelID(42);
        map.setTopLevelOrder(3.5);
        map.setPathIn("root");
        map.setTableLocation("table");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(map);
        }
        Item javaCopy;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            javaCopy = (Item) in.readObject();
        }
        Kryo kryo = RumbleKryoRegistrator.newKryo();
        Output out = new Output(4096, -1);
        kryo.writeClassAndObject(out, map);
        Item kryoCopy = (Item) kryo.readClassAndObject(new Input(out.toBytes()));
        for (Item copy : List.of(javaCopy, kryoCopy)) {
            Assertions.assertEquals(map, copy);
            Assertions.assertEquals(2, copy.getMutabilityLevel());
            Assertions.assertEquals(42, copy.getTopLevelID());
            Assertions.assertEquals(3.5, copy.getTopLevelOrder());
            Assertions.assertEquals("root", copy.getPathIn());
            Assertions.assertEquals("table", copy.getTableLocation());
            copy.putItemByKey("new", F.createIntItem(3));
            Assertions.assertFalse(map.hasKey("new"));
            Assertions.assertEquals(3, copy.getSize());
        }
    }

    @Test
    void orderedObjectsKeepTheirTypeOrderAndNestedUpdateMetadata() {
        Item child = F.createObjectItem();
        child.putItemByKey("nested", F.createIntItem(1));
        Item object = F.createObjectItem(
                new ArrayList<>(List.of("z", "a")),
                new ArrayList<>(List.of(child, F.createIntItem(2))),
                ExceptionMetadata.EMPTY_METADATA,
                true);
        Assertions.assertEquals(List.of("z", "a"), object.getStringKeys());
        Assertions.assertEquals(BuiltinTypesCatalogue.objectItem, object.getDynamicType());
        object.setMutabilityLevel(2);
        object.setTopLevelID(4);
        object.setPathIn("root");
        Assertions.assertEquals(2, child.getMutabilityLevel());
        Assertions.assertEquals(4, child.getTopLevelID());
        Assertions.assertEquals("root.z", child.getPathIn());
        object.removeItemByKey("z");
        object.putItemByKey("z", child);
        Assertions.assertEquals(List.of("a", "z"), object.getStringKeys());
    }

    @Test
    void lazyObjectFieldsStayUnevaluatedUntilRequested() {
        RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
        RuntimeStaticContext context = RuntimeStaticContext.builder()
                .configuration(configuration)
                .executionMode(ExecutionMode.LOCAL)
                .staticType(new SequenceType(BuiltinTypesCatalogue.intItem))
                .metadata(ExceptionMetadata.EMPTY_METADATA)
                .build();
        int[] evaluations = {0};
        AbstractAtMostOneItemRuntimePlan plan = new AbstractAtMostOneItemRuntimePlan(List.of(), context) {
            @Override
            public Item evaluateAtMostOne(DynamicContext dynamic) {
                evaluations[0]++;
                return F.createIntItem(7);
            }
        };
        Item lazy = F.createLazyObjectItem();
        lazy.putItemByKey("scalar", F.createIntItem(1));
        lazy.putLazyItemByKey("expensive", plan, new DynamicContext(configuration), false);
        Assertions.assertEquals(2, lazy.getStringKeys().size());
        Assertions.assertEquals(0, evaluations[0]);
        Assertions.assertEquals(1, lazy.getItemByKey("scalar").getIntValue());
        Assertions.assertEquals(0, evaluations[0]);
        Assertions.assertEquals(7, lazy.getItemByKey("expensive").getIntValue());
        Assertions.assertEquals(7, lazy.getItemByKey("expensive").getIntValue());
        Assertions.assertEquals(1, evaluations[0]);
    }
}
