package org.rumbledb.runtime.update.primitives;

public interface UpdatePrimitiveInterface {

    void apply();

    boolean hasSelector();

    UpdatePrimitiveTarget getTarget();

    UpdatePrimitiveSelector getSelector();

    UpdatePrimitiveSource getSource();

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
