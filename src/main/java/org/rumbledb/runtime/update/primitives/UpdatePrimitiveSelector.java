package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.StringItem;


public class UpdatePrimitiveSelector {

    private Item selector;

    public UpdatePrimitiveSelector(Item selector) {
        if (!selector.isString() && !selector.isNumeric()) {
            throw new OurBadException("Locators of primitives must be strings or numeric");
        }
        this.selector = selector;
    }

    public Item getSelector() {
        return selector;
    }

    public StringItem getSelectorAsString() {
        if (!this.isString()) {
            throw new OurBadException("Invalid call to getSelectorAsString");
        }
        return (StringItem) this.selector;
    }

    public IntItem getSelectorAsInt() {
        if (!this.isNumeric()) {
            throw new OurBadException("Invalid call to getSelectorAsInt");
        }
        return (IntItem) this.selector;
    }

    public boolean isString() {
        return this.selector.isString();
    }

    public boolean isNumeric() {
        return this.selector.isNumeric();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UpdatePrimitiveSelector that = (UpdatePrimitiveSelector) o;
        return selector.equals(that.selector);
    }

    @Override
    public int hashCode() {
        return selector.hashCode();
    }
}
