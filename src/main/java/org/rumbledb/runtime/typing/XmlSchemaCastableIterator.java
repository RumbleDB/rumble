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
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

/** Tests whether an operand can be cast to an imported XML Schema simple type. */
public final class XmlSchemaCastableIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator operandIterator;
    private final XmlSchemaConstructorFunction constructor;
    private final boolean allowsEmptyInput;

    public XmlSchemaCastableIterator(
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
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> atomizedValue = XmlSchemaCastSupport.atomize(this.operandIterator, context);
        if (atomizedValue.size() > 1) {
            return booleanItem(false);
        }
        if (atomizedValue.isEmpty()) {
            return booleanItem(this.allowsEmptyInput);
        }

        try {
            this.constructor.construct(atomizedValue.get(0), this.staticContext);
            return booleanItem(true);
        } catch (OurBadException exception) {
            throw exception;
        } catch (RumbleException exception) {
            return booleanItem(false);
        }
    }

    private static Item booleanItem(boolean value) {
        return ItemFactory.getInstance().createBooleanItem(value);
    }
}
