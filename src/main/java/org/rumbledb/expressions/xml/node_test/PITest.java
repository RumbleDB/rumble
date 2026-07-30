package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * XQuery 3.1 Section 2.5.5 - SequenceType Matching
 * PITest ::= "processing-instruction" "(" (NCName | StringLiteral)? ")"
 * A PITest with no argument matches any processing-instruction node.
 * A PITest with an NCName or StringLiteral argument matches any processing-instruction node
 * whose target name (node name) equals the given name.
 */
public class PITest implements NodeTest {
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
        this.targetName = targetName;
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

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.targetName);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.targetName = input.readString();
    }
}

