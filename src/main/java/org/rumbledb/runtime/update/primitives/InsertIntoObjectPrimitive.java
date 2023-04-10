package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DuplicateObjectKeyException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ObjectItem;

public class InsertIntoObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item content;


    public InsertIntoObjectPrimitive(Item targetObject, Item contentObject) {
        if (!targetObject.isObject() || !contentObject.isObject()) {
            // TODO: ERROR
        }
        this.target = targetObject;
        this.content = contentObject;
    }

    @Override
    public void apply() {
        for (String key : this.content.getKeys()) {
            this.target.putItemByKey(key, this.target.getItemByKey(key));
        }
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public Item getTarget() {
        return target;
    }

    @Override
    public Item getSelector() {
        throw new OurBadException("INVALID SELECTOR GET IN INSERTINTOOBJECT PRIMITIVE");
    }

    @Override
    public Item getContent() {
        return content;
    }

    @Override
    public boolean isInsertObject() {
        return true;
    }

    public static Item mergeSources(Item first, Item second) {
        ObjectItem merged = new ObjectItem();
        ObjectItem objFirst = (ObjectItem) first;
        ObjectItem objSecond = (ObjectItem) second;
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
        return merged;
    }
}
