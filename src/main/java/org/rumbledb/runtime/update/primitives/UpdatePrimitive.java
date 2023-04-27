package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

import java.util.List;

public interface UpdatePrimitive {

    void apply();

    boolean hasSelector();

    Item getTarget();

    default Item getSelector() {throw new UnsupportedOperationException("Operation not defined");}

    default Item getContent() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default List<Item> getContentList() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default boolean isDeleteObject() {
        return false;
    }

    default boolean isDeleteArray() {
        return false;
    }

    default boolean isInsertObject() {
        return false;
    }

    default boolean isInsertArray() {
        return false;
    }

    default boolean isReplaceObject() {
        return false;
    }

    default boolean isReplaceArray() {
        return false;
    }

    default boolean isRenameObject() {
        return false;
    }


}
