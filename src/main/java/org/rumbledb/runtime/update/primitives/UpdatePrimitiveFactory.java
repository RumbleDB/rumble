package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

import java.util.List;

public class UpdatePrimitiveFactory {

    private static UpdatePrimitiveFactory instance;

    public static UpdatePrimitiveFactory getInstance() {
        if (instance == null) {
            instance = new UpdatePrimitiveFactory();
        }
        return instance;
    }

    public UpdatePrimitive createDeleteFromArrayPrimitive(Item targetArray, Item selectorInt) {
        return new DeleteFromArrayPrimitive(targetArray, selectorInt);
    }

    public UpdatePrimitive createDeleteFromObjectPrimitive(Item targetObject, List<Item> selectorStrs) {
        return new DeleteFromObjectPrimitive(targetObject, selectorStrs);
    }

    public UpdatePrimitive createInsertIntoArrayPrimitive(Item targetArray, Item selectorInt, List<Item> contents ) {
        return new InsertIntoArrayPrimitive(targetArray, selectorInt, contents);
    }

    public UpdatePrimitive createInsertIntoObjectPrimitive(Item targetObject, Item contentsObject) {
        return new InsertIntoObjectPrimitive(targetObject, contentsObject);
    }

    public UpdatePrimitive createReplaceInArrayPrimitive(Item targetArray, Item selectorInt, Item content) {
        return new ReplaceInArrayPrimitive(targetArray, selectorInt, content);
    }

    public UpdatePrimitive createReplaceInObjectPrimitive(Item targetObject, Item selectorStr, Item content) {
        return new ReplaceInObjectPrimitive(targetObject, selectorStr, content);
    }

    public UpdatePrimitive createRenameInObjectPrimitive(Item targetObject, Item selectorStr, Item content) {
        return new RenameInObjectPrimitive(targetObject, selectorStr, content);
    }


}
