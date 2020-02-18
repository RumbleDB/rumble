package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.semantics.types.ItemType;

public class InstanceOfClosure implements Function<Item, Boolean> {
    private ItemType itemType;

    public InstanceOfClosure(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        return !input.isTypeOf(this.itemType);
    }
}
