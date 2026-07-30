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

package org.rumbledb.runtime.functions.arrays;

import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;


import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:filter}:
 * {@code array:filter($array as array(*), $predicate as function(item()*) as xs:boolean) as array(*)}.
 */
public class ArrayFilterFunctionIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item>,
            AtMostOneLocalRuntimePlan<Item> {


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return computeResult(context);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> arrayIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> predicateIterator;

    public ArrayFilterFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:filter must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.predicateIterator = arguments.get(1);
    }

    private Item computeResult(DynamicContext context) {
        Item arrayItem = null;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:filter expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return null;
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:filter must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> predicateItems = this.predicateIterator.materialize(context);
        if (predicateItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:filter must be exactly one item.",
                    getMetadata()
            );
        }
        if (predicateItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:filter must be exactly one item.",
                    getMetadata()
            );
        }

        Item predicate = predicateItems.get(0);
        boolean allSingleton = true;
        List<List<Item>> kept = new ArrayList<>();
        for (List<Item> memberSequence : memberSequences) {
            if (predicateHoldsForCallableItem(predicate, memberSequence, context)) {
                if (allSingleton && memberSequence.size() != 1) {
                    allSingleton = false;
                }
                kept.add(memberSequence);
            }
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>();
            for (List<Item> member : kept) {
                items.add(member.get(0));
            }
            return ItemFactory.getInstance()
                .createArrayItem(items, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
        return ItemFactory.getInstance()
            .createSequenceArrayItem(kept, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    private boolean predicateHoldsForCallableItem(
            Item predicate,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> memberIterator = createSequenceIterator(
            memberSequence
        );
        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments = new ArrayList<>(1);
        arguments.add(memberIterator);
        org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> functionCall = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(predicate, functionItemContext),
                arguments,
                functionItemContext
        );
        List<Item> result = functionCall.materialize(context);
        return booleanValueFromFilterResult(result);
    }

    private boolean booleanValueFromFilterResult(List<Item> items) {
        if (items.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; array:filter predicate must return exactly one xs:boolean value.",
                    getMetadata()
            );
        }
        Item value = items.get(0);
        if (!value.isBoolean()) {
            throw new UnexpectedTypeException(
                    "Type error; array:filter predicate must return exactly one xs:boolean value.",
                    getMetadata()
            );
        }
        return value.getBooleanValue();
    }

    private org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            return new CommaExpressionIterator(
                    Collections.emptyList(),
                    staticContext
            );
        }

        List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> childIterators = new ArrayList<>(
                items.size()
        );
        for (Item item : items) {
            RuntimeStaticContext childStaticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            childIterators.add(
                new ConstantRuntimeIterator(
                        item,
                        childStaticContext
                )
            );
        }

        RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        return new CommaExpressionIterator(childIterators, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:filter is currently supported only in local execution mode."
        );
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:filter is currently supported only in local execution mode."
        );
    }
}
