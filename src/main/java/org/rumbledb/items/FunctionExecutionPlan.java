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
 * Function items created from the same declaration share this plan. The prototype is only inspected and copied; it is
 * never executed directly. Local invocations may reuse closed iterator copies, while other execution modes receive an
 * independent copy.
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

    RuntimeIterator createIndependentIterator() {
        return this.bodyIterator.deepCopy();
    }

    synchronized RuntimeIterator borrowLocalIterator() {
        RuntimeIterator iterator = this.availableIterators.pollFirst();
        return iterator == null ? createIndependentIterator() : iterator;
    }

    synchronized void returnLocalIterator(RuntimeIterator iterator) {
        if (iterator == null) {
            throw new IllegalArgumentException("Cannot return a null function body iterator.");
        }
        if (iterator.isOpen()) {
            throw new IllegalStateException("Only closed function body iterators can be returned for reuse.");
        }
        if (this.availableIterators.size() < MAX_RETAINED_LOCAL_ITERATORS) {
            this.availableIterators.addFirst(iterator);
        }
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        this.availableIterators = new ArrayDeque<>();
    }
}
