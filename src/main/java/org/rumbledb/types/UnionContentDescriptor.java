package org.rumbledb.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnionContentDescriptor {
    private final List<TypeOrReference> types;

    public UnionContentDescriptor() {
        this.types = new ArrayList<>();
    }

    public List<TypeOrReference> getTypes() {
        return this.types;
    }

    public void resolveTypeDescriptors(Map<String, ItemType> populatedSchema) {
        for (TypeOrReference type : this.types) {
            type.resolve(populatedSchema);
        }
    }
}
