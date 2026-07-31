/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.types.SequenceType;

/**
 * Shared immutable compilation state for plans that produce items.
 */
public abstract class AbstractItemRuntimePlan extends RuntimePlan<Item> {

    private final List<RuntimePlan<Item>> children;

    protected AbstractItemRuntimePlan(
            List<? extends RuntimePlan<Item>> children,
            @NonNull RuntimeStaticContext staticContext
    ) {
        super(staticContext, ItemRuntimeDataFrameFactory.INSTANCE);
        this.children = List.copyOf(Objects.requireNonNullElse(children, Collections.emptyList()));
    }

    protected final RuntimePlan<Item> getChild(int index) {
        return this.children.get(index);
    }

    protected final List<RuntimePlan<Item>> getChildren() {
        return this.children;
    }

    final List<? extends RuntimePlan<?>> diagnosticChildren() {
        return this.children;
    }

    protected final ExecutionMode getHighestExecutionMode() {
        return this.staticContext.getExecutionMode();
    }

    protected final SequenceType getStaticType() {
        return this.staticContext.getStaticType();
    }

    protected final RumbleRuntimeConfiguration getConfiguration() {
        return this.staticContext.getConfiguration();
    }

    public final boolean isUpdating() {
        return this.staticContext.isUpdating();
    }
}
