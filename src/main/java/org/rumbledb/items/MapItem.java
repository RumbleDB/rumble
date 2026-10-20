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

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import scala.collection.immutable.HashMap;
import scala.collection.immutable.HashMap$;
import scala.jdk.javaapi.CollectionConverters;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;

/**
 * A map backed by Scala's immutable hash array mapped trie (CHAMP). {@link #with} and {@link #without}
 * return new maps that share all unchanged trie nodes with this one, so building a map one entry at a
 * time costs O(log n) per entry. The in-place updates of mutable maps replace the trie.
 */
public class MapItem extends AbstractMapItem implements KryoSerializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The trie keeps the first key object it was given for an entry, so the entry stores the key of the
     * latest put, as op:same-key keys such as 1 and 1.0 have different types.
     */
    private record Entry(Item key, List<Item> value) implements Serializable {
        boolean isObjectField() {
            return this.key.isString() && this.value.size() == 1;
        }
    }

    private HashMap<Item, Entry> entries;
    // Number of entries without a string key and a singleton value, so that isObject() is O(1).
    private int nonObjectEntries;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private double topLevelOrder;
    private Collection collection;

    public MapItem() {
        this.entries = HashMap$.MODULE$.empty();
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.collection = null;
        this.topLevelOrder = 0.0;
    }

    public MapItem(List<Item> keys, List<List<Item>> valueSequences, ExceptionMetadata metadata) {
        this();
        for (int i = 0; i < keys.size(); i++) {
            putNewEntry(keys.get(i), valueSequences.get(i), metadata);
        }
    }

    /**
     * Returns the map as a MapItem, copying the entries only if it has another representation.
     */
    static MapItem from(Item map) {
        if (map instanceof MapItem mapItem) {
            return mapItem;
        }
        return new MapItem(map.getItemKeys(), map.getSequenceValues(), ExceptionMetadata.EMPTY_METADATA);
    }

    /**
     * Returns a new immutable map with the entry added, or replacing the entry with the same key.
     */
    MapItem with(Item key, List<Item> valueSequence) {
        MapItem result = new MapItem();
        result.entries = this.entries;
        result.nonObjectEntries = this.nonObjectEntries;
        result.internalPut(key, valueSequence);
        return result;
    }

    /**
     * Returns a new immutable map without the entries with these keys.
     */
    MapItem without(List<Item> keys) {
        MapItem result = new MapItem();
        result.entries = this.entries;
        result.nonObjectEntries = this.nonObjectEntries;
        for (Item key : keys) {
            result.internalRemove(key);
        }
        return result;
    }

    private Iterable<Entry> entries() {
        return () -> CollectionConverters.asJava(this.entries.valuesIterator());
    }

    private Entry getEntry(Item key) {
        return this.entries.getOrElse(key, () -> null);
    }

    private void putNewEntry(Item key, List<Item> valueSequence, ExceptionMetadata metadata) {
        if (key == null || !key.isAtomic()) {
            throw new OurBadException("Map keys must be atomic items.", metadata);
        }
        if (this.entries.contains(key)) {
            throw new DuplicateObjectKeyException(key.serialize(), metadata);
        }
        internalPut(key, valueSequence);
    }

    private void internalPut(Item key, List<Item> valueSequence) {
        Entry entry = new Entry(key, valueSequence);
        this.nonObjectEntries += nonObjectCount(entry) - nonObjectCount(getEntry(key));
        this.entries = this.entries.updated(key, entry);
    }

    private void internalRemove(Item key) {
        this.nonObjectEntries -= nonObjectCount(getEntry(key));
        this.entries = this.entries.removed(key);
    }

    private static int nonObjectCount(Entry entry) {
        return entry == null || entry.isObjectField() ? 0 : 1;
    }

    @Override
    public Item copy(boolean mutable) {
        if (mutable) {
            List<String> keys = this.getStringKeys();
            List<Item> values = new ArrayList<>(keys.size());
            for (String key : keys) {
                values.add(getItemByKey(key).copy(true));
            }
            Item result = new ObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA);
            result.setMutabilityLevel(0);
            return result;
        }
        MapItem result = new MapItem();
        for (Entry entry : entries()) {
            result.internalPut(
                    entry.key().copy(false),
                    entry.value().stream().map(item -> item.copy(false)).toList());
        }
        return result;
    }

    // region maps

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return this.nonObjectEntries == 0;
    }

    @Override
    public List<String> getStringKeys() {
        List<String> result = new ArrayList<>(getSize());
        for (Entry entry : entries()) {
            if (!entry.key().isString()) {
                throw new OurBadException("Map contains non-string keys.");
            }
            result.add(entry.key().getStringValue());
        }
        return result;
    }

    @Override
    public List<Item> getItemKeys() {
        List<Item> result = new ArrayList<>(getSize());
        for (Entry entry : entries()) {
            result.add(entry.key());
        }
        return result;
    }

    @Override
    public int getSize() {
        return this.entries.size();
    }

    @Override
    public boolean hasKey(String key) {
        return hasKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public boolean hasKey(Item key) {
        return this.entries.contains(key);
    }

    @Override
    public List<Item> getItemValues() {
        List<Item> result = new ArrayList<>(getSize());
        for (Entry entry : entries()) {
            if (entry.value().size() != 1) {
                throw new OurBadException("Map contains non-singleton values.");
            }
            result.add(entry.value().get(0));
        }
        return result;
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        List<List<Item>> result = new ArrayList<>(getSize());
        for (Entry entry : entries()) {
            result.add(entry.value());
        }
        return result;
    }

    @Override
    public Item getItemByKey(String key) {
        return getItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public Item getItemByKey(Item key) {
        List<Item> valueSequence = getSequenceByKey(key);
        if (valueSequence == null) {
            return null;
        }
        if (valueSequence.size() != 1) {
            throw new OurBadException("Map contains non-singleton values.");
        }
        return valueSequence.get(0);
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        return getSequenceByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        Entry entry = getEntry(key);
        return entry == null ? null : entry.value();
    }

    @Override
    public void putItemByKey(String key, Item value) {
        putSequenceByKey(ItemFactory.getInstance().createStringItem(key), Collections.singletonList(value));
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        putSequenceByKey(key, Collections.singletonList(value));
    }

    @Override
    public void putSequenceByKey(String key, List<Item> valueSequence) {
        putSequenceByKey(ItemFactory.getInstance().createStringItem(key), valueSequence);
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        putNewEntry(key, valueSequence, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void removeItemByKey(String key) {
        removeItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public void removeItemByKey(Item key) {
        internalRemove(key);
    }

    // endregion maps

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.mapItem;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return true;
    }

    @Override
    public int getMutabilityLevel() {
        return this.mutabilityLevel;
    }

    @Override
    public void setMutabilityLevel(int mutabilityLevel) {
        this.mutabilityLevel = mutabilityLevel;
        for (Entry entry : entries()) {
            for (Item item : entry.value()) {
                item.setMutabilityLevel(mutabilityLevel);
            }
        }
    }

    @Override
    public long getTopLevelID() {
        return this.topLevelID;
    }

    @Override
    public void setTopLevelID(long topLevelID) {
        this.topLevelID = topLevelID;
        for (Entry entry : entries()) {
            for (Item item : entry.value()) {
                item.setTopLevelID(topLevelID);
            }
        }
    }

    @Override
    public double getTopLevelOrder() {
        return this.topLevelOrder;
    }

    @Override
    public void setTopLevelOrder(double topLevelOrder) {
        this.topLevelOrder = topLevelOrder;
        for (Entry entry : entries()) {
            for (Item item : entry.value()) {
                item.setTopLevelOrder(topLevelOrder);
            }
        }
    }

    @Override
    public String getPathIn() {
        return this.pathIn;
    }

    @Override
    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
        for (Entry entry : entries()) {
            String keyFragment = entry.key().serialize();
            for (Item item : entry.value()) {
                item.setPathIn(pathIn + "." + keyFragment);
            }
        }
    }

    @Override
    public String getTableLocation() {
        return this.location;
    }

    @Override
    public void setTableLocation(String location) {
        this.location = location;
        for (Entry entry : entries()) {
            for (Item item : entry.value()) {
                item.setTableLocation(location);
            }
        }
    }

    @Override
    public String getSparkSQLValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("named_struct(");
        List<String> keysAsStrings = getStringKeys();
        List<Item> values = getItemValues();
        for (int i = 0; i < keysAsStrings.size(); i++) {
            sb.append("\"");
            sb.append(keysAsStrings.get(i));
            sb.append("\"");
            sb.append(", ");
            sb.append(values.get(i).getSparkSQLValue());
            if (i + 1 < keysAsStrings.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = itemType.getObjectKeysFacet();

        sb.append("named_struct(");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            FieldDescriptor field = itemType.getObjectContentFacet(key);
            Item value = getItemByKey(key);

            sb.append("\"").append(key).append("\"").append(", ");
            if (value == null) {
                if (!field.isRequired()) {
                    sb.append("NULL");
                }
            } else {
                sb.append(value.getSparkSQLValue(field.getType()));
            }

            if (i + 1 < keys.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLType() {
        StringBuilder sb = new StringBuilder();
        sb.append("STRUCT<");
        List<String> keysAsStrings = getStringKeys();
        List<Item> values = getItemValues();
        for (int i = 0; i < keysAsStrings.size(); i++) {
            sb.append(keysAsStrings.get(i));
            sb.append(": ");
            sb.append(values.get(i).getSparkSQLType());
            if (i + 1 < keysAsStrings.size()) {
                sb.append(", ");
            }
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public List<Item> atomizedValue() {
        throw new CannotAtomizeException("tried to atomize Map", ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public String getStringValue() {
        throw new FunctionItemStringValueException(
                FunctionItemStringValueException.DEFAULT_MESSAGE, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public Object getVariantValue() {
        Map<String, Object> resultMap = new java.util.HashMap<>();
        List<String> keysAsStrings = getStringKeys();
        List<Item> values = getItemValues();
        for (int i = 0; i < keysAsStrings.size(); i++) {
            resultMap.put(keysAsStrings.get(i), values.get(i).getVariantValue());
        }
        return resultMap;
    }

    @Override
    public Collection getCollection() {
        return this.collection;
    }

    @Override
    public void setCollection(Collection collection) {
        this.collection = collection;
        for (Entry entry : entries()) {
            for (Item item : entry.value()) {
                item.setCollection(collection);
            }
        }
    }

    /**
     * Kryo writes the entries rather than the trie nodes, which are internal to Scala.
     */
    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(getSize(), true);
        for (Entry entry : entries()) {
            kryo.writeClassAndObject(output, entry.key());
            output.writeInt(entry.value().size(), true);
            for (Item item : entry.value()) {
                kryo.writeClassAndObject(output, item);
            }
        }
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        output.writeString(this.pathIn);
        output.writeString(this.location);
        output.writeDouble(this.topLevelOrder);
        kryo.writeClassAndObject(output, this.collection);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entries = HashMap$.MODULE$.empty();
        this.nonObjectEntries = 0;
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            Item key = (Item) kryo.readClassAndObject(input);
            int valueCount = input.readInt(true);
            List<Item> valueSequence = new ArrayList<>(valueCount);
            for (int j = 0; j < valueCount; j++) {
                valueSequence.add((Item) kryo.readClassAndObject(input));
            }
            internalPut(key, valueSequence);
        }
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = input.readString();
        this.location = input.readString();
        this.topLevelOrder = input.readDouble();
        this.collection = (Collection) kryo.readClassAndObject(input);
    }
}
