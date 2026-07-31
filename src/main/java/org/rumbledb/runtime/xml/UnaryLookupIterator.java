/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unary lookup with XQuery 3.1 semantics. Array index out of bounds yields err:FOAY0001
 * per XPath and XQuery Functions 3.1.
 */
public class UnaryLookupIterator extends AbstractItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> lookupIterator;
    private final boolean wildcard;

    public UnaryLookupIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            (lookupIterator != null) ? Collections.singletonList(lookupIterator) : new ArrayList<>(),
            staticContext
        );
        this.lookupIterator = lookupIterator;
        this.wildcard = this.lookupIterator == null;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> lookup(
                    context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata()),
                    this.wildcard
                        ? List.of()
                        : this.lookupIterator.materialize(context)
                ).iterator(),
                getMetadata()
        );
    }

    private List<Item> lookup(List<Item> contextItems, List<Item> keys) {
        List<Item> results = new ArrayList<>();
        for (Item item : contextItems) {
            if (item.isMap()) {
                appendMapLookup(item, keys, results);
            } else if (item.isArray()) {
                appendArrayLookup(item, keys, results);
            } else {
                throw new UnexpectedTypeException(
                        "Type error; Lookup is only possible on Maps and Arrays, "
                            + item.getDynamicType()
                            + " detected instead",
                        getMetadata()
                );
            }
        }
        return results;
    }

    private void appendMapLookup(Item map, List<Item> keys, List<Item> results) {
        if (this.wildcard) {
            if (map.isObject()) {
                results.addAll(map.getItemValues());
            } else {
                map.getSequenceValues().forEach(results::addAll);
            }
            return;
        }
        for (Item rawKey : keys) {
            List<Item> atomized = rawKey.atomizedValue();
            if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
                throw new UnexpectedTypeException(
                        "Map lookup key must atomize to a single atomic value [err:XPTY0004].",
                        getMetadata()
                );
            }
            Item key = atomized.get(0);
            if (map.isObject()) {
                Item value = map.getItemByKey(key);
                if (value != null) {
                    results.add(value);
                }
            } else {
                List<Item> values = map.getSequenceByKey(key);
                if (values != null) {
                    results.addAll(values);
                }
            }
        }
    }

    private void appendArrayLookup(Item array, List<Item> keys, List<Item> results) {
        if (this.wildcard) {
            if (array.isArrayOfItems()) {
                results.addAll(array.getItemMembers());
            } else {
                array.getSequenceMembers().forEach(results::addAll);
            }
            return;
        }
        for (Item key : keys) {
            if (key.isString()) {
                throw new UnexpectedTypeException(
                        "Type error; Lookup with String on Arrays is not possible",
                        getMetadata()
                );
            }
            if (key.isNumeric()) {
                int index = key.castToIntValue() - 1;
                if (array.isArrayOfItems()) {
                    results.add(array.getItemAt(index));
                } else {
                    results.addAll(array.getSequenceAt(index));
                }
            }
        }
    }

}
