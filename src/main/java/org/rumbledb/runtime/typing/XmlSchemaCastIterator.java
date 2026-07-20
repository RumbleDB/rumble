/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.typing;

import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

/** Casts an operand to an imported XML Schema simple type. */
public final class XmlSchemaCastIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator operandIterator;
    private final XmlSchemaConstructorFunction constructor;
    private final boolean allowsEmptyInput;
    private List<Item> results;
    private int position;

    public XmlSchemaCastIterator(
            RuntimeIterator operandIterator,
            XmlSchemaConstructorFunction constructor,
            boolean allowsEmptyInput,
            RuntimeStaticContext staticContext
    ) {
        super(List.of(operandIterator), staticContext);
        this.operandIterator = operandIterator;
        this.constructor = constructor;
        this.allowsEmptyInput = allowsEmptyInput;
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
        List<Item> atomizedValue = XmlSchemaCastSupport.atomize(this.operandIterator, context);
        if (atomizedValue.size() > 1) {
            throw cardinalityError();
        }
        if (atomizedValue.isEmpty()) {
            if (!this.allowsEmptyInput) {
                throw cardinalityError();
            }
            this.results = List.of();
        } else {
            this.results = this.constructor.construct(atomizedValue.get(0), this.staticContext);
        }
        this.position = 0;
        this.hasNext = !this.results.isEmpty();
    }

    private UnexpectedTypeException cardinalityError() {
        return new UnexpectedTypeException(
                "A cast expression operand must atomize to exactly one item unless '?' is specified.",
                getMetadata()
        );
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "XML Schema cast", getMetadata());
        }
        Item result = this.results.get(this.position++);
        this.hasNext = this.position < this.results.size();
        return result;
    }
}
