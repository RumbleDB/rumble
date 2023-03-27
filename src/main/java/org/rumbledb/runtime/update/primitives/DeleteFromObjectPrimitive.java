package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive extends UpdatePrimitive {

    public DeleteFromObjectPrimitive(ObjectItem targetObject, List<StringItem> namesToRemove) {
        super(targetObject, namesToRemove);
    }

    public ObjectItem getTargetObject() {
        return (ObjectItem) target.getMainTarget();
    }

    public List<StringItem> getNamesToRemove() {
        return (List<StringItem>) source.getSourceItems();
    }

    @Override
    public void apply() {
        for (String str : this.getNamesToRemove().stream().map(StringItem::getStringValue).collect(Collectors.toList())) {
            this.getTargetObject().removeItemByKey(str);
        }
    }

}
