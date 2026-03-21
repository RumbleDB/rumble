package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Set;

/**
 * Class representing the abstract node() item type.
 * This is the supertype for all 7 concrete XML node types
 * (element, attribute, document-node, comment, text, namespace-node, processing-instruction)
 * as defined by the XPath Data Model 3.1, Section 2.7.4.
 *
 * node() sits at depth 1 in the type hierarchy, with item at depth 0.
 */
public class NodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    static final ItemType nodeItem = new NodeItemType();
    private final Name name;

    NodeItemType() {
        this.name = Name.createVariableInDefaultTypeNamespace("node");
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
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
    public boolean isNodeItemType() {
        return true;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return superType.equals(BuiltinTypesCatalogue.item) || superType.equals(nodeItem);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        while (other.getTypeTreeDepth() > 1) {
            other = other.getBaseType();
        }
        return other.equals(nodeItem) ? nodeItem : BuiltinTypesCatalogue.item;
    }

    @Override
    public int getTypeTreeDepth() {
        return 1;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("node item type does not support facets");
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}


