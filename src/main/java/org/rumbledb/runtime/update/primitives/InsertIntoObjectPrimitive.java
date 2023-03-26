package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;

public class InsertIntoObjectPrimitive extends UpdatePrimitive {

    private ObjectItem targetObject;
    private ObjectItem sourceObject;

    public InsertIntoObjectPrimitive(ObjectItem targetObject, ObjectItem sourceObject) {
        this.targetObject = targetObject;
        this.sourceObject = sourceObject;
    }

    public ObjectItem getTargetObject() {
        return targetObject;
    }

    public ObjectItem getSourceObject() {
        return sourceObject;
    }

    @Override
    public void apply() {
        for (String key : this.sourceObject.getKeys()) {
            this.targetObject.putItemByKey(key, this.sourceObject.getItemByKey(key));
        }
    }

    @Override
    public Item getTarget() {
        return targetObject;
    }
}
