/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.typing;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

final class XmlSchemaCastSupport {

    private XmlSchemaCastSupport() {}

    static List<Item> materializeAtomizedAtMostTwo(
            ItemRuntimePlan operand, DynamicContext context, ExceptionMetadata metadata) {
        List<Item> result = new ArrayList<>(2);
        try (Cursor<Item> cursor = operand.getCursor(context)) {
            while (cursor.hasNext() && result.size() < 2) {
                Item item = cursor.next();
                if (item.isAtomic()) {
                    addAtomizedItem(result, item, context, metadata);
                } else {
                    try {
                        for (Item atomizedItem : item.atomizedValue()) {
                            addAtomizedItem(result, atomizedItem, context, metadata);
                            if (result.size() == 2) {
                                break;
                            }
                        }
                    } catch (CannotAtomizeException exception) {
                        CannotAtomizeException resultException = new CannotAtomizeException(
                                "Atomization in XML Schema cast failed for \"" + item.serialize() + "\".", metadata);
                        resultException.initCause(exception);
                        throw resultException;
                    }
                }
            }
        }
        return result;
    }

    private static void addAtomizedItem(
            List<Item> result, Item item, DynamicContext context, ExceptionMetadata metadata) {
        if (!item.getDynamicType().isResolved()) {
            item.getDynamicType().resolve(context, metadata);
        }
        result.add(item);
    }
}
