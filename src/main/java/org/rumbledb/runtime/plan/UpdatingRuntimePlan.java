/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.PendingUpdateList;

/**
 * Capability for a plan that produces pending updates.
 */
public interface UpdatingRuntimePlan {

    PendingUpdateList getPendingUpdateList(DynamicContext context);

    static PendingUpdateList get(RuntimePlan<?> plan, DynamicContext context) {
        if (plan instanceof UpdatingRuntimePlan updatingPlan) {
            return updatingPlan.getPendingUpdateList(context);
        }
        throw new OurBadException(
                "The runtime plan " + plan.getClass().getCanonicalName() + " does not support pending updates.",
                plan.getRuntimeStaticContext().getMetadata());
    }
}
