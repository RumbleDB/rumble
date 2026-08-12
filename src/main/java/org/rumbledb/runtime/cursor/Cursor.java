/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Mutable state for one local evaluation of a {@link RuntimePlan}.
 *
 * <p>
 * A cursor has a single owner, is single-use, and must not be shared between evaluations. Its lifecycle is
 * {@code created -> open -> closed}; closing an already closed cursor should be harmless. A fresh cursor replaces the
 * reset and cloning operations used by the legacy iterator architecture. Implementations should normally extend
 * {@link AbstractLocalCursor} instead of implementing lifecycle handling themselves.
 * </p>
 *
 * @param <T> the value type returned by this cursor
 */
public interface Cursor<T> extends AutoCloseable {

    /**
     * @return whether another value is available
     *         Opens the cursor if needed, then returns whether another value is available.
     */
    boolean hasNext();

    /**
     * @return the next value
     *         Opens the cursor if needed, then returns the next value.
     */
    T next();

    /**
     * Releases resources owned by this evaluation. This method is idempotent.
     */
    @Override
    void close();
}
