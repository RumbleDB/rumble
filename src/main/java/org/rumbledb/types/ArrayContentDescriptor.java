package org.rumbledb.types;

import java.io.Serializable;

public class ArrayContentDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ItemType type;

    public ArrayContentDescriptor(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }
}
