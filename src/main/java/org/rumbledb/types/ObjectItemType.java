package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.OurBadException;

import java.util.*;

public class ObjectItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    final static ObjectItemType anyObjectItem = new ObjectItemType(
            new Name(Name.JS_NS, "js", "object"),
            BuiltinTypesCatalogue.JSONItem,
            false,
            Collections.emptyMap(),
            Collections.emptyList(),
            null
    );

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
        }
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
        return !(this.equals(anyObjectItem));
    }

    @Override
    public boolean isPrimitive() {
        return this.equals(anyObjectItem);
    }

    @Override
    public ItemType getPrimitiveType() {
        return anyObjectItem;
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
        if (this.isPrimitive()) {
            return this.content;
        } else {
            // recursively get content facet, overriding new descriptors
            Map<String, FieldDescriptor> map = new LinkedHashMap<>(this.baseType.getObjectContentFacet());
            map.putAll(this.content);
            return map;
        }
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
            
            if(isResolved())
            {
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
    public boolean isDataFrameType() {
        if (!this.isClosed) {
            return false;
        }
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            if (!entry.getValue().getType().isDataFrameType()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isResolved() {
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
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            entry.getValue().resolve(context, metadata);
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            entry.getValue().resolve(context, metadata);
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames() {
        if (!this.isClosed) {
            return false;
        }
        for (Map.Entry<String, FieldDescriptor> entry : this.content.entrySet()) {
            if (!entry.getValue().getType().isCompatibleWithDataFrames()) {
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
}
