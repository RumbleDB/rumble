package org.rumbledb.expressions.scripting.statement;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;

public abstract class Statement extends Node {
    protected StaticContext staticContext;

    protected Statement(ExceptionMetadata metadata) {
        super(metadata);
    }


    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }
}
