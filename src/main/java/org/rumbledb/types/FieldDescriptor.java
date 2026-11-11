/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.types;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.runtime.typing.CastIterator;

public class FieldDescriptor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    public String name;

    @Setter
    @Getter
    private ItemType type;

    @Getter
    private boolean required = false;

    @Setter
    @Getter
    private Item defaultValue = null;

    private boolean unique = false;
    private boolean requiredIsSet = false;
    private boolean uniqueIsSet = false;

    public void setRequired(Boolean required) {
        this.requiredIsSet = true;
        this.required = required;
    }

    public void setUnique(Boolean unique) {
        this.uniqueIsSet = true;
        this.unique = unique;
    }

    public Boolean isUnique() {
        return this.unique;
    }

    public boolean requiredIsSet() {
        return this.requiredIsSet;
    }

    public boolean uniqueIsSet() {
        return this.uniqueIsSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
                        "Default values can only be literals for atomic types", ExceptionMetadata.EMPTY_METADATA);
            }
            Item castValue = CastIterator.castItemToType(this.defaultValue, this.type, null);
            if (castValue == null) {
                throw new InvalidSchemaException(
                        "The literal " + this.defaultValue + " is not a valid literal for type " + this.type.toString(),
                        ExceptionMetadata.EMPTY_METADATA);
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
                        "Default values can only be literals for atomic types", ExceptionMetadata.EMPTY_METADATA);
            }
            Item castValue = CastIterator.castItemToType(this.defaultValue, this.type, null, context);
            if (castValue == null) {
                throw new InvalidSchemaException(
                        "The literal " + this.defaultValue + " is not a valid literal for type " + this.type.toString(),
                        ExceptionMetadata.EMPTY_METADATA);
            }
            this.defaultValue = castValue;
        }
    }

    public static FieldDescriptor copy(FieldDescriptor descriptor) {
        FieldDescriptor clone = new FieldDescriptor();
        clone.setName(descriptor.name);
        clone.setType(descriptor.type);
        clone.setRequired(descriptor.required);
        clone.setUnique(descriptor.unique);
        if (descriptor.defaultValue != null) {
            clone.setDefaultValue(descriptor.defaultValue);
        }
        return clone;
    }
}
