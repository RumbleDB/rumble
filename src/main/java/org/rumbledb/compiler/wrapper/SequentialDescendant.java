package org.rumbledb.compiler.wrapper;

public class SequentialDescendant {
    private final boolean isSequential;
    private final boolean hasInterruptStatement;

    public SequentialDescendant(boolean isSequential, boolean hasInterruptStatement) {
        this.isSequential = isSequential;
        this.hasInterruptStatement = hasInterruptStatement;
    }

    public boolean isSequential() {
        return this.isSequential;
    }

    public boolean hasInterruptStatement() {
        return this.hasInterruptStatement;
    }
}
