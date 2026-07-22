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
 * A cursor has a single owner and must not be shared between evaluations. A fresh cursor replaces the reset and
 * cloning operations used by the legacy iterator architecture.
 * </p>
 *
 * @param <T> the value type returned by this cursor
 */
public interface LocalCursor<T> extends AutoCloseable {

    void open();

    boolean hasNext();

    T next();

    @Override
    void close();
}
