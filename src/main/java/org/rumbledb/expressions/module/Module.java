package org.rumbledb.expressions.module;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;

public abstract class Module extends Node {
    private static final long serialVersionUID = 1L;

    public Module(ExceptionMetadata metadata) {
        super(metadata);
    }

    public abstract StaticContext getStaticContext();
}
