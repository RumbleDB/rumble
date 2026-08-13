/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.typing;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/** Local castability test for an imported XML Schema simple type. */
public final class XmlSchemaCastableIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan operand;
    private final Name targetTypeName;
    private final boolean allowsEmptyInput;

    /** Xerces grammars are intentionally local-only and are not serialized with a runtime plan. */
    private final transient XmlSchemaCatalog schemaCatalog;

    public XmlSchemaCastableIterator(
            ItemRuntimePlan operand,
            Name targetTypeName,
            boolean allowsEmptyInput,
            XmlSchemaCatalog schemaCatalog,
            RuntimeStaticContext staticContext) {
        super(List.of(operand), staticContext);
        this.operand = operand;
        this.targetTypeName = targetTypeName;
        this.allowsEmptyInput = allowsEmptyInput;
        this.schemaCatalog = schemaCatalog;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> atomizedValue =
                XmlSchemaCastSupport.materializeAtomizedAtMostTwo(this.operand, context, getMetadata());
        if (atomizedValue.size() > 1) {
            return booleanItem(false);
        }
        if (atomizedValue.isEmpty()) {
            return booleanItem(this.allowsEmptyInput);
        }
        if (this.schemaCatalog == null) {
            throw new OurBadException(
                    "The local XML Schema catalog is unavailable to the castable runtime plan.", getMetadata());
        }
        try {
            this.schemaCatalog.castSimpleType(
                    this.targetTypeName,
                    atomizedValue.get(0),
                    NamespaceBindingUtils.namespaceResolver(this.staticContext),
                    getMetadata());
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
