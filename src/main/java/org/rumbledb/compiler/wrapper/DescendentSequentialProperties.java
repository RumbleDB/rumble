package org.rumbledb.compiler.wrapper;

public class DescendentSequentialProperties {
    private final boolean hasInterruptStatement;
    private final boolean hasNonExitSequentialStatement;
    private boolean hasExitStatement;

    public DescendentSequentialProperties(boolean hasNonExitSequentialStatement, boolean hasInterruptStatement) {
        this.hasInterruptStatement = hasInterruptStatement;
        this.hasNonExitSequentialStatement = hasNonExitSequentialStatement;
        this.hasExitStatement = false;
    }

    public DescendentSequentialProperties(
            boolean hasNonExitSequentialStatement,
            boolean hasInterruptStatement,
            boolean hasExitStatement
    ) {
        this.hasInterruptStatement = hasInterruptStatement;
        this.hasNonExitSequentialStatement = hasNonExitSequentialStatement;
        this.hasExitStatement = hasExitStatement;
    }

    public boolean isSequential() {
        return this.hasNonExitSequentialStatement || this.hasExitStatement;
    }

    public boolean hasInterruptStatement() {
        return this.hasInterruptStatement;
    }

    public boolean hasExitStatement() {
        return this.hasExitStatement;
    }

    public void setHasExitStatement(boolean hasExitStatement) {
        this.hasExitStatement = true;
    }

    public boolean hasNonExitSequentialStatement() {
        return this.hasNonExitSequentialStatement;
    }
}
