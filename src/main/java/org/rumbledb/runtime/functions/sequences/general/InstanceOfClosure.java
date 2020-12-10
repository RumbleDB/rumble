package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.ItemType;

public class InstanceOfClosure implements Function<Item, Boolean> {
    private ItemType itemType;
    private static final long serialVersionUID = 1L;

    public InstanceOfClosure(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        return !InstanceOfIterator.doesItemTypeMatchItem(this.itemType, input);
    }
}
