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
 * Native DataFrame execution capability for item plans.
 *
 * <p>
 * A plan must implement this interface only when it has a native DataFrame implementation. Implementations must not
 * convert from local or RDD execution merely to satisfy this contract; execution-mode fallback belongs to the runtime
 * execution layer.
 * </p>
 */
public interface DataFrameRuntimePlan {

    /**
     * Builds the DataFrame for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting DataFrame
     */
    JSoundDataFrame executeDataFrame(DynamicContext context);
}
