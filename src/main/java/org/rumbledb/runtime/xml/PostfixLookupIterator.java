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
import java.util.ArrayList;
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
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

/**
 * Postfix lookup with XQuery 3.1 semantics. Array index out of bounds yields err:FOAY0001
 * per XPath and XQuery Functions 3.1.
 */
public class PostfixLookupIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new LookupLocalCursor(
                this.iterator,
                this.lookupIterator,
                this.wildcard,
                context,
                getRuntimeStaticContext()
        );
    }

    private static final class LookupLocalCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan inputPlan;
        private final ItemRuntimePlan lookupPlan;
        private final boolean wildcard;
        private final DynamicContext context;
        private final RuntimeStaticContext staticContext;
        private Cursor<Item> inputCursor;
        private List<Item> keys;
        private Iterator<Item> currentResults;

        private LookupLocalCursor(
                ItemRuntimePlan inputPlan,
                ItemRuntimePlan lookupPlan,
                boolean wildcard,
                DynamicContext context,
                RuntimeStaticContext staticContext
        ) {
            super(staticContext.getMetadata());
            this.inputPlan = inputPlan;
            this.lookupPlan = lookupPlan;
            this.wildcard = wildcard;
            this.context = context;
            this.staticContext = staticContext;
        }

        @Override
        protected void openLocal() {
            this.keys = this.wildcard
                ? List.of()
                : this.lookupPlan.materialize(this.context);
            this.inputCursor = this.inputPlan.getCursor(this.context);
            this.currentResults = Collections.emptyIterator();
        }

        @Override
        protected boolean hasNextLocal() {
            while (!this.currentResults.hasNext() && this.inputCursor.hasNext()) {
                this.currentResults = lookupLocally(
                    this.inputCursor.next(),
                    this.keys,
                    this.wildcard,
                    this.staticContext
                ).iterator();
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

    private static List<Item> lookupLocally(
            Item item,
            List<Item> keys,
            boolean wildcard,
            RuntimeStaticContext staticContext
    ) {
        List<Item> results = new ArrayList<>();
        if (item.isMap()) {
            if (wildcard) {
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
                            staticContext.getMetadata()
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
            if (wildcard) {
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
                            staticContext.getMetadata()
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
                staticContext.getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan iterator;
    private final ItemRuntimePlan lookupIterator;
    private boolean wildcard;

    public PostfixLookupIterator(
            ItemRuntimePlan object,
            ItemRuntimePlan lookupIterator,
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
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.getChild(0).getRDD(dynamicContext);
        List<Item> keys = this.wildcard
            ? List.of()
            : this.lookupIterator.materialize(dynamicContext);
        FlatMapFunction<Item, Item> transformation = new PostfixLookupClosure(
                keys,
                this.wildcard,
                getMetadata()
        );
        return childRDD.flatMap(transformation);
    }
}
