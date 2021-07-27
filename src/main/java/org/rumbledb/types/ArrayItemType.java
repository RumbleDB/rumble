package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.OurBadException;

import java.util.*;

public class ArrayItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    final static ArrayItemType anyArrayItem = new ArrayItemType(
            new Name(Name.JS_NS, "js", "array"),
            BuiltinTypesCatalogue.JSONItem,
            null,
            null,
            null,
            null
    );

    final static Set<FacetTypes> allowedFacets = new HashSet<>(
            Arrays.asList(
                FacetTypes.ENUMERATION,
                FacetTypes.CONTENT,
                FacetTypes.MINLENGTH,
                FacetTypes.MAXLENGTH
            )
    );

    final private Name name;
    private ArrayContentDescriptor content;
    private List<Item> enumeration;
    final private ItemType baseType;
    final private int typeTreeDepth;
    private Integer minLength, maxLength;

    ArrayItemType(
            Name name,
            ItemType baseType,
            ArrayContentDescriptor content,
            Integer minLength,
            Integer maxLength,
            List<Item> enumeration
    ) {
        this.name = name;
        if (baseType == null) {
            throw new OurBadException("Unexpected error: baseType is null.");
        }
        this.baseType = baseType;
        this.typeTreeDepth = baseType.getTypeTreeDepth() + 1;
        this.content = content;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.enumeration = enumeration;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public boolean isArrayItemType() {
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
        return !(this.equals(anyArrayItem));
    }

    @Override
    public boolean isPrimitive() {
        return this.equals(anyArrayItem);
    }

    @Override
    public ItemType getPrimitiveType() {
        return anyArrayItem;
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
        return this.enumeration;
    }

    @Override
    public Integer getMinLengthFacet() {
        return this.minLength;
    }

    @Override
    public Integer getMaxLengthFacet() {
        return this.maxLength;
    }

    @Override
    public ArrayContentDescriptor getArrayContentFacet() {
        return this.content;
    }

    @Override
    public String getIdentifierString() {
        if (this.hasName()) {
            return this.name.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#anonymous-array-base{");
        sb.append(this.baseType.getIdentifierString());
        sb.append("}");
        if (this.content != null) {
            sb.append("-content{");
            sb.append(this.content.getType().getIdentifierString());
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
        return sb.toString();
    }

    @Override
    public String toString() {
        // consider add content and various stuff
        return ((this.name == null) ? "<anonymous>" : this.name.toString())
            + "(array of "
            + this.getArrayContentFacet().getType()
            + ")";
    }

    @Override
    public boolean isDataFrameType() {
        return this.content.getType().isDataFrameType();
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.content.getType().isResolved()) {
            this.content.getType().resolve(context, metadata);
        }
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            if (!this.baseType.isArrayItemType()) {
                if (this.content == null) {
                    this.content = this.baseType.getArrayContentFacet();
                }
            }
            checkSubtypeConsistency();
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.content.getType().isResolved()) {
            this.content.getType().resolve(context, metadata);
        }
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            if (this.baseType.isArrayItemType()) {
                if (this.content == null) {
                    this.content = this.baseType.getArrayContentFacet();
                }
                if (this.minLength == null) {
                    this.minLength = this.baseType.getMinLengthFacet();
                }
                if (this.maxLength == null) {
                    this.maxLength = this.baseType.getMaxLengthFacet();
                }
                
                if (this.enumeration == null) {
                    this.enumeration = this.baseType.getEnumerationFacet();
                }
            }
            checkSubtypeConsistency();
        }
    }

    @Override
    public boolean isResolved() {
        return this.content.getType().isResolved() && this.baseType.isResolved();
    }

    public void checkSubtypeConsistency() {
        if (!this.baseType.isArrayItemType()) {
            if (!this.equals(BuiltinTypesCatalogue.arrayItem)) {
                throw new InvalidSchemaException(
                        "Any user-defined array type must have an array type as its base type.",
                        null
                );
            }
            return;
        }
        if (!this.content.getType().isSubtypeOf(this.baseType.getArrayContentFacet().getType())) {
            throw new InvalidSchemaException(
                    "The content of an array subtype must be a subtype of the content of its base type.",
                    null
            );
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames() {
        return this.content.getType().isCompatibleWithDataFrames();
    }

}
