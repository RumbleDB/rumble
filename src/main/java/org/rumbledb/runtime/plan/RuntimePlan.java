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

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;

/**
 * Immutable, reusable description of a runtime computation.
 *
 * <p>
 * A plan may be shared by multiple evaluations. Mutable state belonging to one local evaluation must be kept in the
 * {@link LocalCursor} returned by {@link #createLocalCursor(DynamicContext)}, never in the plan.
 * </p>
 *
 * <p>
 * Plans may also implement {@link RDDRuntimePlan} or {@link DataFrameRuntimePlan} to request distributed execution
 * with centrally managed conversion.
 * </p>
 *
 * @param <T> the type produced by local execution of this plan
 */
public abstract class RuntimePlan<T> implements Serializable {

    /**
     * Creates an unopened cursor owned by one local evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return an independent, unopened local cursor
     */
    public abstract LocalCursor<T> createLocalCursor(DynamicContext context);

    private LocalCursor<T> createExecutionCursor(@NonNull DynamicContext context) {
        return switch (this.getExecutionMode()) {
            case LOCAL -> this.createLocalCursor(context);
            case RDD -> RuntimePlanConversions.rddToLocalCursor(
                this.getRDD(context),
                this.getRuntimeStaticContext()
            );
            case DATAFRAME -> RuntimePlanConversions.rddToLocalCursor(
                this.getDataFrameResult(context).toRDD(this.getRuntimeStaticContext().getMetadata()),
                this.getRuntimeStaticContext()
            );
            case UNSET -> throw this.unsetExecutionMode();
        };
    }

    /**
     * Returns this plan as an RDD, executing its compiled representation first and converting only at this boundary.
     */
    public final JavaRDD<T> getRDD(@NonNull DynamicContext context) {
        return switch (this.getExecutionMode()) {
            case LOCAL -> RuntimePlanConversions.localToRDD(this, context);
            case RDD -> {
                if (this.hasNativeRDD()) {
                    yield this.getNativeRDD(context);
                }
                if (this.hasNativeDataFrame()) {
                    yield this.getNativeDataFrame(context).toRDD(this.getRuntimeStaticContext().getMetadata());
                }
                yield RuntimePlanConversions.localToRDD(this, context);
            }
            case DATAFRAME -> {
                if (this.hasNativeDataFrame()) {
                    yield this.getNativeDataFrame(context).toRDD(this.getRuntimeStaticContext().getMetadata());
                }
                if (this.hasNativeRDD()) {
                    yield this.getNativeRDD(context);
                }
                yield RuntimePlanConversions.localToRDD(this, context);
            }
            case UNSET -> throw this.unsetExecutionMode();
        };
    }

    /**
     * Executes this plan in its compiled representation and exposes the result as a typed runtime DataFrame.
     */
    protected final RuntimeDataFrame<T> getDataFrameResult(@NonNull DynamicContext context) {
        return switch (this.getExecutionMode()) {
            case LOCAL -> this.convertLocalToDataFrame(context);
            case RDD -> {
                if (this.hasNativeRDD()) {
                    yield this.convertRDDToDataFrame(this.getNativeRDD(context), context);
                }
                if (this.hasNativeDataFrame()) {
                    yield this.getNativeDataFrame(context);
                }
                yield this.convertLocalToDataFrame(context);
            }
            case DATAFRAME -> {
                if (this.hasNativeDataFrame()) {
                    yield this.getNativeDataFrame(context);
                }
                if (this.hasNativeRDD()) {
                    yield this.convertRDDToDataFrame(this.getNativeRDD(context), context);
                }
                yield this.convertLocalToDataFrame(context);
            }
            case UNSET -> throw this.unsetExecutionMode();
        };
    }

    protected RuntimeDataFrame<T> convertLocalToDataFrame(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.DATAFRAME);
    }

    protected RuntimeDataFrame<T> convertRDDToDataFrame(JavaRDD<T> rdd, DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.RDD);
    }

    protected JavaRDD<T> getNativeRDD(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.RDD);
    }

    protected RuntimeDataFrame<T> getNativeDataFrame(DynamicContext context) {
        throw this.unsupportedRepresentation(ExecutionMode.DATAFRAME);
    }

    private boolean hasNativeRDD() {
        return this instanceof RDDRuntimePlan<?>;
    }

    private boolean hasNativeDataFrame() {
        return this instanceof DataFrameRuntimePlan<?>;
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
        try (LocalCursor<T> cursor = this.createExecutionCursor(context)) {
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
        try (LocalCursor<T> cursor = this.createExecutionCursor(context)) {
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
        try (LocalCursor<T> cursor = this.createExecutionCursor(context)) {
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
        try (LocalCursor<T> cursor = this.createExecutionCursor(context)) {
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
