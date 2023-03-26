package org.rumbledb.runtime.update.primitives;

import java.io.Serializable;

public interface UpdatePrimitiveInterface extends Serializable {

    void apply();
}
