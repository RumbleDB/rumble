package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive extends UpdatePrimitive {

    private ObjectItem targetObject;

    private List<StringItem> namesToRemove;

    public DeleteFromObjectPrimitive(ObjectItem targetObject, List<StringItem> namesToRemove) {
        this.targetObject = targetObject;
        this.namesToRemove = namesToRemove;
    }

    public ObjectItem getTargetObject() {
        return targetObject;
    }

    public List<StringItem> getNamesToRemove() {
        return namesToRemove;
    }

    @Override
    public void apply() {
        for (String str : this.namesToRemove.stream().map(StringItem::getStringValue).collect(Collectors.toList())) {
            this.targetObject.removeItemByKey(str);
        }
    }

    @Override
    public Item getTarget() {
        return targetObject;
    }
}
