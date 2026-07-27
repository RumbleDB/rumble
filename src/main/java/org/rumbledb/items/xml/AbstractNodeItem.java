package org.rumbledb.items.xml;

import org.rumbledb.api.Item;

import java.io.Serial;

/**
 * Centralizes XDM node identity for Java collections.
 */
public abstract class AbstractNodeItem implements Item {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isNode()) {
            return false;
        }

        // Note: we do not check if `this` and `other` has the same Java class
        // Because XMLDocumentPosition should uniquely identify a node across all node kinds
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
