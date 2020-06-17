package org.rumbledb.expressions.module;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;

public abstract class Module extends Node {
    public Module(ExceptionMetadata metadata) {
        super(metadata);
    }

    public abstract StaticContext getStaticContext();
}
