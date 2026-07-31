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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;
import org.rumbledb.runtime.dataframe.RuntimeDataFrameFactory;

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

    protected final RuntimeStaticContext staticContext;
    private final ExceptionMetadata metadata;
    private final int materializationCap;
    private final RuntimeDataFrameFactory<T> dataFrameFactory;

    protected RuntimePlan(@NonNull RuntimeStaticContext staticContext) {
        this(staticContext, null);
    }

    protected RuntimePlan(
            @NonNull RuntimeStaticContext staticContext,
            RuntimeDataFrameFactory<T> dataFrameFactory
    ) {
        this.staticContext = this.resolveCompiledExecutionMode(staticContext);
        this.metadata = this.staticContext.getMetadata();
        this.materializationCap = this.staticContext.getConfiguration().getMaterializationCap();
        this.dataFrameFactory = dataFrameFactory;
    }

    private RuntimeStaticContext resolveCompiledExecutionMode(RuntimeStaticContext context) {
        if (context.getExecutionMode() != ExecutionMode.DATAFRAME || this instanceof DataFrameRuntimePlan<?>) {
            return context;
        }
        if (this instanceof RDDRuntimePlan<?>) {
            return context.toBuilder().executionMode(ExecutionMode.RDD).build();
        }
        if (this instanceof LocalRuntimePlan<?>) {
            return context.toBuilder().executionMode(ExecutionMode.LOCAL).build();
        }
        return context;
    }

    protected final ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * Executes this plan in its compiled representation and exposes the result as a cursor.
     */
    public final Cursor<T> getCursor(@NonNull DynamicContext context) {
        return this.executeSelectedRepresentation(
            context,
            cursor -> cursor,
            rdd -> RuntimePlanConversions.rddToCursor(rdd, this.materializationCap, this.metadata),
            dataFrame -> RuntimePlanConversions.rddToCursor(
                dataFrame.toRDD(this.metadata),
                this.materializationCap,
                this.metadata
            )
        );
    }

    /**
     * Returns this plan as an RDD, executing its compiled representation first and converting only at this boundary.
     */
    public final JavaRDD<T> getRDD(@NonNull DynamicContext context) {
        return this.executeSelectedRepresentation(
            context,
            cursor -> RuntimePlanConversions.cursorToRDD(cursor, this.materializationCap, this.metadata),
            rdd -> rdd,
            dataFrame -> dataFrame.toRDD(this.metadata)
        );
    }

    /**
     * Executes this plan in its compiled representation and exposes the result as a typed runtime DataFrame.
     */
    public final RuntimeDataFrame<T> getDataFrame(@NonNull DynamicContext context) {
        if (!this.staticContext.getExecutionMode().isDataFrame() && this.dataFrameFactory == null) {
            throw this.unsupportedDataFrameConversion();
        }
        return this.executeSelectedRepresentation(
            context,
            cursor -> this.dataFrameFactory.fromList(
                RuntimePlanConversions.materializeCursor(cursor),
                context,
                this.staticContext
            ),
            rdd -> this.dataFrameFactory.fromRDD(rdd, context, this.staticContext),
            dataFrame -> dataFrame
        );
    }

    private <R, E extends Exception> R executeSelectedRepresentation(
            DynamicContext context,
            ExecutionAdapter<Cursor<T>, R, E> fromCursor,
            ExecutionAdapter<JavaRDD<T>, R, E> fromRDD,
            ExecutionAdapter<RuntimeDataFrame<T>, R, E> fromDataFrame
    )
            throws E {
        return switch (this.staticContext.getExecutionMode()) {
            case LOCAL -> {
                if (this instanceof LocalRuntimePlan<?>) {
                    yield fromCursor.apply(this.localCapability().createNativeCursor(context));
                }
                if (this instanceof RDDRuntimePlan<?>) {
                    yield fromRDD.apply(this.rddCapability().createNativeRDD(context));
                }
                if (this instanceof DataFrameRuntimePlan<?>) {
                    yield fromDataFrame.apply(this.dataFrameCapability().createNativeDataFrame(context));
                }
                throw this.missingCapability(ExecutionMode.LOCAL);
            }
            case RDD -> {
                if (this instanceof RDDRuntimePlan<?>) {
                    yield fromRDD.apply(this.rddCapability().createNativeRDD(context));
                }
                if (this instanceof DataFrameRuntimePlan<?>) {
                    yield fromDataFrame.apply(this.dataFrameCapability().createNativeDataFrame(context));
                }
                if (this instanceof LocalRuntimePlan<?>) {
                    yield fromCursor.apply(this.localCapability().createNativeCursor(context));
                }
                throw this.missingCapability(ExecutionMode.RDD);
            }
            case DATAFRAME -> {
                if (this instanceof DataFrameRuntimePlan<?>) {
                    yield fromDataFrame.apply(this.dataFrameCapability().createNativeDataFrame(context));
                }
                if (this instanceof RDDRuntimePlan<?>) {
                    yield fromRDD.apply(this.rddCapability().createNativeRDD(context));
                }
                if (this instanceof LocalRuntimePlan<?>) {
                    yield fromCursor.apply(this.localCapability().createNativeCursor(context));
                }
                throw this.missingCapability(ExecutionMode.DATAFRAME);
            }
            case UNSET -> throw new OurBadException("Cannot execute a runtime plan whose execution mode is unset.");
        };
    }

    private OurBadException missingCapability(ExecutionMode mode) {
        return new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " was compiled for "
                    + mode
                    + " execution but does not implement the corresponding capability or any convertible capability."
        );
    }

    private OurBadException unsupportedDataFrameConversion() {
        return new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " cannot convert its selected native representation to a DataFrame."
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
        return this.executeSelectedRepresentation(
            context,
            RuntimePlanConversions::materializeCursor,
            rdd -> RuntimePlanConversions.collectRDDWithLimit(
                rdd,
                this.materializationCap,
                this.metadata
            ),
            dataFrame -> RuntimePlanConversions.collectRDDWithLimit(
                dataFrame.toRDD(this.metadata),
                this.materializationCap,
                this.metadata
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
            return this.atMostOneCapability().evaluateAtMostOne(context);
        }
        return this.executeSelectedRepresentation(
            context,
            RuntimePlan::materializeFirstFromCursor,
            RuntimePlan::firstOrNull,
            dataFrame -> firstOrNull(
                dataFrame.toRDD(this.metadata)
            )
        );
    }

    /**
     * Evaluates this plan locally and materializes no more than {@code limit} results.
     *
     * @param context the dynamic context for the evaluation
     * @param limit the maximum number of results to materialize
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
        return this.executeSelectedRepresentation(
            context,
            cursor -> materializeAtMostFromCursor(cursor, limit),
            rdd -> rdd.take(limit),
            dataFrame -> dataFrame.toRDD(this.metadata)
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
            return this.atMostOneCapability().evaluateAtMostOne(context);
        }
        return this.executeSelectedRepresentation(
            context,
            (cursor) -> RuntimePlan.materializeAtMostOneFromCursor(
                cursor,
                this.metadata
            ),
            rdd -> RuntimePlan.materializeAtMostOneFromRDD(
                rdd,
                this.metadata
            ),
            dataFrame -> RuntimePlan.materializeAtMostOneFromRDD(
                dataFrame.toRDD(this.metadata),
                this.metadata
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
        return this.staticContext.getExecutionMode() == ExecutionMode.LOCAL
            && this instanceof AtMostOneLocalRuntimePlan<?>;
    }

    private List<T> materializeDirectAtMostOne(DynamicContext context) {
        List<T> result = new ArrayList<>(1);
        T item = this.atMostOneCapability().evaluateAtMostOne(context);
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

    @SuppressWarnings("unchecked")
    private LocalRuntimePlan<T> localCapability() {
        return (LocalRuntimePlan<T>) this;
    }

    @SuppressWarnings("unchecked")
    private AtMostOneLocalRuntimePlan<T> atMostOneCapability() {
        return (AtMostOneLocalRuntimePlan<T>) this;
    }

    @SuppressWarnings("unchecked")
    private RDDRuntimePlan<T> rddCapability() {
        return (RDDRuntimePlan<T>) this;
    }

    @SuppressWarnings("unchecked")
    private DataFrameRuntimePlan<T> dataFrameCapability() {
        return (DataFrameRuntimePlan<T>) this;
    }

    @FunctionalInterface
    private interface ExecutionAdapter<I, O, E extends Exception> {
        O apply(I input) throws E;
    }

    /**
     * Returns the immutable metadata determined while compiling this plan.
     *
     * @return the non-null runtime static context
     */
    public final RuntimeStaticContext getRuntimeStaticContext() {
        return this.staticContext;
    }
}
