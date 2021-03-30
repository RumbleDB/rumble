package org.rumbledb.types;

import org.rumbledb.api.Item;

public class FieldDescriptor {
    public String name;
    private ItemType type;
    private boolean required = false;
    private Item defaultValue = null;
    private boolean unique = false;
    private boolean requiredIsSet = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ItemType type) {
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

    public ItemType getType() {
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
}
