package org.rumbledb.compiler.wrapper;

import org.rumbledb.expressions.Node;

public class SequentialDescendant {
    private final boolean isSequential;
    private final Node parent;

    public SequentialDescendant(boolean isSequential, Node parent) {
        this.isSequential = isSequential;
        this.parent = parent;
    }

    public boolean isSequential() {
        return isSequential;
    }

    public Node getParent() {
        return parent;
    }
}
