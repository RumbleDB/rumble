/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.context.DynamicContext;

/**
 * Native RDD execution capability.
 *
 * <p>
 * A plan must implement this interface only when it has a native RDD implementation. Implementations must not
 * materialize a local cursor merely to satisfy this contract; execution-mode fallback belongs to the runtime
 * execution layer.
 * </p>
 *
 * @param <T> the RDD element type
 */
public interface RDDRuntimePlan<T> extends RuntimePlan<T> {

    /**
     * Builds the RDD for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting RDD
     */
    JavaRDD<T> executeRDD(DynamicContext context);
}
