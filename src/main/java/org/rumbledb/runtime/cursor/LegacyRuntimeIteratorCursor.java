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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.RuntimeIterator;

/**
 * Compatibility cursor for a legacy {@link RuntimeIterator}.
 *
 * <p>
 * Each opening deep-copies the pristine legacy iterator prototype and delegates the old iterator lifecycle to that
 * private copy. Migrated plans should return purpose-built cursors and must not use this adapter.
 * </p>
 */
public final class LegacyRuntimeIteratorCursor implements LocalCursor<Item> {

    private final RuntimeIterator prototype;
    private final DynamicContext context;
    private RuntimeIterator execution;

    public LegacyRuntimeIteratorCursor(RuntimeIterator prototype, DynamicContext context) {
        this.prototype = Objects.requireNonNull(prototype, "prototype cannot be null");
        this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
    }

    @Override
    public void open() {
        if (this.execution != null) {
            throw new IteratorFlowException("Local cursor cannot be opened twice.", this.prototype.getMetadata());
        }
        this.execution = this.prototype.deepCopy();
        try {
            this.execution.open(this.context);
        } catch (RuntimeException exception) {
            this.execution = null;
            throw exception;
        }
    }

    @Override
    public boolean hasNext() {
        return getOpenExecution().hasNext();
    }

    @Override
    public Item next() {
        return getOpenExecution().next();
    }

    @Override
    public void close() {
        RuntimeIterator iterator = this.execution;
        this.execution = null;
        if (iterator != null && iterator.isOpen()) {
            iterator.close();
        }
    }

    private RuntimeIterator getOpenExecution() {
        if (this.execution == null) {
            throw new IteratorFlowException("Local cursor is not open.", this.prototype.getMetadata());
        }
        return this.execution;
    }
}
