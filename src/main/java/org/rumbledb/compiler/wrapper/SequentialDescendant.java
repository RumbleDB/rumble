package org.rumbledb.compiler.wrapper;

import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.Prolog;

public class SequentialDescendant {
    private final boolean isSequential;
    private final Node parent;
    private final Prolog prolog;

    public SequentialDescendant(boolean isSequential, Node parent, Prolog prolog) {
        this.isSequential = isSequential;
        this.parent = parent;
        this.prolog = prolog;
    }

    public boolean isSequential() {
        return this.isSequential;
    }

    public Node getParent() {
        return this.parent;
    }

    public Prolog getProlog() {
        return this.prolog;
    }
}
