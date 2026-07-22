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
 * Capability implemented by a runtime plan that can natively produce an RDD.
 *
 * @param <T> the RDD element type
 */
public interface RDDRuntimePlan<T> extends RuntimePlan<T> {

    JavaRDD<T> getRDD(DynamicContext context);
}
