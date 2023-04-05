package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

import java.util.ArrayList;
import java.util.List;

public class InsertIntoArrayPrimitive extends UpdatePrimitive implements UpdatePrimitiveInterface {

    public InsertIntoArrayPrimitive(Item targetArray, Item positionInt, List<Item> sourceSequence) {
        super(targetArray, positionInt, sourceSequence);
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }
    }

    public ArrayItem getTargetArray() {
        return target.getTargetAsArray();
    }

    public IntItem getPositionInt() {
        return selector.getSelectorAsInt();
    }

    public List<Item> getSourceSequence() {
        return source.getSourceAsListOfItems();
    }

    @Override
    public void apply() {
        this.getTargetArray().putItemsAt(this.getSourceSequence(), this.getPositionInt().getIntValue());
    }

    public static UpdatePrimitiveSource mergeSources(UpdatePrimitiveSource first, UpdatePrimitiveSource second) {
        List<Item> merged = new ArrayList<>(first.getSource());
        merged.addAll(second.getSource());
        return new UpdatePrimitiveSource(merged);
    }

    @Override
    public boolean isInsertArray() {
        return true;
    }
}
