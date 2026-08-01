/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.config.RumbleRuntimeConfiguration;
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
 * representation it supports natively. Each access method prefers the matching native representation and falls back
 * to another native capability only when conversion is required.
 * </p>
 *
 * @param <T> the logical value produced by this plan
 */
public abstract class RuntimePlan<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected final RuntimeStaticContext staticContext;
    private final ExceptionMetadata metadata;
    private final int materializationCap;
    private final RuntimeDataFrameFactory<T> dataFrameFactory;

    protected RuntimePlan(@NonNull RuntimeStaticContext staticContext) {
        this(staticContext, null);
    }

    RuntimePlan(
            @NonNull RuntimeStaticContext staticContext,
            RuntimeDataFrameFactory<T> dataFrameFactory
    ) {
        this.staticContext = staticContext;
        this.metadata = this.staticContext.getMetadata();
        this.materializationCap = this.staticContext.getConfiguration().getMaterializationCap();
        this.dataFrameFactory = dataFrameFactory;
    }

    protected final ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    protected final RumbleRuntimeConfiguration getConfiguration() {
        return this.staticContext.getConfiguration();
    }

    /**
     * Exposes this plan as a cursor, using its native local capability when available.
     */
    public final Cursor<T> getCursor(@NonNull DynamicContext context) {
        return this.execute(
            ExecutionMode.LOCAL,
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
     * Exposes this plan as an RDD, using its native RDD capability when available.
     */
    public final JavaRDD<T> getRDD(@NonNull DynamicContext context) {
        return this.execute(
            ExecutionMode.RDD,
            context,
            cursor -> RuntimePlanConversions.cursorToRDD(cursor, this.materializationCap, this.metadata),
            rdd -> rdd,
            dataFrame -> dataFrame.toRDD(this.metadata)
        );
    }

    /**
     * Exposes this plan as a typed runtime DataFrame, using its native DataFrame capability when available.
     */
    public final RuntimeDataFrame<T> getDataFrame(@NonNull DynamicContext context) {
        if (!(this instanceof DataFrameRuntimePlan<?>) && this.dataFrameFactory == null) {
            throw this.unsupportedDataFrameConversion();
        }
        return this.execute(
            ExecutionMode.DATAFRAME,
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

    private <R, E extends Exception> R execute(
            ExecutionMode requestedExecutionMode,
            DynamicContext context,
            ExecutionAdapter<Cursor<T>, R, E> fromCursor,
            ExecutionAdapter<JavaRDD<T>, R, E> fromRDD,
            ExecutionAdapter<RuntimeDataFrame<T>, R, E> fromDataFrame
    )
            throws E {
        return switch (this.selectNativeExecutionMode(requestedExecutionMode)) {
            case LOCAL -> fromCursor.apply(this.localCapability().createNativeCursor(context));
            case RDD -> fromRDD.apply(this.rddCapability().createNativeRDD(context));
            case DATAFRAME -> fromDataFrame.apply(this.dataFrameCapability().createNativeDataFrame(context));
            case UNSET -> throw new OurBadException("Cannot execute a runtime plan whose execution mode is unset.");
        };
    }

    private ExecutionMode selectNativeExecutionMode(ExecutionMode requestedExecutionMode) {
        boolean supportsLocal = this instanceof LocalRuntimePlan<?>;
        boolean supportsRDD = this instanceof RDDRuntimePlan<?>;
        boolean supportsDataFrame = this instanceof DataFrameRuntimePlan<?>;

        if (!supportsLocal && !supportsRDD && !supportsDataFrame) {
            throw this.missingCapability(requestedExecutionMode);
        }

        return switch (requestedExecutionMode) {
            case LOCAL -> supportsLocal
                ? ExecutionMode.LOCAL
                : supportsRDD
                    ? ExecutionMode.RDD
                    : supportsDataFrame ? ExecutionMode.DATAFRAME : requestedExecutionMode;
            case RDD -> supportsRDD
                ? ExecutionMode.RDD
                : supportsDataFrame
                    ? ExecutionMode.DATAFRAME
                    : supportsLocal ? ExecutionMode.LOCAL : requestedExecutionMode;
            case DATAFRAME -> supportsDataFrame
                ? ExecutionMode.DATAFRAME
                : supportsRDD ? ExecutionMode.RDD : supportsLocal ? ExecutionMode.LOCAL : requestedExecutionMode;
            case UNSET -> requestedExecutionMode;
        };
    }

    private OurBadException missingCapability(ExecutionMode mode) {
        return new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " prefers "
                    + mode
                    + " execution but does not implement any local, RDD, or DataFrame execution capability."
        );
    }

    private OurBadException unsupportedDataFrameConversion() {
        return new OurBadException(
                "The runtime plan "
                    + this.getClass().getCanonicalName()
                    + " has no native DataFrame capability or DataFrame conversion factory."
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
        return this.execute(
            ExecutionMode.LOCAL,
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
        return this.execute(
            ExecutionMode.LOCAL,
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
        if (limit < 0) {
            throw new IllegalArgumentException("limit cannot be negative");
        }
        if (limit == 0) {
            return List.of();
        }
        if (this.canEvaluateAtMostOneDirectly()) {
            return this.materializeDirectAtMostOne(context);
        }
        return this.execute(
            ExecutionMode.LOCAL,
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
        return this.execute(
            ExecutionMode.LOCAL,
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

    private boolean canEvaluateAtMostOneDirectly() {
        return this instanceof AtMostOneLocalRuntimePlan<?>;
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
     * Returns the immutable static context determined while compiling this plan.
     *
     * @return the non-null runtime static context
     */
    public final RuntimeStaticContext getRuntimeStaticContext() {
        return this.staticContext;
    }
}
