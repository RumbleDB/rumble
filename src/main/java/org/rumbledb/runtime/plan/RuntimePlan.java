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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
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
        if (this instanceof AtMostOneLocalRuntimePlan<?>) {
            return new AtMostOneLocalCursor<>(this.getRuntimeStaticContext().getMetadata()) {
                @Override
                protected T materializeOneItemOrNull() {
                    return RuntimePlan.this.evaluateAtMostOne(context);
                }
            };
        }
        throw this.unsupportedRepresentation(ExecutionMode.LOCAL);
    }

    /**
     * Direct native-local evaluation hook for {@link AtMostOneLocalRuntimePlan} implementations.
     */
    public T evaluateAtMostOne(DynamicContext context) {
        throw new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " does not support direct at-most-one evaluation."
        );
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
    public RuntimeDataFrame<T> getDataFrame(@NonNull DynamicContext context) {
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
        return switch (this.getRuntimeStaticContext().getExecutionMode()) {
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
            case UNSET -> throw new OurBadException("Cannot execute a runtime plan whose execution mode is unset.");
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
        if (this.canEvaluateAtMostOneDirectly()) {
            return this.materializeDirectAtMostOne(context);
        }
        return this.executeAs(
            context,
            RuntimePlanConversions::materializeCursor,
            rdd -> RuntimePlanConversions.collectRDDWithLimit(
                rdd,
                this.getRuntimeStaticContext().getConfiguration(),
                this.getRuntimeStaticContext().getMetadata()
            ),
            dataFrame -> RuntimePlanConversions.collectRDDWithLimit(
                dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata()),
                this.getRuntimeStaticContext().getConfiguration(),
                this.getRuntimeStaticContext().getMetadata()
            )
        );
    }

    /**
     * Evaluates this plan locally and returns its first result, or {@code null} for an empty sequence.
     *
     * @param context the dynamic context for the evaluation
     * @return the first result, or {@code null}
     */
    public final T materializeFirstOrNull(@NonNull DynamicContext context) {
        if (this.canEvaluateAtMostOneDirectly()) {
            return this.evaluateAtMostOne(context);
        }
        return this.executeAs(
            context,
            RuntimePlan::materializeFirstFromCursor,
            RuntimePlan::firstOrNull,
            dataFrame -> firstOrNull(
                dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata())
            )
        );
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
        if (limit == 0) {
            return new ArrayList<>();
        }
        if (this.canEvaluateAtMostOneDirectly()) {
            return this.materializeDirectAtMostOne(context);
        }
        return this.executeAs(
            context,
            cursor -> materializeAtMostFromCursor(cursor, limit),
            rdd -> rdd.take(limit),
            dataFrame -> dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata())
                .take(limit)
        );
    }

    /**
     * Evaluates this plan locally and returns its only result, or {@code null} for an empty sequence.
     *
     * @param context the dynamic context for the evaluation
     * @return the only result, or {@code null}
     * @throws MoreThanOneItemException if more than one result is produced
     */
    public final T materializeAtMostOne(@NonNull DynamicContext context) throws MoreThanOneItemException {
        if (this.canEvaluateAtMostOneDirectly()) {
            return this.evaluateAtMostOne(context);
        }
        return this.executeAs(
            context,
            (cursor) -> RuntimePlan.materializeAtMostOneFromCursor(
                cursor,
                this.getRuntimeStaticContext().getMetadata()
            ),
            rdd -> RuntimePlan.materializeAtMostOneFromRDD(
                rdd,
                this.getRuntimeStaticContext().getMetadata()
            ),
            dataFrame -> RuntimePlan.materializeAtMostOneFromRDD(
                dataFrame.toRDD(this.getRuntimeStaticContext().getMetadata()),
                this.getRuntimeStaticContext().getMetadata()
            )
        );
    }

    /**
     * Evaluates this plan and returns its only result.
     *
     * @param context the dynamic context for the evaluation
     * @return the only result
     * @throws NoItemException if the plan produces no result
     * @throws MoreThanOneItemException if the plan produces more than one result
     */
    public final T materializeExactlyOne(@NonNull DynamicContext context)
            throws NoItemException,
                MoreThanOneItemException {
        T result = this.materializeAtMostOne(context);
        if (result == null) {
            throw new NoItemException();
        }
        return result;
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

    private boolean canEvaluateAtMostOneDirectly() {
        return this.getRuntimeStaticContext().getExecutionMode() == ExecutionMode.LOCAL
            && this instanceof AtMostOneLocalRuntimePlan<?>;
    }

    private List<T> materializeDirectAtMostOne(DynamicContext context) {
        List<T> result = new ArrayList<>(1);
        T item = this.evaluateAtMostOne(context);
        if (item != null) {
            result.add(item);
        }
        return result;
    }

    private static <T> T materializeFirstFromCursor(Cursor<T> cursor) {
        try (cursor) {
            return cursor.hasNext() ? cursor.next() : null;
        }
    }

    private static <T> List<T> materializeAtMostFromCursor(Cursor<T> cursor, int limit) {
        List<T> result = new ArrayList<>(limit);
        try (cursor) {
            while (result.size() < limit && cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }

    private static <T> T materializeAtMostOneFromCursor(Cursor<T> cursor, ExceptionMetadata metadata)
            throws MoreThanOneItemException {
        try (cursor) {
            if (!cursor.hasNext()) {
                return null;
            }
            T result = cursor.next();
            if (cursor.hasNext()) {
                throw new MoreThanOneItemException(metadata);
            }
            return result;
        }
    }

    private static <T> T firstOrNull(JavaRDD<T> rdd) {
        List<T> result = rdd.take(1);
        return result.isEmpty() ? null : result.get(0);
    }

    private static <T> T materializeAtMostOneFromRDD(JavaRDD<T> rdd, ExceptionMetadata metadata)
            throws MoreThanOneItemException {
        List<T> result = rdd.take(2);
        if (result.size() > 1) {
            throw new MoreThanOneItemException(metadata);
        }
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Returns the immutable metadata determined while compiling this plan.
     *
     * @return the non-null runtime static context
     */
    public abstract RuntimeStaticContext getRuntimeStaticContext();
}
