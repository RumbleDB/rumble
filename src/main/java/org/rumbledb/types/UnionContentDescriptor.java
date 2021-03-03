package org.rumbledb.types;

import java.util.ArrayList;
import java.util.List;

public class UnionContentDescriptor {
    private final List<ItemType> types;

    public UnionContentDescriptor() {
        this.types = new ArrayList<>();
    }

    public List<ItemType> getTypes() {
        return this.types;
    }
}
