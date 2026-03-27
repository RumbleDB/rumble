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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LazyObjectItem implements Item {


    private static final long serialVersionUID = 1L;
    private List<String> keys;
    private Map<String, Item> values;
    transient private Map<String, LazyValue> lazyValues;

    public class LazyValue {
        private RuntimeIterator iterator;
        private DynamicContext context;
        private boolean isArray;

        public LazyValue(RuntimeIterator iterator, DynamicContext context, boolean isArray) {
            this.iterator = iterator;
            this.context = context;
            this.isArray = isArray;
        }

        public RuntimeIterator getIterator() {
            return this.iterator;
        }

        public DynamicContext getDynamicContext() {
            return this.context;
        }

        public boolean isArray() {
            return this.isArray();
        }

        public Item getItem() {
            List<Item> items = this.iterator.materialize(this.context);
            if (!this.isArray) {
                if (items.size() == 1) {
                    return items.get(0);
                }
                if (items.size() == 0) {
                    return ItemFactory.getInstance().createNullItem();
                }
            }
            return ItemFactory.getInstance().createArrayItem(items, true);
        }
    }

    public LazyObjectItem() {
        super();
        this.keys = new ArrayList<>();
        this.values = new HashMap<>();
        this.lazyValues = new HashMap<>();
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


    // region maps

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public List<String> getStringKeys() {
        return this.keys;
    }

    @Override
    public List<Item> getItemKeys() {
        List<Item> result = new ArrayList<>(this.keys.size());
        for (String key : this.keys) {
            result.add(ItemFactory.getInstance().createStringItem(key));
        }
        return result;
    }

    @Override
    public List<Item> getValues() {
        List<Item> result = new ArrayList<>();
        materialize();
        for (Item i : this.values.values()) {
            result.add(i);
        }
        return result;
    }

    @Override
    public List<Item> getItemValues() {
        return getValues();
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        List<List<Item>> result = new ArrayList<>(this.keys.size());
        for (String key : this.keys) {
            result.add(java.util.Collections.singletonList(getItemByKey(key)));
        }
        return result;
    }

    @Override
    public Item getItemByKey(String key) {
        if (this.keys.contains(key)) {
            if (this.values.containsKey(key)) {
                return this.values.get(key);
            }
            if (this.lazyValues.containsKey(key)) {
                Item i = this.lazyValues.get(key).getItem();
                this.lazyValues.remove(key);
                this.values.put(key, i);
                return i;
            }
            throw new OurBadException("Key " + key + "not found in lazy object. Inconsistent layout.");
        } else {
            return null;
        }
    }

    @Override
    public Item getItemByKey(Item key) {
        if (key == null || !key.isString()) {
            return null;
        }
        return getItemByKey(key.getStringValue());
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        Item value = getItemByKey(key);
        if (value == null) {
            return null;
        }
        return java.util.Collections.singletonList(value);
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        if (key == null || !key.isString()) {
            return null;
        }
        return getSequenceByKey(key.getStringValue());
    }

    @Override
    public void putItemByKey(String key, Item value) {
        this.keys.add(key);
        this.values.put(key, value);
        checkForDuplicateKeys(this.keys, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        if (key == null || !key.isString()) {
            throw new OurBadException("LazyObjectItem keys must be strings.");
        }
        putItemByKey(key.getStringValue(), value);
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        if (valueSequence == null || valueSequence.isEmpty()) {
            putItemByKey(key, ItemFactory.getInstance().createNullItem());
            return;
        }
        if (valueSequence.size() != 1) {
            throw new OurBadException(
                    "LazyObjectItem only supports singleton values; use MapItem for non-singleton sequences."
            );
        }
        putItemByKey(key, valueSequence.get(0));
    }

    @Override
    public void putSequenceByKey(String key, List<Item> valueSequence){
        if (valueSequence == null) {
            throw new OurBadException("Value sequence cannot be empty.");
        }
        if (valueSequence.size() == 1) {
            putItemByKey(key, valueSequence.get(0));
            return;
        }
        throw new OurBadException(
                "ObjectItem only supports singleton values; use MapItem for non-singleton sequences."
        );
    }

    @Override
    public void removeItemByKey(String key) {
        if (this.keys.contains(key)) {
            this.keys.remove(key);
            this.values.remove(key);
            this.lazyValues.remove(key);
        }
    }

    @Override
    public void removeItemByKey(Item key) {
        if (key == null || !key.isString()) {
            return;
        }
        removeItemByKey(key.getStringValue());
    }

    @Override
    public void putLazyItemByKey(String key, RuntimeIterator iterator, DynamicContext context, boolean isArray) {
        this.keys.add(key);
        LazyValue lv = new LazyValue(iterator, context, isArray);
        this.lazyValues.put(key, lv);
        checkForDuplicateKeys(this.keys, ExceptionMetadata.EMPTY_METADATA);
    }

    // endregion maps

    private void materialize() {
        for (Map.Entry<String, LazyValue> e : this.lazyValues.entrySet()) {
            this.values.put(e.getKey(), e.getValue().getItem());
        }
        this.lazyValues.clear();
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
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.keys);
        materialize();
        kryo.writeObject(output, this.values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.keys = kryo.readObject(input, ArrayList.class);
        this.values = kryo.readObject(input, HashMap.class);
        this.lazyValues = new HashMap<>();
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

}
