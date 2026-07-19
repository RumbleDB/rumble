/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import org.rumbledb.runtime.RuntimeIterator;

final class FunctionBodyExecutionPlan {

    private final Supplier<RuntimeIterator> iteratorFactory;
    private final Deque<RuntimeIterator> availableIterators;

    FunctionBodyExecutionPlan(Supplier<RuntimeIterator> iteratorFactory) {
        this.iteratorFactory = iteratorFactory;
        this.availableIterators = new ArrayDeque<>();
    }

    synchronized RuntimeIterator acquireIterator() {
        RuntimeIterator iterator = this.availableIterators.pollFirst();
        return iterator == null ? this.iteratorFactory.get() : iterator;
    }

    synchronized void releaseIterator(RuntimeIterator iterator) {
        this.availableIterators.addFirst(iterator);
    }
}
