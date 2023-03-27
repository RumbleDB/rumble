package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class ReplaceInObjectPrimitive extends UpdatePrimitive {

    public ReplaceInObjectPrimitive(ObjectItem targetObject, StringItem targetName, Item replacementItem) {
        super(targetObject, targetName, replacementItem);
    }

    public ObjectItem getTargetObject() {
        return (ObjectItem) target.getMainTarget();
    }

    public StringItem getTargetName() {
        return (StringItem) source.getLocator();
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

}
