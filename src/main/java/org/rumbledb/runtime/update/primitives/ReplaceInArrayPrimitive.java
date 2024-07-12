package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;

public class ReplaceInArrayPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public ReplaceInArrayPrimitive(
            Item targetArray,
            Item positionInt,
            Item replacementItem,
            ExceptionMetadata metadata
    ) {
        if (positionInt.getIntValue() <= 0 || positionInt.getIntValue() > targetArray.getSize()) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot replace item at index out of range of target array",
                    metadata
            );
        }

        this.target = targetArray;
        this.selector = positionInt;
        this.content = replacementItem;
    }

    @Override
    public void apply() {
        int index = this.selector.getIntValue() - 1;
        if (index >= 0 || index < this.target.getSize()) {
            this.target.removeItemAt(index);
            if (index == this.target.getSize()) {
                this.target.append(this.content);
            } else {
                this.target.putItemAt(this.content, index);
            }
        }
    }

    @Override
    public boolean hasSelector() {
        return true;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public Item getSelector() {
        return this.selector;
    }

    @Override
    public int getIntSelector() {
        return this.selector.getIntValue();
    }

    @Override
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isReplaceArray() {
        return true;
    }
}
