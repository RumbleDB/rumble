package org.rumbledb.items.xml;

import org.rumbledb.api.Item;

/**
 * Centralizes XDM node identity for Java collections.
 */
public abstract class AbstractNodeItem implements Item {

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isNode()) {
            return false;
        }
        XMLDocumentPosition position = getXmlDocumentPosition();
        XMLDocumentPosition otherPosition = otherItem.getXmlDocumentPosition();
        return position != null && position.equals(otherPosition);
    }

    @Override
    public final int hashCode() {
        XMLDocumentPosition position = getXmlDocumentPosition();
        return position == null ? System.identityHashCode(this) : position.hashCode();
    }
}
