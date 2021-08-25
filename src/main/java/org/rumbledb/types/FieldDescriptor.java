package org.rumbledb.types;

import java.io.Serializable;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.runtime.typing.CastIterator;

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

    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.type.isResolved()) {
            this.type.resolve(context, metadata);
        }
        if (this.defaultValue != null) {
            if (!this.type.isAtomicItemType()) {
                throw new InvalidSchemaException(
                        "Default values can only be literals for atomic types",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            Item castValue = CastIterator.castItemToType(this.defaultValue, this.type, null);
            if (castValue == null) {
                throw new InvalidSchemaException(
                        "The literal " + this.defaultValue + " is not a valid literal for type " + this.type.toString(),
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            this.defaultValue = castValue;
        }
    }

    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.type.isResolved()) {
            this.type.resolve(context, metadata);
        }
        if (this.defaultValue != null) {
            if (!this.type.isAtomicItemType()) {
                throw new InvalidSchemaException(
                        "Default values can only be literals for atomic types",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            Item castValue = CastIterator.castItemToType(this.defaultValue, this.type, null);
            if (castValue == null) {
                throw new InvalidSchemaException(
                        "The literal " + this.defaultValue + " is not a valid literal for type " + this.type.toString(),
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            this.defaultValue = castValue;
        }
    }
}
