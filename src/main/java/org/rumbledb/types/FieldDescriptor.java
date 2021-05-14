package org.rumbledb.types;

import java.io.Serializable;

import org.rumbledb.api.Item;

public class FieldDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Field " + this.name + " of type " + this.type);
        if (this.isRequired()) {
            sb.append(", required");
        } else {
            sb.append(", not required");
        }
        if (this.isUnique()) {
            sb.append(", unique");
        } else {
            sb.append(", not unique");
        }
        if (this.defaultValue != null) {
            sb.append(", default value: " + this.defaultValue);
        }
        return sb.toString();
    }
}
