/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;

/**
 * Native DataFrame execution capability. Plans that do not implement this interface are converted centrally from
 * another supported representation.
 *
 * @param <T> the logical value represented by each DataFrame row
 */
public interface DataFrameRuntimePlan<T> {

    /**
     * Builds the DataFrame for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting DataFrame
     */
    RuntimeDataFrame<T> getNativeDataFrame(DynamicContext context);
}
