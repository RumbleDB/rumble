/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.runtime.flwor.NativeClauseContext;

/**
 * Capability for a plan that can translate itself to a native Spark SQL expression.
 */
public interface NativeQueryRuntimePlan {

    default NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return NativeClauseContext.NoNativeQuery;
    }

    static NativeClauseContext generate(RuntimePlan<?> plan, NativeClauseContext context) {
        return plan instanceof NativeQueryRuntimePlan nativePlan
                ? nativePlan.generateNativeQuery(context)
                : NativeClauseContext.NoNativeQuery;
    }
}
