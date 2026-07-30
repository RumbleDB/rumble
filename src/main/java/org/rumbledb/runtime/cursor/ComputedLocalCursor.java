/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.rumbledb.runtime.cursor;

import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Evaluates a zero-or-one result lazily when the cursor is opened.
 */
public final class ComputedLocalCursor<T> extends AtMostOneLocalCursor<T> {

    private final Supplier<? extends T> computation;

    public ComputedLocalCursor(
            @NonNull Supplier<? extends T> computation,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.computation = computation;
    }

    @Override
    protected T materializeOneItemOrNull() {
        return this.computation.get();
    }
}
