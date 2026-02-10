package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Set;

/**
 * Class representing a concrete XML node item type at depth 2 in the type hierarchy.
 * This is used for all 7 concrete node types defined by the XPath Data Model 3.1, Section 2.7.4:
 * element(), attribute(), document-node(), comment(), text(), namespace-node(), processing-instruction().
 *
 * All concrete node types share node() as their base type at depth 1.
 */
public class XmlNodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private final Name name;

    XmlNodeItemType(Name name) {
        this.name = name;
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
    public int getTypeTreeDepth() {
        return 2;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.nodeItem;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("XML node item type does not support facets");
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

