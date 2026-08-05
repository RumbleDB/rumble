package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing a concrete XML node item type at depth 2 in the type hierarchy. This class now
 * covers the remaining concrete XML node kinds after dedicated implementations for element(),
 * attribute(), document-node(), and processing-instruction(): comment(), text(), namespace-node().
 *
 * <p>All concrete node types share node() as their base type at depth 1.
 */
public class XmlNodeItemType extends AbstractItemType {

    @Serial private static final long serialVersionUID = 1L;

    private final Name name;

    XmlNodeItemType(Name name) {
        this.name = name;
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
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
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
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
