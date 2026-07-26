/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.RuntimeIterator;

/**
 * Compatibility cursor for a legacy {@link RuntimeIterator}.
 *
 * <p>
 * Opening deep-copies the pristine legacy iterator prototype and delegates the old iterator lifecycle to that
 * private copy. Migrated plans should return purpose-built cursors and must not use this adapter.
 * </p>
 */
public final class LegacyRuntimeIteratorCursor extends AbstractLocalCursor<Item> {

    private final RuntimeIterator prototype;
    private final DynamicContext context;
    private RuntimeIterator execution;

    public LegacyRuntimeIteratorCursor(RuntimeIterator prototype, DynamicContext context) {
        super(Objects.requireNonNull(prototype, "prototype cannot be null").getMetadata());
        this.prototype = prototype;
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    @Override
    protected void openLocal() {
        this.execution = this.prototype.deepCopy();
        this.execution.open(this.context);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.execution.hasNext();
    }

    @Override
    protected Item nextLocal() {
        return this.execution.next();
    }

    @Override
    protected void closeLocal() {
        if (this.execution != null && this.execution.isOpen()) {
            this.execution.close();
        }
        this.execution = null;
    }

}
