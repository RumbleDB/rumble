package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;

public class DeleteFromArrayPrimitive implements UpdatePrimitive {


    private Item target;
    private Item selector;

    public DeleteFromArrayPrimitive(Item targetArray, Item positionInt, ExceptionMetadata metadata) {
        if (positionInt.getIntValue() <= 0 || positionInt.getIntValue() > targetArray.getSize()) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot delete item at index out of range of target array",
                    metadata
            );
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
