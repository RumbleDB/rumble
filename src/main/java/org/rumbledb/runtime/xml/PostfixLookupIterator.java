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

import java.io.Serial;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

/**
 * Postfix lookup with XQuery 3.1 semantics. Array index out of bounds yields err:FOAY0001
 * per XPath and XQuery Functions 3.1.
 */
public class PostfixLookupIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new LookupLocalCursor(this, context);
    }

    private static final class LookupLocalCursor extends AbstractLocalCursor<Item> {

        private final PostfixLookupIterator plan;
        private final DynamicContext context;
        private LocalCursor<Item> inputCursor;
        private List<Item> keys;
        private Iterator<Item> currentResults;

        private LookupLocalCursor(PostfixLookupIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.keys = this.plan.wildcard
                ? List.of()
                : LocalCursorUtils.materialize(this.plan.lookupIterator, this.context);
            this.inputCursor = this.plan.iterator.createLocalCursor(this.context);
            this.currentResults = Collections.emptyIterator();
        }

        @Override
        protected boolean hasNextLocal() {
            while (!this.currentResults.hasNext() && this.inputCursor.hasNext()) {
                this.currentResults = this.plan.lookupLocally(this.inputCursor.next(), this.keys).iterator();
            }
            return this.currentResults.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("No more values are available.");
            }
            return this.currentResults.next();
        }

        @Override
        protected void closeLocal() {
            if (this.inputCursor != null) {
                this.inputCursor.close();
                this.inputCursor = null;
            }
            this.keys = null;
            this.currentResults = null;
        }
    }

    private List<Item> lookupLocally(Item item, List<Item> keys) {
        List<Item> results = new java.util.ArrayList<>();
        if (item.isMap()) {
            if (this.wildcard) {
                if (item.isObject()) {
                    results.addAll(item.getItemValues());
                } else {
                    for (List<Item> valueSequence : item.getSequenceValues()) {
                        results.addAll(valueSequence);
                    }
                }
                return results;
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
                if (item.isObject()) {
                    Item value = item.getItemByKey(key);
                    if (value != null) {
                        results.add(value);
                    }
                } else {
                    List<Item> valueSequence = item.getSequenceByKey(key);
                    if (valueSequence != null) {
                        results.addAll(valueSequence);
                    }
                }
            }
            return results;
        }
        if (item.isArray()) {
            if (this.wildcard) {
                if (item.isArrayOfItems()) {
                    results.addAll(item.getItemMembers());
                } else {
                    for (List<Item> member : item.getSequenceMembers()) {
                        results.addAll(member);
                    }
                }
                return results;
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
                    if (item.isArrayOfItems()) {
                        results.add(item.getItemAt(index));
                    } else {
                        results.addAll(item.getSequenceAt(index));
                    }
                }
            }
            return results;
        }
        throw new UnexpectedTypeException(
                "Type error; Lookup is only possible on Maps and Arrays, "
                    + item.getDynamicType()
                    + " detected instead",
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private final RuntimeIterator lookupIterator;
    private boolean wildcard;

    public PostfixLookupIterator(
            RuntimeIterator object,
            RuntimeIterator lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Stream.of(object, lookupIterator).filter(Objects::nonNull).collect(Collectors.toList()),
            staticContext
        );
        this.iterator = object;
        this.lookupIterator = lookupIterator;
        this.wildcard = this.lookupIterator == null;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.getChild(0).getRDD(dynamicContext);
        List<Item> keys = this.wildcard
            ? List.of()
            : LocalCursorUtils.materialize(this.lookupIterator, dynamicContext);
        FlatMapFunction<Item, Item> transformation = new PostfixLookupClosure(
                keys,
                this.wildcard,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }
}
