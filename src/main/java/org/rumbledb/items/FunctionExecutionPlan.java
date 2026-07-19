/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

import org.rumbledb.runtime.RuntimeIterator;

/**
 * The compiled, closure-independent part of a function item.
 *
 * Function items created from the same declaration share this plan. Each local invocation borrows an iterator because
 * runtime iterators contain mutable execution state and cannot be used concurrently.
 */
final class FunctionExecutionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator bodyIterator;
    private transient Deque<RuntimeIterator> availableIterators;

    FunctionExecutionPlan(RuntimeIterator bodyIterator) {
        this.bodyIterator = bodyIterator;
        this.availableIterators = new ArrayDeque<>();
    }

    RuntimeIterator getBodyIterator() {
        return this.bodyIterator;
    }

    synchronized RuntimeIterator acquireIterator() {
        RuntimeIterator iterator = this.availableIterators.pollFirst();
        return iterator == null ? this.bodyIterator.deepCopy() : iterator;
    }

    synchronized void releaseIterator(RuntimeIterator iterator) {
        this.availableIterators.addFirst(iterator);
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        this.availableIterators = new ArrayDeque<>();
    }
}
