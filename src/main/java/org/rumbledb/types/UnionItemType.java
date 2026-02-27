package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnionItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private static Set<ConstrainingFacetTypes> allowedFacets = new HashSet<>(
            Arrays.asList(ConstrainingFacetTypes.CONTENT)
    );

    private final Name name;
    private final ItemType baseType;
    private final int typeTreeDepth;
    private final List<ItemType> types;

    UnionItemType(Name name, ItemType baseType, List<ItemType> types) {
        this.name = name;
        this.baseType = baseType;
        this.typeTreeDepth = baseType.getTypeTreeDepth() + 1;
        this.types = types;
    }

    UnionItemType(Name name, List<ItemType> types) {
        this.name = name;
        this.baseType = BuiltinTypesCatalogue.item;
        this.typeTreeDepth = 1;
        this.types = types;
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
    public boolean isUnionType() {
        return true;
    }

    @Override
    public boolean hasName() {
        return true;
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
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public ItemType getPrimitiveType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        return allowedFacets;
    }

    @Override
    public List<ItemType> getTypes() {
        return this.types;
    }

    @Override
    public String getIdentifierString() {
        if (this.hasName()) {
            return this.name.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#anonymous-union-base{");
        sb.append(this.baseType.getIdentifierString());
        sb.append("}");
        if (this.types != null) {
            sb.append("-content{");
            String comma = "";
            for (ItemType it : this.types) {
                sb.append(comma);
                sb.append(it.getIdentifierString());
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
            sb.append("{ ");
            if (this.name != null && !this.name.getLocalName().equals("")) {
                sb.append("\"name\": \"");
                sb.append(this.name.toString());
                sb.append("\", ");
            }
            sb.append("\"kind\": \"union\", ");

            sb.append("\"baseType\": \"");
            sb.append(this.baseType.toString());
            sb.append("\", ");

            sb.append("\"treeDepth\": ");
            sb.append(this.typeTreeDepth);
            sb.append(", ");

            if (isResolved()) {
                sb.append("\"content\": [ ");
                String comma = "";
                for (ItemType type : this.types) {
                    sb.append(comma);
                    comma = ", ";
                    if (type.toString().startsWith("{")) {
                        sb.append(type);
                    } else {
                        sb.append("\"");
                        sb.append(type.toString());
                        sb.append("\"");
                    }
                }
                sb.append(" ]");
            } else {
                sb.append(" (content not resolved yet) ");
            }
            sb.append(" }");
            return sb.toString();
        }
    }

    @Override
    public boolean isResolved() {
        for (ItemType itemType : this.types) {
            if (!itemType.isResolved())
                return false;
        }
        return true;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        for (ItemType itemType : this.types) {
            if (!itemType.isResolved()) {
                itemType.resolve(context, metadata);
            }
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        for (ItemType itemType : this.types) {
            if (!itemType.isResolved()) {
                itemType.resolve(context, metadata);
            }
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
