package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class ReplaceInObjectPrimitive extends UpdatePrimitive implements UpdatePrimitiveInterface {

    public ReplaceInObjectPrimitive(Item targetObject, Item targetName, Item replacementItem) {
        super(targetObject, targetName, replacementItem);
    }

    public ObjectItem getTargetObject() {
        return target.getTargetAsObject();
    }

    public StringItem getTargetName() {
        return selector.getSelectorAsString();
    }

    public Item getReplacementItem() {
        return source.getSingletonSource();
    }

    @Override
    public void apply() {
        String name = this.getTargetName().getStringValue();
        if (this.getTargetObject().getKeys().contains(name)) {
            this.getTargetObject().removeItemByKey(name);
            this.getTargetObject().putItemByKey(name, this.getReplacementItem());
        }
    }

    @Override
    public boolean isReplaceObject() {
        return true;
    }
}
