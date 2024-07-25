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

    public ElementTest(boolean hasWildcard) {
        this.elementName = null;
        this.typeName = null;
        this.hasWildcard = true;
    }

    public ElementTest() {
        this.elementName = null;
        this.typeName = null;
        this.hasWildcard = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("element(");
        if (this.hasWildcard) {
            sb.append("*");
        } else if (this.elementName != null) {
            sb.append(this.elementName);
        }
        if (this.typeName != null) {
            sb.append(this.typeName);
        }
        sb.append(")");
        return sb.toString();
    }
}
