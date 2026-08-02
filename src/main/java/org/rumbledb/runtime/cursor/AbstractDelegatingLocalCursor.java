/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import lombok.NonNull;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Lifecycle template for cursors that pass every operation to one lazily created cursor.
 *
 * @param <T> the delegated value type
 */
public abstract class AbstractDelegatingLocalCursor<T> extends AbstractLocalCursor<T> {

    private Cursor<T> delegate;

    protected AbstractDelegatingLocalCursor(@NonNull ExceptionMetadata metadata) {
        super(metadata);
    }

    /**
     * Creates the delegate during this cursor's lazy open.
     *
     * @return a fresh cursor owned by this cursor
     */
    protected abstract Cursor<T> createDelegateCursor();

    @Override
    protected final void openLocal() {
        this.delegate = createDelegateCursor();
        if (this.delegate == null) {
            throw invalidState("Delegating cursor created a null delegate.");
        }
    }

    @Override
    protected final boolean hasNextLocal() {
        return this.delegate.hasNext();
    }

    @Override
    protected final T nextLocal() {
        return this.delegate.next();
    }

    @Override
    protected final void closeLocal() {
        if (this.delegate != null) {
            this.delegate.close();
            this.delegate = null;
        }
    }
}
