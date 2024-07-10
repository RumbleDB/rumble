package org.rumbledb.expressions.scripting.statement;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

public abstract class Statement extends Node {
    protected StaticContext staticContext;
    protected SequenceType staticSequenceType;
    protected boolean isSequential;

    protected Statement(ExceptionMetadata metadata) {
        super(metadata);
    }


    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }

    public void setStaticSequenceType(SequenceType staticSequenceType) {
        this.staticSequenceType = staticSequenceType;
    }

    public SequenceType getStaticSequenceType() {
        return this.staticSequenceType;
    }

    public boolean isSequential() {
        return this.isSequential;
    }

    public void setSequential(boolean isSequential) {
        this.isSequential = isSequential;
    }

    public RuntimeStaticContext getStaticContextForRuntime(
            RumbleRuntimeConfiguration conf,
            VisitorConfig visitorConfig
    ) {
        return new RuntimeStaticContext(
                conf,
                getStaticSequenceType(),
                getHighestExecutionMode(visitorConfig),
                getMetadata()
        );
    }

    @Override
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        if (this.isSequential) {
            buffer.append(" | " + "sequential");
        } else {
            buffer.append(" | " + "non-sequential");
        }
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }
}
