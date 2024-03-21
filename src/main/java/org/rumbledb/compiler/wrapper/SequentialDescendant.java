package org.rumbledb.compiler.wrapper;

import org.rumbledb.expressions.module.Prolog;

public class SequentialDescendant {
    private final boolean isSequential;
    private final boolean hasInterruptStatement;
    private final Prolog prolog;

    public SequentialDescendant(boolean isSequential, boolean hasInterruptStatement, Prolog prolog) {
        this.isSequential = isSequential;
        this.hasInterruptStatement = hasInterruptStatement;
        this.prolog = prolog;
    }

    public SequentialDescendant(SequentialDescendant argument, Prolog prolog) {
        this.isSequential = argument.isSequential();
        this.hasInterruptStatement = argument.hasInterruptStatement();
        this.prolog = prolog;
    }

    public SequentialDescendant(SequentialDescendant argument) {
        this.isSequential = argument.isSequential();
        this.hasInterruptStatement = argument.hasInterruptStatement();
        this.prolog = argument.getProlog();
    }

    public boolean isSequential() {
        return this.isSequential;
    }

    public boolean hasInterruptStatement() {
        return this.hasInterruptStatement;
    }

    public Prolog getProlog() {
        return this.prolog;
    }
}
