/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.util.function.IntFunction;

import lombok.NonNull;

/**
 * Lazily resolves and memoizes positional arguments for one evaluation.
 *
 * @param <T> the argument value type
 */
public final class EvaluationArguments<T> {

    private final Object[] values;
    private final boolean[] resolved;
    private final IntFunction<? extends T> resolver;

    private EvaluationArguments(int size, @NonNull IntFunction<? extends T> resolver) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        this.values = new Object[size];
        this.resolved = new boolean[size];
        this.resolver = resolver;
    }

    public static <T> EvaluationArguments<T> lazy(
            int size,
            @NonNull IntFunction<? extends T> resolver
    ) {
        return new EvaluationArguments<>(size, resolver);
    }

    public int size() {
        return this.values.length;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (!this.resolved[index]) {
            this.values[index] = this.resolver.apply(index);
            this.resolved[index] = true;
        }
        return (T) this.values[index];
    }
}
