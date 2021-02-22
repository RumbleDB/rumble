package org.rumbledb.types;

import jsound.types.ArrayContentDescriptor;
import jsound.types.FieldDescriptor;
import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.*;

public class ArrayItemType implements ItemType {

    final static ArrayItemType anyArrayItem = new ArrayItemType(
            new Name(Name.JS_NS, "js", "array"),
            null,
            null,
            null,
            null,
            Collections.emptyList(),
            null
    );

    final static Set<FacetTypes> allowedFacets = new HashSet<>(Arrays.asList(
            FacetTypes.ENUMERATION,
            FacetTypes.CONSTRAINTS,
            FacetTypes.CONTENT,
            FacetTypes.MINLENGTH,
            FacetTypes.MAXLENGTH
    ));

    final private Name name;
    final private ArrayContentDescriptor content;
    final private List<String> constraints;
    final private List<Item> enumeration;
    final private ItemType baseType;
    final private Integer minLength, maxLength;

    ArrayItemType(Name name, ItemType baseType, ArrayContentDescriptor content, Integer minLength, Integer maxLength, List<String> constraints, List<Item> enumeration){
        this.name = name;
        this.baseType = baseType;
        this.content = content;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.constraints = constraints;
        this.enumeration = enumeration;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    @Override
    public boolean isArrayItemType() {
        return true;
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public Name getName() {
        // TODO : what about anonymous types
        return this.name;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return this == anyArrayItem ? superType == anyArrayItem || superType == BuiltinTypesCatalogue.JSONItem || superType == BuiltinTypesCatalogue.item :
                this.equals(superType) || this.baseType.isSubtypeOf(superType);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if(this.isSubtypeOf(other)){
            return other;
        } else if(other.isSubtypeOf(this)){
            return this;
        } else if(this == anyArrayItem) {
            // if we reach here other not object item for sure
            return other.isObjectItemType() ? BuiltinTypesCatalogue.JSONItem : BuiltinTypesCatalogue.item;
        } else {
            return this.getBaseType().findLeastCommonSuperTypeWith(other.isPrimitive() ? other : other.getBaseType());
        }
    }

    @Override
    public boolean isUserDefined() {
        return !(this == anyArrayItem);
    }

    @Override
    public boolean isPrimitive() {
        return this == anyArrayItem;
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
        return this.enumeration != null || this.isPrimitive() ? this.enumeration : this.baseType.getEnumerationFacet();
    }

    @Override
    public List<String> getConstraintsFacet() {
        return this.isPrimitive() ? this.constraints : ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Integer getMinLengthFacet() {
        return this.minLength != null || this.isPrimitive() ? this.minLength : this.baseType.getMinLengthFacet();
    }

    @Override
    public Integer getMaxLengthFacet() {
        return this.maxLength != null || this.isPrimitive() ? this.maxLength : this.baseType.getMaxLengthFacet();
    }

    @Override
    public ArrayContentDescriptor getArrayContentFacet() {
        return this.content != null || this.isPrimitive() ? this.content : this.baseType.getArrayContentFacet();
    }

    @Override
    public String toString() {
        // consider add content and various stuff
        return this.name.toString();
    }

}
