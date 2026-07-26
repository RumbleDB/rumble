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

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Evaluates a zero-or-one result lazily when the cursor is opened.
 */
public final class ComputedLocalCursor<T> extends AtMostOneLocalCursor<T> {

    public interface Arguments<T> {

        int size();

        T get(int index);
    }

    private final Supplier<? extends T> computation;

    public ComputedLocalCursor(
            @NonNull Supplier<? extends T> computation,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.computation = computation;
    }

    public static <I, O> ComputedLocalCursor<O> fromArguments(
            @NonNull List<? extends RuntimePlan<I>> argumentPlans,
            @NonNull DynamicContext context,
            @NonNull Function<? super Arguments<I>, ? extends O> computation,
            @NonNull ExceptionMetadata metadata
    ) {
        List<? extends RuntimePlan<I>> plans = List.copyOf(argumentPlans);
        return new ComputedLocalCursor<>(
                () -> computation.apply(
                    arguments(
                        plans.size(),
                        index -> LocalCursorUtils.materializeFirst(plans.get(index), context)
                    )
                ),
                metadata
        );
    }

    public static <T> Arguments<T> arguments(
            int size,
            @NonNull IntFunction<? extends T> resolver
    ) {
        return new LazyArguments<>(size, resolver);
    }

    @Override
    protected T materializeFirstItemOrNull() {
        return this.computation.get();
    }

    private static final class LazyArguments<T> implements Arguments<T> {

        private final Object[] values;
        private final boolean[] resolved;
        private final IntFunction<? extends T> resolver;

        private LazyArguments(int size, IntFunction<? extends T> resolver) {
            this.values = new Object[size];
            this.resolved = new boolean[size];
            this.resolver = resolver;
        }

        @Override
        public int size() {
            return this.values.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (!this.resolved[index]) {
                this.values[index] = this.resolver.apply(index);
                this.resolved[index] = true;
            }
            return (T) this.values[index];
        }
    }
}
