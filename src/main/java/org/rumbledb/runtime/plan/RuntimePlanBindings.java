/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;

/**
 * Binds a plan result using the representation selected by its runtime static context.
 */
public final class RuntimePlanBindings {

    private RuntimePlanBindings() {}

    public static void bind(
            ItemRuntimePlan plan, DynamicContext targetContext, Name variable, DynamicContext executionContext) {
        if (plan.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            targetContext
                    .getVariableValues()
                    .addVariableValue(variable, ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(plan, executionContext));
        } else if (plan.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            targetContext.getVariableValues().addVariableValue(variable, plan.getRDD(executionContext));
        } else {
            targetContext.getVariableValues().addVariableValue(variable, plan.materialize(executionContext));
        }
    }
}
