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
    private Map<String, LazyValue> lazyValues;

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
            return ItemFactory.getInstance().createArrayItem(items);
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

    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    private void materialize() {
        for (Map.Entry<String, LazyValue> e : this.lazyValues.entrySet()) {
            this.values.put(e.getKey(), e.getValue().getItem());
        }
        this.lazyValues.clear();
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
            if (this.values.containsKey(s)) {
                return this.values.get(s);
            }
            if (this.lazyValues.containsKey(s)) {
                Item i = this.lazyValues.get(s).getItem();
                this.lazyValues.remove(s);
                this.values.put(s, i);
            }
            throw new OurBadException("Key " + s + "not found in lazy object. Inconsistent layout.");
        } else {
            return null;
        }
    }

    @Override
    public void putItemByKey(String s, Item value) {
        this.keys.add(s);
        this.values.put(s, value);
        checkForDuplicateKeys(this.keys, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public void putLazyItemByKey(String s, RuntimeIterator iterator, DynamicContext context, boolean isArray) {
        this.keys.add(s);
        LazyValue lv = new LazyValue(iterator, context, isArray);
        this.lazyValues.put(s, lv);
        checkForDuplicateKeys(this.keys, ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public boolean isObject() {
        return true;
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
