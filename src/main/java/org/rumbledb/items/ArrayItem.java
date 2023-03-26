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
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import java.util.ArrayList;
import java.util.List;

public class ArrayItem implements Item {


    private static final long serialVersionUID = 1L;
    private List<Item> arrayItems;

    public ArrayItem() {
        super();
        this.arrayItems = new ArrayList<>();
    }

    public ArrayItem(List<Item> arrayItems) {
        super();
        this.arrayItems = arrayItems;
    }

    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isArray()) {
            return false;
        }
        if (getSize() != o.getSize()) {
            return false;
        }
        for (int i = 0; i < getSize(); ++i) {
            if (!getItemAt(i).equals(o.getItemAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void append(Item other) {
        this.arrayItems.add(other);
    }


    public List<Item> getItems() {
        return this.arrayItems;
    }

    @Override
    public Item getItemAt(int i) {
        return this.arrayItems.get(i);
    }

    @Override
    public void putItem(Item value) {
        this.arrayItems.add(value);
    }

    public void putItemAt(Item value, int i) {
        this.arrayItems.add(i, value);
    }

    public void putItemsAt(List<Item> values, int i) {
        this.arrayItems.addAll(i, values);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public int getSize() {
        return this.arrayItems.size();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.arrayItems);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.arrayItems = kryo.readObject(input, ArrayList.class);
    }

    public int hashCode() {
        int result = 0;
        result += getSize();
        for (int i = 0; i < getSize(); ++i) {
            result += getItemAt(i).hashCode();
        }
        return result;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.arrayItem;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return true;
    }
}
