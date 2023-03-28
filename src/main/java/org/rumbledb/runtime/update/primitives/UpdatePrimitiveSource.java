package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.StringItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdatePrimitiveSource {

    private List<? extends Item> source;

    public UpdatePrimitiveSource(List<? extends Item> source) {
        this.source = source;
    }

    public UpdatePrimitiveSource(Item source) {
        this.source = Collections.singletonList(source);
    }

    public List<? extends Item> getSource() {
        return source;
    }

    public Item getSingletonSource() {
        if (this.isSingleton()) {
            return this.source.get(0);
        }
        // TODO: Find out if error
        return null;
    }

    public boolean isSingleton() {
        return this.source.size() == 1;
    }

    public List<StringItem> getSourceAsListOfStrings() {
        List<StringItem> result = new ArrayList<>();
        for (Item item : this.source) {
            result.add((StringItem) item);
        }
        return result;
    }

    public List<Item> getSourceAsListOfItems() {
        return new ArrayList<>(this.source);
    }

}
