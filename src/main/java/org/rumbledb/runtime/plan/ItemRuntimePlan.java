/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.types.SequenceType;

/**
 * Shared immutable compilation state for plans that produce items.
 */
public abstract class ItemRuntimePlan extends RuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<RuntimePlan<Item>> children;

    protected ItemRuntimePlan(
            @NonNull List<? extends RuntimePlan<Item>> children,
            @NonNull RuntimeStaticContext staticContext
    ) {
        super(staticContext, ItemRuntimeDataFrameFactory.INSTANCE);
        this.children = new ArrayList<>(children);
    }

    protected final RuntimePlan<Item> getChild(int index) {
        return this.children.get(index);
    }

    protected final List<RuntimePlan<Item>> getChildren() {
        return this.children;
    }

    protected final SequenceType getStaticType() {
        return this.staticContext.getStaticType();
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (RuntimePlan<Item> child : this.children) {
            DynamicContext.mergeVariableDependencies(result, RuntimePlanDependencies.get(child));
        }
        return result;
    }
}
