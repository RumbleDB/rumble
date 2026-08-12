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

import org.rumbledb.runtime.plan.ItemRuntimePlan;



import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:for-each}:
 * {@code array:for-each($array as array(*), $action as function(item()*) as item()*) as array(*)}.
 */
public class ArrayForEachFunctionIterator extends AbstractAtMostOneItemRuntimePlan {


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item arrayItem = null;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:for-each expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return null;
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:for-each must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> actionItems = this.functionIterator.materialize(context);
        if (actionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each must be a function item.",
                    getMetadata()
            );
        }
        if (actionItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each must be exactly one function item.",
                    getMetadata()
            );
        }

        Item action = actionItems.get(0);

        boolean allSingleton = true;
        List<List<Item>> resultMemberSequences = new ArrayList<>(memberSequences.size());
        for (List<Item> memberSequence : memberSequences) {
            List<Item> result = applyAction(action, memberSequence, context);
            if (allSingleton && result.size() != 1) {
                allSingleton = false;
            }
            resultMemberSequences.add(result);
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>(memberSequences.size());
            for (List<Item> member : resultMemberSequences) {
                items.add(member.get(0));
            }
            return ItemFactory.getInstance()
                .createArrayItem(items, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
        return ItemFactory.getInstance()
            .createSequenceArrayItem(resultMemberSequences, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan arrayIterator;
    private final ItemRuntimePlan functionIterator;

    public ArrayForEachFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:for-each must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.functionIterator = arguments.get(1);
    }


    private ItemRuntimePlan createSequenceIterator(List<Item> items) {
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

        List<ItemRuntimePlan> childIterators = new ArrayList<>(
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

    /**
     * Invokes {@code $action} with the array member as {@code item()*} (one argument, sequence type).
     */
    private List<Item> applyAction(
            Item action,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        ItemRuntimePlan memberIterator = createSequenceIterator(
            memberSequence
        );

        List<ItemRuntimePlan> arguments = new ArrayList<>(1);
        arguments.add(memberIterator);

        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        ItemRuntimePlan functionCall = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(action, functionItemContext),
                arguments,
                functionItemContext
        );
        return functionCall.materialize(context);
    }
}
