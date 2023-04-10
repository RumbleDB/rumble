package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ArrayItem;

public class ReplaceInArrayPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public ReplaceInArrayPrimitive(Item targetArray, Item positionInt, Item replacementItem) {
        if (!targetArray.isArray() || !positionInt.isNumeric()) {
            // TODO ERROR
        }

        this.target = targetArray;
        this.selector = positionInt;
        this.content = replacementItem;
    }

    @Override
    public void apply() {
        int index = this.selector.getIntValue() - 1;
        if (index >= 0 || index < this.target.getSize()) {
            ((ArrayItem) this.target).removeItemAt(index);
            if (index == this.target.getSize()) {
                this.target.append(this.content);
            } else {
                ((ArrayItem) this.target).putItemAt(this.content, index);
            }
        }
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
    public Item getContent() {
        return content;
    }

    @Override
    public boolean isReplaceArray() {
        return true;
    }
}
