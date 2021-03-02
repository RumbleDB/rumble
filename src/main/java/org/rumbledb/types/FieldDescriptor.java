package org.rumbledb.types;

import java.util.Map;
import org.rumbledb.api.Item;

public class FieldDescriptor {
    public String name;
    private TypeOrReference type;
    private boolean required = false;
    private Item defaultValue = null;
    private boolean unique = false;
    private boolean requiredIsSet = false;
    public boolean defaultIsChecked = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeOrReference type) {
        this.type = type;
    }

    public void setRequired(Boolean required) {
        this.requiredIsSet = true;
        this.required = required;
    }

    public void setDefaultValue(Item defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public String getName() {
        return this.name;
    }

    public TypeOrReference getTypeOrReference() {
        return this.type;
    }

    public boolean isRequired() {
        return this.required;
    }

    public Boolean isUnique() {
        return this.unique;
    }

    public Item getDefaultValue() {
        return this.defaultValue;
    }

    public boolean requiredIsSet() {
        return this.requiredIsSet;
    }

    public void resolveTypeDescriptors(Map<String, ItemType> populatedSchema) {
        this.type.resolve(populatedSchema);
    }
}
