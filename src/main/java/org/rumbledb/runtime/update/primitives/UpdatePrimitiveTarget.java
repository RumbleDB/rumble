package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.ObjectItem;

public class UpdatePrimitiveTarget {

    private Item target;

    public UpdatePrimitiveTarget(Item target) {
        if (!target.isArray() && !target.isObject()) {
            throw new OurBadException("Targets of primitives must be arrays or objects");
        }
        this.target = target;
    }

    public Item getTarget() {
        return target;
    }

    public ArrayItem getTargetAsArray() {
        if (!this.isArray()) {
            throw new OurBadException("Invalid call to getTargetAsArray");
        }
        return (ArrayItem) this.target;
    }

    public ObjectItem getTargetAsObject() {
        if (!this.isArray()) {
            throw new OurBadException("Invalid call to getTargetAsObject");
        }
        return (ObjectItem) this.target;
    }

    public boolean isArray() {
        return this.target.isArray();
    }

    public boolean isObject() {
        return this.target.isObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UpdatePrimitiveTarget that = (UpdatePrimitiveTarget) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }
}
