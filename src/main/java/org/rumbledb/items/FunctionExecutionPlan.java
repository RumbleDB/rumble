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
 * Function items created from the same declaration share this plan. Local invocations borrow iterators; distributed
 * invocations receive fresh copies because their lazy evaluation can outlive the call that created them.
 */
final class FunctionExecutionPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_RETAINED_LOCAL_ITERATORS = 128;

    private final RuntimeIterator bodyIterator;
    private transient Deque<RuntimeIterator> availableIterators;

    FunctionExecutionPlan(RuntimeIterator bodyIterator) {
        this.bodyIterator = bodyIterator;
        this.availableIterators = new ArrayDeque<>();
    }

    RuntimeIterator getBodyIterator() {
        return this.bodyIterator;
    }

    RuntimeIterator createIterator() {
        return this.bodyIterator.deepCopy();
    }

    synchronized RuntimeIterator acquireIterator() {
        RuntimeIterator iterator = this.availableIterators.pollFirst();
        return iterator == null ? createIterator() : iterator;
    }

    synchronized void releaseIterator(RuntimeIterator iterator) {
        if (iterator == null) {
            throw new IllegalArgumentException("Cannot return a null function body iterator.");
        }
        if (iterator.isOpen()) {
            throw new IllegalStateException("Only closed function body iterators can be returned for reuse.");
        }
        // The deque is a cache: discarding excess iterators is safe.
        if (this.availableIterators.size() < MAX_RETAINED_LOCAL_ITERATORS) {
            this.availableIterators.addFirst(iterator);
        }
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        this.availableIterators = new ArrayDeque<>();
    }
}
