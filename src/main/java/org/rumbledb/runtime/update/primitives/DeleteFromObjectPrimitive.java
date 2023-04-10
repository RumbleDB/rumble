package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ObjectItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private List<Item> content;

    public DeleteFromObjectPrimitive(Item targetObject, List<Item> namesToRemove) {
        if (!targetObject.isObject() || !namesToRemove.stream().allMatch(Item::isString)) {
            // TODO ERROR
        }

        this.target = targetObject;
        this.content = namesToRemove;
    }

    @Override
    public void apply() {
        for (
            String str : this.content.stream().map(Item::getStringValue).collect(Collectors.toList())
        ) {
            ((ObjectItem) this.target).removeItemByKey(str);
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
        throw new OurBadException("INVALID SELECTOR GET IN DELETEFROMOBJECT PRIMITIVE");
    }

    @Override
    public List<Item> getContentList() {
        return content;
    }

    @Override
    public boolean isDeleteObject() {
        return true;
    }

    public static List<Item> mergeSources(List<Item> first, List<Item> second) {
        List<Item> merged = new ArrayList<>(first);
        merged.addAll(second);
        merged = merged.stream().distinct().collect(Collectors.toList());
        return merged;
    }
}
