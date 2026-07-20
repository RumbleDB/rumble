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

import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.RuntimeIterator;

/**
 * Serializable factory for independent executions of one function body.
 *
 * The body is serialized once when the factory is created. Creating an execution only deserializes that immutable
 * snapshot; it never serializes a live or previously executed iterator tree.
 */
final class FunctionBodyIteratorFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] serializedBody;
    private transient RuntimeIterator prototype;

    FunctionBodyIteratorFactory(RuntimeIterator bodyIterator) {
        this.prototype = bodyIterator;
        this.serializedBody = serialize(bodyIterator);
    }

    /**
     * Returns the inspection-only prototype, reconstructing it after factory deserialization when necessary.
     */
    RuntimeIterator getPrototype() {
        if (this.prototype == null) {
            this.prototype = deserialize(this.serializedBody);
        }
        return this.prototype;
    }

    /**
     * Creates mutable state for one independent function invocation from the pristine body snapshot.
     */
    RuntimeIterator createExecutionInstance() {
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
