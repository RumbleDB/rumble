package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class RenameInObjectPrimitive extends UpdatePrimitive {

    private ObjectItem targetObject;

    private StringItem targetName;

    private StringItem replacementName;

    public RenameInObjectPrimitive(ObjectItem targetObject, StringItem targetName, StringItem replacementName) {
        this.targetObject = targetObject;
        this.targetName = targetName;
        this.replacementName = replacementName;
    }

    public ObjectItem getTargetObject() {
        return targetObject;
    }

    public StringItem getTargetName() {
        return targetName;
    }

    public StringItem getReplacementName() {
        return replacementName;
    }

    @Override
    public void apply() {
        String name = this.targetName.getStringValue();
        if (this.targetObject.getKeys().contains(name)) {
            Item item = this.targetObject.getItemByKey(name);
            this.targetObject.removeItemByKey(name);
            this.targetObject.putItemByKey(this.replacementName.getStringValue(), item);
        }
        // TODO: implement replace and rename methods for Array & Object to avoid deletion and append
    }
}
