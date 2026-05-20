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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class MapEntryItem implements Item {

    private static final long serialVersionUID = 1L;

    /**
     * Insertion-ordered map keyed by {@link Item} (uses each key's {@code equals}/{@code hashCode}, typically value
     * comparison for atomics). When built via {@link #MapItem(Map, ExceptionMetadata)}, the live map may be aliased
     * if the argument is already a {@link LinkedHashMap}.
     */
    private Item key;
    private List<Item> value;

    public MapEntryItem() {
        this.key = null;
        this.value = Collections.emptyList();
    }

    public MapEntryItem(Item key, List<Item> value) {
        this.key = key;
        this.value = value;
    }

    // region maps

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return !this.key.isString() && this.value.size() == 1;
    }

    @Override
    public List<String> getKeys() {
        return Collections.singletonList(this.key.getStringValue());
    }

    @Override
    public List<String> getStringKeys() {
        return Collections.singletonList(this.key.getStringValue());
    }

    @Override
    public List<Item> getItemKeys() {
        return Collections.singletonList(this.key);
    }

    @Override
    public List<Item> getValues() {
        return this.getItemValues();
    }

    @Override
    public List<Item> getItemValues() {
        return this.value;
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        return Collections.singletonList(this.value);
    }

    @Override
    public Item getItemByKey(String key) {
        return getItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public Item getItemByKey(Item key) {
        if (key.equals(this.key)) {
            if (this.value.size() != 1) {
                throw new OurBadException("Map contains non-singleton values.");
            }
            return this.value.get(0);
        }
        return null;
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        return getSequenceByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        if (key.equals(this.key)) {
            return this.value;
        }
        return null;
    }

    @Override
    public void putItemByKey(String key, Item value) {
        throw new OurBadException("Cannot put an item by key in a MapEntryItem, which is not mutable.");
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        throw new OurBadException("Cannot put an item by key in a MapEntryItem, which is not mutable.");
    }

    @Override
    public void putSequenceByKey(String key, List<Item> valueSequence) {
        throw new OurBadException("Cannot put an item by key in a MapEntryItem, which is not mutable.");
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> valueSequence) {
        throw new OurBadException("Cannot put an item by key in a MapEntryItem, which is not mutable.");
    }

    @Override
    public void removeItemByKey(String key) {
        throw new OurBadException("Cannot remove an item by key in a MapEntryItem, which is not mutable.");
    }

    @Override
    public void removeItemByKey(Item key) {
        throw new OurBadException("Cannot remove an item by key in a MapEntryItem, which is not mutable.");
    }

    // endregion maps

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeClassAndObject(output, this.key);
        kryo.writeClassAndObject(output, this.value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.key = (Item) kryo.readClassAndObject(input);
        this.value = (List<Item>) kryo.readClassAndObject(input);
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
        throw new OurBadException("Cannot change mutability of a MapEntryItem, which is not mutable.");
    }

    @Override
    public long getTopLevelID() {
        throw new OurBadException("Map entry items have no top-level ID");
    }

    @Override
    public void setTopLevelID(long topLevelID) {
        throw new OurBadException("Cannot change top level ID of a MapEntryItem, which is not mutable.");
    }

    @Override
    public double getTopLevelOrder() {
        throw new OurBadException("Map entry items have no top-level order");
    }

    @Override
    public void setTopLevelOrder(double topLevelOrder) {
        throw new OurBadException("Cannot change top level order of a MapEntryItem, which is not mutable.");
    }

    @Override
    public String getPathIn() {
        throw new OurBadException("Map entry items have no path");
    }

    @Override
    public void setPathIn(String pathIn) {
        throw new OurBadException("Cannot change path of a MapEntryItem, which is not mutable.");
    }

    @Override
    public String getTableLocation() {
        throw new OurBadException("Map entry items have no table location");
    }

    @Override
    public void setTableLocation(String location) {
        throw new OurBadException("Cannot change table location of a MapEntryItem, which is not mutable.");
    }

    @Override
    public String getSparkSQLValue() {
        if (this.value.size() != 1) {
            throw new OurBadException("Maps with more than one value do not have a Spark SQL value.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("named_struct(");
        sb.append("\"");
        sb.append(this.key.getStringValue());
        sb.append("\"");
        sb.append(", ");
        sb.append(this.value.get(0).getSparkSQLValue());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        if (this.value.size() != 1) {
            throw new OurBadException("Maps with more than one value do not have a Spark SQL value.");
        }
        StringBuilder sb = new StringBuilder();
        Map<String, FieldDescriptor> content = itemType.getObjectContentFacet();

        sb.append("named_struct(");
        FieldDescriptor field = content.get(this.key.getStringValue());
        Item value = getItemByKey(this.key);

        sb.append("\"").append(this.key).append("\"").append(", ");
        if (value == null) {
            if (!field.isRequired()) {
                sb.append("NULL");
            }
        } else {
            sb.append(this.value.get(0).getSparkSQLValue(field.getType()));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getSparkSQLType() {
        if (this.value.size() != 1) {
            throw new OurBadException("Maps with more than one value do not have a Spark SQL type.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("STRUCT<");
        sb.append(this.key.getStringValue());
        sb.append(": ");
        sb.append(this.value.get(0).getSparkSQLType());
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
        if (this.value.size() != 1) {
            throw new OurBadException("Maps with more than one value do not have a Spark SQL variant value.");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(this.key.getStringValue(), this.value.get(0).getVariantValue());
        return resultMap;
    }

    @Override
    public Collection getCollection() {
        throw new OurBadException("Map entry items have no collection");
    }

    @Override
    public void setCollection(Collection collection) {
        throw new OurBadException("Cannot change collection of a MapEntryItem, which is not mutable.");
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
        List<Item> otherSequence = other.getSequenceByKey(this.key);
        if (otherSequence == null || this.value.size() != otherSequence.size()) {
            return false;
        }
        for (int i = 0; i < this.value.size(); i++) {
            if (!this.value.get(i).equals(otherSequence.get(i))) {
                return false;
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
        int result = this.value.size();
        result += this.key.hashCode();
        for (Item value : this.value) {
            result += value.hashCode();
        }
        return result;
    }
}
