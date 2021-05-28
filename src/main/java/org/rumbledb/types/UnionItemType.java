package org.rumbledb.types;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UnionItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private static Set<FacetTypes> allowedFacets = new HashSet<>(Arrays.asList(FacetTypes.CONTENT));

    private final Name name;
    private final ItemType baseType;
    private final int typeTreeDepth;
    private final UnionContentDescriptor content;

    UnionItemType(Name name, ItemType baseType, UnionContentDescriptor content) {
        this.name = name;
        this.baseType = baseType;
        this.typeTreeDepth = baseType.getTypeTreeDepth() + 1;
        this.content = content;
    }

    UnionItemType(Name name, UnionContentDescriptor content) {
        this.name = name;
        this.baseType = BuiltinTypesCatalogue.item;
        this.typeTreeDepth = 1;
        this.content = content;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.getIdentifierString().equals(((ItemType) other).getIdentifierString());
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
    public Set<FacetTypes> getAllowedFacets() {
        return allowedFacets;
    }

    @Override
    public UnionContentDescriptor getUnionContentFacet() {
        return this.content;
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
        if (this.content != null) {
            sb.append("-content{");
            String comma = "";
            for (ItemType it : this.content.getTypes()) {
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
        // TODO : consider providing more info
        return this.name.toString();
    }

    @Override
    public DataType toDataFrameType() {
        return DataTypes.BinaryType;
    }

    @Override
    public boolean isDataFrameType() {
        return false;
    }
    
    @Override
    public boolean isResolved() {
        for (ItemType itemType : this.content.getTypes()) {
            if(!itemType.isResolved())
                return false;
        }
        return true;
    }
    
    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        for (ItemType itemType : this.content.getTypes()) {
            if(!itemType.isResolved())
            {
                itemType.resolve(context, metadata);
            }
        }
    }
}
