package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

import java.util.Collections;
import java.util.List;

public class UpdatePrimitiveSource {

    private List<? extends Item> sourceItems;

    private Item locator;

    public UpdatePrimitiveSource(List<? extends Item> sourceItems, Item locator) {
        this.sourceItems = sourceItems;
        this.locator = locator;
    }

    public UpdatePrimitiveSource(Item sourceItem, Item locator) {
        this(Collections.singletonList(sourceItem), locator);
    }

    public UpdatePrimitiveSource(List<? extends Item> sourceItems) {
        this(sourceItems, null);
    }

    public UpdatePrimitiveSource(Item sourceItem) {
        this(sourceItem, null);
    }

    public boolean isSingleton() {
        return this.sourceItems.size() == 1;
    }

    public Item getSingletonSource() {
        if (!this.isSingleton()) {
            throw new OurBadException("Invalid call to getSingletonSource when source of UpdatePrimitiveSource is a list");
        }
        return this.sourceItems.get(0);
    }

    public List<? extends Item> getSourceItems() {
        return sourceItems;
    }
    public Item getLocator() {
        if (!this.hasLocator()) {
            throw new OurBadException("Invalid call to getLocator for UpdatePrimitiveTarget without locator");
        }
        return locator;
    }

    public boolean hasLocator() {
        return this.locator != null;
    }

}
