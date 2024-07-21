package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

public class ElementTest implements NodeTest {
    private Name elementName;
    private boolean hasWildcard;
    private Name typeName;
    private boolean hasOptional;


    public ElementTest(Name elementName, Name typeName, boolean hasOptional) {
        this.elementName = elementName;
        this.typeName = typeName;
        this.hasOptional = hasOptional;
        this.hasWildcard = false;
    }

    public ElementTest(Name typeName, boolean hasOptional) {
        this.elementName = null;
        this.typeName = typeName;
        this.hasOptional = hasOptional;
        this.hasWildcard = true;
    }
}
