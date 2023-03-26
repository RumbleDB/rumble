package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

public class ReplaceInArrayPrimitive extends UpdatePrimitive {

    private ArrayItem targetArray;

    private IntItem positionInt;

    private Item replacementItem;

    public ReplaceInArrayPrimitive(ArrayItem targetArray, IntItem positionInt, Item replacementItem) {
        this.targetArray = targetArray;
        this.positionInt = positionInt;
        this.replacementItem = replacementItem;
    }

    public ArrayItem getTargetArray() {
        return targetArray;
    }

    public IntItem getPositionInt() {
        return positionInt;
    }

    public Item getReplacementItem() {
        return replacementItem;
    }

    @Override
    public void apply() {
        int index = this.positionInt.getIntValue() - 1;
        if (index >= 0 || index < this.targetArray.getSize()) {
            this.targetArray.removeItemAt(index);
            if (index == this.targetArray.getSize()) {
                this.targetArray.append(this.replacementItem);
            } else {
                this.targetArray.putItemAt(this.replacementItem, index);
            }
        }
    }

    @Override
    public Item getTarget() {
        return targetArray;
    }
}
