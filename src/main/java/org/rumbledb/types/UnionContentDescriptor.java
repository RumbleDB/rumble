package org.rumbledb.types;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UnionContentDescriptor {
    private final List<ItemType> types;

    public UnionContentDescriptor() {
        this.types = new ArrayList<>();
    }

}
