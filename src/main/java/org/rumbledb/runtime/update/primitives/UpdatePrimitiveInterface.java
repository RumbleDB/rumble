package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;

import java.io.Serializable;

public interface UpdatePrimitiveInterface extends Serializable {

    void apply();

    Item getTarget();
}
