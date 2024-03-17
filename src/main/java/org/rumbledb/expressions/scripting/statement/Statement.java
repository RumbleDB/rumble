package org.rumbledb.expressions.scripting.statement;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

public abstract class Statement extends Node {
    protected StaticContext staticContext;
    protected SequenceType staticSequenceType;

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
}
