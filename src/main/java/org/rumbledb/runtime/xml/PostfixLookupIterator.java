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

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This Iterator is for the postfix lookup operator in XQuery. It is similar to ObjectLookup in JSONiq but supports both
 * Objects (should be maps in the future) and Arrays. The lookupIterator is null in case we have a wildcard
 */
public class PostfixLookupIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private final RuntimeIterator lookupIterator;
    private List<Item> lookupKeys;
    private Queue<Item> nextResult;
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
        this.nextResult = new LinkedList<>();
        this.lookupIterator = lookupIterator;
        this.wildcard = this.lookupIterator == null;
    }

    private void initLookupKey(DynamicContext context) {
        if (this.wildcard)
            return;
        this.lookupKeys = this.lookupIterator.materialize(context);
    }

    @Override
    public void openLocal() {
        initLookupKey(this.currentDynamicContextForLocalExecution);
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult.poll(); // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        if (!this.nextResult.isEmpty())
            return;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
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

        if (this.nextResult.isEmpty()) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        initLookupKey(dynamicContext);
        List<Item> keys = this.lookupKeys;
        FlatMapFunction<Item, Item> transformation = new PostfixLookupClosure(keys, this.wildcard);
        return childRDD.flatMap(transformation);
    }
}
