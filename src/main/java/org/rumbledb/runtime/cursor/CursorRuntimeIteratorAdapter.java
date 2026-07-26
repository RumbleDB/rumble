/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.cursor;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

/**
 * Presents a cursor-native plan through the legacy iterator protocol.
 *
 * <p>
 * This adapter is intended only inside an evaluation-owned iterator recreated by
 * {@link RecreatedRuntimeIteratorCursor}. It lets mature legacy algorithms consume migrated child plans without
 * sharing child iterator state.
 * </p>
 */
public final class CursorRuntimeIteratorAdapter extends LocalRuntimeIterator {

    private final RuntimePlan<Item> plan;
    private transient LocalCursor<Item> cursor;

    public CursorRuntimeIteratorAdapter(@NonNull RuntimePlan<Item> plan) {
        super(
            List.of(),
            plan.getRuntimeStaticContext()
                .toBuilder()
                .executionMode(org.rumbledb.expressions.ExecutionMode.LOCAL)
                .build()
        );
        this.plan = plan;
    }

    public static RuntimeIterator adapt(@NonNull RuntimePlan<Item> plan) {
        return new CursorRuntimeIteratorAdapter(plan);
    }

    public static List<RuntimeIterator> adaptItems(
            @NonNull List<? extends RuntimePlan<Item>> plans
    ) {
        List<RuntimeIterator> result = new ArrayList<>(plans.size());
        for (RuntimePlan<Item> plan : plans) {
            result.add(plan == null ? null : new CursorRuntimeIteratorAdapter(plan));
        }
        return result;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return this.plan.createLocalCursor(context);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.cursor = this.plan.createLocalCursor(context);
        this.cursor.open();
    }

    @Override
    public boolean hasNext() {
        return this.cursor.hasNext();
    }

    @Override
    public Item next() {
        return this.cursor.next();
    }

    @Override
    public void close() {
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = null;
        super.close();
    }
}
