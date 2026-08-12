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
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.types.SequenceType;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:for-each-pair}:
 * {@code array:for-each-pair($array1 as array(*), $array2 as array(*), $function as function(item()*, item()*) as item()*) as array(*)}.
 */
public class ArrayForEachPairFunctionIterator extends AbstractAtMostOneItemRuntimePlan {


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item arrayItem1;
        try {
            arrayItem1 = this.arrayIterator1.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:for-each-pair expects exactly one array as the first argument.",
                    getMetadata()
            );
        }
        if (arrayItem1 == null) {
            return null;
        }

        if (!arrayItem1.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:for-each-pair must be an array.",
                    getMetadata()
            );
        }

        Item arrayItem2;
        try {
            arrayItem2 = this.arrayIterator2.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:for-each-pair expects exactly one array as the second argument.",
                    getMetadata()
            );
        }
        if (arrayItem2 == null) {
            return null;
        }

        if (!arrayItem2.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each-pair must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> members1 = arrayItem1.getSequenceMembers();
        List<List<Item>> members2 = arrayItem2.getSequenceMembers();

        int n = Math.min(members1.size(), members2.size());

        List<Item> functionItems = this.functionIterator.materialize(context);
        if (functionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:for-each-pair must be a function item.",
                    getMetadata()
            );
        }
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:for-each-pair must be a single function item.",
                    getMetadata()
            );
        }

        FunctionItem functionItem = (FunctionItem) functionItems.get(0);

        boolean allSingleton = true;
        List<List<Item>> resultMemberSequences = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<Item> result = applyFunction(functionItem, members1.get(i), members2.get(i), context);
            if (allSingleton && result.size() != 1) {
                allSingleton = false;
            }
            resultMemberSequences.add(result);
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>(n);
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

    private final ItemRuntimePlan arrayIterator1;
    private final ItemRuntimePlan arrayIterator2;
    private final ItemRuntimePlan functionIterator;

    public ArrayForEachPairFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:for-each-pair must have exactly three arguments.");
        }
        this.arrayIterator1 = arguments.get(0);
        this.arrayIterator2 = arguments.get(1);
        this.functionIterator = arguments.get(2);
    }


    private ItemRuntimePlan createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            return new CommaExpressionIterator(Collections.emptyList(), staticContext);
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
            childIterators.add(new ConstantRuntimeIterator(item, childStaticContext));
        }

        RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        return new CommaExpressionIterator(childIterators, staticContext);
    }

    private List<Item> applyFunction(
            FunctionItem functionItem,
            List<Item> memberSequence1,
            List<Item> memberSequence2,
            DynamicContext context
    ) {
        ItemRuntimePlan firstArg = createSequenceIterator(memberSequence1);
        ItemRuntimePlan secondArg = createSequenceIterator(
            memberSequence2
        );

        List<ItemRuntimePlan> arguments = new ArrayList<>(2);
        arguments.add(firstArg);
        arguments.add(secondArg);

        ItemRuntimePlan functionCall = NamedFunctions
            .buildFunctionItemCallIterator(
                functionItem,
                this.staticContext,
                ExecutionMode.LOCAL,
                arguments,
                false
            );
        return functionCall.materialize(context);
    }
}
