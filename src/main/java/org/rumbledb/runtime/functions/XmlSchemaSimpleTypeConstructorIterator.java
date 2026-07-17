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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

/** Constructs the atomic sequence represented by an XML Schema list or non-pure union value. */
public final class XmlSchemaSimpleTypeConstructorIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    private final XmlSchemaConstructorFunction constructor;
    private List<Item> results;
    private int position;

    public XmlSchemaSimpleTypeConstructorIterator(
            List<RuntimeIterator> arguments,
            XmlSchemaConstructorFunction constructor,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.constructor = constructor;
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
        Item argument;
        try {
            argument = this.children.get(0).materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException exception) {
            throw new UnexpectedTypeException(
                    "An XML Schema constructor expects at most one argument item.",
                    getMetadata()
            );
        }
        argument = atomize(argument);
        if (argument == null) {
            this.results = List.of();
        } else if (this.constructor.variety() == XmlSchemaConstructorFunction.Variety.LIST) {
            this.results = constructList(argument);
        } else {
            this.results = constructUnion(argument);
        }
        this.position = 0;
        this.hasNext = !this.results.isEmpty();
    }

    private Item atomize(Item argument) {
        if (argument == null || argument.isAtomic()) {
            return argument;
        }
        List<Item> atomizedValue = argument.atomizedValue();
        if (atomizedValue.isEmpty()) {
            return null;
        }
        if (atomizedValue.size() > 1) {
            throw new UnexpectedTypeException(
                    "Atomization of an XML Schema constructor argument produced more than one item.",
                    getMetadata()
            );
        }
        return atomizedValue.get(0);
    }

    private List<Item> constructList(Item argument) {
        if (!argument.isString() && !argument.isUntypedAtomic()) {
            throw castError(argument);
        }
        return this.constructor.validator().validate(argument, this.staticContext);
    }

    private List<Item> constructUnion(Item argument) {
        if (argument.isString() || argument.isUntypedAtomic()) {
            return this.constructor.validator().validate(argument, this.staticContext);
        }
        for (ItemType memberType : this.constructor.unionAtomicMemberTypes()) {
            Item result;
            try {
                result = CastIterator.castItemToType(argument, memberType, getMetadata(), this.staticContext);
            } catch (RumbleException exception) {
                continue;
            }
            if (result != null) {
                return this.constructor.validator().validate(result, this.staticContext);
            }
        }
        throw castError(argument);
    }

    private CastException castError(Item argument) {
        return new CastException(
                "A value of type "
                    + argument.getDynamicType()
                    + " cannot be cast to XML Schema type "
                    + this.constructor.identifier().getName()
                    + ".",
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
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "XML Schema constructor", getMetadata());
        }
        Item result = this.results.get(this.position++);
        this.hasNext = this.position < this.results.size();
        return result;
    }
}
