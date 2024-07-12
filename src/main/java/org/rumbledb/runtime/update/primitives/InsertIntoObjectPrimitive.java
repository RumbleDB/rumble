package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;

import java.util.ArrayList;
import java.util.List;

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
        try {
            for (String key : this.content.getKeys()) {
                this.target.putItemByKey(key, this.content.getItemByKey(key));
            }
        } catch (DuplicateObjectKeyException e) {
            throw new DuplicateKeyOnUpdateApplyException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public Item getSelector() {
        throw new OurBadException("INVALID SELECTOR GET IN INSERTINTOOBJECT PRIMITIVE");
    }

    @Override
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isInsertObject() {
        return true;
    }

    public static Item mergeSources(Item first, Item second, ExceptionMetadata metadata) {
        Item res;

        List<String> keys = new ArrayList<>(first.getKeys());
        keys.addAll(second.getKeys());

        List<Item> values = new ArrayList<>(first.getValues());
        values.addAll(second.getValues());

        try {
            res = ItemFactory.getInstance().createObjectItem(keys, values, metadata);
        } catch (DuplicateObjectKeyException e) {
            throw new DuplicateObjectInsertSourceException(e.getMessage(), metadata);
        }

        return res;
    }
}
