/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.items;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.ItemType;

/**
 * Mutable identity and update metadata around an immutable CHAMP root. Functional map operations
 * share that root; in-place updates replace it without changing previously returned maps.
 */
public final class MutableMapItem extends AbstractMapItem {
    @Serial
    private static final long serialVersionUID = 1L;

    private PersistentMapItem map;
    private int mutabilityLevel = -1;
    private long topLevelID = -1;
    private String pathIn = "null";
    private String location = "null";
    private double topLevelOrder;
    private Collection collection;

    public MutableMapItem() {
        this.map = new PersistentMapItem();
    }

    public MutableMapItem(List<Item> keys, List<List<Item>> values, ExceptionMetadata metadata) {
        this.map = new PersistentMapItem(keys, values, metadata);
    }

    public MutableMapItem(Map<Item, List<Item>> values, ExceptionMetadata metadata) {
        this.map = new PersistentMapItem(values, metadata);
    }

    PersistentMapItem getPersistentMap() {
        return this.map;
    }

    @Override
    public Item copy(boolean mutable) {
        return this.map.copy(mutable);
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isObject() {
        return this.map.isObject();
    }

    @Override
    public int getSize() {
        return this.map.getSize();
    }

    @Override
    public List<Item> getItemKeys() {
        return this.map.getItemKeys();
    }

    @Override
    public List<String> getStringKeys() {
        return this.map.getStringKeys();
    }

    @Override
    public List<Item> getItemValues() {
        return this.map.getItemValues();
    }

    @Override
    public List<List<Item>> getSequenceValues() {
        return this.map.getSequenceValues();
    }

    @Override
    public boolean hasKey(Item key) {
        return key != null && this.map.hasKey(key);
    }

    @Override
    public boolean hasKey(String key) {
        return this.map.hasKey(key);
    }

    @Override
    public Item getItemByKey(Item key) {
        return key == null ? null : this.map.getItemByKey(key);
    }

    @Override
    public Item getItemByKey(String key) {
        return this.map.getItemByKey(key);
    }

    @Override
    public List<Item> getSequenceByKey(Item key) {
        return key == null ? null : this.map.getSequenceByKey(key);
    }

    @Override
    public List<Item> getSequenceByKey(String key) {
        return this.map.getSequenceByKey(key);
    }

    @Override
    public void putItemByKey(String key, Item value) {
        putSequenceByKey(key, Collections.singletonList(value));
    }

    @Override
    public void putItemByKey(Item key, Item value) {
        putSequenceByKey(key, Collections.singletonList(value));
    }

    @Override
    public void putSequenceByKey(String key, List<Item> value) {
        putSequenceByKey(ItemFactory.getInstance().createStringItem(key), value);
    }

    @Override
    public void putSequenceByKey(Item key, List<Item> value) {
        if (key == null || !key.isAtomic()) {
            throw new OurBadException("Map keys must be atomic items.");
        }
        if (this.map.hasKey(key)) {
            throw new DuplicateObjectKeyException(key.serialize(), ExceptionMetadata.EMPTY_METADATA);
        }
        this.map = this.map.withEntry(key, value);
    }

    @Override
    public void removeItemByKey(String key) {
        removeItemByKey(ItemFactory.getInstance().createStringItem(key));
    }

    @Override
    public void removeItemByKey(Item key) {
        if (key != null) this.map = this.map.withoutKeys(Collections.singletonList(key));
    }

    private void forEachValue(Consumer<Item> action) {
        this.map.forEachEntry((key, sequence) -> sequence.forEach(action));
    }

    @Override
    public ItemType getDynamicType() {
        return this.map.getDynamicType();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.map.getEffectiveBooleanValue();
    }

    @Override
    public int getMutabilityLevel() {
        return this.mutabilityLevel;
    }

    @Override
    public void setMutabilityLevel(int level) {
        this.mutabilityLevel = level;
        forEachValue(item -> item.setMutabilityLevel(level));
    }

    @Override
    public long getTopLevelID() {
        return this.topLevelID;
    }

    @Override
    public void setTopLevelID(long id) {
        this.topLevelID = id;
        forEachValue(item -> item.setTopLevelID(id));
    }

    @Override
    public double getTopLevelOrder() {
        return this.topLevelOrder;
    }

    @Override
    public void setTopLevelOrder(double order) {
        this.topLevelOrder = order;
        forEachValue(item -> item.setTopLevelOrder(order));
    }

    @Override
    public String getPathIn() {
        return this.pathIn;
    }

    @Override
    public void setPathIn(String path) {
        this.pathIn = path;
        this.map.forEachEntry((key, sequence) -> {
            String childPath = path + "." + key.serialize();
            sequence.forEach(item -> item.setPathIn(childPath));
        });
    }

    @Override
    public String getTableLocation() {
        return this.location;
    }

    @Override
    public void setTableLocation(String location) {
        this.location = location;
        forEachValue(item -> item.setTableLocation(location));
    }

    @Override
    public Collection getCollection() {
        return this.collection;
    }

    @Override
    public void setCollection(Collection collection) {
        this.collection = collection;
        forEachValue(item -> item.setCollection(collection));
    }

    @Override
    public String getSparkSQLValue() {
        return this.map.getSparkSQLValue();
    }

    @Override
    public String getSparkSQLValue(ItemType type) {
        return this.map.getSparkSQLValue(type);
    }

    @Override
    public String getSparkSQLType() {
        return this.map.getSparkSQLType();
    }

    @Override
    public Object getVariantValue() {
        return this.map.getVariantValue();
    }

    @Override
    public List<Item> atomizedValue() {
        return this.map.atomizedValue();
    }

    @Override
    public String getStringValue() {
        return this.map.getStringValue();
    }
}
