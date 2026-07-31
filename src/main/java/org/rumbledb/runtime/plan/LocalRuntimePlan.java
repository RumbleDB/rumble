/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.cursor.Cursor;

/**
 * Native local execution capability.
 *
 * @param <T> the cursor value type
 */
public interface LocalRuntimePlan<T> {

    /**
     * Creates an independent cursor for one native local evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return an unopened cursor
     */
    Cursor<T> createNativeCursor(DynamicContext context);
}
