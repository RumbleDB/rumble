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


import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayFoldLeftFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResult(context).iterator(), getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arrayIterator;
    private final RuntimePlan<Item> zeroIterator;
    private final RuntimePlan<Item> functionIterator;

    public ArrayFoldLeftFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:fold-left must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.zeroIterator = arguments.get(1);
        this.functionIterator = arguments.get(2);
    }

    private List<Item> computeResult(DynamicContext context) {
        Item arrayItem;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:fold-left expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return Collections.emptyList();
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:fold-left must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> accumulator = this.zeroIterator.materialize(context);

        List<Item> functionItems = this.functionIterator.materialize(context);
        if (functionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a function item.",
                    getMetadata()
            );
        }
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a single function item.",
                    getMetadata()
            );
        }

        FunctionItem functionItem = (FunctionItem) functionItems.get(0);

        for (List<Item> memberSequence : memberSequences) {
            accumulator = applyFunction(functionItem, accumulator, memberSequence, context);
        }

        return accumulator;
    }

    private RuntimePlan<Item> createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = RuntimeStaticContext.builder()
                .configuration(getConfiguration())
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(getMetadata())
                .build();
            return new CommaExpressionIterator(Collections.emptyList(), staticContext);
        }

        List<RuntimePlan<Item>> childIterators = new ArrayList<>(
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
            List<Item> accumulator,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        RuntimePlan<Item> accIterator = createSequenceIterator(accumulator);
        RuntimePlan<Item> memberIterator = createSequenceIterator(
            memberSequence
        );

        List<RuntimePlan<Item>> arguments = new ArrayList<>(2);
        arguments.add(accIterator);
        arguments.add(memberIterator);

        RuntimePlan<Item> functionCall = NamedFunctions
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
