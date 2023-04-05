package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive extends UpdatePrimitive implements UpdatePrimitiveInterface {

    public DeleteFromObjectPrimitive(Item targetObject, List<Item> namesToRemove) {
        super(targetObject, namesToRemove);
    }

    public ObjectItem getTargetObject() {
        return target.getTargetAsObject();
    }

    public List<Item> getNamesToRemove() {
        return source.getSourceAsListOfStrings();
    }

    @Override
    public void apply() {
        for (
            String str : this.getNamesToRemove().stream().map(Item::getStringValue).collect(Collectors.toList())
        ) {
            this.getTargetObject().removeItemByKey(str);
        }
    }

    public static UpdatePrimitiveSource mergeSources(UpdatePrimitiveSource first, UpdatePrimitiveSource second) {
        List<Item> merged = first.getSourceAsListOfStrings();
        merged.addAll(second.getSourceAsListOfStrings());
        merged = merged.stream().distinct().collect(Collectors.toList());
        return new UpdatePrimitiveSource(merged);
    }

    @Override
    public boolean isDeleteObject() {
        return true;
    }
}
