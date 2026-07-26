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
 * RDD execution capability.
 *
 * <p>
 * The runtime may use a native RDD implementation or centrally convert local execution.
 * </p>
 *
 * @param <T> the RDD element type
 */
public interface RDDRuntimePlan<T> {

    /**
     * Builds the RDD for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting RDD
     */
    JavaRDD<T> getRDD(DynamicContext context);
}
