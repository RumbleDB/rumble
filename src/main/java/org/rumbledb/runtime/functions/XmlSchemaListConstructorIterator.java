/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.functions;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.xml.schema.XmlSchemaSimpleTypeValidator;

/** Constructs the typed atomic sequence represented by an XML Schema list value. */
public final class XmlSchemaListConstructorIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    private final XmlSchemaSimpleTypeValidator validator;
    private List<Item> results;
    private int position;

    public XmlSchemaListConstructorIterator(
            List<RuntimeIterator> arguments,
            XmlSchemaSimpleTypeValidator validator,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.validator = validator;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initialize(context);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        initialize(context);
    }

    private void initialize(DynamicContext context) {
        Item argument = this.children.get(0).materializeFirstItemOrNull(context);
        if (argument == null) {
            this.results = List.of();
        } else {
            if (!argument.isString() && !argument.isUntypedAtomic()) {
                throw new CastException(
                        "A value of type "
                            + argument.getDynamicType()
                            + " cannot be cast to an XML Schema list type.",
                        getMetadata()
                );
            }
            this.results = this.validator.validate(argument, this.staticContext);
        }
        this.position = 0;
        this.hasNext = !this.results.isEmpty();
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "XML Schema list constructor", getMetadata());
        }
        Item result = this.results.get(this.position++);
        this.hasNext = this.position < this.results.size();
        return result;
    }
}
