package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;

public class DeleteFromArrayPrimitive implements UpdatePrimitive {


    private Item target;
    private Item selector;

    public DeleteFromArrayPrimitive(Item targetArray, Item positionInt) {
        if (!targetArray.isArray() || !positionInt.isNumeric()) {
            // TODO ERROR
        }
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }
        this.target = targetArray;
        this.selector = positionInt;
    }

    @Override
    public void apply() {
        this.target.removeItemAt(this.selector.getIntValue() - 1);
    }

    @Override
    public boolean hasSelector() {
        return true;
    }

    @Override
    public Item getTarget() {
        return target;
    }

    @Override
    public Item getSelector() {
        return selector;
    }

    @Override
    public int getIntSelector() {
        return selector.getIntValue();
    }

    @Override
    public Item getContent() {
        return null;
    }

    @Override
    public boolean isDeleteArray() {
        return true;
    }
}
