package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;

public class ReplaceInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public ReplaceInObjectPrimitive(Item targetObject, Item targetName, Item replacementItem) {

        if (!targetObject.isObject() || !targetName.isString()) {
            // TODO ERROR
        }

        this.target = targetObject;
        this.selector = targetName;
        this.content = replacementItem;
    }

    @Override
    public void apply() {
        String name = this.getSelector().getStringValue();
        if (this.getTarget().getKeys().contains(name)) {
            ((ObjectItem) this.getTarget()).removeItemByKey(name);
            this.getTarget().putItemByKey(name, this.getContent());
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
    public boolean isReplaceObject() {
        return true;
    }
}
