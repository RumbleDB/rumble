package org.rumbledb.runtime.update.primitives;

import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

public class DeleteFromArrayPrimitive extends UpdatePrimitive {

    public DeleteFromArrayPrimitive(ArrayItem targetArray, IntItem positionInt) {
        super(targetArray, positionInt, positionInt);
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }
    }

    public ArrayItem getTargetArray() {
        return target.getTargetAsArray();
    }

    public IntItem getPositionInt() {
        return (IntItem) source.getSingletonSource();
    }

    @Override
    public void apply() {
        this.getTargetArray().removeItemAt(this.getPositionInt().getIntValue());
    }

}
