/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.AttributeNodeItemType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class SchemaAttributeTest implements NodeTest {

    private static final long serialVersionUID = 1L;
    private AttributeNodeItemType itemType;

    public SchemaAttributeTest() {
    }

    public SchemaAttributeTest(Name declarationName) {
        this.itemType = AttributeNodeItemType.schemaAttribute(declarationName);
    }

    public boolean matches(Item item) {
        return this.itemType.matches(item);
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        this.itemType.resolve(context, metadata);
    }

    @Override
    public String toString() {
        return this.itemType.toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.itemType);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.itemType = kryo.readObject(input, AttributeNodeItemType.class);
    }
}
