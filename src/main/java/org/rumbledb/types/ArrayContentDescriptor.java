package org.rumbledb.types;

public class ArrayContentDescriptor {
    private final ItemType type;

    public ArrayContentDescriptor(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return this.type;
    }
}
