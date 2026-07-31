/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.runtime.RuntimeTupleIterator;

/**
 * Diagnostics kept outside the item runtime plan API.
 */
public final class RuntimePlanDiagnostics {

    private RuntimePlanDiagnostics() {
    }

    public static boolean isSparkJobNeeded(RuntimePlan<?> plan) {
        if (plan instanceof RuntimeTupleIterator tuplePlan) {
            return tuplePlan.isSparkJobNeeded();
        }
        if (plan.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return true;
        }
        return plan.diagnosticChildren().stream().anyMatch(RuntimePlanDiagnostics::isSparkJobNeeded);
    }

    public static void print(RuntimePlan<?> plan, StringBuilder buffer, int indent) {
        if (plan instanceof RuntimeTupleIterator tuplePlan) {
            tuplePlan.print(buffer, indent);
            return;
        }
        for (int i = 0; i < indent; i++) {
            buffer.append("  ");
        }
        buffer.append(plan.getClass().getSimpleName())
            .append(" | ")
            .append(plan.getRuntimeStaticContext().getExecutionMode())
            .append(" | ")
            .append(plan.getRuntimeStaticContext().getStaticType())
            .append('\n');
        for (RuntimePlan<?> child : plan.diagnosticChildren()) {
            print(child, buffer, indent + 1);
        }
    }
}
