/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import lombok.NonNull;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Streams each input value through a zero-or-more mapping.
 */
public final class FlatMappingLocalCursor<I, O> extends AbstractLocalCursor<O> {

    private final RuntimePlan<I> inputPlan;
    private final DynamicContext context;
    private final Function<? super I, ? extends Iterator<? extends O>> mapper;
    private Cursor<I> inputCursor;
    private Iterator<? extends O> currentResults;

    public FlatMappingLocalCursor(
            @NonNull RuntimePlan<I> inputPlan,
            @NonNull DynamicContext context,
            @NonNull Function<? super I, ? extends Iterator<? extends O>> mapper,
            @NonNull ExceptionMetadata metadata) {
        super(metadata);
        this.inputPlan = inputPlan;
        this.context = context;
        this.mapper = mapper;
    }

    @Override
    protected void openLocal() {
        this.inputCursor = this.inputPlan.getCursor(this.context);
        this.currentResults = Collections.emptyIterator();
    }

    @Override
    protected boolean hasNextLocal() {
        while (!this.currentResults.hasNext() && this.inputCursor.hasNext()) {
            this.currentResults = Objects.requireNonNull(
                    this.mapper.apply(this.inputCursor.next()), "flat-map function returned a null iterator");
        }
        return this.currentResults.hasNext();
    }

    @Override
    protected O nextLocal() {
        if (!hasNextLocal()) {
            throw invalidState("No more values are available.");
        }
        return this.currentResults.next();
    }

    @Override
    protected void closeLocal() {
        if (this.inputCursor != null) {
            this.inputCursor.close();
        }
        this.inputCursor = null;
        this.currentResults = null;
    }
}
