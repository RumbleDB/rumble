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
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayItem implements Item {


    private static final long serialVersionUID = 1L;
    private List<Item> arrayItems;
    private int mutabilityLevel;
    private long topLevelID;
    private String pathIn;
    private String location;
    private Collection collection;

    public ArrayItem() {
        super();
        this.arrayItems = new ArrayList<>();
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.collection = null;
    }

    public ArrayItem(List<Item> arrayItems) {
        super();
        this.arrayItems = arrayItems;
        this.mutabilityLevel = -1;
        this.topLevelID = -1;
        this.pathIn = "null";
        this.location = "null";
        this.collection = null;
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
    public List<List<Item>> getMemberSequences() {
        List<List<Item>> result = new ArrayList<>(this.arrayItems.size());
        for (Item item : this.arrayItems) {
            result.add(Collections.singletonList(item));
        }
        return result;
    }

    @Override
    public Item getItemAt(int i) {
        if (i >= this.arrayItems.size() || i < 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + (i + 1) + ", of array with length: " + this.arrayItems.size(),
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        return this.arrayItems.get(i);
    }

    @Override
    public List<Item> getMemberSequenceAt(int i) {
        Item member = this.getItemAt(i);
        return Collections.singletonList(member);
    }

    @Override
    public void putItem(Item value) {
        this.arrayItems.add(value);
    }

    @Override
    public void appendMemberSequence(List<Item> items) {
        if (items.size() == 1) {
            this.arrayItems.add(items.get(0));
            return;
        }
        throw new UnsupportedOperationException(
                "ArrayItem only supports singleton member sequences in this phase."
        );
    }

    @Override
    public void putItemAt(Item value, int i) {
        this.arrayItems.add(i, value);
    }

    @Override
    public void putMemberSequenceAt(List<Item> values, int i) {
        if (values.size() == 1) {
            this.arrayItems.set(i, values.get(0));
            return;
        }
        throw new UnsupportedOperationException(
                "ArrayItem only supports singleton member sequences in this phase."
        );
    }

    @Override
    public void putItemsAt(List<Item> values, int i) {
        this.arrayItems.addAll(i, values);
    }

    @Override
    public void removeItemAt(int i) {
        this.arrayItems.remove(i);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean allowsNonSingletons() {
        return false;
    }

    @Override
    public int getSize() {
        return this.arrayItems.size();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.arrayItems);
        output.writeInt(this.mutabilityLevel);
        output.writeLong(this.topLevelID);
        kryo.writeObject(output, this.pathIn);
        kryo.writeObject(output, this.location);
        kryo.writeObjectOrNull(output, this.collection, Collection.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.arrayItems = kryo.readObject(input, ArrayList.class);
        this.mutabilityLevel = input.readInt();
        this.topLevelID = input.readLong();
        this.pathIn = kryo.readObject(input, String.class);
        this.location = kryo.readObject(input, String.class);
        this.collection = kryo.readObjectOrNull(input, Collection.class);
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

    @Override
    public int getMutabilityLevel() {
        return this.mutabilityLevel;
    }

    @Override
    public void setMutabilityLevel(int mutabilityLevel) {
        this.mutabilityLevel = mutabilityLevel;
        for (Item item : this.arrayItems) {
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
        for (Item item : this.arrayItems) {
            item.setTopLevelID(topLevelID);
        }
    }

    @Override
    public String getPathIn() {
        return this.pathIn;
    }

    @Override
    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
        for (int i = 0; i < this.arrayItems.size(); i++) {
            Item item = this.arrayItems.get(i);
            item.setPathIn(pathIn + "[" + i + "]");
        }
    }

    @Override
    public String getTableLocation() {
        return this.location;
    }

    @Override
    public void setTableLocation(String location) {
        this.location = location;
        for (Item item : this.arrayItems) {
            item.setTableLocation(location);
        }
    }

    @Override
    public String getSparkSQLValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("array(");
        for (int i = 0; i < this.arrayItems.size(); i++) {
            Item item = this.arrayItems.get(i);
            sb.append(item.getSparkSQLValue());
            if (i + 1 < this.arrayItems.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        StringBuilder sb = new StringBuilder();
        ItemType elementType = itemType.getArrayContentFacet();
        sb.append("array(");
        for (int i = 0; i < this.arrayItems.size(); i++) {
            Item item = this.arrayItems.get(i);
            sb.append(item.getSparkSQLValue(elementType));
            if (i + 1 < this.arrayItems.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLType() {
        // TODO: Is it okay to assume first elem type is same as rest?
        StringBuilder sb = new StringBuilder();
        sb.append("ARRAY<");
        if (this.getSize() <= 0) {
            // TODO: Throw error? No empty arrays?
        }
        sb.append(this.getItemAt(0).getSparkSQLType());
        sb.append(">");
        return sb.toString();
    }

    @Override
    public List<Item> atomizedValue() {
        throw new CannotAtomizeException("tried to atomize Array", ExceptionMetadata.EMPTY_METADATA);
        // return getItems();
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
        List<Item> arrayItems = this.getItems();
        List<Object> arrayItemsForRow = new ArrayList<>(arrayItems.size());
        for (int i = 0; i < arrayItems.size(); i++) {
            arrayItemsForRow.add(this.getItemAt(i).getVariantValue());
        }
        return arrayItemsForRow;
    }

    @Override
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    @Override
    public Collection getCollection() {
        return this.collection;
    }
}
