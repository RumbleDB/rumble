/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.update.expression;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.EmptyLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

/**
 * Base plan for updating expressions whose local value is always the empty sequence.
 */
abstract class UpdatingExpressionIterator extends HybridRuntimeIterator {

    protected UpdatingExpressionIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public final LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new EmptyLocalCursor<>();
    }
}
