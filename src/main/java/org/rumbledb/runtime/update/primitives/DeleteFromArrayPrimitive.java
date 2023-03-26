package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

public class DeleteFromArrayPrimitive extends UpdatePrimitive {

    private ArrayItem targetArray;

    private IntItem positionInt;

    public DeleteFromArrayPrimitive(ArrayItem targetArray, IntItem positionInt) {
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }
        this.targetArray = targetArray;
        this.positionInt = positionInt;
    }

    public ArrayItem getTargetArray() {
        return targetArray;
    }

    public IntItem getPositionInt() {
        return positionInt;
    }

    @Override
    public void apply() {
        this.targetArray.removeItemAt(this.positionInt.getIntValue());
    }

    @Override
    public Item getTarget() {
        return targetArray;
    }
}
