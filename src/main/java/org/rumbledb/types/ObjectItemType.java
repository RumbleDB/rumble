package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.*;

public class ObjectItemType implements ItemType {

    final static ObjectItemType anyObjectItem = new ObjectItemType(
            new Name(Name.JS_NS, "js", "object"),
            BuiltinTypesCatalogue.JSONItem,
            false,
            Collections.emptyMap(),
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
    final private int typeTreeDepth;

    ObjectItemType(Name name, ItemType baseType, boolean isClosed, Map<String, FieldDescriptor> content, List<String> constraints,List<Item> enumeration){
        this.name = name;
        this.isClosed = isClosed;
        this.content = content;
        this.baseType = baseType;
        this.typeTreeDepth = baseType.getTypeTreeDepth() + 1;
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
    public int getTypeTreeDepth() {
        return this.typeTreeDepth;
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
        return this.enumeration != null || this.isPrimitive() ? this.enumeration : this.baseType.getEnumerationFacet();
    }

    @Override
    public List<String> getConstraintsFacet() {
        return this.isPrimitive() ? this.constraints : ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Map<String, FieldDescriptor> getObjectContentFacet() {
        if(this.isPrimitive()){
            return this.content;
        } else {
            // recursively get content facet, overriding new descriptors
            Map<String, FieldDescriptor> map = new HashMap<>(this.baseType.getObjectContentFacet());
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
