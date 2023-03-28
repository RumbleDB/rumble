package org.rumbledb.runtime.update.primitives;

import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive extends UpdatePrimitive {

    public DeleteFromObjectPrimitive(ObjectItem targetObject, List<StringItem> namesToRemove) {
        super(targetObject, namesToRemove);
    }

    public ObjectItem getTargetObject() {
        return target.getTargetAsObject();
    }

    public List<StringItem> getNamesToRemove() {
        return source.getSourceAsListOfStrings();
    }

    @Override
    public void apply() {
        for (
            String str : this.getNamesToRemove().stream().map(StringItem::getStringValue).collect(Collectors.toList())
        ) {
            this.getTargetObject().removeItemByKey(str);
        }
    }

    public static UpdatePrimitiveSource mergeSources(UpdatePrimitiveSource first, UpdatePrimitiveSource second) {
        List<StringItem> merged = first.getSourceAsListOfStrings();
        merged.addAll(second.getSourceAsListOfStrings());
        merged = merged.stream().distinct().collect(Collectors.toList());
        return new UpdatePrimitiveSource(merged);
    }

}
