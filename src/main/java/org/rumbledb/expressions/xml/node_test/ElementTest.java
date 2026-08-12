package org.rumbledb.expressions.xml.node_test;

import java.io.Serial;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.rumbledb.context.Name;

@NoArgsConstructor(force = true)
public class ElementTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Expanded name from the kind test (namespace URI + local name).
     * Only valid when isNameWithoutTypeCheck is true.
     */
    @Getter
    private final Name elementName;

    private final boolean hasWildcard;
    private final Name typeName;
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

    public boolean isEmptyCheck() {
        return !this.hasWildcard && this.elementName == null;
    }

    public boolean isNameWithoutTypeCheck() {
        return this.elementName != null && this.typeName == null;
    }

    public boolean isWildcardOnly() {
        return this.elementName == null && this.typeName == null && this.hasWildcard;
    }
}
