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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/** Local cast to an imported XML Schema simple type. */
public final class XmlSchemaCastIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan operand;
    private final Name targetTypeName;
    private final boolean allowsEmptyInput;

    /** Xerces grammars are intentionally local-only and are not serialized with a runtime plan. */
    private final transient XmlSchemaCatalog schemaCatalog;

    public XmlSchemaCastIterator(
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> evaluate(context).iterator(), getMetadata());
    }

    private List<Item> evaluate(DynamicContext context) {
        List<Item> atomizedValue =
                XmlSchemaCastSupport.materializeAtomizedAtMostTwo(this.operand, context, getMetadata());
        if (atomizedValue.size() > 1) {
            throw cardinalityError();
        }
        if (atomizedValue.isEmpty()) {
            if (!this.allowsEmptyInput) {
                throw cardinalityError();
            }
            return List.of();
        }
        if (this.schemaCatalog == null) {
            throw new OurBadException(
                    "The local XML Schema catalog is unavailable to the cast runtime plan.", getMetadata());
        }
        return this.schemaCatalog.castSimpleType(
                this.targetTypeName,
                atomizedValue.get(0),
                NamespaceBindingUtils.namespaceResolver(this.staticContext),
                getMetadata());
    }

    private UnexpectedTypeException cardinalityError() {
        return new UnexpectedTypeException(
                "A cast expression operand must atomize to exactly one item unless '?' is specified.", getMetadata());
    }
}
