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

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private List<RuntimeIterator> keys;
    private List<RuntimeIterator> values;
    private boolean isMergedObject = false;

    public ObjectConstructorRuntimeIterator(
            List<RuntimeIterator> keys,
            List<RuntimeIterator> values,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(keys, executionMode, iteratorMetadata);
        this.children.addAll(values);
        this.keys = keys;
        this.values = values;
    }

    public ObjectConstructorRuntimeIterator(
            List<RuntimeIterator> childExpressions,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.addAll(childExpressions);
        this.isMergedObject = true;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        List<Item> values = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        if (this.isMergedObject) {
            for (RuntimeIterator iterator : this.children) {
                iterator.open(dynamicContext);
                while (iterator.hasNext()) {
                    ObjectItem item = (ObjectItem) iterator.next();
                    keys.addAll(item.getKeys());
                    values.addAll(item.getValues());
                }
                iterator.close();
            }
            this.hasNext = false;
            return ItemFactory.getInstance()
                .createObjectItem(keys, values, getMetadata());

        } else {

            for (RuntimeIterator valueIterator : this.values) {
                List<Item> currentResults = new ArrayList<>();
                valueIterator.open(dynamicContext);
                while (valueIterator.hasNext()) {
                    currentResults.add(valueIterator.next());
                }
                valueIterator.close();
                // SIMILAR TO ZORBA, if value is more than one item, wrap it in an array
                if (currentResults.size() > 1) {
                    values.add(ItemFactory.getInstance().createArrayItem(currentResults));
                } else if (currentResults.size() == 1) {
                    values.add(currentResults.get(0));
                } else {
                    values.add(ItemFactory.getInstance().createNullItem());
                }
            }

            for (RuntimeIterator keyIterator : this.keys) {
                keyIterator.open(dynamicContext);
                if (!keyIterator.hasNext()) {
                    throw new IteratorFlowException("A key cannot be the empty sequence", getMetadata());
                }
                Item key = keyIterator.next();
                if (!key.isString()) {
                    throw new UnexpectedTypeException(
                            "Key provided for object creation must be of type String",
                            getMetadata()
                    );
                }
                keys.add(key.getStringValue());
                if (keyIterator.hasNext()) {
                    throw new IteratorFlowException(
                            "A key cannot be a sequence of more than one item",
                            getMetadata()
                    );
                }
                keyIterator.close();
            }
            this.hasNext = false;
            return ItemFactory.getInstance()
                .createObjectItem(keys, values, getMetadata());
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        // the keys need to be strings
        if (this.keys.stream().anyMatch(key -> !(key instanceof StringRuntimeIterator))) {
            return NativeClauseContext.NoNativeQuery;
        }
        List<NativeClauseContext> keyNativeContexts = this.keys.stream()
            .map(key -> key.generateNativeQuery(nativeClauseContext))
            .collect(Collectors.toList());
        List<NativeClauseContext> valueNativeContexts = this.values.stream()
            .map(value -> value.generateNativeQuery(nativeClauseContext))
            .collect(Collectors.toList());
        if (
            keyNativeContexts.stream().allMatch(key -> key != NativeClauseContext.NoNativeQuery)
                && valueNativeContexts.stream().allMatch(value -> value != NativeClauseContext.NoNativeQuery)
        ) {
            int subQueryCount = keyNativeContexts.size();
            if (subQueryCount == 0) {
                return NativeClauseContext.NoNativeQuery;
            }

            List<String> objectItems = new ArrayList<>();
            for (int i = 0; i < subQueryCount; i++) {
                objectItems.add(
                    String.format(
                        "%s, (%s)",
                        keyNativeContexts.get(i).getResultingQuery(),
                        valueNativeContexts.get(i).getResultingQuery()
                    )
                );
            }
            String resultString =
                String.format(
                    "named_struct(%s)",
                    String.join(",", objectItems)
                );

            ItemType resultType =
                ItemTypeFactory.createAnonymousObjectType(
                    keyNativeContexts
                        .stream()
                        .map(NativeClauseContext::getResultingQuery)
                        .map(key -> key.substring(1, key.length() - 1)) // because string wrapped in ""
                        .collect(Collectors.toList()),
                    valueNativeContexts
                        .stream()
                        .map(value -> value.getResultingType().getItemType())
                        .collect(Collectors.toList())
                );
            return new NativeClauseContext(
                    nativeClauseContext,
                    resultString,
                    new SequenceType(
                            resultType,
                            SequenceType.Arity.One
                    )
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }
}
