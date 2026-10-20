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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import io.usethesource.capsule.Map;

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
 * Immutable map backed by a structurally shared CHAMP trie. Updates copy only the affected path.
 * The fields are assigned during construction or deserialization; ordinary operations never mutate them.
 */
public final class PersistentMapItem extends AbstractMapItem implements KryoSerializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient Map.Immutable<Item, Entry> entries;
    private transient int nonStringKeys;
    private transient int nonSingletonValues;

    /** Kryo creates an empty instance before reading its entries, allowing shared Item references. */
    public PersistentMapItem() {
        this.entries = Map.Immutable.of();
    }

    PersistentMapItem(Item key, List<Item> value) {
        validateKey(key);
        Entry entry = new Entry(key, value);
        this.entries = Map.Immutable.of(key, entry);
        this.nonStringKeys = nonString(entry);
        this.nonSingletonValues = nonSingleton(entry);
    }

    PersistentMapItem(List<Item> keys, List<List<Item>> values, ExceptionMetadata metadata) {
        Map.Transient<Item, Entry> builder = Map.Transient.of();
        for (int i = 0; i < keys.size(); i++) {
            addEntry(builder, keys.get(i), values.get(i), metadata);
        }
        this.entries = builder.freeze();
    }

    PersistentMapItem(java.util.Map<Item, List<Item>> values, ExceptionMetadata metadata) {
        Map.Transient<Item, Entry> builder = Map.Transient.of();
        for (var entry : values.entrySet()) {
            addEntry(builder, entry.getKey(), entry.getValue(), metadata);
        }
        this.entries = builder.freeze();
    }

    private PersistentMapItem(Map.Immutable<Item, Entry> entries, int nonStringKeys, int nonSingletonValues) {
        this.entries = entries;
        this.nonStringKeys = nonStringKeys;
        this.nonSingletonValues = nonSingletonValues;
    }

    static PersistentMapItem from(Item original) {
        if (original instanceof PersistentMapItem persistent) {
            return persistent;
        }
        if (original instanceof MutableMapItem mutable) {
            return mutable.getPersistentMap();
        }
        return new PersistentMapItem(
                original.getItemKeys(), original.getSequenceValues(), ExceptionMetadata.EMPTY_METADATA);
    }

    void forEachEntry(BiConsumer<Item, List<Item>> action) {
        this.entries.valueIterator().forEachRemaining(entry -> action.accept(entry.key, entry.value));
    }

    PersistentMapItem withEntry(Item key, List<Item> value) {
        validateKey(key);
        Entry old = this.entries.get(key);
        return new PersistentMapItem(
                this.entries.__put(key, new Entry(key, value)),
                this.nonStringKeys - nonString(old) + (key.isString() ? 0 : 1),
                this.nonSingletonValues - nonSingleton(old) + (value.size() == 1 ? 0 : 1));
    }

    PersistentMapItem withoutKeys(List<Item> keys) {
        Map.Transient<Item, Entry> builder = this.entries.asTransient();
        int strings = this.nonStringKeys;
        int singletons = this.nonSingletonValues;
        boolean changed = false;
        for (Item key : keys) {
            Entry removed = builder.__remove(key);
            if (removed != null) {
                strings -= nonString(removed);
                singletons -= nonSingleton(removed);
                changed = true;
            }
        }
        return changed ? new PersistentMapItem(builder.freeze(), strings, singletons) : this;
    }

    private static void validateKey(Item key) {
        if (key == null || !key.isAtomic()) {
            throw new OurBadException("Map keys must be atomic items.");
        }
    }

    private void addEntry(Map.Transient<Item, Entry> builder, Item key, List<Item> value, ExceptionMetadata metadata) {
        validateKey(key);
        if (builder.containsKey(key)) {
            throw new DuplicateObjectKeyException(key.serialize(), metadata);
        }
        Entry entry = new Entry(key, value);
        builder.__put(key, entry);
        this.nonStringKeys += nonString(entry);
        this.nonSingletonValues += nonSingleton(entry);
    }

    private static int nonString(Entry entry) {
        return entry == null || entry.key.isString() ? 0 : 1;
    }

    private static int nonSingleton(Entry entry) {
        return entry == null || entry.value.size() == 1 ? 0 : 1;
    }

    /**
     * Keep identity equality and hashing: Capsule hashes payloads on put/remove, while sequences
     * may be large or contain nested maps. Structural equality/hashCode belong to AbstractMapItem.
     * Store the logical key here because Capsule retains the original physical key on replacement.
     * Sequences and their Items follow the same sharing/ownership rules as other map Items.
     */
    private static final class Entry {
        private final Item key;
        private final List<Item> value;

        private Entry(Item key, List<Item> value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public Item copy(boolean mutable) {
        if (mutable) {
            List<String> keys = getStringKeys();
            List<Item> values = new ArrayList<>(keys.size());
            for (String key : keys) {
                values.add(getItemByKey(key).copy(true));
            }
            Item result = new ObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA);
            result.setMutabilityLevel(0);
            return result;
        }
        Map.Transient<Item, Entry> builder = Map.Transient.of();
        var iterator = this.entries.valueIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            Item key = entry.key.copy(false);
            List<Item> value =
                    entry.value.stream().map(item -> item.copy(false)).toList();
            builder.__put(key, new Entry(key, value));
        }
        return new PersistentMapItem(builder.freeze(), this.nonStringKeys, this.nonSingletonValues);
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return this.nonStringKeys == 0 && this.nonSingletonValues == 0;
    }

    @Override
    public int getSize() {
        return this.entries.size();
    }

    @Override
    public List<Item> getItemKeys() {
        List<Item> result = new ArrayList<>(getSize());
        this.entries.valueIterator().forEachRemaining(entry -> result.add(entry.key));
        return result;
    }

    @Override
    public List<String> getStringKeys() {
        if (this.nonStringKeys != 0) throw new OurBadException("Map contains non-string keys.");
        List<String> result = new ArrayList<>(getSize());
        this.entries.valueIterator().forEachRemaining(entry -> result.add(entry.key.getStringValue()));
        return result;
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        List<List<Item>> result = new ArrayList<>(getSize());
        this.entries.valueIterator().forEachRemaining(entry -> result.add(entry.value));
        return result;
    }

    @Override
    public List<Item> getItemValues() {
        if (this.nonSingletonValues != 0) throw new OurBadException("Map contains non-singleton values.");
        List<Item> result = new ArrayList<>(getSize());
        this.entries.valueIterator().forEachRemaining(entry -> result.add(entry.value.get(0)));
        return result;
    }

    @Override
    public boolean hasKey(Item key) {
        return this.entries.containsKey(key);
    }

    @Override
    public boolean hasKey(String key) {
        return hasKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        Entry entry = this.entries.get(key);
        return entry == null ? null : entry.value;
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        return getSequenceByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public Item getItemByKey(Item key) {
        List<Item> value = getSequenceByKey(key);
        if (value == null) return null;
        if (value.size() != 1) throw new OurBadException("Map contains non-singleton values.");
        return value.get(0);
    }

    @Override
    public Item getItemByKey(String key) {
        return getItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    /** Serialize logical entries through Kryo, preserving shared Items without exposing trie internals. */
    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(getSize(), true);
        var iterator = this.entries.valueIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            kryo.writeClassAndObject(output, entry.key);
            output.writeInt(entry.value.size(), true);
            for (Item item : entry.value) kryo.writeClassAndObject(output, item);
        }
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Map.Transient<Item, Entry> builder = Map.Transient.of();
        this.nonStringKeys = 0;
        this.nonSingletonValues = 0;
        int size = input.readInt(true);
        for (int i = 0; i < size; i++) {
            Item key = (Item) kryo.readClassAndObject(input);
            int count = input.readInt(true);
            List<Item> value = new ArrayList<>(count);
            for (int j = 0; j < count; j++) value.add((Item) kryo.readClassAndObject(input));
            addEntry(builder, key, value, ExceptionMetadata.EMPTY_METADATA);
        }
        this.entries = builder.freeze();
    }

    @Serial
    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeInt(getSize());
        var iterator = this.entries.valueIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            output.writeObject(entry.key);
            output.writeObject(entry.value);
        }
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        Map.Transient<Item, Entry> builder = Map.Transient.of();
        this.nonStringKeys = 0;
        this.nonSingletonValues = 0;
        int size = input.readInt();
        for (int i = 0; i < size; i++) {
            Item key = (Item) input.readObject();
            List<Item> value = (List<Item>) input.readObject();
            addEntry(builder, key, value, ExceptionMetadata.EMPTY_METADATA);
        }
        this.entries = builder.freeze();
    }

    @Override
    public void putItemByKey(String key, Item value) {
        throw new OurBadException("Cannot put an item by key in a PersistentMapItem, which is not mutable.");
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        throw new OurBadException("Cannot put an item by key in a PersistentMapItem, which is not mutable.");
    }

    @Override
    public void putSequenceByKey(String key, List<Item> valueSequence) {
        throw new OurBadException("Cannot put an item by key in a PersistentMapItem, which is not mutable.");
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        throw new OurBadException("Cannot put an item by key in a PersistentMapItem, which is not mutable.");
    }

    @Override
    public void removeItemByKey(String key) {
        throw new OurBadException("Cannot remove an item by key in a PersistentMapItem, which is not mutable.");
    }

    @Override
    public void removeItemByKey(Item key) {
        throw new OurBadException("Cannot remove an item by key in a PersistentMapItem, which is not mutable.");
    }

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
        return -1;
    }

    @Override
    public void setMutabilityLevel(int mutabilityLevel) {
        if (mutabilityLevel != -1) {
            throw new OurBadException("Cannot change mutability level.");
        }
    }

    @Override
    public long getTopLevelID() {
        throw new OurBadException("Persistent map items have no top-level ID");
    }

    @Override
    public void setTopLevelID(long topLevelID) {
        throw new OurBadException("Cannot change top level ID of a PersistentMapItem, which is not mutable.");
    }

    @Override
    public double getTopLevelOrder() {
        throw new OurBadException("Persistent map items have no top-level order");
    }

    @Override
    public void setTopLevelOrder(double topLevelOrder) {
        throw new OurBadException("Cannot change top level order of a PersistentMapItem, which is not mutable.");
    }

    @Override
    public String getPathIn() {
        throw new OurBadException("Persistent map items have no path");
    }

    @Override
    public void setPathIn(String pathIn) {
        throw new OurBadException("Cannot change path of a PersistentMapItem, which is not mutable.");
    }

    @Override
    public String getTableLocation() {
        throw new OurBadException("Persistent map items have no table location");
    }

    @Override
    public void setTableLocation(String location) {
        throw new OurBadException("Cannot change table location of a PersistentMapItem, which is not mutable.");
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
        if (!isObject()) {
            throw new OurBadException("Only objects have a Spark SQL variant value.");
        }
        java.util.Map<String, Object> result = new HashMap<>();
        this.entries
                .valueIterator()
                .forEachRemaining(entry -> result.put(
                        entry.key.getStringValue(), entry.value.get(0).getVariantValue()));
        return result;
    }

    @Override
    public Collection getCollection() {
        throw new OurBadException("Persistent map items have no collection");
    }

    @Override
    public void setCollection(Collection collection) {
        throw new OurBadException("Cannot change collection of a PersistentMapItem, which is not mutable.");
    }
}
