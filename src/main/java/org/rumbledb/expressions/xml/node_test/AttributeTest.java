package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

public class AttributeTest implements NodeTest {
    private Name attributeName;
    private boolean hasWildcard;
    private Name typeName;

    public AttributeTest(Name attributeName, Name typeName) {
        this.attributeName = attributeName;
        this.typeName = typeName;
        this.hasWildcard = false;
    }

    public AttributeTest(Name typeName) {
        this.attributeName = null;
        this.typeName = typeName;
        this.hasWildcard = true;
    }
}
