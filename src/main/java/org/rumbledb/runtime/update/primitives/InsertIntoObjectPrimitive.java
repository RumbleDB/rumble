package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;

public class InsertIntoObjectPrimitive extends UpdatePrimitive {


    public InsertIntoObjectPrimitive(ObjectItem targetObject, ObjectItem sourceObject) {
        super(targetObject, sourceObject);
    }

    public ObjectItem getTargetObject() {
        return (ObjectItem) target.getMainTarget();
    }

    public ObjectItem getSourceObject() {
        return (ObjectItem) source.getSingletonSource();
    }

    @Override
    public void apply() {
        for (String key : this.getSourceObject().getKeys()) {
            this.getTargetObject().putItemByKey(key, this.getTargetObject().getItemByKey(key));
        }
    }

}
