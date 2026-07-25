package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.ItemType;

import java.io.Serial;

public class InstanceOfClosure implements Function<Item, Boolean> {
    private ItemType itemType;
    @Serial
    private static final long serialVersionUID = 1L;

    public InstanceOfClosure(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        return !InstanceOfIterator.doesItemTypeMatchItem(this.itemType, input);
    }
}
