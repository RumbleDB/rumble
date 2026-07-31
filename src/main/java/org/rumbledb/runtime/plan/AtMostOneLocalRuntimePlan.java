/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;

/**
 * Native local execution capability for a plan that produces at most one value.
 *
 * @param <T> the value type
 */
public interface AtMostOneLocalRuntimePlan<T> {

    /**
     * Evaluates the plan without allocating a cursor.
     *
     * @param context the dynamic context for the evaluation
     * @return the value, or {@code null} for the empty sequence
     */
    T evaluateAtMostOne(DynamicContext context);
}
