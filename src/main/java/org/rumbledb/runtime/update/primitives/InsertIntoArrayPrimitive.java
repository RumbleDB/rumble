package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

import java.util.List;

public class InsertIntoArrayPrimitive extends UpdatePrimitive {

    public InsertIntoArrayPrimitive(ArrayItem targetArray, IntItem positionInt, List<Item> sourceSequence) {
        super(targetArray, positionInt, sourceSequence);
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }
    }

    public ArrayItem getTargetArray() {
        return (ArrayItem) target.getMainTarget();
    }

    public IntItem getPositionInt() {
        return (IntItem) source.getLocator();
    }

    public List<Item> getSourceSequence() {
        return (List<Item>) source.getSourceItems();
    }

    @Override
    public void apply() {
        this.getTargetArray().putItemsAt(this.getSourceSequence(), this.getPositionInt().getIntValue());
    }

}
