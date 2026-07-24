/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.items;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.NoArgsConstructor;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.RuntimeIterator;

/**
 * Serializable factory for independent executions of one function body.
 *
 * Ordinary bodies are serialized once when the factory is created. Creating an execution only deserializes that
 * immutable snapshot; it never serializes a live or previously executed iterator tree. Bodies that own Spark runtime
 * state are retained instead.
 * 
 * Note: this is just a temporary solution to reduce the expense of deep copy operations on function body iterators.
 * We will switch to a cheaper and generic cursor-based solution in the future.
 */
@NoArgsConstructor(force = true)  // For Kryo serialization
final class FunctionBodyIteratorFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] serializedBody;
    /**
     * Spark ML bodies own runtime state such as broadcasts and datasets. They must not be cloned from a Java
     * serialization snapshot; retaining the body preserves that state for the lifetime of the function item.
     */
    private final RuntimeIterator retainedBody;
    private transient RuntimeIterator prototype;

    FunctionBodyIteratorFactory(RuntimeIterator bodyIterator, boolean retainBody) {
        this.prototype = bodyIterator;
        this.retainedBody = retainBody ? bodyIterator : null;
        this.serializedBody = retainBody ? null : serialize(bodyIterator);
    }

    /**
     * Returns the inspection-only prototype, reconstructing it after factory deserialization when necessary.
     */
    RuntimeIterator getPrototype() {
        if (this.retainedBody != null) {
            return this.retainedBody;
        }
        if (this.prototype == null) {
            this.prototype = deserialize(this.serializedBody);
        }
        return this.prototype;
    }

    /**
     * Creates mutable state for one independent function invocation from the pristine body snapshot, unless the body
     * owns Spark runtime state and must therefore be retained.
     */
    RuntimeIterator createExecutionInstance() {
        if (this.retainedBody != null) {
            return this.retainedBody;
        }
        return deserialize(this.serializedBody);
    }

    private static byte[] serialize(RuntimeIterator iterator) {
        try {
            ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputBytes);
            output.writeObject(iterator);
            output.flush();
            return outputBytes.toByteArray();
        } catch (IOException exception) {
            RumbleException rumbleException = new OurBadException("Error while snapshotting a function body iterator");
            rumbleException.initCause(exception);
            throw rumbleException;
        }
    }

    private static RuntimeIterator deserialize(byte[] serializedBody) {
        try {
            ByteArrayInputStream inputBytes = new ByteArrayInputStream(serializedBody);
            ObjectInputStream input = new ObjectInputStream(inputBytes);
            return (RuntimeIterator) input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            RumbleException rumbleException = new OurBadException("Error while creating a function body iterator");
            rumbleException.initCause(exception);
            throw rumbleException;
        }
    }
}
