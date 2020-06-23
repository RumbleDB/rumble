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
import org.apache.commons.text.StringEscapeUtils;
import org.rumbledb.api.Item;
import org.rumbledb.types.ItemType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectItem extends JsonItem {


    private static final long serialVersionUID = 1L;
    private Map<String, Item> content;

    public ObjectItem() {
        super();
        this.content = new LinkedHashMap<>();
    }

    public ObjectItem(LinkedHashMap<String, Item> content) {
        super();
        this.content = content;
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

        this.content = new LinkedHashMap<>(keyValuePairs.size(), 1);
        for (String key : keyValuePairs.keySet()) {
            // add all keys to the keyList
            List<Item> values = keyValuePairs.get(key);
            // for each key, convert the lists of values into arrayItems
            if (values.size() > 1) {
                Item valuesArray = ItemFactory.getInstance().createArrayItem(values);
                this.content.put(key, valuesArray);
            } else if (values.size() == 1) {
                Item value = values.get(0);
                this.content.put(key, value);
            } else {
                throw new RuntimeException("Unexpected list size found.");
            }
        }
    }

    @Override
    public List<String> getKeys() {
        return new ArrayList<String>(this.content.keySet());
    }

    @Override
    public List<Item> getValues() {
        return new ArrayList<Item>(this.content.values());
    }

    @Override
    public Map<String, Item> getAsMap() {
        return this.content;
    }

    @Override
    public Item getItemByKey(String s) {
        if (this.content.containsKey(s)) {
            return this.content.get(s);
        } else {
            return null;
        }
    }

    @Override
    public void putItemByKey(String s, Item value) {
        this.content.put(s, value);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.objectItem) || super.isTypeOf(type);
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String comma = " ";
        for (String key : this.content.keySet()) {
            sb.append(comma);
            comma = ", ";

            Item value = this.content.get(key);
            boolean isStringValue = value.isString();
            sb.append("\"").append(StringEscapeUtils.escapeJson(key)).append("\"").append(" : ");
            if (isStringValue) {
                sb.append("\"");
                sb.append(StringEscapeUtils.escapeJson(value.serialize()));
                sb.append("\"");
            } else {
                sb.append(value.serialize());
            }

        }
        sb.append(" }");
        return sb.toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.content = kryo.readObject(input, LinkedHashMap.class);
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
        return ItemType.objectItem;
    }

}
