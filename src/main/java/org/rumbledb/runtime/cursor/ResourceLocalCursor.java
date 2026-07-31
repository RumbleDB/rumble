/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Cursor backed by an evaluation-owned iterator that also owns a closeable resource.
 */
public final class ResourceLocalCursor<T> extends AbstractLocalCursor<T> {

    public interface ResourceIterator<T> extends Iterator<T>, AutoCloseable {
        @Override
        void close();
    }

    private final Supplier<? extends ResourceIterator<? extends T>> iteratorFactory;
    private ResourceIterator<? extends T> iterator;

    public ResourceLocalCursor(
            @NonNull Supplier<? extends ResourceIterator<? extends T>> iteratorFactory,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.iteratorFactory = iteratorFactory;
    }

    @Override
    protected void openLocal() {
        this.iterator = Objects.requireNonNull(
            this.iteratorFactory.get(),
            "resource iterator factory returned null"
        );
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
        if (this.iterator != null) {
            this.iterator.close();
        }
        this.iterator = null;
    }
}
