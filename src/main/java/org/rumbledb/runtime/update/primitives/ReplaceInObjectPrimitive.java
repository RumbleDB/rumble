package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class ReplaceInObjectPrimitive extends UpdatePrimitive {

    private ObjectItem targetObject;

    private StringItem targetName;

    private Item replacementItem;

    public ReplaceInObjectPrimitive(ObjectItem targetObject, StringItem targetName, Item replacementItem) {
        this.targetObject = targetObject;
        this.targetName = targetName;
        this.replacementItem = replacementItem;
    }

    public ObjectItem getTargetObject() {
        return targetObject;
    }

    public StringItem getTargetName() {
        return targetName;
    }

    public Item getReplacementItem() {
        return replacementItem;
    }

    @Override
    public void apply() {
        String name = this.targetName.getStringValue();
        if (this.targetObject.getKeys().contains(name)) {
            this.targetObject.removeItemByKey(name);
            this.targetObject.putItemByKey(name, this.replacementItem);
        }
    }
}
