package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class RenameInObjectPrimitive extends UpdatePrimitive {

    public RenameInObjectPrimitive(ObjectItem targetObject, StringItem targetName, StringItem replacementName) {
        super(targetObject, targetName, replacementName);
    }

    public ObjectItem getTargetObject() {
        return (ObjectItem) target.getMainTarget();
    }

    public StringItem getTargetName() {
        return (StringItem) source.getLocator();
    }

    public StringItem getReplacementName() {
        return (StringItem) source.getSingletonSource();
    }

    @Override
    public void apply() {
        String name = this.getTargetName().getStringValue();
        if (this.getTargetObject().getKeys().contains(name)) {
            Item item = this.getTargetObject().getItemByKey(name);
            this.getTargetObject().removeItemByKey(name);
            this.getTargetObject().putItemByKey(this.getTargetName().getStringValue(), item);
        }
        // TODO: implement replace and rename methods for Array & Object to avoid deletion and append
    }

}
