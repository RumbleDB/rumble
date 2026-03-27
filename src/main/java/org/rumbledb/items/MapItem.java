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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapItem implements Item {

    private static final long serialVersionUID = 1L;

    private List<Item> keys;
    private List<List<Item>> valueSequences;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private double topLevelOrder;
    private Collection collection;

    public MapItem() {
        this.keys = new ArrayList<>();
        this.valueSequences = new ArrayList<>();
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
            validateAtomicKey(keys.get(i));
            putSequenceByKeyInternal(keys.get(i), valueSequences.get(i), metadata);
        }
    }

    private void validateAtomicKey(Item key) {
        if (key == null || !key.isAtomic()) {
            throw new OurBadException("Map keys must be atomic items.");
        }
    }

    private void putSequenceByKeyInternal(Item key, List<Item> valueSequence, ExceptionMetadata metadata) {
        validateAtomicKey(key);
        if (findKeyPosition(key) != -1) {
            throw new DuplicateObjectKeyException(key.serialize(), metadata);
        }
        this.keys.add(key);
        this.valueSequences.add(valueSequence == null ? new ArrayList<>() : valueSequence);
    }

    private boolean keysMatch(Item left, Item right) {
        return MapAtomicSameKey.sameKey(left, right);
    }

    private int findKeyPosition(Item key) {
        for (int i = 0; i < this.keys.size(); i++) {
            if (keysMatch(this.keys.get(i), key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean allowsNonSingletons() {
        return true;
    }

    @Override
    public List<Item> getItemKeys() {
        return this.keys;
    }

    @Override
    public List<List<Item>> getSequences() {
        return this.valueSequences;
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        if (key == null || !key.isAtomic()) {
            return null;
        }
        int position = findKeyPosition(key);
        if (position == -1) {
            return null;
        }
        return this.valueSequences.get(position);
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        return getSequenceByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public Item getItemByKey(String key) {
        List<Item> sequence = getSequenceByKey(key);
        if (sequence == null) {
            return null;
        }
        if (sequence.isEmpty()) {
            return ItemFactory.getInstance().createNullItem();
        }
        if (sequence.size() == 1) {
            return sequence.get(0);
        }
        return ItemFactory.getInstance()
            .createArrayFromMemberSequences(java.util.Collections.singletonList(sequence), false);
    }

    @Override
    public void putItemByKey(String key, Item value) {
        putItemByKey(ItemFactory.getInstance().createStringItem(key), value);
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        putSequenceByKey(key, java.util.Collections.singletonList(value));
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        putSequenceByKeyInternal(key, valueSequence, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void removeItemByKey(String key) {
        removeItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public void removeItemByKey(Item key) {
        int position = findKeyPosition(key);
        if (position == -1) {
            return;
        }
        this.keys.remove(position);
        this.valueSequences.remove(position);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.keys);
        kryo.writeObject(output, this.valueSequences);
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        kryo.writeObject(output, this.pathIn);
        kryo.writeObject(output, this.location);
        output.writeDouble(this.topLevelOrder);
        kryo.writeObjectOrNull(output, this.collection, Collection.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.keys = kryo.readObject(input, ArrayList.class);
        this.valueSequences = kryo.readObject(input, ArrayList.class);
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = kryo.readObject(input, String.class);
        this.location = kryo.readObject(input, String.class);
        this.topLevelOrder = input.readDouble();
        this.collection = kryo.readObjectOrNull(input, Collection.class);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.objectItem;
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
        for (List<Item> sequence : this.valueSequences) {
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
        for (List<Item> sequence : this.valueSequences) {
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
        for (List<Item> sequence : this.valueSequences) {
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
        for (int i = 0; i < this.keys.size(); i++) {
            String keyFragment = this.keys.get(i).serialize();
            List<Item> sequence = this.valueSequences.get(i);
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
        for (List<Item> sequence : this.valueSequences) {
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
        throw new CannotAtomizeException("tried to atomize Object", ExceptionMetadata.EMPTY_METADATA);
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
        for (List<Item> sequence : this.valueSequences) {
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
            if (thisSequence == null || otherSequence == null || thisSequence.size() != otherSequence.size()) {
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
        for (int i = 0; i < this.keys.size(); i++) {
            result += this.keys.get(i).hashCode();
            for (Item value : this.valueSequences.get(i)) {
                result += value.hashCode();
            }
        }
        return result;
    }
}
