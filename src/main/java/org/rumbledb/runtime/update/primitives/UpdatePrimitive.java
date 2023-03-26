package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

public abstract class UpdatePrimitive implements UpdatePrimitiveInterface {

    @Override
    public abstract void apply();

    @Override
    public abstract Item getTarget();
}
