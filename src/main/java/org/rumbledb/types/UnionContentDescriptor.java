package org.rumbledb.types;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class UnionContentDescriptor {
    private final List<ItemType> types;

    public UnionContentDescriptor() {
        this.types = new ArrayList<>();
    }
}
