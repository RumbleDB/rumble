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
 */

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XQuery 3.1 map constructor: atomized single-atomic keys and general- sequence values.
 */
public class MapConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final List<RuntimeIterator> keys;
    private final List<RuntimeIterator> values;

    public MapConstructorRuntimeIterator(
            List<RuntimeIterator> keys,
            List<RuntimeIterator> values,
            RuntimeStaticContext staticContext
    ) {
        super(keys, staticContext);
        this.children.addAll(values);
        this.keys = keys;
        this.values = values;
    }

    private static Item atomizeSingleMapKey(
            RuntimeIterator keyIterator,
            DynamicContext dynamicContext,
            org.rumbledb.exceptions.ExceptionMetadata metadata
    ) {
        List<Item> keySequence = new ArrayList<>();
        keyIterator.materialize(dynamicContext, keySequence);
        List<Item> atomized = new ArrayList<>();
        for (Item item : keySequence) {
            atomized.addAll(item.atomizedValue());
        }
        if (atomized.size() != 1) {
            throw new UnexpectedTypeException(
                    "Map constructor key must atomize to a single atomic value [err:XPTY0004].",
                    metadata
            );
        }
        Item k = atomized.get(0);
        if (!k.isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map constructor key must atomize to a single atomic value [err:XPTY0004].",
                    metadata
            );
        }
        return k;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        List<Item> mapKeys = new ArrayList<>();
        List<List<Item>> valueSequences = new ArrayList<>();
        boolean allKeysString = true;
        boolean allValuesSingletons = true;
        for (int i = 0; i < this.keys.size(); i++) {
            Item key = atomizeSingleMapKey(this.keys.get(i), dynamicContext, getMetadata());
            mapKeys.add(key);
            RuntimeIterator valueIterator = this.values.get(i);
            List<Item> valueSeq = new ArrayList<>();
            valueIterator.materialize(dynamicContext, valueSeq);
            if (!key.isString()) {
                allKeysString = false;
            }
            if (valueSeq.size() != 1) {
                allValuesSingletons = false;
            }
            valueSequences.add(valueSeq);
        }
        this.hasNext = false;
        if (allKeysString && allValuesSingletons) {
            return ItemFactory.getInstance()
                .createObjectItem(
                    mapKeys.stream().map(Item::getStringValue).collect(Collectors.toList()),
                    valueSequences.stream().map(values -> values.get(0)).collect(Collectors.toList()),
                    getMetadata(),
                    false
                );
        } else {
            return ItemFactory.getInstance()
                .createMapItem(mapKeys, valueSequences, getMetadata(), false);
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        return NativeClauseContext.NoNativeQuery;
    }
}
