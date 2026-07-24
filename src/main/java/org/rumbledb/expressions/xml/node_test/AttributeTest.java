package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

import java.io.Serial;

public class AttributeTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
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

    public AttributeTest(boolean hasWildcard) {
        this.attributeName = null;
        this.typeName = null;
        this.hasWildcard = hasWildcard;
    }

    public AttributeTest() {
        this.attributeName = null;
        this.typeName = null;
        this.hasWildcard = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("attribute(");
        if (this.hasWildcard) {
            sb.append("*");
        } else if (this.attributeName != null) {
            sb.append(this.attributeName);
        }
        if (this.typeName != null) {
            sb.append(",");
            sb.append(this.typeName);
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmptyCheck() {
        return !this.hasWildcard && this.attributeName == null;
    }

    public boolean isNameWithoutTypeCheck() {
        return this.attributeName != null && this.typeName == null;
    }

    /**
     * Expanded name from the kind test (namespace URI + local name). Only valid when
     * {@link #isNameWithoutTypeCheck()} is true.
     */
    public Name getAttributeName() {
        return this.attributeName;
    }

    public boolean isWildcardOnly() {
        return this.attributeName == null && this.typeName == null && this.hasWildcard;
    }


}
