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
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.*;

/**
 * This Iterator is for the unary lookup operator in XQuery. It is similar to ObjectLookup in JSONiq but supports both
 * Objects
 * (should be maps in the future) and Arrays. The lookupIterator is null in case we have a wildcard
 */
public class UnaryLookupIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator lookupIterator;
    private List<Item> lookupKeys;
    private List<Item> contextItem;
    private Queue<Item> nextResult;
    private boolean wildcard;

    public UnaryLookupIterator(
            RuntimeIterator lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            (lookupIterator != null) ? Collections.singletonList(lookupIterator) : new ArrayList<>(),
            staticContext
        );
        this.nextResult = new LinkedList<>();
        this.lookupIterator = lookupIterator;
        this.wildcard = this.lookupIterator == null;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.hasNext = true;
        this.contextItem = this.currentDynamicContextForLocalExecution.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
        if (!this.wildcard)
            this.lookupKeys = this.lookupIterator.materialize(context);

        for (Item item : this.contextItem) {
            if (item.isObject()) {
                if (this.wildcard) {
                    this.nextResult.addAll(item.getValues());
                } else {
                    for (Item key : this.lookupKeys) {
                        if (key.isString()) {
                            this.nextResult.add(item.getItemByKey(key.getStringValue()));
                        }
                        if (key.isNumeric()) {
                            // TODO numeric maps
                        }
                    }
                }

            } else if (item.isArray()) {
                if (this.wildcard) {
                    this.nextResult.addAll(item.getItems());
                } else {
                    for (Item key : this.lookupKeys) {
                        if (key.isString()) {
                            throw new UnexpectedTypeException(
                                    "Type error; Lookup with String on Arrays is not possible",
                                    getMetadata()
                            );
                        }
                        if (key.isNumeric()) {
                            this.nextResult.add(item.getItemAt(key.castToIntValue() - 1));
                        }
                    }
                }

            } else {
                throw new UnexpectedTypeException(
                        "Type error; Lookup is only possible on Maps and Arrays, "
                            + item.getDynamicType().toString()
                            + " detected instead",
                        getMetadata()
                );
            }
        }
        this.hasNext = !this.nextResult.isEmpty();
    }

    @Override
    public Item next() {
        Item result = this.nextResult.poll();
        this.hasNext = !this.nextResult.isEmpty();
        return result;
    }
}
