/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;

/**
 * Capability for a plan that reports the variables required by its evaluation.
 */
public interface VariableDependencyRuntimePlan {

    default Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (!(this instanceof ItemRuntimePlan plan)) {
            return Collections.emptyMap();
        }
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        for (RuntimePlan<?> child : plan.getChildren()) {
            DynamicContext.mergeVariableDependencies(result, get(child));
        }
        return result;
    }

    static Map<Name, DynamicContext.VariableDependency> get(RuntimePlan<?> plan) {
        return plan instanceof VariableDependencyRuntimePlan dependencyPlan
            ? dependencyPlan.getVariableDependencies()
            : Collections.emptyMap();
    }
}
