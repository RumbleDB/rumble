/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Compatibility cursor backed by an explicitly recreated, evaluation-owned legacy iterator.
 *
 * <p>
 * This bridge performs no serialization. The supplied iterator must be a fresh object and its children must be
 * {@link CursorRuntimeIteratorAdapter}s.
 * </p>
 */
public final class RecreatedRuntimeIteratorCursor extends AbstractLocalCursor<Item> {

    private final Supplier<? extends RuntimeIterator> executionFactory;
    private final DynamicContext context;
    private RuntimeIterator execution;

    public RecreatedRuntimeIteratorCursor(
            @NonNull Supplier<? extends RuntimeIterator> executionFactory,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata
    ) {
        super(metadata);
        this.executionFactory = executionFactory;
        this.context = context;
    }

    public static LocalCursor<Item> fromArguments(
            @NonNull List<? extends RuntimePlan<Item>> argumentPlans,
            @NonNull DynamicContext context,
            @NonNull RuntimeStaticContext staticContext,
            @NonNull BiFunction<List<RuntimeIterator>, RuntimeStaticContext, ? extends RuntimeIterator> factory,
            @NonNull ExceptionMetadata metadata
    ) {
        return new RecreatedRuntimeIteratorCursor(
                () -> factory.apply(
                    CursorRuntimeIteratorAdapter.adaptItems(argumentPlans),
                    staticContext.toBuilder().executionMode(ExecutionMode.LOCAL).build()
                ),
                context,
                metadata
        );
    }

    public static RuntimeStaticContext localStaticContext(RuntimeStaticContext staticContext) {
        return staticContext.toBuilder().executionMode(ExecutionMode.LOCAL).build();
    }

    @Override
    protected void openLocal() {
        this.execution = this.executionFactory.get();
        this.execution.open(this.context);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.execution.hasNext();
    }

    @Override
    protected Item nextLocal() {
        return this.execution.next();
    }

    @Override
    protected void closeLocal() {
        if (this.execution != null && this.execution.isOpen()) {
            this.execution.close();
        }
        this.execution = null;
    }
}
