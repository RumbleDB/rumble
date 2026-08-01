/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.util.Map;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;

/**
 * Variable dependency traversal for generic runtime plan references.
 */
public final class RuntimePlanDependencies {

    private RuntimePlanDependencies() {
    }

    public static Map<Name, DynamicContext.VariableDependency> get(RuntimePlan<?> plan) {
        return plan instanceof ItemRuntimePlan itemPlan
            ? itemPlan.getVariableDependencies()
            : Map.of();
    }
}
