/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;

/**
 * Base plan for updating expressions whose local value is always the empty sequence.
 */
abstract class UpdatingExpressionIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, UpdatingRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    protected UpdatingExpressionIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public final Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AtMostOneLocalCursor<>(null, this.getRuntimeStaticContext().getMetadata());
    }
}
