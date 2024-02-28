package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;

public class RenameInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public RenameInObjectPrimitive(
            Item targetObject,
            Item targetName,
            Item replacementName,
            ExceptionMetadata metadata
    ) {

        if (targetObject.getItemByKey(targetName.getStringValue()) == null) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot rename key that does not exist in target object",
                    metadata
            );
        }


        this.target = targetObject;
        this.selector = targetName;
        this.content = replacementName;
    }

    @Override
    public void apply() {
        String name = this.selector.getStringValue();
        if (this.target.getKeys().contains(name)) {
            Item item = this.target.getItemByKey(name);
            this.target.removeItemByKey(name);
            this.target.putItemByKey(this.content.getStringValue(), item);
        }
        // TODO: implement replace and rename methods for Array & Object to avoid deletion and append
    }

    @Override
    public boolean hasSelector() {
        return true;
    }

    @Override
    public Item getTarget() {
        return target;
    }

    @Override
    public Item getSelector() {
        return selector;
    }

    @Override
    public Item getContent() {
        return content;
    }

    @Override
    public boolean isRenameObject() {
        return true;
    }
}
