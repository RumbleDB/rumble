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
 */

package org.rumbledb.runtime.xml;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Spark closure for XQuery 3.1 postfix lookup: array index out of bounds raises err:FOAY0001
 * (propagated from {@link org.rumbledb.exceptions.ArrayIndexOutOfBoundsException}).
 */
public class XQueryPostfixLookupClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;
    private final List<Item> keys;
    private final boolean wildcard;
    private final ExceptionMetadata expressionMetadata;

    public XQueryPostfixLookupClosure(
            List<Item> keys,
            boolean wildcard,
            ExceptionMetadata expressionMetadata
    ) {
        this.keys = keys;
        this.wildcard = wildcard;
        this.expressionMetadata =
            expressionMetadata != null ? expressionMetadata : ExceptionMetadata.EMPTY_METADATA;
    }

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<>();

        if (arg0.isMap()) {
            if (this.wildcard) {
                if (arg0.isObject()) {
                    results.addAll(arg0.getItemValues());
                } else {
                    for (List<Item> valueSequence : arg0.getSequenceValues()) {
                        results.addAll(valueSequence);
                    }
                }
            } else {
                for (Item rawKey : this.keys) {
                    List<Item> atomized = rawKey.atomizedValue();
                    if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
                        throw new UnexpectedTypeException(
                                "Map lookup key must atomize to a single atomic value [err:XPTY0004].",
                                this.expressionMetadata
                        );
                    }
                    Item key = atomized.get(0);
                    if (arg0.isObject()) {
                        Item value = arg0.getItemByKey(key);
                        if (value != null) {
                            results.add(value);
                        }
                    } else {
                        List<Item> valueSequence = arg0.getSequenceByKey(key);
                        if (valueSequence != null && !valueSequence.isEmpty()) {
                            results.addAll(valueSequence);
                        }
                    }
                }
            }
        } else if (arg0.isArray()) {
            if (this.wildcard) {
                if (arg0.isArrayOfItems()) {
                    results = arg0.getItemMembers();
                } else {
                    for (List<Item> member : arg0.getSequenceMembers()) {
                        results.addAll(member);
                    }
                }
            } else {
                for (Item key : this.keys) {
                    if (key.isString()) {
                        throw new UnexpectedTypeException(
                                "Type error; Lookup with String on Arrays is not possible",
                                this.expressionMetadata
                        );
                    }
                    if (key.isNumeric()) {
                        int idx = key.castToIntValue() - 1;
                        if (arg0.isArrayOfItems()) {
                            results.add(arg0.getItemAt(idx));
                        } else {
                            results.addAll(arg0.getSequenceAt(idx));
                        }
                    }
                }
            }

        }
        return results.iterator();
    }
}
