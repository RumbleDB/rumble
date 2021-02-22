package org.rumbledb.types;

import jsound.types.FieldDescriptor;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.*;

public class ObjectItemType implements ItemType {

    final static ObjectItemType anyObjectItem = new ObjectItemType(
            new Name(Name.JS_NS, "js", "object"),
            false,
            Collections.emptyMap(),
            null,
            Collections.emptyList(),
            null
    );

    final static Set<FacetTypes> allowedFacets = new HashSet<>(Arrays.asList(
            FacetTypes.ENUMERATION,
            FacetTypes.CONSTRAINTS,
            FacetTypes.CONTENT,
            FacetTypes.CLOSED
    ));

    final private Name name;
    final private Map<String, FieldDescriptor> content;
    final private boolean isClosed;
    final private List<String> constraints;
    final private List<Item> enumeration;
    final private ItemType baseType;

    ObjectItemType(Name name, boolean isClosed, Map<String, FieldDescriptor> content, ItemType baseType, List<String> constraints,List<Item> enumeration){
        this.name = name;
        this.isClosed = isClosed;
        this.content = content;
        this.baseType = baseType;
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
    public boolean isObjectItemType() {
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
        return this == anyObjectItem ? superType == anyObjectItem || superType == BuiltinTypesCatalogue.JSONItem || superType == BuiltinTypesCatalogue.item :
                this.equals(superType) || this.baseType.isSubtypeOf(superType);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if(this.isSubtypeOf(other)){
            return other;
        } else if(other.isSubtypeOf(this)){
            return this;
        } else if(this == anyObjectItem) {
            // if we reach here other not object item for sure
            return other.isArrayItemType() ? BuiltinTypesCatalogue.JSONItem : BuiltinTypesCatalogue.item;
        } else {
            return this.getBaseType().findLeastCommonSuperTypeWith(other.isPrimitive() ? other : other.getBaseType());
        }
    }

    @Override
    public boolean isUserDefined() {
        return !(this == anyObjectItem);
    }

    @Override
    public boolean isPrimitive() {
        return this == anyObjectItem;
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
        if(this.enumeration != null){
            return this.enumeration;
        } else if(this.isPrimitive()){
            return null;
        } else {
            return baseType.getEnumerationFacet();
        }
    }

    @Override
    public List<String> getConstraintsFacet() {
        return this.isPrimitive() ? this.constraints : ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Map<String, FieldDescriptor> getContentFacet() {
        if(this.isPrimitive()){
            return this.content;
        } else {
            // recursively get content facet, overriding new descriptors
            Map<String, FieldDescriptor> map = new HashMap<>(this.baseType.getContentFacet());
            map.putAll(this.content);
            return map;
        }
    }

    @Override
    public boolean getClosedFacet() {
        return this.isClosed;
    }

    @Override
    public String toString() {
        // consider add content and various stuff
        return this.name.toString();
    }
}
