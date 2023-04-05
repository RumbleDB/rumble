package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.IntItem;

public class ReplaceInArrayPrimitive extends UpdatePrimitive implements UpdatePrimitiveInterface {

    public ReplaceInArrayPrimitive(Item targetArray, Item positionInt, Item replacementItem) {
        super(targetArray, positionInt, replacementItem);
    }

    public ArrayItem getTargetArray() {
        return target.getTargetAsArray();
    }

    public IntItem getPositionInt() {
        return selector.getSelectorAsInt();
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

    @Override
    public boolean isReplaceArray() {
        return true;
    }
}
