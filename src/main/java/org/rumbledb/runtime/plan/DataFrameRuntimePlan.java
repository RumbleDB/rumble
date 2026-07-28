/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.items.structured.JSoundDataFrame;

/**
 * Native DataFrame execution capability for item plans. Plans that do not implement this interface are converted
 * centrally from another supported representation.
 */
public interface DataFrameRuntimePlan {

    /**
     * Builds the DataFrame for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting DataFrame
     */
    JSoundDataFrame getNativeDataFrame(DynamicContext context);
}
