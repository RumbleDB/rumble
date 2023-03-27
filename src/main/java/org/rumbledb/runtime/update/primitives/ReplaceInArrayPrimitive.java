package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

public class ReplaceInArrayPrimitive extends UpdatePrimitive {

    public ReplaceInArrayPrimitive(ArrayItem targetArray, IntItem positionInt, Item replacementItem) {
        super(targetArray, positionInt, replacementItem);
    }

    public ArrayItem getTargetArray() {
        return (ArrayItem) target.getMainTarget();
    }

    public IntItem getPositionInt() {
        return (IntItem) source.getLocator();
    }

    public Item getReplacementItem() {
        return source.getSingletonSource();
    }

    @Override
    public void apply() {
        int index = this.getPositionInt().getIntValue() - 1;
        if (index >= 0 || index < this.getTargetArray().getSize()) {
            this.getTargetArray().removeItemAt(index);
            if (index == this.getTargetArray().getSize()) {
                this.getTargetArray().append(this.getReplacementItem());
            } else {
                this.getTargetArray().putItemAt(this.getReplacementItem(), index);
            }
        }
    }

}
