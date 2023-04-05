package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ObjectItem;

public class InsertIntoObjectPrimitive extends UpdatePrimitive implements UpdatePrimitiveInterface {


    public InsertIntoObjectPrimitive(Item targetObject, Item sourceObject) {
        super(targetObject, sourceObject);
    }

    public ObjectItem getTargetObject() {
        return target.getTargetAsObject();
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

    public static UpdatePrimitiveSource mergeSources(UpdatePrimitiveSource first, UpdatePrimitiveSource second) {
        ObjectItem merged = new ObjectItem();
        ObjectItem objFirst = (ObjectItem) first.getSingletonSource();
        ObjectItem objSecond = (ObjectItem) second.getSingletonSource();
        try {
            for (String otherKey : objFirst.getKeys()) {
                merged.putItemByKey(otherKey, objFirst.getItemByKey(otherKey));
            }
            for (String otherKey : objSecond.getKeys()) {
                merged.putItemByKey(otherKey, objSecond.getItemByKey(otherKey));
            }
        } catch (DuplicateObjectKeyException e) {
            throw new OurBadException("SHOULD THROW SMTH ELSE");
            // TODO THROW jerr:JNUP0005 INSTEAD ON COLLISION
        }
        return new UpdatePrimitiveSource(merged);
    }

    @Override
    public boolean isInsertObject() {
        return true;
    }
}
