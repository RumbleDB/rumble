/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.scripting.block;

import java.util.List;

import lombok.NonNull;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

/**
 * Drains a sequence of side-effecting plans before streaming a result plan.
 */
final class SequentialLocalCursor<T> extends AbstractLocalCursor<T> {

    private final List<? extends RuntimePlan<?>> prefixPlans;
    private final RuntimePlan<T> resultPlan;
    private final DynamicContext context;
    private LocalCursor<T> resultCursor;

    public SequentialLocalCursor(
            @NonNull List<? extends RuntimePlan<?>> prefixPlans,
            @NonNull RuntimePlan<T> resultPlan,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.prefixPlans = List.copyOf(prefixPlans);
        this.resultPlan = resultPlan;
        this.context = context;
    }

    @Override
    protected void openLocal() {
        for (RuntimePlan<?> prefixPlan : this.prefixPlans) {
            drain(prefixPlan);
        }
        this.resultCursor = this.resultPlan.createLocalCursor(this.context);
    }

    private <V> void drain(RuntimePlan<V> plan) {
        try (LocalCursor<V> cursor = plan.createLocalCursor(this.context)) {
            while (cursor.hasNext()) {
                cursor.next();
            }
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.resultCursor.hasNext();
    }

    @Override
    protected T nextLocal() {
        return this.resultCursor.next();
    }

    @Override
    protected void closeLocal() {
        if (this.resultCursor != null) {
            this.resultCursor.close();
        }
        this.resultCursor = null;
    }
}
