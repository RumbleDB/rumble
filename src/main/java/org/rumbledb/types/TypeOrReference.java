package org.rumbledb.types;

import org.rumbledb.exceptions.RumbleException;

import java.util.Map;

public class TypeOrReference {

    private ItemType type;
    private String stringType;

    public TypeOrReference(ItemType type) {
        this.type = type;
    }

    public TypeOrReference(String stringType) {
        this.stringType = stringType;
    }

    public void resolve(Map<String, ItemType> populatedSchema) {
        if (this.type != null) {
            return;
        }
        this.type = populatedSchema.get(this.stringType);
        if (this.type == null)
            throw new RumbleException("Type " + this.stringType + " could not be resolved.");
    }

    public ItemType getTypeDescriptor() {
        if (this.type == null) {
            throw new RumbleException("Type " + this.stringType + " was not resolved.");
        }
        return this.type;
    }

    public String getStringType() {
        return this.stringType;
    }

    public ItemType getType() {
        return this.type;
    }
}
