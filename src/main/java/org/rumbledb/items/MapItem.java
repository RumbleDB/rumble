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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */
package org.rumbledb.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MapItem implements Item {

    private static final long serialVersionUID = 1L;

    private List<List<Item>> values;
    private List<Item> keys;
    private Map<Item, Integer> keyToIndex;
    private boolean allKeysString;
    private boolean allValuesSingletons;
    private boolean recomputeObjectShapeCache;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private double topLevelOrder;
    private Collection collection;

    public MapItem() {
        this.values = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.keyToIndex = new TreeMap<>(new ItemSameKeyComparator());
        this.allKeysString = true;
        this.allValuesSingletons = true;
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
            // assume atomicity of keys is checked by the caller, or at the typesystem level
            validateDuplicateKey(keys.get(i), metadata);
            internalPutSequenceByKey(keys.get(i), valueSequences.get(i), metadata);
        }
    }

    public MapItem(Map<Item, List<Item>> keyValuePairs, ExceptionMetadata metadata) {
        this();
        // thin constructor
        // assume atomicity of keys is checked by the caller, or at the typesystem level
        this.values = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.keyToIndex = new TreeMap<>(new ItemSameKeyComparator());
        for (Map.Entry<Item, List<Item>> entry : keyValuePairs.entrySet()) {
            Item key = entry.getKey();
            List<Item> valueSequence = entry.getValue();
            validateDuplicateKey(key, metadata);
            internalPutSequenceByKey(key, valueSequence, metadata);
        }
    }

    private void rebuildKeyStringIndex() {
        if (this.keyToIndex == null) {
            this.keyToIndex = new TreeMap<>(new ItemSameKeyComparator());
        } else {
            this.keyToIndex.clear();
        }
        for (int i = 0; i < this.keys.size(); i++) {
            this.keyToIndex.put(this.keys.get(i), i);
        }
    }

    private void validateAtomicKey(Item key, ExceptionMetadata metadata) {
        if (key == null || !key.isAtomic()) {
            throw new OurBadException("Map keys must be atomic items.", metadata);
        }
    }

    private void validateDuplicateKey(Item key, ExceptionMetadata metadata) {
        if (this.keyToIndex.containsKey(key)) {
            throw new DuplicateObjectKeyException(key.serialize(), metadata);
        }
    }

    private void internalPutSequenceByKey(Item key, List<Item> valueSequence, ExceptionMetadata metadata) {
        if (this.keyToIndex == null) {
            rebuildKeyStringIndex();
        }
        if (this.keyToIndex.containsKey(key)) {
            int index = this.keyToIndex.get(key);
            this.keys.set(index, key);
            this.values.set(index, valueSequence);
        } else {
            this.values.add(valueSequence);
            this.keys.add(key);
            this.keyToIndex.put(key, this.values.size() - 1);
        }
        if (this.allKeysString && !key.isString()) {
            this.allKeysString = false;
        }
        if (this.allValuesSingletons && valueSequence.size() != 1) {
            this.allValuesSingletons = false;
        }
    }

    // region maps

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        if (this.recomputeObjectShapeCache) {
            this.allKeysString = true;
            this.allValuesSingletons = true;
            for (Item k : this.keys) {
                if (!k.isString()) {
                    this.allKeysString = false;
                    break;
                }
            }
            for (List<Item> valueSequence : this.values) {
                if (valueSequence.size() != 1) {
                    this.allValuesSingletons = false;
                    break;
                }
            }
            this.recomputeObjectShapeCache = false;
        }
        return this.allValuesSingletons && this.allKeysString;
    }

    @Override
    public List<String> getKeys() {
        return this.getStringKeys();
    }

    @Override
    public List<String> getStringKeys() {
        List<String> result = new ArrayList<>(this.keys.size());
        for (Item key : this.keys) {
            if (!key.isString()) {
                throw new OurBadException("Map contains non-string keys.");
            }
            result.add(key.getStringValue());
        }
        return result;
    }

    @Override
    public List<Item> getItemKeys() {
        int n = this.keys.size();
        List<Item> result = new ArrayList<>(n);
        result.addAll(this.keys);
        return result;
    }

    @Override
    public int getSize() {
        return this.keys.size();
    }

    public boolean hasKey(String key) throws UnsupportedOperationException {
        return hasKey(ItemFactory.getInstance().createStringItem(key));
    }

    public boolean hasKey(Item key) throws UnsupportedOperationException {
        return this.keys.contains(key);
    }

    @Override
    public List<Item> getValues() {
        return this.getItemValues();
    }

    @Override
    public List<Item> getItemValues() {
        List<Item> result = new ArrayList<>(this.values.size());
        for (List<Item> valueSequence : this.values) {
            if (valueSequence.size() != 1) {
                throw new OurBadException("Map contains non-singleton values.");
            }
            result.add(valueSequence.get(0));
        }
        return result;
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        return new ArrayList<>(this.values);
    }

    @Override
    public Item getItemByKey(String key) {
        return getItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public Item getItemByKey(Item key) {
        if (this.keyToIndex == null) {
            rebuildKeyStringIndex();
        }
        if (!this.keyToIndex.containsKey(key)) {
            return null;
        }
        Integer index = this.keyToIndex.get(key);
        List<Item> valueSequence = this.values.get(index);
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
        if (this.keyToIndex == null) {
            rebuildKeyStringIndex();
        }
        if (!this.keyToIndex.containsKey(key)) {
            return null;
        }
        Integer index = this.keyToIndex.get(key);
        List<Item> valueSequence = this.values.get(index);
        return valueSequence;
    }

    @Override
    public void putItemByKey(String key, Item value) {
        putSequenceByKey(ItemFactory.getInstance().createStringItem(key), java.util.Collections.singletonList(value));
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        putSequenceByKey(key, java.util.Collections.singletonList(value));
    }

    @Override
    public void putSequenceByKey(String key, List<Item> valueSequence) {
        putSequenceByKey(ItemFactory.getInstance().createStringItem(key), valueSequence);
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        validateAtomicKey(key, ExceptionMetadata.EMPTY_METADATA);
        validateDuplicateKey(key, ExceptionMetadata.EMPTY_METADATA);
        internalPutSequenceByKey(key, valueSequence, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void removeItemByKey(String key) {
        removeItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public void removeItemByKey(Item key) {
        if (this.keyToIndex == null) {
            rebuildKeyStringIndex();
        }
        Integer idx = this.keyToIndex.remove(key);
        if (idx == null) {
            return;
        }
        int index = idx;
        this.values.remove(index);
        this.keys.remove(index);
        rebuildKeyStringIndex();
    }

    // endregion maps

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.keys);
        kryo.writeObject(output, this.values);
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        kryo.writeObject(output, this.pathIn);
        kryo.writeObject(output, this.location);
        output.writeDouble(this.topLevelOrder);
        output.writeBoolean(this.allKeysString);
        output.writeBoolean(this.allValuesSingletons);
        output.writeBoolean(this.recomputeObjectShapeCache);
        kryo.writeObjectOrNull(output, this.collection, Collection.class);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.keys = kryo.readObject(input, ArrayList.class);
        this.values = kryo.readObject(input, ArrayList.class);
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = kryo.readObject(input, String.class);
        this.location = kryo.readObject(input, String.class);
        this.topLevelOrder = input.readDouble();
        this.allKeysString = input.readBoolean();
        this.allValuesSingletons = input.readBoolean();
        this.recomputeObjectShapeCache = input.readBoolean();
        this.collection = kryo.readObjectOrNull(input, Collection.class);
        rebuildKeyStringIndex();
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
        return this.mutabilityLevel;
    }

    @Override
    public void setMutabilityLevel(int mutabilityLevel) {
        this.mutabilityLevel = mutabilityLevel;
        for (List<Item> sequence : this.values) {
            for (Item item : sequence) {
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
        for (List<Item> sequence : this.values) {
            for (Item item : sequence) {
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
        for (List<Item> sequence : this.values) {
            for (Item item : sequence) {
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
        for (Item key : this.keys) {
            int index = this.keyToIndex.get(key);
            String keyFragment = key.serialize();
            List<Item> sequence = this.values.get(index);
            for (Item item : sequence) {
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
        for (List<Item> sequence : this.values) {
            for (Item item : sequence) {
                item.setTableLocation(location);
            }
        }
    }

    @Override
    public String getSparkSQLValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("named_struct(");
        List<String> keysAsStrings = getKeys();
        List<Item> values = getValues();
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
        Map<String, FieldDescriptor> content = itemType.getObjectContentFacet();
        String[] keys = content.keySet().toArray(new String[0]);

        sb.append("named_struct(");
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            FieldDescriptor field = content.get(key);
            Item value = getItemByKey(key);

            sb.append("\"").append(key).append("\"").append(", ");
            if (value == null) {
                if (!field.isRequired()) {
                    sb.append("NULL");
                }
            } else {
                sb.append(value.getSparkSQLValue(field.getType()));
            }

            if (i + 1 < keys.length) {
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
        List<String> keysAsStrings = getKeys();
        List<Item> values = getValues();
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
                FunctionItemStringValueException.DEFAULT_MESSAGE,
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    @Override
    public Object getVariantValue() {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> keysAsStrings = getKeys();
        List<Item> values = getValues();
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
        for (List<Item> sequence : this.values) {
            for (Item item : sequence) {
                item.setCollection(collection);
            }
        }
    }

    @Override
    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item other = (Item) otherItem;
        if (!other.isObject()) {
            return false;
        }
        for (Item key : this.keys) {
            List<Item> thisSequence = getSequenceByKey(key);
            List<Item> otherSequence = other.getSequenceByKey(key);
            if (otherSequence == null || thisSequence.size() != otherSequence.size()) {
                return false;
            }
            for (int i = 0; i < thisSequence.size(); i++) {
                if (!thisSequence.get(i).equals(otherSequence.get(i))) {
                    return false;
                }
            }
        }
        for (Item key : other.getItemKeys()) {
            if (getSequenceByKey(key) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.keys.size();
        for (Item key : this.keys) {
            List<Item> valueSequence = getSequenceByKey(key);
            result += key.hashCode();
            for (Item item : valueSequence) {
                result += item.hashCode();
            }
        }
        return result;
    }
}
