package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ObjectItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private List<Item> content;

    public DeleteFromObjectPrimitive(Item targetObject, List<Item> namesToRemove, ExceptionMetadata metadata) {

        for (Item item : namesToRemove) {
            if (targetObject.getItemByKey(item.getStringValue()) == null) {
                throw new CannotResolveUpdateSelectorException("Cannot delete key that does not exist in target object", metadata);
            }
        }

            this.target = targetObject;
        this.content = namesToRemove;
    }

    @Override
    public void apply() {
        for (
            String str : this.content.stream().map(Item::getStringValue).collect(Collectors.toList())
        ) {
            this.target.removeItemByKey(str);
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
