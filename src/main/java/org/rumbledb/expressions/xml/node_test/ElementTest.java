package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

public class ElementTest implements NodeTest {
    private Name elementName;
    private boolean hasWildcard;
    private Name typeName;
    // TODO: add support for optional type


    public ElementTest(Name elementName, Name typeName) {
        this.elementName = elementName;
        this.typeName = typeName;
        this.hasWildcard = false;
    }

    public ElementTest(Name typeName) {
        this.elementName = null;
        this.typeName = typeName;
        this.hasWildcard = true;
    }
}
