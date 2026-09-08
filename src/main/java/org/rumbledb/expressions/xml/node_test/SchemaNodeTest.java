package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.types.ItemType;

/** Uses the same resolved schema declaration for axis steps and sequence-type matching. */
public record SchemaNodeTest(ItemType itemType) implements NodeTest {
    @Override
    public String toString() {
        return this.itemType.toString();
    }
}
