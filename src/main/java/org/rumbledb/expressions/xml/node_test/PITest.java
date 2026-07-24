package org.rumbledb.expressions.xml.node_test;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Objects;

/**
 * XQuery 3.1 Section 2.5.5 - SequenceType Matching
 * PITest ::= "processing-instruction" "(" (NCName | StringLiteral)? ")"
 * A PITest with no argument matches any processing-instruction node.
 * A PITest with an NCName or StringLiteral argument matches any processing-instruction node
 * whose PITarget equals fn:normalize-space of the given name.
 */
public class PITest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;
    private String targetName;

    /**
     * Creates a PITest that matches any processing-instruction node.
     */
    public PITest() {
        this.targetName = null;
    }

    /**
     * Creates a PITest that matches processing-instruction nodes with the given target name.
     *
     * @param targetName the target name to match against (from NCName or StringLiteral)
     */
    public PITest(String targetName) {
        this.targetName = StringUtils.normalizeSpace(Objects.requireNonNull(targetName));
    }

    public boolean hasTargetName() {
        return this.targetName != null;
    }

    public String getTargetName() {
        return this.targetName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("processing-instruction(");
        if (this.targetName != null) {
            sb.append(this.targetName);
        }
        sb.append(")");
        return sb.toString();
    }


}

