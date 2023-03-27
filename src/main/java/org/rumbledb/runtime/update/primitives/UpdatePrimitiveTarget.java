package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

public class UpdatePrimitiveTarget {

    private Item mainTarget;

    public UpdatePrimitiveTarget(Item mainTarget, Item locator) {
        this.mainTarget = mainTarget;
    }

    public UpdatePrimitiveTarget(Item mainTarget) {
        this(mainTarget, null);
    }

    public Item getMainTarget() {
        return mainTarget;
    }

}
