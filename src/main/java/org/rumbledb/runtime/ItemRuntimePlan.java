/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanConversions;
import org.rumbledb.runtime.plan.VariableDependencyRuntimePlan;
import org.rumbledb.types.SequenceType;

/**
 * Item-specific plan base shared by the runtime implementations.
 *
 * <p>
 * This class owns immutable plan metadata and children. Evaluation state belongs in cursors.
 * </p>
 */
public abstract class ItemRuntimePlan extends RuntimePlan<Item>
        implements
            NativeQueryRuntimePlan,
            VariableDependencyRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<RuntimePlan<Item>> children;
    protected final RuntimeStaticContext staticContext;

    protected ItemRuntimePlan(
            List<? extends RuntimePlan<Item>> children,
            @NonNull RuntimeStaticContext staticContext
    ) {
        this.staticContext = staticContext;
        if (staticContext.getStaticType() == null) {
            throw new OurBadException(
                    "Runtime plan created without a static type: " + this.getClass().getCanonicalName()
            );
        }
        this.children = List.copyOf(Objects.requireNonNullElse(children, Collections.emptyList()));
    }

    protected final RuntimePlan<Item> getChild(int index) {
        return this.children.get(index);
    }

    protected final List<RuntimePlan<Item>> getChildren() {
        return this.children;
    }

    protected final ExceptionMetadata getMetadata() {
        return this.staticContext.getMetadata();
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

    @Override
    public final RuntimeStaticContext getRuntimeStaticContext() {
        return this.staticContext;
    }

    @Override
    protected final RuntimeDataFrame<Item> convertRDDToDataFrame(JavaRDD<Item> rdd, DynamicContext context) {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromRDD(rdd, context, this.staticContext);
    }

    @Override
    protected final RuntimeDataFrame<Item> convertLocalToDataFrame(Cursor<Item> cursor, DynamicContext context) {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromList(
            RuntimePlanConversions.materializeCursor(cursor),
            context,
            this.staticContext
        );
    }

    public final boolean isUpdating() {
        return this.staticContext.isUpdating();
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (RuntimePlan<Item> child : this.children) {
            DynamicContext.mergeVariableDependencies(result, VariableDependencyRuntimePlan.get(child));
        }
        return result;
    }

    public void print(StringBuilder buffer, int indent) {
        buffer.append("  ".repeat(Math.max(0, indent)))
            .append(this.getClass().getSimpleName())
            .append(" | ")
            .append(this.staticContext.getExecutionMode())
            .append(" | ")
            .append(this.getStaticType())
            .append('\n');
        for (RuntimePlan<Item> child : this.children) {
            org.rumbledb.runtime.plan.RuntimePlanDiagnostics.print(child, buffer, indent + 1);
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }

    public boolean isSparkJobNeeded() {
        for (RuntimePlan<Item> child : this.children) {
            if (org.rumbledb.runtime.plan.RuntimePlanDiagnostics.isSparkJobNeeded(child)) {
                return true;
            }
        }
        return this.staticContext.getExecutionMode().isRDDOrDataFrame();
    }

    public static JavaRDD<Item> dataFrameToRDDOfItems(
            HomogeneousItemDataFrame dataFrame,
            ExceptionMetadata metadata
    ) {
        return dataFrame.toRDD(metadata);
    }

    private ExecutionMode checkedExecutionMode() {
        ExecutionMode mode = this.staticContext.getExecutionMode();
        if (mode == ExecutionMode.UNSET) {
            throw new OurBadException("Execution mode is unset for " + this.getClass().getCanonicalName());
        }
        return mode;
    }
}
