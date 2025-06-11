package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.OurBadException;

import java.util.*;

public class ObjectItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    final static Set<FacetTypes> allowedFacets = new HashSet<>(
            Arrays.asList(
                FacetTypes.ENUMERATION,
                FacetTypes.CONSTRAINTS,
                FacetTypes.CONTENT,
                FacetTypes.CLOSED
            )
    );

    final private Name name;
    private Map<String, FieldDescriptor> content;
    private boolean isClosed;
    private List<String> constraints;
    private List<Item> enumeration;
    final private ItemType baseType;
    private int typeTreeDepth;

    ObjectItemType(
            Name name,
            ItemType baseType,
            boolean isClosed,
            Map<String, FieldDescriptor> content,
            List<String> constraints,
            List<Item> enumeration
    ) {
        this.name = name;
        this.baseType = baseType;
        this.isClosed = isClosed;
        this.content = content == null ? Collections.emptyMap() : content;
        this.constraints = constraints == null ? Collections.emptyList() : constraints;
        this.enumeration = enumeration;
        if (this.baseType.isResolved()) {
            processBaseType();
            if (areContentTypesResolved()) {
                checkSubtypeConsistency();
            }
        }
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        // Implement serialization logic here if needed
        throw new UnsupportedOperationException("Serialization not implemented yet.");
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        // Implement deserialization logic here if needed
        throw new UnsupportedOperationException("Deserialization not implemented yet.");
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public boolean isObjectItemType() {
        return true;
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public int getTypeTreeDepth() {
        return this.typeTreeDepth;
    }

    @Override
    public boolean isUserDefined() {
        return !(this.equals(BuiltinTypesCatalogue.objectItem));
    }

    @Override
    public boolean isPrimitive() {
        return this.equals(BuiltinTypesCatalogue.objectItem);
    }

    @Override
    public ItemType getPrimitiveType() {
        return BuiltinTypesCatalogue.objectItem;
    }

    @Override
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return allowedFacets;
    }

    @Override
    public List<Item> getEnumerationFacet() {
        return this.enumeration != null || this.isPrimitive() ? this.enumeration : this.baseType.getEnumerationFacet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getConstraintsFacet() {
        return this.isPrimitive()
            ? this.constraints
            : ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Map<String, FieldDescriptor> getObjectContentFacet() {
        return this.content;
    }

    @Override
    public boolean getClosedFacet() {
        return this.isClosed;
    }

    @Override
    public String getIdentifierString() {
        if (this.hasName()) {
            return this.name.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#anonymous-object-base{");
        sb.append(this.baseType.getIdentifierString());
        sb.append("}");
        sb.append(this.isClosed ? "-c" : "-nc");
        if (this.content != null) {
            sb.append("-content{");
            String comma = "";
            for (FieldDescriptor fd : this.content.values()) {
                sb.append(comma);
                sb.append(fd.getName());
                sb.append(fd.isRequired() ? "(r):" : "(nr):");
                sb.append(fd.getType().getIdentifierString());
                Item dv = fd.getDefaultValue();
                if (dv != null) {
                    sb.append("(def:");
                    sb.append(dv.serialize());
                    sb.append(")");
                } else {
                    sb.append("(nd)");
                }
                comma = ",";
            }
            sb.append("}");
        }
        if (this.enumeration != null) {
            sb.append("-enum{");
            String comma = "";
            for (Item item : this.enumeration) {
                sb.append(comma);
                sb.append(item.serialize());
                comma = ",";
            }
            sb.append("}");
        }
        if (this.constraints.size() > 0) {
            sb.append("-const{");
            String comma = "";
            for (String c : this.constraints) {
                sb.append(comma);
                sb.append("\"");
                sb.append(c);
                sb.append("\"");
                comma = ",";
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        if ((new Name(Name.JS_NS, "js", "object")).equals(this.name)) {
            // generic object
            return this.name.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name == null ? "#anonymous" : this.name.toString());
            sb.append(" (object item)\n");

            sb.append("base type : ");
            sb.append(this.baseType.toString());
            sb.append("\n");

            if (isResolved()) {
                List<FieldDescriptor> fields = new ArrayList<>(this.getObjectContentFacet().values());
                if (fields.size() > 0) {
                    sb.append("content facet:\n");
                    // String comma = "";
                    for (FieldDescriptor field : fields) {
                        sb.append("  ");
                        sb.append(field.getName());
                        if (field.isRequired()) {
                            sb.append(" (required)");
                        }
                        sb.append(" : ");
                        sb.append(field.getType().toString());
                        sb.append("\n");
                    }
                }
            } else {
                sb.append("(content not resolved yet)");
            }
            return sb.toString();
        }
    }

    @Override
    public boolean isResolved() {
        return this.baseType.isResolved() && areContentTypesResolved();
    }

    private boolean areContentTypesResolved() {
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            if (!entry.getValue().getType().isResolved()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        if (!areContentTypesResolved()) {
            for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
                entry.getValue().resolve(context, metadata);
            }
            checkSubtypeConsistency();
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        if (!areContentTypesResolved()) {
            for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
                entry.getValue().resolve(context, metadata);
            }
            checkSubtypeConsistency();
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        if (!this.isClosed) {
            return false;
        }
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            if (!entry.getValue().getType().isCompatibleWithDataFrames(configuration)) {
                return false;
            }
        }
        return true;
    }

    public void processBaseType() {
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (this.baseType.isObjectItemType()) {
            if (this.content == null) {
                this.content = this.baseType.getObjectContentFacet();
            } else {
                for (Map.Entry<String, FieldDescriptor> entry : this.baseType.getObjectContentFacet().entrySet()) {
                    if (!this.content.containsKey(entry.getKey())) {
                        FieldDescriptor descriptor = entry.getValue();
                        if (!descriptor.requiredIsSet()) {
                            descriptor.setRequired(false);
                        }
                        if (!descriptor.uniqueIsSet()) {
                            descriptor.setUnique(false);
                        }
                        this.content.put(entry.getKey(), descriptor);
                    } else {
                        FieldDescriptor descriptor = this.content.get(entry.getKey());
                        if (!descriptor.requiredIsSet()) {
                            descriptor.setRequired(entry.getValue().isRequired());
                        }
                        if (!descriptor.uniqueIsSet()) {
                            descriptor.setUnique(entry.getValue().isUnique());
                        }
                    }
                }
            }
            if (this.enumeration == null) {
                this.enumeration = this.baseType.getEnumerationFacet();
            }
            return;
        }
        if (!this.baseType.equals(BuiltinTypesCatalogue.JSONItem)) {
            throw new InvalidSchemaException(
                    "This type cannot be the base type of an object type: " + this.baseType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (this.content == null) {
            throw new OurBadException("Content cannot be null in primitive object type.");
        }
    }

    public void checkSubtypeConsistency() {
        if (!this.baseType.isObjectItemType()) {
            if (this.getTypeTreeDepth() >= 3) {
                throw new InvalidSchemaException(
                        "Any user-defined object type must have an object type as its base type.",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            return;
        }
        // TODO Check field types
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            if (!this.getBaseType().getObjectContentFacet().containsKey(entry.getKey())) {
                if (this.baseType.getClosedFacet()) {
                    throw new InvalidSchemaException(
                            "If the base type is closed, it is not possible to add new fields.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                } else {
                    continue;
                }
            }
            FieldDescriptor superTypeDescriptor = this.getBaseType().getObjectContentFacet().get(entry.getKey());
            if (!entry.getValue().getType().isSubtypeOf(superTypeDescriptor.getType())) {
                throw new InvalidSchemaException(
                        "The type of an object field descriptor (here: "
                            + entry.getValue().getType()
                            + ") associated with key "
                            + entry.getKey()
                            + " must be a subtype of the type declared for this field in its base type (here: "
                            + superTypeDescriptor.getType()
                            + ")",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            if (!entry.getValue().isRequired() && superTypeDescriptor.isRequired()) {
                throw new InvalidSchemaException(
                        "Since the field "
                            + entry.getKey()
                            + " is required in the base type, it must also be required in the derived type.",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
        }
        if (this.baseType.getClosedFacet() && !this.isClosed) {
            throw new InvalidSchemaException(
                    "If the base type is closed, it is not possible to re-open it.",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }


    @Override
    public String getSparkSQLType() {
        StringBuilder sb = new StringBuilder();
        Map<String, FieldDescriptor> content = this.getObjectContentFacet();
        String[] keys = content.keySet().toArray(new String[0]);

        sb.append("STRUCT<");
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            FieldDescriptor field = content.get(key);

            sb.append(key);
            sb.append(":");
            sb.append(field.getType().getSparkSQLType());
            if (i < keys.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(">");

        return sb.toString();
    }
}
