/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;

/**
 * Immutable, reusable description of a runtime computation.
 *
 * <p>
 * A plan may be shared by multiple evaluations. Mutable state belonging to one local evaluation must be kept in the
 * {@link Cursor} returned by a {@link LocalRuntimePlan}, never in the plan.
 * </p>
 *
 * <p>
 * A plan implements {@link LocalRuntimePlan}, {@link RDDRuntimePlan}, or {@link DataFrameRuntimePlan} for every
 * representation it supports natively. The compiled execution mode selects one native representation; conversion to
 * a representation requested by a caller happens only after that selection.
 * </p>
 *
 * @param <T> the logical value produced by this plan
 */
public abstract class RuntimePlan<T> implements Serializable {

    public Cursor<T> createNativeCursor(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.LOCAL);
    }

    /**
     * Executes this plan in its compiled representation and exposes the result as a cursor.
     */
    public final Cursor<T> getCursor(@NonNull DynamicContext context) {
        return this.executeAs(
            context,
            Function.identity(),
            rdd -> RuntimePlanConversions.rddToCursor(rdd, this.getRuntimeStaticContext()),
            dataFrame -> RuntimePlanConversions.rddToCursor(
                dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata()),
                this.getRuntimeStaticContext()
            )
        );
    }

    /**
     * Returns this plan as an RDD, executing its compiled representation first and converting only at this boundary.
     */
    public final JavaRDD<T> getRDD(@NonNull DynamicContext context) {
        return this.executeAs(
            context,
            RuntimePlanConversions::cursorToRDD,
            Function.identity(),
            dataFrame -> dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata())
        );
    }

    /**
     * Executes this plan in its compiled representation and exposes the result as a typed runtime DataFrame.
     */
    protected final RuntimeDataFrame<T> getDataFrameResult(@NonNull DynamicContext context) {
        return this.executeAs(
            context,
            cursor -> this.convertLocalToDataFrame(cursor, context),
            rdd -> this.convertRDDToDataFrame(rdd, context),
            Function.identity()
        );
    }

    protected RuntimeDataFrame<T> convertLocalToDataFrame(Cursor<T> cursor, DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.DATAFRAME);
    }

    protected RuntimeDataFrame<T> convertRDDToDataFrame(JavaRDD<T> rdd, DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.DATAFRAME);
    }

    protected JavaRDD<T> getNativeRDD(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.RDD);
    }

    protected RuntimeDataFrame<T> getNativeDataFrame(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.DATAFRAME);
    }

    private <R> R executeAs(
            DynamicContext context,
            Function<Cursor<T>, R> fromCursor,
            Function<JavaRDD<T>, R> fromRDD,
            Function<RuntimeDataFrame<T>, R> fromDataFrame
    ) {
        return switch (this.getExecutionMode()) {
            case LOCAL -> {
                this.requireCapability(this instanceof LocalRuntimePlan<?>, ExecutionMode.LOCAL);
                yield fromCursor.apply(this.createNativeCursor(context));
            }
            case RDD -> {
                this.requireCapability(this instanceof RDDRuntimePlan<?>, ExecutionMode.RDD);
                yield fromRDD.apply(this.getNativeRDD(context));
            }
            case DATAFRAME -> {
                this.requireCapability(this instanceof DataFrameRuntimePlan<?>, ExecutionMode.DATAFRAME);
                yield fromDataFrame.apply(this.getNativeDataFrame(context));
            }
            case UNSET -> throw this.unsetExecutionMode();
        };
    }

    private void requireCapability(boolean supported, ExecutionMode mode) {
        if (!supported) {
            throw new OurBadException(
                    "The runtime plan "
                        + this.getClass().getCanonicalName()
                        + " was compiled for "
                        + mode
                        + " execution but does not implement the corresponding capability."
            );
        }
    }

    private ExecutionMode getExecutionMode() {
        return this.getRuntimeStaticContext().getExecutionMode();
    }

    private OurBadException unsetExecutionMode() {
        return new OurBadException("Cannot execute a runtime plan whose execution mode is unset.");
    }

    private OurBadException unsupportedRepresentation(ExecutionMode representation) {
        return new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " does not support "
                    + representation
                    + " execution."
        );
    }

    /**
     * Evaluates this plan locally and materializes every result.
     *
     * @param context the dynamic context for the evaluation
     * @return the materialized result sequence
     */
    public final List<T> materialize(@NonNull DynamicContext context) {
        List<T> result = new ArrayList<>();
        try (Cursor<T> cursor = this.getCursor(context)) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    /**
     * Evaluates this plan locally and returns its first result, or {@code null} for an empty sequence.
     *
     * @param context the dynamic context for the evaluation
     * @return the first result, or {@code null}
     */
    public final T materializeFirstOrNull(@NonNull DynamicContext context) {
        try (Cursor<T> cursor = this.getCursor(context)) {
            return cursor.hasNext() ? cursor.next() : null;
        }
    }

    /**
     * Evaluates this plan locally and materializes no more than {@code limit} results.
     *
     * @param context the dynamic context for the evaluation
     * @param limit the maximum number of results to materialize
     * @return the materialized prefix
     */
    public final List<T> materializeAtMost(@NonNull DynamicContext context, int limit) {
        Objects.requireNonNull(context, "dynamic context cannot be null");
        if (limit < 0) {
            throw new IllegalArgumentException("limit cannot be negative");
        }
        List<T> result = new ArrayList<>(limit);
        try (Cursor<T> cursor = this.getCursor(context)) {
            while (result.size() < limit && cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    /**
     * Evaluates this plan locally and returns its only result, or {@code null} for an empty sequence.
     *
     * @param context the dynamic context for the evaluation
     * @return the only result, or {@code null}
     * @throws MoreThanOneItemException if more than one result is produced
     */
    public final T materializeAtMostOne(@NonNull DynamicContext context) throws MoreThanOneItemException {
        try (Cursor<T> cursor = this.getCursor(context)) {
            if (!cursor.hasNext()) {
                return null;
            }
            T result = cursor.next();
            if (cursor.hasNext()) {
                throw new MoreThanOneItemException();
            }
            return result;
        }
    }

    /**
     * Evaluates this plan locally and substitutes a default value for an empty sequence.
     *
     * @param context the dynamic context for the evaluation
     * @param defaultValue the value returned for an empty sequence
     * @return the only result or {@code defaultValue}
     * @throws MoreThanOneItemException if more than one result is produced
     */
    public final T materializeAtMostOneOrDefault(@NonNull DynamicContext context, @NonNull T defaultValue)
            throws MoreThanOneItemException {
        T result = this.materializeAtMostOne(context);
        return result == null ? defaultValue : result;
    }

    /**
     * Returns the immutable metadata determined while compiling this plan.
     *
     * @return the non-null runtime static context
     */
    public abstract RuntimeStaticContext getRuntimeStaticContext();
}
