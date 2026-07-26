/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.RuntimeTupleIterator;

import sparksoniq.jsoniq.tuple.FlworTuple;

/**
 * Cursor backed by a fresh, evaluation-owned tuple iterator.
 *
 * <p>
 * This is the tuple counterpart of {@link RecreatedRuntimeIteratorCursor}. It preserves the mature FLWOR algorithms
 * while moving their mutable state out of the reusable tuple plan, without serialization.
 * </p>
 */
public final class RecreatedRuntimeTupleIteratorCursor extends AbstractLocalCursor<FlworTuple> {

    private final Supplier<? extends RuntimeTupleIterator> executionFactory;
    private final DynamicContext context;
    private RuntimeTupleIterator execution;

    public RecreatedRuntimeTupleIteratorCursor(
            @NonNull Supplier<? extends RuntimeTupleIterator> executionFactory,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.executionFactory = executionFactory;
        this.context = context;
    }

    @Override
    protected void openLocal() {
        this.execution = this.executionFactory.get();
        this.execution.open(this.context);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.execution.hasNext();
    }

    @Override
    protected FlworTuple nextLocal() {
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
