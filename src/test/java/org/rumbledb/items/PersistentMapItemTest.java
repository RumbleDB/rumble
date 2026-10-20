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
import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.functions.maps.MapPutFunctionIterator;
import org.rumbledb.runtime.functions.maps.MapRemoveFunctionIterator;
import org.rumbledb.spark.RumbleKryoRegistrator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

class PersistentMapItemTest {
    private static final ItemFactory F = ItemFactory.getInstance();

    private static Item empty() {
        return F.createMapItem(List.of(), List.of(), ExceptionMetadata.EMPTY_METADATA, false);
    }

    private static Item put(Item map, Item key, List<Item> value) {
        return F.createMapItemAddingKey(map, key, value);
    }

    private static List<Item> value(int i) {
        return List.of(F.createIntItem(i));
    }

    @Test
    void repeatedUpdatesPreserveEarlierVersions() {
        Item original = empty();
        Item current = original;
        Item checkpoint = null;
        for (int i = 0; i < 10000; i++) {
            current = put(current, F.createStringItem("k" + i), value(i));
            if (i == 4999) checkpoint = current;
        }
        Assertions.assertInstanceOf(PersistentMapItem.class, current);
        Assertions.assertEquals(0, original.getSize());
        Assertions.assertEquals(5000, checkpoint.getSize());
        Assertions.assertFalse(checkpoint.hasKey("k9999"));
        Assertions.assertEquals(10000, current.getSize());
        Assertions.assertEquals(9999, current.getItemByKey("k9999").getIntValue());
        Item removed = F.createMapItemRemovingKeys(
                current, List.of(F.createStringItem("k0"), F.createStringItem("k9999"), F.createStringItem("k0")));
        Assertions.assertEquals(9998, removed.getSize());
        Assertions.assertFalse(removed.hasKey("k0"));
        Assertions.assertTrue(current.hasKey("k0"));
        Assertions.assertSame(current, F.createMapItemRemovingKeys(current, List.of(F.createStringItem("absent"))));
    }

    @Test
    void sameKeyUpdatesKeepTheReplacementRepresentativeAndRestoreObjectShape() {
        Item uri = F.createAnyURIItem("key");
        Item string = F.createStringItem("key");
        Item original = put(empty(), uri, List.of());
        Assertions.assertFalse(original.isObject());
        Item replaced = put(original, string, value(1));
        Assertions.assertTrue(replaced.isObject());
        Assertions.assertSame(string, replaced.getItemKeys().get(0));
        Assertions.assertEquals(List.of("key"), replaced.getStringKeys());
        Assertions.assertEquals(1, replaced.getSize());
        Item nonObject = put(replaced, F.createIntItem(10), value(10));
        Assertions.assertFalse(nonObject.isObject());
        Assertions.assertTrue(F.createMapItemRemovingKeys(nonObject, List.of(F.createDoubleItem(10)))
                .isObject());
        Item multi = put(replaced, string, List.of(F.createIntItem(1), F.createIntItem(2)));
        Assertions.assertFalse(multi.isObject());
        Assertions.assertThrows(OurBadException.class, () -> multi.getItemByKey("key"));
        Assertions.assertTrue(F.createMapItemRemovingKeys(multi, List.of(uri)).isObject());
        Assertions.assertTrue(replaced.isObject());
    }

    @Test
    void atomicKeyFamiliesAndFullHashCollisionsWork() {
        List<List<Item>> families = List.of(
                List.of(F.createStringItem("x"), F.createAnyURIItem("x"), F.createUntypedAtomicItem("x")),
                List.of(F.createIntItem(1), F.createDoubleItem(1), F.createDecimalItem(new BigDecimal("1.00"))),
                List.of(F.createIntItem(0), F.createDoubleItem(-0.0)),
                List.of(F.createDoubleItem(Double.NaN), F.createFloatItem(Float.NaN)),
                List.of(
                        F.createDateTimeItem("2020-01-01T00:00:00Z"),
                        F.createDateTimeItem("2020-01-01T01:00:00+01:00")));
        for (List<Item> family : families) {
            Item current = empty();
            for (int i = 0; i < family.size(); i++) {
                current = put(current, family.get(i), value(i));
                Assertions.assertEquals(1, current.getSize());
                Assertions.assertSame(family.get(i), current.getItemKeys().get(0));
            }
            Assertions.assertEquals(
                    0,
                    F.createMapItemRemovingKeys(current, List.of(family.get(0))).getSize());
        }
        Item map = empty();
        List<Item> keys = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            Item key = F.createDateItem((2000 + i) + "-01-01Z");
            keys.add(key);
            map = put(map, key, value(i));
        }
        Item newRepresentative = F.createDateItem("2000-01-01Z");
        Item replaced = put(map, newRepresentative, value(999));
        Assertions.assertTrue(replaced.getItemKeys().stream().anyMatch(k -> k == newRepresentative));
        Assertions.assertEquals(999, replaced.getItemByKey(keys.get(0)).getIntValue());
        Assertions.assertEquals(0, map.getItemByKey(keys.get(0)).getIntValue());
        Item removed = F.createMapItemRemovingKeys(replaced, keys.subList(0, 127));
        Assertions.assertEquals(1, removed.getSize());
        Assertions.assertEquals(127, removed.getItemByKey(keys.get(127)).getIntValue());
    }

    @Test
    void updatesNeverHashOrCompareSequenceContents() {
        List<Item> sequence = new ArrayList<>(value(1)) {
            @Override
            public int hashCode() {
                throw new AssertionError("Sequence hash during update");
            }

            @Override
            public boolean equals(Object other) {
                throw new AssertionError("Sequence comparison during update");
            }
        };
        Item key = F.createStringItem("key");
        Item original = put(empty(), key, sequence);
        Item replaced = put(original, key, sequence);
        Assertions.assertSame(sequence, replaced.getSequenceByKey(key));
        Assertions.assertEquals(
                0, F.createMapItemRemovingKeys(replaced, List.of(key)).getSize());
        Assertions.assertEquals(1, original.getSize());
    }

    @Test
    void mutableInputsAreNotChangedAndCopiesUseTheRightRepresentation() {
        Item source = F.createObjectItem(
                new ArrayList<>(List.of("a")), new ArrayList<>(value(1)), ExceptionMetadata.EMPTY_METADATA, true);
        Item updated = put(source, F.createStringItem("b"), value(2));
        Assertions.assertEquals(1, source.getSize());
        Assertions.assertEquals(0, source.getMutabilityLevel());
        Assertions.assertEquals(-1, updated.getMutabilityLevel());
        Item removed = F.createMapItemRemovingKeys(source, List.of(F.createStringItem("a")));
        Assertions.assertEquals(0, removed.getSize());
        Assertions.assertEquals(1, source.getSize());
        Item mutable = updated.copy(true);
        Assertions.assertInstanceOf(ObjectItem.class, mutable);
        Assertions.assertEquals(0, mutable.getMutabilityLevel());
        mutable.removeItemByKey("a");
        Assertions.assertTrue(updated.hasKey("a"));
        Item immutable = updated.copy(false);
        Assertions.assertInstanceOf(PersistentMapItem.class, immutable);
        Assertions.assertEquals(updated, immutable);
        Assertions.assertEquals(updated.hashCode(), immutable.hashCode());
        Assertions.assertThrows(OurBadException.class, () -> updated.removeItemByKey("a"));
        Item mutableMap = F.createMapItem(F.createStringItem("a"), value(1), true);
        Assertions.assertEquals(0, mutableMap.getMutabilityLevel());
    }

    @Test
    void bulkConstructionDetectsEquivalentDuplicatesAndKeepsKeysAlignedWithValues() {
        Assertions.assertThrows(
                DuplicateObjectKeyException.class,
                () -> F.createMapItem(
                        List.of(F.createStringItem("a"), F.createAnyURIItem("a")),
                        List.of(value(1), value(2)),
                        ExceptionMetadata.EMPTY_METADATA,
                        false));
        java.util.Map<Item, List<Item>> pairs = new LinkedHashMap<>();
        pairs.put(F.createStringItem("z"), List.of());
        pairs.put(F.createStringItem("a"), value(1));
        pairs.put(F.createIntItem(2), List.of(F.createIntItem(2), F.createIntItem(3)));
        Item map = F.createMapItem(pairs, ExceptionMetadata.EMPTY_METADATA, false);
        Assertions.assertInstanceOf(PersistentMapItem.class, map);
        List<Item> keys = map.getItemKeys();
        List<List<Item>> values = map.getSequenceValues();
        for (int i = 0; i < keys.size(); i++) Assertions.assertSame(pairs.get(keys.get(i)), values.get(i));
        Item materialized = new MutableMapItem(keys, values, ExceptionMetadata.EMPTY_METADATA);
        Assertions.assertEquals(materialized, map);
        Assertions.assertEquals(map, materialized);
        Assertions.assertEquals(materialized.hashCode(), map.hashCode());
        Assertions.assertThrows(OurBadException.class, map::getStringKeys);
        Assertions.assertNull(map.getSequenceByKey("absent"));
    }

    @Test
    void randomUpdatesMatchMaterializedReference() {
        Random random = new Random(20261020);
        Item current = empty();
        java.util.Map<Item, List<Item>> expected = new HashMap<>();
        for (int i = 0; i < 2000; i++) {
            Item key = F.createStringItem("k" + random.nextInt(100));
            if (random.nextBoolean()) {
                List<Item> sequence = i % 3 == 0 ? List.of() : value(i);
                current = put(current, key, sequence);
                expected.put(key, sequence);
            } else {
                current = F.createMapItemRemovingKeys(current, List.of(key));
                expected.remove(key);
            }
            Assertions.assertEquals(expected.size(), current.getSize());
            for (var entry : expected.entrySet())
                Assertions.assertEquals(entry.getValue(), current.getSequenceByKey(entry.getKey()));
        }
    }

    @Test
    void javaAndKryoSerializationPreserveLogicalKeysShapeAndSharedItems() throws Exception {
        Item shared = F.createMapItem(F.createStringItem("nested"), value(7), false);
        Item map = empty();
        for (int i = 0; i < 128; i++) map = put(map, F.createStringItem("k" + i), List.of(shared));
        map = put(map, F.createAnyURIItem("representative"), value(1));
        Item key = F.createStringItem("representative");
        map = put(map, key, value(2));
        Object[] graph = {shared, map};
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(graph);
        }
        Object[] javaCopy;
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            javaCopy = (Object[]) in.readObject();
        }
        Kryo kryo = RumbleKryoRegistrator.newKryo();
        Output output = new Output(4096, -1);
        kryo.writeClassAndObject(output, graph);
        Object[] kryoCopy = (Object[]) kryo.readClassAndObject(new Input(output.toBytes()));
        for (Object[] copy : List.of(javaCopy, kryoCopy)) {
            Item decoded = (Item) copy[1];
            Assertions.assertEquals(map, decoded);
            Assertions.assertEquals(map.hashCode(), decoded.hashCode());
            Assertions.assertTrue(decoded.isObject());
            Assertions.assertSame(copy[0], decoded.getItemByKey("k0"));
            Assertions.assertSame(decoded.getItemByKey("k0"), decoded.getItemByKey("k127"));
            Assertions.assertTrue(decoded.getItemKeys().stream()
                    .filter(k -> k.equals(key))
                    .findFirst()
                    .orElseThrow()
                    .isString());
            Assertions.assertEquals(
                    128, F.createMapItemRemovingKeys(decoded, List.of(key)).getSize());
        }
        Item multi = put(put(empty(), key, List.of()), F.createIntItem(2), value(2));
        Output out = new Output(1024, -1);
        kryo.writeClassAndObject(out, multi);
        Item decoded = (Item) kryo.readClassAndObject(new Input(out.toBytes()));
        Assertions.assertFalse(decoded.isObject());
        Assertions.assertTrue(put(F.createMapItemRemovingKeys(decoded, List.of(F.createIntItem(2))), key, value(3))
                .isObject());
    }

    @Test
    void singletonAndObjectAccessorsRemainConsistent() {
        Item singleton = F.createMapItem(F.createStringItem("a"), value(1), false);
        Assertions.assertInstanceOf(PersistentMapItem.class, singleton);
        Assertions.assertEquals(value(1), singleton.getItemValues());
        Assertions.assertEquals(
                java.util.Map.of("a", F.createIntItem(1).getVariantValue()), singleton.getVariantValue());
        Assertions.assertEquals("named_struct(\"a\", 1)", singleton.getSparkSQLValue());
        Assertions.assertTrue(singleton.getSparkSQLType().startsWith("STRUCT<"));
        Item emptyValue = F.createMapItem(F.createStringItem("a"), List.of(), false);
        Assertions.assertFalse(emptyValue.isObject());
        Assertions.assertThrows(OurBadException.class, emptyValue::getItemValues);
        Assertions.assertThrows(OurBadException.class, emptyValue::getVariantValue);
    }

    @Test
    void mapFunctionsPreserveMutableResultsInSideEffectingQueries() {
        RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
        DynamicContext dynamic = new DynamicContext(configuration);
        for (boolean sideEffecting : List.of(false, true)) {
            RuntimeStaticContext context = RuntimeStaticContext.builder()
                    .configuration(configuration)
                    .staticType(new SequenceType(BuiltinTypesCatalogue.mapItem))
                    .executionMode(ExecutionMode.LOCAL)
                    .metadata(ExceptionMetadata.EMPTY_METADATA)
                    .isQuerySideEffecting(sideEffecting)
                    .build();
            Item source = F.createMapItem(F.createStringItem("a"), value(1), true);
            Item put = new MapPutFunctionIterator(
                            List.of(
                                    new ConstantRuntimeIterator(source, context),
                                    new ConstantRuntimeIterator(F.createIntItem(2), context),
                                    new ConstantRuntimeIterator(F.createIntItem(2), context)),
                            context)
                    .evaluateAtMostOne(dynamic);
            Assertions.assertEquals(sideEffecting ? 0 : -1, put.getMutabilityLevel());
            Assertions.assertEquals(1, source.getSize());
            Item mixed = F.createMapItem(
                    List.of(F.createStringItem("a"), F.createIntItem(2)),
                    List.of(value(1), value(2)),
                    ExceptionMetadata.EMPTY_METADATA,
                    true);
            Item removed = new MapRemoveFunctionIterator(
                            List.of(
                                    new ConstantRuntimeIterator(mixed, context),
                                    new ConstantRuntimeIterator(F.createIntItem(2), context)),
                            context)
                    .evaluateAtMostOne(dynamic);
            Assertions.assertTrue(removed.isObject());
            Assertions.assertEquals(sideEffecting ? 0 : -1, removed.getMutabilityLevel());
            Assertions.assertEquals(2, mixed.getSize());
        }
    }

    @Test
    void mergedStringMapsRemainPersistentForIndependentUpdates() {
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build());
        for (String query : List.of("map:merge(())", "map:merge((map:entry('a', 1), map:entry('b', 2)))")) {
            Item merged = rumble.runQuery(query).getAsList().get(0);
            Assertions.assertInstanceOf(PersistentMapItem.class, merged);
            Assertions.assertTrue(merged.isObject());
        }
        // Reduced QT3 op/same-key.xml:same-key-023. Each update starts from the same map,
        // so a non-persistent merge result would be converted in full for every key.
        Item result = rumble.runQuery(
                        """
                    let $keys :=
                      let $range := 48 to 63
                      for $c1 in $range, $c2 in $range, $c3 in $range
                      return codepoints-to-string(($c1, $c2, $c3))
                    let $map := map:merge(($keys, $keys) ! map:entry(., .))
                    return map:size($map) eq count($keys) and (
                      every $key in $keys satisfies
                        $map($key) eq $key and
                        not(map:contains(map:remove($map, $key), $key)) and
                        map:get(map:put($map, $key, 'x'), $key) eq 'x'
                    )
                    """)
                .getAsList()
                .get(0);
        Assertions.assertTrue(result.getBooleanValue());
    }

    @Test
    void queryPutAndRemoveUsePersistentMaps() {
        Rumble rumble = new Rumble(RumbleConfiguration.builder()
                .configureSemantics(semantics -> semantics.queryLanguage("xquery31"))
                .build());
        Item result = rumble.runQuery("map:remove(map:put(map { 'a': 1, 'b': () }, 'c', (2, 3)), 'a')")
                .getAsList()
                .get(0);
        Assertions.assertInstanceOf(PersistentMapItem.class, result);
        Assertions.assertEquals(2, result.getSize());
        Assertions.assertEquals(List.of(), result.getSequenceByKey("b"));
        Assertions.assertEquals(2, result.getSequenceByKey("c").size());
        Assertions.assertFalse(result.hasKey("a"));
        Item object = rumble.runQuery("map:remove(map { 'a': 1, 2: (2, 3) }, 2)")
                .getAsList()
                .get(0);
        Assertions.assertTrue(object.isObject());
        Assertions.assertEquals(1, object.getItemByKey("a").getIntValue());
    }
}
