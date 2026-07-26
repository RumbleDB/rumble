/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.Iterator;
import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Cursor backed by a fresh Java iterator created for each evaluation.
 *
 * @param <T> the produced value type
 */
public final class IteratorLocalCursor<T> extends AbstractLocalCursor<T> {

    private final Supplier<? extends Iterator<? extends T>> iteratorFactory;
    private Iterator<? extends T> iterator;

    public IteratorLocalCursor(
            @NonNull Supplier<? extends Iterator<? extends T>> iteratorFactory,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.iteratorFactory = iteratorFactory;
    }

    @Override
    protected void openLocal() {
        this.iterator = this.iteratorFactory.get();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.iterator.hasNext();
    }

    @Override
    protected T nextLocal() {
        if (!this.iterator.hasNext()) {
            throw invalidState("No more values are available.");
        }
        return this.iterator.next();
    }

    @Override
    protected void closeLocal() {
        this.iterator = null;
    }
}
