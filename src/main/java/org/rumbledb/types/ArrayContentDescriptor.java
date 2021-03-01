package org.rumbledb.types;

import java.util.Map;

public class ArrayContentDescriptor {
    private final TypeOrReference type;

    public ArrayContentDescriptor(TypeOrReference type) {
        this.type = type;
    }

    public TypeOrReference getType() {
        return this.type;
    }

    public void resolveTypeDescriptors(Map<String, ItemType> populatedSchema) {
        this.type.resolve(populatedSchema);
    }
}
