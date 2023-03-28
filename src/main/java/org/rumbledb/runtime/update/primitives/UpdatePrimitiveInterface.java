package org.rumbledb.runtime.update.primitives;

public interface UpdatePrimitiveInterface {

    void apply();

    boolean hasSelector();

    UpdatePrimitiveTarget getTarget();

    UpdatePrimitiveSelector getSelector();

    UpdatePrimitiveSource getSource();

}
