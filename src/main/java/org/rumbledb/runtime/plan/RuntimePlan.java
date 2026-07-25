/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.io.Serializable;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.LocalCursor;

/**
 * Immutable, reusable description of a runtime computation.
 *
 * <p>
 * A plan may be shared by multiple evaluations. Mutable state belonging to one local evaluation must be kept in the
 * {@link LocalCursor} returned by {@link #createLocalCursor(DynamicContext)}, never in the plan.
 * </p>
 *
 * <p>
 * Implementations advertise native distributed execution by also implementing {@link RDDRuntimePlan} or
 * {@link DataFrameRuntimePlan}. Conversion between execution modes is deliberately outside this interface.
 * </p>
 *
 * @param <T> the type produced by local execution of this plan
 */
public interface RuntimePlan<T> extends Serializable {

    /**
     * Creates an unopened cursor owned by one local evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return an independent, unopened local cursor
     */
    LocalCursor<T> createLocalCursor(DynamicContext context);

    /**
     * Returns the immutable metadata determined while compiling this plan.
     *
     * @return the non-null runtime static context
     */
    RuntimeStaticContext getRuntimeStaticContext();
}
