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
import org.rumbledb.runtime.RuntimeIterator;

final class XmlSchemaCastSupport {

    private XmlSchemaCastSupport() {
    }

    static List<Item> atomize(RuntimeIterator iterator, DynamicContext context) {
        List<Item> result = new ArrayList<>();
        iterator.open(context);
        try {
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item.isAtomic()) {
                    result.add(item);
                } else {
                    result.addAll(item.atomizedValue());
                }
            }
        } finally {
            iterator.close();
        }
        return result;
    }
}
