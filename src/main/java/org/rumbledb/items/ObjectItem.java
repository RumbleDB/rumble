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
import org.rumbledb.exceptions.FunctionAtomizationException;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;

import java.util.*;

public class ObjectItem implements Item {


    private static final long serialVersionUID = 1L;
    private List<Item> values;
    private List<String> keys;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private double topLevelOrder;

    public ObjectItem() {
        super();
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.topLevelOrder = 0.0;
    }

    public ObjectItem(List<String> keys, List<Item> values, ExceptionMetadata itemMetadata) {
        super();
        checkForDuplicateKeys(keys, itemMetadata);
        this.keys = keys;
        this.values = values;
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.topLevelOrder = 0.0;
    }

    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isObject()) {
            return false;
        }
        for (String s : getKeys()) {
            Item v = o.getItemByKey(s);
            if (v == null) {
                return false;
            }
            if (!getItemByKey(s).equals(v)) {
                return false;
            }
        }
        for (String s : o.getKeys()) {
            Item v = getItemByKey(s);
            if (v == null) {
                return false;
            }
            if (!o.getItemByKey(s).equals(v)) {
                return false;
            }
        }
        return true;
    }

    /**
     * ObjectItem constructor from the given map data structure.
     * For each key, the corresponding values list is turned into an ArrayItem if it contains more than a single
     * element.
     *
     * @param keyValuePairs LinkedHashMap -- this map implementation preserves order of the keys -- essential for
     *        functionality
     */
    public ObjectItem(Map<String, List<Item>> keyValuePairs) {
        super();

        List<String> keyList = new ArrayList<>();
        List<Item> valueList = new ArrayList<>();
        for (String key : keyValuePairs.keySet()) {
            // add all keys to the keyList
            keyList.add(key);
            List<Item> values = keyValuePairs.get(key);
            // for each key, convert the lists of values into arrayItems
            if (values.size() > 1) {
                Item valuesArray = ItemFactory.getInstance().createArrayItem(values, false);
                valueList.add(valuesArray);
            } else if (values.size() == 1) {
                Item value = values.get(0);
                valueList.add(value);
            } else {
                throw new RuntimeException("Unexpected list size found.");
            }
        }

        this.keys = keyList;
        this.values = valueList;
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.topLevelOrder = 0.0;
    }

    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public List<Item> getValues() {
        return this.values;
    }

    private void checkForDuplicateKeys(List<String> keys, ExceptionMetadata metadata) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        for (String key : keys) {
            if (frequencies.containsKey(key)) {
                throw new DuplicateObjectKeyException(key, metadata);
            } else {
                frequencies.put(key, 1);
            }
        }
    }

    @Override
    public Item getItemByKey(String s) {
        if (this.keys.contains(s)) {
            return this.values.get(this.keys.indexOf(s));
        } else {
            return null;
        }
    }

    @Override
    public void putItemByKey(String s, Item value) {
        this.keys.add(s);
        this.values.add(value);
        checkForDuplicateKeys(this.keys, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void removeItemByKey(String s) {
        if (this.keys.contains(s)) {
            int index = this.keys.indexOf(s);
            this.values.remove(index);
            this.keys.remove(index);
        }
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.keys);
        kryo.writeObject(output, this.values);
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        kryo.writeObject(output, this.pathIn);
        kryo.writeObject(output, this.location);
        output.writeDouble(this.topLevelOrder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.keys = kryo.readObject(input, ArrayList.class);
        this.values = kryo.readObject(input, ArrayList.class);
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = kryo.readObject(input, String.class);
        this.location = kryo.readObject(input, String.class);
        this.topLevelOrder = input.readDouble();
    }

    public int hashCode() {
        int result = 0;
        result += getKeys().size();
        for (String s : getKeys()) {
            result += getItemByKey(s).hashCode();
        }
        return result;
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
        for (Item item : this.values) {
            item.setMutabilityLevel(mutabilityLevel);
        }
    }

    @Override
    public long getTopLevelID() {
        return this.topLevelID;
    }

    @Override
    public void setTopLevelID(long topLevelID) {
        this.topLevelID = topLevelID;
        for (Item item : this.values) {
            item.setTopLevelID(topLevelID);
        }
    }

    @Override
    public double getTopLevelOrder() {
        return this.topLevelOrder;
    }

    @Override
    public void setTopLevelOrder(double topLevelOrder) {
        this.topLevelOrder = topLevelOrder;
        // The loop below is redundant now, but might be useful to port inner updates to rowOrder (since rowOrder is
        // also candidate key)
        for (Item item : this.values) {
            item.setTopLevelOrder(topLevelOrder);
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
            String key = this.keys.get(i);
            Item item = this.values.get(i);
            item.setPathIn(pathIn + "." + key);
        }
    }

    @Override
    public String getTableLocation() {
        return this.location;
    }

    @Override
    public void setTableLocation(String location) {
        this.location = location;
        for (Item item : this.values) {
            item.setTableLocation(location);
        }
    }

    @Override
    public String getSparkSQLValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("named_struct(");
        for (int i = 0; i < this.keys.size(); i++) {
            sb.append("\"");
            sb.append(this.keys.get(i));
            sb.append("\"");
            sb.append(", ");
            sb.append(this.values.get(i).getSparkSQLValue());
            if (i + 1 < this.keys.size()) {
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
            int keyIndex = this.keys.indexOf(key);

            sb.append("\"");
            sb.append(key);
            sb.append("\"");
            sb.append(", ");

            if (keyIndex == -1) {
                if (!field.isRequired()) {
                    sb.append("NULL");
                }
            } else {
                sb.append(this.values.get(keyIndex).getSparkSQLValue(field.getType()));
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
        for (int i = 0; i < this.keys.size(); i++) {
            sb.append(this.keys.get(i));
            sb.append(": ");
            sb.append(this.values.get(i).getSparkSQLType());
            if (i + 1 < this.keys.size()) {
                sb.append(", ");
            }
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public List<Item> atomizedValue() {
        throw new FunctionAtomizationException("tried to atomize Object", ExceptionMetadata.EMPTY_METADATA);
    }
}
