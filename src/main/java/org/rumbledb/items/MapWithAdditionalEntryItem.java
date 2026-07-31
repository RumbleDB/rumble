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

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;


public class MapWithAdditionalEntryItem extends AbstractMapItem {

    @Serial
    private static final long serialVersionUID = 1L;

    static final int MAX_CHAIN_LENGTH = 32;

    /**
     * This is an optimization version of maps when there is exactly one key-value pair.
     */
    private final Item original;
    private final Item additionalKey;
    private final List<Item> additionalValue;
    private final int size;
    private final int chainLength;

    public MapWithAdditionalEntryItem(Item original, Item additionalKey, List<Item> additionalValue) {
        this.original = original;
        this.additionalKey = additionalKey;
        this.additionalValue = additionalValue;
        this.size = original.getSize() + (original.hasKey(additionalKey) ? 0 : 1);
        this.chainLength = original instanceof MapWithAdditionalEntryItem mapWithAdditionalEntry
            ? mapWithAdditionalEntry.chainLength + 1
            : 1;
    }

    int getChainLength() {
        return this.chainLength;
    }

    @Override
    public Item copy(boolean mutable) {
        if (mutable) {
            List<String> keys = this.getStringKeys();
            List<Item> values = new ArrayList<>(keys.size());
            for (String key : keys) {
                values.add(getItemByKey(key).copy(true));
            }
            Item result = new ObjectItem(keys, values, ExceptionMetadata.EMPTY_METADATA);
            result.setMutabilityLevel(0);
            return result;
        }
        return new MapItem(
                this.getItemKeys().stream().map(item -> item.copy(mutable)).toList(),
                this.getSequenceValues()
                    .stream()
                    .map(item -> item.stream().map(subitem -> subitem.copy(mutable)).toList())
                    .toList(),
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    // region maps

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return this.original.isObject() && this.additionalKey.isString() && this.additionalValue.size() == 1;
    }

    @Override
    public List<String> getStringKeys() {
        List<String> result = new ArrayList<>();
        for (String key : this.original.getStringKeys()) {
            if (!key.equals(this.additionalKey.getStringValue())) {
                result.add(key);
            }
        }
        if (this.additionalKey.isString()) {
            result.add(this.additionalKey.getStringValue());
            return result;
        }
        throw new OurBadException("Map is not an object.");
    }

    @Override
    public List<Item> getItemKeys() {
        List<Item> result = new ArrayList<>();
        for (Item key : this.original.getItemKeys()) {
            if (!AtomicItemEquivalence.equivalent(key, this.additionalKey)) {
                result.add(key);
            }
        }
        result.add(this.additionalKey);
        return result;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean hasKey(String key) throws UnsupportedOperationException {
        if (this.additionalKey.isString() && this.additionalKey.getStringValue().equals(key)) {
            return true;
        }
        return this.original.hasKey(key);
    }

    @Override
    public boolean hasKey(Item key) throws UnsupportedOperationException {
        if (AtomicItemEquivalence.equivalent(this.additionalKey, key)) {
            return true;
        }
        return this.original.hasKey(key);
    }

    @Override
    public List<Item> getItemValues() {
        List<Item> result = new ArrayList<>();
        for (Item key : this.original.getItemKeys()) {
            if (AtomicItemEquivalence.equivalent(key, this.additionalKey)) {
                continue;
            }
            result.add(this.original.getItemByKey(key));
        }
        if (this.additionalValue.size() != 1) {
            throw new OurBadException("Map is not an object.");
        }
        result.add(this.additionalValue.get(0));
        return result;
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        List<List<Item>> result = new ArrayList<>();
        for (Item key : this.original.getItemKeys()) {
            if (AtomicItemEquivalence.equivalent(key, this.additionalKey)) {
                continue;
            }
            result.add(this.original.getSequenceByKey(key));
        }
        result.add(this.additionalValue);
        return result;
    }

    @Override
    public Item getItemByKey(String key) {
        if (this.additionalKey.isString() && this.additionalKey.getStringValue().equals(key)) {
            if (this.additionalValue.size() != 1) {
                throw new OurBadException("Map is not an object.");
            }
            return this.additionalValue.get(0);
        }
        return this.original.getItemByKey(key);
    }

    @Override
    public Item getItemByKey(Item key) {
        if (AtomicItemEquivalence.equivalent(this.additionalKey, key)) {
            if (this.additionalValue.size() != 1) {
                throw new OurBadException("Map is not an object.");
            }
            return this.additionalValue.get(0);
        }
        return this.original.getItemByKey(key);
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        if (this.additionalKey.isString() && this.additionalKey.getStringValue().equals(key)) {
            return this.additionalValue;
        }
        return this.original.getSequenceByKey(key);
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        if (AtomicItemEquivalence.equivalent(this.additionalKey, key)) {
            return this.additionalValue;
        }
        return this.original.getSequenceByKey(key);
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
        throw new OurBadException("No Spark SQL value.");
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        throw new OurBadException("No Spark SQL value.");
    }

    @Override
    public String getSparkSQLType() {
        throw new OurBadException("No Spark SQL type.");
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
        throw new OurBadException("No variant value.");
    }

    @Override
    public Collection getCollection() {
        throw new OurBadException("Map entry items have no collection");
    }

    @Override
    public void setCollection(Collection collection) {
        throw new OurBadException("Cannot change collection of a MapEntryItem, which is not mutable.");
    }

}
