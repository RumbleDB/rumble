package org.rumbledb.expressions.xml.node_test;

import java.io.Serial;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.rumbledb.context.Name;

@NoArgsConstructor(force = true)
public class AttributeTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Expanded name from the kind test (namespace URI + local name).
     * Only valid when isNameWithoutTypeCheck is true.
     */
    @Getter
    private final Name attributeName;

    private final boolean hasWildcard;
    private final Name typeName;

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

    public boolean isWildcardOnly() {
        return this.attributeName == null && this.typeName == null && this.hasWildcard;
    }
}
